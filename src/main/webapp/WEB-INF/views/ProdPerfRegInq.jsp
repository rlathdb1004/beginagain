<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<%--
    생산실적 등록/조회 JSP

    화면 동작 방식
    1. 처음 진입:
       - searched 파라미터가 없으므로 검색창 + 테이블 헤더만 보인다.
    2. 검색 버튼 클릭:
       - searched=Y 로 들어오고, Controller 가 실제 목록을 조회해서 list 를 넘긴다.
    3. 삭제 버튼:
       - 1차 클릭 -> 체크박스 컬럼 표시
       - 2차 클릭 -> 체크된 항목 삭제
--%>

<div class="prodperf-page">

    <%-- 검색 버튼 눌렀는지 여부 --%>
    <c:set var="isSearched" value="${param.searched eq 'Y'}" />

    <%-- 상단 버튼 영역 --%>
    <div class="taPageActions">

        <%--
            등록 버튼
            - 현재는 모달 연결 전 단계이므로 버튼만 유지
            - 나중에 onclick 으로 등록 모달 연결
        --%>
        <button type="button" class="taBtn taBtnPrimary">등록</button>

        <%--
            삭제 버튼
            - 1차 클릭: 체크박스 컬럼 표시
            - 2차 클릭: 선택 항목 삭제
        --%>
        <button type="button"
                id="deleteToggleBtn"
                class="taBtn taBtnOutline"
                onclick="handleDeleteButton()">
            삭제
        </button>
    </div>

    <%-- 검색 폼 --%>
    <form method="get" action="${pageContext.request.contextPath}/prodperf">
        <%--
            searched=Y 를 같이 보내서
            Controller 에서 실제 조회를 수행하도록 한다.
        --%>
        <input type="hidden" name="searched" value="Y">

        <div class="taToolbarRow">

            <%-- 기간 검색 영역 --%>
            <div class="taToolbarField">
                <div class="taSearchBox">

                    <%-- 시작일 --%>
                    <input type="date"
                           class="taSearchInput"
                           name="startDate"
                           value="${param.startDate}">

                    <%-- 종료일 --%>
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
                    <option value="workOrderNo" ${param.searchType eq 'workOrderNo' ? "selected" : ""}>작업지시번호</option>
                    <option value="itemCode" ${param.searchType eq 'itemCode' ? "selected" : ""}>품목코드</option>
                    <option value="itemName" ${param.searchType eq 'itemName' ? "selected" : ""}>품목명</option>
                    <option value="lineCode" ${param.searchType eq 'lineCode' ? "selected" : ""}>라인</option>
                    <option value="lotNo" ${param.searchType eq 'lotNo' ? "selected" : ""}>LOT</option>
                </select>
            </div>

            <%-- 검색어 입력 영역 --%>
            <div class="taToolbarField taToolbarFieldGrow">
                <div class="taSearchBox">
                    <input type="text"
                           class="taSearchInput"
                           name="keyword"
                           placeholder="작업지시번호 / 품목코드 / 품목명 / 라인 / LOT 검색"
                           value="${param.keyword}">

                    <%--
                        검색 버튼
                        - 검색조건이 비어 있어도 전체조회 가능
                    --%>
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

    <%--
        삭제용 폼
        - 체크박스 선택값을 POST 로 Controller 에 보낸다.
        - 삭제 후에도 검색조건/페이지를 유지하기 위해 hidden 값 같이 보낸다.
    --%>
    <form id="deleteForm" method="post" action="${pageContext.request.contextPath}/prodperf">

        <%-- doPost 에서 삭제 동작 구분용 --%>
        <input type="hidden" name="cmd" value="delete">

        <%--
            삭제 모드 상태값
            N : 일반상태
            Y : 체크박스 보이는 상태
        --%>
        <input type="hidden" id="deleteMode" value="N">

        <%-- 삭제 후에도 기존 화면 상태 유지용 hidden 값들 --%>
        <input type="hidden" name="searched" value="${param.searched}">
        <input type="hidden" name="page" value="${page}">
        <input type="hidden" name="startDate" value="${param.startDate}">
        <input type="hidden" name="endDate" value="${param.endDate}">
        <input type="hidden" name="searchType" value="${param.searchType}">
        <input type="hidden" name="keyword" value="${param.keyword}">

        <%--
            테이블 영역
            - 초기 화면에서도 thead 는 항상 보이게 한다.
            - tbody 데이터는 searched=Y 일 때만 출력한다.
        --%>
        <div class="taTableShell prodperf-table-shell">
            <div class="taTableScroll">
                <table class="taMesTable">
                    <thead>
                        <tr>
                            <%--
                                삭제 모드일 때만 보이는 체크박스 컬럼
                                - CSS에서 기본 숨김
                                - JS에서 show-delete-col 클래스 추가 시 노출
                            --%>
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
                            <th class="taTableHeadCell taColGrow">비고</th>
                            <th class="taTableHeadCell taColAction taLastCol">상세보기</th>
                        </tr>
                    </thead>

                    <tbody>
                        <%--
                            searched=Y 일 때만 실제 목록 출력
                            처음 들어왔을 때는 아무 행도 보이지 않음
                        --%>
                        <c:if test="${isSearched}">

                            <%-- 목록 출력 --%>
                            <c:forEach var="dto" items="${list}">
                                <tr class="taTableBodyRow">

                                    <%-- 삭제용 체크박스 --%>
                                    <td class="taTableBodyCell taColCheck delete-col">
                                        <input type="checkbox"
                                               name="seqNO"
                                               value="${dto.seqNO}"
                                               class="rowCheck">
                                    </td>

                                    <%-- 화면 컬럼 값 출력 --%>
                                    <td class="taTableBodyCell taColFit">${dto.seqNO}</td>
                                    <td class="taTableBodyCell taColFit">${dto.workOrderNo}</td>
                                    <td class="taTableBodyCell taColDate">${dto.resultDate}</td>
                                    <td class="taTableBodyCell taColFit">${dto.itemCode}</td>
                                    <td class="taTableBodyCell taColGrow">${dto.itemName}</td>
                                    <td class="taTableBodyCell taColFit">${dto.producedQty}</td>
                                    <td class="taTableBodyCell taColFit">${dto.unit}</td>
                                    <td class="taTableBodyCell taColFit">${dto.lineCode}</td>
                                    <td class="taTableBodyCell taColFit">${dto.lotNo}</td>
                                    <td class="taTableBodyCell taColGrow">${dto.remark}</td>

                                    <%-- 상세보기 버튼: 모달 연결용 data-* 저장 --%>
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
                                                data-status="${dto.status}"
                                                data-remark="${dto.remark}">
                                            상세보기
                                        </button>
                                    </td>
                                </tr>
                            </c:forEach>

                            <%--
                                검색은 했는데 결과가 없을 때
                                colspan 은 체크박스 컬럼 포함 총 13칸
                            --%>
                            <c:if test="${empty list}">
                                <tr class="taTableBodyRow">
                                    <td class="taTableBodyCell taLastCol"
                                        colspan="13"
                                        style="text-align:center;">
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

    <%--
        페이징 영역
        - 검색했을 때만 표시
        - 검색조건을 유지한 채 페이지 이동
    --%>
    <c:if test="${isSearched}">
        <div class="taPagination">

            <%-- 이전 블록 --%>
            <c:if test="${startPage > 1}">
                <c:url var="prevUrl" value="/prodperf">
                    <c:param name="searched" value="Y" />
                    <c:param name="page" value="${startPage - 1}" />
                    <c:param name="startDate" value="${param.startDate}" />
                    <c:param name="endDate" value="${param.endDate}" />
                    <c:param name="searchType" value="${param.searchType}" />
                    <c:param name="keyword" value="${param.keyword}" />
                </c:url>
                <a class="taPageBtn" href="${prevUrl}">이전</a>
            </c:if>

            <%-- 페이지 번호들 --%>
            <c:forEach var="i" begin="${startPage}" end="${endPage}">
                <c:url var="pageUrl" value="/prodperf">
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
                <c:url var="nextUrl" value="/prodperf">
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

    /*
     * 전체선택 체크박스 처리
     * - header 의 checkAll 상태를 각 rowCheck 에 반영
     */
    if (checkAll) {
        checkAll.addEventListener("change", function () {
            const rowChecks = document.querySelectorAll(".rowCheck");
            rowChecks.forEach(function (checkbox) {
                checkbox.checked = checkAll.checked;
            });
        });
    }

    /*
     * 삭제 버튼 처리
     *
     * 1차 클릭
     * - 체크박스 컬럼 표시
     *
     * 2차 클릭
     * - 체크된 항목 있으면 삭제 submit
     * - 체크 안 했으면 다시 숨김
     */
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

        // 삭제 최종 확인
        if (confirm("선택한 생산실적을 삭제하시겠습니까?")) {
            deleteForm.submit();
        }
    };

    /*
     * 상세보기 버튼 처리
     * - 팀원이 모달 연결하기 쉽게 data 값만 전달
     */
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

            // 전역 함수가 있으면 바로 연결
            if (typeof window.openProdPerfDetailModal === "function") {
                window.openProdPerfDetailModal(detailData);
                return;
            }

            // 아니면 커스텀 이벤트로 전달
            window.dispatchEvent(new CustomEvent("prodperf:openDetail", {
                detail: detailData
            }));
        });
    });
});
</script>