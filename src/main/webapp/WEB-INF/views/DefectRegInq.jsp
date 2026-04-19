<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:if test="${not empty errorMsg}"><script>alert('${errorMsg}');</script></c:if>
<div class="taPageActions">
    <button type="button" class="taOpenModal taBtn taBtnPrimary" data-modal-target="defectRegisterModal">등록</button>
    <button type="submit" form="defectDeleteForm" class="taBtn taBtnOutline">선택 삭제</button>
</div>
<div id="defectRegisterModal" class="taModal" hidden aria-hidden="true">
    <div class="taModalDialog">
        <div class="taModalHeader"><h3 class="taModalTitle">불량 등록</h3><button type="button" class="taModalClose">×</button></div>
        <form method="post" action="${pageContext.request.contextPath}/defectRegInq">
            <input type="hidden" name="cmd" value="register">
            <div class="taModalBody taModalGrid">
                <div class="form-row full"><label>완제품검사 선택</label>
                    <select name="finalInspectionId" id="defectInspectionSelect" class="taSelect" required>
                        <option value="">완제품검사를 선택하세요</option>
                        <c:forEach var="insp" items="${inspectionList}">
                            <option value="${insp.finalInspectionId}"
                                data-result-id="${insp.resultId}"
                                data-work-order-id="${insp.workOrderId}"
                                data-plan-id="${insp.planId}"
                                data-item-code="${insp.itemCode}"
                                data-item-name="${insp.itemName}"
                                data-lot-no="${insp.lotNo}"
                                data-inspect-qty="${insp.inspectQty}"
                                data-status="${insp.inspectionStatus}"
                                data-defect-count="${insp.defectCodeCount}">
                                [검사 ${insp.finalInspectionId}] ${insp.itemCode} / ${insp.itemName} / LOT ${insp.lotNo}
                            </option>
                        </c:forEach>
                    </select>
                </div>
                <div class="form-row"><label>생산실적번호</label><input type="text" id="defectResultId" readonly></div>
                <div class="form-row"><label>작업지시번호</label><input type="text" id="defectWorkOrderId" readonly></div>
                <div class="form-row"><label>품목코드</label><input type="text" id="defectItemCode" readonly></div>
                <div class="form-row"><label>품목명</label><input type="text" id="defectItemName" readonly></div>
                <div class="form-row"><label>LOT번호</label><input type="text" id="defectLotNo" readonly></div>
                <div class="form-row"><label>검사수량(참고)</label><input type="text" id="defectInspectQty" readonly></div>
                <div class="form-row"><label>검사판정</label><input type="text" id="defectInspectionStatus" readonly></div>
                <div class="form-row"><label>등록된 불량코드 수</label><input type="text" id="defectCodeCount" readonly></div>
                <div class="form-row full"><label>불량코드 선택</label>
                    <select name="defectCodeId" id="defectCodeSelect" class="taSelect" required>
                        <option value="">불량코드를 선택하세요</option>
                        <c:forEach var="code" items="${defectCodeList}">
                            <option value="${code.defectCodeId}"
                                data-defect-code="${code.defectCode}"
                                data-defect-name="${code.defectName}"
                                data-defect-type="${code.defectType}">
                                ${code.defectCode} / ${code.defectName}
                            </option>
                        </c:forEach>
                    </select>
                </div>
                <div class="form-row"><label>불량코드</label><input type="text" id="selectedDefectCode" readonly></div>
                <div class="form-row"><label>불량명</label><input type="text" id="selectedDefectName" readonly></div>
                <div class="form-row"><label>유형</label><input type="text" id="selectedDefectType" readonly></div>
                <div class="form-row full"><label>비고</label><textarea name="remark"></textarea></div>
            </div>
            <div class="taModalFooter"><button type="button" class="taBtn taBtnOutline taModalClose">취소</button><button type="submit" class="taBtn taBtnPrimary">저장</button></div>
        </form>
    </div>
</div>

<form id="paSearchForm" method="post" action="${pageContext.request.contextPath}/defectRegInq">
    <input type="hidden" name="cmd" value="list"><input type="hidden" name="page" id="paPage" value="${paCurrentPage}">
    <div class="taToolbarRow">
        <div class="taToolbarField taToolbarSpan2">
            <select class="taSelect taAutoSelectColor ${empty defectRegInqSearchDTO.defectTypeSearch or defectRegInqSearchDTO.defectTypeSearch eq '전체' ? 'taSelectPlaceholder' : ''}" name="defectTypeSearch">
                <option value="" disabled hidden <c:if test="${empty defectRegInqSearchDTO.defectTypeSearch or defectRegInqSearchDTO.defectTypeSearch eq '전체'}">selected</c:if>>전체 / 공정 / 외관 / 치수</option>
                <option value="전체" <c:if test="${defectRegInqSearchDTO.defectTypeSearch eq '전체'}">selected</c:if>>전체</option>
                <option value="공정" <c:if test="${defectRegInqSearchDTO.defectTypeSearch eq '공정'}">selected</c:if>>공정</option>
                <option value="외관" <c:if test="${defectRegInqSearchDTO.defectTypeSearch eq '외관'}">selected</c:if>>외관</option>
                <option value="치수" <c:if test="${defectRegInqSearchDTO.defectTypeSearch eq '치수'}">selected</c:if>>치수</option>
            </select>
        </div>
        <div class="taToolbarField taToolbarSpan2">
            <select class="taSelect taAutoSelectColor ${empty defectRegInqSearchDTO.searchType ? 'taSelectPlaceholder' : ''}" name="searchType">
                <option value="" disabled hidden <c:if test="${empty defectRegInqSearchDTO.searchType}">selected</c:if>>전체 / 품목코드 ...</option>
                <option value="all" <c:if test="${defectRegInqSearchDTO.searchType eq 'all'}">selected</c:if>>전체</option>
                <option value="itemCode" <c:if test="${defectRegInqSearchDTO.searchType eq 'itemCode'}">selected</c:if>>품목코드</option>
                <option value="itemName" <c:if test="${defectRegInqSearchDTO.searchType eq 'itemName'}">selected</c:if>>품목명</option>
                <option value="defectCode" <c:if test="${defectRegInqSearchDTO.searchType eq 'defectCode'}">selected</c:if>>불량코드</option>
                <option value="defectName" <c:if test="${defectRegInqSearchDTO.searchType eq 'defectName'}">selected</c:if>>불량명</option>
            </select>
        </div>
        <div class="taToolbarField taToolbarSpan2"><input type="date" class="taSearchInput" name="startDate" value="${defectRegInqSearchDTO.startDate}"></div>
        <div class="taToolbarField taToolbarSpan2"><input type="date" class="taSearchInput" name="endDate" value="${defectRegInqSearchDTO.endDate}"></div>
        <div class="taToolbarField taToolbarFieldGrow taToolbarSpan4"><div class="taSearchBox"><input type="text" class="taSearchInput" name="keyword" placeholder="검색키워드" value="${defectRegInqSearchDTO.keyword}"><button type="submit" class="taSearchBtn" aria-label="검색" onclick="document.getElementById('paPage').value=1;"><svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><circle cx="11" cy="11" r="7"></circle><path d="M20 20L16.65 16.65"></path></svg></button><button type="button" class="taBtn taBtnOutline taSearchReset" onclick="location.href='${pageContext.request.contextPath}/defectRegInq'">초기화</button></div></div>
    </div>
</form>

<form id="defectDeleteForm" method="post" action="${pageContext.request.contextPath}/defectRegInq">
    <input type="hidden" name="cmd" value="delete">
    <div class="taTableShell" id="paTableBox"><div class="taTableScroll"><table class="taMesTable"><thead><tr>
        <th class="taTableHeadCell taColFit"><input type="checkbox" id="checkAll" class="taCheckInput"></th>
        <th class="taTableHeadCell taColFit">NO</th>
        <th class="taTableHeadCell taColFit">완제품검사번호</th>
        <th class="taTableHeadCell taColFit">품목코드</th>
        <th class="taTableHeadCell taColGrow">품목명</th>
        <th class="taTableHeadCell taColFit">LOT번호</th>
        <th class="taTableHeadCell taColFit">검사판정</th>
        <th class="taTableHeadCell taColFit">불량코드</th>
        <th class="taTableHeadCell taColFit">불량명</th>
        <th class="taTableHeadCell taColAction taLastCol">상세보기</th>
    </tr></thead><tbody>
        <c:forEach var="dto" items="${defectRegInqList}"><tr class="taTableBodyRow">
            <td class="taTableBodyCell taColFit"><input type="checkbox" class="taCheckInput" name="defectProductIds" value="${dto.defectProductId}"></td>
            <td class="taTableBodyCell taColFit">${dto.defectProductId}</td>
            <td class="taTableBodyCell taColFit">${dto.finalInspectionId}</td>
            <td class="taTableBodyCell taColFit">${dto.itemCode}</td>
            <td class="taTableBodyCell taColGrow">${dto.itemName}</td>
            <td class="taTableBodyCell taColFit">${dto.lotNo}</td>
            <td class="taTableBodyCell taColFit">${dto.inspectionStatus}</td>
            <td class="taTableBodyCell taColFit">${dto.defectCode}</td>
            <td class="taTableBodyCell taColFit">${dto.defectName}</td>
            <td class="taTableBodyCell taColAction taLastCol"><a class="taLinkAnchor" href="${pageContext.request.contextPath}/defectRegInq?defectProductId=${dto.defectProductId}">상세보기</a></td>
        </tr></c:forEach>
        <c:if test="${empty defectRegInqList}"><tr class="taTableBodyRow"><td class="taTableBodyCell taLastCol" colspan="10" style="text-align:center;">조회된 데이터가 없습니다.</td></tr></c:if>
    </tbody></table></div></div>
</form>
<script>
(function(){
  const insp = document.getElementById('defectInspectionSelect');
  const code = document.getElementById('defectCodeSelect');
  function fillInspection(){
    const opt = insp && insp.options[insp.selectedIndex];
    document.getElementById('defectResultId').value = opt && opt.value ? (opt.dataset.resultId || '') : '';
    document.getElementById('defectWorkOrderId').value = opt && opt.value ? (opt.dataset.workOrderId || '') : '';
    document.getElementById('defectItemCode').value = opt && opt.value ? (opt.dataset.itemCode || '') : '';
    document.getElementById('defectItemName').value = opt && opt.value ? (opt.dataset.itemName || '') : '';
    document.getElementById('defectLotNo').value = opt && opt.value ? (opt.dataset.lotNo || '') : '';
    document.getElementById('defectInspectQty').value = opt && opt.value ? (opt.dataset.inspectQty || '') : '';
    document.getElementById('defectInspectionStatus').value = opt && opt.value ? (opt.dataset.status || '') : '';
    document.getElementById('defectCodeCount').value = opt && opt.value ? (opt.dataset.defectCount || '0') : '';
  }
  function fillCode(){
    const opt = code && code.options[code.selectedIndex];
    document.getElementById('selectedDefectCode').value = opt && opt.value ? (opt.dataset.defectCode || '') : '';
    document.getElementById('selectedDefectName').value = opt && opt.value ? (opt.dataset.defectName || '') : '';
    document.getElementById('selectedDefectType').value = opt && opt.value ? (opt.dataset.defectType || '') : '';
  }
  if (insp) insp.addEventListener('change', fillInspection);
  if (code) code.addEventListener('change', fillCode);
})();
</script>
