package WorkMgmt.WORegInq.DTO;

import java.sql.Date;

/*
 * 작업지시 등록/조회 화면에서 사용할 DTO
 *
 * 역할
 * - DAO가 조회한 데이터를 담는다.
 * - Controller -> JSP 로 데이터를 넘길 때 사용한다.
 *
 * 주의
 * - seqNO 는 화면의 NO 컬럼이며 실제로는 WORK_ORDER_ID 값을 넣는다.
 * - workOrderNo 는 화면 표시용으로 가공한 문자열이다.
 */
public class WORegInqDTO {

    // WORK_ORDER_ID를 화면 NO 및 삭제 키로 사용
    private int seqNO;

    // 화면 표시용 작업지시번호
    // 예: WO-20260402-001
    private String workOrderNo;

    // 작업일자
    private Date workDate;

    // 품목 정보
    private String itemCode;
    private String itemName;
    private String unit;

    // 작업지시 정보
    private int workQty;
    private String lineCode;
    private String processCode;
    private String empName;
    private String bomCode;
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

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public int getWorkQty() {
        return workQty;
    }

    public void setWorkQty(int workQty) {
        this.workQty = workQty;
    }

    public String getLineCode() {
        return lineCode;
    }

    public void setLineCode(String lineCode) {
        this.lineCode = lineCode;
    }

    public String getProcessCode() {
        return processCode;
    }

    public void setProcessCode(String processCode) {
        this.processCode = processCode;
    }

    public String getEmpName() {
        return empName;
    }

    public void setEmpName(String empName) {
        this.empName = empName;
    }

    public String getBomCode() {
        return bomCode;
    }

    public void setBomCode(String bomCode) {
        this.bomCode = bomCode;
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