package MasterDataMgmt.BOMManagement;

public class BOMMgmtSearchDTO {
	private String startDate;  
    public String getStartDate() { return startDate;}
    public void setStartDate(String startDate) { this.startDate = startDate;}
    
    private String endDate; 
    public String getEndDate() { return endDate;}
    public void setEndDate(String endDate) {this.endDate = endDate;}
    
    private String field;  
    public String getField() {return field; }
    public void setField(String field) {this.field = field;}
    
    private String keyword; 
    public String getKeyword() { return keyword;}
    public void setKeyword(String keyword) {this.keyword = keyword;}
}
