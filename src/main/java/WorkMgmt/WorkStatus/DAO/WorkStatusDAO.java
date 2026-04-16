package WorkMgmt.WorkStatus.DAO;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import WorkMgmt.WorkStatus.DTO.*;

public class WorkStatusDAO {

    public List<WorkStatusDTO> selectWorkStatusList(Connection conn) {
        List<WorkStatusDTO> list = new ArrayList<WorkStatusDTO>();
        PreparedStatement ps = null;
        ResultSet rs = null;

        String sql = ""
                + "SELECT WO.WORK_ORDER_ID, WO.WORK_DATE, WO.WORK_QTY, WO.STATUS, WO.REMARK, "
                + "       I.ITEM_CODE, I.ITEM_NAME, E.EMP_NAME, "
                + "       NVL(SUM(CASE WHEN PR.USE_YN = 'Y' THEN PR.PRODUCED_QTY ELSE 0 END), 0) AS PRODUCED_QTY, "
                + "       NVL(SUM(CASE WHEN PR.USE_YN = 'Y' THEN PR.LOSS_QTY ELSE 0 END), 0) AS LOSS_QTY, "
                + "       MAX(CASE WHEN PR.USE_YN = 'Y' THEN PR.RESULT_DATE END) AS LAST_RESULT_DATE "
                + "FROM WORK_ORDER WO "
                + "JOIN ITEM I ON WO.ITEM_ID = I.ITEM_ID "
                + "JOIN EMP E ON WO.EMP_ID = E.EMP_ID "
                + "LEFT JOIN PRODUCTION_RESULT PR ON WO.WORK_ORDER_ID = PR.WORK_ORDER_ID "
                + "WHERE WO.USE_YN = 'Y' "
                + "GROUP BY WO.WORK_ORDER_ID, WO.WORK_DATE, WO.WORK_QTY, WO.STATUS, WO.REMARK, "
                + "         I.ITEM_CODE, I.ITEM_NAME, E.EMP_NAME "
                + "ORDER BY WO.WORK_DATE DESC, WO.WORK_ORDER_ID DESC";

        try {
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();

            while (rs.next()) {
                WorkStatusDTO dto = new WorkStatusDTO();
                int id = rs.getInt("WORK_ORDER_ID");
                Date workDate = rs.getDate("WORK_DATE");
                String displayCode;
                if (workDate != null) {
                    displayCode = "WO-" + new SimpleDateFormat("yyyyMMdd").format(workDate) + "-" + String.format("%05d", id);
                } else {
                    displayCode = "WO-UNKNOWN-" + String.format("%05d", id);
                }
                dto.setWorkOrderId(id);
                dto.setWorkOrderDisplayCode(displayCode);
                dto.setWorkDate(workDate);
                dto.setWorkQty(rs.getDouble("WORK_QTY"));
                dto.setWorkOrderStatus(rs.getString("STATUS"));
                dto.setRemark(rs.getString("REMARK"));
                dto.setItemCode(rs.getString("ITEM_CODE"));
                dto.setItemName(rs.getString("ITEM_NAME"));
                dto.setEmpName(rs.getString("EMP_NAME"));
                dto.setProducedQty(rs.getDouble("PRODUCED_QTY"));
                dto.setLossQty(rs.getDouble("LOSS_QTY"));
                dto.setLastResultDate(rs.getDate("LAST_RESULT_DATE"));
                list.add(dto);
            }
        } catch (Exception e) {
            throw new RuntimeException("작업 현황 조회 실패", e);
        } finally {
            try { if (rs != null) rs.close(); } catch (Exception e) { e.printStackTrace(); }
            try { if (ps != null) ps.close(); } catch (Exception e) { e.printStackTrace(); }
        }

        return list;
    }
    
    public WorkStatusDTO selectWorkStatusById(Connection conn, int workOrderId) {
        WorkStatusDTO dto = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        String sql = ""
                + "SELECT WO.WORK_ORDER_ID, WO.WORK_DATE, WO.WORK_QTY, WO.STATUS, WO.REMARK, "
                + "       I.ITEM_CODE, I.ITEM_NAME, E.EMP_NAME, "
                + "       NVL(SUM(CASE WHEN PR.USE_YN = 'Y' THEN PR.PRODUCED_QTY ELSE 0 END), 0) AS PRODUCED_QTY, "
                + "       NVL(SUM(CASE WHEN PR.USE_YN = 'Y' THEN PR.LOSS_QTY ELSE 0 END), 0) AS LOSS_QTY, "
                + "       MAX(CASE WHEN PR.USE_YN = 'Y' THEN PR.RESULT_DATE END) AS LAST_RESULT_DATE "
                + "FROM WORK_ORDER WO "
                + "JOIN ITEM I ON WO.ITEM_ID = I.ITEM_ID "
                + "JOIN EMP E ON WO.EMP_ID = E.EMP_ID "
                + "LEFT JOIN PRODUCTION_RESULT PR ON WO.WORK_ORDER_ID = PR.WORK_ORDER_ID "
                + "WHERE WO.USE_YN = 'Y' "
                + "  AND WO.WORK_ORDER_ID = ? "
                + "GROUP BY WO.WORK_ORDER_ID, WO.WORK_DATE, WO.WORK_QTY, WO.STATUS, WO.REMARK, "
                + "         I.ITEM_CODE, I.ITEM_NAME, E.EMP_NAME";

        try {
            ps = conn.prepareStatement(sql);
            ps.setInt(1, workOrderId);
            rs = ps.executeQuery();

            if (rs.next()) {
                dto = new WorkStatusDTO();
                int id = rs.getInt("WORK_ORDER_ID");
                Date workDate = rs.getDate("WORK_DATE");

                String displayCode;
                if (workDate != null) {
                    displayCode = "WO-" + new SimpleDateFormat("yyyyMMdd").format(workDate)
                            + "-" + String.format("%05d", id);
                } else {
                    displayCode = "WO-UNKNOWN-" + String.format("%05d", id);
                }

                dto.setWorkOrderId(id);
                dto.setWorkOrderDisplayCode(displayCode);
                dto.setWorkDate(workDate);
                dto.setWorkQty(rs.getDouble("WORK_QTY"));
                dto.setWorkOrderStatus(rs.getString("STATUS"));
                dto.setRemark(rs.getString("REMARK"));
                dto.setItemCode(rs.getString("ITEM_CODE"));
                dto.setItemName(rs.getString("ITEM_NAME"));
                dto.setEmpName(rs.getString("EMP_NAME"));
                dto.setProducedQty(rs.getDouble("PRODUCED_QTY"));
                dto.setLossQty(rs.getDouble("LOSS_QTY"));
                dto.setLastResultDate(rs.getDate("LAST_RESULT_DATE"));
            }
        } catch (Exception e) {
            throw new RuntimeException("작업 현황 상세 조회 실패", e);
        } finally {
            try { if (rs != null) rs.close(); } catch (Exception e) { e.printStackTrace(); }
            try { if (ps != null) ps.close(); } catch (Exception e) { e.printStackTrace(); }
        }

        return dto;
    }
}
