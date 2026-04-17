<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!DOCTYPE html>
<html lang="ko">

<head>
<meta charset="UTF-8">
<title>자재 불량 관리</title>

<link rel="stylesheet"
	href="${pageContext.request.contextPath}/assets/css/common.css" />
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/assets/css/table.css" />
</head>

<body>

	<div class="taPageActions">
		<button type="button" class="taBtn taBtnPrimary" onclick="openModal()">등록</button>
		<button type="button" class="taBtn taBtnDanger"
			onclick="deleteSelected()">삭제</button>

	</div>


	<form method="get"
		action="${pageContext.request.contextPath}/defect-mgmt">
		<div class="taToolbarRow">

			<div class="taToolbarField taToolbarFieldGrow">
				<div class="taSearchBox">
					<input type="text" class="taSearchInput" name="keyword"
						placeholder="불량코드/불량명 검색">
					<button type="submit" class="taSearchBtn" aria-label="검색">
						<svg viewBox="0 0 24 24" fill="none" stroke="currentColor"
							stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
        <circle cx="11" cy="11" r="7"></circle>
        <path d="M20 20L16.65 16.65"></path>
    </svg>
					</button>
				</div>
			</div>
		</div>
	</form>

	<div class="taTableShell">
		<div class="taTableScroll">

			<table class="taMesTable">
				<thead>
					<tr>
						<th class="taTableHeadCell"><input type="checkbox"
							id="checkAll"></th>
						<th class="taTableHeadCell">ID</th>
						<th class="taTableHeadCell">코드</th>
						<th class="taTableHeadCell">불량명</th>
						<th class="taTableHeadCell">유형</th>
						<th class="taTableHeadCell">비고</th>
						<th class="taTableHeadCell">수정</th>
					</tr>
				</thead>

				<tbody>
					<c:forEach var="dto" items="${defectList}">
						<tr class="taTableBodyRow">
							<td class="taTableBodyCell"><input type="checkbox"
								class="row-check" name="defect_code_id"
								value="${dto.defect_code_id}"></td>
							<td class="taTableBodyCell">${dto.defect_code_id}</td>
							<td class="taTableBodyCell">${dto.defect_code}</td>
							<td class="taTableBodyCell">${dto.defect_name}</td>
							<td class="taTableBodyCell">${dto.defect_type}</td>
							<td class="taTableBodyCell">${dto.remark}</td>
							<td class="taTableBodyCell"><button type="button"
									class="taBtn"
									onclick="editDefect('${dto.defect_code_id}','${dto.defect_code}','${dto.defect_name}','${dto.defect_type}','${dto.remark}')">수정</button>
							</td>
						</tr>
					</c:forEach>


					<c:if test="${empty defectList}">
						<tr>
							<td colspan="7" style="text-align: center;">조회된 데이터가 없습니다.</td>
						</tr>
					</c:if>
				</tbody>
			</table>

		</div>
	</div>


	<div class="modal">
		<div class="modal-box">
			<div class="modal-title">불량 등록</div>

			<form method="post"
				action="${pageContext.request.contextPath}/defect-mgmt"
				class="form-row">
				<input type="hidden" name="defect_code_id" id="defect_code_id">

				<input type="text" name="defect_code" id="defect_code"
					placeholder="불량코드"> <input type="text" name="defect_name"
					id="defect_name" placeholder="불량명"> <input type="text"
					name="defect_type" id="defect_type" placeholder="유형"> <input
					type="text" name="remark" id="remark" placeholder="비고">

				<div style="display: flex; gap: 10px; margin-top: 10px;">
					<button type="submit" class="taBtn taBtnPrimary">등록</button>
					<button type="button" class="taBtn" onclick="openModal()">취소</button>
				</div>
			</form>
		</div>
	</div>

	<script>
                function openModal() {
                    document.querySelector(".modal").classList.toggle("open")
                }

                function deleteSelected() {

                    const checked = document.querySelectorAll("input[name='defect_code_id']:checked");

                    if (checked.length === 0) {
                        alert("삭제할 항목을 선택하세요.");
                        return;
                    }

                    const form = document.createElement("form");
                    form.method = "post";
                    form.action = "${pageContext.request.contextPath}/defect-mgmt-del";

                    checked.forEach(c => {
                        const input = document.createElement("input");
                        input.type = "hidden";
                        input.name = "defect_code_id";
                        input.value = c.value;
                        form.appendChild(input);
                    });

                    document.body.appendChild(form);
                    form.submit();
                }

                function editDefect(id, code, name, type, remark) {

                    document.getElementById("defect_code_id").value = id;
                    document.getElementById("defect_code").value = code;
                    document.getElementById("defect_name").value = name;
                    document.getElementById("defect_type").value = type;
                    document.getElementById("remark").value = remark;

                    openModal();
                }

                document.addEventListener("DOMContentLoaded", function () {
                    const checkAll = document.getElementById("checkAll");
                    const rowChecks = document.querySelectorAll(".row-check");
                    
                    checkAll.addEventListener("change", function () {
                        rowChecks.forEach(cb => {
                            cb.checked = checkAll.checked;
                        })
                    })

                    rowChecks.forEach(cb => {
                        cb.addEventListener("change", function () {
                            const allChecked = Array.from(rowChecks).every(c => c.checked);
                            checkAll.checked = allChecked;
                        })
                    })
                })
            </script>

</body>

</html>