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

	    request.setCharacterEncoding("utf-8");
	    response.setContentType("text/html; charset=utf-8;");

	    // 공통 껍데기에 들어갈 페이지 정보
	    request.setAttribute("pageId", "page-materials-register");
	    request.setAttribute("pageTitle", "입출고 등록 / 조회");
	    request.setAttribute("pageSubTitle", "자재 입출고 내역을 등록하고 조회하는 화면");

	    // 실제 내용 JSP
	    request.setAttribute("contentPage", "/WEB-INF/views/IORegInq.jsp");

	    // 공통 껍데기로 이동
	    request.getRequestDispatcher("/WEB-INF/views/table.jsp").forward(request, response);
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
	        throws ServletException, IOException {

		request.setCharacterEncoding("utf-8");
		response.setContentType("text/html; charset=utf-8;");

		String cmd = request.getParameter("cmd");
		System.out.println("IORegInqServlet cmd : " + cmd);

		if (cmd == null || "".equals(cmd)) {
			cmd = "list";
		}

		try {
			if ("list".equals(cmd)) {
				list(request, response);
			} else if ("detail".equals(cmd)) {
				detail(request, response);
			} else {
				list(request, response);
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new ServletException("IORegInqServlet 처리 중 오류 발생", e);
		}
	}

	// 입출고 목록 조회
	private void list(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String inoutType = request.getParameter("inoutType");
		String startDateStr = request.getParameter("startDate");
		String endDateStr = request.getParameter("endDate");
		String searchType = request.getParameter("searchType");
		String keyword = request.getParameter("keyword");

		System.out.println("inoutType : " + inoutType);
		System.out.println("startDate : " + startDateStr);
		System.out.println("endDate   : " + endDateStr);
		System.out.println("searchType: " + searchType);
		System.out.println("keyword   : " + keyword);

		IORegInqDTO searchDTO = new IORegInqDTO();
		searchDTO.setInoutType(inoutType);
		searchDTO.setSearchType(searchType);
		searchDTO.setKeyword(keyword);

		if (startDateStr != null && !"".equals(startDateStr)) {
			searchDTO.setStartDate(Date.valueOf(startDateStr));
		}

		if (endDateStr != null && !"".equals(endDateStr)) {
			searchDTO.setEndDate(Date.valueOf(endDateStr));
		}

		IORegInqService service = new IORegInqService();
		List<IORegInqDTO> list = service.getIORegInqList(searchDTO);

		request.setAttribute("ioRegInqList", list);
		request.setAttribute("ioRegInqSearchDTO", searchDTO);

		request.setAttribute("pageId", "page-materials-register");
		request.setAttribute("pageTitle", "입출고 등록 / 조회");
		request.setAttribute("pageSubTitle", "자재 입출고 내역을 등록하고 조회하는 화면");
		request.setAttribute("contentPage", "/WEB-INF/views/IORegInq.jsp");

		request.getRequestDispatcher("/WEB-INF/views/table.jsp").forward(request, response);
	}

	// 입출고 상세 조회
	private void detail(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String inoutIdStr = request.getParameter("inoutId");
		System.out.println("inoutId : [" + inoutIdStr + "]");

		if (inoutIdStr == null || "".equals(inoutIdStr.trim())) {
			throw new ServletException("상세조회용 inoutId 값이 비어 있습니다.");
		}

		int inoutId = Integer.parseInt(inoutIdStr);

		IORegInqService service = new IORegInqService();
		IORegInqDTO dto = service.getIORegInqDetail(inoutId);

		request.setAttribute("ioRegInqDTO", dto);

		request.setAttribute("pageId", "page-materials-register");
		request.setAttribute("pageTitle", "입출고 등록 / 조회");
		request.setAttribute("pageSubTitle", "자재 입출고 내역을 등록하고 조회하는 화면");
		request.setAttribute("contentPage", "/WEB-INF/views/IORegInq.jsp");

		request.getRequestDispatcher("/WEB-INF/views/table.jsp").forward(request, response);
	}
}

