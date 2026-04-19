package MaterialMgmt.InvRegInq.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

import MaterialMgmt.InvRegInq.DTO.InvRegInqDTO;

public class InvRegInqDAO {

    private Connection getConnection() throws Exception {
        Context ctx = new InitialContext();
        DataSource dataFactory = (DataSource) ctx.lookup("java:/comp/env/jdbc/oracle");
        return dataFactory.getConnection();
    }

    public List<InvRegInqDTO> selectInvRegInqList(InvRegInqDTO searchDTO) {
        List<InvRegInqDTO> list = new ArrayList<InvRegInqDTO>();
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            conn = getConnection();

            StringBuilder query = new StringBuilder();
            query.append(" SELECT iv.INVENTORY_ID, iv.ITEM_ID, i.ITEM_CODE, i.ITEM_NAME, i.ITEM_TYPE, ");
            query.append("        iv.QTY_ON_HAND, iv.SAFETY_STOCK, i.UNIT, iv.REMARK, iv.CREATED_AT, iv.UPDATED_AT ");
            query.append("   FROM INVENTORY iv ");
            query.append("   JOIN ITEM i ON iv.ITEM_ID = i.ITEM_ID ");
            query.append("  WHERE 1 = 1 ");

            List<Object> params = new ArrayList<Object>();

            if (searchDTO.getStartDate() != null) {
                query.append(" AND TRUNC(iv.CREATED_AT) >= ? ");
                params.add(searchDTO.getStartDate());
            }
            if (searchDTO.getEndDate() != null) {
                query.append(" AND TRUNC(iv.CREATED_AT) <= ? ");
                params.add(searchDTO.getEndDate());
            }

            String searchType = searchDTO.getSearchType();
            String keyword = searchDTO.getKeyword();
            if (keyword != null && !"".equals(keyword.trim())) {
                String keywordLike = "%" + keyword.trim() + "%";
                if ("itemCode".equals(searchType)) {
                    query.append(" AND i.ITEM_CODE LIKE ? ");
                    params.add(keywordLike);
                } else if ("itemName".equals(searchType)) {
                    query.append(" AND i.ITEM_NAME LIKE ? ");
                    params.add(keywordLike);
                } else {
                    query.append(" AND (i.ITEM_CODE LIKE ? OR i.ITEM_NAME LIKE ?) ");
                    params.add(keywordLike);
                    params.add(keywordLike);
                }
            }

            query.append(" ORDER BY CASE WHEN iv.QTY_ON_HAND < iv.SAFETY_STOCK THEN 0 ELSE 1 END, iv.INVENTORY_ID DESC ");

            ps = conn.prepareStatement(query.toString());
            for (int i = 0; i < params.size(); i++) {
                ps.setObject(i + 1, params.get(i));
            }
            rs = ps.executeQuery();

            while (rs.next()) {
                list.add(mapRow(rs));
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

    public InvRegInqDTO selectInvRegInqOne(int inventoryId) {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = getConnection();
            String sql = "SELECT iv.INVENTORY_ID, iv.ITEM_ID, i.ITEM_CODE, i.ITEM_NAME, i.ITEM_TYPE, iv.QTY_ON_HAND, iv.SAFETY_STOCK, i.UNIT, iv.REMARK, iv.CREATED_AT, iv.UPDATED_AT FROM INVENTORY iv JOIN ITEM i ON iv.ITEM_ID = i.ITEM_ID WHERE iv.INVENTORY_ID = ?";
            ps = conn.prepareStatement(sql);
            ps.setInt(1, inventoryId);
            rs = ps.executeQuery();
            if (rs.next()) return mapRow(rs);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try { if (rs != null) rs.close(); } catch (Exception e) {}
            try { if (ps != null) ps.close(); } catch (Exception e) {}
            try { if (conn != null) conn.close(); } catch (Exception e) {}
        }
        return null;
    }

    public int updateInvRegInq(InvRegInqDTO dto) {
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = getConnection();
            String sql = "UPDATE INVENTORY SET SAFETY_STOCK = ?, REMARK = ?, UPDATED_AT = SYSDATE WHERE INVENTORY_ID = ?";
            ps = conn.prepareStatement(sql);
            ps.setDouble(1, dto.getSafetyStock());
            ps.setString(2, dto.getRemark());
            ps.setInt(3, dto.getInventoryId());
            return ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try { if (ps != null) ps.close(); } catch (Exception e) {}
            try { if (conn != null) conn.close(); } catch (Exception e) {}
        }
        return 0;
    }

    public boolean existsInventory(int inventoryId) {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = getConnection();
            ps = conn.prepareStatement("SELECT COUNT(*) FROM INVENTORY WHERE INVENTORY_ID = ?");
            ps.setInt(1, inventoryId);
            rs = ps.executeQuery();
            if (rs.next()) return rs.getInt(1) > 0;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try { if (rs != null) rs.close(); } catch (Exception e) {}
            try { if (ps != null) ps.close(); } catch (Exception e) {}
            try { if (conn != null) conn.close(); } catch (Exception e) {}
        }
        return false;
    }

    private InvRegInqDTO mapRow(ResultSet rs) throws Exception {
        InvRegInqDTO dto = new InvRegInqDTO();
        dto.setInventoryId(rs.getInt("INVENTORY_ID"));
        dto.setItemId(rs.getInt("ITEM_ID"));
        dto.setItemCode(rs.getString("ITEM_CODE"));
        dto.setItemName(rs.getString("ITEM_NAME"));
        dto.setItemType(rs.getString("ITEM_TYPE"));
        dto.setQtyOnHand(rs.getDouble("QTY_ON_HAND"));
        dto.setSafetyStock(rs.getDouble("SAFETY_STOCK"));
        dto.setUnit(rs.getString("UNIT"));
        dto.setRemark(rs.getString("REMARK"));
        dto.setCreatedAt(rs.getDate("CREATED_AT"));
        dto.setUpdatedAt(rs.getDate("UPDATED_AT"));
        dto.setInventoryStatus(dto.getQtyOnHand() < dto.getSafetyStock() ? "부족" : "정상");
        return dto;
    }
}
