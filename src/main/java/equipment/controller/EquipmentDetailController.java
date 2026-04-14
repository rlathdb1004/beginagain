package equipment.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import equipment.dto.EquipmentDTO;
import equipment.service.EquipmentService;

@WebServlet("/equipment/detail")
public class EquipmentDetailController extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private EquipmentService equipmentService = new EquipmentService();

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String equipmentIdStr = request.getParameter("equipmentId");

		if (equipmentIdStr == null || "".equals(equipmentIdStr)) {
			response.sendRedirect(request.getContextPath() + "/equipment/list");
			return;
		}

		int equipmentId = Integer.parseInt(equipmentIdStr);
		EquipmentDTO equipment = equipmentService.getEquipmentById(equipmentId);

		if (equipment == null) {
			response.sendRedirect(request.getContextPath() + "/equipment/list");
			return;
		}

		request.setAttribute("equipment", equipment);
		request.setAttribute("pageTitle", "설비관리");
		request.setAttribute("pageSubTitle", "설비 상세 / 수정");
		request.setAttribute("contentPage", "/WEB-INF/views/equipment/equipmentDetail.jsp");
		request.getRequestDispatcher("/WEB-INF/views/table.jsp").forward(request, response);
	}
}