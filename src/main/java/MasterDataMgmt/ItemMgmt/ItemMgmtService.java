package MasterDataMgmt.ItemMgmt;

import java.util.List;

import MasterDataMgmt.DefectManagement.DefectMgmtDTO;


public class ItemMgmtService {
    private ItemMgmtDAO dao = new ItemMgmtDAO();

    public List<ItemMgmtDTO> getItemList(ItemMgmtSearchDTO dto) {
        return dao.getItemList(dto);
    }
    
    public void insert(ItemMgmtDTO dto) {
        dao.insertItem(dto);
    }
    
    public void deleteItem(int id) {
        dao.delete(id);
    }
    
    public int delete(int id) {
        return dao.delete(id);
    }

    public int deleteList(List<Integer> ids) {
        int result = 0;
        for (int id : ids) {
            result += dao.delete(id);
        }
        return result;
    }    
    
    public int update(ItemMgmtDTO dto) {
        ItemMgmtDAO dao = new ItemMgmtDAO();
        return dao.update(dto);
    }
}
