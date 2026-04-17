<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<div class="taPageActions">
	<button type="button" class="taBtn taBtnPrimary"
		data-modal-target="registerModal">등록</button>
	<button type="submit" form="deleteForm" class="taBtn taBtnOutline"
		onclick="return confirm('선택한 사원을 삭제하시겠습니까?');">선택 삭제</button>
</div>
<!-- 페이징때문에 내용추가함 / 령 -->
<!-- <form class="taLocalSearchForm" data-table-id="memberTable"> -->
<form id="paSearchForm" method="get"
	action="${pageContext.request.contextPath}/member/list">
	<input type="hidden" name="page" id="paPage" value="${paCurrentPage}">

	<div class="taToolbarRow">
		<div class="taToolbarField">
			<!-- 		페이징때문에 주석처리함 /령 -->
			<!-- 			<select class="taSelect" name="searchType"><option -->
			<!-- 					value="all">전체</option> -->
			<!-- 				<option value="empNo">사번</option> -->
			<!-- 				<option value="empName">이름</option> -->
			<!-- 				<option value="deptCode">부서</option> -->
			<!-- 				<option value="positionName">직급</option></select> -->
			<select class="taSelect" name="searchType">
				<option value="all"
					${empty searchType or searchType eq 'all' ? 'selected' : ''}>전체</option>
				<option value="empNo" ${searchType eq 'empNo' ? 'selected' : ''}>사번</option>
				<option value="empName" ${searchType eq 'empName' ? 'selected' : ''}>이름</option>
				<option value="deptCode"
					${searchType eq 'deptCode' ? 'selected' : ''}>부서</option>
				<option value="positionName"
					${searchType eq 'positionName' ? 'selected' : ''}>직급</option>
			</select>
		</div>
		<div class="taToolbarField taToolbarFieldGrow"
			style="grid-column: span 3;">
			<div class="taSearchBox">
<!-- 			페이징때문에 아래 내용주석처리함 -->
<!-- 				<input type="text" class="taSearchInput" name="keyword" -->
<!-- 					placeholder="검색어를 입력하세요"> -->

				<input type="text" class="taSearchInput" name="keyword"
					value="${keyword}" placeholder="검색어를 입력하세요">
				<button type="submit" class="taSearchBtn">⌕</button>
<!-- 				페이징 때문에 주석처리함 -->
<!-- 				<button type="button" class="taBtn taBtnOutline taSearchReset">초기화</button> -->
			<button type="button"
        class="taBtn taBtnOutline taSearchReset"
        onclick="location.href='${pageContext.request.contextPath}/member/list'">
    초기화
</button>
			</div>
		</div>
	</div>
</form>
<form id="deleteForm"
	action="${pageContext.request.contextPath}/member/delete" method="post">
	<div class="taTableShell">
<!-- 	페이징때문에 id="paTableBox" 추가함  -->
		<div class="taTableScroll" id="paTableBox">
			<table class="taMesTable" id="memberTable">
				<thead>
					<tr>
						<th class="taTableHeadCell taCheckCell"><input
							type="checkbox" id="checkAll" class="taCheckInput"></th>
						<th class="taTableHeadCell taColFit">ID</th>
						<th class="taTableHeadCell taColFit">사번</th>
						<th class="taTableHeadCell taColFit">이름</th>
						<th class="taTableHeadCell taColFit">부서</th>
						<th class="taTableHeadCell taColFit">직급</th>
						<th class="taTableHeadCell taColFit">상태</th>
						<th class="taTableHeadCell taColFit">권한</th>
						<th class="taTableHeadCell taColFit">임시비밀번호</th>
						<th class="taTableHeadCell taColAction taLastCol">상세</th>
					</tr>
				</thead>
				<tbody>
					<c:choose>
						<c:when test="${not empty memberList}">
							<c:forEach var="m" items="${memberList}">
								<tr class="taTableBodyRow">
									<td class="taTableBodyCell taCheckCell"><input
										type="checkbox" name="empId" value="${m.empId}"
										class="taCheckInput"></td>
									<td class="taTableBodyCell taColFit">${m.empId}</td>
									<td class="taTableBodyCell taColFit" data-search-key="empNo">${m.empNo}</td>
									<td class="taTableBodyCell taColFit" data-search-key="empName">${m.empName}</td>
									<td class="taTableBodyCell taColFit" data-search-key="deptCode">${m.deptCode}</td>
									<td class="taTableBodyCell taColFit"
										data-search-key="positionName">${m.positionName}</td>
									<td class="taTableBodyCell taColFit">${m.status}</td>
									<td class="taTableBodyCell taColFit">${m.roleName}</td>
									<td class="taTableBodyCell taColFit">${m.tempPwdYn}</td>
									<td class="taTableBodyCell taColAction taLastCol"><a
										class="taLinkAnchor"
										href="${pageContext.request.contextPath}/member/detail?empId=${m.empId}">상세보기</a></td>
								</tr>
							</c:forEach>
						</c:when>
						<c:otherwise>
							<tr class="taTableBodyRow">
								<td class="taTableBodyCell taLastCol" colspan="10"
									style="text-align: center;">조회된 사원이 없습니다.</td>
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
			<h3 class="taModalTitle">사원 등록</h3>
			<button type="button" class="taModalClose">&times;</button>
		</div>
		<form action="${pageContext.request.contextPath}/member/register"
			method="post">
			<div class="taModalBody taModalGrid">
				<div class="form-row">
					<label>사번</label><input type="text" name="empNo" required>
				</div>
				<div class="form-row">
					<label>이름</label><input type="text" name="empName" required>
				</div>
				<div class="form-row">
					<label>부서코드</label><select name="deptCode"><option value="PROD">생산</option><option value="QUAL">품질</option><option value="MTRL">자재</option><option value="FAC">설비</option><option value="ADMIN">관리</option></select>
				</div>
				<div class="form-row">
					<label>직급</label><select name="positionName"><option value="사원">사원</option><option value="대리">대리</option><option value="과장">과장</option><option value="차장">차장</option><option value="부장">부장</option></select>
				</div>
				<div class="form-row">
					<label>이메일</label><input type="email" name="email">
				</div>
				<div class="form-row">
					<label>전화번호</label><input type="text" name="phone">
				</div>
				<div class="form-row">
					<label>상태</label><select name="status"><option value="재직" selected>재직</option><option value="휴직">휴직</option><option value="퇴사">퇴사</option></select>
				</div>
				<div class="form-row">
					<label>권한</label><select name="roleName"><option value="USER" selected>USER</option><option value="ADMIN">ADMIN</option></select>
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
