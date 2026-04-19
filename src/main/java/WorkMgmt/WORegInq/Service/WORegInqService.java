package WorkMgmt.WORegInq.Service;

import java.util.ArrayList;
import java.util.List;

import WorkMgmt.WORegInq.DAO.WORegInqDAO;
import WorkMgmt.WORegInq.DTO.WORegInqDTO;

public class WORegInqService {

    private WORegInqDAO dao = new WORegInqDAO();

    public int getTotalCount(String startDate, String endDate, String searchType, String keyword) {
        return dao.getTotalCount(startDate, endDate, searchType, keyword);
    }

    public List<WORegInqDTO> getListByPage(String startDate, String endDate, String searchType, String keyword, int startRow, int endRow) {
        return dao.getListByPage(startDate, endDate, searchType, keyword, startRow, endRow);
    }

    public List<WORegInqDTO> getPlanOptions() {
        return dao.getPlanOptions();
    }

    public List<WORegInqDTO> getEmpOptions() {
        return dao.getEmpOptions();
    }

    public WORegInqDTO getDetail(int workOrderId) {
        return dao.getDetail(workOrderId);
    }

    public boolean insertWorkOrder(WORegInqDTO dto) {
        validateCommon(dto);

        int itemId = dao.getItemIdByPlanId(dto.getPlanId());
        int planQty = dao.getPlanQtyByPlanId(dto.getPlanId());
        int currentSum = dao.getWorkQtySumByPlanId(dto.getPlanId());

        if (itemId <= 0 || planQty <= 0) {
            throw new IllegalArgumentException("유효한 생산계획만 선택할 수 있습니다.");
        }
        if (currentSum + dto.getWorkQty() > planQty) {
            throw new IllegalArgumentException("작업지시 총합이 생산계획량을 초과할 수 없습니다.");
        }

        dto.setItemId(itemId);
        boolean inserted = dao.insertWorkOrder(dto) > 0;
        if (inserted) {
            dao.updatePlanStatusByPlanId(dto.getPlanId());
        }
        return inserted;
    }

    public boolean updateWorkOrder(WORegInqDTO dto) {
        validateCommon(dto);

        WORegInqDTO original = dao.getDetail(dto.getSeqNO());
        if (original == null) {
            throw new IllegalArgumentException("존재하지 않는 작업지시입니다.");
        }
        if (dto.getPlanId() != original.getPlanId()) {
            throw new IllegalArgumentException("생산계획은 수정할 수 없습니다.");
        }
        if (dao.countProductionResultsByWorkOrderId(dto.getSeqNO()) > 0) {
            throw new IllegalArgumentException("생산실적이 있는 작업지시는 수정할 수 없습니다.");
        }

        int planQty = dao.getPlanQtyByPlanId(original.getPlanId());
        int currentSum = dao.getWorkQtySumByPlanId(original.getPlanId());
        int oldQty = dao.getCurrentWorkQty(dto.getSeqNO());

        if ((currentSum - oldQty + dto.getWorkQty()) > planQty) {
            throw new IllegalArgumentException("작업지시 총합이 생산계획량을 초과할 수 없습니다.");
        }

        dto.setItemId(original.getItemId());
        dto.setPlanId(original.getPlanId());

        boolean updated = dao.updateWorkOrder(dto) > 0;
        if (updated) {
            dao.updatePlanStatusByPlanId(dto.getPlanId());
        }
        return updated;
    }

    public int deleteByIds(String[] seqNos) {
        if (seqNos == null || seqNos.length == 0) {
            return 0;
        }
        if (dao.countProductionResultsByWorkOrderIds(seqNos) > 0) {
            throw new IllegalArgumentException("생산실적이 연결된 작업지시는 삭제할 수 없습니다.");
        }

        List<WORegInqDTO> before = getListForDelete(seqNos);
        int deleted = dao.deleteByIds(seqNos);
        if (deleted > 0) {
            for (WORegInqDTO dto : before) {
                dao.updatePlanStatusByPlanId(dto.getPlanId());
            }
        }
        return deleted;
    }

    private List<WORegInqDTO> getListForDelete(String[] seqNos) {
        ArrayList<WORegInqDTO> list = new ArrayList<WORegInqDTO>();
        for (String seqNo : seqNos) {
            try {
                WORegInqDTO dto = dao.getDetail(Integer.parseInt(seqNo));
                if (dto != null) {
                    list.add(dto);
                }
            } catch (Exception e) {
            }
        }
        return list;
    }

    private void validateCommon(WORegInqDTO dto) {
        if (dto.getPlanId() <= 0) {
            throw new IllegalArgumentException("생산계획을 선택하세요.");
        }
        if (dto.getEmpId() <= 0) {
            throw new IllegalArgumentException("작업자를 선택하세요.");
        }
        if (dto.getWorkDate() == null) {
            throw new IllegalArgumentException("작업일자를 입력하세요.");
        }
        if (dto.getWorkQty() <= 0) {
            throw new IllegalArgumentException("지시량은 1 이상이어야 합니다.");
        }
        if (!isAllowedStatus(dto.getStatus())) {
            throw new IllegalArgumentException("허용되지 않은 상태값입니다.");
        }
    }

    private boolean isAllowedStatus(String status) {
        return "대기".equals(status) || "진행중".equals(status) || "완료".equals(status);
    }
}
