package QualityMgmt.FPInspRegInq.Controller;

import java.io.IOException;
import java.sql.Date;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import QualityMgmt.FPInspRegInq.DTO.FPInspRegInqDTO;
import QualityMgmt.FPInspRegInq.Service.FPInspRegInqService;

@WebServlet("/fpInspRegInq")
public class FPInspRegInqServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    @Override protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException { forwardList(request, response, new FPInspRegInqDTO(), null); }
    @Override protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException { request.setCharacterEncoding("utf-8"); String cmd = request.getParameter("cmd"); if (cmd == null || "".equals(cmd)) cmd = "list"; try { if ("register".equals(cmd)) register(request, response); else if ("delete".equals(cmd)) delete(request, response); else if ("detail".equals(cmd)) detail(request, response); else list(request, response); } catch (Exception e) { throw new ServletException("FPInspRegInq 처리 중 오류 발생", e); } }
    private void list(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException { FPInspRegInqDTO searchDTO = new FPInspRegInqDTO(); searchDTO.setSearchType(request.getParameter("searchType")); searchDTO.setKeyword(request.getParameter("keyword")); searchDTO.setResultType(request.getParameter("resultType")); String startDateStr = request.getParameter("startDate"); String endDateStr = request.getParameter("endDate"); if (startDateStr != null && !"".equals(startDateStr)) searchDTO.setStartDate(Date.valueOf(startDateStr)); if (endDateStr != null && !"".equals(endDateStr)) searchDTO.setEndDate(Date.valueOf(endDateStr)); forwardList(request, response, searchDTO, null); }
    private void register(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException { FPInspRegInqDTO dto = new FPInspRegInqDTO(); dto.setResultId(Integer.parseInt(request.getParameter("resultId"))); dto.setInspectQty(parseDouble(request.getParameter("inspectQty"))); dto.setResult(request.getParameter("result")); dto.setRemark(request.getParameter("remark")); String inspectionDate = request.getParameter("inspectionDate"); dto.setInspectionDate((inspectionDate == null || "".equals(inspectionDate)) ? new Date(System.currentTimeMillis()) : Date.valueOf(inspectionDate)); new FPInspRegInqService().register(dto); response.sendRedirect(request.getContextPath() + "/fpInspRegInq"); }
    private void delete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException { new FPInspRegInqService().delete(parseIntArray(request.getParameterValues("finalInspectionIds"))); response.sendRedirect(request.getContextPath() + "/fpInspRegInq"); }
    private void detail(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException { int id = Integer.parseInt(request.getParameter("finalInspectionId")); FPInspRegInqDTO dto = new FPInspRegInqService().getFPInspRegInqDetail(id); forwardList(request, response, new FPInspRegInqDTO(), dto); }
    private void forwardList(HttpServletRequest request, HttpServletResponse response, FPInspRegInqDTO searchDTO, FPInspRegInqDTO detailDTO) throws ServletException, IOException { request.setCharacterEncoding("utf-8"); response.setContentType("text/html; charset=utf-8;"); List<FPInspRegInqDTO> list = new FPInspRegInqService().getFPInspRegInqList(searchDTO); request.setAttribute("fpInspRegInqList", list); request.setAttribute("fpInspRegInqSearchDTO", searchDTO); if (detailDTO != null) request.setAttribute("fpInspRegInqDTO", detailDTO); request.setAttribute("pageId", "page-quality-final-inspection"); request.setAttribute("pageTitle", "완제품 검사 등록 / 조회"); request.setAttribute("pageSubTitle", "완제품 검사 내역을 등록하고 조회하는 화면"); request.setAttribute("contentPage", "/WEB-INF/views/FPInspRegInq.jsp"); request.getRequestDispatcher("/WEB-INF/views/table.jsp").forward(request, response); }
    private int[] parseIntArray(String[] values) { if (values == null) return new int[0]; int[] tmp = new int[values.length]; int idx = 0; for (String v : values) if (v != null && !"".equals(v)) tmp[idx++] = Integer.parseInt(v); int[] r = new int[idx]; System.arraycopy(tmp, 0, r, 0, idx); return r; }
    private double parseDouble(String value) { return (value == null || "".equals(value)) ? 0 : Double.parseDouble(value); }
}
