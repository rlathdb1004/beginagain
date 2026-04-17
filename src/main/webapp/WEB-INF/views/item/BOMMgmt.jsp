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
                <c:choose>
                    <c:when test="${empty param.product_code}">
                        <button type="button" class="taBtn taBtnPrimary" onclick="alert('먼저 품목을 선택하세요.');">등록</button>
                    </c:when>
                    <c:otherwise>
                        <button type="button" class="taBtn taBtnPrimary" data-modal-target="registerModal">등록</button>
                    </c:otherwise>
                </c:choose>
                <button class="taBtn taBtnOutline" onclick="deleteSelected()">선택 삭제</button>
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
                                <th class="taTableHeadCell"><input type="checkbox" id="checkAll"></th>
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
                            <c:forEach var="BOM" items="${BOMList}">
                                <tr class="taTableBodyRow">

                                    <td class="taTableBodyCell"><input type="checkbox" class="row-check"
                                            name="bom_detail_id" value="${BOM.bom_detail_id}"></td>

                                    <td class="taTableBodyCell">${BOM.product_code}</td>
                                    <td class="taTableBodyCell">${BOM.product_name}</td>
                                    <td class="taTableBodyCell">${BOM.material_code}</td>
                                    <td class="taTableBodyCell">${BOM.material_name}</td>
                                    <td class="taTableBodyCell">${BOM.qty_required}</td>
                                    <td class="taTableBodyCell">${BOM.unit}</td>
                                    <td class="taTableBodyCell taColAction taLastCol">
                                        <a class="taLinkAnchor"
                                            href="${pageContext.request.contextPath}/bom-detail?bomDetailId=${BOM.bom_detail_id}">
                                            상세보기
                                        </a>
                                    </td>
                                </tr>
                            </c:forEach>

                            <c:if test="${empty BOMList}">
                                <tr>
                                    <td colspan="8" style="text-align:center;">
                                        조회된 데이터가 없습니다.
                                    </td>
                                </tr>
                            </c:if>
                        </tbody>
                    </table>
                </div>
            </div>


            <div class="taModal" id="registerModal" hidden aria-hidden="true">
                <div class="taModalDialog modal-lg">

                    <div class="taModalHeader">
                        <h3 class="taModalTitle">BOM 등록</h3>
                        <button type="button" class="taModalClose" onclick="closeModal()">&times;</button>
                    </div>

                    <form method="post" action="${pageContext.request.contextPath}/BOM-mgmt"
                        onsubmit="return validateForm()">

                        <input type="hidden" name="bom_detail_id" id="bom_detail_id">
                        <input type="hidden" name="product_code" id="product_code">

                        <div class="taModalBody taModalGrid">

                            <div class="form-row full">
                                <div class="taReadonlyText" id="selectedProduct"></div>
                            </div>

                            <div class="form-row">
                                <label>원자재 코드</label>
                                <input type="text" name="material_code" required>
                            </div>

                            <div class="form-row">
                                <label>단위</label>
                                <input type="text" name="unit" required>
                            </div>

                            <div class="form-row">
                                <label>소요량</label>
                                <input type="number" name="qty_required" step="0.1" min="1" required>
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

                function deleteSelected() {
                    if (!confirm('선택한 BOM을 삭제하시겠습니까?')) {
                        return false;
                    }
                    const checked = document.querySelectorAll("input[name='bom_detail_id']:checked");

                    if (checked.length === 0) {
                        alert("삭제할 항목을 선택하세요.");
                        return;
                    }

                    const form = document.createElement("form");
                    form.method = "post";
                    form.action = "${pageContext.request.contextPath}/BOM-del";

                    checked.forEach(c => {
                        const input = document.createElement("input");
                        input.type = "hidden";
                        input.name = "bom_detail_id";
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
                    })
                })

            </script>

        </body>

        </html>