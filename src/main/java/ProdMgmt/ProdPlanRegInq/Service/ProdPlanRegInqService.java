package ProdMgmt.ProdPlanRegInq.Service;

import java.util.List;

import ProdMgmt.ProdPlanRegInq.DAO.ProdPlanRegInqDAO;

public class ProdPlanRegInqService {
	
	public List getList() {
		ProdPlanRegInqDAO dao = new ProdPlanRegInqDAO();
		
		List list = dao.selectAll();
		
		return list;
	}

}
