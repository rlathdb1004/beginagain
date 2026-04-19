package MaterialMgmt.InvRegInq.Service;

import java.util.List;

import MaterialMgmt.InvRegInq.DAO.InvRegInqDAO;
import MaterialMgmt.InvRegInq.DTO.InvRegInqDTO;

public class InvRegInqService {
    private final InvRegInqDAO dao = new InvRegInqDAO();

    public List<InvRegInqDTO> getInvRegInqList(InvRegInqDTO searchDTO) {
        return dao.selectInvRegInqList(searchDTO);
    }

    public InvRegInqDTO getInvRegInqDetail(int inventoryId) {
        return dao.selectInvRegInqOne(inventoryId);
    }

    public int register(InvRegInqDTO dto) {
        throw new RuntimeException("재고는 직접 등록하지 않고 입출고 결과로 관리합니다.");
    }

    public int delete(int[] inventoryIds) {
        throw new RuntimeException("재고는 삭제하지 않습니다.");
    }

    public int update(InvRegInqDTO dto) {
        if (!dao.existsInventory(dto.getInventoryId())) {
            throw new RuntimeException("존재하지 않는 재고입니다.");
        }
        if (dto.getSafetyStock() < 0) {
            throw new RuntimeException("안전재고는 0 이상이어야 합니다.");
        }
        return dao.updateInvRegInq(dto);
    }
}
