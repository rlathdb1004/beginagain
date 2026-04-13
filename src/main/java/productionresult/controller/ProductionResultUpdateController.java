package productionresult.controller;

import java.io.IOException;
import java.sql.Date;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

import productionresult.dto.ProductionResultDTO;
import productionresult.service.ProductionResultService;

@WebServlet("/productionresult/update")
public class ProductionResultUpdateController extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private ProductionResultService productionResultService = new ProductionResultService();

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		request.setCharacterEncoding("UTF-8");

		String resultIdStr = request.getParameter("resultId");
		String resultDateStr = request.getParameter("resultDate");
		String lotNo = request.getParameter("lotNo");
		String producedQtyStr = request.getParameter("producedQty");
		String lossQtyStr = request.getParameter("lossQty");
		String status = request.getParameter("status");
		String remark = request.getParameter("remark");

		if (resultIdStr == null || "".equals(resultIdStr)) {
			response.sendRedirect(request.getContextPath() + "/workorder/list");
			return;
		}

		Date resultDate = null;
		if (resultDateStr != null && !"".equals(resultDateStr.trim())) {
			resultDate = Date.valueOf(resultDateStr);
		}

		double producedQty = 0;
		if (producedQtyStr != null && !"".equals(producedQtyStr.trim())) {
			producedQty = Double.parseDouble(producedQtyStr);
		}

		double lossQty = 0;
		if (lossQtyStr != null && !"".equals(lossQtyStr.trim())) {
			lossQty = Double.parseDouble(lossQtyStr);
		}

		ProductionResultDTO dto = new ProductionResultDTO();
		dto.setResultId(Integer.parseInt(resultIdStr));
		dto.setResultDate(resultDate);
		dto.setLotNo(lotNo);
		dto.setProducedQty(producedQty);
		dto.setLossQty(lossQty);
		dto.setStatus(status);
		dto.setRemark(remark);

		boolean result = productionResultService.updateProductionResult(dto);

		if (result) {
			response.sendRedirect(request.getContextPath() + "/productionresult/detail?resultId=" + dto.getResultId());
		} else {
			response.sendRedirect(request.getContextPath() + "/workorder/list");
		}
	}
}