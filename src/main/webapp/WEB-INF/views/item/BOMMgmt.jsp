<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

        <!DOCTYPE html>
        <html lang="ko">

        <head>
            <meta charset="UTF-8">
            <title>BOM 관리</title>

            <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/common.css" />
            <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/table.css" />
            <script src="${pageContext.request.contextPath}/assets/js/layout.js"></script>
        </head>

        <body>
            <div class="app">
                <jsp:include page="/WEB-INF/views/common/sidebar.jsp" />

                <div class="main">
                    <jsp:include page="/WEB-INF/views/common/topbar.jsp" />
                    <div class="taPageActions">
                        <button type="button" class="taBtn taBtnPrimary" onclick="openModal()">등록</button>
                        <button type="button" class="taBtn taBtnOutline">삭제</button>
                    </div>


                    <form method="get" action="${pageContext.request.contextPath}/BOM-mgmt">
                        <div class="taToolbarRow">
                            <div class="taToolbarField">
                                <div class="taSearchBox">
                                    <input type="date" class="taSearchInput" name="startDate">
                                    <input type="date" class="taSearchInput" name="endDate">
                                </div>
                            </div>

                            <div class="taToolbarField">
                                <select class="taSelect" name="searchField">
                                    <option value="">전체</option>
                                    <option value="item_code">품목 코드</option>
                                    <option value="item_name">품목명</option>
                                </select>
                            </div>

                            <div class="taToolbarField taToolbarFieldGrow">
                                <div class="taSearchBox">
                                    <input type="text" class="taSearchInput" name="keyword" placeholder="검색">
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
                                        <th class="taTableHeadCell">NO</th>
                                        <th class="taTableHeadCell">품목 코드</th>
                                        <th class="taTableHeadCell">품목명</th>
                                        <th class="taTableHeadCell">비고</th>
                                        <th class="taTableHeadCell">등록일</th>
                                    </tr>
                                </thead>

                                <tbody>
                                    <c:forEach var="BOM" items="${BOMList}">
                                        <tr class="taTableBodyRow">
                                            <td class="taTableBodyCell">${BOM.BOM_id}</td>
                                            <td class="taTableBodyCell">${BOM.item_code}</td>
                                            <td class="taTableBodyCell">${BOM.item_name}</td>
                                            <td class="taTableBodyCell">${BOM.remark}</td>
                                            <td class="taTableBodyCell">${BOM.created_at}</td>
                                        </tr>
                                    </c:forEach>

                                    <c:if test="${empty BOMList}">
                                        <tr>
                                            <td colspan="10" style="text-align:center;">
                                                조회된 데이터가 없습니다.
                                            </td>
                                        </tr>
                                    </c:if>
                                </tbody>

                            </table>
                        </div>
                    </div>


                    <div class="modal">
                        <div class="modal-box">
                            <div class="modal-title">품목 등록</div>

                            <form method="post" action="${pageContext.request.contextPath}/BOM-mgmt" class="form-row">
                                <div>1. 완제품 내역</div>
                                <input type="text" name="item_code" placeholder="제품 코드">
                                <input type="text" name="item_name" placeholder="품목명">                                
                                <input type="text" name="spec" placeholder="규격">

                                <div>2. 재료 내역</div>
                                                                
                                <input type="text" name="" placeholder="원자재코드">
                                <input type="number" name="" placeholder="원자재명">
                                <input type="text" name="unit" placeholder="단위">
                                <input type="text" name="" placeholder="소요량">
                                <input type="text" name="remark" placeholder="비고">

                                <div style="display:flex; gap:10px; margin-top:10px;">
                                    <button type="submit" class="taBtn taBtnPrimary">등록</button>
                                    <button type="button" class="taBtn taBtnOutline" onclick="openModal()">취소</button>
                                </div>

                            </form>
                        </div>
                    </div>

                </div>
            </div>
            <script src="${pageContext.request.contextPath}/assets/js/itemMgmt.js"></script>

        </body>

        </html>