package member.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

import member.dto.MemberDTO;
import member.service.MemberService;

@WebServlet("/member/list")
public class MemberListController extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private MemberService memberService = new MemberService();

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		List<MemberDTO> memberList = memberService.getMemberList();

		request.setAttribute("memberList", memberList);
		request.getRequestDispatcher("/WEB-INF/views/member/memberList.jsp").forward(request, response);
	}
}