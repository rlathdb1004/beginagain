package MasterDataMgmt.BOMManagement.controller;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import MasterDataMgmt.BOMManagement.dto.BOMMgmtDTO;
import MasterDataMgmt.BOMManagement.service.BOMMgmtService;
import routing.dto.RoutingDTO;


@WebServlet("/bom-update")
public class BOMMgmtUpdateController extends HttpServlet {	
    
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
	}

	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");

        BOMMgmtDTO dto = new BOMMgmtDTO();
        dto.setBom_detail_id(Integer.parseInt(request.getParameter("bomDetailId")));
        dto.setMaterial_code(request.getParameter("materialCode"));       
        dto.setQty_required(Double.parseDouble(request.getParameter("qtyRequired")));
        dto.setUnit(request.getParameter("unit"));        
        dto.setRemark(request.getParameter("remark"));

        BOMMgmtService.updateBom(dto);
       
        response.sendRedirect(request.getContextPath() + "/bom-detail?bomDetailId=" + dto.getBom_detail_id());
        
    
	}

}
