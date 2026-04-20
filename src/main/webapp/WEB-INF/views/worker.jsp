<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:if test="${not empty errorMsg}">
    <script>alert("${errorMsg}");</script>
</c:if>

<div class="worker-page">
    <div class="worker-page-head">
        <div>
            <h2 class="worker-title">내 작업 생산 실적 등록</h2>
            <p class="worker-sub">로그인한 작업자에게 배정된 작업지시만 선택할 수 있습니다.</p>
        </div>
        <button type="submit" form="workerProdPerfForm" class="taBtn taBtnPrimary">등록</button>
    </div>

    <form id="workerProdPerfForm" action="${pageContext.request.contextPath}/prodperf/register" method="post">
        <div class="taModalBody taModalGrid worker-form-grid">
            <div class="form-row"><label>작업자</label><input type="text" value="${sessionScope.loginUser.empName}" readonly></div>
            <div class="form-row"><label>생산실적일자</label><input type="date" name="resultDate" id="workerResultDate" readonly></div>
            <div class="form-row">
                <label>작업지시</label>
                <select name="workOrderId" id="registerWorkOrderId" required>
                    <option value="">선택</option>
                    <c:forEach var="wo" items="${workOrderOptions}">
                        <option value="${wo.workOrderId}"
                            data-plan-id="${wo.planId}"
                            data-item-code="${wo.itemCode}"
                            data-item-name="${wo.itemName}"
                            data-line-code="${wo.lineCode}"
                            data-unit="${wo.unit}"
                            data-work-qty="${wo.workQty}"
                            data-current-produced="${wo.currentProducedSum}"
                            data-current-loss="${wo.currentLossSum}"
                            data-remaining-qty="${wo.remainingQty}">
                            ${wo.workOrderNo} / ${wo.itemName} / ${wo.lineCode}
                        </option>
                    </c:forEach>
                </select>
            </div>
            <div class="form-row"><label>생산계획번호</label><input type="text" id="registerPlanId" readonly></div>
            <div class="form-row"><label>작업지시량</label><input type="text" id="registerWorkQty" readonly></div>
            <div class="form-row"><label>남은 가능 수량</label><input type="text" id="registerRemainingQty" readonly></div>
            <div class="form-row"><label>품목코드</label><input type="text" id="registerItemCode" readonly></div>
            <div class="form-row"><label>품목명</label><input type="text" id="registerItemName" readonly></div>
            <div class="form-row"><label>단위</label><input type="text" id="registerUnit" readonly></div>
            <div class="form-row"><label>라인</label><input type="text" id="registerLineCode" readonly></div>
            <div class="form-row"><label>생산량</label><input type="number" name="producedQty" id="registerProducedQty" min="0" required></div>
            <div class="form-row"><label>손실량</label><input type="number" name="lossQty" min="0" value="0"></div>
            <div class="form-row"><label>LOT</label><input type="text" name="lotNo" id="registerLotNo" required readonly></div>
            <div class="form-row full"><label>비고</label><textarea name="remark"></textarea></div>
            <input type="hidden" name="status" value="완료">
        </div>
    </form>

    <c:if test="${empty workOrderOptions}">
        <div class="worker-empty">현재 등록 가능한 내 작업지시가 없습니다.</div>
    </c:if>
</div>

<style>
.worker-page { display: flex; flex-direction: column; gap: 18px; }
.worker-page-head { display: flex; align-items: center; justify-content: space-between; gap: 16px; flex-wrap: wrap; }
.worker-title { margin: 0; font-size: 28px; color: var(--main); }
.worker-sub { margin: 8px 0 0; color: var(--text-soft); }
.worker-form-grid { display: grid; grid-template-columns: repeat(2, minmax(0, 1fr)); gap: 18px 24px; background: var(--panel); border: 1px solid var(--line-soft); border-radius: 18px; padding: 24px; }
.worker-empty { padding: 18px 20px; border-radius: 14px; background: var(--point-soft); color: var(--point); font-weight: 600; }
@media (max-width: 768px) {
  .worker-form-grid { grid-template-columns: 1fr; padding: 18px; }
  .worker-page-head { align-items: stretch; }
  .worker-page-head .taBtn { width: 100%; }
}
</style>

<script>
document.addEventListener("DOMContentLoaded", function () {
    const workOrder = document.getElementById("registerWorkOrderId");
    const fields = {
        planId: document.getElementById("registerPlanId"),
        workQty: document.getElementById("registerWorkQty"),
        remainingQty: document.getElementById("registerRemainingQty"),
        itemCode: document.getElementById("registerItemCode"),
        itemName: document.getElementById("registerItemName"),
        unit: document.getElementById("registerUnit"),
        lineCode: document.getElementById("registerLineCode"),
        lotNo: document.getElementById("registerLotNo"),
        producedQty: document.getElementById("registerProducedQty")
    };
    const resultDate = document.getElementById("workerResultDate");
    const today = new Date();
    const yyyy = today.getFullYear();
    const mm = String(today.getMonth() + 1).padStart(2, '0');
    const dd = String(today.getDate()).padStart(2, '0');
    resultDate.value = yyyy + "-" + mm + "-" + dd;

    function syncData() {
        const opt = workOrder.options[workOrder.selectedIndex];
        fields.planId.value = opt.dataset.planId || "";
        fields.workQty.value = opt.dataset.workQty || "";
        fields.remainingQty.value = opt.dataset.remainingQty || "";
        fields.itemCode.value = opt.dataset.itemCode || "";
        fields.itemName.value = opt.dataset.itemName || "";
        fields.unit.value = opt.dataset.unit || "";
        fields.lineCode.value = opt.dataset.lineCode || "";

        if (opt.value) {
            fields.producedQty.value = opt.dataset.remainingQty || "";
            const lineCode = opt.dataset.lineCode || "LINE";
            fields.lotNo.value = "LOT-" + yyyy + mm + dd + "-" + opt.value + "-" + lineCode;
        } else {
            fields.producedQty.value = "";
            fields.lotNo.value = "";
        }
    }

    workOrder.addEventListener("change", syncData);
});
</script>
