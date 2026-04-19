package MasterDataMgmt.DefectManagement.dto;

public class DefectMgmtSearchDTO {

    private String searchType;
    private String keyword;
    private int page = 1;
    private int pageSize = 10;

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

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getStartRow() {
        return (page - 1) * pageSize + 1;
    }

    public int getEndRow() {
        return page * pageSize;
    }
}
