<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <title>품목 관리</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/common.css" />
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/table.css" />
    <style>
        .taSelect[name="dateType"] { min-width: 100px; }
        .taReadonlyLike { background:#f6f7f9; }
        .taFormGrid { display:grid; grid-template-columns: repeat(2, minmax(0, 1fr)); gap:10px; }
        .taFormGrid .full { grid-column: 1 / -1; }
    </style>
</head>
<body>
    <c:if test="${not empty errorMsg}">
        <script>alert('${errorMsg}');</script>
    </c:if>

    <div class="taPageActions">
        <button type="button" class="taBtn taBtnPrimary" onclick="openModal()">등록</button>
        <button type="button" class="taBtn taBtnDanger" onclick="deleteSelected()">삭제</button>
    </div>

    <form method="get" action="${pageContext.request.contextPath}/master-item">
        <div class="taToolbarRow">
            <div class="taToolbarField">
                <select class="taSelect" name="itemType">
                    <option value="">전체</option>
                    <option value="원자재">원자재</option>
                    <option value="완제품">완제품</option>
                </select>
            </div>
            <div class="taToolbarField">
                <div class="taSearchBox">
                    <select class="taSelect" name="dateType">
                        <option value="">전체</option>
                        <option value="reg">등록일</option>
                        <option value="mod">수정일</option>
                    </select>
                    <input type="date" class="taSearchInput" name="startDate">
                    <input type="date" class="taSearchInput" name="endDate">
                </div>
            </div>
            <div class="taToolbarField">
                <select class="taSelect" name="searchField">
                    <option value="">전체</option>
                    <option value="item_id">품목 ID</option>
                    <option value="item_code">품목 코드</option>
                    <option value="item_name">품목명</option>
                    <option value="unit">단위</option>
                    <option value="spec">규격</option>
                    <option value="supplier_name">공급업체</option>
                    <option value="safety_stock">안전재고</option>
                    <option value="use_yn">사용 여부</option>
                    <option value="created_at">등록일</option>
                    <option value="updated_at">수정일</option>
                </select>
            </div>
            <div class="taToolbarField taToolbarFieldGrow">
                <div class="taSearchBox">
                    <input type="text" class="taSearchInput" name="keyword" placeholder="검색">
                    <button type="submit" class="taSearchBtn">⌕</button>
                </div>
            </div>
        </div>
    </form>

    <div class="taTableShell">
        <div class="taTableScroll">
            <table class="taMesTable">
                <thead>
                    <tr>
                        <th class="taTableHeadCell"><input type="checkbox" id="checkAll"></th>
                        <th class="taTableHeadCell">품목 ID</th>
                        <th class="taTableHeadCell">품목 코드</th>
                        <th class="taTableHeadCell">품목명</th>
                        <th class="taTableHeadCell">품목 유형</th>
                        <th class="taTableHeadCell">단위</th>
                        <th class="taTableHeadCell">규격</th>
                        <th class="taTableHeadCell">공급업체</th>
                        <th class="taTableHeadCell">안전재고</th>
                        <th class="taTableHeadCell">사용 여부</th>
                        <th class="taTableHeadCell">수정</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach var="item" items="${itemList}">
                        <tr class="taTableBodyRow">
                            <td class="taTableBodyCell"><input type="checkbox" class="row-check" name="item_id" value="${item.item_id}"></td>
                            <td class="taTableBodyCell">${item.item_id}</td>
                            <td class="taTableBodyCell">${item.item_code}</td>
                            <td class="taTableBodyCell">${item.item_name}</td>
                            <td class="taTableBodyCell">${item.item_type}</td>
                            <td class="taTableBodyCell">${item.unit}</td>
                            <td class="taTableBodyCell">${item.spec}</td>
                            <td class="taTableBodyCell">${item.supplier_name}</td>
                            <td class="taTableBodyCell">${item.safety_stock}</td>
                            <td class="taTableBodyCell">${item.use_yn}</td>
                            <td class="taTableBodyCell">
                                <button type="button" class="taBtn"
                                    onclick="editItem('${item.item_id}','${item.item_code}','${item.item_name}','${item.item_type}','${item.unit}','${item.spec}','${item.supplier_name}','${item.safety_stock}','${item.use_yn}','${item.remark}')">수정</button>
                            </td>
                        </tr>
                    </c:forEach>
                    <c:if test="${empty itemList}">
                        <tr><td colspan="11" style="text-align:center;">조회된 데이터가 없습니다.</td></tr>
                    </c:if>
                </tbody>
            </table>
        </div>
    </div>

    <div class="modal">
        <div class="modal-box">
            <div class="modal-title">품목 등록/수정</div>
            <form method="post" action="${pageContext.request.contextPath}/master-item" class="taFormGrid">
                <input type="hidden" name="item_id" id="item_id">
                <div>
                    <label>품목 코드</label>
                    <input type="text" name="item_code" id="item_code" placeholder="품목 코드" required>
                </div>
                <div>
                    <label>품목명</label>
                    <input type="text" name="item_name" placeholder="품목명" required>
                </div>
                <div>
                    <label>품목 유형</label>
                    <select name="item_type" id="item_type" class="taSelect" required>
                        <option value="">선택</option>
                        <option value="원자재">원자재</option>
                        <option value="완제품">완제품</option>
                    </select>
                </div>
                <div>
                    <label>단위</label>
                    <select name="unit" class="taSelect" required>
                        <option value="">선택</option>
                        <option value="EA">EA</option>
                        <option value="KG">KG</option>
                        <option value="TON">TON</option>
                    </select>
                </div>
                <div>
                    <label>규격</label>
                    <input type="text" name="spec" placeholder="규격">
                </div>
                <div>
                    <label>공급업체</label>
                    <input type="text" name="supplier_name" placeholder="공급업체">
                </div>
                <div>
                    <label>안전재고</label>
                    <input type="number" min="0" name="safety_stock" placeholder="안전재고">
                </div>
                <div>
                    <label>사용 여부</label>
                    <select name="use_yn" class="taSelect" required>
                        <option value="Y">Y</option>
                        <option value="N">N</option>
                    </select>
                </div>
                <div class="full">
                    <label>비고</label>
                    <input type="text" name="remark" placeholder="비고">
                </div>
                <div class="full" style="display:flex; gap:10px; margin-top:10px;">
                    <button type="submit" class="taBtn taBtnPrimary">저장</button>
                    <button type="button" class="taBtn" onclick="closeModal()">취소</button>
                </div>
            </form>
        </div>
    </div>

    <script>
        function openModal() {
            document.querySelector(".modal").classList.add("open");
        }
        function closeModal() {
            document.querySelector(".modal").classList.remove("open");
            document.querySelector(".modal form").reset();
            document.querySelector("#item_id").value = "";
            document.querySelector("#item_code").readOnly = false;
            document.querySelector("#item_type").disabled = false;
        }
        function deleteSelected() {
            const checked = document.querySelectorAll("input[name='item_id']:checked");
            if (checked.length === 0) {
                alert("삭제할 항목을 선택하세요.");
                return;
            }
            const form = document.createElement("form");
            form.method = "post";
            form.action = "${pageContext.request.contextPath}/item-del";
            checked.forEach(c => {
                const input = document.createElement("input");
                input.type = "hidden";
                input.name = "item_id";
                input.value = c.value;
                form.appendChild(input);
            });
            document.body.appendChild(form);
            form.submit();
        }
        function editItem(item_id, item_code, item_name, item_type, unit, spec, supplier_name, safety_stock, use_yn, remark) {
            document.querySelector("#item_id").value = item_id;
            document.querySelector("input[name='item_code']").value = item_code;
            document.querySelector("input[name='item_name']").value = item_name;
            document.querySelector("select[name='item_type']").value = item_type;
            document.querySelector("select[name='unit']").value = unit;
            document.querySelector("input[name='spec']").value = spec || '';
            document.querySelector("input[name='supplier_name']").value = supplier_name || '';
            document.querySelector("input[name='safety_stock']").value = safety_stock || 0;
            document.querySelector("select[name='use_yn']").value = use_yn;
            document.querySelector("input[name='remark']").value = remark || '';
            document.querySelector("#item_code").readOnly = true;
            document.querySelector("#item_type").disabled = false;
            openModal();
        }
        document.addEventListener("DOMContentLoaded", function () {
            const checkAll = document.getElementById("checkAll");
            const rowChecks = document.querySelectorAll(".row-check");
            checkAll.addEventListener("change", function () {
                rowChecks.forEach(cb => cb.checked = checkAll.checked);
            });
            rowChecks.forEach(cb => {
                cb.addEventListener("change", function () {
                    checkAll.checked = Array.from(rowChecks).every(c => c.checked);
                });
            });
            document.querySelector(".modal form").addEventListener("submit", function(){
                const type = document.querySelector("#item_type");
                if (type.disabled) type.disabled = false;
            });
        });
    </script>
</body>
</html>
