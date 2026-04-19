<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:if test="${not empty errorMsg}"><script>alert('${errorMsg}');</script></c:if>
<c:choose>
<c:when test="${empty fpInspRegInqDTO}"><div class="taFormShell taEmptyState"><p>조회된 완제품 검사 정보가 없습니다.</p><div class="taPageActions" style="justify-content:center; margin-top:16px;"><a class="taBtn taBtnOutline" href="${pageContext.request.contextPath}/fpInspRegInq" style="text-decoration:none;">목록</a></div></div></c:when>
<c:otherwise>
<form id="fpInspUpdateForm" action="${pageContext.request.contextPath}/fpInspRegInq" method="post"><input type="hidden" name="cmd" value="update"><input type="hidden" name="finalInspectionId" value="${fpInspRegInqDTO.finalInspectionId}">
<div class="taPageActions"><button type="button" id="fpInspEditBtn" class="taBtn taBtnPrimary">수정</button><button type="submit" id="fpInspSaveBtn" class="taBtn taBtnPrimary" style="display:none;">수정완료</button><button type="button" id="fpInspCancelBtn" class="taBtn taBtnOutline" style="display:none;">취소</button><a class="taBtn taBtnOutline" href="${pageContext.request.contextPath}/fpInspRegInq" style="text-decoration:none;">목록</a></div>
<div class="taFormShell"><table class="taFormTable">
<tr><th class="taFormLabel">완제품검사번호</th><td class="taFormValue"><span class="taReadonlyText">${fpInspRegInqDTO.finalInspectionId}</span></td></tr>
<tr><th class="taFormLabel">생산실적번호</th><td class="taFormValue"><span class="taReadonlyText">${fpInspRegInqDTO.resultId}</span></td></tr>
<tr><th class="taFormLabel">작업지시번호</th><td class="taFormValue"><span class="taReadonlyText">${fpInspRegInqDTO.workOrderId}</span></td></tr>
<tr><th class="taFormLabel">품목코드</th><td class="taFormValue"><span class="taReadonlyText">${fpInspRegInqDTO.itemCode}</span></td></tr>
<tr><th class="taFormLabel">품목명</th><td class="taFormValue"><span class="taReadonlyText">${fpInspRegInqDTO.itemName}</span></td></tr>
<tr><th class="taFormLabel">LOT번호</th><td class="taFormValue"><span class="taReadonlyText">${fpInspRegInqDTO.lotNo}</span></td></tr>
<tr><th class="taFormLabel">생산량</th><td class="taFormValue"><span class="taReadonlyText">${fpInspRegInqDTO.producedQty}</span></td></tr>
<tr><th class="taFormLabel">누적 검사수량</th><td class="taFormValue"><span class="taReadonlyText">${fpInspRegInqDTO.currentInspectSum}</span></td></tr>
<tr><th class="taFormLabel">남은 가능 수량</th><td class="taFormValue"><span class="taReadonlyText">${fpInspRegInqDTO.remainingQty}</span></td></tr>
<tr><th class="taFormLabel">검사수량</th><td class="taFormValue"><input class="taFormInput taEditableField" type="number" step="0.001" min="0.001" name="inspectQty" value="${fpInspRegInqDTO.inspectQty}"></td></tr>
<tr><th class="taFormLabel">판정</th><td class="taFormValue"><select class="taFormInput taEditableField" name="result"><option value="합격" ${fpInspRegInqDTO.result eq '합격' ? 'selected' : ''}>합격</option><option value="부분합격" ${fpInspRegInqDTO.result eq '부분합격' ? 'selected' : ''}>부분합격</option><option value="불합격" ${fpInspRegInqDTO.result eq '불합격' ? 'selected' : ''}>불합격</option></select></td></tr>
<tr><th class="taFormLabel">검사일</th><td class="taFormValue"><input class="taFormInput taEditableField" type="date" name="inspectionDate" value="${fpInspRegInqDTO.inspectionDate}"></td></tr>
<tr><th class="taFormLabel">비고</th><td class="taFormValue"><textarea class="taFormTextarea taEditableField" name="remark">${fpInspRegInqDTO.remark}</textarea></td></tr>
</table></div></form>
<script>(function(){const f=document.getElementById('fpInspUpdateForm'); if(!f) return; const e=document.getElementById('fpInspEditBtn'), s=document.getElementById('fpInspSaveBtn'), c=document.getElementById('fpInspCancelBtn'); const fields=f.querySelectorAll('.taEditableField'); function view(){fields.forEach(x=>{if(x.tagName==='SELECT')x.disabled=true; else x.readOnly=true;}); e.style.display=''; s.style.display='none'; c.style.display='none';} function edit(){fields.forEach(x=>{if(x.tagName==='SELECT')x.disabled=false; else x.readOnly=false;}); e.style.display='none'; s.style.display=''; c.style.display='';} fields.forEach(x=>x.dataset.originalValue=x.value); e.addEventListener('click',edit); c.addEventListener('click',()=>{fields.forEach(x=>x.value=x.dataset.originalValue||''); view();}); f.addEventListener('submit',ev=>{if(s.style.display==='none'){ev.preventDefault(); return;} fields.forEach(x=>{if(x.tagName==='SELECT')x.disabled=false;}); if(!confirm('수정하시겠습니까?')) ev.preventDefault();}); view();})();</script>
</c:otherwise>
</c:choose>
