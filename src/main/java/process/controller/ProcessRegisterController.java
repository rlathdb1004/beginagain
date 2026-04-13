
package process.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import process.dto.ProcessDTO;
import process.service.ProcessService;

@WebServlet("/process/register")
public class ProcessRegisterController extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private ProcessService service = new ProcessService();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        ProcessDTO dto = new ProcessDTO();
        dto.setProcessCode(request.getParameter("processCode"));
        dto.setProcessName(request.getParameter("processName"));
        dto.setDescription(request.getParameter("description"));
        dto.setRemark(request.getParameter("remark"));
        service.insert(dto);
        response.sendRedirect(request.getContextPath() + "/process/list");
    }
}
