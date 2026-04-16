package equipment.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import equipment.dto.EquipmentDTO;
import equipment.service.EquipmentService;

@WebServlet("/equipment/list")
public class EquipmentListController extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private EquipmentService equipmentService = new EquipmentService();

//	페이징 기능을 위해서 주석처리 후 하단 코딩 추가 / 령
//	@Override
//	protected void doGet(HttpServletRequest request, HttpServletResponse response)
//			throws ServletException, IOException {
//
//		List<EquipmentDTO> equipmentList = equipmentService.getEquipmentList();
//
//		request.setAttribute("equipmentList", equipmentList);
//		request.setAttribute("pageTitle", "설비관리");
//		request.setAttribute("pageSubTitle", "설비 등록 / 조회 / 수정 / 삭제");
//		request.setAttribute("contentPage", "/WEB-INF/views/equipment/equipmentList.jsp");
//		request.getRequestDispatcher("/WEB-INF/views/table.jsp").forward(request, response);
//	}
	
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

	    List<EquipmentDTO> fullList = equipmentService.getEquipmentList();
	    List<EquipmentDTO> filteredList = new ArrayList<>();

	    for (EquipmentDTO dto : fullList) {
	        if (matches(dto, searchType, keyword)) {
	            filteredList.add(dto);
	        }
	    }

	    int totalCount = filteredList.size();
	    int totalPage = (int) Math.ceil((double) totalCount / pageSize);

	    if (totalPage < 1) {
	        totalPage = 1;
	    }

	    if (page < 1) {
	        page = 1;
	    }

	    if (page > totalPage) {
	        page = totalPage;
	    }

	    int fromIndex = (page - 1) * pageSize;
	    int toIndex = Math.min(fromIndex + pageSize, totalCount);

	    List<EquipmentDTO> pageList = new ArrayList<>();
	    if (totalCount > 0) {
	        pageList = filteredList.subList(fromIndex, toIndex);
	    }

	    int startPage = ((page - 1) / pageBlock) * pageBlock + 1;
	    int endPage = startPage + pageBlock - 1;

	    if (endPage > totalPage) {
	        endPage = totalPage;
	    }

	    request.setAttribute("equipmentList", pageList);
	    request.setAttribute("searchType", searchType);
	    request.setAttribute("keyword", keyword);

	    request.setAttribute("paCurrentPage", page);
	    request.setAttribute("paPageSize", pageSize);
	    request.setAttribute("paBlockSize", pageBlock);
	    request.setAttribute("paTotalCount", totalCount);
	    request.setAttribute("paTotalPage", totalPage);
	    request.setAttribute("paStartPage", startPage);
	    request.setAttribute("paEndPage", endPage);

	    request.setAttribute("pageTitle", "설비관리");
	    request.setAttribute("pageSubTitle", "설비 등록, 조회");
	    request.setAttribute("contentPage", "/WEB-INF/views/equipment/equipmentList.jsp");
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

	private boolean matches(EquipmentDTO dto, String searchType, String keyword) {
	    if (keyword == null || keyword.trim().isEmpty()) {
	        return true;
	    }

	    String lowerKeyword = keyword.toLowerCase();

	    String equipmentCode = dto.getEquipmentCode() == null ? "" : dto.getEquipmentCode().toLowerCase();
	    String equipmentName = dto.getEquipmentName() == null ? "" : dto.getEquipmentName().toLowerCase();
	    String modelName = dto.getModelName() == null ? "" : dto.getModelName().toLowerCase();
	    String manufacturer = dto.getManufacturer() == null ? "" : dto.getManufacturer().toLowerCase();
	    String vendorName = dto.getVendorName() == null ? "" : dto.getVendorName().toLowerCase();
	    String location = dto.getLocation() == null ? "" : dto.getLocation().toLowerCase();

	    if ("equipmentCode".equals(searchType)) {
	        return equipmentCode.contains(lowerKeyword);
	    } else if ("equipmentName".equals(searchType)) {
	        return equipmentName.contains(lowerKeyword);
	    } else if ("modelName".equals(searchType)) {
	        return modelName.contains(lowerKeyword);
	    } else if ("manufacturer".equals(searchType)) {
	        return manufacturer.contains(lowerKeyword);
	    } else if ("vendorName".equals(searchType)) {
	        return vendorName.contains(lowerKeyword);
	    } else if ("location".equals(searchType)) {
	        return location.contains(lowerKeyword);
	    } else {
	        return equipmentCode.contains(lowerKeyword)
	                || equipmentName.contains(lowerKeyword)
	                || modelName.contains(lowerKeyword)
	                || manufacturer.contains(lowerKeyword)
	                || vendorName.contains(lowerKeyword)
	                || location.contains(lowerKeyword);
	    }
	}
}
