package board.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

import board.dto.SuggestionBoardDTO;
import board.service.BoardService;

@WebServlet("/suggestion/list")
public class SuggestionListController extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private BoardService boardService = new BoardService();

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		List<SuggestionBoardDTO> suggestionList = boardService.getSuggestionList();

		request.setAttribute("suggestionList", suggestionList);
		request.getRequestDispatcher("/WEB-INF/views/board/suggestionList.jsp").forward(request, response);
	}
}