package dashboard.dto;

import java.sql.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CeoDashboardDTO {

    private Date baseDate;
    private int totalLineCount;
    private String briefingText;

    private Map<String, Object> kpi = new HashMap<String, Object>();

    private List<Map<String, Object>> riskList = new ArrayList<Map<String, Object>>();
    private List<Map<String, Object>> factoryStatusList = new ArrayList<Map<String, Object>>();

    private List<Map<String, Object>> downtimeCauseList = new ArrayList<Map<String, Object>>();
    private List<Map<String, Object>> defectCauseList = new ArrayList<Map<String, Object>>();
    private List<Map<String, Object>> delayCauseList = new ArrayList<Map<String, Object>>();

    private List<Map<String, Object>> approvalList = new ArrayList<Map<String, Object>>();

    private List<Map<String, Object>> productionTrendList = new ArrayList<Map<String, Object>>();
    private List<Map<String, Object>> qualityTrendList = new ArrayList<Map<String, Object>>();
    private List<Map<String, Object>> shipmentTrendList = new ArrayList<Map<String, Object>>();
    private List<Map<String, Object>> costTrendList = new ArrayList<Map<String, Object>>();

    private double productionTrendMax;
    private double qualityTrendMax;
    private double shipmentTrendMax;
    private double costTrendMax;

    public Date getBaseDate() {
        return baseDate;
    }

    public void setBaseDate(Date baseDate) {
        this.baseDate = baseDate;
    }

    public int getTotalLineCount() {
        return totalLineCount;
    }

    public void setTotalLineCount(int totalLineCount) {
        this.totalLineCount = totalLineCount;
    }

    public String getBriefingText() {
        return briefingText;
    }

    public void setBriefingText(String briefingText) {
        this.briefingText = briefingText;
    }

    public Map<String, Object> getKpi() {
        return kpi;
    }

    public void setKpi(Map<String, Object> kpi) {
        this.kpi = kpi;
    }

    public List<Map<String, Object>> getRiskList() {
        return riskList;
    }

    public void setRiskList(List<Map<String, Object>> riskList) {
        this.riskList = riskList;
    }

    public List<Map<String, Object>> getFactoryStatusList() {
        return factoryStatusList;
    }

    public void setFactoryStatusList(List<Map<String, Object>> factoryStatusList) {
        this.factoryStatusList = factoryStatusList;
    }

    public List<Map<String, Object>> getDowntimeCauseList() {
        return downtimeCauseList;
    }

    public void setDowntimeCauseList(List<Map<String, Object>> downtimeCauseList) {
        this.downtimeCauseList = downtimeCauseList;
    }

    public List<Map<String, Object>> getDefectCauseList() {
        return defectCauseList;
    }

    public void setDefectCauseList(List<Map<String, Object>> defectCauseList) {
        this.defectCauseList = defectCauseList;
    }

    public List<Map<String, Object>> getDelayCauseList() {
        return delayCauseList;
    }

    public void setDelayCauseList(List<Map<String, Object>> delayCauseList) {
        this.delayCauseList = delayCauseList;
    }

    public List<Map<String, Object>> getApprovalList() {
        return approvalList;
    }

    public void setApprovalList(List<Map<String, Object>> approvalList) {
        this.approvalList = approvalList;
    }

    public List<Map<String, Object>> getProductionTrendList() {
        return productionTrendList;
    }

    public void setProductionTrendList(List<Map<String, Object>> productionTrendList) {
        this.productionTrendList = productionTrendList;
    }

    public List<Map<String, Object>> getQualityTrendList() {
        return qualityTrendList;
    }

    public void setQualityTrendList(List<Map<String, Object>> qualityTrendList) {
        this.qualityTrendList = qualityTrendList;
    }

    public List<Map<String, Object>> getShipmentTrendList() {
        return shipmentTrendList;
    }

    public void setShipmentTrendList(List<Map<String, Object>> shipmentTrendList) {
        this.shipmentTrendList = shipmentTrendList;
    }

    public List<Map<String, Object>> getCostTrendList() {
        return costTrendList;
    }

    public void setCostTrendList(List<Map<String, Object>> costTrendList) {
        this.costTrendList = costTrendList;
    }

    public double getProductionTrendMax() {
        return productionTrendMax;
    }

    public void setProductionTrendMax(double productionTrendMax) {
        this.productionTrendMax = productionTrendMax;
    }

    public double getQualityTrendMax() {
        return qualityTrendMax;
    }

    public void setQualityTrendMax(double qualityTrendMax) {
        this.qualityTrendMax = qualityTrendMax;
    }

    public double getShipmentTrendMax() {
        return shipmentTrendMax;
    }

    public void setShipmentTrendMax(double shipmentTrendMax) {
        this.shipmentTrendMax = shipmentTrendMax;
    }

    public double getCostTrendMax() {
        return costTrendMax;
    }

    public void setCostTrendMax(double costTrendMax) {
        this.costTrendMax = costTrendMax;
    }
}