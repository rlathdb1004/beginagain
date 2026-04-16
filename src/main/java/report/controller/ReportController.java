package report.controller;

import java.io.IOException;
import java.sql.Date;
import java.time.LocalDate;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import report.dto.ReportDTO;
import report.service.ReportService;

@WebServlet("/report")
public class ReportController extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private ReportService reportService = new ReportService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        LocalDate today = LocalDate.now();
        LocalDate defaultStart = today.withDayOfYear(1);
        LocalDate defaultEnd = today;

        String startDateParam = request.getParameter("startDate");
        String endDateParam = request.getParameter("endDate");

        LocalDate startLocalDate = parseOrDefault(startDateParam, defaultStart);
        LocalDate endLocalDate = parseOrDefault(endDateParam, defaultEnd);

        if (startLocalDate.isAfter(endLocalDate)) {
            LocalDate temp = startLocalDate;
            startLocalDate = endLocalDate;
            endLocalDate = temp;
        }

        Date startDate = Date.valueOf(startLocalDate);
        Date endDate = Date.valueOf(endLocalDate);

        ReportDTO report = reportService.getReport(startDate, endDate);

        request.setAttribute("report", report);
        request.setAttribute("startDate", startLocalDate.toString());
        request.setAttribute("endDate", endLocalDate.toString());
        request.setAttribute("pageTitle", "리포트");
        request.setAttribute("pageSubTitle", "생산 · 품질 · 설비 데이터 기간별 분석 ");
        request.setAttribute("prodUnit", "kg");
        request.setAttribute("contentPage", "/WEB-INF/views/report/report.jsp");
        request.getRequestDispatcher("/WEB-INF/views/table.jsp").forward(request, response);
    }

    private LocalDate parseOrDefault(String value, LocalDate defaultValue) {
        try {
            if (value == null || "".equals(value.trim())) {
                return defaultValue;
            }
            return LocalDate.parse(value);
        } catch (Exception e) {
            return defaultValue;
        }
    }
}
