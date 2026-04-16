package downtime.controller;

import java.io.IOException;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import downtime.dto.DowntimeDTO;
import downtime.service.DowntimeService;

@WebServlet("/downtime/list")
public class DowntimeListController extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private DowntimeService downtimeService = new DowntimeService();

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

        List<DowntimeDTO> fullList = downtimeService.getDowntimeList();
        List<DowntimeDTO> filteredList = new ArrayList<DowntimeDTO>();

        // ===== 현황판 카운트 =====
        int totalDowntimeCount = fullList.size();
        int inProgressCount = 0;
        int completedCount = 0;
        int todayCount = 0;

        Date today = new Date(System.currentTimeMillis());

        for (DowntimeDTO dto : fullList) {
            String status = dto.getStatus() == null ? "" : dto.getStatus().trim();

            if ("진행중".equals(status) || "접수".equals(status)) {
                inProgressCount++;
            }

            if ("완료".equals(status) || "조치완료".equals(status)) {
                completedCount++;
            }

            if (dto.getFailureDate() != null && dto.getFailureDate().equals(today)) {
                todayCount++;
            }
        }

        for (DowntimeDTO dto : fullList) {
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

        List<DowntimeDTO> pageList = new ArrayList<DowntimeDTO>();
        if (totalCount > 0) {
            pageList = filteredList.subList(fromIndex, toIndex);
        }

        int startPage = ((page - 1) / pageBlock) * pageBlock + 1;
        int endPage = startPage + pageBlock - 1;
        if (endPage > totalPage) endPage = totalPage;

        request.setAttribute("downtimeList", pageList);
        request.setAttribute("searchType", searchType);
        request.setAttribute("keyword", keyword);

        request.setAttribute("paCurrentPage", page);
        request.setAttribute("paPageSize", pageSize);
        request.setAttribute("paBlockSize", pageBlock);
        request.setAttribute("paTotalCount", totalCount);
        request.setAttribute("paTotalPage", totalPage);
        request.setAttribute("paStartPage", startPage);
        request.setAttribute("paEndPage", endPage);

        // ===== 현황판 값 전달 =====
        request.setAttribute("totalDowntimeCount", totalDowntimeCount);
        request.setAttribute("inProgressCount", inProgressCount);
        request.setAttribute("completedCount", completedCount);
        request.setAttribute("todayCount", todayCount);

        request.setAttribute("pageTitle", "설비운영");
        request.setAttribute("pageSubTitle", "비가동 현황 조회");
        request.setAttribute("contentPage", "/WEB-INF/views/downtime/downtimeList.jsp");
        request.getRequestDispatcher("/WEB-INF/views/table.jsp").forward(request, response);
    }

    private int parseInt(String value, int defaultValue) {
        try {
            return Integer.parseInt(value);
        } catch (Exception e) {
            return defaultValue;
        }
    }

    private String nvl(String str) {
        return str == null ? "" : str.trim();
    }

    private boolean matches(DowntimeDTO dto, String searchType, String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return true;
        }

        String lowerKeyword = keyword.toLowerCase();

        String equipmentCode = dto.getEquipmentCode() == null ? "" : dto.getEquipmentCode().toLowerCase();
        String equipmentName = dto.getEquipmentName() == null ? "" : dto.getEquipmentName().toLowerCase();
        String failurePart = dto.getFailurePart() == null ? "" : dto.getFailurePart().toLowerCase();
        String status = dto.getStatus() == null ? "" : dto.getStatus().toLowerCase();
        String failureContent = dto.getFailureContent() == null ? "" : dto.getFailureContent().toLowerCase();

        if ("equipmentCode".equals(searchType)) {
            return equipmentCode.contains(lowerKeyword);
        } else if ("equipmentName".equals(searchType)) {
            return equipmentName.contains(lowerKeyword);
        } else if ("failurePart".equals(searchType)) {
            return failurePart.contains(lowerKeyword);
        } else if ("status".equals(searchType)) {
            return status.contains(lowerKeyword);
        } else {
            return equipmentCode.contains(lowerKeyword)
                    || equipmentName.contains(lowerKeyword)
                    || failurePart.contains(lowerKeyword)
                    || status.contains(lowerKeyword)
                    || failureContent.contains(lowerKeyword);
        }
    }
}