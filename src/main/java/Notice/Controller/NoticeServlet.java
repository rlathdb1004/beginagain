package Notice.Controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import Notice.DTO.NoticeDTO;
import Notice.Service.NoticeService;

/*
 * 공지사항 Servlet
 * - 목록 / 상세 / 등록 / 수정 / 내리기 / 다시게시 / 삭제 처리
 */
@WebServlet("/notice")
public class NoticeServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private NoticeService noNoticeService = new NoticeService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/html; charset=UTF-8");

        String noAction = request.getParameter("action");

        if (noAction == null || "".equals(noAction) || "list".equals(noAction)) {
            noNoticeList(request, response);
            return;
        }

        if ("detail".equals(noAction)) {
            noNoticeDetail(request, response);
            return;
        }

        noNoticeList(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/html; charset=UTF-8");

        String noAction = request.getParameter("action");

        if ("create".equals(noAction)) {
            noCreateNotice(request, response);
            return;
        }

        if ("update".equals(noAction)) {
            noUpdateNotice(request, response);
            return;
        }

        if ("hide".equals(noAction)) {
            noHideNotice(request, response);
            return;
        }

        if ("restore".equals(noAction)) {
            noRestoreNotice(request, response);
            return;
        }

        if ("delete".equals(noAction)) {
            noDeleteNotice(request, response);
            return;
        }

        doGet(request, response);
    }

    /*
     * 공지사항 목록 조회
     */
    private void noNoticeList(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            noForwardNoticePage(request, response, null);
        } catch (Exception e) {
            e.printStackTrace();
            throw new ServletException("공지사항 목록 조회 중 오류가 발생했습니다.");
        }
    }

    /*
     * 공지사항 상세 조회
     * - 조회수 증가 후 상세정보 세팅
     */
    private void noNoticeDetail(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            int noNoticeId = noParseInt(request.getParameter("noticeId"), 0);

            if (noNoticeId <= 0) {
                noForwardNoticePage(request, response, null);
                return;
            }

            noNoticeService.increaseViewCount(noNoticeId);

            NoticeDTO noDetailNotice = noNoticeService.getNoticeDetail(noNoticeId);

            noForwardNoticePage(request, response, noDetailNotice);

        } catch (Exception e) {
            e.printStackTrace();
            throw new ServletException("공지사항 상세 조회 중 오류가 발생했습니다.");
        }
    }

    /*
     * 공지사항 등록
     */
    private void noCreateNotice(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            NoticeDTO noDto = new NoticeDTO();

            noDto.setTitle(request.getParameter("title"));
            noDto.setContent(request.getParameter("content"));
            noDto.setStatus(request.getParameter("status"));
            noDto.setRemark(request.getParameter("remark"));

            /*
             * 로그인 세션 키가 프로젝트마다 다를 수 있으므로
             * 기존 로그인에서 쓰는 세션 키에 맞게 이 helper만 맞추면 됨
             */
            noDto.setWriterId(noGetLoginEmpId(request));

            noNoticeService.insertNotice(noDto);

            response.sendRedirect(request.getContextPath() + "/notice?action=list#noNoticeSection");

        } catch (Exception e) {
            e.printStackTrace();
            throw new ServletException("공지사항 등록 중 오류가 발생했습니다.");
        }
    }

    /*
     * 공지사항 수정
     */
    private void noUpdateNotice(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            NoticeDTO noDto = new NoticeDTO();

            noDto.setNoticeId(noParseInt(request.getParameter("noticeId"), 0));
            noDto.setTitle(request.getParameter("title"));
            noDto.setContent(request.getParameter("content"));
            noDto.setStatus(request.getParameter("status"));
            noDto.setRemark(request.getParameter("remark"));

            noNoticeService.updateNotice(noDto);

            response.sendRedirect(request.getContextPath() + "/notice?action=list#noNoticeSection");

        } catch (Exception e) {
            e.printStackTrace();
            throw new ServletException("공지사항 수정 중 오류가 발생했습니다.");
        }
    }

    /*
     * 공지 내리기
     */
    private void noHideNotice(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            int noNoticeId = noParseInt(request.getParameter("noticeId"), 0);
            noNoticeService.hideNotice(noNoticeId);

            response.sendRedirect(request.getContextPath() + "/notice?action=list#noNoticeSection");

        } catch (Exception e) {
            e.printStackTrace();
            throw new ServletException("공지 내리기 중 오류가 발생했습니다.");
        }
    }

    /*
     * 공지 다시 게시
     */
    private void noRestoreNotice(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            int noNoticeId = noParseInt(request.getParameter("noticeId"), 0);
            noNoticeService.restoreNotice(noNoticeId);

            response.sendRedirect(request.getContextPath() + "/notice?action=list#noNoticeSection");

        } catch (Exception e) {
            e.printStackTrace();
            throw new ServletException("공지 다시 게시 중 오류가 발생했습니다.");
        }
    }

    /*
     * 공지 삭제
     */
    private void noDeleteNotice(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            int noNoticeId = noParseInt(request.getParameter("noticeId"), 0);
            noNoticeService.deleteNotice(noNoticeId);

            response.sendRedirect(request.getContextPath() + "/notice?action=list#noNoticeSection");

        } catch (Exception e) {
            e.printStackTrace();
            throw new ServletException("공지 삭제 중 오류가 발생했습니다.");
        }
    }

    /*
     * 공지사항 공통 forward
     * - 총 건수 / 검색 / 상태필터 / 페이징 / 상세모달 데이터 세팅
     */
    private void noForwardNoticePage(HttpServletRequest request, HttpServletResponse response, NoticeDTO noDetailNotice)
            throws ServletException, IOException {

        String noSearchType = request.getParameter("searchType");
        String noKeyword = request.getParameter("keyword");
        String noStatusFilter = request.getParameter("statusFilter");
        int noCurrentPage = noParseInt(request.getParameter("page"), 1);

        if (noSearchType == null || "".equals(noSearchType.trim())) {
            noSearchType = "all";
        }

        if (noKeyword == null) {
            noKeyword = "";
        }

        if (noStatusFilter == null || "".equals(noStatusFilter.trim())) {
            noStatusFilter = "all";
        }

        int noPageSize = 10;
        int noBlockSize = 5;

        int noTotalCount = noNoticeService.getNoticeCount(noSearchType, noKeyword, noStatusFilter);
        int noTotalPage = (int) Math.ceil((double) noTotalCount / noPageSize);

        if (noTotalPage < 1) {
            noTotalPage = 1;
        }

        if (noCurrentPage < 1) {
            noCurrentPage = 1;
        }

        if (noCurrentPage > noTotalPage) {
            noCurrentPage = noTotalPage;
        }

        int noStartRow = (noCurrentPage - 1) * noPageSize + 1;
        int noEndRow = noCurrentPage * noPageSize;

        int noStartPage = ((noCurrentPage - 1) / noBlockSize) * noBlockSize + 1;
        int noEndPage = noStartPage + noBlockSize - 1;

        if (noEndPage > noTotalPage) {
            noEndPage = noTotalPage;
        }

        List<NoticeDTO> noNoticeList = noNoticeService.getNoticeList(noStartRow, noEndRow, noSearchType, noKeyword, noStatusFilter);

        int noStartNo = noTotalCount - ((noCurrentPage - 1) * noPageSize);

        request.setAttribute("noNoticeList", noNoticeList);
        request.setAttribute("noCurrentPage", noCurrentPage);
        request.setAttribute("noPageSize", noPageSize);
        request.setAttribute("noBlockSize", noBlockSize);
        request.setAttribute("noTotalCount", noTotalCount);
        request.setAttribute("noTotalPage", noTotalPage);
        request.setAttribute("noStartPage", noStartPage);
        request.setAttribute("noEndPage", noEndPage);
        request.setAttribute("noSearchType", noSearchType);
        request.setAttribute("noKeyword", noKeyword);
        request.setAttribute("noStatusFilter", noStatusFilter);
        request.setAttribute("noStartNo", noStartNo);

        if (noDetailNotice != null) {
            request.setAttribute("noDetailNotice", noDetailNotice);
        }
        
        request.setAttribute("pageTitle", "공지사항 게시판");
        request.setAttribute("contentPage", "/WEB-INF/views/Notice.jsp");
        request.getRequestDispatcher("/WEB-INF/views/table.jsp").forward(request, response);
    }

    /*
     * 숫자 파싱
     */
    private int noParseInt(String noValue, int noDefaultValue) {
        try {
            return Integer.parseInt(noValue);
        } catch (Exception e) {
            return noDefaultValue;
        }
    }

    /*
     * 로그인 사용자 사번 가져오기
     * - 기존 로그인 세션명에 맞게 이 부분만 수정하면 됨
     */
    private int noGetLoginEmpId(HttpServletRequest request) {

        Object noLoginEmpId = request.getSession().getAttribute("loginEmpId");

        if (noLoginEmpId == null) {
            noLoginEmpId = request.getSession().getAttribute("empId");
        }

        // 추가: loginUser 객체에서 empId 꺼내기
        if (noLoginEmpId == null) {
            Object noLoginUser = request.getSession().getAttribute("loginUser");
            if (noLoginUser != null) {
                try {
                    java.lang.reflect.Method method = noLoginUser.getClass().getMethod("getEmpId");
                    Object value = method.invoke(noLoginUser);
                    if (value instanceof Integer) {
                        return (Integer) value;
                    }
                    return noParseInt(String.valueOf(value), 0);
                } catch (Exception e) {
                    // 무시하고 아래로 진행
                }
            }
        }

        if (noLoginEmpId == null) {
            String noWriterEmpId = request.getParameter("writerEmpId");
            return noParseInt(noWriterEmpId, 0);
        }

        if (noLoginEmpId instanceof Integer) {
            return (Integer) noLoginEmpId;
        }

        return noParseInt(String.valueOf(noLoginEmpId), 0);
    }
}