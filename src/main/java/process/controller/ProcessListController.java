package process.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import process.dto.ProcessDTO;
import process.service.ProcessService;

@WebServlet("/process/list")
public class ProcessListController extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private ProcessService service = new ProcessService();

//	페이징을 위해 하위 주석 및 내용 하단 추가 / 령
	/*
	 * @Override protected void doGet(HttpServletRequest request,
	 * HttpServletResponse response) throws ServletException, IOException {
	 * 
	 * List<ProcessDTO> list = service.getList();
	 * 
	 * request.setAttribute("list", list); request.setAttribute("pageTitle",
	 * "공정관리"); request.setAttribute("pageSubTitle", "공정 등록 / 조회 / 수정 / 삭제");
	 * request.setAttribute("contentPage",
	 * "/WEB-INF/views/process/processList.jsp");
	 * request.getRequestDispatcher("/WEB-INF/views/table.jsp").forward(request,
	 * response); }
	 */
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

	    List<ProcessDTO> fullList = service.getList();
	    List<ProcessDTO> filteredList = new ArrayList<>();

	    for (ProcessDTO dto : fullList) {
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

	    List<ProcessDTO> pageList = new ArrayList<>();
	    if (totalCount > 0) {
	        pageList = filteredList.subList(fromIndex, toIndex);
	    }

	    int startPage = ((page - 1) / pageBlock) * pageBlock + 1;
	    int endPage = startPage + pageBlock - 1;

	    if (endPage > totalPage) {
	        endPage = totalPage;
	    }

	    request.setAttribute("list", pageList);
	    request.setAttribute("searchType", searchType);
	    request.setAttribute("keyword", keyword);

	    request.setAttribute("paCurrentPage", page);
	    request.setAttribute("paPageSize", pageSize);
	    request.setAttribute("paBlockSize", pageBlock);
	    request.setAttribute("paTotalCount", totalCount);
	    request.setAttribute("paTotalPage", totalPage);
	    request.setAttribute("paStartPage", startPage);
	    request.setAttribute("paEndPage", endPage);

	    request.setAttribute("pageTitle", "공정관리");
	    request.setAttribute("pageSubTitle", "공정 등록, 조회");
	    request.setAttribute("contentPage", "/WEB-INF/views/process/processList.jsp");
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

	private boolean matches(ProcessDTO dto, String searchType, String keyword) {
	    if (keyword == null || keyword.trim().isEmpty()) {
	        return true;
	    }

	    String lowerKeyword = keyword.toLowerCase();

	    String processCode = dto.getProcessCode() == null ? "" : dto.getProcessCode().toLowerCase();
	    String processName = dto.getProcessName() == null ? "" : dto.getProcessName().toLowerCase();

	    if ("processCode".equals(searchType)) {
	        return processCode.contains(lowerKeyword);
	    } else if ("processName".equals(searchType)) {
	        return processName.contains(lowerKeyword);
	    } else {
	        return processCode.contains(lowerKeyword) || processName.contains(lowerKeyword);
	    }
	}
}