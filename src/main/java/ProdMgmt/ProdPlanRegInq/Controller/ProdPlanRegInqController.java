package ProdMgmt.ProdPlanRegInq.Controller;

import java.io.IOException;
import java.net.URLEncoder;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import ProdMgmt.ProdPlanRegInq.DTO.ProdPlanRegInqDTO;
import ProdMgmt.ProdPlanRegInq.Service.ProdPlanRegInqService;

@WebServlet("/prodplan")
public class ProdPlanRegInqController extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("utf-8");
        response.setContentType("text/html;charset=utf-8");

        String seqNo = request.getParameter("seqNO");
        if (seqNo != null && !seqNo.trim().equals("")) {
            detail(request, response);
            return;
        }
        forwardList(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("utf-8");
        response.setContentType("text/html;charset=utf-8");

        String cmd = request.getParameter("cmd");
        if ("register".equals(cmd)) {
            register(request, response);
            return;
        }
        if ("update".equals(cmd)) {
            update(request, response);
            return;
        }
        if ("delete".equals(cmd)) {
            delete(request, response);
            return;
        }
        doGet(request, response);
    }

    private void forwardList(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        ProdPlanRegInqService service = new ProdPlanRegInqService();

        String searched = nvl(request.getParameter("searched"));
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
        List<ProdPlanRegInqDTO> list = new ArrayList<ProdPlanRegInqDTO>();
        list = service.getListByPage(startDate, endDate, searchType, keyword, startRow, endRow);

        int startPage = ((page - 1) / pageBlock) * pageBlock + 1;
        int endPage = startPage + pageBlock - 1;
        if (endPage > totalPage) endPage = totalPage;

        request.setAttribute("list", list);
        request.setAttribute("itemOptions", service.getFinishedItemOptions());
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
        request.setAttribute("searched", searched);
        request.setAttribute("pageTitle", "생산관리");
        request.setAttribute("pageSubTitle", "생산 계획 등록, 조회");
        request.setAttribute("contentPage", "/WEB-INF/views/ProdPlanRegInq.jsp");
        request.getRequestDispatcher("/WEB-INF/views/table.jsp").forward(request, response);
    }

    private void detail(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        int seqNo = Integer.parseInt(request.getParameter("seqNO"));
        ProdPlanRegInqService service = new ProdPlanRegInqService();
        ProdPlanRegInqDTO dto = service.getDetailById(seqNo);
        request.setAttribute("productionPlan", dto);
        request.setAttribute("itemOptions", service.getFinishedItemOptions());
        request.setAttribute("pageTitle", "생산관리");
        request.setAttribute("pageSubTitle", "생산 계획 상세");
        request.setAttribute("contentPage", "/WEB-INF/views/ProdPlanDetail.jsp");
        request.getRequestDispatcher("/WEB-INF/views/table.jsp").forward(request, response);
    }

    private void register(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        ProdPlanRegInqDTO dto = new ProdPlanRegInqDTO();
        dto.setItemId(parseInt(request.getParameter("itemId")));
        String planDate = request.getParameter("planDate");
        if (planDate != null && !planDate.trim().equals("")) dto.setPlanDate(Date.valueOf(planDate));
        dto.setPlanAmount(parseInt(request.getParameter("planAmount")));
        dto.setPlanLine(request.getParameter("planLine"));
        dto.setStatus(request.getParameter("status"));
        dto.setMemo(request.getParameter("memo"));

        try {
            new ProdPlanRegInqService().insert(dto);
            response.sendRedirect(request.getContextPath() + "/prodplan");
        } catch (Exception e) {
            alertBack(response, e.getMessage());
        }
    }

    private void update(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        ProdPlanRegInqDTO dto = new ProdPlanRegInqDTO();
        dto.setSeqNO(parseInt(request.getParameter("seqNO")));
        dto.setItemId(parseInt(request.getParameter("itemId")));
        String planDate = request.getParameter("planDate");
        if (planDate != null && !planDate.trim().equals("")) dto.setPlanDate(Date.valueOf(planDate));
        dto.setPlanAmount(parseInt(request.getParameter("planAmount")));
        dto.setPlanLine(request.getParameter("planLine"));
        dto.setStatus(request.getParameter("status"));
        dto.setMemo(request.getParameter("memo"));

        try {
            new ProdPlanRegInqService().update(dto);
            response.sendRedirect(request.getContextPath() + "/prodplan?seqNO=" + dto.getSeqNO());
        } catch (Exception e) {
            alertBack(response, e.getMessage());
        }
    }

    private void delete(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        ProdPlanRegInqService service = new ProdPlanRegInqService();
        String[] seqNos = request.getParameterValues("seqNO");
        try {
            service.deleteByIds(seqNos);
        } catch (Exception e) {
            alertBack(response, e.getMessage());
            return;
        }

        String page = nvl(request.getParameter("page"));
        String searched = nvl(request.getParameter("searched"));
        String startDate = nvl(request.getParameter("startDate"));
        String endDate = nvl(request.getParameter("endDate"));
        String searchType = nvl(request.getParameter("searchType"));
        String keyword = nvl(request.getParameter("keyword"));

        String redirectUrl = request.getContextPath() + "/prodplan"
                + "?searched=" + URLEncoder.encode(searched, "UTF-8")
                + "&page=" + URLEncoder.encode(page, "UTF-8")
                + "&startDate=" + URLEncoder.encode(startDate, "UTF-8")
                + "&endDate=" + URLEncoder.encode(endDate, "UTF-8")
                + "&searchType=" + URLEncoder.encode(searchType, "UTF-8")
                + "&keyword=" + URLEncoder.encode(keyword, "UTF-8");
        response.sendRedirect(redirectUrl);
    }

    private void alertBack(HttpServletResponse response, String message) throws IOException {
        response.setContentType("text/html; charset=UTF-8");
        response.getWriter().write("<script>alert('" + escapeJs(message) + "');history.back();</script>");
    }

    private String escapeJs(String str) {
        if (str == null) return "오류가 발생했습니다.";
        return str.replace("\\", "\\\\").replace("'", "\\'").replace("\r", " ").replace("\n", " ");
    }

    private int parseInt(String str) {
        try { return Integer.parseInt(str); } catch (Exception e) { return 0; }
    }

    private String nvl(String str) {
        return str == null ? "" : str;
    }
}
