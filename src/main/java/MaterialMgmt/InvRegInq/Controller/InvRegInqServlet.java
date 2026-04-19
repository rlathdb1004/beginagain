package MaterialMgmt.InvRegInq.Controller;

import java.io.IOException;
import java.sql.Date;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import MaterialMgmt.InvRegInq.DTO.InvRegInqDTO;
import MaterialMgmt.InvRegInq.Service.InvRegInqService;

@WebServlet("/invRegInq")
public class InvRegInqServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private final InvRegInqService service = new InvRegInqService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String inventoryId = request.getParameter("inventoryId");
        if (inventoryId != null && !"".equals(inventoryId)) {
            detail(request, response);
            return;
        }
        forwardList(request, response, new InvRegInqDTO());
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
            throw new ServletException("InvRegInqServlet 처리 중 오류 발생", e);
        }
    }

    private void list(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        InvRegInqDTO searchDTO = new InvRegInqDTO();
        searchDTO.setSearchType(request.getParameter("searchType"));
        searchDTO.setKeyword(request.getParameter("keyword"));
        String startDateStr = request.getParameter("startDate");
        String endDateStr = request.getParameter("endDate");
        if (startDateStr != null && !"".equals(startDateStr)) searchDTO.setStartDate(Date.valueOf(startDateStr));
        if (endDateStr != null && !"".equals(endDateStr)) searchDTO.setEndDate(Date.valueOf(endDateStr));
        forwardList(request, response, searchDTO);
    }

    private void register(HttpServletRequest request, HttpServletResponse response) throws IOException {
        request.getSession().setAttribute("errorMsg", "재고는 직접 등록하지 않고 입출고 결과로 관리합니다.");
        response.sendRedirect(request.getContextPath() + "/invRegInq");
    }

    private void update(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession();
        InvRegInqDTO dto = new InvRegInqDTO();
        dto.setInventoryId(parseInt(request.getParameter("inventoryId"), 0));
        dto.setSafetyStock(parseDouble(request.getParameter("safetyStock")));
        dto.setRemark(request.getParameter("remark"));
        try {
            service.update(dto);
        } catch (RuntimeException e) {
            session.setAttribute("errorMsg", e.getMessage());
        }
        response.sendRedirect(request.getContextPath() + "/invRegInq?inventoryId=" + dto.getInventoryId());
    }

    private void delete(HttpServletRequest request, HttpServletResponse response) throws IOException {
        request.getSession().setAttribute("errorMsg", "재고는 삭제하지 않습니다.");
        response.sendRedirect(request.getContextPath() + "/invRegInq");
    }

    private void detail(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int inventoryId = Integer.parseInt(request.getParameter("inventoryId"));
        InvRegInqDTO dto = service.getInvRegInqDetail(inventoryId);
        request.setAttribute("errorMsg", popErrorMessage(request.getSession(false)));
        request.setAttribute("invRegInqDTO", dto);
        request.setAttribute("pageId", "page-materials-inventory-detail");
        request.setAttribute("pageTitle", "재고 상세");
        request.setAttribute("pageSubTitle", "재고 상세 정보 화면");
        request.setAttribute("contentPage", "/WEB-INF/views/material/inventoryDetail.jsp");
        request.getRequestDispatcher("/WEB-INF/views/table.jsp").forward(request, response);
    }

    private void forwardList(HttpServletRequest request, HttpServletResponse response, InvRegInqDTO searchDTO)
            throws ServletException, IOException {
        request.setCharacterEncoding("utf-8");
        response.setContentType("text/html; charset=utf-8;");

        List<InvRegInqDTO> fullList = service.getInvRegInqList(searchDTO);
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
        List<InvRegInqDTO> pageList = fullList.subList(fromIndex, toIndex);
        int paStartPage = ((paCurrentPage - 1) / paBlockSize) * paBlockSize + 1;
        int paEndPage = Math.min(paStartPage + paBlockSize - 1, paTotalPage);

        request.setAttribute("invRegInqList", pageList);
        request.setAttribute("errorMsg", popErrorMessage(request.getSession(false)));
        request.setAttribute("invRegInqSearchDTO", searchDTO);
        request.setAttribute("paCurrentPage", paCurrentPage);
        request.setAttribute("paPageSize", paPageSize);
        request.setAttribute("paBlockSize", paBlockSize);
        request.setAttribute("paTotalCount", paTotalCount);
        request.setAttribute("paTotalPage", paTotalPage);
        request.setAttribute("paStartPage", paStartPage);
        request.setAttribute("paEndPage", paEndPage);
        request.setAttribute("pageId", "page-materials-inventory");
        request.setAttribute("pageTitle", "재고 조회");
        request.setAttribute("pageSubTitle", "재고 현황 조회 및 안전재고 관리");
        request.setAttribute("contentPage", "/WEB-INF/views/InvRegInq.jsp");
        request.getRequestDispatcher("/WEB-INF/views/table.jsp").forward(request, response);
    }

    private String popErrorMessage(HttpSession session) {
        if (session == null) return null;
        Object value = session.getAttribute("errorMsg");
        if (value == null) return null;
        session.removeAttribute("errorMsg");
        return String.valueOf(value);
    }

    private double parseDouble(String value) {
        return (value == null || "".equals(value)) ? 0 : Double.parseDouble(value);
    }

    private int parseInt(String value, int defaultValue) {
        try { return Integer.parseInt(value); } catch (Exception e) { return defaultValue; }
    }
}
