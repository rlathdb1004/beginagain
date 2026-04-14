package MasterDataMgmt.ItemMgmt;

import java.util.List;

public class ItemMgmtService {
    private ItemMgmtDAO dao = new ItemMgmtDAO();

    public List<ItemMgmtDTO> getItemList(ItemMgmtSearchDTO dto) {
        return dao.getItemList(dto);
    }
    public void deleteItem(int id) {
        dao.deleteItem(id);
    }
}
