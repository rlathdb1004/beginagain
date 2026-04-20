package QualityMgmt.FPInspRegInq.DAO;

import java.sql.SQLException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

import QualityMgmt.FPInspRegInq.DTO.FPInspRegInqDTO;

public class FPInspRegInqDAO {

    private Connection getConnection() throws Exception {
        Context ctx = new InitialContext();
        DataSource dataFactory = (DataSource) ctx.lookup("java:/comp/env/jdbc/oracle");
        return dataFactory.getConnection();
    }

    public List<FPInspRegInqDTO> selectFPInspRegInqList(FPInspRegInqDTO searchDTO) {
        List<FPInspRegInqDTO> list = new ArrayList<>();
        Connection conn = null; PreparedStatement ps = null; ResultSet rs = null;
        try {
            conn = getConnection();
            StringBuilder query = new StringBuilder();
            query.append(" SELECT fi.FINAL_INSPECTION_ID, fi.RESULT_ID, fi.EMP_ID, pr.WORK_ORDER_ID, pp.ITEM_ID, i.ITEM_CODE, i.ITEM_NAME, pr.LOT_NO, ");
            query.append("        NVL(pr.PRODUCED_QTY,0) AS PRODUCED_QTY, fi.INSPECT_QTY, fi.STATUS AS RESULT, fi.INSPECTION_DATE, fi.REMARK, fi.CREATED_AT, fi.UPDATED_AT ");
            query.append("   FROM FINAL_INSPECTION fi ");
            query.append("   JOIN PRODUCTION_RESULT pr ON fi.RESULT_ID = pr.RESULT_ID ");
            query.append("   JOIN WORK_ORDER wo ON pr.WORK_ORDER_ID = wo.WORK_ORDER_ID ");
            query.append("   JOIN PRODUCTION_PLAN pp ON wo.PLAN_ID = pp.PLAN_ID ");
            query.append("   JOIN ITEM i ON pp.ITEM_ID = i.ITEM_ID ");
            query.append("  WHERE fi.USE_YN = 'Y' ");
            List<Object> params = new ArrayList<>();
            if (searchDTO.getResultType() != null && !"".equals(searchDTO.getResultType().trim()) && !"전체".equals(searchDTO.getResultType())) {
                query.append(" AND fi.STATUS = ? "); params.add(searchDTO.getResultType());
            }
            if (searchDTO.getStartDate() != null) { query.append(" AND TRUNC(fi.INSPECTION_DATE) >= ? "); params.add(searchDTO.getStartDate()); }
            if (searchDTO.getEndDate() != null) { query.append(" AND TRUNC(fi.INSPECTION_DATE) <= ? "); params.add(searchDTO.getEndDate()); }
            String searchType = searchDTO.getSearchType();
            String keyword = searchDTO.getKeyword();
            if (keyword != null && !"".equals(keyword.trim())) {
                String like = "%" + keyword.trim() + "%";
                if ("itemCode".equals(searchType)) { query.append(" AND i.ITEM_CODE LIKE ? "); params.add(like); }
                else if ("itemName".equals(searchType)) { query.append(" AND i.ITEM_NAME LIKE ? "); params.add(like); }
                else { query.append(" AND (i.ITEM_CODE LIKE ? OR i.ITEM_NAME LIKE ?) "); params.add(like); params.add(like); }
            }
            query.append(" ORDER BY fi.FINAL_INSPECTION_ID DESC ");
            ps = conn.prepareStatement(query.toString());
            for (int i = 0; i < params.size(); i++) ps.setObject(i + 1, params.get(i));
            rs = ps.executeQuery();
            while (rs.next()) {
                FPInspRegInqDTO dto = new FPInspRegInqDTO();
                mapBase(rs, dto);
                list.add(dto);
            }
        } catch (Exception e) { e.printStackTrace(); }
        finally { close(rs, ps, conn); }
        return list;
    }

    public FPInspRegInqDTO selectFPInspRegInqOne(int finalInspectionId) {
        FPInspRegInqDTO dto = null;
        Connection conn = null; PreparedStatement ps = null; ResultSet rs = null;
        try {
            conn = getConnection();
            String sql = "SELECT fi.FINAL_INSPECTION_ID, fi.RESULT_ID, fi.EMP_ID, pr.WORK_ORDER_ID, pp.ITEM_ID, i.ITEM_CODE, i.ITEM_NAME, pr.LOT_NO, NVL(pr.PRODUCED_QTY,0) AS PRODUCED_QTY, fi.INSPECT_QTY, fi.STATUS AS RESULT, fi.INSPECTION_DATE, fi.REMARK, fi.CREATED_AT, fi.UPDATED_AT FROM FINAL_INSPECTION fi JOIN PRODUCTION_RESULT pr ON fi.RESULT_ID = pr.RESULT_ID JOIN WORK_ORDER wo ON pr.WORK_ORDER_ID = wo.WORK_ORDER_ID JOIN PRODUCTION_PLAN pp ON wo.PLAN_ID = pp.PLAN_ID JOIN ITEM i ON pp.ITEM_ID = i.ITEM_ID WHERE fi.FINAL_INSPECTION_ID = ? AND fi.USE_YN = 'Y'";
            ps = conn.prepareStatement(sql);
            ps.setInt(1, finalInspectionId);
            rs = ps.executeQuery();
            if (rs.next()) {
                dto = new FPInspRegInqDTO();
                mapBase(rs, dto);
                dto.setCurrentInspectSum(sumInspectQtyByResultId(dto.getResultId()));
                dto.setRemainingQty(Math.max(0, dto.getProducedQty() - (int) Math.round(dto.getCurrentInspectSum() - dto.getInspectQty())));
                dto.setDefectCount(countDefectProductByFinalInspectionId(finalInspectionId));
            }
        } catch (Exception e) { e.printStackTrace(); }
        finally { close(rs, ps, conn); }
        return dto;
    }

    public List<FPInspRegInqDTO> selectAvailableProductionResultList() {
        List<FPInspRegInqDTO> list = new ArrayList<>();
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            conn = getConnection();

            String sql = ""
                    + "SELECT pr.RESULT_ID, "
                    + "       pr.WORK_ORDER_ID, "
                    + "       pp.ITEM_ID, "
                    + "       i.ITEM_CODE, "
                    + "       i.ITEM_NAME, "
                    + "       pr.LOT_NO, "
                    + "       NVL(pr.PRODUCED_QTY, 0) AS PRODUCED_QTY, "
                    + "       NVL(fi_sum.SUM_INSPECT_QTY, 0) AS CURRENT_INSPECT_SUM, "
                    + "       (NVL(pr.PRODUCED_QTY, 0) - NVL(fi_sum.SUM_INSPECT_QTY, 0)) AS REMAINING_QTY "
                    + "FROM PRODUCTION_RESULT pr "
                    + "JOIN WORK_ORDER wo ON pr.WORK_ORDER_ID = wo.WORK_ORDER_ID "
                    + "JOIN PRODUCTION_PLAN pp ON wo.PLAN_ID = pp.PLAN_ID "
                    + "JOIN ITEM i ON pp.ITEM_ID = i.ITEM_ID "
                    + "LEFT JOIN ( "
                    + "    SELECT RESULT_ID, NVL(SUM(INSPECT_QTY), 0) AS SUM_INSPECT_QTY "
                    + "    FROM FINAL_INSPECTION "
                    + "    WHERE NVL(USE_YN, 'Y') = 'Y' "
                    + "    GROUP BY RESULT_ID "
                    + ") fi_sum ON pr.RESULT_ID = fi_sum.RESULT_ID "
                    + "WHERE NVL(pr.USE_YN, 'Y') = 'Y' "
                    + "  AND (NVL(pr.PRODUCED_QTY, 0) - NVL(fi_sum.SUM_INSPECT_QTY, 0)) > 0 "
                    + "ORDER BY pr.RESULT_ID DESC";

            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();

            while (rs.next()) {
                FPInspRegInqDTO dto = new FPInspRegInqDTO();
                dto.setResultId(rs.getInt("RESULT_ID"));
                dto.setWorkOrderId(rs.getInt("WORK_ORDER_ID"));
                dto.setItemId(rs.getInt("ITEM_ID"));
                dto.setItemCode(rs.getString("ITEM_CODE"));
                dto.setItemName(rs.getString("ITEM_NAME"));
                dto.setLotNo(rs.getString("LOT_NO"));
                dto.setProducedQty(rs.getInt("PRODUCED_QTY"));
                dto.setCurrentInspectSum(rs.getInt("CURRENT_INSPECT_SUM"));
                dto.setRemainingQty(rs.getInt("REMAINING_QTY"));
                list.add(dto);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            close(rs, ps, conn);
        }

        return list;
    }

    public boolean existsActiveProductionResult(int resultId) {
        return countBySql("SELECT COUNT(*) FROM PRODUCTION_RESULT WHERE RESULT_ID = ? AND NVL(USE_YN,'Y')='Y'", resultId) > 0;
    }

    public int sumInspectQtyByResultId(int resultId) {
        return countBySql("SELECT NVL(SUM(INSPECT_QTY),0) FROM FINAL_INSPECTION WHERE RESULT_ID = ? AND NVL(USE_YN,'Y')='Y'", resultId);
    }

    public int getCurrentInspectQty(int finalInspectionId) {
        return countBySql("SELECT NVL(INSPECT_QTY,0) FROM FINAL_INSPECTION WHERE FINAL_INSPECTION_ID = ?", finalInspectionId);
    }

    public int getProducedQtyByResultId(int resultId) {
        return countBySql("SELECT NVL(PRODUCED_QTY,0) FROM PRODUCTION_RESULT WHERE RESULT_ID = ? AND NVL(USE_YN,'Y')='Y'", resultId);
    }

    public int countDefectProductByFinalInspectionId(int finalInspectionId) {
        return countBySql("SELECT COUNT(*) FROM DEFECT_PRODUCT WHERE FINAL_INSPECTION_ID = ? AND NVL(USE_YN,'Y')='Y'", finalInspectionId);
    }

    public int getResultIdByFinalInspectionId(int finalInspectionId) {
        return countBySql("SELECT RESULT_ID FROM FINAL_INSPECTION WHERE FINAL_INSPECTION_ID = ? AND NVL(USE_YN,'Y')='Y'", finalInspectionId);
    }

    public int insertFPInspRegInq(FPInspRegInqDTO dto) {
        Connection conn = null; PreparedStatement ps = null;
        try {
            conn = getConnection();
            conn.setAutoCommit(false);
            String sql = "INSERT INTO FINAL_INSPECTION (FINAL_INSPECTION_ID, RESULT_ID, EMP_ID, INSPECT_QTY, STATUS, INSPECTION_DATE, REMARK, USE_YN, CREATED_AT, UPDATED_AT) VALUES ((SELECT NVL(MAX(FINAL_INSPECTION_ID),0)+1 FROM FINAL_INSPECTION), ?, ?, ?, ?, ?, ?, 'Y', SYSDATE, SYSDATE)";
            ps = conn.prepareStatement(sql);
            ps.setInt(1, dto.getResultId());
            ps.setInt(2, dto.getEmpId());
            ps.setDouble(3, dto.getInspectQty());
            ps.setString(4, dto.getResult());
            ps.setDate(5, dto.getInspectionDate());
            ps.setString(6, dto.getRemark());
            int result = ps.executeUpdate();
            close(null, ps, null); ps = null;

            if (result > 0 && "합격".equals(dto.getResult())) {
                int itemId = getItemIdByResultId(conn, dto.getResultId());
                applyInventoryDelta(conn, itemId, dto.getInspectQty());
            }

            conn.commit();
            return result;
        } catch (Exception e) {
            rollback(conn);
            throw new RuntimeException("완제품 검사 등록 실패", e);
        } finally {
            resetAutoCommit(conn);
            close(null, ps, conn);
        }
    }

    public int deleteFPInspRegInq(int[] ids) {
        Connection conn = null; PreparedStatement ps = null; int result = 0;
        try {
            if (ids == null || ids.length == 0) return 0;
            conn = getConnection();
            conn.setAutoCommit(false);

            for (int id : ids) {
                FPInspRegInqDTO origin = selectFPInspRegInqCore(conn, id);
                if (origin != null && "합격".equals(origin.getResult())) {
                    int itemId = getItemIdByResultId(conn, origin.getResultId());
                    applyInventoryDelta(conn, itemId, -origin.getInspectQty());
                }
            }

            StringBuilder sql = new StringBuilder("UPDATE FINAL_INSPECTION SET USE_YN='N', UPDATED_AT=SYSDATE WHERE FINAL_INSPECTION_ID IN (");
            for (int i = 0; i < ids.length; i++) { if (i > 0) sql.append(","); sql.append("?"); }
            sql.append(")");
            ps = conn.prepareStatement(sql.toString());
            for (int i = 0; i < ids.length; i++) ps.setInt(i + 1, ids[i]);
            result = ps.executeUpdate();
            conn.commit();
        } catch (Exception e) {
            rollback(conn);
            throw new RuntimeException("완제품 검사 삭제 실패", e);
        } finally {
            resetAutoCommit(conn);
            close(null, ps, conn);
        }
        return result;
    }

    public int updateFPInspRegInq(FPInspRegInqDTO dto) {
        Connection conn = null; PreparedStatement ps = null;
        try {
            conn = getConnection();
            conn.setAutoCommit(false);
            FPInspRegInqDTO origin = selectFPInspRegInqCore(conn, dto.getFinalInspectionId());
            if (origin == null) throw new RuntimeException("존재하지 않는 완제품 검사입니다.");

            String sql = "UPDATE FINAL_INSPECTION SET INSPECT_QTY = ?, STATUS = ?, INSPECTION_DATE = ?, REMARK = ?, UPDATED_AT = SYSDATE WHERE FINAL_INSPECTION_ID = ?";
            ps = conn.prepareStatement(sql);
            ps.setDouble(1, dto.getInspectQty());
            ps.setString(2, dto.getResult());
            ps.setDate(3, dto.getInspectionDate());
            ps.setString(4, dto.getRemark());
            ps.setInt(5, dto.getFinalInspectionId());
            int result = ps.executeUpdate();
            close(null, ps, null); ps = null;

            if (result > 0) {
                int itemId = getItemIdByResultId(conn, origin.getResultId());
                double delta = 0;
                if ("합격".equals(origin.getResult())) delta -= origin.getInspectQty();
                if ("합격".equals(dto.getResult())) delta += dto.getInspectQty();
                if (delta != 0) applyInventoryDelta(conn, itemId, delta);
            }

            conn.commit();
            return result;
        } catch (Exception e) {
            rollback(conn);
            throw new RuntimeException("완제품 검사 수정 실패", e);
        } finally {
            resetAutoCommit(conn);
            close(null, ps, conn);
        }
    }


    private FPInspRegInqDTO selectFPInspRegInqCore(Connection conn, int finalInspectionId) throws Exception {
        PreparedStatement ps = null; ResultSet rs = null;
        try {
            String sql = "SELECT FINAL_INSPECTION_ID, RESULT_ID, INSPECT_QTY, STATUS AS RESULT FROM FINAL_INSPECTION WHERE FINAL_INSPECTION_ID = ? AND NVL(USE_YN,'Y')='Y'";
            ps = conn.prepareStatement(sql);
            ps.setInt(1, finalInspectionId);
            rs = ps.executeQuery();
            if (rs.next()) {
                FPInspRegInqDTO dto = new FPInspRegInqDTO();
                dto.setFinalInspectionId(rs.getInt("FINAL_INSPECTION_ID"));
                dto.setResultId(rs.getInt("RESULT_ID"));
                dto.setInspectQty(rs.getDouble("INSPECT_QTY"));
                dto.setResult(rs.getString("RESULT"));
                return dto;
            }
            return null;
        } finally { close(rs, ps, null); }
    }

    private int getItemIdByResultId(Connection conn, int resultId) throws Exception {
        PreparedStatement ps = null; ResultSet rs = null;
        try {
            String sql = "SELECT pp.ITEM_ID FROM PRODUCTION_RESULT pr JOIN WORK_ORDER wo ON pr.WORK_ORDER_ID = wo.WORK_ORDER_ID JOIN PRODUCTION_PLAN pp ON wo.PLAN_ID = pp.PLAN_ID WHERE pr.RESULT_ID = ?";
            ps = conn.prepareStatement(sql);
            ps.setInt(1, resultId);
            rs = ps.executeQuery();
            if (rs.next()) return rs.getInt(1);
            throw new RuntimeException("생산실적에 연결된 품목을 찾을 수 없습니다.");
        } finally { close(rs, ps, null); }
    }

    private void applyInventoryDelta(Connection conn, int itemId, double delta) throws Exception {
        double currentStock = selectCurrentStockByItemId(conn, itemId);
        double nextStock = currentStock + delta;
        if (nextStock < 0) throw new RuntimeException("재고가 부족하여 완제품 검사 흐름을 처리할 수 없습니다.");

        PreparedStatement ps = null;
        try {
            ps = conn.prepareStatement("UPDATE INVENTORY SET QTY_ON_HAND = ?, UPDATED_AT = SYSDATE WHERE ITEM_ID = ?");
            ps.setDouble(1, nextStock);
            ps.setInt(2, itemId);
            int updated = ps.executeUpdate();
            close(null, ps, null); ps = null;
            if (updated == 0) {
                ps = conn.prepareStatement("INSERT INTO INVENTORY (INVENTORY_ID, ITEM_ID, QTY_ON_HAND, SAFETY_STOCK, REMARK, CREATED_AT, UPDATED_AT) VALUES ((SELECT NVL(MAX(INVENTORY_ID),0)+1 FROM INVENTORY), ?, ?, 0, NULL, SYSDATE, SYSDATE)");
                ps.setInt(1, itemId);
                ps.setDouble(2, nextStock);
                ps.executeUpdate();
            }
        } finally { close(null, ps, null); }
    }

    private double selectCurrentStockByItemId(Connection conn, int itemId) throws Exception {
        PreparedStatement ps = null; ResultSet rs = null;
        try {
            ps = conn.prepareStatement("SELECT NVL(QTY_ON_HAND, 0) AS QTY_ON_HAND FROM INVENTORY WHERE ITEM_ID = ?");
            ps.setInt(1, itemId);
            rs = ps.executeQuery();
            return rs.next() ? rs.getDouble("QTY_ON_HAND") : 0.0;
        } finally { close(rs, ps, null); }
    }

    private void rollback(Connection conn) {
        if (conn == null) return;
        try { conn.rollback(); } catch (Exception e) { e.printStackTrace(); }
    }

    private void resetAutoCommit(Connection conn) {
        if (conn == null) return;
        try { conn.setAutoCommit(true); } catch (Exception e) { e.printStackTrace(); }
    }

    private void mapBase(ResultSet rs, FPInspRegInqDTO dto) throws Exception {
        dto.setFinalInspectionId(rs.getInt("FINAL_INSPECTION_ID"));
        dto.setResultId(rs.getInt("RESULT_ID"));
        dto.setEmpId(rs.getInt("EMP_ID"));
        dto.setWorkOrderId(rs.getInt("WORK_ORDER_ID"));
        dto.setItemId(rs.getInt("ITEM_ID"));
        dto.setItemCode(rs.getString("ITEM_CODE"));
        dto.setItemName(rs.getString("ITEM_NAME"));
        dto.setLotNo(rs.getString("LOT_NO"));
        dto.setProducedQty(rs.getInt("PRODUCED_QTY"));
        dto.setInspectQty(rs.getDouble("INSPECT_QTY"));
        dto.setResult(rs.getString("RESULT"));
        dto.setInspectionDate(rs.getDate("INSPECTION_DATE"));
        dto.setRemark(rs.getString("REMARK"));
        dto.setCreatedAt(rs.getDate("CREATED_AT"));
        dto.setUpdatedAt(rs.getDate("UPDATED_AT"));
        dto.setCurrentInspectSum(sumInspectQtyByResultId(dto.getResultId()));
        dto.setRemainingQty(Math.max(0, dto.getProducedQty() - dto.getCurrentInspectSum()));
    }

    private int countBySql(String sql, int id) {
        Connection conn = null; PreparedStatement ps = null; ResultSet rs = null;
        try {
            conn = getConnection();
            ps = conn.prepareStatement(sql);
            ps.setInt(1, id);
            rs = ps.executeQuery();
            if (rs.next()) return rs.getInt(1);
        } catch (Exception e) { e.printStackTrace(); }
        finally { close(rs, ps, conn); }
        return 0;
    }

    private void close(ResultSet rs, PreparedStatement ps, Connection conn) {
        try { if (rs != null) rs.close(); } catch (Exception e) {}
        try { if (ps != null) ps.close(); } catch (Exception e) {}
        try { if (conn != null) conn.close(); } catch (Exception e) {}
    }
}
