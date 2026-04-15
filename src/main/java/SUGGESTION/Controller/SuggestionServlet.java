package SUGGESTION.Controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import ANSWER.DTO.AnswerDTO;
import ANSWER.Service.AnswerService;
import SUGGESTION.DTO.SuggestionDTO;
import SUGGESTION.Service.SuggestionService;

/**
 * 건의사항 Servlet
 *
 * [핵심 구조]
 * 1. JSP 파일은 Suggestion.jsp 하나만 사용
 * 2. mode 값으로 화면 분기
 *    - list   : 목록
 *    - write  : 등록 모달 열기
 *    - detail : 상세 화면 보기
 *
 * 3. modal 값으로 상세 화면 안에서 모달 분기
 *    - suggestionEdit   : 게시글 수정 모달
 *    - suggestionDelete : 게시글 삭제 확인 모달
 *    - answerWrite      : 답글 등록 모달
 *    - answerEdit       : 답글 수정 모달
 *    - answerDelete     : 답글 삭제 확인 모달
 *
 * 4. 권한 정책
 *    - 관리자 : 모든 게시글 / 답글 수정, 삭제 가능
 *    - 일반 사용자 : 본인 게시글 / 본인 답글만 수정, 삭제 가능
 */
@WebServlet("/suggestion/*")
public class SuggestionServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    /** 건의사항 서비스 */
    private final SuggestionService suggestionService = new SuggestionService();

    /** 답글 서비스 */
    private final AnswerService answerService = new AnswerService();

    /**
     * GET 요청
     * - 목록 / 등록 모달 / 상세 화면 모두 같은 JSP로 처리
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        String path = request.getPathInfo();

        if (path == null || "/list".equals(path)) {
            list(request, response);
            return;
        }

        response.sendRedirect(request.getContextPath() + "/suggestion/list");
    }

    /**
     * POST 요청
     * - 건의 등록 / 수정 / 삭제
     * - 답글 등록 / 수정 / 삭제
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        String path = request.getPathInfo();

        if ("/insert".equals(path)) {
            insert(request, response);
            return;
        }

        if ("/update".equals(path)) {
            update(request, response);
            return;
        }

        if ("/delete".equals(path)) {
            delete(request, response);
            return;
        }

        if ("/answerInsert".equals(path)) {
            answerInsert(request, response);
            return;
        }

        if ("/answerUpdate".equals(path)) {
            answerUpdate(request, response);
            return;
        }

        if ("/answerDelete".equals(path)) {
            answerDelete(request, response);
            return;
        }

        response.sendRedirect(request.getContextPath() + "/suggestion/list");
    }

    /**
     * 목록 / 상세 공통 처리
     *
     * [처리 내용]
     * 1. 검색 + 페이징
     * 2. mode=detail 이면 원글 + 답글 조회
     * 3. 현재 로그인 사용자 / 관리자 여부 판정
     * 4. 권한별 버튼 노출용 request attribute 세팅
     */
    private void list(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // ================================
        // 검색 조건
        // ================================
        String keyword = request.getParameter("keyword");
        String status = request.getParameter("status");
        String deptCode = request.getParameter("deptCode");

        // mode 기본값은 list
        String mode = request.getParameter("mode");
        if (mode == null || mode.trim().isEmpty()) {
            mode = "list";
        }

        // detail 화면 안에서 어떤 모달을 띄울지 구분
        String modal = request.getParameter("modal");

        // 상세 대상 건의글 번호
        String idParam = request.getParameter("id");

        // ================================
        // 페이징 계산
        // ================================
        int size = 10;
        int currentPage = 1;
        int blockSize = 5;

        String pageParam = request.getParameter("page");
        if (pageParam != null && !pageParam.trim().isEmpty()) {
            currentPage = Integer.parseInt(pageParam);
        }

        int totalCount = suggestionService.getSuggestionCount(keyword, status, deptCode);
        int totalPage = (int) Math.ceil((double) totalCount / size);

        if (totalPage < 1) {
            totalPage = 1;
        }

        if (currentPage < 1) {
            currentPage = 1;
        }

        if (currentPage > totalPage) {
            currentPage = totalPage;
        }

        int startRow = (currentPage - 1) * size + 1;
        int endRow = currentPage * size;

        int startPage = ((currentPage - 1) / blockSize) * blockSize + 1;
        int endPage = startPage + blockSize - 1;

        if (endPage > totalPage) {
            endPage = totalPage;
        }

        // ================================
        // 목록 조회
        // ================================
        List<SuggestionDTO> suggestionList =
                suggestionService.getSuggestionList(keyword, status, deptCode, startRow, endRow);

        // ================================
        // 현재 로그인 사용자 정보
        // ================================
        long loginEmpId = getLoginEmpId(request);
        boolean isAdmin = isAdmin(request);

        // ================================
        // 상세 화면용 데이터
        // ================================
        SuggestionDTO selectedSuggestion = null;
        AnswerDTO selectedAnswer = null;

        // 게시글 권한
        boolean canEditSuggestion = false;
        boolean canDeleteSuggestion = false;

        // 답글 권한
        boolean canWriteAnswer = false;
        boolean canEditAnswer = false;
        boolean canDeleteAnswer = false;

        if ("detail".equals(mode)
                && idParam != null
                && !idParam.trim().isEmpty()) {

            long suggestionId = Long.parseLong(idParam);

            /**
             * 조회수 증가 조건
             * - 상세 화면 처음 진입 시에만 증가
             * - 수정/삭제/답글 관련 모달을 띄울 때는 증가하지 않게 처리
             */
            boolean increaseViewCount = (modal == null || modal.trim().isEmpty());

            selectedSuggestion =
                    suggestionService.getSuggestionDetail(suggestionId, increaseViewCount);

            // 건의글에 연결된 답글 1건 조회
            selectedAnswer = answerService.getAnswerBySuggestionId(suggestionId);

            if (selectedSuggestion != null) {
                // 관리자거나 본인 글이면 수정/삭제 가능
                canEditSuggestion =
                        isAdmin || loginEmpId == selectedSuggestion.getWriterEmpId();

                canDeleteSuggestion =
                        isAdmin || loginEmpId == selectedSuggestion.getWriterEmpId();
            }

            if (selectedAnswer == null) {
                /**
                 * 답글이 아직 없을 때
                 * - 로그인한 사용자면 누구나 답글 작성 가능
                 * - 관리자도 작성 가능
                 */
                canWriteAnswer = isAdmin || loginEmpId > 0;
            } else {
                /**
                 * 답글이 있을 때
                 * - 관리자 또는 답글 작성자 본인만 수정/삭제 가능
                 */
                canEditAnswer =
                        isAdmin || loginEmpId == selectedAnswer.getWriterEmpId();

                canDeleteAnswer =
                        isAdmin || loginEmpId == selectedAnswer.getWriterEmpId();
            }
        }

        // ================================
        // JSP 전달값
        // ================================
        request.setAttribute("suggestionList", suggestionList);
        request.setAttribute("keyword", keyword);
        request.setAttribute("status", status);
        request.setAttribute("deptCode", deptCode);

        request.setAttribute("mode", mode);
        request.setAttribute("modal", modal);

        request.setAttribute("selectedSuggestion", selectedSuggestion);
        request.setAttribute("selectedAnswer", selectedAnswer);

        request.setAttribute("size", size);
        request.setAttribute("page", currentPage);
        request.setAttribute("totalCount", totalCount);
        request.setAttribute("totalPage", totalPage);
        request.setAttribute("startPage", startPage);
        request.setAttribute("endPage", endPage);

        // 로그인 / 권한 관련 값
        request.setAttribute("loginEmpId", loginEmpId);
        request.setAttribute("isAdmin", isAdmin);

        request.setAttribute("canEditSuggestion", canEditSuggestion);
        request.setAttribute("canDeleteSuggestion", canDeleteSuggestion);
        request.setAttribute("canWriteAnswer", canWriteAnswer);
        request.setAttribute("canEditAnswer", canEditAnswer);
        request.setAttribute("canDeleteAnswer", canDeleteAnswer);

        request.setAttribute("pageTitle", "건의사항 게시판");
        request.setAttribute("contentPage", "/WEB-INF/views/Suggestion.jsp");

        request.getRequestDispatcher("/WEB-INF/views/table.jsp").forward(request, response);
    }

    /**
     * 건의 등록
     */
    private void insert(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        SuggestionDTO dto = new SuggestionDTO();

        dto.setTitle(request.getParameter("title"));
        dto.setContent(request.getParameter("content"));
        dto.setStatus("접수");
        dto.setRemark(request.getParameter("remark"));

        // 로그인 사용자 사번
        long writerEmpId = getLoginEmpId(request);

        // 로그인값이 없을 때 테스트 기본값
        if (writerEmpId <= 0) {
            writerEmpId = 1L;
        }

        dto.setWriterEmpId(writerEmpId);

        suggestionService.addSuggestion(dto);

        response.sendRedirect(request.getContextPath() + "/suggestion/list");
    }

    /**
     * 건의 수정
     *
     * [서버단 권한 체크]
     * - 관리자 또는 글 작성자 본인만 가능
     */
    private void update(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        long suggestionId = Long.parseLong(request.getParameter("suggestionId"));

        // 기존 글 조회
        SuggestionDTO oldDto = suggestionService.getSuggestionDetail(suggestionId, false);

        if (oldDto == null) {
            response.sendRedirect(request.getContextPath() + "/suggestion/list");
            return;
        }

        long loginEmpId = getLoginEmpId(request);
        boolean isAdmin = isAdmin(request);

        // 권한 없으면 상세로 되돌림
        if (!(isAdmin || loginEmpId == oldDto.getWriterEmpId())) {
            response.sendRedirect(request.getContextPath()
                    + "/suggestion/list?mode=detail&id=" + suggestionId);
            return;
        }

        SuggestionDTO dto = new SuggestionDTO();

        dto.setSuggestionId(suggestionId);
        dto.setTitle(request.getParameter("title"));
        dto.setContent(request.getParameter("content"));
        dto.setStatus(request.getParameter("status"));
        dto.setRemark(request.getParameter("remark"));

        suggestionService.modifySuggestion(dto);

        response.sendRedirect(request.getContextPath()
                + "/suggestion/list?mode=detail&id=" + suggestionId);
    }

    /**
     * 건의 삭제
     *
     * [서버단 권한 체크]
     * - 관리자 또는 글 작성자 본인만 가능
     */
    private void delete(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        long suggestionId = Long.parseLong(request.getParameter("suggestionId"));

        SuggestionDTO oldDto = suggestionService.getSuggestionDetail(suggestionId, false);

        if (oldDto == null) {
            response.sendRedirect(request.getContextPath() + "/suggestion/list");
            return;
        }

        long loginEmpId = getLoginEmpId(request);
        boolean isAdmin = isAdmin(request);

        if (!(isAdmin || loginEmpId == oldDto.getWriterEmpId())) {
            response.sendRedirect(request.getContextPath()
                    + "/suggestion/list?mode=detail&id=" + suggestionId);
            return;
        }

        suggestionService.removeSuggestion(suggestionId);

        response.sendRedirect(request.getContextPath() + "/suggestion/list");
    }

    /**
     * 답글 등록
     *
     * [정책]
     * - 현재 구조는 건의글 1건당 답글 1건 기준
     * - 이미 답글이 있으면 추가 등록 막음
     * - 로그인 사용자는 답글 작성 가능
     */
    private void answerInsert(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        long suggestionId = Long.parseLong(request.getParameter("suggestionId"));

        // 이미 답글이 있으면 다시 상세로 이동
        AnswerDTO existingAnswer = answerService.getAnswerBySuggestionId(suggestionId);
        if (existingAnswer != null) {
            response.sendRedirect(request.getContextPath()
                    + "/suggestion/list?mode=detail&id=" + suggestionId);
            return;
        }

        AnswerDTO dto = new AnswerDTO();

        dto.setSuggestionId(suggestionId);
        dto.setContent(request.getParameter("content"));
        dto.setStatus(request.getParameter("status"));
        dto.setRemark(request.getParameter("remark"));

        long writerEmpId = getLoginEmpId(request);

        // 로그인값이 없을 때 테스트 기본값
        if (writerEmpId <= 0) {
            writerEmpId = 1L;
        }

        dto.setWriterEmpId(writerEmpId);

        answerService.addAnswer(dto);

        response.sendRedirect(request.getContextPath()
                + "/suggestion/list?mode=detail&id=" + suggestionId);
    }

    /**
     * 답글 수정
     *
     * [서버단 권한 체크]
     * - 관리자 또는 답글 작성자 본인만 가능
     */
    private void answerUpdate(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        long answerId = Long.parseLong(request.getParameter("answerId"));
        long suggestionId = Long.parseLong(request.getParameter("suggestionId"));

        AnswerDTO oldDto = answerService.getAnswerById(answerId);

        if (oldDto == null) {
            response.sendRedirect(request.getContextPath()
                    + "/suggestion/list?mode=detail&id=" + suggestionId);
            return;
        }

        long loginEmpId = getLoginEmpId(request);
        boolean isAdmin = isAdmin(request);

        if (!(isAdmin || loginEmpId == oldDto.getWriterEmpId())) {
            response.sendRedirect(request.getContextPath()
                    + "/suggestion/list?mode=detail&id=" + suggestionId);
            return;
        }

        AnswerDTO dto = new AnswerDTO();

        dto.setAnswerId(answerId);
        dto.setSuggestionId(suggestionId);
        dto.setContent(request.getParameter("content"));
        dto.setStatus(request.getParameter("status"));
        dto.setRemark(request.getParameter("remark"));

        answerService.modifyAnswer(dto);

        response.sendRedirect(request.getContextPath()
                + "/suggestion/list?mode=detail&id=" + suggestionId);
    }

    /**
     * 답글 삭제
     *
     * [서버단 권한 체크]
     * - 관리자 또는 답글 작성자 본인만 가능
     */
    private void answerDelete(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        long answerId = Long.parseLong(request.getParameter("answerId"));
        long suggestionId = Long.parseLong(request.getParameter("suggestionId"));

        AnswerDTO oldDto = answerService.getAnswerById(answerId);

        if (oldDto == null) {
            response.sendRedirect(request.getContextPath()
                    + "/suggestion/list?mode=detail&id=" + suggestionId);
            return;
        }

        long loginEmpId = getLoginEmpId(request);
        boolean isAdmin = isAdmin(request);

        if (!(isAdmin || loginEmpId == oldDto.getWriterEmpId())) {
            response.sendRedirect(request.getContextPath()
                    + "/suggestion/list?mode=detail&id=" + suggestionId);
            return;
        }

        answerService.removeAnswer(answerId);

        response.sendRedirect(request.getContextPath()
                + "/suggestion/list?mode=detail&id=" + suggestionId);
    }

    /**
     * 로그인 사용자 사번 가져오기
     *
     * [프로젝트마다 세션명 다를 수 있음]
     * - loginEmpId
     * - empId
     *
     * 위 두 개를 우선 확인
     */
    private long getLoginEmpId(HttpServletRequest request) {
        HttpSession session = request.getSession(false);

        if (session == null) {
            return 0L;
        }

        Object loginEmpId = session.getAttribute("loginEmpId");
        if (loginEmpId == null) {
            loginEmpId = session.getAttribute("empId");
        }

        if (loginEmpId instanceof Number) {
            return ((Number) loginEmpId).longValue();
        }

        if (loginEmpId != null) {
            try {
                return Long.parseLong(String.valueOf(loginEmpId));
            } catch (Exception e) {
                return 0L;
            }
        }

        return 0L;
    }

    /**
     * 관리자 여부 판단
     *
     * [주의]
     * 실제 로그인 세션명에 맞게 이 메서드만 조정하면 됨
     *
     * 우선 확인하는 값 예시:
     * - isAdmin (Boolean)
     * - role / roleName / loginRole / loginRoleName / auth
     *
     * 값에 ADMIN 또는 관리자 포함 시 관리자 처리
     */
    private boolean isAdmin(HttpServletRequest request) {
        HttpSession session = request.getSession(false);

        if (session == null) {
            return false;
        }

        Object adminFlag = session.getAttribute("isAdmin");
        if (adminFlag instanceof Boolean) {
            return (Boolean) adminFlag;
        }

        Object role = session.getAttribute("loginRole");
        if (role == null) role = session.getAttribute("loginRoleName");
        if (role == null) role = session.getAttribute("role");
        if (role == null) role = session.getAttribute("roleName");
        if (role == null) role = session.getAttribute("auth");

        if (role != null) {
            String roleText = String.valueOf(role).toUpperCase();
            if (roleText.contains("ADMIN") || roleText.contains("관리자")) {
                return true;
            }
        }

        return false;
    }
}