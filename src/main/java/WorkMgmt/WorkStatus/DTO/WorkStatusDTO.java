package WorkMgmt.WorkStatus.DTO;

import java.sql.Date;

public class WorkStatusDTO {
    private int workOrderId;
    private String workOrderDisplayCode;
    private String itemCode;
    private String itemName;
    private String empName;
    private Date workDate;
    private double workQty;
    private String workOrderStatus;
    private double producedQty;
    private double lossQty;
    private double progressRate;
    private Date lastResultDate;
    private String progressStatus;
    private String scheduleStatus;
    private String remark;

    public int getWorkOrderId() { return workOrderId; }
    public void setWorkOrderId(int workOrderId) { this.workOrderId = workOrderId; }
    public String getWorkOrderDisplayCode() { return workOrderDisplayCode; }
    public void setWorkOrderDisplayCode(String workOrderDisplayCode) { this.workOrderDisplayCode = workOrderDisplayCode; }
    public String getItemCode() { return itemCode; }
    public void setItemCode(String itemCode) { this.itemCode = itemCode; }
    public String getItemName() { return itemName; }
    public void setItemName(String itemName) { this.itemName = itemName; }
    public String getEmpName() { return empName; }
    public void setEmpName(String empName) { this.empName = empName; }
    public Date getWorkDate() { return workDate; }
    public void setWorkDate(Date workDate) { this.workDate = workDate; }
    public double getWorkQty() { return workQty; }
    public void setWorkQty(double workQty) { this.workQty = workQty; }
    public String getWorkOrderStatus() { return workOrderStatus; }
    public void setWorkOrderStatus(String workOrderStatus) { this.workOrderStatus = workOrderStatus; }
    public double getProducedQty() { return producedQty; }
    public void setProducedQty(double producedQty) { this.producedQty = producedQty; }
    public double getLossQty() { return lossQty; }
    public void setLossQty(double lossQty) { this.lossQty = lossQty; }
    public double getProgressRate() { return progressRate; }
    public void setProgressRate(double progressRate) { this.progressRate = progressRate; }
    public Date getLastResultDate() { return lastResultDate; }
    public void setLastResultDate(Date lastResultDate) { this.lastResultDate = lastResultDate; }
    public String getProgressStatus() { return progressStatus; }
    public void setProgressStatus(String progressStatus) { this.progressStatus = progressStatus; }
    public String getScheduleStatus() { return scheduleStatus; }
    public void setScheduleStatus(String scheduleStatus) { this.scheduleStatus = scheduleStatus; }
    public String getRemark() { return remark; }
    public void setRemark(String remark) { this.remark = remark; }
}
