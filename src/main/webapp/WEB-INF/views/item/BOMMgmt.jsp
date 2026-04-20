<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<c:set var="isMesAdmin" value="${sessionScope.loginUser.roleName eq 'MES_ADMIN'}" />
<c:set var="productCodeValue" value="${not empty param.product_code ? param.product_code : keyword}" />

<div class="taPageActions">
    <c:if test="${isMesAdmin}">
        <button type="button" class="taBtn taBtnPrimary" data-modal-target="registerModal">등록</button>
        <button type="submit" form="deleteForm" class="taBtn taBtnOutline" onclick="return confirm('선택한 BOM을 삭제하시겠습니까?');">선택 삭제</button>
    </c:if>
</div>

<form id="paSearchForm" method="get" action="${pageContext.request.contextPath}/BOM-mgmt">
    <div class="taToolbarRow">
        <div class="taToolbarField taToolbarSpan12">
            <div class="taSearchBox">
                <select class="taSelect taAutoSelectColor ${empty productCodeValue ? 'taSelectPlaceholder' : ''}" name="product_code" onchange="this.form.submit()">
                    <option value="">완제품 선택</option>
                    <c:forEach var="item" items="${productItems}">
                        <option value="${item.item_code}" ${productCodeValue eq item.item_code ? 'selected' : ''}>${item.item_code} - ${item.item_name}</option>
                    </c:forEach>
                </select>
                <button type="button" class="taBtn taBtnOutline taSearchReset" onclick="location.href='${pageContext.request.contextPath}/BOM-mgmt'">초기화</button>
            </div>
        </div>
    </div>
</form>

<form id="deleteForm" method="post" action="${pageContext.request.contextPath}/BOM-del">
    <input type="hidden" name="product_code" value="${productCodeValue}">

    <div class="taTableShell">
        <div class="taTableScroll">
            <table class="taMesTable">
                <thead>
                    <tr>
                        <th class="taTableHeadCell taCheckCell">
                            <c:if test="${isMesAdmin}">
                                <input type="checkbox" id="checkAll" class="taCheckInput">
                            </c:if>
                        </th>
                        <th class="taTableHeadCell taColFit">완제품코드</th>
                        <th class="taTableHeadCell taColGrow">완제품명</th>
                        <th class="taTableHeadCell taColFit">원자재코드</th>
                        <th class="taTableHeadCell taColGrow">원자재명</th>
                        <th class="taTableHeadCell taColFit">소요량</th>
                        <th class="taTableHeadCell taColFit">단위</th>
                        <th class="taTableHeadCell taColAction taLastCol">상세</th>
                    </tr>
                </thead>
                <tbody>
                    <c:choose>
                        <c:when test="${not empty list}">
                            <c:forEach var="b" items="${list}">
                                <tr class="taTableBodyRow">
                                    <td class="taTableBodyCell taCheckCell">
                                        <c:if test="${isMesAdmin}">
                                            <input type="checkbox" name="bom_detail_id" value="${b.bom_detail_id}" class="taCheckInput">
                                        </c:if>
                                    </td>
                                    <td class="taTableBodyCell taColFit">${b.product_code}</td>
                                    <td class="taTableBodyCell taColGrow">${b.product_name}</td>
                                    <td class="taTableBodyCell taColFit">${b.material_code}</td>
                                    <td class="taTableBodyCell taColGrow">${b.material_name}</td>
                                    <td class="taTableBodyCell taColFit">${b.qty_required}</td>
                                    <td class="taTableBodyCell taColFit">${b.unit}</td>
                                    <td class="taTableBodyCell taColAction taLastCol">
                                        <button type="button" class="taLinkButton" onclick="location.href='${pageContext.request.contextPath}/bom-detail?bomDetailId=${b.bom_detail_id}'">상세보기</button>
                                    </td>
                                </tr>
                            </c:forEach>
                        </c:when>
                        <c:otherwise>
                            <tr class="taTableBodyRow">
                                <td class="taTableBodyCell taLastCol" colspan="8">
                                    <c:choose>
                                        <c:when test="${empty productCodeValue}">완제품 코드를 검색하세요.</c:when>
                                        <c:otherwise>조회된 BOM이 없습니다.</c:otherwise>
                                    </c:choose>
                                </td>
                            </tr>
                        </c:otherwise>
                    </c:choose>
                </tbody>
            </table>
        </div>
    </div>
</form>

<c:if test="${isMesAdmin}">
    <div class="taModal" id="registerModal" hidden aria-hidden="true">
        <div class="taModalDialog modal-lg">
            <div class="taModalHeader">
                <h3 class="taModalTitle">BOM 등록</h3>
                <button type="button" class="taModalClose">&times;</button>
            </div>
            <form method="post" action="${pageContext.request.contextPath}/BOM-mgmt">
                <div class="taModalBody taModalGrid">
                    <div class="form-row">
                        <label>완제품</label>
                        <select name="product_code" required>
                            <option value="">완제품 선택</option>
                            <c:forEach var="item" items="${productItems}">
                                <option value="${item.item_code}" ${productCodeValue eq item.item_code ? 'selected' : ''}>${item.item_code} - ${item.item_name}</option>
                            </c:forEach>
                        </select>
                    </div>
                    <div class="form-row">
                        <label>원자재</label>
                        <select name="material_id" required>
                            <option value="">원자재 선택</option>
                            <c:forEach var="item" items="${materialItems}">
                                <option value="${item.item_id}">${item.item_code} - ${item.item_name}</option>
                            </c:forEach>
                        </select>
                    </div>
                    <div class="form-row">
                        <label>소요량</label>
                        <input type="number" step="0.001" min="0" name="qty_required" required>
                    </div>
                    <div class="form-row">
                        <label>비고</label>
                        <input type="text" name="remark">
                    </div>
                </div>
                <div class="taModalFooter">
                    <button type="button" class="taBtn taBtnOutline taModalClose">취소</button>
                    <button type="submit" class="taBtn taBtnPrimary">등록</button>
                </div>
            </form>
        </div>
    </div>
</c:if>

<script>
(function() {
    const checkAll = document.getElementById('checkAll');
    if (checkAll) {
        checkAll.addEventListener('change', function() {
            document.querySelectorAll("input[name='bom_detail_id']").forEach(function(cb) {
                cb.checked = checkAll.checked;
            });
        });
    }
})();
</script>
