package ProdPerfRegWorker;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import ProdMgmt.ProdPerfRegInq.DTO.ProdPerfRegInqDTO;
import ProdMgmt.ProdPerfRegInq.Service.ProdPerfRegInqService;
import member.dto.MemberDTO;

@WebServlet("/prod/worker")
public class ProdPerfRegWorkerController extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private final ProdPerfRegInqService service = new ProdPerfRegInqService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("utf-8");
        response.setContentType("text/html;charset=utf-8");

        HttpSession session = request.getSession(false);
        MemberDTO loginUser = session == null ? null : (MemberDTO) session.getAttribute("loginUser");

        if (loginUser == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        List<ProdPerfRegInqDTO> workOrderOptions = service.getWorkOrderOptionsByEmpId(loginUser.getEmpId());

        request.setAttribute("errorMsg", session.getAttribute("errorMsg"));
        session.removeAttribute("errorMsg");
        request.setAttribute("workOrderOptions", workOrderOptions);
        request.setAttribute("pageTitle", "작업자 메인");
        request.setAttribute("pageSubTitle", "내 작업 생산 실적 등록");
        request.setAttribute("contentPage", "/WEB-INF/views/worker.jsp");

        request.getRequestDispatcher("/WEB-INF/views/table.jsp").forward(request, response);
    }
}
