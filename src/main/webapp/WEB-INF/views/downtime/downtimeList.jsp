<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!-- ===== 비가동 현황판 ===== -->
<div
	style="display: grid; grid-template-columns: repeat(4, minmax(180px, 1fr)); gap: 16px; margin-bottom: 20px;">
	<div class="taFormShell" style="padding: 20px;">
		<div style="font-size: 14px; color: #666; margin-bottom: 8px;">총
			비가동</div>
		<div style="font-size: 28px; font-weight: 700;">${totalDowntimeCount}건</div>
	</div>

	<div class="taFormShell" style="padding: 20px;">
		<div style="font-size: 14px; color: #666; margin-bottom: 8px;">비가동</div>
		<div style="font-size: 28px; font-weight: 700;">${inProgressCount}건</div>
	</div>

	<div class="taFormShell" style="padding: 20px;">
		<div style="font-size: 14px; color: #666; margin-bottom: 8px;">재가동</div>
		<div style="font-size: 28px; font-weight: 700;">${completedCount}건</div>
	</div>

	<div class="taFormShell" style="padding: 20px;">
		<div style="font-size: 14px; color: #666; margin-bottom: 8px;">금일
			발생</div>
		<div style="font-size: 28px; font-weight: 700;">${todayCount}건</div>
	</div>
</div>

<form id="paSearchForm" method="get"
	action="${pageContext.request.contextPath}/downtime/list">
	<input type="hidden" name="page" id="paPage" value="${paCurrentPage}">

	<div class="taToolbarRow">

		<!-- 1) 첫 검색창 -->
		<div class="taToolbarField taToolbarSpan3">
			<select
				class="taSelect taAutoSelectColor ${empty searchType or searchType eq 'all' ? 'taSelectPlaceholder' : ''}"
				name="searchType">
				<option value="" disabled hidden
					<c:if test="${empty searchType or searchType eq 'all'}">selected</c:if>>
					전체 / 설비코드 ...</option>
				<option value="all"
					<c:if test="${searchType eq 'all'}">selected</c:if>>전체</option>
				<option value="equipmentCode"
					<c:if test="${searchType eq 'equipmentCode'}">selected</c:if>>
					설비코드</option>
				<option value="equipmentName"
					<c:if test="${searchType eq 'equipmentName'}">selected</c:if>>
					설비명</option>
				<option value="failurePart"
					<c:if test="${searchType eq 'failurePart'}">selected</c:if>>
					고장부위</option>
				<option value="status"
					<c:if test="${searchType eq 'status'}">selected</c:if>>상태
				</option>
			</select>
		</div>

		<!-- 2) 기존 두 번째 검색창 -->
		<div class="taToolbarField taToolbarFieldGrow taToolbarSpan9">
			<div class="taSearchBox">
				<input type="text" class="taSearchInput" name="keyword"
					value="${keyword}" placeholder="검색키워드">

				<button type="submit" class="taSearchBtn" aria-label="검색"
					onclick="document.getElementById('paPage').value=1;">
					<svg viewBox="0 0 24 24" fill="none" stroke="currentColor"
						stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
						<circle cx="11" cy="11" r="7"></circle>
						<path d="M20 20L16.65 16.65"></path>
					</svg>
				</button>

				<button type="button" class="taBtn taBtnOutline taSearchReset"
					onclick="location.href='${pageContext.request.contextPath}/downtime/list'">
					초기화</button>
			</div>
		</div>

	</div>
</form>

<div class="taTableShell" id="paTableBox">
	<div class="taTableScroll">
		<table class="taMesTable">
			<thead>
				<tr>
					<th class="taTableHeadCell taColFit">고장번호</th>
					<th class="taTableHeadCell taColFit">설비코드</th>
					<th class="taTableHeadCell taColGrow">설비명</th>
					<th class="taTableHeadCell taColFit">고장일자</th>
					<th class="taTableHeadCell taColFit">고장부위</th>
					<th class="taTableHeadCell taColFit">상태</th>
					<th class="taTableHeadCell taColAction taLastCol">상세</th>
				</tr>
			</thead>
			<tbody>
				<c:choose>
					<c:when test="${not empty downtimeList}">
						<c:forEach var="d" items="${downtimeList}">
							<tr class="taTableBodyRow">
								<td class="taTableBodyCell taColFit">${d.failureActionId}</td>
								<td class="taTableBodyCell taColFit">${d.equipmentCode}</td>
								<td class="taTableBodyCell taColGrow">${d.equipmentName}</td>
								<td class="taTableBodyCell taColFit">${d.failureDate}</td>
								<td class="taTableBodyCell taColFit">${d.failurePart}</td>
								<td class="taTableBodyCell taColFit"><span>${d.status}</span>
								</td>
								<td class="taTableBodyCell taColAction taLastCol"><a
									class="taLinkAnchor"
									href="${pageContext.request.contextPath}/downtime/detail?failureActionId=${d.failureActionId}">
										상세보기 </a></td>
							</tr>
						</c:forEach>
					</c:when>

					<c:otherwise>
						<tr class="taTableBodyRow">
							<td class="taTableBodyCell taLastCol" colspan="7"
								style="text-align: center;">조회된 비가동 현황이 없습니다.</td>
						</tr>
					</c:otherwise>
				</c:choose>
			</tbody>
		</table>
	</div>
</div>