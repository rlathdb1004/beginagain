<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<c:if test="${not empty errorMsg}">
	<script>alert("${errorMsg}");</script>
</c:if>

<div class="taPageActions">
	<button type="button" class="taBtn taBtnPrimary taOpenModal"
		data-modal-target="registerModal">등록</button>
	<button type="button" class="taBtn taBtnOutline"
		onclick="handleDeleteButton()">선택삭제</button>
</div>

<div class="taSearchShell">
	<form method="get" action="${pageContext.request.contextPath}/woreginq"
		class="taLocalSearchForm">
		<div class="taToolbarRow">
			<div class="taToolbarField taToolbarSpan2">
				<select class="taSelect" name="searchType">
					<option value="">검색항목 선택</option>
					<option value="workOrderNo" ${param.searchType eq 'workOrderNo' ? 'selected' : ''}>작업지시번호</option>
					<option value="itemCode" ${param.searchType eq 'itemCode' ? 'selected' : ''}>품목코드</option>
					<option value="itemName" ${param.searchType eq 'itemName' ? 'selected' : ''}>품목명</option>
					<option value="lineCode" ${param.searchType eq 'lineCode' ? 'selected' : ''}>라인</option>
					<option value="empName" ${param.searchType eq 'empName' ? 'selected' : ''}>작업자</option>
				</select>
			</div>
			<div class="taToolbarField taToolbarSpan2"><input class="taSearchInput" type="date" name="startDate" value="${param.startDate}"></div>
			<div class="taToolbarField taToolbarSpan2"><input class="taSearchInput" type="date" name="endDate" value="${param.endDate}"></div>
			<div class="taToolbarField taToolbarFieldGrow taToolbarSpan6">
				<div class="taSearchBox">
					<input class="taSearchInput" type="text" name="keyword" placeholder="검색어" value="${param.keyword}">
					<button class="taSearchBtn" type="submit" aria-label="검색">
						<svg viewBox="0 0 24 24" fill="none" stroke-width="2"><circle cx="11" cy="11" r="7"></circle><path d="M20 20L16.65 16.65"></path></svg>
					</button>
					<button type="button" class="taBtn taBtnOutline taSearchReset"
						onclick="location.href='${pageContext.request.contextPath}/woreginq'">초기화</button>
				</div>
			</div>
		</div>
	</form>
</div>

<div class="taTableShell">
	<form id="deleteForm" action="${pageContext.request.contextPath}/woreginq" method="post">
		<input type="hidden" name="cmd" value="delete">
		<input type="hidden" name="page" value="${page}">
		<input type="hidden" name="searched" value="Y">
		<input type="hidden" name="startDate" value="${param.startDate}">
		<input type="hidden" name="endDate" value="${param.endDate}">
		<input type="hidden" name="searchType" value="${param.searchType}">
		<input type="hidden" name="keyword" value="${param.keyword}">

		<div class="taTableScroll">
			<table class="taMesTable">
				<thead>
					<tr>
						<th class="taTableHeadCell taColFit"><input type="checkbox" id="checkAll" class="taCheckInput"></th>
						<th class="taTableHeadCell taColFit">NO</th>
						<th class="taTableHeadCell taColFit">작업지시번호</th>
						<th class="taTableHeadCell taColDate">작업일자</th>
						<th class="taTableHeadCell taColFit">품목코드</th>
						<th class="taTableHeadCell taColGrow">품목명</th>
						<th class="taTableHeadCell taColFit">지시량</th>
						<th class="taTableHeadCell taColFit">단위</th>
						<th class="taTableHeadCell taColFit">라인</th>
						<th class="taTableHeadCell taColFit">작업자</th>
						<th class="taTableHeadCell taColAction taLastCol">상세보기</th>
					</tr>
				</thead>
				<tbody>
					<c:forEach var="dto" items="${list}">
						<tr class="taTableBodyRow">
							<td class="taTableBodyCell taColFit"><input type="checkbox" class="rowCheck taCheckInput" name="seqNO" value="${dto.seqNO}"></td>
							<td class="taTableBodyCell taColFit">${dto.seqNO}</td>
							<td class="taTableBodyCell taColFit">${dto.workOrderNo}</td>
							<td class="taTableBodyCell taColDate">${dto.workDate}</td>
							<td class="taTableBodyCell taColFit">${dto.itemCode}</td>
							<td class="taTableBodyCell taColGrow">${dto.itemName}</td>
							<td class="taTableBodyCell taColFit">${dto.workQty}</td>
							<td class="taTableBodyCell taColFit">${dto.unit}</td>
							<td class="taTableBodyCell taColFit">${dto.lineCode}</td>
														<td class="taTableBodyCell taColFit">${dto.empName}</td>
														<td class="taTableBodyCell taColAction taLastCol"><a class="taLinkAnchor" href="${pageContext.request.contextPath}/woreginq/detail?seqNO=${dto.seqNO}">상세보기</a></td>
						</tr>
					</c:forEach>
					<c:if test="${empty list}">
						<tr class="taTableBodyRow"><td class="taTableBodyCell taLastCol" colspan="11" style="text-align:center;">조회된 데이터가 없습니다.</td></tr>
					</c:if>
				</tbody>
			</table>
		</div>
	</form>
</div>

<div class="taModal" id="registerModal" hidden aria-hidden="true">
	<div class="taModalDialog modal-lg">
		<div class="taModalHeader">
			<h3 class="taModalTitle">작업지시 등록</h3>
			<button type="button" class="taModalClose">&times;</button>
		</div>
		<form action="${pageContext.request.contextPath}/woreginq/register" method="post">
			<div class="taModalBody taModalGrid">
				<div class="form-row">
					<label>생산계획</label>
					<select name="planId" id="woPlanId" required>
						<option value="">선택</option>
						<c:forEach var="plan" items="${planOptions}">
							<option value="${plan.planId}" data-item-code="${plan.itemCode}" data-item-name="${plan.itemName}" data-unit="${plan.unit}" data-line-code="${plan.lineCode}" data-plan-qty="${plan.planQty}" data-current-sum="${plan.currentWorkQtySum}" data-remaining-qty="${plan.remainingQty}">
								계획 ${plan.planId} / ${plan.itemName} / ${plan.lineCode}
							</option>
						</c:forEach>
					</select>
				</div>
				<div class="form-row">
					<label>작업자</label>
					<select name="empId" required>
						<option value="">선택</option>
						<c:forEach var="emp" items="${empOptions}">
							<option value="${emp.empId}">${emp.empName} (${emp.empNo})</option>
						</c:forEach>
					</select>
				</div>
				<div class="form-row"><label>품목코드</label><input type="text" id="woItemCode" readonly></div>
				<div class="form-row"><label>품목명</label><input type="text" id="woItemName" readonly></div>
				<div class="form-row"><label>단위</label><input type="text" id="woUnit" readonly></div>
				<div class="form-row"><label>라인</label><input type="text" id="woLineCode" readonly></div>
				<div class="form-row"><label>계획수량</label><input type="text" id="woPlanQty" readonly></div>
				<div class="form-row"><label>현재 지시합계</label><input type="text" id="woCurrentSum" readonly></div>
				<div class="form-row"><label>남은 가능 수량</label><input type="text" id="woRemainingQty" readonly></div>
				<div class="form-row"><label>작업일자</label><input type="date" name="workDate" required></div>
				<div class="form-row"><label>지시량</label><input type="number" name="workQty" min="1" required></div>
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
	const deleteForm = document.getElementById("deleteForm");
	const rowChecks = document.querySelectorAll(".rowCheck");
	const planSelect = document.getElementById("woPlanId");
	const itemCode = document.getElementById("woItemCode");
	const itemName = document.getElementById("woItemName");
	const unit = document.getElementById("woUnit");
	const lineCode = document.getElementById("woLineCode");
	const planQty = document.getElementById("woPlanQty");
	const currentSum = document.getElementById("woCurrentSum");
	const remainingQty = document.getElementById("woRemainingQty");

	function syncPlanInfo() {
		if (!planSelect) return;
		const selected = planSelect.options[planSelect.selectedIndex];
		if (!selected || !selected.value) {
			itemCode.value = "";
			itemName.value = "";
			unit.value = "";
			lineCode.value = "";
			planQty.value = "";
			currentSum.value = "";
			remainingQty.value = "";
			return;
		}
		itemCode.value = selected.dataset.itemCode || "";
		itemName.value = selected.dataset.itemName || "";
		unit.value = selected.dataset.unit || "";
		lineCode.value = selected.dataset.lineCode || "";
		planQty.value = selected.dataset.planQty || "0";
		currentSum.value = selected.dataset.currentSum || "0";
		remainingQty.value = selected.dataset.remainingQty || "0";
	}

	if (planSelect) {
		planSelect.addEventListener("change", syncPlanInfo);
		syncPlanInfo();
	}

	if (checkAll) {
		checkAll.addEventListener("change", function() {
			rowChecks.forEach(function(checkbox) { checkbox.checked = checkAll.checked; });
		});
	}

	rowChecks.forEach(function(checkbox) {
		checkbox.addEventListener("change", function() {
			const checkedCount = document.querySelectorAll(".rowCheck:checked").length;
			if (checkAll) {
				checkAll.checked = rowChecks.length > 0 && checkedCount === rowChecks.length;
			}
		});
	});

	window.handleDeleteButton = function() {
		const checkedRows = document.querySelectorAll(".rowCheck:checked");
		if (rowChecks.length === 0) {
			alert("삭제할 데이터가 없습니다.");
			return;
		}
		if (checkedRows.length === 0) {
			alert("삭제할 항목을 선택하세요.");
			return;
		}
		if (confirm("선택한 작업지시를 삭제하시겠습니까?")) {
			deleteForm.submit();
		}
	};
});
</script>
