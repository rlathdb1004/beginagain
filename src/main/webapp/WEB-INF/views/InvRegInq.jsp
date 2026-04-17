<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<div class="taPageActions">
	<button type="button" class="taOpenModal taBtn taBtnPrimary"
		data-modal-target="invRegisterModal">등록</button>
	<button type="submit" form="invDeleteForm" class="taBtn taBtnOutline">선택
		삭제</button>
</div>

<div id="invRegisterModal" class="taModal" hidden aria-hidden="true">
	<div class="taModalDialog">
		<div class="taModalHeader">
			<h3 class="taModalTitle">재고 등록</h3>
			<button type="button" class="taModalClose"
				data-modal-target="invRegisterModal">×</button>
		</div>
		<form method="post"
			action="${pageContext.request.contextPath}/invRegInq">
			<input type="hidden" name="cmd" value="register">
			<div class="taModalBody taModalGrid">
				<div class="form-row">
					<label>품목코드</label><input type="text" name="itemCode" required>
				</div>
				<div class="form-row">
					<label>현재재고</label><input type="number" step="0.001"
						name="qtyOnHand" required>
				</div>
				<div class="form-row">
					<label>안전재고</label><input type="number" step="0.001"
						name="safetyStock" required>
				</div>
				<div class="form-row full">
					<label>비고</label>
					<textarea name="remark"></textarea>
				</div>
			</div>
			<div class="taModalFooter">
				<button type="button" class="taBtn taBtnOutline taModalClose">취소</button>
				<button type="submit" class="taBtn taBtnPrimary">저장</button>
			</div>
		</form>
	</div>
</div>

<form id="paSearchForm" method="post"
	action="${pageContext.request.contextPath}/invRegInq">
	<input type="hidden" name="cmd" value="list"> <input
		type="hidden" name="page" id="paPage" value="${paCurrentPage}">

	<div class="taToolbarRow">

		<!-- 1) 전체 / 품목코드 / 품목명 -->
		<div class="taToolbarField taToolbarSpan2">
			<select id="paSearchType"
				class="taSelect taAutoSelectColor ${empty ioRegInqSearchDTO.searchType ? 'taSelectPlaceholder' : ''}"
				name="searchType">
				<option value="" hidden
					<c:if test="${empty invRegInqSearchDTO.searchType}">selected</c:if>>
					전체 / 품목코드 / 품목명</option>
				<option value="all">전체</option>
				<option value="itemCode"
					<c:if test="${invRegInqSearchDTO.searchType eq 'itemCode'}">selected</c:if>>
					품목코드</option>
				<option value="itemName"
					<c:if test="${invRegInqSearchDTO.searchType eq 'itemName'}">selected</c:if>>
					품목명</option>
			</select>
		</div>

		<!-- 2) 시작일 -->
		<div class="taToolbarField taToolbarSpan2">
			<input type="date" class="taSearchInput" name="startDate"
				value="${invRegInqSearchDTO.startDate}">
		</div>

		<!-- 3) 종료일 -->
		<div class="taToolbarField taToolbarSpan2">
			<input type="date" class="taSearchInput" name="endDate"
				value="${invRegInqSearchDTO.endDate}">
		</div>

		<!-- 4) 검색키워드 + 5) 돋보기 + 6) 초기화 -->
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

<form id="invDeleteForm" method="post"
	action="${pageContext.request.contextPath}/invRegInq">
	<input type="hidden" name="cmd" value="delete">
	<div class="taTableShell" id="paTableBox">
		<div class="taTableScroll">
			<table class="taMesTable">
				<thead>
					<tr>
						<th class="taTableHeadCell taColFit"><input type="checkbox"
							id="checkAll" class="taCheckInput"></th>
						<th class="taTableHeadCell taColFit">NO</th>
						<th class="taTableHeadCell taColFit">품목코드</th>
						<th class="taTableHeadCell taColGrow">품목명</th>
						<th class="taTableHeadCell taColFit">현재재고</th>
						<th class="taTableHeadCell taColFit">안전재고</th>
						<th class="taTableHeadCell taColFit">단위</th>
						<th class="taTableHeadCell taColGrow">비고</th>
						<th class="taTableHeadCell taColAction taLastCol">상세보기</th>
					</tr>
				</thead>
				<tbody>
					<c:forEach var="dto" items="${invRegInqList}">
						<tr class="taTableBodyRow">
							<td class="taTableBodyCell taColFit"><input type="checkbox"
								class="taCheckInput" name="inventoryIds"
								value="${dto.inventoryId}"></td>
							<td class="taTableBodyCell taColFit">${dto.inventoryId}</td>
							<td class="taTableBodyCell taColFit">${dto.itemCode}</td>
							<td class="taTableBodyCell taColGrow">${dto.itemName}</td>
							<td class="taTableBodyCell taColFit">${dto.qtyOnHand}</td>
							<td class="taTableBodyCell taColFit">${dto.safetyStock}</td>
							<td class="taTableBodyCell taColFit">${dto.unit}</td>
							<td class="taTableBodyCell taColGrow">${dto.remark}</td>
							<td class="taTableBodyCell taColAction taLastCol"><a
								class="taLinkAnchor"
								href="${pageContext.request.contextPath}/invRegInq?inventoryId=${dto.inventoryId}">상세보기</a></td>
						</tr>
					</c:forEach>
					<c:if test="${empty invRegInqList}">
						<tr class="taTableBodyRow">
							<td class="taTableBodyCell taLastCol" colspan="9"
								style="text-align: center;">조회된 데이터가 없습니다.</td>
						</tr>
					</c:if>
				</tbody>
			</table>
		</div>
	</div>
</form>

