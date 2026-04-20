<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<c:set var="isMesAdmin" value="${sessionScope.loginUser.roleName eq 'MES_ADMIN'}" />

<div class="page-title">설비 상세</div>

<form id="equipmentUpdateForm" method="post" action="${pageContext.request.contextPath}/equipment/update">
    <input type="hidden" name="equipmentId" value="${equipment.equipmentId}">

    <div class="taPageActions">
        <c:if test="${isMesAdmin}">
            <button type="button" id="editBtn" class="taBtn taBtnPrimary">수정</button>
            <button type="submit" id="saveBtn" class="taBtn taBtnPrimary" style="display:none;">수정완료</button>
            <button type="button" id="cancelBtn" class="taBtn taBtnOutline" style="display:none;">취소</button>
        </c:if>
        <a href="${pageContext.request.contextPath}/equipment/list" class="taBtn taBtnOutline" style="text-decoration: none;">목록</a>
    </div>

    <div class="taFormShell">
        <table class="taFormTable">
            <tbody>
                <tr><th class="taFormLabel">설비코드</th><td class="taFormValue"><span class="taReadonlyText">${equipment.equipmentCode}</span></td></tr>
                <tr><th class="taFormLabel">설비명</th><td class="taFormValue"><input type="text" name="equipmentName" value="${equipment.equipmentName}" class="taFormInput taEditableField"></td></tr>
                <tr><th class="taFormLabel">모델명</th><td class="taFormValue"><input type="text" name="modelName" value="${equipment.modelName}" class="taFormInput taEditableField"></td></tr>
                <tr><th class="taFormLabel">위치</th><td class="taFormValue"><input type="text" name="location" value="${equipment.location}" class="taFormInput taEditableField"></td></tr>
                <tr><th class="taFormLabel">제조사</th><td class="taFormValue"><input type="text" name="manufacturer" value="${equipment.manufacturer}" class="taFormInput taEditableField"></td></tr>
                <tr><th class="taFormLabel">공급처</th><td class="taFormValue"><input type="text" name="vendorName" value="${equipment.vendorName}" class="taFormInput taEditableField"></td></tr>
                <tr><th class="taFormLabel">금액</th><td class="taFormValue"><input type="number" step="0.01" name="equipmentPrice" value="${equipment.equipmentPrice}" class="taFormInput taEditableField"><div class="taAutoCodeHint">현재 표시: <fmt:formatNumber value="${equipment.equipmentPrice / 10000}" pattern="#,#00.##"/>만원</div></td></tr>
                <tr><th class="taFormLabel">구매일</th><td class="taFormValue"><input type="date" name="purchaseDate" value="${equipment.purchaseDate}" class="taFormInput taEditableField"></td></tr>
                <tr><th class="taFormLabel">비고</th><td class="taFormValue"><input type="text" name="remark" value="${equipment.remark}" class="taFormInput taEditableField"></td></tr>
            </tbody>
        </table>
    </div>
</form>

<script>
(function() {
    const form = document.getElementById("equipmentUpdateForm");
    const editBtn = document.getElementById("editBtn");
    const saveBtn = document.getElementById("saveBtn");
    const cancelBtn = document.getElementById("cancelBtn");
    const fields = form ? form.querySelectorAll(".taEditableField") : [];

    function viewMode() {
        fields.forEach(field => field.readOnly = true);
        if (editBtn) editBtn.style.display = "";
        if (saveBtn) saveBtn.style.display = "none";
        if (cancelBtn) cancelBtn.style.display = "none";
    }

    function editMode() {
        fields.forEach(field => field.readOnly = false);
        if (editBtn) editBtn.style.display = "none";
        if (saveBtn) saveBtn.style.display = "";
        if (cancelBtn) cancelBtn.style.display = "";
    }

    fields.forEach(field => field.dataset.originalValue = field.value);

    if (editBtn && saveBtn && cancelBtn) {
        editBtn.addEventListener("click", editMode);
        cancelBtn.addEventListener("click", function() {
            fields.forEach(field => field.value = field.dataset.originalValue || "");
            viewMode();
        });
    }

    viewMode();
})();
</script>
