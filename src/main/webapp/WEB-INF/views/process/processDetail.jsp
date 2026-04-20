<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<c:set var="isMesAdmin"
	value="${sessionScope.loginUser.roleName eq 'MES_ADMIN'}" />

<div class="page-title">공정 상세</div>

<form id="processUpdateForm" method="post"
	action="${pageContext.request.contextPath}/process/update">
	<input type="hidden" name="processId" value="${p.processId}">

	<div class="table-box">
		<table class="ta">
			<tbody>
				<tr>
					<th>공정코드</th>
					<td><input type="text" name="processCode"
						value="${p.processCode}" readonly></td>
				</tr>

				<tr>
					<th>공정명</th>
					<td><input type="text" name="processName"
						value="${p.processName}" class="editable-field" readonly>
					</td>
				</tr>

				<tr>
					<th>설명</th>
					<td><input type="text" name="description"
						value="${p.description}" class="editable-field" readonly>
					</td>
				</tr>
			</tbody>
		</table>
	</div>

	<div class="taPageActions">
		<c:if test="${isMesAdmin}">
			<button type="button" id="editBtn" class="taBtn taBtnPrimary">수정</button>
			<button type="submit" id="saveBtn" class="taBtn taBtnPrimary"
				style="display: none;">수정완료</button>
			<button type="button" id="cancelBtn" class="taBtn taBtnOutline"
				style="display: none;">취소</button>
		</c:if>

		<a href="${pageContext.request.contextPath}/process/list"
			class="taBtn taBtnOutline">목록</a>
	</div>
</form>

<script>
	const editBtn = document.getElementById("editBtn");
	const saveBtn = document.getElementById("saveBtn");
	const cancelBtn = document.getElementById("cancelBtn");
	const fields = document.querySelectorAll(".editable-field");

	if (!editBtn || !saveBtn || !cancelBtn) {
		fields.forEach(function(field) {
			field.readOnly = true;
		});
	} else {
		editBtn.addEventListener("click", function() {
			fields.forEach(function(field) {
				field.readOnly = false;
			});

			editBtn.style.display = "none";
			saveBtn.style.display = "inline-block";
			cancelBtn.style.display = "inline-block";
		});

		cancelBtn.addEventListener("click", function() {
			location.reload();
		});
	}
</script>