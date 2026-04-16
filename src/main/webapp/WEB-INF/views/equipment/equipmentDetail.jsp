<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:choose>
    <c:when test="${empty equipment}">
        <div class="taFormShell taEmptyState">
            <p>조회된 설비 정보가 없습니다.</p>
            <div class="taPageActions" style="justify-content:center; margin-top:16px;">
                <a class="taBtn taBtnOutline" href="${pageContext.request.contextPath}/equipment/list" style="text-decoration:none;">목록</a>
            </div>
        </div>
    </c:when>
    <c:otherwise>
        <form id="equipmentUpdateForm" action="${pageContext.request.contextPath}/equipment/update" method="post">
            <input type="hidden" name="equipmentId" value="${equipment.equipmentId}">
            <div class="taPageActions">
                <button type="button" id="equipmentUpdateFormEditBtn" class="taBtn taBtnPrimary">수정</button>
                <button type="submit" id="equipmentUpdateFormSaveBtn" class="taBtn taBtnPrimary" style="display:none;">수정완료</button>
                <button type="button" id="equipmentUpdateFormCancelBtn" class="taBtn taBtnOutline" style="display:none;">취소</button>
                <a class="taBtn taBtnOutline" href="${pageContext.request.contextPath}/equipment/list" style="text-decoration:none;">목록</a>
            </div>
            <div class="taFormShell">
                <table class="taFormTable">
                    <tr><th class="taFormLabel">설비번호</th><td class="taFormValue"><span class="taReadonlyText">${equipment.equipmentId}</span></td></tr>
                    <tr><th class="taFormLabel">설비코드</th><td class="taFormValue"><span class="taReadonlyText">${equipment.equipmentCode}</span></td></tr>
                    <tr><th class="taFormLabel">설비명</th><td class="taFormValue"><input class="taFormInput taEditableField" type="text" name="equipmentName" value="${equipment.equipmentName}"></td></tr>
                    <tr><th class="taFormLabel">모델명</th><td class="taFormValue"><input class="taFormInput taEditableField" type="text" name="modelName" value="${equipment.modelName}"></td></tr>
                    <tr><th class="taFormLabel">위치</th><td class="taFormValue"><input class="taFormInput taEditableField" type="text" name="location" value="${equipment.location}"></td></tr>
                    <tr><th class="taFormLabel">제조사</th><td class="taFormValue"><input class="taFormInput taEditableField" type="text" name="manufacturer" value="${equipment.manufacturer}"></td></tr>
                    <tr><th class="taFormLabel">공급업체</th><td class="taFormValue"><input class="taFormInput taEditableField" type="text" name="vendorName" value="${equipment.vendorName}"></td></tr>
                    <tr><th class="taFormLabel">설비가격</th><td class="taFormValue"><input class="taFormInput taEditableField" type="number" name="equipmentPrice" value="${equipment.equipmentPrice}"></td></tr>
                    <tr><th class="taFormLabel">구매일자</th><td class="taFormValue"><input class="taFormInput taEditableField" type="date" name="purchaseDate" value="${equipment.purchaseDate}"></td></tr>
                    <tr><th class="taFormLabel">비고</th><td class="taFormValue"><textarea class="taFormTextarea taEditableField" name="remark">${equipment.remark}</textarea></td></tr>
                </table>
            </div>
        </form>
<script>
(function() {
    const form = document.getElementById("equipmentUpdateForm");
    if (!form) return;

    const editBtn = document.getElementById("equipmentUpdateFormEditBtn");
    const saveBtn = document.getElementById("equipmentUpdateFormSaveBtn");
    const cancelBtn = document.getElementById("equipmentUpdateFormCancelBtn");
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
