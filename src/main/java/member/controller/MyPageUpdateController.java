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

@WebServlet("/mypage/update")
public class MyPageUpdateController extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private MemberService memberService = new MemberService();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("loginUser") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        MemberDTO loginUser = (MemberDTO) session.getAttribute("loginUser");

        String email = nvl(request.getParameter("email"));
        String phone = nvl(request.getParameter("phone"));
        String remark = nvl(request.getParameter("remark"));

        if (!"".equals(email) && !email.matches("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")) {
            session.setAttribute("errorMsg", "이메일 형식이 올바르지 않습니다.");
            response.sendRedirect(request.getContextPath() + "/mypage");
            return;
        }

        if (!"".equals(phone) && !phone.matches("^01[0-9]-\\d{3,4}-\\d{4}$")) {
            session.setAttribute("errorMsg", "전화번호는 010-0000-0000 형식으로 입력해주세요.");
            response.sendRedirect(request.getContextPath() + "/mypage");
            return;
        }

        MemberDTO dto = new MemberDTO();
        dto.setEmpId(loginUser.getEmpId());
        dto.setEmail(email);
        dto.setPhone(phone);
        dto.setRemark(remark);

        boolean result = memberService.updateMyPage(dto);

        if (result) {
            MemberDTO refreshedUser = memberService.getMemberById(loginUser.getEmpId());
            if (refreshedUser != null) {
                session.setAttribute("loginUser", refreshedUser);
            }
            session.setAttribute("successMsg", "내 정보가 수정되었습니다.");
        } else {
            session.setAttribute("errorMsg", "내 정보 수정에 실패했습니다.");
        }

        response.sendRedirect(request.getContextPath() + "/mypage");
    }

    private String nvl(String value) {
        return value == null ? "" : value.trim();
    }
}
