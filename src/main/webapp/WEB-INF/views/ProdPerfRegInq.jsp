<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<div class="prodperf-page">
    <c:set var="isSearched" value="${not empty list}" />

    <div class="taPageActions">
        <button type="button" class="taBtn taBtnPrimary" data-modal-target="registerModal">등록</button>
        <button type="button" id="deleteToggleBtn" class="taBtn taBtnOutline" onclick="handleDeleteButton()">삭제</button>
    </div>

    <form id="paSearchForm" method="get" action="${pageContext.request.contextPath}/prodperf">
        <input type="hidden" name="searched" value="Y">
        <input type="hidden" name="page" id="paPage" value="${paCurrentPage}">
        <div class="taToolbarRow">
            <div class="taToolbarField">
                <div class="taSearchBox">
                    <input type="date" class="taSearchInput" name="startDate" value="${param.startDate}">
                    <input type="date" class="taSearchInput" name="endDate" value="${param.endDate}">
                </div>
            </div>
            <div class="taToolbarField">
                <select class="taSelect" name="searchType">
                    <option value="" ${empty param.searchType ? "selected" : ""}>전체</option>
                    <option value="workOrderNo" ${param.searchType eq 'workOrderNo' ? "selected" : ""}>작업지시번호</option>
                    <option value="itemCode" ${param.searchType eq 'itemCode' ? "selected" : ""}>품목코드</option>
                    <option value="itemName" ${param.searchType eq 'itemName' ? "selected" : ""}>품목명</option>
                    <option value="lineCode" ${param.searchType eq 'lineCode' ? "selected" : ""}>라인</option>
                    <option value="lotNo" ${param.searchType eq 'lotNo' ? "selected" : ""}>LOT</option>
                </select>
            </div>
            <div class="taToolbarField taToolbarFieldGrow">
                <div class="taSearchBox">
                    <input type="text" class="taSearchInput" name="keyword" placeholder="작업지시번호 / 품목코드 / 품목명 / 라인 / LOT 검색" value="${param.keyword}">
                    <button type="submit" class="taSearchBtn" aria-label="검색">
                        <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                            <circle cx="11" cy="11" r="7"></circle>
                            <path d="M20 20L16.65 16.65"></path>
                        </svg>
                    </button>
                </div>
            </div>
        </div>
    </form>

    <form id="deleteForm" method="post" action="${pageContext.request.contextPath}/prodperf">
        <input type="hidden" name="cmd" value="delete">
        <input type="hidden" id="deleteMode" value="N">
        <input type="hidden" name="searched" value="${param.searched}">
        <input type="hidden" name="page" value="${page}">
        <input type="hidden" name="startDate" value="${param.startDate}">
        <input type="hidden" name="endDate" value="${param.endDate}">
        <input type="hidden" name="searchType" value="${param.searchType}">
        <input type="hidden" name="keyword" value="${param.keyword}">

        <div class="taTableShell prodperf-table-shell" id="paTableBox">
            <div class="taTableScroll">
                <table class="taMesTable">
                    <thead>
                        <tr>
                            <th class="taTableHeadCell taColCheck delete-col"><input type="checkbox" id="checkAll"></th>
                            <th class="taTableHeadCell taColFit">NO</th>
                            <th class="taTableHeadCell taColFit">작업지시번호</th>
                            <th class="taTableHeadCell taColDate">일자</th>
                            <th class="taTableHeadCell taColFit">품목코드</th>
                            <th class="taTableHeadCell taColGrow">품목명</th>
                            <th class="taTableHeadCell taColFit">생산량</th>
                            <th class="taTableHeadCell taColFit">단위</th>
                            <th class="taTableHeadCell taColFit">라인</th>
                            <th class="taTableHeadCell taColFit">LOT</th>
                            <th class="taTableHeadCell taColGrow">비고</th>
                            <th class="taTableHeadCell taColAction taLastCol">상세보기</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="dto" items="${list}">
                            <tr class="taTableBodyRow">
                                <td class="taTableBodyCell taColCheck delete-col"><input type="checkbox" name="seqNO" value="${dto.seqNO}" class="rowCheck"></td>
                                <td class="taTableBodyCell taColFit">${dto.seqNO}</td>
                                <td class="taTableBodyCell taColFit">${dto.workOrderNo}</td>
                                <td class="taTableBodyCell taColDate">${dto.resultDate}</td>
                                <td class="taTableBodyCell taColFit">${dto.itemCode}</td>
                                <td class="taTableBodyCell taColGrow">${dto.itemName}</td>
                                <td class="taTableBodyCell taColFit">${dto.producedQty}</td>
                                <td class="taTableBodyCell taColFit">${dto.unit}</td>
                                <td class="taTableBodyCell taColFit">${dto.lineCode}</td>
                                <td class="taTableBodyCell taColFit">${dto.lotNo}</td>
                                <td class="taTableBodyCell taColGrow">${dto.remark}</td>
                                <td class="taTableBodyCell taColAction taLastCol"><a class="taLinkAnchor" href="${pageContext.request.contextPath}/prodperf/detail?seqNO=${dto.seqNO}">상세보기</a></td>
                            </tr>
                        </c:forEach>
                        <c:if test="${empty list}">
                            <tr class="taTableBodyRow">
                                <td class="taTableBodyCell taLastCol" colspan="12" style="text-align:center;">조회된 데이터가 없습니다.</td>
                            </tr>
                        </c:if>
                    </tbody>
                </table>
            </div>
        </div>
    </form>
</div>

<div class="taModal" id="registerModal" hidden aria-hidden="true">
    <div class="taModalDialog modal-lg">
        <div class="taModalHeader">
            <h3 class="taModalTitle">생산실적 등록</h3>
            <button type="button" class="taModalClose">&times;</button>
        </div>
        <form action="${pageContext.request.contextPath}/prodperf/register" method="post">
            <div class="taModalBody taModalGrid">
                <div class="form-row">
                    <label>작업지시</label>
                    <select name="workOrderId" id="registerWorkOrderId" required>
                        <option value="">선택</option>
                        <c:forEach var="wo" items="${workOrderOptions}">
                            <option value="${wo.workOrderId}" data-item-code="${wo.itemCode}" data-item-name="${wo.itemName}" data-line-code="${wo.lineCode}" data-unit="${wo.unit}">${wo.workOrderNo} / ${wo.itemName} / ${wo.lineCode}</option>
                        </c:forEach>
                    </select>
                </div>
                <div class="form-row"><label>생산실적일자</label><input type="date" name="resultDate" required></div>
                <div class="form-row"><label>품목코드</label><input type="text" id="registerItemCode" readonly></div>
                <div class="form-row"><label>품목명</label><input type="text" id="registerItemName" readonly></div>
                <div class="form-row"><label>생산량</label><input type="number" name="producedQty" min="0" required></div>
                <div class="form-row"><label>손실량</label><input type="number" name="lossQty" min="0" value="0"></div>
                <div class="form-row"><label>단위</label><input type="text" id="registerUnit" readonly></div>
                <div class="form-row"><label>라인</label><input type="text" id="registerLineCode" readonly></div>
                <div class="form-row"><label>LOT</label><input type="text" name="lotNo" required></div>
                <div class="form-row">
                    <label>상태</label>
                    <select name="status" required>
                        <option value="대기">대기</option>
                        <option value="진행중">진행중</option>
                        <option value="완료">완료</option>
                    </select>
                </div>
                <div class="form-row full"><label>비고</label><textarea name="remark"></textarea></div>
            </div>
            <div class="taModalFooter">
                <button type="button" class="taBtn taBtnOutline taModalClose">취소</button>
                <button type="submit" class="taBtn taBtnPrimary">등록</button>
            </div>
        </form>
    </div>
</div>

<script>
document.addEventListener("DOMContentLoaded", function() {
    const checkAll = document.getElementById("checkAll");
    const deleteModeInput = document.getElementById("deleteMode");
    const deleteToggleBtn = document.getElementById("deleteToggleBtn");
    const deleteForm = document.getElementById("deleteForm");

    if (checkAll) {
        checkAll.addEventListener("change", function() {
            document.querySelectorAll(".rowCheck").forEach(function(checkbox) {
                checkbox.checked = checkAll.checked;
            });
        });
    }

    window.handleDeleteButton = function() {
        const deleteCols = document.querySelectorAll(".delete-col");
        const rowChecks = document.querySelectorAll(".rowCheck");
        if (rowChecks.length === 0) {
            alert("삭제할 데이터가 없습니다.");
            return;
        }
        if (deleteModeInput.value === "N") {
            deleteCols.forEach(function(col) { col.classList.add("show-delete-col"); });
            deleteModeInput.value = "Y";
            deleteToggleBtn.textContent = "삭제확인";
            return;
        }
        let checkedCount = 0;
        rowChecks.forEach(function(checkbox) { if (checkbox.checked) checkedCount++; });
        if (checkedCount === 0) {
            alert("삭제할 항목을 선택하세요.");
            if (checkAll) checkAll.checked = false;
            rowChecks.forEach(function(checkbox) { checkbox.checked = false; });
            deleteCols.forEach(function(col) { col.classList.remove("show-delete-col"); });
            deleteModeInput.value = "N";
            deleteToggleBtn.textContent = "삭제";
            return;
        }
        if (confirm("선택한 생산실적을 삭제하시겠습니까?")) {
            deleteForm.submit();
        }
    };

    const registerModal = document.getElementById("registerModal");
    const registerOpenBtn = document.querySelector('[data-modal-target="registerModal"]');
    const registerCloseBtns = document.querySelectorAll("#registerModal .taModalClose");
    const registerWorkOrderId = document.getElementById("registerWorkOrderId");
    const registerItemCode = document.getElementById("registerItemCode");
    const registerItemName = document.getElementById("registerItemName");
    const registerLineCode = document.getElementById("registerLineCode");
    const registerUnit = document.getElementById("registerUnit");

    function syncRegisterWorkOrderMeta() {
        const opt = registerWorkOrderId && registerWorkOrderId.options[registerWorkOrderId.selectedIndex];
        registerItemCode.value = opt ? (opt.dataset.itemCode || "") : "";
        registerItemName.value = opt ? (opt.dataset.itemName || "") : "";
        registerLineCode.value = opt ? (opt.dataset.lineCode || "") : "";
        registerUnit.value = opt ? (opt.dataset.unit || "") : "";
    }

    if (registerOpenBtn && registerModal) {
        registerOpenBtn.addEventListener("click", function() {
            registerModal.hidden = false;
            registerModal.setAttribute("aria-hidden", "false");
            syncRegisterWorkOrderMeta();
        });
    }
    registerCloseBtns.forEach(function(btn) {
        btn.addEventListener("click", function() {
            registerModal.hidden = true;
            registerModal.setAttribute("aria-hidden", "true");
        });
    });
    if (registerModal) {
        registerModal.addEventListener("click", function(e) {
            if (e.target === registerModal) {
                registerModal.hidden = true;
                registerModal.setAttribute("aria-hidden", "true");
            }
        });
    }
    if (registerWorkOrderId) {
        registerWorkOrderId.addEventListener("change", syncRegisterWorkOrderMeta);
        syncRegisterWorkOrderMeta();
    }
});
</script>
