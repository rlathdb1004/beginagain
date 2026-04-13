package workorder.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

import workorder.service.WorkOrderService;

@WebServlet("/workorder/delete")
public class WorkOrderDeleteController extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private WorkOrderService workOrderService = new WorkOrderService();

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		request.setCharacterEncoding("UTF-8");

		String[] workOrderIds = request.getParameterValues("workOrderId");

		if (workOrderIds == null || workOrderIds.length == 0) {
			response.sendRedirect(request.getContextPath() + "/workorder/list");
			return;
		}

		workOrderService.deleteWorkOrders(workOrderIds);
		response.sendRedirect(request.getContextPath() + "/workorder/list");
	}
}