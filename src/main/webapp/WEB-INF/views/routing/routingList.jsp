<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<c:set var="isMesAdmin" value="${sessionScope.loginUser.roleName eq 'MES_ADMIN'}" />

<div class="taPageActions">
    <c:if test="${isMesAdmin}">
        <c:choose>
            <c:when test="${empty selectedItemId}">
                <button type="button" class="taBtn taBtnPrimary" onclick="alert('먼저 완제품을 선택하세요.');">등록</button>
            </c:when>
            <c:otherwise>
                <button type="button" class="taBtn taBtnPrimary" data-modal-target="registerModal">등록</button>
            </c:otherwise>
        </c:choose>
        <button type="submit" form="deleteForm" class="taBtn taBtnOutline" onclick="return confirm('선택한 라우팅을 삭제하시겠습니까?');">선택 삭제</button>
    </c:if>
</div>

<form id="paSearchForm" method="get" action="${pageContext.request.contextPath}/routing/list">
    <div class="taToolbarRow">
        <div class="taToolbarField taToolbarSpan12">
            <label style="display:block; margin-bottom:8px; font-weight:600;">품목 선택</label>
            <div class="taSearchBox">
                <select class="taSelect taAutoSelectColor ${empty selectedItemId ? 'taSelectPlaceholder' : ''}" name="itemId" onchange="this.form.submit()">
                    <option value="">완제품을 선택하세요</option>
                    <c:forEach var="item" items="${itemList}">
                        <option value="${item.itemId}" ${selectedItemId == item.itemId ? 'selected' : ''}>${item.itemCode} - ${item.itemName}</option>
                    </c:forEach>
                </select>
                <button type="button" class="taBtn taBtnOutline taSearchReset" onclick="location.href='${pageContext.request.contextPath}/routing/list'">초기화</button>
            </div>
        </div>
    </div>
</form>

<form id="deleteForm" method="post" action="${pageContext.request.contextPath}/routing/delete">
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
                        <th class="taTableHeadCell taColFit">공정순서</th>
                        <th class="taTableHeadCell taColGrow">공정명</th>
                        <th class="taTableHeadCell taColGrow">설비명</th>
                        <th class="taTableHeadCell taColAction taLastCol">상세</th>
                    </tr>
                </thead>
                <tbody>
                    <c:choose>
                        <c:when test="${not empty list}">
                            <c:forEach var="r" items="${list}">
                                <tr class="taTableBodyRow">
                                    <td class="taTableBodyCell taCheckCell">
                                        <c:if test="${isMesAdmin}">
                                            <input type="checkbox" name="routingId" value="${r.routingId}" class="taCheckInput">
                                        </c:if>
                                    </td>
                                    <td class="taTableBodyCell taColFit">${r.processSeq}</td>
                                    <td class="taTableBodyCell taColGrow">${r.processName}</td>
                                    <td class="taTableBodyCell taColGrow">${r.equipmentName}</td>
                                    <td class="taTableBodyCell taColAction taLastCol">
                                        <button type="button" class="taLinkButton" onclick="location.href='${pageContext.request.contextPath}/routing/detail?routingId=${r.routingId}'">상세보기</button>
                                    </td>
                                </tr>
                            </c:forEach>
                        </c:when>
                        <c:otherwise>
                            <tr class="taTableBodyRow">
                                <td class="taTableBodyCell taLastCol" colspan="5">
                                    <c:choose>
                                        <c:when test="${empty selectedItemId}">완제품을 먼저 선택하세요.</c:when>
                                        <c:otherwise>등록된 라우팅이 없습니다.</c:otherwise>
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
        <div class="taModalDialog">
            <div class="taModalHeader">
                <h3 class="taModalTitle">라우팅 등록</h3>
                <button type="button" class="taModalClose">&times;</button>
            </div>
            <form method="post" action="${pageContext.request.contextPath}/routing/register">
                <input type="hidden" name="itemId" value="${selectedItemId}">
                <div class="taModalBody taModalGrid">
                    <div class="form-row">
                        <label>공정</label>
                        <select name="processId" required>
                            <option value="">공정 선택</option>
                            <c:forEach var="p" items="${processList}">
                                <option value="${p.processId}">${p.processName}</option>
                            </c:forEach>
                        </select>
                    </div>
                    <div class="form-row">
                        <label>설비</label>
                        <select name="equipmentId" required>
                            <option value="">설비 선택</option>
                            <c:forEach var="e" items="${equipmentList}">
                                <option value="${e.equipmentId}">${e.equipmentName}</option>
                            </c:forEach>
                        </select>
                    </div>
                    <div class="form-row">
                        <label>순서</label>
                        <input type="number" name="processSeq" min="1" required>
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
            document.querySelectorAll("input[name='routingId']").forEach(function(cb) {
                cb.checked = checkAll.checked;
            });
        });
    }
})();
</script>
