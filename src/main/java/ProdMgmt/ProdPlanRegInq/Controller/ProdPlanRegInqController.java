package ProdMgmt.ProdPlanRegInq.Controller;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import ProdMgmt.ProdPlanRegInq.DTO.ProdPlanRegInqDTO;
import ProdMgmt.ProdPlanRegInq.Service.ProdPlanRegInqService;

/*
 * 생산계획 등록/조회 Controller
 *
 * URL
 * - /prodplan
 *
 * 기능
 * - GET  : 목록 조회 / 검색 / 페이징
 * - POST : 삭제 처리
 */
@WebServlet("/prodplan")
public class ProdPlanRegInqController extends HttpServlet {
    private static final long serialVersionUID = 1L;

    /*
     * 목록 조회 + 검색 + 페이징 처리
     *
     * searched=Y 일 때만 실제 목록을 조회한다.
     * 처음 진입했을 때는 헤더만 보이고 데이터는 안 보이게 하기 위함이다.
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // 한글 처리
        request.setCharacterEncoding("utf-8");
        response.setContentType("text/html;charset=utf-8");

        ProdPlanRegInqService service = new ProdPlanRegInqService();

        // 검색 버튼 눌렀는지 여부
        String searched = nvl(request.getParameter("searched"));

        // 검색 조건
        String startDate = nvl(request.getParameter("startDate"));
        String endDate = nvl(request.getParameter("endDate"));
        String searchType = nvl(request.getParameter("searchType"));
        String keyword = nvl(request.getParameter("keyword"));

        // 페이징 기본값
        int page = 1;
        int pageSize = 10;   // 한 페이지당 10건
        int pageBlock = 5;   // 페이지 번호 5개씩 묶음

        String pageParam = request.getParameter("page");
        if (pageParam != null && !pageParam.trim().equals("")) {
            try {
                page = Integer.parseInt(pageParam);
            } catch (Exception e) {
                page = 1;
            }
        }

        // JSP에 넘길 기본값
        int totalCount = 0;
        int totalPage = 1;
        int startPage = 1;
        int endPage = 1;
        List<ProdPlanRegInqDTO> list = new ArrayList<>();

        /*
         * 검색 버튼 눌렀을 때만 실제 조회
         *
         * 이유:
         * - 처음 들어왔을 때는 데이터 없이 헤더만 보여주기 위함
         */
        if ("Y".equals(searched)) {

            // 검색 조건이 반영된 전체 건수 조회
            totalCount = service.getTotalCount(startDate, endDate, searchType, keyword);

            // 전체 페이지 수 계산
            totalPage = (int) Math.ceil((double) totalCount / pageSize);

            // 데이터가 없어도 최소 1페이지로 보정
            if (totalPage == 0) {
                totalPage = 1;
            }

            // 현재 페이지 범위 보정
            if (page < 1) {
                page = 1;
            }
            if (page > totalPage) {
                page = totalPage;
            }

            // 현재 페이지의 시작/끝 row 계산
            int startRow = (page - 1) * pageSize + 1;
            int endRow = page * pageSize;

            // 페이지별 목록 조회
            list = service.getListByPage(startDate, endDate, searchType, keyword, startRow, endRow);

            // 하단 페이지 번호 시작값 계산
            startPage = ((page - 1) / pageBlock) * pageBlock + 1;

            // 하단 페이지 번호 끝값 계산
            endPage = startPage + pageBlock - 1;

            // 마지막 페이지 보정
            if (endPage > totalPage) {
                endPage = totalPage;
            }
        }

        // JSP에 데이터 전달
        request.setAttribute("list", list);

        request.setAttribute("page", page);
        request.setAttribute("pageSize", pageSize);
        request.setAttribute("totalCount", totalCount);
        request.setAttribute("totalPage", totalPage);
        request.setAttribute("startPage", startPage);
        request.setAttribute("endPage", endPage);
        
        //페이징을 위해 아래에 추가함 / 령
        request.setAttribute("paCurrentPage", page);
        request.setAttribute("paPageSize", pageSize);
        request.setAttribute("paBlockSize", pageBlock);
        request.setAttribute("paTotalCount", totalCount);
        request.setAttribute("paTotalPage", totalPage);
        request.setAttribute("paStartPage", startPage);
        request.setAttribute("paEndPage", endPage);

        // 공통 레이아웃 제목
        request.setAttribute("pageTitle", "생산관리");
        request.setAttribute("pageSubTitle", "생산 계획 등록/조회");

        // table.jsp 안에서 include 할 실제 본문 JSP
        request.setAttribute("contentPage", "/WEB-INF/views/ProdPlanRegInq.jsp");

        // 공통 레이아웃으로 이동
        request.getRequestDispatcher("/WEB-INF/views/table.jsp").forward(request, response);
    }

    /*
     * 삭제 처리
     *
     * 현재는 cmd=delete 만 처리한다.
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // 한글 처리
        request.setCharacterEncoding("utf-8");
        response.setContentType("text/html;charset=utf-8");

        ProdPlanRegInqService service = new ProdPlanRegInqService();

        // 어떤 POST 동작인지 구분
        String cmd = request.getParameter("cmd");

        /*
         * 삭제 처리
         *
         * JSP에서 체크박스 name="seqNO" 로 넘어온 PLAN_ID 배열을 받아
         * DAO에서 USE_YN='N' 으로 논리삭제한다.
         */
        if ("delete".equals(cmd)) {
            String[] seqNos = request.getParameterValues("seqNO");

            // 논리삭제 실행
            service.deleteByIds(seqNos);

            /*
             * 삭제 후에도 기존 검색조건 / 페이지 상태를 유지하기 위해
             * 다시 redirect 할 URL을 만들어준다.
             */
            String page = nvl(request.getParameter("page"));
            String searched = nvl(request.getParameter("searched"));
            String startDate = nvl(request.getParameter("startDate"));
            String endDate = nvl(request.getParameter("endDate"));
            String searchType = nvl(request.getParameter("searchType"));
            String keyword = nvl(request.getParameter("keyword"));

            String redirectUrl =
                    request.getContextPath() + "/prodplan"
                    + "?searched=" + URLEncoder.encode(searched, "UTF-8")
                    + "&page=" + URLEncoder.encode(page, "UTF-8")
                    + "&startDate=" + URLEncoder.encode(startDate, "UTF-8")
                    + "&endDate=" + URLEncoder.encode(endDate, "UTF-8")
                    + "&searchType=" + URLEncoder.encode(searchType, "UTF-8")
                    + "&keyword=" + URLEncoder.encode(keyword, "UTF-8");

            response.sendRedirect(redirectUrl);
            return;
        }

        /*
         * 다른 POST 요청이 들어오면 일단 목록 화면으로 다시 보냄
         */
        doGet(request, response);
    }

    /*
     * null 방지용 공통 메서드
     *
     * request.getParameter() 값이 null이면 "" 로 바꿔준다.
     */
    private String nvl(String str) {
        return str == null ? "" : str;
    }
}