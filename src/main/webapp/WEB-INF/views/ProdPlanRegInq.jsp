<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<div class="prodplan-page">

	<div class="taPageActions">
		<button type="button" class="taOpenModal taBtn taBtnPrimary"
			data-modal-target="prodPlanRegisterModal">등록</button>
		<button type="button" id="deleteToggleBtn" class="taBtn taBtnOutline"
			onclick="handleDeleteButton()">삭제</button>
	</div>

	<div id="prodPlanRegisterModal" class="taModal" hidden
		aria-hidden="true">
		<div class="taModalDialog">
			<div class="taModalHeader">
				<h3 class="taModalTitle">생산계획 등록</h3>
				<button type="button" class="taModalClose"
					data-modal-target="prodPlanRegisterModal">×</button>
			</div>
			<form method="post"
				action="${pageContext.request.contextPath}/prodplan">
				<input type="hidden" name="cmd" value="register">
				<div class="taModalBody taModalGrid">
					<div class="form-row full">
						<label>품목</label> <select name="planCode" class="taFormInput"
							required>
							<option value="">선택</option>
							<c:forEach var="item" items="${itemOptions}">
								<option value="${item.planCode}">${item.planCode}/
									${item.planName}</option>
							</c:forEach>
						</select>
					</div>
					<div class="form-row">
						<label>계획일자</label><input type="date" name="planDate" required>
					</div>
					<div class="form-row">
						<label>계획수량</label><input type="number" name="planAmount" min="0"
							required>
					</div>
					<div class="form-row">
						<label>라인</label><input type="text" name="planLine"
							placeholder="예: LN-A" required>
					</div>
					<div class="form-row">
						<label>상태</label> <select name="status" class="taFormInput">
							<option value="REL">REL</option>
							<option value="대기">대기</option>
							<option value="진행중">진행중</option>
							<option value="완료">완료</option>
						</select>
					</div>
					<div class="form-row full">
						<label>비고</label>
						<textarea name="memo"></textarea>
					</div>
				</div>
				<div class="taModalFooter">
					<button type="button" class="taBtn taBtnOutline taModalClose">취소</button>
					<button type="submit" class="taBtn taBtnPrimary">저장</button>
				</div>
			</form>
		</div>
	</div>

	<form id="paSearchForm" method="get"
		action="${pageContext.request.contextPath}/prodplan">
		<input type="hidden" name="searched" value="Y"> <input
			type="hidden" name="page" id="paPage" value="${paCurrentPage}">

		<div class="taToolbarRow">

			<!-- 1) 첫 검색창 -->
			<div class="taToolbarField taToolbarSpan2">
				<select
					class="taSelect taAutoSelectColor ${empty param.searchType ? 'taSelectPlaceholder' : ''}"
					name="searchType">
					<option value="" hidden
						<c:if test="${empty param.searchType}">selected</c:if>>
						전체 / 생산계획번호 ...</option>
					<option value="all"
						<c:if test="${param.searchType eq 'all'}">selected</c:if>>
						전체</option>
					<option value="planNo"
						<c:if test="${param.searchType eq 'planNo'}">selected</c:if>>
						생산계획번호</option>
					<option value="planCode"
						<c:if test="${param.searchType eq 'planCode'}">selected</c:if>>
						품목코드</option>
					<option value="planName"
						<c:if test="${param.searchType eq 'planName'}">selected</c:if>>
						품목명</option>
					<option value="planLine"
						<c:if test="${param.searchType eq 'planLine'}">selected</c:if>>
						라인</option>
				</select>
			</div>

			<!-- 2) 시작일 -->
			<div class="taToolbarField taToolbarSpan2">
				<input type="date" class="taSearchInput" name="startDate"
					value="${param.startDate}">
			</div>

			<!-- 3) 종료일 -->
			<div class="taToolbarField taToolbarSpan2">
				<input type="date" class="taSearchInput" name="endDate"
					value="${param.endDate}">
			</div>

			<!-- 4) 검색키워드 + 돋보기 + 초기화 -->
			<div class="taToolbarField taToolbarFieldGrow taToolbarSpan6">
				<div class="taSearchBox">
					<input type="text" class="taSearchInput" name="keyword"
						placeholder="검색키워드" value="${param.keyword}">

					<button type="submit" class="taSearchBtn" aria-label="검색"
						onclick="document.getElementById('paPage').value=1;">
						<svg viewBox="0 0 24 24" fill="none" stroke="currentColor"
							stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
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
		action="${pageContext.request.contextPath}/prodplan">
		<input type="hidden" name="cmd" value="delete"> <input
			type="hidden" id="deleteMode" value="N"> <input type="hidden"
			name="searched" value="${param.searched}"> <input
			type="hidden" name="page" value="${page}"> <input
			type="hidden" name="startDate" value="${param.startDate}"> <input
			type="hidden" name="endDate" value="${param.endDate}"> <input
			type="hidden" name="searchType" value="${param.searchType}">
		<input type="hidden" name="keyword" value="${param.keyword}">

		<div class="taTableShell prodplan-table-shell">
			<div class="taTableScroll">
				<table class="taMesTable">
					<thead>
						<tr>
							<!-- 						css주석하면서 같이 주석 -->
							<!-- 							<th class="taTableHeadCell taColCheck delete-col"><input -->
							<!-- 								type="checkbox" id="checkAll"></th> -->
							<th class="taTableHeadCell taColFit">NO</th>
							<th class="taTableHeadCell taColFit">생산계획번호</th>
							<th class="taTableHeadCell taColDate">일자</th>
							<th class="taTableHeadCell taColFit">품목코드</th>
							<th class="taTableHeadCell taColGrow">품목명</th>
							<th class="taTableHeadCell taColFit">생산계획량</th>
							<th class="taTableHeadCell taColFit">단위</th>
							<th class="taTableHeadCell taColFit">라인</th>
							<th class="taTableHeadCell taColGrow">비고</th>
							<th class="taTableHeadCell taColAction taLastCol">상세보기</th>
						</tr>
					</thead>
					<tbody>
						<c:forEach var="dto" items="${list}">
							<tr class="taTableBodyRow">
								<td class="taTableBodyCell taColCheck delete-col"><input
									type="checkbox" name="seqNO" value="${dto.seqNO}"
									class="rowCheck"></td>
								<td class="taTableBodyCell taColFit">${dto.seqNO}</td>
								<td class="taTableBodyCell taColFit">${dto.planNo}</td>
								<td class="taTableBodyCell taColDate">${dto.planDate}</td>
								<td class="taTableBodyCell taColFit">${dto.planCode}</td>
								<td class="taTableBodyCell taColGrow">${dto.planName}</td>
								<td class="taTableBodyCell taColFit">${dto.planAmount}</td>
								<td class="taTableBodyCell taColFit">${dto.planUnit}</td>
								<td class="taTableBodyCell taColFit">${dto.planLine}</td>
								<td class="taTableBodyCell taColGrow">${dto.memo}</td>
								<td class="taTableBodyCell taColAction taLastCol"><a
									class="taLinkAnchor"
									href="${pageContext.request.contextPath}/prodplan?seqNO=${dto.seqNO}">상세보기</a></td>
							</tr>
						</c:forEach>
						<c:if test="${empty list}">
							<tr class="taTableBodyRow">
								<td class="taTableBodyCell taLastCol" colspan="11"
									style="text-align: center;">조회된 데이터가 없습니다.</td>
							</tr>
						</c:if>
					</tbody>
				</table>
			</div>
		</div>
	</form>
</div>

<script>
	document.addEventListener("DOMContentLoaded", function() {
		const checkAll = document.getElementById("checkAll");
		const deleteModeInput = document.getElementById("deleteMode");
		const deleteToggleBtn = document.getElementById("deleteToggleBtn");
		const deleteForm = document.getElementById("deleteForm");
		if (checkAll) {
			checkAll.addEventListener("change", function() {
				document.querySelectorAll(".rowCheck").forEach(
						function(checkbox) {
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
				deleteCols.forEach(function(col) {
					col.classList.add("show-delete-col");
				});
				deleteModeInput.value = "Y";
				deleteToggleBtn.textContent = "삭제확인";
				return;
			}
			let checkedCount = 0;
			rowChecks.forEach(function(checkbox) {
				if (checkbox.checked)
					checkedCount++;
			});
			if (checkedCount === 0) {
				alert("삭제할 항목을 선택하세요.");
				if (checkAll)
					checkAll.checked = false;
				rowChecks.forEach(function(checkbox) {
					checkbox.checked = false;
				});
				deleteCols.forEach(function(col) {
					col.classList.remove("show-delete-col");
				});
				deleteModeInput.value = "N";
				deleteToggleBtn.textContent = "삭제";
				return;
			}
			if (confirm("선택한 생산계획을 삭제하시겠습니까?"))
				deleteForm.submit();
		};
	});
</script>
