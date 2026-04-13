package board.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

import board.dto.SuggestionBoardDTO;
import board.service.BoardService;

@WebServlet("/suggestion/update")
public class SuggestionUpdateController extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private BoardService boardService = new BoardService();

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		request.setCharacterEncoding("UTF-8");

		String suggestionIdStr = request.getParameter("suggestionId");
		if (suggestionIdStr == null || "".equals(suggestionIdStr)) {
			response.sendRedirect(request.getContextPath() + "/suggestion/list");
			return;
		}

		SuggestionBoardDTO dto = new SuggestionBoardDTO();
		dto.setSuggestionId(Integer.parseInt(suggestionIdStr));
		dto.setTitle(request.getParameter("title"));
		dto.setContent(request.getParameter("content"));
		dto.setStatus(request.getParameter("status"));
		dto.setRemark(request.getParameter("remark"));

		boolean result = boardService.updateSuggestion(dto);

		if (result) {
			response.sendRedirect(
					request.getContextPath() + "/suggestion/detail?suggestionId=" + dto.getSuggestionId());
		} else {
			response.sendRedirect(request.getContextPath() + "/suggestion/list");
		}
	}
}