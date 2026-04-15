package dashboard.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import dashboard.dto.DashboardDTO;

public class DashboardDAO {

    public DashboardDTO selectDashboardData(Connection conn) {
        DashboardDTO dto = new DashboardDTO();

        dto.setItemCount(selectCount(conn, "SELECT COUNT(*) FROM ITEM WHERE USE_YN = 'Y'"));
        dto.setEquipmentCount(selectCount(conn, "SELECT COUNT(*) FROM EQUIPMENT WHERE USE_YN = 'Y'"));
        dto.setProcessCount(selectCount(conn, "SELECT COUNT(*) FROM PROCESS WHERE USE_YN = 'Y'"));
        dto.setWorkOrderCount(selectCount(conn, "SELECT COUNT(*) FROM WORK_ORDER WHERE USE_YN = 'Y'"));
        dto.setMemberCount(selectCount(conn, "SELECT COUNT(*) FROM EMP WHERE USE_YN = 'Y'"));

        return dto;
    }

    private int selectCount(Connection conn, String sql) {
        PreparedStatement ps = null;
        ResultSet rs = null;
        int count = 0;

        try {
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();

            if (rs.next()) {
                count = rs.getInt(1);
            }
        } catch (Exception e) {
            throw new RuntimeException("대시보드 카운트 조회 실패", e);
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

        return count;
    }
}