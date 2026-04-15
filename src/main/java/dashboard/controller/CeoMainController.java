package dashboard.controller;

import java.io.IOException;
import java.sql.Date;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dashboard.dto.CeoDashboardDTO;
import dashboard.service.CeoMainService;

@WebServlet("/ceomain")
public class CeoMainController extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private final CeoMainService service = new CeoMainService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("utf-8");
        response.setContentType("text/html; charset=utf-8");

        System.out.println("CeoMainController doGet 실행");

        try {
            String baseDateParam = request.getParameter("baseDate");
            Date baseDate = parseBaseDate(baseDateParam);

            CeoDashboardDTO dashboard = service.getDashboard(baseDate);

            request.setAttribute("pageTitle", "CEO 대시보드");
            request.setAttribute("pageSubtitle", "공장 전체 운영 상태와 핵심 리스크를 한눈에 확인합니다.");
            request.setAttribute("contentPage", "/WEB-INF/views/ceomain.jsp");

            if (dashboard != null) {
                request.setAttribute("baseDate", dashboard.getBaseDate());
                request.setAttribute("totalLineCount", dashboard.getTotalLineCount());
                request.setAttribute("briefingText", dashboard.getBriefingText());

                request.setAttribute("kpi", dashboard.getKpi());

                request.setAttribute("riskList", dashboard.getRiskList());
                request.setAttribute("factoryStatusList", dashboard.getFactoryStatusList());

                request.setAttribute("downtimeCauseList", dashboard.getDowntimeCauseList());
                request.setAttribute("defectCauseList", dashboard.getDefectCauseList());
                request.setAttribute("delayCauseList", dashboard.getDelayCauseList());

                request.setAttribute("approvalList", dashboard.getApprovalList());

                request.setAttribute("productionTrendList", dashboard.getProductionTrendList());
                request.setAttribute("qualityTrendList", dashboard.getQualityTrendList());
                request.setAttribute("shipmentTrendList", dashboard.getShipmentTrendList());
                request.setAttribute("costTrendList", dashboard.getCostTrendList());

                request.setAttribute("productionTrendMax", dashboard.getProductionTrendMax());
                request.setAttribute("qualityTrendMax", dashboard.getQualityTrendMax());
                request.setAttribute("shipmentTrendMax", dashboard.getShipmentTrendMax());
                request.setAttribute("costTrendMax", dashboard.getCostTrendMax());

                request.setAttribute("topCostItemList", dashboard.getTopCostItemList());
            }

            System.out.println("baseDate = " + dashboard.getBaseDate());
            System.out.println("kpi = " + dashboard.getKpi());
            System.out.println("productionTrendList = " + dashboard.getProductionTrendList());
            System.out.println("costTrendList = " + dashboard.getCostTrendList());
            System.out.println("topCostItemList = " + dashboard.getTopCostItemList());
            
            request.getRequestDispatcher("/WEB-INF/views/table.jsp").forward(request, response);

        } catch (Exception e) {
            e.printStackTrace();
            throw new ServletException("CEO 대시보드 조회 중 오류가 발생했습니다.", e);
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
}