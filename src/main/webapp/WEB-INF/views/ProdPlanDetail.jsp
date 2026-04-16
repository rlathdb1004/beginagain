<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:choose>
    <c:when test="${empty productionPlan}">
        <div class="taFormShell taEmptyState">
            <p>조회된 생산계획 정보가 없습니다.</p>
            <div class="taPageActions" style="justify-content:center; margin-top:16px;">
                <a class="taBtn taBtnOutline" href="${pageContext.request.contextPath}/prodplan" style="text-decoration:none;">목록</a>
            </div>
        </div>
    </c:when>
    <c:otherwise>
        <form id="prodPlanUpdateForm" action="${pageContext.request.contextPath}/prodplan" method="post">
            <input type="hidden" name="cmd" value="update">
            <input type="hidden" name="seqNO" value="${productionPlan.seqNO}">
            <div class="taPageActions">
                <button type="button" id="prodPlanEditBtn" class="taBtn taBtnPrimary">수정</button>
                <button type="submit" id="prodPlanSaveBtn" class="taBtn taBtnPrimary" style="display:none;">수정완료</button>
                <button type="button" id="prodPlanCancelBtn" class="taBtn taBtnOutline" style="display:none;">취소</button>
                <a class="taBtn taBtnOutline" href="${pageContext.request.contextPath}/prodplan" style="text-decoration:none;">목록</a>
            </div>
            <div class="taFormShell">
                <table class="taFormTable">
                    <tr><th class="taFormLabel">NO</th><td class="taFormValue"><span class="taReadonlyText">${productionPlan.seqNO}</span></td></tr>
                    <tr><th class="taFormLabel">생산계획번호</th><td class="taFormValue"><span class="taReadonlyText">${productionPlan.planNo}</span></td></tr>
                    <tr><th class="taFormLabel">계획일자</th><td class="taFormValue"><input class="taFormInput taEditableField" type="date" name="planDate" value="${productionPlan.planDate}"></td></tr>
                    <tr><th class="taFormLabel">품목</th><td class="taFormValue"><select class="taFormInput taEditableField" name="planCode" id="prodPlanCodeSelect"><c:forEach var="item" items="${itemOptions}"><option value="${item.planCode}" data-name="${item.planName}" data-unit="${item.planUnit}" ${item.planCode eq productionPlan.planCode ? 'selected' : ''}>${item.planCode} / ${item.planName}</option></c:forEach></select></td></tr>
                    <tr><th class="taFormLabel">품목명</th><td class="taFormValue"><span class="taReadonlyText" id="prodPlanName">${productionPlan.planName}</span></td></tr>
                    <tr><th class="taFormLabel">생산계획량</th><td class="taFormValue"><input class="taFormInput taEditableField" type="number" min="0" name="planAmount" value="${productionPlan.planAmount}"></td></tr>
                    <tr><th class="taFormLabel">단위</th><td class="taFormValue"><span class="taReadonlyText" id="prodPlanUnit">${productionPlan.planUnit}</span></td></tr>
                    <tr><th class="taFormLabel">라인</th><td class="taFormValue"><input class="taFormInput taEditableField" type="text" name="planLine" value="${productionPlan.planLine}"></td></tr>
                    <tr><th class="taFormLabel">상태</th><td class="taFormValue"><select class="taFormInput taEditableField" name="status"><option value="대기" ${productionPlan.status eq '대기' ? 'selected' : ''}>대기</option><option value="진행중" ${productionPlan.status eq '진행중' ? 'selected' : ''}>진행중</option><option value="완료" ${productionPlan.status eq '완료' ? 'selected' : ''}>완료</option></select></td></tr>
                    <tr><th class="taFormLabel">비고</th><td class="taFormValue"><textarea class="taFormTextarea taEditableField" name="memo">${productionPlan.memo}</textarea></td></tr>
                </table>
            </div>
        </form>
<script>
(function(){const f=document.getElementById('prodPlanUpdateForm'); if(!f) return; const e=document.getElementById('prodPlanEditBtn'), s=document.getElementById('prodPlanSaveBtn'), c=document.getElementById('prodPlanCancelBtn'); const fields=f.querySelectorAll('.taEditableField'); const code=document.getElementById('prodPlanCodeSelect'), name=document.getElementById('prodPlanName'), unit=document.getElementById('prodPlanUnit'); function sync(){const o=code&&code.options[code.selectedIndex]; if(!o) return; name.textContent=o.dataset.name||''; unit.textContent=o.dataset.unit||'';} function view(){fields.forEach(x=>{if(x.tagName==='SELECT')x.disabled=true; else x.readOnly=true;}); e.style.display=''; s.style.display='none'; c.style.display='none';} function edit(){fields.forEach(x=>{if(x.tagName==='SELECT')x.disabled=false; else x.readOnly=false;}); e.style.display='none'; s.style.display=''; c.style.display='';} fields.forEach(x=>x.dataset.originalValue=x.value); code&&code.addEventListener('change',sync); e.addEventListener('click',edit); c.addEventListener('click',()=>{fields.forEach(x=>x.value=x.dataset.originalValue||''); sync(); view();}); f.addEventListener('submit',ev=>{if(s.style.display==='none'){ev.preventDefault(); return;} fields.forEach(x=>{if(x.tagName==='SELECT')x.disabled=false;}); if(!confirm('수정하시겠습니까?')) ev.preventDefault();}); sync(); view();})();
</script>
    </c:otherwise>
</c:choose>
