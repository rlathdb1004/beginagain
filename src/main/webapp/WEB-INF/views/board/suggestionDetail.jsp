<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:set var="pageTitle" value="건의사항" />
<c:set var="pageSubTitle" value="건의사항 상세 / 수정 / 답변" />
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>건의사항</title>
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
				<c:if test="${not empty suggestion}">
					<form action="${pageContext.request.contextPath}/suggestion/update"
						method="post">
						<input type="hidden" name="suggestionId"
							value="${suggestion.suggestionId}">
						<table>
							<tr>
								<th>ID</th>
								<td>${suggestion.suggestionId}</td>
							</tr>
							<tr>
								<th>작성자</th>
								<td>${suggestion.writerEmpName}</td>
							</tr>
							<tr>
								<th>제목</th>
								<td><input type="text" name="title"
									value="${suggestion.title}"></td>
							</tr>
							<tr>
								<th>내용</th>
								<td><textarea name="content">${suggestion.content}</textarea></td>
							</tr>
							<tr>
								<th>상태</th>
								<td><input type="text" name="status"
									value="${suggestion.status}"></td>
							</tr>
							<tr>
								<th>비고</th>
								<td><input type="text" name="remark"
									value="${suggestion.remark}"></td>
							</tr>
						</table>
						<div class="page-actions" style="margin-top: 16px">
							<button type="submit" class="btn">수정</button>
							<a href="${pageContext.request.contextPath}/suggestion/list"
								class="btn"
								style="display: inline-flex; align-items: center; text-decoration: none; color: var(--text); border: 1px solid var(--line); background: #fff;">목록</a>
						</div>
					</form>
					<div class="page-card" style="margin-top: 18px;">
						<h3 style="margin-top: 0">답변 목록</h3>
						<c:choose>
							<c:when test="${not empty answerList}">
								<c:forEach var="a" items="${answerList}">
									<div
										style="border: 1px solid var(--line-soft); padding: 14px; border-radius: 12px; margin-bottom: 10px; background: #fafafa;">
										<p>
											<strong>작성자:</strong> ${a.writerEmpName}
										</p>
										<p>
											<strong>상태:</strong> ${a.status}
										</p>
										<p>
											<strong>내용:</strong> ${a.content}
										</p>
										<p>
											<strong>작성일:</strong> ${a.createdAt}
										</p>
									</div>
								</c:forEach>
							</c:when>
							<c:otherwise>
								<p>등록된 답변이 없습니다.</p>
							</c:otherwise>
						</c:choose>
					</div>
				</c:if>
			</div>
		</main>
	</div>
</body>
</html>
