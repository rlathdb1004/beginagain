<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<%--
    WorkStatusInq.jsp

    현재 화면 구성
    1. 상단 : 라인별 공정 진행도 그래프
    2. 중단 : 등록 / 삭제 버튼
    3. 하단 : 조회 검색영역

    제외한 영역
    - 테이블
    - 모바일 카드
    - 페이징

    사용 request attribute
    1. lineProgressList
       - 라인별 공정 진행도 목록
       - lineCode
       - processName
       - progressRate

    주의
    - lineProgressList 는 lineCode 순으로 정렬되어 있어야
      같은 라인끼리 카드로 정상 묶인다.
--%>

<div class="workstatus-page">

    <%-- 검색 버튼 눌렀는지 여부 --%>
    <c:set var="isSearched" value="${param.searched eq 'Y'}" />

    <%-- =========================
         1. 상단 그래프 영역
         라인별 공정 진행도
         ========================= --%>
    <c:if test="${isSearched}">
        <div class="workstatus-chart-section">
            <div class="workstatus-chart-header">
                <h3 class="workstatus-section-title">라인별 공정 진행도</h3>
                <p class="workstatus-section-sub">라인별로 공정 진행 현황을 막대그래프로 표시합니다.</p>
            </div>

            <%-- 그래프 데이터가 없을 때 --%>
            <c:if test="${empty lineProgressList}">
                <div class="workstatus-empty-box">
                    표시할 진행도 데이터가 없습니다.
                </div>
            </c:if>

            <%-- 그래프 데이터가 있을 때 --%>
            <c:if test="${not empty lineProgressList}">
                <div class="workstatus-line-group-wrap">

                    <%-- 이전 라인코드를 기억해서 같은 라인끼리 묶기 위한 변수 --%>
                    <c:set var="prevLineCode" value="" />

                    <c:forEach var="progress" items="${lineProgressList}" varStatus="status">

                        <%-- 라인이 바뀌는 시점이면 새 라인 카드 시작 --%>
                        <c:if test="${prevLineCode ne progress.lineCode}">

                            <%-- 첫 번째가 아니면 이전 라인 카드 닫기 --%>
                            <c:if test="${not status.first}">
                                    </div>
                                </div>
                            </c:if>

                            <%-- 새 라인 카드 시작 --%>
                            <div class="workstatus-line-card">
                                <div class="workstatus-line-card-header">
                                    <div class="workstatus-line-title">${progress.lineCode}</div>
                                </div>

                                <div class="workstatus-line-process-list">

                            <%-- 현재 라인코드 기억 --%>
                            <c:set var="prevLineCode" value="${progress.lineCode}" />
                        </c:if>

                        <%-- 같은 라인 카드 안의 공정별 막대그래프 한 줄 --%>
                        <div class="workstatus-process-row">
                            <div class="workstatus-process-row-top">
                                <span class="workstatus-process-name">${progress.processName}</span>
                                <span class="workstatus-process-rate">${progress.progressRate}%</span>
                            </div>

                            <div class="workstatus-process-bar">
                                <div class="workstatus-process-fill"
                                     style="width:${progress.progressRate}%;"></div>
                            </div>
                        </div>

                        <%-- 마지막 데이터면 마지막 라인 카드 닫기 --%>
                        <c:if test="${status.last}">
                                </div>
                            </div>
                        </c:if>

                    </c:forEach>
                </div>
            </c:if>
        </div>
    </c:if>

    <%-- =========================
         2. 등록 / 삭제 버튼
         ========================= --%>
    <div class="taPageActions">
        <%-- 등록 버튼 : 추후 등록 모달/페이지 연결용 --%>
        <button type="button" class="taBtn taBtnPrimary">등록</button>

        <%--
            삭제 버튼
            - 현재 화면에는 테이블/체크박스가 없으므로
              실제 삭제 기능 대신 안내 메시지만 띄운다.
        --%>
        <button type="button"
                id="deleteToggleBtn"
                class="taBtn taBtnOutline"
                onclick="handleDeleteButton()">
            삭제
        </button>
    </div>

    <%-- =========================
         3. 조회 검색영역
         ========================= --%>
    <form method="get" action="${pageContext.request.contextPath}/workstatusinq">
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
                    <option value="workOrderNo" ${param.searchType eq 'workOrderNo' ? "selected" : ""}>작업지시번호</option>
                    <option value="itemCode" ${param.searchType eq 'itemCode' ? "selected" : ""}>품목코드</option>
                    <option value="itemName" ${param.searchType eq 'itemName' ? "selected" : ""}>품목명</option>
                    <option value="lineCode" ${param.searchType eq 'lineCode' ? "selected" : ""}>라인</option>
                    <option value="empName" ${param.searchType eq 'empName' ? "selected" : ""}>작업자</option>
                    <option value="status" ${param.searchType eq 'status' ? "selected" : ""}>상태</option>
                </select>
            </div>

            <%-- 검색어 입력 --%>
            <div class="taToolbarField taToolbarFieldGrow">
                <div class="taSearchBox">
                    <input type="text"
                           class="taSearchInput"
                           name="keyword"
                           placeholder="작업지시번호 / 품목코드 / 품목명 / 라인 / 작업자 / 상태 검색"
                           value="${param.keyword}">

                    <button type="submit" class="taSearchBtn" aria-label="검색">⌕</button>
                </div>
            </div>
        </div>
    </form>

</div>

<script>
document.addEventListener("DOMContentLoaded", function () {

    /*
     * 삭제 버튼 처리
     *
     * 현재 화면에서는 테이블/체크박스 영역을 제외했기 때문에
     * 실제 삭제 대신 안내 메시지만 출력한다.
     */
    window.handleDeleteButton = function () {
        alert("현재 화면에서는 테이블 영역이 제외되어 삭제할 항목을 선택할 수 없습니다.");
    };
});
</script>