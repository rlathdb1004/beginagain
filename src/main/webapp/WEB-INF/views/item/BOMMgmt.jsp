<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

        <!DOCTYPE html>
        <html lang="ko">

        <head>
            <meta charset="UTF-8">
            <title>BOM 관리</title>

            <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/common.css" />
            <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/table.css" />

        </head>

        <body>

            <div class="taPageActions">
                <button type="button" class="taBtn taBtnPrimary" onclick="openModal()">등록</button>
                <button type="button" class="taBtn taBtnDanger">삭제</button>
            </div>


            <form method="get" action="${pageContext.request.contextPath}/BOM-mgmt">
                <div class="taToolbarRow">

                    <div class="taToolbarField">
                        <select class="taSelect" name="product_code" id="productSelect" onchange="this.form.submit()">

                            <option value="">품목 코드 선택</option>

                            <c:forEach var="BOM" items="${BOMList}">
                                <option value="${BOM.product_code}" data-name="${BOM.product_name}" <c:if
                                    test="${param.product_code == BOM.product_code}">selected</c:if>>

                                    ${BOM.product_code} - ${BOM.product_name}
                                </option>
                            </c:forEach>

                        </select>
                    </div>


                </div>
            </form>

            <div class="taTableShell">
                <div class="taTableScroll">
                    <table class="taMesTable">
                        <thead>
                            <tr>
                                <th class="taTableHeadCell">완제품 코드</th>
                                <th class="taTableHeadCell">완제품명</th>
                                <th class="taTableHeadCell">원자재 코드</th>
                                <th class="taTableHeadCell">원자재명</th>
                                <th class="taTableHeadCell">소요량</th>
                                <th class="taTableHeadCell">단위</th>
                            </tr>
                        </thead>

                        <tbody>
                            <c:forEach var="BOM" items="${BOMList}">
                                <tr class="taTableBodyRow">

                                    <!-- 완제품 -->
                                    <td class="taTableBodyCell">${BOM.product_code}</td>
                                    <td class="taTableBodyCell">${BOM.product_name}</td>

                                    <!-- 원자재 -->
                                    <td class="taTableBodyCell">${BOM.material_code}</td>
                                    <td class="taTableBodyCell">${BOM.material_name}</td>

                                    <!-- BOM 상세 -->
                                    <td class="taTableBodyCell">${BOM.qty_required}</td>
                                    <td class="taTableBodyCell">${BOM.unit}</td>

                                </tr>
                            </c:forEach>

                            <c:if test="${empty BOMList}">
                                <tr>
                                    <td colspan="6" style="text-align:center;">
                                        조회된 데이터가 없습니다.
                                    </td>
                                </tr>
                            </c:if>
                        </tbody>
                    </table>
                </div>
            </div>


            <div class="modal">
                <div class="modal-box">
                    <div class="modal-title">품목 등록</div>

                    <form method="post" action="${pageContext.request.contextPath}/BOM-mgmt" class="form-row">
                        <input type="hidden" name="product_code" id="product_code">

                        <div id="selectedProduct"></div>

                        <input type="text" name="material_code" placeholder="원자재코드">
                        <input type="text" name="unit" placeholder="단위">
                        <input type="text" name="qty_required" placeholder="소요량">
                        <input type="text" name="remark" placeholder="비고">

                        <div style="display:flex; gap:10px; margin-top:10px;">
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

                document.addEventListener("DOMContentLoaded", function () {

                    const select = document.getElementById("productSelect");
                    const display = document.getElementById("selectedProduct");
                    const hidden = document.getElementById("product_code");

                    const selectedOption = select.options[select.selectedIndex];

                    if (selectedOption && selectedOption.value) {
                        const code = selectedOption.value;
                        const name = selectedOption.getAttribute("data-name");

                        display.textContent = code + " - " + name;
                        hidden.value = code;
                    }

                    select.addEventListener("change", function () {

                        const option = this.options[this.selectedIndex];

                        const code = option.value;
                        const name = option.getAttribute("data-name");

                        if (code) {
                            display.textContent = code + " - " + name;
                            hidden.value = code;
                        } else {
                            display.textContent = "선택 없음";
                            hidden.value = "";
                        }

                        this.form.submit();
                    });
                });

            </script>

        </body>

        </html>