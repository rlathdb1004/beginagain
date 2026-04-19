package maintenance.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import equipment.dto.EquipmentDTO;
import equipment.service.EquipmentService;
import maintenance.dto.MaintenanceDTO;
import maintenance.service.MaintenanceService;

@WebServlet("/maintenance/list")
public class MaintenanceListController extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private MaintenanceService maintenanceService = new MaintenanceService();
    private EquipmentService equipmentService = new EquipmentService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("utf-8");
        response.setContentType("text/html; charset=utf-8;");

        String searchType = nvl(request.getParameter("searchType"));
        String keyword = nvl(request.getParameter("keyword"));

        int page = parseInt(request.getParameter("page"), 1);
        int pageSize = 10;
        int pageBlock = 5;

        List<MaintenanceDTO> fullList = maintenanceService.getMaintenanceList();
        List<MaintenanceDTO> filteredList = new ArrayList<>();

        for (MaintenanceDTO dto : fullList) {
            if (matches(dto, searchType, keyword)) {
                filteredList.add(dto);
            }
        }

        int totalCount = filteredList.size();
        int totalPage = (int) Math.ceil((double) totalCount / pageSize);

        if (totalPage < 1) totalPage = 1;
        if (page < 1) page = 1;
        if (page > totalPage) page = totalPage;

        int fromIndex = (page - 1) * pageSize;
        int toIndex = Math.min(fromIndex + pageSize, totalCount);

        List<MaintenanceDTO> pageList = new ArrayList<>();
        if (totalCount > 0) {
            pageList = filteredList.subList(fromIndex, toIndex);
        }

        int startPage = ((page - 1) / pageBlock) * pageBlock + 1;
        int endPage = startPage + pageBlock - 1;
        if (endPage > totalPage) endPage = totalPage;

        request.setAttribute("maintenanceList", pageList);
        request.setAttribute("equipmentList", equipmentService.getEquipmentList());
        request.setAttribute("searchType", searchType);
        request.setAttribute("keyword", keyword);

        HttpSession session = request.getSession(false);
        if (session != null) {
            Object errorMsg = session.getAttribute("errorMsg");
            if (errorMsg != null) {
                request.setAttribute("errorMsg", errorMsg);
                session.removeAttribute("errorMsg");
            }
        }

        request.setAttribute("paCurrentPage", page);
        request.setAttribute("paPageSize", pageSize);
        request.setAttribute("paBlockSize", pageBlock);
        request.setAttribute("paTotalCount", totalCount);
        request.setAttribute("paTotalPage", totalPage);
        request.setAttribute("paStartPage", startPage);
        request.setAttribute("paEndPage", endPage);

        request.setAttribute("pageTitle", "설비운영");
        request.setAttribute("pageSubTitle", "정비 이력 등록, 조회");
        request.setAttribute("contentPage", "/WEB-INF/views/maintenance/maintenanceList.jsp");
        request.getRequestDispatcher("/WEB-INF/views/table.jsp").forward(request, response);
    }

    private int parseInt(String value, int defaultValue) {
        try { return Integer.parseInt(value); } catch (Exception e) { return defaultValue; }
    }

    private String nvl(String str) { return str == null ? "" : str.trim(); }

    private boolean matches(MaintenanceDTO dto, String searchType, String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) return true;

        String lowerKeyword = keyword.toLowerCase();
        String equipmentCode = dto.getEquipmentCode() == null ? "" : dto.getEquipmentCode().toLowerCase();
        String equipmentName = dto.getEquipmentName() == null ? "" : dto.getEquipmentName().toLowerCase();
        String maintenanceType = dto.getMaintenanceType() == null ? "" : dto.getMaintenanceType().toLowerCase();
        String maintenanceContent = dto.getMaintenanceContent() == null ? "" : dto.getMaintenanceContent().toLowerCase();
        String status = dto.getStatus() == null ? "" : dto.getStatus().toLowerCase();

        if ("equipmentCode".equals(searchType)) return equipmentCode.contains(lowerKeyword);
        if ("equipmentName".equals(searchType)) return equipmentName.contains(lowerKeyword);
        if ("maintenanceType".equals(searchType)) return maintenanceType.contains(lowerKeyword);
        if ("status".equals(searchType)) return status.contains(lowerKeyword);
        return equipmentCode.contains(lowerKeyword) || equipmentName.contains(lowerKeyword) || maintenanceType.contains(lowerKeyword) || maintenanceContent.contains(lowerKeyword) || status.contains(lowerKeyword);
    }
}
