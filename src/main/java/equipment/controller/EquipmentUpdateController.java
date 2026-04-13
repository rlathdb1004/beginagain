package equipment.controller;

import java.io.IOException;
import java.sql.Date;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import equipment.dto.EquipmentDTO;
import equipment.service.EquipmentService;

@WebServlet("/equipment/update")
public class EquipmentUpdateController extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private EquipmentService equipmentService = new EquipmentService();

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		request.setCharacterEncoding("UTF-8");

		String equipmentIdStr = request.getParameter("equipmentId");
		String equipmentName = request.getParameter("equipmentName");
		String modelName = request.getParameter("modelName");
		String location = request.getParameter("location");
		String manufacturer = request.getParameter("manufacturer");
		String vendorName = request.getParameter("vendorName");
		String equipmentPriceStr = request.getParameter("equipmentPrice");
		String purchaseDateStr = request.getParameter("purchaseDate");
		String remark = request.getParameter("remark");

		if (equipmentIdStr == null || "".equals(equipmentIdStr)) {
			response.sendRedirect(request.getContextPath() + "/equipment/list");
			return;
		}

		double equipmentPrice = 0;
		if (equipmentPriceStr != null && !"".equals(equipmentPriceStr.trim())) {
			equipmentPrice = Double.parseDouble(equipmentPriceStr);
		}

		Date purchaseDate = null;
		if (purchaseDateStr != null && !"".equals(purchaseDateStr.trim())) {
			purchaseDate = Date.valueOf(purchaseDateStr);
		}

		EquipmentDTO dto = new EquipmentDTO();
		dto.setEquipmentId(Integer.parseInt(equipmentIdStr));
		dto.setEquipmentName(equipmentName);
		dto.setModelName(modelName);
		dto.setLocation(location);
		dto.setManufacturer(manufacturer);
		dto.setVendorName(vendorName);
		dto.setEquipmentPrice(equipmentPrice);
		dto.setPurchaseDate(purchaseDate);
		dto.setRemark(remark);

		boolean result = equipmentService.updateEquipment(dto);

		if (result) {
			response.sendRedirect(request.getContextPath() + "/equipment/detail?equipmentId=" + dto.getEquipmentId());
		} else {
			response.sendRedirect(request.getContextPath() + "/equipment/list");
		}
	}
}