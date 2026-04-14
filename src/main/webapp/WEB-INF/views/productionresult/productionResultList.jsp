<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<div class="taPageActions">
	<button type="button" class="taBtn taBtnPrimary"
		 data-modal-target="registerModal">등록</button>
	<button type="submit" form="deleteForm" class="taBtn taBtnOutline"
		onclick="return confirm('선택한 생산실적을 삭제하시겠습니까?');">선택 삭제</button>
	<a
		href="${pageContext.request.contextPath}/workorder/detail?workOrderId=${workOrderId}"
		class="taBtn taBtnOutline" style="text-decoration: none;">작업지시 상세</a>
</div>
<form class="taLocalSearchForm" data-table-id="productionResultTable">
	<div class="taToolbarRow">
		<div class="taToolbarField">
			<select class="taSelect" name="searchType">
				<option value="all">전체</option>
				<option value="itemName">품목명</option>
				<option value="empName">작업자</option>
				<option value="lotNo">LOT NO</option>
				<option value="status">상태</option>
			</select>
		</div>
		<div class="taToolbarField taToolbarFieldGrow" style="grid-column: span 3;">
			<div class="taSearchBox">
				<input type="text" class="taSearchInput" name="keyword" placeholder="검색어를 입력하세요">
				<button type="submit" class="taSearchBtn">⌕</button>
				<button type="button" class="taBtn taBtnOutline taSearchReset">초기화</button>
			</div>
		</div>
	</div>
</form>
<form id="deleteForm"
	action="${pageContext.request.contextPath}/productionresult/delete"
	method="post">
	<input type="hidden" name="workOrderId" value="${workOrderId}">
	<div class="taTableShell">
		<div class="taTableScroll">
			<table class="taMesTable" id="productionResultTable">
				<thead>
					<tr>
						<th class="taTableHeadCell taCheckCell"><input
							type="checkbox" id="checkAll" class="taCheckInput"></th>
						<th class="taTableHeadCell taColFit">ID</th>
						<th class="taTableHeadCell taColGrow">품목명</th>
						<th class="taTableHeadCell taColFit">작업자</th>
						<th class="taTableHeadCell taColDate">실적일</th>
						<th class="taTableHeadCell taColFit">LOT NO</th>
						<th class="taTableHeadCell taColFit">생산수량</th>
						<th class="taTableHeadCell taColFit">손실수량</th>
						<th class="taTableHeadCell taColFit">상태</th>
						<th class="taTableHeadCell taColAction taLastCol">상세</th>
					</tr>
				</thead>
				<tbody>
					<c:choose>
						<c:when test="${not empty productionResultList}">
							<c:forEach var="pr" items="${productionResultList}">
								<tr class="taTableBodyRow">
									<td class="taTableBodyCell taCheckCell"><input
										type="checkbox" name="resultId" value="${pr.resultId}"
										class="taCheckInput"></td>
									<td class="taTableBodyCell taColFit">${pr.resultId}</td>
									<td class="taTableBodyCell taColGrow" data-search-key="itemName">${pr.itemName}</td>
									<td class="taTableBodyCell taColFit" data-search-key="empName">${pr.empName}</td>
									<td class="taTableBodyCell taColDate">${pr.resultDate}</td>
									<td class="taTableBodyCell taColFit" data-search-key="lotNo">${pr.lotNo}</td>
									<td class="taTableBodyCell taColFit">${pr.producedQty}</td>
									<td class="taTableBodyCell taColFit">${pr.lossQty}</td>
									<td class="taTableBodyCell taColFit" data-search-key="status">${pr.status}</td>
									<td class="taTableBodyCell taColAction taLastCol"><a
										class="taLinkAnchor"
										href="${pageContext.request.contextPath}/productionresult/detail?resultId=${pr.resultId}">상세보기</a></td>
								</tr>
							</c:forEach>
						</c:when>
						<c:otherwise>
							<tr class="taTableBodyRow">
								<td class="taTableBodyCell taLastCol" colspan="10"
									style="text-align: center;">조회된 생산실적이 없습니다.</td>
							</tr>
						</c:otherwise>
					</c:choose>
				</tbody>
			</table>
		</div>
	</div>
</form>
<div class="taModal" id="registerModal" hidden aria-hidden="true">
	<div class="taModalDialog modal-lg">
		<div class="taModalHeader">
			<h3 class="taModalTitle">생산실적 등록</h3>
			<button type="button" class="taModalClose">&times;</button>
		</div>
		<form
			action="${pageContext.request.contextPath}/productionresult/register"
			method="post">
			<div class="taModalBody taModalGrid">
				<input type="hidden" name="workOrderId" value="${workOrderId}">
				<div class="form-row">
					<label>실적일</label><input type="date" name="resultDate">
				</div>
				<div class="form-row">
					<label>LOT NO</label><input type="text" name="lotNo">
				</div>
				<div class="form-row">
					<label>생산수량</label><input type="number" step="0.001"
						name="producedQty" required>
				</div>
				<div class="form-row">
					<label>손실수량</label><input type="number" step="0.001" name="lossQty">
				</div>
				<div class="form-row">
					<label>상태</label><input type="text" name="status" value="등록">
				</div>
				<div class="form-row full">
					<label>비고</label>
					<textarea name="remark"></textarea>
				</div>
			</div>
			<div class="taModalFooter">
				<button type="button" class="taBtn taBtnOutline taModalClose">취소</button>
				<button type="submit" class="taBtn taBtnPrimary">등록</button>
			</div>
		</form>
	</div>
</div>
