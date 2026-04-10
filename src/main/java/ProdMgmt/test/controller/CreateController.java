package mes.controller;

import java.io.IOException;
import java.sql.Date;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import mes.dto.CreateDTO;
import mes.service.CreateService;

@WebServlet("/create")
public class CreateController extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("utf-8");
		response.setContentType("text/html;charset=utf-8");
		
		System.out.println("CreateController doGet 실행");
		
		request.getRequestDispatcher("/createItems.jsp").forward(request, response);
	
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("utf-8");
		response.setContentType("text/html;charset=utf-8");
		
		System.out.println("CreateController doPost 실행");
		
		CreateDTO dto = new CreateDTO();
	
		
		dto.setSubject(request.getParameter("subject"));
		dto.setSuplier(request.getParameter("suplier"));
		dto.setItemsCode(request.getParameter("itemsCode"));
		dto.setItemsName(request.getParameter("itemsName"));
		dto.setItemsUnit(request.getParameter("itemsUnit"));
		dto.setCreateDate(Date.valueOf(request.getParameter("createDate")));
		dto.setItemsMemo(request.getParameter("itemsMemo"));
		
		System.out.println("doPost subject : "+ request.getParameter("subject"));
		
		CreateService cs = new CreateService();
		
		cs.addItem(dto);
		
//		response.sendRedirect("create");
		
		
		
	}

}
