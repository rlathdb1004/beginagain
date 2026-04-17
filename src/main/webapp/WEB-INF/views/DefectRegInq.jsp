<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<div class="taPageActions">
	<button type="button" class="taOpenModal taBtn taBtnPrimary"
		data-modal-target="defectRegisterModal">등록</button>
	<button type="submit" form="defectDeleteForm"
		class="taBtn taBtnOutline">선택 삭제</button>
</div>
<div id="defectRegisterModal" class="taModal" hidden aria-hidden="true">
	<div class="taModalDialog">
		<div class="taModalHeader">
			<h3 class="taModalTitle">불량 등록</h3>
			<button type="button" class="taModalClose"
				data-modal-target="defectRegisterModal">×</button>
		</div>
		<form method="post"
			action="${pageContext.request.contextPath}/defectRegInq">
			<input type="hidden" name="cmd" value="register">
			<div class="taModalBody taModalGrid">
				<div class="form-row">
					<label>완제품검사번호</label><input type="number" name="finalInspectionId"
						required>
				</div>
				<div class="form-row">
					<label>불량코드ID</label><input type="number" name="defectCodeId"
						required>
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
	action="${pageContext.request.contextPath}/defectRegInq">
	<input type="hidden" name="cmd" value="list"> <input
		type="hidden" name="page" id="paPage" value="${paCurrentPage}">

	<div class="taToolbarRow">

		<!-- 1) 유형 -->
		<div class="taToolbarField taToolbarSpan2">
			<select
				class="taSelect taAutoSelectColor ${empty defectRegInqSearchDTO.defectTypeSearch or defectRegInqSearchDTO.defectTypeSearch eq '전체' ? 'taSelectPlaceholder' : ''}"
				name="defectTypeSearch">
				<option value="" disabled hidden
					<c:if test="${empty defectRegInqSearchDTO.defectTypeSearch or defectRegInqSearchDTO.defectTypeSearch eq '전체'}">selected</c:if>>
					전체 / 공정 / 외관 / 치수</option>
				<option value="전체"
					<c:if test="${defectRegInqSearchDTO.defectTypeSearch eq '전체'}">selected</c:if>>
					전체</option>
				<option value="공정"
					<c:if test="${defectRegInqSearchDTO.defectTypeSearch eq '공정'}">selected</c:if>>
					공정</option>
				<option value="외관"
					<c:if test="${defectRegInqSearchDTO.defectTypeSearch eq '외관'}">selected</c:if>>
					외관</option>
				<option value="치수"
					<c:if test="${defectRegInqSearchDTO.defectTypeSearch eq '치수'}">selected</c:if>>
					치수</option>
			</select>
		</div>

		<!-- 2) 조회기준 -->
		<div class="taToolbarField taToolbarSpan2">
			<select
				class="taSelect taAutoSelectColor ${empty defectRegInqSearchDTO.searchType ? 'taSelectPlaceholder' : ''}"
				name="searchType">
				<option value="" disabled hidden
					<c:if test="${empty defectRegInqSearchDTO.searchType}">selected</c:if>>
					전체 / 품목코드 ...</option>
				<option value="all"
					<c:if test="${defectRegInqSearchDTO.searchType eq 'all'}">selected</c:if>>
					전체</option>
				<option value="itemCode"
					<c:if test="${defectRegInqSearchDTO.searchType eq 'itemCode'}">selected</c:if>>
					품목코드</option>
				<option value="itemName"
					<c:if test="${defectRegInqSearchDTO.searchType eq 'itemName'}">selected</c:if>>
					품목명</option>
				<option value="defectCode"
					<c:if test="${defectRegInqSearchDTO.searchType eq 'defectCode'}">selected</c:if>>
					불량코드</option>
				<option value="defectName"
					<c:if test="${defectRegInqSearchDTO.searchType eq 'defectName'}">selected</c:if>>
					불량명</option>
			</select>
		</div>

		<!-- 3) 시작일 -->
		<div class="taToolbarField taToolbarSpan2">
			<input type="date" class="taSearchInput" name="startDate"
				value="${defectRegInqSearchDTO.startDate}">
		</div>

		<!-- 4) 종료일 -->
		<div class="taToolbarField taToolbarSpan2">
			<input type="date" class="taSearchInput" name="endDate"
				value="${defectRegInqSearchDTO.endDate}">
		</div>

		<!-- 5) 검색키워드 + 돋보기 + 초기화 -->
		<div class="taToolbarField taToolbarFieldGrow taToolbarSpan4">
			<div class="taSearchBox">
				<input type="text" class="taSearchInput" name="keyword"
					placeholder="검색키워드" value="${defectRegInqSearchDTO.keyword}">

				<button type="submit" class="taSearchBtn" aria-label="검색"
					onclick="document.getElementById('paPage').value=1;">
					<svg viewBox="0 0 24 24" fill="none" stroke="currentColor"
						stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
						<circle cx="11" cy="11" r="7"></circle>
						<path d="M20 20L16.65 16.65"></path>
					</svg>
				</button>

				<button type="button" class="taBtn taBtnOutline taSearchReset"
					onclick="location.href='${pageContext.request.contextPath}/defectRegInq'">
					초기화</button>
			</div>
		</div>

	</div>
</form>

<form id="defectDeleteForm" method="post"
	action="${pageContext.request.contextPath}/defectRegInq">
	<input type="hidden" name="cmd" value="delete">
	<div class="taTableShell" id="paTableBox">
		<div class="taTableScroll">
			<table class="taMesTable">
				<thead>
					<tr>
						<th class="taTableHeadCell taColFit"><input type="checkbox"
							id="checkAll" class="taCheckInput"></th>
						<th class="taTableHeadCell taColFit">NO</th>
						<th class="taTableHeadCell taColFit">완제품검사번호</th>
						<th class="taTableHeadCell taColFit">품목코드</th>
						<th class="taTableHeadCell taColGrow">품목명</th>
						<th class="taTableHeadCell taColFit">LOT번호</th>
						<th class="taTableHeadCell taColFit">불량코드</th>
						<th class="taTableHeadCell taColFit">불량명</th>
						<th class="taTableHeadCell taColFit">유형</th>
						<th class="taTableHeadCell taColAction taLastCol">상세보기</th>
					</tr>
				</thead>
				<tbody>
					<c:forEach var="dto" items="${defectRegInqList}">
						<tr class="taTableBodyRow">
							<td class="taTableBodyCell taColFit"><input type="checkbox"
								class="taCheckInput" name="defectProductIds"
								value="${dto.defectProductId}"></td>
							<td class="taTableBodyCell taColFit">${dto.defectProductId}</td>
							<td class="taTableBodyCell taColFit">${dto.finalInspectionId}</td>
							<td class="taTableBodyCell taColFit">${dto.itemCode}</td>
							<td class="taTableBodyCell taColGrow">${dto.itemName}</td>
							<td class="taTableBodyCell taColFit">${dto.lotNo}</td>
							<td class="taTableBodyCell taColFit">${dto.defectCode}</td>
							<td class="taTableBodyCell taColFit">${dto.defectName}</td>
							<td class="taTableBodyCell taColFit">${dto.defectType}</td>
							<td class="taTableBodyCell taColAction taLastCol"><a
								class="taLinkAnchor"
								href="${pageContext.request.contextPath}/defectRegInq?defectProductId=${dto.defectProductId}">상세보기</a></td>
						</tr>
					</c:forEach>
					<c:if test="${empty defectRegInqList}">
						<tr class="taTableBodyRow">
							<td class="taTableBodyCell taLastCol" colspan="10"
								style="text-align: center;">조회된 데이터가 없습니다.</td>
						</tr>
					</c:if>
				</tbody>
			</table>
		</div>
	</div>
</form>
