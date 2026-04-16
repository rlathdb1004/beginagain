package MasterDataMgmt.ItemMgmt;

public class ItemMgmtSearchDTO {
	
	    private String type; 
	    public String getType() {
	        return type;
	    }
	    public void setType(String type) {
	        this.type = type;
	    }
	   
	    private String dateType; 
	    public String getDateType() {
	        return dateType;
	    }
	    public void setDateType(String dateType) {
	        this.dateType = dateType;
	    }
	    
	    private String startDate;  
	    public String getStartDate() {
	        return startDate;
	    }
	    public void setStartDate(String startDate) {
	        this.startDate = startDate;
	    }
	    
	    private String endDate; 
	    public String getEndDate() {
	        return endDate;
	    }
	    public void setEndDate(String endDate) {
	        this.endDate = endDate;
	    }
	   
	    private String field;  
	    public String getField() {
	        return field;
	    }
	    public void setField(String field) {
	        this.field = field;
	    }
	    
	    private String keyword; 
	    public String getKeyword() {
	        return keyword;
	    }
	    public void setKeyword(String keyword) {
	        this.keyword = keyword;
	    }
}
