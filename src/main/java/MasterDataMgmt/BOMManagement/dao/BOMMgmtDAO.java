package MasterDataMgmt.BOMManagement.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

import MasterDataMgmt.BOMManagement.dto.BOMMgmtDTO;
import MasterDataMgmt.BOMManagement.dto.BOMMgmtSearchDTO;

public class BOMMgmtDAO {
    private Connection getConnection() throws Exception {
        Context ctx = new InitialContext();
        DataSource ds = (DataSource) ctx.lookup("java:comp/env/jdbc/oracle");
        return ds.getConnection();
    }

    public List<BOMMgmtDTO> getBOMList(BOMMgmtSearchDTO dto) {
        List<BOMMgmtDTO> list = new ArrayList<BOMMgmtDTO>();
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = getConnection();
            StringBuilder sql = new StringBuilder();
            sql.append("SELECT d.BOM_DETAIL_ID, b.BOM_ID, p.ITEM_ID AS PRODUCT_ITEM_ID, p.ITEM_CODE AS PRODUCT_CODE, p.ITEM_NAME AS PRODUCT_NAME, ");
            sql.append("d.ITEM_ID AS MATERIAL_ID, m.ITEM_CODE AS MATERIAL_CODE, m.ITEM_NAME AS MATERIAL_NAME, d.QTY_REQUIRED, d.UNIT, d.REMARK, d.CREATED_AT, d.UPDATED_AT ");
            sql.append("FROM BOM b JOIN ITEM p ON b.ITEM_ID = p.ITEM_ID JOIN BOM_DETAIL d ON b.BOM_ID = d.BOM_ID JOIN ITEM m ON d.ITEM_ID = m.ITEM_ID WHERE NVL(b.USE_YN, 'Y') = 'Y' AND NVL(d.USE_YN, 'Y') = 'Y' AND NVL(p.USE_YN, 'Y') = 'Y' AND NVL(m.USE_YN, 'Y') = 'Y' ");
            List<Object> params = new ArrayList<Object>();
            if (dto.getProduct_code() != null && !dto.getProduct_code().isEmpty()) {
                sql.append("AND p.ITEM_CODE = ? ");
                params.add(dto.getProduct_code());
            }
            sql.append("ORDER BY p.ITEM_CODE, d.BOM_DETAIL_ID DESC");
            ps = conn.prepareStatement(sql.toString());
            for (int i = 0; i < params.size(); i++) ps.setObject(i + 1, params.get(i));
            rs = ps.executeQuery();
            while (rs.next()) {
                BOMMgmtDTO row = new BOMMgmtDTO();
                row.setBom_detail_id(rs.getInt("BOM_DETAIL_ID"));
                row.setBOM_id(rs.getInt("BOM_ID"));
                row.setItem_id(rs.getInt("PRODUCT_ITEM_ID"));
                row.setProduct_code(rs.getString("PRODUCT_CODE"));
                row.setProduct_name(rs.getString("PRODUCT_NAME"));
                row.setMaterial_id(rs.getInt("MATERIAL_ID"));
                row.setMaterial_code(rs.getString("MATERIAL_CODE"));
                row.setMaterial_name(rs.getString("MATERIAL_NAME"));
                row.setQty_required(rs.getDouble("QTY_REQUIRED"));
                row.setUnit(rs.getString("UNIT"));
                row.setRemark(rs.getString("REMARK"));
                list.add(row);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try { if (rs != null) rs.close(); } catch (Exception e) {}
            try { if (ps != null) ps.close(); } catch (Exception e) {}
            try { if (conn != null) conn.close(); } catch (Exception e) {}
        }
        return list;
    }

    public int findActiveBomIdByProductItemId(int productItemId) {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        int bomId = 0;
        try {
            conn = getConnection();
            String sql = "SELECT BOM_ID FROM BOM WHERE ITEM_ID = ? AND NVL(USE_YN, 'Y') = 'Y' ORDER BY BOM_ID DESC";
            ps = conn.prepareStatement(sql);
            ps.setInt(1, productItemId);
            rs = ps.executeQuery();
            if (rs.next()) bomId = rs.getInt("BOM_ID");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try { if (rs != null) rs.close(); } catch (Exception e) {}
            try { if (ps != null) ps.close(); } catch (Exception e) {}
            try { if (conn != null) conn.close(); } catch (Exception e) {}
        }
        return bomId;
    }

    public int insertBOMAndReturnId(int itemId, String useYn, String remark) {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        int bomId = 0;
        try {
            conn = getConnection();
            ps = conn.prepareStatement("SELECT SEQ_BOM.NEXTVAL FROM DUAL");
            rs = ps.executeQuery();
            if (rs.next()) bomId = rs.getInt(1);
            try { if (rs != null) rs.close(); } catch (Exception e) {}
            try { if (ps != null) ps.close(); } catch (Exception e) {}
            String sql = "INSERT INTO BOM (BOM_ID, ITEM_ID, VERSION_NO, USE_YN, REMARK, CREATED_AT, UPDATED_AT) VALUES (?, ?, 1, ?, ?, SYSDATE, SYSDATE)";
            ps = conn.prepareStatement(sql);
            ps.setInt(1, bomId);
            ps.setInt(2, itemId);
            ps.setString(3, useYn != null ? useYn : "Y");
            ps.setString(4, remark);
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
            bomId = 0;
        } finally {
            try { if (rs != null) rs.close(); } catch (Exception e) {}
            try { if (ps != null) ps.close(); } catch (Exception e) {}
            try { if (conn != null) conn.close(); } catch (Exception e) {}
        }
        return bomId;
    }

    public void insertBOMDetail(BOMMgmtDTO dto) {
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = getConnection();
            String sql = "INSERT INTO BOM_DETAIL (BOM_DETAIL_ID, BOM_ID, ITEM_ID, QTY_REQUIRED, UNIT, REMARK, CREATED_AT, UPDATED_AT) VALUES (SEQ_BOM_DETAIL.NEXTVAL, ?, ?, ?, ?, ?, SYSDATE, SYSDATE)";
            ps = conn.prepareStatement(sql);
            ps.setInt(1, dto.getBOM_id());
            ps.setInt(2, dto.getMaterial_id());
            ps.setDouble(3, dto.getQty_required());
            ps.setString(4, dto.getUnit());
            ps.setString(5, dto.getRemark());
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try { if (ps != null) ps.close(); } catch (Exception e) {}
            try { if (conn != null) conn.close(); } catch (Exception e) {}
        }
    }

    public boolean existsBomDetailCombination(int bomId, int materialId) {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        boolean exists = false;
        try {
            conn = getConnection();
            ps = conn.prepareStatement("SELECT COUNT(*) FROM BOM_DETAIL WHERE BOM_ID = ? AND ITEM_ID = ? AND NVL(USE_YN, 'Y') = 'Y'");
            ps.setInt(1, bomId);
            ps.setInt(2, materialId);
            rs = ps.executeQuery();
            if (rs.next()) exists = rs.getInt(1) > 0;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try { if (rs != null) rs.close(); } catch (Exception e) {}
            try { if (ps != null) ps.close(); } catch (Exception e) {}
            try { if (conn != null) conn.close(); } catch (Exception e) {}
        }
        return exists;
    }

    public int delete(int id) {
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = getConnection();
            ps = conn.prepareStatement("UPDATE BOM_DETAIL SET USE_YN = 'N', UPDATED_AT = SYSDATE WHERE BOM_DETAIL_ID = ? AND NVL(USE_YN, 'Y') = 'Y'");
            ps.setInt(1, id);
            return ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try { if (ps != null) ps.close(); } catch (Exception e) {}
            try { if (conn != null) conn.close(); } catch (Exception e) {}
        }
        return 0;
    }

    public BOMMgmtDTO selectOne(int bomDetailId) {
        BOMMgmtDTO dto = null;
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = getConnection();
            String sql = "SELECT b.BOM_ID, p.ITEM_ID AS PRODUCT_ITEM_ID, p.ITEM_CODE AS PRODUCT_CODE, p.ITEM_NAME AS PRODUCT_NAME, d.BOM_DETAIL_ID, d.ITEM_ID AS MATERIAL_ID, m.ITEM_CODE AS MATERIAL_CODE, m.ITEM_NAME AS MATERIAL_NAME, d.QTY_REQUIRED, d.UNIT, d.REMARK FROM BOM b JOIN ITEM p ON b.ITEM_ID = p.ITEM_ID JOIN BOM_DETAIL d ON b.BOM_ID = d.BOM_ID JOIN ITEM m ON d.ITEM_ID = m.ITEM_ID WHERE d.BOM_DETAIL_ID = ? AND NVL(b.USE_YN, 'Y') = 'Y' AND NVL(d.USE_YN, 'Y') = 'Y' AND NVL(p.USE_YN, 'Y') = 'Y' AND NVL(m.USE_YN, 'Y') = 'Y'";
            ps = conn.prepareStatement(sql);
            ps.setInt(1, bomDetailId);
            rs = ps.executeQuery();
            if (rs.next()) {
                dto = new BOMMgmtDTO();
                dto.setBOM_id(rs.getInt("BOM_ID"));
                dto.setItem_id(rs.getInt("PRODUCT_ITEM_ID"));
                dto.setBom_detail_id(rs.getInt("BOM_DETAIL_ID"));
                dto.setProduct_code(rs.getString("PRODUCT_CODE"));
                dto.setProduct_name(rs.getString("PRODUCT_NAME"));
                dto.setMaterial_id(rs.getInt("MATERIAL_ID"));
                dto.setMaterial_code(rs.getString("MATERIAL_CODE"));
                dto.setMaterial_name(rs.getString("MATERIAL_NAME"));
                dto.setQty_required(rs.getDouble("QTY_REQUIRED"));
                dto.setUnit(rs.getString("UNIT"));
                dto.setRemark(rs.getString("REMARK"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try { if (rs != null) rs.close(); } catch(Exception e) {}
            try { if (ps != null) ps.close(); } catch(Exception e) {}
            try { if (conn != null) conn.close(); } catch(Exception e) {}
        }
        return dto;
    }

    public static int updateBom(Connection conn, BOMMgmtDTO dto) {
        PreparedStatement ps = null;
        int result = 0;
        String sql = "UPDATE BOM_DETAIL SET QTY_REQUIRED = ?, REMARK = ?, UPDATED_AT = SYSDATE WHERE BOM_DETAIL_ID = ? AND NVL(USE_YN, 'Y') = 'Y'";
        try {
            ps = conn.prepareStatement(sql);
            ps.setDouble(1, dto.getQty_required());
            ps.setString(2, dto.getRemark());
            ps.setInt(3, dto.getBom_detail_id());
            result = ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try { if (ps != null) ps.close(); } catch(Exception e) {}
        }
        return result;
    }
}
