<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<c:if test="${not empty errorMsg}">
    <script>alert("${errorMsg}");</script>
</c:if>

<div id="ioRegisterModal" class="taModal" hidden aria-hidden="true">
    <div class="taModalDialog">
        <div class="taModalHeader">
            <h3 class="taModalTitle">입출고 등록</h3>
            <button type="button" class="taModalClose">×</button>
        </div>
        <form method="post" action="${pageContext.request.contextPath}/ioRegInq">
            <input type="hidden" name="cmd" value="register">
            <div class="taModalBody taModalGrid">
                <div class="form-row">
                    <label>품목 선택</label>
                    <select name="itemId" id="ioItemId" class="taSelect" required>
                        <option value="">품목을 선택하세요</option>
                        <c:forEach var="item" items="${itemList}">
                            <option value="${item.itemId}"
                                data-code="${item.itemCode}"
                                data-name="${item.itemName}"
                                data-type="${item.itemType}"
                                data-unit="${item.unit}"
                                data-stock="${item.safetyStock}">
                                ${item.itemCode} - ${item.itemName}
                            </option>
                        </c:forEach>
                    </select>
                </div>
                <div class="form-row">
                    <label>품목코드</label>
                    <input type="text" id="ioItemCode" readonly>
                </div>
                <div class="form-row">
                    <label>품목명</label>
                    <input type="text" id="ioItemName" readonly>
                </div>
                <div class="form-row">
                    <label>품목 유형</label>
                    <input type="text" id="ioItemType" readonly>
                </div>
                <div class="form-row">
                    <label>입출고구분</label>
                    <select name="inoutType" class="taSelect">
                        <option value="입고">입고</option>
                        <option value="출고">출고</option>
                        <option value="반품">반품</option>
                        <option value="폐기">폐기</option>
                    </select>
                </div>
                <div class="form-row">
                    <label>수량</label>
                    <input type="number" step="0.001" min="0.001" name="qty" required>
                </div>
                <div class="form-row">
                    <label>단위</label>
                    <input type="text" name="unit" id="ioUnit" readonly>
                </div>
                <div class="form-row">
                    <label>현재고</label>
                    <input type="text" id="ioCurrentStock" readonly>
                </div>
                <div class="form-row">
                    <label>일자</label>
                    <input type="date" name="inoutDate" required>
                </div>
                <div class="form-row">
                    <label>상태</label>
                    <select name="status" class="taSelect">
                        <option value="대기">대기</option>
                        <option value="완료">완료</option>
                    </select>
                </div>
                <div class="form-row full">
                    <label>비고</label>
                    <textarea name="remark"></textarea>
                </div>
            </div>
            <div class="taModalFooter">
                <button type="button" class="taBtn taBtnOutline taModalClose">취소</button>
                <button type="submit" class="taBtn taBtnPrimary">저장</button>
            </div>
        </form>
    </div>
</div>

<div class="taPageActions">
    <button type="button" class="taOpenModal taBtn taBtnPrimary" data-modal-target="ioRegisterModal">등록</button>
    <button type="submit" form="ioDeleteForm" class="taBtn taBtnOutline">선택 삭제</button>
</div>

<form id="paSearchForm" method="post" action="${pageContext.request.contextPath}/ioRegInq">
    <input type="hidden" name="cmd" value="list">
    <input type="hidden" name="page" id="paPage" value="${paCurrentPage}">
    <div class="taToolbarRow">
        <div class="taToolbarField taToolbarSpan2">
            <select class="taSelect taAutoSelectColor ${empty searchType or searchType eq 'all' ? 'taSelectPlaceholder' : ''}"
                name="searchType" id="workStatusSearchType">
                <option value="" hidden
                    <c:if test="${empty ioRegInqSearchDTO.searchType}">selected</c:if>>
                    전체 / 품목코드 / 품목명</option>
                <option value="all">전체</option>
                <option value="itemCode" <c:if test="${ioRegInqSearchDTO.searchType eq 'itemCode'}">selected</c:if>>품목코드</option>
                <option value="itemName" <c:if test="${ioRegInqSearchDTO.searchType eq 'itemName'}">selected</c:if>>품목명</option>
            </select>
        </div>
        <div class="taToolbarField taToolbarSpan2">
            <input type="date" class="taSearchInput" name="startDate" value="${ioRegInqSearchDTO.startDate}">
        </div>
        <div class="taToolbarField taToolbarSpan2">
            <input type="date" class="taSearchInput" name="endDate" value="${ioRegInqSearchDTO.endDate}">
        </div>
        <div class="taToolbarField taToolbarFieldGrow taToolbarSpan6">
            <div class="taSearchBox">
                <input type="text" class="taSearchInput" name="keyword" placeholder="검색키워드" value="${ioRegInqSearchDTO.keyword}">
                <button type="submit" class="taSearchBtn" aria-label="검색" onclick="document.getElementById('paPage').value=1;">
                    <svg viewBox="0 0 24 24" fill="none" stroke-width="2">
                        <circle cx="11" cy="11" r="7"></circle>
                        <path d="M20 20L16.65 16.65"></path>
                    </svg>
                </button>
                <button type="button" class="taBtn taBtnOutline taSearchReset"
                    onclick="location.href='${pageContext.request.contextPath}/ioRegInq'">초기화</button>
            </div>
        </div>
    </div>
</form>

<form id="ioDeleteForm" method="post" action="${pageContext.request.contextPath}/ioRegInq">
    <input type="hidden" name="cmd" value="delete">
    <div class="taTableShell" id="paTableBox">
        <div class="taTableScroll">
            <table class="taMesTable">
                <thead>
                    <tr>
                        <th class="taTableHeadCell taColFit"><input type="checkbox" id="checkAll" class="taCheckInput"></th>
                        <th class="taTableHeadCell taColFit">NO</th>
                        <th class="taTableHeadCell taColFit">입출고구분</th>
                        <th class="taTableHeadCell taColFit">품목코드</th>
                        <th class="taTableHeadCell taColFit">품목 유형</th>
                        <th class="taTableHeadCell taColGrow">품목명</th>
                        <th class="taTableHeadCell taColFit">입출고량</th>
                        <th class="taTableHeadCell taColFit">단위</th>
                        <th class="taTableHeadCell taColFit">상태</th>
                        <th class="taTableHeadCell taColDate">일자</th>
                        <th class="taTableHeadCell taColAction taLastCol">상세보기</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach var="dto" items="${ioRegInqList}">
                        <tr class="taTableBodyRow">
                            <td class="taTableBodyCell taColFit"><input type="checkbox" class="taCheckInput" name="inoutIds" value="${dto.inoutId}"></td>
                            <td class="taTableBodyCell taColFit">${dto.inoutId}</td>
                            <td class="taTableBodyCell taColFit">${dto.inoutType}</td>
                            <td class="taTableBodyCell taColFit">${dto.itemCode}</td>
                            <td class="taTableBodyCell taColFit">${dto.itemType}</td>
                            <td class="taTableBodyCell taColGrow">${dto.itemName}</td>
                            <td class="taTableBodyCell taColFit">${dto.qty}</td>
                            <td class="taTableBodyCell taColFit">${dto.unit}</td>
                            <td class="taTableBodyCell taColFit">${dto.status}</td>
                            <td class="taTableBodyCell taColDate">${dto.inoutDate}</td>
                            <td class="taTableBodyCell taColAction taLastCol">
                                <a class="taLinkAnchor" href="${pageContext.request.contextPath}/ioRegInq?inoutId=${dto.inoutId}">상세보기</a>
                            </td>
                        </tr>
                    </c:forEach>
                    <c:if test="${empty ioRegInqList}">
                        <tr class="taTableBodyRow">
                            <td class="taTableBodyCell taLastCol" colspan="11" style="text-align: center;">조회된 데이터가 없습니다.</td>
                        </tr>
                    </c:if>
                </tbody>
            </table>
        </div>
    </div>
</form>

<script>
(function() {
    const itemSelect = document.getElementById("ioItemId");
    const itemCode = document.getElementById("ioItemCode");
    const itemName = document.getElementById("ioItemName");
    const itemType = document.getElementById("ioItemType");
    const unit = document.getElementById("ioUnit");
    const currentStock = document.getElementById("ioCurrentStock");

    if (!itemSelect) return;

    itemSelect.addEventListener("change", function() {
        const selected = this.options[this.selectedIndex];
        if (!selected || !selected.value) {
            itemCode.value = "";
            itemName.value = "";
            itemType.value = "";
            unit.value = "";
            currentStock.value = "";
            return;
        }
        itemCode.value = selected.dataset.code || "";
        itemName.value = selected.dataset.name || "";
        itemType.value = selected.dataset.type || "";
        unit.value = selected.dataset.unit || "";
        currentStock.value = selected.dataset.stock || "0";
    });
})();
</script>
