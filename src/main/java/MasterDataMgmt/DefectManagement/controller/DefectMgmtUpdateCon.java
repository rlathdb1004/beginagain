package MasterDataMgmt.DefectManagement.controller;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import MasterDataMgmt.BOMManagement.dto.BOMMgmtDTO;
import MasterDataMgmt.BOMManagement.service.BOMMgmtService;
import MasterDataMgmt.DefectManagement.dto.DefectMgmtDTO;
import MasterDataMgmt.DefectManagement.service.DefectMgmtService;

@WebServlet("/defect-update")
public class DefectMgmtUpdateCon extends HttpServlet {
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
	}

	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");

        DefectMgmtDTO dto = new DefectMgmtDTO();
        dto.setDefect_code_id(Integer.parseInt(request.getParameter("defectCodeId")));
        dto.setDefect_code(request.getParameter("defectCode"));       
        dto.setDefect_name(request.getParameter("defectName"));
        dto.setDefect_type(request.getParameter("defectType"));
        dto.setDescription(request.getParameter("description"));        
        dto.setRemark(request.getParameter("remark"));

        DefectMgmtService.update(dto);
       
        response.sendRedirect(request.getContextPath() + "/defect-detail?defectCodeId=" + dto.getDefect_code_id());
	}

}
