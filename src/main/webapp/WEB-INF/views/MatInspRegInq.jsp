<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:if test="${not empty errorMsg}"><script>alert('${errorMsg}');</script></c:if>
<div class="taPageActions">
    <button type="button" class="taOpenModal taBtn taBtnPrimary" data-modal-target="matInspRegisterModal">등록</button>
    <button type="submit" form="matInspDeleteForm" class="taBtn taBtnOutline">선택 삭제</button>
</div>
<div id="matInspRegisterModal" class="taModal" hidden aria-hidden="true">
    <div class="taModalDialog">
        <div class="taModalHeader">
            <h3 class="taModalTitle">자재 검사 등록</h3>
            <button type="button" class="taModalClose" data-modal-target="matInspRegisterModal">×</button>
        </div>
        <form method="post" action="${pageContext.request.contextPath}/matInspRegInq">
            <input type="hidden" name="cmd" value="register">
            <div class="taModalBody taModalGrid">
                <div class="form-row">
                    <label>품목선택</label>
                    <select name="itemId" id="matItemId" class="taSelect" required>
                        <option value="">자재 품목을 선택하세요</option>
                        <c:forEach var="item" items="${itemList}">
                            <option value="${item.itemId}" data-code="${item.itemCode}" data-name="${item.itemName}" data-unit="${item.unit}">${item.itemCode} - ${item.itemName}</option>
                        </c:forEach>
                    </select>
                </div>
                <div class="form-row"><label>품목코드</label><input type="text" id="matItemCode" readonly></div>
                <div class="form-row"><label>품목명</label><input type="text" id="matItemName" readonly></div>
                <div class="form-row"><label>단위</label><input type="text" id="matUnit" readonly></div>
                <div class="form-row"><label>검사수량</label><input type="number" step="0.001" min="0.001" name="inspectQty" required></div>
                <div class="form-row"><label>양품수량</label><input type="number" step="0.001" min="0" name="goodQty" required></div>
                <div class="form-row"><label>불량수량</label><input type="number" step="0.001" min="0" name="defectQty" required></div>
                <div class="form-row"><label>판정</label><select name="result" class="taSelect"><option value="합격">합격</option><option value="부분합격">부분합격</option><option value="불합격">불합격</option></select></div>
                <div class="form-row"><label>검사일</label><input type="date" name="inspectionDate" required></div>
                <div class="form-row full"><label>비고</label><textarea name="remark"></textarea></div>
            </div>
            <div class="taModalFooter">
                <button type="button" class="taBtn taBtnOutline taModalClose">취소</button>
                <button type="submit" class="taBtn taBtnPrimary">저장</button>
            </div>
        </form>
    </div>
</div>

<form id="paSearchForm" method="post" action="${pageContext.request.contextPath}/matInspRegInq">
    <input type="hidden" name="cmd" value="list">
    <input type="hidden" name="page" id="paPage" value="${paCurrentPage}">
    <div class="taToolbarRow">
        <div class="taToolbarField taToolbarSpan2">
            <select class="taSelect taAutoSelectColor ${empty matInspRegInqSearchDTO.resultType or matInspRegInqSearchDTO.resultType eq '전체' ? 'taSelectPlaceholder' : ''}" name="resultType">
                <option value="" hidden <c:if test="${empty matInspRegInqSearchDTO.resultType or matInspRegInqSearchDTO.resultType eq '전체'}">selected</c:if>>전체 / 합격 / 부분합격 / 불합격</option>
                <option value="all">전체</option>
                <option value="합격" <c:if test="${matInspRegInqSearchDTO.resultType eq '합격'}">selected</c:if>>합격</option>
                <option value="부분합격" <c:if test="${matInspRegInqSearchDTO.resultType eq '부분합격'}">selected</c:if>>부분합격</option>
                <option value="불합격" <c:if test="${matInspRegInqSearchDTO.resultType eq '불합격'}">selected</c:if>>불합격</option>
            </select>
        </div>
        <div class="taToolbarField taToolbarSpan2">
            <select class="taSelect taAutoSelectColor ${empty matInspRegInqSearchDTO.searchType ? 'taSelectPlaceholder' : ''}" name="searchType">
                <option value="" hidden <c:if test="${empty matInspRegInqSearchDTO.searchType}">selected</c:if>>전체 / 품목코드 / 품목명</option>
                <option value="all">전체</option>
                <option value="itemCode" <c:if test="${matInspRegInqSearchDTO.searchType eq 'itemCode'}">selected</c:if>>품목코드</option>
                <option value="itemName" <c:if test="${matInspRegInqSearchDTO.searchType eq 'itemName'}">selected</c:if>>품목명</option>
            </select>
        </div>
        <div class="taToolbarField taToolbarSpan2"><input type="date" class="taSearchInput" name="startDate" value="${matInspRegInqSearchDTO.startDate}"></div>
        <div class="taToolbarField taToolbarSpan2"><input type="date" class="taSearchInput" name="endDate" value="${matInspRegInqSearchDTO.endDate}"></div>
        <div class="taToolbarField taToolbarFieldGrow taToolbarSpan4">
            <div class="taSearchBox">
                <input type="text" class="taSearchInput" name="keyword" placeholder="검색키워드" value="${matInspRegInqSearchDTO.keyword}">
                <button type="submit" class="taSearchBtn" aria-label="검색" onclick="document.getElementById('paPage').value=1;">
                    <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><circle cx="11" cy="11" r="7"></circle><path d="M20 20L16.65 16.65"></path></svg>
                </button>
                <button type="button" class="taBtn taBtnOutline taSearchReset" onclick="location.href='${pageContext.request.contextPath}/matInspRegInq'">초기화</button>
            </div>
        </div>
    </div>
</form>

<form id="matInspDeleteForm" method="post" action="${pageContext.request.contextPath}/matInspRegInq">
    <input type="hidden" name="cmd" value="delete">
    <div class="taTableShell" id="paTableBox">
        <div class="taTableScroll">
            <table class="taMesTable">
                <thead>
                    <tr>
                        <th class="taTableHeadCell taColFit"><input type="checkbox" id="checkAll" class="taCheckInput"></th>
                        <th class="taTableHeadCell taColFit">NO</th>
                        <th class="taTableHeadCell taColFit">품목코드</th>
                        <th class="taTableHeadCell taColGrow">품목명</th>
                        <th class="taTableHeadCell taColFit">단위</th>
                        <th class="taTableHeadCell taColFit">검사수량</th>
                        <th class="taTableHeadCell taColFit">양품수량</th>
                        <th class="taTableHeadCell taColFit">불량수량</th>
                        <th class="taTableHeadCell taColFit">판정</th>
                        <th class="taTableHeadCell taColDate">검사일</th>
                        <th class="taTableHeadCell taColAction taLastCol">상세보기</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach var="dto" items="${matInspRegInqList}">
                        <tr class="taTableBodyRow">
                            <td class="taTableBodyCell taColFit"><input type="checkbox" class="taCheckInput" name="materialInspectionIds" value="${dto.materialInspectionId}"></td>
                            <td class="taTableBodyCell taColFit">${dto.materialInspectionId}</td>
                            <td class="taTableBodyCell taColFit">${dto.itemCode}</td>
                            <td class="taTableBodyCell taColGrow">${dto.itemName}</td>
                            <td class="taTableBodyCell taColFit">${dto.unit}</td>
                            <td class="taTableBodyCell taColFit">${dto.inspectQty}</td>
                            <td class="taTableBodyCell taColFit">${dto.goodQty}</td>
                            <td class="taTableBodyCell taColFit">${dto.defectQty}</td>
                            <td class="taTableBodyCell taColFit">${dto.result}</td>
                            <td class="taTableBodyCell taColDate">${dto.inspectionDate}</td>
                            <td class="taTableBodyCell taColAction taLastCol"><a class="taLinkAnchor" href="${pageContext.request.contextPath}/matInspRegInq?materialInspectionId=${dto.materialInspectionId}">상세보기</a></td>
                        </tr>
                    </c:forEach>
                    <c:if test="${empty matInspRegInqList}"><tr class="taTableBodyRow"><td class="taTableBodyCell taLastCol" colspan="11" style="text-align:center;">조회된 데이터가 없습니다.</td></tr></c:if>
                </tbody>
            </table>
        </div>
    </div>
</form>
<script>
(function(){
  const itemSelect=document.getElementById('matItemId');
  const code=document.getElementById('matItemCode');
  const name=document.getElementById('matItemName');
  const unit=document.getElementById('matUnit');
  if(!itemSelect) return;
  itemSelect.addEventListener('change', function(){
    const opt=this.options[this.selectedIndex];
    if(!opt || !opt.value){ code.value=''; name.value=''; unit.value=''; return; }
    code.value=opt.dataset.code || '';
    name.value=opt.dataset.name || '';
    unit.value=opt.dataset.unit || '';
  });
})();
</script>
