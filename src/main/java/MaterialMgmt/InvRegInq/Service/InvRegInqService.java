package MaterialMgmt.InvRegInq.Service;

import java.util.List;

import MaterialMgmt.InvRegInq.DAO.InvRegInqDAO;
import MaterialMgmt.InvRegInq.DTO.InvRegInqDTO;

/*
 * 재고 등록 / 조회 Service
 * 
 * Controller에서 받은 요청을 DAO로 전달
 */
public class InvRegInqService {

    // 재고 목록 조회
    public List<InvRegInqDTO> getInvRegInqList(InvRegInqDTO searchDTO) {
        InvRegInqDAO dao = new InvRegInqDAO();
        return dao.selectInvRegInqList(searchDTO);
    }

    // 재고 상세 조회
    public InvRegInqDTO getInvRegInqDetail(int inventoryId) {
        InvRegInqDAO dao = new InvRegInqDAO();
        return dao.selectInvRegInqOne(inventoryId);
    }
}