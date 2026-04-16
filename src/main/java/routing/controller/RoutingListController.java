package routing.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import item.dto.ItemDTO;
import item.service.ItemService;
import routing.dto.RoutingDTO;
import routing.service.RoutingService;

@WebServlet("/routing/list")
public class RoutingListController extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private ItemService itemService = new ItemService();
    private RoutingService routingService = new RoutingService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        List<ItemDTO> itemList = itemService.getItemList();
        request.setAttribute("itemList", itemList);

        String itemIdParam = request.getParameter("itemId");
        Integer selectedItemId = null;

        if (itemIdParam != null && !itemIdParam.trim().isEmpty()) {
            try {
                selectedItemId = Integer.parseInt(itemIdParam);
                List<RoutingDTO> routingList = routingService.getRoutingListByItemId(selectedItemId);
                request.setAttribute("routingList", routingList);
                request.setAttribute("selectedItemId", selectedItemId);
            } catch (NumberFormatException e) {
                request.setAttribute("errorMessage", "잘못된 품목 번호입니다.");
            }
        }

        request.setAttribute("pageTitle", "라우팅관리");
        request.setAttribute("pageSubTitle", "품목별 라우팅 조회");
        request.setAttribute("contentPage", "/WEB-INF/views/routing/routingList.jsp");
        request.getRequestDispatcher("/WEB-INF/views/table.jsp").forward(request, response);
    }
}
