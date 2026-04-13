package productionresult.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import productionresult.dto.ProductionResultDTO;

public class ProductionResultDAO {

    public List<ProductionResultDTO> selectProductionResultList(Connection conn, int workOrderId) {
        List<ProductionResultDTO> list = new ArrayList<ProductionResultDTO>();
        PreparedStatement ps = null;
        ResultSet rs = null;

        String sql = ""
                + "SELECT PR.RESULT_ID, PR.WORK_ORDER_ID, PR.RESULT_DATE, PR.LOT_NO, "
                + "       PR.PRODUCED_QTY, PR.LOSS_QTY, PR.STATUS, PR.USE_YN, "
                + "       PR.REMARK, PR.CREATED_AT, PR.UPDATED_AT, "
                + "       I.ITEM_NAME, E.EMP_NAME "
                + "FROM PRODUCTION_RESULT PR "
                + "JOIN WORK_ORDER WO ON PR.WORK_ORDER_ID = WO.WORK_ORDER_ID "
                + "JOIN ITEM I ON WO.ITEM_ID = I.ITEM_ID "
                + "JOIN EMP E ON WO.EMP_ID = E.EMP_ID "
                + "WHERE PR.USE_YN = 'Y' "
                + "  AND PR.WORK_ORDER_ID = ? "
                + "ORDER BY PR.RESULT_ID DESC";

        try {
            ps = conn.prepareStatement(sql);
            ps.setInt(1, workOrderId);
            rs = ps.executeQuery();

            while (rs.next()) {
                ProductionResultDTO dto = new ProductionResultDTO();
                dto.setResultId(rs.getInt("RESULT_ID"));
                dto.setWorkOrderId(rs.getInt("WORK_ORDER_ID"));
                dto.setResultDate(rs.getDate("RESULT_DATE"));
                dto.setLotNo(rs.getString("LOT_NO"));
                dto.setProducedQty(rs.getDouble("PRODUCED_QTY"));
                dto.setLossQty(rs.getDouble("LOSS_QTY"));
                dto.setStatus(rs.getString("STATUS"));
                dto.setUseYn(rs.getString("USE_YN"));
                dto.setRemark(rs.getString("REMARK"));
                dto.setCreatedAt(rs.getDate("CREATED_AT"));
                dto.setUpdatedAt(rs.getDate("UPDATED_AT"));
                dto.setItemName(rs.getString("ITEM_NAME"));
                dto.setEmpName(rs.getString("EMP_NAME"));
                list.add(dto);
            }
        } catch (Exception e) {
            throw new RuntimeException("생산실적 목록 조회 실패", e);
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

    public ProductionResultDTO selectProductionResultById(Connection conn, int resultId) {
        ProductionResultDTO dto = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        String sql = ""
                + "SELECT PR.RESULT_ID, PR.WORK_ORDER_ID, PR.RESULT_DATE, PR.LOT_NO, "
                + "       PR.PRODUCED_QTY, PR.LOSS_QTY, PR.STATUS, PR.USE_YN, "
                + "       PR.REMARK, PR.CREATED_AT, PR.UPDATED_AT, "
                + "       I.ITEM_NAME, E.EMP_NAME "
                + "FROM PRODUCTION_RESULT PR "
                + "JOIN WORK_ORDER WO ON PR.WORK_ORDER_ID = WO.WORK_ORDER_ID "
                + "JOIN ITEM I ON WO.ITEM_ID = I.ITEM_ID "
                + "JOIN EMP E ON WO.EMP_ID = E.EMP_ID "
                + "WHERE PR.RESULT_ID = ? "
                + "  AND PR.USE_YN = 'Y'";

        try {
            ps = conn.prepareStatement(sql);
            ps.setInt(1, resultId);
            rs = ps.executeQuery();

            if (rs.next()) {
                dto = new ProductionResultDTO();
                dto.setResultId(rs.getInt("RESULT_ID"));
                dto.setWorkOrderId(rs.getInt("WORK_ORDER_ID"));
                dto.setResultDate(rs.getDate("RESULT_DATE"));
                dto.setLotNo(rs.getString("LOT_NO"));
                dto.setProducedQty(rs.getDouble("PRODUCED_QTY"));
                dto.setLossQty(rs.getDouble("LOSS_QTY"));
                dto.setStatus(rs.getString("STATUS"));
                dto.setUseYn(rs.getString("USE_YN"));
                dto.setRemark(rs.getString("REMARK"));
                dto.setCreatedAt(rs.getDate("CREATED_AT"));
                dto.setUpdatedAt(rs.getDate("UPDATED_AT"));
                dto.setItemName(rs.getString("ITEM_NAME"));
                dto.setEmpName(rs.getString("EMP_NAME"));
            }
        } catch (Exception e) {
            throw new RuntimeException("생산실적 상세 조회 실패", e);
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

    public int updateProductionResult(Connection conn, ProductionResultDTO dto) {
        PreparedStatement ps = null;
        int result = 0;

        String sql = ""
                + "UPDATE PRODUCTION_RESULT "
                + "SET RESULT_DATE = ?, "
                + "    LOT_NO = ?, "
                + "    PRODUCED_QTY = ?, "
                + "    LOSS_QTY = ?, "
                + "    STATUS = ?, "
                + "    REMARK = ?, "
                + "    UPDATED_AT = SYSDATE "
                + "WHERE RESULT_ID = ?";

        try {
            ps = conn.prepareStatement(sql);
            ps.setDate(1, dto.getResultDate());
            ps.setString(2, dto.getLotNo());
            ps.setDouble(3, dto.getProducedQty());
            ps.setDouble(4, dto.getLossQty());
            ps.setString(5, dto.getStatus());
            ps.setString(6, dto.getRemark());
            ps.setInt(7, dto.getResultId());

            result = ps.executeUpdate();
        } catch (Exception e) {
            throw new RuntimeException("생산실적 수정 실패", e);
        } finally {
            try {
                if (ps != null) ps.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return result;
    }

    public int deleteProductionResults(Connection conn, String[] resultIds) {
        PreparedStatement ps = null;
        int result = 0;

        String sql = ""
                + "UPDATE PRODUCTION_RESULT "
                + "SET USE_YN = 'N', "
                + "    UPDATED_AT = SYSDATE "
                + "WHERE RESULT_ID = ?";

        try {
            ps = conn.prepareStatement(sql);

            for (int i = 0; i < resultIds.length; i++) {
                ps.setInt(1, Integer.parseInt(resultIds[i]));
                result += ps.executeUpdate();
            }
        } catch (Exception e) {
            throw new RuntimeException("생산실적 삭제 실패", e);
        } finally {
            try {
                if (ps != null) ps.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return result;
    }

public int insertProductionResult(Connection conn, ProductionResultDTO dto) {
    PreparedStatement ps = null;
    int result = 0;
    String sql = "INSERT INTO PRODUCTION_RESULT (RESULT_ID, WORK_ORDER_ID, RESULT_DATE, LOT_NO, PRODUCED_QTY, LOSS_QTY, STATUS, USE_YN, REMARK, CREATED_AT, UPDATED_AT) VALUES (SEQ_PRODUCTION_RESULT.NEXTVAL, ?, ?, ?, ?, ?, ?, 'Y', ?, SYSDATE, SYSDATE)";
    try {
        ps = conn.prepareStatement(sql);
        ps.setInt(1, dto.getWorkOrderId());
        ps.setDate(2, dto.getResultDate());
        ps.setString(3, dto.getLotNo());
        ps.setDouble(4, dto.getProducedQty());
        ps.setDouble(5, dto.getLossQty());
        ps.setString(6, dto.getStatus());
        ps.setString(7, dto.getRemark());
        result = ps.executeUpdate();
    } catch (Exception e) {
        throw new RuntimeException("생산실적 등록 실패", e);
    } finally {
        try { if (ps != null) ps.close(); } catch (Exception e) { e.printStackTrace(); }
    }
    return result;
}

}