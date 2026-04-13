<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:set var="pageTitle" value="공지사항" />
<c:set var="pageSubTitle" value="공지사항 상세 / 수정" />
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>공지사항</title>
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
				<c:if test="${not empty notice}">
					<form action="${pageContext.request.contextPath}/notice/update"
						method="post">
						<input type="hidden" name="noticeId" value="${notice.noticeId}">
						<table>
							<tr>
								<th>ID</th>
								<td>${notice.noticeId}</td>
							</tr>
							<tr>
								<th>작성자</th>
								<td>${notice.writerEmpName}</td>
							</tr>
							<tr>
								<th>제목</th>
								<td><input type="text" name="title" value="${notice.title}"></td>
							</tr>
							<tr>
								<th>내용</th>
								<td><textarea name="content">${notice.content}</textarea></td>
							</tr>
							<tr>
								<th>상태</th>
								<td><input type="text" name="status"
									value="${notice.status}"></td>
							</tr>
							<tr>
								<th>비고</th>
								<td><input type="text" name="remark"
									value="${notice.remark}"></td>
							</tr>
						</table>
						<div class="page-actions" style="margin-top: 16px">
							<button type="submit" class="btn">수정</button>
							<a href="${pageContext.request.contextPath}/notice/list"
								class="btn"
								style="display: inline-flex; align-items: center; text-decoration: none; color: var(--text); border: 1px solid var(--line); background: #fff;">목록</a>
						</div>
					</form>
				</c:if>
			</div>
		</main>
	</div>
</body>
</html>
