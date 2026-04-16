<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<%--
    작업지시 등록/조회 JSP

    화면 동작
    1. 처음 진입
       - searched 파라미터가 없으므로 검색창 + 테이블 헤더만 보임
    2. 검색 버튼 클릭
       - searched=Y 로 들어오고 Controller 가 list 를 채워서 전달
    3. 삭제 버튼
       - 1차 클릭 : 체크박스 컬럼 표시
       - 2차 클릭 : 선택 항목 삭제
    4. 상세보기 버튼
       - 상세 모달 또는 상세 페이지 연결용
--%>

<div class="woreg-page">

	<c:set var="isSearched" value="${not empty list}" />

	<%-- 상단 버튼 영역 --%>
	<div class="taPageActions">
		<button type="button" class="taBtn taBtnPrimary" data-modal-target="registerModal">등록</button>

		<%-- 삭제 버튼: 1차 클릭 시 체크박스 표시, 2차 클릭 시 삭제 --%>
		<button type="button" id="deleteToggleBtn" class="taBtn taBtnOutline"
			onclick="handleDeleteButton()">삭제</button>
	</div>

	<%-- 검색 폼 --%>
	<!--     페이징을 위해 아이디 추가함 id="paSearchForm" 또한 마지막 input 추가함 -->
	<form id="paSearchForm" method="get"
		action="${pageContext.request.contextPath}/woreginq">
		<%-- 검색 버튼 눌렀다는 표시 --%>
		<input type="hidden" name="searched" value="Y"> <input
			type="hidden" name="page" id="paPage" value="${paCurrentPage}">

		<div class="taToolbarRow">
			<%-- 기간 검색 --%>
			<div class="taToolbarField">
				<div class="taSearchBox">
					<input type="date" class="taSearchInput" name="startDate"
						value="${param.startDate}"> <input type="date"
						class="taSearchInput" name="endDate" value="${param.endDate}">
				</div>
			</div>

			<%-- 검색 기준 선택 --%>
			<div class="taToolbarField">
				<select class="taSelect" name="searchType">
					<option value="" ${empty param.searchType ? "selected" : ""}>전체</option>
					<option value="workOrderNo"
						${param.searchType eq 'workOrderNo' ? "selected" : ""}>작업지시번호</option>
					<option value="itemCode"
						${param.searchType eq 'itemCode' ? "selected" : ""}>품목코드</option>
					<option value="itemName"
						${param.searchType eq 'itemName' ? "selected" : ""}>품목명</option>
					<option value="lineCode"
						${param.searchType eq 'lineCode' ? "selected" : ""}>라인</option>
					<option value="processCode"
						${param.searchType eq 'processCode' ? "selected" : ""}>공정</option>
					<option value="empName"
						${param.searchType eq 'empName' ? "selected" : ""}>작업자</option>
					<option value="bomCode"
						${param.searchType eq 'bomCode' ? "selected" : ""}>BOM</option>
				</select>
			</div>

			<%-- 검색어 입력 --%>
			<div class="taToolbarField taToolbarFieldGrow">
				<div class="taSearchBox">
					<input type="text" class="taSearchInput" name="keyword"
						placeholder="작업지시번호 / 품목코드 / 품목명 / 라인 / 공정 / 작업자 / BOM 검색"
						value="${param.keyword}">

					<!--                     <button type="submit" class="taSearchBtn" aria-label="검색">⌕</button> -->
					<button type="submit" class="taSearchBtn" aria-label="검색">
						<svg viewBox="0 0 24 24" fill="none" stroke="currentColor"
							stroke-width="2">
                        <circle cx="11" cy="11" r="7"></circle>
                        <path d="M20 20L16.65 16.65"></path></svg>
					</button>
				</div>
			</div>
		</div>
	</form>

	<%-- 삭제용 폼 --%>
	<form id="deleteForm" method="post"
		action="${pageContext.request.contextPath}/woreginq">
		<%-- doPost에서 삭제 동작 구분용 --%>
		<input type="hidden" name="cmd" value="delete">

		<%-- 삭제 모드 상태값 --%>
		<input type="hidden" id="deleteMode" value="N">

		<%-- 삭제 후에도 검색조건 / 페이지 유지 --%>
		<input type="hidden" name="searched" value="${param.searched}">
		<input type="hidden" name="page" value="${page}"> <input
			type="hidden" name="startDate" value="${param.startDate}"> <input
			type="hidden" name="endDate" value="${param.endDate}"> <input
			type="hidden" name="searchType" value="${param.searchType}">
		<input type="hidden" name="keyword" value="${param.keyword}">

		<%-- 테이블 영역 --%>
		<!--         페이징을 위해 아이디 추가함 / 령 -->
		<div class="taTableShell woreg-table-shell" id="paTableBox">
			<div class="taTableScroll">
				<table class="taMesTable">
					<thead>
						<tr>
							<%--
                                삭제 체크박스 컬럼
                                CSS에서 기본 숨김, 삭제버튼 누르면 표시
                            --%>
							<th class="taTableHeadCell taColCheck delete-col"><input
								type="checkbox" id="checkAll"></th>

							<th class="taTableHeadCell taColFit">NO</th>
							<th class="taTableHeadCell taColFit">작업지시번호</th>
							<th class="taTableHeadCell taColDate">일자</th>
							<th class="taTableHeadCell taColFit">품목코드</th>
							<th class="taTableHeadCell taColGrow">품목명</th>
							<th class="taTableHeadCell taColFit">지시량(생산량)</th>
							<th class="taTableHeadCell taColFit">단위</th>
							<th class="taTableHeadCell taColFit">라인</th>
							<th class="taTableHeadCell taColFit">공정</th>
							<th class="taTableHeadCell taColFit">작업자</th>
							<th class="taTableHeadCell taColFit">BOM</th>
							<th class="taTableHeadCell taColGrow">비고</th>
							<th class="taTableHeadCell taColAction taLastCol">상세보기</th>
						</tr>
					</thead>

					<tbody>
						<c:forEach var="dto" items="${list}">
							<tr class="taTableBodyRow">
								<%-- 삭제 체크박스 --%>
								<td class="taTableBodyCell taColCheck delete-col"><input
									type="checkbox" name="seqNO" value="${dto.seqNO}"
									class="rowCheck"></td>

								<td class="taTableBodyCell taColFit">${dto.seqNO}</td>
								<td class="taTableBodyCell taColFit">${dto.workOrderNo}</td>
								<td class="taTableBodyCell taColDate">${dto.workDate}</td>
								<td class="taTableBodyCell taColFit">${dto.itemCode}</td>
								<td class="taTableBodyCell taColGrow">${dto.itemName}</td>
								<td class="taTableBodyCell taColFit">${dto.workQty}</td>
								<td class="taTableBodyCell taColFit">${dto.unit}</td>
								<td class="taTableBodyCell taColFit">${dto.lineCode}</td>
								<td class="taTableBodyCell taColFit">${dto.processCode}</td>
								<td class="taTableBodyCell taColFit">${dto.empName}</td>
								<td class="taTableBodyCell taColFit">${dto.bomCode}</td>

								<%-- 비고는 말줄임 처리되도록 클래스 유지 --%>
								<td class="taTableBodyCell taColGrow taRemarkCell"
									title="${dto.remark}">${dto.remark}</td>

								<%-- 상세보기는 마지막 컬럼으로 고정 --%>
								<td class="taTableBodyCell taColAction taLastCol">
									<a class="taLinkAnchor" href="${pageContext.request.contextPath}/woreginq/detail?seqNO=${dto.seqNO}">상세보기</a>
								</td>
							</tr>
						</c:forEach>

						<%-- 검색했는데 결과가 없을 때 --%>
						<c:if test="${empty list}">
							<tr class="taTableBodyRow">
								<td class="taTableBodyCell taLastCol" colspan="14"
									style="text-align: center;">조회된 데이터가 없습니다.</td>
							</tr>
						</c:if>
					</tbody>
				</table>
			</div>
		</div>
	</form>

	<!-- 페이징을 위해 아래 내용 주석/령 -->
	<%-- 검색했을 때만 페이징 표시 --%>
	<%--     <c:if test="${isSearched}"> --%>
	<!--         <div class="taPagination"> -->

	<%--             이전 블록 --%>
	<%--             <c:if test="${startPage > 1}"> --%>
	<%--                 <c:url var="prevUrl" value="/woreginq"> --%>
	<%--                     <c:param name="searched" value="Y" /> --%>
	<%--                     <c:param name="page" value="${startPage - 1}" /> --%>
	<%--                     <c:param name="startDate" value="${param.startDate}" /> --%>
	<%--                     <c:param name="endDate" value="${param.endDate}" /> --%>
	<%--                     <c:param name="searchType" value="${param.searchType}" /> --%>
	<%--                     <c:param name="keyword" value="${param.keyword}" /> --%>
	<%--                 </c:url> --%>
	<%--                 <a class="taPageBtn" href="${prevUrl}">이전</a> --%>
	<%--             </c:if> --%>

	<%--             페이지 번호 --%>
	<%--             <c:forEach var="i" begin="${startPage}" end="${endPage}"> --%>
	<%--                 <c:url var="pageUrl" value="/woreginq"> --%>
	<%--                     <c:param name="searched" value="Y" /> --%>
	<%--                     <c:param name="page" value="${i}" /> --%>
	<%--                     <c:param name="startDate" value="${param.startDate}" /> --%>
	<%--                     <c:param name="endDate" value="${param.endDate}" /> --%>
	<%--                     <c:param name="searchType" value="${param.searchType}" /> --%>
	<%--                     <c:param name="keyword" value="${param.keyword}" /> --%>
	<%--                 </c:url> --%>

	<%--                 <a class="${page eq i ? 'taPageBtn active' : 'taPageBtn'}" --%>
	<%--                    href="${pageUrl}"> --%>
	<%--                     ${i} --%>
	<!--                 </a> -->
	<%--             </c:forEach> --%>

	<%--             다음 블록 --%>
	<%--             <c:if test="${endPage < totalPage}"> --%>
	<%--                 <c:url var="nextUrl" value="/woreginq"> --%>
	<%--                     <c:param name="searched" value="Y" /> --%>
	<%--                     <c:param name="page" value="${endPage + 1}" /> --%>
	<%--                     <c:param name="startDate" value="${param.startDate}" /> --%>
	<%--                     <c:param name="endDate" value="${param.endDate}" /> --%>
	<%--                     <c:param name="searchType" value="${param.searchType}" /> --%>
	<%--                     <c:param name="keyword" value="${param.keyword}" /> --%>
	<%--                 </c:url> --%>
	<%--                 <a class="taPageBtn" href="${nextUrl}">다음</a> --%>
	<%--             </c:if> --%>

	<!--         </div> -->
	<%--     </c:if> --%>
</div>

<div class="taModal" id="registerModal" hidden aria-hidden="true">
	<div class="taModalDialog modal-lg">
		<div class="taModalHeader">
			<h3 class="taModalTitle">작업지시 등록</h3>
			<button type="button" class="taModalClose">&times;</button>
		</div>
		<form action="${pageContext.request.contextPath}/woreginq/register" method="post">
			<div class="taModalBody taModalGrid">
				<div class="form-row">
					<label>생산계획</label>
					<select name="planId" required>
						<option value="">선택</option>
						<c:forEach var="plan" items="${planOptions}">
							<option value="${plan.planId}">계획 ${plan.planId} / ${plan.itemName} / ${plan.lineCode}</option>
						</c:forEach>
					</select>
				</div>
				<div class="form-row">
					<label>작업자</label>
					<select name="empId" required>
						<option value="">선택</option>
						<c:forEach var="emp" items="${empOptions}">
							<option value="${emp.empId}">${emp.empName} (${emp.empNo})</option>
						</c:forEach>
					</select>
				</div>
				<div class="form-row">
					<label>작업일자</label><input type="date" name="workDate" required>
				</div>
				<div class="form-row">
					<label>지시량</label><input type="number" name="workQty" min="0" required>
				</div>
				<div class="form-row">
					<label>상태</label>
					<select name="status" required>
						<option value="대기">대기</option>
						<option value="진행중">진행중</option>
						<option value="완료">완료</option>
					</select>
				</div>
				<div class="form-row full">
					<label>비고</label><textarea name="remark"></textarea>
				</div>
			</div>
			<div class="taModalFooter">
				<button type="button" class="taBtn taBtnOutline taModalClose">취소</button>
				<button type="submit" class="taBtn taBtnPrimary">등록</button>
			</div>
		</form>
	</div>
</div>

<script>
	document.addEventListener("DOMContentLoaded", function() {
		const checkAll = document.getElementById("checkAll");
		const deleteModeInput = document.getElementById("deleteMode");
		const deleteToggleBtn = document.getElementById("deleteToggleBtn");
		const deleteForm = document.getElementById("deleteForm");

		// 전체선택 체크박스
		if (checkAll) {
			checkAll.addEventListener("change", function() {
				const rowChecks = document.querySelectorAll(".rowCheck");
				rowChecks.forEach(function(checkbox) {
					checkbox.checked = checkAll.checked;
				});
			});
		}

		// 삭제 버튼 처리
		window.handleDeleteButton = function() {
			const deleteCols = document.querySelectorAll(".delete-col");
			const rowChecks = document.querySelectorAll(".rowCheck");

			if (rowChecks.length === 0) {
			    alert("삭제할 데이터가 없습니다.");
			    return;
			}

			// 1차 클릭: 체크박스 보이기
			if (deleteModeInput.value === "N") {
				deleteCols.forEach(function(col) {
					col.classList.add("show-delete-col");
				});

				deleteModeInput.value = "Y";
				deleteToggleBtn.textContent = "삭제확인";
				return;
			}

			// 2차 클릭: 체크된 항목 확인
			let checkedCount = 0;
			rowChecks.forEach(function(checkbox) {
				if (checkbox.checked) {
					checkedCount++;
				}
			});

			// 선택 안 했으면 다시 숨김
			if (checkedCount === 0) {
				alert("삭제할 항목을 선택하세요.");

				if (checkAll) {
					checkAll.checked = false;
				}

				rowChecks.forEach(function(checkbox) {
					checkbox.checked = false;
				});

				deleteCols.forEach(function(col) {
					col.classList.remove("show-delete-col");
				});

				deleteModeInput.value = "N";
				deleteToggleBtn.textContent = "삭제";
				return;
			}

			if (confirm("선택한 작업지시를 삭제하시겠습니까?")) {
				deleteForm.submit();
			}
		};

		// 상세보기는 실제 상세 페이지로 이동
	});
</script>