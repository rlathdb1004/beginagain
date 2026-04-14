package process.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

import process.dto.ProcessDTO;
import process.service.ProcessService;

@WebServlet("/process/list")
public class ProcessListController extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private ProcessService service = new ProcessService();

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		List<ProcessDTO> list = service.getList();

		request.setAttribute("list", list);
		request.setAttribute("pageTitle", "공정관리");
		request.setAttribute("pageSubTitle", "공정 등록 / 조회 / 수정 / 삭제");
		request.setAttribute("contentPage", "/WEB-INF/views/process/processList.jsp");
		request.getRequestDispatcher("/WEB-INF/views/table.jsp").forward(request, response);
	}
}