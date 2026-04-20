package common.filter;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import member.dto.MemberDTO;

@WebFilter("/*")
public class AuthFilter extends HttpFilter implements Filter {
    private static final long serialVersionUID = 1L;

    private static final Set<String> COMMON_GET_PATHS = new HashSet<String>(Arrays.asList(
            "/notice", "/suggestion", "/suggestion/list"));

    private static final Set<String> CEO_GET_PATHS = new HashSet<String>(Arrays.asList(
            "/ceomain",
            "/report",
            "/prodperf", "/prodperf/detail",
            "/workstatus", "/workstatus/detail",
            "/item/list", "/item/detail",
            "/process/list", "/process/detail",
            "/routing/list", "/routing/detail",
            "/equipment/list", "/equipment/detail",
            "/BOM-mgmt", "/defect-mgmt"));

    private static final Set<String> SITE_MANAGER_GET_PATHS = new HashSet<String>(Arrays.asList(
            "/prodmain",
            "/report",
            "/ioRegInq", "/invRegInq",
            "/prodplan",
            "/prodperf", "/prodperf/detail",
            "/woreginq", "/woreginq/detail",
            "/workstatus", "/workstatus/detail",
            "/matInspRegInq", "/fpInspRegInq", "/defectRegInq",
            "/maintenance/list", "/maintenance/detail",
            "/downtime/list", "/downtime/detail",
            "/failureaction/register", "/failureaction/detail"));

    private static final Set<String> SITE_MANAGER_POST_PATHS = new HashSet<String>(Arrays.asList(
            "/ioRegInq", "/invRegInq",
            "/prodplan",
            "/prodperf", "/prodperf/register", "/prodperf/update",
            "/woreginq", "/woreginq/register", "/woreginq/update",
            "/matInspRegInq", "/fpInspRegInq", "/defectRegInq",
            "/maintenance/register", "/maintenance/update", "/maintenance/delete",
            "/failureaction/register", "/failureaction/update", "/failureaction/delete"));

    private static final Set<String> WORKER_GET_PATHS = new HashSet<String>(Arrays.asList(
            "/prod/worker"));

    private static final Set<String> WORKER_POST_PATHS = new HashSet<String>(Arrays.asList(
            "/prodperf/register",
            "/suggestion/insert", "/suggestion/update", "/suggestion/delete"));

    private static final Set<String> CEO_POST_PATHS = new HashSet<String>(Arrays.asList(
            "/suggestion/insert"));

    @Override
    public void init(FilterConfig fConfig) throws ServletException {
    }

    @Override
    public void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        String uri = request.getRequestURI();
        String cp = request.getContextPath();
        String path = uri.substring(cp.length());
        String method = request.getMethod();

        boolean loginRequest = path.equals("/login");
        boolean logoutRequest = path.equals("/logout");
        boolean changePasswordRequest = path.equals("/changePassword");
        boolean mainRequest = path.equals("/main");
        boolean myPageRequest = path.equals("/mypage") || path.equals("/mypage/update");
        boolean staticRequest = path.startsWith("/assets/") || path.equals("/favicon.ico");

        if (staticRequest) {
            chain.doFilter(request, response);
            return;
        }

        HttpSession session = request.getSession(false);
        MemberDTO loginUser = null;

        if (session != null) {
            loginUser = (MemberDTO) session.getAttribute("loginUser");
        }

        if (loginUser == null) {
            if (loginRequest) {
                chain.doFilter(request, response);
            } else {
                response.sendRedirect(cp + "/login");
            }
            return;
        }

        if ("Y".equals(loginUser.getTempPwdYn())) {
            if (changePasswordRequest || logoutRequest) {
                chain.doFilter(request, response);
            } else {
                response.sendRedirect(cp + "/changePassword");
            }
            return;
        }

        if (loginRequest) {
            response.sendRedirect(cp + getMainPageByRole(loginUser.getRoleName()));
            return;
        }

        if (mainRequest) {
            response.sendRedirect(cp + getMainPageByRole(loginUser.getRoleName()));
            return;
        }

        String role = loginUser.getRoleName();

        if (logoutRequest || changePasswordRequest || myPageRequest || isCommonAllowed(path, method)) {
            chain.doFilter(request, response);
            return;
        }

        if ("MES_ADMIN".equals(role)) {
            chain.doFilter(request, response);
            return;
        }

        if ("CEO".equals(role)) {
            if (isCeoAllowed(path, method) || isCeoPostAllowed(path, method)) {
                chain.doFilter(request, response);
                return;
            }
            redirectNoAuth(request, response, loginUser);
            return;
        }

        if ("SITE_MANAGER".equals(role)) {
            if (isSiteManagerAllowed(path, method)) {
                chain.doFilter(request, response);
                return;
            }
            redirectNoAuth(request, response, loginUser);
            return;
        }

        if ("WORKER".equals(role)) {
            if (isWorkerAllowed(path, method) || isWorkerPostAllowed(request, path)) {
                chain.doFilter(request, response);
                return;
            }
            redirectNoAuth(request, response, loginUser);
            return;
        }

        session.invalidate();
        response.sendRedirect(cp + "/login");
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

    private boolean isCommonAllowed(String path, String method) {
        return "GET".equalsIgnoreCase(method) && COMMON_GET_PATHS.contains(path);
    }

    private boolean isCeoAllowed(String path, String method) {
        return "GET".equalsIgnoreCase(method) && CEO_GET_PATHS.contains(path);
    }

    private boolean isCeoPostAllowed(String path, String method) {
        return "POST".equalsIgnoreCase(method) && CEO_POST_PATHS.contains(path);
    }

    private boolean isSiteManagerAllowed(String path, String method) {
        if ("GET".equalsIgnoreCase(method)) {
            return SITE_MANAGER_GET_PATHS.contains(path);
        }
        return SITE_MANAGER_POST_PATHS.contains(path);
    }

    private boolean isWorkerAllowed(String path, String method) {
        if ("GET".equalsIgnoreCase(method)) {
            return WORKER_GET_PATHS.contains(path);
        }
        return false;
    }

    private boolean isWorkerPostAllowed(HttpServletRequest request, String path) {
        if (!"POST".equalsIgnoreCase(request.getMethod())) {
            return false;
        }

        if (!WORKER_POST_PATHS.contains(path)) {
            return false;
        }

        if ("/suggestion/update".equals(path) || "/suggestion/delete".equals(path)) {
            String suggestionId = request.getParameter("suggestionId");
            return suggestionId != null && !"".equals(suggestionId.trim());
        }

        return true;
    }

    private void redirectNoAuth(HttpServletRequest request, HttpServletResponse response, MemberDTO loginUser)
            throws IOException {
        HttpSession session = request.getSession();
        session.setAttribute("alertMsg", "접근 권한이 없습니다.");
        response.sendRedirect(request.getContextPath() + getMainPageByRole(loginUser.getRoleName()));
    }

    @Override
    public void destroy() {
    }
}
