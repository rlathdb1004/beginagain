package MasterDataMgmt.BOMManagement;

import java.time.LocalDate;

public class BOMMgmtDTO {
	
	private int bom_id;
	public int getBOM_id() { return bom_id; }
	public void setBOM_id(int BOM_id) { this.bom_id = BOM_id; }
	
    private String item_code;
    public String getItem_code() { return item_code; }
    public void setItem_code(String item_code) { this.item_code = item_code; }
    
    private String item_name; 
    public String getItem_name() { return item_name; }
    public void setItem_name(String item_name) { this.item_name = item_name; }
    
    private String remark;
    public String getRemark() { return remark; }
    public void setRemark(String remark) { this.remark = remark; }
    
    private LocalDate created_at;
    public LocalDate getCreated_at() { return created_at; }
    public void setCreated_at(LocalDate created_at) { this.created_at = created_at; }

}
