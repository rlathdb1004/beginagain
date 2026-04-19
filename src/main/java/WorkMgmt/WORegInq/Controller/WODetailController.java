package WorkMgmt.WORegInq.Controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import WorkMgmt.WORegInq.DTO.WORegInqDTO;
import WorkMgmt.WORegInq.Service.WORegInqService;
import bom.service.BOMService;
import routing.service.RoutingService;

@WebServlet("/woreginq/detail")
public class WODetailController extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private WORegInqService service = new WORegInqService();
    private BOMService bomService = new BOMService();
    private RoutingService routingService = new RoutingService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        int workOrderId = parseInt(request.getParameter("seqNO"));
        if (workOrderId <= 0) {
            response.sendRedirect(request.getContextPath() + "/woreginq");
            return;
        }

        WORegInqDTO workOrder = service.getDetail(workOrderId);
        if (workOrder == null) {
            response.sendRedirect(request.getContextPath() + "/woreginq");
            return;
        }

        HttpSession session = request.getSession();
        request.setAttribute("errorMsg", session.getAttribute("errorMsg"));
        session.removeAttribute("errorMsg");

        request.setAttribute("workOrder", workOrder);
        request.setAttribute("bomList", bomService.getBomListByItemId(workOrder.getItemId()));
        request.setAttribute("routingList", routingService.getRoutingListByItemId(workOrder.getItemId()));
        request.setAttribute("empOptions", service.getEmpOptions());
        request.setAttribute("pageTitle", "작업관리");
        request.setAttribute("pageSubTitle", "작업지시 상세 / 수정");
        request.setAttribute("contentPage", "/WEB-INF/views/WODetail.jsp");
        request.getRequestDispatcher("/WEB-INF/views/table.jsp").forward(request, response);
    }

    private int parseInt(String value) {
        try { return Integer.parseInt(value); } catch (Exception e) { return 0; }
    }
}
