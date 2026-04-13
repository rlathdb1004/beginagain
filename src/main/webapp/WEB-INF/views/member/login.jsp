<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>MES 로그인</title>
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/assets/css/common.css" />
<style>
body {
	display: grid;
	place-items: center;
	min-height: 100vh
}

.login-card {
	width: 380px;
	background: var(--panel);
	padding: 28px;
	border-radius: 24px;
	box-shadow: var(--shadow);
	border: 1px solid var(--line-soft)
}

.field {
	margin-bottom: 12px
}

.field input {
	width: 100%;
	height: 44px;
	border: 1px solid var(--line);
	border-radius: 12px;
	padding: 0 12px
}

.error {
	color: var(--danger);
	margin-bottom: 10px
}

.primary-btn {
	width: 100%;
	background: var(--main);
	color: #fff
}
</style>
</head>
<body>
	<div class="login-card">
		<h2 style="margin-top: 0">Begin Again MES</h2>
		<p style="color: var(--text-soft)">로그인</p>
		<c:if test="${not empty errorMsg}">
			<div class="error">${errorMsg}</div>
		</c:if>
		<form action="${pageContext.request.contextPath}/login" method="post">
			<div class="field">
				<input type="text" name="empNo" placeholder="사번" required>
			</div>
			<div class="field">
				<input type="password" name="password" placeholder="비밀번호" required>
			</div>
			<button class="btn primary-btn" type="submit">로그인</button>
		</form>
	</div>
</body>
</html>
