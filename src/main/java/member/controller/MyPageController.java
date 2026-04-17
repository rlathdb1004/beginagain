package member.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import member.dto.MemberDTO;
import member.service.MemberService;

@WebServlet("/mypage")
public class MyPageController extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private MemberService memberService = new MemberService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("loginUser") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        MemberDTO loginUser = (MemberDTO) session.getAttribute("loginUser");
        MemberDTO freshUser = memberService.getMemberById(loginUser.getEmpId());

        if (freshUser == null) {
            session.invalidate();
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        session.setAttribute("loginUser", freshUser);

        request.setAttribute("pageTitle", "마이페이지");
        request.setAttribute("myPageUser", freshUser);
        request.setAttribute("contentPage", "/WEB-INF/views/member/myPage.jsp");
        request.getRequestDispatcher("/WEB-INF/views/table.jsp").forward(request, response);
    }
}
