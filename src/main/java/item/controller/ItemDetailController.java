package item.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import item.dto.ItemDTO;
import item.service.ItemService;

@WebServlet("/item/detail")
public class ItemDetailController extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private ItemService itemService = new ItemService();

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String itemIdStr = request.getParameter("itemId");

		if (itemIdStr == null || "".equals(itemIdStr)) {
			response.sendRedirect(request.getContextPath() + "/item/list");
			return;
		}

		int itemId = Integer.parseInt(itemIdStr);
		ItemDTO item = itemService.getItemById(itemId);

		if (item == null) {
			response.sendRedirect(request.getContextPath() + "/item/list");
			return;
		}

		request.setAttribute("item", item);
		request.setAttribute("pageTitle", "품목관리");
		request.setAttribute("pageSubTitle", "품목 상세 / 수정");
		request.setAttribute("contentPage", "/WEB-INF/views/item/itemDetail.jsp");
		request.getRequestDispatcher("/WEB-INF/views/table.jsp").forward(request, response);
	}
}