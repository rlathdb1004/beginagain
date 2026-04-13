package process.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

import process.dto.ProcessDTO;
import process.service.ProcessService;

@WebServlet("/process/update")
public class ProcessUpdateController extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private ProcessService service = new ProcessService();

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		request.setCharacterEncoding("UTF-8");

		String idStr = request.getParameter("processId");

		if (idStr == null || idStr.equals("")) {
			response.sendRedirect(request.getContextPath() + "/process/list");
			return;
		}

		ProcessDTO dto = new ProcessDTO();
		dto.setProcessId(Integer.parseInt(idStr));
		dto.setProcessName(request.getParameter("processName"));
		dto.setDescription(request.getParameter("description"));
		dto.setRemark(request.getParameter("remark"));

		boolean result = service.update(dto);

		if (result) {
			response.sendRedirect(request.getContextPath() + "/process/detail?processId=" + dto.getProcessId());
		} else {
			response.sendRedirect(request.getContextPath() + "/process/list");
		}
	}
}