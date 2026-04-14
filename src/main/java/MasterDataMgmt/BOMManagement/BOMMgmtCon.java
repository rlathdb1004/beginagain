package MasterDataMgmt.BOMManagement;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import MasterDataMgmt.ItemMgmt.ItemMgmtDTO;
import MasterDataMgmt.ItemMgmt.ItemMgmtSearchDTO;
import MasterDataMgmt.ItemMgmt.ItemMgmtService;


@WebServlet("/BOM-mgmt")
public class BOMMgmtCon extends HttpServlet {
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("utf-8");
		response.setContentType("text/html; charset=utf-8");
		
		BOMMgmtSearchDTO searchDTO = new BOMMgmtSearchDTO();

	    searchDTO.setStartDate(request.getParameter("startDate"));
	    searchDTO.setEndDate(request.getParameter("endDate"));
	    searchDTO.setField(request.getParameter("searchField"));
	    searchDTO.setKeyword(request.getParameter("keyword"));

	    BOMMgmtService service = new BOMMgmtService();
	    List<BOMMgmtDTO> list = service.getBOMList(searchDTO);

	    request.setAttribute("BOMList", list);
	    request.getRequestDispatcher("/WEB-INF/views/item/BOMMgmt.jsp").forward(request, response);
	}

	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("utf-8");
		response.setContentType("text/html; charset=utf-8");
		
//		BOMMgmtService service = new BOMMgmtService();
//
//	    // 삭제
//	    String[] deleteIds = request.getParameterValues("deleteIds");
//	    if (deleteIds != null) {
//	        service.removeBOM(deleteIds);
//	    } else {
//	        // 등록
//	        BOMMgmtDTO dto = new BOMMgmtDTO();
//	        dto.setItem_code(request.getParameter("item_code"));
//	        dto.setRemark(request.getParameter("remark"));
//	        dto.setUse_yn(request.getParameter("use_yn"));
//
//	        service.addBOM(dto);
//	    }
//
//	    response.sendRedirect(request.getContextPath() + "/BOM-mgmt");
	}

}
