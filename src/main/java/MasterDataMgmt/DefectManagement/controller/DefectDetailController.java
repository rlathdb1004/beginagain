package MasterDataMgmt.DefectManagement.controller;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import MasterDataMgmt.BOMManagement.dao.BOMMgmtDAO;
import MasterDataMgmt.BOMManagement.dto.BOMMgmtDTO;
import MasterDataMgmt.DefectManagement.dao.DefectMgmtDAO;
import MasterDataMgmt.DefectManagement.dto.DefectMgmtDTO;


@WebServlet("/defect-detail")
public class DefectDetailController extends HttpServlet {
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		int defectCodeId = Integer.parseInt(request.getParameter("defectCodeId"));

	    DefectMgmtDAO dao = new DefectMgmtDAO();
	    DefectMgmtDTO dto = dao.selectOne(defectCodeId);

	    request.setAttribute("defect", dto);
	    request.setAttribute("contentPage", "/WEB-INF/views/item/DefectDetail.jsp");
        request.setAttribute("pageTitle", "불량 관리");
        request.setAttribute("pageSubTitle", "불량 상세 내용 및 수정");
        
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
