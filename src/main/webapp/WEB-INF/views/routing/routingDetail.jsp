<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<c:choose>
  <c:when test="${empty routing}">
    <div class="taFormShell taEmptyState">
      <p>조회된 라우팅 정보가 없습니다.</p>
      <div class="taPageActions" style="justify-content:center; margin-top:16px;">
        <a class="taBtn taBtnOutline" href="${pageContext.request.contextPath}/routing/list" style="text-decoration:none;">목록</a>
      </div>
    </div>
  </c:when>
  <c:otherwise>
    <form action="${pageContext.request.contextPath}/routing/update" method="post">
      <input type="hidden" name="routingId" value="${routing.routingId}">
      <input type="hidden" name="itemId" value="${routing.itemId}">
      <div class="taPageActions">
        <button type="submit" class="taBtn taBtnPrimary">수정</button>
        <a class="taBtn taBtnOutline" href="${pageContext.request.contextPath}/routing/list?itemId=${routing.itemId}" style="text-decoration:none;">목록</a>
      </div>
      <div class="taFormShell">
        <table class="taFormTable">
          <tr>
            <th class="taFormLabel">라우팅번호</th>
            <td class="taFormValue"><span class="taReadonlyText">${routing.routingId}</span></td>
          </tr>
          <tr>
            <th class="taFormLabel">품목</th>
            <td class="taFormValue"><span class="taReadonlyText">${routing.itemCode} - ${routing.itemName}</span></td>
          </tr>
          <tr>
            <th class="taFormLabel">공정</th>
            <td class="taFormValue">
              <select class="taFormInput" name="processId">
                <c:forEach var="process" items="${processList}">
                  <option value="${process.processId}" ${routing.processId == process.processId ? 'selected' : ''}>${process.processCode} - ${process.processName}</option>
                </c:forEach>
              </select>
            </td>
          </tr>
          <tr>
            <th class="taFormLabel">설비</th>
            <td class="taFormValue">
              <select class="taFormInput" name="equipmentId">
                <c:forEach var="equipment" items="${equipmentList}">
                  <option value="${equipment.equipmentId}" ${routing.equipmentId == equipment.equipmentId ? 'selected' : ''}>${equipment.equipmentCode} - ${equipment.equipmentName}</option>
                </c:forEach>
              </select>
            </td>
          </tr>
          <tr>
            <th class="taFormLabel">공정순서</th>
            <td class="taFormValue"><input class="taFormInput" type="number" min="1" name="processSeq" value="${routing.processSeq}"></td>
          </tr>
          <tr>
            <th class="taFormLabel">비고</th>
            <td class="taFormValue"><textarea class="taFormTextarea" name="remark">${routing.remark}</textarea></td>
          </tr>
        </table>
      </div>
    </form>
  </c:otherwise>
</c:choose>
