package WorkMgmt.WORegInq.Controller;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.sql.Date;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import WorkMgmt.WORegInq.DTO.WORegInqDTO;
import WorkMgmt.WORegInq.Service.WORegInqService;

@WebServlet("/woregInq")
public class WORegInqController extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private WORegInqService service = new WORegInqService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("utf-8");
        response.setContentType("text/html; charset=utf-8");

        String startDate = nvl(request.getParameter("startDate"));
        String endDate = nvl(request.getParameter("endDate"));
        String searchType = nvl(request.getParameter("searchType"));
        String keyword = nvl(request.getParameter("keyword"));

        int page = parseInt(request.getParameter("page"), 1);
        int pageSize = 10;
        int blockSize = 5;

        try {
            int totalCount = service.getTotalCount(startDate, endDate, searchType, keyword);

            int totalPage = (int) Math.ceil(totalCount / (double) pageSize);
            if (totalPage == 0) {
                totalPage = 1;
            }

            if (page < 1) {
                page = 1;
            }
            if (page > totalPage) {
                page = totalPage;
            }

            int startRow = (page - 1) * pageSize + 1;
            int endRow = page * pageSize;

            List<WORegInqDTO> list = service.getList(startDate, endDate, searchType, keyword, startRow, endRow);

            int startPage = ((page - 1) / blockSize) * blockSize + 1;
            int endPage = startPage + blockSize - 1;
            if (endPage > totalPage) {
                endPage = totalPage;
            }

            request.setAttribute("list", list);
            request.setAttribute("page", page);
            request.setAttribute("pageSize", pageSize);
            request.setAttribute("totalCount", totalCount);
            request.setAttribute("totalPage", totalPage);
            request.setAttribute("startPage", startPage);
            request.setAttribute("endPage", endPage);

            request.getRequestDispatcher("/WEB-INF/views/WORegInq.jsp").forward(request, response);

        } catch (Exception e) {
            throw new ServletException("작업지시 조회 중 오류 발생", e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("utf-8");
        response.setContentType("text/html; charset=utf-8");

        String actionType = nvl(request.getParameter("actionType"));

        try {
            if ("insert".equals(actionType)) {
                WORegInqDTO dto = new WORegInqDTO();

                dto.setWorkOrderNo(nvl(request.getParameter("workOrderNo")));
                dto.setWorkDate(parseDate(request.getParameter("workDate")));
                dto.setItemCode(nvl(request.getParameter("itemCode")));
                dto.setItemName(nvl(request.getParameter("itemName")));
                dto.setWorkQty(parseBigDecimal(request.getParameter("workQty")));
                dto.setUnitName(nvl(request.getParameter("unitName")));
                dto.setLineName(nvl(request.getParameter("lineName")));
                dto.setProcessName(nvl(request.getParameter("processName")));
                dto.setEmpName(nvl(request.getParameter("empName")));
                dto.setBomName(nvl(request.getParameter("bomName")));
                dto.setRemark(nvl(request.getParameter("remark")));

                service.insert(dto);

                response.sendRedirect(request.getContextPath() + "/workorder");
                return;
            }

            if ("delete".equals(actionType)) {
                int workOrderId = parseInt(request.getParameter("selectedWorkOrderId"), 0);
                service.delete(workOrderId);

                response.sendRedirect(request.getContextPath() + "/workorder");
                return;
            }

            response.sendRedirect(request.getContextPath() + "/workorder");

        } catch (IllegalArgumentException e) {
            String msg = URLEncoder.encode(e.getMessage(), "UTF-8");
            response.sendRedirect(request.getContextPath() + "/workorder?error=" + msg);
        } catch (Exception e) {
            throw new ServletException("작업지시 등록/삭제 중 오류 발생", e);
        }
    }

    private String nvl(String str) {
        return str == null ? "" : str.trim();
    }

    private int parseInt(String str, int defaultValue) {
        try {
            return Integer.parseInt(str);
        } catch (Exception e) {
            return defaultValue;
        }
    }

    private Date parseDate(String str) {
        if (str == null || str.trim().isEmpty()) {
            return null;
        }
        return Date.valueOf(str);
    }

    private BigDecimal parseBigDecimal(String str) {
        if (str == null || str.trim().isEmpty()) {
            return null;
        }
        return new BigDecimal(str);
    }
}