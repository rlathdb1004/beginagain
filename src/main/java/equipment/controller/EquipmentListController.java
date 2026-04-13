package equipment.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import equipment.dto.EquipmentDTO;
import equipment.service.EquipmentService;

@WebServlet("/equipment/list")
public class EquipmentListController extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private EquipmentService equipmentService = new EquipmentService();

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		List<EquipmentDTO> equipmentList = equipmentService.getEquipmentList();

		request.setAttribute("equipmentList", equipmentList);
		request.getRequestDispatcher("/WEB-INF/views/equipment/equipmentList.jsp").forward(request, response);
	}
}