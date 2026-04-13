package board.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

import board.dto.AnswerDTO;
import board.dto.SuggestionBoardDTO;
import board.service.BoardService;

@WebServlet("/suggestion/detail")
public class SuggestionDetailController extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private BoardService boardService = new BoardService();

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String suggestionIdStr = request.getParameter("suggestionId");

		if (suggestionIdStr == null || "".equals(suggestionIdStr)) {
			response.sendRedirect(request.getContextPath() + "/suggestion/list");
			return;
		}

		int suggestionId = Integer.parseInt(suggestionIdStr);

		SuggestionBoardDTO suggestion = boardService.getSuggestionById(suggestionId);
		List<AnswerDTO> answerList = boardService.getAnswerListBySuggestionId(suggestionId);

		if (suggestion == null) {
			response.sendRedirect(request.getContextPath() + "/suggestion/list");
			return;
		}

		request.setAttribute("suggestion", suggestion);
		request.setAttribute("answerList", answerList);
		request.getRequestDispatcher("/WEB-INF/views/board/suggestionDetail.jsp").forward(request, response);
	}
}