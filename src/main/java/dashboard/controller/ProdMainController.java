package dashboard.controller;

import java.io.IOException;
import java.sql.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dashboard.dto.ProdMainDTO;
import dashboard.service.ProdMainService;

@WebServlet("/prodmain")
public class ProdMainController extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private final ProdMainService service = new ProdMainService();

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		request.setCharacterEncoding("utf-8");
		response.setContentType("text/html; charset=utf-8");

		System.out.println("ProdMainController doGet 실행");

		try {
			String baseDateParam = request.getParameter("baseDate");
			Date requestBaseDate = parseBaseDate(baseDateParam);

			ProdMainDTO dashboard = service.getDashboard(requestBaseDate);

			request.setAttribute("pageTitle", "생산관리 대시보드");
			request.setAttribute("pageSubtitle", "생산계획 달성률, 수율, 납기 준수율, 설비 종합 효율을 한 화면에서 관리합니다.");
			request.setAttribute("contentPage", "/WEB-INF/views/prodmain.jsp");

			if (dashboard != null) {
				request.setAttribute("baseDate", dashboard.getBaseDate());
				request.setAttribute("kpi", nullSafeMap(dashboard.getKpi()));

				request.setAttribute("lineStatusList", nullSafeList(dashboard.getLineStatusList()));
				request.setAttribute("lineRuntimeList", nullSafeList(dashboard.getLineRuntimeList()));
				request.setAttribute("downtimeCauseList", nullSafeList(dashboard.getDowntimeCauseList()));

				request.setAttribute("materialAlertList", nullSafeList(dashboard.getMaterialAlertList()));
				request.setAttribute("equipmentAlertList", nullSafeList(dashboard.getEquipmentAlertList()));

				request.setAttribute("planDetailList", nullSafeList(dashboard.getPlanDetailList()));
				request.setAttribute("yieldDetailList", nullSafeList(dashboard.getYieldDetailList()));
				request.setAttribute("shipmentDetailList", nullSafeList(dashboard.getShipmentDetailList()));
				request.setAttribute("oeeDetailList", nullSafeList(dashboard.getOeeDetailList()));

				// 호환용
				request.setAttribute("workOrderList", nullSafeList(dashboard.getWorkOrderList()));
				request.setAttribute("issueList", nullSafeList(dashboard.getIssueList()));
				request.setAttribute("hourlyProductionList", nullSafeList(dashboard.getHourlyProductionList()));
				request.setAttribute("handoverList", nullSafeList(dashboard.getHandoverList()));
				request.setAttribute("updatedAt", dashboard.getUpdatedAt());
			} else {
				request.setAttribute("baseDate", new Date(System.currentTimeMillis()));
				request.setAttribute("kpi", new HashMap<String, Object>());

				request.setAttribute("lineStatusList", new ArrayList<Map<String, Object>>());
				request.setAttribute("lineRuntimeList", new ArrayList<Map<String, Object>>());
				request.setAttribute("downtimeCauseList", new ArrayList<Map<String, Object>>());

				request.setAttribute("materialAlertList", new ArrayList<Map<String, Object>>());
				request.setAttribute("equipmentAlertList", new ArrayList<Map<String, Object>>());

				request.setAttribute("planDetailList", new ArrayList<Map<String, Object>>());
				request.setAttribute("yieldDetailList", new ArrayList<Map<String, Object>>());
				request.setAttribute("shipmentDetailList", new ArrayList<Map<String, Object>>());
				request.setAttribute("oeeDetailList", new ArrayList<Map<String, Object>>());

				request.setAttribute("workOrderList", new ArrayList<Map<String, Object>>());
				request.setAttribute("issueList", new ArrayList<Map<String, Object>>());
				request.setAttribute("hourlyProductionList", new ArrayList<Map<String, Object>>());
				request.setAttribute("handoverList", new ArrayList<Map<String, Object>>());
				request.setAttribute("updatedAt", new java.sql.Timestamp(System.currentTimeMillis()));
			}

			System.out.println("baseDate = " + request.getAttribute("baseDate"));
			System.out.println("kpi = " + request.getAttribute("kpi"));
			System.out.println("lineStatusList = " + request.getAttribute("lineStatusList"));
			System.out.println("lineRuntimeList = " + request.getAttribute("lineRuntimeList"));
			System.out.println("downtimeCauseList = " + request.getAttribute("downtimeCauseList"));
			System.out.println("materialAlertList = " + request.getAttribute("materialAlertList"));
			System.out.println("equipmentAlertList = " + request.getAttribute("equipmentAlertList"));
			System.out.println("planDetailList = " + request.getAttribute("planDetailList"));
			System.out.println("yieldDetailList = " + request.getAttribute("yieldDetailList"));
			System.out.println("shipmentDetailList = " + request.getAttribute("shipmentDetailList"));
			System.out.println("oeeDetailList = " + request.getAttribute("oeeDetailList"));

			request.getRequestDispatcher("/WEB-INF/views/table.jsp").forward(request, response);

		} catch (Exception e) {
			e.printStackTrace();
			throw new ServletException("생산관리 대시보드 조회 중 오류가 발생했습니다.", e);
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