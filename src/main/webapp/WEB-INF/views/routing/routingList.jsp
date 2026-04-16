<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<form method="get" action="${pageContext.request.contextPath}/routing/list" class="taLocalSearchForm">
  <div class="taToolbarRow">
    <div class="taToolbarField taToolbarFieldGrow" style="grid-column: span 2;">
      <label style="display:block; margin-bottom:8px; font-weight:600;">품목 선택</label>
      <select class="taSelect" name="itemId" onchange="this.form.submit()">
        <option value="">품목을 선택하세요</option>
        <c:forEach var="item" items="${itemList}">
          <c:if test="${item.itemType eq '완제품'}">
            <option value="${item.itemId}" ${selectedItemId == item.itemId ? 'selected' : ''}>
              ${item.itemCode} - ${item.itemName}
            </option>
          </c:if>
        </c:forEach>
      </select>
    </div>
  </div>
</form>

<c:if test="${not empty errorMessage}">
  <div style="margin: 12px 0; color: #d9534f; font-weight: 600;">${errorMessage}</div>
</c:if>

<div class="taTableShell">
  <div class="taTableScroll">
    <table class="taMesTable" id="routingTable">
      <thead>
        <tr>
          <th class="taTableHeadCell taColFit">순서</th>
          <th class="taTableHeadCell taColFit">공정코드</th>
          <th class="taTableHeadCell taColGrow">공정명</th>
          <th class="taTableHeadCell taColGrow taLastCol">설비명</th>
        </tr>
      </thead>
      <tbody>
        <c:choose>
          <c:when test="${empty selectedItemId}">
            <tr class="taTableBodyRow">
              <td class="taTableBodyCell taLastCol" colspan="4" style="text-align:center;">품목을 선택하면 라우팅이 조회됩니다.</td>
            </tr>
          </c:when>
          <c:when test="${not empty routingList}">
            <c:forEach var="routing" items="${routingList}">
              <tr class="taTableBodyRow">
                <td class="taTableBodyCell taColFit">${routing.processSeq}</td>
                <td class="taTableBodyCell taColFit">${routing.processCode}</td>
                <td class="taTableBodyCell taColGrow">${routing.processName}</td>
                <td class="taTableBodyCell taColGrow taLastCol">
                  <c:choose>
                    <c:when test="${not empty routing.equipmentName}">${routing.equipmentName}</c:when>
                    <c:otherwise>-</c:otherwise>
                  </c:choose>
                </td>
              </tr>
            </c:forEach>
          </c:when>
          <c:otherwise>
            <tr class="taTableBodyRow">
              <td class="taTableBodyCell taLastCol" colspan="4" style="text-align:center;">해당 품목의 라우팅 데이터가 없습니다.</td>
            </tr>
          </c:otherwise>
        </c:choose>
      </tbody>
    </table>
  </div>
</div>
