<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:set var="pageTitle" value="품목관리" />
<c:set var="pageSubTitle" value="품목 상세 / 수정" />
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>품목관리</title>
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
				<c:if test="${empty item}">
					<p>조회된 품목 정보가 없습니다.</p>
					<a class="btn" href="${pageContext.request.contextPath}/item/list">목록</a>
				</c:if>
				<c:if test="${not empty item}">
					<form action="${pageContext.request.contextPath}/item/update"
						method="post">
						<input type="hidden" name="itemId" value="${item.itemId}">
						<table>
							<tr>
								<th>품목번호</th>
								<td>${item.itemId}</td>
							</tr>
							<tr>
								<th>품목코드</th>
								<td>${item.itemCode}</td>
							</tr>
							<tr>
								<th>품목명</th>
								<td><input type="text" name="itemName"
									value="${item.itemName}"></td>
							</tr>
							<tr>
								<th>품목유형</th>
								<td><input type="text" name="itemType"
									value="${item.itemType}"></td>
							</tr>
							<tr>
								<th>단위</th>
								<td><input type="text" name="unit" value="${item.unit}"></td>
							</tr>
							<tr>
								<th>규격</th>
								<td><input type="text" name="spec" value="${item.spec}"></td>
							</tr>
							<tr>
								<th>공급처</th>
								<td><input type="text" name="supplierName"
									value="${item.supplierName}"></td>
							</tr>
							<tr>
								<th>안전재고</th>
								<td><input type="number" step="0.001" name="safetyStock"
									value="${item.safetyStock}"></td>
							</tr>
							<tr>
								<th>비고</th>
								<td><input type="text" name="remark" value="${item.remark}"></td>
							</tr>
						</table>
						<div class="page-actions" style="margin-top: 16px">
							<button type="submit" class="btn">수정</button>
							<a class="btn"
								href="${pageContext.request.contextPath}/item/list"
								style="display: inline-flex; align-items: center; text-decoration: none; color: var(--text); border: 1px solid var(--line); background: #fff;">목록</a>
						</div>
					</form>
				</c:if>
			</div>
		</main>
	</div>
</body>
</html>
