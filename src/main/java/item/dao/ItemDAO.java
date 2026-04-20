package item.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import item.dto.ItemDTO;

public class ItemDAO {

    public List<ItemDTO> selectItemList(Connection conn) {
        List<ItemDTO> list = new ArrayList<ItemDTO>();
        PreparedStatement ps = null;
        ResultSet rs = null;

        String sql = ""
                + "SELECT ITEM_ID, ITEM_CODE, ITEM_NAME, ITEM_TYPE, UNIT, SPEC, "
                + "       SUPPLIER_NAME, SAFETY_STOCK, USE_YN, REMARK, CREATED_AT, UPDATED_AT "
                + "FROM ITEM "
                + "WHERE USE_YN = 'Y' "
                + "ORDER BY ITEM_ID DESC";

        try {
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();

            while (rs.next()) {
                ItemDTO dto = new ItemDTO();
                dto.setItemId(rs.getInt("ITEM_ID"));
                dto.setItemCode(rs.getString("ITEM_CODE"));
                dto.setItemName(rs.getString("ITEM_NAME"));
                dto.setItemType(rs.getString("ITEM_TYPE"));
                dto.setUnit(rs.getString("UNIT"));
                dto.setSpec(rs.getString("SPEC"));
                dto.setSupplierName(rs.getString("SUPPLIER_NAME"));
                dto.setSafetyStock(rs.getDouble("SAFETY_STOCK"));
                dto.setUseYn(rs.getString("USE_YN"));
                dto.setRemark(rs.getString("REMARK"));
                dto.setCreatedAt(rs.getDate("CREATED_AT"));
                dto.setUpdatedAt(rs.getDate("UPDATED_AT"));

                list.add(dto);
            }
        } catch (Exception e) {
            throw new RuntimeException("품목 목록 조회 실패", e);
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

    public ItemDTO selectItemById(Connection conn, int itemId) {
        ItemDTO dto = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        String sql = ""
                + "SELECT ITEM_ID, ITEM_CODE, ITEM_NAME, ITEM_TYPE, UNIT, SPEC, "
                + "       SUPPLIER_NAME, SAFETY_STOCK, USE_YN, REMARK, CREATED_AT, UPDATED_AT "
                + "FROM ITEM "
                + "WHERE ITEM_ID = ? "
                + "  AND USE_YN = 'Y'";

        try {
            ps = conn.prepareStatement(sql);
            ps.setInt(1, itemId);
            rs = ps.executeQuery();

            if (rs.next()) {
                dto = new ItemDTO();
                dto.setItemId(rs.getInt("ITEM_ID"));
                dto.setItemCode(rs.getString("ITEM_CODE"));
                dto.setItemName(rs.getString("ITEM_NAME"));
                dto.setItemType(rs.getString("ITEM_TYPE"));
                dto.setUnit(rs.getString("UNIT"));
                dto.setSpec(rs.getString("SPEC"));
                dto.setSupplierName(rs.getString("SUPPLIER_NAME"));
                dto.setSafetyStock(rs.getDouble("SAFETY_STOCK"));
                dto.setUseYn(rs.getString("USE_YN"));
                dto.setRemark(rs.getString("REMARK"));
                dto.setCreatedAt(rs.getDate("CREATED_AT"));
                dto.setUpdatedAt(rs.getDate("UPDATED_AT"));
            }
        } catch (Exception e) {
            throw new RuntimeException("품목 상세 조회 실패", e);
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

        return dto;
    }

    public int updateItem(Connection conn, ItemDTO dto) {
        PreparedStatement ps = null;
        int result = 0;

        String sql = ""
                + "UPDATE ITEM "
                + "SET ITEM_NAME = ?, "
                + "    ITEM_TYPE = ?, "
                + "    UNIT = ?, "
                + "    SPEC = ?, "
                + "    SUPPLIER_NAME = ?, "
                + "    SAFETY_STOCK = ?, "
                + "    REMARK = ?, "
                + "    UPDATED_AT = SYSDATE "
                + "WHERE ITEM_ID = ?";

        try {
            ps = conn.prepareStatement(sql);
            ps.setString(1, dto.getItemName());
            ps.setString(2, dto.getItemType());
            ps.setString(3, dto.getUnit());
            ps.setString(4, dto.getSpec());
            ps.setString(5, dto.getSupplierName());
            ps.setDouble(6, dto.getSafetyStock());
            ps.setString(7, dto.getRemark());
            ps.setInt(8, dto.getItemId());

            result = ps.executeUpdate();
        } catch (Exception e) {
            throw new RuntimeException("품목 수정 실패", e);
        } finally {
            try {
                if (ps != null) ps.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return result;
    }

    public int deleteItems(Connection conn, String[] itemIds) {
        PreparedStatement ps = null;
        int result = 0;

        String sql = ""
                + "UPDATE ITEM "
                + "SET USE_YN = 'N', "
                + "    UPDATED_AT = SYSDATE "
                + "WHERE ITEM_ID = ?";

        try {
            ps = conn.prepareStatement(sql);

            for (int i = 0; i < itemIds.length; i++) {
                ps.setInt(1, Integer.parseInt(itemIds[i]));
                result += ps.executeUpdate();
            }
        } catch (Exception e) {
            throw new RuntimeException("품목 삭제 실패", e);
        } finally {
            try {
                if (ps != null) ps.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return result;
    }

public int insertItem(Connection conn, ItemDTO dto) {
    PreparedStatement ps = null;
    int result = 0;
    int itemId = getNextSequenceValue(conn, "SEQ_ITEM");
    String itemCode = buildCode("ITEM", itemId);
    String sql = "INSERT INTO ITEM (ITEM_ID, ITEM_CODE, ITEM_NAME, ITEM_TYPE, UNIT, SPEC, SUPPLIER_NAME, SAFETY_STOCK, USE_YN, REMARK, CREATED_AT, UPDATED_AT) "
            + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, 'Y', ?, SYSDATE, SYSDATE)";
    try {
        ps = conn.prepareStatement(sql);
        ps.setInt(1, itemId);
        ps.setString(2, itemCode);
        ps.setString(3, dto.getItemName());
        ps.setString(4, dto.getItemType());
        ps.setString(5, dto.getUnit());
        ps.setString(6, dto.getSpec());
        ps.setString(7, dto.getSupplierName());
        ps.setDouble(8, dto.getSafetyStock());
        ps.setString(9, dto.getRemark());
        result = ps.executeUpdate();
    } catch (Exception e) {
        throw new RuntimeException("품목 등록 실패", e);
    } finally {
        try { if (ps != null) ps.close(); } catch (Exception e) { e.printStackTrace(); }
    }
    return result;
}

private int getNextSequenceValue(Connection conn, String sequenceName) {
    PreparedStatement ps = null;
    ResultSet rs = null;
    String sql = "SELECT " + sequenceName + ".NEXTVAL FROM DUAL";
    try {
        ps = conn.prepareStatement(sql);
        rs = ps.executeQuery();
        if (rs.next()) {
            return rs.getInt(1);
        }
        throw new RuntimeException("시퀀스 값을 가져오지 못했습니다: " + sequenceName);
    } catch (Exception e) {
        throw new RuntimeException("시퀀스 조회 실패: " + sequenceName, e);
    } finally {
        try { if (rs != null) rs.close(); } catch (Exception e) { e.printStackTrace(); }
        try { if (ps != null) ps.close(); } catch (Exception e) { e.printStackTrace(); }
    }
}

private String buildCode(String prefix, int seqValue) {
    return prefix + String.format("%04d", seqValue);
}

}