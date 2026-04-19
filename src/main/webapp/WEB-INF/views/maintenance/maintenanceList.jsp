<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<c:if test="${not empty errorMsg}">
	<script>
		alert('${errorMsg}');
	</script>
</c:if>

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
		<div class="taToolbarField taToolbarSpan3">
			<select
				class="taSelect taAutoSelectColor ${empty searchType or searchType eq 'all' ? 'taSelectPlaceholder' : ''}"
				name="searchType">
				<option value="" disabled hidden
					<c:if test="${empty searchType or searchType eq 'all'}">selected</c:if>>전체
					/ 설비코드 ...</option>
				<option value="all"
					<c:if test="${searchType eq 'all'}">selected</c:if>>전체</option>
				<option value="equipmentCode"
					<c:if test="${searchType eq 'equipmentCode'}">selected</c:if>>설비코드</option>
				<option value="equipmentName"
					<c:if test="${searchType eq 'equipmentName'}">selected</c:if>>설비명</option>
				<option value="maintenanceType"
					<c:if test="${searchType eq 'maintenanceType'}">selected</c:if>>정비유형</option>
				<option value="status"
					<c:if test="${searchType eq 'status'}">selected</c:if>>상태</option>
			</select>
		</div>
		<div class="taToolbarField taToolbarFieldGrow taToolbarSpan9">
			<div class="taSearchBox">
				<input type="text" class="taSearchInput" name="keyword"
					value="${keyword}" placeholder="검색키워드">
				<button type="submit" class="taSearchBtn" aria-label="검색"
					onclick="document.getElementById('paPage').value=1;">검색</button>
				<button type="button" class="taBtn taBtnOutline taSearchReset"
					onclick="location.href='${pageContext.request.contextPath}/maintenance/list'">초기화</button>
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
									<td class="taTableBodyCell taColFit">${m.status}</td>
									<td class="taTableBodyCell taColAction taLastCol"><a
										class="taLinkAnchor"
										href="${pageContext.request.contextPath}/maintenance/detail?maintenanceId=${m.maintenanceId}">상세보기</a></td>
								</tr>
							</c:forEach>
						</c:when>
						<c:otherwise>
							<tr class="taTableBodyRow">
								<td class="taTableBodyCell taLastCol" colspan="8"
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
					<label>설비선택</label> <select name="equipmentId"
						id="maintenanceEquipmentId" class="taSelect" required>
						<option value="">설비를 선택하세요</option>
						<c:forEach var="equipment" items="${equipmentList}">
							<option value="${equipment.equipmentId}"
								data-code="${equipment.equipmentCode}"
								data-name="${equipment.equipmentName}"
								data-model="${equipment.modelName}"
								data-location="${equipment.location}">
								${equipment.equipmentCode} - ${equipment.equipmentName}</option>
						</c:forEach>
					</select>
				</div>
				<div class="form-row">
					<label>설비코드</label><input type="text" id="maintenanceEquipmentCode"
						readonly>
				</div>
				<div class="form-row">
					<label>설비명</label><input type="text" id="maintenanceEquipmentName"
						readonly>
				</div>
				<div class="form-row">
					<label>모델명</label><input type="text" id="maintenanceModelName"
						readonly>
				</div>
				<div class="form-row">
					<label>위치</label><input type="text" id="maintenanceLocation"
						readonly>
				</div>
				<div class="form-row">
					<label>정비일자</label><input type="date" name="maintenanceDate"
						required>
				</div>
				<div class="form-row">
					<label>정비유형</label><select name="maintenanceType"><option
							value="정기점검">정기점검</option>
						<option value="예방정비">예방정비</option>
						<option value="고장정비">고장정비</option></select>
				</div>
				<div class="form-row">
					<label>다음정비일</label><input type="date" name="nextMaintenanceDate">
				</div>
				<div class="form-row">
					<label>상태</label><select name="status"><option value="정상">정상</option>
						<option value="점검중">점검중</option>
						<option value="고장">고장</option>
						<option value="수리완료">수리완료</option></select>
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
				document.querySelectorAll("input[name='maintenanceId']")
						.forEach(function(chk) {
							chk.checked = this.checked;
						});
			});
	(function() {
		const select = document.getElementById('maintenanceEquipmentId');
		if (!select)
			return;
		function sync() {
			const opt = select.options[select.selectedIndex];
			document.getElementById('maintenanceEquipmentCode').value = opt
					&& opt.value ? (opt.dataset.code || '') : '';
			document.getElementById('maintenanceEquipmentName').value = opt
					&& opt.value ? (opt.dataset.name || '') : '';
			document.getElementById('maintenanceModelName').value = opt
					&& opt.value ? (opt.dataset.model || '') : '';
			document.getElementById('maintenanceLocation').value = opt
					&& opt.value ? (opt.dataset.location || '') : '';
		}
		select.addEventListener('change', sync);
	})();
</script>
