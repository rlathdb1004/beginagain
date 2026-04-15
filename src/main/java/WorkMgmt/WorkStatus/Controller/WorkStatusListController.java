package WorkMgmt.WorkStatus.Controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import WorkMgmt.WorkStatus.DTO.*;
import WorkMgmt.WorkStatus.Service.*;

@WebServlet("/workstatus")
public class WorkStatusListController extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private WorkStatusService workStatusService = new WorkStatusService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<WorkStatusDTO> workStatusList = workStatusService.getWorkStatusList();
        request.setAttribute("workStatusList", workStatusList);
        request.setAttribute("pageTitle", "작업관리");
        request.setAttribute("pageSubTitle", "작업 현황 조회");
        request.setAttribute("contentPage", "/WEB-INF/views/workStatusList.jsp");
        request.getRequestDispatcher("/WEB-INF/views/table.jsp").forward(request, response);
    }
}
