<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<div class="prodresult-page">

    <!-- searched=Y 일 때만 실제 데이터 영역 표시 -->
    <c:set var="isSearched" value="${param.searched eq 'Y'}" />

    <!-- 상단 버튼 영역: 검색창 위 -->
    <div class="taPageActions">
        <!-- 등록 모달은 팀원이 작업 중이므로 버튼만 유지 -->
        <button type="button" class="taBtn taBtnPrimary">등록</button>

        <!-- 삭제 버튼:
             1차 클릭 -> 체크박스 표시
             2차 클릭 -> 선택 항목 삭제 -->
        <button type="button"
                id="deleteToggleBtn"
                class="taBtn taBtnOutline"
                onclick="handleDeleteButton()">
            삭제
        </button>
    </div>

    <!-- 검색 영역 -->
    <form method="get" action="${pageContext.request.contextPath}/prodresult">
        <!-- 검색 버튼 눌렀다는 표시 -->
        <input type="hidden" name="searched" value="Y">

        <div class="taToolbarRow">
            <div class="taToolbarField">
                <div class="taSearchBox">
                    <!-- 시작일 -->
                    <input type="date"
                           class="taSearchInput"
                           name="startDate"
                           value="${param.startDate}">

                    <!-- 종료일 -->
                    <input type="date"
                           class="taSearchInput"
                           name="endDate"
                           value="${param.endDate}">
                </div>
            </div>

            <div class="taToolbarField">
                <!-- 검색 기준 -->
                <select class="taSelect" name="searchType">
                    <option value="" ${empty param.searchType ? "selected" : ""}>전체</option>
                    <option value="workOrderNo" ${param.searchType eq 'workOrderNo' ? "selected" : ""}>작업지시번호</option>
                    <option value="itemCode" ${param.searchType eq 'itemCode' ? "selected" : ""}>품목코드</option>
                    <option value="itemName" ${param.searchType eq 'itemName' ? "selected" : ""}>품목명</option>
                    <option value="lineCode" ${param.searchType eq 'lineCode' ? "selected" : ""}>라인</option>
                    <option value="lotNo" ${param.searchType eq 'lotNo' ? "selected" : ""}>LOT</option>
                </select>
            </div>

            <div class="taToolbarField taToolbarFieldGrow">
                <div class="taSearchBox">
                    <!-- 검색 키워드 -->
                    <input type="text"
                           class="taSearchInput"
                           name="keyword"
                           placeholder="작업지시번호 / 품목코드 / 품목명 / 라인 / LOT 검색"
                           value="${param.keyword}">

                    <!-- 조건 없이 눌러도 전체조회 -->
                    <button type="submit" class="taSearchBtn" aria-label="검색">⌕</button>
                </div>
            </div>
        </div>
    </form>

    <!-- 삭제용 form -->
    <form id="deleteForm" method="post" action="${pageContext.request.contextPath}/prodresult">
        <input type="hidden" name="cmd" value="delete">

        <!-- 삭제 버튼 1차/2차 상태값 -->
        <input type="hidden" id="deleteMode" value="N">

        <!-- 삭제 후에도 검색조건 유지 -->
        <input type="hidden" name="searched" value="${param.searched}">
        <input type="hidden" name="page" value="${page}">
        <input type="hidden" name="startDate" value="${param.startDate}">
        <input type="hidden" name="endDate" value="${param.endDate}">
        <input type="hidden" name="searchType" value="${param.searchType}">
        <input type="hidden" name="keyword" value="${param.keyword}">

        <!-- 초기 화면에서도 헤더는 보이게 -->
        <div class="taTableShell prodresult-table-shell">
            <div class="taTableScroll">
                <table class="taMesTable">
                    <thead>
                        <tr>
                            <!-- 삭제 모드일 때만 보이는 체크박스 컬럼 -->
                            <th class="taTableHeadCell taColCheck delete-col">
                                <input type="checkbox" id="checkAll">
                            </th>

                            <th class="taTableHeadCell taColFit">NO</th>
                            <th class="taTableHeadCell taColFit">작업지시번호</th>
                            <th class="taTableHeadCell taColDate">일자</th>
                            <th class="taTableHeadCell taColFit">품목코드</th>
                            <th class="taTableHeadCell taColGrow">품목명</th>
                            <th class="taTableHeadCell taColFit">생산량</th>
                            <th class="taTableHeadCell taColFit">단위</th>
                            <th class="taTableHeadCell taColFit">라인</th>
                            <th class="taTableHeadCell taColFit">LOT</th>
                            <th class="taTableHeadCell taColFit">발주처</th>
                            <th class="taTableHeadCell taColGrow">비고</th>
                            <th class="taTableHeadCell taColAction taLastCol">상세보기</th>
                        </tr>
                    </thead>

                    <tbody>
                        <!-- 검색했을 때만 실제 데이터 출력 -->
                        <c:if test="${isSearched}">
                            <c:forEach var="dto" items="${list}">
                                <tr class="taTableBodyRow">
                                    <!-- 삭제 체크박스 -->
                                    <td class="taTableBodyCell taColCheck delete-col">
                                        <input type="checkbox"
                                               name="seqNO"
                                               value="${dto.seqNO}"
                                               class="rowCheck">
                                    </td>

                                    <!-- 현재 구조에서는 NO = RESULT_ID -->
                                    <td class="taTableBodyCell taColFit">${dto.seqNO}</td>
                                    <td class="taTableBodyCell taColFit">${dto.workOrderNo}</td>
                                    <td class="taTableBodyCell taColDate">${dto.resultDate}</td>
                                    <td class="taTableBodyCell taColFit">${dto.itemCode}</td>
                                    <td class="taTableBodyCell taColGrow">${dto.itemName}</td>
                                    <td class="taTableBodyCell taColFit">${dto.producedQty}</td>
                                    <td class="taTableBodyCell taColFit">${dto.unit}</td>
                                    <td class="taTableBodyCell taColFit">${dto.lineCode}</td>
                                    <td class="taTableBodyCell taColFit">${dto.lotNo}</td>
                                    <td class="taTableBodyCell taColFit">${dto.supplierName}</td>
                                    <td class="taTableBodyCell taColGrow">${dto.remark}</td>

                                    <!-- 상세보기 모달용 data 값 -->
                                    <td class="taTableBodyCell taColAction taLastCol">
                                        <button type="button"
                                                class="taLinkButton detailBtn"
                                                data-seqno="${dto.seqNO}"
                                                data-workorderno="${dto.workOrderNo}"
                                                data-resultdate="${dto.resultDate}"
                                                data-itemcode="${dto.itemCode}"
                                                data-itemname="${dto.itemName}"
                                                data-producedqty="${dto.producedQty}"
                                                data-lossqty="${dto.lossQty}"
                                                data-unit="${dto.unit}"
                                                data-linecode="${dto.lineCode}"
                                                data-lotno="${dto.lotNo}"
                                                data-suppliername="${dto.supplierName}"
                                                data-status="${dto.status}"
                                                data-remark="${dto.remark}">
                                            상세보기
                                        </button>
                                    </td>
                                </tr>
                            </c:forEach>

                            <!-- 검색했는데 결과가 없을 때 -->
                            <c:if test="${empty list}">
                                <tr class="taTableBodyRow">
                                    <td class="taTableBodyCell taLastCol" colspan="13" style="text-align:center;">
                                        조회된 데이터가 없습니다.
                                    </td>
                                </tr>
                            </c:if>
                        </c:if>
                    </tbody>
                </table>
            </div>
        </div>
    </form>

    <!-- 검색했을 때만 페이징 표시 -->
    <c:if test="${isSearched}">
        <div class="taPagination">

            <!-- 이전 블록 -->
            <c:if test="${startPage > 1}">
                <c:url var="prevUrl" value="/prodresult">
                    <c:param name="searched" value="Y" />
                    <c:param name="page" value="${startPage - 1}" />
                    <c:param name="startDate" value="${param.startDate}" />
                    <c:param name="endDate" value="${param.endDate}" />
                    <c:param name="searchType" value="${param.searchType}" />
                    <c:param name="keyword" value="${param.keyword}" />
                </c:url>
                <a class="taPageBtn" href="${prevUrl}">이전</a>
            </c:if>

            <!-- 페이지 번호 -->
            <c:forEach var="i" begin="${startPage}" end="${endPage}">
                <c:url var="pageUrl" value="/prodresult">
                    <c:param name="searched" value="Y" />
                    <c:param name="page" value="${i}" />
                    <c:param name="startDate" value="${param.startDate}" />
                    <c:param name="endDate" value="${param.endDate}" />
                    <c:param name="searchType" value="${param.searchType}" />
                    <c:param name="keyword" value="${param.keyword}" />
                </c:url>

                <a class="${page eq i ? 'taPageBtn active' : 'taPageBtn'}"
                   href="${pageUrl}">
                    ${i}
                </a>
            </c:forEach>

            <!-- 다음 블록 -->
            <c:if test="${endPage < totalPage}">
                <c:url var="nextUrl" value="/prodresult">
                    <c:param name="searched" value="Y" />
                    <c:param name="page" value="${endPage + 1}" />
                    <c:param name="startDate" value="${param.startDate}" />
                    <c:param name="endDate" value="${param.endDate}" />
                    <c:param name="searchType" value="${param.searchType}" />
                    <c:param name="keyword" value="${param.keyword}" />
                </c:url>
                <a class="taPageBtn" href="${nextUrl}">다음</a>
            </c:if>

        </div>
    </c:if>
</div>

<script>
document.addEventListener("DOMContentLoaded", function () {
    const checkAll = document.getElementById("checkAll");
    const deleteModeInput = document.getElementById("deleteMode");
    const deleteToggleBtn = document.getElementById("deleteToggleBtn");
    const deleteForm = document.getElementById("deleteForm");

    // 전체선택 체크박스
    if (checkAll) {
        checkAll.addEventListener("change", function () {
            const rowChecks = document.querySelectorAll(".rowCheck");
            rowChecks.forEach(function (checkbox) {
                checkbox.checked = checkAll.checked;
            });
        });
    }

    // 삭제 버튼 처리
    window.handleDeleteButton = function () {
        const deleteCols = document.querySelectorAll(".delete-col");
        const rowChecks = document.querySelectorAll(".rowCheck");

        // 검색 전에는 삭제 금지
        const isSearched = "${param.searched}" === "Y";
        if (!isSearched) {
            alert("먼저 조회를 해주세요.");
            return;
        }

        // 1차 클릭: 체크박스 보이기
        if (deleteModeInput.value === "N") {
            deleteCols.forEach(function (col) {
                col.classList.add("show-delete-col");
            });

            deleteModeInput.value = "Y";
            deleteToggleBtn.textContent = "삭제확인";
            return;
        }

        // 2차 클릭: 체크된 항목 확인
        let checkedCount = 0;
        rowChecks.forEach(function (checkbox) {
            if (checkbox.checked) {
                checkedCount++;
            }
        });

        // 체크 안 했으면 다시 숨김
        if (checkedCount === 0) {
            alert("삭제할 항목을 선택하세요.");

            if (checkAll) {
                checkAll.checked = false;
            }

            rowChecks.forEach(function (checkbox) {
                checkbox.checked = false;
            });

            deleteCols.forEach(function (col) {
                col.classList.remove("show-delete-col");
            });

            deleteModeInput.value = "N";
            deleteToggleBtn.textContent = "삭제";
            return;
        }

        if (confirm("선택한 생산실적을 삭제하시겠습니까?")) {
            deleteForm.submit();
        }
    };

    // 상세보기 버튼:
    // 성범이가 모달 연결하기 쉽게 data 값만 넘겨둠
    const detailButtons = document.querySelectorAll(".detailBtn");

    detailButtons.forEach(function (btn) {
        btn.addEventListener("click", function () {
            const detailData = {
                seqNo: this.dataset.seqno,
                workOrderNo: this.dataset.workorderno,
                resultDate: this.dataset.resultdate,
                itemCode: this.dataset.itemcode,
                itemName: this.dataset.itemname,
                producedQty: this.dataset.producedqty,
                lossQty: this.dataset.lossqty,
                unit: this.dataset.unit,
                lineCode: this.dataset.linecode,
                lotNo: this.dataset.lotno,
                supplierName: this.dataset.suppliername,
                status: this.dataset.status,
                remark: this.dataset.remark
            };

            // 팀원이 전역 함수 붙이면 바로 연결됨
            if (typeof window.openProdResultDetailModal === "function") {
                window.openProdResultDetailModal(detailData);
                return;
            }

            // 아니면 커스텀 이벤트로도 받을 수 있음
            window.dispatchEvent(new CustomEvent("prodresult:openDetail", {
                detail: detailData
            }));
        });
    });
});
</script>