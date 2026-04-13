package dashboard.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dashboard.dto.DashboardDTO;
import dashboard.service.DashboardService;

@WebServlet("/main")
public class MainController extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private DashboardService dashboardService = new DashboardService();

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		DashboardDTO dashboard = dashboardService.getDashboardData();

		request.setAttribute("dashboard", dashboard);
		request.getRequestDispatcher("/WEB-INF/views/common/main.jsp").forward(request, response);
	}
}