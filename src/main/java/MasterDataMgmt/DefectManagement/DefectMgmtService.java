package MasterDataMgmt.DefectManagement;

import java.util.List;

public class DefectMgmtService {
	private DefectMgmtDAO dao = new DefectMgmtDAO();

    public List<DefectMgmtDTO> getList(DefectMgmtSearchDTO dto) {
        return dao.getList(dto);
    }

    public void insert(DefectMgmtDTO dto) {
        dao.insert(dto);
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
    
    public int update(DefectMgmtDTO dto) {
        DefectMgmtDAO dao = new DefectMgmtDAO();
        return dao.update(dto);
    }
}
