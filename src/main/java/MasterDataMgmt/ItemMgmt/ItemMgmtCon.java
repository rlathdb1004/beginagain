package MasterDataMgmt.ItemMgmt;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet("/master-item")
public class ItemMgmtCon extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("utf-8");
        response.setContentType("text/html; charset=utf-8");

        String type = request.getParameter("itemType");
        String dateType = request.getParameter("dateType");
        String startDate = request.getParameter("startDate");
        String endDate = request.getParameter("endDate");
        String field = request.getParameter("searchField");
        String keyword = request.getParameter("keyword");

        ItemMgmtSearchDTO dto = new ItemMgmtSearchDTO();
        dto.setType(type);
        dto.setDateType(dateType);
        dto.setStartDate(startDate);
        dto.setEndDate(endDate);
        dto.setField(field);
        dto.setKeyword(keyword);

        ItemMgmtService service = new ItemMgmtService();
        List<ItemMgmtDTO> list = service.getItemList(dto);

        HttpSession session = request.getSession();
        Object errorMsg = session.getAttribute("errorMsg");
        if (errorMsg != null) {
            request.setAttribute("errorMsg", errorMsg.toString());
            session.removeAttribute("errorMsg");
        }

        request.setAttribute("itemList", list);
        request.setAttribute("contentPage", "/WEB-INF/views/item/ItemMgmt.jsp");
        request.setAttribute("pageTitle", "품목 관리");
        request.setAttribute("pageSubTitle", "품목 조회 및 등록");

        request.getRequestDispatcher("/WEB-INF/views/table.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("utf-8");
        response.setContentType("text/html; charset=utf-8");

        String item_code = trim(request.getParameter("item_code"));
        String item_name = trim(request.getParameter("item_name"));
        String item_type = trim(request.getParameter("item_type"));
        String unit = trim(request.getParameter("unit"));
        String spec = trim(request.getParameter("spec"));
        String supplier_name = trim(request.getParameter("supplier_name"));
        String use_yn = trim(request.getParameter("use_yn"));
        String remark = trim(request.getParameter("remark"));

        int safety_stock = 0;
        String safetyStockParam = trim(request.getParameter("safety_stock"));
        if (safetyStockParam != null && !safetyStockParam.isEmpty()) {
            safety_stock = Integer.parseInt(safetyStockParam);
        }

        ItemMgmtDTO dto = new ItemMgmtDTO();
        dto.setItem_code(item_code);
        dto.setItem_name(item_name);
        dto.setItem_type(item_type);
        dto.setUnit(unit);
        dto.setSpec(spec);
        dto.setSupplier_name(supplier_name);
        dto.setSafety_stock(safety_stock);
        dto.setUse_yn(use_yn);
        dto.setRemark(remark);

        ItemMgmtService service = new ItemMgmtService();
        try {
            String item_id = trim(request.getParameter("item_id"));
            if (item_id != null && !item_id.isEmpty()) {
                dto.setItem_id(Integer.parseInt(item_id));
                service.update(dto);
            } else {
                service.insert(dto);
            }
        } catch (RuntimeException e) {
            request.getSession().setAttribute("errorMsg", e.getMessage());
        }
        response.sendRedirect(request.getContextPath() + "/master-item");
    }

    private String trim(String value) {
        return value == null ? null : value.trim();
    }
}
