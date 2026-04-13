package board.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

import board.service.BoardService;

@WebServlet("/suggestion/delete")
public class SuggestionDeleteController extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private BoardService boardService = new BoardService();

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String[] suggestionIds = request.getParameterValues("suggestionId");

		if (suggestionIds == null || suggestionIds.length == 0) {
			response.sendRedirect(request.getContextPath() + "/suggestion/list");
			return;
		}

		boardService.deleteSuggestion(suggestionIds);
		response.sendRedirect(request.getContextPath() + "/suggestion/list");
	}
}