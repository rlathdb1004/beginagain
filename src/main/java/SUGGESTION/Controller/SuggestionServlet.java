package SUGGESTION.Controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.RequestDispatcher;
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
import member.dto.MemberDTO;

@WebServlet("/suggestion/*")
public class SuggestionServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private final SuggestionService suSuggestionService = new SuggestionService();
    private final AnswerService anAnswerService = new AnswerService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        String suPath = request.getPathInfo();

        try {
            if (suPath == null || "/".equals(suPath) || "/list".equals(suPath)) {
                list(request, response);
                return;
            }

            response.sendRedirect(request.getContextPath() + "/suggestion/list");

        } catch (Exception e) {
            e.printStackTrace();
            throw new ServletException("SuggestionServlet doGet 오류", e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        String suPath = request.getPathInfo();

        try {
            if ("/insert".equals(suPath)) {
                insert(request, response);
                return;
            }

            if ("/update".equals(suPath)) {
                update(request, response);
                return;
            }

            if ("/delete".equals(suPath)) {
                delete(request, response);
                return;
            }

            if ("/hide".equals(suPath)) {
                hide(request, response);
                return;
            }
            
            if ("/restore".equals(suPath)) {
                restore(request, response);
                return;
            }

            if ("/answerInsert".equals(suPath)) {
                answerInsert(request, response);
                return;
            }

            if ("/answerUpdate".equals(suPath)) {
                answerUpdate(request, response);
                return;
            }

            if ("/answerDelete".equals(suPath)) {
                answerDelete(request, response);
                return;
            }

            if ("/answerHide".equals(suPath)) {
                answerHide(request, response);
                return;
            }

            response.sendRedirect(request.getContextPath() + "/suggestion/list");

        } catch (Exception e) {
            e.printStackTrace();
            throw new ServletException("SuggestionServlet doPost 오류", e);
        }
    }


    private void list(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String suMode = getString(request.getParameter("mode"), "list");
        String suKeyword = getString(request.getParameter("keyword"), "");
        String suStatus = getString(request.getParameter("status"), "");
        String suDeptCode = getString(request.getParameter("deptCode"), "");
        String suModal = getString(request.getParameter("modal"), "");
        String suAnswerMode = getString(request.getParameter("answerMode"), "");

        int suPage = parseInt(request.getParameter("page"), 1);
        int suSize = parseInt(request.getParameter("size"), 10);

        if (suPage < 1) {
            suPage = 1;
        }
        if (suSize < 1) {
            suSize = 10;
        }

        int suTotalCount = suSuggestionService.getSuggestionCount(suKeyword, suStatus, suDeptCode);
        int suTotalPage = (int) Math.ceil((double) suTotalCount / suSize);

        if (suTotalPage < 1) {
            suTotalPage = 1;
        }

        if (suPage > suTotalPage) {
            suPage = suTotalPage;
        }

        int suStartRow = (suPage - 1) * suSize + 1;
        int suEndRow = suPage * suSize;

        List<SuggestionDTO> suSuggestionList =
                suSuggestionService.getSuggestionList(suStartRow, suEndRow, suKeyword, suStatus, suDeptCode);

        int suPageBlock = 5;
        int suStartPage = ((suPage - 1) / suPageBlock) * suPageBlock + 1;
        int suEndPage = suStartPage + suPageBlock - 1;

        if (suEndPage > suTotalPage) {
            suEndPage = suTotalPage;
        }

        request.setAttribute("suggestionList", suSuggestionList);
        request.setAttribute("keyword", suKeyword);
        request.setAttribute("status", suStatus);
        request.setAttribute("deptCode", suDeptCode);
        request.setAttribute("mode", suMode);
        request.setAttribute("modal", suModal);
        request.setAttribute("answerMode", suAnswerMode);
        request.setAttribute("page", suPage);
        request.setAttribute("size", suSize);
        request.setAttribute("totalCount", suTotalCount);
        request.setAttribute("totalPage", suTotalPage);
        request.setAttribute("startPage", suStartPage);
        request.setAttribute("endPage", suEndPage);

        /*
         * 상세 모드일 때 게시글 / 답글 다시 조회
         */
        SuggestionDTO suSelectedSuggestion = null;
        AnswerDTO anSelectedAnswer = null;
        List<AnswerDTO> anAnswerList = null;

        if ("detail".equals(suMode)) {
            long suSuggestionId = parseLong(request.getParameter("id"), 0L);

            if (suSuggestionId > 0L) {
                suSelectedSuggestion = suSuggestionService.getSuggestionDetail(suSuggestionId, true);
                anSelectedAnswer = anAnswerService.getAnswerBySuggestionId(suSuggestionId);

                try {
                    anAnswerList = anAnswerService.getAnswerListBySuggestionId(suSuggestionId);
                } catch (Exception e) {
                    anAnswerList = null;
                }

                request.setAttribute("selectedSuggestion", suSelectedSuggestion);
                request.setAttribute("selectedAnswer", anSelectedAnswer);
                request.setAttribute("answerList", anAnswerList);
            }
        }

        MemberDTO loginUser = (MemberDTO) request.getSession().getAttribute("loginUser");
        boolean isAdminRole = isAdminRole(loginUser);
        boolean isOwnSuggestion = suSelectedSuggestion != null && loginUser != null
                && suSelectedSuggestion.getWriterEmpId() == loginUser.getEmpId();

        request.setAttribute("isAdmin", Boolean.valueOf(isAdminRole));
        request.setAttribute("canEditSuggestion", Boolean.valueOf(isAdminRole || isOwnSuggestion));
        request.setAttribute("canDeleteSuggestion", Boolean.valueOf(isAdminRole || isOwnSuggestion));
        request.setAttribute("canWriteAnswer", Boolean.valueOf(isAdminRole));
        request.setAttribute("canEditAnswer", Boolean.valueOf(isAdminRole));
        request.setAttribute("canDeleteAnswer", Boolean.valueOf(isAdminRole));

        request.setAttribute("pageTitle", "건의사항 게시판");
        request.setAttribute("contentPage", "/WEB-INF/views/Suggestion.jsp");

        RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/views/table.jsp");
        dispatcher.forward(request, response);
    }
    

    private void insert(HttpServletRequest request, HttpServletResponse response) throws Exception {
        SuggestionDTO suDto = new SuggestionDTO();
        suDto.setTitle(getString(request.getParameter("title"), ""));
        suDto.setContent(getString(request.getParameter("content"), ""));
        suDto.setRemark(getString(request.getParameter("remark"), ""));
        suDto.setStatus("접수");
        suDto.setWriterEmpId(resolveWriterEmpId(request.getSession(false)));
        suSuggestionService.insertSuggestion(suDto);
        response.sendRedirect(request.getContextPath() + "/suggestion/list");
    }

    private void update(HttpServletRequest request, HttpServletResponse response) throws Exception {
        long suSuggestionId = parseLong(request.getParameter("suggestionId"), 0L);
        validateSuggestionAuthority(request.getSession(false), suSuggestionId, true);

        SuggestionDTO suDto = new SuggestionDTO();
        suDto.setSuggestionId(suSuggestionId);
        suDto.setTitle(getString(request.getParameter("title"), ""));
        suDto.setContent(getString(request.getParameter("content"), ""));
        suDto.setStatus(getString(request.getParameter("status"), ""));
        suDto.setRemark(getString(request.getParameter("remark"), ""));

        suSuggestionService.updateSuggestion(suDto);
        response.sendRedirect(request.getContextPath() + "/suggestion/list?mode=detail&id=" + suSuggestionId);
    }

    private void delete(HttpServletRequest request, HttpServletResponse response) throws Exception {
        long suSuggestionId = parseLong(request.getParameter("suggestionId"), 0L);
        validateSuggestionAuthority(request.getSession(false), suSuggestionId, true);
        suSuggestionService.deleteSuggestion(suSuggestionId);
        response.sendRedirect(request.getContextPath() + "/suggestion/list");
    }

    private void hide(HttpServletRequest request, HttpServletResponse response) throws Exception {
        long suSuggestionId = parseLong(request.getParameter("suggestionId"), 0L);
        validateAdminRole(request.getSession(false));
        suSuggestionService.hideSuggestion(suSuggestionId);
        response.sendRedirect(request.getContextPath() + "/suggestion/list?mode=detail&id=" + suSuggestionId);
    }
    
    private void restore(HttpServletRequest request, HttpServletResponse response) throws Exception {
        long suSuggestionId = parseLong(request.getParameter("suggestionId"), 0L);
        validateAdminRole(request.getSession(false));
        suSuggestionService.restoreSuggestion(suSuggestionId);
        response.sendRedirect(request.getContextPath() + "/suggestion/list?mode=detail&id=" + suSuggestionId);
    }

    private void answerInsert(HttpServletRequest request, HttpServletResponse response) throws Exception {
        validateAdminRole(request.getSession(false));
        long suSuggestionId = parseLong(request.getParameter("suggestionId"), 0L);

        AnswerDTO anDto = new AnswerDTO();
        anDto.setSuggestionId(suSuggestionId);
        anDto.setStatus(getString(request.getParameter("status"), "등록"));
        anDto.setContent(getString(request.getParameter("content"), ""));
        anDto.setRemark(getString(request.getParameter("remark"), ""));
        anDto.setWriterEmpId(resolveWriterEmpId(request.getSession(false)));

        anAnswerService.insertAnswer(anDto);
        response.sendRedirect(request.getContextPath() + "/suggestion/list?mode=detail&id=" + suSuggestionId + "#suAnswerWriteBox");
    }

    private void answerUpdate(HttpServletRequest request, HttpServletResponse response) throws Exception {
        validateAdminRole(request.getSession(false));
        long anAnswerId = parseLong(request.getParameter("answerId"), 0L);
        long suSuggestionId = parseLong(request.getParameter("suggestionId"), 0L);

        AnswerDTO anDto = new AnswerDTO();
        anDto.setAnswerId(anAnswerId);
        anDto.setSuggestionId(suSuggestionId);
        anDto.setStatus(getString(request.getParameter("status"), ""));
        anDto.setContent(getString(request.getParameter("content"), ""));
        anDto.setRemark(getString(request.getParameter("remark"), ""));

        anAnswerService.updateAnswer(anDto);
        response.sendRedirect(request.getContextPath() + "/suggestion/list?mode=detail&id=" + suSuggestionId + "#suAnswerWriteBox");
    }

    private void answerDelete(HttpServletRequest request, HttpServletResponse response) throws Exception {
        validateAdminRole(request.getSession(false));
        long anAnswerId = parseLong(request.getParameter("answerId"), 0L);
        long suSuggestionId = parseLong(request.getParameter("suggestionId"), 0L);
        anAnswerService.deleteAnswer(anAnswerId);
        response.sendRedirect(request.getContextPath() + "/suggestion/list?mode=detail&id=" + suSuggestionId);
    }

    private void answerHide(HttpServletRequest request, HttpServletResponse response) throws Exception {
        validateAdminRole(request.getSession(false));
        long anAnswerId = parseLong(request.getParameter("answerId"), 0L);
        long suSuggestionId = parseLong(request.getParameter("suggestionId"), 0L);
        anAnswerService.hideAnswer(anAnswerId);
        response.sendRedirect(request.getContextPath() + "/suggestion/list?mode=detail&id=" + suSuggestionId);
    }

    private String getString(String value, String defaultValue) {
        return value == null ? defaultValue : value.trim();
    }

    private int parseInt(String value, int defaultValue) {
        try {
            return Integer.parseInt(value);
        } catch (Exception e) {
            return defaultValue;
        }
    }

    private long parseLong(String value, long defaultValue) {
        try {
            return Long.parseLong(value);
        } catch (Exception e) {
            return defaultValue;
        }
    }

    private long resolveWriterEmpId(HttpSession session) {
        if (session != null) {
            Object loginUserObj = session.getAttribute("loginUser");

            if (loginUserObj instanceof MemberDTO) {
                return ((MemberDTO) loginUserObj).getEmpId();
            }

            Object loginEmpId = session.getAttribute("loginEmpId");
            if (loginEmpId instanceof Number) {
                return ((Number) loginEmpId).longValue();
            }

            Object empId = session.getAttribute("empId");
            if (empId instanceof Number) {
                return ((Number) empId).longValue();
            }
        }

        throw new IllegalStateException("로그인 사용자 정보를 찾을 수 없습니다.");
    }
    private boolean isAdminRole(MemberDTO loginUser) {
        if (loginUser == null || loginUser.getRoleName() == null) {
            return false;
        }

        String roleName = loginUser.getRoleName();
        return "MES_ADMIN".equals(roleName) || "CEO".equals(roleName) || "SITE_MANAGER".equals(roleName);
    }

    private void validateAdminRole(HttpSession session) {
        MemberDTO loginUser = session == null ? null : (MemberDTO) session.getAttribute("loginUser");
        if (!isAdminRole(loginUser)) {
            throw new IllegalStateException("해당 작업에 대한 권한이 없습니다.");
        }
    }

    private void validateSuggestionAuthority(HttpSession session, long suggestionId, boolean allowOwner) throws Exception {
        MemberDTO loginUser = session == null ? null : (MemberDTO) session.getAttribute("loginUser");
        if (loginUser == null) {
            throw new IllegalStateException("로그인 사용자 정보를 찾을 수 없습니다.");
        }

        if (isAdminRole(loginUser)) {
            return;
        }

        if (!allowOwner) {
            throw new IllegalStateException("해당 작업에 대한 권한이 없습니다.");
        }

        SuggestionDTO target = suSuggestionService.getSuggestionDetail(suggestionId, false);
        if (target == null || target.getWriterEmpId() != loginUser.getEmpId()) {
            throw new IllegalStateException("본인 게시글만 처리할 수 있습니다.");
        }
    }

}

