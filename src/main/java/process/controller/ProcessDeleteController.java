package process.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

import process.service.ProcessService;

@WebServlet("/process/delete")
public class ProcessDeleteController extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private ProcessService service = new ProcessService();

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		request.setCharacterEncoding("UTF-8");

		String[] ids = request.getParameterValues("processId");

		if (ids == null || ids.length == 0) {
			response.sendRedirect(request.getContextPath() + "/process/list");
			return;
		}

		service.delete(ids);

		response.sendRedirect(request.getContextPath() + "/process/list");
	}
}