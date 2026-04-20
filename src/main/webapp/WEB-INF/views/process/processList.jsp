<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<c:set var="isMesAdmin" value="${sessionScope.loginUser.roleName eq 'MES_ADMIN'}" />

<div class="taPageActions">
    <c:if test="${isMesAdmin}">
        <button type="button" class="taBtn taBtnPrimary" data-modal-target="registerModal">등록</button>
        <button type="submit" form="deleteForm" class="taBtn taBtnOutline" onclick="return confirm('선택한 공정을 삭제하시겠습니까?');">선택 삭제</button>
    </c:if>
</div>

<form id="paSearchForm" method="get" action="${pageContext.request.contextPath}/process/list">
    <input type="hidden" name="page" id="paPage" value="${paCurrentPage}">

    <div class="taToolbarRow">
        <div class="taToolbarField taToolbarSpan3">
            <select class="taSelect taAutoSelectColor ${empty searchType ? 'taSelectPlaceholder' : ''}" name="searchType">
                <option value="" ${empty searchType ? 'selected' : ''}>전체 / 공정코드 ...</option>
                <option value="processCode" ${searchType eq 'processCode' ? 'selected' : ''}>공정코드</option>
                <option value="processName" ${searchType eq 'processName' ? 'selected' : ''}>공정명</option>
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
                <button type="button" class="taBtn taBtnOutline taSearchReset" onclick="location.href='${pageContext.request.contextPath}/process/list'">초기화</button>
            </div>
        </div>
    </div>
</form>

<form id="deleteForm" method="post" action="${pageContext.request.contextPath}/process/delete">
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
                        <th class="taTableHeadCell taColFit">공정코드</th>
                        <th class="taTableHeadCell taColGrow">공정명</th>
                        <th class="taTableHeadCell taColGrow">설명</th>
                        <th class="taTableHeadCell taColAction taLastCol">상세</th>
                    </tr>
                </thead>
                <tbody>
                    <c:choose>
                        <c:when test="${not empty list}">
                            <c:forEach var="p" items="${list}">
                                <tr class="taTableBodyRow">
                                    <td class="taTableBodyCell taCheckCell">
                                        <c:if test="${isMesAdmin}">
                                            <input type="checkbox" name="processId" value="${p.processId}" class="taCheckInput">
                                        </c:if>
                                    </td>
                                    <td class="taTableBodyCell taColFit">${p.processCode}</td>
                                    <td class="taTableBodyCell taColGrow">${p.processName}</td>
                                    <td class="taTableBodyCell taColGrow">${p.description}</td>
                                    <td class="taTableBodyCell taColAction taLastCol">
                                        <button type="button" class="taLinkButton" onclick="location.href='${pageContext.request.contextPath}/process/detail?processId=${p.processId}'">상세보기</button>
                                    </td>
                                </tr>
                            </c:forEach>
                        </c:when>
                        <c:otherwise>
                            <tr class="taTableBodyRow">
                                <td class="taTableBodyCell taLastCol" colspan="5">조회된 공정이 없습니다.</td>
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
                <h3 class="taModalTitle">공정 등록</h3>
                <button type="button" class="taModalClose">&times;</button>
            </div>
            <form method="post" action="${pageContext.request.contextPath}/process/register">
                <div class="taModalBody taModalGrid">
                    <div class="form-row">
                        <label>공정코드</label>
                        <input type="text" value="자동생성" readonly>
                        <small class="taAutoCodeHint">등록 시 자동 생성됩니다.</small>
                    </div>
                    <div class="form-row">
                        <label>공정명</label>
                        <input type="text" name="processName" required>
                    </div>
                    <div class="form-row full">
                        <label>설명</label>
                        <input type="text" name="description">
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
            document.querySelectorAll("input[name='processId']").forEach(function(cb) {
                cb.checked = checkAll.checked;
            });
        });
    }
})();
</script>
