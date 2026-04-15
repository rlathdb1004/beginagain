package WorkMgmt.WorkStatusInq.Controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import WorkMgmt.WorkStatusInq.DTO.WorkStatusInqDTO;
import WorkMgmt.WorkStatusInq.DTO.WorkStatusLineProgressDTO;
import WorkMgmt.WorkStatusInq.Service.WorkStatusInqService;

/*
 * 작업 현황 조회 Controller
 *
 * URL
 * - /workstatusinq
 *
 * 기능
 * - GET  : 목록 조회 / 검색 / 페이징 / 그래프 데이터 조회
 * - POST : 현재는 조회 화면이므로 doGet으로 위임
 *
 * 화면 동작 방식
 * - 처음 진입했을 때는 searched 파라미터가 없으므로
 *   검색창 + 테이블 헤더만 보이게 한다.
 * - 검색 버튼을 누르면 searched=Y 로 들어오고
 *   그때 실제 목록과 그래프 데이터를 조회한다.
 */
@WebServlet("/workstatusinq")
public class WorkStatusInqController extends HttpServlet {
    private static final long serialVersionUID = 1L;

    /*
     * 목록 조회 + 검색 + 페이징 + 그래프 데이터 처리
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // 한글 처리
        request.setCharacterEncoding("utf-8");
        response.setContentType("text/html;charset=utf-8");

        // Service 객체 생성
        WorkStatusInqService service = new WorkStatusInqService();

        // 검색 버튼을 눌렀는지 여부
        // searched=Y 일 때만 실제 목록 조회
        String searched = nvl(request.getParameter("searched"));

        // 검색 조건
        String startDate = nvl(request.getParameter("startDate"));
        String endDate = nvl(request.getParameter("endDate"));
        String searchType = nvl(request.getParameter("searchType"));
        String keyword = nvl(request.getParameter("keyword"));

        // 페이징 기본값
        int page = 1;       // 현재 페이지
        int pageSize = 10;  // 한 페이지당 10건
        int pageBlock = 5;  // 페이지 번호 5개씩 묶음

        // page 파라미터 받기
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

        // 테이블 목록
        List<WorkStatusInqDTO> list = new ArrayList<>();

        // 상단 그래프 목록
        List<WorkStatusLineProgressDTO> lineProgressList = new ArrayList<>();

        /*
         * 검색 버튼 눌렀을 때만 실제 조회
         *
         * 이유
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

            // 현재 페이지 보정
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

            // 라인별 공정 진행도 조회
            lineProgressList = service.getLineProgressList(startDate, endDate, searchType, keyword);

            // 하단 페이지 번호 시작값 계산
            startPage = ((page - 1) / pageBlock) * pageBlock + 1;

            // 하단 페이지 번호 끝값 계산
            endPage = startPage + pageBlock - 1;

            // 마지막 페이지 보정
            if (endPage > totalPage) {
                endPage = totalPage;
            }
        }

        /*
         * JSP에 데이터 전달
         */
        request.setAttribute("list", list);
        request.setAttribute("lineProgressList", lineProgressList);

        request.setAttribute("page", page);
        request.setAttribute("pageSize", pageSize);
        request.setAttribute("totalCount", totalCount);
        request.setAttribute("totalPage", totalPage);
        request.setAttribute("startPage", startPage);
        request.setAttribute("endPage", endPage);

        // 공통 레이아웃용 제목
        request.setAttribute("pageTitle", "작업관리");
        request.setAttribute("pageSubTitle", "작업 현황 조회");

        // table.jsp 안에 include 될 본문 JSP
        request.setAttribute("contentPage", "/WEB-INF/views/WorkStatusInq.jsp");

        // 공통 레이아웃으로 이동
        request.getRequestDispatcher("/WEB-INF/views/table.jsp").forward(request, response);
    }

    /*
     * 작업 현황 조회 화면은 현재 별도의 등록/삭제 POST 기능이 없으므로
     * POST 요청이 와도 목록 조회로 처리한다.
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
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