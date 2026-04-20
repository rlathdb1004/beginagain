package QualityMgmt.FPInspRegInq.Service;

import java.util.List;

import QualityMgmt.FPInspRegInq.DAO.FPInspRegInqDAO;
import QualityMgmt.FPInspRegInq.DTO.FPInspRegInqDTO;

public class FPInspRegInqService {
    private final FPInspRegInqDAO dao = new FPInspRegInqDAO();

    public List<FPInspRegInqDTO> getFPInspRegInqList(FPInspRegInqDTO searchDTO) { return dao.selectFPInspRegInqList(searchDTO); }
    public FPInspRegInqDTO getFPInspRegInqDetail(int finalInspectionId) { return dao.selectFPInspRegInqOne(finalInspectionId); }
    public List<FPInspRegInqDTO> getAvailableProductionResultList() { return dao.selectAvailableProductionResultList(); }

    public int register(FPInspRegInqDTO dto) {
        validateStatus(dto.getResult());
        if (dto.getEmpId() <= 0) throw new RuntimeException("로그인 사용자 정보를 확인할 수 없습니다.");
        if (!dao.existsActiveProductionResult(dto.getResultId())) throw new RuntimeException("유효한 생산실적만 선택할 수 있습니다.");
        if (dto.getInspectQty() <= 0) throw new RuntimeException("검사수량은 0보다 커야 합니다.");
        int producedQty = dao.getProducedQtyByResultId(dto.getResultId());
        int currentSum = dao.sumInspectQtyByResultId(dto.getResultId());
        if (currentSum + dto.getInspectQty() > producedQty) throw new RuntimeException("검사수량 합계가 생산량을 초과할 수 없습니다.");
        return dao.insertFPInspRegInq(dto);
    }

    public int update(FPInspRegInqDTO dto) {
        validateStatus(dto.getResult());
        if (dto.getInspectQty() <= 0) throw new RuntimeException("검사수량은 0보다 커야 합니다.");
        if (dao.countDefectProductByFinalInspectionId(dto.getFinalInspectionId()) > 0) throw new RuntimeException("불량이력이 연결된 완제품검사는 수정할 수 없습니다.");
        int resultId = dao.getResultIdByFinalInspectionId(dto.getFinalInspectionId());
        int producedQty = dao.getProducedQtyByResultId(resultId);
        int currentSum = dao.sumInspectQtyByResultId(resultId);
        int oldQty = dao.getCurrentInspectQty(dto.getFinalInspectionId());
        if (currentSum - oldQty + dto.getInspectQty() > producedQty) throw new RuntimeException("검사수량 합계가 생산량을 초과할 수 없습니다.");
        return dao.updateFPInspRegInq(dto);
    }

    public int delete(int[] ids) {
        if (ids == null || ids.length == 0) return 0;
        for (int id : ids) {
            if (dao.countDefectProductByFinalInspectionId(id) > 0) throw new RuntimeException("불량이력이 연결된 완제품검사는 삭제할 수 없습니다.");
        }
        return dao.deleteFPInspRegInq(ids);
    }

    private void validateStatus(String status) {
        if (!"합격".equals(status) && !"불합격".equals(status)) {
            throw new RuntimeException("허용되지 않은 판정값입니다.");
        }
    }
}
