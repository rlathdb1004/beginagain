package board.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

import board.dto.NoticeBoardDTO;
import board.service.BoardService;

@WebServlet("/notice/update")
public class NoticeUpdateController extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private BoardService boardService = new BoardService();

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		request.setCharacterEncoding("UTF-8");

		String noticeIdStr = request.getParameter("noticeId");
		if (noticeIdStr == null || "".equals(noticeIdStr)) {
			response.sendRedirect(request.getContextPath() + "/notice/list");
			return;
		}

		NoticeBoardDTO dto = new NoticeBoardDTO();
		dto.setNoticeId(Integer.parseInt(noticeIdStr));
		dto.setTitle(request.getParameter("title"));
		dto.setContent(request.getParameter("content"));
		dto.setStatus(request.getParameter("status"));
		dto.setRemark(request.getParameter("remark"));

		boolean result = boardService.updateNotice(dto);

		if (result) {
			response.sendRedirect(request.getContextPath() + "/notice/detail?noticeId=" + dto.getNoticeId());
		} else {
			response.sendRedirect(request.getContextPath() + "/notice/list");
		}
	}
}