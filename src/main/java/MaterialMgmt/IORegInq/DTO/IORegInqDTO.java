package MaterialMgmt.IORegInq.DTO;

import java.sql.Date;

/*
 * 입출고 등록 / 조회 DTO
 * 목록 조회 / 상세 조회 / 검색 조건 / 화면 보조값(currentStock) 용도
 */
public class IORegInqDTO {

    private int inoutId;
    private int itemId;
    private String itemCode;
    private String itemName;
    private String itemType;
    private String inoutType;
    private double qty;
    private String unit;
    private Date inoutDate;
    private String status;
    private String remark;
    private Date createdAt;
    private Date updatedAt;
    private double currentStock;
    private Date startDate;
    private Date endDate;
    private String searchType;
    private String keyword;

    public int getInoutId() { return inoutId; }
    public void setInoutId(int inoutId) { this.inoutId = inoutId; }
    public int getItemId() { return itemId; }
    public void setItemId(int itemId) { this.itemId = itemId; }
    public String getItemCode() { return itemCode; }
    public void setItemCode(String itemCode) { this.itemCode = itemCode; }
    public String getItemName() { return itemName; }
    public void setItemName(String itemName) { this.itemName = itemName; }
    public String getItemType() { return itemType; }
    public void setItemType(String itemType) { this.itemType = itemType; }
    public String getInoutType() { return inoutType; }
    public void setInoutType(String inoutType) { this.inoutType = inoutType; }
    public double getQty() { return qty; }
    public void setQty(double qty) { this.qty = qty; }
    public String getUnit() { return unit; }
    public void setUnit(String unit) { this.unit = unit; }
    public Date getInoutDate() { return inoutDate; }
    public void setInoutDate(Date inoutDate) { this.inoutDate = inoutDate; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getRemark() { return remark; }
    public void setRemark(String remark) { this.remark = remark; }
    public Date getCreatedAt() { return createdAt; }
    public void setCreatedAt(Date createdAt) { this.createdAt = createdAt; }
    public Date getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(Date updatedAt) { this.updatedAt = updatedAt; }
    public double getCurrentStock() { return currentStock; }
    public void setCurrentStock(double currentStock) { this.currentStock = currentStock; }
    public Date getStartDate() { return startDate; }
    public void setStartDate(Date startDate) { this.startDate = startDate; }
    public Date getEndDate() { return endDate; }
    public void setEndDate(Date endDate) { this.endDate = endDate; }
    public String getSearchType() { return searchType; }
    public void setSearchType(String searchType) { this.searchType = searchType; }
    public String getKeyword() { return keyword; }
    public void setKeyword(String keyword) { this.keyword = keyword; }

    @Override
    public String toString() {
        return "IORegInqDTO [inoutId=" + inoutId + ", itemId=" + itemId + ", itemCode=" + itemCode + ", itemName="
                + itemName + ", itemType=" + itemType + ", inoutType=" + inoutType + ", qty=" + qty + ", unit=" + unit + ", inoutDate="
                + inoutDate + ", status=" + status + ", currentStock=" + currentStock + "]";
    }
}
