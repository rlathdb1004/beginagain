package MasterDataMgmt.BOMManagement.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

import MasterDataMgmt.ItemMgmt.ItemMgmtDTO;

public class BOMMgmtChangeDAO {
    public int findItemIdByCode(String itemCode) {

        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        int itemId = 0;

        try {

            Context ctx = new InitialContext();
            DataSource ds = (DataSource) ctx.lookup("java:comp/env/jdbc/oracle");

            conn = ds.getConnection();

            String sql = "SELECT ITEM_ID FROM ITEM WHERE ITEM_CODE = ?";

            ps = conn.prepareStatement(sql);

            ps.setString(1, itemCode);

            rs = ps.executeQuery();

            if (rs.next()) {
                itemId = rs.getInt("ITEM_ID");
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try { if (rs != null) rs.close(); } catch (Exception e) {}
            try { if (ps != null) ps.close(); } catch (Exception e) {}
            try { if (conn != null) conn.close(); } catch (Exception e) {}
        }

        return itemId;
    }

    
    public ItemMgmtDTO getItemById(int itemId) {

        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        ItemMgmtDTO dto = null;

        try {

            Context ctx = new InitialContext();
            DataSource ds = (DataSource) ctx.lookup("java:comp/env/jdbc/oracle");

            conn = ds.getConnection();

            String sql = "SELECT ITEM_ID, ITEM_CODE, ITEM_NAME, UNIT "
                       + "FROM ITEM WHERE ITEM_ID = ?";

            ps = conn.prepareStatement(sql);

            ps.setInt(1, itemId);

            rs = ps.executeQuery();

            if (rs.next()) {

                dto = new ItemMgmtDTO();

                dto.setItem_id(rs.getInt("ITEM_ID"));
                dto.setItem_code(rs.getString("ITEM_CODE"));
                dto.setItem_name(rs.getString("ITEM_NAME"));
                dto.setUnit(rs.getString("UNIT"));
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try { if (rs != null) rs.close(); } catch (Exception e) {}
            try { if (ps != null) ps.close(); } catch (Exception e) {}
            try { if (conn != null) conn.close(); } catch (Exception e) {}
        }

        return dto;
    }
}