package MasterDataMgmt.BOMManagement;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import MasterDataMgmt.ItemMgmt.ItemMgmtDTO;
import MasterDataMgmt.ItemMgmt.ItemMgmtSearchDTO;
import MasterDataMgmt.ItemMgmt.ItemMgmtService;


@WebServlet("/BOM-mgmt")
public class BOMMgmtCon extends HttpServlet {

    private BOMMgmtService service = new BOMMgmtService();
    
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("utf-8");
       
        String productCode = request.getParameter("product_code");

        BOMMgmtSearchDTO searchDTO = new BOMMgmtSearchDTO();
        searchDTO.setProduct_code(productCode); 

        List<BOMMgmtDTO> list = service.getBOMList(searchDTO);

        request.setAttribute("BOMList", list);
        request.setAttribute("contentPage", "/WEB-INF/views/item/BOMMgmt.jsp");

        request.getRequestDispatcher("/WEB-INF/views/table.jsp")
               .forward(request, response);
    }
    
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("utf-8");

        String productCode = request.getParameter("product_code");
        String materialCode = request.getParameter("material_code");
        String unit = request.getParameter("unit");
        String qty = request.getParameter("qty_required");
        String remark = request.getParameter("remark");

        BOMMgmtDTO dto = new BOMMgmtDTO();

        dto.setProduct_code(productCode);
        dto.setMaterial_code(materialCode);
        dto.setUnit(unit);
        dto.setQty_required(Double.parseDouble(qty));
        dto.setRemark(remark);

        service.insert(dto);

        response.sendRedirect(request.getContextPath() + "/BOM-mgmt");
    }
}


