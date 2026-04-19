<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page contentType="text/html; charset=UTF-8"%>
<c:if test="${not empty errorMsg}"><script>alert('${errorMsg}');</script></c:if>
<form id="failureActionUpdateForm" action="${pageContext.request.contextPath}/failureaction/update" method="post">
    <input type="hidden" name="failureActionId" value="${failureAction.failureActionId}">
    <input type="hidden" name="maintenanceId" value="${failureAction.maintenanceId}">
    <div class="taPageActions">
        <button type="button" id="failureActionUpdateFormEditBtn" class="taBtn taBtnPrimary">수정</button>
        <button type="submit" id="failureActionUpdateFormSaveBtn" class="taBtn taBtnPrimary" style="display:none;">수정완료</button>
        <button type="button" id="failureActionUpdateFormCancelBtn" class="taBtn taBtnOutline" style="display:none;">취소</button>
        <a class="taBtn taBtnOutline" href="${pageContext.request.contextPath}/maintenance/detail?maintenanceId=${failureAction.maintenanceId}" style="text-decoration:none;">목록</a>
    </div>
    <div class="taFormShell">
        <table class="taFormTable">
            <tr><th class="taFormLabel">고장번호</th><td class="taFormValue"><span class="taReadonlyText">${failureAction.failureActionId}</span></td></tr>
            <tr><th class="taFormLabel">정비번호</th><td class="taFormValue"><span class="taReadonlyText">${failureAction.maintenanceId}</span></td></tr>
            <tr><th class="taFormLabel">설비코드</th><td class="taFormValue"><span class="taReadonlyText">${failureAction.equipmentCode}</span></td></tr>
            <tr><th class="taFormLabel">설비명</th><td class="taFormValue"><span class="taReadonlyText">${failureAction.equipmentName}</span></td></tr>
            <tr><th class="taFormLabel">정비유형</th><td class="taFormValue"><span class="taReadonlyText">${failureAction.maintenanceType}</span></td></tr>
            <tr><th class="taFormLabel">정비일자</th><td class="taFormValue"><span class="taReadonlyText">${failureAction.maintenanceDate}</span></td></tr>
            <tr><th class="taFormLabel">고장일자</th><td class="taFormValue"><input class="taFormInput taEditableField" type="date" name="failureDate" value="${failureAction.failureDate}"></td></tr>
            <tr><th class="taFormLabel">고장부위</th><td class="taFormValue"><input class="taFormInput taEditableField" type="text" name="failurePart" value="${failureAction.failurePart}"></td></tr>
            <tr><th class="taFormLabel">고장내용</th><td class="taFormValue"><textarea class="taFormTextarea taEditableField" name="failureContent">${failureAction.failureContent}</textarea></td></tr>
            <tr><th class="taFormLabel">원인</th><td class="taFormValue"><textarea class="taFormTextarea taEditableField" name="causeText">${failureAction.causeText}</textarea></td></tr>
            <tr><th class="taFormLabel">조치내용</th><td class="taFormValue"><textarea class="taFormTextarea taEditableField" name="actionText">${failureAction.actionText}</textarea></td></tr>
            <tr><th class="taFormLabel">조치일</th><td class="taFormValue"><input class="taFormInput taEditableField" type="date" name="actionDate" value="${failureAction.actionDate}"></td></tr>
            <tr><th class="taFormLabel">상태</th><td class="taFormValue"><select class="taFormInput taEditableField" name="status"><option value="접수" ${failureAction.status eq '접수' ? 'selected' : ''}>접수</option><option value="처리중" ${failureAction.status eq '처리중' ? 'selected' : ''}>처리중</option><option value="완료" ${failureAction.status eq '완료' ? 'selected' : ''}>완료</option></select></td></tr>
        </table>
    </div>
</form>
<script>
(function() {
    const form = document.getElementById("failureActionUpdateForm"); if (!form) return;
    const editBtn = document.getElementById("failureActionUpdateFormEditBtn");
    const saveBtn = document.getElementById("failureActionUpdateFormSaveBtn");
    const cancelBtn = document.getElementById("failureActionUpdateFormCancelBtn");
    const fields = form.querySelectorAll('.taEditableField');
    function setViewMode() { fields.forEach(function(field) { if (field.tagName === 'SELECT') field.disabled = true; else field.readOnly = true; }); editBtn.style.display=''; saveBtn.style.display='none'; cancelBtn.style.display='none'; }
    function setEditMode() { fields.forEach(function(field) { if (field.tagName === 'SELECT') field.disabled = false; else field.readOnly = false; }); editBtn.style.display='none'; saveBtn.style.display=''; cancelBtn.style.display=''; }
    fields.forEach(function(field) { field.dataset.originalValue = field.value; });
    editBtn.addEventListener('click', setEditMode);
    cancelBtn.addEventListener('click', function() { fields.forEach(function(field) { field.value = field.dataset.originalValue || ''; }); setViewMode(); });
    form.addEventListener('submit', function(e) { if (saveBtn.style.display === 'none') { e.preventDefault(); return; } fields.forEach(function(field) { if (field.tagName === 'SELECT') field.disabled = false; }); if (!confirm('수정하시겠습니까?')) e.preventDefault(); });
    setViewMode();
})();
</script>
