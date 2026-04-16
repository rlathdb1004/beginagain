<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:choose>
    <c:when test="${empty workOrder}">
        <div class="taFormShell taEmptyState">
            <p>조회된 작업지시 정보가 없습니다.</p>
            <div class="taPageActions" style="justify-content:center; margin-top:16px;">
                <a class="taBtn taBtnOutline" href="${pageContext.request.contextPath}/woreginq" style="text-decoration:none;">목록</a>
            </div>
        </div>
    </c:when>
    <c:otherwise>
        <form id="woUpdateForm" action="${pageContext.request.contextPath}/woreginq/update" method="post">
            <input type="hidden" name="seqNO" value="${workOrder.seqNO}">
            <div class="taPageActions">
                <button type="button" id="woUpdateFormEditBtn" class="taBtn taBtnPrimary">수정</button>
                <button type="submit" id="woUpdateFormSaveBtn" class="taBtn taBtnPrimary" style="display:none;">수정완료</button>
                <button type="button" id="woUpdateFormCancelBtn" class="taBtn taBtnOutline" style="display:none;">취소</button>
                <a class="taBtn taBtnOutline" href="${pageContext.request.contextPath}/woreginq" style="text-decoration:none;">목록</a>
            </div>
            <div class="taFormShell">
                <table class="taFormTable">
                    <tr><th class="taFormLabel">NO</th><td class="taFormValue"><span class="taReadonlyText">${workOrder.seqNO}</span></td></tr>
                    <tr><th class="taFormLabel">작업지시번호</th><td class="taFormValue"><span class="taReadonlyText">${workOrder.workOrderNo}</span></td></tr>
                    <tr><th class="taFormLabel">생산계획</th><td class="taFormValue"><select class="taFormInput taEditableField" name="planId"><c:forEach var="plan" items="${planOptions}"><option value="${plan.planId}" ${plan.planId eq workOrder.planId ? 'selected' : ''}>계획 ${plan.planId} / ${plan.itemName} / ${plan.lineCode}</option></c:forEach></select></td></tr>
                    <tr><th class="taFormLabel">품목코드</th><td class="taFormValue"><span class="taReadonlyText">${workOrder.itemCode}</span></td></tr>
                    <tr><th class="taFormLabel">품목명</th><td class="taFormValue"><span class="taReadonlyText">${workOrder.itemName}</span></td></tr>
                    <tr><th class="taFormLabel">작업일자</th><td class="taFormValue"><input class="taFormInput taEditableField" type="date" name="workDate" value="${workOrder.workDate}"></td></tr>
                    <tr><th class="taFormLabel">지시량</th><td class="taFormValue"><input class="taFormInput taEditableField" type="number" name="workQty" min="0" value="${workOrder.workQty}"></td></tr>
                    <tr><th class="taFormLabel">단위</th><td class="taFormValue"><span class="taReadonlyText">${workOrder.unit}</span></td></tr>
                    <tr><th class="taFormLabel">라인</th><td class="taFormValue"><span class="taReadonlyText">${workOrder.lineCode}</span></td></tr>
                    <tr><th class="taFormLabel">공정</th><td class="taFormValue"><span class="taReadonlyText">${workOrder.processCode}</span></td></tr>
                    <tr><th class="taFormLabel">작업자</th><td class="taFormValue"><select class="taFormInput taEditableField" name="empId"><c:forEach var="emp" items="${empOptions}"><option value="${emp.empId}" ${emp.empId eq workOrder.empId ? 'selected' : ''}>${emp.empName} (${emp.empNo})</option></c:forEach></select></td></tr>
                    <tr><th class="taFormLabel">BOM</th><td class="taFormValue"><span class="taReadonlyText">${workOrder.bomCode}</span></td></tr>
                    <tr><th class="taFormLabel">상태</th><td class="taFormValue"><select class="taFormInput taEditableField" name="status"><option value="대기" ${workOrder.status eq '대기' ? 'selected' : ''}>대기</option><option value="진행중" ${workOrder.status eq '진행중' ? 'selected' : ''}>진행중</option><option value="완료" ${workOrder.status eq '완료' ? 'selected' : ''}>완료</option></select></td></tr>
                    <tr><th class="taFormLabel">비고</th><td class="taFormValue"><textarea class="taFormTextarea taEditableField" name="remark">${workOrder.remark}</textarea></td></tr>
                </table>
            </div>
        </form>
<script>
(function() {
    const form = document.getElementById("woUpdateForm");
    if (!form) return;

    const editBtn = document.getElementById("woUpdateFormEditBtn");
    const saveBtn = document.getElementById("woUpdateFormSaveBtn");
    const cancelBtn = document.getElementById("woUpdateFormCancelBtn");
    const fields = form.querySelectorAll('.taEditableField');

    function setViewMode() {
        fields.forEach(function(field) {
            if (field.tagName === 'SELECT') {
                field.disabled = true;
            } else {
                field.readOnly = true;
            }
        });
        editBtn.style.display = '';
        saveBtn.style.display = 'none';
        cancelBtn.style.display = 'none';
    }

    function setEditMode() {
        fields.forEach(function(field) {
            if (field.tagName === 'SELECT') {
                field.disabled = false;
            } else {
                field.readOnly = false;
            }
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
            if (field.tagName === 'SELECT') {
                field.disabled = false;
            }
        });
        if (!confirm('수정하시겠습니까?')) {
            e.preventDefault();
        }
    });

    setViewMode();
})();
</script>
    </c:otherwise>
</c:choose>
