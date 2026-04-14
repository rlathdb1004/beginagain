package MasterDataMgmt.BOMManagement;

import java.util.List;

public class BOMMgmtService {
	private BOMMgmtDAO dao = new BOMMgmtDAO();

   
    public List<BOMMgmtDTO> getBOMList(BOMMgmtSearchDTO searchDTO) {
        return dao.getBOMList(searchDTO);
    }

    
    public void addBOM(BOMMgmtDTO dto) {
        
        if (dto.getItem_code() == null || dto.getItem_code().isEmpty()) {
            throw new IllegalArgumentException("품목 코드는 필수입니다.");
        }

        dao.insertBOM(dto);
    }

    
//    public void removeBOM(String[] ids) {
//        if (ids != null && ids.length > 0) {
//            dao.deleteItems(ids);
//        }
//    }
}
