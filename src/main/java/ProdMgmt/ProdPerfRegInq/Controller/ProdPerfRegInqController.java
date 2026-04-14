package ProdMgmt.ProdPerfRegInq.Controller;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import ProdMgmt.ProdPerfRegInq.DTO.ProdPerfRegInqDTO;
import ProdMgmt.ProdPerfRegInq.Service.ProdPerfRegInqService;

@WebServlet("/prodresult")
public class ProdPerfRegInqController extends HttpServlet {
    private static final long serialVersionUID = 1L;

    // 목록 조회 + 검색 + 페이징 처리
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // 한글 처리
        request.setCharacterEncoding("utf-8");
        response.setContentType("text/html;charset=utf-8");

        ProdPerfRegInqService service = new ProdPerfRegInqService();

        // 검색 버튼 눌렀는지 여부
        // searched=Y 일 때만 실제 조회
        String searched = nvl(request.getParameter("searched"));

        // 검색 조건 받기
        String startDate = nvl(request.getParameter("startDate"));
        String endDate = nvl(request.getParameter("endDate"));
        String searchType = nvl(request.getParameter("searchType"));
        String keyword = nvl(request.getParameter("keyword"));

        // 페이징 기본값
        int page = 1;
        int pageSize = 10;   // 한 페이지당 10개
        int pageBlock = 5;   // 페이지 번호 5개씩 묶음

        String pageParam = request.getParameter("page");
        if (pageParam != null && !pageParam.trim().equals("")) {
            try {
                page = Integer.parseInt(pageParam);
            } catch (Exception e) {
                page = 1;
            }
        }

        // 초기화
        int totalCount = 0;
        int totalPage = 1;
        int startPage = 1;
        int endPage = 1;
        List<ProdPerfRegInqDTO> list = new ArrayList<>();

        // searched=Y 일 때만 실제 조회
        if ("Y".equals(searched)) {
            totalCount = service.getTotalCount(startDate, endDate, searchType, keyword);
            totalPage = (int) Math.ceil((double) totalCount / pageSize);

            if (totalPage == 0) {
                totalPage = 1;
            }

            if (page < 1) {
                page = 1;
            }
            if (page > totalPage) {
                page = totalPage;
            }

            // 현재 페이지 범위 계산
            int startRow = (page - 1) * pageSize + 1;
            int endRow = page * pageSize;

            // 목록 조회
            list = service.getListByPage(startDate, endDate, searchType, keyword, startRow, endRow);

            // 하단 페이지 번호 계산
            startPage = ((page - 1) / pageBlock) * pageBlock + 1;
            endPage = startPage + pageBlock - 1;

            if (endPage > totalPage) {
                endPage = totalPage;
            }
        }

        // JSP에 전달
        request.setAttribute("list", list);
        request.setAttribute("page", page);
        request.setAttribute("pageSize", pageSize);
        request.setAttribute("totalCount", totalCount);
        request.setAttribute("totalPage", totalPage);
        request.setAttribute("startPage", startPage);
        request.setAttribute("endPage", endPage);

        // 공통 레이아웃 타이틀
        request.setAttribute("pageTitle", "생산관리");
        request.setAttribute("pageSubTitle", "생산실적 등록/조회");

        // table.jsp 안에 include 될 본문 JSP
        request.setAttribute("contentPage", "/WEB-INF/views/ProdPerfRegInq.jsp");

        request.getRequestDispatcher("/WEB-INF/views/table.jsp").forward(request, response);
    }

    // 삭제 처리
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("utf-8");
        response.setContentType("text/html;charset=utf-8");

        ProdPerfRegInqService service = new ProdPerfRegInqService();
        String cmd = request.getParameter("cmd");

        // 삭제 버튼으로 들어온 경우
        if ("delete".equals(cmd)) {
            // 체크된 RESULT_ID 목록
            String[] seqNos = request.getParameterValues("seqNO");

            // 논리삭제 실행
            service.deleteByIds(seqNos);

            // 삭제 후에도 검색조건/페이지 유지
            String page = nvl(request.getParameter("page"));
            String searched = nvl(request.getParameter("searched"));
            String startDate = nvl(request.getParameter("startDate"));
            String endDate = nvl(request.getParameter("endDate"));
            String searchType = nvl(request.getParameter("searchType"));
            String keyword = nvl(request.getParameter("keyword"));

            String redirectUrl =
                    request.getContextPath() + "/prodresult"
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

    // null 방지용
    private String nvl(String str) {
        return str == null ? "" : str;
    }
}