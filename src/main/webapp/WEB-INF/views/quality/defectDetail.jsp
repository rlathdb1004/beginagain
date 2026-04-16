<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:choose>
<c:when test="${empty defectRegInqDTO}"><div class="taFormShell taEmptyState"><p>조회된 불량 정보가 없습니다.</p><div class="taPageActions" style="justify-content:center; margin-top:16px;"><a class="taBtn taBtnOutline" href="${pageContext.request.contextPath}/defectRegInq" style="text-decoration:none;">목록</a></div></div></c:when>
<c:otherwise>
<form id="defectUpdateForm" action="${pageContext.request.contextPath}/defectRegInq" method="post"><input type="hidden" name="cmd" value="update"><input type="hidden" name="defectProductId" value="${defectRegInqDTO.defectProductId}">
<div class="taPageActions"><button type="button" id="defectEditBtn" class="taBtn taBtnPrimary">수정</button><button type="submit" id="defectSaveBtn" class="taBtn taBtnPrimary" style="display:none;">수정완료</button><button type="button" id="defectCancelBtn" class="taBtn taBtnOutline" style="display:none;">취소</button><a class="taBtn taBtnOutline" href="${pageContext.request.contextPath}/defectRegInq" style="text-decoration:none;">목록</a></div>
<div class="taFormShell"><table class="taFormTable">
<tr><th class="taFormLabel">불량번호</th><td class="taFormValue"><span class="taReadonlyText">${defectRegInqDTO.defectProductId}</span></td></tr>
<tr><th class="taFormLabel">완제품검사번호</th><td class="taFormValue"><input class="taFormInput taEditableField" type="number" min="1" name="finalInspectionId" value="${defectRegInqDTO.finalInspectionId}"></td></tr>
<tr><th class="taFormLabel">품목코드</th><td class="taFormValue"><span class="taReadonlyText">${defectRegInqDTO.itemCode}</span></td></tr>
<tr><th class="taFormLabel">품목명</th><td class="taFormValue"><span class="taReadonlyText">${defectRegInqDTO.itemName}</span></td></tr>
<tr><th class="taFormLabel">LOT번호</th><td class="taFormValue"><span class="taReadonlyText">${defectRegInqDTO.lotNo}</span></td></tr>
<tr><th class="taFormLabel">불량수량</th><td class="taFormValue"><span class="taReadonlyText">${defectRegInqDTO.defectQty}</span></td></tr>
<tr><th class="taFormLabel">불량코드ID</th><td class="taFormValue"><input class="taFormInput taEditableField" type="number" min="1" name="defectCodeId" value="${defectRegInqDTO.defectCodeId}"></td></tr>
<tr><th class="taFormLabel">불량코드</th><td class="taFormValue"><span class="taReadonlyText">${defectRegInqDTO.defectCode}</span></td></tr>
<tr><th class="taFormLabel">불량명</th><td class="taFormValue"><span class="taReadonlyText">${defectRegInqDTO.defectName}</span></td></tr>
<tr><th class="taFormLabel">유형</th><td class="taFormValue"><span class="taReadonlyText">${defectRegInqDTO.defectType}</span></td></tr>
<tr><th class="taFormLabel">비고</th><td class="taFormValue"><textarea class="taFormTextarea taEditableField" name="remark">${defectRegInqDTO.remark}</textarea></td></tr>
</table></div></form>
<script>(function(){const f=document.getElementById('defectUpdateForm'); if(!f) return; const e=document.getElementById('defectEditBtn'), s=document.getElementById('defectSaveBtn'), c=document.getElementById('defectCancelBtn'); const fields=f.querySelectorAll('.taEditableField'); function view(){fields.forEach(x=>x.readOnly=true); e.style.display=''; s.style.display='none'; c.style.display='none';} function edit(){fields.forEach(x=>x.readOnly=false); e.style.display='none'; s.style.display=''; c.style.display='';} fields.forEach(x=>x.dataset.originalValue=x.value); e.addEventListener('click',edit); c.addEventListener('click',()=>{fields.forEach(x=>x.value=x.dataset.originalValue||''); view();}); f.addEventListener('submit',ev=>{if(s.style.display==='none'){ev.preventDefault(); return;} if(!confirm('수정하시겠습니까?')) ev.preventDefault();}); view();})();</script>
</c:otherwise>
</c:choose>
