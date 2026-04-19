package QualityMgmt.DefectRegInq.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

import QualityMgmt.DefectRegInq.DTO.DefectRegInqDTO;

public class DefectRegInqDAO {

    public List<DefectRegInqDTO> selectDefectRegInqList(DefectRegInqDTO searchDTO) {
        List<DefectRegInqDTO> list = new ArrayList<DefectRegInqDTO>();
        Connection conn = null; PreparedStatement ps = null; ResultSet rs = null;
        try {
            conn = getConnection();
            StringBuilder query = new StringBuilder();
            query.append(" SELECT dp.DEFECT_PRODUCT_ID, dp.FINAL_INSPECTION_ID, fi.RESULT_ID, wo.WORK_ORDER_ID, pp.PLAN_ID, ");
            query.append("        pp.ITEM_ID, i.ITEM_CODE, i.ITEM_NAME, pr.LOT_NO, fi.INSPECT_QTY, fi.STATUS AS INSPECTION_STATUS, ");
            query.append("        dc.DEFECT_CODE_ID, dc.DEFECT_CODE, dc.DEFECT_NAME, dc.DEFECT_TYPE, dp.REMARK, dp.CREATED_AT, dp.UPDATED_AT ");
            query.append("   FROM DEFECT_PRODUCT dp ");
            query.append("   JOIN DEFECT_CODE dc ON dp.DEFECT_CODE_ID = dc.DEFECT_CODE_ID ");
            query.append("   JOIN FINAL_INSPECTION fi ON dp.FINAL_INSPECTION_ID = fi.FINAL_INSPECTION_ID ");
            query.append("   JOIN PRODUCTION_RESULT pr ON fi.RESULT_ID = pr.RESULT_ID ");
            query.append("   JOIN WORK_ORDER wo ON pr.WORK_ORDER_ID = wo.WORK_ORDER_ID ");
            query.append("   JOIN PRODUCTION_PLAN pp ON wo.PLAN_ID = pp.PLAN_ID ");
            query.append("   JOIN ITEM i ON pp.ITEM_ID = i.ITEM_ID ");
            query.append("  WHERE dp.USE_YN = 'Y' AND dc.USE_YN = 'Y' AND fi.USE_YN = 'Y' ");
            List<Object> params = new ArrayList<Object>();
            if (searchDTO.getDefectTypeSearch() != null && !"".equals(searchDTO.getDefectTypeSearch().trim()) && !"전체".equals(searchDTO.getDefectTypeSearch())) {
                query.append(" AND dc.DEFECT_TYPE = ? "); params.add(searchDTO.getDefectTypeSearch());
            }
            if (searchDTO.getStartDate() != null) { query.append(" AND TRUNC(dp.CREATED_AT) >= ? "); params.add(searchDTO.getStartDate()); }
            if (searchDTO.getEndDate() != null) { query.append(" AND TRUNC(dp.CREATED_AT) <= ? "); params.add(searchDTO.getEndDate()); }
            String searchType = searchDTO.getSearchType();
            String keyword = searchDTO.getKeyword();
            if (keyword != null && !"".equals(keyword.trim())) {
                String like = "%" + keyword.trim() + "%";
                if ("itemCode".equals(searchType)) { query.append(" AND i.ITEM_CODE LIKE ? "); params.add(like); }
                else if ("itemName".equals(searchType)) { query.append(" AND i.ITEM_NAME LIKE ? "); params.add(like); }
                else if ("defectCode".equals(searchType)) { query.append(" AND dc.DEFECT_CODE LIKE ? "); params.add(like); }
                else if ("defectName".equals(searchType)) { query.append(" AND dc.DEFECT_NAME LIKE ? "); params.add(like); }
                else {
                    query.append(" AND (i.ITEM_CODE LIKE ? OR i.ITEM_NAME LIKE ? OR dc.DEFECT_CODE LIKE ? OR dc.DEFECT_NAME LIKE ?) ");
                    params.add(like); params.add(like); params.add(like); params.add(like);
                }
            }
            query.append(" ORDER BY dp.DEFECT_PRODUCT_ID DESC ");
            ps = conn.prepareStatement(query.toString());
            bind(ps, params);
            rs = ps.executeQuery();
            while (rs.next()) list.add(mapRow(rs));
        } catch (Exception e) { e.printStackTrace(); }
        finally { close(rs, ps, conn); }
        return list;
    }

    public DefectRegInqDTO selectDefectRegInqOne(int defectProductId) {
        Connection conn = null; PreparedStatement ps = null; ResultSet rs = null;
        try {
            conn = getConnection();
            String sql = " SELECT dp.DEFECT_PRODUCT_ID, dp.FINAL_INSPECTION_ID, fi.RESULT_ID, wo.WORK_ORDER_ID, pp.PLAN_ID, "
                    + " pp.ITEM_ID, i.ITEM_CODE, i.ITEM_NAME, pr.LOT_NO, fi.INSPECT_QTY, fi.STATUS AS INSPECTION_STATUS, "
                    + " dc.DEFECT_CODE_ID, dc.DEFECT_CODE, dc.DEFECT_NAME, dc.DEFECT_TYPE, dp.REMARK, dp.CREATED_AT, dp.UPDATED_AT, "
                    + " (SELECT COUNT(*) FROM DEFECT_PRODUCT x WHERE x.FINAL_INSPECTION_ID = dp.FINAL_INSPECTION_ID AND x.USE_YN='Y') AS DEFECT_CODE_COUNT "
                    + " FROM DEFECT_PRODUCT dp "
                    + " JOIN DEFECT_CODE dc ON dp.DEFECT_CODE_ID = dc.DEFECT_CODE_ID "
                    + " JOIN FINAL_INSPECTION fi ON dp.FINAL_INSPECTION_ID = fi.FINAL_INSPECTION_ID "
                    + " JOIN PRODUCTION_RESULT pr ON fi.RESULT_ID = pr.RESULT_ID "
                    + " JOIN WORK_ORDER wo ON pr.WORK_ORDER_ID = wo.WORK_ORDER_ID "
                    + " JOIN PRODUCTION_PLAN pp ON wo.PLAN_ID = pp.PLAN_ID "
                    + " JOIN ITEM i ON pp.ITEM_ID = i.ITEM_ID "
                    + " WHERE dp.DEFECT_PRODUCT_ID = ? AND dp.USE_YN='Y' ";
            ps = conn.prepareStatement(sql); ps.setInt(1, defectProductId); rs = ps.executeQuery();
            if (rs.next()) return mapRow(rs);
        } catch (Exception e) { e.printStackTrace(); }
        finally { close(rs, ps, conn); }
        return null;
    }

    public List<DefectRegInqDTO> selectAvailableFinalInspectionList() {
        List<DefectRegInqDTO> list = new ArrayList<DefectRegInqDTO>();
        Connection conn = null; PreparedStatement ps = null; ResultSet rs = null;
        try {
            conn = getConnection();
            String sql = " SELECT fi.FINAL_INSPECTION_ID, fi.RESULT_ID, wo.WORK_ORDER_ID, pp.PLAN_ID, pp.ITEM_ID, i.ITEM_CODE, i.ITEM_NAME, pr.LOT_NO, fi.INSPECT_QTY, fi.STATUS AS INSPECTION_STATUS, "
                    + " (SELECT COUNT(*) FROM DEFECT_PRODUCT dp WHERE dp.FINAL_INSPECTION_ID = fi.FINAL_INSPECTION_ID AND dp.USE_YN='Y') AS DEFECT_CODE_COUNT "
                    + " FROM FINAL_INSPECTION fi "
                    + " JOIN PRODUCTION_RESULT pr ON fi.RESULT_ID = pr.RESULT_ID "
                    + " JOIN WORK_ORDER wo ON pr.WORK_ORDER_ID = wo.WORK_ORDER_ID "
                    + " JOIN PRODUCTION_PLAN pp ON wo.PLAN_ID = pp.PLAN_ID "
                    + " JOIN ITEM i ON pp.ITEM_ID = i.ITEM_ID "
                    + " WHERE fi.USE_YN='Y' AND NVL(fi.STATUS, ' ') <> '합격' "
                    + " ORDER BY fi.FINAL_INSPECTION_ID DESC ";
            ps = conn.prepareStatement(sql); rs = ps.executeQuery();
            while (rs.next()) list.add(mapRow(rs));
        } catch (Exception e) { e.printStackTrace(); }
        finally { close(rs, ps, conn); }
        return list;
    }

    public List<DefectRegInqDTO> selectAvailableDefectCodeList() {
        List<DefectRegInqDTO> list = new ArrayList<DefectRegInqDTO>();
        Connection conn = null; PreparedStatement ps = null; ResultSet rs = null;
        try {
            conn = getConnection();
            String sql = " SELECT DEFECT_CODE_ID, DEFECT_CODE, DEFECT_NAME, DEFECT_TYPE FROM DEFECT_CODE WHERE USE_YN='Y' ORDER BY DEFECT_CODE ";
            ps = conn.prepareStatement(sql); rs = ps.executeQuery();
            while (rs.next()) {
                DefectRegInqDTO dto = new DefectRegInqDTO();
                dto.setDefectCodeId(rs.getInt("DEFECT_CODE_ID"));
                dto.setDefectCode(rs.getString("DEFECT_CODE"));
                dto.setDefectName(rs.getString("DEFECT_NAME"));
                dto.setDefectType(rs.getString("DEFECT_TYPE"));
                list.add(dto);
            }
        } catch (Exception e) { e.printStackTrace(); }
        finally { close(rs, ps, conn); }
        return list;
    }

    public boolean existsActiveFinalInspection(int finalInspectionId) {
        return count("SELECT COUNT(*) FROM FINAL_INSPECTION WHERE FINAL_INSPECTION_ID = ? AND USE_YN='Y'", finalInspectionId) > 0;
    }
    public boolean existsActiveDefectCode(int defectCodeId) {
        return count("SELECT COUNT(*) FROM DEFECT_CODE WHERE DEFECT_CODE_ID = ? AND USE_YN='Y'", defectCodeId) > 0;
    }
    public boolean existsDuplicateMapping(int finalInspectionId, int defectCodeId) {
        Connection conn = null; PreparedStatement ps = null; ResultSet rs = null;
        try {
            conn = getConnection();
            String sql = "SELECT COUNT(*) FROM DEFECT_PRODUCT WHERE FINAL_INSPECTION_ID = ? AND DEFECT_CODE_ID = ? AND USE_YN='Y'";
            ps = conn.prepareStatement(sql); ps.setInt(1, finalInspectionId); ps.setInt(2, defectCodeId); rs = ps.executeQuery();
            if (rs.next()) return rs.getInt(1) > 0;
        } catch (Exception e) { e.printStackTrace(); }
        finally { close(rs, ps, conn); }
        return false;
    }
    public boolean existsDuplicateMappingExcluding(int defectProductId, int finalInspectionId, int defectCodeId) {
        Connection conn = null; PreparedStatement ps = null; ResultSet rs = null;
        try {
            conn = getConnection();
            String sql = "SELECT COUNT(*) FROM DEFECT_PRODUCT WHERE FINAL_INSPECTION_ID = ? AND DEFECT_CODE_ID = ? AND DEFECT_PRODUCT_ID <> ? AND USE_YN='Y'";
            ps = conn.prepareStatement(sql); ps.setInt(1, finalInspectionId); ps.setInt(2, defectCodeId); ps.setInt(3, defectProductId); rs = ps.executeQuery();
            if (rs.next()) return rs.getInt(1) > 0;
        } catch (Exception e) { e.printStackTrace(); }
        finally { close(rs, ps, conn); }
        return false;
    }
    public String getInspectionStatus(int finalInspectionId) {
        Connection conn = null; PreparedStatement ps = null; ResultSet rs = null;
        try {
            conn = getConnection();
            ps = conn.prepareStatement("SELECT STATUS FROM FINAL_INSPECTION WHERE FINAL_INSPECTION_ID = ?");
            ps.setInt(1, finalInspectionId); rs = ps.executeQuery();
            if (rs.next()) return rs.getString(1);
        } catch (Exception e) { e.printStackTrace(); }
        finally { close(rs, ps, conn); }
        return null;
    }
    public int getFinalInspectionIdByDefectProductId(int defectProductId) {
        Connection conn = null; PreparedStatement ps = null; ResultSet rs = null;
        try {
            conn = getConnection();
            ps = conn.prepareStatement("SELECT FINAL_INSPECTION_ID FROM DEFECT_PRODUCT WHERE DEFECT_PRODUCT_ID = ?");
            ps.setInt(1, defectProductId); rs = ps.executeQuery();
            if (rs.next()) return rs.getInt(1);
        } catch (Exception e) { e.printStackTrace(); }
        finally { close(rs, ps, conn); }
        return 0;
    }
    public int insertDefectRegInq(DefectRegInqDTO dto) {
        Connection conn = null; PreparedStatement ps = null;
        try {
            conn = getConnection();
            String sql = "INSERT INTO DEFECT_PRODUCT (DEFECT_PRODUCT_ID, FINAL_INSPECTION_ID, DEFECT_CODE_ID, USE_YN, REMARK, CREATED_AT, UPDATED_AT) VALUES ((SELECT NVL(MAX(DEFECT_PRODUCT_ID),0)+1 FROM DEFECT_PRODUCT), ?, ?, 'Y', ?, SYSDATE, SYSDATE)";
            ps = conn.prepareStatement(sql);
            ps.setInt(1, dto.getFinalInspectionId());
            ps.setInt(2, dto.getDefectCodeId());
            ps.setString(3, dto.getRemark());
            return ps.executeUpdate();
        } catch (Exception e) { e.printStackTrace(); }
        finally { close(null, ps, conn); }
        return 0;
    }
    public int deleteDefectRegInq(int[] ids) {
        Connection conn = null; PreparedStatement ps = null; int result = 0;
        try {
            if (ids == null || ids.length == 0) return 0;
            conn = getConnection();
            StringBuilder sql = new StringBuilder("UPDATE DEFECT_PRODUCT SET USE_YN='N', UPDATED_AT = SYSDATE WHERE DEFECT_PRODUCT_ID IN (");
            for (int i = 0; i < ids.length; i++) { if (i > 0) sql.append(','); sql.append('?'); }
            sql.append(')');
            ps = conn.prepareStatement(sql.toString());
            for (int i = 0; i < ids.length; i++) ps.setInt(i + 1, ids[i]);
            result = ps.executeUpdate();
        } catch (Exception e) { e.printStackTrace(); }
        finally { close(null, ps, conn); }
        return result;
    }
    public int updateDefectRegInq(DefectRegInqDTO dto) {
        Connection conn = null; PreparedStatement ps = null;
        try {
            conn = getConnection();
            String sql = "UPDATE DEFECT_PRODUCT SET REMARK = ?, UPDATED_AT = SYSDATE WHERE DEFECT_PRODUCT_ID = ?";
            ps = conn.prepareStatement(sql);
            ps.setString(1, dto.getRemark());
            ps.setInt(2, dto.getDefectProductId());
            return ps.executeUpdate();
        } catch (Exception e) { e.printStackTrace(); }
        finally { close(null, ps, conn); }
        return 0;
    }

    private DefectRegInqDTO mapRow(ResultSet rs) throws Exception {
        DefectRegInqDTO dto = new DefectRegInqDTO();
        safeSetInt(rs, dto);
        dto.setRemark(getString(rs, "REMARK"));
        dto.setCreatedAt(getDate(rs, "CREATED_AT"));
        dto.setUpdatedAt(getDate(rs, "UPDATED_AT"));
        return dto;
    }

    private void safeSetInt(ResultSet rs, DefectRegInqDTO dto) throws Exception {
        if (hasColumn(rs, "DEFECT_PRODUCT_ID")) dto.setDefectProductId(rs.getInt("DEFECT_PRODUCT_ID"));
        if (hasColumn(rs, "FINAL_INSPECTION_ID")) dto.setFinalInspectionId(rs.getInt("FINAL_INSPECTION_ID"));
        if (hasColumn(rs, "RESULT_ID")) dto.setResultId(rs.getInt("RESULT_ID"));
        if (hasColumn(rs, "WORK_ORDER_ID")) dto.setWorkOrderId(rs.getInt("WORK_ORDER_ID"));
        if (hasColumn(rs, "PLAN_ID")) dto.setPlanId(rs.getInt("PLAN_ID"));
        if (hasColumn(rs, "ITEM_ID")) dto.setItemId(rs.getInt("ITEM_ID"));
        if (hasColumn(rs, "ITEM_CODE")) dto.setItemCode(rs.getString("ITEM_CODE"));
        if (hasColumn(rs, "ITEM_NAME")) dto.setItemName(rs.getString("ITEM_NAME"));
        if (hasColumn(rs, "LOT_NO")) dto.setLotNo(rs.getString("LOT_NO"));
        if (hasColumn(rs, "INSPECT_QTY")) dto.setInspectQty(rs.getDouble("INSPECT_QTY"));
        if (hasColumn(rs, "DEFECT_CODE_ID")) dto.setDefectCodeId(rs.getInt("DEFECT_CODE_ID"));
        if (hasColumn(rs, "DEFECT_CODE")) dto.setDefectCode(rs.getString("DEFECT_CODE"));
        if (hasColumn(rs, "DEFECT_NAME")) dto.setDefectName(rs.getString("DEFECT_NAME"));
        if (hasColumn(rs, "DEFECT_TYPE")) dto.setDefectType(rs.getString("DEFECT_TYPE"));
        if (hasColumn(rs, "INSPECTION_STATUS")) dto.setInspectionStatus(rs.getString("INSPECTION_STATUS"));
        if (hasColumn(rs, "DEFECT_CODE_COUNT")) dto.setDefectCodeCount(rs.getInt("DEFECT_CODE_COUNT"));
    }

    private boolean hasColumn(ResultSet rs, String name) {
        try { rs.findColumn(name); return true; } catch (Exception e) { return false; }
    }
    private String getString(ResultSet rs, String name) { try { return rs.getString(name); } catch (Exception e) { return null; } }
    private java.sql.Date getDate(ResultSet rs, String name) { try { return rs.getDate(name); } catch (Exception e) { return null; } }

    private int count(String sql, int id) {
        Connection conn = null; PreparedStatement ps = null; ResultSet rs = null;
        try {
            conn = getConnection(); ps = conn.prepareStatement(sql); ps.setInt(1, id); rs = ps.executeQuery(); if (rs.next()) return rs.getInt(1);
        } catch (Exception e) { e.printStackTrace(); }
        finally { close(rs, ps, conn); }
        return 0;
    }
    private void bind(PreparedStatement ps, List<Object> params) throws Exception {
        for (int i = 0; i < params.size(); i++) {
            Object p = params.get(i);
            if (p instanceof java.sql.Date) ps.setDate(i + 1, (java.sql.Date) p);
            else ps.setObject(i + 1, p);
        }
    }
    private Connection getConnection() throws Exception {
        Context ctx = new InitialContext(); DataSource dataFactory = (DataSource) ctx.lookup("java:/comp/env/jdbc/oracle"); return dataFactory.getConnection();
    }
    private void close(ResultSet rs, PreparedStatement ps, Connection conn) {
        try { if (rs != null) rs.close(); } catch (Exception e) {}
        try { if (ps != null) ps.close(); } catch (Exception e) {}
        try { if (conn != null) conn.close(); } catch (Exception e) {}
    }
}
