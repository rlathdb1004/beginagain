<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<c:set var="isMesAdmin"
	value="${sessionScope.loginUser.roleName eq 'MES_ADMIN'}" />

<div class="page-title">품목 상세</div>

<form id="itemUpdateForm" method="post"
	action="${pageContext.request.contextPath}/item/update">

	<input type="hidden" name="itemId" value="${item.itemId}">

	<div class="table-box">
		<table class="ta">
			<tbody>
				<tr>
					<th>품목코드</th>
					<td><input type="text" name="itemCode"
						value="${item.itemCode}" readonly></td>
				</tr>

				<tr>
					<th>품목명</th>
					<td><input type="text" name="itemName"
						value="${item.itemName}" class="editable-field" readonly>
					</td>
				</tr>

				<tr>
					<th>품목유형</th>
					<td><select name="itemType" class="editable-field" disabled>
							<option value="완제품" ${item.itemType eq '완제품' ? 'selected' : ''}>완제품</option>
							<option value="원자재" ${item.itemType eq '원자재' ? 'selected' : ''}>원자재</option>
					</select></td>
				</tr>

				<tr>
					<th>단위</th>
					<td><input type="text" name="unit" value="${item.unit}"
						class="editable-field" readonly></td>
				</tr>
			</tbody>
		</table>
	</div>

	<!-- 🔥 버튼 영역 -->
	<div class="taPageActions">

		<c:if test="${isMesAdmin}">
			<button type="button" id="editBtn" class="taBtn taBtnPrimary">수정</button>
			<button type="submit" id="saveBtn" class="taBtn taBtnPrimary"
				style="display: none;">수정완료</button>
			<button type="button" id="cancelBtn" class="taBtn taBtnOutline"
				style="display: none;">취소</button>
		</c:if>

		<a href="${pageContext.request.contextPath}/item/list"
			class="taBtn taBtnOutline">목록</a>
	</div>

</form>

<script>
const editBtn = document.getElementById("editBtn");
const saveBtn = document.getElementById("saveBtn");
const cancelBtn = document.getElementById("cancelBtn");

const fields = document.querySelectorAll(".editable-field");

// 🔥 버튼 없는 경우 (CEO)
if (!editBtn || !saveBtn || !cancelBtn) {
    fields.forEach(field => {
        if (field.tagName === 'SELECT') {
            field.disabled = true;
        } else {
            field.readOnly = true;
        }
    });
}

// 수정 모드
if (editBtn) {
    editBtn.addEventListener("click", function () {
        fields.forEach(field => {
            if (field.tagName === 'SELECT') {
                field.disabled = false;
            } else {
                field.readOnly = false;
            }
        });

        editBtn.style.display = "none";
        saveBtn.style.display = "inline-block";
        cancelBtn.style.display = "inline-block";
    });

    cancelBtn.addEventListener("click", function () {
        location.reload();
    });
}
</script>