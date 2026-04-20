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
	<c:when test="${empty defectRegInqDTO}">
		<div class="taFormShell taEmptyState">
			<p>조회된 불량 정보가 없습니다.</p>
			<div class="taPageActions"
				style="justify-content: center; margin-top: 16px;">
				<a class="taBtn taBtnOutline"
					href="${pageContext.request.contextPath}/defectRegInq"
					style="text-decoration: none;">목록</a>
			</div>
		</div>
	</c:when>

	<c:otherwise>
		<form id="defectUpdateForm"
			action="${pageContext.request.contextPath}/defectRegInq"
			method="post">
			<input type="hidden" name="cmd" value="update"> <input
				type="hidden" name="defectProductId"
				value="${defectRegInqDTO.defectProductId}">

			<div class="taPageActions">
				<c:if test="${isManager}">
					<button type="button" id="defectEditBtn" class="taBtn taBtnPrimary">수정</button>
					<button type="submit" id="defectSaveBtn" class="taBtn taBtnPrimary"
						style="display: none;">수정완료</button>
					<button type="button" id="defectCancelBtn"
						class="taBtn taBtnOutline" style="display: none;">취소</button>
				</c:if>

				<a class="taBtn taBtnOutline"
					href="${pageContext.request.contextPath}/defectRegInq"
					style="text-decoration: none;">목록</a>
			</div>

			<div class="taFormShell">
				<table class="taFormTable">
					<tr>
						<th class="taFormLabel">불량번호</th>
						<td class="taFormValue"><span class="taReadonlyText">${defectRegInqDTO.defectProductId}</span></td>
					</tr>
					<tr>
						<th class="taFormLabel">완제품검사번호</th>
						<td class="taFormValue"><span class="taReadonlyText">${defectRegInqDTO.finalInspectionId}</span></td>
					</tr>
					<tr>
						<th class="taFormLabel">생산실적번호</th>
						<td class="taFormValue"><span class="taReadonlyText">${defectRegInqDTO.resultId}</span></td>
					</tr>
					<tr>
						<th class="taFormLabel">작업지시번호</th>
						<td class="taFormValue"><span class="taReadonlyText">${defectRegInqDTO.workOrderId}</span></td>
					</tr>
					<tr>
						<th class="taFormLabel">품목코드</th>
						<td class="taFormValue"><span class="taReadonlyText">${defectRegInqDTO.itemCode}</span></td>
					</tr>
					<tr>
						<th class="taFormLabel">품목명</th>
						<td class="taFormValue"><span class="taReadonlyText">${defectRegInqDTO.itemName}</span></td>
					</tr>
					<tr>
						<th class="taFormLabel">LOT번호</th>
						<td class="taFormValue"><span class="taReadonlyText">${defectRegInqDTO.lotNo}</span></td>
					</tr>
					<tr>
						<th class="taFormLabel">검사수량(참고)</th>
						<td class="taFormValue"><span class="taReadonlyText">${defectRegInqDTO.inspectQty}</span></td>
					</tr>
					<tr>
						<th class="taFormLabel">검사판정</th>
						<td class="taFormValue"><span class="taReadonlyText">${defectRegInqDTO.inspectionStatus}</span></td>
					</tr>
					<tr>
						<th class="taFormLabel">불량코드</th>
						<td class="taFormValue"><span class="taReadonlyText">${defectRegInqDTO.defectCode}</span></td>
					</tr>
					<tr>
						<th class="taFormLabel">불량명</th>
						<td class="taFormValue"><span class="taReadonlyText">${defectRegInqDTO.defectName}</span></td>
					</tr>
					<tr>
						<th class="taFormLabel">유형</th>
						<td class="taFormValue"><span class="taReadonlyText">${defectRegInqDTO.defectType}</span></td>
					</tr>
					<tr>
						<th class="taFormLabel">등록된 불량코드 수</th>
						<td class="taFormValue"><span class="taReadonlyText">${defectRegInqDTO.defectCodeCount}</span></td>
					</tr>
					<tr>
						<th class="taFormLabel">비고</th>
						<td class="taFormValue"><textarea
								class="taFormTextarea taEditableField" name="remark">${defectRegInqDTO.remark}</textarea></td>
					</tr>
				</table>
			</div>
		</form>

		<script>
			(function() {
				const f = document.getElementById('defectUpdateForm');
				if (!f)
					return;

				const e = document.getElementById('defectEditBtn');
				const s = document.getElementById('defectSaveBtn');
				const c = document.getElementById('defectCancelBtn');
				const fields = f.querySelectorAll('.taEditableField');

				function view() {
					fields.forEach(function(x) {
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
						if (!confirm('수정하시겠습니까?'))
							ev.preventDefault();
					});
				}

				view();
			})();
		</script>
	</c:otherwise>
</c:choose>