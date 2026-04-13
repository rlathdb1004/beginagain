<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<c:set var="pageTitle" value="작업지시" />
<c:set var="pageSubTitle" value="작업지시 상세 / BOM / 라우팅" />

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>작업지시</title>
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/assets/css/common.css" />
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/assets/css/table.css" />
<script src="${pageContext.request.contextPath}/assets/js/layout.js"></script>

<style>
.section-title {
	margin: 28px 0 14px;
	font-size: 18px;
	font-weight: 700;
	color: var(- -text);
}

.mini-table th, .mini-table td {
	font-size: 14px;
}

.routing-flow {
	display: flex;
	flex-wrap: wrap;
	align-items: center;
	gap: 10px;
	margin-top: 12px;
}

.routing-step {
	min-width: 160px;
	background: #fff;
	border: 1px solid var(- -line-soft);
	border-radius: 14px;
	padding: 14px 16px;
	box-shadow: var(- -shadow-soft);
}

.routing-order {
	font-size: 12px;
	color: var(- -text-soft);
	margin-bottom: 6px;
}

.routing-name {
	font-size: 16px;
	font-weight: 700;
	color: var(- -main);
}

.routing-sub {
	margin-top: 6px;
	font-size: 13px;
	color: var(- -text-soft);
}

.routing-arrow {
	font-size: 22px;
	color: var(- -sub);
	font-weight: 700;
}
</style>
</head>
<body>

	<div class="app">
		<jsp:include page="/WEB-INF/views/common/sidebar.jsp" />

		<main class="main">
			<jsp:include page="/WEB-INF/views/common/topbar.jsp" />

			<div class="page-wrap">
				<div class="page-card">
					<c:if test="${empty workOrder}">
						<p>조회된 작업지시 정보가 없습니다.</p>
						<a class="btn"
							href="${pageContext.request.contextPath}/workorder/list">목록</a>
					</c:if>

					<c:if test="${not empty workOrder}">
						<form action="${pageContext.request.contextPath}/workorder/update"
							method="post">
							<input type="hidden" name="workOrderId"
								value="${workOrder.workOrderId}">

							<table>
								<tr>
									<th>작업지시번호</th>
									<td>${workOrder.workOrderId}</td>
								</tr>
								<tr>
									<th>품목명</th>
									<td>${workOrder.itemName}</td>
								</tr>
								<tr>
									<th>작업자</th>
									<td>${workOrder.empName}</td>
								</tr>
								<tr>
									<th>지시일</th>
									<td><input type="date" name="workDate"
										value="${workOrder.workDate}"></td>
								</tr>
								<tr>
									<th>지시수량</th>
									<td><input type="number" step="0.001" name="workQty"
										value="${workOrder.workQty}"></td>
								</tr>
								<tr>
									<th>비고</th>
									<td><input type="text" name="remark"
										value="${workOrder.remark}"></td>
								</tr>
							</table>

							<div class="page-actions" style="margin-top: 16px;">
								<button type="submit" class="btn">수정</button>
								<a class="btn"
									href="${pageContext.request.contextPath}/workorder/list">목록</a>
								<a class="btn"
									href="${pageContext.request.contextPath}/productionresult/list?workOrderId=${workOrder.workOrderId}">생산실적</a>
							</div>
						</form>

						<div class="section-title">BOM</div>
						<table class="mini-table">
							<thead>
								<tr>
									<th>BOM ID</th>
									<th>자재코드</th>
									<th>자재명</th>
									<th>단위</th>
									<th>소요량</th>
								</tr>
							</thead>
							<tbody>
								<c:choose>
									<c:when test="${not empty bomList}">
										<c:forEach var="bom" items="${bomList}">
											<tr>
												<td>${bom.bomId}</td>
												<td>${bom.childItemCode}</td>
												<td>${bom.childItemName}</td>
												<td>${bom.unit}</td>
												<td>${bom.requiredQty}</td>
											</tr>
										</c:forEach>
									</c:when>
									<c:otherwise>
										<tr>
											<td colspan="5">등록된 BOM 정보가 없습니다.</td>
										</tr>
									</c:otherwise>
								</c:choose>
							</tbody>
						</table>

						<div class="section-title">라우팅</div>
						<c:choose>
							<c:when test="${not empty routingList}">
								<div class="routing-flow">
									<c:forEach var="routing" items="${routingList}" varStatus="st">
										<div class="routing-step">
											<div class="routing-order">STEP ${routing.processSeq}</div>
											<div class="routing-name">${routing.processName}</div>
											<div class="routing-sub">
												공정코드: ${routing.processCode}<br> 설비:
												${routing.equipmentName}
											</div>
										</div>

										<c:if test="${not st.last}">
											<div class="routing-arrow">→</div>
										</c:if>
									</c:forEach>
								</div>
							</c:when>
							<c:otherwise>
								<p>등록된 라우팅 정보가 없습니다.</p>
							</c:otherwise>
						</c:choose>
					</c:if>
				</div>
			</div>
		</main>
	</div>

</body>
</html>