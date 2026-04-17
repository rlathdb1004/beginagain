<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<style>
/* =========================
   Work Status Local Page
   ========================= */
.workStatusPage .taSummaryCards {
	display: grid;
	grid-template-columns: repeat(4, minmax(0, 1fr));
	gap: 14px;
	margin-bottom: 18px;
}

.workStatusPage .taSummaryCard {
	padding: 18px 20px;
	border: 1px solid #dbe3ef;
	border-radius: 18px;
	background: #f8fbff;
}

.workStatusPage .taSummaryLabel {
	font-size: 13px;
	color: #5e718d;
	margin-bottom: 8px;
}

.workStatusPage .taSummaryValue {
	font-size: 28px;
	font-weight: 800;
	color: #173b6c;
	line-height: 1;
}

.workStatusPage .taStatusBadge {
	display: inline-flex;
	align-items: center;
	justify-content: center;
	min-width: 68px;
	padding: 6px 10px;
	border-radius: 999px;
	font-size: 12px;
	font-weight: 700;
}

.workStatusPage .is-wait {
	background: #eef3fb;
	color: #5f6f86;
}

.workStatusPage .is-run {
	background: #e8f2ff;
	color: #1d63c9;
}

.workStatusPage .is-done {
	background: #e8f8ef;
	color: #1f8a4d;
}

.workStatusPage .is-overdue-row td {
	background: #fff8f8;
}

.workStatusPage .taMuted {
	color: #798aa4;
	font-size: 12px;
}

/* /* 검색창 한 줄 정렬 */ */
/* .workStatusPage .taToolbarRow { */
/* 	display: flex; */
/* 	align-items: center; */
/* 	gap: 10px; */
/* 	flex-wrap: nowrap; */
/* 	width: 100%; */
/* 	margin-bottom: 14px; */
/* } */

.workStatusPage .taToolbarField {
	display: flex;
	align-items: center;
	min-width: 0;
}

/* .workStatusPage .taToolbarFieldGrow { */
/* 	flex: 1; */
/* 	min-width: 0; */
/* } */

.workStatusPage .taSearchBox {
	display: flex;
	align-items: center;
	gap: 8px;
	width: 100%;
}

/* .workStatusPage .taSelect { */
/* 	min-width: 150px; */
/* 	height: 40px; */
/* } */

/* .workStatusPage .taSearchInput { */
/* 	flex: 1; */
/* 	min-width: 0; */
/* 	height: 40px; */
/* } */

.workStatusPage .taSearchBtn, .workStatusPage .taSearchReset {
	flex-shrink: 0;
	height: 40px;
}

/* 테이블 한 창 최적화 */
.workStatusPage .taTableShell {
	width: 100%;
	overflow: hidden;
	border-radius: 18px;
}

.workStatusPage .taTableScroll {
	width: 100%;
	overflow-x: hidden;
	overflow-y: auto;
}

.workStatusPage .taMesTable {
	width: 100%;
	table-layout: fixed;
	border-collapse: collapse;
	font-size: 12px;
}

.workStatusPage .taTableHeadCell, .workStatusPage .taTableBodyCell {
	padding: 10px 6px;
	white-space: nowrap;
	overflow: hidden;
	text-overflow: ellipsis;
	vertical-align: middle;
	box-sizing: border-box;
}

.workStatusPage .taTableHeadCell {
	font-weight: 700;
}

/* 컬럼 폭 조정 */
.workStatusPage .taMesTable th:nth-child(1), .workStatusPage .taMesTable td:nth-child(1)
	{
	width: 108px;
	min-width: 108px;
	max-width: 108px;
}

.workStatusPage .taMesTable th:nth-child(2), .workStatusPage .taMesTable td:nth-child(2)
	{
	width: 140px;
	min-width: 140px;
	max-width: 140px;
}

.workStatusPage .taMesTable th:nth-child(3), .workStatusPage .taMesTable td:nth-child(3)
	{
	width: 88px;
	min-width: 88px;
	max-width: 88px;
}

.workStatusPage .taMesTable th:nth-child(4), .workStatusPage .taMesTable td:nth-child(4)
	{
	width: 88px;
	min-width: 88px;
	max-width: 88px;
}

.workStatusPage .taMesTable th:nth-child(5), .workStatusPage .taMesTable td:nth-child(5)
	{
	width: 88px;
	min-width: 88px;
	max-width: 88px;
}

.workStatusPage .taMesTable th:nth-child(6), .workStatusPage .taMesTable td:nth-child(6)
	{
	width: 88px;
	min-width: 88px;
	max-width: 88px;
}

.workStatusPage .taMesTable th:nth-child(7), .workStatusPage .taMesTable td:nth-child(7)
	{
	width: 78px;
	min-width: 78px;
	max-width: 78px;
}

.workStatusPage .taMesTable th:nth-child(8), .workStatusPage .taMesTable td:nth-child(8)
	{
	width: 88px;
	min-width: 88px;
	max-width: 88px;
}

.workStatusPage .taMesTable th:nth-child(9), .workStatusPage .taMesTable td:nth-child(9)
	{
	width: 88px;
	min-width: 88px;
	max-width: 88px;
}

.workStatusPage .taMesTable th:nth-child(10), .workStatusPage .taMesTable td:nth-child(10)
	{
	width: 84px;
	min-width: 84px;
	max-width: 84px;
	text-align: center;
}

.workStatusPage .taLinkAnchor {
	display: inline-flex;
	align-items: center;
	justify-content: center;
	min-width: 58px;
	white-space: nowrap;
}

/* /* 페이징 */ */
/* .workStatusPage .taPagination { */
/* 	display: flex; */
/* 	justify-content: center; */
/* 	align-items: center; */
/* 	gap: 6px; */
/* 	padding-top: 12px; */
/* 	flex-wrap: wrap; */
/* } */

/* .workStatusPage .taPageBtn { */
/* 	min-width: 36px; */
/* 	height: 36px; */
/* 	padding: 0 10px; */
/* 	border: 1px solid #d7deea; */
/* 	border-radius: 8px; */
/* 	background: #fff; */
/* 	color: #243b63; */
/* 	text-decoration: none; */
/* 	display: inline-flex; */
/* 	align-items: center; */
/* 	justify-content: center; */
/* 	font-weight: 600; */
/* 	cursor: pointer; */
/* } */

/* .workStatusPage .taPageBtn.active { */
/* 	background: #0d47a1; */
/* 	color: #fff; */
/* 	border-color: #0d47a1; */
/* } */

/* .workStatusPage .taPageBtn:disabled { */
/* 	opacity: 0.5; */
/* 	cursor: default; */
/* } */

/* 반응형 */
/* @media ( max-width : 1100px) { */
/* 	.workStatusPage .taSummaryCards { */
/* 		grid-template-columns: repeat(2, minmax(0, 1fr)); */
/* 	} */
/* 	.workStatusPage .taToolbarRow { */
/* 		flex-wrap: wrap; */
/* 	} */
/* 	.workStatusPage .taToolbarField, .workStatusPage .taToolbarFieldGrow { */
/* 		width: 100%; */
/* 	} */
/* 	.workStatusPage .taTableScroll { */
/* 		overflow-x: auto; */
/* 	} */
/* 	.workStatusPage .taMesTable { */
/* 		min-width: 980px; */
/* 	} */
/* } */

/* @media ( max-width : 640px) { */
/* 	.workStatusPage .taSummaryCards { */
/* 		grid-template-columns: 1fr; */
/* 	} */
/* 	.workStatusPage .taSearchBox { */
/* 		flex-wrap: wrap; */
/* 	} */
/* 	.workStatusPage .taSelect, .workStatusPage .taSearchInput, */
/* 		.workStatusPage .taSearchBtn, .workStatusPage .taSearchReset { */
/* 		width: 100%; */
/* 	} */
/* } */
</style>

<div class="workStatusPage">
	<c:set var="totalCount" value="0" />
	<c:set var="waitingCount" value="0" />
	<c:set var="runningCount" value="0" />
	<c:set var="doneCount" value="0" />

	<!-- 페이징을 위해 주석처리함/령  -->
	<%--     <c:forEach var="ws" items="${workStatusList}"> --%>
	<%--         <c:set var="totalCount" value="${totalCount + 1}" /> --%>
	<%--         <c:choose> --%>
	<%--             <c:when test="${ws.progressStatus eq '완료'}"> --%>
	<%--                 <c:set var="doneCount" value="${doneCount + 1}" /> --%>
	<%--             </c:when> --%>
	<%--             <c:when test="${ws.progressStatus eq '진행중'}"> --%>
	<%--                 <c:set var="runningCount" value="${runningCount + 1}" /> --%>
	<%--             </c:when> --%>
	<%--             <c:otherwise> --%>
	<%--                 <c:set var="waitingCount" value="${waitingCount + 1}" /> --%>
	<%--             </c:otherwise> --%>
	<%--         </c:choose> --%>
	<%--     </c:forEach> --%>
	<div class="taSummaryCards">
		<div class="taSummaryCard">
			<div class="taSummaryLabel">전체 작업</div>
			<div class="taSummaryValue">${summaryTotalCount}</div>
		</div>
		<div class="taSummaryCard">
			<div class="taSummaryLabel">대기 작업</div>
			<div class="taSummaryValue">${summaryWaitingCount}</div>
		</div>
		<div class="taSummaryCard">
			<div class="taSummaryLabel">진행중 작업</div>
			<div class="taSummaryValue">${summaryRunningCount}</div>
		</div>
		<div class="taSummaryCard">
			<div class="taSummaryLabel">완료 작업</div>
			<div class="taSummaryValue">${summaryDoneCount}</div>
		</div>
	</div>

	<!-- 	<div class="taSummaryCards"> -->
	<!-- 		<div class="taSummaryCard"> -->
	<!-- 			<div class="taSummaryLabel">전체 작업</div> -->
	<%-- 			<div class="taSummaryValue">${totalCount}</div> --%>
	<!-- 		</div> -->
	<!-- 		<div class="taSummaryCard"> -->
	<!-- 			<div class="taSummaryLabel">대기 작업</div> -->
	<%-- 			<div class="taSummaryValue">${waitingCount}</div> --%>
	<!-- 		</div> -->
	<!-- 		<div class="taSummaryCard"> -->
	<!-- 			<div class="taSummaryLabel">진행중 작업</div> -->
	<%-- 			<div class="taSummaryValue">${runningCount}</div> --%>
	<!-- 		</div> -->
	<!-- 		<div class="taSummaryCard"> -->
	<!-- 			<div class="taSummaryLabel">완료 작업</div> -->
	<%-- 			<div class="taSummaryValue">${doneCount}</div> --%>
	<!-- 		</div> -->
	<!-- 	</div> -->
	<!-- 페이징떄문에 주석처러/령 -->
	<!-- 	<form class="taLocalSearchForm" id="workStatusSearchForm"> -->
	<form id="paSearchForm" method="get"
		action="${pageContext.request.contextPath}/workstatus">
		<input type="hidden" name="page" id="paPage" value="${paCurrentPage}">

		<div class="taToolbarRow">
			<div class="taToolbarField taToolbarSpan3">
				<!-- 			페이징때문에 주석처리함/령 -->
				<!-- 				<select class="taSelect" name="searchType" id="workStatusSearchType"> -->
				<!-- 					<option value="all">전체</option> -->
				<!-- 					<option value="workOrderDisplayCode">작업지시코드</option> -->
				<!-- 					<option value="itemName">품목명</option> -->
				<!-- 					<option value="empName">담당자</option> -->
				<!-- 					<option value="progressStatus">진행상태</option> -->
				<!-- 				</select> -->
				<select
					class="taSelect taAutoSelectColor ${empty searchType or searchType eq 'all' ? 'taSelectPlaceholder' : ''}"
					name="searchType" id="workStatusSearchType">
					<option value="" hidden
						<c:if test="${empty searchType or searchType eq 'all'}">selected</c:if>>
						전체 / 작업지시코드 ...</option>
					<option value="all"
						<c:if test="${searchType eq 'all'}">selected</c:if>>전체</option>
					<option value="workOrderDisplayCode"
						<c:if test="${searchType eq 'workOrderDisplayCode'}">selected</c:if>>
						작업지시코드</option>
					<option value="itemName"
						<c:if test="${searchType eq 'itemName'}">selected</c:if>>
						품목명</option>
					<option value="empName"
						<c:if test="${searchType eq 'empName'}">selected</c:if>>
						담당자</option>
					<option value="progressStatus"
						<c:if test="${searchType eq 'progressStatus'}">selected</c:if>>
						진행상태</option>
				</select>
			</div>

			<div class="taToolbarField taToolbarFieldGrow taToolbarSpan9">
				<div class="taSearchBox">
<!-- 					<input type="text" class="taSearchInput" name="keyword" -->
<%-- 						id="workStatusKeyword" value="${keyword}" placeholder="검색키워드"> --%>
					<!-- 				페이징떄문에 주석처리함 / 령 -->
					<!--                     <button type="submit" class="taSearchBtn">⌕</button> -->
					<input type="text" class="taSearchInput" name="keyword"
						id="workStatusKeyword" value="${keyword}"
						placeholder="작업지시코드, 품목, 담당자, 상태를 검색하세요">
					<button type="submit" class="taSearchBtn" aria-label="검색">
						<svg viewBox="0 0 24 24" fill="none" stroke="currentColor"
							stroke-width="2">
                        <circle cx="11" cy="11" r="7"></circle>
                        <path d="M20 20L16.65 16.65"></path></svg>
					</button>
					<!-- 					페이징 떄문에 주석처리함/령 -->
					<!-- 					<button type="button" class="taBtn taBtnOutline taSearchReset" -->
					<!-- 						id="workStatusResetBtn">초기화</button> -->
					<button type="button" class="taBtn taBtnOutline taSearchReset"
						id="workStatusResetBtn"
						onclick="location.href='${pageContext.request.contextPath}/workstatus'">
						초기화</button>
				</div>
			</div>
		</div>
	</form>

	<!-- 페이징떄문에 아이디 추가함 id="paTableBox" -->
	<div class="taTableShell" id="paTableBox">
		<div class="taTableScroll">
			<table class="taMesTable" id="workStatusTable">
				<thead>
					<tr>
						<th class="taTableHeadCell taColFit">작업지시코드</th>
						<th class="taTableHeadCell taColGrow">품목명</th>
						<th class="taTableHeadCell taColFit">담당자</th>
						<th class="taTableHeadCell taColDate">지시일</th>
						<th class="taTableHeadCell taColFit">계획수량</th>
						<th class="taTableHeadCell taColFit">실적수량</th>
						<th class="taTableHeadCell taColFit">손실수량</th>
						<th class="taTableHeadCell taColFit">진행률</th>
						<th class="taTableHeadCell taColFit">진행상태</th>
						<th class="taTableHeadCell taColAction taLastCol">상세</th>
					</tr>
				</thead>
				<tbody id="workStatusTableBody">
					<c:choose>
						<c:when test="${not empty workStatusList}">
							<c:forEach var="ws" items="${workStatusList}">
								<fmt:parseDate value="${ws.workDate}" pattern="yyyy-MM-dd"
									var="parsedWorkDate" type="date" />
								<jsp:useBean id="today" class="java.util.Date" />
								<tr
									class="taTableBodyRow ${parsedWorkDate lt today and ws.progressRate lt 100 ? 'is-overdue-row' : ''}">
									<td class="taTableBodyCell taColFit"
										data-search-key="workOrderDisplayCode">${ws.workOrderDisplayCode}</td>
									<td class="taTableBodyCell taColGrow"
										data-search-key="itemName">${ws.itemName}</td>
									<td class="taTableBodyCell taColFit" data-search-key="empName">${ws.empName}</td>
									<td class="taTableBodyCell taColDate">${ws.workDate}</td>
									<td class="taTableBodyCell taColFit"><fmt:formatNumber
											value="${ws.workQty}" pattern="#,##0.###" /></td>
									<td class="taTableBodyCell taColFit"><fmt:formatNumber
											value="${ws.producedQty}" pattern="#,##0.###" /></td>
									<td class="taTableBodyCell taColFit"><fmt:formatNumber
											value="${ws.lossQty}" pattern="#,##0.###" /></td>
									<td class="taTableBodyCell taColFit"
										style="text-align: center; font-weight: 700;"><fmt:formatNumber
											value="${ws.progressRate}" pattern="0.0" />%</td>
									<td class="taTableBodyCell taColFit"
										data-search-key="progressStatus"><span
										class="taStatusBadge ${ws.progressStatus eq '완료' ? 'is-done' : ws.progressStatus eq '진행중' ? 'is-run' : 'is-wait'}">
											${ws.progressStatus} </span></td>
									<td class="taTableBodyCell taColAction taLastCol"><a
										class="taLinkAnchor"
										href="${pageContext.request.contextPath}/workstatus/detail?workOrderId=${ws.workOrderId}">
											상세보기 </a></td>
								</tr>
							</c:forEach>
						</c:when>
					</c:choose>

					<tr class="taTableBodyRow" id="workStatusEmptyRow"
						style="display: none;">
						<td class="taTableBodyCell taLastCol" colspan="10"
							style="text-align: center;">조회된 작업 현황이 없습니다.</td>
					</tr>
				</tbody>
			</table>
		</div>
	</div>
	<!-- 페이징때문에 주석처리함/령 -->
	<!-- 	<div class="taPagination" id="workStatusPagination"></div> -->
</div>

<!-- <!-- <script> -->
<!-- // (function () { -->
<!-- //     const form = document.getElementById("workStatusSearchForm"); -->
<!-- //     const searchTypeEl = document.getElementById("workStatusSearchType"); -->
<!-- //     const keywordEl = document.getElementById("workStatusKeyword"); -->
<!-- //     const resetBtn = document.getElementById("workStatusResetBtn"); -->
<!-- //     const tbody = document.getElementById("workStatusTableBody"); -->
<!-- //     const emptyRow = document.getElementById("workStatusEmptyRow"); -->
<!-- //     const pagination = document.getElementById("workStatusPagination"); -->

<!-- //     if (!form || !tbody || !pagination) return; -->

<!-- //     // 한 페이지당 10줄 -->
<!-- //     const pageSize = 10; -->

<!-- //     // 페이지 번호는 5개씩 표시 -->
<!-- //     const pageBlock = 5; -->

<!-- //     let currentPage = 1; -->

<!-- //     // 검색 버튼을 눌렀는지 여부 -->
<!-- //     // false면 조회 전 상태 = 컬럼만 보이고 데이터는 숨김 -->
<!-- //     let hasSearched = false; -->

<!-- //     function getDataRows() { -->
<!-- //         return Array.from(tbody.querySelectorAll("tr.taTableBodyRow")) -->
<!-- //             .filter(row => row.id !== "workStatusEmptyRow"); -->
<!-- //     } -->

<!-- //     function getSearchValue(row, searchType) { -->
<!-- //         if (searchType === "all") { -->
<!-- //             return row.innerText.replace(/\s+/g, " ").toLowerCase(); -->
<!-- //         } -->

<!-- //         const cell = row.querySelector('[data-search-key="' + searchType + '"]'); -->
<!-- //         return cell ? cell.innerText.replace(/\s+/g, " ").toLowerCase() : ""; -->
<!-- //     } -->

<!-- //     function getFilteredRows() { -->
<!-- //         const rows = getDataRows(); -->
<!-- //         const searchType = searchTypeEl.value; -->
<!-- //         const keyword = keywordEl.value.trim().toLowerCase(); -->

<!-- //         // 검색어 없이 조회 버튼 누르면 전체 조회 -->
<!-- //         if (!keyword) return rows; -->

<!-- //         return rows.filter(row => { -->
<!-- //             const targetText = getSearchValue(row, searchType); -->
<!-- //             return targetText.includes(keyword); -->
<!-- //         }); -->
<!-- //     } -->

<!-- //     function hideAllRows() { -->
<!-- //         getDataRows().forEach(row => { -->
<!-- //             row.style.display = "none"; -->
<!-- //         }); -->
<!-- //     } -->

<!-- //     function renderPagination(totalItems) { -->
<!-- //         pagination.innerHTML = ""; -->

<!-- //         const totalPages = Math.ceil(totalItems / pageSize); -->

<!-- //         if (totalPages <= 1) return; -->

<!-- //         // 현재 페이지가 속한 5개 묶음 계산 -->
<!-- //         const currentBlock = Math.ceil(currentPage / pageBlock); -->
<!-- //         const startPage = (currentBlock - 1) * pageBlock + 1; -->
<!-- //         const endPage = Math.min(startPage + pageBlock - 1, totalPages); -->

<!-- //         // 이전 묶음 버튼 -->
<!-- //         const prevBtn = document.createElement("button"); -->
<!-- //         prevBtn.type = "button"; -->
<!-- //         prevBtn.className = "taPageBtn"; -->
<!-- //         prevBtn.textContent = "이전"; -->
<!-- //         prevBtn.disabled = startPage === 1; -->
<!-- //         prevBtn.addEventListener("click", function () { -->
<!-- //             if (startPage > 1) { -->
<!-- //                 currentPage = startPage - 1; -->
<!-- //                 render(); -->
<!-- //             } -->
<!-- //         }); -->
<!-- //         pagination.appendChild(prevBtn); -->

<!-- //         // 페이지 번호 5개씩 출력 -->
<!-- //         for (let i = startPage; i <= endPage; i++) { -->
<!-- //             const btn = document.createElement("button"); -->
<!-- //             btn.type = "button"; -->
<!-- //             btn.className = "taPageBtn" + (i === currentPage ? " active" : ""); -->
<!-- //             btn.textContent = i; -->
<!-- //             btn.addEventListener("click", function () { -->
<!-- //                 currentPage = i; -->
<!-- //                 render(); -->
<!-- //             }); -->
<!-- //             pagination.appendChild(btn); -->
<!-- //         } -->

<!-- //         // 다음 묶음 버튼 -->
<!-- //         const nextBtn = document.createElement("button"); -->
<!-- //         nextBtn.type = "button"; -->
<!-- //         nextBtn.className = "taPageBtn"; -->
<!-- //         nextBtn.textContent = "다음"; -->
<!-- //         nextBtn.disabled = endPage === totalPages; -->
<!-- //         nextBtn.addEventListener("click", function () { -->
<!-- //             if (endPage < totalPages) { -->
<!-- //                 currentPage = endPage + 1; -->
<!-- //                 render(); -->
<!-- //             } -->
<!-- //         }); -->
<!-- //         pagination.appendChild(nextBtn); -->
<!-- //     } -->

<!-- //     function render() { -->
<!-- //         hideAllRows(); -->
<!-- //         emptyRow.style.display = "none"; -->
<!-- //         pagination.innerHTML = ""; -->

<!-- //         // 조회 전이면 컬럼만 보이고 데이터/페이징은 숨김 -->
<!-- //         if (!hasSearched) { -->
<!-- //             return; -->
<!-- //         } -->

<!-- //         const filteredRows = getFilteredRows(); -->

<!-- //         if (filteredRows.length === 0) { -->
<!-- //             emptyRow.style.display = ""; -->
<!-- //             return; -->
<!-- //         } -->

<!-- //         const totalPages = Math.ceil(filteredRows.length / pageSize); -->
<!-- //         if (currentPage > totalPages) currentPage = 1; -->

<!-- //         const start = (currentPage - 1) * pageSize; -->
<!-- //         const end = start + pageSize; -->

<!-- //         filteredRows.slice(start, end).forEach(row => { -->
<!-- //             row.style.display = ""; -->
<!-- //         }); -->

<!-- //         renderPagination(filteredRows.length); -->
<!-- //     } -->

<!-- //     // 검색 버튼 클릭 시 조회 상태로 변경 -->
<!-- //     form.addEventListener("submit", function (e) { -->
<!-- //         e.preventDefault(); -->
<!-- //         hasSearched = true; -->
<!-- //         currentPage = 1; -->
<!-- //         render(); -->
<!-- //     }); -->

<!-- //     // 초기화 버튼 누르면 다시 조회 전 상태 -->
<!-- //     resetBtn.addEventListener("click", function () { -->
<!-- //         searchTypeEl.value = "all"; -->
<!-- //         keywordEl.value = ""; -->
<!-- //         hasSearched = false; -->
<!-- //         currentPage = 1; -->
<!-- //         render(); -->
<!-- //     }); -->

<!-- //     // 첫 화면: 컬럼만 보이게 -->
<!-- //     render(); -->
<!-- // })(); -->
<!-- <!-- </script> -->