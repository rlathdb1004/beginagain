package downtime.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import downtime.dto.DowntimeDTO;

public class DowntimeDAO {

    public List<DowntimeDTO> selectDowntimeList(Connection conn) {
        List<DowntimeDTO> list = new ArrayList<DowntimeDTO>();
        PreparedStatement ps = null;
        ResultSet rs = null;

        String sql = ""
                + "SELECT FA.EQUIPMENT_FAILURE_ACTION_ID, FA.MAINTENANCE_ID, "
                + "       M.EQUIPMENT_ID, M.MAINTENANCE_TYPE, "
                + "       E.EQUIPMENT_CODE, E.EQUIPMENT_NAME, E.MODEL_NAME, E.LOCATION, "
                + "       FA.FAILURE_DATE, FA.FAILURE_PART, FA.FAILURE_CONTENT, "
                + "       FA.CAUSE_TEXT, FA.ACTION_TEXT, FA.ACTION_DATE, FA.STATUS, FA.REMARK "
                + "FROM EQUIPMENT_FAILURE_ACTION FA "
                + "JOIN EQUIPMENT_MAINTENANCE M ON FA.MAINTENANCE_ID = M.MAINTENANCE_ID "
                + "JOIN EQUIPMENT E ON M.EQUIPMENT_ID = E.EQUIPMENT_ID "
                + "ORDER BY FA.FAILURE_DATE DESC, FA.EQUIPMENT_FAILURE_ACTION_ID DESC";

        try {
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();

            while (rs.next()) {
                DowntimeDTO dto = new DowntimeDTO();
                dto.setFailureActionId(rs.getInt("EQUIPMENT_FAILURE_ACTION_ID"));
                dto.setMaintenanceId(rs.getInt("MAINTENANCE_ID"));
                dto.setEquipmentId(rs.getInt("EQUIPMENT_ID"));
                dto.setMaintenanceType(rs.getString("MAINTENANCE_TYPE"));
                dto.setEquipmentCode(rs.getString("EQUIPMENT_CODE"));
                dto.setEquipmentName(rs.getString("EQUIPMENT_NAME"));
                dto.setModelName(rs.getString("MODEL_NAME"));
                dto.setLocation(rs.getString("LOCATION"));
                dto.setFailureDate(rs.getDate("FAILURE_DATE"));
                dto.setFailurePart(rs.getString("FAILURE_PART"));
                dto.setFailureContent(rs.getString("FAILURE_CONTENT"));
                dto.setCauseText(rs.getString("CAUSE_TEXT"));
                dto.setActionText(rs.getString("ACTION_TEXT"));
                dto.setActionDate(rs.getDate("ACTION_DATE"));
                dto.setStatus(rs.getString("STATUS"));
                dto.setRemark(rs.getString("REMARK"));
                list.add(dto);
            }
        } catch (Exception e) {
            throw new RuntimeException("비가동 현황 목록 조회 실패", e);
        } finally {
            try { if (rs != null) rs.close(); } catch (Exception e) { e.printStackTrace(); }
            try { if (ps != null) ps.close(); } catch (Exception e) { e.printStackTrace(); }
        }

        return list;
    }

    public DowntimeDTO selectDowntimeById(Connection conn, int failureActionId) {
        DowntimeDTO dto = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        String sql = ""
                + "SELECT FA.EQUIPMENT_FAILURE_ACTION_ID, FA.MAINTENANCE_ID, "
                + "       M.EQUIPMENT_ID, M.MAINTENANCE_TYPE, M.MAINTENANCE_DATE, "
                + "       E.EQUIPMENT_CODE, E.EQUIPMENT_NAME, E.MODEL_NAME, E.LOCATION, "
                + "       FA.FAILURE_DATE, FA.FAILURE_PART, FA.FAILURE_CONTENT, "
                + "       FA.CAUSE_TEXT, FA.ACTION_TEXT, FA.ACTION_DATE, FA.STATUS, FA.REMARK "
                + "FROM EQUIPMENT_FAILURE_ACTION FA "
                + "JOIN EQUIPMENT_MAINTENANCE M ON FA.MAINTENANCE_ID = M.MAINTENANCE_ID "
                + "JOIN EQUIPMENT E ON M.EQUIPMENT_ID = E.EQUIPMENT_ID "
                + "WHERE FA.EQUIPMENT_FAILURE_ACTION_ID = ?";

        try {
            ps = conn.prepareStatement(sql);
            ps.setInt(1, failureActionId);
            rs = ps.executeQuery();

            if (rs.next()) {
                dto = new DowntimeDTO();
                dto.setFailureActionId(rs.getInt("EQUIPMENT_FAILURE_ACTION_ID"));
                dto.setMaintenanceId(rs.getInt("MAINTENANCE_ID"));
                dto.setEquipmentId(rs.getInt("EQUIPMENT_ID"));
                dto.setMaintenanceType(rs.getString("MAINTENANCE_TYPE"));
                dto.setEquipmentCode(rs.getString("EQUIPMENT_CODE"));
                dto.setEquipmentName(rs.getString("EQUIPMENT_NAME"));
                dto.setModelName(rs.getString("MODEL_NAME"));
                dto.setLocation(rs.getString("LOCATION"));
                dto.setFailureDate(rs.getDate("FAILURE_DATE"));
                dto.setFailurePart(rs.getString("FAILURE_PART"));
                dto.setFailureContent(rs.getString("FAILURE_CONTENT"));
                dto.setCauseText(rs.getString("CAUSE_TEXT"));
                dto.setActionText(rs.getString("ACTION_TEXT"));
                dto.setActionDate(rs.getDate("ACTION_DATE"));
                dto.setStatus(rs.getString("STATUS"));
                dto.setRemark(rs.getString("REMARK"));
            }
        } catch (Exception e) {
            throw new RuntimeException("비가동 현황 상세 조회 실패", e);
        } finally {
            try { if (rs != null) rs.close(); } catch (Exception e) { e.printStackTrace(); }
            try { if (ps != null) ps.close(); } catch (Exception e) { e.printStackTrace(); }
        }

        return dto;
    }
}