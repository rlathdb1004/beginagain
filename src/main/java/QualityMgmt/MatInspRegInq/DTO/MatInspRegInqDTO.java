package QualityMgmt.MatInspRegInq.DTO;

import java.sql.Date;

public class MatInspRegInqDTO {
    private int materialInspectionId;
    private int itemId;
    private int empId;
    private String itemCode;
    private String itemName;
    private String unit;
    private String empName;
    private double inspectQty;
    private double goodQty;
    private double defectQty;
    private String result;
    private Date inspectionDate;
    private String remark;
    private Date createdAt;
    private Date updatedAt;

    private String resultType;
    private String searchType;
    private String keyword;
    private Date startDate;
    private Date endDate;

    public int getMaterialInspectionId() { return materialInspectionId; }
    public void setMaterialInspectionId(int materialInspectionId) { this.materialInspectionId = materialInspectionId; }
    public int getItemId() { return itemId; }
    public void setItemId(int itemId) { this.itemId = itemId; }
    public int getEmpId() { return empId; }
    public void setEmpId(int empId) { this.empId = empId; }
    public String getItemCode() { return itemCode; }
    public void setItemCode(String itemCode) { this.itemCode = itemCode; }
    public String getItemName() { return itemName; }
    public void setItemName(String itemName) { this.itemName = itemName; }
    public String getUnit() { return unit; }
    public void setUnit(String unit) { this.unit = unit; }
    public String getEmpName() { return empName; }
    public void setEmpName(String empName) { this.empName = empName; }
    public double getInspectQty() { return inspectQty; }
    public void setInspectQty(double inspectQty) { this.inspectQty = inspectQty; }
    public double getGoodQty() { return goodQty; }
    public void setGoodQty(double goodQty) { this.goodQty = goodQty; }
    public double getDefectQty() { return defectQty; }
    public void setDefectQty(double defectQty) { this.defectQty = defectQty; }
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
