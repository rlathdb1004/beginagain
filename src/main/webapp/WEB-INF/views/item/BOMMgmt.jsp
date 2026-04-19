<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<div class="taPageActions">
	<c:choose>
		<c:when test="${empty param.product_code}">
			<button type="button" class="taBtn taBtnPrimary"
				onclick="alert('먼저 완제품을 선택하세요.');">등록</button>
		</c:when>
		<c:otherwise>
			<button type="button" class="taBtn taBtnPrimary"
				data-modal-target="registerModal">등록</button>
		</c:otherwise>
	</c:choose>
	<button class="taBtn taBtnOutline" onclick="deleteSelected()">선택
		삭제</button>
</div>

<form method="get" action="${pageContext.request.contextPath}/BOM-mgmt"
	class="taLocalSearchForm">
	<div class="taToolbarRow">
		<div class="taToolbarField taToolbarFieldGrow taSpan2">
			<label style="display: block; margin-bottom: 8px; font-weight: 600;">완제품
				선택</label> <select class="taSelect" name="product_code" id="productSelect"
				onchange="this.form.submit()">
				<option value="">품목을 선택하세요</option>
				<c:forEach var="item" items="${productItems}">
					<option value="${item.item_code}" data-name="${item.item_name}"
						<c:if test="${param.product_code == item.item_code}">selected</c:if>>
						${item.item_code} - ${item.item_name}</option>
				</c:forEach>
			</select>
		</div>
	</div>
</form>

<c:if test="${not empty sessionScope.errorMsg}">
	<script>
		alert('${sessionScope.errorMsg}');
	</script>
	<c:remove var="errorMsg" scope="session" />
</c:if>

<div class="taTableShell">
	<div class="taTableScroll">
		<table class="taMesTable">
			<thead>
				<tr>
					<th class="taTableHeadCell taCheckCell"><input type="checkbox"
						id="checkAll" class="taCheckInput"></th>
					<th class="taTableHeadCell">완제품 코드</th>
					<th class="taTableHeadCell">완제품명</th>
					<th class="taTableHeadCell">원자재 코드</th>
					<th class="taTableHeadCell">원자재명</th>
					<th class="taTableHeadCell">소요량</th>
					<th class="taTableHeadCell">단위</th>
					<th class="taTableHeadCell taColAction taLastCol">상세</th>
				</tr>
			</thead>
			<tbody>
				<c:choose>
					<c:when test="${empty param.product_code}">
						<tr class="taTableBodyRow">
							<td class="taTableBodyCell taLastCol" colspan="8"
								style="text-align: center;">완제품을 선택하면 BOM이 조회됩니다.</td>
						</tr>
					</c:when>

					<c:when test="${not empty BOMList}">
						<c:forEach var="BOM" items="${BOMList}">
							<tr class="taTableBodyRow">
								<td class="taTableBodyCell taCheckCell"><input
									type="checkbox" class="row-check taCheckInput"
									name="bom_detail_id" value="${BOM.bom_detail_id}"></td>
								<td class="taTableBodyCell">${BOM.product_code}</td>
								<td class="taTableBodyCell">${BOM.product_name}</td>
								<td class="taTableBodyCell">${BOM.material_code}</td>
								<td class="taTableBodyCell">${BOM.material_name}</td>
								<td class="taTableBodyCell">${BOM.qty_required}</td>
								<td class="taTableBodyCell">${BOM.unit}</td>
								<td class="taTableBodyCell taColAction taLastCol"><a
									class="taLinkAnchor"
									href="${pageContext.request.contextPath}/bom-detail?bomDetailId=${BOM.bom_detail_id}">상세보기</a>
								</td>
							</tr>
						</c:forEach>
					</c:when>

					<c:otherwise>
						<tr class="taTableBodyRow">
							<td class="taTableBodyCell taLastCol" colspan="8"
								style="text-align: center;">해당 품목의 BOM 데이터가 없습니다.</td>
						</tr>
					</c:otherwise>
				</c:choose>
			</tbody>
		</table>
	</div>
</div>

<div class="taModal" id="registerModal" hidden aria-hidden="true">
	<div class="taModalDialog modal-lg">
		<div class="taModalHeader">
			<h3 class="taModalTitle">BOM 등록</h3>
			<button type="button" class="taModalClose">&times;</button>
		</div>
		<form method="post"
			action="${pageContext.request.contextPath}/BOM-mgmt">
			<input type="hidden" name="product_code" id="product_code">
			<div class="taModalBody taModalGrid">
				<div class="form-row full">
					<label>선택 완제품</label>
					<div class="taReadonlyText" id="selectedProduct">선택 없음</div>
				</div>
				<div class="form-row">
					<label>원자재 선택</label> <select class="taSelect" name="material_id"
						id="materialSelect" required>
						<option value="">원자재 선택</option>
						<c:forEach var="item" items="${materialItems}">
							<option value="${item.item_id}" data-code="${item.item_code}"
								data-name="${item.item_name}" data-unit="${item.unit}">
								${item.item_code} - ${item.item_name}</option>
						</c:forEach>
					</select>
				</div>
				<div class="form-row">
					<label>원자재 코드</label><input type="text" id="materialCode" readonly>
				</div>
				<div class="form-row">
					<label>원자재명</label><input type="text" id="materialName" readonly>
				</div>
				<div class="form-row">
					<label>단위</label><input type="text" id="materialUnit" readonly>
				</div>
				<div class="form-row">
					<label>소요량</label><input type="number" name="qty_required"
						step="0.001" min="0.001" required>
				</div>
				<div class="form-row full">
					<label>비고</label>
					<textarea name="remark"></textarea>
				</div>
			</div>
			<div class="taModalFooter">
				<button type="button" class="taBtn taBtnOutline taModalClose">취소</button>
				<button type="submit" class="taBtn taBtnPrimary">등록</button>
			</div>
		</form>
	</div>
</div>

<script>
	document.getElementById('checkAll').addEventListener('change', function() {
		const checked = this.checked;
		document.querySelectorAll('.row-check').forEach(function(cb) {
			cb.checked = checked;
		});
	});

	function deleteSelected() {
		if (!confirm('선택한 BOM을 삭제하시겠습니까?'))
			return false;
		const checked = document
				.querySelectorAll("input[name='bom_detail_id']:checked");
		if (checked.length === 0) {
			alert("삭제할 항목을 선택하세요.");
			return;
		}
		const form = document.createElement("form");
		form.method = "post";
		form.action = "${pageContext.request.contextPath}/BOM-del";

		checked.forEach(function(c) {
			const input = document.createElement("input");
			input.type = "hidden";
			input.name = "bom_detail_id";
			input.value = c.value;
			form.appendChild(input);
		});

		document.body.appendChild(form);
		form.submit();
	}

	(function() {
		const productSelect = document.getElementById('productSelect');
		const selectedProduct = document.getElementById('selectedProduct');
		const productCode = document.getElementById('product_code');
		const materialSelect = document.getElementById('materialSelect');
		const materialCode = document.getElementById('materialCode');
		const materialName = document.getElementById('materialName');
		const materialUnit = document.getElementById('materialUnit');

		function syncProduct() {
			const option = productSelect.options[productSelect.selectedIndex];
			if (option && option.value) {
				selectedProduct.textContent = option.value + ' - '
						+ (option.getAttribute('data-name') || '');
				productCode.value = option.value;
			} else {
				selectedProduct.textContent = '선택 없음';
				productCode.value = '';
			}
		}

		function syncMaterial() {
			const option = materialSelect.options[materialSelect.selectedIndex];
			materialCode.value = option && option.value ? (option
					.getAttribute('data-code') || '') : '';
			materialName.value = option && option.value ? (option
					.getAttribute('data-name') || '') : '';
			materialUnit.value = option && option.value ? (option
					.getAttribute('data-unit') || '') : '';
		}

		if (productSelect)
			syncProduct();
		if (materialSelect)
			syncMaterial();
		if (materialSelect)
			materialSelect.addEventListener('change', syncMaterial);
	})();
</script>