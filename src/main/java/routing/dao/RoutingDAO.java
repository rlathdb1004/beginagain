package routing.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import routing.dto.RoutingDTO;

public class RoutingDAO {

    public List<RoutingDTO> selectRoutingListByItemId(Connection conn, int itemId) {
        List<RoutingDTO> list = new ArrayList<RoutingDTO>();
        PreparedStatement ps = null;
        ResultSet rs = null;

        String sql = ""
                + "SELECT R.ROUTING_ID, "
                + "       R.ITEM_ID, "
                + "       R.PROCESS_ID, "
                + "       R.EQUIPMENT_ID, "
                + "       R.PROCESS_SEQ, "
                + "       P.PROCESS_CODE, "
                + "       P.PROCESS_NAME, "
                + "       E.EQUIPMENT_NAME "
                + "FROM ROUTING R "
                + "JOIN PROCESS P ON R.PROCESS_ID = P.PROCESS_ID "
                + "LEFT JOIN EQUIPMENT E ON R.EQUIPMENT_ID = E.EQUIPMENT_ID "
                + "WHERE R.ITEM_ID = ? "
                + "  AND R.USE_YN = 'Y' "
                + "ORDER BY R.PROCESS_SEQ ASC";

        try {
            ps = conn.prepareStatement(sql);
            ps.setInt(1, itemId);
            rs = ps.executeQuery();

            while (rs.next()) {
                RoutingDTO dto = new RoutingDTO();
                dto.setRoutingId(rs.getInt("ROUTING_ID"));
                dto.setItemId(rs.getInt("ITEM_ID"));
                dto.setProcessId(rs.getInt("PROCESS_ID"));
                dto.setEquipmentId(rs.getInt("EQUIPMENT_ID"));
                dto.setProcessSeq(rs.getInt("PROCESS_SEQ"));
                dto.setProcessCode(rs.getString("PROCESS_CODE"));
                dto.setProcessName(rs.getString("PROCESS_NAME"));
                dto.setEquipmentName(rs.getString("EQUIPMENT_NAME"));
                list.add(dto);
            }
        } catch (Exception e) {
            throw new RuntimeException("라우팅 목록 조회 실패", e);
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