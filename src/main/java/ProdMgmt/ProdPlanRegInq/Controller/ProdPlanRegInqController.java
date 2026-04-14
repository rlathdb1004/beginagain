package ProdMgmt.ProdPlanRegInq.Controller;

import java.io.IOException;
import java.sql.Date;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import ProdMgmt.ProdPlanRegInq.DTO.ProdPlanRegInqDTO;
import ProdMgmt.ProdPlanRegInq.Service.ProdPlanRegInqService;

@WebServlet("/prodplan")
public class ProdPlanRegInqController extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private final ProdPlanRegInqService service = new ProdPlanRegInqService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String cmd = request.getParameter("cmd");
        if (cmd == null || "".equals(cmd) || "list".equals(cmd)) {
            forwardList(request, response);
            return;
        }
        if ("detail".equals(cmd)) {
            forwardDetail(request, response);
            return;
        }
        response.sendRedirect(request.getContextPath() + "/prodplan");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("utf-8");
        String cmd = request.getParameter("cmd");
        if (cmd == null || "".equals(cmd))
            cmd = "list";
        try {
            if ("register".equals(cmd)) {
                ProdPlanRegInqDTO dto = buildDto(request);
                service.register(dto);
                setFlashMessage(request.getSession(), "생산계획이 등록되었습니다.");
                response.sendRedirect(request.getContextPath() + "/prodplan");
            } else if ("update".equals(cmd)) {
                ProdPlanRegInqDTO dto = buildDto(request);
                dto.setSeqNO(parseInt(request.getParameter("planId")));
                service.update(dto);
                setFlashMessage(request.getSession(), "생산계획이 수정되었습니다.");
                response.sendRedirect(request.getContextPath() + "/prodplan?cmd=detail&planId=" + dto.getSeqNO());
            } else if ("delete".equals(cmd)) {
                int[] ids = extractIds(request.getParameterValues("planIds"));
                service.delete(ids);
                setFlashMessage(request.getSession(), "선택한 생산계획이 삭제되었습니다.");
                response.sendRedirect(request.getContextPath() + "/prodplan");
            } else if ("deleteOne".equals(cmd)) {
                int planId = parseInt(request.getParameter("planId"));
                service.delete(new int[] { planId });
                setFlashMessage(request.getSession(), "생산계획이 삭제되었습니다.");
                response.sendRedirect(request.getContextPath() + "/prodplan");
            } else {
                forwardList(request, response);
            }
        } catch (Exception e) {
            throw new ServletException("ProdPlanRegInq 처리 중 오류 발생", e);
        }
    }

    private ProdPlanRegInqDTO buildDto(HttpServletRequest request) {
        ProdPlanRegInqDTO dto = new ProdPlanRegInqDTO();
        dto.setPlanCode(request.getParameter("planCode"));
        dto.setPlanDate(Date.valueOf(request.getParameter("planDate")));
        dto.setPlanAmount(parseInt(request.getParameter("planAmount")));
        dto.setPlanLine(request.getParameter("planLine"));
        dto.setMemo(request.getParameter("memo"));
        return dto;
    }

    private int[] extractIds(String[] values) {
        int[] ids = new int[values == null ? 0 : values.length];
        int idx = 0;
        if (values != null) {
            for (String v : values) {
                if (v != null && !"".equals(v)) {
                    ids[idx++] = parseInt(v);
                }
            }
        }
        int[] trimmed = new int[idx];
        System.arraycopy(ids, 0, trimmed, 0, idx);
        return trimmed;
    }

    private int parseInt(String value) {
        if (value == null || "".equals(value))
            return 0;
        return Integer.parseInt(value);
    }

    private void forwardList(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        List<ProdPlanRegInqDTO> list = service.getList();
        request.setAttribute("list", list);
        request.setAttribute("itemOptions", service.getItemOptions());
        request.setAttribute("pageTitle", "생산관리");
        request.setAttribute("pageSubTitle", "생산계획 등록 / 조회 / 수정 / 삭제");
        request.setAttribute("contentPage", "/WEB-INF/views/ProdPlanRegInq.jsp");
        request.getRequestDispatcher("/WEB-INF/views/table.jsp").forward(request, response);
    }

    private void forwardDetail(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        int planId = parseInt(request.getParameter("planId"));
        ProdPlanRegInqDTO detail = service.getDetail(planId);
        if (detail == null) {
            setFlashMessage(request.getSession(), "조회할 생산계획이 없습니다.");
            response.sendRedirect(request.getContextPath() + "/prodplan");
            return;
        }
        request.setAttribute("detail", detail);
        request.setAttribute("itemOptions", service.getItemOptions());
        request.setAttribute("pageTitle", "생산관리");
        request.setAttribute("pageSubTitle", "생산계획 상세 / 수정 / 삭제");
        request.setAttribute("contentPage", "/WEB-INF/views/prodplan/prodPlanDetail.jsp");
        request.getRequestDispatcher("/WEB-INF/views/table.jsp").forward(request, response);
    }

    private void setFlashMessage(HttpSession session, String message) {
        session.setAttribute("flashMessage", message);
    }
}
