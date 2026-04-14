package MaterialMgmt.IORegInq.Service;

import java.util.List;
import MaterialMgmt.IORegInq.DAO.IORegInqDAO;
import MaterialMgmt.IORegInq.DTO.IORegInqDTO;

public class IORegInqService {
    public List<IORegInqDTO> getIORegInqList(IORegInqDTO searchDTO) { return new IORegInqDAO().selectIORegInqList(searchDTO); }
    public IORegInqDTO getIORegInqDetail(int inoutId) { return new IORegInqDAO().selectIORegInqOne(inoutId); }
    public int register(IORegInqDTO dto) { return new IORegInqDAO().insertIORegInq(dto); }
    public int delete(int[] inoutIds) { return new IORegInqDAO().deleteIORegInq(inoutIds); }
}
