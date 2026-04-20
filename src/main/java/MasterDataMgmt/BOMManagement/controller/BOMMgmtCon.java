package MasterDataMgmt.BOMManagement.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import MasterDataMgmt.BOMManagement.dto.BOMMgmtDTO;
import MasterDataMgmt.BOMManagement.dto.BOMMgmtSearchDTO;
import MasterDataMgmt.BOMManagement.service.BOMMgmtService;
import MasterDataMgmt.ItemMgmt.ItemMgmtDTO;

@WebServlet("/BOM-mgmt")
public class BOMMgmtCon extends HttpServlet {
    private BOMMgmtService service = new BOMMgmtService();

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("utf-8");
        String productCode = request.getParameter("product_code");
        if (productCode == null || productCode.trim().isEmpty()) {
            productCode = request.getParameter("keyword");
        }
        BOMMgmtSearchDTO searchDTO = new BOMMgmtSearchDTO();
        if (productCode != null && !productCode.trim().isEmpty()) {
            searchDTO.setProduct_code(productCode);
        }
        List<BOMMgmtDTO> list = java.util.Collections.emptyList();
        if (searchDTO.getProduct_code() != null && !searchDTO.getProduct_code().trim().isEmpty()) {
            list = service.getBOMList(searchDTO);
        }
        List<ItemMgmtDTO> productItems = service.getProductItems();
        List<ItemMgmtDTO> materialItems = service.getMaterialItems();
        request.setAttribute("list", list);
        request.setAttribute("keyword", productCode);
        request.setAttribute("productItems", productItems);
        request.setAttribute("materialItems", materialItems);
        request.setAttribute("contentPage", "/WEB-INF/views/item/BOMMgmt.jsp");
        request.setAttribute("pageTitle", "BOM 관리");
        request.setAttribute("pageSubTitle", "BOM 조회 및 등록");
        request.getRequestDispatcher("/WEB-INF/views/table.jsp").forward(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("utf-8");
        HttpSession session = request.getSession();
        try {
            String productCode = request.getParameter("product_code");
        if (productCode == null || productCode.trim().isEmpty()) {
            productCode = request.getParameter("keyword");
        }
            String materialId = request.getParameter("material_id");
            String qty = request.getParameter("qty_required");
            String remark = request.getParameter("remark");
            BOMMgmtDTO dto = new BOMMgmtDTO();
            dto.setProduct_code(productCode);
            dto.setMaterial_id(materialId == null || materialId.isEmpty() ? 0 : Integer.parseInt(materialId));
            dto.setQty_required(qty == null || qty.isEmpty() ? 0 : Double.parseDouble(qty));
            dto.setRemark(remark);
            service.insert(dto);
        } catch (Exception e) {
            session.setAttribute("errorMsg", e.getMessage());
        }
        response.sendRedirect(request.getContextPath() + "/BOM-mgmt?product_code=" + (request.getParameter("product_code") == null ? "" : request.getParameter("product_code")));
    }
}
