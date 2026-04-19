package failureaction.controller;

import java.io.IOException;
import java.sql.Date;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import failureaction.dto.FailureActionDTO;
import failureaction.service.FailureActionService;

@WebServlet("/failureaction/update")
public class FailureActionUpdateController extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private FailureActionService failureActionService = new FailureActionService();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        int failureActionId = Integer.parseInt(request.getParameter("failureActionId"));
        int maintenanceId = Integer.parseInt(request.getParameter("maintenanceId"));

        try {
            String failureDateStr = request.getParameter("failureDate");
            String failurePart = request.getParameter("failurePart");
            String failureContent = request.getParameter("failureContent");
            String causeText = request.getParameter("causeText");
            String actionText = request.getParameter("actionText");
            String actionDateStr = request.getParameter("actionDate");
            String status = request.getParameter("status");

            Date failureDate = (failureDateStr != null && !"".equals(failureDateStr.trim())) ? Date.valueOf(failureDateStr) : null;
            Date actionDate = (actionDateStr != null && !"".equals(actionDateStr.trim())) ? Date.valueOf(actionDateStr) : null;

            FailureActionDTO dto = new FailureActionDTO();
            dto.setFailureActionId(failureActionId);
            dto.setMaintenanceId(maintenanceId);
            dto.setFailureDate(failureDate);
            dto.setFailurePart(failurePart);
            dto.setFailureContent(failureContent);
            dto.setCauseText(causeText);
            dto.setActionText(actionText);
            dto.setActionDate(actionDate);
            dto.setStatus(status);

            failureActionService.updateFailureAction(dto);
        } catch (Exception e) {
            request.getSession().setAttribute("errorMsg", e.getMessage());
        }

        response.sendRedirect(request.getContextPath() + "/failureaction/detail?failureActionId=" + failureActionId);
    }
}
