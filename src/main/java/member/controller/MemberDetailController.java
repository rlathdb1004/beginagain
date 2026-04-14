package member.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

import member.dto.MemberDTO;
import member.service.MemberService;

@WebServlet("/member/detail")
public class MemberDetailController extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private MemberService memberService = new MemberService();

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String empIdStr = request.getParameter("empId");

		if (empIdStr == null || "".equals(empIdStr)) {
			response.sendRedirect(request.getContextPath() + "/member/list");
			return;
		}

		int empId = Integer.parseInt(empIdStr);
		MemberDTO member = memberService.getMemberById(empId);

		if (member == null) {
			response.sendRedirect(request.getContextPath() + "/member/list");
			return;
		}

		request.setAttribute("member", member);
		request.setAttribute("pageTitle", "사원관리");
		request.setAttribute("pageSubTitle", "사원 상세 / 수정");
		request.setAttribute("contentPage", "/WEB-INF/views/member/memberDetail.jsp");
		request.getRequestDispatcher("/WEB-INF/views/table.jsp").forward(request, response);
	}
}