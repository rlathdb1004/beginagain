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

        int suPage = parseInt(request.getParameter("page"), 1);
        int suSize = parseInt(request.getParameter("size"), 10);
        if (suPage < 1) suPage = 1;
        if (suSize < 1) suSize = 10;

        int suStartRow = (suPage - 1) * suSize + 1;
        int suEndRow = suPage * suSize;

        int suTotalCount = suSuggestionService.getSuggestionCount(suKeyword, suStatus, suDeptCode);
        List<SuggestionDTO> suSuggestionList =
                suSuggestionService.getSuggestionList(suStartRow, suEndRow, suKeyword, suStatus, suDeptCode);

        int suTotalPage = (int) Math.ceil((double) suTotalCount / suSize);
        if (suTotalPage < 1) suTotalPage = 1;

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
        request.setAttribute("page", suPage);
        request.setAttribute("size", suSize);
        request.setAttribute("totalCount", suTotalCount);
        request.setAttribute("totalPage", suTotalPage);
        request.setAttribute("startPage", suStartPage);
        request.setAttribute("endPage", suEndPage);

        if ("detail".equals(suMode)) {
            long suSuggestionId = parseLong(request.getParameter("id"), 0L);
            if (suSuggestionId > 0) {
                SuggestionDTO suSelectedSuggestion = suSuggestionService.getSuggestionDetail(suSuggestionId, false);
                AnswerDTO anSelectedAnswer = anAnswerService.getAnswerBySuggestionId(suSuggestionId);
                List<AnswerDTO> anAnswerList = anAnswerService.getAnswerListBySuggestionId(suSuggestionId);

                request.setAttribute("selectedSuggestion", suSelectedSuggestion);
                request.setAttribute("selectedAnswer", anSelectedAnswer);
                request.setAttribute("answerList", anAnswerList);
            }
        }

        request.setAttribute("isAdmin", true);
        request.setAttribute("canEditSuggestion", true);
        request.setAttribute("canDeleteSuggestion", true);
        request.setAttribute("canWriteAnswer", true);
        request.setAttribute("canEditAnswer", true);
        request.setAttribute("canDeleteAnswer", true);

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
        suSuggestionService.deleteSuggestion(suSuggestionId);
        response.sendRedirect(request.getContextPath() + "/suggestion/list");
    }

    private void hide(HttpServletRequest request, HttpServletResponse response) throws Exception {
        long suSuggestionId = parseLong(request.getParameter("suggestionId"), 0L);
        suSuggestionService.hideSuggestion(suSuggestionId);
        response.sendRedirect(request.getContextPath() + "/suggestion/list?mode=detail&id=" + suSuggestionId);
    }

    private void answerInsert(HttpServletRequest request, HttpServletResponse response) throws Exception {
        long suSuggestionId = parseLong(request.getParameter("suggestionId"), 0L);

        AnswerDTO anDto = new AnswerDTO();
        anDto.setSuggestionId(suSuggestionId);
        anDto.setStatus(getString(request.getParameter("status"), "등록"));
        anDto.setContent(getString(request.getParameter("content"), ""));
        anDto.setRemark(getString(request.getParameter("remark"), ""));
        anDto.setWriterEmpId(resolveWriterEmpId(request.getSession(false)));

        anAnswerService.insertAnswer(anDto);
        response.sendRedirect(request.getContextPath() + "/suggestion/list?mode=detail&id=" + suSuggestionId);
    }

    private void answerUpdate(HttpServletRequest request, HttpServletResponse response) throws Exception {
        long anAnswerId = parseLong(request.getParameter("answerId"), 0L);
        long suSuggestionId = parseLong(request.getParameter("suggestionId"), 0L);

        AnswerDTO anDto = new AnswerDTO();
        anDto.setAnswerId(anAnswerId);
        anDto.setSuggestionId(suSuggestionId);
        anDto.setStatus(getString(request.getParameter("status"), ""));
        anDto.setContent(getString(request.getParameter("content"), ""));
        anDto.setRemark(getString(request.getParameter("remark"), ""));

        anAnswerService.updateAnswer(anDto);
        response.sendRedirect(request.getContextPath() + "/suggestion/list?mode=detail&id=" + suSuggestionId);
    }

    private void answerDelete(HttpServletRequest request, HttpServletResponse response) throws Exception {
        long anAnswerId = parseLong(request.getParameter("answerId"), 0L);
        long suSuggestionId = parseLong(request.getParameter("suggestionId"), 0L);
        anAnswerService.deleteAnswer(anAnswerId);
        response.sendRedirect(request.getContextPath() + "/suggestion/list?mode=detail&id=" + suSuggestionId);
    }

    private void answerHide(HttpServletRequest request, HttpServletResponse response) throws Exception {
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
            Object loginEmpId = session.getAttribute("loginEmpId");
            if (loginEmpId instanceof Number) {
                return ((Number) loginEmpId).longValue();
            }
            Object empId = session.getAttribute("empId");
            if (empId instanceof Number) {
                return ((Number) empId).longValue();
            }
        }
        return 1L;
    }
}
