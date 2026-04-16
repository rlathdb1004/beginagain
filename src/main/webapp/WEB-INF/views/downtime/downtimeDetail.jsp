<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<c:choose>
	<c:when test="${empty downtime}">
		<div class="taFormShell taEmptyState">
			<p>조회된 비가동 정보가 없습니다.</p>
			<div class="taPageActions"
				style="justify-content: center; margin-top: 16px;">
				<a class="taBtn taBtnOutline"
					href="${pageContext.request.contextPath}/downtime/list"
					style="text-decoration: none;">목록</a>
			</div>
		</div>
	</c:when>

	<c:otherwise>
		<div class="taPageActions">
			<a class="taBtn taBtnOutline"
				href="${pageContext.request.contextPath}/downtime/list"
				style="text-decoration: none;">목록</a> <a class="taBtn taBtnPrimary"
				href="${pageContext.request.contextPath}/failureaction/detail?failureActionId=${downtime.failureActionId}"
				style="text-decoration: none;">고장조치 상세</a>
		</div>

		<div class="taFormShell">
			<table class="taFormTable">
				<tr>
					<th class="taFormLabel">고장번호</th>
					<td class="taFormValue"><span class="taReadonlyText">${downtime.failureActionId}</span></td>
				</tr>
				<tr>
					<th class="taFormLabel">설비코드</th>
					<td class="taFormValue"><span class="taReadonlyText">${downtime.equipmentCode}</span></td>
				</tr>
				<tr>
					<th class="taFormLabel">설비명</th>
					<td class="taFormValue"><span class="taReadonlyText">${downtime.equipmentName}</span></td>
				</tr>
				<tr>
					<th class="taFormLabel">모델명</th>
					<td class="taFormValue"><span class="taReadonlyText">${downtime.modelName}</span></td>
				</tr>
				<tr>
					<th class="taFormLabel">설치위치</th>
					<td class="taFormValue"><span class="taReadonlyText">${downtime.location}</span></td>
				</tr>
				<tr>
					<th class="taFormLabel">연결 정비번호</th>
					<td class="taFormValue"><span class="taReadonlyText">${downtime.maintenanceId}</span></td>
				</tr>
				<tr>
					<th class="taFormLabel">정비유형</th>
					<td class="taFormValue"><span class="taReadonlyText">${downtime.maintenanceType}</span></td>
				</tr>
				<tr>
					<th class="taFormLabel">고장일자</th>
					<td class="taFormValue"><span class="taReadonlyText">${downtime.failureDate}</span></td>
				</tr>
				<tr>
					<th class="taFormLabel">고장부위</th>
					<td class="taFormValue"><span class="taReadonlyText">${downtime.failurePart}</span></td>
				</tr>
				<tr>
					<th class="taFormLabel">고장내용</th>
					<td class="taFormValue"><span class="taReadonlyText">${downtime.failureContent}</span></td>
				</tr>
				<tr>
					<th class="taFormLabel">원인</th>
					<td class="taFormValue"><span class="taReadonlyText">${downtime.causeText}</span></td>
				</tr>
				<tr>
					<th class="taFormLabel">조치내용</th>
					<td class="taFormValue"><span class="taReadonlyText">${downtime.actionText}</span></td>
				</tr>
				<tr>
					<th class="taFormLabel">조치일</th>
					<td class="taFormValue"><span class="taReadonlyText">${downtime.actionDate}</span></td>
				</tr>
				<tr>
					<th class="taFormLabel">상태</th>
					<td class="taFormValue"><span class="taReadonlyText">${downtime.status}</span></td>
				</tr>
				<tr>
					<th class="taFormLabel">비고</th>
					<td class="taFormValue"><span class="taReadonlyText"> <c:choose>
								<c:when test="${not empty downtime.remark}">
                                    ${downtime.remark}
                                </c:when>
								<c:otherwise>-</c:otherwise>
							</c:choose>
					</span></td>
				</tr>
			</table>
		</div>
	</c:otherwise>
</c:choose>