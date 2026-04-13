<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:set var="pageTitle" value="생산실적" />
<c:set var="pageSubTitle" value="생산실적 상세 / 수정" />
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
				<c:if test="${empty productionResult}">
					<p>조회된 생산실적 정보가 없습니다.</p>
					<a class="btn"
						href="${pageContext.request.contextPath}/workorder/list">작업지시
						목록</a>
				</c:if>
				<c:if test="${not empty productionResult}">
					<form
						action="${pageContext.request.contextPath}/productionresult/update"
						method="post">
						<input type="hidden" name="resultId"
							value="${productionResult.resultId}">
						<table>
							<tr>
								<th>실적번호</th>
								<td>${productionResult.resultId}</td>
							</tr>
							<tr>
								<th>작업지시번호</th>
								<td>${productionResult.workOrderId}</td>
							</tr>
							<tr>
								<th>품목명</th>
								<td>${productionResult.itemName}</td>
							</tr>
							<tr>
								<th>작업자</th>
								<td>${productionResult.empName}</td>
							</tr>
							<tr>
								<th>실적일</th>
								<td><input type="date" name="resultDate"
									value="${productionResult.resultDate}"></td>
							</tr>
							<tr>
								<th>LOT NO</th>
								<td><input type="text" name="lotNo"
									value="${productionResult.lotNo}"></td>
							</tr>
							<tr>
								<th>생산수량</th>
								<td><input type="number" step="0.001" name="producedQty"
									value="${productionResult.producedQty}"></td>
							</tr>
							<tr>
								<th>손실수량</th>
								<td><input type="number" step="0.001" name="lossQty"
									value="${productionResult.lossQty}"></td>
							</tr>
							<tr>
								<th>상태</th>
								<td><input type="text" name="status"
									value="${productionResult.status}"></td>
							</tr>
							<tr>
								<th>비고</th>
								<td><input type="text" name="remark"
									value="${productionResult.remark}"></td>
							</tr>
						</table>
						<div class="page-actions" style="margin-top: 16px">
							<button type="submit" class="btn">수정</button>
							<a class="btn"
								href="${pageContext.request.contextPath}/productionresult/list?workOrderId=${productionResult.workOrderId}"
								style="display: inline-flex; align-items: center; text-decoration: none; color: var(--text); border: 1px solid var(--line); background: #fff;">목록</a>
						</div>
					</form>
				</c:if>
			</div>
		</main>
	</div>
</body>
</html>
