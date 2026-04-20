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
	<c:when test="${empty matInspRegInqDTO}">
		<div class="taFormShell taEmptyState">
			<p>조회된 자재 검사 정보가 없습니다.</p>
			<div class="taPageActions"
				style="justify-content: center; margin-top: 16px;">
				<a class="taBtn taBtnOutline"
					href="${pageContext.request.contextPath}/matInspRegInq"
					style="text-decoration: none;">목록</a>
			</div>
		</div>
	</c:when>

	<c:otherwise>
		<form id="matInspUpdateForm"
			action="${pageContext.request.contextPath}/matInspRegInq"
			method="post">
			<input type="hidden" name="cmd" value="update"> <input
				type="hidden" name="materialInspectionId"
				value="${matInspRegInqDTO.materialInspectionId}">

			<div class="taPageActions">
				<c:if test="${isManager}">
					<button type="button" id="matInspEditBtn"
						class="taBtn taBtnPrimary">수정</button>
					<button type="submit" id="matInspSaveBtn"
						class="taBtn taBtnPrimary" style="display: none;">수정완료</button>
					<button type="button" id="matInspCancelBtn"
						class="taBtn taBtnOutline" style="display: none;">취소</button>
				</c:if>

				<a class="taBtn taBtnOutline"
					href="${pageContext.request.contextPath}/matInspRegInq"
					style="text-decoration: none;">목록</a>
			</div>

			<div class="taFormShell">
				<table class="taFormTable">
					<tr>
						<th class="taFormLabel">자재검사번호</th>
						<td class="taFormValue"><span class="taReadonlyText">${matInspRegInqDTO.materialInspectionId}</span></td>
					</tr>
					<tr>
						<th class="taFormLabel">품목코드</th>
						<td class="taFormValue"><span class="taReadonlyText">${matInspRegInqDTO.itemCode}</span></td>
					</tr>
					<tr>
						<th class="taFormLabel">품목명</th>
						<td class="taFormValue"><span class="taReadonlyText">${matInspRegInqDTO.itemName}</span></td>
					</tr>
					<tr>
						<th class="taFormLabel">단위</th>
						<td class="taFormValue"><span class="taReadonlyText">${matInspRegInqDTO.unit}</span></td>
					</tr>
					<tr>
						<th class="taFormLabel">검사자</th>
						<td class="taFormValue"><span class="taReadonlyText">${matInspRegInqDTO.empName}</span></td>
					</tr>
					<tr>
						<th class="taFormLabel">검사수량</th>
						<td class="taFormValue"><input
							class="taFormInput taEditableField" type="number" step="0.001"
							min="0.001" name="inspectQty"
							value="${matInspRegInqDTO.inspectQty}"></td>
					</tr>
					<tr>
						<th class="taFormLabel">양품수량</th>
						<td class="taFormValue"><input
							class="taFormInput taEditableField" type="number" step="0.001"
							min="0" name="goodQty" value="${matInspRegInqDTO.goodQty}"></td>
					</tr>
					<tr>
						<th class="taFormLabel">불량수량</th>
						<td class="taFormValue"><input
							class="taFormInput taEditableField" type="number" step="0.001"
							min="0" name="defectQty" value="${matInspRegInqDTO.defectQty}"></td>
					</tr>
					<tr>
						<th class="taFormLabel">판정</th>
						<td class="taFormValue">
							<input type="hidden" name="result" value="${matInspRegInqDTO.result}">
							<input class="taFormInput" type="text" value="${matInspRegInqDTO.result}" readonly>
							<div style="margin-top:8px; color:#6F7B8D; font-size:13px;">불량률 5% 이하 합격, 초과 시 불합격으로 자동 판정됩니다.</div>
						</td>
					</tr>
					<tr>
						<th class="taFormLabel">검사일</th>
						<td class="taFormValue"><input
							class="taFormInput taEditableField" type="date"
							name="inspectionDate" value="${matInspRegInqDTO.inspectionDate}"></td>
					</tr>
					<tr>
						<th class="taFormLabel">비고</th>
						<td class="taFormValue"><textarea
								class="taFormTextarea taEditableField" name="remark">${matInspRegInqDTO.remark}</textarea></td>
					</tr>
					<tr>
						<th class="taFormLabel">생성일</th>
						<td class="taFormValue"><span class="taReadonlyText">${matInspRegInqDTO.createdAt}</span></td>
					</tr>
					<tr>
						<th class="taFormLabel">수정일</th>
						<td class="taFormValue"><span class="taReadonlyText">${matInspRegInqDTO.updatedAt}</span></td>
					</tr>
				</table>
			</div>
		</form>

		<script>
			(function() {
				const f = document.getElementById('matInspUpdateForm');
				if (!f)
					return;

				const e = document.getElementById('matInspEditBtn');
				const s = document.getElementById('matInspSaveBtn');
				const c = document.getElementById('matInspCancelBtn');
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