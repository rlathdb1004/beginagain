package equipment.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import equipment.service.EquipmentService;

@WebServlet("/equipment/delete")
public class EquipmentDeleteController extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private EquipmentService equipmentService = new EquipmentService();

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		request.setCharacterEncoding("UTF-8");

		String[] equipmentIds = request.getParameterValues("equipmentId");

		if (equipmentIds == null || equipmentIds.length == 0) {
			response.sendRedirect(request.getContextPath() + "/equipment/list");
			return;
		}

		boolean result = equipmentService.deleteEquipment(equipmentIds);

		if (result) {
			response.sendRedirect(request.getContextPath() + "/equipment/list");
		} else {
			response.sendRedirect(request.getContextPath() + "/equipment/list");
		}
	}
}