package failureaction.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import failureaction.dto.FailureActionDTO;

public class FailureActionDAO {

    public List<FailureActionDTO> selectFailureActionListByMaintenanceId(Connection conn, int maintenanceId) {
        List<FailureActionDTO> list = new ArrayList<FailureActionDTO>();
        PreparedStatement ps = null;
        ResultSet rs = null;

        String sql = ""
                + "SELECT EQUIPMENT_FAILURE_ACTION_ID, MAINTENANCE_ID, FAILURE_DATE, FAILURE_PART, "
                + "       FAILURE_CONTENT, CAUSE_TEXT, ACTION_TEXT, ACTION_DATE, STATUS "
                + "FROM EQUIPMENT_FAILURE_ACTION "
                + "WHERE MAINTENANCE_ID = ? "
                + "ORDER BY EQUIPMENT_FAILURE_ACTION_ID DESC";

        try {
            ps = conn.prepareStatement(sql);
            ps.setInt(1, maintenanceId);
            rs = ps.executeQuery();

            while (rs.next()) {
                FailureActionDTO dto = new FailureActionDTO();
                dto.setFailureActionId(rs.getInt("EQUIPMENT_FAILURE_ACTION_ID"));
                dto.setMaintenanceId(rs.getInt("MAINTENANCE_ID"));
                dto.setFailureDate(rs.getDate("FAILURE_DATE"));
                dto.setFailurePart(rs.getString("FAILURE_PART"));
                dto.setFailureContent(rs.getString("FAILURE_CONTENT"));
                dto.setCauseText(rs.getString("CAUSE_TEXT"));
                dto.setActionText(rs.getString("ACTION_TEXT"));
                dto.setActionDate(rs.getDate("ACTION_DATE"));
                dto.setStatus(rs.getString("STATUS"));
                list.add(dto);
            }
        } catch (Exception e) {
            throw new RuntimeException("고장조치 목록 조회 실패", e);
        } finally {
            try { if (rs != null) rs.close(); } catch (Exception e) { e.printStackTrace(); }
            try { if (ps != null) ps.close(); } catch (Exception e) { e.printStackTrace(); }
        }

        return list;
    }

    public FailureActionDTO selectFailureActionById(Connection conn, int failureActionId) {
        FailureActionDTO dto = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        String sql = ""
                + "SELECT EQUIPMENT_FAILURE_ACTION_ID, MAINTENANCE_ID, FAILURE_DATE, FAILURE_PART, "
                + "       FAILURE_CONTENT, CAUSE_TEXT, ACTION_TEXT, ACTION_DATE, STATUS "
                + "FROM EQUIPMENT_FAILURE_ACTION "
                + "WHERE EQUIPMENT_FAILURE_ACTION_ID = ?";

        try {
            ps = conn.prepareStatement(sql);
            ps.setInt(1, failureActionId);
            rs = ps.executeQuery();

            if (rs.next()) {
                dto = new FailureActionDTO();
                dto.setFailureActionId(rs.getInt("EQUIPMENT_FAILURE_ACTION_ID"));
                dto.setMaintenanceId(rs.getInt("MAINTENANCE_ID"));
                dto.setFailureDate(rs.getDate("FAILURE_DATE"));
                dto.setFailurePart(rs.getString("FAILURE_PART"));
                dto.setFailureContent(rs.getString("FAILURE_CONTENT"));
                dto.setCauseText(rs.getString("CAUSE_TEXT"));
                dto.setActionText(rs.getString("ACTION_TEXT"));
                dto.setActionDate(rs.getDate("ACTION_DATE"));
                dto.setStatus(rs.getString("STATUS"));
            }
        } catch (Exception e) {
            throw new RuntimeException("고장조치 상세 조회 실패", e);
        } finally {
            try { if (rs != null) rs.close(); } catch (Exception e) { e.printStackTrace(); }
            try { if (ps != null) ps.close(); } catch (Exception e) { e.printStackTrace(); }
        }

        return dto;
    }

    public int insertFailureAction(Connection conn, FailureActionDTO dto) {
        PreparedStatement ps = null;

        String sql = ""
                + "INSERT INTO EQUIPMENT_FAILURE_ACTION ( "
                + "    EQUIPMENT_FAILURE_ACTION_ID, "
                + "    MAINTENANCE_ID, FAILURE_DATE, FAILURE_PART, FAILURE_CONTENT, "
                + "    CAUSE_TEXT, ACTION_TEXT, ACTION_DATE, STATUS "
                + ") VALUES ( "
                + "    (SELECT NVL(MAX(EQUIPMENT_FAILURE_ACTION_ID), 0) + 1 FROM EQUIPMENT_FAILURE_ACTION), "
                + "    ?, ?, ?, ?, ?, ?, ?, ? "
                + ")";

        try {
            ps = conn.prepareStatement(sql);
            ps.setInt(1, dto.getMaintenanceId());
            ps.setDate(2, dto.getFailureDate());
            ps.setString(3, dto.getFailurePart());
            ps.setString(4, dto.getFailureContent());
            ps.setString(5, dto.getCauseText());
            ps.setString(6, dto.getActionText());
            ps.setDate(7, dto.getActionDate());
            ps.setString(8, dto.getStatus());

            return ps.executeUpdate();
        } catch (Exception e) {
            throw new RuntimeException("고장조치 등록 실패", e);
        } finally {
            try { if (ps != null) ps.close(); } catch (Exception e) { e.printStackTrace(); }
        }
    }

    public int updateFailureAction(Connection conn, FailureActionDTO dto) {
        PreparedStatement ps = null;

        String sql = ""
                + "UPDATE EQUIPMENT_FAILURE_ACTION "
                + "SET FAILURE_DATE = ?, "
                + "    FAILURE_PART = ?, "
                + "    FAILURE_CONTENT = ?, "
                + "    CAUSE_TEXT = ?, "
                + "    ACTION_TEXT = ?, "
                + "    ACTION_DATE = ?, "
                + "    STATUS = ? "
                + "WHERE EQUIPMENT_FAILURE_ACTION_ID = ?";

        try {
            ps = conn.prepareStatement(sql);
            ps.setDate(1, dto.getFailureDate());
            ps.setString(2, dto.getFailurePart());
            ps.setString(3, dto.getFailureContent());
            ps.setString(4, dto.getCauseText());
            ps.setString(5, dto.getActionText());
            ps.setDate(6, dto.getActionDate());
            ps.setString(7, dto.getStatus());
            ps.setInt(8, dto.getFailureActionId());

            return ps.executeUpdate();
        } catch (Exception e) {
            throw new RuntimeException("고장조치 수정 실패", e);
        } finally {
            try { if (ps != null) ps.close(); } catch (Exception e) { e.printStackTrace(); }
        }
    }

    public int deleteFailureAction(Connection conn, String[] failureActionIds) {
        PreparedStatement ps = null;
        int result = 0;

        String sql = "DELETE FROM EQUIPMENT_FAILURE_ACTION WHERE EQUIPMENT_FAILURE_ACTION_ID = ?";

        try {
            ps = conn.prepareStatement(sql);

            for (String id : failureActionIds) {
                ps.setInt(1, Integer.parseInt(id));
                result += ps.executeUpdate();
            }
        } catch (Exception e) {
            throw new RuntimeException("고장조치 삭제 실패", e);
        } finally {
            try { if (ps != null) ps.close(); } catch (Exception e) { e.printStackTrace(); }
        }

        return result;
    }
}