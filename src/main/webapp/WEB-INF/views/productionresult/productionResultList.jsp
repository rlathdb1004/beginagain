<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:set var="pageTitle" value="생산실적" />
<c:set var="pageSubTitle" value="작업지시 기준 생산실적 조회 / 수정 / 삭제" />
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>생산실적</title>
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/assets/css/common.css" />
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/assets/css/table.css" />
<script src="${pageContext.request.contextPath}/assets/js/layout.js"></script>
</head>
<body>
	<div class="app">
		<jsp:include page="/WEB-INF/views/common/sidebar.jsp" />
		<main class="main">
			<jsp:include page="/WEB-INF/views/common/topbar.jsp" />
			<div class="page-card">
				<div class="page-actions">
                    <button type="button" class="btn" onclick="openRegisterModal()">등록</button>
					<button type="submit" form="deleteForm" class="btn"
						onclick="return confirm('선택한 생산실적을 삭제하시겠습니까?');">선택 삭제</button>
					<a
						href="${pageContext.request.contextPath}/workorder/detail?workOrderId=${workOrderId}"
						class="btn"
						style="display: inline-flex; align-items: center; text-decoration: none; color: var(--text); border: 1px solid var(--line); background: #fff;">작업지시
						상세</a>
				</div>
				<form id="deleteForm"
					action="${pageContext.request.contextPath}/productionresult/delete"
					method="post">
					<input type="hidden" name="workOrderId" value="${workOrderId}">
					<table>
						<thead>
							<tr>
								<th><input type="checkbox" id="checkAll"></th>
								<th>ID</th>
								<th>품목명</th>
								<th>작업자</th>
								<th>실적일</th>
								<th>LOT NO</th>
								<th>생산수량</th>
								<th>손실수량</th>
								<th>상태</th>
								<th>상세</th>
							</tr>
						</thead>
						<tbody>
							<c:choose>
								<c:when test="${not empty productionResultList}">
									<c:forEach var="pr" items="${productionResultList}">
										<tr>
											<td><input type="checkbox" name="resultId"
												value="${pr.resultId}"></td>
											<td>${pr.resultId}</td>
											<td>${pr.itemName}</td>
											<td>${pr.empName}</td>
											<td>${pr.resultDate}</td>
											<td>${pr.lotNo}</td>
											<td>${pr.producedQty}</td>
											<td>${pr.lossQty}</td>
											<td>${pr.status}</td>
											<td><a class="btn"
												style="display: inline-flex; align-items: center; text-decoration: none; color: var(--text); border: 1px solid var(--line); background: #fff;"
												href="${pageContext.request.contextPath}/productionresult/detail?resultId=${pr.resultId}">상세</a></td>
										</tr>
									</c:forEach>
								</c:when>
								<c:otherwise>
									<tr>
										<td colspan="10">조회된 생산실적이 없습니다.</td>
									</tr>
								</c:otherwise>
							</c:choose>
						</tbody>
					</table>
				</form>
				<script>
					document
							.getElementById('checkAll')
							.addEventListener(
									'change',
									function() {
										const checks = document
												.querySelectorAll("input[name='resultId']");
										for (let i = 0; i < checks.length; i++) {
											checks[i].checked = this.checked;
										}
									});
				</script>
			</div>
		</main>
	</div>

<div class="modal" id="registerModal">
    <div class="modal-box">
        <div class="modal-header">
            <h3 class="modal-title">생산실적 등록</h3>
            <button type="button" class="modal-close" onclick="closeRegisterModal()">&times;</button>
        </div>
        <form action="${pageContext.request.contextPath}/productionresult/register" method="post">
            <div class="modal-form-grid">
                <input type="hidden" name="workOrderId" value="${workOrderId}">
<div class="form-row"><label>실적일</label><input type="date" name="resultDate"></div>
<div class="form-row"><label>LOT NO</label><input type="text" name="lotNo"></div>
<div class="form-row"><label>생산수량</label><input type="number" step="0.001" name="producedQty" required></div>
<div class="form-row"><label>손실수량</label><input type="number" step="0.001" name="lossQty"></div>
<div class="form-row"><label>상태</label><input type="text" name="status" value="등록"></div>
<div class="form-row full"><label>비고</label><textarea name="remark"></textarea></div>
            </div>
            <div class="modal-actions">
                <button type="button" class="btn" onclick="closeRegisterModal()">취소</button>
                <button type="submit" class="btn">등록</button>
            </div>
        </form>
    </div>
</div>
<script>
function openRegisterModal() { document.getElementById("registerModal").classList.add("open"); }
function closeRegisterModal() { document.getElementById("registerModal").classList.remove("open"); }
window.addEventListener("click", function(e) { const m = document.getElementById("registerModal"); if (e.target === m) closeRegisterModal(); });
</script>

</body>
</html>
