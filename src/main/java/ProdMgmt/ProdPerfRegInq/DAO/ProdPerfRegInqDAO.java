package ProdMgmt.ProdPerfRegInq.DAO;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

import ProdMgmt.ProdPerfRegInq.DTO.ProdPerfRegInqDTO;

public class ProdPerfRegInqDAO {

    private Connection getConnection() throws Exception {
        Context ctx = new InitialContext();
        DataSource dataFactory = (DataSource) ctx.lookup("java:/comp/env/jdbc/oracle");
        return dataFactory.getConnection();
    }

    public int getTotalCount(String startDate, String endDate, String searchType, String keyword) {
        int count = 0;
        StringBuilder sql = new StringBuilder();
        List<Object> params = new ArrayList<Object>();

        sql.append("SELECT COUNT(*) ");
        sql.append("FROM PRODUCTION_RESULT pr ");
        sql.append("JOIN WORK_ORDER wo ON pr.WORK_ORDER_ID = wo.WORK_ORDER_ID ");
        sql.append("JOIN PRODUCTION_PLAN pp ON wo.PLAN_ID = pp.PLAN_ID ");
        sql.append("JOIN ITEM i ON wo.ITEM_ID = i.ITEM_ID ");
        sql.append("WHERE NVL(pr.USE_YN, 'Y') = 'Y' ");

        appendSearchCondition(sql, params, startDate, endDate, searchType, keyword);

        try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql.toString())) {
            bindParams(ps, params);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) count = rs.getInt(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return count;
    }

    public List<ProdPerfRegInqDTO> getListByPage(String startDate, String endDate, String searchType, String keyword,
            int startRow, int endRow) {
        List<ProdPerfRegInqDTO> list = new ArrayList<ProdPerfRegInqDTO>();
        StringBuilder sql = new StringBuilder();
        List<Object> params = new ArrayList<Object>();

        sql.append("SELECT * FROM ( ");
        sql.append("    SELECT ROW_NUMBER() OVER (ORDER BY q.RESULT_DATE DESC, q.RESULT_ID DESC) AS RN, q.* ");
        sql.append("    FROM ( ");
        sql.append("        SELECT pr.RESULT_ID, pr.WORK_ORDER_ID, wo.PLAN_ID, ");
        sql.append("               'WO-' || TO_CHAR(wo.WORK_DATE, 'YYYYMMDD') || '-' || LPAD(wo.WORK_ORDER_ID, 3, '0') AS WORK_ORDER_NO, ");
        sql.append("               wo.WORK_DATE, pr.RESULT_DATE, i.ITEM_CODE, i.ITEM_NAME, ");
        sql.append("               wo.WORK_QTY, pp.PLAN_QTY, ");
        sql.append("               NVL(sumInfo.SUM_PRODUCED_QTY, 0) AS CURRENT_PRODUCED_SUM, ");
        sql.append("               NVL(sumInfo.SUM_LOSS_QTY, 0) AS CURRENT_LOSS_SUM, ");
        sql.append("               (wo.WORK_QTY - NVL(sumInfo.SUM_TOTAL_QTY, 0)) AS REMAINING_QTY, ");
        sql.append("               pr.PRODUCED_QTY, pr.LOSS_QTY, i.UNIT, pp.LINE_CODE, pr.LOT_NO, pr.STATUS, pr.REMARK ");
        sql.append("        FROM PRODUCTION_RESULT pr ");
        sql.append("        JOIN WORK_ORDER wo ON pr.WORK_ORDER_ID = wo.WORK_ORDER_ID ");
        sql.append("        JOIN PRODUCTION_PLAN pp ON wo.PLAN_ID = pp.PLAN_ID ");
        sql.append("        JOIN ITEM i ON wo.ITEM_ID = i.ITEM_ID ");
        sql.append("        LEFT JOIN ( ");
        sql.append("            SELECT WORK_ORDER_ID, NVL(SUM(PRODUCED_QTY), 0) AS SUM_PRODUCED_QTY, ");
        sql.append("                   NVL(SUM(LOSS_QTY), 0) AS SUM_LOSS_QTY, ");
        sql.append("                   NVL(SUM(PRODUCED_QTY + LOSS_QTY), 0) AS SUM_TOTAL_QTY ");
        sql.append("              FROM PRODUCTION_RESULT ");
        sql.append("             WHERE NVL(USE_YN, 'Y') = 'Y' ");
        sql.append("             GROUP BY WORK_ORDER_ID ");
        sql.append("        ) sumInfo ON pr.WORK_ORDER_ID = sumInfo.WORK_ORDER_ID ");
        sql.append("        WHERE NVL(pr.USE_YN, 'Y') = 'Y' ");

        appendSearchCondition(sql, params, startDate, endDate, searchType, keyword);

        sql.append("    ) q ");
        sql.append(") WHERE RN BETWEEN ? AND ? ORDER BY RN");
        params.add(startRow);
        params.add(endRow);

        try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql.toString())) {
            bindParams(ps, params);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapResult(rs));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public List<ProdPerfRegInqDTO> getWorkOrderOptions() {
        List<ProdPerfRegInqDTO> list = new ArrayList<ProdPerfRegInqDTO>();
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT wo.WORK_ORDER_ID, wo.PLAN_ID, wo.WORK_DATE, wo.WORK_QTY, wo.STATUS AS WORK_STATUS, ");
        sql.append("       pp.PLAN_QTY, pp.LINE_CODE, i.ITEM_CODE, i.ITEM_NAME, i.UNIT, ");
        sql.append("       NVL(sumInfo.SUM_PRODUCED_QTY, 0) AS CURRENT_PRODUCED_SUM, ");
        sql.append("       NVL(sumInfo.SUM_LOSS_QTY, 0) AS CURRENT_LOSS_SUM, ");
        sql.append("       (wo.WORK_QTY - NVL(sumInfo.SUM_TOTAL_QTY, 0)) AS REMAINING_QTY, ");
        sql.append("       'WO-' || TO_CHAR(wo.WORK_DATE, 'YYYYMMDD') || '-' || LPAD(wo.WORK_ORDER_ID, 3, '0') AS WORK_ORDER_NO ");
        sql.append("FROM WORK_ORDER wo ");
        sql.append("JOIN PRODUCTION_PLAN pp ON wo.PLAN_ID = pp.PLAN_ID ");
        sql.append("JOIN ITEM i ON wo.ITEM_ID = i.ITEM_ID ");
        sql.append("LEFT JOIN ( ");
        sql.append("    SELECT WORK_ORDER_ID, ");
        sql.append("           NVL(SUM(PRODUCED_QTY), 0) AS SUM_PRODUCED_QTY, ");
        sql.append("           NVL(SUM(LOSS_QTY), 0) AS SUM_LOSS_QTY, ");
        sql.append("           NVL(SUM(PRODUCED_QTY + LOSS_QTY), 0) AS SUM_TOTAL_QTY ");
        sql.append("    FROM PRODUCTION_RESULT ");
        sql.append("    WHERE NVL(USE_YN, 'Y') = 'Y' ");
        sql.append("    GROUP BY WORK_ORDER_ID ");
        sql.append(") sumInfo ON wo.WORK_ORDER_ID = sumInfo.WORK_ORDER_ID ");
        sql.append("WHERE NVL(wo.USE_YN, 'Y') = 'Y' ");
        sql.append("  AND NVL(wo.STATUS, '대기') = '완료' ");
        sql.append("  AND (wo.WORK_QTY - NVL(sumInfo.SUM_TOTAL_QTY, 0)) > 0 ");
        sql.append("ORDER BY wo.WORK_ORDER_ID DESC");

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql.toString());
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                ProdPerfRegInqDTO dto = new ProdPerfRegInqDTO();
                dto.setWorkOrderId(rs.getInt("WORK_ORDER_ID"));
                dto.setPlanId(rs.getInt("PLAN_ID"));
                dto.setWorkOrderNo(rs.getString("WORK_ORDER_NO"));
                dto.setWorkDate(rs.getDate("WORK_DATE"));
                dto.setLineCode(rs.getString("LINE_CODE"));
                dto.setItemCode(rs.getString("ITEM_CODE"));
                dto.setItemName(rs.getString("ITEM_NAME"));
                dto.setUnit(rs.getString("UNIT"));
                dto.setPlanQty(rs.getInt("PLAN_QTY"));
                dto.setWorkQty(rs.getInt("WORK_QTY"));
                dto.setCurrentProducedSum(rs.getInt("CURRENT_PRODUCED_SUM"));
                dto.setCurrentLossSum(rs.getInt("CURRENT_LOSS_SUM"));
                dto.setRemainingQty(rs.getInt("REMAINING_QTY"));
                list.add(dto);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public ProdPerfRegInqDTO getDetail(int resultId) {
        ProdPerfRegInqDTO dto = null;
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT pr.RESULT_ID, pr.WORK_ORDER_ID, wo.PLAN_ID, wo.WORK_DATE, ");
        sql.append("       'WO-' || TO_CHAR(wo.WORK_DATE, 'YYYYMMDD') || '-' || LPAD(wo.WORK_ORDER_ID, 3, '0') AS WORK_ORDER_NO, ");
        sql.append("       pr.RESULT_DATE, i.ITEM_CODE, i.ITEM_NAME, wo.WORK_QTY, pp.PLAN_QTY, ");
        sql.append("       NVL(sumInfo.SUM_PRODUCED_QTY, 0) AS CURRENT_PRODUCED_SUM, ");
        sql.append("       NVL(sumInfo.SUM_LOSS_QTY, 0) AS CURRENT_LOSS_SUM, ");
        sql.append("       (wo.WORK_QTY - NVL(sumInfo.SUM_TOTAL_QTY, 0)) AS REMAINING_QTY, ");
        sql.append("       pr.PRODUCED_QTY, pr.LOSS_QTY, i.UNIT, pp.LINE_CODE, pr.LOT_NO, pr.STATUS, pr.REMARK ");
        sql.append("FROM PRODUCTION_RESULT pr ");
        sql.append("JOIN WORK_ORDER wo ON pr.WORK_ORDER_ID = wo.WORK_ORDER_ID ");
        sql.append("JOIN PRODUCTION_PLAN pp ON wo.PLAN_ID = pp.PLAN_ID ");
        sql.append("JOIN ITEM i ON wo.ITEM_ID = i.ITEM_ID ");
        sql.append("LEFT JOIN ( ");
        sql.append("    SELECT WORK_ORDER_ID, NVL(SUM(PRODUCED_QTY), 0) AS SUM_PRODUCED_QTY, ");
        sql.append("           NVL(SUM(LOSS_QTY), 0) AS SUM_LOSS_QTY, ");
        sql.append("           NVL(SUM(PRODUCED_QTY + LOSS_QTY), 0) AS SUM_TOTAL_QTY ");
        sql.append("      FROM PRODUCTION_RESULT ");
        sql.append("     WHERE NVL(USE_YN, 'Y') = 'Y' ");
        sql.append("     GROUP BY WORK_ORDER_ID ");
        sql.append(") sumInfo ON pr.WORK_ORDER_ID = sumInfo.WORK_ORDER_ID ");
        sql.append("WHERE pr.RESULT_ID = ? AND NVL(pr.USE_YN, 'Y') = 'Y'");
        try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql.toString())) {
            ps.setInt(1, resultId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    dto = mapResult(rs);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dto;
    }

    public int insertProductionResult(ProdPerfRegInqDTO dto) {
        int result = 0;
        String sql = "INSERT INTO PRODUCTION_RESULT (RESULT_ID, WORK_ORDER_ID, RESULT_DATE, LOT_NO, PRODUCED_QTY, LOSS_QTY, STATUS, USE_YN, REMARK, CREATED_AT, UPDATED_AT) "
                + "VALUES (SEQ_PRODUCTION_RESULT.NEXTVAL, ?, ?, ?, ?, ?, '완료', 'Y', ?, SYSDATE, SYSDATE)";
        try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, dto.getWorkOrderId());
            ps.setDate(2, dto.getResultDate());
            ps.setString(3, dto.getLotNo());
            ps.setInt(4, dto.getProducedQty());
            ps.setInt(5, dto.getLossQty());
            ps.setString(6, dto.getRemark());
            result = ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public int updateProductionResult(ProdPerfRegInqDTO dto) {
        int result = 0;
        String sql = "UPDATE PRODUCTION_RESULT SET RESULT_DATE = ?, LOT_NO = ?, PRODUCED_QTY = ?, LOSS_QTY = ?, STATUS = '완료', REMARK = ?, UPDATED_AT = SYSDATE "
                + "WHERE RESULT_ID = ? AND NVL(USE_YN, 'Y') = 'Y'";
        try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setDate(1, dto.getResultDate());
            ps.setString(2, dto.getLotNo());
            ps.setInt(3, dto.getProducedQty());
            ps.setInt(4, dto.getLossQty());
            ps.setString(5, dto.getRemark());
            ps.setInt(6, dto.getSeqNO());
            result = ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public int deleteByIds(String[] seqNos) {
        int result = 0;
        if (seqNos == null || seqNos.length == 0) return 0;
        StringBuilder sql = new StringBuilder();
        sql.append("UPDATE PRODUCTION_RESULT SET USE_YN = 'N', UPDATED_AT = SYSDATE WHERE RESULT_ID IN (");
        for (int i = 0; i < seqNos.length; i++) {
            sql.append("?");
            if (i < seqNos.length - 1) sql.append(", ");
        }
        sql.append(")");

        try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql.toString())) {
            for (int i = 0; i < seqNos.length; i++) ps.setInt(i + 1, Integer.parseInt(seqNos[i]));
            result = ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public int getWorkQtyByWorkOrderId(int workOrderId) {
        String sql = "SELECT WORK_QTY FROM WORK_ORDER WHERE WORK_ORDER_ID = ? AND NVL(USE_YN, 'Y') = 'Y'";
        try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, workOrderId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getInt(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    public int getProducedSumByWorkOrderId(int workOrderId) {
        String sql = "SELECT NVL(SUM(PRODUCED_QTY), 0) FROM PRODUCTION_RESULT WHERE WORK_ORDER_ID = ? AND NVL(USE_YN, 'Y') = 'Y'";
        try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, workOrderId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getInt(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    public int getLossSumByWorkOrderId(int workOrderId) {
        String sql = "SELECT NVL(SUM(LOSS_QTY), 0) FROM PRODUCTION_RESULT WHERE WORK_ORDER_ID = ? AND NVL(USE_YN, 'Y') = 'Y'";
        try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, workOrderId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getInt(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    public int countFinalInspectionsByResultId(int resultId) {
        String sql = "SELECT COUNT(*) FROM FINAL_INSPECTION WHERE RESULT_ID = ? AND NVL(USE_YN, 'Y') = 'Y'";
        try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, resultId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getInt(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    public int countFinalInspectionsByResultIds(String[] seqNos) {
        if (seqNos == null || seqNos.length == 0) return 0;
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT COUNT(*) FROM FINAL_INSPECTION WHERE NVL(USE_YN, 'Y') = 'Y' AND RESULT_ID IN (");
        for (int i = 0; i < seqNos.length; i++) {
            sql.append("?");
            if (i < seqNos.length - 1) sql.append(", ");
        }
        sql.append(")");
        try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql.toString())) {
            for (int i = 0; i < seqNos.length; i++) {
                ps.setInt(i + 1, Integer.parseInt(seqNos[i]));
            }
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getInt(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    public int getCurrentProducedQty(int resultId) {
        String sql = "SELECT PRODUCED_QTY FROM PRODUCTION_RESULT WHERE RESULT_ID = ? AND NVL(USE_YN, 'Y') = 'Y'";
        try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, resultId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getInt(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    public int getCurrentLossQty(int resultId) {
        String sql = "SELECT LOSS_QTY FROM PRODUCTION_RESULT WHERE RESULT_ID = ? AND NVL(USE_YN, 'Y') = 'Y'";
        try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, resultId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getInt(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    public int countLotNo(String lotNo, Integer excludeResultId) {
        StringBuilder sql = new StringBuilder("SELECT COUNT(*) FROM PRODUCTION_RESULT WHERE LOT_NO = ? AND NVL(USE_YN, 'Y') = 'Y'");
        if (excludeResultId != null) {
            sql.append(" AND RESULT_ID <> ?");
        }
        try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql.toString())) {
            ps.setString(1, lotNo);
            if (excludeResultId != null) {
                ps.setInt(2, excludeResultId.intValue());
            }
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getInt(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    private ProdPerfRegInqDTO mapResult(ResultSet rs) throws Exception {
        ProdPerfRegInqDTO dto = new ProdPerfRegInqDTO();
        dto.setSeqNO(rs.getInt("RESULT_ID"));
        dto.setWorkOrderId(rs.getInt("WORK_ORDER_ID"));
        dto.setPlanId(rs.getInt("PLAN_ID"));
        dto.setWorkOrderNo(rs.getString("WORK_ORDER_NO"));
        dto.setWorkDate(rs.getDate("WORK_DATE"));
        dto.setResultDate(rs.getDate("RESULT_DATE"));
        dto.setItemCode(rs.getString("ITEM_CODE"));
        dto.setItemName(rs.getString("ITEM_NAME"));
        dto.setWorkQty(rs.getInt("WORK_QTY"));
        dto.setPlanQty(rs.getInt("PLAN_QTY"));
        dto.setCurrentProducedSum(rs.getInt("CURRENT_PRODUCED_SUM"));
        dto.setCurrentLossSum(rs.getInt("CURRENT_LOSS_SUM"));
        dto.setRemainingQty(rs.getInt("REMAINING_QTY"));
        dto.setProducedQty(rs.getInt("PRODUCED_QTY"));
        dto.setLossQty(rs.getInt("LOSS_QTY"));
        dto.setUnit(rs.getString("UNIT"));
        dto.setLineCode(rs.getString("LINE_CODE"));
        dto.setLotNo(rs.getString("LOT_NO"));
        dto.setStatus(rs.getString("STATUS"));
        dto.setRemark(rs.getString("REMARK"));
        return dto;
    }

    private void appendSearchCondition(StringBuilder sql, List<Object> params, String startDate, String endDate, String searchType, String keyword) {
        if (startDate != null && !startDate.trim().equals("")) {
            sql.append(" AND pr.RESULT_DATE >= ? ");
            params.add(Date.valueOf(startDate));
        }
        if (endDate != null && !endDate.trim().equals("")) {
            sql.append(" AND pr.RESULT_DATE <= ? ");
            params.add(Date.valueOf(endDate));
        }
        if (keyword != null && !keyword.trim().equals("")) {
            String kw = "%" + keyword.trim() + "%";
            if (searchType == null || searchType.trim().equals("") || "all".equals(searchType)) {
                sql.append(" AND ('WO-' || TO_CHAR(wo.WORK_DATE, 'YYYYMMDD') || '-' || LPAD(wo.WORK_ORDER_ID, 3, '0') LIKE ? OR i.ITEM_CODE LIKE ? OR i.ITEM_NAME LIKE ? OR pp.LINE_CODE LIKE ? OR pr.LOT_NO LIKE ?) ");
                params.add(kw); params.add(kw); params.add(kw); params.add(kw); params.add(kw);
            } else if ("workOrderNo".equals(searchType)) {
                sql.append(" AND 'WO-' || TO_CHAR(wo.WORK_DATE, 'YYYYMMDD') || '-' || LPAD(wo.WORK_ORDER_ID, 3, '0') LIKE ? ");
                params.add(kw);
            } else if ("itemCode".equals(searchType)) {
                sql.append(" AND i.ITEM_CODE LIKE ? "); params.add(kw);
            } else if ("itemName".equals(searchType)) {
                sql.append(" AND i.ITEM_NAME LIKE ? "); params.add(kw);
            } else if ("lineCode".equals(searchType)) {
                sql.append(" AND pp.LINE_CODE LIKE ? "); params.add(kw);
            } else if ("lotNo".equals(searchType)) {
                sql.append(" AND pr.LOT_NO LIKE ? "); params.add(kw);
            }
        }
    }

    private void bindParams(PreparedStatement ps, List<Object> params) throws Exception {
        for (int i = 0; i < params.size(); i++) {
            Object param = params.get(i);
            if (param instanceof Date) ps.setDate(i + 1, (Date) param);
            else if (param instanceof Integer) ps.setInt(i + 1, ((Integer) param).intValue());
            else ps.setString(i + 1, String.valueOf(param));
        }
    }
}
