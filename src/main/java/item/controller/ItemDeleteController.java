package item.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import item.service.ItemService;

@WebServlet("/item/delete")
public class ItemDeleteController extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private ItemService itemService = new ItemService();

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		request.setCharacterEncoding("UTF-8");

		String[] itemIds = request.getParameterValues("itemId");

		if (itemIds == null || itemIds.length == 0) {
			response.sendRedirect(request.getContextPath() + "/item/list");
			return;
		}

		boolean result = itemService.deleteItems(itemIds);

		if (result) {
			response.sendRedirect(request.getContextPath() + "/item/list");
		} else {
			response.sendRedirect(request.getContextPath() + "/item/list");
		}
	}
}