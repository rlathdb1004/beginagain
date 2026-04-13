package item.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import item.dto.ItemDTO;
import item.service.ItemService;

@WebServlet("/item/update")
public class ItemUpdateController extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private ItemService itemService = new ItemService();

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		request.setCharacterEncoding("UTF-8");

		String itemIdStr = request.getParameter("itemId");
		String itemName = request.getParameter("itemName");
		String itemType = request.getParameter("itemType");
		String unit = request.getParameter("unit");
		String spec = request.getParameter("spec");
		String supplierName = request.getParameter("supplierName");
		String safetyStockStr = request.getParameter("safetyStock");
		String remark = request.getParameter("remark");

		if (itemIdStr == null || "".equals(itemIdStr)) {
			response.sendRedirect(request.getContextPath() + "/item/list");
			return;
		}

		double safetyStock = 0;
		if (safetyStockStr != null && !"".equals(safetyStockStr.trim())) {
			safetyStock = Double.parseDouble(safetyStockStr);
		}

		ItemDTO dto = new ItemDTO();
		dto.setItemId(Integer.parseInt(itemIdStr));
		dto.setItemName(itemName);
		dto.setItemType(itemType);
		dto.setUnit(unit);
		dto.setSpec(spec);
		dto.setSupplierName(supplierName);
		dto.setSafetyStock(safetyStock);
		dto.setRemark(remark);

		boolean result = itemService.updateItem(dto);

		if (result) {
			response.sendRedirect(request.getContextPath() + "/item/detail?itemId=" + dto.getItemId());
		} else {
			response.sendRedirect(request.getContextPath() + "/item/list");
		}
	}
}