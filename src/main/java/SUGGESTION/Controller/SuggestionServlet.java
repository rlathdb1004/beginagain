package SUGGESTION.Controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import SUGGESTION.DTO.SuggestionDTO;
import SUGGESTION.Service.SuggestionService;

/**
 * 건의사항 서블릿
 * - 목록 JSP 1개만 사용
 * - 등록/상세/수정 모두 suggestionList.jsp에서 모달로 처리
 */
@WebServlet("/suggestion/*")
public class SuggestionServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private final SuggestionService suggestionService = new SuggestionService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");

        String path = request.getPathInfo();

        if (path == null || "/list".equals(path)) {
            list(request, response);
        } else {
            response.sendRedirect(request.getContextPath() + "/suggestion/list");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");

        String path = request.getPathInfo();

        if ("/insert".equals(path)) {
            insert(request, response);
        } else if ("/update".equals(path)) {
            update(request, response);
        } else if ("/delete".equals(path)) {
            delete(request, response);
        } else {
            response.sendRedirect(request.getContextPath() + "/suggestion/list");
        }
    }

    /**
     * 목록 + 모달 분기
     * mode
     * - "" or null : 일반 목록
     * - write      : 등록 모달 열기
     * - detail     : 상세 모달 열기
     * - edit       : 수정 모달 열기
     * 페이징 포함
     */
    private void list(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String keyword = request.getParameter("keyword");
        String status = request.getParameter("status");
        String deptCode = request.getParameter("deptCode");
        String mode = request.getParameter("mode");
        String idParam = request.getParameter("id");

        int size = 10;   // 한 페이지당 10개
        int page = 1;    // 현재 페이지

        String sSize = request.getParameter("size");
        String sPage = request.getParameter("page");

        try {
            size = Integer.parseInt(sSize);
        } catch (Exception e) {
        }

        try {
            page = Integer.parseInt(sPage);
        } catch (Exception e) {
        }

        if (page < 1) {
            page = 1;
        }

        // 기존 전체 목록 조회 유지
        List<SuggestionDTO> fullList =
                suggestionService.getSuggestionList(keyword, status, deptCode);

        int totalCount = fullList.size();
        int totalPage = (int) Math.ceil((double) totalCount / size);

        if (totalPage == 0) {
            totalPage = 1;
        }

        if (page > totalPage) {
            page = totalPage;
        }

        int startIndex = (page - 1) * size;
        int endIndex = Math.min(startIndex + size, totalCount);

        List<SuggestionDTO> suggestionList;
        if (totalCount == 0) {
            suggestionList = new java.util.ArrayList<>();
        } else {
            suggestionList = fullList.subList(startIndex, endIndex);
        }

        // 페이지 번호 묶음
        int pageGroupSize = 5;
        int startPage = ((page - 1) / pageGroupSize) * pageGroupSize + 1;
        int endPage = Math.min(startPage + pageGroupSize - 1, totalPage);

        SuggestionDTO selectedSuggestion = null;

        if (idParam != null && !idParam.trim().isEmpty()) {
            long suggestionId = Long.parseLong(idParam);

            boolean increaseViewCount = "detail".equals(mode);
            selectedSuggestion = suggestionService.getSuggestionDetail(suggestionId, increaseViewCount);
        }

        request.setAttribute("suggestionList", suggestionList);
        request.setAttribute("keyword", keyword);
        request.setAttribute("status", status);
        request.setAttribute("deptCode", deptCode);
        request.setAttribute("mode", mode);
        request.setAttribute("selectedSuggestion", selectedSuggestion);

        // 페이징 정보
        request.setAttribute("size", size);
        request.setAttribute("page", page);
        request.setAttribute("totalCount", totalCount);
        request.setAttribute("totalPage", totalPage);
        request.setAttribute("startPage", startPage);
        request.setAttribute("endPage", endPage);

        request.setAttribute("contentPage", "/WEB-INF/views/Suggestion.jsp");
        request.getRequestDispatcher("/WEB-INF/views/table.jsp").forward(request, response);
    }

    /**
     * 등록
     */
    private void insert(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        SuggestionDTO dto = new SuggestionDTO();
        dto.setTitle(request.getParameter("title"));
        dto.setContent(request.getParameter("content"));
        dto.setStatus("접수");
        dto.setRemark(request.getParameter("remark"));

        long writerEmpId = 1L; // 테스트용 기본값
        HttpSession session = request.getSession(false);
        if (session != null) {
            Object loginEmpId = session.getAttribute("loginEmpId");
            if (loginEmpId instanceof Number) {
                writerEmpId = ((Number) loginEmpId).longValue();
            }
        }
        dto.setWriterEmpId(writerEmpId);

        suggestionService.addSuggestion(dto);
        response.sendRedirect(request.getContextPath() + "/suggestion/list");
    }

    /**
     * 수정
     */
    private void update(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        SuggestionDTO dto = new SuggestionDTO();
        dto.setSuggestionId(Long.parseLong(request.getParameter("suggestionId")));
        dto.setTitle(request.getParameter("title"));
        dto.setContent(request.getParameter("content"));
        dto.setStatus(request.getParameter("status"));
        dto.setRemark(request.getParameter("remark"));

        suggestionService.modifySuggestion(dto);
        response.sendRedirect(request.getContextPath() + "/suggestion/list?mode=detail&id=" + dto.getSuggestionId());
    }

    /**
     * 삭제
     */
    private void delete(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        long suggestionId = Long.parseLong(request.getParameter("suggestionId"));
        suggestionService.removeSuggestion(suggestionId);

        response.sendRedirect(request.getContextPath() + "/suggestion/list");
    }
}
