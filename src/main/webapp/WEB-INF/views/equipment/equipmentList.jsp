<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<div class="taPageActions">
	<button type="button" class="taBtn taBtnPrimary"
		data-modal-target="registerModal">등록</button>
	<button type="submit" form="deleteForm" class="taBtn taBtnOutline"
		onclick="return confirm('선택한 설비를 삭제하시겠습니까?');">선택 삭제</button>
</div>
<!-- 페이징 기능을 위해 하단 코드 주석처리하고 하단에 내용 추가 /령 -->
<!-- <form class="taLocalSearchForm" data-table-id="equipmentTable"> -->
<!-- 	<div class="taToolbarRow"> -->
<!-- 		<div class="taToolbarField"> -->
<!-- 			<select class="taSelect" name="searchType"> -->
<!-- 				<option value="all">전체</option> -->
<!-- 				<option value="equipmentCode">설비코드</option> -->
<!-- 				<option value="equipmentName">설비명</option> -->
<!-- 				<option value="location">위치</option> -->
<!-- 				<option value="vendorName">공급업체</option> -->
<!-- 			</select> -->
<!-- 		</div> -->
<!-- 		<div class="taToolbarField taToolbarFieldGrow" -->
<!-- 			style="grid-column: span 3;"> -->
<!-- 			<div class="taSearchBox"> -->
<!-- 				<input type="text" class="taSearchInput" name="keyword" -->
<!-- 					placeholder="검색어를 입력하세요"> -->
<!-- 				<button type="submit" class="taSearchBtn">⌕</button> -->
<!-- 				<button type="button" class="taBtn taBtnOutline taSearchReset">초기화</button> -->
<!-- 			</div> -->
<!-- 		</div> -->
<!-- 	</div> -->
<!-- </form> -->
<form id="paSearchForm" method="get" action="${pageContext.request.contextPath}/equipment/list">
  <input type="hidden" name="page" id="paPage" value="${paCurrentPage}">

  <div class="taToolbarRow">
    <div class="taToolbarField">
      <select class="taSelect" name="searchType">
        <option value="all" ${empty searchType or searchType eq 'all' ? 'selected' : ''}>전체</option>
        <option value="equipmentCode" ${searchType eq 'equipmentCode' ? 'selected' : ''}>설비코드</option>
        <option value="equipmentName" ${searchType eq 'equipmentName' ? 'selected' : ''}>설비명</option>
        <option value="modelName" ${searchType eq 'modelName' ? 'selected' : ''}>모델명</option>
        <option value="location" ${searchType eq 'location' ? 'selected' : ''}>위치</option>
        <option value="manufacturer" ${searchType eq 'manufacturer' ? 'selected' : ''}>제조사</option>
        <option value="vendorName" ${searchType eq 'vendorName' ? 'selected' : ''}>공급업체</option>
      </select>
    </div>

    <div class="taToolbarField taToolbarFieldGrow" style="grid-column: span 3;">
      <div class="taSearchBox">
        <input type="text"
               class="taSearchInput"
               name="keyword"
               value="${keyword}"
               placeholder="검색어를 입력하세요">
        <button type="submit" class="taSearchBtn">⌕</button>
        <button type="button"
                class="taBtn taBtnOutline taSearchReset"
                onclick="location.href='${pageContext.request.contextPath}/equipment/list'">
          초기화
        </button>
      </div>
    </div>
  </div>
</form>
<form id="deleteForm"
	action="${pageContext.request.contextPath}/equipment/delete"
	method="post">
<!-- 	페이징을 위해 아이디 추가함 id="paTableBox" / 령 -->
	<div class="taTableShell" id="paTableBox">
		<div class="taTableScroll">
			<table class="taMesTable" id="equipmentTable">
				<thead>
					<tr>
						<th class="taTableHeadCell taCheckCell"><input
							type="checkbox" id="checkAll" class="taCheckInput"></th>
						<th class="taTableHeadCell taColFit">설비번호</th>
						<th class="taTableHeadCell taColFit">설비코드</th>
						<th class="taTableHeadCell taColGrow">설비명</th>
						<th class="taTableHeadCell taColFit">모델명</th>
						<th class="taTableHeadCell taColFit">위치</th>
						<th class="taTableHeadCell taColFit">제조사</th>
						<th class="taTableHeadCell taColFit">공급업체</th>
						<th class="taTableHeadCell taColFit">설비가격</th>
						<th class="taTableHeadCell taColAction taLastCol">상세</th>
					</tr>
				</thead>
				<tbody>
					<c:choose>
						<c:when test="${not empty equipmentList}">
							<c:forEach var="equipment" items="${equipmentList}">
								<tr class="taTableBodyRow">
									<td class="taTableBodyCell taCheckCell"><input
										type="checkbox" name="equipmentId"
										value="${equipment.equipmentId}" class="taCheckInput"></td>
									<td class="taTableBodyCell taColFit">${equipment.equipmentId}</td>
									<td class="taTableBodyCell taColFit"
										data-search-key="equipmentCode">${equipment.equipmentCode}</td>
									<td class="taTableBodyCell taColGrow"
										data-search-key="equipmentName">${equipment.equipmentName}</td>
									<td class="taTableBodyCell taColFit">${equipment.modelName}</td>
									<td class="taTableBodyCell taColFit" data-search-key="location">${equipment.location}</td>
									<td class="taTableBodyCell taColFit">${equipment.manufacturer}</td>
									<td class="taTableBodyCell taColFit"
										data-search-key="vendorName">${equipment.vendorName}</td>
									<td class="taTableBodyCell taColFit">${equipment.equipmentPrice}</td>
									<td class="taTableBodyCell taColAction taLastCol"><a
										class="taLinkAnchor"
										href="${pageContext.request.contextPath}/equipment/detail?equipmentId=${equipment.equipmentId}">상세보기</a></td>
								</tr>
							</c:forEach>
						</c:when>
						<c:otherwise>
							<tr class="taTableBodyRow">
								<td class="taTableBodyCell taLastCol" colspan="10"
									style="text-align: center;">조회된 설비가 없습니다.</td>
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
			<h3 class="taModalTitle">설비 등록</h3>
			<button type="button" class="taModalClose">&times;</button>
		</div>
		<form action="${pageContext.request.contextPath}/equipment/register"
			method="post">
			<div class="taModalBody taModalGrid">
				<div class="form-row">
					<label>설비코드</label><input type="text" name="equipmentCode" required>
				</div>
				<div class="form-row">
					<label>설비명</label><input type="text" name="equipmentName" required>
				</div>
				<div class="form-row">
					<label>모델명</label><input type="text" name="modelName">
				</div>
				<div class="form-row">
					<label>위치</label><input type="text" name="location">
				</div>
				<div class="form-row">
					<label>제조사</label><input type="text" name="manufacturer">
				</div>
				<div class="form-row">
					<label>공급업체</label><input type="text" name="vendorName">
				</div>
				<div class="form-row">
					<label>설비가격</label><input type="number" step="0.01"
						name="equipmentPrice">
				</div>
				<div class="form-row">
					<label>구매일자</label><input type="date" name="purchaseDate">
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
