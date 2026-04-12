package MaterialMgmt.IORegInq.DTO;

import java.sql.Date;

	/* 1. 목록 조회 결과를 담는 용도
	 * 2. 상세 조회 결과를 담는 용도
	 * 3. 검색 조건을 담아서 DAO로 보내는 용도
	*/
public class IORegInqDTO {

	// ------><---------	
	// DB 조회 결과용 필드 
	
	// 입출고 PK
	private int inoutId;
	
	// 품목 PK
	private int itemId;
	
	// 품목코드
	private String itemCode;
	
	// 품목명
	private String itemName;
	
	// 입출고구분 (입고 / 출고)
	private String inoutType;
		
	// 수량 NUMBER(15,3) 이기 때문에 double로 처리
	private double qty;
		
	// 단위
	private String unit;
		
	// 입출고일자
	private Date inoutDate;
		
	// 처리상태
	private String status;

	// 비고
	private String remark;
		
	// 생성일시
	private Date createdAt;
		
	// 수정일시
	private Date updatedAt;
		
	
	// ------><---------		
	// 검색 조건용 필드

		
	// 시작일
	private Date startDate;
		
	// 종료일
	private Date endDate;
		
	// 검색구분 (itemCode / itemName)
	private String searchType;
		
	// 검색어
	private String keyword;
	
	// 마우스 오른쪽 누르고 source 들어가서 
	// getter / setter로 전부다 가져오기 

	public int getInoutId() {
		return inoutId;
	}

	public void setInoutId(int inoutId) {
		this.inoutId = inoutId;
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

	public String getInoutType() {
		return inoutType;
	}

	public void setInoutType(String inoutType) {
		this.inoutType = inoutType;
	}

	public double getQty() {
		return qty;
	}

	public void setQty(double qty) {
		this.qty = qty;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public Date getInoutDate() {
		return inoutDate;
	}

	public void setInoutDate(Date inoutDate) {
		this.inoutDate = inoutDate;
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

	// ------><---------	
	// Override
	@Override
	public String toString() {
		return "IORegInqDTO [inoutId=" + inoutId + ", itemId=" + itemId + ", itemCode=" + itemCode + ", itemName="
				+ itemName + ", inoutType=" + inoutType + ", qty=" + qty + ", unit=" + unit + ", status=" + status
				+ ", remark=" + remark + ", searchType=" + searchType + ", keyword=" + keyword + "]";
	}
		
	
}
