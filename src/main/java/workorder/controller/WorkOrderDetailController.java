package workorder.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import bom.dto.BOMDTO;
import bom.service.BOMService;
import routing.dto.RoutingDTO;
import routing.service.RoutingService;
import workorder.dto.WorkOrderDTO;
import workorder.service.WorkOrderService;

@WebServlet("/workorder/detail")
public class WorkOrderDetailController extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private WorkOrderService workOrderService = new WorkOrderService();
	private BOMService bomService = new BOMService();
	private RoutingService routingService = new RoutingService();

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String workOrderIdStr = request.getParameter("workOrderId");

		if (workOrderIdStr == null || "".equals(workOrderIdStr)) {
			response.sendRedirect(request.getContextPath() + "/workorder/list");
			return;
		}

		int workOrderId = Integer.parseInt(workOrderIdStr);
		WorkOrderDTO workOrder = workOrderService.getWorkOrderById(workOrderId);

		if (workOrder == null) {
			response.sendRedirect(request.getContextPath() + "/workorder/list");
			return;
		}

		List<BOMDTO> bomList = bomService.getBomListByItemId(workOrder.getItemId());
		List<RoutingDTO> routingList = routingService.getRoutingListByItemId(workOrder.getItemId());

		request.setAttribute("workOrder", workOrder);
		request.setAttribute("bomList", bomList);
		request.setAttribute("routingList", routingList);

		request.getRequestDispatcher("/WEB-INF/views/workorder/workOrderDetail.jsp").forward(request, response);
	}
}