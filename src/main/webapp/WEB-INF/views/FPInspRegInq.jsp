<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<div class="taPageActions">
	<button type="button" class="taOpenModal taBtn taBtnPrimary"
		data-modal-target="fpInspRegisterModal">등록</button>
	<button type="submit" form="fpInspDeleteForm"
		class="taBtn taBtnOutline">선택 삭제</button>
</div>
<div id="fpInspRegisterModal" class="taModal" hidden aria-hidden="true">
	<div class="taModalDialog">
		<div class="taModalHeader">
			<h3 class="taModalTitle">완제품 검사 등록</h3>
			<button type="button" class="taModalClose"
				data-modal-target="fpInspRegisterModal">×</button>
		</div>
		<form method="post"
			action="${pageContext.request.contextPath}/fpInspRegInq">
			<input type="hidden" name="cmd" value="register">
			<div class="taModalBody taModalGrid">
				<div class="form-row">
					<label>생산실적번호</label><input type="number" name="resultId" required>
				</div>
				<div class="form-row">
					<label>검사수량</label><input type="number" step="0.001"
						name="inspectQty" required>
				</div>
				<div class="form-row">
					<label>판정</label><select name="result"><option>합격</option>
						<option>불합격</option></select>
				</div>
				<div class="form-row">
					<label>검사일</label><input type="date" name="inspectionDate" required>
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
	action="${pageContext.request.contextPath}/fpInspRegInq">
	<input type="hidden" name="cmd" value="list"> <input
		type="hidden" name="page" id="paPage" value="${paCurrentPage}">

	<div class="taToolbarRow">

		<!-- 1) 판정 -->
		<div class="taToolbarField taToolbarSpan2">
			<select
				class="taSelect taAutoSelectColor ${empty fpInspRegInqSearchDTO.resultType or fpInspRegInqSearchDTO.resultType eq '전체' ? 'taSelectPlaceholder' : ''}"
				name="resultType">
				<option value="" disabled hidden
					<c:if test="${empty fpInspRegInqSearchDTO.resultType or fpInspRegInqSearchDTO.resultType eq '전체'}">selected</c:if>>
					전체 / 합격 / 불합격</option>
				<option value="전체"
					<c:if test="${fpInspRegInqSearchDTO.resultType eq '전체'}">selected</c:if>>
					전체</option>
				<option value="합격"
					<c:if test="${fpInspRegInqSearchDTO.resultType eq '합격'}">selected</c:if>>
					합격</option>
				<option value="불합격"
					<c:if test="${fpInspRegInqSearchDTO.resultType eq '불합격'}">selected</c:if>>
					불합격</option>
			</select>
		</div>

		<!-- 2) 조회기준 -->
		<div class="taToolbarField taToolbarSpan2">
			<select
				class="taSelect taAutoSelectColor ${empty fpInspRegInqSearchDTO.searchType ? 'taSelectPlaceholder' : ''}"
				name="searchType">
				<option value="" disabled hidden
					<c:if test="${empty fpInspRegInqSearchDTO.searchType}">selected</c:if>>
					전체 / 품목코드 / 품목명</option>
				<option value="all"
					<c:if test="${fpInspRegInqSearchDTO.searchType eq 'all'}">selected</c:if>>
					전체</option>
				<option value="itemCode"
					<c:if test="${fpInspRegInqSearchDTO.searchType eq 'itemCode'}">selected</c:if>>
					품목코드</option>
				<option value="itemName"
					<c:if test="${fpInspRegInqSearchDTO.searchType eq 'itemName'}">selected</c:if>>
					품목명</option>
			</select>
		</div>

		<!-- 3) 시작일 -->
		<div class="taToolbarField taToolbarSpan2">
			<input type="date" class="taSearchInput" name="startDate"
				value="${fpInspRegInqSearchDTO.startDate}">
		</div>

		<!-- 4) 종료일 -->
		<div class="taToolbarField taToolbarSpan2">
			<input type="date" class="taSearchInput" name="endDate"
				value="${fpInspRegInqSearchDTO.endDate}">
		</div>

		<!-- 5) 검색키워드 + 돋보기 + 초기화 -->
		<div class="taToolbarField taToolbarFieldGrow taToolbarSpan4">
			<div class="taSearchBox">
				<input type="text" class="taSearchInput" name="keyword"
					placeholder="검색키워드" value="${fpInspRegInqSearchDTO.keyword}">

				<button type="submit" class="taSearchBtn" aria-label="검색"
					onclick="document.getElementById('paPage').value=1;">
					<svg viewBox="0 0 24 24" fill="none" stroke="currentColor"
						stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
						<circle cx="11" cy="11" r="7"></circle>
						<path d="M20 20L16.65 16.65"></path>
					</svg>
				</button>

				<button type="button" class="taBtn taBtnOutline taSearchReset"
					onclick="location.href='${pageContext.request.contextPath}/fpInspRegInq'">
					초기화</button>
			</div>
		</div>

	</div>
</form>

<form id="fpInspDeleteForm" method="post"
	action="${pageContext.request.contextPath}/fpInspRegInq">
	<input type="hidden" name="cmd" value="delete">
	<div class="taTableShell" id="paTableBox">
		<div class="taTableScroll">
			<table class="taMesTable">
				<thead>
					<tr>
						<th class="taTableHeadCell taColFit"><input type="checkbox"
							id="checkAll" class="taCheckInput"></th>
						<th class="taTableHeadCell taColFit">NO</th>
<!-- 						<th class="taTableHeadCell taColFit">생산실적번호</th> -->
						<th class="taTableHeadCell taColFit">작업지시번호</th>
						<th class="taTableHeadCell taColFit">품목코드</th>
						<th class="taTableHeadCell taColGrow">품목명</th>
						<th class="taTableHeadCell taColFit">LOT번호</th>
						<th class="taTableHeadCell taColFit">검사수량</th>
						<th class="taTableHeadCell taColFit">판정</th>
						<!-- 						<th class="taTableHeadCell taColDate">검사일</th> -->
						<th class="taTableHeadCell taColAction taLastCol">상세보기</th>
					</tr>
				</thead>
				<tbody>
					<c:forEach var="dto" items="${fpInspRegInqList}">
						<tr class="taTableBodyRow">
							<td class="taTableBodyCell taColFit"><input type="checkbox"
								class="taCheckInput" name="finalInspectionIds"
								value="${dto.finalInspectionId}"></td>
							<td class="taTableBodyCell taColFit">${dto.finalInspectionId}</td>
<%-- 							<td class="taTableBodyCell taColFit">${dto.resultId}</td> --%>
							<td class="taTableBodyCell taColFit">${dto.workOrderId}</td>
							<td class="taTableBodyCell taColFit">${dto.itemCode}</td>
							<td class="taTableBodyCell taColGrow">${dto.itemName}</td>
							<td class="taTableBodyCell taColFit">${dto.lotNo}</td>
							<td class="taTableBodyCell taColFit">${dto.inspectQty}</td>
							<td class="taTableBodyCell taColFit">${dto.result}</td>
							<%-- 							<td class="taTableBodyCell taColDate">${dto.inspectionDate}</td> --%>
							<td class="taTableBodyCell taColAction taLastCol"><a
								class="taLinkAnchor"
								href="${pageContext.request.contextPath}/fpInspRegInq?finalInspectionId=${dto.finalInspectionId}">상세보기</a></td>
						</tr>
					</c:forEach>
					<c:if test="${empty fpInspRegInqList}">
						<tr class="taTableBodyRow">
							<td class="taTableBodyCell taLastCol" colspan="11"
								style="text-align: center;">조회된 데이터가 없습니다.</td>
						</tr>
					</c:if>
				</tbody>
			</table>
		</div>
	</div>
</form>
