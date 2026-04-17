<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:choose>
	<c:when test="${empty ioRegInqDTO}">
		<div class="taFormShell taEmptyState">
			<p>조회된 입출고 정보가 없습니다.</p>
			<div class="taPageActions"
				style="justify-content: center; margin-top: 16px;">
				<a class="taBtn taBtnOutline"
					href="${pageContext.request.contextPath}/ioRegInq"
					style="text-decoration: none;">목록</a>
			</div>
		</div>
	</c:when>
	<c:otherwise>
		<form id="ioUpdateForm"
			action="${pageContext.request.contextPath}/ioRegInq" method="post">
			<input type="hidden" name="cmd" value="update"><input
				type="hidden" name="inoutId" value="${ioRegInqDTO.inoutId}">
			<div class="taPageActions">
				<button type="button" id="ioEditBtn" class="taBtn taBtnPrimary">수정</button>
				<button type="submit" id="ioSaveBtn" class="taBtn taBtnPrimary"
					style="display: none;">수정완료</button>
				<button type="button" id="ioCancelBtn" class="taBtn taBtnOutline"
					style="display: none;">취소</button>
				<a class="taBtn taBtnOutline"
					href="${pageContext.request.contextPath}/ioRegInq"
					style="text-decoration: none;">목록</a>
			</div>
			<div class="taFormShell">
				<table class="taFormTable">
					<tr>
						<th class="taFormLabel">입출고번호</th>
						<td class="taFormValue"><span class="taReadonlyText">${ioRegInqDTO.inoutId}</span></td>
					</tr>
					<tr>
						<th class="taFormLabel">품목코드</th>
						<td class="taFormValue"><span class="taReadonlyText">${ioRegInqDTO.itemCode}</span></td>
					</tr>
					<tr>
						<th class="taFormLabel">품목명</th>
						<td class="taFormValue"><span class="taReadonlyText">${ioRegInqDTO.itemName}</span></td>
					</tr>
					<tr>
						<th class="taFormLabel">입출고구분</th>
						<td class="taFormValue"><select
							class="taFormInput taEditableField" name="inoutType"><option
									value="입고" ${ioRegInqDTO.inoutType eq '입고' ? 'selected' : ''}>입고</option>
								<option value="출고"
									${ioRegInqDTO.inoutType eq '출고' ? 'selected' : ''}>출고</option></select></td>
					</tr>
					<tr>
						<th class="taFormLabel">수량</th>
						<td class="taFormValue"><input
							class="taFormInput taEditableField" type="number" step="0.001"
							min="0" name="qty" value="${ioRegInqDTO.qty}"></td>
					</tr>
					<tr>
						<th class="taFormLabel">단위</th>
						<td class="taFormValue"><input
							class="taFormInput taEditableField" type="text" name="unit"
							value="${ioRegInqDTO.unit}"></td>
					</tr>
					<tr>
						<th class="taFormLabel">입출고일자</th>
						<td class="taFormValue"><input
							class="taFormInput taEditableField" type="date" name="inoutDate"
							value="${ioRegInqDTO.inoutDate}"></td>
					</tr>
					<tr>
						<th class="taFormLabel">상태</th>
						<td class="taFormValue"><select
							class="taFormInput taEditableField" name="status"><option
									value="대기" ${ioRegInqDTO.status eq '대기' ? 'selected' : ''}>대기</option>
								<option value="완료"
									${ioRegInqDTO.status eq '완료' ? 'selected' : ''}>완료</option></select></td>
					</tr>
					<tr>
						<th class="taFormLabel">비고</th>
						<td class="taFormValue"><textarea
								class="taFormTextarea taEditableField" name="remark">${ioRegInqDTO.remark}</textarea></td>
					</tr>
				</table>
			</div>
		</form>
		<script>
(function(){const f=document.getElementById('ioUpdateForm'); if(!f) return; const e=document.getElementById('ioEditBtn'), s=document.getElementById('ioSaveBtn'), c=document.getElementById('ioCancelBtn'); const fields=f.querySelectorAll('.taEditableField'); function view(){fields.forEach(x=>{if(x.tagName==='SELECT')x.disabled=true; else x.readOnly=true;}); e.style.display=''; s.style.display='none'; c.style.display='none';} function edit(){fields.forEach(x=>{if(x.tagName==='SELECT')x.disabled=false; else x.readOnly=false;}); e.style.display='none'; s.style.display=''; c.style.display='';} fields.forEach(x=>x.dataset.originalValue=x.value); e.addEventListener('click',edit); c.addEventListener('click',()=>{fields.forEach(x=>x.value=x.dataset.originalValue||''); view();}); f.addEventListener('submit',ev=>{if(s.style.display==='none'){ev.preventDefault(); return;} fields.forEach(x=>{if(x.tagName==='SELECT')x.disabled=false;}); if(!confirm('수정하시겠습니까?')) ev.preventDefault();}); view();})();
</script>
	</c:otherwise>
</c:choose>
