package QualityMgmt.DefectRegInq.Service;

import java.util.List;

import QualityMgmt.DefectRegInq.DAO.DefectRegInqDAO;
import QualityMgmt.DefectRegInq.DTO.DefectRegInqDTO;

public class DefectRegInqService {
    private final DefectRegInqDAO dao = new DefectRegInqDAO();

    public List<DefectRegInqDTO> getDefectRegInqList(DefectRegInqDTO searchDTO) { return dao.selectDefectRegInqList(searchDTO); }
    public DefectRegInqDTO getDefectRegInqDetail(int defectProductId) { return dao.selectDefectRegInqOne(defectProductId); }
    public List<DefectRegInqDTO> getAvailableFinalInspectionList() { return dao.selectAvailableFinalInspectionList(); }
    public List<DefectRegInqDTO> getAvailableDefectCodeList() { return dao.selectAvailableDefectCodeList(); }

    public int register(DefectRegInqDTO dto) {
        if (!dao.existsActiveFinalInspection(dto.getFinalInspectionId())) throw new RuntimeException("유효한 완제품검사만 선택할 수 있습니다.");
        if (!dao.existsActiveDefectCode(dto.getDefectCodeId())) throw new RuntimeException("유효한 불량코드만 선택할 수 있습니다.");
        String inspectionStatus = dao.getInspectionStatus(dto.getFinalInspectionId());
        if ("합격".equals(inspectionStatus)) throw new RuntimeException("합격 판정의 완제품검사에는 불량이력을 등록할 수 없습니다.");
        if (dao.existsDuplicateMapping(dto.getFinalInspectionId(), dto.getDefectCodeId())) throw new RuntimeException("같은 완제품검사에는 동일한 불량코드를 중복 등록할 수 없습니다.");
        return dao.insertDefectRegInq(dto);
    }

    public int update(DefectRegInqDTO dto) {
        if (dto.getDefectProductId() <= 0) throw new RuntimeException("유효한 불량이력만 수정할 수 있습니다.");
        return dao.updateDefectRegInq(dto);
    }

    public int delete(int[] ids) {
        if (ids == null || ids.length == 0) return 0;
        return dao.deleteDefectRegInq(ids);
    }
}
