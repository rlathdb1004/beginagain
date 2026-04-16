package WorkMgmt.WORegInq.Service;

import java.util.List;

import WorkMgmt.WORegInq.DAO.WORegInqDAO;
import WorkMgmt.WORegInq.DTO.WORegInqDTO;

public class WORegInqService {

    private WORegInqDAO dao = new WORegInqDAO();

    public int getTotalCount(String startDate, String endDate, String searchType, String keyword) {
        return dao.getTotalCount(startDate, endDate, searchType, keyword);
    }

    public List<WORegInqDTO> getListByPage(
            String startDate,
            String endDate,
            String searchType,
            String keyword,
            int startRow,
            int endRow) {

        return dao.getListByPage(startDate, endDate, searchType, keyword, startRow, endRow);
    }

    public int deleteByIds(String[] seqNos) {
        return dao.deleteByIds(seqNos);
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
        int itemId = dao.getItemIdByPlanId(dto.getPlanId());
        if (itemId <= 0) {
            return false;
        }
        dto.setItemId(itemId);
        return dao.insertWorkOrder(dto) > 0;
    }

    public boolean updateWorkOrder(WORegInqDTO dto) {
        int itemId = dao.getItemIdByPlanId(dto.getPlanId());
        if (itemId <= 0) {
            return false;
        }
        dto.setItemId(itemId);
        return dao.updateWorkOrder(dto) > 0;
    }
}
