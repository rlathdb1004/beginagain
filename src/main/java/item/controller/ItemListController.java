package item.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import item.dto.ItemDTO;
import item.service.ItemService;

@WebServlet("/item/list")
public class ItemListController extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private ItemService itemService = new ItemService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        List<ItemDTO> itemList = itemService.getItemList();

        request.setAttribute("itemList", itemList);
        request.getRequestDispatcher("/WEB-INF/views/item/itemList.jsp").forward(request, response);
    }
}