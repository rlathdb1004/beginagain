package workorder.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import workorder.dto.WorkOrderDTO;
import workorder.service.WorkOrderService;

@WebServlet("/workorder/list")
public class WorkOrderListController extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private WorkOrderService workOrderService = new WorkOrderService();

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		List<WorkOrderDTO> workOrderList = workOrderService.getWorkOrderList();

		request.setAttribute("workOrderList", workOrderList);
		request.setAttribute("pageTitle", "작업관리");
		request.setAttribute("pageSubTitle", "작업지시 등록 / 조회");
		request.setAttribute("contentPage", "/WEB-INF/views/workorder/workOrderList.jsp");
		request.getRequestDispatcher("/WEB-INF/views/table.jsp").forward(request, response);
	}
}