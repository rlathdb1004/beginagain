<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:if test="${not empty errorMsg}"><script>alert('${errorMsg}');</script></c:if>
<c:choose>
<c:when test="${empty maintenance}">
<div class="taFormShell taEmptyState"><p>조회된 정비이력 정보가 없습니다.</p><div class="taPageActions" style="justify-content:center; margin-top:16px;"><a class="taBtn taBtnOutline" href="${pageContext.request.contextPath}/maintenance/list" style="text-decoration:none;">목록</a></div></div>
</c:when>
<c:otherwise>
<form id="maintenanceUpdateForm" action="${pageContext.request.contextPath}/maintenance/update" method="post">
<input type="hidden" name="maintenanceId" value="${maintenance.maintenanceId}">
<input type="hidden" name="equipmentId" value="${maintenance.equipmentId}">
<div class="taPageActions">
    <button type="button" id="maintenanceUpdateFormEditBtn" class="taBtn taBtnPrimary">수정</button>
    <button type="submit" id="maintenanceUpdateFormSaveBtn" class="taBtn taBtnPrimary" style="display:none;">수정완료</button>
    <button type="button" id="maintenanceUpdateFormCancelBtn" class="taBtn taBtnOutline" style="display:none;">취소</button>
    <a class="taBtn taBtnOutline" href="${pageContext.request.contextPath}/maintenance/list" style="text-decoration:none;">목록</a>
</div>
<div class="taFormShell">
<table class="taFormTable">
<tr><th class="taFormLabel">정비번호</th><td class="taFormValue"><span class="taReadonlyText">${maintenance.maintenanceId}</span></td></tr>
<tr><th class="taFormLabel">설비번호</th><td class="taFormValue"><span class="taReadonlyText">${maintenance.equipmentId}</span></td></tr>
<tr><th class="taFormLabel">설비코드</th><td class="taFormValue"><span class="taReadonlyText">${maintenance.equipmentCode}</span></td></tr>
<tr><th class="taFormLabel">설비명</th><td class="taFormValue"><span class="taReadonlyText">${maintenance.equipmentName}</span></td></tr>
<tr><th class="taFormLabel">모델명</th><td class="taFormValue"><span class="taReadonlyText">${maintenance.modelName}</span></td></tr>
<tr><th class="taFormLabel">위치</th><td class="taFormValue"><span class="taReadonlyText">${maintenance.location}</span></td></tr>
<tr><th class="taFormLabel">정비일자</th><td class="taFormValue"><input class="taFormInput taEditableField" type="date" name="maintenanceDate" value="${maintenance.maintenanceDate}"></td></tr>
<tr><th class="taFormLabel">정비유형</th><td class="taFormValue"><select class="taFormInput taEditableField" name="maintenanceType"><option value="정기점검" ${maintenance.maintenanceType eq '정기점검' ? 'selected' : ''}>정기점검</option><option value="예방정비" ${maintenance.maintenanceType eq '예방정비' ? 'selected' : ''}>예방정비</option><option value="고장정비" ${maintenance.maintenanceType eq '고장정비' ? 'selected' : ''}>고장정비</option></select></td></tr>
<tr><th class="taFormLabel">정비내용</th><td class="taFormValue"><textarea class="taFormTextarea taEditableField" name="maintenanceContent">${maintenance.maintenanceContent}</textarea></td></tr>
<tr><th class="taFormLabel">다음정비일</th><td class="taFormValue"><input class="taFormInput taEditableField" type="date" name="nextMaintenanceDate" value="${maintenance.nextMaintenanceDate}"></td></tr>
<tr><th class="taFormLabel">상태</th><td class="taFormValue"><select class="taFormInput taEditableField" name="status"><option value="정상" ${maintenance.status eq '정상' ? 'selected' : ''}>정상</option><option value="점검중" ${maintenance.status eq '점검중' ? 'selected' : ''}>점검중</option><option value="고장" ${maintenance.status eq '고장' ? 'selected' : ''}>고장</option><option value="수리완료" ${maintenance.status eq '수리완료' ? 'selected' : ''}>수리완료</option></select></td></tr>
<tr><th class="taFormLabel">비고</th><td class="taFormValue"><textarea class="taFormTextarea taEditableField" name="remark">${maintenance.remark}</textarea></td></tr>
<tr><th class="taFormLabel">생성일</th><td class="taFormValue"><span class="taReadonlyText">${maintenance.createdAt}</span></td></tr>
<tr><th class="taFormLabel">수정일</th><td class="taFormValue"><span class="taReadonlyText">${maintenance.updatedAt}</span></td></tr>
<tr><th class="taFormLabel">고장조치 수</th><td class="taFormValue"><span class="taReadonlyText">${maintenance.failureActionCount}</span></td></tr>
</table>
</div>
</form>
<div class="taSectionStack" style="margin-top:24px;">
<div class="taPageActions" style="justify-content:space-between; align-items:center;">
<h3 style="margin:0; font-size:18px;">고장조치 내역</h3>
<div style="display:flex; gap:8px;">
<a class="taBtn taBtnPrimary" href="${pageContext.request.contextPath}/failureaction/register?maintenanceId=${maintenance.maintenanceId}" style="text-decoration:none;">고장조치 등록</a>
<button type="submit" form="failureDeleteForm" class="taBtn taBtnOutline" onclick="return confirm('선택한 고장조치 내역을 삭제하시겠습니까?');">선택삭제</button>
</div>
</div>
<form id="failureDeleteForm" action="${pageContext.request.contextPath}/failureaction/delete" method="post">
<input type="hidden" name="maintenanceId" value="${maintenance.maintenanceId}">
<div class="taTableShell"><div class="taTableScroll"><table class="taMesTable"><thead><tr><th class="taTableHeadCell taCheckCell"><input type="checkbox" id="checkAllFailure" class="taCheckInput"></th><th class="taTableHeadCell taColFit">고장번호</th><th class="taTableHeadCell taColFit">고장일자</th><th class="taTableHeadCell taColFit">고장부위</th><th class="taTableHeadCell taColGrow">고장내용</th><th class="taTableHeadCell taColGrow">원인</th><th class="taTableHeadCell taColGrow">조치내용</th><th class="taTableHeadCell taColFit">조치일</th><th class="taTableHeadCell taColFit">상태</th><th class="taTableHeadCell taColAction taLastCol">상세</th></tr></thead><tbody><c:choose><c:when test="${not empty failureActionList}"><c:forEach var="fa" items="${failureActionList}"><tr class="taTableBodyRow"><td class="taTableBodyCell taCheckCell"><input type="checkbox" name="failureActionId" value="${fa.failureActionId}" class="taCheckInput"></td><td class="taTableBodyCell taColFit">${fa.failureActionId}</td><td class="taTableBodyCell taColFit">${fa.failureDate}</td><td class="taTableBodyCell taColFit">${fa.failurePart}</td><td class="taTableBodyCell taColGrow">${fa.failureContent}</td><td class="taTableBodyCell taColGrow">${fa.causeText}</td><td class="taTableBodyCell taColGrow">${fa.actionText}</td><td class="taTableBodyCell taColFit">${fa.actionDate}</td><td class="taTableBodyCell taColFit">${fa.status}</td><td class="taTableBodyCell taColAction taLastCol"><a class="taLinkAnchor" href="${pageContext.request.contextPath}/failureaction/detail?failureActionId=${fa.failureActionId}">상세보기</a></td></tr></c:forEach></c:when><c:otherwise><tr class="taTableBodyRow"><td class="taTableBodyCell taLastCol" colspan="10" style="text-align:center;">해당 정비이력에 등록된 고장조치가 없습니다.</td></tr></c:otherwise></c:choose></tbody></table></div></div>
</form>
</div>
<script>
document.getElementById("checkAllFailure")?.addEventListener("change", function() {
    const checks = document.querySelectorAll("input[name='failureActionId']");
    for (let i = 0; i < checks.length; i++) checks[i].checked = this.checked;
});
(function(){
 const form = document.getElementById("maintenanceUpdateForm"); if(!form) return;
 const editBtn = document.getElementById("maintenanceUpdateFormEditBtn");
 const saveBtn = document.getElementById("maintenanceUpdateFormSaveBtn");
 const cancelBtn = document.getElementById("maintenanceUpdateFormCancelBtn");
 const fields = form.querySelectorAll('.taEditableField');
 function setViewMode(){ fields.forEach(function(f){ if(f.tagName==='SELECT'){f.disabled=true;} else {f.readOnly=true;} }); editBtn.style.display=''; saveBtn.style.display='none'; cancelBtn.style.display='none'; }
 function setEditMode(){ fields.forEach(function(f){ if(f.tagName==='SELECT'){f.disabled=false;} else {f.readOnly=false;} }); editBtn.style.display='none'; saveBtn.style.display=''; cancelBtn.style.display=''; }
 fields.forEach(function(f){ f.dataset.originalValue=f.value; });
 editBtn.addEventListener('click', setEditMode);
 cancelBtn.addEventListener('click', function(){ fields.forEach(function(f){ f.value = f.dataset.originalValue || ''; }); setViewMode(); });
 form.addEventListener('submit', function(e){ if(saveBtn.style.display==='none'){ e.preventDefault(); return; } fields.forEach(function(f){ if(f.tagName==='SELECT'){ f.disabled=false; } }); if(!confirm('수정하시겠습니까?')) e.preventDefault(); });
 setViewMode();
})();
</script>
</c:otherwise>
</c:choose>
