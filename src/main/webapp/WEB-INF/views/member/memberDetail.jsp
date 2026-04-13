<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:set var="pageTitle" value="사원관리" />
<c:set var="pageSubTitle" value="사원 상세 / 수정" />
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>사원관리</title>
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
				<c:if test="${empty member}">
					<p>조회된 사원 정보가 없습니다.</p>
					<a class="btn"
						href="${pageContext.request.contextPath}/member/list">목록</a>
				</c:if>
				<c:if test="${not empty member}">
					<form action="${pageContext.request.contextPath}/member/update"
						method="post">
						<input type="hidden" name="empId" value="${member.empId}">
						<table>
							<tr>
								<th>ID</th>
								<td>${member.empId}</td>
							</tr>
							<tr>
								<th>사번</th>
								<td>${member.empNo}</td>
							</tr>
							<tr>
								<th>이름</th>
								<td><input type="text" name="empName"
									value="${member.empName}"></td>
							</tr>
							<tr>
								<th>부서코드</th>
								<td><input type="text" name="deptCode"
									value="${member.deptCode}"></td>
							</tr>
							<tr>
								<th>직급</th>
								<td><input type="text" name="positionName"
									value="${member.positionName}"></td>
							</tr>
							<tr>
								<th>이메일</th>
								<td><input type="text" name="email" value="${member.email}"></td>
							</tr>
							<tr>
								<th>전화번호</th>
								<td><input type="text" name="phone" value="${member.phone}"></td>
							</tr>
							<tr>
								<th>상태</th>
								<td><input type="text" name="status"
									value="${member.status}"></td>
							</tr>
							<tr>
								<th>권한</th>
								<td><input type="text" name="roleName"
									value="${member.roleName}"></td>
							</tr>
							<tr>
								<th>임시비밀번호여부</th>
								<td><input type="text" name="tempPwdYn"
									value="${member.tempPwdYn}"></td>
							</tr>
							<tr>
								<th>비고</th>
								<td><input type="text" name="remark"
									value="${member.remark}"></td>
							</tr>
						</table>
						<div class="page-actions" style="margin-top: 16px">
							<button type="submit" class="btn">수정</button>
							<a class="btn"
								href="${pageContext.request.contextPath}/member/list"
								style="display: inline-flex; align-items: center; text-decoration: none; color: var(--text); border: 1px solid var(--line); background: #fff;">목록</a>
						</div>
					</form>
				</c:if>
			</div>
		</main>
	</div>
</body>
</html>
