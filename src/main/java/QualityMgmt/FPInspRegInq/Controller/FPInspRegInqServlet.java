package QualityMgmt.FPInspRegInq.Controller;

import java.io.IOException;
import java.sql.Date;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import QualityMgmt.FPInspRegInq.DTO.FPInspRegInqDTO;
import QualityMgmt.FPInspRegInq.Service.FPInspRegInqService;

/*
 * 완제품 검사 등록 / 조회 Servlet
 * 
 * 전송 방식
 * - 전부 form method="post"
 * - doPost() 중심
 * 
 * cmd 값
 * - list   : 목록 조회
 * - detail : 상세 조회
 */
@WebServlet("/fpInspRegInq")
public class FPInspRegInqServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    // 첫 진입
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("utf-8");
        response.setContentType("text/html; charset=utf-8;");

        request.setAttribute("pageId", "page-quality-finished-inspection");
        request.setAttribute("pageTitle", "완제품 검사 등록 / 조회");
        request.setAttribute("pageSubTitle", "완제품 검사 내역을 등록하고 조회하는 화면");

        request.setAttribute("contentPage", "/WEB-INF/views/FPInspRegInq.jsp");
        request.getRequestDispatcher("/WEB-INF/views/table.jsp").forward(request, response);
    }

    // POST 처리
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("utf-8");
        response.setContentType("text/html; charset=utf-8;");

        String cmd = request.getParameter("cmd");
        System.out.println("FPInspRegInq cmd : " + cmd);

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
            throw new ServletException("FPInspRegInq 처리 중 오류 발생", e);
        }
    }

    // 목록 조회
    private void list(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String resultType = request.getParameter("resultType");
        String startDateStr = request.getParameter("startDate");
        String endDateStr = request.getParameter("endDate");
        String searchType = request.getParameter("searchType");
        String keyword = request.getParameter("keyword");

        System.out.println("resultType: " + resultType);
        System.out.println("startDate : " + startDateStr);
        System.out.println("endDate   : " + endDateStr);
        System.out.println("searchType: " + searchType);
        System.out.println("keyword   : " + keyword);

        FPInspRegInqDTO searchDTO = new FPInspRegInqDTO();
        searchDTO.setResultType(resultType);
        searchDTO.setSearchType(searchType);
        searchDTO.setKeyword(keyword);

        if (startDateStr != null && !"".equals(startDateStr)) {
            searchDTO.setStartDate(Date.valueOf(startDateStr));
        }

        if (endDateStr != null && !"".equals(endDateStr)) {
            searchDTO.setEndDate(Date.valueOf(endDateStr));
        }

        FPInspRegInqService service = new FPInspRegInqService();
        List<FPInspRegInqDTO> list = service.getFPInspRegInqList(searchDTO);

        request.setAttribute("fpInspRegInqList", list);
        request.setAttribute("fpInspRegInqSearchDTO", searchDTO);

        request.setAttribute("pageId", "page-quality-finished-inspection");
        request.setAttribute("pageTitle", "완제품 검사 등록 / 조회");
        request.setAttribute("pageSubTitle", "완제품 검사 내역을 등록하고 조회하는 화면");

        request.setAttribute("contentPage", "/WEB-INF/views/FPInspRegInq.jsp");
        request.getRequestDispatcher("/WEB-INF/views/table.jsp").forward(request, response);
    }

    // 상세 조회
    private void detail(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String finalInspectionIdStr = request.getParameter("finalInspectionId");
        System.out.println("finalInspectionId : [" + finalInspectionIdStr + "]");

        if (finalInspectionIdStr == null || "".equals(finalInspectionIdStr.trim())) {
            throw new ServletException("상세조회용 finalInspectionId 값이 비어 있습니다.");
        }

        int finalInspectionId = Integer.parseInt(finalInspectionIdStr);

        FPInspRegInqService service = new FPInspRegInqService();
        FPInspRegInqDTO dto = service.getFPInspRegInqDetail(finalInspectionId);

        request.setAttribute("fpInspRegInqDTO", dto);

        request.setAttribute("pageId", "page-quality-finished-inspection");
        request.setAttribute("pageTitle", "완제품 검사 등록 / 조회");
        request.setAttribute("pageSubTitle", "완제품 검사 내역을 등록하고 조회하는 화면");

        request.setAttribute("contentPage", "/WEB-INF/views/FPInspRegInq.jsp");
        request.getRequestDispatcher("/WEB-INF/views/table.jsp").forward(request, response);
    }
}
