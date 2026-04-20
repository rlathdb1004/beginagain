<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<c:if test="${not empty errorMsg}">
	<script>
		alert('${errorMsg}');
	</script>
</c:if>

<form id="paSearchForm" method="post"
	action="${pageContext.request.contextPath}/invRegInq">
	<input type="hidden" name="cmd" value="list"> <input
		type="hidden" name="page" id="paPage" value="${paCurrentPage}">

	<div class="taToolbarRow">
		<div class="taToolbarField taToolbarSpan2">
			<select
				class="taSelect taAutoSelectColor ${empty invRegInqSearchDTO.searchType ? 'taSelectPlaceholder' : ''}"
				name="searchType">
				<option value="" disabled hidden
					<c:if test="${empty invRegInqSearchDTO.searchType}">selected</c:if>>
					전체 / 품목코드 / 품목명</option>
				<option value="all"
					<c:if test="${invRegInqSearchDTO.searchType eq 'all'}">selected</c:if>>전체</option>
				<option value="itemCode"
					<c:if test="${invRegInqSearchDTO.searchType eq 'itemCode'}">selected</c:if>>품목코드</option>
				<option value="itemName"
					<c:if test="${invRegInqSearchDTO.searchType eq 'itemName'}">selected</c:if>>품목명</option>
			</select>
		</div>

		<div class="taToolbarField taToolbarSpan2">
			<label class="taDateLabel">시작일</label>
			<input type="date" class="taSearchInput" name="startDate"
				value="${invRegInqSearchDTO.startDate}">
		</div>

		<div class="taToolbarField taToolbarSpan2">
			<label class="taDateLabel">종료일</label>
			<input type="date" class="taSearchInput" name="endDate"
				value="${invRegInqSearchDTO.endDate}">
		</div>

		<div class="taToolbarField taToolbarFieldGrow taToolbarSpan6">
			<div class="taSearchBox">
				<input type="text" class="taSearchInput" name="keyword"
					placeholder="검색키워드" value="${invRegInqSearchDTO.keyword}">

				<button type="submit" class="taSearchBtn" aria-label="검색"
					onclick="document.getElementById('paPage').value=1;">
					<svg viewBox="0 0 24 24" fill="none" stroke-width="2">
						<circle cx="11" cy="11" r="7"></circle>
						<path d="M20 20L16.65 16.65"></path>
					</svg>
				</button>

				<button type="button" class="taBtn taBtnOutline taSearchReset"
					onclick="location.href='${pageContext.request.contextPath}/invRegInq'">
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
					<th class="taTableHeadCell taColFit">NO</th>
					<th class="taTableHeadCell taColFit">품목코드</th>
					<th class="taTableHeadCell taColFit">품목 유형</th>
					<th class="taTableHeadCell taColGrow">품목명</th>
					<th class="taTableHeadCell taColFit">현재재고</th>
					<th class="taTableHeadCell taColFit">안전재고</th>
					<th class="taTableHeadCell taColFit">단위</th>
					<th class="taTableHeadCell taColFit">상태</th>
					<th class="taTableHeadCell taColGrow">비고</th>
					<th class="taTableHeadCell taColAction taLastCol">상세보기</th>
				</tr>
			</thead>
			<tbody>
				<c:forEach var="dto" items="${invRegInqList}">
					<tr class="taTableBodyRow">
						<td class="taTableBodyCell taColFit">${dto.inventoryId}</td>
						<td class="taTableBodyCell taColFit">${dto.itemCode}</td>
						<td class="taTableBodyCell taColFit">${dto.itemType}</td>
						<td class="taTableBodyCell taColGrow">${dto.itemName}</td>
						<td class="taTableBodyCell taColFit">${dto.qtyOnHand}</td>
						<td class="taTableBodyCell taColFit">${dto.safetyStock}</td>
						<td class="taTableBodyCell taColFit">${dto.unit}</td>
						<td class="taTableBodyCell taColFit">${dto.inventoryStatus}</td>
						<td class="taTableBodyCell taColGrow">${dto.remark}</td>
						<td class="taTableBodyCell taColAction taLastCol"><a
							class="taLinkAnchor"
							href="${pageContext.request.contextPath}/invRegInq?inventoryId=${dto.inventoryId}">상세보기</a>
						</td>
					</tr>
				</c:forEach>
				<c:if test="${empty invRegInqList}">
					<tr class="taTableBodyRow">
						<td class="taTableBodyCell taLastCol" colspan="10"
							style="text-align: center;">조회된 데이터가 없습니다.</td>
					</tr>
				</c:if>
			</tbody>
		</table>
	</div>
</div>