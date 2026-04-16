<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:choose>
    <c:when test="${empty productionResult}">
        <div class="taFormShell taEmptyState">
            <p>조회된 생산실적 정보가 없습니다.</p>
            <div class="taPageActions" style="justify-content:center; margin-top:16px;">
                <a class="taBtn taBtnOutline" href="${pageContext.request.contextPath}/prodperf" style="text-decoration:none;">목록</a>
            </div>
        </div>
    </c:when>
    <c:otherwise>
        <form id="prodPerfUpdateForm" action="${pageContext.request.contextPath}/prodperf/update" method="post">
            <input type="hidden" name="seqNO" value="${productionResult.seqNO}">
            <div class="taPageActions">
                <button type="button" id="prodPerfUpdateFormEditBtn" class="taBtn taBtnPrimary">수정</button>
                <button type="submit" id="prodPerfUpdateFormSaveBtn" class="taBtn taBtnPrimary" style="display:none;">수정완료</button>
                <button type="button" id="prodPerfUpdateFormCancelBtn" class="taBtn taBtnOutline" style="display:none;">취소</button>
                <a class="taBtn taBtnOutline" href="${pageContext.request.contextPath}/prodperf" style="text-decoration:none;">목록</a>
            </div>
            <div class="taFormShell">
                <table class="taFormTable">
                    <tr><th class="taFormLabel">NO</th><td class="taFormValue"><span class="taReadonlyText">${productionResult.seqNO}</span></td></tr>
                    <tr><th class="taFormLabel">작업지시</th><td class="taFormValue">
                        <select class="taFormInput taEditableField" name="workOrderId" id="prodPerfWorkOrderSelect">
                            <c:forEach var="wo" items="${workOrderOptions}">
                                <option value="${wo.workOrderId}" data-item-code="${wo.itemCode}" data-item-name="${wo.itemName}" data-line-code="${wo.lineCode}" data-unit="${wo.unit}" ${wo.workOrderId eq productionResult.workOrderId ? 'selected' : ''}>${wo.workOrderNo} / ${wo.itemName} / ${wo.lineCode}</option>
                            </c:forEach>
                        </select>
                    </td></tr>
                    <tr><th class="taFormLabel">생산실적일자</th><td class="taFormValue"><input class="taFormInput taEditableField" type="date" name="resultDate" value="${productionResult.resultDate}"></td></tr>
                    <tr><th class="taFormLabel">품목코드</th><td class="taFormValue"><span class="taReadonlyText" id="prodPerfItemCode">${productionResult.itemCode}</span></td></tr>
                    <tr><th class="taFormLabel">품목명</th><td class="taFormValue"><span class="taReadonlyText" id="prodPerfItemName">${productionResult.itemName}</span></td></tr>
                    <tr><th class="taFormLabel">생산량</th><td class="taFormValue"><input class="taFormInput taEditableField" type="number" name="producedQty" min="0" value="${productionResult.producedQty}"></td></tr>
                    <tr><th class="taFormLabel">손실량</th><td class="taFormValue"><input class="taFormInput taEditableField" type="number" name="lossQty" min="0" value="${productionResult.lossQty}"></td></tr>
                    <tr><th class="taFormLabel">단위</th><td class="taFormValue"><span class="taReadonlyText" id="prodPerfUnit">${productionResult.unit}</span></td></tr>
                    <tr><th class="taFormLabel">라인</th><td class="taFormValue"><span class="taReadonlyText" id="prodPerfLineCode">${productionResult.lineCode}</span></td></tr>
                    <tr><th class="taFormLabel">LOT</th><td class="taFormValue"><input class="taFormInput taEditableField" type="text" name="lotNo" value="${productionResult.lotNo}"></td></tr>
                    <tr><th class="taFormLabel">상태</th><td class="taFormValue"><select class="taFormInput taEditableField" name="status"><option value="대기" ${productionResult.status eq '대기' ? 'selected' : ''}>대기</option><option value="진행중" ${productionResult.status eq '진행중' ? 'selected' : ''}>진행중</option><option value="완료" ${productionResult.status eq '완료' ? 'selected' : ''}>완료</option></select></td></tr>
                    <tr><th class="taFormLabel">비고</th><td class="taFormValue"><textarea class="taFormTextarea taEditableField" name="remark">${productionResult.remark}</textarea></td></tr>
                </table>
            </div>
        </form>
<script>
(function() {
    const form = document.getElementById("prodPerfUpdateForm");
    if (!form) return;
    const editBtn = document.getElementById("prodPerfUpdateFormEditBtn");
    const saveBtn = document.getElementById("prodPerfUpdateFormSaveBtn");
    const cancelBtn = document.getElementById("prodPerfUpdateFormCancelBtn");
    const fields = form.querySelectorAll('.taEditableField');
    const workOrderSelect = document.getElementById('prodPerfWorkOrderSelect');
    const itemCode = document.getElementById('prodPerfItemCode');
    const itemName = document.getElementById('prodPerfItemName');
    const lineCode = document.getElementById('prodPerfLineCode');
    const unit = document.getElementById('prodPerfUnit');

    function syncWorkOrderMeta() {
        const opt = workOrderSelect && workOrderSelect.options[workOrderSelect.selectedIndex];
        if (!opt) return;
        itemCode.textContent = opt.dataset.itemCode || '';
        itemName.textContent = opt.dataset.itemName || '';
        lineCode.textContent = opt.dataset.lineCode || '';
        unit.textContent = opt.dataset.unit || '';
    }

    function setViewMode() {
        fields.forEach(function(field) {
            if (field.tagName === 'SELECT') field.disabled = true;
            else field.readOnly = true;
        });
        editBtn.style.display = '';
        saveBtn.style.display = 'none';
        cancelBtn.style.display = 'none';
    }

    function setEditMode() {
        fields.forEach(function(field) {
            if (field.tagName === 'SELECT') field.disabled = false;
            else field.readOnly = false;
        });
        editBtn.style.display = 'none';
        saveBtn.style.display = '';
        cancelBtn.style.display = '';
    }

    fields.forEach(function(field) { field.dataset.originalValue = field.value; });
    workOrderSelect && workOrderSelect.addEventListener('change', syncWorkOrderMeta);
    editBtn.addEventListener('click', setEditMode);
    cancelBtn.addEventListener('click', function() {
        fields.forEach(function(field) { field.value = field.dataset.originalValue || ''; });
        syncWorkOrderMeta();
        setViewMode();
    });
    form.addEventListener('submit', function(e) {
        if (saveBtn.style.display === 'none') { e.preventDefault(); return; }
        fields.forEach(function(field) { if (field.tagName === 'SELECT') field.disabled = false; });
        if (!confirm('수정하시겠습니까?')) e.preventDefault();
    });
    syncWorkOrderMeta();
    setViewMode();
})();
</script>
    </c:otherwise>
</c:choose>
