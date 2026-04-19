package QualityMgmt.MatInspRegInq.Service;

import java.util.List;

import QualityMgmt.MatInspRegInq.DAO.MatInspRegInqDAO;
import QualityMgmt.MatInspRegInq.DTO.MatInspRegInqDTO;
import item.dto.ItemDTO;

public class MatInspRegInqService {
    private final MatInspRegInqDAO dao = new MatInspRegInqDAO();

    public List<MatInspRegInqDTO> getMatInspRegInqList(MatInspRegInqDTO searchDTO) { return dao.selectMatInspRegInqList(searchDTO); }
    public MatInspRegInqDTO getMatInspRegInqDetail(int materialInspectionId) { return dao.selectMatInspRegInqOne(materialInspectionId); }
    public List<ItemDTO> getMaterialItemList() { return dao.selectMaterialItemList(); }

    public int register(MatInspRegInqDTO dto) {
        validateForSave(dto, false);
        return dao.insertMatInspRegInq(dto);
    }

    public int delete(int[] ids) {
        if (ids == null || ids.length == 0) return 0;
        for (int id : ids) {
            if (dao.hasMaterialDefect(id)) {
                throw new RuntimeException("자재불량이 등록된 검사건은 삭제할 수 없습니다.");
            }
        }
        return dao.deleteMatInspRegInq(ids);
    }

    public int update(MatInspRegInqDTO dto) {
        if (dao.hasMaterialDefect(dto.getMaterialInspectionId())) {
            throw new RuntimeException("자재불량이 등록된 검사건은 수정할 수 없습니다.");
        }
        MatInspRegInqDTO origin = dao.selectMatInspRegInqOne(dto.getMaterialInspectionId());
        if (origin == null) {
            throw new RuntimeException("존재하지 않는 자재 검사입니다.");
        }
        dto.setItemId(origin.getItemId());
        dto.setEmpId(origin.getEmpId());
        validateForSave(dto, true);
        return dao.updateMatInspRegInq(dto);
    }

    private void validateForSave(MatInspRegInqDTO dto, boolean isUpdate) {
        if (!dao.existsMaterialItem(dto.getItemId())) {
            throw new RuntimeException("유효한 자재 품목만 선택할 수 있습니다.");
        }
        if (!dao.existsActiveEmp(dto.getEmpId())) {
            throw new RuntimeException("유효한 검사자 정보가 없습니다. 다시 로그인 후 시도해주세요.");
        }
        if (dto.getInspectQty() <= 0) {
            throw new RuntimeException("검사수량은 0보다 커야 합니다.");
        }
        if (dto.getGoodQty() < 0 || dto.getDefectQty() < 0) {
            throw new RuntimeException("양품수량과 불량수량은 음수일 수 없습니다.");
        }
        if (!equalsQty(dto.getGoodQty() + dto.getDefectQty(), dto.getInspectQty())) {
            throw new RuntimeException("양품수량 + 불량수량은 검사수량과 같아야 합니다.");
        }
        String result = dto.getResult();
        if (!("합격".equals(result) || "부분합격".equals(result) || "불합격".equals(result))) {
            throw new RuntimeException("판정값이 올바르지 않습니다.");
        }
        if (equalsQty(dto.getDefectQty(), 0d) && !"합격".equals(result)) {
            throw new RuntimeException("불량수량이 0이면 판정은 합격이어야 합니다.");
        }
        if (equalsQty(dto.getDefectQty(), dto.getInspectQty()) && !"불합격".equals(result)) {
            throw new RuntimeException("불량수량이 검사수량과 같으면 판정은 불합격이어야 합니다.");
        }
        if (dto.getDefectQty() > 0 && dto.getDefectQty() < dto.getInspectQty() && !"부분합격".equals(result)) {
            throw new RuntimeException("불량수량이 일부 존재하면 판정은 부분합격이어야 합니다.");
        }
    }

    private boolean equalsQty(double a, double b) {
        return Math.abs(a - b) < 0.000001d;
    }
}
