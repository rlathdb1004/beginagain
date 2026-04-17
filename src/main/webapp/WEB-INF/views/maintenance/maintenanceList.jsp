<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<div class="taPageActions">
	<button type="button" class="taBtn taBtnPrimary"
		data-modal-target="registerModal">등록</button>
	<button type="submit" form="deleteForm" class="taBtn taBtnOutline"
		onclick="return confirm('선택한 정비이력을 삭제하시겠습니까?');">선택 삭제</button>
</div>

<form id="paSearchForm" method="get"
	action="${pageContext.request.contextPath}/maintenance/list">
	<input type="hidden" name="page" id="paPage" value="${paCurrentPage}">

	<div class="taToolbarRow">
		<div class="taToolbarField">
			<select class="taSelect" name="searchType">
				<option value="all"
					${empty searchType or searchType eq 'all' ? 'selected' : ''}>전체</option>
				<option value="equipmentCode"
					${searchType eq 'equipmentCode' ? 'selected' : ''}>설비코드</option>
				<option value="equipmentName"
					${searchType eq 'equipmentName' ? 'selected' : ''}>설비명</option>
				<option value="maintenanceType"
					${searchType eq 'maintenanceType' ? 'selected' : ''}>정비유형</option>
				<option value="status" ${searchType eq 'status' ? 'selected' : ''}>상태</option>
			</select>
		</div>

		<div class="taToolbarField taToolbarFieldGrow"
			style="grid-column: span 3;">
			<div class="taSearchBox">
				<input type="text" class="taSearchInput" name="keyword"
					value="${keyword}" placeholder="검색어를 입력하세요">
				<button type="submit" class="taSearchBtn" aria-label="검색">
					<svg viewBox="0 0 24 24" fill="none" stroke="currentColor"
						stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
        <circle cx="11" cy="11" r="7"></circle>
        <path d="M20 20L16.65 16.65"></path>
    </svg>
				</button>
				<button type="button" class="taBtn taBtnOutline taSearchReset"
					onclick="location.href='${pageContext.request.contextPath}/maintenance/list'">
					초기화</button>
			</div>
		</div>
	</div>
</form>

<form id="deleteForm"
	action="${pageContext.request.contextPath}/maintenance/delete"
	method="post">
	<div class="taTableShell" id="paTableBox">
		<div class="taTableScroll">
			<table class="taMesTable" id="maintenanceTable">
				<thead>
					<tr>
						<th class="taTableHeadCell taCheckCell"><input
							type="checkbox" id="checkAll" class="taCheckInput"></th>
						<th class="taTableHeadCell taColFit">정비번호</th>
						<th class="taTableHeadCell taColFit">정비일자</th>
						<th class="taTableHeadCell taColFit">설비코드</th>
						<th class="taTableHeadCell taColGrow">설비명</th>
						<th class="taTableHeadCell taColFit">정비유형</th>
						<th class="taTableHeadCell taColGrow">정비내용</th>
						<th class="taTableHeadCell taColFit">다음정비일</th>
						<th class="taTableHeadCell taColFit">상태</th>
						<th class="taTableHeadCell taColAction taLastCol">상세</th>
					</tr>
				</thead>
				<tbody>
					<c:choose>
						<c:when test="${not empty maintenanceList}">
							<c:forEach var="m" items="${maintenanceList}">
								<tr class="taTableBodyRow">
									<td class="taTableBodyCell taCheckCell"><input
										type="checkbox" name="maintenanceId"
										value="${m.maintenanceId}" class="taCheckInput"></td>
									<td class="taTableBodyCell taColFit">${m.maintenanceId}</td>
									<td class="taTableBodyCell taColFit">${m.maintenanceDate}</td>
									<td class="taTableBodyCell taColFit">${m.equipmentCode}</td>
									<td class="taTableBodyCell taColGrow">${m.equipmentName}</td>
									<td class="taTableBodyCell taColFit">${m.maintenanceType}</td>
									<td class="taTableBodyCell taColGrow">${m.maintenanceContent}</td>
									<td class="taTableBodyCell taColFit">${m.nextMaintenanceDate}</td>
									<td class="taTableBodyCell taColFit">${m.status}</td>
									<td class="taTableBodyCell taColAction taLastCol"><a
										class="taLinkAnchor"
										href="${pageContext.request.contextPath}/maintenance/detail?maintenanceId=${m.maintenanceId}">
											상세보기 </a></td>
								</tr>
							</c:forEach>
						</c:when>

						<c:otherwise>
							<tr class="taTableBodyRow">
								<td class="taTableBodyCell taLastCol" colspan="10"
									style="text-align: center;">조회된 정비이력이 없습니다.</td>
							</tr>
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
			<h3 class="taModalTitle">정비이력 등록</h3>
			<button type="button" class="taModalClose">&times;</button>
		</div>

		<form action="${pageContext.request.contextPath}/maintenance/register"
			method="post">
			<div class="taModalBody taModalGrid">
				<div class="form-row">
					<label>설비번호</label> <input type="number" name="equipmentId"
						required>
				</div>

				<div class="form-row">
					<label>정비일자</label> <input type="date" name="maintenanceDate"
						required>
				</div>

				<div class="form-row">
					<label>정비유형</label> <select name="maintenanceType">
						<option value="정기정비">정기정비</option>
						<option value="예방정비">예방정비</option>
						<option value="고장수리">고장수리</option>
						<option value="부품교체">부품교체</option>
						<option value="기타">기타</option>
					</select>
				</div>

				<div class="form-row">
					<label>다음정비일</label> <input type="date" name="nextMaintenanceDate">
				</div>

				<div class="form-row">
					<label>상태</label> <select name="status">
						<option value="예정">예정</option>
						<option value="진행중">진행중</option>
						<option value="완료">완료</option>
					</select>
				</div>

				<div class="form-row full">
					<label>정비내용</label>
					<textarea name="maintenanceContent"></textarea>
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
	document.getElementById("checkAll").addEventListener(
			"change",
			function() {
				const checks = document
						.querySelectorAll("input[name='maintenanceId']");
				for (let i = 0; i < checks.length; i++) {
					checks[i].checked = this.checked;
				}
			});
</script>