package board.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

import board.service.BoardService;

@WebServlet("/notice/delete")
public class NoticeDeleteController extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private BoardService boardService = new BoardService();

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String[] noticeIds = request.getParameterValues("noticeId");

		if (noticeIds == null || noticeIds.length == 0) {
			response.sendRedirect(request.getContextPath() + "/notice/list");
			return;
		}

		boardService.deleteNotice(noticeIds);
		response.sendRedirect(request.getContextPath() + "/notice/list");
	}
}