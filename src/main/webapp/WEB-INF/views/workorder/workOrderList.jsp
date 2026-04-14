<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<div class="taPageActions">
	<button type="button" class="taOpenModal taBtn taBtnPrimary" data-modal-target="registerModal">등록</button>
	<button type="submit" form="deleteForm" class="taBtn taBtnOutline" onclick="return confirm('선택한 작업지시를 삭제하시겠습니까?');">선택 삭제</button>
</div>
<form class="taLocalSearchForm" data-table-id="workOrderTable">
	<div class="taToolbarRow">
		<div class="taToolbarField">
			<select class="taSelect" name="searchType">
				<option value="all">전체</option>
				<option value="workOrderDisplayCode">작업지시코드</option>
				<option value="itemCode">품목코드</option>
				<option value="itemName">품목명</option>
				<option value="empName">담당자</option>
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
<form id="deleteForm" action="${pageContext.request.contextPath}/workorder/delete" method="post">
<div class="taTableShell">
	<div class="taTableScroll">
		<table class="taMesTable" id="workOrderTable">
			<thead>
				<tr>
					<th class="taTableHeadCell taColFit"><input type="checkbox" id="checkAll" class="taCheckInput"></th>
					<th class="taTableHeadCell taColFit">지시번호</th>
					<th class="taTableHeadCell taColFit">작업지시코드</th>
					<th class="taTableHeadCell taColFit">품목코드</th>
					<th class="taTableHeadCell taColGrow">품목명</th>
					<th class="taTableHeadCell taColFit">담당자</th>
					<th class="taTableHeadCell taColDate">지시일</th>
					<th class="taTableHeadCell taColFit">지시수량</th>
					<th class="taTableHeadCell taColAction taLastCol">상세</th>
				</tr>
			</thead>
			<tbody>
				<c:choose>
					<c:when test="${not empty workOrderList}">
						<c:forEach var="wo" items="${workOrderList}">
							<tr class="taTableBodyRow">
								<td class="taTableBodyCell taColFit"><input type="checkbox" class="taCheckInput" name="workOrderId" value="${wo.workOrderId}"></td>
								<td class="taTableBodyCell taColFit">${wo.workOrderId}</td>
								<td class="taTableBodyCell taColFit" data-search-key="workOrderDisplayCode">${wo.workOrderDisplayCode}</td>
								<td class="taTableBodyCell taColFit" data-search-key="itemCode">${wo.itemCode}</td>
								<td class="taTableBodyCell taColGrow" data-search-key="itemName">${wo.itemName}</td>
								<td class="taTableBodyCell taColFit" data-search-key="empName">${wo.empName}</td>
								<td class="taTableBodyCell taColDate">${wo.workDate}</td>
								<td class="taTableBodyCell taColFit">${wo.workQty}</td>
								<td class="taTableBodyCell taColAction taLastCol"><a
									class="taLinkAnchor"
									href="${pageContext.request.contextPath}/workorder/detail?workOrderId=${wo.workOrderId}">상세보기</a></td>
							</tr>
						</c:forEach>
					</c:when>
					<c:otherwise>
						<tr class="taTableBodyRow">
							<td class="taTableBodyCell taLastCol" colspan="9"
								style="text-align: center;">데이터가 없습니다.</td>
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
			<h3 class="taModalTitle">작업지시 등록</h3>
			<button type="button" class="taModalClose">&times;</button>
		</div>
		<form action="${pageContext.request.contextPath}/workorder/register"
			method="post">
			<div class="taModalBody taModalGrid">
				<div class="form-row">
					<label>품목ID</label><input type="number" name="itemId" required>
				</div>
				<div class="form-row">
					<label>계획ID</label><input type="number" name="planId" required>
				</div>
				<div class="form-row">
					<label>담당자ID</label><input type="number" name="empId" required>
				</div>
				<div class="form-row">
					<label>지시일</label><input type="date" name="workDate" required>
				</div>
				<div class="form-row">
					<label>지시수량</label><input type="number" step="0.001" name="workQty"
						required>
				</div>
				<div class="form-row">
					<label>상태</label><select name="status"><option
							value="READY">READY</option>
						<option value="APPROVED">APPROVED</option></select>
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
