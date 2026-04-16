package WorkMgmt.WORegInq.DAO;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

import WorkMgmt.WORegInq.DTO.WORegInqDTO;

public class WORegInqDAO {

    private Connection getConnection() throws Exception {
        Context ctx = new InitialContext();
        DataSource dataFactory = (DataSource) ctx.lookup("java:/comp/env/jdbc/oracle");
        return dataFactory.getConnection();
    }

    public int getTotalCount(String startDate, String endDate, String searchType, String keyword) {
        int count = 0;

        StringBuilder sql = new StringBuilder();
        List<Object> params = new ArrayList<>();

        sql.append("SELECT COUNT(*) ");
        sql.append("FROM WORK_ORDER wo ");
        sql.append("JOIN PRODUCTION_PLAN pp ON wo.PLAN_ID = pp.PLAN_ID ");
        sql.append("JOIN ITEM i ON wo.ITEM_ID = i.ITEM_ID ");
        sql.append("LEFT JOIN EMP e ON wo.EMP_ID = e.EMP_ID ");
        sql.append("LEFT JOIN ( ");
        sql.append("    SELECT item_id, process_code ");
        sql.append("    FROM ( ");
        sql.append("        SELECT r.ITEM_ID, p.PROCESS_CODE, ");
        sql.append("               ROW_NUMBER() OVER (PARTITION BY r.ITEM_ID ORDER BY r.PROCESS_SEQ ASC, r.ROUTING_ID ASC) AS RN ");
        sql.append("        FROM ROUTING r ");
        sql.append("        JOIN PROCESS p ON r.PROCESS_ID = p.PROCESS_ID ");
        sql.append("    ) ");
        sql.append("    WHERE RN = 1 ");
        sql.append(") procInfo ON wo.ITEM_ID = procInfo.ITEM_ID ");
        sql.append("LEFT JOIN ( ");
        sql.append("    SELECT item_id, bom_code ");
        sql.append("    FROM ( ");
        sql.append("        SELECT b.ITEM_ID, ");
        sql.append("               'BOM-' || LPAD(b.BOM_ID, 3, '0') AS BOM_CODE, ");
        sql.append("               ROW_NUMBER() OVER ( ");
        sql.append("                   PARTITION BY b.ITEM_ID ");
        sql.append("                   ORDER BY CASE NVL(b.USE_YN, 'Y') WHEN 'Y' THEN 0 ELSE 1 END, ");
        sql.append("                            b.VERSION_NO DESC, b.BOM_ID DESC ");
        sql.append("               ) AS RN ");
        sql.append("        FROM BOM b ");
        sql.append("    ) ");
        sql.append("    WHERE RN = 1 ");
        sql.append(") bomInfo ON wo.ITEM_ID = bomInfo.ITEM_ID ");
        sql.append("WHERE NVL(wo.USE_YN, 'Y') = 'Y' ");

        appendSearchCondition(sql, params, startDate, endDate, searchType, keyword);

        try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql.toString())) {
            bindParams(ps, params);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    count = rs.getInt(1);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return count;
    }

    public List<WORegInqDTO> getListByPage(String startDate, String endDate, String searchType, String keyword, int startRow, int endRow) {
        List<WORegInqDTO> list = new ArrayList<>();
        StringBuilder sql = new StringBuilder();
        List<Object> params = new ArrayList<>();

        sql.append("SELECT * FROM ( ");
        sql.append("    SELECT ROW_NUMBER() OVER (ORDER BY q.WORK_ORDER_ID DESC) AS RN, q.* ");
        sql.append("    FROM ( ");
        sql.append("        SELECT wo.WORK_ORDER_ID, ");
        sql.append("               wo.ITEM_ID, wo.PLAN_ID, wo.EMP_ID, ");
        sql.append("               'WO-' || TO_CHAR(wo.WORK_DATE, 'YYYYMMDD') || '-' || LPAD(wo.WORK_ORDER_ID, 3, '0') AS WORK_ORDER_NO, ");
        sql.append("               wo.WORK_DATE, i.ITEM_CODE, i.ITEM_NAME, wo.WORK_QTY, i.UNIT, ");
        sql.append("               pp.LINE_CODE, procInfo.PROCESS_CODE, e.EMP_NAME, bomInfo.BOM_CODE, ");
        sql.append("               wo.STATUS, wo.REMARK ");
        sql.append("        FROM WORK_ORDER wo ");
        sql.append("        JOIN PRODUCTION_PLAN pp ON wo.PLAN_ID = pp.PLAN_ID ");
        sql.append("        JOIN ITEM i ON wo.ITEM_ID = i.ITEM_ID ");
        sql.append("        LEFT JOIN EMP e ON wo.EMP_ID = e.EMP_ID ");
        sql.append("        LEFT JOIN ( ");
        sql.append("            SELECT item_id, process_code ");
        sql.append("            FROM ( ");
        sql.append("                SELECT r.ITEM_ID, p.PROCESS_CODE, ");
        sql.append("                       ROW_NUMBER() OVER (PARTITION BY r.ITEM_ID ORDER BY r.PROCESS_SEQ ASC, r.ROUTING_ID ASC) AS RN ");
        sql.append("                FROM ROUTING r ");
        sql.append("                JOIN PROCESS p ON r.PROCESS_ID = p.PROCESS_ID ");
        sql.append("            ) WHERE RN = 1 ");
        sql.append("        ) procInfo ON wo.ITEM_ID = procInfo.ITEM_ID ");
        sql.append("        LEFT JOIN ( ");
        sql.append("            SELECT item_id, bom_code ");
        sql.append("            FROM ( ");
        sql.append("                SELECT b.ITEM_ID, 'BOM-' || LPAD(b.BOM_ID, 3, '0') AS BOM_CODE, ");
        sql.append("                       ROW_NUMBER() OVER (PARTITION BY b.ITEM_ID ORDER BY CASE NVL(b.USE_YN, 'Y') WHEN 'Y' THEN 0 ELSE 1 END, b.VERSION_NO DESC, b.BOM_ID DESC) AS RN ");
        sql.append("                FROM BOM b ");
        sql.append("            ) WHERE RN = 1 ");
        sql.append("        ) bomInfo ON wo.ITEM_ID = bomInfo.ITEM_ID ");
        sql.append("        WHERE NVL(wo.USE_YN, 'Y') = 'Y' ");

        appendSearchCondition(sql, params, startDate, endDate, searchType, keyword);

        sql.append("    ) q ");
        sql.append(") WHERE RN BETWEEN ? AND ?");

        params.add(startRow);
        params.add(endRow);

        try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql.toString())) {
            bindParams(ps, params);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    WORegInqDTO dto = new WORegInqDTO();
                    dto.setSeqNO(rs.getInt("WORK_ORDER_ID"));
                    dto.setItemId(rs.getInt("ITEM_ID"));
                    dto.setPlanId(rs.getInt("PLAN_ID"));
                    dto.setEmpId(rs.getInt("EMP_ID"));
                    dto.setWorkOrderNo(rs.getString("WORK_ORDER_NO"));
                    dto.setWorkDate(rs.getDate("WORK_DATE"));
                    dto.setItemCode(rs.getString("ITEM_CODE"));
                    dto.setItemName(rs.getString("ITEM_NAME"));
                    dto.setWorkQty(rs.getInt("WORK_QTY"));
                    dto.setUnit(rs.getString("UNIT"));
                    dto.setLineCode(rs.getString("LINE_CODE"));
                    dto.setProcessCode(rs.getString("PROCESS_CODE"));
                    dto.setEmpName(rs.getString("EMP_NAME"));
                    dto.setBomCode(rs.getString("BOM_CODE"));
                    dto.setStatus(rs.getString("STATUS"));
                    dto.setRemark(rs.getString("REMARK"));
                    list.add(dto);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    public WORegInqDTO getDetail(int workOrderId) {
        WORegInqDTO dto = null;
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT wo.WORK_ORDER_ID, wo.ITEM_ID, wo.PLAN_ID, wo.EMP_ID, ");
        sql.append("       'WO-' || TO_CHAR(wo.WORK_DATE, 'YYYYMMDD') || '-' || LPAD(wo.WORK_ORDER_ID, 3, '0') AS WORK_ORDER_NO, ");
        sql.append("       wo.WORK_DATE, i.ITEM_CODE, i.ITEM_NAME, wo.WORK_QTY, i.UNIT, ");
        sql.append("       pp.LINE_CODE, procInfo.PROCESS_CODE, e.EMP_NAME, bomInfo.BOM_CODE, ");
        sql.append("       wo.STATUS, wo.REMARK ");
        sql.append("FROM WORK_ORDER wo ");
        sql.append("JOIN PRODUCTION_PLAN pp ON wo.PLAN_ID = pp.PLAN_ID ");
        sql.append("JOIN ITEM i ON wo.ITEM_ID = i.ITEM_ID ");
        sql.append("LEFT JOIN EMP e ON wo.EMP_ID = e.EMP_ID ");
        sql.append("LEFT JOIN ( ");
        sql.append("    SELECT item_id, process_code ");
        sql.append("    FROM ( ");
        sql.append("        SELECT r.ITEM_ID, p.PROCESS_CODE, ");
        sql.append("               ROW_NUMBER() OVER (PARTITION BY r.ITEM_ID ORDER BY r.PROCESS_SEQ ASC, r.ROUTING_ID ASC) AS RN ");
        sql.append("        FROM ROUTING r ");
        sql.append("        JOIN PROCESS p ON r.PROCESS_ID = p.PROCESS_ID ");
        sql.append("    ) WHERE RN = 1 ");
        sql.append(") procInfo ON wo.ITEM_ID = procInfo.ITEM_ID ");
        sql.append("LEFT JOIN ( ");
        sql.append("    SELECT item_id, bom_code ");
        sql.append("    FROM ( ");
        sql.append("        SELECT b.ITEM_ID, 'BOM-' || LPAD(b.BOM_ID, 3, '0') AS BOM_CODE, ");
        sql.append("               ROW_NUMBER() OVER (PARTITION BY b.ITEM_ID ORDER BY CASE NVL(b.USE_YN, 'Y') WHEN 'Y' THEN 0 ELSE 1 END, b.VERSION_NO DESC, b.BOM_ID DESC) AS RN ");
        sql.append("        FROM BOM b ");
        sql.append("    ) WHERE RN = 1 ");
        sql.append(") bomInfo ON wo.ITEM_ID = bomInfo.ITEM_ID ");
        sql.append("WHERE wo.WORK_ORDER_ID = ? AND NVL(wo.USE_YN, 'Y') = 'Y'");

        try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql.toString())) {
            ps.setInt(1, workOrderId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    dto = new WORegInqDTO();
                    dto.setSeqNO(rs.getInt("WORK_ORDER_ID"));
                    dto.setItemId(rs.getInt("ITEM_ID"));
                    dto.setPlanId(rs.getInt("PLAN_ID"));
                    dto.setEmpId(rs.getInt("EMP_ID"));
                    dto.setWorkOrderNo(rs.getString("WORK_ORDER_NO"));
                    dto.setWorkDate(rs.getDate("WORK_DATE"));
                    dto.setItemCode(rs.getString("ITEM_CODE"));
                    dto.setItemName(rs.getString("ITEM_NAME"));
                    dto.setWorkQty(rs.getInt("WORK_QTY"));
                    dto.setUnit(rs.getString("UNIT"));
                    dto.setLineCode(rs.getString("LINE_CODE"));
                    dto.setProcessCode(rs.getString("PROCESS_CODE"));
                    dto.setEmpName(rs.getString("EMP_NAME"));
                    dto.setBomCode(rs.getString("BOM_CODE"));
                    dto.setStatus(rs.getString("STATUS"));
                    dto.setRemark(rs.getString("REMARK"));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return dto;
    }

    public List<WORegInqDTO> getPlanOptions() {
        List<WORegInqDTO> list = new ArrayList<>();
        String sql = "SELECT pp.PLAN_ID, pp.ITEM_ID, pp.PLAN_DATE, pp.LINE_CODE, pp.STATUS, i.ITEM_CODE, i.ITEM_NAME, i.UNIT "
                   + "FROM PRODUCTION_PLAN pp JOIN ITEM i ON pp.ITEM_ID = i.ITEM_ID "
                   + "WHERE NVL(pp.USE_YN, 'Y') = 'Y' ORDER BY pp.PLAN_ID DESC";
        try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                WORegInqDTO dto = new WORegInqDTO();
                dto.setPlanId(rs.getInt("PLAN_ID"));
                dto.setItemId(rs.getInt("ITEM_ID"));
                dto.setPlanDate(rs.getDate("PLAN_DATE"));
                dto.setLineCode(rs.getString("LINE_CODE"));
                dto.setPlanStatus(rs.getString("STATUS"));
                dto.setItemCode(rs.getString("ITEM_CODE"));
                dto.setItemName(rs.getString("ITEM_NAME"));
                dto.setUnit(rs.getString("UNIT"));
                list.add(dto);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public List<WORegInqDTO> getEmpOptions() {
        List<WORegInqDTO> list = new ArrayList<>();
        String sql = "SELECT EMP_ID, EMP_NO, EMP_NAME FROM EMP WHERE NVL(USE_YN, 'Y') = 'Y' ORDER BY EMP_ID ASC";
        try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                WORegInqDTO dto = new WORegInqDTO();
                dto.setEmpId(rs.getInt("EMP_ID"));
                dto.setEmpNo(rs.getString("EMP_NO"));
                dto.setEmpName(rs.getString("EMP_NAME"));
                list.add(dto);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public int getItemIdByPlanId(int planId) {
        int itemId = 0;
        String sql = "SELECT ITEM_ID FROM PRODUCTION_PLAN WHERE PLAN_ID = ? AND NVL(USE_YN, 'Y') = 'Y'";
        try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, planId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    itemId = rs.getInt(1);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return itemId;
    }

    public int insertWorkOrder(WORegInqDTO dto) {
        int result = 0;
        String sql = "INSERT INTO WORK_ORDER (WORK_ORDER_ID, ITEM_ID, PLAN_ID, EMP_ID, WORK_DATE, WORK_QTY, STATUS, USE_YN, REMARK, CREATED_AT, UPDATED_AT) "
                   + "VALUES (SEQ_WORK_ORDER.NEXTVAL, ?, ?, ?, ?, ?, ?, 'Y', ?, SYSDATE, SYSDATE)";
        try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, dto.getItemId());
            ps.setInt(2, dto.getPlanId());
            ps.setInt(3, dto.getEmpId());
            ps.setDate(4, dto.getWorkDate());
            ps.setInt(5, dto.getWorkQty());
            ps.setString(6, dto.getStatus());
            ps.setString(7, dto.getRemark());
            result = ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public int updateWorkOrder(WORegInqDTO dto) {
        int result = 0;
        String sql = "UPDATE WORK_ORDER SET ITEM_ID = ?, PLAN_ID = ?, EMP_ID = ?, WORK_DATE = ?, WORK_QTY = ?, STATUS = ?, REMARK = ?, UPDATED_AT = SYSDATE "
                   + "WHERE WORK_ORDER_ID = ? AND NVL(USE_YN, 'Y') = 'Y'";
        try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, dto.getItemId());
            ps.setInt(2, dto.getPlanId());
            ps.setInt(3, dto.getEmpId());
            ps.setDate(4, dto.getWorkDate());
            ps.setInt(5, dto.getWorkQty());
            ps.setString(6, dto.getStatus());
            ps.setString(7, dto.getRemark());
            ps.setInt(8, dto.getSeqNO());
            result = ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public int deleteByIds(String[] seqNos) {
        int result = 0;
        if (seqNos == null || seqNos.length == 0) {
            return 0;
        }
        StringBuilder sql = new StringBuilder();
        sql.append("UPDATE WORK_ORDER SET USE_YN = 'N', UPDATED_AT = SYSDATE WHERE WORK_ORDER_ID IN (");
        for (int i = 0; i < seqNos.length; i++) {
            sql.append("?");
            if (i < seqNos.length - 1) {
                sql.append(", ");
            }
        }
        sql.append(")");

        try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql.toString())) {
            for (int i = 0; i < seqNos.length; i++) {
                ps.setInt(i + 1, Integer.parseInt(seqNos[i]));
            }
            result = ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    private void appendSearchCondition(StringBuilder sql, List<Object> params, String startDate, String endDate, String searchType, String keyword) {
        if (startDate != null && !startDate.trim().equals("")) {
            sql.append(" AND wo.WORK_DATE >= ? ");
            params.add(Date.valueOf(startDate));
        }
        if (endDate != null && !endDate.trim().equals("")) {
            sql.append(" AND wo.WORK_DATE <= ? ");
            params.add(Date.valueOf(endDate));
        }
        if (keyword != null && !keyword.trim().equals("")) {
            String kw = "%" + keyword.trim() + "%";
            if (searchType == null || searchType.trim().equals("")) {
                sql.append(" AND ( 'WO-' || TO_CHAR(wo.WORK_DATE, 'YYYYMMDD') || '-' || LPAD(wo.WORK_ORDER_ID, 3, '0') LIKE ? OR i.ITEM_CODE LIKE ? OR i.ITEM_NAME LIKE ? OR pp.LINE_CODE LIKE ? OR procInfo.PROCESS_CODE LIKE ? OR e.EMP_NAME LIKE ? OR bomInfo.BOM_CODE LIKE ? ) ");
                params.add(kw); params.add(kw); params.add(kw); params.add(kw); params.add(kw); params.add(kw); params.add(kw);
            } else if ("workOrderNo".equals(searchType)) {
                sql.append(" AND 'WO-' || TO_CHAR(wo.WORK_DATE, 'YYYYMMDD') || '-' || LPAD(wo.WORK_ORDER_ID, 3, '0') LIKE ? ");
                params.add(kw);
            } else if ("itemCode".equals(searchType)) {
                sql.append(" AND i.ITEM_CODE LIKE ? ");
                params.add(kw);
            } else if ("itemName".equals(searchType)) {
                sql.append(" AND i.ITEM_NAME LIKE ? ");
                params.add(kw);
            } else if ("lineCode".equals(searchType)) {
                sql.append(" AND pp.LINE_CODE LIKE ? ");
                params.add(kw);
            } else if ("processCode".equals(searchType)) {
                sql.append(" AND procInfo.PROCESS_CODE LIKE ? ");
                params.add(kw);
            } else if ("empName".equals(searchType)) {
                sql.append(" AND e.EMP_NAME LIKE ? ");
                params.add(kw);
            } else if ("bomCode".equals(searchType)) {
                sql.append(" AND bomInfo.BOM_CODE LIKE ? ");
                params.add(kw);
            }
        }
    }

    private void bindParams(PreparedStatement ps, List<Object> params) throws Exception {
        for (int i = 0; i < params.size(); i++) {
            Object param = params.get(i);
            if (param instanceof Date) {
                ps.setDate(i + 1, (Date) param);
            } else if (param instanceof Integer) {
                ps.setInt(i + 1, (Integer) param);
            } else {
                ps.setString(i + 1, String.valueOf(param));
            }
        }
    }
}
