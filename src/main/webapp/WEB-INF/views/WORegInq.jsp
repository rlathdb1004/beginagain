<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<div class="woreg-page">

	<c:set var="isSearched" value="${not empty list}" />

	<div class="taPageActions">
		<button type="button" class="taBtn taBtnPrimary"
			data-modal-target="registerModal">등록</button>
		<button type="button" id="deleteToggleBtn" class="taBtn taBtnOutline"
			onclick="handleDeleteButton()">선택 삭제</button>
	</div>

	<form id="paSearchForm" method="get"
		action="${pageContext.request.contextPath}/woreginq">
		<input type="hidden" name="searched" value="Y"> <input
			type="hidden" name="page" id="paPage" value="${paCurrentPage}">

		<div class="taToolbarRow">

			<div class="taToolbarField taToolbarSpan2">
				<select
					class="taSelect taAutoSelectColor ${empty param.searchType ? 'taSelectPlaceholder' : ''}"
					name="searchType">
					<option value=""hidden ${emptyparam.searchType ? "selected" : ""}>
						전체 / 작업지시번호 ...</option>
					<option value="all" ${param.searchType eq 'all' ? "selected" : ""}>
						전체</option>
					<option value="workOrderNo"
						${param.searchType eq 'workOrderNo' ? "selected" : ""}>작업지시번호</option>
					<option value="itemCode"
						${param.searchType eq 'itemCode' ? "selected" : ""}>품목코드</option>
					<option value="itemName"
						${param.searchType eq 'itemName' ? "selected" : ""}>품목명</option>
					<option value="lineCode"
						${param.searchType eq 'lineCode' ? "selected" : ""}>라인</option>
					<option value="processCode"
						${param.searchType eq 'processCode' ? "selected" : ""}>공정</option>
					<option value="empName"
						${param.searchType eq 'empName' ? "selected" : ""}>작업자</option>
					<option value="bomCode"
						${param.searchType eq 'bomCode' ? "selected" : ""}>BOM</option>
				</select>
			</div>

			<div class="taToolbarField taToolbarSpan2">
				<input type="date" class="taSearchInput" name="startDate"
					value="${param.startDate}">
			</div>

			<div class="taToolbarField taToolbarSpan2">
				<input type="date" class="taSearchInput" name="endDate"
					value="${param.endDate}">
			</div>

			<div class="taToolbarField taToolbarFieldGrow taToolbarSpan6">
				<div class="taSearchBox">
					<input type="text" class="taSearchInput" name="keyword"
						placeholder="검색키워드" value="${param.keyword}">

					<button type="submit" class="taSearchBtn" aria-label="검색"
						onclick="document.getElementById('paPage').value=1;">
						<svg viewBox="0 0 24 24" fill="none" stroke="currentColor"
							stroke-width="2">
							<circle cx="11" cy="11" r="7"></circle>
							<path d="M20 20L16.65 16.65"></path>
						</svg>
					</button>

					<button type="button" class="taBtn taBtnOutline taSearchReset"
						onclick="location.href=document.getElementById('paSearchForm').action">
						초기화</button>
				</div>
			</div>

		</div>
	</form>

	<form id="deleteForm" method="post"
		action="${pageContext.request.contextPath}/woreginq">
		<input type="hidden" name="cmd" value="delete"> <input
			type="hidden" name="searched" value="${param.searched}"> <input
			type="hidden" name="page" value="${page}"> <input
			type="hidden" name="startDate" value="${param.startDate}"> <input
			type="hidden" name="endDate" value="${param.endDate}"> <input
			type="hidden" name="searchType" value="${param.searchType}">
		<input type="hidden" name="keyword" value="${param.keyword}">

		<div class="taTableShell woreg-table-shell" id="paTableBox">
			<div class="taTableScroll">
				<table class="taMesTable">
					<thead>
						<tr>
							<th class="taTableHeadCell taColFit"><input type="checkbox"
								id="checkAll" class="taCheckInput"></th>
							<th class="taTableHeadCell taColFit">NO</th>
							<th class="taTableHeadCell taColFit">작업지시번호</th>
							<th class="taTableHeadCell taColDate">일자</th>
							<th class="taTableHeadCell taColFit">품목코드</th>
							<th class="taTableHeadCell taColGrow">품목명</th>
							<th class="taTableHeadCell taColFit">지시량(생산량)</th>
							<th class="taTableHeadCell taColFit">단위</th>
							<th class="taTableHeadCell taColFit">라인</th>
							<th class="taTableHeadCell taColFit">공정</th>
							<th class="taTableHeadCell taColFit">작업자</th>
							<th class="taTableHeadCell taColFit">BOM</th>
							<th class="taTableHeadCell taColGrow">비고</th>
							<th class="taTableHeadCell taColAction taLastCol">상세보기</th>
						</tr>
					</thead>

					<tbody>
						<c:forEach var="dto" items="${list}">
							<tr class="taTableBodyRow">
								<td class="taTableBodyCell taColFit"><input type="checkbox"
									name="seqNO" value="${dto.seqNO}" class="rowCheck taCheckInput"></td>
								<td class="taTableBodyCell taColFit">${dto.seqNO}</td>
								<td class="taTableBodyCell taColFit">${dto.workOrderNo}</td>
								<td class="taTableBodyCell taColDate">${dto.workDate}</td>
								<td class="taTableBodyCell taColFit">${dto.itemCode}</td>
								<td class="taTableBodyCell taColGrow">${dto.itemName}</td>
								<td class="taTableBodyCell taColFit">${dto.workQty}</td>
								<td class="taTableBodyCell taColFit">${dto.unit}</td>
								<td class="taTableBodyCell taColFit">${dto.lineCode}</td>
								<td class="taTableBodyCell taColFit">${dto.processCode}</td>
								<td class="taTableBodyCell taColFit">${dto.empName}</td>
								<td class="taTableBodyCell taColFit">${dto.bomCode}</td>
								<td class="taTableBodyCell taColGrow taRemarkCell"
									title="${dto.remark}">${dto.remark}</td>
								<td class="taTableBodyCell taColAction taLastCol"><a
									class="taLinkAnchor"
									href="${pageContext.request.contextPath}/woreginq/detail?seqNO=${dto.seqNO}">상세보기</a>
								</td>
							</tr>
						</c:forEach>

						<c:if test="${empty list}">
							<tr class="taTableBodyRow">
								<td class="taTableBodyCell taLastCol" colspan="14"
									style="text-align: center;">조회된 데이터가 없습니다.</td>
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
			<h3 class="taModalTitle">작업지시 등록</h3>
			<button type="button" class="taModalClose">&times;</button>
		</div>
		<form action="${pageContext.request.contextPath}/woreginq/register"
			method="post">
			<div class="taModalBody taModalGrid">
				<div class="form-row">
					<label>생산계획</label> <select name="planId" required>
						<option value="">선택</option>
						<c:forEach var="plan" items="${planOptions}">
							<option value="${plan.planId}">계획 ${plan.planId} /
								${plan.itemName} / ${plan.lineCode}</option>
						</c:forEach>
					</select>
				</div>
				<div class="form-row">
					<label>작업자</label> <select name="empId" required>
						<option value="">선택</option>
						<c:forEach var="emp" items="${empOptions}">
							<option value="${emp.empId}">${emp.empName}
								(${emp.empNo})</option>
						</c:forEach>
					</select>
				</div>
				<div class="form-row">
					<label>작업일자</label><input type="date" name="workDate" required>
				</div>
				<div class="form-row">
					<label>지시량</label><input type="number" name="workQty" min="0"
						required>
				</div>
				<div class="form-row">
					<label>상태</label> <select name="status" required>
						<option value="대기">대기</option>
						<option value="진행중">진행중</option>
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
				<button type="submit" class="taBtn taBtnPrimary">등록</button>
			</div>
		</form>
	</div>
</div>

<script>
	document.addEventListener("DOMContentLoaded", function() {
		const checkAll = document.getElementById("checkAll");
		const deleteForm = document.getElementById("deleteForm");

		if (checkAll) {
			checkAll.addEventListener("change", function() {
				const rowChecks = document.querySelectorAll(".rowCheck");
				rowChecks.forEach(function(checkbox) {
					checkbox.checked = checkAll.checked;
				});
			});
		}

		const rowChecks = document.querySelectorAll(".rowCheck");
		rowChecks.forEach(function(checkbox) {
			checkbox.addEventListener("change", function() {
				const checkedCount = document
						.querySelectorAll(".rowCheck:checked").length;
				if (checkAll) {
					checkAll.checked = rowChecks.length > 0
							&& checkedCount === rowChecks.length;
				}
			});
		});

		window.handleDeleteButton = function() {
			const checkedRows = document.querySelectorAll(".rowCheck:checked");
			const allRows = document.querySelectorAll(".rowCheck");

			if (allRows.length === 0) {
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
