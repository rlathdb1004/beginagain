<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<div class="taPageActions">
	<c:choose>
		<c:when test="${empty selectedItemId}">
			<button type="button" class="taBtn taBtnPrimary"
				onclick="alert('먼저 품목을 선택하세요.');">등록</button>
		</c:when>
		<c:otherwise>
			<button type="button" class="taBtn taBtnPrimary"
				data-modal-target="registerModal">등록</button>
		</c:otherwise>
	</c:choose>
	<button type="submit" form="deleteForm" class="taBtn taBtnOutline"
		onclick="return confirm('선택한 라우팅을 삭제하시겠습니까?');">선택 삭제</button>
</div>

<form method="get"
	action="${pageContext.request.contextPath}/routing/list"
	class="taLocalSearchForm">
	<div class="taToolbarRow">
		<div class="taToolbarField taToolbarFieldGrow taSpan2">
			<label style="display: block; margin-bottom: 8px; font-weight: 600;">품목
				선택</label> <select class="taSelect" name="itemId"
				onchange="this.form.submit()">
				<option value="">품목을 선택하세요</option>
				<c:forEach var="item" items="${itemList}">
					<c:if test="${item.itemType eq '완제품'}">
						<option value="${item.itemId}"
							${selectedItemId == item.itemId ? 'selected' : ''}>
							${item.itemCode} - ${item.itemName}</option>
					</c:if>
				</c:forEach>
			</select>
		</div>
	</div>
</form>

<c:if test="${not empty errorMessage}">
	<div style="margin: 12px 0; color: #d9534f; font-weight: 600;">${errorMessage}</div>
</c:if>

<form id="deleteForm"
	action="${pageContext.request.contextPath}/routing/delete"
	method="post">
	<input type="hidden" name="itemId" value="${selectedItemId}">
	<div class="taTableShell">
		<div class="taTableScroll">
			<table class="taMesTable taRoutingTable" id="routingTable">
				<thead>
					<tr>
						<th class="taTableHeadCell taCheckCell"><input
							type="checkbox" id="checkAll" class="taCheckInput"></th>
						<th class="taTableHeadCell taColFit">순서</th>
						<th class="taTableHeadCell taColGrow">공정명</th>
						<th class="taTableHeadCell taColGrow">설비명</th>
						<th class="taTableHeadCell taColAction taLastCol">상세</th>
					</tr>
				</thead>
				<tbody>
					<c:choose>
						<c:when test="${empty selectedItemId}">
							<tr class="taTableBodyRow">
								<td class="taTableBodyCell taLastCol" colspan="5"
									style="text-align: center;">품목을 선택하면 라우팅이 조회됩니다.</td>
							</tr>
						</c:when>
						<c:when test="${not empty routingList}">
							<c:forEach var="routing" items="${routingList}">
								<tr class="taTableBodyRow">
									<td class="taTableBodyCell taCheckCell"><input
										type="checkbox" name="routingId" value="${routing.routingId}"
										class="taCheckInput"></td>
									<td class="taTableBodyCell taColFit">${routing.processSeq}</td>
									<td class="taTableBodyCell taColGrow">${routing.processName}</td>
									<td class="taTableBodyCell taColGrow"><c:choose>
											<c:when test="${not empty routing.equipmentName}">
												${routing.equipmentName}
											</c:when>
											<c:otherwise>-</c:otherwise>
										</c:choose></td>
									<td class="taTableBodyCell taColAction taLastCol"><a
										class="taLinkAnchor"
										href="${pageContext.request.contextPath}/routing/detail?routingId=${routing.routingId}">
											상세보기 </a></td>
								</tr>
							</c:forEach>
						</c:when>
						<c:otherwise>
							<tr class="taTableBodyRow">
								<td class="taTableBodyCell taLastCol" colspan="5"
									style="text-align: center;">해당 품목의 라우팅 데이터가 없습니다.</td>
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
			<h3 class="taModalTitle">라우팅 등록</h3>
			<button type="button" class="taModalClose">&times;</button>
		</div>
		<form action="${pageContext.request.contextPath}/routing/register"
			method="post">
			<input type="hidden" name="itemId" value="${selectedItemId}">
			<div class="taModalBody taModalGrid">
				<div class="form-row full">
					<label>선택 품목</label>
					<div class="taReadonlyText">
						<c:forEach var="item" items="${itemList}">
							<c:if test="${item.itemId == selectedItemId}">
								${item.itemCode} - ${item.itemName}
							</c:if>
						</c:forEach>
					</div>
				</div>
				<div class="form-row">
					<label>공정</label> <select name="processId" required>
						<option value="">공정을 선택하세요</option>
						<c:forEach var="process" items="${processList}">
							<option value="${process.processId}">${process.processCode}
								- ${process.processName}</option>
						</c:forEach>
					</select>
				</div>
				<div class="form-row">
					<label>설비</label> <select name="equipmentId" required>
						<option value="">설비를 선택하세요</option>
						<c:forEach var="equipment" items="${equipmentList}">
							<option value="${equipment.equipmentId}">${equipment.equipmentCode}
								- ${equipment.equipmentName}</option>
						</c:forEach>
					</select>
				</div>
				<div class="form-row">
					<label>공정순서</label> <input type="number" name="processSeq" min="1"
						required>
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