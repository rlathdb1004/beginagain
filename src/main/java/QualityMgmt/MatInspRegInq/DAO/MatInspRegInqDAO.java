package QualityMgmt.MatInspRegInq.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

import QualityMgmt.MatInspRegInq.DTO.MatInspRegInqDTO;
import item.dto.ItemDTO;

public class MatInspRegInqDAO {

    public List<MatInspRegInqDTO> selectMatInspRegInqList(MatInspRegInqDTO searchDTO) {
        List<MatInspRegInqDTO> list = new ArrayList<MatInspRegInqDTO>();
        Connection conn = null; PreparedStatement ps = null; ResultSet rs = null;
        try {
            conn = getConnection();
            StringBuilder query = new StringBuilder();
            query.append(" SELECT mi.MATERIAL_INSPECTION_ID, mi.ITEM_ID, mi.EMP_ID, i.ITEM_CODE, i.ITEM_NAME, i.UNIT, ");
            query.append("        mi.INSPECT_QTY, mi.GOOD_QTY, mi.DEFECT_QTY, mi.INSPECTION_RESULT AS RESULT, ");
            query.append("        mi.INSPECTION_DATE, mi.REMARK, mi.CREATED_AT, mi.UPDATED_AT, e.EMP_NAME ");
            query.append("   FROM MATERIAL_INSPECTION mi ");
            query.append("   JOIN ITEM i ON mi.ITEM_ID = i.ITEM_ID ");
            query.append("   LEFT JOIN EMP e ON mi.EMP_ID = e.EMP_ID ");
            query.append("  WHERE mi.USE_YN = 'Y' ");
            List<Object> params = new ArrayList<Object>();
            if (searchDTO.getResultType() != null && !"".equals(searchDTO.getResultType().trim()) && !"전체".equals(searchDTO.getResultType()) && !"all".equals(searchDTO.getResultType())) {
                query.append(" AND mi.INSPECTION_RESULT = ? "); params.add(searchDTO.getResultType());
            }
            if (searchDTO.getStartDate() != null) { query.append(" AND TRUNC(mi.INSPECTION_DATE) >= ? "); params.add(searchDTO.getStartDate()); }
            if (searchDTO.getEndDate() != null) { query.append(" AND TRUNC(mi.INSPECTION_DATE) <= ? "); params.add(searchDTO.getEndDate()); }
            String searchType = searchDTO.getSearchType();
            String keyword = searchDTO.getKeyword();
            if (keyword != null && !"".equals(keyword.trim())) {
                String like = "%" + keyword.trim() + "%";
                if ("itemCode".equals(searchType)) {
                    query.append(" AND i.ITEM_CODE LIKE ? "); params.add(like);
                } else if ("itemName".equals(searchType)) {
                    query.append(" AND i.ITEM_NAME LIKE ? "); params.add(like);
                } else {
                    query.append(" AND (i.ITEM_CODE LIKE ? OR i.ITEM_NAME LIKE ?) "); params.add(like); params.add(like);
                }
            }
            query.append(" ORDER BY mi.MATERIAL_INSPECTION_ID DESC ");
            ps = conn.prepareStatement(query.toString());
            for (int i = 0; i < params.size(); i++) ps.setObject(i + 1, params.get(i));
            rs = ps.executeQuery();
            while (rs.next()) list.add(mapRow(rs));
        } catch (Exception e) { e.printStackTrace(); }
        finally { close(rs); close(ps); close(conn); }
        return list;
    }

    public MatInspRegInqDTO selectMatInspRegInqOne(int materialInspectionId) {
        Connection conn = null; PreparedStatement ps = null; ResultSet rs = null;
        try {
            conn = getConnection();
            String sql = "SELECT mi.MATERIAL_INSPECTION_ID, mi.ITEM_ID, mi.EMP_ID, i.ITEM_CODE, i.ITEM_NAME, i.UNIT, "
                    + "mi.INSPECT_QTY, mi.GOOD_QTY, mi.DEFECT_QTY, mi.INSPECTION_RESULT AS RESULT, "
                    + "mi.INSPECTION_DATE, mi.REMARK, mi.CREATED_AT, mi.UPDATED_AT, e.EMP_NAME "
                    + "FROM MATERIAL_INSPECTION mi "
                    + "JOIN ITEM i ON mi.ITEM_ID = i.ITEM_ID "
                    + "LEFT JOIN EMP e ON mi.EMP_ID = e.EMP_ID "
                    + "WHERE mi.MATERIAL_INSPECTION_ID = ? AND mi.USE_YN = 'Y'";
            ps = conn.prepareStatement(sql); ps.setInt(1, materialInspectionId); rs = ps.executeQuery();
            if (rs.next()) return mapRow(rs);
        } catch (Exception e) { e.printStackTrace(); }
        finally { close(rs); close(ps); close(conn); }
        return null;
    }

    public int insertMatInspRegInq(MatInspRegInqDTO dto) {
        Connection conn = null; PreparedStatement ps = null;
        try {
            conn = getConnection();
            String sql = "INSERT INTO MATERIAL_INSPECTION (MATERIAL_INSPECTION_ID, ITEM_ID, EMP_ID, INSPECT_QTY, GOOD_QTY, DEFECT_QTY, INSPECTION_RESULT, INSPECTION_DATE, REMARK, USE_YN, CREATED_AT, UPDATED_AT) "
                    + "VALUES ((SELECT NVL(MAX(MATERIAL_INSPECTION_ID),0)+1 FROM MATERIAL_INSPECTION), ?, ?, ?, ?, ?, ?, ?, ?, 'Y', SYSDATE, SYSDATE)";
            ps = conn.prepareStatement(sql);
            ps.setInt(1, dto.getItemId());
            ps.setInt(2, dto.getEmpId());
            ps.setDouble(3, dto.getInspectQty());
            ps.setDouble(4, dto.getGoodQty());
            ps.setDouble(5, dto.getDefectQty());
            ps.setString(6, dto.getResult());
            ps.setDate(7, dto.getInspectionDate());
            ps.setString(8, dto.getRemark());
            return ps.executeUpdate();
        } catch (Exception e) { e.printStackTrace(); }
        finally { close(ps); close(conn); }
        return 0;
    }

    public int deleteMatInspRegInq(int[] ids) {
        if (ids == null || ids.length == 0) return 0;
        Connection conn = null; PreparedStatement ps = null;
        try {
            conn = getConnection();
            StringBuilder sql = new StringBuilder("UPDATE MATERIAL_INSPECTION SET USE_YN='N', UPDATED_AT=SYSDATE WHERE MATERIAL_INSPECTION_ID IN (");
            for (int i = 0; i < ids.length; i++) { if (i > 0) sql.append(','); sql.append('?'); }
            sql.append(')');
            ps = conn.prepareStatement(sql.toString());
            for (int i = 0; i < ids.length; i++) ps.setInt(i + 1, ids[i]);
            return ps.executeUpdate();
        } catch (Exception e) { e.printStackTrace(); }
        finally { close(ps); close(conn); }
        return 0;
    }

    public int updateMatInspRegInq(MatInspRegInqDTO dto) {
        Connection conn = null; PreparedStatement ps = null;
        try {
            conn = getConnection();
            String sql = "UPDATE MATERIAL_INSPECTION SET INSPECT_QTY = ?, GOOD_QTY = ?, DEFECT_QTY = ?, INSPECTION_RESULT = ?, INSPECTION_DATE = ?, REMARK = ?, UPDATED_AT = SYSDATE WHERE MATERIAL_INSPECTION_ID = ?";
            ps = conn.prepareStatement(sql);
            ps.setDouble(1, dto.getInspectQty());
            ps.setDouble(2, dto.getGoodQty());
            ps.setDouble(3, dto.getDefectQty());
            ps.setString(4, dto.getResult());
            ps.setDate(5, dto.getInspectionDate());
            ps.setString(6, dto.getRemark());
            ps.setInt(7, dto.getMaterialInspectionId());
            return ps.executeUpdate();
        } catch (Exception e) { e.printStackTrace(); }
        finally { close(ps); close(conn); }
        return 0;
    }

    public List<ItemDTO> selectMaterialItemList() {
        List<ItemDTO> list = new ArrayList<ItemDTO>();
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            conn = getConnection();

            String sql = ""
                    + "SELECT DISTINCT i.ITEM_ID, i.ITEM_CODE, i.ITEM_NAME, i.UNIT "
                    + "FROM ITEM i "
                    + "JOIN MATERIAL_INOUT mi ON mi.ITEM_ID = i.ITEM_ID "
                    + "WHERE i.USE_YN = 'Y' "
                    + "  AND i.ITEM_TYPE = '원자재' "
                    + "  AND NVL(mi.USE_YN, 'Y') = 'Y' "
                    + "  AND mi.INOUT_TYPE = '입고' "
                    + "ORDER BY i.ITEM_CODE";

            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();

            while (rs.next()) {
                ItemDTO dto = new ItemDTO();
                dto.setItemId(rs.getInt("ITEM_ID"));
                dto.setItemCode(rs.getString("ITEM_CODE"));
                dto.setItemName(rs.getString("ITEM_NAME"));
                dto.setUnit(rs.getString("UNIT"));
                list.add(dto);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            close(rs);
            close(ps);
            close(conn);
        }

        return list;
    }

    public boolean existsMaterialItem(int itemId) {
        return existsByQuery("SELECT COUNT(*) FROM ITEM WHERE ITEM_ID = ? AND USE_YN = 'Y' AND ITEM_TYPE = '원자재'", itemId);
    }

    public boolean existsActiveEmp(int empId) {
        return existsByQuery("SELECT COUNT(*) FROM EMP WHERE EMP_ID = ? AND USE_YN = 'Y'", empId);
    }

    public boolean hasMaterialDefect(int materialInspectionId) {
        return existsByQuery("SELECT COUNT(*) FROM DEFECT_MATERIAL WHERE MATERIAL_INSPECTION_ID = ? AND USE_YN = 'Y'", materialInspectionId);
    }

    private boolean existsByQuery(String sql, int value) {
        Connection conn = null; PreparedStatement ps = null; ResultSet rs = null;
        try {
            conn = getConnection(); ps = conn.prepareStatement(sql); ps.setInt(1, value); rs = ps.executeQuery();
            if (rs.next()) return rs.getInt(1) > 0;
        } catch (Exception e) { e.printStackTrace(); }
        finally { close(rs); close(ps); close(conn); }
        return false;
    }

    private MatInspRegInqDTO mapRow(ResultSet rs) throws Exception {
        MatInspRegInqDTO dto = new MatInspRegInqDTO();
        dto.setMaterialInspectionId(rs.getInt("MATERIAL_INSPECTION_ID"));
        dto.setItemId(rs.getInt("ITEM_ID"));
        dto.setEmpId(rs.getInt("EMP_ID"));
        dto.setItemCode(rs.getString("ITEM_CODE"));
        dto.setItemName(rs.getString("ITEM_NAME"));
        dto.setUnit(rs.getString("UNIT"));
        dto.setEmpName(rs.getString("EMP_NAME"));
        dto.setInspectQty(rs.getDouble("INSPECT_QTY"));
        dto.setGoodQty(rs.getDouble("GOOD_QTY"));
        dto.setDefectQty(rs.getDouble("DEFECT_QTY"));
        dto.setResult(rs.getString("RESULT"));
        dto.setInspectionDate(rs.getDate("INSPECTION_DATE"));
        dto.setRemark(rs.getString("REMARK"));
        dto.setCreatedAt(rs.getDate("CREATED_AT"));
        dto.setUpdatedAt(rs.getDate("UPDATED_AT"));
        return dto;
    }

    private Connection getConnection() throws Exception {
        Context ctx = new InitialContext();
        DataSource dataFactory = (DataSource) ctx.lookup("java:/comp/env/jdbc/oracle");
        return dataFactory.getConnection();
    }
    private void close(AutoCloseable c) { try { if (c != null) c.close(); } catch (Exception e) {} }
}
