<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<c:set var="isMesAdmin" value="${sessionScope.loginUser.roleName eq 'MES_ADMIN'}" />

<div class="page-title">설비 상세</div>

<form id="equipmentUpdateForm" method="post" action="${pageContext.request.contextPath}/equipment/update">
    <input type="hidden" name="equipmentId" value="${equipment.equipmentId}">

    <div class="table-box">
        <table class="ta">
            <tbody>
                <tr><th>설비코드</th><td><input type="text" name="equipmentCode" value="${equipment.equipmentCode}" readonly></td></tr>
                <tr><th>설비명</th><td><input type="text" name="equipmentName" value="${equipment.equipmentName}" class="editable-field" readonly></td></tr>
                <tr><th>모델명</th><td><input type="text" name="modelName" value="${equipment.modelName}" class="editable-field" readonly></td></tr>
                <tr><th>위치</th><td><input type="text" name="location" value="${equipment.location}" class="editable-field" readonly></td></tr>
                <tr><th>제조사</th><td><input type="text" name="manufacturer" value="${equipment.manufacturer}" class="editable-field" readonly></td></tr>
                <tr><th>공급처</th><td><input type="text" name="vendorName" value="${equipment.vendorName}" class="editable-field" readonly></td></tr>
                <tr><th>금액</th><td><input type="number" step="0.01" name="equipmentPrice" value="${equipment.equipmentPrice}" class="editable-field" readonly><div class="taAutoCodeHint">현재 표시: <fmt:formatNumber value="${equipment.equipmentPrice / 10000}" pattern="#,##0.##"/>만원</div></td></tr>
                <tr><th>구매일</th><td><input type="date" name="purchaseDate" value="${equipment.purchaseDate}" class="editable-field" readonly></td></tr>
                <tr><th>비고</th><td><input type="text" name="remark" value="${equipment.remark}" class="editable-field" readonly></td></tr>
            </tbody>
        </table>
    </div>

    <div class="taPageActions">
        <c:if test="${isMesAdmin}">
            <button type="button" id="editBtn" class="taBtn taBtnPrimary">수정</button>
            <button type="submit" id="saveBtn" class="taBtn taBtnPrimary" style="display:none;">수정완료</button>
            <button type="button" id="cancelBtn" class="taBtn taBtnOutline" style="display:none;">취소</button>
        </c:if>
        <a href="${pageContext.request.contextPath}/equipment/list" class="taBtn taBtnOutline">목록</a>
    </div>
</form>

<script>
const editBtn = document.getElementById("editBtn");
const saveBtn = document.getElementById("saveBtn");
const cancelBtn = document.getElementById("cancelBtn");
const fields = document.querySelectorAll(".editable-field");
if (!editBtn || !saveBtn || !cancelBtn) {
    fields.forEach(field => field.readOnly = true);
}
if (editBtn) {
    editBtn.addEventListener("click", function () {
        fields.forEach(field => field.readOnly = false);
        editBtn.style.display = "none";
        saveBtn.style.display = "inline-block";
        cancelBtn.style.display = "inline-block";
    });
    cancelBtn.addEventListener("click", function () { location.reload(); });
}
</script>
