package report.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import report.dto.DefectTypeDTO;
import report.dto.EquipmentIssueDTO;
import report.dto.PlanAchievementDTO;
import report.dto.ProductionTrendDTO;
import report.dto.ReportSummaryDTO;

public class ReportDAO {

    public ReportSummaryDTO selectSummary(Connection conn, Date startDate, Date endDate) {
        ReportSummaryDTO dto = new ReportSummaryDTO();
        PreparedStatement ps = null;
        ResultSet rs = null;

        String sql = ""
                + "SELECT "
                + "    NVL((SELECT SUM(PR.PRODUCED_QTY) "
                + "           FROM PRODUCTION_RESULT PR "
                + "          WHERE PR.USE_YN = 'Y' "
                + "            AND PR.RESULT_DATE BETWEEN ? AND ?), 0) AS TOTAL_PRODUCED_QTY, "
                + "    NVL((SELECT SUM(PR.LOSS_QTY) "
                + "           FROM PRODUCTION_RESULT PR "
                + "          WHERE PR.USE_YN = 'Y' "
                + "            AND PR.RESULT_DATE BETWEEN ? AND ?), 0) AS TOTAL_LOSS_QTY, "
                + "    NVL((SELECT COUNT(*) "
                + "           FROM DEFECT_PRODUCT DP "
                + "           JOIN FINAL_INSPECTION FI ON DP.FINAL_INSPECTION_ID = FI.FINAL_INSPECTION_ID "
                + "           JOIN PRODUCTION_RESULT PR ON FI.RESULT_ID = PR.RESULT_ID "
                + "          WHERE DP.USE_YN = 'Y' "
                + "            AND FI.USE_YN = 'Y' "
                + "            AND PR.USE_YN = 'Y' "
                + "            AND PR.RESULT_DATE BETWEEN ? AND ?), 0) AS DEFECT_COUNT, "
                + "    NVL((SELECT SUM(PP.PLAN_QTY) "
                + "           FROM PRODUCTION_PLAN PP "
                + "          WHERE PP.USE_YN = 'Y' "
                + "            AND PP.PLAN_DATE BETWEEN ? AND ?), 0) AS TOTAL_PLAN_QTY "
                + "FROM DUAL";

        try {
            ps = conn.prepareStatement(sql);
            int idx = 1;
            for (int i = 0; i < 4; i++) {
                ps.setDate(idx++, startDate);
                ps.setDate(idx++, endDate);
            }
            rs = ps.executeQuery();
            if (rs.next()) {
                dto.setTotalProducedQty(rs.getDouble("TOTAL_PRODUCED_QTY"));
                dto.setTotalLossQty(rs.getDouble("TOTAL_LOSS_QTY"));
                dto.setDefectCount(rs.getInt("DEFECT_COUNT"));
                dto.setTotalPlanQty(rs.getDouble("TOTAL_PLAN_QTY"));
            }
        } catch (Exception e) {
            throw new RuntimeException("리포트 요약 조회 실패", e);
        } finally {
            close(rs, ps);
        }

        return dto;
    }

    public List<ProductionTrendDTO> selectProductionTrend(Connection conn, Date startDate, Date endDate) {
        List<ProductionTrendDTO> list = new ArrayList<ProductionTrendDTO>();
        PreparedStatement ps = null;
        ResultSet rs = null;

        String sql = ""
                + "SELECT TO_CHAR(PR.RESULT_DATE, 'YYYY-MM') AS LABEL, "
                + "       SUM(NVL(PR.PRODUCED_QTY, 0)) AS PRODUCED_QTY, "
                + "       SUM(NVL(PR.LOSS_QTY, 0)) AS LOSS_QTY "
                + "  FROM PRODUCTION_RESULT PR "
                + " WHERE PR.USE_YN = 'Y' "
                + "   AND PR.RESULT_DATE BETWEEN ? AND ? "
                + " GROUP BY TO_CHAR(PR.RESULT_DATE, 'YYYY-MM') "
                + " ORDER BY LABEL";

        try {
            ps = conn.prepareStatement(sql);
            ps.setDate(1, startDate);
            ps.setDate(2, endDate);
            rs = ps.executeQuery();
            while (rs.next()) {
                ProductionTrendDTO dto = new ProductionTrendDTO();
                dto.setLabel(rs.getString("LABEL"));
                dto.setProducedQty(rs.getDouble("PRODUCED_QTY"));
                dto.setLossQty(rs.getDouble("LOSS_QTY"));
                list.add(dto);
            }
        } catch (Exception e) {
            throw new RuntimeException("생산 추이 조회 실패", e);
        } finally {
            close(rs, ps);
        }

        return list;
    }

    public List<DefectTypeDTO> selectDefectTypeRank(Connection conn, Date startDate, Date endDate) {
        List<DefectTypeDTO> list = new ArrayList<DefectTypeDTO>();
        PreparedStatement ps = null;
        ResultSet rs = null;

        String sql = ""
                + "SELECT * FROM ( "
                + "    SELECT DC.DEFECT_NAME, COUNT(*) AS DEFECT_COUNT "
                + "      FROM DEFECT_PRODUCT DP "
                + "      JOIN DEFECT_CODE DC ON DP.DEFECT_CODE_ID = DC.DEFECT_CODE_ID "
                + "      JOIN FINAL_INSPECTION FI ON DP.FINAL_INSPECTION_ID = FI.FINAL_INSPECTION_ID "
                + "      JOIN PRODUCTION_RESULT PR ON FI.RESULT_ID = PR.RESULT_ID "
                + "     WHERE DP.USE_YN = 'Y' "
                + "       AND DC.USE_YN = 'Y' "
                + "       AND FI.USE_YN = 'Y' "
                + "       AND PR.USE_YN = 'Y' "
                + "       AND PR.RESULT_DATE BETWEEN ? AND ? "
                + "     GROUP BY DC.DEFECT_NAME "
                + "     ORDER BY DEFECT_COUNT DESC, DC.DEFECT_NAME ASC "
                + ") WHERE ROWNUM <= 5";

        try {
            ps = conn.prepareStatement(sql);
            ps.setDate(1, startDate);
            ps.setDate(2, endDate);
            rs = ps.executeQuery();
            while (rs.next()) {
                DefectTypeDTO dto = new DefectTypeDTO();
                dto.setDefectName(rs.getString("DEFECT_NAME"));
                dto.setDefectCount(rs.getInt("DEFECT_COUNT"));
                list.add(dto);
            }
        } catch (Exception e) {
            throw new RuntimeException("불량 유형 조회 실패", e);
        } finally {
            close(rs, ps);
        }

        return list;
    }

    public List<PlanAchievementDTO> selectPlanAchievement(Connection conn, Date startDate, Date endDate) {
        List<PlanAchievementDTO> list = new ArrayList<PlanAchievementDTO>();
        PreparedStatement ps = null;
        ResultSet rs = null;

        String sql = ""
                + "SELECT NVL(P.ITEM_NAME, R.ITEM_NAME) AS ITEM_NAME, "
                + "       NVL(P.PLAN_QTY, 0) AS PLAN_QTY, "
                + "       NVL(R.PRODUCED_QTY, 0) AS PRODUCED_QTY, "
                + "       CASE WHEN NVL(P.PLAN_QTY, 0) = 0 THEN 0 "
                + "            ELSE ROUND((NVL(R.PRODUCED_QTY, 0) / P.PLAN_QTY) * 100, 1) END AS ACHIEVEMENT_RATE "
                + "  FROM ( "
                + "        SELECT I.ITEM_ID, I.ITEM_NAME, SUM(NVL(PP.PLAN_QTY, 0)) AS PLAN_QTY "
                + "          FROM PRODUCTION_PLAN PP "
                + "          JOIN ITEM I ON PP.ITEM_ID = I.ITEM_ID "
                + "         WHERE PP.USE_YN = 'Y' "
                + "           AND I.USE_YN = 'Y' "
                + "           AND PP.PLAN_DATE BETWEEN ? AND ? "
                + "         GROUP BY I.ITEM_ID, I.ITEM_NAME "
                + "       ) P "
                + "  FULL OUTER JOIN ( "
                + "        SELECT I.ITEM_ID, I.ITEM_NAME, SUM(NVL(PR.PRODUCED_QTY, 0)) AS PRODUCED_QTY "
                + "          FROM PRODUCTION_RESULT PR "
                + "          JOIN WORK_ORDER WO ON PR.WORK_ORDER_ID = WO.WORK_ORDER_ID "
                + "          JOIN ITEM I ON WO.ITEM_ID = I.ITEM_ID "
                + "         WHERE PR.USE_YN = 'Y' "
                + "           AND WO.USE_YN = 'Y' "
                + "           AND I.USE_YN = 'Y' "
                + "           AND PR.RESULT_DATE BETWEEN ? AND ? "
                + "         GROUP BY I.ITEM_ID, I.ITEM_NAME "
                + "       ) R "
                + "    ON P.ITEM_ID = R.ITEM_ID "
                + " ORDER BY ACHIEVEMENT_RATE DESC, ITEM_NAME ASC";

        try {
            ps = conn.prepareStatement(sql);
            ps.setDate(1, startDate);
            ps.setDate(2, endDate);
            ps.setDate(3, startDate);
            ps.setDate(4, endDate);
            rs = ps.executeQuery();
            while (rs.next()) {
                PlanAchievementDTO dto = new PlanAchievementDTO();
                dto.setItemName(rs.getString("ITEM_NAME"));
                dto.setPlanQty(rs.getDouble("PLAN_QTY"));
                dto.setProducedQty(rs.getDouble("PRODUCED_QTY"));
                dto.setAchievementRate(rs.getDouble("ACHIEVEMENT_RATE"));
                list.add(dto);
            }
        } catch (Exception e) {
            throw new RuntimeException("목표 달성 현황 조회 실패", e);
        } finally {
            close(rs, ps);
        }

        return list;
    }

    public List<EquipmentIssueDTO> selectEquipmentIssueRank(Connection conn, Date startDate, Date endDate) {
        List<EquipmentIssueDTO> list = new ArrayList<EquipmentIssueDTO>();
        PreparedStatement ps = null;
        ResultSet rs = null;

        String sql = ""
                + "SELECT * FROM ( "
                + "    SELECT E.EQUIPMENT_NAME, "
                + "           NVL(M.MAINTENANCE_COUNT, 0) AS MAINTENANCE_COUNT, "
                + "           NVL(F.FAILURE_COUNT, 0) AS FAILURE_COUNT, "
                + "           NVL(M.MAINTENANCE_COUNT, 0) + NVL(F.FAILURE_COUNT, 0) AS TOTAL_ISSUE_COUNT "
                + "      FROM EQUIPMENT E "
                + "      LEFT JOIN ( "
                + "            SELECT EM.EQUIPMENT_ID, COUNT(*) AS MAINTENANCE_COUNT "
                + "              FROM EQUIPMENT_MAINTENANCE EM "
                + "             WHERE EM.USE_YN = 'Y' "
                + "               AND EM.MAINTENANCE_DATE BETWEEN ? AND ? "
                + "             GROUP BY EM.EQUIPMENT_ID "
                + "      ) M ON E.EQUIPMENT_ID = M.EQUIPMENT_ID "
                + "      LEFT JOIN ( "
                + "            SELECT EM.EQUIPMENT_ID, COUNT(*) AS FAILURE_COUNT "
                + "              FROM EQUIPMENT_FAILURE_ACTION EFA "
                + "              JOIN EQUIPMENT_MAINTENANCE EM ON EFA.MAINTENANCE_ID = EM.MAINTENANCE_ID "
                + "             WHERE EFA.USE_YN = 'Y' "
                + "               AND EM.USE_YN = 'Y' "
                + "               AND EFA.FAILURE_DATE BETWEEN ? AND ? "
                + "             GROUP BY EM.EQUIPMENT_ID "
                + "      ) F ON E.EQUIPMENT_ID = F.EQUIPMENT_ID "
                + "     WHERE E.USE_YN = 'Y' "
                + "     ORDER BY TOTAL_ISSUE_COUNT DESC, E.EQUIPMENT_NAME ASC "
                + ") WHERE ROWNUM <= 5";

        try {
            ps = conn.prepareStatement(sql);
            ps.setDate(1, startDate);
            ps.setDate(2, endDate);
            ps.setDate(3, startDate);
            ps.setDate(4, endDate);
            rs = ps.executeQuery();
            while (rs.next()) {
                EquipmentIssueDTO dto = new EquipmentIssueDTO();
                dto.setEquipmentName(rs.getString("EQUIPMENT_NAME"));
                dto.setMaintenanceCount(rs.getInt("MAINTENANCE_COUNT"));
                dto.setFailureCount(rs.getInt("FAILURE_COUNT"));
                dto.setTotalIssueCount(rs.getInt("TOTAL_ISSUE_COUNT"));
                list.add(dto);
            }
        } catch (Exception e) {
            throw new RuntimeException("설비 이력 조회 실패", e);
        } finally {
            close(rs, ps);
        }

        return list;
    }

    public double selectProducedQtySum(Connection conn, Date startDate, Date endDate) {
        return selectDouble(conn,
                "SELECT NVL(SUM(PR.PRODUCED_QTY), 0) AS VAL FROM PRODUCTION_RESULT PR WHERE PR.USE_YN = 'Y' AND PR.RESULT_DATE BETWEEN ? AND ?",
                startDate, endDate, "생산량 합계 조회 실패");
    }

    public double selectLossQtySum(Connection conn, Date startDate, Date endDate) {
        return selectDouble(conn,
                "SELECT NVL(SUM(PR.LOSS_QTY), 0) AS VAL FROM PRODUCTION_RESULT PR WHERE PR.USE_YN = 'Y' AND PR.RESULT_DATE BETWEEN ? AND ?",
                startDate, endDate, "손실량 합계 조회 실패");
    }

    public int selectDefectCount(Connection conn, Date startDate, Date endDate) {
        return (int) selectDouble(conn,
                "SELECT NVL(COUNT(*), 0) AS VAL FROM DEFECT_PRODUCT DP JOIN FINAL_INSPECTION FI ON DP.FINAL_INSPECTION_ID = FI.FINAL_INSPECTION_ID JOIN PRODUCTION_RESULT PR ON FI.RESULT_ID = PR.RESULT_ID WHERE DP.USE_YN = 'Y' AND FI.USE_YN = 'Y' AND PR.USE_YN = 'Y' AND PR.RESULT_DATE BETWEEN ? AND ?",
                startDate, endDate, "불량 건수 조회 실패");
    }

    public double selectAchievementRate(Connection conn, Date startDate, Date endDate) {
        PreparedStatement ps = null;
        ResultSet rs = null;

        String sql = ""
                + "SELECT NVL((SELECT SUM(PP.PLAN_QTY) FROM PRODUCTION_PLAN PP WHERE PP.USE_YN = 'Y' AND PP.PLAN_DATE BETWEEN ? AND ?), 0) AS PLAN_QTY, "
                + "       NVL((SELECT SUM(PR.PRODUCED_QTY) FROM PRODUCTION_RESULT PR WHERE PR.USE_YN = 'Y' AND PR.RESULT_DATE BETWEEN ? AND ?), 0) AS PRODUCED_QTY "
                + "  FROM DUAL";

        try {
            ps = conn.prepareStatement(sql);
            ps.setDate(1, startDate);
            ps.setDate(2, endDate);
            ps.setDate(3, startDate);
            ps.setDate(4, endDate);
            rs = ps.executeQuery();
            if (rs.next()) {
                double planQty = rs.getDouble("PLAN_QTY");
                double producedQty = rs.getDouble("PRODUCED_QTY");
                if (planQty <= 0) {
                    return 0;
                }
                return Math.round((producedQty / planQty) * 1000.0) / 10.0;
            }
            return 0;
        } catch (Exception e) {
            throw new RuntimeException("달성률 조회 실패", e);
        } finally {
            close(rs, ps);
        }
    }

    public int selectEquipmentIssueCount(Connection conn, Date startDate, Date endDate) {
        PreparedStatement ps = null;
        ResultSet rs = null;

        String sql = ""
                + "SELECT NVL(M.MAINTENANCE_COUNT, 0) + NVL(F.FAILURE_COUNT, 0) AS VAL "
                + "  FROM (SELECT COUNT(*) AS MAINTENANCE_COUNT FROM EQUIPMENT_MAINTENANCE EM WHERE EM.USE_YN = 'Y' AND EM.MAINTENANCE_DATE BETWEEN ? AND ?) M, "
                + "       (SELECT COUNT(*) AS FAILURE_COUNT FROM EQUIPMENT_FAILURE_ACTION EFA JOIN EQUIPMENT_MAINTENANCE EM ON EFA.MAINTENANCE_ID = EM.MAINTENANCE_ID WHERE EFA.USE_YN = 'Y' AND EM.USE_YN = 'Y' AND EFA.FAILURE_DATE BETWEEN ? AND ?) F";

        try {
            ps = conn.prepareStatement(sql);
            ps.setDate(1, startDate);
            ps.setDate(2, endDate);
            ps.setDate(3, startDate);
            ps.setDate(4, endDate);
            rs = ps.executeQuery();
            return rs.next() ? rs.getInt("VAL") : 0;
        } catch (Exception e) {
            throw new RuntimeException("설비 이력 건수 조회 실패", e);
        } finally {
            close(rs, ps);
        }
    }

    private double selectDouble(Connection conn, String sql, Date startDate, Date endDate, String errorMessage) {
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = conn.prepareStatement(sql);
            ps.setDate(1, startDate);
            ps.setDate(2, endDate);
            rs = ps.executeQuery();
            return rs.next() ? rs.getDouble("VAL") : 0;
        } catch (Exception e) {
            throw new RuntimeException(errorMessage, e);
        } finally {
            close(rs, ps);
        }
    }

    private void close(ResultSet rs, PreparedStatement ps) {
        try { if (rs != null) rs.close(); } catch (Exception e) { e.printStackTrace(); }
        try { if (ps != null) ps.close(); } catch (Exception e) { e.printStackTrace(); }
    }
}
