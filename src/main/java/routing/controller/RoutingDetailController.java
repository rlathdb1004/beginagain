package routing.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import equipment.dto.EquipmentDTO;
import equipment.service.EquipmentService;
import item.dto.ItemDTO;
import item.service.ItemService;
import process.dto.ProcessDTO;
import process.service.ProcessService;
import routing.dto.RoutingDTO;
import routing.service.RoutingService;

@WebServlet("/routing/detail")
public class RoutingDetailController extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private RoutingService routingService = new RoutingService();
    private ItemService itemService = new ItemService();
    private ProcessService processService = new ProcessService();
    private EquipmentService equipmentService = new EquipmentService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String routingIdStr = request.getParameter("routingId");

        if (routingIdStr == null || "".equals(routingIdStr)) {
            response.sendRedirect(request.getContextPath() + "/routing/list");
            return;
        }

        int routingId = Integer.parseInt(routingIdStr);
        RoutingDTO routing = routingService.getRoutingById(routingId);

        if (routing == null) {
            response.sendRedirect(request.getContextPath() + "/routing/list");
            return;
        }

        List<ItemDTO> itemList = itemService.getItemList();
        List<ProcessDTO> processList = processService.getList();
        List<EquipmentDTO> equipmentList = equipmentService.getEquipmentList();

        request.setAttribute("routing", routing);
        request.setAttribute("itemList", itemList);
        request.setAttribute("processList", processList);
        request.setAttribute("equipmentList", equipmentList);

        request.setAttribute("pageTitle", "라우팅관리");
        request.setAttribute("pageSubTitle", "라우팅 상세 / 수정");
        request.setAttribute("contentPage", "/WEB-INF/views/routing/routingDetail.jsp");
        request.getRequestDispatcher("/WEB-INF/views/table.jsp").forward(request, response);
    }
}
