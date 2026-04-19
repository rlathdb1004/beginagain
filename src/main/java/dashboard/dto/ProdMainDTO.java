package dashboard.dto;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProdMainDTO {

	private Date baseDate;
	private Timestamp updatedAt; // 갱신일시

	private Map<String, Object> kpi = new HashMap<String, Object>();

	private List<Map<String, Object>> lineStatusList = new ArrayList<Map<String, Object>>();
	private List<Map<String, Object>> lineRuntimeList = new ArrayList<Map<String, Object>>();
	private List<Map<String, Object>> downtimeCauseList = new ArrayList<Map<String, Object>>();

	private List<Map<String, Object>> materialAlertList = new ArrayList<Map<String, Object>>();
	private List<Map<String, Object>> equipmentAlertList = new ArrayList<Map<String, Object>>();

	private List<Map<String, Object>> planDetailList = new ArrayList<Map<String, Object>>();
	private List<Map<String, Object>> yieldDetailList = new ArrayList<Map<String, Object>>();
	private List<Map<String, Object>> shipmentDetailList = new ArrayList<Map<String, Object>>();
	private List<Map<String, Object>> oeeDetailList = new ArrayList<Map<String, Object>>();

	// 호환용 유지
	private List<Map<String, Object>> workOrderList = new ArrayList<Map<String, Object>>();
	private List<Map<String, Object>> issueList = new ArrayList<Map<String, Object>>();
	private List<Map<String, Object>> hourlyProductionList = new ArrayList<Map<String, Object>>();
	private List<Map<String, Object>> handoverList = new ArrayList<Map<String, Object>>();

	public Date getBaseDate() {
		return baseDate;
	}

	public void setBaseDate(Date baseDate) {
		this.baseDate = baseDate;
	}

	public Timestamp getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(Timestamp updatedAt) {
		this.updatedAt = updatedAt;
	}

	public Map<String, Object> getKpi() {
		return kpi;
	}

	public void setKpi(Map<String, Object> kpi) {
		this.kpi = kpi;
	}

	public List<Map<String, Object>> getLineStatusList() {
		return lineStatusList;
	}

	public void setLineStatusList(List<Map<String, Object>> lineStatusList) {
		this.lineStatusList = lineStatusList;
	}

	public List<Map<String, Object>> getLineRuntimeList() {
		return lineRuntimeList;
	}

	public void setLineRuntimeList(List<Map<String, Object>> lineRuntimeList) {
		this.lineRuntimeList = lineRuntimeList;
	}

	public List<Map<String, Object>> getDowntimeCauseList() {
		return downtimeCauseList;
	}

	public void setDowntimeCauseList(List<Map<String, Object>> downtimeCauseList) {
		this.downtimeCauseList = downtimeCauseList;
	}

	public List<Map<String, Object>> getMaterialAlertList() {
		return materialAlertList;
	}

	public void setMaterialAlertList(List<Map<String, Object>> materialAlertList) {
		this.materialAlertList = materialAlertList;
	}

	public List<Map<String, Object>> getEquipmentAlertList() {
		return equipmentAlertList;
	}

	public void setEquipmentAlertList(List<Map<String, Object>> equipmentAlertList) {
		this.equipmentAlertList = equipmentAlertList;
	}

	public List<Map<String, Object>> getPlanDetailList() {
		return planDetailList;
	}

	public void setPlanDetailList(List<Map<String, Object>> planDetailList) {
		this.planDetailList = planDetailList;
	}

	public List<Map<String, Object>> getYieldDetailList() {
		return yieldDetailList;
	}

	public void setYieldDetailList(List<Map<String, Object>> yieldDetailList) {
		this.yieldDetailList = yieldDetailList;
	}

	public List<Map<String, Object>> getShipmentDetailList() {
		return shipmentDetailList;
	}

	public void setShipmentDetailList(List<Map<String, Object>> shipmentDetailList) {
		this.shipmentDetailList = shipmentDetailList;
	}

	public List<Map<String, Object>> getOeeDetailList() {
		return oeeDetailList;
	}

	public void setOeeDetailList(List<Map<String, Object>> oeeDetailList) {
		this.oeeDetailList = oeeDetailList;
	}

	public List<Map<String, Object>> getWorkOrderList() {
		return workOrderList;
	}

	public void setWorkOrderList(List<Map<String, Object>> workOrderList) {
		this.workOrderList = workOrderList;
	}

	public List<Map<String, Object>> getIssueList() {
		return issueList;
	}

	public void setIssueList(List<Map<String, Object>> issueList) {
		this.issueList = issueList;
	}

	public List<Map<String, Object>> getHourlyProductionList() {
		return hourlyProductionList;
	}

	public void setHourlyProductionList(List<Map<String, Object>> hourlyProductionList) {
		this.hourlyProductionList = hourlyProductionList;
	}

	public List<Map<String, Object>> getHandoverList() {
		return handoverList;
	}

	public void setHandoverList(List<Map<String, Object>> handoverList) {
		this.handoverList = handoverList;
	}

	@Override
	public String toString() {
		return "ProdMainDTO{" + "baseDate=" + baseDate + ", updatedAt=" + updatedAt + ", kpi=" + kpi
				+ ", lineStatusList=" + lineStatusList + ", lineRuntimeList=" + lineRuntimeList + ", downtimeCauseList="
				+ downtimeCauseList + ", materialAlertList=" + materialAlertList + ", equipmentAlertList="
				+ equipmentAlertList + ", planDetailList=" + planDetailList + ", yieldDetailList=" + yieldDetailList
				+ ", shipmentDetailList=" + shipmentDetailList + ", oeeDetailList=" + oeeDetailList + ", workOrderList="
				+ workOrderList + ", issueList=" + issueList + ", hourlyProductionList=" + hourlyProductionList
				+ ", handoverList=" + handoverList + '}';
	}
}