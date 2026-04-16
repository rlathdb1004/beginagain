package ProdMgmt.ProdPlanRegInq.DAO;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

import ProdMgmt.ProdPlanRegInq.DTO.ProdPlanRegInqDTO;

public class ProdPlanRegInqDAO {

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
        sql.append("FROM PRODUCTION_PLAN p ");
        sql.append("JOIN ITEM i ON p.ITEM_ID = i.ITEM_ID ");
        sql.append("WHERE NVL(p.USE_YN, 'Y') = 'Y' ");

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

    public List<ProdPlanRegInqDTO> getListByPage(String startDate, String endDate, String searchType, String keyword,
            int startRow, int endRow) {
        List<ProdPlanRegInqDTO> list = new ArrayList<>();
        StringBuilder sql = new StringBuilder();
        List<Object> params = new ArrayList<>();

        sql.append("SELECT * ");
        sql.append("FROM ( ");
        sql.append("    SELECT ROW_NUMBER() OVER (ORDER BY p.PLAN_DATE DESC, p.PLAN_ID DESC) AS RN, ");
        sql.append("           p.PLAN_ID AS PLAN_ID, ");
        sql.append("           'PP-' || TO_CHAR(p.PLAN_DATE, 'YYYYMMDD') || '-' || LPAD(p.PLAN_ID, 3, '0') AS PLAN_NO, ");
        sql.append("           p.PLAN_DATE AS PLAN_DATE, ");
        sql.append("           i.ITEM_CODE AS ITEM_CODE, ");
        sql.append("           i.ITEM_NAME AS ITEM_NAME, ");
        sql.append("           p.PLAN_QTY AS PLAN_QTY, ");
        sql.append("           i.UNIT AS UNIT, ");
        sql.append("           p.LINE_CODE AS LINE_CODE, ");
        sql.append("           p.STATUS AS STATUS, ");
        sql.append("           p.REMARK AS REMARK ");
        sql.append("    FROM PRODUCTION_PLAN p ");
        sql.append("    JOIN ITEM i ON p.ITEM_ID = i.ITEM_ID ");
        sql.append("    WHERE NVL(p.USE_YN, 'Y') = 'Y' ");
        appendSearchCondition(sql, params, startDate, endDate, searchType, keyword);
        sql.append(") ");
        sql.append("WHERE RN BETWEEN ? AND ? ");
        sql.append("ORDER BY RN ");
        params.add(startRow);
        params.add(endRow);

        try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql.toString())) {
            bindParams(ps, params);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapDto(rs));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public ProdPlanRegInqDTO getDetailById(int planId) {
        ProdPlanRegInqDTO dto = null;
        String sql = "SELECT p.PLAN_ID, 'PP-' || TO_CHAR(p.PLAN_DATE, 'YYYYMMDD') || '-' || LPAD(p.PLAN_ID, 3, '0') AS PLAN_NO, "
                + "p.PLAN_DATE, i.ITEM_CODE, i.ITEM_NAME, p.PLAN_QTY, i.UNIT, p.LINE_CODE, p.STATUS, p.REMARK "
                + "FROM PRODUCTION_PLAN p JOIN ITEM i ON p.ITEM_ID = i.ITEM_ID "
                + "WHERE NVL(p.USE_YN, 'Y') = 'Y' AND p.PLAN_ID = ?";
        try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, planId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) dto = mapDto(rs);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dto;
    }

    public List<ProdPlanRegInqDTO> getFinishedItemOptions() {
        List<ProdPlanRegInqDTO> list = new ArrayList<>();
        String sql = "SELECT ITEM_CODE, ITEM_NAME, UNIT FROM ITEM WHERE NVL(USE_YN, 'Y') = 'Y' AND ITEM_TYPE = '완제품' ORDER BY ITEM_CODE";
        try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                ProdPlanRegInqDTO dto = new ProdPlanRegInqDTO();
                dto.setPlanCode(rs.getString("ITEM_CODE"));
                dto.setPlanName(rs.getString("ITEM_NAME"));
                dto.setPlanUnit(rs.getString("UNIT"));
                list.add(dto);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public int insert(ProdPlanRegInqDTO dto) {
        int result = 0;
        String sql = "INSERT INTO PRODUCTION_PLAN (PLAN_ID, ITEM_ID, PLAN_DATE, PLAN_QTY, LINE_CODE, STATUS, USE_YN, REMARK, CREATED_AT, UPDATED_AT) "
                + "VALUES (SEQ_PRODUCTION_PLAN.NEXTVAL, (SELECT ITEM_ID FROM ITEM WHERE ITEM_CODE = ? AND NVL(USE_YN, 'Y') = 'Y'), ?, ?, ?, ?, 'Y', ?, SYSDATE, SYSDATE)";
        try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, dto.getPlanCode());
            ps.setDate(2, dto.getPlanDate());
            ps.setInt(3, dto.getPlanAmount());
            ps.setString(4, dto.getPlanLine());
            ps.setString(5, dto.getStatus());
            ps.setString(6, dto.getMemo());
            result = ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }


    public int update(ProdPlanRegInqDTO dto) {
        int result = 0;
        String sql = "UPDATE PRODUCTION_PLAN SET ITEM_ID = (SELECT ITEM_ID FROM ITEM WHERE ITEM_CODE = ? AND NVL(USE_YN, 'Y') = 'Y'), PLAN_DATE = ?, PLAN_QTY = ?, LINE_CODE = ?, STATUS = ?, REMARK = ?, UPDATED_AT = SYSDATE WHERE PLAN_ID = ?";
        try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, dto.getPlanCode());
            ps.setDate(2, dto.getPlanDate());
            ps.setInt(3, dto.getPlanAmount());
            ps.setString(4, dto.getPlanLine());
            ps.setString(5, dto.getStatus());
            ps.setString(6, dto.getMemo());
            ps.setInt(7, dto.getSeqNO());
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
        sql.append("UPDATE PRODUCTION_PLAN SET USE_YN = 'N', UPDATED_AT = SYSDATE WHERE PLAN_ID IN (");
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

    private void appendSearchCondition(StringBuilder sql, List<Object> params, String startDate, String endDate,
            String searchType, String keyword) {
        if (startDate != null && !startDate.trim().equals("")) {
            sql.append(" AND p.PLAN_DATE >= ? ");
            params.add(Date.valueOf(startDate));
        }
        if (endDate != null && !endDate.trim().equals("")) {
            sql.append(" AND p.PLAN_DATE <= ? ");
            params.add(Date.valueOf(endDate));
        }
        if (keyword != null && !keyword.trim().equals("")) {
            String kw = "%" + keyword.trim() + "%";
            if (searchType == null || searchType.trim().equals("")) {
                sql.append(" AND ( 'PP-' || TO_CHAR(p.PLAN_DATE, 'YYYYMMDD') || '-' || LPAD(p.PLAN_ID, 3, '0') LIKE ? OR i.ITEM_CODE LIKE ? OR i.ITEM_NAME LIKE ? OR p.LINE_CODE LIKE ? ) ");
                params.add(kw); params.add(kw); params.add(kw); params.add(kw);
            } else if ("planNo".equals(searchType)) {
                sql.append(" AND 'PP-' || TO_CHAR(p.PLAN_DATE, 'YYYYMMDD') || '-' || LPAD(p.PLAN_ID, 3, '0') LIKE ? ");
                params.add(kw);
            } else if ("planCode".equals(searchType)) {
                sql.append(" AND i.ITEM_CODE LIKE ? ");
                params.add(kw);
            } else if ("planName".equals(searchType)) {
                sql.append(" AND i.ITEM_NAME LIKE ? ");
                params.add(kw);
            } else if ("planLine".equals(searchType)) {
                sql.append(" AND p.LINE_CODE LIKE ? ");
                params.add(kw);
            }
        }
    }

    private void bindParams(PreparedStatement ps, List<Object> params) throws Exception {
        for (int i = 0; i < params.size(); i++) {
            Object param = params.get(i);
            if (param instanceof Date) ps.setDate(i + 1, (Date) param);
            else if (param instanceof Integer) ps.setInt(i + 1, (Integer) param);
            else ps.setString(i + 1, String.valueOf(param));
        }
    }

    private ProdPlanRegInqDTO mapDto(ResultSet rs) throws Exception {
        ProdPlanRegInqDTO dto = new ProdPlanRegInqDTO();
        dto.setSeqNO(rs.getInt("PLAN_ID"));
        dto.setPlanNo(rs.getString("PLAN_NO"));
        dto.setPlanDate(rs.getDate("PLAN_DATE"));
        dto.setPlanCode(rs.getString("ITEM_CODE"));
        dto.setPlanName(rs.getString("ITEM_NAME"));
        dto.setPlanAmount(rs.getInt("PLAN_QTY"));
        dto.setPlanUnit(rs.getString("UNIT"));
        dto.setPlanLine(rs.getString("LINE_CODE"));
        dto.setStatus(rs.getString("STATUS"));
        dto.setMemo(rs.getString("REMARK"));
        return dto;
    }
}
