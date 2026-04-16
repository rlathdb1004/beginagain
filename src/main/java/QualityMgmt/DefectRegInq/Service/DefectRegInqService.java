package QualityMgmt.DefectRegInq.Service;

import java.util.List;
import QualityMgmt.DefectRegInq.DAO.DefectRegInqDAO;
import QualityMgmt.DefectRegInq.DTO.DefectRegInqDTO;

public class DefectRegInqService {
    public List<DefectRegInqDTO> getDefectRegInqList(DefectRegInqDTO searchDTO) { return new DefectRegInqDAO().selectDefectRegInqList(searchDTO); }
    public DefectRegInqDTO getDefectRegInqDetail(int defectProductId) { return new DefectRegInqDAO().selectDefectRegInqOne(defectProductId); }
    public int register(DefectRegInqDTO dto) { return new DefectRegInqDAO().insertDefectRegInq(dto); }
    public int delete(int[] ids) { return new DefectRegInqDAO().deleteDefectRegInq(ids); }
    public int update(DefectRegInqDTO dto) { return new DefectRegInqDAO().updateDefectRegInq(dto); }
}
