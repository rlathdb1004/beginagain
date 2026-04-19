package WorkMgmt.WORegInq.Controller;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import WorkMgmt.WORegInq.DTO.WORegInqDTO;
import WorkMgmt.WORegInq.Service.WORegInqService;

@WebServlet("/woreginq")
public class WORegInqController extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("utf-8");
        response.setContentType("text/html;charset=utf-8");

        WORegInqService service = new WORegInqService();

        String startDate = nvl(request.getParameter("startDate"));
        String endDate = nvl(request.getParameter("endDate"));
        String searchType = nvl(request.getParameter("searchType"));
        String keyword = nvl(request.getParameter("keyword"));

        int page = 1;
        int pageSize = 10;
        int pageBlock = 5;

        String pageParam = request.getParameter("page");
        if (pageParam != null && !pageParam.trim().equals("")) {
            try { page = Integer.parseInt(pageParam); } catch (Exception e) { page = 1; }
        }

        int totalCount = service.getTotalCount(startDate, endDate, searchType, keyword);
        int totalPage = (int) Math.ceil((double) totalCount / pageSize);
        if (totalPage == 0) totalPage = 1;
        if (page < 1) page = 1;
        if (page > totalPage) page = totalPage;

        int startRow = (page - 1) * pageSize + 1;
        int endRow = page * pageSize;
        List<WORegInqDTO> list = new ArrayList<WORegInqDTO>();
        list = service.getListByPage(startDate, endDate, searchType, keyword, startRow, endRow);

        int startPage = ((page - 1) / pageBlock) * pageBlock + 1;
        int endPage = startPage + pageBlock - 1;
        if (endPage > totalPage) endPage = totalPage;

        HttpSession session = request.getSession();
        request.setAttribute("errorMsg", session.getAttribute("errorMsg"));
        session.removeAttribute("errorMsg");

        request.setAttribute("list", list);
        request.setAttribute("planOptions", service.getPlanOptions());
        request.setAttribute("empOptions", service.getEmpOptions());
        request.setAttribute("page", page);
        request.setAttribute("pageSize", pageSize);
        request.setAttribute("totalCount", totalCount);
        request.setAttribute("totalPage", totalPage);
        request.setAttribute("startPage", startPage);
        request.setAttribute("endPage", endPage);

        request.setAttribute("paCurrentPage", page);
        request.setAttribute("paPageSize", pageSize);
        request.setAttribute("paBlockSize", pageBlock);
        request.setAttribute("paTotalCount", totalCount);
        request.setAttribute("paTotalPage", totalPage);
        request.setAttribute("paStartPage", startPage);
        request.setAttribute("paEndPage", endPage);

        request.setAttribute("pageTitle", "작업관리");
        request.setAttribute("pageSubTitle", "작업 지시 등록, 조회");
        request.setAttribute("contentPage", "/WEB-INF/views/WORegInq.jsp");

        request.getRequestDispatcher("/WEB-INF/views/table.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("utf-8");
        response.setContentType("text/html;charset=utf-8");

        WORegInqService service = new WORegInqService();
        String cmd = request.getParameter("cmd");

        if ("delete".equals(cmd)) {
            String[] seqNos = request.getParameterValues("seqNO");
            try {
                service.deleteByIds(seqNos);
            } catch (IllegalArgumentException e) {
                request.getSession().setAttribute("errorMsg", e.getMessage());
            }

            String page = nvl(request.getParameter("page"));
            String searched = nvl(request.getParameter("searched"));
            String startDate = nvl(request.getParameter("startDate"));
            String endDate = nvl(request.getParameter("endDate"));
            String searchType = nvl(request.getParameter("searchType"));
            String keyword = nvl(request.getParameter("keyword"));

            String redirectUrl = request.getContextPath() + "/woreginq"
                    + "?searched=" + URLEncoder.encode(searched, "UTF-8")
                    + "&page=" + URLEncoder.encode(page, "UTF-8")
                    + "&startDate=" + URLEncoder.encode(startDate, "UTF-8")
                    + "&endDate=" + URLEncoder.encode(endDate, "UTF-8")
                    + "&searchType=" + URLEncoder.encode(searchType, "UTF-8")
                    + "&keyword=" + URLEncoder.encode(keyword, "UTF-8");
            response.sendRedirect(redirectUrl);
            return;
        }

        doGet(request, response);
    }

    private String nvl(String str) {
        return str == null ? "" : str;
    }
}
