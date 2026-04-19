package failureaction.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import failureaction.dto.FailureActionDTO;
import failureaction.service.FailureActionService;

@WebServlet("/failureaction/detail")
public class FailureActionDetailController extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private FailureActionService failureActionService = new FailureActionService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String failureActionIdStr = request.getParameter("failureActionId");

        if (failureActionIdStr == null || "".equals(failureActionIdStr.trim())) {
            response.sendRedirect(request.getContextPath() + "/maintenance/list");
            return;
        }

        int failureActionId = Integer.parseInt(failureActionIdStr);
        FailureActionDTO failureAction = failureActionService.getFailureActionById(failureActionId);

        if (failureAction == null) {
            response.sendRedirect(request.getContextPath() + "/maintenance/list");
            return;
        }

        HttpSession session = request.getSession(false);
        if (session != null) {
            Object errorMsg = session.getAttribute("errorMsg");
            if (errorMsg != null) {
                request.setAttribute("errorMsg", errorMsg);
                session.removeAttribute("errorMsg");
            }
        }

        request.setAttribute("failureAction", failureAction);
        request.setAttribute("pageTitle", "설비운영");
        request.setAttribute("pageSubTitle", "고장조치 상세 / 수정");
        request.setAttribute("contentPage", "/WEB-INF/views/failureaction/failureActionDetail.jsp");
        request.getRequestDispatcher("/WEB-INF/views/table.jsp").forward(request, response);
    }
}
