package dashboard.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

public class CeoMainDAO {

    /*
     * JNDI 커넥션 풀에서 DB 연결 가져오기
     * context.xml 의 jdbc/oracle 사용
     */
    private Connection getConnection() throws Exception {
        Context ctx = new InitialContext();
        DataSource dataFactory = (DataSource) ctx.lookup("java:/comp/env/jdbc/oracle");
        return dataFactory.getConnection();
    }

    // =========================================================
    // 1. 기준일
    // 생산/검사 기준일만 사용
    // =========================================================
    public Date selectLatestBaseDate() throws Exception {
        Date baseDate = null;

        String sql =
                "SELECT MAX(base_date) AS base_date " +
                "  FROM ( " +
                "        SELECT MAX(TRUNC(RESULT_DATE)) AS base_date " +
                "          FROM PRODUCTION_RESULT " +
                "         WHERE NVL(USE_YN, 'Y') = 'Y' " +
                "        UNION ALL " +
                "        SELECT MAX(TRUNC(INSPECTION_DATE)) AS base_date " +
                "          FROM FINAL_INSPECTION " +
                "         WHERE NVL(USE_YN, 'Y') = 'Y' " +
                "        UNION ALL " +
                "        SELECT MAX(TRUNC(PLAN_DATE)) AS base_date " +
                "          FROM PRODUCTION_PLAN " +
                "         WHERE NVL(USE_YN, 'Y') = 'Y' " +
                "       )";

        try (
            Connection conn = getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery()
        ) {
            if (rs.next()) {
                baseDate = rs.getDate("base_date");
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }

        return baseDate;
    }

    public int selectTotalLineCount() throws Exception {
        int count = 0;
        String sql = "SELECT COUNT(*) AS cnt FROM LINE WHERE NVL(USE_YN, 'Y') = 'Y'";

        try (
            Connection conn = getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery()
        ) {
            if (rs.next()) {
                count = rs.getInt("cnt");
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }

        return count;
    }

    /*
     * 별도 브리핑 저장 테이블이 없어서 null 반환
     * service 에서 자동 생성
     */
    public String selectBriefingText(Date baseDate) {
        return null;
    }

    // =========================================================
    // 2. KPI
    // 생산/품질은 baseDate
    // 출하는 SHIPMENT 최신일
    // 원가는 ACTUAL_COST_DAILY 최신일
    // =========================================================
    public Map<String, Object> selectKpi(Date baseDate) throws Exception {
        Map<String, Object> result = new HashMap<String, Object>();

        try (Connection conn = getConnection()) {

            Date shipmentBaseDate = selectLatestUsedDate(conn, "SHIPMENT", "NVL(DUE_DATE, SHIP_DATE)");
            if (shipmentBaseDate == null) {
                shipmentBaseDate = baseDate;
            }

            Date costBaseDate = selectLatestUsedDate(conn, "ACTUAL_COST_DAILY", "COST_DATE");
            if (costBaseDate == null) {
                costBaseDate = baseDate;
            }

            String sql =
                    "SELECT " +
                    "    NVL(plan_data.plan_qty, 0) AS plan_qty, " +
                    "    NVL(prod_data.actual_qty, 0) AS actual_qty, " +
                    "    NVL(prod_data.defect_qty, 0) AS defect_qty, " +
                    "    NVL(prod_data.good_qty, 0) AS good_qty, " +
                    "    NVL(inspect_data.inspect_qty, 0) AS inspect_qty, " +
                    "    NVL(delivery_data.delivery_target_count, 0) AS delivery_target_count, " +
                    "    NVL(delivery_data.on_time_count, 0) AS on_time_count, " +
                    "    NVL(delivery_data.delay_count, 0) AS delay_count, " +
                    "    NVL(downtime_data.downtime_min, 0) AS downtime_min, " +
                    "    NVL(line_data.total_line_count, 0) AS total_line_count, " +
                    "    NVL(inv_data.inventory_risk_count, 0) AS inventory_risk_count, " +
                    "    NVL(po_data.urgent_order_count, 0) AS urgent_order_count, " +
                    "    NVL(cost_data.actual_unit_cost, 0) AS actual_unit_cost, " +
                    "    NVL(std_cost_data.standard_unit_cost, 0) AS standard_unit_cost " +
                    "FROM " +
                    "    (SELECT SUM(PP.PLAN_QTY) AS plan_qty " +
                    "       FROM PRODUCTION_PLAN PP " +
                    "      WHERE TRUNC(PP.PLAN_DATE) = ? " +
                    "        AND NVL(PP.USE_YN, 'Y') = 'Y') plan_data, " +
                    "    (SELECT SUM(PR.PRODUCED_QTY) AS actual_qty, " +
                    "            SUM(PR.LOSS_QTY) AS defect_qty, " +
                    "            SUM(PR.PRODUCED_QTY - NVL(PR.LOSS_QTY, 0)) AS good_qty " +
                    "       FROM PRODUCTION_RESULT PR " +
                    "      WHERE TRUNC(PR.RESULT_DATE) = ? " +
                    "        AND NVL(PR.USE_YN, 'Y') = 'Y') prod_data, " +
                    "    (SELECT SUM(FI.INSPECT_QTY) AS inspect_qty " +
                    "       FROM FINAL_INSPECTION FI " +
                    "      WHERE TRUNC(FI.INSPECTION_DATE) = ? " +
                    "        AND NVL(FI.USE_YN, 'Y') = 'Y') inspect_data, " +
                    "    (SELECT COUNT(*) AS delivery_target_count, " +
                    "            SUM(CASE WHEN NVL(S.ON_TIME_YN, 'N') = 'Y' THEN 1 ELSE 0 END) AS on_time_count, " +
                    "            SUM(CASE WHEN NVL(S.ON_TIME_YN, 'N') = 'N' THEN 1 ELSE 0 END) AS delay_count " +
                    "       FROM SHIPMENT S " +
                    "      WHERE TRUNC(NVL(S.DUE_DATE, S.SHIP_DATE)) = ? " +
                    "        AND NVL(S.USE_YN, 'Y') = 'Y') delivery_data, " +
                    "    (SELECT SUM(ED.DOWNTIME_MIN) AS downtime_min " +
                    "       FROM EQUIPMENT_DOWNTIME ED " +
                    "      WHERE TRUNC(ED.START_TIME) = ? " +
                    "        AND NVL(ED.USE_YN, 'Y') = 'Y') downtime_data, " +
                    "    (SELECT COUNT(*) AS total_line_count " +
                    "       FROM LINE L " +
                    "      WHERE NVL(L.USE_YN, 'Y') = 'Y') line_data, " +
                    "    (SELECT COUNT(*) AS inventory_risk_count " +
                    "       FROM INVENTORY I " +
                    "      WHERE NVL(I.QTY_ON_HAND, 0) < NVL(I.SAFETY_STOCK, 0)) inv_data, " +
                    "    (SELECT COUNT(*) AS urgent_order_count " +
                    "       FROM PURCHASE_ORDER PO " +
                    "      WHERE NVL(PO.USE_YN, 'Y') = 'Y' " +
                    "        AND TRUNC(PO.EXPECTED_IN_DATE) <= ? " +
                    "        AND NVL(PO.STATUS, '대기') NOT IN ('완료', '입고완료')) po_data, " +
                    "    (SELECT AVG(ACD.ACTUAL_UNIT_COST) AS actual_unit_cost " +
                    "       FROM ACTUAL_COST_DAILY ACD " +
                    "      WHERE TRUNC(ACD.COST_DATE) = ? " +
                    "        AND NVL(ACD.USE_YN, 'Y') = 'Y') cost_data, " +
                    "    (SELECT AVG(SC.STANDARD_UNIT_COST) AS standard_unit_cost " +
                    "       FROM STANDARD_COST SC " +
                    "      WHERE ? BETWEEN NVL(SC.EFFECTIVE_FROM, ?) AND NVL(SC.EFFECTIVE_TO, ?) " +
                    "        AND NVL(SC.USE_YN, 'Y') = 'Y') std_cost_data";

            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                int idx = 1;

                ps.setDate(idx++, baseDate);
                ps.setDate(idx++, baseDate);
                ps.setDate(idx++, baseDate);
                ps.setDate(idx++, shipmentBaseDate);
                ps.setDate(idx++, baseDate);
                ps.setDate(idx++, baseDate);
                ps.setDate(idx++, costBaseDate);
                ps.setDate(idx++, costBaseDate);
                ps.setDate(idx++, costBaseDate);
                ps.setDate(idx++, costBaseDate);

                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        double planQty = rs.getDouble("plan_qty");
                        double actualQty = rs.getDouble("actual_qty");
                        double defectQty = rs.getDouble("defect_qty");
                        double goodQty = rs.getDouble("good_qty");
                        double inspectQty = rs.getDouble("inspect_qty");

                        int deliveryTargetCount = rs.getInt("delivery_target_count");
                        int onTimeCount = rs.getInt("on_time_count");
                        int delayCount = rs.getInt("delay_count");

                        double downtimeMin = rs.getDouble("downtime_min");
                        int totalLineCount = rs.getInt("total_line_count");

                        int inventoryRiskCount = rs.getInt("inventory_risk_count");
                        int urgentOrderCount = rs.getInt("urgent_order_count");

                        double actualUnitCost = rs.getDouble("actual_unit_cost");
                        double standardUnitCost = rs.getDouble("standard_unit_cost");

                        double productionRate = percent(actualQty, planQty);

                        double availabilityRate = 100.0;
                        if (totalLineCount > 0) {
                            double totalPlannedMinutes = totalLineCount * 1440.0;
                            availabilityRate = 100.0 - ((downtimeMin / totalPlannedMinutes) * 100.0);
                        }
                        availabilityRate = round1(clamp(availabilityRate, 0, 100));

                        double performanceRate = round1(clamp(productionRate, 0, 100));
                        double qualityRate = percent(goodQty, actualQty);
                        double oeeRate = round1((availabilityRate * performanceRate * qualityRate) / 10000.0);

                        double defectRate = percent(defectQty, actualQty);
                        int shipRiskCount = delayCount;

                        double costVarianceRate = 0;
                        if (standardUnitCost != 0) {
                            costVarianceRate = ((actualUnitCost - standardUnitCost) / standardUnitCost) * 100.0;
                        }

                        result.put("productionRate", round1(productionRate));
                        result.put("productionTargetRate", 100.0);
                        result.put("planQty", round3(planQty));
                        result.put("actualQty", round3(actualQty));

                        result.put("deliveryRate", round1(percent(onTimeCount, deliveryTargetCount)));
                        result.put("onTimeCount", onTimeCount);
                        result.put("deliveryTargetCount", deliveryTargetCount);
                        result.put("delayCount", delayCount);

                        result.put("oeeRate", oeeRate);
                        result.put("availabilityRate", availabilityRate);
                        result.put("performanceRate", performanceRate);
                        result.put("qualityRate", round1(qualityRate));

                        result.put("defectRate", round2(defectRate));
                        result.put("defectQty", round3(defectQty));
                        result.put("inspectQty", round3(inspectQty));
                        result.put("goodQty", round3(goodQty));

                        result.put("inventoryRiskCount", inventoryRiskCount);
                        result.put("urgentOrderCount", urgentOrderCount);
                        result.put("shipRiskCount", shipRiskCount);

                        result.put("actualUnitCost", round2(actualUnitCost));
                        result.put("standardUnitCost", round2(standardUnitCost));
                        result.put("costVarianceRate", round2(costVarianceRate));
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }

        return result;
    }

    // =========================================================
    // 3. 리스크 TOP 5
    // 출하 리스크는 SHIPMENT 최신일 기준
    // =========================================================
    public List<Map<String, Object>> selectRiskList(Date baseDate) throws Exception {
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

        try (Connection conn = getConnection()) {
            Date shipmentBaseDate = selectLatestUsedDate(conn, "SHIPMENT", "NVL(DUE_DATE, SHIP_DATE)");
            if (shipmentBaseDate == null) {
                shipmentBaseDate = baseDate;
            }

            String sql =
                    "SELECT category, title, target_name, severity, detail " +
                    "  FROM ( " +
                    "        SELECT category, title, target_name, severity, detail, score " +
                    "          FROM ( " +
                    "                SELECT '설비' AS category, " +
                    "                       NVL(E.EQUIPMENT_NAME, '설비') || ' 비가동 ' || NVL(TO_CHAR(ED.DOWNTIME_MIN), '0') || '분' AS title, " +
                    "                       NVL(L.LINE_NAME, L.LINE_CODE) AS target_name, " +
                    "                       CASE " +
                    "                           WHEN NVL(ED.DOWNTIME_MIN, 0) >= 60 THEN 'HIGH' " +
                    "                           WHEN NVL(ED.DOWNTIME_MIN, 0) >= 30 THEN 'MEDIUM' " +
                    "                           ELSE 'LOW' " +
                    "                       END AS severity, " +
                    "                       NVL(ED.CAUSE_DETAIL, NVL(ED.CAUSE_CODE, '원인 미등록')) AS detail, " +
                    "                       NVL(ED.DOWNTIME_MIN, 0) + 300 AS score " +
                    "                  FROM EQUIPMENT_DOWNTIME ED " +
                    "                  LEFT JOIN EQUIPMENT E ON E.EQUIPMENT_ID = ED.EQUIPMENT_ID " +
                    "                  LEFT JOIN LINE L ON L.LINE_ID = ED.LINE_ID " +
                    "                 WHERE TRUNC(ED.START_TIME) = ? " +
                    "                   AND NVL(ED.USE_YN, 'Y') = 'Y' " +
                    "                UNION ALL " +
                    "                SELECT '자재' AS category, " +
                    "                       I.ITEM_NAME || ' 안전재고 미달' AS title, " +
                    "                       I.ITEM_CODE AS target_name, " +
                    "                       CASE " +
                    "                           WHEN NVL(IV.QTY_ON_HAND, 0) < NVL(IV.SAFETY_STOCK, 0) * 0.5 THEN 'HIGH' " +
                    "                           WHEN NVL(IV.QTY_ON_HAND, 0) < NVL(IV.SAFETY_STOCK, 0) THEN 'MEDIUM' " +
                    "                           ELSE 'LOW' " +
                    "                       END AS severity, " +
                    "                       '현재재고 ' || TO_CHAR(NVL(IV.QTY_ON_HAND, 0)) || ' / 안전재고 ' || TO_CHAR(NVL(IV.SAFETY_STOCK, 0)) AS detail, " +
                    "                       NVL(ROUND(((NVL(IV.SAFETY_STOCK, 0) - NVL(IV.QTY_ON_HAND, 0)) / NULLIF(IV.SAFETY_STOCK, 0)) * 100, 0), 0) + 150 AS score " +
                    "                  FROM INVENTORY IV " +
                    "                  JOIN ITEM I ON I.ITEM_ID = IV.ITEM_ID " +
                    "                 WHERE NVL(I.USE_YN, 'Y') = 'Y' " +
                    "                   AND NVL(IV.QTY_ON_HAND, 0) < NVL(IV.SAFETY_STOCK, 0) " +
                    "                UNION ALL " +
                    "                SELECT '품질' AS category, " +
                    "                       I.ITEM_NAME || ' 품질 이슈' AS title, " +
                    "                       DC.DEFECT_NAME AS target_name, " +
                    "                       CASE " +
                    "                           WHEN COUNT(*) >= 3 THEN 'HIGH' " +
                    "                           WHEN COUNT(*) = 2 THEN 'MEDIUM' " +
                    "                           ELSE 'LOW' " +
                    "                       END AS severity, " +
                    "                       DC.DEFECT_NAME || ' ' || COUNT(*) || '건' AS detail, " +
                    "                       COUNT(*) * 50 + 100 AS score " +
                    "                  FROM DEFECT_PRODUCT DP " +
                    "                  JOIN FINAL_INSPECTION FI ON FI.FINAL_INSPECTION_ID = DP.FINAL_INSPECTION_ID " +
                    "                  JOIN DEFECT_CODE DC ON DC.DEFECT_CODE_ID = DP.DEFECT_CODE_ID " +
                    "                  JOIN PRODUCTION_RESULT PR ON PR.RESULT_ID = FI.RESULT_ID " +
                    "                  JOIN WORK_ORDER WO ON WO.WORK_ORDER_ID = PR.WORK_ORDER_ID " +
                    "                  JOIN ITEM I ON I.ITEM_ID = WO.ITEM_ID " +
                    "                 WHERE TRUNC(FI.INSPECTION_DATE) = ? " +
                    "                   AND NVL(DP.USE_YN, 'Y') = 'Y' " +
                    "                 GROUP BY I.ITEM_NAME, DC.DEFECT_NAME " +
                    "                UNION ALL " +
                    "                SELECT '출하' AS category, " +
                    "                       NVL(C.CUSTOMER_NAME, '고객') || ' 출하 지연 위험' AS title, " +
                    "                       NVL(I.ITEM_NAME, '품목') AS target_name, " +
                    "                       'HIGH' AS severity, " +
                    "                       NVL(S.DELAY_REASON, '납기 지연') AS detail, " +
                    "                       200 AS score " +
                    "                  FROM SHIPMENT S " +
                    "                  LEFT JOIN SALES_ORDER SO ON SO.SALES_ORDER_ID = S.SALES_ORDER_ID " +
                    "                  LEFT JOIN CUSTOMER C ON C.CUSTOMER_ID = SO.CUSTOMER_ID " +
                    "                  LEFT JOIN ITEM I ON I.ITEM_ID = S.ITEM_ID " +
                    "                 WHERE TRUNC(NVL(S.DUE_DATE, S.SHIP_DATE)) = ? " +
                    "                   AND NVL(S.USE_YN, 'Y') = 'Y' " +
                    "                   AND NVL(S.ON_TIME_YN, 'N') = 'N' " +
                    "               ) " +
                    "         ORDER BY score DESC, title ASC " +
                    "       ) " +
                    " WHERE ROWNUM <= 5";

            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setDate(1, baseDate);
                ps.setDate(2, baseDate);
                ps.setDate(3, shipmentBaseDate);

                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        Map<String, Object> row = new LinkedHashMap<String, Object>();
                        row.put("category", rs.getString("category"));
                        row.put("title", rs.getString("title"));
                        row.put("targetName", rs.getString("target_name"));
                        row.put("severity", rs.getString("severity"));
                        row.put("detail", rs.getString("detail"));
                        list.add(row);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }

        return list;
    }

    // =========================================================
    // 4. 공장 전체 상태 맵
    // =========================================================
    public List<Map<String, Object>> selectFactoryStatusList(Date baseDate) throws Exception {
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        Map<String, Object> kpi = selectKpi(baseDate);

        double productionRate = getDouble(kpi.get("productionRate"));
        double defectRate = getDouble(kpi.get("defectRate"));
        int shipDelayCount = getInt(kpi.get("delayCount"));

        try (Connection conn = getConnection()) {

            int prodIssueCount = queryForInt(conn,
                    "SELECT COUNT(*) " +
                    "  FROM WORK_ORDER " +
                    " WHERE TRUNC(WORK_DATE) = ? " +
                    "   AND NVL(USE_YN, 'Y') = 'Y' " +
                    "   AND NVL(STATUS, '대기') <> '완료'",
                    baseDate);

            list.add(buildStatusRow("생산",
                    roundInt(productionRate),
                    prodIssueCount,
                    "계획 대비 실적 기준"));

            int qualityIssueCount = queryForInt(conn,
                    "SELECT COUNT(*) " +
                    "  FROM FINAL_INSPECTION " +
                    " WHERE TRUNC(INSPECTION_DATE) = ? " +
                    "   AND NVL(USE_YN, 'Y') = 'Y' " +
                    "   AND NVL(STATUS, '대기') <> '합격'",
                    baseDate);

            int qualityScore = clampScore(100 - roundInt(defectRate * 8));
            list.add(buildStatusRow("품질",
                    qualityScore,
                    qualityIssueCount,
                    "최종검사 불합격 및 불량 기준"));

            int equipIssueCount = queryForInt(conn,
                    "SELECT COUNT(*) " +
                    "  FROM EQUIPMENT_DOWNTIME " +
                    " WHERE TRUNC(START_TIME) = ? " +
                    "   AND NVL(USE_YN, 'Y') = 'Y'",
                    baseDate);

            int downtimeMin = queryForInt(conn,
                    "SELECT NVL(SUM(DOWNTIME_MIN), 0) " +
                    "  FROM EQUIPMENT_DOWNTIME " +
                    " WHERE TRUNC(START_TIME) = ? " +
                    "   AND NVL(USE_YN, 'Y') = 'Y'",
                    baseDate);

            int equipScore = clampScore(100 - (downtimeMin / 3));
            list.add(buildStatusRow("설비",
                    equipScore,
                    equipIssueCount,
                    "비가동 시간 기준"));

            int materialIssueCount = queryForInt(conn,
                    "SELECT COUNT(*) " +
                    "  FROM INVENTORY " +
                    " WHERE NVL(QTY_ON_HAND, 0) < NVL(SAFETY_STOCK, 0)");

            int materialScore = clampScore(100 - (materialIssueCount * 12));
            list.add(buildStatusRow("자재",
                    materialScore,
                    materialIssueCount,
                    "안전재고 미달 기준"));

            int shipmentScore = clampScore(100 - (shipDelayCount * 20));
            list.add(buildStatusRow("출하",
                    shipmentScore,
                    shipDelayCount,
                    "납기 지연 기준"));
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }

        return list;
    }
    
    // =========================================================
    // 5. 원인 분석 요약
    // =========================================================
    public List<Map<String, Object>> selectDowntimeCauseList(Date baseDate) throws Exception {
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

        String sql =
                "SELECT cause_name, total_min " +
                "  FROM ( " +
                "        SELECT NVL(CAUSE_DETAIL, NVL(CAUSE_CODE, '미분류')) AS cause_name, " +
                "               SUM(NVL(DOWNTIME_MIN, 0)) AS total_min " +
                "          FROM EQUIPMENT_DOWNTIME " +
                "         WHERE TRUNC(START_TIME) = ? " +
                "           AND NVL(USE_YN, 'Y') = 'Y' " +
                "         GROUP BY NVL(CAUSE_DETAIL, NVL(CAUSE_CODE, '미분류')) " +
                "         ORDER BY total_min DESC, cause_name ASC " +
                "       ) " +
                " WHERE ROWNUM <= 3";

        try (
            Connection conn = getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)
        ) {
            ps.setDate(1, baseDate);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Map<String, Object> row = new LinkedHashMap<String, Object>();
                    row.put("causeName", rs.getString("cause_name"));
                    row.put("causeValue", round0(rs.getDouble("total_min")) + "분");
                    list.add(row);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }

        return list;
    }

    public List<Map<String, Object>> selectDefectCauseList(Date baseDate) throws Exception {
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

        String sql =
                "SELECT defect_name, defect_cnt " +
                "  FROM ( " +
                "        SELECT DC.DEFECT_NAME AS defect_name, COUNT(*) AS defect_cnt " +
                "          FROM DEFECT_PRODUCT DP " +
                "          JOIN FINAL_INSPECTION FI ON FI.FINAL_INSPECTION_ID = DP.FINAL_INSPECTION_ID " +
                "          JOIN DEFECT_CODE DC ON DC.DEFECT_CODE_ID = DP.DEFECT_CODE_ID " +
                "         WHERE TRUNC(FI.INSPECTION_DATE) = ? " +
                "           AND NVL(DP.USE_YN, 'Y') = 'Y' " +
                "         GROUP BY DC.DEFECT_NAME " +
                "         ORDER BY defect_cnt DESC, defect_name ASC " +
                "       ) " +
                " WHERE ROWNUM <= 3";

        try (
            Connection conn = getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)
        ) {
            ps.setDate(1, baseDate);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Map<String, Object> row = new LinkedHashMap<String, Object>();
                    row.put("causeName", rs.getString("defect_name"));
                    row.put("causeValue", rs.getInt("defect_cnt") + "건");
                    list.add(row);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }

        return list;
    }

    public List<Map<String, Object>> selectDelayCauseList(Date baseDate) throws Exception {
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

        try (Connection conn = getConnection()) {
            Date shipmentBaseDate = selectLatestUsedDate(conn, "SHIPMENT", "NVL(DUE_DATE, SHIP_DATE)");
            if (shipmentBaseDate == null) {
                shipmentBaseDate = baseDate;
            }

            String sql =
                    "SELECT delay_reason, delay_cnt " +
                    "  FROM ( " +
                    "        SELECT NVL(DELAY_REASON, '사유 미등록') AS delay_reason, COUNT(*) AS delay_cnt " +
                    "          FROM SHIPMENT " +
                    "         WHERE TRUNC(NVL(DUE_DATE, SHIP_DATE)) = ? " +
                    "           AND NVL(USE_YN, 'Y') = 'Y' " +
                    "           AND NVL(ON_TIME_YN, 'N') = 'N' " +
                    "         GROUP BY NVL(DELAY_REASON, '사유 미등록') " +
                    "         ORDER BY delay_cnt DESC, delay_reason ASC " +
                    "       ) " +
                    " WHERE ROWNUM <= 3";

            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setDate(1, shipmentBaseDate);

                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        Map<String, Object> row = new LinkedHashMap<String, Object>();
                        row.put("causeName", rs.getString("delay_reason"));
                        row.put("causeValue", rs.getInt("delay_cnt") + "건");
                        list.add(row);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }

        return list;
    }

    // =========================================================
    // 6. 승인 필요 항목
    // 현재 승인 테이블 없어서 빈 리스트 반환
    // =========================================================
    public List<Map<String, Object>> selectApprovalList(Date baseDate) {
        return new ArrayList<Map<String, Object>>();
    }

    // =========================================================
    // 7. 추이 차트
    // =========================================================
    public List<Map<String, Object>> selectProductionTrendList(Date baseDate) throws Exception {
        String sql =
                "WITH days AS ( " +
                "    SELECT TRUNC(?) - (LEVEL - 1) AS dt " +
                "      FROM DUAL " +
                "   CONNECT BY LEVEL <= 7 " +
                "), agg AS ( " +
                "    SELECT TRUNC(RESULT_DATE) AS dt, SUM(PRODUCED_QTY) AS val " +
                "      FROM PRODUCTION_RESULT " +
                "     WHERE TRUNC(RESULT_DATE) BETWEEN TRUNC(?) - 6 AND TRUNC(?) " +
                "       AND NVL(USE_YN, 'Y') = 'Y' " +
                "     GROUP BY TRUNC(RESULT_DATE) " +
                ") " +
                "SELECT TO_CHAR(d.dt, 'MM-DD') AS label, NVL(a.val, 0) AS value " +
                "  FROM days d " +
                "  LEFT JOIN agg a ON a.dt = d.dt " +
                " ORDER BY d.dt";

        return selectTrendList(baseDate, sql);
    }

    public List<Map<String, Object>> selectQualityTrendList(Date baseDate) throws Exception {
        String sql =
                "WITH days AS ( " +
                "    SELECT TRUNC(?) - (LEVEL - 1) AS dt " +
                "      FROM DUAL " +
                "   CONNECT BY LEVEL <= 7 " +
                "), agg AS ( " +
                "    SELECT TRUNC(INSPECTION_DATE) AS dt, " +
                "           ROUND((SUM(CASE WHEN STATUS = '합격' THEN INSPECT_QTY ELSE 0 END) / NULLIF(SUM(INSPECT_QTY), 0)) * 100, 1) AS val " +
                "      FROM FINAL_INSPECTION " +
                "     WHERE TRUNC(INSPECTION_DATE) BETWEEN TRUNC(?) - 6 AND TRUNC(?) " +
                "       AND NVL(USE_YN, 'Y') = 'Y' " +
                "     GROUP BY TRUNC(INSPECTION_DATE) " +
                ") " +
                "SELECT TO_CHAR(d.dt, 'MM-DD') AS label, NVL(a.val, 0) AS value " +
                "  FROM days d " +
                "  LEFT JOIN agg a ON a.dt = d.dt " +
                " ORDER BY d.dt";

        return selectTrendList(baseDate, sql);
    }

    public List<Map<String, Object>> selectShipmentTrendList(Date baseDate) throws Exception {
        try (Connection conn = getConnection()) {
            Date shipmentBaseDate = selectLatestUsedDate(conn, "SHIPMENT", "SHIP_DATE");
            if (shipmentBaseDate == null) {
                shipmentBaseDate = baseDate;
            }

            String sql =
                    "WITH days AS ( " +
                    "    SELECT TRUNC(?) - (LEVEL - 1) AS dt " +
                    "      FROM DUAL " +
                    "   CONNECT BY LEVEL <= 7 " +
                    "), agg AS ( " +
                    "    SELECT TRUNC(SHIP_DATE) AS dt, SUM(SHIP_QTY) AS val " +
                    "      FROM SHIPMENT " +
                    "     WHERE TRUNC(SHIP_DATE) BETWEEN TRUNC(?) - 6 AND TRUNC(?) " +
                    "       AND NVL(USE_YN, 'Y') = 'Y' " +
                    "     GROUP BY TRUNC(SHIP_DATE) " +
                    ") " +
                    "SELECT TO_CHAR(d.dt, 'MM-DD') AS label, NVL(a.val, 0) AS value " +
                    "  FROM days d " +
                    "  LEFT JOIN agg a ON a.dt = d.dt " +
                    " ORDER BY d.dt";

            return selectTrendList(shipmentBaseDate, sql);
        }
    }

    public List<Map<String, Object>> selectCostTrendList(Date baseDate) throws Exception {
        try (Connection conn = getConnection()) {
            Date costBaseDate = selectLatestUsedDate(conn, "ACTUAL_COST_DAILY", "COST_DATE");
            if (costBaseDate == null) {
                costBaseDate = baseDate;
            }

            String sql =
                    "WITH days AS ( " +
                    "    SELECT TRUNC(?) - (LEVEL - 1) AS dt " +
                    "      FROM DUAL " +
                    "   CONNECT BY LEVEL <= 7 " +
                    "), agg AS ( " +
                    "    SELECT TRUNC(COST_DATE) AS dt, AVG(ACTUAL_UNIT_COST) AS val " +
                    "      FROM ACTUAL_COST_DAILY " +
                    "     WHERE TRUNC(COST_DATE) BETWEEN TRUNC(?) - 6 AND TRUNC(?) " +
                    "       AND NVL(USE_YN, 'Y') = 'Y' " +
                    "     GROUP BY TRUNC(COST_DATE) " +
                    ") " +
                    "SELECT TO_CHAR(d.dt, 'MM-DD') AS label, NVL(a.val, 0) AS value " +
                    "  FROM days d " +
                    "  LEFT JOIN agg a ON a.dt = d.dt " +
                    " ORDER BY d.dt";

            return selectTrendList(costBaseDate, sql);
        }
    }

    // =========================================================
    // helper
    // =========================================================
    private List<Map<String, Object>> selectTrendList(Date baseDate, String sql) throws Exception {
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

        try (
            Connection conn = getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)
        ) {
            ps.setDate(1, baseDate);
            ps.setDate(2, baseDate);
            ps.setDate(3, baseDate);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Map<String, Object> row = new LinkedHashMap<String, Object>();
                    row.put("label", rs.getString("label"));
                    row.put("value", rs.getDouble("value"));
                    list.add(row);
                }
            }
        }

        return list;
    }

    private Date selectLatestUsedDate(Connection conn, String tableName, String dateExpr) throws SQLException {
        String sql =
                "SELECT MAX(TRUNC(" + dateExpr + ")) AS latest_date " +
                "  FROM " + tableName +
                " WHERE NVL(USE_YN, 'Y') = 'Y'";

        try (PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                return rs.getDate("latest_date");
            }
            return null;
        }
    }

    private Map<String, Object> buildStatusRow(String areaName, int score, int issueCount, String detail) {
        Map<String, Object> row = new LinkedHashMap<String, Object>();
        row.put("areaName", areaName);
        row.put("score", score);
        row.put("statusLabel", toStatusLabel(score));
        row.put("issueCount", issueCount);
        row.put("detail", detail);
        return row;
    }

    private int queryForInt(Connection conn, String sql, Date baseDate) throws SQLException {
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setDate(1, baseDate);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
                return 0;
            }
        }
    }

    private int queryForInt(Connection conn, String sql) throws SQLException {
        try (PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                return rs.getInt(1);
            }
            return 0;
        }
    }

    private double percent(double numerator, double denominator) {
        if (denominator == 0) {
            return 0;
        }
        return (numerator / denominator) * 100.0;
    }

    private int clampScore(int score) {
        if (score < 0) {
            return 0;
        }
        if (score > 100) {
            return 100;
        }
        return score;
    }

    private double clamp(double value, double min, double max) {
        if (value < min) {
            return min;
        }
        if (value > max) {
            return max;
        }
        return value;
    }

    private String toStatusLabel(int score) {
        if (score >= 90) {
            return "정상";
        }
        if (score >= 75) {
            return "주의";
        }
        return "위험";
    }

    private int getInt(Object obj) {
        if (obj == null) {
            return 0;
        }
        if (obj instanceof Number) {
            return ((Number) obj).intValue();
        }
        try {
            return Integer.parseInt(String.valueOf(obj));
        } catch (Exception e) {
            return 0;
        }
    }

    private double getDouble(Object obj) {
        if (obj == null) {
            return 0;
        }
        if (obj instanceof Number) {
            return ((Number) obj).doubleValue();
        }
        try {
            return Double.parseDouble(String.valueOf(obj));
        } catch (Exception e) {
            return 0;
        }
    }

    private int roundInt(double value) {
        return (int) Math.round(value);
    }

    private double round0(double value) {
        return Math.round(value);
    }

    private double round1(double value) {
        return Math.round(value * 10.0) / 10.0;
    }

    private double round2(double value) {
        return Math.round(value * 100.0) / 100.0;
    }

    private double round3(double value) {
        return Math.round(value * 1000.0) / 1000.0;
    }
}