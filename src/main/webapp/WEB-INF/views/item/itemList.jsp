<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<div class="taPageActions">
    <button type="button" class="taBtn taBtnPrimary" onclick="openRegisterModal()">등록</button>
    <button type="submit" form="deleteForm" class="taBtn taBtnOutline"
        onclick="return confirm('선택한 품목을 삭제하시겠습니까?');">선택 삭제</button>
</div>

<form id="paSearchForm" method="get" action="${pageContext.request.contextPath}/item/list">
    <input type="hidden" name="page" id="paPage" value="${paCurrentPage}">
    <div class="taToolbarRow">
        <div class="taToolbarField">
            <select class="taSelect" name="searchType">
                <option value="all" ${empty searchType or searchType eq 'all' ? 'selected' : ''}>전체</option>
                <option value="itemCode" ${searchType eq 'itemCode' ? 'selected' : ''}>품목코드</option>
                <option value="itemName" ${searchType eq 'itemName' ? 'selected' : ''}>품목명</option>
                <option value="itemType" ${searchType eq 'itemType' ? 'selected' : ''}>품목유형</option>
                <option value="supplierName" ${searchType eq 'supplierName' ? 'selected' : ''}>공급처</option>
            </select>
        </div>

        <div class="taToolbarField taToolbarFieldGrow" style="grid-column: span 2;">
            <div class="taSearchBox">
                <input type="text"
                       class="taSearchInput"
                       name="keyword"
                       value="${keyword}"
                       placeholder="검색어를 입력하세요">
                <button type="submit" class="taSearchBtn">⌕</button>
                <button type="button"
                        class="taBtn taBtnOutline taSearchReset"
                        onclick="location.href='${pageContext.request.contextPath}/item/list'">
                    초기화
                </button>
            </div>
        </div>
    </div>
</form>

<form id="deleteForm"
<<<<<<< HEAD
	action="${pageContext.request.contextPath}/item/delete" method="post">
	<div class="taTableShell" id="paTableBox">
		<div class="taTableScroll">
			<table class="taMesTable">
				<thead>
					<tr>
						<th class="taTableHeadCell taCheckCell"><input
							type="checkbox" id="checkAll" class="taCheckInput"></th>
						<th class="taTableHeadCell taColFit">품목번호</th>
						<th class="taTableHeadCell taColFit">품목코드</th>
						<th class="taTableHeadCell taColGrow">품목명</th>
<!-- 						<th class="taTableHeadCell taColFit">품목유형</th> -->
<!-- 						<th class="taTableHeadCell taColFit">단위</th> -->
						<th class="taTableHeadCell taColGrow">규격</th>
						<th class="taTableHeadCell taColGrow">공급처</th>
						<th class="taTableHeadCell taColFit">안전재고</th>
						<th class="taTableHeadCell taColAction taLastCol">상세</th>
					</tr>
				</thead>
				<tbody>
					<c:choose>
						<c:when test="${not empty itemList}">
							<c:forEach var="item" items="${itemList}">
								<tr class="taTableBodyRow">
									<td class="taTableBodyCell taCheckCell"><input
										type="checkbox" name="itemId" value="${item.itemId}"
										class="taCheckInput"></td>
									<td class="taTableBodyCell taColFit">${item.itemId}</td>
									<td class="taTableBodyCell taColFit">${item.itemCode}</td>
									<td class="taTableBodyCell taColGrow">${item.itemName}</td>
<%-- 									<td class="taTableBodyCell taColFit">${item.itemType}</td> --%>
<%-- 									<td class="taTableBodyCell taColFit">${item.unit}</td> --%>
									<td class="taTableBodyCell taColGrow">${item.spec}</td>
									<td class="taTableBodyCell taColGrow">${item.supplierName}</td>
									<td class="taTableBodyCell taColFit">${item.safetyStock}</td>
									<td class="taTableBodyCell taColAction taLastCol"><a
										class="taLinkAnchor"
										href="${pageContext.request.contextPath}/item/detail?itemId=${item.itemId}">
											상세보기 </a></td>
								</tr>
							</c:forEach>
						</c:when>
						<c:otherwise>
							<tr class="taTableBodyRow">
								<td class="taTableBodyCell taLastCol" colspan="10"
									style="text-align: center;">조회된 품목이 없습니다.</td>
							</tr>
						</c:otherwise>
					</c:choose>
				</tbody>
			</table>
		</div>
	</div>
=======
    action="${pageContext.request.contextPath}/item/delete"
    method="post">
    <div class="taTableShell" id="paTableBox">
        <div class="taTableScroll">
            <table class="taMesTable">
                <thead>
                    <tr>
                        <th class="taTableHeadCell taCheckCell"><input type="checkbox" id="checkAll" class="taCheckInput"></th>
                        <th class="taTableHeadCell taColFit">품목번호</th>
                        <th class="taTableHeadCell taColFit">품목코드</th>
                        <th class="taTableHeadCell taColGrow">품목명</th>
                        <th class="taTableHeadCell taColFit">품목유형</th>
                        <th class="taTableHeadCell taColFit">단위</th>
                        <th class="taTableHeadCell taColGrow">규격</th>
                        <th class="taTableHeadCell taColGrow">공급처</th>
                        <th class="taTableHeadCell taColFit">안전재고</th>
                        <th class="taTableHeadCell taColAction taLastCol">상세</th>
                    </tr>
                </thead>
                <tbody>
                    <c:choose>
                        <c:when test="${not empty itemList}">
                            <c:forEach var="item" items="${itemList}">
                                <tr class="taTableBodyRow">
                                    <td class="taTableBodyCell taCheckCell"><input type="checkbox" name="itemId" value="${item.itemId}" class="taCheckInput"></td>
                                    <td class="taTableBodyCell taColFit">${item.itemId}</td>
                                    <td class="taTableBodyCell taColFit">${item.itemCode}</td>
                                    <td class="taTableBodyCell taColGrow">${item.itemName}</td>
                                    <td class="taTableBodyCell taColFit">${item.itemType}</td>
                                    <td class="taTableBodyCell taColFit">${item.unit}</td>
                                    <td class="taTableBodyCell taColGrow">${item.spec}</td>
                                    <td class="taTableBodyCell taColGrow">${item.supplierName}</td>
                                    <td class="taTableBodyCell taColFit">${item.safetyStock}</td>
                                    <td class="taTableBodyCell taColAction taLastCol">
                                        <a class="taLinkAnchor"
                                           href="${pageContext.request.contextPath}/item/detail?itemId=${item.itemId}">
                                           상세보기
                                        </a>
                                    </td>
                                </tr>
                            </c:forEach>
                        </c:when>
                        <c:otherwise>
                            <tr class="taTableBodyRow">
                                <td class="taTableBodyCell taLastCol" colspan="10" style="text-align:center;">조회된 품목이 없습니다.</td>
                            </tr>
                        </c:otherwise>
                    </c:choose>
                </tbody>
            </table>
        </div>
    </div>
>>>>>>> d991ce902344e59b82b7bca4dae3615878c6f389
</form>

<div class="taModal" id="registerModal" hidden aria-hidden="true">
    <div class="taModalDialog">
        <div class="taModalHeader">
            <h3 class="taModalTitle">품목 등록</h3>
            <button type="button" class="taModalClose" onclick="closeRegisterModal()">&times;</button>
        </div>
        <form action="${pageContext.request.contextPath}/item/register" method="post">
            <div class="taModalBody taModalGrid">
                <div class="form-row"><label>품목코드</label><input type="text" name="itemCode" required></div>
                <div class="form-row"><label>품목명</label><input type="text" name="itemName" required></div>
                <div class="form-row"><label>품목유형</label><input type="text" name="itemType"></div>
                <div class="form-row"><label>단위</label><input type="text" name="unit"></div>
                <div class="form-row"><label>규격</label><input type="text" name="spec"></div>
                <div class="form-row"><label>공급처</label><input type="text" name="supplierName"></div>
                <div class="form-row"><label>안전재고</label><input type="number" step="0.001" name="safetyStock"></div>
                <div class="form-row full"><label>비고</label><textarea name="remark"></textarea></div>
            </div>
            <div class="taModalFooter">
                <button type="button" class="taBtn taBtnOutline" onclick="closeRegisterModal()">취소</button>
                <button type="submit" class="taBtn taBtnPrimary">등록</button>
            </div>
        </form>
    </div>
</div>

<script>
document.getElementById("checkAll").addEventListener("change", function() {
    const checks = document.querySelectorAll("input[name='itemId']");
    for (let i = 0; i < checks.length; i++) {
        checks[i].checked = this.checked;
    }
});

function openRegisterModal() {
    const modal = document.getElementById("registerModal");
    modal.hidden = false;
    modal.classList.add("open");
    document.body.classList.add("modal-open");
}

function closeRegisterModal() {
    const modal = document.getElementById("registerModal");
    modal.classList.remove("open");
    modal.hidden = true;
    document.body.classList.remove("modal-open");
}

window.addEventListener("click", function(e) {
    const modal = document.getElementById("registerModal");
    if (e.target === modal) {
        closeRegisterModal();
    }
});
</script>
