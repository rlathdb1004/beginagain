package board.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

import board.dto.NoticeBoardDTO;
import board.service.BoardService;

@WebServlet("/notice/detail")
public class NoticeDetailController extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private BoardService boardService = new BoardService();

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String noticeIdStr = request.getParameter("noticeId");

		if (noticeIdStr == null || "".equals(noticeIdStr)) {
			response.sendRedirect(request.getContextPath() + "/notice/list");
			return;
		}

		int noticeId = Integer.parseInt(noticeIdStr);
		NoticeBoardDTO notice = boardService.getNoticeById(noticeId);

		if (notice == null) {
			response.sendRedirect(request.getContextPath() + "/notice/list");
			return;
		}

		request.setAttribute("notice", notice);
		request.getRequestDispatcher("/WEB-INF/views/board/noticeDetail.jsp").forward(request, response);
	}
}