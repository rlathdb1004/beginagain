<%@ page language="java" contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<div class="taPageActions">
    <button type="button" class="taBtn taBtnPrimary" data-modal-target="registerModal">등록</button>
    <button class="taBtn taBtnOutline" onclick="deleteSelected()">선택 삭제</button>
</div>

<div class="taSearchShell">
    <form id="paSearchForm" method="get" action="${pageContext.request.contextPath}/defect-mgmt" class="taLocalSearchForm">
        <input type="hidden" name="page" id="paPage" value="${paCurrentPage}">
        <div class="taToolbarRow">
            <div class="taToolbarField taToolbarSpan3">
                <select class="taSelect taAutoSelectColor ${empty searchType or searchType eq 'all' ? 'taSelectPlaceholder' : ''}" name="searchType">
                    <option value="" disabled hidden <c:if test="${empty searchType or searchType eq 'all'}">selected</c:if>>전체 / 불량코드 ...</option>
                    <option value="all" <c:if test="${searchType eq 'all'}">selected</c:if>>전체</option>
                    <option value="defectCode" <c:if test="${searchType eq 'defectCode'}">selected</c:if>>불량코드</option>
                    <option value="defectName" <c:if test="${searchType eq 'defectName'}">selected</c:if>>불량명</option>
                    <option value="defectType" <c:if test="${searchType eq 'defectType'}">selected</c:if>>유형</option>
                </select>
            </div>
            <div class="taToolbarField taToolbarFieldGrow taToolbarSpan9">
                <div class="taSearchBox">
                    <input type="text" class="taSearchInput" name="keyword" placeholder="검색어를 입력하세요" value="${keyword}">
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
</div>

<c:if test="${not empty errorMessage}"><script>alert('${errorMessage}');</script></c:if>

<form id="deleteForm" method="post" action="${pageContext.request.contextPath}/defect-mgmt-del">
    <input type="hidden" name="searchType" value="${searchType}">
    <input type="hidden" name="keyword" value="${keyword}">
    <input type="hidden" name="page" value="${paCurrentPage}">

    <div class="taTableShell" id="paTableBox">
        <div class="taTableScroll">
            <table class="taMesTable">
                <thead>
                    <tr>
                        <th class="taTableHeadCell taCheckCell"><input type="checkbox" id="checkAll" class="taCheckInput"></th>
                        <th class="taTableHeadCell taColFit">ID</th>
                        <th class="taTableHeadCell taColFit">불량코드</th>
                        <th class="taTableHeadCell taColGrow">불량명</th>
                        <th class="taTableHeadCell taColFit">유형</th>
                        <th class="taTableHeadCell taColAction taLastCol">상세</th>
                    </tr>
                </thead>
                <tbody>
                    <c:choose>
                        <c:when test="${not empty defectList}">
                            <c:forEach var="d" items="${defectList}">
                                <tr class="taTableBodyRow">
                                    <td class="taTableBodyCell taCheckCell"><input type="checkbox" class="row-check taCheckInput" name="defect_code_id" value="${d.defect_code_id}"></td>
                                    <td class="taTableBodyCell taColFit">${d.defect_code_id}</td>
                                    <td class="taTableBodyCell taColFit">${d.defect_code}</td>
                                    <td class="taTableBodyCell taColGrow">${d.defect_name}</td>
                                    <td class="taTableBodyCell taColFit">${d.defect_type}</td>
                                    <td class="taTableBodyCell taColAction taLastCol"><a class="taLinkAnchor" href="${pageContext.request.contextPath}/defect-detail?defectCodeId=${d.defect_code_id}">상세보기</a></td>
                                </tr>
                            </c:forEach>
                        </c:when>
                        <c:otherwise>
                            <tr class="taTableBodyRow"><td class="taTableBodyCell taLastCol" colspan="6" style="text-align:center;">조회된 불량코드가 없습니다.</td></tr>
                        </c:otherwise>
                    </c:choose>
                </tbody>
            </table>
        </div>
    </div>
</form>

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
            <input type="hidden" name="defect_code_id" id="defect_code_id">
            <div class="taModalBody taModalGrid">
                <div class="form-row"><label>불량코드</label><input type="text" name="defect_code" id="defect_code" required></div>
                <div class="form-row"><label>불량명</label><input type="text" name="defect_name" id="defect_name" required></div>
                <div class="form-row"><label>유형</label><select name="defect_type" id="defect_type" required><option value="">유형 선택</option><option value="완제품">완제품</option><option value="자재">자재</option><option value="공통">공통</option></select></div>
                <div class="form-row full"><label>상세 설명</label><textarea name="description" id="description"></textarea></div>
                <div class="form-row full"><label>비고</label><textarea name="remark" id="remark"></textarea></div>
            </div>
            <div class="taModalFooter">
                <button type="button" class="taBtn taBtnOutline taModalClose">취소</button>
                <button type="submit" class="taBtn taBtnPrimary">등록</button>
            </div>
        </form>
    </div>
</div>

<script>
function deleteSelected() {
    const checked = document.querySelectorAll("input[name='defect_code_id']:checked");
    if (checked.length === 0) { alert("삭제할 항목을 선택하세요."); return; }
    if (!confirm("선택한 항목을 삭제하시겠습니까?")) return;
    document.getElementById("deleteForm").submit();
}

document.addEventListener("DOMContentLoaded", function () {
    const checkAll = document.getElementById("checkAll");
    const rowChecks = document.querySelectorAll(".row-check");
    if (checkAll) {
        checkAll.addEventListener("change", function () { rowChecks.forEach(cb => { cb.checked = checkAll.checked; }); });
    }
    rowChecks.forEach(cb => { cb.addEventListener("change", function () { if (checkAll) { checkAll.checked = Array.from(rowChecks).length > 0 && Array.from(rowChecks).every(c => c.checked); } }); });
});
</script>
