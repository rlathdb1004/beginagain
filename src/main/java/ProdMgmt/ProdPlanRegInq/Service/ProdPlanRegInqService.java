package ProdMgmt.ProdPlanRegInq.Service;

import java.util.List;

import ProdMgmt.ProdPlanRegInq.DAO.ProdPlanRegInqDAO;
import ProdMgmt.ProdPlanRegInq.DTO.ProdPlanRegInqDTO;

public class ProdPlanRegInqService {
    private ProdPlanRegInqDAO dao = new ProdPlanRegInqDAO();

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
        return dao.insert(dto);
    }

    public int deleteByIds(String[] seqNos) {
        return dao.deleteByIds(seqNos);
    }
    public int update(ProdPlanRegInqDTO dto) { return dao.update(dto); }
}
