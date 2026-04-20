package MasterDataMgmt.DefectManagement.controller;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import MasterDataMgmt.DefectManagement.dto.DefectMgmtDTO;
import MasterDataMgmt.DefectManagement.dto.DefectMgmtSearchDTO;
import MasterDataMgmt.DefectManagement.service.DefectMgmtService;

@WebServlet("/defect-mgmt")
public class DefectMgmtCon extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("utf-8");
        response.setContentType("text/html; charset=utf-8");

        String searchType = request.getParameter("searchType");
        String keyword = request.getParameter("keyword");
        int page = parsePage(request.getParameter("page"));

        DefectMgmtSearchDTO dto = new DefectMgmtSearchDTO();
        dto.setSearchType(searchType);
        dto.setKeyword(keyword);
        dto.setPage(page);
        dto.setPageSize(10);

        DefectMgmtService service = new DefectMgmtService();
        int totalCount = service.getTotalCount(dto);
        int pageSize = dto.getPageSize();
        int totalPage = totalCount == 0 ? 1 : (int) Math.ceil((double) totalCount / pageSize);
        if (page > totalPage) {
            page = totalPage;
            dto.setPage(page);
        }

        List<DefectMgmtDTO> list = service.getList(dto);

        int pageBlock = 5;
        int startPage = ((page - 1) / pageBlock) * pageBlock + 1;
        int endPage = startPage + pageBlock - 1;
        if (endPage > totalPage) endPage = totalPage;

        Object errorMessage = request.getSession().getAttribute("errorMessage");
        if (errorMessage != null) {
            request.setAttribute("errorMessage", errorMessage);
            request.getSession().removeAttribute("errorMessage");
        }

        request.setAttribute("searchType", searchType);
        request.setAttribute("keyword", keyword);
        request.setAttribute("list", list);
        request.setAttribute("paCurrentPage", page);
        request.setAttribute("paTotalPage", totalPage);
        request.setAttribute("paStartPage", startPage);
        request.setAttribute("paEndPage", endPage);
        request.setAttribute("contentPage", "/WEB-INF/views/item/DefectMgmt.jsp");
        request.setAttribute("pageTitle", "불량 관리");
        request.setAttribute("pageSubTitle", "불량 코드 조회 및 등록");
        request.getRequestDispatcher("/WEB-INF/views/table.jsp").forward(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("utf-8");
        DefectMgmtDTO dto = new DefectMgmtDTO();
        String id = request.getParameter("defect_code_id");
        dto.setDefect_code(request.getParameter("defect_code"));
        dto.setDefect_name(request.getParameter("defect_name"));
        dto.setDefect_type(request.getParameter("defect_type"));
        dto.setDescription(request.getParameter("description"));
        dto.setUse_yn(request.getParameter("use_yn"));
        dto.setRemark(request.getParameter("remark"));
        DefectMgmtService service = new DefectMgmtService();
        try {
            if (id != null && !id.isEmpty()) { dto.setDefect_code_id(Integer.parseInt(id)); service.update(dto); }
            else service.insert(dto);
        } catch (RuntimeException e) {
            request.getSession().setAttribute("errorMessage", e.getMessage());
        }
        response.sendRedirect(request.getContextPath() + "/defect-mgmt" + buildQuery(request));
    }

    private int parsePage(String pageParam) {
        try {
            int page = Integer.parseInt(pageParam);
            return page < 1 ? 1 : page;
        } catch (Exception e) {
            return 1;
        }
    }

    private String buildQuery(HttpServletRequest request) throws IOException {
        String searchType = request.getParameter("searchType");
        String keyword = request.getParameter("keyword");
        String page = request.getParameter("page");

        StringBuilder sb = new StringBuilder();
        if ((searchType != null && !searchType.isEmpty()) || (keyword != null && !keyword.isEmpty()) || (page != null && !page.isEmpty())) {
            sb.append("?");
            boolean first = true;
            if (searchType != null && !searchType.isEmpty()) {
                sb.append("searchType=").append(URLEncoder.encode(searchType, "UTF-8"));
                first = false;
            }
            if (keyword != null && !keyword.isEmpty()) {
                if (!first) sb.append("&");
                sb.append("keyword=").append(URLEncoder.encode(keyword, "UTF-8"));
                first = false;
            }
            if (page != null && !page.isEmpty()) {
                if (!first) sb.append("&");
                sb.append("page=").append(URLEncoder.encode(page, "UTF-8"));
            }
        }
        return sb.toString();
    }
}
