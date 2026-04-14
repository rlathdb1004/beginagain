package ProdMgmt.ProdPerfRegInq.DTO;

import java.sql.Date;

public class ProdPerfRegInqDTO {

    // RESULT_ID를 화면 NO 및 삭제용 키로 사용
    private int seqNO;

    // 작업지시번호 표시용
    private String workOrderNo;

    // 생산실적일자
    private Date resultDate;

    // 품목 정보
    private String itemCode;
    private String itemName;
    private String unit;

    // 생산실적 정보
    private int producedQty;
    private int lossQty;
    private String lineCode;
    private String lotNo;
    private String supplierName;
    private String status;
    private String remark;

    public int getSeqNO() {
        return seqNO;
    }

    public void setSeqNO(int seqNO) {
        this.seqNO = seqNO;
    }

    public String getWorkOrderNo() {
        return workOrderNo;
    }

    public void setWorkOrderNo(String workOrderNo) {
        this.workOrderNo = workOrderNo;
    }

    public Date getResultDate() {
        return resultDate;
    }

    public void setResultDate(Date resultDate) {
        this.resultDate = resultDate;
    }

    public String getItemCode() {
        return itemCode;
    }

    public void setItemCode(String itemCode) {
        this.itemCode = itemCode;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public int getProducedQty() {
        return producedQty;
    }

    public void setProducedQty(int producedQty) {
        this.producedQty = producedQty;
    }

    public int getLossQty() {
        return lossQty;
    }

    public void setLossQty(int lossQty) {
        this.lossQty = lossQty;
    }

    public String getLineCode() {
        return lineCode;
    }

    public void setLineCode(String lineCode) {
        this.lineCode = lineCode;
    }

    public String getLotNo() {
        return lotNo;
    }

    public void setLotNo(String lotNo) {
        this.lotNo = lotNo;
    }

    public String getSupplierName() {
        return supplierName;
    }

    public void setSupplierName(String supplierName) {
        this.supplierName = supplierName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}