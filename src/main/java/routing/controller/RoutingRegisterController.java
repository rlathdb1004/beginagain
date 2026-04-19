package routing.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import routing.dto.RoutingDTO;
import routing.service.RoutingService;

@WebServlet("/routing/register")
public class RoutingRegisterController extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private RoutingService routingService = new RoutingService();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        int itemId = Integer.parseInt(request.getParameter("itemId"));

        RoutingDTO dto = new RoutingDTO();
        dto.setItemId(itemId);
        dto.setProcessId(Integer.parseInt(request.getParameter("processId")));
        dto.setEquipmentId(Integer.parseInt(request.getParameter("equipmentId")));
        dto.setProcessSeq(Integer.parseInt(request.getParameter("processSeq")));
        dto.setRemark(request.getParameter("remark"));

        try {
            routingService.insertRouting(dto);
        } catch (RuntimeException e) {
            request.getSession().setAttribute("errorMessage", e.getMessage());
        }

        response.sendRedirect(request.getContextPath() + "/routing/list?itemId=" + itemId);
    }
}
