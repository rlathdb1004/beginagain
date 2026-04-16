<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:choose>
    <c:when test="${empty item}">
        <div class="taFormShell taEmptyState">
            <p>조회된 품목 정보가 없습니다.</p>
            <div class="taPageActions" style="justify-content:center; margin-top:16px;">
                <a class="taBtn taBtnOutline" href="${pageContext.request.contextPath}/item/list" style="text-decoration:none;">목록</a>
            </div>
        </div>
    </c:when>
    <c:otherwise>
        <div class="taSectionStack">
            <div class="taPageActions">
                <button type="button" id="itemUpdateFormEditBtn" class="taBtn taBtnPrimary">수정</button>
                <button type="submit" id="itemUpdateFormSaveBtn" form="itemUpdateForm" class="taBtn taBtnPrimary" style="display:none;">수정완료</button>
                <button type="button" id="itemUpdateFormCancelBtn" class="taBtn taBtnOutline" style="display:none;">취소</button>
                <a class="taBtn taBtnOutline" href="${pageContext.request.contextPath}/item/list" style="text-decoration:none;">목록</a>
            </div>
            <div class="taFormShell">
                <form id="itemUpdateForm" action="${pageContext.request.contextPath}/item/update" method="post">
                    <input type="hidden" name="itemId" value="${item.itemId}">
                    <table class="taFormTable">
                        <tr><th class="taFormLabel">품목번호</th><td class="taFormValue"><span class="taReadonlyText">${item.itemId}</span></td></tr>
                        <tr><th class="taFormLabel">품목코드</th><td class="taFormValue"><span class="taReadonlyText">${item.itemCode}</span></td></tr>
                        <tr><th class="taFormLabel">품목명</th><td class="taFormValue"><input class="taFormInput taEditableField" type="text" name="itemName" value="${item.itemName}"></td></tr>
                        <tr><th class="taFormLabel">품목유형</th><td class="taFormValue"><input class="taFormInput taEditableField" type="text" name="itemType" value="${item.itemType}"></td></tr>
                        <tr><th class="taFormLabel">단위</th><td class="taFormValue"><input class="taFormInput taEditableField" type="text" name="unit" value="${item.unit}"></td></tr>
                        <tr><th class="taFormLabel">규격</th><td class="taFormValue"><input class="taFormInput taEditableField" type="text" name="spec" value="${item.spec}"></td></tr>
                        <tr><th class="taFormLabel">공급처</th><td class="taFormValue"><input class="taFormInput taEditableField" type="text" name="supplierName" value="${item.supplierName}"></td></tr>
                        <tr><th class="taFormLabel">안전재고</th><td class="taFormValue"><input class="taFormInput taEditableField" type="number" step="0.001" name="safetyStock" value="${item.safetyStock}"></td></tr>
                        <tr><th class="taFormLabel">비고</th><td class="taFormValue"><textarea class="taFormTextarea taEditableField" name="remark">${item.remark}</textarea></td></tr>
                    </table>
                </form>
            </div>
        </div>
<script>
(function() {
    const form = document.getElementById("itemUpdateForm");
    if (!form) return;

    const editBtn = document.getElementById("itemUpdateFormEditBtn");
    const saveBtn = document.getElementById("itemUpdateFormSaveBtn");
    const cancelBtn = document.getElementById("itemUpdateFormCancelBtn");
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
