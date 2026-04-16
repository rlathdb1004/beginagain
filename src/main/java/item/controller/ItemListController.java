package item.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import item.dto.ItemDTO;
import item.service.ItemService;

@WebServlet("/item/list")
public class ItemListController extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private ItemService itemService = new ItemService();

//    페이지를 위한 주석처리 하단 코딩 추가 /령
//    @Override
//    protected void doGet(HttpServletRequest request, HttpServletResponse response)
//            throws ServletException, IOException {
//
//        List<ItemDTO> itemList = itemService.getItemList();
//
//        request.setAttribute("itemList", itemList);
//        request.setAttribute("pageTitle", "품목관리");
//		request.setAttribute("pageSubTitle", "품목 등록 / 조회 / 수정 / 삭제");
//		request.setAttribute("contentPage", "/WEB-INF/views/item/itemList.jsp");
//		request.getRequestDispatcher("/WEB-INF/views/table.jsp").forward(request, response);
//    }
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

        List<ItemDTO> fullList = itemService.getItemList();
        List<ItemDTO> filteredList = new ArrayList<>();

        for (ItemDTO dto : fullList) {
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

        List<ItemDTO> pageList = new ArrayList<>();
        if (totalCount > 0) {
            pageList = filteredList.subList(fromIndex, toIndex);
        }

        int startPage = ((page - 1) / pageBlock) * pageBlock + 1;
        int endPage = startPage + pageBlock - 1;

        if (endPage > totalPage) {
            endPage = totalPage;
        }

        request.setAttribute("itemList", pageList);
        request.setAttribute("searchType", searchType);
        request.setAttribute("keyword", keyword);

        request.setAttribute("paCurrentPage", page);
        request.setAttribute("paPageSize", pageSize);
        request.setAttribute("paBlockSize", pageBlock);
        request.setAttribute("paTotalCount", totalCount);
        request.setAttribute("paTotalPage", totalPage);
        request.setAttribute("paStartPage", startPage);
        request.setAttribute("paEndPage", endPage);

        request.setAttribute("pageTitle", "품목관리");
        request.setAttribute("pageSubTitle", "품목 등록 / 조회 / 수정 / 삭제");
        request.setAttribute("contentPage", "/WEB-INF/views/item/itemList.jsp");
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

    private boolean matches(ItemDTO dto, String searchType, String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return true;
        }

        String lowerKeyword = keyword.toLowerCase();

        String itemCode = dto.getItemCode() == null ? "" : dto.getItemCode().toLowerCase();
        String itemName = dto.getItemName() == null ? "" : dto.getItemName().toLowerCase();
        String itemType = dto.getItemType() == null ? "" : dto.getItemType().toLowerCase();
        String supplierName = dto.getSupplierName() == null ? "" : dto.getSupplierName().toLowerCase();

        if ("itemCode".equals(searchType)) {
            return itemCode.contains(lowerKeyword);
        } else if ("itemName".equals(searchType)) {
            return itemName.contains(lowerKeyword);
        } else if ("itemType".equals(searchType)) {
            return itemType.contains(lowerKeyword);
        } else if ("supplierName".equals(searchType)) {
            return supplierName.contains(lowerKeyword);
        } else {
            return itemCode.contains(lowerKeyword)
                    || itemName.contains(lowerKeyword)
                    || itemType.contains(lowerKeyword)
                    || supplierName.contains(lowerKeyword);
        }
    }
}