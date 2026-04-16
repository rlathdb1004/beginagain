package MaterialMgmt.InvRegInq.Service;

import java.util.List;
import MaterialMgmt.InvRegInq.DAO.InvRegInqDAO;
import MaterialMgmt.InvRegInq.DTO.InvRegInqDTO;

public class InvRegInqService {
    public List<InvRegInqDTO> getInvRegInqList(InvRegInqDTO searchDTO) { return new InvRegInqDAO().selectInvRegInqList(searchDTO); }
    public InvRegInqDTO getInvRegInqDetail(int inventoryId) { return new InvRegInqDAO().selectInvRegInqOne(inventoryId); }
    public int register(InvRegInqDTO dto) { return new InvRegInqDAO().insertInvRegInq(dto); }
    public int delete(int[] inventoryIds) { return new InvRegInqDAO().deleteInvRegInq(inventoryIds); }
    public int update(InvRegInqDTO dto) { return new InvRegInqDAO().updateInvRegInq(dto); }
}
