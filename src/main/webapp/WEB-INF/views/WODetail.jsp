<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<c:choose>
	<c:when test="${empty workOrder}">
		<div class="taFormShell taEmptyState">
			<p>조회된 작업지시 정보가 없습니다.</p>
			<div class="taPageActions"
				style="justify-content: center; margin-top: 16px;">
				<a class="taBtn taBtnOutline"
					href="${pageContext.request.contextPath}/woreginq"
					style="text-decoration: none;">목록</a>
			</div>
		</div>
	</c:when>

	<c:otherwise>
		<c:if test="${not empty errorMsg}">
			<script>
				alert("${errorMsg}");
			</script>
		</c:if>

		<!-- 기본정보 -->
		<form id="woUpdateForm"
			action="${pageContext.request.contextPath}/woreginq/update"
			method="post">
			<input type="hidden" name="seqNO" value="${workOrder.seqNO}">
			<input type="hidden" name="planId" value="${workOrder.planId}">

			<div class="taPageActions">
				<button type="button" id="woUpdateFormEditBtn"
					class="taBtn taBtnPrimary">수정</button>
				<button type="submit" id="woUpdateFormSaveBtn"
					class="taBtn taBtnPrimary" style="display: none;">수정완료</button>
				<button type="button" id="woUpdateFormCancelBtn"
					class="taBtn taBtnOutline" style="display: none;">취소</button>
				<a class="taBtn taBtnOutline"
					href="${pageContext.request.contextPath}/woreginq"
					style="text-decoration: none;">목록</a>
			</div>

			<div class="taFormShell">
				<table class="taFormTable">
					<tr>
						<th class="taFormLabel">NO</th>
						<td class="taFormValue"><span class="taReadonlyText">${workOrder.seqNO}</span></td>
					</tr>
					<tr>
						<th class="taFormLabel">작업지시번호</th>
						<td class="taFormValue"><span class="taReadonlyText">${workOrder.workOrderNo}</span></td>
					</tr>
					<tr>
						<th class="taFormLabel">생산계획번호</th>
						<td class="taFormValue"><span class="taReadonlyText">${workOrder.planId}</span></td>
					</tr>
					<tr>
						<th class="taFormLabel">품목코드</th>
						<td class="taFormValue"><span class="taReadonlyText">${workOrder.itemCode}</span></td>
					</tr>
					<tr>
						<th class="taFormLabel">품목명</th>
						<td class="taFormValue"><span class="taReadonlyText">${workOrder.itemName}</span></td>
					</tr>
					<tr>
						<th class="taFormLabel">단위</th>
						<td class="taFormValue"><span class="taReadonlyText">${workOrder.unit}</span></td>
					</tr>
					<tr>
						<th class="taFormLabel">라인</th>
						<td class="taFormValue"><span class="taReadonlyText">${workOrder.lineCode}</span></td>
					</tr>
					<tr>
						<th class="taFormLabel">계획수량</th>
						<td class="taFormValue"><span class="taReadonlyText">${workOrder.planQty}</span></td>
					</tr>
					<tr>
						<th class="taFormLabel">현재 지시합계</th>
						<td class="taFormValue"><span class="taReadonlyText">${workOrder.currentWorkQtySum}</span></td>
					</tr>
					<tr>
						<th class="taFormLabel">남은 가능 수량</th>
						<td class="taFormValue"><span class="taReadonlyText">${workOrder.remainingQty}</span></td>
					</tr>
					<tr>
						<th class="taFormLabel">작업일자</th>
						<td class="taFormValue"><input
							class="taFormInput taEditableField" type="date" name="workDate"
							value="${workOrder.workDate}"></td>
					</tr>
					<tr>
						<th class="taFormLabel">지시량</th>
						<td class="taFormValue"><input
							class="taFormInput taEditableField" type="number" name="workQty"
							min="1" value="${workOrder.workQty}"></td>
					</tr>
					<tr>
						<th class="taFormLabel">작업자</th>
						<td class="taFormValue"><select
							class="taFormInput taEditableField" name="empId"><c:forEach
									var="emp" items="${empOptions}">
									<option value="${emp.empId}"
										${emp.empId eq workOrder.empId ? 'selected' : ''}>${emp.empName}
										(${emp.empNo})</option>
								</c:forEach></select></td>
					</tr>
					<tr>
						<th class="taFormLabel">상태</th>
						<td class="taFormValue"><select
							class="taFormInput taEditableField" name="status"><option
									value="대기" ${workOrder.status eq '대기' ? 'selected' : ''}>대기</option>
								<option value="진행중"
									${workOrder.status eq '진행중' ? 'selected' : ''}>진행중</option>
								<option value="완료" ${workOrder.status eq '완료' ? 'selected' : ''}>완료</option></select></td>
					</tr>
					<tr>
						<th class="taFormLabel">비고</th>
						<td class="taFormValue"><textarea
								class="taFormTextarea taEditableField" name="remark">${workOrder.remark}</textarea></td>
					</tr>
				</table>
			</div>
		</form>

		<!-- BOM 정보 -->
		<div class="taSectionStack" style="margin-top: 24px;">
			<div class="taPageActions"
				style="justify-content: space-between; align-items: center;">
				<h3 style="margin: 0; font-size: 18px;">BOM 정보</h3>
			</div>
			<div class="taTableShell">
				<div class="taTableScroll">
					<table class="taMesTable">
						<thead>
							<tr>
								<th class="taTableHeadCell taColFit">BOM ID</th>
								<th class="taTableHeadCell taColFit">상세 ID</th>
								<th class="taTableHeadCell taColFit">자재코드</th>
								<th class="taTableHeadCell taColGrow">자재명</th>
								<th class="taTableHeadCell taColFit">소요량</th>
								<th class="taTableHeadCell taColFit taLastCol">단위</th>
							</tr>
						</thead>
						<tbody>
							<c:choose>
								<c:when test="${not empty bomList}">
									<c:forEach var="bom" items="${bomList}">
										<tr class="taTableBodyRow">
											<td class="taTableBodyCell taColFit">${bom.bomId}</td>
											<td class="taTableBodyCell taColFit">${bom.bomDetailId}</td>
											<td class="taTableBodyCell taColFit">${bom.childItemCode}</td>
											<td class="taTableBodyCell taColGrow">${bom.childItemName}</td>
											<td class="taTableBodyCell taColFit">${bom.requiredQty}</td>
											<td class="taTableBodyCell taColFit taLastCol">${bom.unit}</td>
										</tr>
									</c:forEach>
								</c:when>
								<c:otherwise>
									<tr class="taTableBodyRow">
										<td class="taTableBodyCell taLastCol" colspan="6"
											style="text-align: center;">등록된 BOM 정보가 없습니다.</td>
									</tr>
								</c:otherwise>
							</c:choose>
						</tbody>
					</table>
				</div>
			</div>
		</div>

		<!-- 라우팅 정보 -->
		<div class="taSectionStack" style="margin-top: 24px;">
			<div class="taPageActions"
				style="justify-content: space-between; align-items: center;">
				<h3 style="margin: 0; font-size: 18px;">라우팅 정보</h3>
			</div>
			<div class="taTableShell">
				<div class="taTableScroll">
					<table class="taMesTable">
						<thead>
							<tr>
								<th class="taTableHeadCell taColFit">순서</th>
								<th class="taTableHeadCell taColFit">공정코드</th>
								<th class="taTableHeadCell taColGrow">공정명</th>
								<th class="taTableHeadCell taColFit">설비코드</th>
								<th class="taTableHeadCell taColGrow taLastCol">설비명</th>
							</tr>
						</thead>
						<tbody>
							<c:choose>
								<c:when test="${not empty routingList}">
									<c:forEach var="routing" items="${routingList}">
										<tr class="taTableBodyRow">
											<td class="taTableBodyCell taColFit">${routing.processSeq}</td>
											<td class="taTableBodyCell taColFit">${routing.processCode}</td>
											<td class="taTableBodyCell taColGrow">${routing.processName}</td>
											<td class="taTableBodyCell taColFit">${routing.equipmentCode}</td>
											<td class="taTableBodyCell taColGrow taLastCol">${routing.equipmentName}</td>
										</tr>
									</c:forEach>
								</c:when>
								<c:otherwise>
									<tr class="taTableBodyRow">
										<td class="taTableBodyCell taLastCol" colspan="5"
											style="text-align: center;">등록된 라우팅 정보가 없습니다.</td>
									</tr>
								</c:otherwise>
							</c:choose>
						</tbody>
					</table>
				</div>
			</div>
		</div>

		<script>
			(function() {
				const form = document.getElementById("woUpdateForm");
				if (!form)
					return;

				const editBtn = document.getElementById("woUpdateFormEditBtn");
				const saveBtn = document.getElementById("woUpdateFormSaveBtn");
				const cancelBtn = document
						.getElementById("woUpdateFormCancelBtn");
				const fields = form.querySelectorAll('.taEditableField');

				function setViewMode() {
					fields.forEach(function(field) {
						if (field.tagName === 'SELECT')
							field.disabled = true;
						else
							field.readOnly = true;
					});
					editBtn.style.display = '';
					saveBtn.style.display = 'none';
					cancelBtn.style.display = 'none';
				}

				function setEditMode() {
					fields.forEach(function(field) {
						if (field.tagName === 'SELECT')
							field.disabled = false;
						else
							field.readOnly = false;
					});
					editBtn.style.display = 'none';
					saveBtn.style.display = '';
					cancelBtn.style.display = '';
				}

				fields.forEach(function(field) {
					field.dataset.originalValue = field.value;
				});

				editBtn.addEventListener('click', function() {
					setEditMode();
				});
				cancelBtn.addEventListener('click', function() {
					fields.forEach(function(field) {
						field.value = field.dataset.originalValue || '';
					});
					setViewMode();
				});

				form.addEventListener('submit', function(e) {
					if (saveBtn.style.display === 'none') {
						e.preventDefault();
						return;
					}
					fields.forEach(function(field) {
						if (field.tagName === 'SELECT')
							field.disabled = false;
					});
					if (!confirm('수정하시겠습니까?'))
						e.preventDefault();
				});

				setViewMode();
			})();
		</script>
	</c:otherwise>
</c:choose>