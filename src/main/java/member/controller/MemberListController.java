package member.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import member.dto.MemberDTO;
import member.service.MemberService;

@WebServlet("/member/list")
public class MemberListController extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private MemberService memberService = new MemberService();

//	페이징을 위해 하단코드 주석후 추가 / 령 
//	@Override
//	protected void doGet(HttpServletRequest request, HttpServletResponse response)
//			throws ServletException, IOException {
//
//		List<MemberDTO> memberList = memberService.getMemberList();
//
//		request.setAttribute("memberList", memberList);
//		request.setAttribute("pageTitle", "사원관리");
//		request.setAttribute("pageSubTitle", "사원 조회 / 수정 / 삭제");
//		request.setAttribute("contentPage", "/WEB-INF/views/member/memberList.jsp");
//		request.getRequestDispatcher("/WEB-INF/views/table.jsp").forward(request, response);
//	}
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
	        throws ServletException, IOException {

	    request.setCharacterEncoding("utf-8");
	    response.setContentType("text/html; charset=utf-8;");

	    int page = parseInt(request.getParameter("page"), 1);
	    int pageSize = 10;
	    int pageBlock = 5;

	    List<MemberDTO> fullList = memberService.getMemberList();

	    int totalCount = fullList.size();
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

	    List<MemberDTO> pageList = new ArrayList<>();
	    if (totalCount > 0) {
	        pageList = fullList.subList(fromIndex, toIndex);
	    }

	    int startPage = ((page - 1) / pageBlock) * pageBlock + 1;
	    int endPage = startPage + pageBlock - 1;

	    if (endPage > totalPage) {
	        endPage = totalPage;
	    }

	    request.setAttribute("memberList", pageList);

	    request.setAttribute("paCurrentPage", page);
	    request.setAttribute("paPageSize", pageSize);
	    request.setAttribute("paBlockSize", pageBlock);
	    request.setAttribute("paTotalCount", totalCount);
	    request.setAttribute("paTotalPage", totalPage);
	    request.setAttribute("paStartPage", startPage);
	    request.setAttribute("paEndPage", endPage);

	    request.setAttribute("pageTitle", "사원관리");
	    request.setAttribute("pageSubTitle", "사원 등록, 조회");
	    request.setAttribute("contentPage", "/WEB-INF/views/member/memberList.jsp");
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

    private boolean matches(MemberDTO dto, String searchType, String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return true;
        }

        String lowerKeyword = keyword.toLowerCase();

        String empNo = dto.getEmpNo() == null ? "" : dto.getEmpNo().toLowerCase();
        String empName = dto.getEmpName() == null ? "" : dto.getEmpName().toLowerCase();
        String deptCode = dto.getDeptCode() == null ? "" : dto.getDeptCode().toLowerCase();
        String positionName = dto.getPositionName() == null ? "" : dto.getPositionName().toLowerCase();

        if ("empNo".equals(searchType)) {
            return empNo.contains(lowerKeyword);
        } else if ("empName".equals(searchType)) {
            return empName.contains(lowerKeyword);
        } else if ("deptCode".equals(searchType)) {
            return deptCode.contains(lowerKeyword);
        } else if ("positionName".equals(searchType)) {
            return positionName.contains(lowerKeyword);
        } else {
            return empNo.contains(lowerKeyword)
                    || empName.contains(lowerKeyword)
                    || deptCode.contains(lowerKeyword)
                    || positionName.contains(lowerKeyword);
        }
    }

}
