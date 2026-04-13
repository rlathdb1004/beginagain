package productionresult.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

import productionresult.service.ProductionResultService;

@WebServlet("/productionresult/delete")
public class ProductionResultDeleteController extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private ProductionResultService productionResultService = new ProductionResultService();

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		request.setCharacterEncoding("UTF-8");

		String[] resultIds = request.getParameterValues("resultId");
		String workOrderIdStr = request.getParameter("workOrderId");

		if (workOrderIdStr == null || "".equals(workOrderIdStr)) {
			response.sendRedirect(request.getContextPath() + "/workorder/list");
			return;
		}

		if (resultIds == null || resultIds.length == 0) {
			response.sendRedirect(request.getContextPath() + "/productionresult/list?workOrderId=" + workOrderIdStr);
			return;
		}

		productionResultService.deleteProductionResults(resultIds);
		response.sendRedirect(request.getContextPath() + "/productionresult/list?workOrderId=" + workOrderIdStr);
	}
}