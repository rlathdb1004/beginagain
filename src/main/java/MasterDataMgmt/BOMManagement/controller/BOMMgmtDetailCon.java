package MasterDataMgmt.BOMManagement.controller;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import MasterDataMgmt.BOMManagement.dao.BOMMgmtDAO;
import MasterDataMgmt.BOMManagement.dto.BOMMgmtDTO;


@WebServlet("/bom-detail")
public class BOMMgmtDetailCon extends HttpServlet {
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) {

	    int bomDetailId = Integer.parseInt(request.getParameter("bomDetailId"));

	    BOMMgmtDAO dao = new BOMMgmtDAO();
	    BOMMgmtDTO dto = dao.selectOne(bomDetailId);

	    request.setAttribute("bom", dto);
	    request.setAttribute("contentPage", "/WEB-INF/views/item/BOMDetail.jsp");
        request.setAttribute("pageTitle", "BOM 관리");
        request.setAttribute("pageSubTitle", "BOM 상세 내용 및 수정");
        
	    try {
	    	request.getRequestDispatcher("/WEB-INF/views/table.jsp")
            .forward(request, response);
		} catch (ServletException e) {			
			e.printStackTrace();
		} catch (IOException e) {			
			e.printStackTrace();
		}
	}

	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

	}

}
