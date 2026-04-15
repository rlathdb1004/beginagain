package WorkMgmt.WorkStatusInq.DTO;

import java.sql.Date;

/*
 * 작업 현황 조회 화면용 DTO
 *
 * 테이블 목록에서 사용하는 데이터
 */
public class WorkStatusInqDTO {

    // 화면 NO
    private int seqNO;

    // 화면 표시용 작업지시번호
    private String workOrderNo;

    // 작업일자
    private Date workDate;

    // 품목 정보
    private String itemCode;
    private String itemName;

    // 작업지시 / 생산 현황
    private int workQty;
    private int producedQty;
    private int progressRate;

    // 기타 정보
    private String lineCode;
    private String empName;
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

    public Date getWorkDate() {
        return workDate;
    }

    public void setWorkDate(Date workDate) {
        this.workDate = workDate;
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

    public int getWorkQty() {
        return workQty;
    }

    public void setWorkQty(int workQty) {
        this.workQty = workQty;
    }

    public int getProducedQty() {
        return producedQty;
    }

    public void setProducedQty(int producedQty) {
        this.producedQty = producedQty;
    }

    public int getProgressRate() {
        return progressRate;
    }

    public void setProgressRate(int progressRate) {
        this.progressRate = progressRate;
    }

    public String getLineCode() {
        return lineCode;
    }

    public void setLineCode(String lineCode) {
        this.lineCode = lineCode;
    }

    public String getEmpName() {
        return empName;
    }

    public void setEmpName(String empName) {
        this.empName = empName;
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