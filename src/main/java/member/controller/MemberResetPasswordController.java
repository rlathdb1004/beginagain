package member.controller;

import java.io.IOException;
import java.net.URLEncoder;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import member.dto.MemberDTO;
import member.service.MemberService;

@WebServlet("/member/resetPassword")
public class MemberResetPasswordController extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private MemberService memberService = new MemberService();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("loginUser") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        MemberDTO loginUser = (MemberDTO) session.getAttribute("loginUser");
        if (!"관리자".equals(loginUser.getRoleName())) {
            response.sendRedirect(request.getContextPath() + "/member/list");
            return;
        }

        String empIdStr = request.getParameter("empId");
        if (empIdStr == null || "".equals(empIdStr.trim())) {
            response.sendRedirect(request.getContextPath() + "/member/list");
            return;
        }

        int targetEmpId = Integer.parseInt(empIdStr);
        String tempPassword = memberService.resetPasswordByAdmin(targetEmpId);

        String redirectUrl = request.getContextPath() + "/member/detail?empId=" + targetEmpId;
        if (tempPassword != null) {
            redirectUrl += "&resetSuccess=Y&tempPassword=" + URLEncoder.encode(tempPassword, "UTF-8");
        } else {
            redirectUrl += "&resetSuccess=N";
        }

        response.sendRedirect(redirectUrl);
    }
}
