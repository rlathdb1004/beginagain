<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<div class="prodplan-page">

	<div class="taPageActions">
		<button type="button" class="taOpenModal taBtn taBtnPrimary"
			data-modal-target="prodPlanRegisterModal">등록</button>
		<button type="submit" form="deleteForm" class="taBtn taBtnOutline">
			선택 삭제</button>
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
						<label>품목</label> <select name="itemId" id="prodPlanItemId"
							class="taFormInput" required>
							<option value="">선택</option>
							<c:forEach var="item" items="${itemOptions}">
								<option value="${item.itemId}" data-code="${item.planCode}"
									data-name="${item.planName}" data-unit="${item.planUnit}">${item.planCode}
									/ ${item.planName}</option>
							</c:forEach>
						</select>
					</div>
					<div class="form-row">
						<label>품목코드</label><input type="text" id="prodPlanItemCode"
							class="taFormInput" readonly>
					</div>
					<div class="form-row">
						<label>품목명</label><input type="text" id="prodPlanItemName"
							class="taFormInput" readonly>
					</div>
					<div class="form-row">
						<label>단위</label><input type="text" id="prodPlanItemUnit"
							class="taFormInput" readonly>
					</div>
					<div class="form-row">
						<label>계획일자</label><input type="date" name="planDate" required>
					</div>
					<div class="form-row">
						<label>계획수량</label><input type="number" name="planAmount" min="1"
							required>
					</div>
					<div class="form-row">
						<label>라인</label><select name="planLine" class="taFormInput" required>
							<option value="">선택</option>
							<option value="LN-A">LN-A</option>
							<option value="LN-B">LN-B</option>
							<option value="LN-C">LN-C</option>
						</select>
					</div>
					<div class="form-row">
						<label>상태</label> <select name="status" class="taFormInput">
							<option value="대기" selected>대기</option>
							<option value="작업지시중">작업지시중</option>
							<option value="작업지시완료">작업지시완료</option>
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

			<div class="taToolbarField taToolbarSpan2">
				<label class="taDateLabel">시작일</label>
				<input type="date" class="taSearchInput" name="startDate"
					value="${param.startDate}">
			</div>

			<div class="taToolbarField taToolbarSpan2">
				<label class="taDateLabel">종료일</label>
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
							<th class="taTableHeadCell taColFit delete-col"><input
								type="checkbox" id="checkAll" class="taCheckInput"></th>
							<th class="taTableHeadCell taColFit">NO</th>
							<th class="taTableHeadCell taColFit">생산계획번호</th>
							<th class="taTableHeadCell taColFit">품목코드</th>
							<th class="taTableHeadCell taColGrow">품목명</th>
							<th class="taTableHeadCell taColFit">생산계획량</th>
							<th class="taTableHeadCell taColFit">라인</th>
							<th class="taTableHeadCell taColGrow">비고</th>
							<th class="taTableHeadCell taColAction taLastCol">상세보기</th>
						</tr>
					</thead>
					<tbody>
						<c:forEach var="dto" items="${list}">
							<tr class="taTableBodyRow">
								<td class="taTableBodyCell taColFit delete-col"><input
									type="checkbox" name="seqNO" value="${dto.seqNO}"
									class="rowCheck taCheckInput"></td>
								<td class="taTableBodyCell taColFit">${dto.seqNO}</td>
								<td class="taTableBodyCell taColFit">${dto.planNo}</td>
								<td class="taTableBodyCell taColFit">${dto.planCode}</td>
								<td class="taTableBodyCell taColGrow">${dto.planName}</td>
								<td class="taTableBodyCell taColFit">${dto.planAmount}</td>
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
(function() {
	const select = document.getElementById('prodPlanItemId');
	const code = document.getElementById('prodPlanItemCode');
	const name = document.getElementById('prodPlanItemName');
	const unit = document.getElementById('prodPlanItemUnit');
	if (!select) return;
	function sync() {
		const opt = select.options[select.selectedIndex];
		if (!opt || !opt.value) {
			code.value = '';
			name.value = '';
			unit.value = '';
			return;
		}
		code.value = opt.dataset.code || '';
		name.value = opt.dataset.name || '';
		unit.value = opt.dataset.unit || '';
	}
	select.addEventListener('change', sync);
	sync();
})();
</script>
