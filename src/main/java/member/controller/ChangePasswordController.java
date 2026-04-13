package member.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

import member.dto.MemberDTO;
import member.service.MemberService;

@WebServlet("/changePassword")
public class ChangePasswordController extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private MemberService memberService = new MemberService();

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.getRequestDispatcher("/WEB-INF/views/member/changePassword.jsp").forward(request, response);
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		request.setCharacterEncoding("UTF-8");

		HttpSession session = request.getSession(false);
		if (session == null) {
			response.sendRedirect(request.getContextPath() + "/login");
			return;
		}

		MemberDTO loginUser = (MemberDTO) session.getAttribute("loginUser");
		if (loginUser == null) {
			response.sendRedirect(request.getContextPath() + "/login");
			return;
		}

		String newPassword = request.getParameter("newPassword");
		String confirmPassword = request.getParameter("confirmPassword");

		if (newPassword == null || "".equals(newPassword.trim())) {
			request.setAttribute("errorMsg", "새 비밀번호를 입력하세요.");
			request.getRequestDispatcher("/WEB-INF/views/member/changePassword.jsp").forward(request, response);
			return;
		}

		if (!newPassword.equals(confirmPassword)) {
			request.setAttribute("errorMsg", "새 비밀번호와 확인 비밀번호가 일치하지 않습니다.");
			request.getRequestDispatcher("/WEB-INF/views/member/changePassword.jsp").forward(request, response);
			return;
		}

		boolean result = memberService.changePassword(loginUser.getEmpId(), newPassword);

		if (result) {
			loginUser.setTempPwdYn("N");
			session.setAttribute("loginUser", loginUser);
			response.sendRedirect(request.getContextPath() + "/main");
		} else {
			request.setAttribute("errorMsg", "비밀번호 변경 실패");
			request.getRequestDispatcher("/WEB-INF/views/member/changePassword.jsp").forward(request, response);
		}
	}
}