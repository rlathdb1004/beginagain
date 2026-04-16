package ProdMgmt.ProdPerfRegInq.DTO;

import java.sql.Date;

public class ProdPerfRegInqDTO {
    private int seqNO;
    private int workOrderId;
    private String workOrderNo;
    private Date workDate;
    private Date resultDate;
    private String itemCode;
    private String itemName;
    private String unit;
    private int producedQty;
    private int lossQty;
    private String lineCode;
    private String lotNo;
    private String status;
    private String remark;

    public int getSeqNO() { return seqNO; }
    public void setSeqNO(int seqNO) { this.seqNO = seqNO; }
    public int getWorkOrderId() { return workOrderId; }
    public void setWorkOrderId(int workOrderId) { this.workOrderId = workOrderId; }
    public String getWorkOrderNo() { return workOrderNo; }
    public void setWorkOrderNo(String workOrderNo) { this.workOrderNo = workOrderNo; }
    public Date getWorkDate() { return workDate; }
    public void setWorkDate(Date workDate) { this.workDate = workDate; }
    public Date getResultDate() { return resultDate; }
    public void setResultDate(Date resultDate) { this.resultDate = resultDate; }
    public String getItemCode() { return itemCode; }
    public void setItemCode(String itemCode) { this.itemCode = itemCode; }
    public String getItemName() { return itemName; }
    public void setItemName(String itemName) { this.itemName = itemName; }
    public String getUnit() { return unit; }
    public void setUnit(String unit) { this.unit = unit; }
    public int getProducedQty() { return producedQty; }
    public void setProducedQty(int producedQty) { this.producedQty = producedQty; }
    public int getLossQty() { return lossQty; }
    public void setLossQty(int lossQty) { this.lossQty = lossQty; }
    public String getLineCode() { return lineCode; }
    public void setLineCode(String lineCode) { this.lineCode = lineCode; }
    public String getLotNo() { return lotNo; }
    public void setLotNo(String lotNo) { this.lotNo = lotNo; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getRemark() { return remark; }
    public void setRemark(String remark) { this.remark = remark; }
}
