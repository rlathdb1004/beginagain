<%@ page language="java" contentType="text/html; charset=UTF-8" %>
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

        <!DOCTYPE html>
        <html lang="ko">

        <head>
            <meta charset="UTF-8">
            <title>자재 불량 관리</title>

            <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/common.css" />
            <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/table.css" />
        </head>

        <body>

            <div class="taPageActions">
                <button type="button" class="taBtn taBtnPrimary" data-modal-target="registerModal">등록</button>
                <button class="taBtn taBtnOutline" onclick="deleteSelected()">선택 삭제</button>
            </div>


            <form method="get" action="${pageContext.request.contextPath}/defect-mgmt">
                <div class="taToolbarRow">

                    <div class="taToolbarField taToolbarFieldGrow">
                        <div class="taSearchBox">
                            <input type="text" class="taSearchInput" name="keyword" placeholder="불량코드/불량명 검색">
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
                                <th class="taTableHeadCell">코드</th>
                                <th class="taTableHeadCell">불량명</th>
                                <th class="taTableHeadCell">유형</th>
                                <th class="taTableHeadCell">비고</th>
                                <th class="taTableHeadCell taColAction taLastCol">상세</th>
                            </tr>
                        </thead>

                        <tbody>
                            <c:forEach var="dto" items="${defectList}">
                                <tr class="taTableBodyRow">
                                    <td class="taTableBodyCell"><input type="checkbox" class="row-check"
                                            name="defect_code_id" value="${dto.defect_code_id}"></td>
                                    <td class="taTableBodyCell">${dto.defect_code}</td>
                                    <td class="taTableBodyCell">${dto.defect_name}</td>
                                    <td class="taTableBodyCell">${dto.defect_type}</td>
                                    <td class="taTableBodyCell">${dto.remark}</td>
                                   <td class="taTableBodyCell taColAction taLastCol">
                                        <a class="taLinkAnchor"
                                            href="${pageContext.request.contextPath}/defect-detail?defectCodeId=${dto.defect_code_id}">
                                            상세보기
                                        </a>
                                    </td>
                                </tr>
                            </c:forEach>

                            <c:if test="${empty defectList}">
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


            <div class="taModal" id="registerModal" hidden aria-hidden="true">
                <div class="taModalDialog modal-lg">

                    <div class="taModalHeader">
                        <h3 class="taModalTitle">불량 등록</h3>
                        <button type="button" class="taModalClose">&times;</button>
                    </div>

                    <form method="post" action="${pageContext.request.contextPath}/defect-mgmt">

                        <input type="hidden" name="defect_code_id" id="defect_code_id">

                        <div class="taModalBody taModalGrid">

                            <div class="form-row">
                                <label>불량코드</label>
                                <input type="text" name="defect_code" id="defect_code" required>
                            </div>

                            <div class="form-row">
                                <label>불량명</label>
                                <input type="text" name="defect_name" id="defect_name" required>
                            </div>

                            <div class="form-row">
                                <label>유형</label>
                                <input type="text" name="defect_type" id="defect_type">
                            </div>

                            <div class="form-row full">
                                <label>비고</label>
                                <textarea name="remark" id="remark"></textarea>
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

                    const checked = document.querySelectorAll("input[name='defect_code_id']:checked");

                    if (checked.length === 0) {
                        alert("삭제할 항목을 선택하세요.");
                        return;
                    }

                    if (!confirm("선택한 항목을 삭제하시겠습니까?")) {
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