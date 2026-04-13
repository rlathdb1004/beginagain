package equipment.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import equipment.dto.EquipmentDTO;

public class EquipmentDAO {

    public List<EquipmentDTO> selectEquipmentList(Connection conn) {
        List<EquipmentDTO> list = new ArrayList<EquipmentDTO>();
        PreparedStatement ps = null;
        ResultSet rs = null;

        String sql = ""
                + "SELECT EQUIPMENT_ID, EQUIPMENT_CODE, EQUIPMENT_NAME, MODEL_NAME, LOCATION, "
                + "       MANUFACTURER, VENDOR_NAME, EQUIPMENT_PRICE, PURCHASE_DATE, "
                + "       USE_YN, REMARK, CREATED_AT, UPDATED_AT "
                + "FROM EQUIPMENT "
                + "WHERE USE_YN = 'Y' "
                + "ORDER BY EQUIPMENT_ID DESC";

        try {
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();

            while (rs.next()) {
                EquipmentDTO dto = new EquipmentDTO();
                dto.setEquipmentId(rs.getInt("EQUIPMENT_ID"));
                dto.setEquipmentCode(rs.getString("EQUIPMENT_CODE"));
                dto.setEquipmentName(rs.getString("EQUIPMENT_NAME"));
                dto.setModelName(rs.getString("MODEL_NAME"));
                dto.setLocation(rs.getString("LOCATION"));
                dto.setManufacturer(rs.getString("MANUFACTURER"));
                dto.setVendorName(rs.getString("VENDOR_NAME"));
                dto.setEquipmentPrice(rs.getDouble("EQUIPMENT_PRICE"));
                dto.setPurchaseDate(rs.getDate("PURCHASE_DATE"));
                dto.setUseYn(rs.getString("USE_YN"));
                dto.setRemark(rs.getString("REMARK"));
                dto.setCreatedAt(rs.getDate("CREATED_AT"));
                dto.setUpdatedAt(rs.getDate("UPDATED_AT"));

                list.add(dto);
            }
        } catch (Exception e) {
            throw new RuntimeException("설비 목록 조회 실패", e);
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

    public EquipmentDTO selectEquipmentById(Connection conn, int equipmentId) {
        EquipmentDTO dto = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        String sql = ""
                + "SELECT EQUIPMENT_ID, EQUIPMENT_CODE, EQUIPMENT_NAME, MODEL_NAME, LOCATION, "
                + "       MANUFACTURER, VENDOR_NAME, EQUIPMENT_PRICE, PURCHASE_DATE, "
                + "       USE_YN, REMARK, CREATED_AT, UPDATED_AT "
                + "FROM EQUIPMENT "
                + "WHERE EQUIPMENT_ID = ? "
                + "  AND USE_YN = 'Y'";

        try {
            ps = conn.prepareStatement(sql);
            ps.setInt(1, equipmentId);
            rs = ps.executeQuery();

            if (rs.next()) {
                dto = new EquipmentDTO();
                dto.setEquipmentId(rs.getInt("EQUIPMENT_ID"));
                dto.setEquipmentCode(rs.getString("EQUIPMENT_CODE"));
                dto.setEquipmentName(rs.getString("EQUIPMENT_NAME"));
                dto.setModelName(rs.getString("MODEL_NAME"));
                dto.setLocation(rs.getString("LOCATION"));
                dto.setManufacturer(rs.getString("MANUFACTURER"));
                dto.setVendorName(rs.getString("VENDOR_NAME"));
                dto.setEquipmentPrice(rs.getDouble("EQUIPMENT_PRICE"));
                dto.setPurchaseDate(rs.getDate("PURCHASE_DATE"));
                dto.setUseYn(rs.getString("USE_YN"));
                dto.setRemark(rs.getString("REMARK"));
                dto.setCreatedAt(rs.getDate("CREATED_AT"));
                dto.setUpdatedAt(rs.getDate("UPDATED_AT"));
            }
        } catch (Exception e) {
            throw new RuntimeException("설비 상세 조회 실패", e);
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

    public int updateEquipment(Connection conn, EquipmentDTO dto) {
        PreparedStatement ps = null;
        int result = 0;

        String sql = ""
                + "UPDATE EQUIPMENT "
                + "SET EQUIPMENT_NAME = ?, "
                + "    MODEL_NAME = ?, "
                + "    LOCATION = ?, "
                + "    MANUFACTURER = ?, "
                + "    VENDOR_NAME = ?, "
                + "    EQUIPMENT_PRICE = ?, "
                + "    PURCHASE_DATE = ?, "
                + "    REMARK = ?, "
                + "    UPDATED_AT = SYSDATE "
                + "WHERE EQUIPMENT_ID = ?";

        try {
            ps = conn.prepareStatement(sql);
            ps.setString(1, dto.getEquipmentName());
            ps.setString(2, dto.getModelName());
            ps.setString(3, dto.getLocation());
            ps.setString(4, dto.getManufacturer());
            ps.setString(5, dto.getVendorName());
            ps.setDouble(6, dto.getEquipmentPrice());
            ps.setDate(7, dto.getPurchaseDate());
            ps.setString(8, dto.getRemark());
            ps.setInt(9, dto.getEquipmentId());

            result = ps.executeUpdate();
        } catch (Exception e) {
            throw new RuntimeException("설비 수정 실패", e);
        } finally {
            try {
                if (ps != null) ps.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return result;
    }

    public int deleteEquipment(Connection conn, String[] equipmentIds) {
        PreparedStatement ps = null;
        int result = 0;

        String sql = ""
                + "UPDATE EQUIPMENT "
                + "SET USE_YN = 'N', "
                + "    UPDATED_AT = SYSDATE "
                + "WHERE EQUIPMENT_ID = ?";

        try {
            ps = conn.prepareStatement(sql);

            for (int i = 0; i < equipmentIds.length; i++) {
                ps.setInt(1, Integer.parseInt(equipmentIds[i]));
                result += ps.executeUpdate();
            }
        } catch (Exception e) {
            throw new RuntimeException("설비 삭제 실패", e);
        } finally {
            try {
                if (ps != null) ps.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return result;
    }

public int insertEquipment(Connection conn, EquipmentDTO dto) {
    PreparedStatement ps = null;
    int result = 0;
    String sql = "INSERT INTO EQUIPMENT (EQUIPMENT_ID, EQUIPMENT_CODE, EQUIPMENT_NAME, MODEL_NAME, LOCATION, MANUFACTURER, VENDOR_NAME, EQUIPMENT_PRICE, PURCHASE_DATE, USE_YN, REMARK, CREATED_AT, UPDATED_AT) "
            + "VALUES (SEQ_EQUIPMENT.NEXTVAL, ?, ?, ?, ?, ?, ?, ?, ?, 'Y', ?, SYSDATE, SYSDATE)";
    try {
        ps = conn.prepareStatement(sql);
        ps.setString(1, dto.getEquipmentCode());
        ps.setString(2, dto.getEquipmentName());
        ps.setString(3, dto.getModelName());
        ps.setString(4, dto.getLocation());
        ps.setString(5, dto.getManufacturer());
        ps.setString(6, dto.getVendorName());
        ps.setDouble(7, dto.getEquipmentPrice());
        ps.setDate(8, dto.getPurchaseDate());
        ps.setString(9, dto.getRemark());
        result = ps.executeUpdate();
    } catch (Exception e) {
        throw new RuntimeException("설비 등록 실패", e);
    } finally {
        try { if (ps != null) ps.close(); } catch (Exception e) { e.printStackTrace(); }
    }
    return result;
}

}