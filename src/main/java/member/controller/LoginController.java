package member.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

import member.dto.MemberDTO;
import member.service.MemberService;

@WebServlet("/login")
public class LoginController extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private MemberService memberService = new MemberService();

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.getRequestDispatcher("/WEB-INF/views/member/login.jsp").forward(request, response);
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		request.setCharacterEncoding("UTF-8");

		String empNo = request.getParameter("empNo");
		String password = request.getParameter("password");

		MemberDTO loginUser = memberService.login(empNo, password);

		if (loginUser == null) {
			request.setAttribute("errorMsg", "사번 또는 비밀번호가 올바르지 않습니다.");
			request.getRequestDispatcher("/WEB-INF/views/member/login.jsp").forward(request, response);
			return;
		}

		HttpSession session = request.getSession();
		session.setAttribute("loginUser", loginUser);

		if ("Y".equals(loginUser.getTempPwdYn())) {
			response.sendRedirect(request.getContextPath() + "/changePassword");
		} else {
			response.sendRedirect(request.getContextPath() + "/main");
		}
	}
}