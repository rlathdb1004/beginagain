<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<c:choose>
  <c:when test="${empty item}">
    <div class="taFormShell taEmptyState">
      <p>조회된 품목 정보가 없습니다.</p>
      <div class="taPageActions" style="justify-content:center; margin-top:16px;">
        <a class="taBtn taBtnOutline" href="${pageContext.request.contextPath}/item/list" style="text-decoration:none;">목록</a>
      </div>
    </div>
  </c:when>
  <c:otherwise>
    <form action="${pageContext.request.contextPath}/item/update" method="post">
      <input type="hidden" name="itemId" value="${item.itemId}">
      <div class="taPageActions">
        <button type="submit" class="taBtn taBtnPrimary">수정</button>
        <a class="taBtn taBtnOutline" href="${pageContext.request.contextPath}/item/list" style="text-decoration:none;">목록</a>
      </div>
      <div class="taFormShell">
        <table class="taFormTable">
          <tr><th class="taFormLabel">품목번호</th><td class="taFormValue"><span class="taReadonlyText">${item.itemId}</span></td></tr>
          <tr><th class="taFormLabel">품목코드</th><td class="taFormValue"><span class="taReadonlyText">${item.itemCode}</span></td></tr>
          <tr><th class="taFormLabel">품목명</th><td class="taFormValue"><input class="taFormInput" type="text" name="itemName" value="${item.itemName}"></td></tr>
          <tr><th class="taFormLabel">품목유형</th><td class="taFormValue"><input class="taFormInput" type="text" name="itemType" value="${item.itemType}"></td></tr>
          <tr><th class="taFormLabel">단위</th><td class="taFormValue"><input class="taFormInput" type="text" name="unit" value="${item.unit}"></td></tr>
          <tr><th class="taFormLabel">규격</th><td class="taFormValue"><input class="taFormInput" type="text" name="spec" value="${item.spec}"></td></tr>
          <tr><th class="taFormLabel">공급처</th><td class="taFormValue"><input class="taFormInput" type="text" name="supplierName" value="${item.supplierName}"></td></tr>
          <tr><th class="taFormLabel">안전재고</th><td class="taFormValue"><input class="taFormInput" type="number" step="0.001" name="safetyStock" value="${item.safetyStock}"></td></tr>
          <tr><th class="taFormLabel">비고</th><td class="taFormValue"><textarea class="taFormTextarea" name="remark">${item.remark}</textarea></td></tr>
        </table>
      </div>
    </form>
  </c:otherwise>
</c:choose>
