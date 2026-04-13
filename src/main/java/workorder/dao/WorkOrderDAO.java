package workorder.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import workorder.dto.WorkOrderDTO;

public class WorkOrderDAO {

    public List<WorkOrderDTO> selectWorkOrderList(Connection conn) {
        List<WorkOrderDTO> list = new ArrayList<WorkOrderDTO>();
        PreparedStatement ps = null;
        ResultSet rs = null;

        String sql = ""
                + "SELECT WO.WORK_ORDER_ID, WO.ITEM_ID, WO.PLAN_ID, WO.EMP_ID, "
                + "       WO.WORK_DATE, WO.WORK_QTY, WO.STATUS, WO.USE_YN, "
                + "       WO.REMARK, WO.CREATED_AT, WO.UPDATED_AT, "
                + "       I.ITEM_CODE, I.ITEM_NAME, E.EMP_NAME "
                + "FROM WORK_ORDER WO "
                + "JOIN ITEM I ON WO.ITEM_ID = I.ITEM_ID "
                + "JOIN EMP E ON WO.EMP_ID = E.EMP_ID "
                + "WHERE WO.USE_YN = 'Y' "
                + "ORDER BY WO.WORK_ORDER_ID DESC";

        try {
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();

            while (rs.next()) {
                WorkOrderDTO dto = new WorkOrderDTO();

                int id = rs.getInt("WORK_ORDER_ID");
                Date workDate = rs.getDate("WORK_DATE");

                String displayCode = "";
                if (workDate != null) {
                    displayCode = "WO-"
                            + new SimpleDateFormat("yyyyMMdd").format(workDate)
                            + "-"
                            + String.format("%05d", id);
                } else {
                    displayCode = "WO-UNKNOWN-" + String.format("%05d", id);
                }

                dto.setWorkOrderId(id);
                dto.setWorkOrderDisplayCode(displayCode);

                dto.setItemId(rs.getInt("ITEM_ID"));
                dto.setPlanId(rs.getInt("PLAN_ID"));
                dto.setEmpId(rs.getInt("EMP_ID"));

                dto.setItemCode(rs.getString("ITEM_CODE"));
                dto.setItemName(rs.getString("ITEM_NAME"));
                dto.setEmpName(rs.getString("EMP_NAME"));

                dto.setWorkDate(workDate);
                dto.setWorkQty(rs.getDouble("WORK_QTY"));
                dto.setStatus(rs.getString("STATUS"));
                dto.setUseYn(rs.getString("USE_YN"));
                dto.setRemark(rs.getString("REMARK"));
                dto.setCreatedAt(rs.getDate("CREATED_AT"));
                dto.setUpdatedAt(rs.getDate("UPDATED_AT"));

                list.add(dto);
            }
        } catch (Exception e) {
            throw new RuntimeException("작업지시 목록 조회 실패", e);
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

    public WorkOrderDTO selectWorkOrderById(Connection conn, int workOrderId) {
        WorkOrderDTO dto = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        String sql = ""
                + "SELECT WO.WORK_ORDER_ID, WO.ITEM_ID, WO.PLAN_ID, WO.EMP_ID, "
                + "       WO.WORK_DATE, WO.WORK_QTY, WO.STATUS, WO.USE_YN, "
                + "       WO.REMARK, WO.CREATED_AT, WO.UPDATED_AT, "
                + "       I.ITEM_CODE, I.ITEM_NAME, E.EMP_NAME "
                + "FROM WORK_ORDER WO "
                + "JOIN ITEM I ON WO.ITEM_ID = I.ITEM_ID "
                + "JOIN EMP E ON WO.EMP_ID = E.EMP_ID "
                + "WHERE WO.WORK_ORDER_ID = ? "
                + "  AND WO.USE_YN = 'Y'";

        try {
            ps = conn.prepareStatement(sql);
            ps.setInt(1, workOrderId);
            rs = ps.executeQuery();

            if (rs.next()) {
                dto = new WorkOrderDTO();

                int id = rs.getInt("WORK_ORDER_ID");
                Date workDate = rs.getDate("WORK_DATE");

                String displayCode = "";
                if (workDate != null) {
                    displayCode = "WO-"
                            + new SimpleDateFormat("yyyyMMdd").format(workDate)
                            + "-"
                            + String.format("%05d", id);
                } else {
                    displayCode = "WO-UNKNOWN-" + String.format("%05d", id);
                }

                dto.setWorkOrderId(id);
                dto.setWorkOrderDisplayCode(displayCode);

                dto.setItemId(rs.getInt("ITEM_ID"));
                dto.setPlanId(rs.getInt("PLAN_ID"));
                dto.setEmpId(rs.getInt("EMP_ID"));

                dto.setItemCode(rs.getString("ITEM_CODE"));
                dto.setItemName(rs.getString("ITEM_NAME"));
                dto.setEmpName(rs.getString("EMP_NAME"));

                dto.setWorkDate(workDate);
                dto.setWorkQty(rs.getDouble("WORK_QTY"));
                dto.setStatus(rs.getString("STATUS"));
                dto.setUseYn(rs.getString("USE_YN"));
                dto.setRemark(rs.getString("REMARK"));
                dto.setCreatedAt(rs.getDate("CREATED_AT"));
                dto.setUpdatedAt(rs.getDate("UPDATED_AT"));
            }
        } catch (Exception e) {
            throw new RuntimeException("작업지시 상세 조회 실패", e);
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

    public int insertWorkOrder(Connection conn, WorkOrderDTO dto) {
        PreparedStatement ps = null;
        int result = 0;

        String sql = ""
                + "INSERT INTO WORK_ORDER ( "
                + "    WORK_ORDER_ID, ITEM_ID, PLAN_ID, EMP_ID, "
                + "    WORK_DATE, WORK_QTY, STATUS, USE_YN, REMARK, CREATED_AT, UPDATED_AT "
                + ") VALUES ( "
                + "    SEQ_WORK_ORDER.NEXTVAL, ?, ?, ?, ?, ?, ?, 'Y', ?, SYSDATE, SYSDATE "
                + ")";

        try {
            ps = conn.prepareStatement(sql);
            ps.setInt(1, dto.getItemId());
            ps.setInt(2, dto.getPlanId());
            ps.setInt(3, dto.getEmpId());
            ps.setDate(4, dto.getWorkDate());
            ps.setDouble(5, dto.getWorkQty());
            ps.setString(6, dto.getStatus());
            ps.setString(7, dto.getRemark());

            result = ps.executeUpdate();
        } catch (Exception e) {
            throw new RuntimeException("작업지시 등록 실패", e);
        } finally {
            try {
                if (ps != null) ps.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return result;
    }

    public int updateWorkOrder(Connection conn, WorkOrderDTO dto) {
        PreparedStatement ps = null;
        int result = 0;

        String sql = ""
                + "UPDATE WORK_ORDER "
                + "SET WORK_DATE = ?, "
                + "    WORK_QTY = ?, "
                + "    STATUS = ?, "
                + "    REMARK = ?, "
                + "    UPDATED_AT = SYSDATE "
                + "WHERE WORK_ORDER_ID = ?";

        try {
            ps = conn.prepareStatement(sql);
            ps.setDate(1, dto.getWorkDate());
            ps.setDouble(2, dto.getWorkQty());
            ps.setString(3, dto.getStatus());
            ps.setString(4, dto.getRemark());
            ps.setInt(5, dto.getWorkOrderId());

            result = ps.executeUpdate();
        } catch (Exception e) {
            throw new RuntimeException("작업지시 수정 실패", e);
        } finally {
            try {
                if (ps != null) ps.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return result;
    }

    public int deleteWorkOrders(Connection conn, String[] workOrderIds) {
        PreparedStatement ps = null;
        int result = 0;

        String sql = ""
                + "UPDATE WORK_ORDER "
                + "SET USE_YN = 'N', "
                + "    UPDATED_AT = SYSDATE "
                + "WHERE WORK_ORDER_ID = ?";

        try {
            ps = conn.prepareStatement(sql);

            for (int i = 0; i < workOrderIds.length; i++) {
                ps.setInt(1, Integer.parseInt(workOrderIds[i]));
                result += ps.executeUpdate();
            }
        } catch (Exception e) {
            throw new RuntimeException("작업지시 삭제 실패", e);
        } finally {
            try {
                if (ps != null) ps.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return result;
    }
}