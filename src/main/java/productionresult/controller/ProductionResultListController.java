package productionresult.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

import productionresult.dto.ProductionResultDTO;
import productionresult.service.ProductionResultService;

@WebServlet("/productionresult/list")
public class ProductionResultListController extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private ProductionResultService productionResultService = new ProductionResultService();

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String workOrderIdStr = request.getParameter("workOrderId");

		if (workOrderIdStr == null || "".equals(workOrderIdStr)) {
			response.sendRedirect(request.getContextPath() + "/workorder/list");
			return;
		}

		int workOrderId = Integer.parseInt(workOrderIdStr);
		List<ProductionResultDTO> productionResultList = productionResultService.getProductionResultList(workOrderId);

		request.setAttribute("workOrderId", workOrderId);
		request.setAttribute("productionResultList", productionResultList);
		request.getRequestDispatcher("/WEB-INF/views/productionresult/productionResultList.jsp").forward(request,
				response);
	}
}