package downtime.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import downtime.dto.DowntimeDTO;
import downtime.service.DowntimeService;

@WebServlet("/downtime/detail")
public class DowntimeDetailController extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private DowntimeService downtimeService = new DowntimeService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String failureActionIdStr = request.getParameter("failureActionId");

        if (failureActionIdStr == null || "".equals(failureActionIdStr.trim())) {
            response.sendRedirect(request.getContextPath() + "/downtime/list");
            return;
        }

        int failureActionId = Integer.parseInt(failureActionIdStr);
        DowntimeDTO downtime = downtimeService.getDowntimeById(failureActionId);

        if (downtime == null) {
            response.sendRedirect(request.getContextPath() + "/downtime/list");
            return;
        }

        request.setAttribute("downtime", downtime);
        request.setAttribute("pageTitle", "설비운영");
        request.setAttribute("pageSubTitle", "비가동 현황 상세");
        request.setAttribute("contentPage", "/WEB-INF/views/downtime/downtimeDetail.jsp");
        request.getRequestDispatcher("/WEB-INF/views/table.jsp").forward(request, response);
    }
}