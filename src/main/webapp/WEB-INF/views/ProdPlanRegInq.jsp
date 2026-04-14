<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:if test="${not empty sessionScope.flashMessage}">
    <script>
        alert('${sessionScope.flashMessage}');
    </script>
    <c:remove var="flashMessage" scope="session"/>
</c:if>
<div class="taPageActions">
    <button type="button" class="taOpenModal taBtn taBtnPrimary"
        data-modal-target="prodPlanRegisterModal">등록</button>
    <button type="submit" form="prodPlanDeleteForm"
        class="taBtn taBtnOutline" onclick="return confirm('선택한 생산계획을 삭제하시겠습니까?');">선택 삭제</button>
</div>
<form class="taLocalSearchForm" data-table-id="prodPlanTable">
    <div class="taToolbarRow">
        <div class="taToolbarField">
            <select class="taSelect" name="searchType">
                <option value="all">전체</option>
                <option value="planCode">품목코드</option>
                <option value="planName">품목명</option>
                <option value="planLine">라인</option>
            </select>
        </div>
        <div class="taToolbarField taToolbarFieldGrow" style="grid-column: span 3;">
            <div class="taSearchBox">
                <input type="text" class="taSearchInput" name="keyword" placeholder="검색어를 입력하세요">
                <button type="submit" class="taSearchBtn">⌕</button>
                <button type="button" class="taBtn taBtnOutline taSearchReset">초기화</button>
            </div>
        </div>
    </div>
</form>
<div id="prodPlanRegisterModal" class="taModal" hidden aria-hidden="true">
    <div class="taModalDialog">
        <div class="taModalHeader">
            <h3 class="taModalTitle">생산계획 등록</h3>
            <button type="button" class="taModalClose">×</button>
        </div>
        <form method="post" action="${pageContext.request.contextPath}/prodplan">
            <input type="hidden" name="cmd" value="register">
            <div class="taModalBody taModalGrid">
                <div class="form-row">
                    <label>품목</label>
                    <select name="planCode" required>
                        <option value="">품목 선택</option>
                        <c:forEach var="item" items="${itemOptions}">
                            <option value="${item.planCode}">${item.planName} (${item.planCode})</option>
                        </c:forEach>
                    </select>
                </div>
                <div class="form-row">
                    <label>계획일자</label><input type="date" name="planDate" required>
                </div>
                <div class="form-row">
                    <label>계획수량</label><input type="number" name="planAmount" required>
                </div>
                <div class="form-row">
                    <label>라인</label><input type="text" name="planLine" required>
                </div>
                <div class="form-row full">
                    <label>비고</label>
                    <textarea name="memo"></textarea>
                </div>
            </div>
            <div class="taModalFooter">
                <button type="button" class="taBtn taBtnOutline taModalClose">취소</button>
                <button type="submit" class="taBtn taBtnPrimary">저장</button>
            </div>
        </form>
    </div>
</div>
<form id="prodPlanDeleteForm" method="post" action="${pageContext.request.contextPath}/prodplan">
    <input type="hidden" name="cmd" value="delete">
    <div class="taTableShell">
        <div class="taTableScroll">
            <table class="taMesTable" id="prodPlanTable">
                <thead>
                    <tr>
                        <th class="taTableHeadCell taColFit"><input type="checkbox" id="checkAll" class="taCheckInput"></th>
                        <th class="taTableHeadCell taColFit">NO</th>
                        <th class="taTableHeadCell taColFit">생산계획번호</th>
                        <th class="taTableHeadCell taColDate">일자</th>
                        <th class="taTableHeadCell taColFit">품목코드</th>
                        <th class="taTableHeadCell taColGrow">품목명</th>
                        <th class="taTableHeadCell taColFit">생산계획량</th>
                        <th class="taTableHeadCell taColFit">단위</th>
                        <th class="taTableHeadCell taColFit">라인</th>
                        <th class="taTableHeadCell taColGrow">비고</th>
                        <th class="taTableHeadCell taColFit taLastCol">상세</th>
                    </tr>
                </thead>
                <tbody>
                    <c:choose>
                        <c:when test="${not empty list}">
                            <c:forEach var="dto" items="${list}">
                                <tr class="taTableBodyRow">
                                    <td class="taTableBodyCell taColFit"><input type="checkbox" name="planIds" value="${dto.seqNO}" class="taCheckInput"></td>
                                    <td class="taTableBodyCell taColFit">${dto.seqNO}</td>
                                    <td class="taTableBodyCell taColFit">${dto.planNo}</td>
                                    <td class="taTableBodyCell taColDate">${dto.planDate}</td>
                                    <td class="taTableBodyCell taColFit" data-search-key="planCode">${dto.planCode}</td>
                                    <td class="taTableBodyCell taColGrow" data-search-key="planName">${dto.planName}</td>
                                    <td class="taTableBodyCell taColFit">${dto.planAmount}</td>
                                    <td class="taTableBodyCell taColFit">${dto.planUnit}</td>
                                    <td class="taTableBodyCell taColFit" data-search-key="planLine">${dto.planLine}</td>
                                    <td class="taTableBodyCell taColGrow">${dto.memo}</td>
                                    <td class="taTableBodyCell taColFit taLastCol"><a class="taBtn taBtnOutline" href="${pageContext.request.contextPath}/prodplan?cmd=detail&planId=${dto.seqNO}">상세</a></td>
                                </tr>
                            </c:forEach>
                        </c:when>
                        <c:otherwise>
                            <tr class="taTableBodyRow">
                                <td class="taTableBodyCell taLastCol" colspan="11" style="text-align: center;">조회된 생산계획이 없습니다.</td>
                            </tr>
                        </c:otherwise>
                    </c:choose>
                </tbody>
            </table>
        </div>
    </div>
</form>
