package workorder.controller;

import java.io.IOException;
import java.sql.Date;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import workorder.dto.WorkOrderDTO;
import workorder.service.WorkOrderService;

@WebServlet("/workorder/register")
public class WorkOrderRegisterController extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private WorkOrderService workOrderService = new WorkOrderService();

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		request.setCharacterEncoding("UTF-8");

		String itemIdStr = request.getParameter("itemId");
		String planIdStr = request.getParameter("planId");
		String empIdStr = request.getParameter("empId");
		String workDateStr = request.getParameter("workDate");
		String workQtyStr = request.getParameter("workQty");
		String status = request.getParameter("status");
		String remark = request.getParameter("remark");

		if (itemIdStr == null || planIdStr == null || empIdStr == null || workDateStr == null || workQtyStr == null
				|| "".equals(itemIdStr.trim()) || "".equals(planIdStr.trim()) || "".equals(empIdStr.trim())
				|| "".equals(workDateStr.trim()) || "".equals(workQtyStr.trim())) {
			response.sendRedirect(request.getContextPath() + "/workorder/list");
			return;
		}

		WorkOrderDTO dto = new WorkOrderDTO();
		dto.setItemId(Integer.parseInt(itemIdStr));
		dto.setPlanId(Integer.parseInt(planIdStr));
		dto.setEmpId(Integer.parseInt(empIdStr));
		dto.setWorkDate(Date.valueOf(workDateStr));
		dto.setWorkQty(Double.parseDouble(workQtyStr));
		dto.setStatus(status);
		dto.setRemark(remark);

		workOrderService.insertWorkOrder(dto);

		response.sendRedirect(request.getContextPath() + "/workorder/list");
	}
}