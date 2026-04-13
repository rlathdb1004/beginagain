package bom.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import bom.dto.BOMDTO;

public class BOMDAO {

    public List<BOMDTO> selectBomListByItemId(Connection conn, int itemId) {
        List<BOMDTO> list = new ArrayList<BOMDTO>();
        PreparedStatement ps = null;
        ResultSet rs = null;

        String sql = ""
                + "SELECT B.BOM_ID, "
                + "       BD.BOM_DETAIL_ID, "
                + "       B.ITEM_ID AS PARENT_ITEM_ID, "
                + "       BD.ITEM_ID AS CHILD_ITEM_ID, "
                + "       P.ITEM_NAME AS PARENT_ITEM_NAME, "
                + "       C.ITEM_CODE AS CHILD_ITEM_CODE, "
                + "       C.ITEM_NAME AS CHILD_ITEM_NAME, "
                + "       BD.UNIT, "
                + "       BD.QTY_REQUIRED "
                + "FROM BOM B "
                + "JOIN BOM_DETAIL BD ON B.BOM_ID = BD.BOM_ID "
                + "JOIN ITEM P ON B.ITEM_ID = P.ITEM_ID "
                + "JOIN ITEM C ON BD.ITEM_ID = C.ITEM_ID "
                + "WHERE B.ITEM_ID = ? "
                + "  AND B.USE_YN = 'Y' "
                + "  AND BD.USE_YN = 'Y' "
                + "ORDER BY BD.BOM_DETAIL_ID ASC";

        try {
            ps = conn.prepareStatement(sql);
            ps.setInt(1, itemId);
            rs = ps.executeQuery();

            while (rs.next()) {
                BOMDTO dto = new BOMDTO();
                dto.setBomId(rs.getInt("BOM_ID"));
                dto.setBomDetailId(rs.getInt("BOM_DETAIL_ID"));
                dto.setParentItemId(rs.getInt("PARENT_ITEM_ID"));
                dto.setChildItemId(rs.getInt("CHILD_ITEM_ID"));
                dto.setParentItemName(rs.getString("PARENT_ITEM_NAME"));
                dto.setChildItemCode(rs.getString("CHILD_ITEM_CODE"));
                dto.setChildItemName(rs.getString("CHILD_ITEM_NAME"));
                dto.setUnit(rs.getString("UNIT"));
                dto.setRequiredQty(rs.getDouble("QTY_REQUIRED"));
                list.add(dto);
            }
        } catch (Exception e) {
            throw new RuntimeException("BOM 목록 조회 실패", e);
        } finally {
            try {
                if (rs != null) rs.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                if (ps != null) ps.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return list;
    }
}