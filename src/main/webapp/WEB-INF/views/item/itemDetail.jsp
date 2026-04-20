<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<c:set var="isMesAdmin"
	value="${sessionScope.loginUser.roleName eq 'MES_ADMIN'}" />

<div class="page-title">품목 상세</div>

<form id="itemUpdateForm" method="post"
	action="${pageContext.request.contextPath}/item/update">

	<input type="hidden" name="itemId" value="${item.itemId}">

	<div class="taPageActions">
		<c:if test="${isMesAdmin}">
			<button type="button" id="editBtn" class="taBtn taBtnPrimary">수정</button>
			<button type="submit" id="saveBtn" class="taBtn taBtnPrimary"
				style="display: none;">수정완료</button>
			<button type="button" id="cancelBtn" class="taBtn taBtnOutline"
				style="display: none;">취소</button>
		</c:if>

		<a href="${pageContext.request.contextPath}/item/list"
			class="taBtn taBtnOutline" style="text-decoration: none;">목록</a>
	</div>

	<div class="taFormShell">
		<table class="taFormTable">
			<tbody>
				<tr>
					<th class="taFormLabel">품목코드</th>
					<td class="taFormValue"><span class="taReadonlyText">${item.itemCode}</span></td>
				</tr>

				<tr>
					<th class="taFormLabel">품목명</th>
					<td class="taFormValue"><input type="text" name="itemName"
						value="${item.itemName}" class="taFormInput taEditableField"></td>
				</tr>

				<tr>
					<th class="taFormLabel">품목유형</th>
					<td class="taFormValue"><select name="itemType" class="taFormInput taEditableField">
							<option value="완제품" ${item.itemType eq '완제품' ? 'selected' : ''}>완제품</option>
							<option value="원자재" ${item.itemType eq '원자재' ? 'selected' : ''}>원자재</option>
					</select></td>
				</tr>

				<tr>
					<th class="taFormLabel">단위</th>
					<td class="taFormValue"><input type="text" name="unit" value="${item.unit}"
						class="taFormInput taEditableField"></td>
				</tr>
			</tbody>
		</table>
	</div>
</form>

<script>
(function() {
    const form = document.getElementById("itemUpdateForm");
    const editBtn = document.getElementById("editBtn");
    const saveBtn = document.getElementById("saveBtn");
    const cancelBtn = document.getElementById("cancelBtn");
    const fields = form ? form.querySelectorAll(".taEditableField") : [];

    function viewMode() {
        fields.forEach(field => {
            if (field.tagName === 'SELECT') field.disabled = true;
            else field.readOnly = true;
        });
        if (editBtn) editBtn.style.display = "";
        if (saveBtn) saveBtn.style.display = "none";
        if (cancelBtn) cancelBtn.style.display = "none";
    }

    function editMode() {
        fields.forEach(field => {
            if (field.tagName === 'SELECT') field.disabled = false;
            else field.readOnly = false;
        });
        if (editBtn) editBtn.style.display = "none";
        if (saveBtn) saveBtn.style.display = "";
        if (cancelBtn) cancelBtn.style.display = "";
    }

    fields.forEach(field => field.dataset.originalValue = field.value);

    if (editBtn && saveBtn && cancelBtn) {
        editBtn.addEventListener("click", editMode);
        cancelBtn.addEventListener("click", function() {
            fields.forEach(field => field.value = field.dataset.originalValue || "");
            viewMode();
        });
    }

    viewMode();
})();
</script>
