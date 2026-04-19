package maintenance.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import maintenance.service.MaintenanceService;

@WebServlet("/maintenance/delete")
public class MaintenanceDeleteController extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private MaintenanceService maintenanceService = new MaintenanceService();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        try {
            String[] maintenanceIds = request.getParameterValues("maintenanceId");
            if (maintenanceIds != null && maintenanceIds.length > 0) {
                maintenanceService.deleteMaintenance(maintenanceIds);
            }
        } catch (Exception e) {
            request.getSession().setAttribute("errorMsg", e.getMessage());
        }

        response.sendRedirect(request.getContextPath() + "/maintenance/list");
    }
}
