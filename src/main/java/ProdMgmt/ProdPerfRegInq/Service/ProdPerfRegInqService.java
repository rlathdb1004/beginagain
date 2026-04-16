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

    public ProdPerfRegInqDTO getDetail(int resultId) {
        return dao.getDetail(resultId);
    }

    public boolean insertProductionResult(ProdPerfRegInqDTO dto) {
        return dao.insertProductionResult(dto) > 0;
    }

    public boolean updateProductionResult(ProdPerfRegInqDTO dto) {
        return dao.updateProductionResult(dto) > 0;
    }

    public int deleteByIds(String[] seqNos) {
        return dao.deleteByIds(seqNos);
    }
}
