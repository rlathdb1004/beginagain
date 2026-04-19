package QualityMgmt.DefectRegInq.DTO;

import java.sql.Date;

public class DefectRegInqDTO {
    private int defectProductId;
    private int finalInspectionId;
    private int resultId;
    private int workOrderId;
    private int planId;
    private int itemId;
    private String itemCode;
    private String itemName;
    private String lotNo;
    private double inspectQty;
    private int defectCodeId;
    private String defectCode;
    private String defectName;
    private String defectType;
    private String inspectionStatus;
    private int defectCodeCount;
    private String remark;
    private Date createdAt;
    private Date updatedAt;

    private String defectTypeSearch;
    private String searchType;
    private String keyword;
    private Date startDate;
    private Date endDate;

    public int getDefectProductId() { return defectProductId; }
    public void setDefectProductId(int defectProductId) { this.defectProductId = defectProductId; }
    public int getFinalInspectionId() { return finalInspectionId; }
    public void setFinalInspectionId(int finalInspectionId) { this.finalInspectionId = finalInspectionId; }
    public int getResultId() { return resultId; }
    public void setResultId(int resultId) { this.resultId = resultId; }
    public int getWorkOrderId() { return workOrderId; }
    public void setWorkOrderId(int workOrderId) { this.workOrderId = workOrderId; }
    public int getPlanId() { return planId; }
    public void setPlanId(int planId) { this.planId = planId; }
    public int getItemId() { return itemId; }
    public void setItemId(int itemId) { this.itemId = itemId; }
    public String getItemCode() { return itemCode; }
    public void setItemCode(String itemCode) { this.itemCode = itemCode; }
    public String getItemName() { return itemName; }
    public void setItemName(String itemName) { this.itemName = itemName; }
    public String getLotNo() { return lotNo; }
    public void setLotNo(String lotNo) { this.lotNo = lotNo; }
    public double getInspectQty() { return inspectQty; }
    public void setInspectQty(double inspectQty) { this.inspectQty = inspectQty; }
    public int getDefectCodeId() { return defectCodeId; }
    public void setDefectCodeId(int defectCodeId) { this.defectCodeId = defectCodeId; }
    public String getDefectCode() { return defectCode; }
    public void setDefectCode(String defectCode) { this.defectCode = defectCode; }
    public String getDefectName() { return defectName; }
    public void setDefectName(String defectName) { this.defectName = defectName; }
    public String getDefectType() { return defectType; }
    public void setDefectType(String defectType) { this.defectType = defectType; }
    public String getInspectionStatus() { return inspectionStatus; }
    public void setInspectionStatus(String inspectionStatus) { this.inspectionStatus = inspectionStatus; }
    public int getDefectCodeCount() { return defectCodeCount; }
    public void setDefectCodeCount(int defectCodeCount) { this.defectCodeCount = defectCodeCount; }
    public String getRemark() { return remark; }
    public void setRemark(String remark) { this.remark = remark; }
    public Date getCreatedAt() { return createdAt; }
    public void setCreatedAt(Date createdAt) { this.createdAt = createdAt; }
    public Date getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(Date updatedAt) { this.updatedAt = updatedAt; }
    public String getDefectTypeSearch() { return defectTypeSearch; }
    public void setDefectTypeSearch(String defectTypeSearch) { this.defectTypeSearch = defectTypeSearch; }
    public String getSearchType() { return searchType; }
    public void setSearchType(String searchType) { this.searchType = searchType; }
    public String getKeyword() { return keyword; }
    public void setKeyword(String keyword) { this.keyword = keyword; }
    public Date getStartDate() { return startDate; }
    public void setStartDate(Date startDate) { this.startDate = startDate; }
    public Date getEndDate() { return endDate; }
    public void setEndDate(Date endDate) { this.endDate = endDate; }
}
