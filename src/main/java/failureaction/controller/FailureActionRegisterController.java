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

@WebServlet("/failureaction/register")
public class FailureActionRegisterController extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private FailureActionService failureActionService = new FailureActionService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String maintenanceId = request.getParameter("maintenanceId");

        if (maintenanceId == null || "".equals(maintenanceId.trim())) {
            response.sendRedirect(request.getContextPath() + "/maintenance/list");
            return;
        }

        request.setAttribute("maintenanceId", maintenanceId);
        request.setAttribute("pageTitle", "설비운영");
        request.setAttribute("pageSubTitle", "고장조치 등록");
        request.setAttribute("contentPage", "/WEB-INF/views/failureaction/failureActionRegister.jsp");
        request.getRequestDispatcher("/WEB-INF/views/table.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        int maintenanceId = Integer.parseInt(request.getParameter("maintenanceId"));
        String failureDateStr = request.getParameter("failureDate");
        String failurePart = request.getParameter("failurePart");
        String failureContent = request.getParameter("failureContent");
        String causeText = request.getParameter("causeText");
        String actionText = request.getParameter("actionText");
        String actionDateStr = request.getParameter("actionDate");
        String status = request.getParameter("status");

        Date failureDate = null;
        if (failureDateStr != null && !"".equals(failureDateStr.trim())) {
            failureDate = Date.valueOf(failureDateStr);
        }

        Date actionDate = null;
        if (actionDateStr != null && !"".equals(actionDateStr.trim())) {
            actionDate = Date.valueOf(actionDateStr);
        }

        FailureActionDTO dto = new FailureActionDTO();
        dto.setMaintenanceId(maintenanceId);
        dto.setFailureDate(failureDate);
        dto.setFailurePart(failurePart);
        dto.setFailureContent(failureContent);
        dto.setCauseText(causeText);
        dto.setActionText(actionText);
        dto.setActionDate(actionDate);
        dto.setStatus(status);

        failureActionService.insertFailureAction(dto);

        response.sendRedirect(request.getContextPath() + "/maintenance/detail?maintenanceId=" + maintenanceId);
    }
}