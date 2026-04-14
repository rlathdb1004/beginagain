package QualityMgmt.FPInspRegInq.Service;

import java.util.List;
import QualityMgmt.FPInspRegInq.DAO.FPInspRegInqDAO;
import QualityMgmt.FPInspRegInq.DTO.FPInspRegInqDTO;

public class FPInspRegInqService {
    public List<FPInspRegInqDTO> getFPInspRegInqList(FPInspRegInqDTO searchDTO) { return new FPInspRegInqDAO().selectFPInspRegInqList(searchDTO); }
    public FPInspRegInqDTO getFPInspRegInqDetail(int finalInspectionId) { return new FPInspRegInqDAO().selectFPInspRegInqOne(finalInspectionId); }
    public int register(FPInspRegInqDTO dto) { return new FPInspRegInqDAO().insertFPInspRegInq(dto); }
    public int delete(int[] ids) { return new FPInspRegInqDAO().deleteFPInspRegInq(ids); }
}
