package ProdMgmt.ProdPlanRegInq.Controller;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/prodplan")
public class ProdPlanRegInqController extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.setCharacterEncoding("utf-8");
		response.setContentType("text/html;charset=utf-8");

		System.out.println("CreateController doGet 실행");

//		request.getRequestDispatcher("/beginagain/src/main/webapp/WEB-INF/views/ProdPlanRegInq.jsp").forward(request, response);
		request.getRequestDispatcher("/WEB-INF/views/ProdPlanRegInq.jsp").forward(request, response);

	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.setCharacterEncoding("utf-8");
		response.setContentType("text/html;charset=utf-8");

	}

}
