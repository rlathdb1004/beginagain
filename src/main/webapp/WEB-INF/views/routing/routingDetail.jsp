<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<c:set var="isMesAdmin"
	value="${sessionScope.loginUser.roleName eq 'MES_ADMIN'}" />

<div class="page-title">라우팅 상세</div>

<form id="routingUpdateForm" method="post"
	action="${pageContext.request.contextPath}/routing/update">

	<input type="hidden" name="routingId" value="${routing.routingId}">
	<input type="hidden" name="itemId" value="${routing.itemId}">

	<div class="table-box">
		<table class="ta">
			<tbody>
				<tr>
					<th>공정순서</th>
					<td><input type="number" name="sequence"
						value="${routing.sequence}" class="editable-field" readonly>
					</td>
				</tr>

				<tr>
					<th>공정</th>
					<td><select name="processId" class="editable-field" disabled>
							<c:forEach var="p" items="${processList}">
								<option value="${p.processId}"
									${p.processId == routing.processId ? 'selected' : ''}>
									${p.processName}</option>
							</c:forEach>
					</select></td>
				</tr>

				<tr>
					<th>설비</th>
					<td><select name="equipmentId" class="editable-field" disabled>
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

	<!-- 🔥 버튼 영역 -->
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
			class="taBtn taBtnOutline">목록</a>
	</div>

</form>

<script>
const editBtn = document.getElementById("editBtn");
const saveBtn = document.getElementById("saveBtn");
const cancelBtn = document.getElementById("cancelBtn");

const fields = document.querySelectorAll(".editable-field");

// 🔥 CEO 대응 (버튼 없음)
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