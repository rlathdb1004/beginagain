package common.filter;

import java.io.IOException;

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

	@Override
	public void init(FilterConfig fConfig) throws ServletException {
	}

	@Override
	public void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
			throws IOException, ServletException {

		String uri = request.getRequestURI();
		String cp = request.getContextPath();

		boolean loginRequest = uri.equals(cp + "/login");
		boolean logoutRequest = uri.equals(cp + "/logout");
		boolean changePasswordRequest = uri.equals(cp + "/changePassword");
		boolean mainRequest = uri.equals(cp + "/main");

		boolean staticRequest = uri.startsWith(cp + "/assets/") || uri.startsWith(cp + "/favicon.ico");

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
			response.sendRedirect(cp + "/main");
			return;
		}

		if (mainRequest) {
			chain.doFilter(request, response);
			return;
		}

		chain.doFilter(request, response);
	}

	@Override
	public void destroy() {
	}
}