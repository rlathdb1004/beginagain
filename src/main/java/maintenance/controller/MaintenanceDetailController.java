package maintenance.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import failureaction.dto.FailureActionDTO;
import failureaction.service.FailureActionService;
import maintenance.dto.MaintenanceDTO;
import maintenance.service.MaintenanceService;

@WebServlet("/maintenance/detail")
public class MaintenanceDetailController extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private MaintenanceService maintenanceService = new MaintenanceService();
	private FailureActionService failureActionService = new FailureActionService();

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String maintenanceIdStr = request.getParameter("maintenanceId");

		if (maintenanceIdStr == null || "".equals(maintenanceIdStr)) {
			response.sendRedirect(request.getContextPath() + "/maintenance/list");
			return;
		}

		int maintenanceId = Integer.parseInt(maintenanceIdStr);
		MaintenanceDTO maintenance = maintenanceService.getMaintenanceById(maintenanceId);

		if (maintenance == null) {
			response.sendRedirect(request.getContextPath() + "/maintenance/list");
			return;
		}

		List<FailureActionDTO> failureActionList = failureActionService
				.getFailureActionListByMaintenanceId(maintenanceId);

		request.setAttribute("maintenance", maintenance);
		request.setAttribute("failureActionList", failureActionList);
		request.setAttribute("pageTitle", "설비운영");
		request.setAttribute("pageSubTitle", "정비이력 상세 / 수정");
		request.setAttribute("contentPage", "/WEB-INF/views/maintenance/maintenanceDetail.jsp");
		request.getRequestDispatcher("/WEB-INF/views/table.jsp").forward(request, response);
	}
}