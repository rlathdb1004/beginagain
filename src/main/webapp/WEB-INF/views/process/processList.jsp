<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<c:set var="pageTitle" value="공정관리" />
<c:set var="pageSubTitle" value="공정 등록 / 조회 / 수정 / 삭제" />

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>공정관리 목록</title>
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
						<button type="submit" form="deleteForm" class="btn"
							onclick="return confirm('선택한 공정을 삭제하시겠습니까?');">선택 삭제</button>
						<a href="${pageContext.request.contextPath}/main" class="btn">메인</a>
					</div>

					<form id="deleteForm"
						action="${pageContext.request.contextPath}/process/delete"
						method="post">
						<table>
							<thead>
								<tr>
									<th><input type="checkbox" id="checkAll"></th>
									<th>ID</th>
									<th>코드</th>
									<th>이름</th>
									<th>상세</th>
								</tr>
							</thead>
							<tbody>
								<c:choose>
									<c:when test="${not empty list}">
										<c:forEach var="p" items="${list}">
											<tr>
												<td><input type="checkbox" name="processId"
													value="${p.processId}"></td>
												<td>${p.processId}</td>
												<td>${p.processCode}</td>
												<td>${p.processName}</td>
												<td><a class="btn"
													href="${pageContext.request.contextPath}/process/detail?processId=${p.processId}">상세</a></td>
											</tr>
										</c:forEach>
									</c:when>
									<c:otherwise>
										<tr>
											<td colspan="5">데이터 없음</td>
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
							.querySelectorAll("input[name='processId']");
					for (let i = 0; i < checks.length; i++) {
						checks[i].checked = this.checked;
					}
				});
	</script>


<div class="modal" id="registerModal">
    <div class="modal-box">
        <div class="modal-header">
            <h3 class="modal-title">공정 등록</h3>
            <button type="button" class="modal-close" onclick="closeRegisterModal()">&times;</button>
        </div>
        <form action="${pageContext.request.contextPath}/process/register" method="post">
            <div class="modal-form-grid">
                <div class="form-row"><label>공정코드</label><input type="text" name="processCode" required></div>
<div class="form-row"><label>공정명</label><input type="text" name="processName" required></div>
<div class="form-row full"><label>설명</label><textarea name="description"></textarea></div>
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