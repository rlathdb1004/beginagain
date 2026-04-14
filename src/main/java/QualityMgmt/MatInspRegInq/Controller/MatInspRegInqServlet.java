package QualityMgmt.MatInspRegInq.Controller;

import java.io.IOException;
import java.sql.Date;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import QualityMgmt.MatInspRegInq.DTO.MatInspRegInqDTO;
import QualityMgmt.MatInspRegInq.Service.MatInspRegInqService;

@WebServlet("/matInspRegInq")
public class MatInspRegInqServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    @Override protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException { forwardList(request, response, new MatInspRegInqDTO(), null); }
    @Override protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException { request.setCharacterEncoding("utf-8"); String cmd = request.getParameter("cmd"); if (cmd == null || "".equals(cmd)) cmd = "list"; try { if ("register".equals(cmd)) register(request, response); else if ("delete".equals(cmd)) delete(request, response); else if ("detail".equals(cmd)) detail(request, response); else list(request, response); } catch (Exception e) { throw new ServletException("MatInspRegInq 처리 중 오류 발생", e); } }
    private void list(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException { MatInspRegInqDTO searchDTO = new MatInspRegInqDTO(); searchDTO.setSearchType(request.getParameter("searchType")); searchDTO.setKeyword(request.getParameter("keyword")); searchDTO.setResultType(request.getParameter("resultType")); String startDateStr = request.getParameter("startDate"); String endDateStr = request.getParameter("endDate"); if (startDateStr != null && !"".equals(startDateStr)) searchDTO.setStartDate(Date.valueOf(startDateStr)); if (endDateStr != null && !"".equals(endDateStr)) searchDTO.setEndDate(Date.valueOf(endDateStr)); forwardList(request, response, searchDTO, null); }
    private void register(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException { MatInspRegInqDTO dto = new MatInspRegInqDTO(); dto.setItemCode(request.getParameter("itemCode")); dto.setInspectQty(parseDouble(request.getParameter("inspectQty"))); dto.setGoodQty(parseDouble(request.getParameter("goodQty"))); dto.setDefectQty(parseDouble(request.getParameter("defectQty"))); dto.setResult(request.getParameter("result")); dto.setRemark(request.getParameter("remark")); String inspectionDate = request.getParameter("inspectionDate"); dto.setInspectionDate((inspectionDate == null || "".equals(inspectionDate)) ? new Date(System.currentTimeMillis()) : Date.valueOf(inspectionDate)); new MatInspRegInqService().register(dto); response.sendRedirect(request.getContextPath() + "/matInspRegInq"); }
    private void delete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException { new MatInspRegInqService().delete(parseIntArray(request.getParameterValues("materialInspectionIds"))); response.sendRedirect(request.getContextPath() + "/matInspRegInq"); }
    private void detail(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException { int id = Integer.parseInt(request.getParameter("materialInspectionId")); MatInspRegInqDTO dto = new MatInspRegInqService().getMatInspRegInqDetail(id); forwardList(request, response, new MatInspRegInqDTO(), dto); }
    private void forwardList(HttpServletRequest request, HttpServletResponse response, MatInspRegInqDTO searchDTO, MatInspRegInqDTO detailDTO) throws ServletException, IOException { request.setCharacterEncoding("utf-8"); response.setContentType("text/html; charset=utf-8;"); List<MatInspRegInqDTO> list = new MatInspRegInqService().getMatInspRegInqList(searchDTO); request.setAttribute("matInspRegInqList", list); request.setAttribute("matInspRegInqSearchDTO", searchDTO); if (detailDTO != null) request.setAttribute("matInspRegInqDTO", detailDTO); request.setAttribute("pageId", "page-quality-material-inspection"); request.setAttribute("pageTitle", "자재 검사 등록 / 조회"); request.setAttribute("pageSubTitle", "자재 검사 내역을 등록하고 조회하는 화면"); request.setAttribute("contentPage", "/WEB-INF/views/MatInspRegInq.jsp"); request.getRequestDispatcher("/WEB-INF/views/table.jsp").forward(request, response); }
    private int[] parseIntArray(String[] values) { if (values == null) return new int[0]; int[] tmp = new int[values.length]; int idx = 0; for (String v : values) if (v != null && !"".equals(v)) tmp[idx++] = Integer.parseInt(v); int[] r = new int[idx]; System.arraycopy(tmp, 0, r, 0, idx); return r; }
    private double parseDouble(String value) { return (value == null || "".equals(value)) ? 0 : Double.parseDouble(value); }
}
