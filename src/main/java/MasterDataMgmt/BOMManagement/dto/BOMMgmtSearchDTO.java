package MasterDataMgmt.BOMManagement.dto;

public class BOMMgmtSearchDTO {
	private String product_code;

	public String getProduct_code() {
		return product_code;
	}

	public void setProduct_code(String product_code) {
		this.product_code = product_code;
	}
	
	private String keyword;
    public String getKeyword() { return keyword; }
    public void setKeyword(String keyword) { this.keyword = keyword; }
}
