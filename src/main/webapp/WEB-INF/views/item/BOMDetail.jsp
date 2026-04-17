<%@ page contentType="text/html; charset=UTF-8" %>
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

        <c:choose>
            <c:when test="${empty bom}">
                <div class="taFormShell taEmptyState">
                    <p>조회된 BOM 정보가 없습니다.</p>
                    <div class="taPageActions" style="justify-content:center; margin-top:16px;">
                        <a class="taBtn taBtnOutline" href="${pageContext.request.contextPath}/bom/list">목록</a>
                    </div>
                </div>
            </c:when>

            <c:otherwise>
                <div class="taSectionStack">

                    <div class="taPageActions">
                        <button type="button" id="editBtn" class="taBtn taBtnPrimary">수정</button>
                        <button type="submit" form="updateForm" id="saveBtn" class="taBtn taBtnPrimary"
                            style="display:none;">수정완료</button>
                        <button type="button" id="cancelBtn" class="taBtn taBtnOutline"
                            style="display:none;">취소</button>
                        <a class="taBtn taBtnOutline" href="${pageContext.request.contextPath}/BOM-mgmt">목록</a>
                    </div>

                    <div class="taFormShell">
                        <form id="updateForm" action="${pageContext.request.contextPath}/bom-update" method="post">

                            <input type="hidden" name="bomDetailId" value="${bom.bom_detail_id}">
                            <table class="taFormTable">
                                <tr>
                                    <th class="taFormLabel">완제품 코드</th>
                                    <td class="taFormValue"><span class="taReadonlyText">${bom.product_code}</span></td>
                                </tr>

                                <tr>
                                    <th class="taFormLabel">완제품명</th>
                                    <td class="taFormValue"><span class="taReadonlyText">${bom.product_name}</span></td>
                                </tr>

                                <tr>
                                    <th class="taFormLabel">원자재 코드</th>
                                    <td class="taFormValue"><input class="taFormInput taEditableField" type="text"
                                            name="materialCode" value="${bom.material_code}"></td>
                                </tr>

                                <tr>
                                    <th class="taFormLabel">원자재명</th>
                                    <td class="taFormValue"><span class="taReadonlyText" type="text">${bom.material_name}</span></td>
                                           
                                </tr>

                                <tr>
                                    <th class="taFormLabel">소요량</th>
                                    <td class="taFormValue"><input class="taFormInput taEditableField" type="number"
                                            name="qtyRequired" value="${bom.qty_required}"></td>
                                </tr>

                                <tr>
                                    <th class="taFormLabel">단위</th>
                                    <td class="taFormValue"><input class="taFormInput taEditableField" type="text"
                                            name="unit" value="${bom.unit}"></td>
                                </tr>

                                <tr>
                                    <th class="taFormLabel">비고</th>
                                    <td class="taFormValue"><input class="taFormInput taEditableField" type="text"
                                            name="remark" value="${bom.remark}"></td>
                                </tr>
                            </table>
                        </form>
                    </div>
                </div>

                <script>
                    (function () {

                        const form = document.getElementById("updateForm");
                        const editBtn = document.getElementById("editBtn");
                        const saveBtn = document.getElementById("saveBtn");
                        const cancelBtn = document.getElementById("cancelBtn");
                        const fields = form.querySelectorAll(".taEditableField");

                        function setViewMode() {
                            fields.forEach(f => f.readOnly = true);
                            editBtn.style.display = "";
                            saveBtn.style.display = "none";
                            cancelBtn.style.display = "none";
                        }

                        function setEditMode() {
                            fields.forEach(f => f.readOnly = false);
                            editBtn.style.display = "none";
                            saveBtn.style.display = "";
                            cancelBtn.style.display = "";
                        }

                        fields.forEach(f => f.dataset.original = f.value);

                        editBtn.onclick = setEditMode;

                        cancelBtn.onclick = function () {
                            fields.forEach(f => f.value = f.dataset.original);
                            setViewMode();
                        };

                        form.onsubmit = function (e) {
                            if (saveBtn.style.display === "none") {
                                e.preventDefault();
                                return;
                            }
                            if (!confirm("수정하시겠습니까?")) {
                                e.preventDefault();
                            }
                        };

                        setViewMode();

                    })()
                </script>

            </c:otherwise>
        </c:choose>