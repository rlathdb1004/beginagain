<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<c:set var="isMesAdmin"
	value="${sessionScope.loginUser.roleName eq 'MES_ADMIN'}" />

<c:if test="${not empty errorMessage}">
	<script>
		alert('${errorMessage}');
	</script>
</c:if>

<div class="page-title">라우팅 상세</div>

<form id="routingUpdateForm" method="post"
	action="${pageContext.request.contextPath}/routing/update">

	<input type="hidden" name="routingId" value="${routing.routingId}">
	<input type="hidden" name="itemId" value="${routing.itemId}">

	<div class="taPageActions">
		<c:if test="${isMesAdmin}">
			<button type="button" id="editBtn" class="taBtn taBtnPrimary">수정</button>
			<button type="submit" id="saveBtn" class="taBtn taBtnPrimary"
				style="display: none;">수정완료</button>
			<button type="button" id="cancelBtn" class="taBtn taBtnOutline"
				style="display: none;">취소</button>
		</c:if>

		<a
			href="${pageContext.request.contextPath}/routing/list?itemId=${routing.itemId}"
			class="taBtn taBtnOutline" style="text-decoration: none;">목록</a>
	</div>

	<div class="taFormShell">
		<table class="taFormTable">
			<tbody>
				<tr>
					<th class="taFormLabel">품목코드</th>
					<td class="taFormValue"><span class="taReadonlyText">${routing.itemCode}</span></td>
				</tr>
				<tr>
					<th class="taFormLabel">품목명</th>
					<td class="taFormValue"><span class="taReadonlyText">${routing.itemName}</span></td>
				</tr>
				<tr>
					<th class="taFormLabel">공정순서</th>
					<td class="taFormValue"><input type="number" name="processSeq"
						value="${routing.processSeq}" class="taFormInput taEditableField"></td>
				</tr>

				<tr>
					<th class="taFormLabel">공정</th>
					<td class="taFormValue"><select name="processId" class="taFormInput taEditableField">
							<c:forEach var="p" items="${processList}">
								<option value="${p.processId}"
									${p.processId == routing.processId ? 'selected' : ''}>
									${p.processName}</option>
							</c:forEach>
					</select></td>
				</tr>

				<tr>
					<th class="taFormLabel">설비</th>
					<td class="taFormValue"><select name="equipmentId" class="taFormInput taEditableField">
							<c:forEach var="e" items="${equipmentList}">
								<option value="${e.equipmentId}"
									${e.equipmentId == routing.equipmentId ? 'selected' : ''}>
									${e.equipmentName}</option>
							</c:forEach>
					</select></td>
				</tr>
			</tbody>
		</table>
	</div>
</form>

<script>
(function() {
    const form = document.getElementById("routingUpdateForm");
    const editBtn = document.getElementById("editBtn");
    const saveBtn = document.getElementById("saveBtn");
    const cancelBtn = document.getElementById("cancelBtn");
    const fields = form ? form.querySelectorAll(".taEditableField") : [];

    function viewMode() {
        fields.forEach(field => {
            if (field.tagName === 'SELECT') {
                field.disabled = true;
            } else {
                field.readOnly = true;
            }
        });
        if (editBtn) editBtn.style.display = "";
        if (saveBtn) saveBtn.style.display = "none";
        if (cancelBtn) cancelBtn.style.display = "none";
    }

    function editMode() {
        fields.forEach(field => {
            if (field.tagName === 'SELECT') {
                field.disabled = false;
            } else {
                field.readOnly = false;
            }
        });
        if (editBtn) editBtn.style.display = "none";
        if (saveBtn) saveBtn.style.display = "";
        if (cancelBtn) cancelBtn.style.display = "";
    }

    fields.forEach(field => {
        field.dataset.originalValue = field.value;
    });

    if (editBtn && saveBtn && cancelBtn) {
        editBtn.addEventListener("click", editMode);

        cancelBtn.addEventListener("click", function() {
            fields.forEach(field => {
                field.value = field.dataset.originalValue || "";
            });
            viewMode();
        });

        form.addEventListener("submit", function(e) {
            if (saveBtn.style.display === "none") {
                e.preventDefault();
                return;
            }
            fields.forEach(field => {
                if (field.tagName === 'SELECT') {
                    field.disabled = false;
                }
            });
            if (!confirm('수정하시겠습니까?')) {
                e.preventDefault();
            }
        });
    }

    viewMode();
})();
</script>
