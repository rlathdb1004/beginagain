<%@ page contentType="text/html; charset=UTF-8"%>

<form action="${pageContext.request.contextPath}/failureaction/register"
	method="post">
	<input type="hidden" name="maintenanceId" value="${maintenanceId}">

	<div class="taPageActions">
		<button type="submit" class="taBtn taBtnPrimary">등록</button>
		<a class="taBtn taBtnOutline"
			href="${pageContext.request.contextPath}/maintenance/detail?maintenanceId=${maintenanceId}"
			style="text-decoration: none;">취소</a>
	</div>

	<div class="taFormShell">
		<table class="taFormTable">

			<tr>
				<th class="taFormLabel">정비번호</th>
				<td class="taFormValue"><span class="taReadonlyText">${maintenanceId}</span>
				</td>
			</tr>

			<tr>
				<th class="taFormLabel">고장일자</th>
				<td class="taFormValue"><input class="taFormInput" type="date"
					name="failureDate" required></td>
			</tr>

			<tr>
				<th class="taFormLabel">고장부위</th>
				<td class="taFormValue"><input class="taFormInput" type="text"
					name="failurePart" placeholder="예: 모터, 센서, 배선"></td>
			</tr>

			<tr>
				<th class="taFormLabel">고장내용</th>
				<td class="taFormValue"><textarea class="taFormTextarea"
						name="failureContent" placeholder="고장 내용을 입력하세요"></textarea></td>
			</tr>

			<tr>
				<th class="taFormLabel">원인</th>
				<td class="taFormValue"><textarea class="taFormTextarea"
						name="causeText" placeholder="고장 원인을 입력하세요"></textarea></td>
			</tr>

			<tr>
				<th class="taFormLabel">조치내용</th>
				<td class="taFormValue"><textarea class="taFormTextarea"
						name="actionText" placeholder="조치 내용을 입력하세요"></textarea></td>
			</tr>

			<tr>
				<th class="taFormLabel">조치일</th>
				<td class="taFormValue"><input class="taFormInput" type="date"
					name="actionDate"></td>
			</tr>

			<tr>
				<th class="taFormLabel">상태</th>
				<td class="taFormValue"><select class="taFormInput"
					name="status">
						<option value="접수">접수</option>
						<option value="진행중">진행중</option>
						<option value="완료">완료</option>
				</select></td>
			</tr>

		</table>
	</div>
</form>