package maintenance.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import maintenance.dto.MaintenanceDTO;

public class MaintenanceDAO {

    public List<MaintenanceDTO> selectMaintenanceList(Connection conn) {
        List<MaintenanceDTO> list = new ArrayList<MaintenanceDTO>();
        PreparedStatement ps = null;
        ResultSet rs = null;

        String sql = ""
                + "SELECT EM.MAINTENANCE_ID, EM.EQUIPMENT_ID, "
                + "       E.EQUIPMENT_CODE, E.EQUIPMENT_NAME, E.MODEL_NAME, E.LOCATION, "
                + "       EM.MAINTENANCE_DATE, EM.MAINTENANCE_TYPE, EM.MAINTENANCE_CONTENT, "
                + "       EM.NEXT_MAINTENANCE_DATE, EM.STATUS, EM.USE_YN, EM.REMARK, "
                + "       EM.CREATED_AT, EM.UPDATED_AT, "
                + "       (SELECT COUNT(*) FROM EQUIPMENT_FAILURE_ACTION FA WHERE FA.MAINTENANCE_ID = EM.MAINTENANCE_ID) AS FAILURE_ACTION_COUNT "
                + "FROM EQUIPMENT_MAINTENANCE EM "
                + "JOIN EQUIPMENT E ON EM.EQUIPMENT_ID = E.EQUIPMENT_ID "
                + "WHERE EM.USE_YN = 'Y' "
                + "ORDER BY EM.MAINTENANCE_ID DESC";

        try {
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();

            while (rs.next()) {
                list.add(mapRow(rs));
            }
        } catch (Exception e) {
            throw new RuntimeException("정비이력 목록 조회 실패", e);
        } finally {
            try { if (rs != null) rs.close(); } catch (Exception e) { e.printStackTrace(); }
            try { if (ps != null) ps.close(); } catch (Exception e) { e.printStackTrace(); }
        }

        return list;
    }

    public MaintenanceDTO selectMaintenanceById(Connection conn, int maintenanceId) {
        MaintenanceDTO dto = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        String sql = ""
                + "SELECT EM.MAINTENANCE_ID, EM.EQUIPMENT_ID, "
                + "       E.EQUIPMENT_CODE, E.EQUIPMENT_NAME, E.MODEL_NAME, E.LOCATION, "
                + "       EM.MAINTENANCE_DATE, EM.MAINTENANCE_TYPE, EM.MAINTENANCE_CONTENT, "
                + "       EM.NEXT_MAINTENANCE_DATE, EM.STATUS, EM.USE_YN, EM.REMARK, "
                + "       EM.CREATED_AT, EM.UPDATED_AT, "
                + "       (SELECT COUNT(*) FROM EQUIPMENT_FAILURE_ACTION FA WHERE FA.MAINTENANCE_ID = EM.MAINTENANCE_ID) AS FAILURE_ACTION_COUNT "
                + "FROM EQUIPMENT_MAINTENANCE EM "
                + "JOIN EQUIPMENT E ON EM.EQUIPMENT_ID = E.EQUIPMENT_ID "
                + "WHERE EM.MAINTENANCE_ID = ? "
                + "  AND EM.USE_YN = 'Y'";

        try {
            ps = conn.prepareStatement(sql);
            ps.setInt(1, maintenanceId);
            rs = ps.executeQuery();

            if (rs.next()) {
                dto = mapRow(rs);
            }
        } catch (Exception e) {
            throw new RuntimeException("정비이력 상세 조회 실패", e);
        } finally {
            try { if (rs != null) rs.close(); } catch (Exception e) { e.printStackTrace(); }
            try { if (ps != null) ps.close(); } catch (Exception e) { e.printStackTrace(); }
        }

        return dto;
    }

    public boolean existsEquipment(Connection conn, int equipmentId) {
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = conn.prepareStatement("SELECT COUNT(*) FROM EQUIPMENT WHERE EQUIPMENT_ID = ? AND USE_YN = 'Y'");
            ps.setInt(1, equipmentId);
            rs = ps.executeQuery();
            return rs.next() && rs.getInt(1) > 0;
        } catch (Exception e) {
            throw new RuntimeException("설비 존재 여부 확인 실패", e);
        } finally {
            try { if (rs != null) rs.close(); } catch (Exception e) { e.printStackTrace(); }
            try { if (ps != null) ps.close(); } catch (Exception e) { e.printStackTrace(); }
        }
    }

    public int countFailureActionByMaintenanceId(Connection conn, int maintenanceId) {
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = conn.prepareStatement("SELECT COUNT(*) FROM EQUIPMENT_FAILURE_ACTION WHERE MAINTENANCE_ID = ?");
            ps.setInt(1, maintenanceId);
            rs = ps.executeQuery();
            return rs.next() ? rs.getInt(1) : 0;
        } catch (Exception e) {
            throw new RuntimeException("고장조치 연결 여부 확인 실패", e);
        } finally {
            try { if (rs != null) rs.close(); } catch (Exception e) { e.printStackTrace(); }
            try { if (ps != null) ps.close(); } catch (Exception e) { e.printStackTrace(); }
        }
    }

    public int insertMaintenance(Connection conn, MaintenanceDTO dto) {
        PreparedStatement ps = null;

        String sql = ""
                + "INSERT INTO EQUIPMENT_MAINTENANCE ( "
                + "    MAINTENANCE_ID, EQUIPMENT_ID, MAINTENANCE_DATE, MAINTENANCE_TYPE, "
                + "    MAINTENANCE_CONTENT, NEXT_MAINTENANCE_DATE, STATUS, USE_YN, REMARK, "
                + "    CREATED_AT, UPDATED_AT "
                + ") VALUES ( "
                + "    SEQ_EQUIPMENT_MAINTENANCE.NEXTVAL, ?, ?, ?, ?, ?, ?, 'Y', ?, SYSDATE, SYSDATE "
                + ")";

        try {
            ps = conn.prepareStatement(sql);
            ps.setInt(1, dto.getEquipmentId());
            ps.setDate(2, dto.getMaintenanceDate());
            ps.setString(3, dto.getMaintenanceType());
            ps.setString(4, dto.getMaintenanceContent());
            ps.setDate(5, dto.getNextMaintenanceDate());
            ps.setString(6, dto.getStatus());
            ps.setString(7, dto.getRemark());

            return ps.executeUpdate();
        } catch (Exception e) {
            throw new RuntimeException("정비이력 등록 실패", e);
        } finally {
            try { if (ps != null) ps.close(); } catch (Exception e) { e.printStackTrace(); }
        }
    }

    public int updateMaintenance(Connection conn, MaintenanceDTO dto) {
        PreparedStatement ps = null;

        String sql = ""
                + "UPDATE EQUIPMENT_MAINTENANCE "
                + "SET MAINTENANCE_DATE = ?, "
                + "    MAINTENANCE_TYPE = ?, "
                + "    MAINTENANCE_CONTENT = ?, "
                + "    NEXT_MAINTENANCE_DATE = ?, "
                + "    STATUS = ?, "
                + "    REMARK = ?, "
                + "    UPDATED_AT = SYSDATE "
                + "WHERE MAINTENANCE_ID = ? "
                + "  AND USE_YN = 'Y'";

        try {
            ps = conn.prepareStatement(sql);
            ps.setDate(1, dto.getMaintenanceDate());
            ps.setString(2, dto.getMaintenanceType());
            ps.setString(3, dto.getMaintenanceContent());
            ps.setDate(4, dto.getNextMaintenanceDate());
            ps.setString(5, dto.getStatus());
            ps.setString(6, dto.getRemark());
            ps.setInt(7, dto.getMaintenanceId());

            return ps.executeUpdate();
        } catch (Exception e) {
            throw new RuntimeException("정비이력 수정 실패", e);
        } finally {
            try { if (ps != null) ps.close(); } catch (Exception e) { e.printStackTrace(); }
        }
    }

    public int deleteMaintenance(Connection conn, String[] maintenanceIds) {
        PreparedStatement ps = null;
        int result = 0;

        String sql = ""
                + "UPDATE EQUIPMENT_MAINTENANCE "
                + "SET USE_YN = 'N', "
                + "    UPDATED_AT = SYSDATE "
                + "WHERE MAINTENANCE_ID = ?";

        try {
            ps = conn.prepareStatement(sql);

            for (String id : maintenanceIds) {
                ps.setInt(1, Integer.parseInt(id));
                result += ps.executeUpdate();
            }
        } catch (Exception e) {
            throw new RuntimeException("정비이력 삭제 실패", e);
        } finally {
            try { if (ps != null) ps.close(); } catch (Exception e) { e.printStackTrace(); }
        }

        return result;
    }

    public int updateMaintenanceStatus(Connection conn, int maintenanceId, String status) {
        PreparedStatement ps = null;
        try {
            ps = conn.prepareStatement("UPDATE EQUIPMENT_MAINTENANCE SET STATUS = ?, UPDATED_AT = SYSDATE WHERE MAINTENANCE_ID = ? AND USE_YN = 'Y'");
            ps.setString(1, status);
            ps.setInt(2, maintenanceId);
            return ps.executeUpdate();
        } catch (Exception e) {
            throw new RuntimeException("정비이력 상태 수정 실패", e);
        } finally {
            try { if (ps != null) ps.close(); } catch (Exception e) { e.printStackTrace(); }
        }
    }

    private MaintenanceDTO mapRow(ResultSet rs) throws Exception {
        MaintenanceDTO dto = new MaintenanceDTO();
        dto.setMaintenanceId(rs.getInt("MAINTENANCE_ID"));
        dto.setEquipmentId(rs.getInt("EQUIPMENT_ID"));
        dto.setEquipmentCode(rs.getString("EQUIPMENT_CODE"));
        dto.setEquipmentName(rs.getString("EQUIPMENT_NAME"));
        dto.setModelName(rs.getString("MODEL_NAME"));
        dto.setLocation(rs.getString("LOCATION"));
        dto.setMaintenanceDate(rs.getDate("MAINTENANCE_DATE"));
        dto.setMaintenanceType(rs.getString("MAINTENANCE_TYPE"));
        dto.setMaintenanceContent(rs.getString("MAINTENANCE_CONTENT"));
        dto.setNextMaintenanceDate(rs.getDate("NEXT_MAINTENANCE_DATE"));
        dto.setStatus(rs.getString("STATUS"));
        dto.setUseYn(rs.getString("USE_YN"));
        dto.setRemark(rs.getString("REMARK"));
        dto.setCreatedAt(rs.getDate("CREATED_AT"));
        dto.setUpdatedAt(rs.getDate("UPDATED_AT"));
        try { dto.setFailureActionCount(rs.getInt("FAILURE_ACTION_COUNT")); } catch (Exception ignore) {}
        return dto;
    }
}
