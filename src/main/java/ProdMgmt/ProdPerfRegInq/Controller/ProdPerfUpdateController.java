package ProdMgmt.ProdPerfRegInq.Controller;

import java.io.IOException;
import java.sql.Date;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import ProdMgmt.ProdPerfRegInq.DTO.ProdPerfRegInqDTO;
import ProdMgmt.ProdPerfRegInq.Service.ProdPerfRegInqService;

@WebServlet("/prodperf/update")
public class ProdPerfUpdateController extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private ProdPerfRegInqService service = new ProdPerfRegInqService();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("utf-8");

        ProdPerfRegInqDTO dto = new ProdPerfRegInqDTO();
        dto.setSeqNO(parseInt(request.getParameter("seqNO")));
        dto.setWorkOrderId(parseInt(request.getParameter("workOrderId")));
        dto.setResultDate(parseDate(request.getParameter("resultDate")));
        dto.setLotNo(nvl(request.getParameter("lotNo")));
        dto.setProducedQty(parseInt(request.getParameter("producedQty")));
        dto.setLossQty(parseInt(request.getParameter("lossQty")));
        dto.setStatus("완료");
        dto.setRemark(nvl(request.getParameter("remark")));

        try {
            service.updateProductionResult(dto);
        } catch (IllegalArgumentException e) {
            HttpSession session = request.getSession();
            session.setAttribute("errorMsg", e.getMessage());
        }
        response.sendRedirect(request.getContextPath() + "/prodperf/detail?seqNO=" + dto.getSeqNO());
    }

    private int parseInt(String value) { try { return Integer.parseInt(value); } catch (Exception e) { return 0; } }
    private Date parseDate(String value) { try { return Date.valueOf(value); } catch (Exception e) { return null; } }
    private String nvl(String str) { return str == null ? "" : str.trim(); }
}
