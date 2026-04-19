package maintenance.controller;

import java.io.IOException;
import java.sql.Date;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import maintenance.dto.MaintenanceDTO;
import maintenance.service.MaintenanceService;

@WebServlet("/maintenance/update")
public class MaintenanceUpdateController extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private MaintenanceService maintenanceService = new MaintenanceService();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        String maintenanceIdStr = request.getParameter("maintenanceId");

        try {
            String equipmentIdStr = request.getParameter("equipmentId");
            String maintenanceDateStr = request.getParameter("maintenanceDate");
            String maintenanceType = request.getParameter("maintenanceType");
            String maintenanceContent = request.getParameter("maintenanceContent");
            String nextMaintenanceDateStr = request.getParameter("nextMaintenanceDate");
            String status = request.getParameter("status");
            String remark = request.getParameter("remark");

            Date maintenanceDate = null;
            if (maintenanceDateStr != null && !"".equals(maintenanceDateStr.trim())) maintenanceDate = Date.valueOf(maintenanceDateStr);
            Date nextMaintenanceDate = null;
            if (nextMaintenanceDateStr != null && !"".equals(nextMaintenanceDateStr.trim())) nextMaintenanceDate = Date.valueOf(nextMaintenanceDateStr);

            MaintenanceDTO dto = new MaintenanceDTO();
            dto.setMaintenanceId(Integer.parseInt(maintenanceIdStr));
            dto.setEquipmentId(Integer.parseInt(equipmentIdStr));
            dto.setMaintenanceDate(maintenanceDate);
            dto.setMaintenanceType(maintenanceType);
            dto.setMaintenanceContent(maintenanceContent);
            dto.setNextMaintenanceDate(nextMaintenanceDate);
            dto.setStatus(status);
            dto.setRemark(remark);

            maintenanceService.updateMaintenance(dto);
        } catch (Exception e) {
            request.getSession().setAttribute("errorMsg", e.getMessage());
        }

        response.sendRedirect(request.getContextPath() + "/maintenance/detail?maintenanceId=" + maintenanceIdStr);
    }
}
