package ANSWER.Controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import ANSWER.DTO.AnswerDTO;
import ANSWER.Service.AnswerService;

/*
 * 답글 전용 Servlet
 * - 등록 / 수정 / 삭제 / 숨김
 */
@WebServlet({
    "/answer/insert",
    "/answer/update",
    "/answer/delete",
    "/answer/hide"
})
public class AnswerServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private AnswerService anAnswerService = new AnswerService();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            request.setCharacterEncoding("UTF-8");

            String anUri = request.getRequestURI();

            if (anUri.endsWith("/insert")) {
                insert(request, response);
                return;
            }

            if (anUri.endsWith("/update")) {
                update(request, response);
                return;
            }

            if (anUri.endsWith("/delete")) {
                delete(request, response);
                return;
            }

            if (anUri.endsWith("/hide")) {
                hide(request, response);
                return;
            }

            response.sendRedirect(request.getContextPath() + "/suggestion/list");

        } catch (Exception e) {
            e.printStackTrace();
            throw new ServletException("AnswerServlet 오류", e);
        }
    }

    /*
     * 답글 등록
     */
    private void insert(HttpServletRequest request, HttpServletResponse response)
            throws Exception {

        long anSuggestionId = parseLong(request.getParameter("suggestionId"), 0L);

        AnswerDTO anDto = new AnswerDTO();
        anDto.setSuggestionId(anSuggestionId);
        anDto.setContent(getString(request.getParameter("content"), ""));
        anDto.setStatus(getString(request.getParameter("status"), "등록"));
        anDto.setRemark(getString(request.getParameter("remark"), ""));

        /*
         * 실제 로그인 사번으로 바꿔야 함
         * 지금은 임시값
         */
        anDto.setWriterEmpId(1L);

        anAnswerService.insertAnswer(anDto);

        response.sendRedirect(
            request.getContextPath() + "/suggestion/list?mode=detail&id=" + anSuggestionId
        );
    }

    /*
     * 답글 수정
     */
    private void update(HttpServletRequest request, HttpServletResponse response)
            throws Exception {

        long anAnswerId = parseLong(request.getParameter("answerId"), 0L);
        long anSuggestionId = parseLong(request.getParameter("suggestionId"), 0L);

        AnswerDTO anDto = new AnswerDTO();
        anDto.setAnswerId(anAnswerId);
        anDto.setSuggestionId(anSuggestionId);
        anDto.setContent(getString(request.getParameter("content"), ""));
        anDto.setStatus(getString(request.getParameter("status"), ""));
        anDto.setRemark(getString(request.getParameter("remark"), ""));

        anAnswerService.updateAnswer(anDto);

        response.sendRedirect(
            request.getContextPath() + "/suggestion/list?mode=detail&id=" + anSuggestionId
        );
    }

    /*
     * 답글 삭제
     */
    private void delete(HttpServletRequest request, HttpServletResponse response)
            throws Exception {

        long anAnswerId = parseLong(request.getParameter("answerId"), 0L);
        long anSuggestionId = parseLong(request.getParameter("suggestionId"), 0L);

        anAnswerService.deleteAnswer(anAnswerId);

        response.sendRedirect(
            request.getContextPath() + "/suggestion/list?mode=detail&id=" + anSuggestionId
        );
    }

    /*
     * 답글 숨김
     */
    private void hide(HttpServletRequest request, HttpServletResponse response)
            throws Exception {

        long anAnswerId = parseLong(request.getParameter("answerId"), 0L);
        long anSuggestionId = parseLong(request.getParameter("suggestionId"), 0L);

        anAnswerService.hideAnswer(anAnswerId);

        response.sendRedirect(
            request.getContextPath() + "/suggestion/list?mode=detail&id=" + anSuggestionId
        );
    }

    private String getString(String anValue, String anDefaultValue) {
        return anValue == null ? anDefaultValue : anValue.trim();
    }

    private long parseLong(String anValue, long anDefaultValue) {
        try {
            return Long.parseLong(anValue);
        } catch (Exception e) {
            return anDefaultValue;
        }
    }
}