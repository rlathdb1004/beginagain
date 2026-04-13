<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<c:set var="pageTitle" value="건의사항" />
<c:set var="pageSubTitle" value="건의사항 조회 / 수정 / 삭제" />

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>건의사항 목록</title>
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/assets/css/common.css" />
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/assets/css/table.css" />
<script src="${pageContext.request.contextPath}/assets/js/layout.js"></script>
</head>
<body>

	<div class="app">
		<jsp:include page="/WEB-INF/views/common/sidebar.jsp" />

		<div class="main">
			<jsp:include page="/WEB-INF/views/common/topbar.jsp" />

			<section class="page-wrap">
				<div class="page-card">
					<div class="page-actions">
                    <button type="button" class="btn" onclick="openRegisterModal()">등록</button>
						<button type="submit" form="deleteForm" class="btn">선택 삭제</button>
						<a href="${pageContext.request.contextPath}/main" class="btn">메인</a>
					</div>

					<form id="deleteForm"
						action="${pageContext.request.contextPath}/suggestion/delete"
						method="post">
						<table>
							<thead>
								<tr>
									<th><input type="checkbox" id="checkAll"></th>
									<th>ID</th>
									<th>제목</th>
									<th>작성자</th>
									<th>상태</th>
									<th>조회수</th>
									<th>상세</th>
								</tr>
							</thead>
							<tbody>
								<c:choose>
									<c:when test="${not empty suggestionList}">
										<c:forEach var="s" items="${suggestionList}">
											<tr>
												<td><input type="checkbox" name="suggestionId"
													value="${s.suggestionId}"></td>
												<td>${s.suggestionId}</td>
												<td>${s.title}</td>
												<td>${s.writerEmpName}</td>
												<td>${s.status}</td>
												<td>${s.viewCount}</td>
												<td><a class="btn"
													href="${pageContext.request.contextPath}/suggestion/detail?suggestionId=${s.suggestionId}">상세</a></td>
											</tr>
										</c:forEach>
									</c:when>
									<c:otherwise>
										<tr>
											<td colspan="7">조회된 건의사항이 없습니다.</td>
										</tr>
									</c:otherwise>
								</c:choose>
							</tbody>
						</table>
					</form>
				</div>
			</section>
		</div>
	</div>

	<script>
		document.getElementById("checkAll").addEventListener(
				"change",
				function() {
					const checks = document
							.querySelectorAll("input[name='suggestionId']");
					for (let i = 0; i < checks.length; i++) {
						checks[i].checked = this.checked;
					}
				});
	</script>


<div class="modal" id="registerModal">
    <div class="modal-box">
        <div class="modal-header">
            <h3 class="modal-title">건의사항 등록</h3>
            <button type="button" class="modal-close" onclick="closeRegisterModal()">&times;</button>
        </div>
        <form action="${pageContext.request.contextPath}/suggestion/register" method="post">
            <div class="modal-form-grid">
                <input type="hidden" name="writerEmpId" value="${loginUser.empId}">
<div class="form-row full"><label>제목</label><input type="text" name="title" required></div>
<div class="form-row full"><label>내용</label><textarea name="content" required></textarea></div>
<div class="form-row"><label>상태</label><input type="text" name="status" value="접수"></div>
<div class="form-row full"><label>비고</label><textarea name="remark"></textarea></div>
            </div>
            <div class="modal-actions">
                <button type="button" class="btn" onclick="closeRegisterModal()">취소</button>
                <button type="submit" class="btn">등록</button>
            </div>
        </form>
    </div>
</div>
<script>
function openRegisterModal() { document.getElementById("registerModal").classList.add("open"); }
function closeRegisterModal() { document.getElementById("registerModal").classList.remove("open"); }
window.addEventListener("click", function(e) { const m = document.getElementById("registerModal"); if (e.target === m) closeRegisterModal(); });
</script>

</body>
</html>