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
<form  id="paSearchForm" method="post"
	action="${pageContext.request.contextPath}/defectRegInq">
	<input type="hidden" name="cmd" value="list">
	<input type="hidden" name="page" id="paPage" value="${paCurrentPage}">
	
	<div class="taToolbarRow">
		<div class="taToolbarField">
			<select class="taSelect" name="defectTypeSearch"><option
					value="전체"
					${empty defectRegInqSearchDTO.defectTypeSearch or defectRegInqSearchDTO.defectTypeSearch eq '전체' ? 'selected' : ''}>전체</option>
				<option value="공정"
					${defectRegInqSearchDTO.defectTypeSearch eq '공정' ? 'selected' : ''}>공정</option>
				<option value="외관"
					${defectRegInqSearchDTO.defectTypeSearch eq '외관' ? 'selected' : ''}>외관</option>
				<option value="치수"
					${defectRegInqSearchDTO.defectTypeSearch eq '치수' ? 'selected' : ''}>치수</option></select>
		</div>
		<div class="taToolbarField">
			<div class="taSearchBox">
				<input type="date" class="taSearchInput" name="startDate"
					value="${defectRegInqSearchDTO.startDate}"><input
					type="date" class="taSearchInput" name="endDate"
					value="${defectRegInqSearchDTO.endDate}">
			</div>
		</div>
		<div class="taToolbarField">
			<select class="taSelect" name="searchType"><option value=""
					${empty defectRegInqSearchDTO.searchType ? 'selected' : ''}>전체</option>
				<option value="itemCode"
					${defectRegInqSearchDTO.searchType eq 'itemCode' ? 'selected' : ''}>품목코드</option>
				<option value="itemName"
					${defectRegInqSearchDTO.searchType eq 'itemName' ? 'selected' : ''}>품목명</option>
				<option value="defectCode"
					${defectRegInqSearchDTO.searchType eq 'defectCode' ? 'selected' : ''}>불량코드</option>
				<option value="defectName"
					${defectRegInqSearchDTO.searchType eq 'defectName' ? 'selected' : ''}>불량명</option></select>
		</div>
		<div class="taToolbarField taToolbarFieldGrow">
			<div class="taSearchBox">
				<input type="text" class="taSearchInput" name="keyword"
					placeholder="검색키워드" value="${defectRegInqSearchDTO.keyword}">
				<button type="submit" class="taSearchBtn">⌕</button>
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
							<td class="taTableBodyCell taColAction taLastCol"><form
									method="post"
									action="${pageContext.request.contextPath}/defectRegInq"
									style="margin: 0;">
									<input type="hidden" name="cmd" value="detail"><input
										type="hidden" name="defectProductId"
										value="${dto.defectProductId}">
									<button type="submit" class="taLinkButton">상세보기</button>
								</form></td>
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
<c:if test="${not empty defectRegInqDTO}">
	<div class="taTableShell" style="padding: 20px; margin-top: 20px;">
		<h3 style="margin-top: 0;">상세조회 결과</h3>
		<p>
			<strong>불량번호 :</strong> ${defectRegInqDTO.defectProductId}
		</p>
		<p>
			<strong>완제품검사번호 :</strong> ${defectRegInqDTO.finalInspectionId}
		</p>
		<p>
			<strong>품목코드 :</strong> ${defectRegInqDTO.itemCode}
		</p>
		<p>
			<strong>품목명 :</strong> ${defectRegInqDTO.itemName}
		</p>
		<p>
			<strong>LOT번호 :</strong> ${defectRegInqDTO.lotNo}
		</p>
		<p>
			<strong>불량코드 :</strong> ${defectRegInqDTO.defectCode}
		</p>
		<p>
			<strong>불량명 :</strong> ${defectRegInqDTO.defectName}
		</p>
		<p>
			<strong>유형 :</strong> ${defectRegInqDTO.defectType}
		</p>
		<p>
			<strong>비고 :</strong> ${defectRegInqDTO.remark}
		</p>
	</div>
</c:if>