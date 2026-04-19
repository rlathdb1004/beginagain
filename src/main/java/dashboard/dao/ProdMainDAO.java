package dashboard.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

public class ProdMainDAO {

	private Connection getConnection() throws Exception {
		Context ctx = new InitialContext();
		DataSource dataFactory = (DataSource) ctx.lookup("java:/comp/env/jdbc/oracle");
		return dataFactory.getConnection();
	}

	// =========================================================
	// 기준일
	// =========================================================
	public Date selectLatestBaseDate() throws Exception {
		Date baseDate = null;

		String sql = "SELECT MAX(base_date) AS base_date " + "  FROM ( "
				+ "        SELECT MAX(TRUNC(RESULT_DATE)) AS base_date " + "          FROM PRODUCTION_RESULT "
				+ "         WHERE NVL(USE_YN, 'Y') = 'Y' " + "        UNION ALL "
				+ "        SELECT MAX(TRUNC(PLAN_DATE)) AS base_date " + "          FROM PRODUCTION_PLAN "
				+ "         WHERE NVL(USE_YN, 'Y') = 'Y' " + "        UNION ALL "
				+ "        SELECT MAX(TRUNC(INSPECTION_DATE)) AS base_date " + "          FROM FINAL_INSPECTION "
				+ "         WHERE NVL(USE_YN, 'Y') = 'Y' " + "       )";

		try (Connection conn = getConnection();
				PreparedStatement ps = conn.prepareStatement(sql);
				ResultSet rs = ps.executeQuery()) {
			if (rs.next()) {
				baseDate = rs.getDate("base_date");
			}
		}

		return baseDate;
	}

	// =========================================================
	// KPI 원값
	// service 에서 생산계획 달성률 / 수율 / OTD / OEE 계산
	// =========================================================
	public Map<String, Object> selectKpi(Date baseDate) throws Exception {
		Map<String, Object> result = new HashMap<String, Object>();

		String sql = "SELECT " + "    NVL(plan_data.plan_qty, 0) AS plan_qty, "
				+ "    NVL(prod_data.actual_qty, 0) AS actual_qty, "
				+ "    NVL(prod_data.defect_qty, 0) AS defect_qty, "
				+ "    NVL(unit_data.qty_unit_label, 'kg') AS qty_unit_label, "
				+ "    NVL(downtime_data.downtime_min, 0) AS downtime_min, "
				+ "    NVL(line_data.total_line_count, 0) AS total_line_count, "
				+ "    NVL(shipment_data.delivery_target_count, 0) AS delivery_target_count, "
				+ "    NVL(shipment_data.on_time_count, 0) AS on_time_count " + "FROM "
				+ "    (SELECT SUM(PP.PLAN_QTY) AS plan_qty " + "       FROM PRODUCTION_PLAN PP "
				+ "      WHERE TRUNC(PP.PLAN_DATE) = TRUNC(?) " + "        AND NVL(PP.USE_YN, 'Y') = 'Y') plan_data, " +

				"    (SELECT SUM(PR.PRODUCED_QTY) AS actual_qty, " + "            SUM(PR.LOSS_QTY) AS defect_qty "
				+ "       FROM PRODUCTION_RESULT PR " + "      WHERE TRUNC(PR.RESULT_DATE) = TRUNC(?) "
				+ "        AND NVL(PR.USE_YN, 'Y') = 'Y') prod_data, " +

				"    (SELECT CASE "
				+ "               WHEN COUNT(DISTINCT NVL(I.UNIT, 'kg')) = 1 THEN MAX(NVL(I.UNIT, 'kg')) "
				+ "               ELSE 'kg' " + "            END AS qty_unit_label "
				+ "       FROM PRODUCTION_RESULT PR "
				+ "       JOIN WORK_ORDER WO ON WO.WORK_ORDER_ID = PR.WORK_ORDER_ID "
				+ "       JOIN ITEM I ON I.ITEM_ID = WO.ITEM_ID " + "      WHERE TRUNC(PR.RESULT_DATE) = TRUNC(?) "
				+ "        AND NVL(PR.USE_YN, 'Y') = 'Y') unit_data, " +

				"    (SELECT SUM(ED.DOWNTIME_MIN) AS downtime_min " + "       FROM EQUIPMENT_DOWNTIME ED "
				+ "      WHERE TRUNC(ED.START_TIME) = TRUNC(?) "
				+ "        AND NVL(ED.USE_YN, 'Y') = 'Y') downtime_data, " +

				"    (SELECT COUNT(*) AS total_line_count " + "       FROM LINE L "
				+ "      WHERE NVL(L.USE_YN, 'Y') = 'Y') line_data, " +

				"    (SELECT COUNT(*) AS delivery_target_count, "
				+ "            SUM(CASE WHEN NVL(S.ON_TIME_YN, 'N') = 'Y' THEN 1 ELSE 0 END) AS on_time_count "
				+ "       FROM SHIPMENT S " + "      WHERE TRUNC(NVL(S.DUE_DATE, S.SHIP_DATE)) = TRUNC(?) "
				+ "        AND NVL(S.USE_YN, 'Y') = 'Y') shipment_data";

		try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
			ps.setDate(1, baseDate);
			ps.setDate(2, baseDate);
			ps.setDate(3, baseDate);
			ps.setDate(4, baseDate);
			ps.setDate(5, baseDate);

			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					result.put("planQty", rs.getDouble("plan_qty"));
					result.put("actualQty", rs.getDouble("actual_qty"));
					result.put("defectQty", rs.getDouble("defect_qty"));
					result.put("qtyUnitLabel", rs.getString("qty_unit_label"));
					result.put("downtimeMin", rs.getDouble("downtime_min"));
					result.put("totalLineCount", rs.getDouble("total_line_count"));
					result.put("deliveryTargetCount", rs.getDouble("delivery_target_count"));
					result.put("onTimeCount", rs.getDouble("on_time_count"));
				}
			}
		}

		return result;
	}

	// =========================================================
	// 라인별 생산 현황
	// =========================================================
	public List<Map<String, Object>> selectLineStatusList(Date baseDate) throws Exception {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

		String sql = "WITH wo_base AS ( " + "    SELECT WO.WORK_ORDER_ID, "
				+ "           NVL(WO.WORK_QTY, 0) AS plan_qty, " + "           NVL(WO.STATUS, '대기') AS status, "
				+ "           PP.LINE_CODE, "
				+ "           ROW_NUMBER() OVER (PARTITION BY PP.LINE_CODE ORDER BY WO.WORK_ORDER_ID DESC) AS rn "
				+ "      FROM WORK_ORDER WO " + "      LEFT JOIN PRODUCTION_PLAN PP ON PP.PLAN_ID = WO.PLAN_ID "
				+ "     WHERE TRUNC(NVL(WO.CREATED_AT, PP.PLAN_DATE)) = TRUNC(?) "
				+ "       AND NVL(WO.USE_YN, 'Y') = 'Y' " + "), pr_agg AS ( "
				+ "    SELECT PR.WORK_ORDER_ID, SUM(PR.PRODUCED_QTY) AS actual_qty "
				+ "      FROM PRODUCTION_RESULT PR " + "     WHERE TRUNC(PR.RESULT_DATE) = TRUNC(?) "
				+ "       AND NVL(PR.USE_YN, 'Y') = 'Y' " + "     GROUP BY PR.WORK_ORDER_ID " + ") "
				+ "SELECT NVL(L.LINE_NAME, '-') || '[' || NVL(L.LINE_TYPE, '-') || ']' AS line_name, " + "       CASE "
				+ "           WHEN WB.WORK_ORDER_ID IS NULL THEN '대기' " + "           WHEN WB.STATUS = '완료' THEN '완료' "
				+ "           WHEN WB.STATUS IN ('진행중', '작업중', '생산중') THEN '가동중' " + "           ELSE WB.STATUS "
				+ "       END AS status_label, " + "       CASE " + "           WHEN NVL(WB.plan_qty, 0) = 0 THEN 0 "
				+ "           ELSE ROUND((NVL(PR.actual_qty, 0) / WB.plan_qty) * 100, 1) "
				+ "       END AS achievement_rate " + "  FROM LINE L "
				+ "  LEFT JOIN wo_base WB ON WB.LINE_CODE = L.LINE_CODE AND WB.rn = 1 "
				+ "  LEFT JOIN pr_agg PR ON PR.WORK_ORDER_ID = WB.WORK_ORDER_ID " + " WHERE NVL(L.USE_YN, 'Y') = 'Y' "
				+ " ORDER BY L.LINE_ID";

		try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
			ps.setDate(1, baseDate);
			ps.setDate(2, baseDate);

			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {
					Map<String, Object> row = new LinkedHashMap<String, Object>();
					row.put("lineName", rs.getString("line_name"));
					row.put("statusLabel", rs.getString("status_label"));
					row.put("achievementRate", rs.getDouble("achievement_rate"));
					list.add(row);
				}
			}
		}

		return list;
	}

	// =========================================================
	// 라인별 설비 가동/비가동
	// =========================================================
	public List<Map<String, Object>> selectLineRuntimeList(Date baseDate) throws Exception {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

		String sql = "WITH dt AS ( " + "    SELECT ED.LINE_ID, SUM(ED.DOWNTIME_MIN) AS downtime_min "
				+ "      FROM EQUIPMENT_DOWNTIME ED " + "     WHERE TRUNC(ED.START_TIME) = TRUNC(?) "
				+ "       AND NVL(ED.USE_YN, 'Y') = 'Y' " + "     GROUP BY ED.LINE_ID " + ") " + "SELECT L.LINE_ID, "
				+ "       NVL(L.LINE_NAME, '-') || '[' || NVL(L.LINE_TYPE, '-') || ']' AS line_name, "
				+ "       NVL(DT.downtime_min, 0) AS downtime_min " + "  FROM LINE L "
				+ "  LEFT JOIN dt DT ON DT.LINE_ID = L.LINE_ID " + " WHERE NVL(L.USE_YN, 'Y') = 'Y' "
				+ " ORDER BY L.LINE_ID";

		try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
			ps.setDate(1, baseDate);

			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {
					Map<String, Object> row = new LinkedHashMap<String, Object>();
					row.put("lineId", rs.getInt("line_id"));
					row.put("lineName", rs.getString("line_name"));
					row.put("downtimeMin", rs.getDouble("downtime_min"));
					list.add(row);
				}
			}
		}

		return list;
	}

	// =========================================================
	// 비가동 원인
	// =========================================================
	public List<Map<String, Object>> selectDowntimeCauseList(Date baseDate) throws Exception {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

		String sql = "SELECT NVL(L.LINE_NAME, '-') || '[' || NVL(L.LINE_TYPE, '-') || ']' AS line_name, "
				+ "       NVL(E.EQUIPMENT_NAME, '-') AS equipment_name, "
				+ "       NVL(ED.DOWNTIME_MIN, 0) AS downtime_min, " + "       NVL(ED.CAUSE_CODE, '-') AS cause_code, "
				+ "       NVL(ED.CAUSE_DETAIL, '-') AS cause_detail, "
				+ "       NVL(TO_CHAR(ED.START_TIME, 'YYYY-MM-DD HH24:MI'), '-') AS start_time_text "
				+ "  FROM EQUIPMENT_DOWNTIME ED " + "  LEFT JOIN LINE L ON L.LINE_ID = ED.LINE_ID "
				+ "  LEFT JOIN EQUIPMENT E ON E.EQUIPMENT_ID = ED.EQUIPMENT_ID "
				+ " WHERE TRUNC(ED.START_TIME) = TRUNC(?) " + "   AND NVL(ED.USE_YN, 'Y') = 'Y' "
				+ " ORDER BY ED.DOWNTIME_MIN DESC, ED.START_TIME DESC";

		try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
			ps.setDate(1, baseDate);

			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {
					Map<String, Object> row = new LinkedHashMap<String, Object>();
					row.put("lineName", rs.getString("line_name"));
					row.put("equipmentName", rs.getString("equipment_name"));
					row.put("downtimeMin", rs.getDouble("downtime_min"));
					row.put("causeCode", rs.getString("cause_code"));
					row.put("causeDetail", rs.getString("cause_detail"));
					row.put("startTimeText", rs.getString("start_time_text"));
					list.add(row);
				}
			}
		}

		return list;
	}

	// =========================================================
	// 자재 알림
	// =========================================================
	public List<Map<String, Object>> selectMaterialAlertList(Date baseDate) throws Exception {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

		String sql = "SELECT * " + "  FROM ( " + "        SELECT I.ITEM_NAME, "
				+ "               '현재재고 ' || TO_CHAR(NVL(IV.QTY_ON_HAND, 0)) || "
				+ "               ' / 안전재고 ' || TO_CHAR(NVL(IV.SAFETY_STOCK, 0)) AS detail "
				+ "          FROM INVENTORY IV " + "          JOIN ITEM I ON I.ITEM_ID = IV.ITEM_ID "
				+ "         WHERE NVL(I.USE_YN, 'Y') = 'Y' "
				+ "           AND NVL(IV.QTY_ON_HAND, 0) < NVL(IV.SAFETY_STOCK, 0) "
				+ "         ORDER BY (NVL(IV.SAFETY_STOCK, 0) - NVL(IV.QTY_ON_HAND, 0)) DESC, I.ITEM_NAME ASC "
				+ "       ) " + " WHERE ROWNUM <= 10";

		try (Connection conn = getConnection();
				PreparedStatement ps = conn.prepareStatement(sql);
				ResultSet rs = ps.executeQuery()) {
			while (rs.next()) {
				Map<String, Object> row = new LinkedHashMap<String, Object>();
				row.put("itemName", rs.getString("item_name"));
				row.put("detail", rs.getString("detail"));
				list.add(row);
			}
		}

		return list;
	}

	// =========================================================
	// 설비 알림
	// =========================================================
	public List<Map<String, Object>> selectEquipmentAlertList(Date baseDate) throws Exception {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

		String sql = "SELECT * " + "  FROM ( " + "        SELECT NVL(E.EQUIPMENT_NAME, '설비') AS equipment_name, "
				+ "               NVL(TO_CHAR(ED.DOWNTIME_MIN), '0') || '분 / ' || NVL(ED.CAUSE_DETAIL, NVL(ED.CAUSE_CODE, '원인 미등록')) AS detail "
				+ "          FROM EQUIPMENT_DOWNTIME ED "
				+ "          LEFT JOIN EQUIPMENT E ON E.EQUIPMENT_ID = ED.EQUIPMENT_ID "
				+ "         WHERE TRUNC(ED.START_TIME) = TRUNC(?) " + "           AND NVL(ED.USE_YN, 'Y') = 'Y' "
				+ "         ORDER BY ED.DOWNTIME_MIN DESC, ED.START_TIME DESC " + "       ) " + " WHERE ROWNUM <= 10";

		try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
			ps.setDate(1, baseDate);

			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {
					Map<String, Object> row = new LinkedHashMap<String, Object>();
					row.put("equipmentName", rs.getString("equipment_name"));
					row.put("detail", rs.getString("detail"));
					list.add(row);
				}
			}
		}

		return list;
	}

	// =========================================================
	// 생산계획 달성률 상세
	// =========================================================
	public List<Map<String, Object>> selectPlanDetailList(Date baseDate) throws Exception {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

		String sql = "WITH wo_plan AS ( " + "    SELECT WO.PLAN_ID, " + "           WO.WORK_ORDER_ID, "
				+ "           WO.EMP_ID, " + "           NVL(WO.STATUS, '대기') AS status, "
				+ "           'WO-' || TO_CHAR(NVL(WO.CREATED_AT, PP.PLAN_DATE), 'YYYYMMDD') || '-' || WO.WORK_ORDER_ID AS work_order_no, "
				+ "           ROW_NUMBER() OVER (PARTITION BY WO.PLAN_ID ORDER BY WO.WORK_ORDER_ID DESC) AS rn "
				+ "      FROM WORK_ORDER WO " + "      LEFT JOIN PRODUCTION_PLAN PP ON PP.PLAN_ID = WO.PLAN_ID "
				+ "     WHERE TRUNC(NVL(WO.CREATED_AT, PP.PLAN_DATE)) = TRUNC(?) "
				+ "       AND NVL(WO.USE_YN, 'Y') = 'Y' " + "), pr_plan AS ( "
				+ "    SELECT WO.PLAN_ID, SUM(PR.PRODUCED_QTY) AS actual_qty " + "      FROM WORK_ORDER WO "
				+ "      JOIN PRODUCTION_RESULT PR ON PR.WORK_ORDER_ID = WO.WORK_ORDER_ID "
				+ "      LEFT JOIN PRODUCTION_PLAN PP ON PP.PLAN_ID = WO.PLAN_ID "
				+ "     WHERE TRUNC(PR.RESULT_DATE) = TRUNC(?) " + "       AND NVL(PR.USE_YN, 'Y') = 'Y' "
				+ "       AND NVL(WO.USE_YN, 'Y') = 'Y' " + "     GROUP BY WO.PLAN_ID " + ") "
				+ "SELECT 'PP-' || TO_CHAR(NVL(PP.CREATED_AT, PP.PLAN_DATE), 'YYYYMMDD') || '-' || PP.PLAN_ID AS plan_no, "
				+ "       NVL(WP.work_order_no, '-') AS work_order_no, " + "       NVL(I.ITEM_NAME, '-') AS item_name, "
				+ "       NVL(PP.PLAN_QTY, 0) AS plan_qty, " + "       NVL(PR.actual_qty, 0) AS actual_qty, "
				+ "       NVL(I.UNIT, 'kg') AS unit, " + "       TO_CHAR(PP.PLAN_DATE, 'YYYY-MM-DD') AS due_date_text, "
				+ "       NVL(E.EMP_NAME, '-') AS worker_name, " + "       NVL(WP.status, '-') AS status "
				+ "  FROM PRODUCTION_PLAN PP " + "  LEFT JOIN ITEM I ON I.ITEM_ID = PP.ITEM_ID "
				+ "  LEFT JOIN wo_plan WP ON WP.PLAN_ID = PP.PLAN_ID AND WP.rn = 1 "
				+ "  LEFT JOIN pr_plan PR ON PR.PLAN_ID = PP.PLAN_ID " + "  LEFT JOIN EMP E ON E.EMP_ID = WP.EMP_ID "
				+ " WHERE TRUNC(PP.PLAN_DATE) = TRUNC(?) " + "   AND NVL(PP.USE_YN, 'Y') = 'Y' "
				+ " ORDER BY PP.PLAN_ID DESC";

		try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
			ps.setDate(1, baseDate);
			ps.setDate(2, baseDate);
			ps.setDate(3, baseDate);

			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {
					Map<String, Object> row = new LinkedHashMap<String, Object>();
					row.put("planNo", rs.getString("plan_no"));
					row.put("workOrderNo", rs.getString("work_order_no"));
					row.put("itemName", rs.getString("item_name"));
					row.put("planQty", rs.getDouble("plan_qty"));
					row.put("actualQty", rs.getDouble("actual_qty"));
					row.put("unit", rs.getString("unit"));
					row.put("dueDateText", rs.getString("due_date_text"));
					row.put("workerName", rs.getString("worker_name"));
					row.put("status", rs.getString("status"));
					list.add(row);
				}
			}
		}

		return list;
	}

	// =========================================================
	// 수율 상세
	// =========================================================
	public List<Map<String, Object>> selectYieldDetailList(Date baseDate) throws Exception {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

		String sql = "SELECT 'PP-' || TO_CHAR(NVL(PP.CREATED_AT, PP.PLAN_DATE), 'YYYYMMDD') || '-' || PP.PLAN_ID AS plan_no, "
				+ "       'WO-' || TO_CHAR(NVL(WO.CREATED_AT, PP.PLAN_DATE), 'YYYYMMDD') || '-' || WO.WORK_ORDER_ID AS work_order_no, "
				+ "       'PR-' || TO_CHAR(PR.RESULT_DATE, 'YYYYMMDD') || '-' || PR.RESULT_ID AS result_no, "
				+ "       NVL(I.ITEM_NAME, '-') AS item_name, " + "       NVL(PR.PRODUCED_QTY, 0) AS actual_qty, "
				+ "       NVL(PR.LOSS_QTY, 0) AS defect_qty, " + "       NVL(I.UNIT, 'kg') AS unit, "
				+ "       NVL(E.EMP_NAME, '-') AS worker_name " + "  FROM PRODUCTION_RESULT PR "
				+ "  LEFT JOIN WORK_ORDER WO ON WO.WORK_ORDER_ID = PR.WORK_ORDER_ID "
				+ "  LEFT JOIN PRODUCTION_PLAN PP ON PP.PLAN_ID = WO.PLAN_ID "
				+ "  LEFT JOIN ITEM I ON I.ITEM_ID = WO.ITEM_ID " + "  LEFT JOIN EMP E ON E.EMP_ID = WO.EMP_ID "
				+ " WHERE TRUNC(PR.RESULT_DATE) = TRUNC(?) " + "   AND NVL(PR.USE_YN, 'Y') = 'Y' "
				+ " ORDER BY PR.RESULT_ID DESC";

		try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
			ps.setDate(1, baseDate);

			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {
					Map<String, Object> row = new LinkedHashMap<String, Object>();
					row.put("planNo", rs.getString("plan_no"));
					row.put("workOrderNo", rs.getString("work_order_no"));
					row.put("resultNo", rs.getString("result_no"));
					row.put("itemName", rs.getString("item_name"));
					row.put("actualQty", rs.getDouble("actual_qty"));
					row.put("defectQty", rs.getDouble("defect_qty"));
					row.put("unit", rs.getString("unit"));
					row.put("workerName", rs.getString("worker_name"));
					list.add(row);
				}
			}
		}

		return list;
	}

	// =========================================================
	// 납기 준수율 상세
	// =========================================================
	public List<Map<String, Object>> selectShipmentDetailList(Date baseDate) throws Exception {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

		String sql = "SELECT '-' AS plan_no, " + "       '-' AS work_order_no, "
				+ "       CASE WHEN S.SALES_ORDER_ID IS NULL THEN '-' ELSE 'SO-' || S.SALES_ORDER_ID END AS sales_order_no, "
				+ "       NVL(I.ITEM_NAME, '-') AS item_name, "
				+ "       'SH-' || TO_CHAR(NVL(S.SHIP_DATE, S.DUE_DATE), 'YYYYMMDD') || '-' || S.SHIPMENT_ID AS shipment_no, "
				+ "       TO_CHAR(S.DUE_DATE, 'YYYY-MM-DD') AS due_date_text, "
				+ "       TO_CHAR(S.SHIP_DATE, 'YYYY-MM-DD') AS ship_date_text, "
				+ "       NVL(S.ON_TIME_YN, '-') AS on_time_yn, " + "       NVL(S.SHIP_QTY, 0) AS ship_qty, "
				+ "       NVL(I.UNIT, 'kg') AS unit, " + "       '-' AS worker_name " + "  FROM SHIPMENT S "
				+ "  LEFT JOIN ITEM I ON I.ITEM_ID = S.ITEM_ID "
				+ " WHERE TRUNC(NVL(S.DUE_DATE, S.SHIP_DATE)) = TRUNC(?) " + "   AND NVL(S.USE_YN, 'Y') = 'Y' "
				+ " ORDER BY S.SHIPMENT_ID DESC";

		try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
			ps.setDate(1, baseDate);

			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {
					Map<String, Object> row = new LinkedHashMap<String, Object>();
					row.put("planNo", rs.getString("plan_no"));
					row.put("workOrderNo", rs.getString("work_order_no"));
					row.put("salesOrderNo", rs.getString("sales_order_no"));
					row.put("itemName", rs.getString("item_name"));
					row.put("shipmentNo", rs.getString("shipment_no"));
					row.put("dueDateText", rs.getString("due_date_text"));
					row.put("shipDateText", rs.getString("ship_date_text"));
					row.put("onTimeYn", rs.getString("on_time_yn"));
					row.put("shipQty", rs.getDouble("ship_qty"));
					row.put("unit", rs.getString("unit"));
					row.put("workerName", rs.getString("worker_name"));
					list.add(row);
				}
			}
		}

		return list;
	}

	// =========================================================
	// OEE 상세
	// =========================================================
	public List<Map<String, Object>> selectOeeDetailList(Date baseDate) throws Exception {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

		String sql = "WITH dt AS ( " + "    SELECT ED.LINE_ID, SUM(ED.DOWNTIME_MIN) AS downtime_min "
				+ "      FROM EQUIPMENT_DOWNTIME ED " + "     WHERE TRUNC(ED.START_TIME) = TRUNC(?) "
				+ "       AND NVL(ED.USE_YN, 'Y') = 'Y' " + "     GROUP BY ED.LINE_ID " + "), wo_latest AS ( "
				+ "    SELECT PP.LINE_CODE, " + "           PP.PLAN_ID, " + "           WO.WORK_ORDER_ID, "
				+ "           WO.EMP_ID, " + "           WO.ITEM_ID, "
				+ "           NVL(WO.CREATED_AT, PP.PLAN_DATE) AS created_at, "
				+ "           NVL(WO.WORK_QTY, 0) AS plan_qty, "
				+ "           ROW_NUMBER() OVER (PARTITION BY PP.LINE_CODE ORDER BY WO.WORK_ORDER_ID DESC) AS rn "
				+ "      FROM WORK_ORDER WO " + "      LEFT JOIN PRODUCTION_PLAN PP ON PP.PLAN_ID = WO.PLAN_ID "
				+ "     WHERE TRUNC(NVL(WO.CREATED_AT, PP.PLAN_DATE)) = TRUNC(?) "
				+ "       AND NVL(WO.USE_YN, 'Y') = 'Y' " + "), pr_agg AS ( " + "    SELECT PR.WORK_ORDER_ID, "
				+ "           SUM(PR.PRODUCED_QTY) AS actual_qty, " + "           SUM(PR.LOSS_QTY) AS defect_qty "
				+ "      FROM PRODUCTION_RESULT PR " + "     WHERE TRUNC(PR.RESULT_DATE) = TRUNC(?) "
				+ "       AND NVL(PR.USE_YN, 'Y') = 'Y' " + "     GROUP BY PR.WORK_ORDER_ID " + ") "
				+ "SELECT NVL(L.LINE_NAME, '-') || '[' || NVL(L.LINE_TYPE, '-') || ']' AS line_name, "
				+ "       'PP-' || TO_CHAR(NVL(PP.CREATED_AT, PP.PLAN_DATE), 'YYYYMMDD') || '-' || PP.PLAN_ID AS plan_no, "
				+ "       CASE " + "           WHEN WO.WORK_ORDER_ID IS NULL THEN '-' "
				+ "           ELSE 'WO-' || TO_CHAR(WO.created_at, 'YYYYMMDD') || '-' || WO.WORK_ORDER_ID "
				+ "       END AS work_order_no, " + "       NVL(I.ITEM_NAME, '-') AS item_name, "
				+ "       NVL(WO.plan_qty, 0) AS plan_qty, " + "       NVL(PR.actual_qty, 0) AS actual_qty, "
				+ "       NVL(PR.defect_qty, 0) AS defect_qty, " + "       NVL(DT.downtime_min, 0) AS downtime_min, "
				+ "       NVL(E.EMP_NAME, '-') AS worker_name " + "  FROM LINE L "
				+ "  LEFT JOIN wo_latest WO ON WO.LINE_CODE = L.LINE_CODE AND WO.rn = 1 "
				+ "  LEFT JOIN PRODUCTION_PLAN PP ON PP.PLAN_ID = WO.PLAN_ID "
				+ "  LEFT JOIN pr_agg PR ON PR.WORK_ORDER_ID = WO.WORK_ORDER_ID "
				+ "  LEFT JOIN ITEM I ON I.ITEM_ID = WO.ITEM_ID " + "  LEFT JOIN EMP E ON E.EMP_ID = WO.EMP_ID "
				+ "  LEFT JOIN dt DT ON DT.LINE_ID = L.LINE_ID " + " WHERE NVL(L.USE_YN, 'Y') = 'Y' "
				+ " ORDER BY L.LINE_ID";

		try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
			ps.setDate(1, baseDate);
			ps.setDate(2, baseDate);
			ps.setDate(3, baseDate);

			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {
					Map<String, Object> row = new LinkedHashMap<String, Object>();
					row.put("lineName", rs.getString("line_name"));
					row.put("planNo", rs.getString("plan_no"));
					row.put("workOrderNo", rs.getString("work_order_no"));
					row.put("itemName", rs.getString("item_name"));
					row.put("planQty", rs.getDouble("plan_qty"));
					row.put("actualQty", rs.getDouble("actual_qty"));
					row.put("defectQty", rs.getDouble("defect_qty"));
					row.put("downtimeMin", rs.getDouble("downtime_min"));
					row.put("workerName", rs.getString("worker_name"));
					list.add(row);
				}
			}
		}

		return list;
	}

	// =========================================================
	// 현재 화면에서 미사용
	// =========================================================
	public List<Map<String, Object>> selectWorkOrderList(Date baseDate) {
		return new ArrayList<Map<String, Object>>();
	}

	public List<Map<String, Object>> selectIssueList(Date baseDate) {
		return new ArrayList<Map<String, Object>>();
	}

	public List<Map<String, Object>> selectHourlyProductionList(Date baseDate) {
		return new ArrayList<Map<String, Object>>();
	}

	public List<Map<String, Object>> selectHandoverList(Date baseDate) {
		return new ArrayList<Map<String, Object>>();
	}
}