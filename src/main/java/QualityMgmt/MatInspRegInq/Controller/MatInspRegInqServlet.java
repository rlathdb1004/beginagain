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

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String id = request.getParameter("materialInspectionId");
        if (id != null && !"".equals(id)) {
            detail(request, response);
            return;
        }
        forwardList(request, response, new MatInspRegInqDTO());
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("utf-8");
        String cmd = request.getParameter("cmd");
        if (cmd == null || "".equals(cmd)) cmd = "list";
        try {
            if ("register".equals(cmd)) register(request, response);
            else if ("update".equals(cmd)) update(request, response);
            else if ("delete".equals(cmd)) delete(request, response);
            else if ("detail".equals(cmd)) detail(request, response);
            else list(request, response);
        } catch (Exception e) {
            throw new ServletException("MatInspRegInq 처리 중 오류 발생", e);
        }
    }

    private void list(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        MatInspRegInqDTO searchDTO = new MatInspRegInqDTO();
        searchDTO.setSearchType(request.getParameter("searchType"));
        searchDTO.setKeyword(request.getParameter("keyword"));
        searchDTO.setResultType(request.getParameter("resultType"));
        String startDateStr = request.getParameter("startDate");
        String endDateStr = request.getParameter("endDate");
        if (startDateStr != null && !"".equals(startDateStr)) searchDTO.setStartDate(Date.valueOf(startDateStr));
        if (endDateStr != null && !"".equals(endDateStr)) searchDTO.setEndDate(Date.valueOf(endDateStr));
        forwardList(request, response, searchDTO);
    }

    private void register(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        MatInspRegInqDTO dto = new MatInspRegInqDTO();
        dto.setItemCode(request.getParameter("itemCode"));
        dto.setInspectQty(parseDouble(request.getParameter("inspectQty")));
        dto.setGoodQty(parseDouble(request.getParameter("goodQty")));
        dto.setDefectQty(parseDouble(request.getParameter("defectQty")));
        dto.setResult(request.getParameter("result"));
        dto.setRemark(request.getParameter("remark"));
        String inspectionDate = request.getParameter("inspectionDate");
        dto.setInspectionDate((inspectionDate == null || "".equals(inspectionDate)) ? new Date(System.currentTimeMillis()) : Date.valueOf(inspectionDate));
        new MatInspRegInqService().register(dto);
        response.sendRedirect(request.getContextPath() + "/matInspRegInq");
    }


    private void update(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        MatInspRegInqDTO dto = new MatInspRegInqDTO();
        dto.setMaterialInspectionId(parseInt(request.getParameter("materialInspectionId"), 0));
        dto.setInspectQty(parseDouble(request.getParameter("inspectQty")));
        dto.setGoodQty(parseDouble(request.getParameter("goodQty")));
        dto.setDefectQty(parseDouble(request.getParameter("defectQty")));
        dto.setResult(request.getParameter("result"));
        String inspectionDate = request.getParameter("inspectionDate");
        dto.setInspectionDate((inspectionDate == null || "".equals(inspectionDate)) ? null : Date.valueOf(inspectionDate));
        dto.setRemark(request.getParameter("remark"));
        new MatInspRegInqService().update(dto);
        response.sendRedirect(request.getContextPath() + "/matInspRegInq?materialInspectionId=" + dto.getMaterialInspectionId());
    }

    private void delete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        new MatInspRegInqService().delete(parseIntArray(request.getParameterValues("materialInspectionIds")));
        response.sendRedirect(request.getContextPath() + "/matInspRegInq");
    }

    private void detail(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int id = Integer.parseInt(request.getParameter("materialInspectionId"));
        MatInspRegInqDTO dto = new MatInspRegInqService().getMatInspRegInqDetail(id);
        request.setAttribute("matInspRegInqDTO", dto);
        request.setAttribute("pageId", "page-quality-matinsp-detail");
        request.setAttribute("pageTitle", "자재 검사 상세");
        request.setAttribute("pageSubTitle", "자재 검사 상세 정보 화면");
        request.setAttribute("contentPage", "/WEB-INF/views/quality/matInspDetail.jsp");
        request.getRequestDispatcher("/WEB-INF/views/table.jsp").forward(request, response);
    }

    private void forwardList(HttpServletRequest request, HttpServletResponse response, MatInspRegInqDTO searchDTO)
            throws ServletException, IOException {
        request.setCharacterEncoding("utf-8");
        response.setContentType("text/html; charset=utf-8;");

        List<MatInspRegInqDTO> fullList = new MatInspRegInqService().getMatInspRegInqList(searchDTO);
        int paCurrentPage = parseInt(request.getParameter("page"), 1);
        int paPageSize = 10;
        int paBlockSize = 5;
        int paTotalCount = fullList.size();
        int paTotalPage = (int) Math.ceil((double) paTotalCount / paPageSize);
        if (paTotalPage < 1) paTotalPage = 1;
        if (paCurrentPage < 1) paCurrentPage = 1;
        if (paCurrentPage > paTotalPage) paCurrentPage = paTotalPage;
        int fromIndex = (paCurrentPage - 1) * paPageSize;
        int toIndex = Math.min(fromIndex + paPageSize, paTotalCount);
        List<MatInspRegInqDTO> pageList = fullList.subList(fromIndex, toIndex);
        int paStartPage = ((paCurrentPage - 1) / paBlockSize) * paBlockSize + 1;
        int paEndPage = Math.min(paStartPage + paBlockSize - 1, paTotalPage);

        request.setAttribute("matInspRegInqList", pageList);
        request.setAttribute("matInspRegInqSearchDTO", searchDTO);
        request.setAttribute("paCurrentPage", paCurrentPage);
        request.setAttribute("paPageSize", paPageSize);
        request.setAttribute("paBlockSize", paBlockSize);
        request.setAttribute("paTotalCount", paTotalCount);
        request.setAttribute("paTotalPage", paTotalPage);
        request.setAttribute("paStartPage", paStartPage);
        request.setAttribute("paEndPage", paEndPage);
        request.setAttribute("pageId", "page-quality-matinsp");
        request.setAttribute("pageTitle", "자재 검사 등록 / 조회");
        request.setAttribute("pageSubTitle", "자재 검사 내역 등록, 조회");
        request.setAttribute("contentPage", "/WEB-INF/views/MatInspRegInq.jsp");
        request.getRequestDispatcher("/WEB-INF/views/table.jsp").forward(request, response);
    }

    private int parseInt(String value, int defaultValue) {
        try { return Integer.parseInt(value); } catch (Exception e) { return defaultValue; }
    }

    private int[] parseIntArray(String[] values) {
        if (values == null) return new int[0];
        int[] tmp = new int[values.length];
        int idx = 0;
        for (String v : values) if (v != null && !"".equals(v)) tmp[idx++] = Integer.parseInt(v);
        int[] r = new int[idx];
        System.arraycopy(tmp, 0, r, 0, idx);
        return r;
    }

    private double parseDouble(String value) {
        return (value == null || "".equals(value)) ? 0 : Double.parseDouble(value);
    }
}
