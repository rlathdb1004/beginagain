<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<c:choose>
	<c:when test="${empty workStatus}">
		<div class="taFormShell taEmptyState">
			<p>조회된 작업 현황 정보가 없습니다.</p>
			<div class="taPageActions"
				style="justify-content: center; margin-top: 16px;">
				<a class="taBtn taBtnOutline"
					href="${pageContext.request.contextPath}/workstatus"
					style="text-decoration: none;">목록</a>
			</div>
		</div>
	</c:when>

	<c:otherwise>
		<div class="taSectionStack">

			<div class="taPageActions">
				<a class="taBtn taBtnOutline"
					href="${pageContext.request.contextPath}/workstatus"
					style="text-decoration: none;">목록</a> 
			</div>

			<div class="taFormShell">
				<table class="taFormTable">
					<tr>
						<th class="taFormLabel">작업지시번호</th>
						<td class="taFormValue"><span class="taReadonlyText">${workStatus.workOrderId}</span>
						</td>
					</tr>
					<tr>
						<th class="taFormLabel">작업지시코드</th>
						<td class="taFormValue"><span class="taReadonlyText">${workStatus.workOrderDisplayCode}</span>
						</td>
					</tr>
					<tr>
						<th class="taFormLabel">품목코드</th>
						<td class="taFormValue"><span class="taReadonlyText">${workStatus.itemCode}</span>
						</td>
					</tr>
					<tr>
						<th class="taFormLabel">품목명</th>
						<td class="taFormValue"><span class="taReadonlyText">${workStatus.itemName}</span>
						</td>
					</tr>
					<tr>
						<th class="taFormLabel">담당자</th>
						<td class="taFormValue"><span class="taReadonlyText">${workStatus.empName}</span>
						</td>
					</tr>
					<tr>
						<th class="taFormLabel">지시일</th>
						<td class="taFormValue"><span class="taReadonlyText">${workStatus.workDate}</span>
						</td>
					</tr>
					<tr>
						<th class="taFormLabel">계획수량</th>
						<td class="taFormValue"><span class="taReadonlyText">
								<fmt:formatNumber value="${workStatus.workQty}"
									pattern="#,##0.###" />
						</span></td>
					</tr>
					<tr>
						<th class="taFormLabel">실적수량</th>
						<td class="taFormValue"><span class="taReadonlyText">
								<fmt:formatNumber value="${workStatus.producedQty}"
									pattern="#,##0.###" />
						</span></td>
					</tr>
					<tr>
						<th class="taFormLabel">손실수량</th>
						<td class="taFormValue"><span class="taReadonlyText">
								<fmt:formatNumber value="${workStatus.lossQty}"
									pattern="#,##0.###" />
						</span></td>
					</tr>
					<tr>
						<th class="taFormLabel">진행률</th>
						<td class="taFormValue"><span class="taReadonlyText">
								<fmt:formatNumber value="${workStatus.progressRate}"
									pattern="0.0" />%
						</span></td>
					</tr>
					<tr>
						<th class="taFormLabel">진행상태</th>
						<td class="taFormValue"><span class="taReadonlyText">${workStatus.progressStatus}</span>
						</td>
					</tr>
					<tr>
						<th class="taFormLabel">일정상태</th>
						<td class="taFormValue"><span class="taReadonlyText">${workStatus.scheduleStatus}</span>
						</td>
					</tr>
					<tr>
						<th class="taFormLabel">최종실적일</th>
						<td class="taFormValue"><span class="taReadonlyText">
								<c:choose>
									<c:when test="${not empty workStatus.lastResultDate}">
										${workStatus.lastResultDate}
									</c:when>
									<c:otherwise>-</c:otherwise>
								</c:choose>
						</span></td>
					</tr>
					<tr>
						<th class="taFormLabel">비고</th>
						<td class="taFormValue"><span class="taReadonlyText">
								<c:choose>
									<c:when test="${not empty workStatus.remark}">
										${workStatus.remark}
									</c:when>
									<c:otherwise>-</c:otherwise>
								</c:choose>
						</span></td>
					</tr>
				</table>
			</div>

		</div>
	</c:otherwise>
</c:choose>