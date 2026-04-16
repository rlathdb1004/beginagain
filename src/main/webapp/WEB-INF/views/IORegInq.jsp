<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<div id="ioRegisterModal" class="taModal" hidden aria-hidden="true">
	<div class="taModalDialog">
		<div class="taModalHeader">
			<h3 class="taModalTitle">입출고 등록</h3>
			<button type="button" class="taModalClose">×</button>
		</div>
		<form method="post"
			action="${pageContext.request.contextPath}/ioRegInq">
			<input type="hidden" name="cmd" value="register">
			<div class="taModalBody taModalGrid">
				<div class="form-row">
					<label>품목코드</label><input type="text" name="itemCode" required>
				</div>
				<div class="form-row">
					<label>입출고구분</label><select name="inoutType"><option>입고</option>
						<option>출고</option>
						<option>반품</option></select>
				</div>
				<div class="form-row">
					<label>수량</label><input type="number" step="0.001" name="qty"
						required>
				</div>
				<div class="form-row">
					<label>단위</label><input type="text" name="unit" value="EA">
				</div>
				<div class="form-row">
					<label>일자</label><input type="date" name="inoutDate" required>
				</div>
				<div class="form-row">
					<label>상태</label><input type="text" name="status" value="정상">
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

<!-- 상단 버튼 영역 -->
<div class="taPageActions">
	<button type="button" class="taOpenModal taBtn taBtnPrimary"
		data-modal-target="ioRegisterModal">등록</button>
	<button type="submit" form="ioDeleteForm" class="taBtn taBtnOutline">선택 삭제</button>
</div>

<!-- 조회 form -->
<form id="paSearchForm"  method="post" action="${pageContext.request.contextPath}/ioRegInq">
	<input type="hidden" name="cmd" value="list">
	<input type="hidden" name="page" id="paPage" value="${paCurrentPage}">

	<div class="taToolbarRow">

		<!-- 1) 입출고구분 -->
		<div class="taToolbarField">
			<select class="taSelect" name="inoutType">
				<option value="전체"
					<c:if test="${empty ioRegInqSearchDTO.inoutType or ioRegInqSearchDTO.inoutType eq '전체'}">selected</c:if>>
					전체</option>
				<option value="입고"
					<c:if test="${ioRegInqSearchDTO.inoutType eq '입고'}">selected</c:if>>
					입고</option>
				<option value="출고"
					<c:if test="${ioRegInqSearchDTO.inoutType eq '출고'}">selected</c:if>>
					출고</option>
				<option value="반품"
					<c:if test="${ioRegInqSearchDTO.inoutType eq '반품'}">selected</c:if>>
					반품</option>
			</select>
		</div>

		<!-- 2) 시작일 + 종료일 -->
		<div class="taToolbarField">
			<div class="taSearchBox">
				<input type="date" class="taSearchInput" name="startDate"
					value="${ioRegInqSearchDTO.startDate}"> <input type="date"
					class="taSearchInput" name="endDate"
					value="${ioRegInqSearchDTO.endDate}">
			</div>
		</div>

		<!-- 3) 조회기준 -->
		<div class="taToolbarField">
			<select class="taSelect" name="searchType">
				<option value=""
					<c:if test="${empty ioRegInqSearchDTO.searchType}">selected</c:if>>
					전체</option>
				<option value="itemCode"
					<c:if test="${ioRegInqSearchDTO.searchType eq 'itemCode'}">selected</c:if>>
					품목코드</option>
				<option value="itemName"
					<c:if test="${ioRegInqSearchDTO.searchType eq 'itemName'}">selected</c:if>>
					품목명</option>
				<option value="orderNo"
					<c:if test="${ioRegInqSearchDTO.searchType eq 'orderNo'}">selected</c:if>>
					발주번호</option>
			</select>
		</div>

		<!-- 4) 검색키워드 + 검색버튼 -->
		<div class="taToolbarField taToolbarFieldGrow">
			<div class="taSearchBox">
				<input type="text" class="taSearchInput" name="keyword"
					placeholder="검색키워드" value="${ioRegInqSearchDTO.keyword}">

				<button type="submit" class="taSearchBtn" aria-label="검색">⌕</button>
			</div>
		</div>

	</div>
</form>

<!-- 테이블 -->
<div class="taTableShell">
	<div class="taTableScroll">
		<form id="ioDeleteForm" method="post"
			action="${pageContext.request.contextPath}/ioRegInq">
			<input type="hidden" name="cmd" value="delete">
			<table class="taMesTable">
			<div class="taTableShell" id="paTableBox">

				<thead>
					<tr>
						<th class="taTableHeadCell taColFit"><input type="checkbox"
							id="checkAll"></th>
						<th class="taTableHeadCell taColFit">NO</th>
						<th class="taTableHeadCell taColFit">입출고구분</th>
						<th class="taTableHeadCell taColFit">품목코드</th>
						<th class="taTableHeadCell taColGrow">품목명</th>
						<th class="taTableHeadCell taColFit">입출고량</th>
						<th class="taTableHeadCell taColFit">단위</th>
						<th class="taTableHeadCell taColDate">일자</th>
						<th class="taTableHeadCell taColAction taLastCol">상세보기</th>
					</tr>
				</thead>

				<tbody>
					<c:forEach var="dto" items="${ioRegInqList}">
						<tr class="taTableBodyRow">
							<td class="taTableBodyCell taColFit"><input type="checkbox"
								name="inoutIds" value="${dto.inoutId}"></td>
							<td class="taTableBodyCell taColFit">${dto.inoutId}</td>
							<td class="taTableBodyCell taColFit">${dto.inoutType}</td>
							<td class="taTableBodyCell taColFit">${dto.itemCode}</td>
							<td class="taTableBodyCell taColGrow">${dto.itemName}</td>
							<td class="taTableBodyCell taColFit">${dto.qty}</td>
							<td class="taTableBodyCell taColFit">${dto.unit}</td>
							<td class="taTableBodyCell taColDate">${dto.inoutDate}</td>
							<td class="taTableBodyCell taColAction taLastCol">
								<form method="post"
									action="${pageContext.request.contextPath}/ioRegInq"
									style="margin: 0;">
									<input type="hidden" name="cmd" value="detail"> <input
										type="hidden" name="inoutId" value="${dto.inoutId}">
									<button type="submit" class="taLinkButton">상세보기</button>
								</form>
							</td>
						</tr>
					</c:forEach>

					<c:if test="${empty ioRegInqList}">
						<tr class="taTableBodyRow">
							<td class="taTableBodyCell taLastCol" colspan="9"
								style="text-align: center;">조회된 데이터가 없습니다.</td>
						</tr>
					</c:if>
				</tbody>

			</table>
		</form>
	</div>
</div>

<!-- 상세조회 결과 -->
<c:if test="${not empty ioRegInqDTO}">
	<div class="taTableShell" style="padding: 20px; margin-top: 20px;">
		<h3 style="margin-top: 0;">상세조회 결과</h3>

		<p>
			<strong>입출고번호 :</strong> ${ioRegInqDTO.inoutId}
		</p>
		<p>
			<strong>품목코드 :</strong> ${ioRegInqDTO.itemCode}
		</p>
		<p>
			<strong>품목명 :</strong> ${ioRegInqDTO.itemName}
		</p>
		<p>
			<strong>입출고구분 :</strong> ${ioRegInqDTO.inoutType}
		</p>
		<p>
			<strong>수량 :</strong> ${ioRegInqDTO.qty}
		</p>
		<p>
			<strong>단위 :</strong> ${ioRegInqDTO.unit}
		</p>
		<p>
			<strong>일자 :</strong> ${ioRegInqDTO.inoutDate}
		</p>
		<p>
			<strong>상태 :</strong> ${ioRegInqDTO.status}
		</p>
		<p>
			<strong>비고 :</strong> ${ioRegInqDTO.remark}
		</p>
	</div>
</c:if>

