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
        if (session == null || session.getAttribute("loginUser") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        MemberDTO loginUser = (MemberDTO) session.getAttribute("loginUser");

        String currentPassword = nvl(request.getParameter("currentPassword"));
        String newPassword = nvl(request.getParameter("newPassword"));
        String confirmPassword = nvl(request.getParameter("confirmPassword"));
        String returnUrl = nvl(request.getParameter("returnUrl"));

        if ("".equals(returnUrl)) {
            returnUrl = "Y".equals(loginUser.getTempPwdYn())
                    ? request.getContextPath() + "/changePassword"
                    : request.getContextPath() + "/mypage";
        }

        boolean tempPasswordUser = "Y".equals(loginUser.getTempPwdYn());

        if (!tempPasswordUser && "".equals(currentPassword)) {
            session.setAttribute("pwdErrorMsg", "현재 비밀번호를 입력하세요.");
            response.sendRedirect(returnUrl);
            return;
        }

        if ("".equals(newPassword)) {
            session.setAttribute("pwdErrorMsg", "새 비밀번호를 입력하세요.");
            response.sendRedirect(returnUrl);
            return;
        }

        if (!newPassword.equals(confirmPassword)) {
            session.setAttribute("pwdErrorMsg", "새 비밀번호와 확인 비밀번호가 일치하지 않습니다.");
            response.sendRedirect(returnUrl);
            return;
        }

        if (!tempPasswordUser) {
            boolean currentMatched = memberService.checkCurrentPassword(loginUser.getEmpId(), currentPassword);
            if (!currentMatched) {
                session.setAttribute("pwdErrorMsg", "현재 비밀번호가 일치하지 않습니다.");
                response.sendRedirect(returnUrl);
                return;
            }
        }

        if (!memberService.isValidPassword(newPassword, loginUser.getEmpNo(), currentPassword)) {
            session.setAttribute("pwdErrorMsg", "비밀번호 규칙에 맞게 입력해주세요.");
            response.sendRedirect(returnUrl);
            return;
        }

        boolean result = memberService.changePassword(loginUser.getEmpId(), newPassword);

        if (result) {
            MemberDTO refreshedUser = memberService.getMemberById(loginUser.getEmpId());
            if (refreshedUser != null) {
                refreshedUser.setTempPwdYn("N");
                session.setAttribute("loginUser", refreshedUser);
            }

            if (tempPasswordUser) {
                response.sendRedirect(request.getContextPath() + getMainPageByRole(refreshedUser.getRoleName()));
                return;
            }

            session.setAttribute("successMsg", "비밀번호가 변경되었습니다.");
        } else {
            session.setAttribute("pwdErrorMsg", "비밀번호 변경에 실패했습니다.");
        }

        response.sendRedirect(returnUrl);
    }
    
    private String getMainPageByRole(String roleName) {
        if ("CEO".equals(roleName)) {
            return "/ceomain";
        } else if ("MES_ADMIN".equals(roleName)) {
            return "/adminmain";
        } else if ("SITE_MANAGER".equals(roleName)) {
            return "/prodmain";
        } else if ("WORKER".equals(roleName)) {
            return "/prod/worker";
        }
        return "/ceomain";
    }

    private String nvl(String value) {
        return value == null ? "" : value.trim();
    }
}
