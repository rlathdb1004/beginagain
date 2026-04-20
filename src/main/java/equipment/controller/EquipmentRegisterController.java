
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

@WebServlet("/equipment/register")
public class EquipmentRegisterController extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private EquipmentService equipmentService = new EquipmentService();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        double equipmentPrice = 0;
        String equipmentPriceStr = request.getParameter("equipmentPrice");
        if (equipmentPriceStr != null && !"".equals(equipmentPriceStr.trim())) {
            equipmentPrice = Double.parseDouble(equipmentPriceStr);
        }
        Date purchaseDate = null;
        String purchaseDateStr = request.getParameter("purchaseDate");
        if (purchaseDateStr != null && !"".equals(purchaseDateStr.trim())) {
            purchaseDate = Date.valueOf(purchaseDateStr);
        }
        EquipmentDTO dto = new EquipmentDTO();
        dto.setEquipmentName(request.getParameter("equipmentName"));
        dto.setModelName(request.getParameter("modelName"));
        dto.setLocation(request.getParameter("location"));
        dto.setManufacturer(request.getParameter("manufacturer"));
        dto.setVendorName(request.getParameter("vendorName"));
        dto.setEquipmentPrice(equipmentPrice);
        dto.setPurchaseDate(purchaseDate);
        dto.setRemark(request.getParameter("remark"));
        equipmentService.insertEquipment(dto);
        response.sendRedirect(request.getContextPath() + "/equipment/list");
    }
}
