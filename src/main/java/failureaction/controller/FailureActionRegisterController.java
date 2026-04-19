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
import maintenance.dto.MaintenanceDTO;
import maintenance.service.MaintenanceService;

@WebServlet("/failureaction/register")
public class FailureActionRegisterController extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private FailureActionService failureActionService = new FailureActionService();
    private MaintenanceService maintenanceService = new MaintenanceService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String maintenanceIdStr = request.getParameter("maintenanceId");
        if (maintenanceIdStr == null || "".equals(maintenanceIdStr.trim())) {
            response.sendRedirect(request.getContextPath() + "/maintenance/list");
            return;
        }
        int maintenanceId = Integer.parseInt(maintenanceIdStr);
        MaintenanceDTO maintenance = maintenanceService.getMaintenanceById(maintenanceId);
        if (maintenance == null) {
            response.sendRedirect(request.getContextPath() + "/maintenance/list");
            return;
        }
        Object errorMsg = request.getSession().getAttribute("errorMsg");
        if (errorMsg != null) {
            request.setAttribute("errorMsg", errorMsg);
            request.getSession().removeAttribute("errorMsg");
        }
        request.setAttribute("maintenanceId", maintenanceId);
        request.setAttribute("maintenance", maintenance);
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
            return;
        } catch (Exception e) {
            request.getSession().setAttribute("errorMsg", e.getMessage());
            response.sendRedirect(request.getContextPath() + "/failureaction/register?maintenanceId=" + maintenanceId);
        }
    }
}
