package member.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

import member.dto.MemberDTO;
import member.service.MemberService;

@WebServlet("/member/update")
public class MemberUpdateController extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private MemberService memberService = new MemberService();

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		request.setCharacterEncoding("UTF-8");

		String empIdStr = request.getParameter("empId");
		String empName = request.getParameter("empName");
		String deptCode = request.getParameter("deptCode");
		String positionName = request.getParameter("positionName");
		String email = request.getParameter("email");
		String phone = request.getParameter("phone");
		String status = request.getParameter("status");
		String roleName = request.getParameter("roleName");
		String tempPwdYn = request.getParameter("tempPwdYn");
		String remark = request.getParameter("remark");

		if (empIdStr == null || "".equals(empIdStr)) {
			response.sendRedirect(request.getContextPath() + "/member/list");
			return;
		}

		MemberDTO dto = new MemberDTO();
		dto.setEmpId(Integer.parseInt(empIdStr));
		dto.setEmpName(empName);
		dto.setDeptCode(deptCode);
		dto.setPositionName(positionName);
		dto.setEmail(email);
		dto.setPhone(phone);
		dto.setStatus(status);
		dto.setRoleName(roleName);
		dto.setTempPwdYn(tempPwdYn);
		dto.setRemark(remark);

		boolean result = memberService.updateMember(dto);

		if (result) {
			response.sendRedirect(request.getContextPath() + "/member/detail?empId=" + dto.getEmpId());
		} else {
			response.sendRedirect(request.getContextPath() + "/member/list");
		}
	}
}