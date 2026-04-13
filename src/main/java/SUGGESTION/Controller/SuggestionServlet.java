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
     */
    private void list(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String keyword = request.getParameter("keyword");
        String status = request.getParameter("status");
        String deptCode = request.getParameter("deptCode");
        String mode = request.getParameter("mode");
        String idParam = request.getParameter("id");

        List<SuggestionDTO> suggestionList =
                suggestionService.getSuggestionList(keyword, status, deptCode);

        SuggestionDTO selectedSuggestion = null;

        if (idParam != null && !idParam.trim().isEmpty()) {
            long suggestionId = Long.parseLong(idParam);

            // 상세만 조회수 증가 / 수정모드는 조회수 증가 안 함
            boolean increaseViewCount = "detail".equals(mode);
            selectedSuggestion = suggestionService.getSuggestionDetail(suggestionId, increaseViewCount);
        }

        request.setAttribute("suggestionList", suggestionList);
        request.setAttribute("keyword", keyword);
        request.setAttribute("status", status);
        request.setAttribute("deptCode", deptCode);
        request.setAttribute("mode", mode);
        request.setAttribute("selectedSuggestion", selectedSuggestion);

        request.getRequestDispatcher("/WEB-INF/views/suggestion/suggestionList.jsp")
               .forward(request, response);
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
