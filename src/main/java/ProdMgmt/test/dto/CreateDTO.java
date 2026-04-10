package mes.dto;

import java.sql.Date;

public class CreateDTO {
	
	private int NO;
	private String subject;
	private String suplier;
	private String itemsCode;
	private String itemsName;
	private String itemsUnit;
	private Date createDate;
	private String itemsMemo;
	
	public int getNO() {
		return NO;
	}
	public void setNO(int nO) {
		NO = nO;
	}
	public String getSubject() {
		return subject;
	}
	public void setSubject(String subject) {
		this.subject = subject;
	}
	public String getSuplier() {
		return suplier;
	}
	public void setSuplier(String suplier) {
		this.suplier = suplier;
	}
	public String getItemsCode() {
		return itemsCode;
	}
	public void setItemsCode(String itemsCode) {
		this.itemsCode = itemsCode;
	}
	public String getItemsName() {
		return itemsName;
	}
	public void setItemsName(String itemsName) {
		this.itemsName = itemsName;
	}
	public String getItemsUnit() {
		return itemsUnit;
	}
	public void setItemsUnit(String itemsUnit) {
		this.itemsUnit = itemsUnit;
	}
	public Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	public String getItemsMemo() {
		return itemsMemo;
	}
	public void setItemsMemo(String itemsMemo) {
		this.itemsMemo = itemsMemo;
	}
	@Override
	public String toString() {
		return "CreateDTO [NO=" + NO + ", subject=" + subject + ", suplier=" + suplier + ", itemsCode=" + itemsCode
				+ ", itemsName=" + itemsName + ", itemsUnit=" + itemsUnit + ", createDate=" + createDate
				+ ", itemsMemo=" + itemsMemo + "]";
	}
	
	

}
