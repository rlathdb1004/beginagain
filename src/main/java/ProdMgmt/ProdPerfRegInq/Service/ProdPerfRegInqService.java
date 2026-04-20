package ProdMgmt.ProdPerfRegInq.Service;

import java.util.List;

import ProdMgmt.ProdPerfRegInq.DAO.ProdPerfRegInqDAO;
import ProdMgmt.ProdPerfRegInq.DTO.ProdPerfRegInqDTO;

public class ProdPerfRegInqService {
    private ProdPerfRegInqDAO dao = new ProdPerfRegInqDAO();

    public int getTotalCount(String startDate, String endDate, String searchType, String keyword) {
        return dao.getTotalCount(startDate, endDate, searchType, keyword);
    }

    public List<ProdPerfRegInqDTO> getListByPage(String startDate, String endDate, String searchType, String keyword, int startRow, int endRow) {
        return dao.getListByPage(startDate, endDate, searchType, keyword, startRow, endRow);
    }

    public List<ProdPerfRegInqDTO> getWorkOrderOptions() {
        return dao.getWorkOrderOptions();
    }

    public List<ProdPerfRegInqDTO> getWorkOrderOptionsByEmpId(int empId) {
        return dao.getWorkOrderOptionsByEmpId(empId);
    }

    public ProdPerfRegInqDTO getDetail(int resultId) {
        return dao.getDetail(resultId);
    }

    public boolean insertProductionResult(ProdPerfRegInqDTO dto) {
        validateCommon(dto);

        int workQty = dao.getWorkQtyByWorkOrderId(dto.getWorkOrderId());
        int producedSum = dao.getProducedSumByWorkOrderId(dto.getWorkOrderId());
        int lossSum = dao.getLossSumByWorkOrderId(dto.getWorkOrderId());

        if (workQty <= 0) {
            throw new IllegalArgumentException("유효한 작업지시만 선택할 수 있습니다.");
        }
        if ((producedSum + lossSum + dto.getProducedQty() + dto.getLossQty()) > workQty) {
            throw new IllegalArgumentException("생산량과 손실량의 누적합이 작업지시 수량을 초과할 수 없습니다.");
        }
        if (dao.countLotNo(dto.getLotNo(), null) > 0) {
            throw new IllegalArgumentException("이미 사용 중인 LOT 번호입니다.");
        }

        return dao.insertProductionResult(dto) > 0;
    }

    public boolean updateProductionResult(ProdPerfRegInqDTO dto) {
        validateCommon(dto);

        ProdPerfRegInqDTO original = dao.getDetail(dto.getSeqNO());
        if (original == null) {
            throw new IllegalArgumentException("존재하지 않는 생산실적입니다.");
        }
        if (dto.getWorkOrderId() != original.getWorkOrderId()) {
            throw new IllegalArgumentException("작업지시는 수정할 수 없습니다.");
        }
        if (dao.countFinalInspectionsByResultId(dto.getSeqNO()) > 0) {
            throw new IllegalArgumentException("완제품검사가 연결된 생산실적은 수정할 수 없습니다.");
        }

        int workQty = dao.getWorkQtyByWorkOrderId(original.getWorkOrderId());
        int producedSum = dao.getProducedSumByWorkOrderId(original.getWorkOrderId());
        int lossSum = dao.getLossSumByWorkOrderId(original.getWorkOrderId());
        int currentProduced = dao.getCurrentProducedQty(dto.getSeqNO());
        int currentLoss = dao.getCurrentLossQty(dto.getSeqNO());

        if ((producedSum + lossSum - currentProduced - currentLoss + dto.getProducedQty() + dto.getLossQty()) > workQty) {
            throw new IllegalArgumentException("생산량과 손실량의 누적합이 작업지시 수량을 초과할 수 없습니다.");
        }
        if (dao.countLotNo(dto.getLotNo(), Integer.valueOf(dto.getSeqNO())) > 0) {
            throw new IllegalArgumentException("이미 사용 중인 LOT 번호입니다.");
        }

        dto.setWorkOrderId(original.getWorkOrderId());
        return dao.updateProductionResult(dto) > 0;
    }

    public int deleteByIds(String[] seqNos) {
        if (seqNos == null || seqNos.length == 0) {
            return 0;
        }
        if (dao.countFinalInspectionsByResultIds(seqNos) > 0) {
            throw new IllegalArgumentException("완제품검사가 연결된 생산실적은 삭제할 수 없습니다.");
        }
        return dao.deleteByIds(seqNos);
    }

    private void validateCommon(ProdPerfRegInqDTO dto) {
        if (dto.getWorkOrderId() <= 0) {
            throw new IllegalArgumentException("작업지시를 선택하세요.");
        }
        if (dto.getResultDate() == null) {
            throw new IllegalArgumentException("생산실적일자를 입력하세요.");
        }
        if (dto.getProducedQty() < 0 || dto.getLossQty() < 0) {
            throw new IllegalArgumentException("생산량과 손실량은 0 이상이어야 합니다.");
        }
        if (dto.getProducedQty() + dto.getLossQty() <= 0) {
            throw new IllegalArgumentException("생산량과 손실량의 합은 1 이상이어야 합니다.");
        }
        if (dto.getLotNo() == null || dto.getLotNo().trim().equals("")) {
            throw new IllegalArgumentException("LOT 번호를 입력하세요.");
        }
        dto.setStatus("완료");
    }
}
