package ProdMgmt.ProdPlanRegInq.Controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import ProdMgmt.ProdPlanRegInq.DTO.ProdPlanRegInqDTO;
import ProdMgmt.ProdPlanRegInq.Service.ProdPlanRegInqService;

@WebServlet("/prodplan/detail")
public class ProdPlanDetailController extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private ProdPlanRegInqService service = new ProdPlanRegInqService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String planIdStr = request.getParameter("planId");

        if (planIdStr == null || "".equals(planIdStr)) {
            response.sendRedirect(request.getContextPath() + "/prodplan");
            return;
        }

        int planId = Integer.parseInt(planIdStr);
        ProdPlanRegInqDTO detailDTO = service.getDetail(planId);

        if (detailDTO == null) {
            response.sendRedirect(request.getContextPath() + "/prodplan");
            return;
        }

        request.setAttribute("detailDTO", detailDTO);
        request.setAttribute("pageTitle", "생산관리");
        request.setAttribute("pageSubTitle", "생산계획 상세 / 수정");
        request.setAttribute("contentPage", "/WEB-INF/views/prodplan/prodPlanDetail.jsp");
        request.getRequestDispatcher("/WEB-INF/views/table.jsp").forward(request, response);
    }
}
