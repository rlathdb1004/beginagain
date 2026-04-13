<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<c:set var="pageTitle" value="작업관리" />
<c:set var="pageSubTitle" value="작업지시 등록 / 조회" />

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>작업지시 목록</title>

<link rel="stylesheet"
	href="${pageContext.request.contextPath}/assets/css/common.css" />
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/assets/css/table.css" />

<style>
.page-actions {
	margin-bottom: 16px;
	display: flex;
	gap: 10px;
}

/* 모달 */
.modal-backdrop {
	display: none;
	position: fixed;
	inset: 0;
	background: rgba(0, 0, 0, 0.4);
	z-index: 999;
}

.modal-backdrop.open {
	display: block;
}

.modal-box {
	width: 600px;
	background: #fff;
	border-radius: 16px;
	padding: 20px;
	position: absolute;
	top: 50%;
	left: 50%;
	transform: translate(-50%, -50%);
}

.form-grid {
	display: grid;
	grid-template-columns: 120px 1fr;
	gap: 10px;
}

.form-grid input, .form-grid select {
	padding: 8px;
}

.modal-footer {
	margin-top: 20px;
	text-align: right;
}
</style>
</head>

<body>

	<div class="app">
		<jsp:include page="/WEB-INF/views/common/sidebar.jsp" />

		<div class="main">
			<jsp:include page="/WEB-INF/views/common/topbar.jsp" />

			<section class="page-wrap">
				<div class="page-card">

					<!-- 버튼 -->
					<div class="page-actions">
						<button type="button" class="btn" id="openModal">등록</button>
					</div>

					<!-- 목록 -->
					<table>
						<thead>
							<tr>
								<th>지시번호</th>
								<th>작업지시코드</th>
								<th>품목코드</th>
								<th>품목명</th>
								<th>담당자</th>
								<th>지시일</th>
								<th>지시수량</th>
								<th>상세</th>
							</tr>
						</thead>

						<tbody>
							<c:choose>
								<c:when test="${not empty workOrderList}">
									<c:forEach var="wo" items="${workOrderList}">
										<tr>
											<td>${wo.workOrderId}</td>
											<td>${wo.workOrderDisplayCode}</td>
											<td>${wo.itemCode}</td>
											<td>${wo.itemName}</td>
											<td>${wo.empName}</td>
											<td>${wo.workDate}</td>
											<td>${wo.workQty}</td>
											<td><a class="btn"
												href="${pageContext.request.contextPath}/workorder/detail?workOrderId=${wo.workOrderId}">
													상세 </a></td>
										</tr>
									</c:forEach>
								</c:when>

								<c:otherwise>
									<tr>
										<td colspan="8">데이터 없음</td>
									</tr>
								</c:otherwise>
							</c:choose>
						</tbody>
					</table>

				</div>
			</section>
		</div>
	</div>

	<!-- 등록 모달 -->
	<div class="modal-backdrop" id="modal">
		<div class="modal-box">

			<h3>작업지시 등록</h3>

			<form action="${pageContext.request.contextPath}/workorder/register"
				method="post">
				<div class="form-grid">

					<label>품목ID</label> <input type="number" name="itemId" required>

					<label>계획ID</label> <input type="number" name="planId" required>

					<label>담당자ID</label> <input type="number" name="empId" required>

					<label>지시일</label> <input type="date" name="workDate" required>

					<label>지시수량</label> <input type="number" step="0.001"
						name="workQty" required> <label>상태</label> <select
						name="status">
						<option value="READY">READY</option>
						<option value="APPROVED">APPROVED</option>
					</select> <label>비고</label> <input type="text" name="remark">

				</div>

				<div class="modal-footer">
					<button type="button" class="btn" id="closeModal">취소</button>
					<button type="submit" class="btn">저장</button>
				</div>
			</form>

		</div>
	</div>

	<script>
const modal = document.getElementById("modal");

document.getElementById("openModal").onclick = () => {
    modal.classList.add("open");
};

document.getElementById("closeModal").onclick = () => {
    modal.classList.remove("open");
};

modal.onclick = (e) => {
    if (e.target === modal) {
        modal.classList.remove("open");
    }
};
</script>

</body>
</html>