<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<c:set var="isMesAdmin" value="${sessionScope.loginUser.roleName eq 'MES_ADMIN'}" />

<div class="taPageActions">
    <c:if test="${isMesAdmin}">
        <button type="button" class="taBtn taBtnPrimary" data-modal-target="registerModal">등록</button>
        <button type="submit" form="deleteForm" class="taBtn taBtnOutline" onclick="return confirm('선택한 불량코드를 삭제하시겠습니까?');">선택 삭제</button>
    </c:if>
</div>

<form id="paSearchForm" method="get" action="${pageContext.request.contextPath}/defect-mgmt">
    <input type="hidden" name="page" id="paPage" value="${paCurrentPage}">

    <div class="taToolbarRow">
        <div class="taToolbarField taToolbarSpan3">
            <select class="taSelect taAutoSelectColor ${empty searchType ? 'taSelectPlaceholder' : ''}" name="searchType">
                <option value="" ${empty searchType ? 'selected' : ''}>전체 / 불량코드 ...</option>
                <option value="defect_code" ${searchType eq 'defect_code' ? 'selected' : ''}>불량코드</option>
                <option value="defect_name" ${searchType eq 'defect_name' ? 'selected' : ''}>불량명</option>
                <option value="defect_type" ${searchType eq 'defect_type' ? 'selected' : ''}>유형</option>
                <option value="description" ${searchType eq 'description' ? 'selected' : ''}>설명</option>
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
                <button type="button" class="taBtn taBtnOutline taSearchReset" onclick="location.href='${pageContext.request.contextPath}/defect-mgmt'">초기화</button>
            </div>
        </div>
    </div>
</form>

<form id="deleteForm" method="post" action="${pageContext.request.contextPath}/defect-mgmt-del">
    <input type="hidden" name="searchType" value="${searchType}">
    <input type="hidden" name="keyword" value="${keyword}">
    <input type="hidden" name="page" value="${paCurrentPage}">

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
                        <th class="taTableHeadCell taColFit">불량코드</th>
                        <th class="taTableHeadCell taColGrow">불량명</th>
                        <th class="taTableHeadCell taColFit">유형</th>
                        <th class="taTableHeadCell taColGrow">설명</th>
                        <th class="taTableHeadCell taColAction taLastCol">상세</th>
                    </tr>
                </thead>
                <tbody>
                    <c:choose>
                        <c:when test="${not empty list}">
                            <c:forEach var="d" items="${list}">
                                <tr class="taTableBodyRow">
                                    <td class="taTableBodyCell taCheckCell">
                                        <c:if test="${isMesAdmin}">
                                            <input type="checkbox" name="defect_code_id" value="${d.defect_code_id}" class="taCheckInput">
                                        </c:if>
                                    </td>
                                    <td class="taTableBodyCell taColFit">${d.defect_code}</td>
                                    <td class="taTableBodyCell taColGrow">${d.defect_name}</td>
                                    <td class="taTableBodyCell taColFit">${d.defect_type}</td>
                                    <td class="taTableBodyCell taColGrow">${d.description}</td>
                                    <td class="taTableBodyCell taColAction taLastCol">
                                        <button type="button" class="taLinkButton" onclick="location.href='${pageContext.request.contextPath}/defect-detail?defectCodeId=${d.defect_code_id}'">상세보기</button>
                                    </td>
                                </tr>
                            </c:forEach>
                        </c:when>
                        <c:otherwise>
                            <tr class="taTableBodyRow">
                                <td class="taTableBodyCell taLastCol" colspan="6">조회된 불량코드가 없습니다.</td>
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
                <h3 class="taModalTitle">불량 등록</h3>
                <button type="button" class="taModalClose">&times;</button>
            </div>
            <form method="post" action="${pageContext.request.contextPath}/defect-mgmt">
                <input type="hidden" name="searchType" value="${searchType}">
                <input type="hidden" name="keyword" value="${keyword}">
                <input type="hidden" name="page" value="${paCurrentPage}">
                <div class="taModalBody taModalGrid">
                    <div class="form-row">
                        <label>불량코드</label>
                        <input type="text" value="자동생성" readonly>
                        <small class="taAutoCodeHint">등록 시 자동 생성됩니다.</small>
                    </div>
                    <div class="form-row">
                        <label>불량명</label>
                        <input type="text" name="defect_name" required>
                    </div>
                    <div class="form-row">
                        <label>유형</label>
                        <input type="text" name="defect_type">
                    </div>
                    <div class="form-row">
                        <label>설명</label>
                        <input type="text" name="description">
                    </div>
                    <div class="form-row">
                        <label>비고</label>
                        <input type="text" name="remark">
                    </div>
                    <input type="hidden" name="use_yn" value="Y">
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
            document.querySelectorAll("input[name='defect_code_id']").forEach(function(cb) {
                cb.checked = checkAll.checked;
            });
        });
    }
})();
</script>
