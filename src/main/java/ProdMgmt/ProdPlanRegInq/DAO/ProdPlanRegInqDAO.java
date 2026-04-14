package ProdMgmt.ProdPlanRegInq.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;
import ProdMgmt.ProdPlanRegInq.DTO.ProdPlanRegInqDTO;

public class ProdPlanRegInqDAO {

    private DataSource getDataSource() throws Exception {
        Context ctx = new InitialContext();
        return (DataSource) ctx.lookup("java:/comp/env/jdbc/oracle");
    }

    public List<ProdPlanRegInqDTO> selectAll() {
        List<ProdPlanRegInqDTO> list = new ArrayList<ProdPlanRegInqDTO>();
        String query = "select p.PLAN_ID, "
                + "'PP-' || TO_CHAR(p.PLAN_DATE, 'YYYYMMDD') || '-' || LPAD(p.PLAN_ID, 3, '0') AS PLAN_NO, "
                + "TO_CHAR(p.PLAN_DATE, 'YYYY-MM-DD') AS PLAN_DATE, "
                + "i.ITEM_CODE, i.ITEM_NAME, p.PLAN_QTY, i.UNIT, p.LINE_CODE, p.REMARK "
                + "from PRODUCTION_PLAN p join ITEM i on p.ITEM_ID = i.ITEM_ID "
                + "order by p.plan_date desc, p.plan_id desc";
        try (Connection conn = getDataSource().getConnection();
             PreparedStatement ps = conn.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(mapPlan(rs));
            }
        } catch (Exception e) {
            throw new RuntimeException("생산계획 목록 조회 실패", e);
        }
        return list;
    }

    public ProdPlanRegInqDTO selectOne(int planId) {
        String sql = "select p.PLAN_ID, "
                + "'PP-' || TO_CHAR(p.PLAN_DATE, 'YYYYMMDD') || '-' || LPAD(p.PLAN_ID, 3, '0') AS PLAN_NO, "
                + "TO_CHAR(p.PLAN_DATE, 'YYYY-MM-DD') AS PLAN_DATE, "
                + "i.ITEM_CODE, i.ITEM_NAME, p.PLAN_QTY, i.UNIT, p.LINE_CODE, p.REMARK "
                + "from PRODUCTION_PLAN p join ITEM i on p.ITEM_ID = i.ITEM_ID "
                + "where p.PLAN_ID = ?";
        try (Connection conn = getDataSource().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, planId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapPlan(rs);
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("생산계획 상세 조회 실패", e);
        }
        return null;
    }

    public List<ProdPlanRegInqDTO> selectItemOptions() {
        List<ProdPlanRegInqDTO> list = new ArrayList<ProdPlanRegInqDTO>();
        String sql = "SELECT ITEM_CODE, ITEM_NAME, UNIT FROM ITEM WHERE USE_YN = 'Y' ORDER BY ITEM_NAME ASC, ITEM_ID ASC";
        try (Connection conn = getDataSource().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                ProdPlanRegInqDTO dto = new ProdPlanRegInqDTO();
                dto.setPlanCode(rs.getString("ITEM_CODE"));
                dto.setPlanName(rs.getString("ITEM_NAME"));
                dto.setPlanUnit(rs.getString("UNIT"));
                list.add(dto);
            }
        } catch (Exception e) {
            throw new RuntimeException("품목 옵션 조회 실패", e);
        }
        return list;
    }

    public int insert(ProdPlanRegInqDTO dto) {
        String sql = "INSERT INTO PRODUCTION_PLAN (PLAN_ID, ITEM_ID, PLAN_DATE, PLAN_QTY, LINE_CODE, REMARK) "
                + "VALUES ((SELECT NVL(MAX(PLAN_ID),0)+1 FROM PRODUCTION_PLAN), "
                + "(SELECT ITEM_ID FROM ITEM WHERE ITEM_CODE = ?), ?, ?, ?, ?)";
        try (Connection conn = getDataSource().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, dto.getPlanCode());
            ps.setDate(2, dto.getPlanDate());
            ps.setInt(3, dto.getPlanAmount());
            ps.setString(4, dto.getPlanLine());
            ps.setString(5, dto.getMemo());
            return ps.executeUpdate();
        } catch (Exception e) {
            throw new RuntimeException("생산계획 등록 실패", e);
        }
    }

    public int update(ProdPlanRegInqDTO dto) {
        String sql = "UPDATE PRODUCTION_PLAN SET ITEM_ID = (SELECT ITEM_ID FROM ITEM WHERE ITEM_CODE = ?), "
                + "PLAN_DATE = ?, PLAN_QTY = ?, LINE_CODE = ?, REMARK = ? WHERE PLAN_ID = ?";
        try (Connection conn = getDataSource().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, dto.getPlanCode());
            ps.setDate(2, dto.getPlanDate());
            ps.setInt(3, dto.getPlanAmount());
            ps.setString(4, dto.getPlanLine());
            ps.setString(5, dto.getMemo());
            ps.setInt(6, dto.getSeqNO());
            return ps.executeUpdate();
        } catch (Exception e) {
            throw new RuntimeException("생산계획 수정 실패", e);
        }
    }

    public int delete(int[] ids) {
        if (ids == null || ids.length == 0)
            return 0;
        StringBuilder sql = new StringBuilder("DELETE FROM PRODUCTION_PLAN WHERE PLAN_ID IN (");
        for (int i = 0; i < ids.length; i++) {
            if (i > 0)
                sql.append(',');
            sql.append('?');
        }
        sql.append(')');
        try (Connection conn = getDataSource().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql.toString())) {
            for (int i = 0; i < ids.length; i++) {
                ps.setInt(i + 1, ids[i]);
            }
            return ps.executeUpdate();
        } catch (Exception e) {
            throw new RuntimeException("생산계획 삭제 실패", e);
        }
    }

    private ProdPlanRegInqDTO mapPlan(ResultSet rs) throws SQLException {
        ProdPlanRegInqDTO dto = new ProdPlanRegInqDTO();
        dto.setSeqNO(rs.getInt("PLAN_ID"));
        dto.setPlanNo(rs.getString("PLAN_NO"));
        dto.setPlanDate(rs.getDate("PLAN_DATE"));
        dto.setPlanCode(rs.getString("ITEM_CODE"));
        dto.setPlanName(rs.getString("ITEM_NAME"));
        dto.setPlanAmount(rs.getInt("PLAN_QTY"));
        dto.setPlanUnit(rs.getString("UNIT"));
        dto.setPlanLine(rs.getString("LINE_CODE"));
        dto.setMemo(rs.getString("REMARK"));
        return dto;
    }
}
