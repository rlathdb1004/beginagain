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

@WebServlet("/master-item")
public class ItemMgmtCon extends HttpServlet {

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		response.setContentType("text/html; charset=utf-8;");

		String type = request.getParameter("type");

	    ItemMgmtService service = new ItemMgmtService();
	    List<ItemMgmtDTO> list = service.getItemList(); 
	   

	    request.setAttribute("itemList", list);
	    request.getRequestDispatcher("/ItemMgmt.jsp").forward(request, response);
	 
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		// 한글 처리
		request.setCharacterEncoding("utf-8");
		response.setContentType("text/html; charset=utf-8");

		// 파라미터 받기
		String item_code = request.getParameter("item_code");
		String item_name = request.getParameter("item_name");
		String item_type = request.getParameter("item_type");
		String unit = request.getParameter("unit");
		String spec = request.getParameter("spec");
		String supplier_name = request.getParameter("supplier_name");

		int safety_stock = 0;
		if (request.getParameter("safety_stock") != null && !request.getParameter("safety_stock").equals("")) {
		    safety_stock = Integer.parseInt(request.getParameter("safety_stock"));
		}

		// DTO 세팅
		ItemMgmtDTO dto = new ItemMgmtDTO();
		dto.setItem_code(item_code);
		dto.setItem_name(item_name);
		dto.setItem_type(item_type);
		dto.setUnit(unit);
		dto.setSpec(spec);
		dto.setSupplier_name(supplier_name);
		dto.setSafety_stock(safety_stock);

		// DAO 호출
		ItemMgmtDAO dao = new ItemMgmtDAO();
		dao.insertItem(dto);

		System.out.println("등록 완료");

		// 🔥 다시 목록으로 이동
		response.sendRedirect("/master-item");
	}

}
