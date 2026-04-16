package MasterDataMgmt.ItemMgmt;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class ItemMgmtDTO {
	private int item_id;
	public int getItem_id() {return item_id;}
	public void setItem_id(int Item_id) { this.item_id = Item_id;}
	
	private String item_code;
	public String getItem_code() {return item_code;}
	public void setItem_code(String Item_code) { this.item_code = Item_code;}
	
	private String item_name;
	public String getItem_name() {return item_name;}
	public void setItem_name(String Item_name) { this.item_name =  Item_name;}
	
	private String item_type;
	public String getItem_type() {return item_type;}
	public void setItem_type(String Item_type) { this.item_type =  Item_type;}
	
	private String unit;
	public String getUnit() {return unit;}
	public void setUnit(String Unit) { this.unit = Unit ;}
	
	private String spec;
	public String getSpec() {return spec;}
	public void setSpec(String Spec) { this.spec = Spec ;}
	
	private String supplier_name;
	public String getSupplier_name() {return supplier_name;}
	public void setSupplier_name(String Supplier_name) { this.supplier_name = Supplier_name ;}
	
	private int safety_stock;
	public int getSafety_stock() {return safety_stock;}
	public void setSafety_stock (int Safety_stock) { this.safety_stock = Safety_stock ;}
	
	private String use_yn;
	public String getUse_yn() {return use_yn;}
	public void setUse_yn(String Use_yn) { this.use_yn = Use_yn;}
	
	private String remark;
	public String getRemark() {return remark;}
	public void setRemark(String Remark) { this.remark = Remark ;}
	
	private LocalDate created_at;
	public LocalDate getCreated_at() {return created_at;}
	public void setCreated_at(LocalDate Created_at) { this.created_at = Created_at ;}
	
	private LocalDate updated_at;
	public LocalDate getUpdated_at() {return updated_at;}
	public void setUpdated_at(LocalDate Updated_at) { this.updated_at = Updated_at ;}
}
