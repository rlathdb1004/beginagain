
package board.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import board.dto.NoticeBoardDTO;
import board.service.BoardService;

@WebServlet("/notice/register")
public class NoticeRegisterController extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private BoardService boardService = new BoardService();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        NoticeBoardDTO dto = new NoticeBoardDTO();
        dto.setTitle(request.getParameter("title"));
        dto.setContent(request.getParameter("content"));
        dto.setWriterEmpId(Integer.parseInt(request.getParameter("writerEmpId")));
        dto.setStatus(request.getParameter("status"));
        dto.setRemark(request.getParameter("remark"));
        boardService.insertNotice(dto);
        response.sendRedirect(request.getContextPath() + "/notice/list");
    }
}
