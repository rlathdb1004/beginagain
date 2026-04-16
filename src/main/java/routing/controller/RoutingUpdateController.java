package routing.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import routing.dto.RoutingDTO;
import routing.service.RoutingService;

@WebServlet("/routing/update")
public class RoutingUpdateController extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private RoutingService routingService = new RoutingService();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        RoutingDTO dto = new RoutingDTO();
        dto.setRoutingId(Integer.parseInt(request.getParameter("routingId")));
        dto.setItemId(Integer.parseInt(request.getParameter("itemId")));
        dto.setProcessId(Integer.parseInt(request.getParameter("processId")));
        dto.setEquipmentId(Integer.parseInt(request.getParameter("equipmentId")));
        dto.setProcessSeq(Integer.parseInt(request.getParameter("processSeq")));
        dto.setRemark(request.getParameter("remark"));

        routingService.updateRouting(dto);

        response.sendRedirect(request.getContextPath() + "/routing/detail?routingId=" + dto.getRoutingId());
    }
}
