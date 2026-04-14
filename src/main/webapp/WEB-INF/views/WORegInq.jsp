<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<%-- 
    ========================================================================
    파일명 예시 : WORegInq.jsp
    화면명     : 작업지시 등록 / 조회
    용도       : 작업지시 등록, 검색, 목록 조회, 삭제
    비고       : 
      1) 기존 ProdPlanRegInq.jsp 구조를 최대한 비슷하게 맞춤
      2) CSS 클래스명도 ta 계열을 유지해서 기존 스타일 재사용 가능하게 작성
      3) 실제 DTO 필드명 / Controller URL / 검색조건명은 프로젝트에 맞게 조정 필요
    ========================================================================
--%>

<div class="workorder-page">

    <%-- 
        =========================
        1. 상단 액션 버튼 영역
        =========================
        - 등록 버튼 : 모달 창 오픈
        - 삭제 버튼 : 현재 선택된 작업지시 1건 삭제
        - 삭제는 체크박스 대신 "행 클릭 선택" 방식으로 처리
    --%>
    <div class="taPageActions">
        <%-- 등록 모달 열기 버튼 --%>
        <button type="button" class="taBtn taBtnPrimary" onclick="openWorkOrderModal()">
            등록
        </button>

        <%-- 
            삭제 버튼
            form="deleteForm" 으로 아래 삭제 전용 form 제출
            onsubmit 이전에 선택된 행이 있는지 JS로 검사
        --%>
        <button
            type="submit"
            form="deleteForm"
            class="taBtn taBtnOutline"
            onclick="return validateDeleteSelection();">
            삭제
        </button>
    </div>

    <%-- 
        =========================
        2. 작업지시 등록 모달
        =========================
        - 신규 작업지시 등록용
        - 실제 운영에서는 품목코드/품목명/BOM/작업자/공정 등을
          select 박스 또는 팝업 조회로 바꾸는 경우가 많음
        - 지금은 바로 붙여 넣어 쓸 수 있도록 기본 입력형으로 작성
    --%>
<!--     <div id="workOrderModal" class="modal-overlay" style="display:none;"> -->
<!--         <div class="modal-box"> -->

<%--             모달 헤더 --%>
<!--             <div class="modal-header"> -->
<!--                 <div class="modal-title">작업지시 등록</div> -->

<%--                 X 버튼 클릭 시 모달 닫기 --%>
<!--                 <span class="modal-close" onclick="closeWorkOrderModal()">&times;</span> -->
<!--             </div> -->

<%--             모달 바디 --%>
<!--             <div class="modal-body"> -->

<%--                 
<%--                     등록 form --%>
<%--                     actionType 값을 hidden으로 넘겨서 --%>
<%--                     Controller에서 insert / delete 분기 처리 가능하게 구성 --%>
<%--                 --%> --%>
<!--                 <form -->
<!--                     method="post" -->
<%--                     action="${pageContext.request.contextPath}/workorder" --%>
<!--                     onsubmit="return validateWorkOrderForm();"> -->

<%--                     Controller 분기용 hidden 값 --%>
<!--                     <input type="hidden" name="actionType" value="insert"> -->

<%--                     
<%--                         작업지시번호 --%>
<%--                         예: WO-20260402-001 --%>
<%--                         보통은 서버에서 자동채번하는 경우가 많지만, --%>
<%--                         현재는 수기 입력 가능하게 구성 --%>
<%--                     --%> --%>
<!--                     <div class="modal-form-group"> -->
<!--                         <label for="workOrderNo">작업지시번호</label> -->
<!--                         <input -->
<!--                             type="text" -->
<!--                             id="workOrderNo" -->
<!--                             name="workOrderNo" -->
<!--                             placeholder="예: WO-20260402-001"> -->
<!--                     </div> -->

<%--                     작업일자 --%>
<!--                     <div class="modal-form-group"> -->
<!--                         <label for="workDate">일자</label> -->
<!--                         <input -->
<!--                             type="date" -->
<!--                             id="workDate" -->
<!--                             name="workDate"> -->
<!--                     </div> -->

<%--                     품목코드 --%>
<!--                     <div class="modal-form-group"> -->
<!--                         <label for="itemCode">품목코드</label> -->
<!--                         <input -->
<!--                             type="text" -->
<!--                             id="itemCode" -->
<!--                             name="itemCode" -->
<!--                             placeholder="예: FG-CAM-001"> -->
<!--                     </div> -->

<%--                     품목명 --%>
<!--                     <div class="modal-form-group"> -->
<!--                         <label for="itemName">품목명</label> -->
<!--                         <input -->
<!--                             type="text" -->
<!--                             id="itemName" -->
<!--                             name="itemName" -->
<!--                             placeholder="예: LIMOH(무수수산화리튬)"> -->
<!--                     </div> -->

<%--                     지시량(생산량) --%>
<!--                     <div class="modal-form-group"> -->
<!--                         <label for="workQty">지시량(생산량)</label> -->
<!--                         <input -->
<!--                             type="number" -->
<!--                             id="workQty" -->
<!--                             name="workQty" -->
<!--                             min="1" -->
<!--                             placeholder="예: 5000"> -->
<!--                     </div> -->

<%--                     단위 --%>
<!--                     <div class="modal-form-group"> -->
<!--                         <label for="unitName">단위</label> -->
<!--                         <select id="unitName" name="unitName"> -->
<!--                             <option value="">선택하세요</option> -->
<!--                             <option value="kg">kg</option> -->
<!--                             <option value="ton">ton</option> -->
<!--                             <option value="ea">ea</option> -->
<!--                         </select> -->
<!--                     </div> -->

<%--                     라인 --%>
<!--                     <div class="modal-form-group"> -->
<!--                         <label for="lineName">라인</label> -->
<!--                         <input -->
<!--                             type="text" -->
<!--                             id="lineName" -->
<!--                             name="lineName" -->
<!--                             placeholder="예: LN-A"> -->
<!--                     </div> -->

<%--                     공정 --%>
<!--                     <div class="modal-form-group"> -->
<!--                         <label for="processName">공정</label> -->
<!--                         <input -->
<!--                             type="text" -->
<!--                             id="processName" -->
<!--                             name="processName" -->
<!--                             placeholder="예: PC-TRS"> -->
<!--                     </div> -->

<%--                     작업자 --%>
<!--                     <div class="modal-form-group"> -->
<!--                         <label for="empName">작업자</label> -->
<!--                         <input -->
<!--                             type="text" -->
<!--                             id="empName" -->
<!--                             name="empName" -->
<!--                             placeholder="예: 박민호"> -->
<!--                     </div> -->

<%--                     BOM --%>
<!--                     <div class="modal-form-group"> -->
<!--                         <label for="bomName">BOM</label> -->
<!--                         <input -->
<!--                             type="text" -->
<!--                             id="bomName" -->
<!--                             name="bomName" -->
<!--                             placeholder="예: BOM-혼합분쇄코드"> -->
<!--                     </div> -->

<%--                     비고는 길게 입력할 수 있도록 textarea 사용 --%>
<!--                     <div class="modal-form-group" style="grid-column: 1 / -1;"> -->
<!--                         <label for="remark">비고</label> -->
<!--                         <textarea -->
<!--                             id="remark" -->
<!--                             name="remark" -->
<!--                             placeholder="비고를 입력하세요"></textarea> -->
<!--                     </div> -->

<%--                     모달 하단 버튼 --%>
<!--                     <div class="modal-footer"> -->
<%--                         취소: 모달 닫기만 수행 --%>
<!--                         <button type="button" class="btn-secondary" onclick="closeWorkOrderModal()"> -->
<!--                             취소 -->
<!--                         </button> -->

<%--                         저장: form submit --%>
<!--                         <button type="submit" class="btn-primary"> -->
<!--                             저장 -->
<!--                         </button> -->
<!--                     </div> -->
<!--                 </form> -->
<!--             </div> -->
<!--         </div> -->
<!--     </div> -->

    <%-- 
        =========================
        3. 검색 영역
        =========================
        - 기간 검색 + 조건 검색
        - GET 방식으로 조회 조건 전달
        - param 값을 그대로 다시 출력해서 검색값 유지
    --%>
    <form method="get" action="${pageContext.request.contextPath}/workorder">
        <div class="taToolbarRow">

            <%-- 시작일 ~ 종료일 검색 --%>
            <div class="taToolbarField">
                <div class="taSearchBox">
                    <input
                        type="date"
                        class="taSearchInput"
                        name="startDate"
                        value="${param.startDate}">

                    <input
                        type="date"
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
                    <option value="lineName" ${param.searchType eq 'lineName' ? "selected" : ""}>라인</option>
                    <option value="processName" ${param.searchType eq 'processName' ? "selected" : ""}>공정</option>
                    <option value="empName" ${param.searchType eq 'empName' ? "selected" : ""}>작업자</option>
                </select>
            </div>

            <%-- 키워드 검색 입력창 --%>
            <div class="taToolbarField taToolbarFieldGrow">
                <div class="taSearchBox">
                    <input
                        type="text"
                        class="taSearchInput"
                        name="keyword"
                        placeholder="작업지시번호 / 품목코드 / 품목명 / 라인 / 공정 / 작업자 검색"
                        value="${param.keyword}">
                    <button type="submit" class="taSearchBtn" aria-label="검색">⌕</button>
                </div>
            </div>
        </div>
    </form>

    <%-- 
        =========================
        4. 삭제용 form + 목록 테이블
        =========================
        - 행 클릭 시 hidden input 에 선택된 작업지시번호 저장
        - 삭제 버튼은 이 form 을 submit
        - 체크박스 컬럼을 따로 두지 않아도 되도록 구성
    --%>
    <form
        id="deleteForm"
        method="post"
        action="${pageContext.request.contextPath}/workorder">

        <%-- Controller 분기용 hidden 값 --%>
        <input type="hidden" name="actionType" value="delete">

        <%-- 실제 삭제 대상 작업지시번호 저장용 hidden input --%>
        <input type="hidden" id="selectedWorkOrderNo" name="selectedWorkOrderNo" value="">

        <div class="taTableShell workorder-table-shell">
            <div class="taTableScroll">
                <table class="taMesTable">

                    <%-- 
                        테이블 헤더
                        사용자가 보여준 화면 기준 컬럼 순서 그대로 반영
                    --%>
                    <thead>
                        <tr>
                            <th class="taTableHeadCell taColFit">NO</th>
                            <th class="taTableHeadCell taColFit">작업지시번호</th>
                            <th class="taTableHeadCell taColDate">일자</th>
                            <th class="taTableHeadCell taColFit">품목코드</th>
                            <th class="taTableHeadCell taColGrow">품목명</th>
                            <th class="taTableHeadCell taColFit">지시량(생산량)</th>
                            <th class="taTableHeadCell taColFit">단위</th>
                            <th class="taTableHeadCell taColFit">라인</th>
                            <th class="taTableHeadCell taColFit">공정</th>
                            <th class="taTableHeadCell taColFit">작업자</th>
                            <th class="taTableHeadCell taColFit">BOM</th>
                            <th class="taTableHeadCell taColGrow taLastCol">비고</th>
                        </tr>
                    </thead>

                    <tbody>
                        <%-- 
                            list 는 Controller에서 request 영역에 담아 보낸다고 가정
                            예: request.setAttribute("list", list);
                        --%>
                        <c:forEach var="dto" items="${list}">
                            <%-- 
                                행 클릭 시 선택 처리
                                data-key 에 작업지시번호 보관
                                삭제 시 이 값을 hidden input 에 세팅
                            --%>
                            <tr
                                class="taTableBodyRow"
                                data-key="${dto.workOrderNo}"
                                onclick="selectWorkOrderRow(this, '${dto.workOrderNo}')">

                                <%-- 화면 표시용 순번 --%>
                                <td class="taTableBodyCell taColFit">${dto.seqNO}</td>

                                <%-- 작업지시번호 --%>
                                <td class="taTableBodyCell taColFit">${dto.workOrderNo}</td>

                                <%-- 
                                    일자
                                    dto.workDate 가 java.util.Date / java.sql.Date 라면 fmt 사용 가능
                                    문자열이면 그냥 ${dto.workDate} 로 바꿔도 됨
                                --%>
                                <td class="taTableBodyCell taColDate">
                                    <fmt:formatDate value="${dto.workDate}" pattern="yyyy-MM-dd"/>
                                </td>

                                <%-- 품목코드 --%>
                                <td class="taTableBodyCell taColFit">${dto.itemCode}</td>

                                <%-- 품목명 --%>
                                <td class="taTableBodyCell taColGrow">${dto.itemName}</td>

                                <%-- 지시량(생산량) --%>
                                <td class="taTableBodyCell taColFit">${dto.workQty}</td>

                                <%-- 단위 --%>
                                <td class="taTableBodyCell taColFit">${dto.unitName}</td>

                                <%-- 라인 --%>
                                <td class="taTableBodyCell taColFit">${dto.lineName}</td>

                                <%-- 공정 --%>
                                <td class="taTableBodyCell taColFit">${dto.processName}</td>

                                <%-- 작업자 --%>
                                <td class="taTableBodyCell taColFit">${dto.empName}</td>

                                <%-- BOM --%>
                                <td class="taTableBodyCell taColFit">${dto.bomName}</td>

                                <%-- 비고 --%>
                                <td class="taTableBodyCell taColGrow taLastCol">${dto.remark}</td>
                            </tr>
                        </c:forEach>

                        <%-- 
                            조회 결과가 없을 때 안내 문구 출력
                            colspan 은 전체 컬럼 개수 12개와 맞춰야 함
                        --%>
                        <c:if test="${empty list}">
                            <tr class="taTableBodyRow">
                                <td class="taTableBodyCell taLastCol" colspan="12" style="text-align:center;">
                                    조회된 작업지시 데이터가 없습니다.
                                </td>
                            </tr>
                        </c:if>
                    </tbody>
                </table>
            </div>
        </div>
    </form>

    <%-- 
        =========================
        5. 페이징 영역
        =========================
        - 검색조건 유지하면서 페이지 이동
        - startPage / endPage / totalPage / page 는 Controller에서 세팅 필요
    --%>
    <div class="taPagination">

        <%-- 이전 페이지 블록 이동 --%>
        <c:if test="${startPage > 1}">
            <a
                class="taPageBtn"
                href="${pageContext.request.contextPath}/workorder?page=${startPage - 1}&startDate=${param.startDate}&endDate=${param.endDate}&searchType=${param.searchType}&keyword=${param.keyword}">
                이전
            </a>
        </c:if>

        <%-- 페이지 번호 반복 출력 --%>
        <c:forEach var="i" begin="${startPage}" end="${endPage}">
            <a
                class="${page eq i ? 'taPageBtn active' : 'taPageBtn'}"
                href="${pageContext.request.contextPath}/workorder?page=${i}&startDate=${param.startDate}&endDate=${param.endDate}&searchType=${param.searchType}&keyword=${param.keyword}">
                ${i}
            </a>
        </c:forEach>

        <%-- 다음 페이지 블록 이동 --%>
        <c:if test="${endPage < totalPage}">
            <a
                class="taPageBtn"
                href="${pageContext.request.contextPath}/workorder?page=${endPage + 1}&startDate=${param.startDate}&endDate=${param.endDate}&searchType=${param.searchType}&keyword=${param.keyword}">
                다음
            </a>
        </c:if>
    </div>
</div>

<script>
    /*
        ====================================================================
        6. 모달 제어 함수
        ====================================================================
        - openWorkOrderModal  : 등록 모달 열기
        - closeWorkOrderModal : 등록 모달 닫기
        ====================================================================
    */

//     // 등록 버튼 클릭 시 모달 표시
//     function openWorkOrderModal() {
//         document.getElementById('workOrderModal').style.display = 'flex';
//     }

//     // 닫기 버튼 또는 바깥 영역 클릭 시 모달 숨김
//     function closeWorkOrderModal() {
//         document.getElementById('workOrderModal').style.display = 'none';
//     }

    /*
        ====================================================================
        7. 행 선택 함수
        ====================================================================
        - 테이블에서 사용자가 클릭한 행을 시각적으로 선택 처리
        - 선택된 작업지시번호를 hidden input 에 저장
        - 삭제 버튼 클릭 시 이 값을 서버로 전송
        ====================================================================
    */
    function selectWorkOrderRow(clickedRow, workOrderNo) {
        // 현재 화면의 모든 행을 가져온다.
        var rows = document.querySelectorAll('.taTableBodyRow');

        // 기존에 선택된 스타일이 있다면 전부 제거
        rows.forEach(function(row) {
            row.classList.remove('selected-row');
        });

        // 클릭한 행에만 선택 스타일 부여
        clickedRow.classList.add('selected-row');

        // 삭제용 hidden input 에 작업지시번호 저장
        document.getElementById('selectedWorkOrderNo').value = workOrderNo;
    }

    /*
        ====================================================================
        8. 삭제 전 검증
        ====================================================================
        - 아무 행도 선택하지 않았는데 삭제 버튼을 누르는 경우 차단
        - 선택되어 있으면 confirm 창으로 한 번 더 확인
        ====================================================================
    */
    function validateDeleteSelection() {
        var selectedWorkOrderNo = document.getElementById('selectedWorkOrderNo').value;

        // 선택된 값이 없으면 삭제 막기
        if (!selectedWorkOrderNo || selectedWorkOrderNo.trim() === '') {
            alert('삭제할 작업지시를 목록에서 먼저 선택하세요.');
            return false;
        }

        // 사용자 최종 확인
        return confirm('선택한 작업지시를 삭제하시겠습니까?');
    }

    /*
        ====================================================================
        9. 등록 form 검증
        ====================================================================
        - 필수값 누락 시 submit 막기
        - 공백만 입력한 값도 막기
        - 가장 먼저 잘못된 입력칸으로 focus 이동
        ====================================================================
    */
//     function validateWorkOrderForm() {
//         // 검사용 input/select 요소 가져오기
//         var workOrderNo = document.getElementById('workOrderNo');
//         var workDate    = document.getElementById('workDate');
//         var itemCode    = document.getElementById('itemCode');
//         var itemName    = document.getElementById('itemName');
//         var workQty     = document.getElementById('workQty');
//         var unitName    = document.getElementById('unitName');
//         var lineName    = document.getElementById('lineName');
//         var processName = document.getElementById('processName');
//         var empName     = document.getElementById('empName');
//         var bomName     = document.getElementById('bomName');

//         // 문자열 입력값 공백 제거용 함수
//         function isBlank(value) {
//             return !value || value.trim() === '';
//         }

//         if (isBlank(workOrderNo.value)) {
//             alert('작업지시번호를 입력하세요.');
//             workOrderNo.focus();
//             return false;
//         }

//         if (isBlank(workDate.value)) {
//             alert('일자를 선택하세요.');
//             workDate.focus();
//             return false;
//         }

//         if (isBlank(itemCode.value)) {
//             alert('품목코드를 입력하세요.');
//             itemCode.focus();
//             return false;
//         }

//         if (isBlank(itemName.value)) {
//             alert('품목명을 입력하세요.');
//             itemName.focus();
//             return false;
//         }

//         if (isBlank(workQty.value) || Number(workQty.value) <= 0) {
//             alert('지시량(생산량)은 1 이상으로 입력하세요.');
//             workQty.focus();
//             return false;
//         }

//         if (isBlank(unitName.value)) {
//             alert('단위를 선택하세요.');
//             unitName.focus();
//             return false;
//         }

//         if (isBlank(lineName.value)) {
//             alert('라인을 입력하세요.');
//             lineName.focus();
//             return false;
//         }

//         if (isBlank(processName.value)) {
//             alert('공정을 입력하세요.');
//             processName.focus();
//             return false;
//         }

//         if (isBlank(empName.value)) {
//             alert('작업자를 입력하세요.');
//             empName.focus();
//             return false;
//         }

//         if (isBlank(bomName.value)) {
//             alert('BOM을 입력하세요.');
//             bomName.focus();
//             return false;
//         }

//         // 모든 검증 통과 시 submit 허용
//         return true;
//     }

//     /*
//         ====================================================================
//         10. 모달 바깥 클릭 시 닫기
//         ====================================================================
//         - overlay 영역 자체를 클릭했을 때만 닫히게 처리
//         - modal-box 내부 클릭은 닫히지 않음
//         ====================================================================
//     */
//     window.addEventListener('click', function(event) {
//         var modal = document.getElementById('workOrderModal');

//         if (event.target === modal) {
//             closeWorkOrderModal();
//         }
//     });
</script>
