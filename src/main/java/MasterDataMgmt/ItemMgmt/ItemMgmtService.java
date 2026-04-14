package MasterDataMgmt.ItemMgmt;

import java.util.List;

public class ItemMgmtService {
	private ItemMgmtDAO dao = new ItemMgmtDAO();

	public List<ItemMgmtDTO> getItemList() {
		return dao.getItemList();
	}

	public List<ItemMgmtDTO> search(String type, String dateType, String startDate, String endDate, String field,
			String keyword) {

		return dao.search(type, dateType, startDate, endDate, field, keyword);
	}
}
