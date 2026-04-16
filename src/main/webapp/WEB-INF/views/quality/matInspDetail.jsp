<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:choose>
<c:when test="${empty matInspRegInqDTO}"><div class="taFormShell taEmptyState"><p>조회된 자재 검사 정보가 없습니다.</p><div class="taPageActions" style="justify-content:center; margin-top:16px;"><a class="taBtn taBtnOutline" href="${pageContext.request.contextPath}/matInspRegInq" style="text-decoration:none;">목록</a></div></div></c:when>
<c:otherwise>
<form id="matInspUpdateForm" action="${pageContext.request.contextPath}/matInspRegInq" method="post"><input type="hidden" name="cmd" value="update"><input type="hidden" name="materialInspectionId" value="${matInspRegInqDTO.materialInspectionId}">
<div class="taPageActions"><button type="button" id="matInspEditBtn" class="taBtn taBtnPrimary">수정</button><button type="submit" id="matInspSaveBtn" class="taBtn taBtnPrimary" style="display:none;">수정완료</button><button type="button" id="matInspCancelBtn" class="taBtn taBtnOutline" style="display:none;">취소</button><a class="taBtn taBtnOutline" href="${pageContext.request.contextPath}/matInspRegInq" style="text-decoration:none;">목록</a></div>
<div class="taFormShell"><table class="taFormTable">
<tr><th class="taFormLabel">자재검사번호</th><td class="taFormValue"><span class="taReadonlyText">${matInspRegInqDTO.materialInspectionId}</span></td></tr>
<tr><th class="taFormLabel">품목코드</th><td class="taFormValue"><span class="taReadonlyText">${matInspRegInqDTO.itemCode}</span></td></tr>
<tr><th class="taFormLabel">품목명</th><td class="taFormValue"><span class="taReadonlyText">${matInspRegInqDTO.itemName}</span></td></tr>
<tr><th class="taFormLabel">검사수량</th><td class="taFormValue"><input class="taFormInput taEditableField" type="number" step="0.001" min="0" name="inspectQty" value="${matInspRegInqDTO.inspectQty}"></td></tr>
<tr><th class="taFormLabel">양품수량</th><td class="taFormValue"><input class="taFormInput taEditableField" type="number" step="0.001" min="0" name="goodQty" value="${matInspRegInqDTO.goodQty}"></td></tr>
<tr><th class="taFormLabel">불량수량</th><td class="taFormValue"><input class="taFormInput taEditableField" type="number" step="0.001" min="0" name="defectQty" value="${matInspRegInqDTO.defectQty}"></td></tr>
<tr><th class="taFormLabel">판정</th><td class="taFormValue"><select class="taFormInput taEditableField" name="result"><option value="합격" ${matInspRegInqDTO.result eq '합격' ? 'selected' : ''}>합격</option><option value="불합격" ${matInspRegInqDTO.result eq '불합격' ? 'selected' : ''}>불합격</option></select></td></tr>
<tr><th class="taFormLabel">검사일</th><td class="taFormValue"><input class="taFormInput taEditableField" type="date" name="inspectionDate" value="${matInspRegInqDTO.inspectionDate}"></td></tr>
<tr><th class="taFormLabel">비고</th><td class="taFormValue"><textarea class="taFormTextarea taEditableField" name="remark">${matInspRegInqDTO.remark}</textarea></td></tr>
</table></div></form>
<script>(function(){const f=document.getElementById('matInspUpdateForm'); if(!f) return; const e=document.getElementById('matInspEditBtn'), s=document.getElementById('matInspSaveBtn'), c=document.getElementById('matInspCancelBtn'); const fields=f.querySelectorAll('.taEditableField'); function view(){fields.forEach(x=>{if(x.tagName==='SELECT')x.disabled=true; else x.readOnly=true;}); e.style.display=''; s.style.display='none'; c.style.display='none';} function edit(){fields.forEach(x=>{if(x.tagName==='SELECT')x.disabled=false; else x.readOnly=false;}); e.style.display='none'; s.style.display=''; c.style.display='';} fields.forEach(x=>x.dataset.originalValue=x.value); e.addEventListener('click',edit); c.addEventListener('click',()=>{fields.forEach(x=>x.value=x.dataset.originalValue||''); view();}); f.addEventListener('submit',ev=>{if(s.style.display==='none'){ev.preventDefault(); return;} fields.forEach(x=>{if(x.tagName==='SELECT')x.disabled=false;}); if(!confirm('수정하시겠습니까?')) ev.preventDefault();}); view();})();</script>
</c:otherwise>
</c:choose>
