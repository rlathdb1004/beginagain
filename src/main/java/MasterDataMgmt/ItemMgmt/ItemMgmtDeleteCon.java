package MasterDataMgmt.ItemMgmt;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


@WebServlet("/item-delete")
public class ItemMgmtDeleteCon extends HttpServlet {
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
	}

	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("utf-8");
		response.setContentType("text/html; charset=utf-8");

        int id = Integer.parseInt(request.getParameter("id"));

        ItemMgmtService service = new ItemMgmtService();
        service.deleteItem(id);

        // 삭제 후 리스트로 이동
        response.sendRedirect("/listItem.do");
	}

}
