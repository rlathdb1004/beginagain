<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<c:set var="pageTitle" value="설비관리" />
<c:set var="pageSubTitle" value="설비 등록 / 조회 / 수정 / 삭제" />

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>설비관리 목록</title>
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
							onclick="return confirm('선택한 설비를 삭제하시겠습니까?');">선택 삭제</button>
						<a href="${pageContext.request.contextPath}/main" class="btn">메인</a>
					</div>

					<form id="deleteForm"
						action="${pageContext.request.contextPath}/equipment/delete"
						method="post">
						<table>
							<thead>
								<tr>
									<th><input type="checkbox" id="checkAll"></th>
									<th>설비번호</th>
									<th>설비코드</th>
									<th>설비명</th>
									<th>모델명</th>
									<th>위치</th>
									<th>제조사</th>
									<th>공급업체</th>
									<th>설비가격</th>
									<th>상세</th>
								</tr>
							</thead>
							<tbody>
								<c:choose>
									<c:when test="${not empty equipmentList}">
										<c:forEach var="equipment" items="${equipmentList}">
											<tr>
												<td><input type="checkbox" name="equipmentId"
													value="${equipment.equipmentId}"></td>
												<td>${equipment.equipmentId}</td>
												<td>${equipment.equipmentCode}</td>
												<td>${equipment.equipmentName}</td>
												<td>${equipment.modelName}</td>
												<td>${equipment.location}</td>
												<td>${equipment.manufacturer}</td>
												<td>${equipment.vendorName}</td>
												<td>${equipment.equipmentPrice}</td>
												<td><a class="btn"
													href="${pageContext.request.contextPath}/equipment/detail?equipmentId=${equipment.equipmentId}">상세</a>
												</td>
											</tr>
										</c:forEach>
									</c:when>
									<c:otherwise>
										<tr>
											<td colspan="10">조회된 설비가 없습니다.</td>
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
							.querySelectorAll("input[name='equipmentId']");
					for (let i = 0; i < checks.length; i++) {
						checks[i].checked = this.checked;
					}
				});
	</script>


<div class="modal" id="registerModal">
    <div class="modal-box">
        <div class="modal-header">
            <h3 class="modal-title">설비 등록</h3>
            <button type="button" class="modal-close" onclick="closeRegisterModal()">&times;</button>
        </div>
        <form action="${pageContext.request.contextPath}/equipment/register" method="post">
            <div class="modal-form-grid">
                <div class="form-row"><label>설비코드</label><input type="text" name="equipmentCode" required></div>
<div class="form-row"><label>설비명</label><input type="text" name="equipmentName" required></div>
<div class="form-row"><label>모델명</label><input type="text" name="modelName"></div>
<div class="form-row"><label>위치</label><input type="text" name="location"></div>
<div class="form-row"><label>제조사</label><input type="text" name="manufacturer"></div>
<div class="form-row"><label>공급업체</label><input type="text" name="vendorName"></div>
<div class="form-row"><label>설비가격</label><input type="number" step="0.01" name="equipmentPrice"></div>
<div class="form-row"><label>구매일자</label><input type="date" name="purchaseDate"></div>
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