<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<div class="paPagingWrap">
    <button type="button"
            class="paPageBtn"
            onclick="paMovePage(1)"
            <c:if test="${paCurrentPage <= 1}">disabled="disabled"</c:if>>
        &lt;&lt;
    </button>

    <button type="button"
            class="paPageBtn"
            onclick="paMovePage(${paCurrentPage - 1})"
            <c:if test="${paCurrentPage <= 1}">disabled="disabled"</c:if>>
        &lt;
    </button>

    <c:forEach begin="${paStartPage}" end="${paEndPage}" var="paPageNo">
        <button type="button"
                class="paPageBtn ${paPageNo == paCurrentPage ? 'paPageActive' : ''}"
                onclick="paMovePage(${paPageNo})">
            ${paPageNo}
        </button>
    </c:forEach>

    <button type="button"
            class="paPageBtn"
            onclick="paMovePage(${paCurrentPage + 1})"
            <c:if test="${paCurrentPage >= paTotalPage}">disabled="disabled"</c:if>>
        &gt;
    </button>

    <button type="button"
            class="paPageBtn"
            onclick="paMovePage(${paTotalPage})"
            <c:if test="${paCurrentPage >= paTotalPage}">disabled="disabled"</c:if>>
        &gt;&gt;
    </button>
</div>