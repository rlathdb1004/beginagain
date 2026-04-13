
package member.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import member.dto.MemberDTO;
import member.service.MemberService;

@WebServlet("/member/register")
public class MemberRegisterController extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private MemberService memberService = new MemberService();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        MemberDTO dto = new MemberDTO();
        dto.setEmpNo(request.getParameter("empNo"));
        dto.setEmpName(request.getParameter("empName"));
        dto.setDeptCode(request.getParameter("deptCode"));
        dto.setPositionName(request.getParameter("positionName"));
        dto.setEmail(request.getParameter("email"));
        dto.setPhone(request.getParameter("phone"));
        dto.setStatus(request.getParameter("status"));
        dto.setRoleName(request.getParameter("roleName"));
        dto.setRemark(request.getParameter("remark"));
        memberService.insertMember(dto);
        response.sendRedirect(request.getContextPath() + "/member/list");
    }
}
