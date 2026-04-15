<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<div class="prodplan-page">

    <%-- 검색 버튼 눌렀는지 여부 --%>
    <c:set var="isSearched" value="${param.searched eq 'Y'}" />

    <%-- 상단 버튼 영역 --%>
    <div class="taPageActions">
        <%-- 등록 버튼: 모달 연결 전 단계라 버튼만 유지 --%>
        <button type="button" class="taBtn taBtnPrimary">등록</button>

        <%-- 삭제 버튼: 1차 클릭 시 체크박스 표시, 2차 클릭 시 삭제 --%>
        <button type="button"
                id="deleteToggleBtn"
                class="taBtn taBtnOutline"
                onclick="handleDeleteButton()">
            삭제
        </button>
    </div>

    <%-- 검색 폼 --%>
    <form method="get" action="${pageContext.request.contextPath}/prodplan">
        <%-- 검색 버튼 눌렀다는 표시 --%>
        <input type="hidden" name="searched" value="Y">

        <div class="taToolbarRow">
            <%-- 기간 검색 --%>
            <div class="taToolbarField">
                <div class="taSearchBox">
                    <input type="date"
                           class="taSearchInput"
                           name="startDate"
                           value="${param.startDate}">

                    <input type="date"
                           class="taSearchInput"
                           name="endDate"
                           value="${param.endDate}">
                </div>
            </div>

            <%-- 검색 기준 선택 --%>
            <div class="taToolbarField">
                <select class="taSelect" name="searchType">
                    <option value="" ${empty param.searchType ? "selected" : ""}>전체</option>
                    <option value="planNo" ${param.searchType eq 'planNo' ? "selected" : ""}>생산계획번호</option>
                    <option value="planCode" ${param.searchType eq 'planCode' ? "selected" : ""}>품목코드</option>
                    <option value="planName" ${param.searchType eq 'planName' ? "selected" : ""}>품목명</option>
                    <option value="planLine" ${param.searchType eq 'planLine' ? "selected" : ""}>라인</option>
                </select>
            </div>

            <%-- 검색어 입력 --%>
            <div class="taToolbarField taToolbarFieldGrow">
                <div class="taSearchBox">
                    <input type="text"
                           class="taSearchInput"
                           name="keyword"
                           placeholder="생산계획번호 / 품목코드 / 품목명 / 라인 검색"
                           value="${param.keyword}">

                    <%-- 조건 없이 눌러도 전체조회 가능 --%>
<!--                     <button type="submit" class="taSearchBtn" aria-label="검색">⌕</button> -->
                    <button type="submit" class="taSearchBtn" aria-label="검색">
                    	<svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                        <circle cx="11" cy="11" r="7"></circle>
                        <path d="M20 20L16.65 16.65"></path></svg>
                    </button>
                </div>
            </div>
        </div>
    </form>

    <%-- 삭제용 폼 --%>
    <form id="deleteForm" method="post" action="${pageContext.request.contextPath}/prodplan">
        <%-- doPost에서 삭제 동작 구분 --%>
        <input type="hidden" name="cmd" value="delete">

        <%-- 삭제 버튼 상태값 --%>
        <input type="hidden" id="deleteMode" value="N">

        <%-- 삭제 후에도 검색조건 유지 --%>
        <input type="hidden" name="searched" value="${param.searched}">
        <input type="hidden" name="page" value="${page}">
        <input type="hidden" name="startDate" value="${param.startDate}">
        <input type="hidden" name="endDate" value="${param.endDate}">
        <input type="hidden" name="searchType" value="${param.searchType}">
        <input type="hidden" name="keyword" value="${param.keyword}">

        <%-- 초기 화면에서도 헤더는 보이게 --%>
        <div class="taTableShell prodplan-table-shell">
            <div class="taTableScroll">
                <table class="taMesTable">
                    <thead>
                        <tr>
                            <%-- 삭제 모드일 때만 보이는 체크박스 컬럼 --%>
                            <th class="taTableHeadCell taColCheck delete-col">
                                <input type="checkbox" id="checkAll">
                            </th>

                            <th class="taTableHeadCell taColFit">NO</th>
                            <th class="taTableHeadCell taColFit">생산계획번호</th>
                            <th class="taTableHeadCell taColDate">일자</th>
                            <th class="taTableHeadCell taColFit">품목코드</th>
                            <th class="taTableHeadCell taColGrow">품목명</th>
                            <th class="taTableHeadCell taColFit">생산계획량</th>
                            <th class="taTableHeadCell taColFit">단위</th>
                            <th class="taTableHeadCell taColFit">라인</th>
                            <th class="taTableHeadCell taColGrow">비고</th>
                            <th class="taTableHeadCell taColAction taLastCol">상세보기</th>
                        </tr>
                    </thead>

                    <tbody>
                        <%-- 검색했을 때만 실제 데이터 출력 --%>
                        <c:if test="${isSearched}">
                            <c:forEach var="dto" items="${list}">
                                <tr class="taTableBodyRow">
                                    <%-- 삭제 체크박스 --%>
                                    <td class="taTableBodyCell taColCheck delete-col">
                                        <input type="checkbox"
                                               name="seqNO"
                                               value="${dto.seqNO}"
                                               class="rowCheck">
                                    </td>

                                    <td class="taTableBodyCell taColFit">${dto.seqNO}</td>
                                    <td class="taTableBodyCell taColFit">${dto.planNo}</td>
                                    <td class="taTableBodyCell taColDate">${dto.planDate}</td>
                                    <td class="taTableBodyCell taColFit">${dto.planCode}</td>
                                    <td class="taTableBodyCell taColGrow">${dto.planName}</td>
                                    <td class="taTableBodyCell taColFit">${dto.planAmount}</td>
                                    <td class="taTableBodyCell taColFit">${dto.planUnit}</td>
                                    <td class="taTableBodyCell taColFit">${dto.planLine}</td>
                                    <td class="taTableBodyCell taColGrow">${dto.memo}</td>

                                    <%-- 상세보기 모달 연결용 data-* --%>
                                    <td class="taTableBodyCell taColAction taLastCol">
                                        <button type="button"
                                                class="taLinkButton detailBtn"
                                                data-seqno="${dto.seqNO}"
                                                data-planno="${dto.planNo}"
                                                data-plandate="${dto.planDate}"
                                                data-plancode="${dto.planCode}"
                                                data-planname="${dto.planName}"
                                                data-planamount="${dto.planAmount}"
                                                data-planunit="${dto.planUnit}"
                                                data-planline="${dto.planLine}"
                                                data-memo="${dto.memo}">
                                            상세보기
                                        </button>
                                    </td>
                                </tr>
                            </c:forEach>

                            <%-- 검색했는데 결과가 없을 때 --%>
                            <c:if test="${empty list}">
                                <tr class="taTableBodyRow">
                                    <td class="taTableBodyCell taLastCol" colspan="11" style="text-align:center;">
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

    <%-- 검색했을 때만 페이징 표시 --%>
    <c:if test="${isSearched}">
        <div class="taPagination">

            <%-- 이전 블록 --%>
            <c:if test="${startPage > 1}">
                <c:url var="prevUrl" value="/prodplan">
                    <c:param name="searched" value="Y" />
                    <c:param name="page" value="${startPage - 1}" />
                    <c:param name="startDate" value="${param.startDate}" />
                    <c:param name="endDate" value="${param.endDate}" />
                    <c:param name="searchType" value="${param.searchType}" />
                    <c:param name="keyword" value="${param.keyword}" />
                </c:url>
                <a class="taPageBtn" href="${prevUrl}">이전</a>
            </c:if>

            <%-- 페이지 번호 --%>
            <c:forEach var="i" begin="${startPage}" end="${endPage}">
                <c:url var="pageUrl" value="/prodplan">
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

            <%-- 다음 블록 --%>
            <c:if test="${endPage < totalPage}">
                <c:url var="nextUrl" value="/prodplan">
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

        // 2차 클릭: 체크된 항목 수 확인
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

        if (confirm("선택한 생산계획을 삭제하시겠습니까?")) {
            deleteForm.submit();
        }
    };

    // 상세보기 버튼
    const detailButtons = document.querySelectorAll(".detailBtn");

    detailButtons.forEach(function (btn) {
        btn.addEventListener("click", function () {
            const detailData = {
                seqNo: this.dataset.seqno,
                planNo: this.dataset.planno,
                planDate: this.dataset.plandate,
                planCode: this.dataset.plancode,
                planName: this.dataset.planname,
                planAmount: this.dataset.planamount,
                planUnit: this.dataset.planunit,
                planLine: this.dataset.planline,
                memo: this.dataset.memo
            };

            // 팀원이 전역 함수 붙이면 바로 사용 가능
            if (typeof window.openProdPlanDetailModal === "function") {
                window.openProdPlanDetailModal(detailData);
                return;
            }

            // 아니면 커스텀 이벤트로도 연결 가능
            window.dispatchEvent(new CustomEvent("prodplan:openDetail", {
                detail: detailData
            }));
        });
    });
});
</script>