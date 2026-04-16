package routing.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import routing.service.RoutingService;

@WebServlet("/routing/delete")
public class RoutingDeleteController extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private RoutingService routingService = new RoutingService();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        String[] routingIds = request.getParameterValues("routingId");
        String itemId = request.getParameter("itemId");

        if (routingIds == null || routingIds.length == 0) {
            response.sendRedirect(request.getContextPath() + "/routing/list?itemId=" + itemId);
            return;
        }

        routingService.deleteRouting(routingIds);

        response.sendRedirect(request.getContextPath() + "/routing/list?itemId=" + itemId);
    }
}
