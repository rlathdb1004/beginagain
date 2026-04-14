package process.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

import process.dto.ProcessDTO;
import process.service.ProcessService;

@WebServlet("/process/detail")
public class ProcessDetailController extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private ProcessService service = new ProcessService();

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String idStr = request.getParameter("processId");

		if (idStr == null || idStr.equals("")) {
			response.sendRedirect(request.getContextPath() + "/process/list");
			return;
		}

		int id = Integer.parseInt(idStr);

		ProcessDTO p = service.getById(id);

		if (p == null) {
			response.sendRedirect(request.getContextPath() + "/process/list");
			return;
		}

		request.setAttribute("p", p);
		request.setAttribute("pageTitle", "공정관리");
		request.setAttribute("pageSubTitle", "공정 상세 / 수정");
		request.setAttribute("contentPage", "/WEB-INF/views/process/processDetail.jsp");
		request.getRequestDispatcher("/WEB-INF/views/table.jsp").forward(request, response);
	}
}