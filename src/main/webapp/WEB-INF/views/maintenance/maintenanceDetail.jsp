<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<c:set var="isManager"
	value="${sessionScope.loginUser.roleName eq 'MES_ADMIN' or sessionScope.loginUser.roleName eq 'SITE_MANAGER'}" />

<c:if test="${not empty errorMsg}">
	<script>
		alert('${errorMsg}');
	</script>
</c:if>

<c:choose>
	<c:when test="${empty maintenance}">
		<div class="taFormShell taEmptyState">
			<p>조회된 정비이력 정보가 없습니다.</p>
			<div class="taPageActions"
				style="justify-content: center; margin-top: 16px;">
				<a class="taBtn taBtnOutline"
					href="${pageContext.request.contextPath}/maintenance/list"
					style="text-decoration: none;">목록</a>
			</div>
		</div>
	</c:when>

	<c:otherwise>
		<form id="maintenanceUpdateForm"
			action="${pageContext.request.contextPath}/maintenance/update"
			method="post">
			<input type="hidden" name="maintenanceId"
				value="${maintenance.maintenanceId}"> <input type="hidden"
				name="equipmentId" value="${maintenance.equipmentId}">

			<div class="taPageActions">
				<c:if test="${isManager}">
					<button type="button" id="maintenanceUpdateFormEditBtn"
						class="taBtn taBtnPrimary">수정</button>
					<button type="submit" id="maintenanceUpdateFormSaveBtn"
						class="taBtn taBtnPrimary" style="display: none;">수정완료</button>
					<button type="button" id="maintenanceUpdateFormCancelBtn"
						class="taBtn taBtnOutline" style="display: none;">취소</button>
				</c:if>

				<a class="taBtn taBtnOutline"
					href="${pageContext.request.contextPath}/maintenance/list"
					style="text-decoration: none;">목록</a>
			</div>

			<div class="taFormShell">
				<table class="taFormTable">
					<tr>
						<th class="taFormLabel">정비번호</th>
						<td class="taFormValue"><span class="taReadonlyText">${maintenance.maintenanceId}</span></td>
					</tr>
					<tr>
						<th class="taFormLabel">설비번호</th>
						<td class="taFormValue"><span class="taReadonlyText">${maintenance.equipmentId}</span></td>
					</tr>
					<tr>
						<th class="taFormLabel">설비코드</th>
						<td class="taFormValue"><span class="taReadonlyText">${maintenance.equipmentCode}</span></td>
					</tr>
					<tr>
						<th class="taFormLabel">설비명</th>
						<td class="taFormValue"><span class="taReadonlyText">${maintenance.equipmentName}</span></td>
					</tr>
					<tr>
						<th class="taFormLabel">모델명</th>
						<td class="taFormValue"><span class="taReadonlyText">${maintenance.modelName}</span></td>
					</tr>
					<tr>
						<th class="taFormLabel">위치</th>
						<td class="taFormValue"><span class="taReadonlyText">${maintenance.location}</span></td>
					</tr>
					<tr>
						<th class="taFormLabel">정비일자</th>
						<td class="taFormValue"><input
							class="taFormInput taEditableField" type="date"
							name="maintenanceDate" value="${maintenance.maintenanceDate}"></td>
					</tr>
					<tr>
						<th class="taFormLabel">정비유형</th>
						<td class="taFormValue"><select
							class="taFormInput taEditableField" name="maintenanceType"><option
									value="정기점검"
									${maintenance.maintenanceType eq '정기점검' ? 'selected' : ''}>정기점검</option>
								<option value="예방정비"
									${maintenance.maintenanceType eq '예방정비' ? 'selected' : ''}>예방정비</option>
								<option value="고장정비"
									${maintenance.maintenanceType eq '고장정비' ? 'selected' : ''}>고장정비</option></select></td>
					</tr>
					<tr>
						<th class="taFormLabel">다음정비일</th>
						<td class="taFormValue"><input
							class="taFormInput taEditableField" type="date"
							name="nextMaintenanceDate"
							value="${maintenance.nextMaintenanceDate}"></td>
					</tr>
					<tr>
						<th class="taFormLabel">상태</th>
						<td class="taFormValue"><select
							class="taFormInput taEditableField" name="status"><option
									value="정상" ${maintenance.status eq '정상' ? 'selected' : ''}>정상</option>
								<option value="점검중"
									${maintenance.status eq '점검중' ? 'selected' : ''}>점검중</option>
								<option value="고장"
									${maintenance.status eq '고장' ? 'selected' : ''}>고장</option>
								<option value="수리완료"
									${maintenance.status eq '수리완료' ? 'selected' : ''}>수리완료</option></select></td>
					</tr>
					<tr>
						<th class="taFormLabel">정비내용</th>
						<td class="taFormValue"><textarea
								class="taFormTextarea taEditableField" name="maintenanceContent">${maintenance.maintenanceContent}</textarea></td>
					</tr>
					<tr>
						<th class="taFormLabel">비고</th>
						<td class="taFormValue"><textarea
								class="taFormTextarea taEditableField" name="remark">${maintenance.remark}</textarea></td>
					</tr>
				</table>
			</div>
		</form>

		<script>
			(function() {
				const f = document.getElementById('maintenanceUpdateForm');
				if (!f)
					return;

				const e = document
						.getElementById('maintenanceUpdateFormEditBtn');
				const s = document
						.getElementById('maintenanceUpdateFormSaveBtn');
				const c = document
						.getElementById('maintenanceUpdateFormCancelBtn');
				const fields = f.querySelectorAll('.taEditableField');

				function view() {
					fields.forEach(function(x) {
						if (x.tagName === 'SELECT')
							x.disabled = true;
						else
							x.readOnly = true;
					});
					if (e)
						e.style.display = '';
					if (s)
						s.style.display = 'none';
					if (c)
						c.style.display = 'none';
				}

				function edit() {
					fields.forEach(function(x) {
						if (x.tagName === 'SELECT')
							x.disabled = false;
						else
							x.readOnly = false;
					});
					if (e)
						e.style.display = 'none';
					if (s)
						s.style.display = '';
					if (c)
						c.style.display = '';
				}

				fields.forEach(function(x) {
					x.dataset.originalValue = x.value;
				});

				if (e && s && c) {
					e.addEventListener('click', edit);

					c.addEventListener('click', function() {
						fields.forEach(function(x) {
							x.value = x.dataset.originalValue || '';
						});
						view();
					});

					f.addEventListener('submit', function(ev) {
						if (s.style.display === 'none') {
							ev.preventDefault();
							return;
						}
						fields.forEach(function(x) {
							if (x.tagName === 'SELECT')
								x.disabled = false;
						});
						if (!confirm('수정하시겠습니까?'))
							ev.preventDefault();
					});
				}

				view();
			})();
		</script>
	</c:otherwise>
</c:choose>