package QualityMgmt.DefectRegInq.DTO;

import java.sql.Date;

/*
 * 불량 등록 / 조회 DTO
 * 
 * DEFECT_PRODUCT + DEFECT_CODE + FINAL_INSPECTION + ITEM 조인 결과
 */
public class DefectRegInqDTO {

    // =========================
    // 1. 불량 정보
    // =========================
    private int defectProductId;      // 불량등록번호
    private int finalInspectionId;    // 완제품검사번호
    private int resultId;             // 생산실적번호
    private int itemId;               // 품목번호
    private String itemCode;          // 품목코드
    private String itemName;          // 품목명
    private String lotNo;             // LOT번호
    private double defectQty;         // 불량수량
    private int defectCodeId;         // 불량코드ID
    private String defectCode;        // 불량코드
    private String defectName;        // 불량명
    private String defectType;        // 불량유형
    private String remark;            // 비고
    private Date createdAt;           // 생성일
    private Date updatedAt;           // 수정일

    // =========================
    // 2. 검색 조건
    // =========================
    private String defectTypeSearch;  // 전체 / 유형별
    private String searchType;        // itemCode / itemName / defectCode / defectName
    private String keyword;           // 검색어
    private Date startDate;           // 시작일
    private Date endDate;             // 종료일

    public int getDefectProductId() {
        return defectProductId;
    }

    public void setDefectProductId(int defectProductId) {
        this.defectProductId = defectProductId;
    }

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

    public double getDefectQty() {
        return defectQty;
    }

    public void setDefectQty(double defectQty) {
        this.defectQty = defectQty;
    }

    public int getDefectCodeId() {
        return defectCodeId;
    }

    public void setDefectCodeId(int defectCodeId) {
        this.defectCodeId = defectCodeId;
    }

    public String getDefectCode() {
        return defectCode;
    }

    public void setDefectCode(String defectCode) {
        this.defectCode = defectCode;
    }

    public String getDefectName() {
        return defectName;
    }

    public void setDefectName(String defectName) {
        this.defectName = defectName;
    }

    public String getDefectType() {
        return defectType;
    }

    public void setDefectType(String defectType) {
        this.defectType = defectType;
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

    public String getDefectTypeSearch() {
        return defectTypeSearch;
    }

    public void setDefectTypeSearch(String defectTypeSearch) {
        this.defectTypeSearch = defectTypeSearch;
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
		return "DefectRegInqDTO [defectProductId=" + defectProductId + ", finalInspectionId=" + finalInspectionId
				+ ", resultId=" + resultId + ", itemId=" + itemId + ", itemCode=" + itemCode + ", itemName=" + itemName
				+ ", lotNo=" + lotNo + ", defectQty=" + defectQty + ", defectCodeId=" + defectCodeId + ", defectCode="
				+ defectCode + ", defectName=" + defectName + ", defectType=" + defectType + ", remark=" + remark
				+ ", createdAt=" + createdAt + ", updatedAt=" + updatedAt + ", defectTypeSearch=" + defectTypeSearch
				+ ", searchType=" + searchType + ", keyword=" + keyword + ", startDate=" + startDate + ", endDate="
				+ endDate + "]";
	}
    
}