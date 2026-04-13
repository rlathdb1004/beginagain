<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:set var="pageTitle" value="설비관리" />
<c:set var="pageSubTitle" value="설비 상세 / 수정" />
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>설비관리</title>
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
				<c:if test="${empty equipment}">
					<p>조회된 설비 정보가 없습니다.</p>
					<a class="btn"
						href="${pageContext.request.contextPath}/equipment/list">목록</a>
				</c:if>
				<c:if test="${not empty equipment}">
					<form action="${pageContext.request.contextPath}/equipment/update"
						method="post">
						<input type="hidden" name="equipmentId"
							value="${equipment.equipmentId}">
						<table>
							<tr>
								<th>설비번호</th>
								<td>${equipment.equipmentId}</td>
							</tr>
							<tr>
								<th>설비코드</th>
								<td>${equipment.equipmentCode}</td>
							</tr>
							<tr>
								<th>설비명</th>
								<td><input type="text" name="equipmentName"
									value="${equipment.equipmentName}"></td>
							</tr>
							<tr>
								<th>모델명</th>
								<td><input type="text" name="modelName"
									value="${equipment.modelName}"></td>
							</tr>
							<tr>
								<th>위치</th>
								<td><input type="text" name="location"
									value="${equipment.location}"></td>
							</tr>
							<tr>
								<th>제조사</th>
								<td><input type="text" name="manufacturer"
									value="${equipment.manufacturer}"></td>
							</tr>
							<tr>
								<th>공급업체</th>
								<td><input type="text" name="vendorName"
									value="${equipment.vendorName}"></td>
							</tr>
							<tr>
								<th>설비가격</th>
								<td><input type="number" name="equipmentPrice"
									value="${equipment.equipmentPrice}"></td>
							</tr>
							<tr>
								<th>구매일자</th>
								<td><input type="date" name="purchaseDate"
									value="${equipment.purchaseDate}"></td>
							</tr>
							<tr>
								<th>비고</th>
								<td><input type="text" name="remark"
									value="${equipment.remark}"></td>
							</tr>
						</table>
						<div class="page-actions" style="margin-top: 16px">
							<button type="submit" class="btn">수정</button>
							<a class="btn"
								href="${pageContext.request.contextPath}/equipment/list"
								style="display: inline-flex; align-items: center; text-decoration: none; color: var(--text); border: 1px solid var(--line); background: #fff;">목록</a>
						</div>
					</form>
				</c:if>
			</div>
		</main>
	</div>
</body>
</html>
