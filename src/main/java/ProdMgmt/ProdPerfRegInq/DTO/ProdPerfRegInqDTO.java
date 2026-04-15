package ProdMgmt.ProdPerfRegInq.DTO;

import java.sql.Date;

/*
 * 생산실적 등록/조회 화면에서 사용할 DTO
 *
 * 역할
 * - DAO가 조회한 데이터를 담는다.
 * - Controller -> JSP 로 데이터를 넘길 때 사용한다.
 *
 * 주의
 * - seqNO 는 화면의 NO 컬럼이며 실제로는 RESULT_ID 값을 넣는다.
 * - workOrderNo 는 화면 표시용으로 가공한 문자열이다.
 */
public class ProdPerfRegInqDTO {

    // RESULT_ID를 화면 NO 및 삭제 키로 사용
    private int seqNO;

    // 화면 표시용 작업지시번호
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