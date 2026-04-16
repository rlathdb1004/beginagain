<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:choose>
  <c:when test="${empty routing}">
    <div class="taFormShell taEmptyState">
      <p>조회된 라우팅 정보가 없습니다.</p>
      <div class="taPageActions" style="justify-content:center; margin-top:16px;">
        <a class="taBtn taBtnOutline" href="${pageContext.request.contextPath}/routing/list" style="text-decoration:none;">목록</a>
      </div>
    </div>
  </c:when>
  <c:otherwise>
    <form id="routingUpdateForm" action="${pageContext.request.contextPath}/routing/update" method="post">
      <input type="hidden" name="routingId" value="${routing.routingId}">
      <input type="hidden" name="itemId" value="${routing.itemId}">
      <div class="taPageActions">
        <button type="button" id="routingUpdateFormEditBtn" class="taBtn taBtnPrimary">수정</button>
        <button type="submit" id="routingUpdateFormSaveBtn" class="taBtn taBtnPrimary" style="display:none;">수정완료</button>
        <button type="button" id="routingUpdateFormCancelBtn" class="taBtn taBtnOutline" style="display:none;">취소</button>
        <a class="taBtn taBtnOutline" href="${pageContext.request.contextPath}/routing/list?itemId=${routing.itemId}" style="text-decoration:none;">목록</a>
      </div>
      <div class="taFormShell">
        <table class="taFormTable">
          <tr><th class="taFormLabel">라우팅번호</th><td class="taFormValue"><span class="taReadonlyText">${routing.routingId}</span></td></tr>
          <tr><th class="taFormLabel">품목</th><td class="taFormValue"><span class="taReadonlyText">${routing.itemCode} - ${routing.itemName}</span></td></tr>
          <tr><th class="taFormLabel">공정</th><td class="taFormValue"><select class="taFormInput taEditableField" name="processId"><c:forEach var="process" items="${processList}"><option value="${process.processId}" ${routing.processId == process.processId ? 'selected' : ''}>${process.processCode} - ${process.processName}</option></c:forEach></select></td></tr>
          <tr><th class="taFormLabel">설비</th><td class="taFormValue"><select class="taFormInput taEditableField" name="equipmentId"><c:forEach var="equipment" items="${equipmentList}"><option value="${equipment.equipmentId}" ${routing.equipmentId == equipment.equipmentId ? 'selected' : ''}>${equipment.equipmentCode} - ${equipment.equipmentName}</option></c:forEach></select></td></tr>
          <tr><th class="taFormLabel">공정순서</th><td class="taFormValue"><input class="taFormInput taEditableField" type="number" min="1" name="processSeq" value="${routing.processSeq}"></td></tr>
          <tr><th class="taFormLabel">비고</th><td class="taFormValue"><textarea class="taFormTextarea taEditableField" name="remark">${routing.remark}</textarea></td></tr>
        </table>
      </div>
    </form>
<script>
(function() {
    const form = document.getElementById("routingUpdateForm");
    if (!form) return;

    const editBtn = document.getElementById("routingUpdateFormEditBtn");
    const saveBtn = document.getElementById("routingUpdateFormSaveBtn");
    const cancelBtn = document.getElementById("routingUpdateFormCancelBtn");
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
