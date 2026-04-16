package WorkMgmt.WORegInq.Controller;

import java.io.IOException;
import java.sql.Date;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import WorkMgmt.WORegInq.DTO.WORegInqDTO;
import WorkMgmt.WORegInq.Service.WORegInqService;

@WebServlet("/woreginq/register")
public class WORegisterController extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private WORegInqService service = new WORegInqService();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("utf-8");

        WORegInqDTO dto = new WORegInqDTO();
        dto.setPlanId(parseInt(request.getParameter("planId")));
        dto.setEmpId(parseInt(request.getParameter("empId")));
        dto.setWorkDate(parseDate(request.getParameter("workDate")));
        dto.setWorkQty(parseInt(request.getParameter("workQty")));
        dto.setStatus(nvl(request.getParameter("status")));
        dto.setRemark(nvl(request.getParameter("remark")));

        service.insertWorkOrder(dto);
        response.sendRedirect(request.getContextPath() + "/woreginq");
    }

    private int parseInt(String value) {
        try {
            return Integer.parseInt(value);
        } catch (Exception e) {
            return 0;
        }
    }

    private Date parseDate(String value) {
        try {
            return Date.valueOf(value);
        } catch (Exception e) {
            return null;
        }
    }

    private String nvl(String str) {
        return str == null ? "" : str.trim();
    }
}
