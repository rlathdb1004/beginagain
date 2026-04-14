package WorkMgmt.WORegInq.DTO;

import java.math.BigDecimal;
import java.sql.Date;

public class WORegInqDTO {

    // 목록 표시용 순번
    private int seqNo;

    // 실제 PK
    private int workOrderId;

    // 화면 표시용 작업지시번호
    private String workOrderNo;

    // 작업일자
    private Date workDate;

    // 품목
    private String itemCode;
    private String itemName;

    // 지시량
    private BigDecimal workQty;

    // 단위
    private String unitName;

    // 라인 / 공정 / 작업자 / BOM
    private String lineName;
    private String processName;
    private String empName;
    private String bomName;

    // 비고
    private String remark;

    public int getSeqNo() {
        return seqNo;
    }
    public void setSeqNo(int seqNo) {
        this.seqNo = seqNo;
    }

    public int getWorkOrderId() {
        return workOrderId;
    }
    public void setWorkOrderId(int workOrderId) {
        this.workOrderId = workOrderId;
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

    public BigDecimal getWorkQty() {
        return workQty;
    }
    public void setWorkQty(BigDecimal workQty) {
        this.workQty = workQty;
    }

    public String getUnitName() {
        return unitName;
    }
    public void setUnitName(String unitName) {
        this.unitName = unitName;
    }

    public String getLineName() {
        return lineName;
    }
    public void setLineName(String lineName) {
        this.lineName = lineName;
    }

    public String getProcessName() {
        return processName;
    }
    public void setProcessName(String processName) {
        this.processName = processName;
    }

    public String getEmpName() {
        return empName;
    }
    public void setEmpName(String empName) {
        this.empName = empName;
    }

    public String getBomName() {
        return bomName;
    }
    public void setBomName(String bomName) {
        this.bomName = bomName;
    }

    public String getRemark() {
        return remark;
    }
    public void setRemark(String remark) {
        this.remark = remark;
    }
}