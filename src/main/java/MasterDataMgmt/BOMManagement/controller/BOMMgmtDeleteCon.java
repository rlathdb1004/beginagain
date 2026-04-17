package MasterDataMgmt.BOMManagement.controller;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import MasterDataMgmt.BOMManagement.service.BOMMgmtService;


@WebServlet("/BOM-del")
public class BOMMgmtDeleteCon extends HttpServlet {
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
	}

	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("utf-8");
	    response.setContentType("text/html; charset=utf-8");

	    String[] ids = request.getParameterValues("bom_detail_id");

	    BOMMgmtService service = new BOMMgmtService();

	    int result = 0;

	    if (ids != null) {
	        for (String id : ids) {
	            result += service.delete(Integer.parseInt(id));
	        }
	    }

	    response.sendRedirect(request.getContextPath() + "/BOM-mgmt");
	}

}
