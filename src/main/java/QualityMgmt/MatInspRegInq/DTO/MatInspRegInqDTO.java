package QualityMgmt.MatInspRegInq.DTO;

import java.sql.Date;

/*
 * 자재 검사 등록 / 조회 DTO
 * 
 * MATERIAL_INSPECTION + ITEM 조인 결과
 * 목록 조회 / 상세 조회 / 검색 조건까지 함께 사용
 */
public class MatInspRegInqDTO {

    // =========================
    // 1. 자재 검사 정보
    // =========================
    private int materialInspectionId;   // 자재검사번호
    private int itemId;                 // 품목번호
    private String itemCode;            // 품목코드
    private String itemName;            // 품목명
    private double inspectQty;          // 검사수량
    private double goodQty;             // 양품수량
    private double defectQty;           // 불량수량
    private String result;              // 판정결과
    private Date inspectionDate;        // 검사일
    private String remark;              // 비고
    private Date createdAt;             // 생성일
    private Date updatedAt;             // 수정일

    // =========================
    // 2. 검색 조건
    // =========================
    private String resultType;          // 전체 / 합격 / 불합격
    private String searchType;          // itemCode / itemName / ""
    private String keyword;             // 검색어
    private Date startDate;             // 시작일
    private Date endDate;               // 종료일

    public int getMaterialInspectionId() {
        return materialInspectionId;
    }

    public void setMaterialInspectionId(int materialInspectionId) {
        this.materialInspectionId = materialInspectionId;
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

    public double getInspectQty() {
        return inspectQty;
    }

    public void setInspectQty(double inspectQty) {
        this.inspectQty = inspectQty;
    }

    public double getGoodQty() {
        return goodQty;
    }

    public void setGoodQty(double goodQty) {
        this.goodQty = goodQty;
    }

    public double getDefectQty() {
        return defectQty;
    }

    public void setDefectQty(double defectQty) {
        this.defectQty = defectQty;
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

	@Override
	public String toString() {
		return "MatInspRegInqDTO [materialInspectionId=" + materialInspectionId + ", itemId=" + itemId + ", itemCode="
				+ itemCode + ", itemName=" + itemName + ", inspectQty=" + inspectQty + ", goodQty=" + goodQty
				+ ", defectQty=" + defectQty + ", result=" + result + ", inspectionDate=" + inspectionDate + ", remark="
				+ remark + ", createdAt=" + createdAt + ", updatedAt=" + updatedAt + ", resultType=" + resultType
				+ ", searchType=" + searchType + ", keyword=" + keyword + ", startDate=" + startDate + ", endDate="
				+ endDate + "]";
	}
    
    
}