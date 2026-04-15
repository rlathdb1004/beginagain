package dashboard.service;

import java.sql.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import dashboard.dao.CeoMainDAO;
import dashboard.dto.CeoDashboardDTO;

public class CeoMainService {

    private final CeoMainDAO dao = new CeoMainDAO();

    public CeoDashboardDTO getDashboard(Date requestBaseDate) throws Exception {
        CeoDashboardDTO dashboard = new CeoDashboardDTO();

        // 1. 기준일 결정
        Date baseDate = requestBaseDate;
        if (baseDate == null) {
            baseDate = new Date(System.currentTimeMillis());
        }

        dashboard.setBaseDate(baseDate);

        // 2. 공통값
        dashboard.setTotalLineCount(dao.selectTotalLineCount());

        // 3. 기준일이 없으면 빈 구조 반환
        if (baseDate == null) {
            dashboard.setBriefingText(null);
            dashboard.setKpi(new HashMap<String, Object>());

            dashboard.setRiskList(new ArrayList<Map<String, Object>>());
            dashboard.setFactoryStatusList(new ArrayList<Map<String, Object>>());

            dashboard.setDowntimeCauseList(new ArrayList<Map<String, Object>>());
            dashboard.setDefectCauseList(new ArrayList<Map<String, Object>>());
            dashboard.setDelayCauseList(new ArrayList<Map<String, Object>>());

            dashboard.setApprovalList(new ArrayList<Map<String, Object>>());

            dashboard.setProductionTrendList(new ArrayList<Map<String, Object>>());
            dashboard.setQualityTrendList(new ArrayList<Map<String, Object>>());
            dashboard.setShipmentTrendList(new ArrayList<Map<String, Object>>());
            dashboard.setCostTrendList(new ArrayList<Map<String, Object>>());
            dashboard.setTopCostItemList(new ArrayList<Map<String, Object>>());

            dashboard.setProductionTrendMax(0);
            dashboard.setQualityTrendMax(0);
            dashboard.setShipmentTrendMax(0);
            dashboard.setCostTrendMax(0);

            return dashboard;
        }

        // 4. 실제 DB 조회
        String briefingText = dao.selectBriefingText(baseDate);
        Map<String, Object> kpi = nullSafeMap(dao.selectKpi(baseDate));

        List<Map<String, Object>> riskList = nullSafeList(dao.selectRiskList(baseDate));
        List<Map<String, Object>> factoryStatusList = nullSafeList(dao.selectFactoryStatusList(baseDate));

        List<Map<String, Object>> downtimeCauseList = nullSafeList(dao.selectDowntimeCauseList(baseDate));
        List<Map<String, Object>> defectCauseList = nullSafeList(dao.selectDefectCauseList(baseDate));
        List<Map<String, Object>> delayCauseList = nullSafeList(dao.selectDelayCauseList(baseDate));

        List<Map<String, Object>> approvalList = nullSafeList(dao.selectApprovalList(baseDate));

        List<Map<String, Object>> productionTrendList = nullSafeList(dao.selectProductionTrendList(baseDate));
        List<Map<String, Object>> qualityTrendList = nullSafeList(dao.selectQualityTrendList(baseDate));
        List<Map<String, Object>> shipmentTrendList = nullSafeList(dao.selectShipmentTrendList(baseDate));
        List<Map<String, Object>> costTrendList = nullSafeList(dao.selectCostTrendList(baseDate));
        List<Map<String, Object>> topCostItemList = nullSafeList(dao.selectTopCostItemList(baseDate));

        // 5. briefingText 없으면 자동 생성
        if (isBlank(briefingText)) {
            briefingText = buildBriefingText(kpi, riskList);
        }

        // 6. DTO 세팅
        dashboard.setBriefingText(briefingText);
        dashboard.setKpi(kpi);

        dashboard.setRiskList(riskList);
        dashboard.setFactoryStatusList(factoryStatusList);

        dashboard.setDowntimeCauseList(downtimeCauseList);
        dashboard.setDefectCauseList(defectCauseList);
        dashboard.setDelayCauseList(delayCauseList);

        dashboard.setApprovalList(approvalList);

        dashboard.setProductionTrendList(productionTrendList);
        dashboard.setQualityTrendList(qualityTrendList);
        dashboard.setShipmentTrendList(shipmentTrendList);
        dashboard.setCostTrendList(costTrendList);

        dashboard.setProductionTrendMax(getMaxValue(productionTrendList, "value"));
        dashboard.setQualityTrendMax(getMaxValue(qualityTrendList, "value"));
        dashboard.setShipmentTrendMax(getMaxValue(shipmentTrendList, "value"));
        dashboard.setCostTrendMax(getMaxValue(costTrendList, "value"));
        dashboard.setTopCostItemList(topCostItemList);

        return dashboard;
    }

    private Map<String, Object> nullSafeMap(Map<String, Object> map) {
        return map == null ? new HashMap<String, Object>() : map;
    }

    private List<Map<String, Object>> nullSafeList(List<Map<String, Object>> list) {
        return list == null ? new ArrayList<Map<String, Object>>() : list;
    }

    private boolean isBlank(String str) {
        return str == null || str.trim().isEmpty();
    }

    private String buildBriefingText(Map<String, Object> kpi, List<Map<String, Object>> riskList) {
        Object productionRate = kpi.get("productionRate");
        Object defectRate = kpi.get("defectRate");
        Object costVarianceRate = kpi.get("costVarianceRate");

        String topRiskTitle = null;
        if (riskList != null && !riskList.isEmpty()) {
            Object titleObj = riskList.get(0).get("title");
            if (titleObj != null) {
                topRiskTitle = String.valueOf(titleObj);
            }
        }

        StringBuilder sb = new StringBuilder();

        if (productionRate != null) {
            sb.append("오늘 생산달성률은 ").append(productionRate).append("%");
        } else {
            sb.append("오늘 생산지표 집계가 완료되었습니다");
        }

        if (defectRate != null) {
            sb.append(", 불량률은 ").append(defectRate).append("%");
        }

        if (costVarianceRate != null) {
            sb.append(", 원가편차율은 ").append(costVarianceRate).append("%");
        }

        if (!isBlank(topRiskTitle)) {
            sb.append(" 수준이며 최우선 확인 항목은 '").append(topRiskTitle).append("' 입니다.");
        } else {
            sb.append(" 수준입니다.");
        }

        return sb.toString();
    }

    private double getMaxValue(List<Map<String, Object>> list, String key) {
        double max = 0;

        if (list == null || list.isEmpty()) {
            return max;
        }

        for (Map<String, Object> row : list) {
            if (row == null) {
                continue;
            }

            Object valueObj = row.get(key);
            double value = toDouble(valueObj);

            if (value > max) {
                max = value;
            }
        }

        return max;
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
}