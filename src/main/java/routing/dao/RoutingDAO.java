package routing.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import routing.dto.RoutingDTO;

public class RoutingDAO {

    public List<RoutingDTO> selectRoutingList(Connection conn) {
        List<RoutingDTO> list = new ArrayList<RoutingDTO>();
        PreparedStatement ps = null;
        ResultSet rs = null;

        String sql = ""
                + "SELECT R.ROUTING_ID, "
                + "       R.ITEM_ID, "
                + "       R.PROCESS_ID, "
                + "       R.EQUIPMENT_ID, "
                + "       R.PROCESS_SEQ, "
                + "       R.REMARK, "
                + "       R.USE_YN, "
                + "       I.ITEM_CODE, "
                + "       I.ITEM_NAME, "
                + "       P.PROCESS_CODE, "
                + "       P.PROCESS_NAME, "
                + "       P.DESCRIPTION AS PROCESS_DESCRIPTION, "
                + "       E.EQUIPMENT_CODE, "
                + "       E.EQUIPMENT_NAME "
                + "FROM ROUTING R "
                + "JOIN ITEM I ON R.ITEM_ID = I.ITEM_ID "
                + "JOIN PROCESS P ON R.PROCESS_ID = P.PROCESS_ID "
                + "LEFT JOIN EQUIPMENT E ON R.EQUIPMENT_ID = E.EQUIPMENT_ID "
                + "WHERE R.USE_YN = 'Y' "
                + "ORDER BY I.ITEM_CODE ASC, R.PROCESS_SEQ ASC, R.ROUTING_ID ASC";

        try {
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();

            while (rs.next()) {
                list.add(mapRouting(rs));
            }
        } catch (Exception e) {
            throw new RuntimeException("라우팅 전체 목록 조회 실패", e);
        } finally {
            close(rs, ps);
        }

        return list;
    }

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
                + "       R.REMARK, "
                + "       R.USE_YN, "
                + "       I.ITEM_CODE, "
                + "       I.ITEM_NAME, "
                + "       P.PROCESS_CODE, "
                + "       P.PROCESS_NAME, "
                + "       P.DESCRIPTION AS PROCESS_DESCRIPTION, "
                + "       E.EQUIPMENT_CODE, "
                + "       E.EQUIPMENT_NAME "
                + "FROM ROUTING R "
                + "JOIN ITEM I ON R.ITEM_ID = I.ITEM_ID "
                + "JOIN PROCESS P ON R.PROCESS_ID = P.PROCESS_ID "
                + "LEFT JOIN EQUIPMENT E ON R.EQUIPMENT_ID = E.EQUIPMENT_ID "
                + "WHERE R.ITEM_ID = ? "
                + "  AND R.USE_YN = 'Y' "
                + "ORDER BY R.PROCESS_SEQ ASC, R.ROUTING_ID ASC";

        try {
            ps = conn.prepareStatement(sql);
            ps.setInt(1, itemId);
            rs = ps.executeQuery();

            while (rs.next()) {
                list.add(mapRouting(rs));
            }
        } catch (Exception e) {
            throw new RuntimeException("라우팅 목록 조회 실패", e);
        } finally {
            close(rs, ps);
        }

        return list;
    }

    public RoutingDTO selectRoutingById(Connection conn, int routingId) {
        PreparedStatement ps = null;
        ResultSet rs = null;
        RoutingDTO dto = null;

        String sql = ""
                + "SELECT R.ROUTING_ID, "
                + "       R.ITEM_ID, "
                + "       R.PROCESS_ID, "
                + "       R.EQUIPMENT_ID, "
                + "       R.PROCESS_SEQ, "
                + "       R.REMARK, "
                + "       R.USE_YN, "
                + "       I.ITEM_CODE, "
                + "       I.ITEM_NAME, "
                + "       P.PROCESS_CODE, "
                + "       P.PROCESS_NAME, "
                + "       P.DESCRIPTION AS PROCESS_DESCRIPTION, "
                + "       E.EQUIPMENT_CODE, "
                + "       E.EQUIPMENT_NAME "
                + "FROM ROUTING R "
                + "JOIN ITEM I ON R.ITEM_ID = I.ITEM_ID "
                + "JOIN PROCESS P ON R.PROCESS_ID = P.PROCESS_ID "
                + "LEFT JOIN EQUIPMENT E ON R.EQUIPMENT_ID = E.EQUIPMENT_ID "
                + "WHERE R.ROUTING_ID = ? "
                + "  AND R.USE_YN = 'Y'";

        try {
            ps = conn.prepareStatement(sql);
            ps.setInt(1, routingId);
            rs = ps.executeQuery();

            if (rs.next()) {
                dto = mapRouting(rs);
            }
        } catch (Exception e) {
            throw new RuntimeException("라우팅 상세 조회 실패", e);
        } finally {
            close(rs, ps);
        }

        return dto;
    }

    public boolean existsFinishedItem(Connection conn, int itemId) {
        return existsByCount(conn, "SELECT COUNT(*) FROM ITEM WHERE ITEM_ID = ? AND ITEM_TYPE = '완제품' AND NVL(USE_YN,'Y')='Y'", itemId);
    }

    public boolean existsProcess(Connection conn, int processId) {
        return existsByCount(conn, "SELECT COUNT(*) FROM PROCESS WHERE PROCESS_ID = ? AND NVL(USE_YN,'Y')='Y'", processId);
    }

    public boolean existsEquipment(Connection conn, int equipmentId) {
        return existsByCount(conn, "SELECT COUNT(*) FROM EQUIPMENT WHERE EQUIPMENT_ID = ?", equipmentId);
    }

    public boolean existsSeqDuplicate(Connection conn, int itemId, int processSeq, Integer excludeRoutingId) {
        String sql = "SELECT COUNT(*) FROM ROUTING WHERE ITEM_ID = ? AND PROCESS_SEQ = ? AND USE_YN = 'Y'";
        if (excludeRoutingId != null) sql += " AND ROUTING_ID <> ?";
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = conn.prepareStatement(sql);
            ps.setInt(1, itemId);
            ps.setInt(2, processSeq);
            if (excludeRoutingId != null) ps.setInt(3, excludeRoutingId);
            rs = ps.executeQuery();
            return rs.next() && rs.getInt(1) > 0;
        } catch (Exception e) {
            throw new RuntimeException("라우팅 순서 중복 확인 실패", e);
        } finally { close(rs, ps); }
    }

    public boolean existsProcessEquipmentDuplicate(Connection conn, int itemId, int processId, int equipmentId, Integer excludeRoutingId) {
        String sql = "SELECT COUNT(*) FROM ROUTING WHERE ITEM_ID = ? AND PROCESS_ID = ? AND EQUIPMENT_ID = ? AND USE_YN = 'Y'";
        if (excludeRoutingId != null) sql += " AND ROUTING_ID <> ?";
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = conn.prepareStatement(sql);
            ps.setInt(1, itemId);
            ps.setInt(2, processId);
            ps.setInt(3, equipmentId);
            if (excludeRoutingId != null) ps.setInt(4, excludeRoutingId);
            rs = ps.executeQuery();
            return rs.next() && rs.getInt(1) > 0;
        } catch (Exception e) {
            throw new RuntimeException("라우팅 공정/설비 중복 확인 실패", e);
        } finally { close(rs, ps); }
    }

    public int insertRouting(Connection conn, RoutingDTO dto) {
        PreparedStatement ps = null;
        int result = 0;

        String sql = ""
                + "INSERT INTO ROUTING ("
                + "ROUTING_ID, ITEM_ID, PROCESS_ID, PROCESS_SEQ, EQUIPMENT_ID, USE_YN, REMARK, CREATED_AT, UPDATED_AT"
                + ") VALUES ("
                + "SEQ_ROUTING.NEXTVAL, ?, ?, ?, ?, 'Y', ?, SYSDATE, SYSDATE"
                + ")";

        try {
            ps = conn.prepareStatement(sql);
            ps.setInt(1, dto.getItemId());
            ps.setInt(2, dto.getProcessId());
            ps.setInt(3, dto.getProcessSeq());
            ps.setInt(4, dto.getEquipmentId());
            ps.setString(5, dto.getRemark());
            result = ps.executeUpdate();
        } catch (Exception e) {
            throw new RuntimeException("라우팅 등록 실패", e);
        } finally {
            close(ps);
        }

        return result;
    }

    public int updateRouting(Connection conn, RoutingDTO dto) {
        PreparedStatement ps = null;
        int result = 0;

        String sql = ""
                + "UPDATE ROUTING "
                + "SET PROCESS_SEQ = ?, "
                + "    REMARK = ?, "
                + "    UPDATED_AT = SYSDATE "
                + "WHERE ROUTING_ID = ?";

        try {
            ps = conn.prepareStatement(sql);
            ps.setInt(1, dto.getProcessSeq());
            ps.setString(2, dto.getRemark());
            ps.setInt(3, dto.getRoutingId());
            result = ps.executeUpdate();
        } catch (Exception e) {
            throw new RuntimeException("라우팅 수정 실패", e);
        } finally {
            close(ps);
        }

        return result;
    }

    public int deleteRouting(Connection conn, String[] routingIds) {
        PreparedStatement ps = null;
        int result = 0;

        String sql = ""
                + "UPDATE ROUTING "
                + "SET USE_YN = 'N', "
                + "    UPDATED_AT = SYSDATE "
                + "WHERE ROUTING_ID = ?";

        try {
            ps = conn.prepareStatement(sql);
            for (int i = 0; i < routingIds.length; i++) {
                ps.setInt(1, Integer.parseInt(routingIds[i]));
                result += ps.executeUpdate();
            }
        } catch (Exception e) {
            throw new RuntimeException("라우팅 삭제 실패", e);
        } finally {
            close(ps);
        }

        return result;
    }

    private boolean existsByCount(Connection conn, String sql, int id) {
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = conn.prepareStatement(sql);
            ps.setInt(1, id);
            rs = ps.executeQuery();
            return rs.next() && rs.getInt(1) > 0;
        } catch (Exception e) {
            throw new RuntimeException("기준정보 확인 실패", e);
        } finally { close(rs, ps); }
    }

    private RoutingDTO mapRouting(ResultSet rs) throws Exception {
        RoutingDTO dto = new RoutingDTO();
        dto.setRoutingId(rs.getInt("ROUTING_ID"));
        dto.setItemId(rs.getInt("ITEM_ID"));
        dto.setProcessId(rs.getInt("PROCESS_ID"));
        dto.setEquipmentId(rs.getInt("EQUIPMENT_ID"));
        dto.setProcessSeq(rs.getInt("PROCESS_SEQ"));
        dto.setRemark(rs.getString("REMARK"));
        dto.setUseYn(rs.getString("USE_YN"));
        dto.setItemCode(rs.getString("ITEM_CODE"));
        dto.setItemName(rs.getString("ITEM_NAME"));
        dto.setProcessCode(rs.getString("PROCESS_CODE"));
        dto.setProcessName(rs.getString("PROCESS_NAME"));
        dto.setProcessDescription(rs.getString("PROCESS_DESCRIPTION"));
        dto.setEquipmentCode(rs.getString("EQUIPMENT_CODE"));
        dto.setEquipmentName(rs.getString("EQUIPMENT_NAME"));
        return dto;
    }

    private void close(ResultSet rs, PreparedStatement ps) {
        try {
            if (rs != null) rs.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        close(ps);
    }

    private void close(PreparedStatement ps) {
        try {
            if (ps != null) ps.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
