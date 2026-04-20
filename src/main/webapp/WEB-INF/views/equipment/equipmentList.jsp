<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<c:set var="isMesAdmin" value="${sessionScope.loginUser.roleName eq 'MES_ADMIN'}" />

<div class="taPageActions">
    <c:if test="${isMesAdmin}">
        <button type="button" class="taBtn taBtnPrimary" data-modal-target="registerModal">등록</button>
        <button type="submit" form="deleteForm" class="taBtn taBtnOutline" onclick="return confirm('선택한 설비를 삭제하시겠습니까?');">선택 삭제</button>
    </c:if>
</div>

<form id="paSearchForm" method="get" action="${pageContext.request.contextPath}/equipment/list">
    <input type="hidden" name="page" id="paPage" value="${paCurrentPage}">

    <div class="taToolbarRow">
        <div class="taToolbarField taToolbarSpan3">
            <select class="taSelect taAutoSelectColor ${empty searchType ? 'taSelectPlaceholder' : ''}" name="searchType">
                <option value="" ${empty searchType ? 'selected' : ''}>전체 / 설비코드 ...</option>
                <option value="equipmentCode" ${searchType eq 'equipmentCode' ? 'selected' : ''}>설비코드</option>
                <option value="equipmentName" ${searchType eq 'equipmentName' ? 'selected' : ''}>설비명</option>
                <option value="modelName" ${searchType eq 'modelName' ? 'selected' : ''}>모델명</option>
                <option value="manufacturer" ${searchType eq 'manufacturer' ? 'selected' : ''}>제조사</option>
                <option value="vendorName" ${searchType eq 'vendorName' ? 'selected' : ''}>공급처</option>
                <option value="location" ${searchType eq 'location' ? 'selected' : ''}>위치</option>
            </select>
        </div>
        <div class="taToolbarField taToolbarFieldGrow taToolbarSpan9">
            <div class="taSearchBox">
                <input type="text" class="taSearchInput" name="keyword" value="${keyword}" placeholder="검색키워드">
                <button type="submit" class="taSearchBtn" aria-label="검색" onclick="document.getElementById('paPage').value=1;">
                    <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
                        <circle cx="11" cy="11" r="7"></circle>
                        <path d="M20 20L16.65 16.65"></path>
                    </svg>
                </button>
                <button type="button" class="taBtn taBtnOutline taSearchReset" onclick="location.href='${pageContext.request.contextPath}/equipment/list'">초기화</button>
            </div>
        </div>
    </div>
</form>

<form id="deleteForm" method="post" action="${pageContext.request.contextPath}/equipment/delete">
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
                        <th class="taTableHeadCell taColFit">설비코드</th>
                        <th class="taTableHeadCell taColGrow">설비명</th>
                        <th class="taTableHeadCell taColFit">위치</th>
                        <th class="taTableHeadCell taColFit">금액(만원)</th>
                        <th class="taTableHeadCell taColAction taLastCol">상세</th>
                    </tr>
                </thead>
                <tbody>
                    <c:choose>
                        <c:when test="${not empty list}">
                            <c:forEach var="e" items="${list}">
                                <tr class="taTableBodyRow">
                                    <td class="taTableBodyCell taCheckCell">
                                        <c:if test="${isMesAdmin}">
                                            <input type="checkbox" name="equipmentId" value="${e.equipmentId}" class="taCheckInput">
                                        </c:if>
                                    </td>
                                    <td class="taTableBodyCell taColFit">${e.equipmentCode}</td>
                                    <td class="taTableBodyCell taColGrow">${e.equipmentName}</td>
                                    <td class="taTableBodyCell taColFit">${e.location}</td>
                                    <td class="taTableBodyCell taColFit"><fmt:formatNumber value="${e.equipmentPrice / 10000}" pattern="#,##0.##"/></td>
                                    <td class="taTableBodyCell taColAction taLastCol">
                                        <button type="button" class="taLinkButton" onclick="location.href='${pageContext.request.contextPath}/equipment/detail?equipmentId=${e.equipmentId}'">상세보기</button>
                                    </td>
                                </tr>
                            </c:forEach>
                        </c:when>
                        <c:otherwise>
                            <tr class="taTableBodyRow">
                                <td class="taTableBodyCell taLastCol" colspan="6">조회된 설비가 없습니다.</td>
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
        <div class="taModalDialog">
            <div class="taModalHeader">
                <h3 class="taModalTitle">설비 등록</h3>
                <button type="button" class="taModalClose">&times;</button>
            </div>
            <form method="post" action="${pageContext.request.contextPath}/equipment/register">
                <div class="taModalBody taModalGrid">
                    <div class="form-row">
                        <label>설비코드</label>
                        <input type="text" value="자동생성" readonly>
                        <small class="taAutoCodeHint">등록 시 자동 생성됩니다.</small>
                    </div>
                    <div class="form-row">
                        <label>설비명</label>
                        <input type="text" name="equipmentName" required>
                    </div>
                    <div class="form-row">
                        <label>모델명</label>
                        <input type="text" name="modelName">
                    </div>
                    <div class="form-row">
                        <label>위치</label>
                        <input type="text" name="location">
                    </div>
                    <div class="form-row">
                        <label>제조사</label>
                        <input type="text" name="manufacturer">
                    </div>
                    <div class="form-row">
                        <label>공급처</label>
                        <input type="text" name="vendorName">
                    </div>
                    <div class="form-row">
                        <label>금액</label>
                        <input type="number" step="0.01" name="equipmentPrice">
                    </div>
                    <div class="form-row">
                        <label>구매일</label>
                        <input type="date" name="purchaseDate">
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
            document.querySelectorAll("input[name='equipmentId']").forEach(function(cb) {
                cb.checked = checkAll.checked;
            });
        });
    }
})();
</script>
