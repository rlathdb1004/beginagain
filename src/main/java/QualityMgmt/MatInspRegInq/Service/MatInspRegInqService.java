package QualityMgmt.MatInspRegInq.Service;

import java.util.List;
import QualityMgmt.MatInspRegInq.DAO.MatInspRegInqDAO;
import QualityMgmt.MatInspRegInq.DTO.MatInspRegInqDTO;

public class MatInspRegInqService {
    public List<MatInspRegInqDTO> getMatInspRegInqList(MatInspRegInqDTO searchDTO) { return new MatInspRegInqDAO().selectMatInspRegInqList(searchDTO); }
    public MatInspRegInqDTO getMatInspRegInqDetail(int materialInspectionId) { return new MatInspRegInqDAO().selectMatInspRegInqOne(materialInspectionId); }
    public int register(MatInspRegInqDTO dto) { return new MatInspRegInqDAO().insertMatInspRegInq(dto); }
    public int delete(int[] ids) { return new MatInspRegInqDAO().deleteMatInspRegInq(ids); }
}
