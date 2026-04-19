package QualityMgmt.FPInspRegInq.DTO;

import java.sql.Date;

public class FPInspRegInqDTO {
    private int finalInspectionId;
    private int resultId;
    private int empId;
    private int workOrderId;
    private int itemId;
    private int producedQty;
    private int currentInspectSum;
    private int remainingQty;
    private int defectCount;
    private String itemCode;
    private String itemName;
    private String lotNo;
    private String result;
    private String remark;
    private Date inspectionDate;
    private Date createdAt;
    private Date updatedAt;
    private double inspectQty;

    private String resultType;
    private String searchType;
    private String keyword;
    private Date startDate;
    private Date endDate;

    public int getFinalInspectionId() { return finalInspectionId; }
    public void setFinalInspectionId(int finalInspectionId) { this.finalInspectionId = finalInspectionId; }
    public int getResultId() { return resultId; }
    public void setResultId(int resultId) { this.resultId = resultId; }
    public int getEmpId() { return empId; }
    public void setEmpId(int empId) { this.empId = empId; }
    public int getWorkOrderId() { return workOrderId; }
    public void setWorkOrderId(int workOrderId) { this.workOrderId = workOrderId; }
    public int getItemId() { return itemId; }
    public void setItemId(int itemId) { this.itemId = itemId; }
    public int getProducedQty() { return producedQty; }
    public void setProducedQty(int producedQty) { this.producedQty = producedQty; }
    public int getCurrentInspectSum() { return currentInspectSum; }
    public void setCurrentInspectSum(int currentInspectSum) { this.currentInspectSum = currentInspectSum; }
    public int getRemainingQty() { return remainingQty; }
    public void setRemainingQty(int remainingQty) { this.remainingQty = remainingQty; }
    public int getDefectCount() { return defectCount; }
    public void setDefectCount(int defectCount) { this.defectCount = defectCount; }
    public String getItemCode() { return itemCode; }
    public void setItemCode(String itemCode) { this.itemCode = itemCode; }
    public String getItemName() { return itemName; }
    public void setItemName(String itemName) { this.itemName = itemName; }
    public String getLotNo() { return lotNo; }
    public void setLotNo(String lotNo) { this.lotNo = lotNo; }
    public double getInspectQty() { return inspectQty; }
    public void setInspectQty(double inspectQty) { this.inspectQty = inspectQty; }
    public String getResult() { return result; }
    public void setResult(String result) { this.result = result; }
    public Date getInspectionDate() { return inspectionDate; }
    public void setInspectionDate(Date inspectionDate) { this.inspectionDate = inspectionDate; }
    public String getRemark() { return remark; }
    public void setRemark(String remark) { this.remark = remark; }
    public Date getCreatedAt() { return createdAt; }
    public void setCreatedAt(Date createdAt) { this.createdAt = createdAt; }
    public Date getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(Date updatedAt) { this.updatedAt = updatedAt; }
    public String getResultType() { return resultType; }
    public void setResultType(String resultType) { this.resultType = resultType; }
    public String getSearchType() { return searchType; }
    public void setSearchType(String searchType) { this.searchType = searchType; }
    public String getKeyword() { return keyword; }
    public void setKeyword(String keyword) { this.keyword = keyword; }
    public Date getStartDate() { return startDate; }
    public void setStartDate(Date startDate) { this.startDate = startDate; }
    public Date getEndDate() { return endDate; }
    public void setEndDate(Date endDate) { this.endDate = endDate; }
}
