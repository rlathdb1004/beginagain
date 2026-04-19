package dashboard.controller;

import java.io.IOException;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dashboard.dto.AdminMainDTO;
import dashboard.service.AdminMainService;

@WebServlet("/adminmain")
public class AdminMainController extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private final AdminMainService service = new AdminMainService();

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		request.setCharacterEncoding("utf-8");
		response.setContentType("text/html; charset=utf-8");

		System.out.println("AdminMainController doGet 실행");

		try {
			String baseDateParam = request.getParameter("baseDate");
			Date requestBaseDate = parseBaseDate(baseDateParam);

			AdminMainDTO dashboard = service.getDashboard(requestBaseDate);

			request.setAttribute("pageTitle", "MES 관리자 대시보드");
			request.setAttribute("pageSubtitle", "시스템 운영 상태, 승인 대기, 인터페이스, 배치, 데이터 품질을 통합 관리합니다.");
			request.setAttribute("contentPage", "/WEB-INF/views/adminmain.jsp");

			if (dashboard != null) {
				request.setAttribute("baseDate", dashboard.getBaseDate());
				request.setAttribute("updatedAt", dashboard.getUpdatedAt());

				request.setAttribute("summary", nullSafeMap(dashboard.getSummary()));

				request.setAttribute("masterStatusList", nullSafeList(dashboard.getMasterStatusList()));
				request.setAttribute("approvalList", nullSafeList(dashboard.getApprovalList()));
				request.setAttribute("interfaceStatusList", nullSafeList(dashboard.getInterfaceStatusList()));
				request.setAttribute("batchStatusList", nullSafeList(dashboard.getBatchStatusList()));
				request.setAttribute("dataQualityList", nullSafeList(dashboard.getDataQualityList()));
				request.setAttribute("userStatusList", nullSafeList(dashboard.getUserStatusList()));
				request.setAttribute("auditLogList", nullSafeList(dashboard.getAuditLogList()));
				request.setAttribute("noticeList", nullSafeList(dashboard.getNoticeList()));
			} else {
				request.setAttribute("baseDate", new Date(System.currentTimeMillis()));
				request.setAttribute("updatedAt", new Timestamp(System.currentTimeMillis()));

				request.setAttribute("summary", new HashMap<String, Object>());

				request.setAttribute("masterStatusList", new ArrayList<Map<String, Object>>());
				request.setAttribute("approvalList", new ArrayList<Map<String, Object>>());
				request.setAttribute("interfaceStatusList", new ArrayList<Map<String, Object>>());
				request.setAttribute("batchStatusList", new ArrayList<Map<String, Object>>());
				request.setAttribute("dataQualityList", new ArrayList<Map<String, Object>>());
				request.setAttribute("userStatusList", new ArrayList<Map<String, Object>>());
				request.setAttribute("auditLogList", new ArrayList<Map<String, Object>>());
				request.setAttribute("noticeList", new ArrayList<Map<String, Object>>());
			}

			System.out.println("baseDate = " + request.getAttribute("baseDate"));
			System.out.println("updatedAt = " + request.getAttribute("updatedAt"));
			System.out.println("summary = " + request.getAttribute("summary"));
			System.out.println("masterStatusList = " + request.getAttribute("masterStatusList"));
			System.out.println("approvalList = " + request.getAttribute("approvalList"));
			System.out.println("interfaceStatusList = " + request.getAttribute("interfaceStatusList"));
			System.out.println("batchStatusList = " + request.getAttribute("batchStatusList"));
			System.out.println("dataQualityList = " + request.getAttribute("dataQualityList"));
			System.out.println("userStatusList = " + request.getAttribute("userStatusList"));
			System.out.println("auditLogList = " + request.getAttribute("auditLogList"));
			System.out.println("noticeList = " + request.getAttribute("noticeList"));

			request.getRequestDispatcher("/WEB-INF/views/table.jsp").forward(request, response);

		} catch (Exception e) {
			e.printStackTrace();
			throw new ServletException("MES 관리자 대시보드 조회 중 오류가 발생했습니다.", e);
		}
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}

	private Date parseBaseDate(String baseDateParam) {
		if (baseDateParam == null || baseDateParam.trim().isEmpty()) {
			return null;
		}

		try {
			return Date.valueOf(baseDateParam.trim());
		} catch (IllegalArgumentException e) {
			return null;
		}
	}

	private Map<String, Object> nullSafeMap(Map<String, Object> map) {
		return map == null ? new HashMap<String, Object>() : map;
	}

	private List<Map<String, Object>> nullSafeList(List<Map<String, Object>> list) {
		return list == null ? new ArrayList<Map<String, Object>>() : list;
	}
}