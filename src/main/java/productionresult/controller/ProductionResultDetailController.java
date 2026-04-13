package productionresult.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

import productionresult.dto.ProductionResultDTO;
import productionresult.service.ProductionResultService;

@WebServlet("/productionresult/detail")
public class ProductionResultDetailController extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private ProductionResultService productionResultService = new ProductionResultService();

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String resultIdStr = request.getParameter("resultId");

		if (resultIdStr == null || "".equals(resultIdStr)) {
			response.sendRedirect(request.getContextPath() + "/workorder/list");
			return;
		}

		int resultId = Integer.parseInt(resultIdStr);
		ProductionResultDTO productionResult = productionResultService.getProductionResultById(resultId);

		if (productionResult == null) {
			response.sendRedirect(request.getContextPath() + "/workorder/list");
			return;
		}

		request.setAttribute("productionResult", productionResult);
		request.getRequestDispatcher("/WEB-INF/views/productionresult/productionResultDetail.jsp").forward(request,
				response);
	}
}