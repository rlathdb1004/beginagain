package WorkMgmt.WorkStatus.Controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import WorkMgmt.WorkStatus.DTO.WorkStatusDTO;
import WorkMgmt.WorkStatus.Service.WorkStatusService;

@WebServlet("/workstatus/detail")
public class WorkStatusDetailController extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private WorkStatusService workStatusService = new WorkStatusService();

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String workOrderIdStr = request.getParameter("workOrderId");

		if (workOrderIdStr == null || "".equals(workOrderIdStr)) {
			response.sendRedirect(request.getContextPath() + "/workstatus");
			return;
		}

		int workOrderId = Integer.parseInt(workOrderIdStr);
		WorkStatusDTO workStatus = workStatusService.getWorkStatusById(workOrderId);

		if (workStatus == null) {
			response.sendRedirect(request.getContextPath() + "/workstatus");
			return;
		}

		request.setAttribute("workStatus", workStatus);
		request.setAttribute("pageTitle", "작업관리");
		request.setAttribute("pageSubTitle", "작업 현황 상세");
		request.setAttribute("contentPage", "/WEB-INF/views/workStatusDetail.jsp");
		request.getRequestDispatcher("/WEB-INF/views/table.jsp").forward(request, response);
	}
}