package WorkMgmt.WORegInq.DTO;

import java.sql.Date;

/*
 * 작업지시 등록/조회 화면에서 사용할 DTO
 */
public class WORegInqDTO {

    private int seqNO;
    private int itemId;
    private int planId;
    private int empId;

    private String workOrderNo;
    private Date workDate;

    private Date planDate;
    private String planStatus;
    private String empNo;

    private String itemCode;
    private String itemName;
    private String unit;

    private int workQty;
    private String lineCode;
    private String processCode;
    private String empName;
    private String bomCode;
    private String status;
    private String remark;

    private int planQty;
    private int currentWorkQtySum;
    private int remainingQty;

    public int getSeqNO() { return seqNO; }
    public void setSeqNO(int seqNO) { this.seqNO = seqNO; }
    public int getItemId() { return itemId; }
    public void setItemId(int itemId) { this.itemId = itemId; }
    public int getPlanId() { return planId; }
    public void setPlanId(int planId) { this.planId = planId; }
    public int getEmpId() { return empId; }
    public void setEmpId(int empId) { this.empId = empId; }
    public String getWorkOrderNo() { return workOrderNo; }
    public void setWorkOrderNo(String workOrderNo) { this.workOrderNo = workOrderNo; }
    public Date getWorkDate() { return workDate; }
    public void setWorkDate(Date workDate) { this.workDate = workDate; }
    public Date getPlanDate() { return planDate; }
    public void setPlanDate(Date planDate) { this.planDate = planDate; }
    public String getPlanStatus() { return planStatus; }
    public void setPlanStatus(String planStatus) { this.planStatus = planStatus; }
    public String getEmpNo() { return empNo; }
    public void setEmpNo(String empNo) { this.empNo = empNo; }
    public String getItemCode() { return itemCode; }
    public void setItemCode(String itemCode) { this.itemCode = itemCode; }
    public String getItemName() { return itemName; }
    public void setItemName(String itemName) { this.itemName = itemName; }
    public String getUnit() { return unit; }
    public void setUnit(String unit) { this.unit = unit; }
    public int getWorkQty() { return workQty; }
    public void setWorkQty(int workQty) { this.workQty = workQty; }
    public String getLineCode() { return lineCode; }
    public void setLineCode(String lineCode) { this.lineCode = lineCode; }
    public String getProcessCode() { return processCode; }
    public void setProcessCode(String processCode) { this.processCode = processCode; }
    public String getEmpName() { return empName; }
    public void setEmpName(String empName) { this.empName = empName; }
    public String getBomCode() { return bomCode; }
    public void setBomCode(String bomCode) { this.bomCode = bomCode; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getRemark() { return remark; }
    public void setRemark(String remark) { this.remark = remark; }
    public int getPlanQty() { return planQty; }
    public void setPlanQty(int planQty) { this.planQty = planQty; }
    public int getCurrentWorkQtySum() { return currentWorkQtySum; }
    public void setCurrentWorkQtySum(int currentWorkQtySum) { this.currentWorkQtySum = currentWorkQtySum; }
    public int getRemainingQty() { return remainingQty; }
    public void setRemainingQty(int remainingQty) { this.remainingQty = remainingQty; }
}
