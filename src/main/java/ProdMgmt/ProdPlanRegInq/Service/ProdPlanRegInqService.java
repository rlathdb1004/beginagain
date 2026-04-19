package ProdMgmt.ProdPlanRegInq.Service;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import ProdMgmt.ProdPlanRegInq.DAO.ProdPlanRegInqDAO;
import ProdMgmt.ProdPlanRegInq.DTO.ProdPlanRegInqDTO;

public class ProdPlanRegInqService {
    private static final Set<String> ALLOWED_LINES = new HashSet<String>(Arrays.asList("LN-A", "LN-B", "LN-C"));
    private static final Set<String> ALLOWED_STATUS = new HashSet<String>(Arrays.asList("대기", "작업지시중", "작업지시완료", "진행중", "완료"));

    private final ProdPlanRegInqDAO dao = new ProdPlanRegInqDAO();

    public int getTotalCount(String startDate, String endDate, String searchType, String keyword) {
        return dao.getTotalCount(startDate, endDate, searchType, keyword);
    }

    public List<ProdPlanRegInqDTO> getListByPage(String startDate, String endDate, String searchType, String keyword,
            int startRow, int endRow) {
        return dao.getListByPage(startDate, endDate, searchType, keyword, startRow, endRow);
    }

    public ProdPlanRegInqDTO getDetailById(int planId) {
        return dao.getDetailById(planId);
    }

    public List<ProdPlanRegInqDTO> getFinishedItemOptions() {
        return dao.getFinishedItemOptions();
    }

    public int insert(ProdPlanRegInqDTO dto) {
        validateCommon(dto);
        if (!dao.existsActiveFinishedItem(dto.getItemId())) {
            throw new IllegalArgumentException("유효한 완제품만 생산계획에 등록할 수 있습니다.");
        }
        return dao.insert(dto);
    }

    public int update(ProdPlanRegInqDTO dto) {
        validateCommon(dto);

        ProdPlanRegInqDTO origin = dao.getDetailById(dto.getSeqNO());
        if (origin == null) {
            throw new IllegalArgumentException("생산계획 정보를 찾을 수 없습니다.");
        }

        int workOrderCount = dao.countActiveWorkOrdersByPlanId(dto.getSeqNO());
        int workOrderSum = dao.getWorkOrderQtySumByPlanId(dto.getSeqNO());

        if (dto.getPlanAmount() < workOrderSum) {
            throw new IllegalArgumentException("생산계획량은 현재 작업지시 합계보다 작을 수 없습니다.");
        }
        if (workOrderCount > 0 && origin.getItemId() != dto.getItemId()) {
            throw new IllegalStateException("작업지시가 연결된 생산계획은 품목을 변경할 수 없습니다.");
        }
        if (!dao.existsActiveFinishedItem(dto.getItemId())) {
            throw new IllegalArgumentException("유효한 완제품만 생산계획에 등록할 수 있습니다.");
        }
        return dao.update(dto);
    }

    public int deleteByIds(String[] seqNos) {
        if (seqNos == null || seqNos.length == 0) return 0;

        for (String seqNo : seqNos) {
            int planId = Integer.parseInt(seqNo);
            if (dao.countActiveWorkOrdersByPlanId(planId) > 0) {
                throw new IllegalStateException("작업지시가 연결된 생산계획은 삭제할 수 없습니다.");
            }
        }
        return dao.deleteByIds(seqNos);
    }

    private void validateCommon(ProdPlanRegInqDTO dto) {
        if (dto.getItemId() <= 0) {
            throw new IllegalArgumentException("품목을 선택하세요.");
        }
        if (dto.getPlanDate() == null) {
            throw new IllegalArgumentException("계획일자를 입력하세요.");
        }
        if (dto.getPlanAmount() <= 0) {
            throw new IllegalArgumentException("계획수량은 1 이상이어야 합니다.");
        }
        if (dto.getPlanLine() == null || !ALLOWED_LINES.contains(dto.getPlanLine())) {
            throw new IllegalArgumentException("라인은 LN-A, LN-B, LN-C 중에서 선택하세요.");
        }
        if (dto.getStatus() == null || !ALLOWED_STATUS.contains(dto.getStatus())) {
            throw new IllegalArgumentException("허용되지 않은 상태값입니다.");
        }
    }
}
