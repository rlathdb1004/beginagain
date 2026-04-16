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

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String inoutId = request.getParameter("inoutId");
        if (inoutId != null && !"".equals(inoutId)) {
            detail(request, response);
            return;
        }
        forwardList(request, response, new IORegInqDTO());
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
            throw new ServletException("IORegInqServlet 처리 중 오류 발생", e);
        }
    }

    private void list(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        IORegInqDTO searchDTO = new IORegInqDTO();
        searchDTO.setInoutType(request.getParameter("inoutType"));
        searchDTO.setSearchType(request.getParameter("searchType"));
        searchDTO.setKeyword(request.getParameter("keyword"));
        String startDateStr = request.getParameter("startDate");
        String endDateStr = request.getParameter("endDate");
        if (startDateStr != null && !"".equals(startDateStr)) searchDTO.setStartDate(Date.valueOf(startDateStr));
        if (endDateStr != null && !"".equals(endDateStr)) searchDTO.setEndDate(Date.valueOf(endDateStr));
        forwardList(request, response, searchDTO);
    }

    private void register(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        IORegInqDTO dto = new IORegInqDTO();
        dto.setItemCode(request.getParameter("itemCode"));
        dto.setInoutType(request.getParameter("inoutType"));
        dto.setQty(parseDouble(request.getParameter("qty")));
        dto.setUnit(request.getParameter("unit"));
        dto.setStatus(request.getParameter("status"));
        dto.setRemark(request.getParameter("remark"));
        String inoutDate = request.getParameter("inoutDate");
        dto.setInoutDate((inoutDate == null || "".equals(inoutDate)) ? new Date(System.currentTimeMillis()) : Date.valueOf(inoutDate));
        new IORegInqService().register(dto);
        response.sendRedirect(request.getContextPath() + "/ioRegInq");
    }


    private void update(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        IORegInqDTO dto = new IORegInqDTO();
        dto.setInoutId(parseInt(request.getParameter("inoutId"), 0));
        dto.setInoutType(request.getParameter("inoutType"));
        dto.setQty(parseDouble(request.getParameter("qty")));
        dto.setUnit(request.getParameter("unit"));
        String inoutDate = request.getParameter("inoutDate");
        dto.setInoutDate((inoutDate == null || "".equals(inoutDate)) ? null : Date.valueOf(inoutDate));
        dto.setStatus(request.getParameter("status"));
        dto.setRemark(request.getParameter("remark"));
        new IORegInqService().update(dto);
        response.sendRedirect(request.getContextPath() + "/ioRegInq?inoutId=" + dto.getInoutId());
    }

    private void delete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        new IORegInqService().delete(parseIntArray(request.getParameterValues("inoutIds")));
        response.sendRedirect(request.getContextPath() + "/ioRegInq");
    }

    private void detail(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int inoutId = Integer.parseInt(request.getParameter("inoutId"));
        IORegInqDTO dto = new IORegInqService().getIORegInqDetail(inoutId);
        request.setAttribute("ioRegInqDTO", dto);
        request.setAttribute("pageId", "page-materials-inout-detail");
        request.setAttribute("pageTitle", "입출고 상세");
        request.setAttribute("pageSubTitle", "자재 입출고 상세 정보 화면");
        request.setAttribute("contentPage", "/WEB-INF/views/material/ioDetail.jsp");
        request.getRequestDispatcher("/WEB-INF/views/table.jsp").forward(request, response);
    }

    private void forwardList(HttpServletRequest request, HttpServletResponse response, IORegInqDTO searchDTO)
            throws ServletException, IOException {
        request.setCharacterEncoding("utf-8");
        response.setContentType("text/html; charset=utf-8;");

        List<IORegInqDTO> fullList = new IORegInqService().getIORegInqList(searchDTO);
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
        List<IORegInqDTO> pageList = fullList.subList(fromIndex, toIndex);
        int paStartPage = ((paCurrentPage - 1) / paBlockSize) * paBlockSize + 1;
        int paEndPage = Math.min(paStartPage + paBlockSize - 1, paTotalPage);

        request.setAttribute("ioRegInqList", pageList);
        request.setAttribute("ioRegInqSearchDTO", searchDTO);
        request.setAttribute("paCurrentPage", paCurrentPage);
        request.setAttribute("paPageSize", paPageSize);
        request.setAttribute("paBlockSize", paBlockSize);
        request.setAttribute("paTotalCount", paTotalCount);
        request.setAttribute("paTotalPage", paTotalPage);
        request.setAttribute("paStartPage", paStartPage);
        request.setAttribute("paEndPage", paEndPage);
        request.setAttribute("pageId", "page-materials-inout");
        request.setAttribute("pageTitle", "입출고 등록 / 조회");
        request.setAttribute("pageSubTitle", "자재 입출고 내역 등록, 조회");
        request.setAttribute("contentPage", "/WEB-INF/views/IORegInq.jsp");
        request.getRequestDispatcher("/WEB-INF/views/table.jsp").forward(request, response);
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

    private int parseInt(String value, int defaultValue) {
        try { return Integer.parseInt(value); } catch (Exception e) { return defaultValue; }
    }
}
