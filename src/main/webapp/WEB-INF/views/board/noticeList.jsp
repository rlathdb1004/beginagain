<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<div class="taPageActions">
    <button type="button" class="taBtn taBtnPrimary" data-modal-target="registerModal">등록</button>
    <button type="submit" form="deleteForm" class="taBtn taBtnOutline">선택 삭제</button>
</div>

<form class="taLocalSearchForm" data-table-id="noticeTable">
    <div class="taToolbarRow">
        <div class="taToolbarField">
            <select class="taSelect" name="searchType">
                <option value="all">전체</option>
                <option value="title">제목</option>
                <option value="writerEmpName">작성자</option>
                <option value="status">상태</option>
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
<form id="deleteForm" action="${pageContext.request.contextPath}/notice/delete" method="post">
    <div class="taTableShell">
        <div class="taTableScroll">
            <table class="taMesTable" id="noticeTable">
                <thead>
                    <tr>
                        <th class="taTableHeadCell taColFit"><input type="checkbox" id="checkAll" class="taCheckInput"></th>
                        <th class="taTableHeadCell taColFit">ID</th>
                        <th class="taTableHeadCell taColGrow">제목</th>
                        <th class="taTableHeadCell taColFit">작성자</th>
                        <th class="taTableHeadCell taColFit">상태</th>
                        <th class="taTableHeadCell taColFit">조회수</th>
                        <th class="taTableHeadCell taColAction taLastCol">상세</th>
                    </tr>
                </thead>
                <tbody>
                    <c:choose>
                        <c:when test="${not empty noticeList}">
                            <c:forEach var="n" items="${noticeList}">
                                <tr class="taTableBodyRow">
                                    <td class="taTableBodyCell taColFit"><input type="checkbox" name="noticeId" value="${n.noticeId}" class="taCheckInput"></td>
                                    <td class="taTableBodyCell taColFit">${n.noticeId}</td>
                                    <td class="taTableBodyCell taColGrow" data-search-key="title">${n.title}</td>
                                    <td class="taTableBodyCell taColFit" data-search-key="writerEmpName">${n.writerEmpName}</td>
                                    <td class="taTableBodyCell taColFit" data-search-key="status">${n.status}</td>
                                    <td class="taTableBodyCell taColFit">${n.viewCount}</td>
                                    <td class="taTableBodyCell taColAction taLastCol">
                                        <a class="taLinkAnchor" href="${pageContext.request.contextPath}/notice/detail?noticeId=${n.noticeId}">상세보기</a>
                                    </td>
                                </tr>
                            </c:forEach>
                        </c:when>
                        <c:otherwise>
                            <tr class="taTableBodyRow">
                                <td class="taTableBodyCell taLastCol" colspan="7" style="text-align:center;">조회된 공지사항이 없습니다.</td>
                            </tr>
                        </c:otherwise>
                    </c:choose>
                </tbody>
            </table>
        </div>
    </div>
</form>

<div class="taModal" id="registerModal" hidden aria-hidden="true">
    <div class="taModalDialog">
        <div class="taModalHeader">
            <h3 class="taModalTitle">공지사항 등록</h3>
            <button type="button" class="taModalClose">&times;</button>
        </div>
        <form action="${pageContext.request.contextPath}/notice/register" method="post">
            <div class="taModalBody taModalGrid">
                <input type="hidden" name="writerEmpId" value="${loginUser.empId}">
                <div class="form-row full">
                    <label>제목</label>
                    <input type="text" name="title" required>
                </div>
                <div class="form-row full">
                    <label>내용</label>
                    <textarea name="content" required></textarea>
                </div>
                <div class="form-row">
                    <label>상태</label>
                    <input type="text" name="status" value="게시">
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
