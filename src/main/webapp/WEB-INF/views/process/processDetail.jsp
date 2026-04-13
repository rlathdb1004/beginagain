<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:set var="pageTitle" value="공정관리" />
<c:set var="pageSubTitle" value="공정 상세 / 수정" />
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>공정관리</title>
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
				<c:if test="${empty p}">
					<p>데이터 없음</p>
					<a href="${pageContext.request.contextPath}/process/list">목록</a>
				</c:if>
				<c:if test="${not empty p}">
					<form action="${pageContext.request.contextPath}/process/update"
						method="post">
						<input type="hidden" name="processId" value="${p.processId}">
						<table>
							<tr>
								<th>코드</th>
								<td>${p.processCode}</td>
							</tr>
							<tr>
								<th>이름</th>
								<td><input type="text" name="processName"
									value="${p.processName}"></td>
							</tr>
							<tr>
								<th>설명</th>
								<td><input type="text" name="description"
									value="${p.description}"></td>
							</tr>
							<tr>
								<th>비고</th>
								<td><input type="text" name="remark" value="${p.remark}"></td>
							</tr>
						</table>
						<div class="page-actions" style="margin-top: 16px">
							<button type="submit" class="btn">수정</button>
							<a class="btn"
								href="${pageContext.request.contextPath}/process/list"
								style="display: inline-flex; align-items: center; text-decoration: none; color: var(--text); border: 1px solid var(--line); background: #fff;">목록</a>
						</div>
					</form>
				</c:if>
			</div>
		</main>
	</div>
</body>
</html>
