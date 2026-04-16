package QualityMgmt.DefectRegInq.Controller;

import java.io.IOException;
import java.sql.Date;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import QualityMgmt.DefectRegInq.DTO.DefectRegInqDTO;
import QualityMgmt.DefectRegInq.Service.DefectRegInqService;

@WebServlet("/defectRegInq")
public class DefectRegInqServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		forwardList(request, response, new DefectRegInqDTO(), null);
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.setCharacterEncoding("utf-8");
		String cmd = request.getParameter("cmd");
		if (cmd == null || "".equals(cmd))
			cmd = "list";
		try {
			if ("register".equals(cmd))
				register(request, response);
			else if ("delete".equals(cmd))
				delete(request, response);
			else if ("detail".equals(cmd))
				detail(request, response);
			else
				list(request, response);
		} catch (Exception e) {
			throw new ServletException("DefectRegInq 처리 중 오류 발생", e);
		}
	}

	private void list(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		DefectRegInqDTO searchDTO = new DefectRegInqDTO();
		searchDTO.setSearchType(request.getParameter("searchType"));
		searchDTO.setKeyword(request.getParameter("keyword"));
		searchDTO.setDefectTypeSearch(request.getParameter("defectTypeSearch"));
		String startDateStr = request.getParameter("startDate");
		String endDateStr = request.getParameter("endDate");
		if (startDateStr != null && !"".equals(startDateStr))
			searchDTO.setStartDate(Date.valueOf(startDateStr));
		if (endDateStr != null && !"".equals(endDateStr))
			searchDTO.setEndDate(Date.valueOf(endDateStr));
		forwardList(request, response, searchDTO, null);
	}

	private void register(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		DefectRegInqDTO dto = new DefectRegInqDTO();
		dto.setFinalInspectionId(Integer.parseInt(request.getParameter("finalInspectionId")));
		dto.setDefectCodeId(Integer.parseInt(request.getParameter("defectCodeId")));
		dto.setRemark(request.getParameter("remark"));
		new DefectRegInqService().register(dto);
		response.sendRedirect(request.getContextPath() + "/defectRegInq");
	}

	private void delete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		new DefectRegInqService().delete(parseIntArray(request.getParameterValues("defectProductIds")));
		response.sendRedirect(request.getContextPath() + "/defectRegInq");
	}

	private void detail(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		int id = Integer.parseInt(request.getParameter("defectProductId"));
		DefectRegInqDTO dto = new DefectRegInqService().getDefectRegInqDetail(id);
		forwardList(request, response, new DefectRegInqDTO(), dto);
	}

	private void forwardList(HttpServletRequest request, HttpServletResponse response, DefectRegInqDTO searchDTO,
			DefectRegInqDTO detailDTO) throws ServletException, IOException {

	    request.setCharacterEncoding("utf-8");
	    response.setContentType("text/html; charset=utf-8;");

	    List<DefectRegInqDTO> fullList = new DefectRegInqService().getDefectRegInqList(searchDTO);

	    int paCurrentPage = parseInt(request.getParameter("page"), 1);
	    int paPageSize = 10;
	    int paBlockSize = 5;

	    int paTotalCount = fullList.size();
	    int paTotalPage = (int) Math.ceil((double) paTotalCount / paPageSize);

	    if (paTotalPage < 1) {
	        paTotalPage = 1;
	    }

	    if (paCurrentPage < 1) {
	        paCurrentPage = 1;
	    }

	    if (paCurrentPage > paTotalPage) {
	        paCurrentPage = paTotalPage;
	    }

	    int fromIndex = (paCurrentPage - 1) * paPageSize;
	    int toIndex = Math.min(fromIndex + paPageSize, paTotalCount);

	    List<DefectRegInqDTO> pageList = fullList.subList(fromIndex, toIndex);

	    int paStartPage = ((paCurrentPage - 1) / paBlockSize) * paBlockSize + 1;
	    int paEndPage = paStartPage + paBlockSize - 1;

	    if (paEndPage > paTotalPage) {
	        paEndPage = paTotalPage;
	    }

	    request.setAttribute("paCurrentPage", paCurrentPage);
	    request.setAttribute("paPageSize", paPageSize);
	    request.setAttribute("paBlockSize", paBlockSize);
	    request.setAttribute("paTotalCount", paTotalCount);
	    request.setAttribute("paTotalPage", paTotalPage);
	    request.setAttribute("paStartPage", paStartPage);
	    request.setAttribute("paEndPage", paEndPage);

	    request.setAttribute("defectRegInqList", pageList);
	    request.setAttribute("defectRegInqSearchDTO", searchDTO);

	    if (detailDTO != null) {
	        request.setAttribute("defectRegInqDTO", detailDTO);
	    }

	    request.setAttribute("contentPage", "/WEB-INF/views/DefectRegInq.jsp");
	    request.getRequestDispatcher("/WEB-INF/views/table.jsp").forward(request, response);
	}

	private int parseInt(String value, int defaultValue) {
	    try {
	        return Integer.parseInt(value);
	    } catch (Exception e) {
	        return defaultValue;
	    }
	}



	private int[] parseIntArray(String[] values) {
		if (values == null)
			return new int[0];
		int[] tmp = new int[values.length];
		int idx = 0;
		for (String v : values)
			if (v != null && !"".equals(v))
				tmp[idx++] = Integer.parseInt(v);
		int[] r = new int[idx];
		System.arraycopy(tmp, 0, r, 0, idx);
		return r;
	}

	private double parseDouble(String value) {
		return (value == null || "".equals(value)) ? 0 : Double.parseDouble(value);
	}
}
