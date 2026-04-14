package MaterialMgmt.IORegInq.Controller;

import java.io.IOException;
import java.sql.Date;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import MaterialMgmt.IORegInq.DTO.IORegInqDTO;
import MaterialMgmt.IORegInq.Service.IORegInqService;

@WebServlet("/ioRegInq")
public class IORegInqServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    @Override protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException { forwardList(request, response, new IORegInqDTO(), null); }
    @Override protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException { request.setCharacterEncoding("utf-8"); String cmd = request.getParameter("cmd"); if (cmd == null || "".equals(cmd)) cmd = "list"; try { if ("register".equals(cmd)) register(request, response); else if ("delete".equals(cmd)) delete(request, response); else if ("detail".equals(cmd)) detail(request, response); else list(request, response); } catch (Exception e) { throw new ServletException("IORegInqServlet 처리 중 오류 발생", e); } }
    private void list(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException { IORegInqDTO searchDTO = new IORegInqDTO(); searchDTO.setInoutType(request.getParameter("inoutType")); searchDTO.setSearchType(request.getParameter("searchType")); searchDTO.setKeyword(request.getParameter("keyword")); String startDateStr = request.getParameter("startDate"); String endDateStr = request.getParameter("endDate"); if (startDateStr != null && !"".equals(startDateStr)) searchDTO.setStartDate(Date.valueOf(startDateStr)); if (endDateStr != null && !"".equals(endDateStr)) searchDTO.setEndDate(Date.valueOf(endDateStr)); forwardList(request, response, searchDTO, null); }
    private void register(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException { IORegInqDTO dto = new IORegInqDTO(); dto.setItemCode(request.getParameter("itemCode")); dto.setInoutType(request.getParameter("inoutType")); dto.setQty(parseDouble(request.getParameter("qty"))); dto.setUnit(request.getParameter("unit")); dto.setStatus(request.getParameter("status")); dto.setRemark(request.getParameter("remark")); String inoutDate = request.getParameter("inoutDate"); dto.setInoutDate((inoutDate == null || "".equals(inoutDate)) ? new Date(System.currentTimeMillis()) : Date.valueOf(inoutDate)); new IORegInqService().register(dto); response.sendRedirect(request.getContextPath() + "/ioRegInq"); }
    private void delete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException { new IORegInqService().delete(parseIntArray(request.getParameterValues("inoutIds"))); response.sendRedirect(request.getContextPath() + "/ioRegInq"); }
    private void detail(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException { int inoutId = Integer.parseInt(request.getParameter("inoutId")); IORegInqDTO dto = new IORegInqService().getIORegInqDetail(inoutId); forwardList(request, response, new IORegInqDTO(), dto); }
    private void forwardList(HttpServletRequest request, HttpServletResponse response, IORegInqDTO searchDTO, IORegInqDTO detailDTO) throws ServletException, IOException { request.setCharacterEncoding("utf-8"); response.setContentType("text/html; charset=utf-8;"); List<IORegInqDTO> list = new IORegInqService().getIORegInqList(searchDTO); request.setAttribute("ioRegInqList", list); request.setAttribute("ioRegInqSearchDTO", searchDTO); if (detailDTO != null) request.setAttribute("ioRegInqDTO", detailDTO); request.setAttribute("pageId", "page-materials-register"); request.setAttribute("pageTitle", "입출고 등록 / 조회"); request.setAttribute("pageSubTitle", "자재 입출고 내역을 등록하고 조회하는 화면"); request.setAttribute("contentPage", "/WEB-INF/views/IORegInq.jsp"); request.getRequestDispatcher("/WEB-INF/views/table.jsp").forward(request, response); }
    private int[] parseIntArray(String[] values) { if (values == null) return new int[0]; int[] tmp = new int[values.length]; int idx = 0; for (String v : values) if (v != null && !"".equals(v)) tmp[idx++] = Integer.parseInt(v); int[] r = new int[idx]; System.arraycopy(tmp, 0, r, 0, idx); return r; }
    private double parseDouble(String value) { return (value == null || "".equals(value)) ? 0 : Double.parseDouble(value); }
}
