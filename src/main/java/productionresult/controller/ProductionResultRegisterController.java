
package productionresult.controller;

import java.io.IOException;
import java.sql.Date;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import productionresult.dto.ProductionResultDTO;
import productionresult.service.ProductionResultService;

@WebServlet("/productionresult/register")
public class ProductionResultRegisterController extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private ProductionResultService productionResultService = new ProductionResultService();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        Date resultDate = null;
        String resultDateStr = request.getParameter("resultDate");
        if (resultDateStr != null && !"".equals(resultDateStr.trim())) {
            resultDate = Date.valueOf(resultDateStr);
        }
        double producedQty = 0;
        String producedQtyStr = request.getParameter("producedQty");
        if (producedQtyStr != null && !"".equals(producedQtyStr.trim())) {
            producedQty = Double.parseDouble(producedQtyStr);
        }
        double lossQty = 0;
        String lossQtyStr = request.getParameter("lossQty");
        if (lossQtyStr != null && !"".equals(lossQtyStr.trim())) {
            lossQty = Double.parseDouble(lossQtyStr);
        }
        ProductionResultDTO dto = new ProductionResultDTO();
        dto.setWorkOrderId(Integer.parseInt(request.getParameter("workOrderId")));
        dto.setResultDate(resultDate);
        dto.setLotNo(request.getParameter("lotNo"));
        dto.setProducedQty(producedQty);
        dto.setLossQty(lossQty);
        dto.setStatus(request.getParameter("status"));
        dto.setRemark(request.getParameter("remark"));
        productionResultService.insertProductionResult(dto);
        response.sendRedirect(request.getContextPath() + "/productionresult/list?workOrderId=" + dto.getWorkOrderId());
    }
}
