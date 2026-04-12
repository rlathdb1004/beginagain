package QualityMgmt.MatInspRegInq.Controller;

import java.io.IOException;
import java.sql.Date;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import QualityMgmt.MatInspRegInq.DTO.MatInspRegInqDTO;
import QualityMgmt.MatInspRegInq.Service.MatInspRegInqService;

/*
 * 자재 검사 등록 / 조회 Servlet
 * 
 * 전송 방식
 * - 전부 form method="post"
 * - doPost() 중심
 * 
 * cmd 값
 * - list   : 목록 조회
 * - detail : 상세 조회
 */
@WebServlet("/matInspRegInq")
public class MatInspRegInqServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    // 첫 진입
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("utf-8");
        response.setContentType("text/html; charset=utf-8;");

        request.setAttribute("pageId", "page-quality-material-inspection");
        request.setAttribute("pageTitle", "자재 검사 등록 / 조회");
        request.setAttribute("pageSubTitle", "자재 검사 내역을 등록하고 조회하는 화면");

        request.setAttribute("contentPage", "/WEB-INF/views/MatInspRegInq.jsp");
        request.getRequestDispatcher("/WEB-INF/views/table.jsp").forward(request, response);
    }

    // POST 처리
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("utf-8");
        response.setContentType("text/html; charset=utf-8;");

        String cmd = request.getParameter("cmd");
        System.out.println("MatInspRegInq cmd : " + cmd);

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
            throw new ServletException("MatInspRegInq 처리 중 오류 발생", e);
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

        MatInspRegInqDTO searchDTO = new MatInspRegInqDTO();
        searchDTO.setResultType(resultType);
        searchDTO.setSearchType(searchType);
        searchDTO.setKeyword(keyword);

        if (startDateStr != null && !"".equals(startDateStr)) {
            searchDTO.setStartDate(Date.valueOf(startDateStr));
        }

        if (endDateStr != null && !"".equals(endDateStr)) {
            searchDTO.setEndDate(Date.valueOf(endDateStr));
        }

        MatInspRegInqService service = new MatInspRegInqService();
        List<MatInspRegInqDTO> list = service.getMatInspRegInqList(searchDTO);

        request.setAttribute("matInspRegInqList", list);
        request.setAttribute("matInspRegInqSearchDTO", searchDTO);

        request.setAttribute("pageId", "page-quality-material-inspection");
        request.setAttribute("pageTitle", "자재 검사 등록 / 조회");
        request.setAttribute("pageSubTitle", "자재 검사 내역을 등록하고 조회하는 화면");

        request.setAttribute("contentPage", "/WEB-INF/views/MatInspRegInq.jsp");
        request.getRequestDispatcher("/WEB-INF/views/table.jsp").forward(request, response);
    }

    // 상세 조회
    private void detail(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String materialInspectionIdStr = request.getParameter("materialInspectionId");
        System.out.println("materialInspectionId : [" + materialInspectionIdStr + "]");

        if (materialInspectionIdStr == null || "".equals(materialInspectionIdStr.trim())) {
            throw new ServletException("상세조회용 materialInspectionId 값이 비어 있습니다.");
        }

        int materialInspectionId = Integer.parseInt(materialInspectionIdStr);

        MatInspRegInqService service = new MatInspRegInqService();
        MatInspRegInqDTO dto = service.getMatInspRegInqDetail(materialInspectionId);

        request.setAttribute("matInspRegInqDTO", dto);

        request.setAttribute("pageId", "page-quality-material-inspection");
        request.setAttribute("pageTitle", "자재 검사 등록 / 조회");
        request.setAttribute("pageSubTitle", "자재 검사 내역을 등록하고 조회하는 화면");

        request.setAttribute("contentPage", "/WEB-INF/views/MatInspRegInq.jsp");
        request.getRequestDispatcher("/WEB-INF/views/table.jsp").forward(request, response);
    }
}
