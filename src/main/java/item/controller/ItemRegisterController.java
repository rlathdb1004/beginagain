
package item.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import item.dto.ItemDTO;
import item.service.ItemService;

@WebServlet("/item/register")
public class ItemRegisterController extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private ItemService itemService = new ItemService();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        double safetyStock = 0;
        String safetyStockStr = request.getParameter("safetyStock");
        if (safetyStockStr != null && !"".equals(safetyStockStr.trim())) {
            safetyStock = Double.parseDouble(safetyStockStr);
        }
        ItemDTO dto = new ItemDTO();
        dto.setItemCode(request.getParameter("itemCode"));
        dto.setItemName(request.getParameter("itemName"));
        dto.setItemType(request.getParameter("itemType"));
        dto.setUnit(request.getParameter("unit"));
        dto.setSpec(request.getParameter("spec"));
        dto.setSupplierName(request.getParameter("supplierName"));
        dto.setSafetyStock(safetyStock);
        dto.setRemark(request.getParameter("remark"));
        itemService.insertItem(dto);
        response.sendRedirect(request.getContextPath() + "/item/list");
    }
}
