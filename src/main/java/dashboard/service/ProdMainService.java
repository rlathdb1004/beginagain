package dashboard.service;

import java.sql.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import dashboard.dao.ProdMainDAO;
import dashboard.dto.ProdMainDTO;

public class ProdMainService {

	private final ProdMainDAO dao = new ProdMainDAO();

	public ProdMainDTO getDashboard(Date requestBaseDate) throws Exception {
		ProdMainDTO dashboard = new ProdMainDTO();

		// 기준일은 항상 오늘
		Date baseDate = new Date(System.currentTimeMillis());
		dashboard.setBaseDate(baseDate);
		dashboard.setUpdatedAt(new java.sql.Timestamp(System.currentTimeMillis()));

		// 기본 조회
		Map<String, Object> kpi = nullSafeMap(dao.selectKpi(baseDate));
		List<Map<String, Object>> lineStatusList = nullSafeList(dao.selectLineStatusList(baseDate));
		List<Map<String, Object>> lineRuntimeList = nullSafeList(dao.selectLineRuntimeList(baseDate));
		List<Map<String, Object>> downtimeCauseList = nullSafeList(dao.selectDowntimeCauseList(baseDate));
		List<Map<String, Object>> materialAlertList = nullSafeList(dao.selectMaterialAlertList(baseDate));
		List<Map<String, Object>> equipmentAlertList = nullSafeList(dao.selectEquipmentAlertList(baseDate));

		// KPI 모달 상세 조회
		List<Map<String, Object>> planDetailList = nullSafeList(dao.selectPlanDetailList(baseDate));
		List<Map<String, Object>> yieldDetailList = nullSafeList(dao.selectYieldDetailList(baseDate));
		List<Map<String, Object>> shipmentDetailList = nullSafeList(dao.selectShipmentDetailList(baseDate));
		List<Map<String, Object>> oeeDetailList = nullSafeList(dao.selectOeeDetailList(baseDate));

		// KPI 계산
		enrichKpi(kpi);

		// 상세 리스트 가공
		enrichPlanDetailList(planDetailList);
		enrichYieldDetailList(yieldDetailList);
		enrichShipmentDetailList(shipmentDetailList);
		enrichLineRuntimeList(lineRuntimeList);
		enrichOeeDetailList(oeeDetailList);

		// DTO 세팅
		dashboard.setKpi(kpi);

		dashboard.setLineStatusList(lineStatusList);
		dashboard.setLineRuntimeList(lineRuntimeList);
		dashboard.setDowntimeCauseList(downtimeCauseList);

		dashboard.setMaterialAlertList(materialAlertList);
		dashboard.setEquipmentAlertList(equipmentAlertList);

		dashboard.setPlanDetailList(planDetailList);
		dashboard.setYieldDetailList(yieldDetailList);
		dashboard.setShipmentDetailList(shipmentDetailList);
		dashboard.setOeeDetailList(oeeDetailList);

		// 현재 화면에서 미사용이지만 구조 호환용으로 유지
		dashboard.setWorkOrderList(new ArrayList<Map<String, Object>>());
		dashboard.setIssueList(new ArrayList<Map<String, Object>>());
		dashboard.setHourlyProductionList(new ArrayList<Map<String, Object>>());
		dashboard.setHandoverList(new ArrayList<Map<String, Object>>());

		return dashboard;
	}

	private void enrichKpi(Map<String, Object> kpi) {
		double planQty = toDouble(kpi.get("planQty"));
		double actualQty = toDouble(kpi.get("actualQty"));
		double defectQty = toDouble(kpi.get("defectQty"));
		double downtimeMin = toDouble(kpi.get("downtimeMin"));
		double totalLineCount = toDouble(kpi.get("totalLineCount"));
		double deliveryTargetCount = toDouble(kpi.get("deliveryTargetCount"));
		double onTimeCount = toDouble(kpi.get("onTimeCount"));

		String qtyUnitLabel = toStr(kpi.get("qtyUnitLabel"));
		if (qtyUnitLabel.isEmpty()) {
			qtyUnitLabel = "kg";
		}

		double goodQty = actualQty - defectQty;
		if (goodQty < 0) {
			goodQty = 0;
		}

		double productionAchievementRate = percent(actualQty, planQty);
		double yieldRate = percent(goodQty, actualQty);
		double otdRate = percent(onTimeCount, deliveryTargetCount);

		double availabilityRate = 100.0;
		if (totalLineCount > 0) {
			double totalAvailableMinutes = totalLineCount * 1440.0;
			availabilityRate = 100.0 - ((downtimeMin / totalAvailableMinutes) * 100.0);
		}
		availabilityRate = round1(clamp(availabilityRate, 0, 100));

		double performanceRate = round1(clamp(productionAchievementRate, 0, 100));
		double qualityRate = round1(clamp(yieldRate, 0, 100));
		double oeeRate = round1((availabilityRate * performanceRate * qualityRate) / 10000.0);

		kpi.put("planQty", round3(planQty));
		kpi.put("actualQty", round3(actualQty));
		kpi.put("defectQty", round3(defectQty));
		kpi.put("goodQty", round3(goodQty));

		kpi.put("productionAchievementRate", round1(productionAchievementRate));
		kpi.put("yieldRate", round1(yieldRate));
		kpi.put("otdRate", round1(otdRate));
		kpi.put("oeeRate", round1(oeeRate));

		kpi.put("availabilityRate", availabilityRate);
		kpi.put("performanceRate", performanceRate);
		kpi.put("qualityRate", qualityRate);

		kpi.put("deliveryTargetCount", round1(deliveryTargetCount));
		kpi.put("onTimeCount", round1(onTimeCount));
		kpi.put("downtimeMin", round1(downtimeMin));
		kpi.put("totalLineCount", round1(totalLineCount));
		kpi.put("qtyUnitLabel", qtyUnitLabel);
	}

	private void enrichPlanDetailList(List<Map<String, Object>> list) {
		if (list == null || list.isEmpty()) {
			return;
		}

		for (Map<String, Object> row : list) {
			double planQty = toDouble(row.get("planQty"));
			double actualQty = toDouble(row.get("actualQty"));
			double achievementRate = percent(actualQty, planQty);

			row.put("planQty", round3(planQty));
			row.put("actualQty", round3(actualQty));
			row.put("achievementRate", round1(achievementRate));

			if (isEmpty(row.get("unit"))) {
				row.put("unit", "kg");
			}
			if (isEmpty(row.get("status"))) {
				row.put("status", "-");
			}
			if (isEmpty(row.get("planNo"))) {
				row.put("planNo", "-");
			}
			if (isEmpty(row.get("workOrderNo"))) {
				row.put("workOrderNo", "-");
			}
			if (isEmpty(row.get("itemName"))) {
				row.put("itemName", "-");
			}
			if (isEmpty(row.get("dueDateText"))) {
				row.put("dueDateText", "-");
			}
			if (isEmpty(row.get("workerName"))) {
				row.put("workerName", "-");
			}
		}
	}

	private void enrichYieldDetailList(List<Map<String, Object>> list) {
		if (list == null || list.isEmpty()) {
			return;
		}

		for (Map<String, Object> row : list) {
			double actualQty = toDouble(row.get("actualQty"));
			double defectQty = toDouble(row.get("defectQty"));
			double goodQty = actualQty - defectQty;

			if (goodQty < 0) {
				goodQty = 0;
			}

			double yieldRate = percent(goodQty, actualQty);

			row.put("actualQty", round3(actualQty));
			row.put("defectQty", round3(defectQty));
			row.put("goodQty", round3(goodQty));
			row.put("yieldRate", round1(yieldRate));

			if (isEmpty(row.get("unit"))) {
				row.put("unit", "kg");
			}
			if (isEmpty(row.get("planNo"))) {
				row.put("planNo", "-");
			}
			if (isEmpty(row.get("workOrderNo"))) {
				row.put("workOrderNo", "-");
			}
			if (isEmpty(row.get("resultNo"))) {
				row.put("resultNo", "-");
			}
			if (isEmpty(row.get("itemName"))) {
				row.put("itemName", "-");
			}
			if (isEmpty(row.get("workerName"))) {
				row.put("workerName", "-");
			}
		}
	}

	private void enrichShipmentDetailList(List<Map<String, Object>> list) {
		if (list == null || list.isEmpty()) {
			return;
		}

		for (Map<String, Object> row : list) {
			double shipQty = toDouble(row.get("shipQty"));

			row.put("shipQty", round3(shipQty));

			if (isEmpty(row.get("planNo"))) {
				row.put("planNo", "-");
			}
			if (isEmpty(row.get("workOrderNo"))) {
				row.put("workOrderNo", "-");
			}
			if (isEmpty(row.get("salesOrderNo"))) {
				row.put("salesOrderNo", "-");
			}
			if (isEmpty(row.get("itemName"))) {
				row.put("itemName", "-");
			}
			if (isEmpty(row.get("shipmentNo"))) {
				row.put("shipmentNo", "-");
			}
			if (isEmpty(row.get("dueDateText"))) {
				row.put("dueDateText", "-");
			}
			if (isEmpty(row.get("shipDateText"))) {
				row.put("shipDateText", "-");
			}
			if (isEmpty(row.get("onTimeYn"))) {
				row.put("onTimeYn", "-");
			}
			if (isEmpty(row.get("unit"))) {
				row.put("unit", "kg");
			}
			if (isEmpty(row.get("workerName"))) {
				row.put("workerName", "-");
			}
		}
	}

	private void enrichLineRuntimeList(List<Map<String, Object>> list) {
		if (list == null || list.isEmpty()) {
			return;
		}

		for (Map<String, Object> row : list) {
			double downtimeMin = toDouble(row.get("downtimeMin"));
			double runtimeMin = 1440.0 - downtimeMin;

			if (runtimeMin < 0) {
				runtimeMin = 0;
			}

			double runtimeRate = (runtimeMin / 1440.0) * 100.0;

			row.put("downtimeMin", round1(downtimeMin));
			row.put("runtimeMin", round1(runtimeMin));
			row.put("runtimeRate", round1(runtimeRate));
		}
	}

	private void enrichOeeDetailList(List<Map<String, Object>> list) {
		if (list == null || list.isEmpty()) {
			return;
		}

		for (Map<String, Object> row : list) {
			double downtimeMin = toDouble(row.get("downtimeMin"));
			double planQty = toDouble(row.get("planQty"));
			double actualQty = toDouble(row.get("actualQty"));
			double defectQty = toDouble(row.get("defectQty"));

			double runtimeMin = 1440.0 - downtimeMin;
			if (runtimeMin < 0) {
				runtimeMin = 0;
			}

			double runtimeRate = percent(runtimeMin, 1440.0);
			double performanceRate = percent(actualQty, planQty);

			double goodQty = actualQty - defectQty;
			if (goodQty < 0) {
				goodQty = 0;
			}

			double qualityRate = percent(goodQty, actualQty);
			double oeeRate = (runtimeRate * performanceRate * qualityRate) / 10000.0;

			row.put("runtimeMin", round1(runtimeMin));
			row.put("downtimeMin", round1(downtimeMin));
			row.put("runtimeRate", round1(runtimeRate));
			row.put("performanceRate", round1(performanceRate));
			row.put("qualityRate", round1(qualityRate));
			row.put("oeeRate", round1(oeeRate));

			if (isEmpty(row.get("lineName"))) {
				row.put("lineName", "-");
			}
			if (isEmpty(row.get("planNo"))) {
				row.put("planNo", "-");
			}
			if (isEmpty(row.get("workOrderNo"))) {
				row.put("workOrderNo", "-");
			}
			if (isEmpty(row.get("itemName"))) {
				row.put("itemName", "-");
			}
			if (isEmpty(row.get("workerName"))) {
				row.put("workerName", "-");
			}
		}
	}

	private Map<String, Object> nullSafeMap(Map<String, Object> map) {
		return map == null ? new HashMap<String, Object>() : map;
	}

	private List<Map<String, Object>> nullSafeList(List<Map<String, Object>> list) {
		return list == null ? new ArrayList<Map<String, Object>>() : list;
	}

	private boolean isEmpty(Object obj) {
		return obj == null || String.valueOf(obj).trim().isEmpty();
	}

	private String toStr(Object obj) {
		return obj == null ? "" : String.valueOf(obj).trim();
	}

	private double toDouble(Object obj) {
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

	private double percent(double numerator, double denominator) {
		if (denominator == 0) {
			return 0;
		}
		return (numerator / denominator) * 100.0;
	}

	private double clamp(double value, double min, double max) {
		return Math.max(min, Math.min(max, value));
	}

	private double round1(double value) {
		return Math.round(value * 10.0) / 10.0;
	}

	private double round3(double value) {
		return Math.round(value * 1000.0) / 1000.0;
	}
}