package MasterDataMgmt.ItemMgmt;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/item-del")
public class ItemMgmtDeleteCon extends HttpServlet {
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("utf-8");
		response.setContentType("text/html; charset=utf-8");

		String[] ids = request.getParameterValues("item_id");
		ItemMgmtService service = new ItemMgmtService();

		try {
			if (ids != null) {
				for (String id : ids) {
					service.delete(Integer.parseInt(id));
				}
			}
		} catch (RuntimeException e) {
			request.getSession().setAttribute("errorMsg", e.getMessage());
		}

		response.sendRedirect(request.getContextPath() + "/master-item");
	}
}
