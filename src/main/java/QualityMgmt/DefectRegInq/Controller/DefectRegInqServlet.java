package QualityMgmt.DefectRegInq.Controller;

import java.io.IOException;
import java.sql.Date;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import QualityMgmt.DefectRegInq.DTO.DefectRegInqDTO;
import QualityMgmt.DefectRegInq.Service.DefectRegInqService;

@WebServlet("/defectRegInq")
public class DefectRegInqServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    @Override protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException { forwardList(request, response, new DefectRegInqDTO(), null); }
    @Override protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException { request.setCharacterEncoding("utf-8"); String cmd = request.getParameter("cmd"); if (cmd == null || "".equals(cmd)) cmd = "list"; try { if ("register".equals(cmd)) register(request, response); else if ("delete".equals(cmd)) delete(request, response); else if ("detail".equals(cmd)) detail(request, response); else list(request, response); } catch (Exception e) { throw new ServletException("DefectRegInq 처리 중 오류 발생", e); } }
    private void list(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException { DefectRegInqDTO searchDTO = new DefectRegInqDTO(); searchDTO.setSearchType(request.getParameter("searchType")); searchDTO.setKeyword(request.getParameter("keyword")); searchDTO.setDefectTypeSearch(request.getParameter("defectTypeSearch")); String startDateStr = request.getParameter("startDate"); String endDateStr = request.getParameter("endDate"); if (startDateStr != null && !"".equals(startDateStr)) searchDTO.setStartDate(Date.valueOf(startDateStr)); if (endDateStr != null && !"".equals(endDateStr)) searchDTO.setEndDate(Date.valueOf(endDateStr)); forwardList(request, response, searchDTO, null); }
    private void register(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException { DefectRegInqDTO dto = new DefectRegInqDTO(); dto.setFinalInspectionId(Integer.parseInt(request.getParameter("finalInspectionId"))); dto.setDefectCodeId(Integer.parseInt(request.getParameter("defectCodeId"))); dto.setRemark(request.getParameter("remark")); new DefectRegInqService().register(dto); response.sendRedirect(request.getContextPath() + "/defectRegInq"); }
    private void delete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException { new DefectRegInqService().delete(parseIntArray(request.getParameterValues("defectProductIds"))); response.sendRedirect(request.getContextPath() + "/defectRegInq"); }
    private void detail(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException { int id = Integer.parseInt(request.getParameter("defectProductId")); DefectRegInqDTO dto = new DefectRegInqService().getDefectRegInqDetail(id); forwardList(request, response, new DefectRegInqDTO(), dto); }
    private void forwardList(HttpServletRequest request, HttpServletResponse response, DefectRegInqDTO searchDTO, DefectRegInqDTO detailDTO) throws ServletException, IOException { request.setCharacterEncoding("utf-8"); response.setContentType("text/html; charset=utf-8;"); List<DefectRegInqDTO> list = new DefectRegInqService().getDefectRegInqList(searchDTO); request.setAttribute("defectRegInqList", list); request.setAttribute("defectRegInqSearchDTO", searchDTO); if (detailDTO != null) request.setAttribute("defectRegInqDTO", detailDTO); request.setAttribute("pageId", "page-quality-defect"); request.setAttribute("pageTitle", "불량 등록 / 조회"); request.setAttribute("pageSubTitle", "불량 내역을 등록하고 조회하는 화면"); request.setAttribute("contentPage", "/WEB-INF/views/DefectRegInq.jsp"); request.getRequestDispatcher("/WEB-INF/views/table.jsp").forward(request, response); }
    private int[] parseIntArray(String[] values) { if (values == null) return new int[0]; int[] tmp = new int[values.length]; int idx = 0; for (String v : values) if (v != null && !"".equals(v)) tmp[idx++] = Integer.parseInt(v); int[] r = new int[idx]; System.arraycopy(tmp, 0, r, 0, idx); return r; }
    private double parseDouble(String value) { return (value == null || "".equals(value)) ? 0 : Double.parseDouble(value); }
}
