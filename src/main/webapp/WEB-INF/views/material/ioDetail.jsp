<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:if test="${not empty errorMsg}">
    <script>alert("${errorMsg}");</script>
</c:if>
<c:choose>
    <c:when test="${empty ioRegInqDTO}">
        <div class="taFormShell taEmptyState">
            <p>조회된 입출고 정보가 없습니다.</p>
            <div class="taPageActions" style="justify-content: center; margin-top: 16px;">
                <a class="taBtn taBtnOutline" href="${pageContext.request.contextPath}/ioRegInq" style="text-decoration: none;">목록</a>
            </div>
        </div>
    </c:when>
    <c:otherwise>
        <form id="ioUpdateForm" action="${pageContext.request.contextPath}/ioRegInq" method="post">
            <input type="hidden" name="cmd" value="update">
            <input type="hidden" name="inoutId" value="${ioRegInqDTO.inoutId}">
            <div class="taPageActions">
                <c:if test="${ioRegInqDTO.status ne '완료'}">
                    <button type="button" id="ioEditBtn" class="taBtn taBtnPrimary">수정</button>
                    <button type="submit" id="ioSaveBtn" class="taBtn taBtnPrimary" style="display: none;">수정완료</button>
                    <button type="button" id="ioCancelBtn" class="taBtn taBtnOutline" style="display: none;">취소</button>
                </c:if>
                <a class="taBtn taBtnOutline" href="${pageContext.request.contextPath}/ioRegInq" style="text-decoration: none;">목록</a>
            </div>
            <div class="taFormShell">
                <table class="taFormTable">
                    <tr><th class="taFormLabel">입출고번호</th><td class="taFormValue"><span class="taReadonlyText">${ioRegInqDTO.inoutId}</span></td></tr>
                    <tr><th class="taFormLabel">품목코드</th><td class="taFormValue"><span class="taReadonlyText">${ioRegInqDTO.itemCode}</span></td></tr>
                    <tr><th class="taFormLabel">품목명</th><td class="taFormValue"><span class="taReadonlyText">${ioRegInqDTO.itemName}</span></td></tr>
                    <tr><th class="taFormLabel">품목 유형</th><td class="taFormValue"><span class="taReadonlyText">${ioRegInqDTO.itemType}</span></td></tr>
                    <tr><th class="taFormLabel">단위</th><td class="taFormValue"><span class="taReadonlyText">${ioRegInqDTO.unit}</span></td></tr>
                    <tr><th class="taFormLabel">현재고</th><td class="taFormValue"><span class="taReadonlyText">${ioRegInqDTO.currentStock}</span></td></tr>
                    <tr>
                        <th class="taFormLabel">입출고구분</th>
                        <td class="taFormValue">
                            <select class="taFormInput taEditableField" name="inoutType">
                                <option value="입고" ${ioRegInqDTO.inoutType eq '입고' ? 'selected' : ''}>입고</option>
                                <option value="출고" ${ioRegInqDTO.inoutType eq '출고' ? 'selected' : ''}>출고</option>
                                <option value="반품" ${ioRegInqDTO.inoutType eq '반품' ? 'selected' : ''}>반품</option>
                                <option value="폐기" ${ioRegInqDTO.inoutType eq '폐기' ? 'selected' : ''}>폐기</option>
                            </select>
                        </td>
                    </tr>
                    <tr><th class="taFormLabel">수량</th><td class="taFormValue"><input class="taFormInput taEditableField" type="number" step="0.001" min="0.001" name="qty" value="${ioRegInqDTO.qty}"></td></tr>
                    <tr><th class="taFormLabel">입출고일자</th><td class="taFormValue"><input class="taFormInput taEditableField" type="date" name="inoutDate" value="${ioRegInqDTO.inoutDate}"></td></tr>
                    <tr>
                        <th class="taFormLabel">상태</th>
                        <td class="taFormValue">
                            <select class="taFormInput taEditableField" name="status">
                                <option value="대기" ${ioRegInqDTO.status eq '대기' ? 'selected' : ''}>대기</option>
                                <option value="완료" ${ioRegInqDTO.status eq '완료' ? 'selected' : ''}>완료</option>
                            </select>
                        </td>
                    </tr>
                    <tr><th class="taFormLabel">비고</th><td class="taFormValue"><textarea class="taFormTextarea taEditableField" name="remark">${ioRegInqDTO.remark}</textarea></td></tr>
                    <tr><th class="taFormLabel">생성일</th><td class="taFormValue"><span class="taReadonlyText">${ioRegInqDTO.createdAt}</span></td></tr>
                    <tr><th class="taFormLabel">수정일</th><td class="taFormValue"><span class="taReadonlyText">${ioRegInqDTO.updatedAt}</span></td></tr>
                </table>
            </div>
        </form>
        <script>
        (function(){
            const f=document.getElementById('ioUpdateForm');
            if(!f) return;
            const e=document.getElementById('ioEditBtn');
            const s=document.getElementById('ioSaveBtn');
            const c=document.getElementById('ioCancelBtn');
            const fields=f.querySelectorAll('.taEditableField');
            function view(){
                fields.forEach(x=>{ if(x.tagName==='SELECT') x.disabled=true; else x.readOnly=true; });
                if(e) e.style.display='';
                if(s) s.style.display='none';
                if(c) c.style.display='none';
            }
            function edit(){
                fields.forEach(x=>{ if(x.tagName==='SELECT') x.disabled=false; else x.readOnly=false; });
                if(e) e.style.display='none';
                if(s) s.style.display='';
                if(c) c.style.display='';
            }
            if(e) e.addEventListener('click', edit);
            if(c) c.addEventListener('click', function(){ f.reset(); view(); });
            view();
        })();
        </script>
    </c:otherwise>
</c:choose>
