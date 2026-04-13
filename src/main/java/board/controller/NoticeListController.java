package board.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

import board.dto.NoticeBoardDTO;
import board.service.BoardService;

@WebServlet("/notice/list")
public class NoticeListController extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private BoardService boardService = new BoardService();

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		List<NoticeBoardDTO> noticeList = boardService.getNoticeList();

		request.setAttribute("noticeList", noticeList);
		request.getRequestDispatcher("/WEB-INF/views/board/noticeList.jsp").forward(request, response);
	}
}