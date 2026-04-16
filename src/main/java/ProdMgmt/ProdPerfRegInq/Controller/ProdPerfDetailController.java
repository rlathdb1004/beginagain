package ProdMgmt.ProdPerfRegInq.Controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import ProdMgmt.ProdPerfRegInq.DTO.ProdPerfRegInqDTO;
import ProdMgmt.ProdPerfRegInq.Service.ProdPerfRegInqService;

@WebServlet("/prodperf/detail")
public class ProdPerfDetailController extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private ProdPerfRegInqService service = new ProdPerfRegInqService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        int resultId = parseInt(request.getParameter("seqNO"));
        if (resultId <= 0) {
            response.sendRedirect(request.getContextPath() + "/prodperf");
            return;
        }

        ProdPerfRegInqDTO productionResult = service.getDetail(resultId);
        if (productionResult == null) {
            response.sendRedirect(request.getContextPath() + "/prodperf");
            return;
        }

        request.setAttribute("productionResult", productionResult);
        request.setAttribute("workOrderOptions", service.getWorkOrderOptions());
        request.setAttribute("pageTitle", "생산관리");
        request.setAttribute("pageSubTitle", "생산실적 상세 / 수정");
        request.setAttribute("contentPage", "/WEB-INF/views/ProdPerfDetail.jsp");
        request.getRequestDispatcher("/WEB-INF/views/table.jsp").forward(request, response);
    }

    private int parseInt(String value) { try { return Integer.parseInt(value); } catch (Exception e) { return 0; } }
}
