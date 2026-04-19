package ProdMgmt.ProdPlanRegInq.DTO;

import java.sql.Date;

public class ProdPlanRegInqDTO {
    private int seqNO;
    private String planNo;
    private Date planDate;

    private int itemId;
    private String planCode;
    private String planName;
    private String planUnit;

    private int planAmount;
    private String planLine;
    private String status;
    private String memo;

    private int workOrderSum;
    private int remainingQty;

    public int getSeqNO() { return seqNO; }
    public void setSeqNO(int seqNO) { this.seqNO = seqNO; }

    public String getPlanNo() { return planNo; }
    public void setPlanNo(String planNo) { this.planNo = planNo; }

    public Date getPlanDate() { return planDate; }
    public void setPlanDate(Date planDate) { this.planDate = planDate; }

    public int getItemId() { return itemId; }
    public void setItemId(int itemId) { this.itemId = itemId; }

    public String getPlanCode() { return planCode; }
    public void setPlanCode(String planCode) { this.planCode = planCode; }

    public String getPlanName() { return planName; }
    public void setPlanName(String planName) { this.planName = planName; }

    public String getPlanUnit() { return planUnit; }
    public void setPlanUnit(String planUnit) { this.planUnit = planUnit; }

    public int getPlanAmount() { return planAmount; }
    public void setPlanAmount(int planAmount) { this.planAmount = planAmount; }

    public String getPlanLine() { return planLine; }
    public void setPlanLine(String planLine) { this.planLine = planLine; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getMemo() { return memo; }
    public void setMemo(String memo) { this.memo = memo; }

    public int getWorkOrderSum() { return workOrderSum; }
    public void setWorkOrderSum(int workOrderSum) { this.workOrderSum = workOrderSum; }

    public int getRemainingQty() { return remainingQty; }
    public void setRemainingQty(int remainingQty) { this.remainingQty = remainingQty; }
}
