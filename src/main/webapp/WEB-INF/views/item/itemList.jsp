<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<c:set var="pageTitle" value="품목관리" />
<c:set var="pageSubTitle" value="품목 등록 / 조회 / 수정 / 삭제" />

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>품목관리 목록</title>
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
							onclick="return confirm('선택한 품목을 삭제하시겠습니까?');">선택 삭제</button>
						<a href="${pageContext.request.contextPath}/main" class="btn">메인</a>
					</div>

					<form id="deleteForm"
						action="${pageContext.request.contextPath}/item/delete"
						method="post">
						<table>
							<thead>
								<tr>
									<th><input type="checkbox" id="checkAll"></th>
									<th>품목번호</th>
									<th>품목코드</th>
									<th>품목명</th>
									<th>품목유형</th>
									<th>단위</th>
									<th>규격</th>
									<th>공급처</th>
									<th>안전재고</th>
									<th>상세</th>
								</tr>
							</thead>
							<tbody>
								<c:choose>
									<c:when test="${not empty itemList}">
										<c:forEach var="item" items="${itemList}">
											<tr>
												<td><input type="checkbox" name="itemId"
													value="${item.itemId}"></td>
												<td>${item.itemId}</td>
												<td>${item.itemCode}</td>
												<td>${item.itemName}</td>
												<td>${item.itemType}</td>
												<td>${item.unit}</td>
												<td>${item.spec}</td>
												<td>${item.supplierName}</td>
												<td>${item.safetyStock}</td>
												<td><a class="btn"
													href="${pageContext.request.contextPath}/item/detail?itemId=${item.itemId}">상세</a>
												</td>
											</tr>
										</c:forEach>
									</c:when>
									<c:otherwise>
										<tr>
											<td colspan="10">조회된 품목이 없습니다.</td>
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
							.querySelectorAll("input[name='itemId']");
					for (let i = 0; i < checks.length; i++) {
						checks[i].checked = this.checked;
					}
				});
	</script>


<div class="modal" id="registerModal">
    <div class="modal-box">
        <div class="modal-header">
            <h3 class="modal-title">품목 등록</h3>
            <button type="button" class="modal-close" onclick="closeRegisterModal()">&times;</button>
        </div>
        <form action="${pageContext.request.contextPath}/item/register" method="post">
            <div class="modal-form-grid">
                <div class="form-row"><label>품목코드</label><input type="text" name="itemCode" required></div>
<div class="form-row"><label>품목명</label><input type="text" name="itemName" required></div>
<div class="form-row"><label>품목유형</label><input type="text" name="itemType"></div>
<div class="form-row"><label>단위</label><input type="text" name="unit"></div>
<div class="form-row"><label>규격</label><input type="text" name="spec"></div>
<div class="form-row"><label>공급처</label><input type="text" name="supplierName"></div>
<div class="form-row"><label>안전재고</label><input type="number" step="0.001" name="safetyStock"></div>
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