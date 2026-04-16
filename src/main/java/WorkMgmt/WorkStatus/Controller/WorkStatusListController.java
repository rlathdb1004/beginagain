package WorkMgmt.WorkStatus.Controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import WorkMgmt.WorkStatus.DTO.*;
import WorkMgmt.WorkStatus.Service.*;

@WebServlet("/workstatus")
public class WorkStatusListController extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private WorkStatusService workStatusService = new WorkStatusService();

	// 페이징을 위해서 아래 코드 주석 후 하위 코드 전체 추가함/령
//	@Override
//	protected void doGet(HttpServletRequest request, HttpServletResponse response)
//			throws ServletException, IOException {
//		List<WorkStatusDTO> workStatusList = workStatusService.getWorkStatusList();
//		request.setAttribute("workStatusList", workStatusList);
//		request.setAttribute("pageTitle", "작업관리");
//		request.setAttribute("pageSubTitle", "작업 현황 조회");
//		request.setAttribute("contentPage", "/WEB-INF/views/workStatusList.jsp");
//		request.getRequestDispatcher("/WEB-INF/views/table.jsp").forward(request, response);
//	}
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	    request.setCharacterEncoding("utf-8");
	    response.setContentType("text/html; charset=utf-8;");

	    String searchType = nvl(request.getParameter("searchType"));
	    String keyword = nvl(request.getParameter("keyword"));

	    int page = parseInt(request.getParameter("page"), 1);
	    int pageSize = 10;
	    int pageBlock = 5;

	    List<WorkStatusDTO> fullList = workStatusService.getWorkStatusList();
	    List<WorkStatusDTO> filteredList = new java.util.ArrayList<>();

	    for (WorkStatusDTO dto : fullList) {
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

	    List<WorkStatusDTO> pageList = new java.util.ArrayList<>();
	    if (totalCount > 0) {
	        pageList = filteredList.subList(fromIndex, toIndex);
	    }

	    int startPage = ((page - 1) / pageBlock) * pageBlock + 1;
	    int endPage = startPage + pageBlock - 1;
	    if (endPage > totalPage) endPage = totalPage;

	    int waitingCount = 0;
	    int runningCount = 0;
	    int doneCount = 0;

	    for (WorkStatusDTO dto : filteredList) {
	        if ("완료".equals(dto.getProgressStatus())) {
	            doneCount++;
	        } else if ("진행중".equals(dto.getProgressStatus())) {
	            runningCount++;
	        } else {
	            waitingCount++;
	        }
	    }

	    request.setAttribute("workStatusList", pageList);
	    request.setAttribute("searchType", searchType);
	    request.setAttribute("keyword", keyword);

	    request.setAttribute("summaryTotalCount", filteredList.size());
	    request.setAttribute("summaryWaitingCount", waitingCount);
	    request.setAttribute("summaryRunningCount", runningCount);
	    request.setAttribute("summaryDoneCount", doneCount);

	    request.setAttribute("paCurrentPage", page);
	    request.setAttribute("paPageSize", pageSize);
	    request.setAttribute("paBlockSize", pageBlock);
	    request.setAttribute("paTotalCount", totalCount);
	    request.setAttribute("paTotalPage", totalPage);
	    request.setAttribute("paStartPage", startPage);
	    request.setAttribute("paEndPage", endPage);

	    request.setAttribute("pageTitle", "작업관리");
	    request.setAttribute("pageSubTitle", "작업 현황 조회");
	    request.setAttribute("contentPage", "/WEB-INF/views/workStatusList.jsp");
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

	private boolean matches(WorkStatusDTO dto, String searchType, String keyword) {
	    if (keyword == null || keyword.trim().isEmpty()) {
	        return true;
	    }

	    String lowerKeyword = keyword.toLowerCase();

	    String workOrderDisplayCode = dto.getWorkOrderDisplayCode() == null ? "" : dto.getWorkOrderDisplayCode().toLowerCase();
	    String itemName = dto.getItemName() == null ? "" : dto.getItemName().toLowerCase();
	    String empName = dto.getEmpName() == null ? "" : dto.getEmpName().toLowerCase();
	    String progressStatus = dto.getProgressStatus() == null ? "" : dto.getProgressStatus().toLowerCase();

	    if ("workOrderDisplayCode".equals(searchType)) {
	        return workOrderDisplayCode.contains(lowerKeyword);
	    } else if ("itemName".equals(searchType)) {
	        return itemName.contains(lowerKeyword);
	    } else if ("empName".equals(searchType)) {
	        return empName.contains(lowerKeyword);
	    } else if ("progressStatus".equals(searchType)) {
	        return progressStatus.contains(lowerKeyword);
	    } else {
	        return workOrderDisplayCode.contains(lowerKeyword)
	                || itemName.contains(lowerKeyword)
	                || empName.contains(lowerKeyword)
	                || progressStatus.contains(lowerKeyword);
	    }
	}
}
