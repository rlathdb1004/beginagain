package MaterialMgmt.InvRegInq.Controller;

import java.io.IOException;
import java.sql.Date;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import MaterialMgmt.InvRegInq.DTO.InvRegInqDTO;
import MaterialMgmt.InvRegInq.Service.InvRegInqService;

@WebServlet("/invRegInq")
public class InvRegInqServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    @Override protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException { forwardList(request, response, new InvRegInqDTO(), null); }
    @Override protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException { request.setCharacterEncoding("utf-8"); String cmd = request.getParameter("cmd"); if (cmd == null || "".equals(cmd)) cmd = "list"; try { if ("register".equals(cmd)) register(request, response); else if ("delete".equals(cmd)) delete(request, response); else if ("detail".equals(cmd)) detail(request, response); else list(request, response); } catch (Exception e) { throw new ServletException("InvRegInqServlet 처리 중 오류 발생", e); } }
    private void list(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException { InvRegInqDTO searchDTO = new InvRegInqDTO(); searchDTO.setSearchType(request.getParameter("searchType")); searchDTO.setKeyword(request.getParameter("keyword")); String startDateStr = request.getParameter("startDate"); String endDateStr = request.getParameter("endDate"); if (startDateStr != null && !"".equals(startDateStr)) searchDTO.setStartDate(Date.valueOf(startDateStr)); if (endDateStr != null && !"".equals(endDateStr)) searchDTO.setEndDate(Date.valueOf(endDateStr)); forwardList(request, response, searchDTO, null); }
    private void register(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException { InvRegInqDTO dto = new InvRegInqDTO(); dto.setItemCode(request.getParameter("itemCode")); dto.setQtyOnHand(parseDouble(request.getParameter("qtyOnHand"))); dto.setSafetyStock(parseDouble(request.getParameter("safetyStock"))); dto.setRemark(request.getParameter("remark")); new InvRegInqService().register(dto); response.sendRedirect(request.getContextPath() + "/invRegInq"); }
    private void delete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException { new InvRegInqService().delete(parseIntArray(request.getParameterValues("inventoryIds"))); response.sendRedirect(request.getContextPath() + "/invRegInq"); }
    private void detail(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException { int inventoryId = Integer.parseInt(request.getParameter("inventoryId")); InvRegInqDTO dto = new InvRegInqService().getInvRegInqDetail(inventoryId); forwardList(request, response, new InvRegInqDTO(), dto); }
    private void forwardList(HttpServletRequest request, HttpServletResponse response, InvRegInqDTO searchDTO, InvRegInqDTO detailDTO) throws ServletException, IOException { request.setCharacterEncoding("utf-8"); response.setContentType("text/html; charset=utf-8;"); List<InvRegInqDTO> list = new InvRegInqService().getInvRegInqList(searchDTO); request.setAttribute("invRegInqList", list); request.setAttribute("invRegInqSearchDTO", searchDTO); if (detailDTO != null) request.setAttribute("invRegInqDTO", detailDTO); request.setAttribute("pageId", "page-materials-inventory"); request.setAttribute("pageTitle", "재고 등록 / 조회"); request.setAttribute("pageSubTitle", "현재 재고 현황을 등록하고 조회하는 화면"); request.setAttribute("contentPage", "/WEB-INF/views/InvRegInq.jsp"); request.getRequestDispatcher("/WEB-INF/views/table.jsp").forward(request, response); }
    private int[] parseIntArray(String[] values) { if (values == null) return new int[0]; int[] tmp = new int[values.length]; int idx = 0; for (String v : values) if (v != null && !"".equals(v)) tmp[idx++] = Integer.parseInt(v); int[] r = new int[idx]; System.arraycopy(tmp, 0, r, 0, idx); return r; }
    private double parseDouble(String value) { return (value == null || "".equals(value)) ? 0 : Double.parseDouble(value); }
}
