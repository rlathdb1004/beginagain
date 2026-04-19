package QualityMgmt.FPInspRegInq.Controller;

import java.io.IOException;
import java.sql.Date;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import QualityMgmt.FPInspRegInq.DTO.FPInspRegInqDTO;
import QualityMgmt.FPInspRegInq.Service.FPInspRegInqService;
import member.dto.MemberDTO;

@WebServlet("/fpInspRegInq")
public class FPInspRegInqServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private final FPInspRegInqService service = new FPInspRegInqService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String id = request.getParameter("finalInspectionId");
        if (id != null && !"".equals(id)) { detail(request, response); return; }
        forwardList(request, response, new FPInspRegInqDTO());
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
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
            throw new ServletException("FPInspRegInq 처리 중 오류 발생", e);
        }
    }

    private void list(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        FPInspRegInqDTO searchDTO = new FPInspRegInqDTO();
        searchDTO.setSearchType(request.getParameter("searchType"));
        searchDTO.setKeyword(request.getParameter("keyword"));
        searchDTO.setResultType(request.getParameter("resultType"));
        String startDateStr = request.getParameter("startDate");
        String endDateStr = request.getParameter("endDate");
        if (startDateStr != null && !"".equals(startDateStr)) searchDTO.setStartDate(Date.valueOf(startDateStr));
        if (endDateStr != null && !"".equals(endDateStr)) searchDTO.setEndDate(Date.valueOf(endDateStr));
        forwardList(request, response, searchDTO);
    }

    private void register(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession();
        try {
            FPInspRegInqDTO dto = new FPInspRegInqDTO();
            dto.setResultId(Integer.parseInt(request.getParameter("resultId")));
            dto.setInspectQty(parseDouble(request.getParameter("inspectQty")));
            dto.setResult(request.getParameter("result"));
            dto.setRemark(request.getParameter("remark"));
            String inspectionDate = request.getParameter("inspectionDate");
            dto.setInspectionDate((inspectionDate == null || "".equals(inspectionDate)) ? new Date(System.currentTimeMillis()) : Date.valueOf(inspectionDate));
            Object loginUser = session.getAttribute("loginUser");
            if (loginUser instanceof MemberDTO) dto.setEmpId(((MemberDTO) loginUser).getEmpId());
            service.register(dto);
        } catch (RuntimeException e) {
            session.setAttribute("errorMsg", e.getMessage());
        }
        response.sendRedirect(request.getContextPath() + "/fpInspRegInq");
    }

    private void update(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession();
        FPInspRegInqDTO dto = new FPInspRegInqDTO();
        dto.setFinalInspectionId(parseInt(request.getParameter("finalInspectionId"), 0));
        dto.setInspectQty(parseDouble(request.getParameter("inspectQty")));
        dto.setResult(request.getParameter("result"));
        String inspectionDate = request.getParameter("inspectionDate");
        dto.setInspectionDate((inspectionDate == null || "".equals(inspectionDate)) ? null : Date.valueOf(inspectionDate));
        dto.setRemark(request.getParameter("remark"));
        try {
            service.update(dto);
        } catch (RuntimeException e) {
            session.setAttribute("errorMsg", e.getMessage());
        }
        response.sendRedirect(request.getContextPath() + "/fpInspRegInq?finalInspectionId=" + dto.getFinalInspectionId());
    }

    private void delete(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession();
        try {
            service.delete(parseIntArray(request.getParameterValues("finalInspectionIds")));
        } catch (RuntimeException e) {
            session.setAttribute("errorMsg", e.getMessage());
        }
        response.sendRedirect(request.getContextPath() + "/fpInspRegInq");
    }

    private void detail(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int id = Integer.parseInt(request.getParameter("finalInspectionId"));
        FPInspRegInqDTO dto = service.getFPInspRegInqDetail(id);
        String errorMsg = popErrorMessage(request.getSession(false));
        request.setAttribute("errorMsg", errorMsg);
        request.setAttribute("fpInspRegInqDTO", dto);
        request.setAttribute("pageId", "page-quality-fpinsp-detail");
        request.setAttribute("pageTitle", "완제품 검사 상세");
        request.setAttribute("pageSubTitle", "완제품 검사 상세 정보 화면");
        request.setAttribute("contentPage", "/WEB-INF/views/quality/fpInspDetail.jsp");
        request.getRequestDispatcher("/WEB-INF/views/table.jsp").forward(request, response);
    }

    private void forwardList(HttpServletRequest request, HttpServletResponse response, FPInspRegInqDTO searchDTO) throws ServletException, IOException {
        request.setCharacterEncoding("utf-8");
        response.setContentType("text/html; charset=utf-8;");
        List<FPInspRegInqDTO> fullList = service.getFPInspRegInqList(searchDTO);
        List<FPInspRegInqDTO> resultList = service.getAvailableProductionResultList();
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
        List<FPInspRegInqDTO> pageList = fullList.subList(fromIndex, toIndex);
        int paStartPage = ((paCurrentPage - 1) / paBlockSize) * paBlockSize + 1;
        int paEndPage = Math.min(paStartPage + paBlockSize - 1, paTotalPage);

        request.setAttribute("fpInspRegInqList", pageList);
        request.setAttribute("resultList", resultList);
        request.setAttribute("errorMsg", popErrorMessage(request.getSession(false)));
        request.setAttribute("fpInspRegInqSearchDTO", searchDTO);
        request.setAttribute("paCurrentPage", paCurrentPage);
        request.setAttribute("paPageSize", paPageSize);
        request.setAttribute("paBlockSize", paBlockSize);
        request.setAttribute("paTotalCount", paTotalCount);
        request.setAttribute("paTotalPage", paTotalPage);
        request.setAttribute("paStartPage", paStartPage);
        request.setAttribute("paEndPage", paEndPage);
        request.setAttribute("pageId", "page-quality-fpinsp");
        request.setAttribute("pageTitle", "완제품 검사 등록 / 조회");
        request.setAttribute("pageSubTitle", "완제품 검사 내역 등록, 조회");
        request.setAttribute("contentPage", "/WEB-INF/views/FPInspRegInq.jsp");
        request.getRequestDispatcher("/WEB-INF/views/table.jsp").forward(request, response);
    }

    private String popErrorMessage(HttpSession session) {
        if (session == null) return null;
        Object value = session.getAttribute("errorMsg");
        if (value == null) return null;
        session.removeAttribute("errorMsg");
        return String.valueOf(value);
    }

    private int parseInt(String value, int defaultValue) { try { return Integer.parseInt(value); } catch (Exception e) { return defaultValue; } }
    private int[] parseIntArray(String[] values) {
        if (values == null) return new int[0];
        int[] tmp = new int[values.length];
        int idx = 0;
        for (String v : values) if (v != null && !"".equals(v)) tmp[idx++] = Integer.parseInt(v);
        int[] r = new int[idx]; System.arraycopy(tmp, 0, r, 0, idx); return r;
    }
    private double parseDouble(String value) { return (value == null || "".equals(value)) ? 0 : Double.parseDouble(value); }
}
