package MasterDataMgmt.DefectManagement.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import MasterDataMgmt.DefectManagement.dto.DefectMgmtDTO;
import MasterDataMgmt.DefectManagement.dto.DefectMgmtSearchDTO;
import MasterDataMgmt.DefectManagement.service.DefectMgmtService;


@WebServlet("/defect-mgmt")
public class DefectMgmtCon extends HttpServlet {
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("utf-8");
        response.setContentType("text/html; charset=utf-8");

        String keyword = request.getParameter("keyword");
       
        DefectMgmtSearchDTO dto = new DefectMgmtSearchDTO();
        dto.setKeyword(keyword);
        DefectMgmtService service = new DefectMgmtService();
        List<DefectMgmtDTO> list = service.getList(dto);

        
        request.setAttribute("defectList", list);        
        request.setAttribute("contentPage", "/WEB-INF/views/item/DefectMgmt.jsp");
        request.setAttribute("pageTitle", "불량 관리");
        request.setAttribute("pageSubTitle", "불량 코드 조회 및 등록");
        request.getRequestDispatcher("/WEB-INF/views/table.jsp")
               .forward(request, response);
	}
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
	        throws ServletException, IOException {

	    request.setCharacterEncoding("utf-8");
	    response.setContentType("text/html; charset=utf-8");

	    String id = request.getParameter("defect_code_id");
	    String defect_code = request.getParameter("defect_code");
	    String defect_name = request.getParameter("defect_name");
	    String defect_type = request.getParameter("defect_type");
	    String description = request.getParameter("description");
	    String use_yn = request.getParameter("use_yn");
	    String remark = request.getParameter("remark");

	    
	    DefectMgmtDTO dto = new DefectMgmtDTO();
	    dto.setDefect_code(defect_code);
	    dto.setDefect_name(defect_name);
	    dto.setDefect_type(defect_type);
	    dto.setDescription(description);
	    dto.setUse_yn(use_yn);
	    dto.setRemark(remark);
	   
	    DefectMgmtService service = new DefectMgmtService();
	    if (id != null && !id.isEmpty()) {
	        dto.setDefect_code_id(Integer.parseInt(id)); 
	        service.update(dto);
	    } else {
	        service.insert(dto);
	    }

	    response.sendRedirect(request.getContextPath() + "/defect-mgmt");
	}

}
