package QualityMgmt.FPInspRegInq.DTO;

import java.sql.Date;

/*
 * 완제품 검사 등록 / 조회 DTO
 * 
 * FINAL_INSPECTION + PRODUCTION_RESULT + WORK_ORDER + PRODUCTION_PLAN + ITEM
 * 조인 결과를 담는 DTO
 */
public class FPInspRegInqDTO {

    // =========================
    // 1. 완제품 검사 정보
    // =========================
    private int finalInspectionId;   // 완제품검사번호
    private int resultId;            // 생산실적번호
    private int workOrderId;         // 작업지시번호
    private int itemId;              // 품목번호
    private String itemCode;         // 품목코드
    private String itemName;         // 품목명
    private String lotNo;            // LOT 번호
    private double inspectQty;       // 검사수량
    private String result;           // 판정결과
    private Date inspectionDate;     // 검사일
    private String remark;           // 비고
    private Date createdAt;          // 생성일
    private Date updatedAt;          // 수정일

    // =========================
    // 2. 검색 조건
    // =========================
    private String resultType;       // 전체 / 합격 / 불합격
    private String searchType;       // itemCode / itemName / ""
    private String keyword;          // 검색어
    private Date startDate;          // 시작일
    private Date endDate;            // 종료일

    public int getFinalInspectionId() {
        return finalInspectionId;
    }

    public void setFinalInspectionId(int finalInspectionId) {
        this.finalInspectionId = finalInspectionId;
    }

    public int getResultId() {
        return resultId;
    }

    public void setResultId(int resultId) {
        this.resultId = resultId;
    }

    public int getWorkOrderId() {
        return workOrderId;
    }

    public void setWorkOrderId(int workOrderId) {
        this.workOrderId = workOrderId;
    }

    public int getItemId() {
        return itemId;
    }

    public void setItemId(int itemId) {
        this.itemId = itemId;
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

    public String getLotNo() {
        return lotNo;
    }

    public void setLotNo(String lotNo) {
        this.lotNo = lotNo;
    }

    public double getInspectQty() {
        return inspectQty;
    }

    public void setInspectQty(double inspectQty) {
        this.inspectQty = inspectQty;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public Date getInspectionDate() {
        return inspectionDate;
    }

    public void setInspectionDate(Date inspectionDate) {
        this.inspectionDate = inspectionDate;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getResultType() {
        return resultType;
    }

    public void setResultType(String resultType) {
        this.resultType = resultType;
    }

    public String getSearchType() {
        return searchType;
    }

    public void setSearchType(String searchType) {
        this.searchType = searchType;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }
}