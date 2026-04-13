package member.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

import member.service.MemberService;

@WebServlet("/member/delete")
public class MemberDeleteController extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private MemberService memberService = new MemberService();

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		request.setCharacterEncoding("UTF-8");

		String[] empIds = request.getParameterValues("empId");

		if (empIds == null || empIds.length == 0) {
			response.sendRedirect(request.getContextPath() + "/member/list");
			return;
		}

		memberService.deleteMembers(empIds);
		response.sendRedirect(request.getContextPath() + "/member/list");
	}
}