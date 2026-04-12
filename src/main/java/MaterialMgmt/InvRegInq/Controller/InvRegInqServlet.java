package MaterialMgmt.InvRegInq.Controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import MaterialMgmt.InvRegInq.DTO.InvRegInqDTO;
import MaterialMgmt.InvRegInq.Service.InvRegInqService;

/*
 * 재고 등록 / 조회 Servlet
 * 
 * 전송 방식
 * - 전부 form method="post"
 * - doPost()만 사용
 * 
 * cmd 값
 * - list   : 목록 조회
 * - detail : 상세 조회
 */
@WebServlet("/invRegInq")
public class InvRegInqServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    // 첫 진입 화면
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("utf-8");
        response.setContentType("text/html; charset=utf-8;");

        request.setAttribute("pageId", "page-materials-inventory");
        request.setAttribute("pageTitle", "재고 등록 / 조회");
        request.setAttribute("pageSubTitle", "현재 재고 현황을 조회하는 화면");

        request.setAttribute("contentPage", "/WEB-INF/views/InvRegInq.jsp");
        request.getRequestDispatcher("/WEB-INF/views/table.jsp").forward(request, response);
    }

    // POST 처리
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("utf-8");
        response.setContentType("text/html; charset=utf-8;");

        String cmd = request.getParameter("cmd");
        System.out.println("InvRegInqServlet cmd : " + cmd);

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
            throw new ServletException("InvRegInqServlet 처리 중 오류 발생", e);
        }
    }

    // 재고 목록 조회
    private void list(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String startDateStr = request.getParameter("startDate");
        String endDateStr = request.getParameter("endDate");
        String searchType = request.getParameter("searchType");
        String keyword = request.getParameter("keyword");

        System.out.println("startDate : " + startDateStr);
        System.out.println("endDate   : " + endDateStr);
        System.out.println("searchType: " + searchType);
        System.out.println("keyword   : " + keyword);

        InvRegInqDTO searchDTO = new InvRegInqDTO();
        searchDTO.setSearchType(searchType);
        searchDTO.setKeyword(keyword);

        if (startDateStr != null && !"".equals(startDateStr)) {
            searchDTO.setStartDate(java.sql.Date.valueOf(startDateStr));
        }

        if (endDateStr != null && !"".equals(endDateStr)) {
            searchDTO.setEndDate(java.sql.Date.valueOf(endDateStr));
        }

        InvRegInqService service = new InvRegInqService();
        List<InvRegInqDTO> list = service.getInvRegInqList(searchDTO);

        request.setAttribute("invRegInqList", list);
        request.setAttribute("invRegInqSearchDTO", searchDTO);

        request.setAttribute("pageId", "page-materials-inventory");
        request.setAttribute("pageTitle", "재고 등록 / 조회");
        request.setAttribute("pageSubTitle", "현재 재고 현황을 조회하는 화면");
        request.setAttribute("contentPage", "/WEB-INF/views/InvRegInq.jsp");

        request.getRequestDispatcher("/WEB-INF/views/table.jsp").forward(request, response);
    }
    
    // 재고 상세 조회
    private void detail(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String inventoryIdStr = request.getParameter("inventoryId");
        System.out.println("inventoryId : [" + inventoryIdStr + "]");

        if (inventoryIdStr == null || "".equals(inventoryIdStr.trim())) {
            throw new ServletException("상세조회용 inventoryId 값이 비어 있습니다.");
        }

        int inventoryId = Integer.parseInt(inventoryIdStr);

        InvRegInqService service = new InvRegInqService();
        InvRegInqDTO dto = service.getInvRegInqDetail(inventoryId);

        request.setAttribute("invRegInqDTO", dto);

        request.setAttribute("pageId", "page-materials-inventory");
        request.setAttribute("pageTitle", "재고 등록 / 조회");
        request.setAttribute("pageSubTitle", "현재 재고 현황을 조회하는 화면");

        request.setAttribute("contentPage", "/WEB-INF/views/InvRegInq.jsp");
        request.getRequestDispatcher("/WEB-INF/views/table.jsp").forward(request, response);
    }
}
