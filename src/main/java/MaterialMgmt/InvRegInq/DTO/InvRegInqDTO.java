package MaterialMgmt.InvRegInq.DTO;

import java.sql.Date;

/*
 * 재고 등록 / 조회 DTO
 * 
 * INVENTORY + ITEM 테이블 조인 결과를 담는 용도
 * 목록 조회 / 상세 조회 / 검색 조건 모두 이 DTO로 사용
 */
public class InvRegInqDTO {

    // =========================
    // 1. 재고 정보 컬럼
    // =========================
	// 재고번호
    private int inventoryId;
    // 품목번호	
    private int itemId;  
    // 품목코드
    private String itemCode;
    // 품목명
    private String itemName;  
    // 품목 유형
    private String itemType;
    // 현재재고
    private double qtyOnHand;   
    // 안전재고
    private double safetyStock;
    // 단위
    private String unit; 
    // 재고상태
    private String inventoryStatus;
    // 비고
    private String remark;  
    // 생성일
    private Date createdAt;  
    // 수정일
    private Date updatedAt;       
    // itemCode / itemName / ""
    private String searchType;
    // 검색어
    private String keyword;  
    // 시작일
    private Date startDate; 
    // 종료일
    private Date endDate;  

    // ------><---------
    // 2. 검색 조건
    // ------><---------

    // 마우스 오른쪽 누르고 source 들어가서 
 	// getter / setter로 전부다 가져오기 
    public int getInventoryId() {
        return inventoryId;
    }

    public void setInventoryId(int inventoryId) {
        this.inventoryId = inventoryId;
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


    public String getItemType() {
        return itemType;
    }

    public void setItemType(String itemType) {
        this.itemType = itemType;
    }

    public double getQtyOnHand() {
        return qtyOnHand;
    }

    public void setQtyOnHand(double qtyOnHand) {
        this.qtyOnHand = qtyOnHand;
    }

    public double getSafetyStock() {
        return safetyStock;
    }

    public void setSafetyStock(double safetyStock) {
        this.safetyStock = safetyStock;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getInventoryStatus() {
        return inventoryStatus;
    }

    public void setInventoryStatus(String inventoryStatus) {
        this.inventoryStatus = inventoryStatus;
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
    // ------><---------	
 	// Override

	@Override
	public String toString() {
		return "InvRegInqDTO [inventoryId=" + inventoryId + ", itemId=" + itemId + ", itemCode=" + itemCode
				+ ", itemName=" + itemName + ", itemType=" + itemType + ", qtyOnHand=" + qtyOnHand + ", safetyStock=" + safetyStock + ", unit="
				+ unit + ", inventoryStatus=" + inventoryStatus + ", remark=" + remark + ", createdAt=" + createdAt + ", updatedAt=" + updatedAt
				+ ", searchType=" + searchType + ", keyword=" + keyword + ", startDate=" + startDate + ", endDate="
				+ endDate + "]";
	}
    
}