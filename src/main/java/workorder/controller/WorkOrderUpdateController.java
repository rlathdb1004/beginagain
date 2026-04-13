package workorder.controller;

import java.io.IOException;
import java.sql.Date;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

import workorder.dto.WorkOrderDTO;
import workorder.service.WorkOrderService;

@WebServlet("/workorder/update")
public class WorkOrderUpdateController extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private WorkOrderService workOrderService = new WorkOrderService();

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		request.setCharacterEncoding("UTF-8");

		String workOrderIdStr = request.getParameter("workOrderId");
		String workDateStr = request.getParameter("workDate");
		String workQtyStr = request.getParameter("workQty");
		String status = request.getParameter("status");
		String remark = request.getParameter("remark");

		if (workOrderIdStr == null || "".equals(workOrderIdStr)) {
			response.sendRedirect(request.getContextPath() + "/workorder/list");
			return;
		}

		Date workDate = null;
		if (workDateStr != null && !"".equals(workDateStr.trim())) {
			workDate = Date.valueOf(workDateStr);
		}

		double workQty = 0;
		if (workQtyStr != null && !"".equals(workQtyStr.trim())) {
			workQty = Double.parseDouble(workQtyStr);
		}

		WorkOrderDTO dto = new WorkOrderDTO();
		dto.setWorkOrderId(Integer.parseInt(workOrderIdStr));
		dto.setWorkDate(workDate);
		dto.setWorkQty(workQty);
		dto.setStatus(status);
		dto.setRemark(remark);

		boolean result = workOrderService.updateWorkOrder(dto);

		if (result) {
			response.sendRedirect(request.getContextPath() + "/workorder/detail?workOrderId=" + dto.getWorkOrderId());
		} else {
			response.sendRedirect(request.getContextPath() + "/workorder/list");
		}
	}
}