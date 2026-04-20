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
	<c:when test="${empty invRegInqDTO}">
		<div class="taFormShell taEmptyState">
			<p>조회된 재고 정보가 없습니다.</p>
			<div class="taPageActions"
				style="justify-content: center; margin-top: 16px;">
				<a class="taBtn taBtnOutline"
					href="${pageContext.request.contextPath}/invRegInq"
					style="text-decoration: none;">목록</a>
			</div>
		</div>
	</c:when>

	<c:otherwise>
		<form id="inventoryUpdateForm"
			action="${pageContext.request.contextPath}/invRegInq" method="post">
			<input type="hidden" name="cmd" value="update"> <input
				type="hidden" name="inventoryId" value="${invRegInqDTO.inventoryId}">

			<div class="taPageActions">
				<c:if test="${isManager}">
					<button type="button" id="inventoryEditBtn"
						class="taBtn taBtnPrimary">수정</button>
					<button type="submit" id="inventorySaveBtn"
						class="taBtn taBtnPrimary" style="display: none;">수정완료</button>
					<button type="button" id="inventoryCancelBtn"
						class="taBtn taBtnOutline" style="display: none;">취소</button>
				</c:if>

				<a class="taBtn taBtnOutline"
					href="${pageContext.request.contextPath}/invRegInq"
					style="text-decoration: none;">목록</a>
			</div>

			<div class="taFormShell">
				<table class="taFormTable">
					<tr>
						<th class="taFormLabel">재고번호</th>
						<td class="taFormValue"><span class="taReadonlyText">${invRegInqDTO.inventoryId}</span></td>
					</tr>
					<tr>
						<th class="taFormLabel">품목코드</th>
						<td class="taFormValue"><span class="taReadonlyText">${invRegInqDTO.itemCode}</span></td>
					</tr>
					<tr>
						<th class="taFormLabel">품목명</th>
						<td class="taFormValue"><span class="taReadonlyText">${invRegInqDTO.itemName}</span></td>
					</tr>
					<tr>
						<th class="taFormLabel">품목 유형</th>
						<td class="taFormValue"><span class="taReadonlyText">${invRegInqDTO.itemType}</span></td>
					</tr>
					<tr>
						<th class="taFormLabel">현재재고</th>
						<td class="taFormValue"><span class="taReadonlyText">${invRegInqDTO.qtyOnHand}</span></td>
					</tr>
					<tr>
						<th class="taFormLabel">안전재고</th>
						<td class="taFormValue"><input
							class="taFormInput taEditableField" type="number" step="0.001"
							min="0" name="safetyStock" value="${invRegInqDTO.safetyStock}"></td>
					</tr>
					<tr>
						<th class="taFormLabel">단위</th>
						<td class="taFormValue"><span class="taReadonlyText">${invRegInqDTO.unit}</span></td>
					</tr>
					<tr>
						<th class="taFormLabel">상태</th>
						<td class="taFormValue"><span class="taReadonlyText">${invRegInqDTO.inventoryStatus}</span></td>
					</tr>
					<tr>
						<th class="taFormLabel">생성일</th>
						<td class="taFormValue"><span class="taReadonlyText">${invRegInqDTO.createdAt}</span></td>
					</tr>
					<tr>
						<th class="taFormLabel">수정일</th>
						<td class="taFormValue"><span class="taReadonlyText">${invRegInqDTO.updatedAt}</span></td>
					</tr>
					<tr>
						<th class="taFormLabel">비고</th>
						<td class="taFormValue"><textarea
								class="taFormTextarea taEditableField" name="remark">${invRegInqDTO.remark}</textarea></td>
					</tr>
				</table>
			</div>
		</form>

		<script>
			(function() {
				const f = document.getElementById('inventoryUpdateForm');
				if (!f)
					return;

				const e = document.getElementById('inventoryEditBtn');
				const s = document.getElementById('inventorySaveBtn');
				const c = document.getElementById('inventoryCancelBtn');
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