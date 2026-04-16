package ProdMgmt.ProdPerfRegInq.Controller;

import java.io.IOException;
import java.sql.Date;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import ProdMgmt.ProdPerfRegInq.DTO.ProdPerfRegInqDTO;
import ProdMgmt.ProdPerfRegInq.Service.ProdPerfRegInqService;

@WebServlet("/prodperf/register")
public class ProdPerfRegisterController extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private ProdPerfRegInqService service = new ProdPerfRegInqService();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("utf-8");

        ProdPerfRegInqDTO dto = new ProdPerfRegInqDTO();
        dto.setWorkOrderId(parseInt(request.getParameter("workOrderId")));
        dto.setResultDate(parseDate(request.getParameter("resultDate")));
        dto.setLotNo(nvl(request.getParameter("lotNo")));
        dto.setProducedQty(parseInt(request.getParameter("producedQty")));
        dto.setLossQty(parseInt(request.getParameter("lossQty")));
        dto.setStatus(nvl(request.getParameter("status")));
        dto.setRemark(nvl(request.getParameter("remark")));

        service.insertProductionResult(dto);
        response.sendRedirect(request.getContextPath() + "/prodperf");
    }

    private int parseInt(String value) { try { return Integer.parseInt(value); } catch (Exception e) { return 0; } }
    private Date parseDate(String value) { try { return Date.valueOf(value); } catch (Exception e) { return null; } }
    private String nvl(String str) { return str == null ? "" : str.trim(); }
}
