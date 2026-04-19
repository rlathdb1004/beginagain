package MasterDataMgmt.DefectManagement.controller;

import java.io.IOException;
import java.net.URLEncoder;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import MasterDataMgmt.DefectManagement.service.DefectMgmtService;

@WebServlet("/defect-mgmt-del")
public class DefectMgmtDeleteCon extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("utf-8");
        String[] ids = request.getParameterValues("defect_code_id");
        DefectMgmtService service = new DefectMgmtService();
        try {
            if (ids != null) {
                for (String id : ids) service.delete(Integer.parseInt(id));
            } else {
                request.getSession().setAttribute("errorMessage", "삭제할 불량코드를 선택하세요.");
            }
        } catch (RuntimeException e) {
            request.getSession().setAttribute("errorMessage", e.getMessage());
        }
        response.sendRedirect(request.getContextPath() + "/defect-mgmt" + buildQuery(request));
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
