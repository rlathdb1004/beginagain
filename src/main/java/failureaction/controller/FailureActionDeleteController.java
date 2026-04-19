package failureaction.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import failureaction.service.FailureActionService;

@WebServlet("/failureaction/delete")
public class FailureActionDeleteController extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private FailureActionService failureActionService = new FailureActionService();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        String maintenanceId = request.getParameter("maintenanceId");
        try {
            String[] failureActionIds = request.getParameterValues("failureActionId");
            if (failureActionIds != null && failureActionIds.length > 0) {
                failureActionService.deleteFailureAction(failureActionIds);
            }
        } catch (Exception e) {
            request.getSession().setAttribute("errorMsg", e.getMessage());
        }
        response.sendRedirect(request.getContextPath() + "/maintenance/detail?maintenanceId=" + maintenanceId);
    }
}
