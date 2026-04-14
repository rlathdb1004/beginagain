package ProdMgmt.ProdPlanRegInq.Service;

import java.util.List;
import ProdMgmt.ProdPlanRegInq.DAO.ProdPlanRegInqDAO;
import ProdMgmt.ProdPlanRegInq.DTO.ProdPlanRegInqDTO;

public class ProdPlanRegInqService {
    private final ProdPlanRegInqDAO dao = new ProdPlanRegInqDAO();

    public List<ProdPlanRegInqDTO> getList() {
        return dao.selectAll();
    }

    public ProdPlanRegInqDTO getDetail(int planId) {
        return dao.selectOne(planId);
    }

    public List<ProdPlanRegInqDTO> getItemOptions() {
        return dao.selectItemOptions();
    }

    public int register(ProdPlanRegInqDTO dto) {
        return dao.insert(dto);
    }

    public int update(ProdPlanRegInqDTO dto) {
        return dao.update(dto);
    }

    public int delete(int[] ids) {
        return dao.delete(ids);
    }
}
