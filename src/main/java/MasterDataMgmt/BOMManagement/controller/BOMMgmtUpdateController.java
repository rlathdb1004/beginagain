package MasterDataMgmt.BOMManagement.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import MasterDataMgmt.BOMManagement.dto.BOMMgmtDTO;
import MasterDataMgmt.BOMManagement.service.BOMMgmtService;

@WebServlet("/bom-update")
public class BOMMgmtUpdateController extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        HttpSession session = request.getSession();
        BOMMgmtDTO dto = new BOMMgmtDTO();
        dto.setBom_detail_id(Integer.parseInt(request.getParameter("bomDetailId")));
        dto.setQty_required(Double.parseDouble(request.getParameter("qtyRequired")));
        dto.setRemark(request.getParameter("remark"));
        try {
            BOMMgmtService.updateBom(dto);
        } catch (Exception e) {
            session.setAttribute("errorMsg", e.getMessage());
        }
        response.sendRedirect(request.getContextPath() + "/bom-detail?bomDetailId=" + dto.getBom_detail_id());
    }
}
