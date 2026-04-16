package MasterDataMgmt.ItemMgmt;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import MasterDataMgmt.DefectManagement.DefectMgmtService;

@WebServlet("/master-item")
public class ItemMgmtCon extends HttpServlet {

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		request.setCharacterEncoding("utf-8");
		response.setContentType("text/html; charset=utf-8");

		String type = request.getParameter("itemType");
		String dateType = request.getParameter("dateType");
		String startDate = request.getParameter("startDate");
		String endDate = request.getParameter("endDate");
		String field = request.getParameter("searchField");
		String keyword = request.getParameter("keyword");

		
		ItemMgmtSearchDTO dto = new ItemMgmtSearchDTO();
		dto.setType(type);
		dto.setDateType(dateType);
		dto.setStartDate(startDate);
		dto.setEndDate(endDate);
		dto.setField(field);
		dto.setKeyword(keyword);

		ItemMgmtService service = new ItemMgmtService();

		
		List<ItemMgmtDTO> list = service.getItemList(dto);

		request.setAttribute("itemList", list);
		request.setAttribute("contentPage", "/WEB-INF/views/item/ItemMgmt.jsp");

		request.getRequestDispatcher("/WEB-INF/views/table.jsp").forward(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		request.setCharacterEncoding("utf-8");
		response.setContentType("text/html; charset=utf-8");

		
		String item_code = request.getParameter("item_code");
		String item_name = request.getParameter("item_name");
		String item_type = request.getParameter("item_type");
		String unit = request.getParameter("unit");
		String spec = request.getParameter("spec");
		String supplier_name = request.getParameter("supplier_name");
		String use_yn = request.getParameter("use_yn");
		int safety_stock = 0;
		if (request.getParameter("safety_stock") != null && !request.getParameter("safety_stock").equals("")) {
			safety_stock = Integer.parseInt(request.getParameter("safety_stock"));
		}

		
		ItemMgmtDTO dto = new ItemMgmtDTO();
		dto.setItem_code(item_code);
		dto.setItem_name(item_name);
		dto.setItem_type(item_type);
		dto.setUnit(unit);
		dto.setSpec(spec);
		dto.setSupplier_name(supplier_name);
		dto.setSafety_stock(safety_stock);
		dto.setUse_yn(use_yn);

		String item_id = request.getParameter("item_id");
		ItemMgmtService service = new ItemMgmtService();
		if (item_id != null && !item_id.isEmpty()) {
		    dto.setItem_id(Integer.parseInt(item_id));
		    service.update(dto);
		} else {
		    service.insert(dto);
		}
		response.sendRedirect(request.getContextPath() + "/master-item");
	}

}
