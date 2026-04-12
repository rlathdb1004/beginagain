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

/*
 * 불량 등록 / 조회 Servlet
 * 
 * 전송 방식
 * - 전부 form method="post"
 * - doPost() 중심
 * 
 * cmd 값
 * - list   : 목록 조회
 * - detail : 상세 조회
 */
@WebServlet("/defectRegInq")
public class DefectRegInqServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    // 첫 진입
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("utf-8");
        response.setContentType("text/html; charset=utf-8;");

        request.setAttribute("pageId", "page-quality-defect");
        request.setAttribute("pageTitle", "불량 등록 / 조회");
        request.setAttribute("pageSubTitle", "불량 내역을 등록하고 조회하는 화면");

        request.setAttribute("contentPage", "/WEB-INF/views/DefectRegInq.jsp");
        request.getRequestDispatcher("/WEB-INF/views/table.jsp").forward(request, response);
    }

    // POST 처리
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("utf-8");
        response.setContentType("text/html; charset=utf-8;");

        String cmd = request.getParameter("cmd");
        System.out.println("DefectRegInqServlet cmd : " + cmd);

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
            throw new ServletException("DefectRegInqServlet 처리 중 오류 발생", e);
        }
    }

    // 목록 조회
    private void list(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String defectTypeSearch = request.getParameter("defectTypeSearch");
        String startDateStr = request.getParameter("startDate");
        String endDateStr = request.getParameter("endDate");
        String searchType = request.getParameter("searchType");
        String keyword = request.getParameter("keyword");

        System.out.println("defectTypeSearch : " + defectTypeSearch);
        System.out.println("startDate        : " + startDateStr);
        System.out.println("endDate          : " + endDateStr);
        System.out.println("searchType       : " + searchType);
        System.out.println("keyword          : " + keyword);

        DefectRegInqDTO searchDTO = new DefectRegInqDTO();
        searchDTO.setDefectTypeSearch(defectTypeSearch);
        searchDTO.setSearchType(searchType);
        searchDTO.setKeyword(keyword);

        if (startDateStr != null && !"".equals(startDateStr)) {
            searchDTO.setStartDate(Date.valueOf(startDateStr));
        }

        if (endDateStr != null && !"".equals(endDateStr)) {
            searchDTO.setEndDate(Date.valueOf(endDateStr));
        }

        DefectRegInqService service = new DefectRegInqService();
        List<DefectRegInqDTO> list = service.getDefectRegInqList(searchDTO);

        request.setAttribute("defectRegInqList", list);
        request.setAttribute("defectRegInqSearchDTO", searchDTO);

        request.setAttribute("pageId", "page-quality-defect");
        request.setAttribute("pageTitle", "불량 등록 / 조회");
        request.setAttribute("pageSubTitle", "불량 내역을 등록하고 조회하는 화면");

        request.setAttribute("contentPage", "/WEB-INF/views/DefectRegInq.jsp");
        request.getRequestDispatcher("/WEB-INF/views/table.jsp").forward(request, response);
    }

    // 상세 조회
    private void detail(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String defectProductIdStr = request.getParameter("defectProductId");
        System.out.println("defectProductId : [" + defectProductIdStr + "]");

        if (defectProductIdStr == null || "".equals(defectProductIdStr.trim())) {
            throw new ServletException("상세조회용 defectProductId 값이 비어 있습니다.");
        }

        int defectProductId = Integer.parseInt(defectProductIdStr);

        DefectRegInqService service = new DefectRegInqService();
        DefectRegInqDTO dto = service.getDefectRegInqDetail(defectProductId);

        request.setAttribute("defectRegInqDTO", dto);

        request.setAttribute("pageId", "page-quality-defect");
        request.setAttribute("pageTitle", "불량 등록 / 조회");
        request.setAttribute("pageSubTitle", "불량 내역을 등록하고 조회하는 화면");

        request.setAttribute("contentPage", "/WEB-INF/views/DefectRegInq.jsp");
        request.getRequestDispatcher("/WEB-INF/views/table.jsp").forward(request, response);
    }
}
