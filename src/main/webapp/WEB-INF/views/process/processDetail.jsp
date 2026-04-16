<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:choose>
    <c:when test="${empty p}">
        <div class="taFormShell taEmptyState">
            <p>조회된 공정 정보가 없습니다.</p>
            <div class="taPageActions" style="justify-content:center; margin-top:16px;">
                <a class="taBtn taBtnOutline" href="${pageContext.request.contextPath}/process/list" style="text-decoration:none;">목록</a>
            </div>
        </div>
    </c:when>
    <c:otherwise>
        <form id="processUpdateForm" action="${pageContext.request.contextPath}/process/update" method="post">
            <input type="hidden" name="processId" value="${p.processId}">
            <div class="taPageActions">
                <button type="button" id="processUpdateFormEditBtn" class="taBtn taBtnPrimary">수정</button>
                <button type="submit" id="processUpdateFormSaveBtn" class="taBtn taBtnPrimary" style="display:none;">수정완료</button>
                <button type="button" id="processUpdateFormCancelBtn" class="taBtn taBtnOutline" style="display:none;">취소</button>
                <a class="taBtn taBtnOutline" href="${pageContext.request.contextPath}/process/list" style="text-decoration:none;">목록</a>
            </div>
            <div class="taFormShell">
                <table class="taFormTable">
                    <tr><th class="taFormLabel">코드</th><td class="taFormValue"><span class="taReadonlyText">${p.processCode}</span></td></tr>
                    <tr><th class="taFormLabel">이름</th><td class="taFormValue"><input class="taFormInput taEditableField" type="text" name="processName" value="${p.processName}"></td></tr>
                    <tr><th class="taFormLabel">설명</th><td class="taFormValue"><textarea class="taFormTextarea taEditableField" name="description">${p.description}</textarea></td></tr>
                    <tr><th class="taFormLabel">비고</th><td class="taFormValue"><textarea class="taFormTextarea taEditableField" name="remark">${p.remark}</textarea></td></tr>
                </table>
            </div>
        </form>
<script>
(function() {
    const form = document.getElementById("processUpdateForm");
    if (!form) return;

    const editBtn = document.getElementById("processUpdateFormEditBtn");
    const saveBtn = document.getElementById("processUpdateFormSaveBtn");
    const cancelBtn = document.getElementById("processUpdateFormCancelBtn");
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
