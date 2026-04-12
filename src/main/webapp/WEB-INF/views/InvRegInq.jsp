<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<div class="taPageActions">
  <button type="button" class="taBtn taBtnPrimary">등록</button>
  <button type="button" class="taBtn taBtnOutline">삭제</button>
</div>

<form method="post" action="${pageContext.request.contextPath}/invRegInq">
  <input type="hidden" name="cmd" value="list">

  <div class="taToolbarRow">

    <!-- 1) 재고조회도 첫 칸은 동일하게 유지 -->
    <div class="taToolbarField">
      <select class="taSelect" name="searchType">
        <option value=""
          <c:if test="${empty invRegInqSearchDTO.searchType}">selected</c:if>>
          전체
        </option>
        <option value="itemCode"
          <c:if test="${invRegInqSearchDTO.searchType eq 'itemCode'}">selected</c:if>>
          품목코드
        </option>
        <option value="itemName"
          <c:if test="${invRegInqSearchDTO.searchType eq 'itemName'}">selected</c:if>>
          품목명
        </option>
      </select>
    </div>

    <!-- 2) 시작일 + 종료일 -->
    <div class="taToolbarField">
      <div class="taSearchBox">
        <input
          type="date"
          class="taSearchInput"
          name="startDate"
          value="${invRegInqSearchDTO.startDate}">

        <input
          type="date"
          class="taSearchInput"
          name="endDate"
          value="${invRegInqSearchDTO.endDate}">
      </div>
    </div>

    <!-- 3) 자재와 동일 위치 유지 -->
    <div class="taToolbarField">
      <select class="taSelect" name="searchType">
        <option value=""
          <c:if test="${empty invRegInqSearchDTO.searchType}">selected</c:if>>
          조회
        </option>
        <option value="itemCode"
          <c:if test="${invRegInqSearchDTO.searchType eq 'itemCode'}">selected</c:if>>
          품목코드
        </option>
        <option value="itemName"
          <c:if test="${invRegInqSearchDTO.searchType eq 'itemName'}">selected</c:if>>
          품목명
        </option>
      </select>
    </div>

    <!-- 4) 검색키워드 + 검색버튼 -->
    <div class="taToolbarField taToolbarFieldGrow">
      <div class="taSearchBox">
        <input
          type="text"
          class="taSearchInput"
          name="keyword"
          placeholder="검색키워드"
          value="${invRegInqSearchDTO.keyword}">

        <button type="submit" class="taSearchBtn" aria-label="검색">⌕</button>
      </div>
    </div>

  </div>
</form>

<div class="taTableShell">
  <div class="taTableScroll">
    <table class="taMesTable">

      <thead>
        <tr>
          <th class="taTableHeadCell taColFit">NO</th>
          <th class="taTableHeadCell taColFit">품목코드</th>
          <th class="taTableHeadCell taColGrow">품목명</th>
          <th class="taTableHeadCell taColFit">현재재고</th>
          <th class="taTableHeadCell taColFit">안전재고</th>
          <th class="taTableHeadCell taColFit">단위</th>
          <th class="taTableHeadCell taColGrow">비고</th>
          <th class="taTableHeadCell taColAction taLastCol">상세보기</th>
        </tr>
      </thead>

      <tbody>
        <c:forEach var="dto" items="${invRegInqList}">
          <tr class="taTableBodyRow">
            <td class="taTableBodyCell taColFit">${dto.inventoryId}</td>
            <td class="taTableBodyCell taColFit">${dto.itemCode}</td>
            <td class="taTableBodyCell taColGrow">${dto.itemName}</td>
            <td class="taTableBodyCell taColFit">${dto.qtyOnHand}</td>
            <td class="taTableBodyCell taColFit">${dto.safetyStock}</td>
            <td class="taTableBodyCell taColFit">${dto.unit}</td>
            <td class="taTableBodyCell taColGrow">${dto.remark}</td>
            <td class="taTableBodyCell taColAction taLastCol">
              <form method="post"
                    action="${pageContext.request.contextPath}/invRegInq"
                    style="margin:0;">
                <input type="hidden" name="cmd" value="detail">
                <input type="hidden" name="inventoryId" value="${dto.inventoryId}">
                <button type="submit" class="taLinkButton">상세보기</button>
              </form>
            </td>
          </tr>
        </c:forEach>

        <c:if test="${empty invRegInqList}">
          <tr class="taTableBodyRow">
            <td class="taTableBodyCell taLastCol" colspan="8" style="text-align:center;">
              조회된 데이터가 없습니다.
            </td>
          </tr>
        </c:if>
      </tbody>

    </table>
  </div>
</div>

<c:if test="${not empty invRegInqDTO}">
  <div class="taTableShell" style="padding:20px; margin-top:20px;">
    <h3 style="margin-top:0;">상세조회 결과</h3>

    <p><strong>재고번호 :</strong> ${invRegInqDTO.inventoryId}</p>
    <p><strong>품목코드 :</strong> ${invRegInqDTO.itemCode}</p>
    <p><strong>품목명 :</strong> ${invRegInqDTO.itemName}</p>
    <p><strong>현재재고 :</strong> ${invRegInqDTO.qtyOnHand}</p>
    <p><strong>안전재고 :</strong> ${invRegInqDTO.safetyStock}</p>
    <p><strong>단위 :</strong> ${invRegInqDTO.unit}</p>
    <p><strong>비고 :</strong> ${invRegInqDTO.remark}</p>
    <p><strong>생성일 :</strong> ${invRegInqDTO.createdAt}</p>
    <p><strong>수정일 :</strong> ${invRegInqDTO.updatedAt}</p>
  </div>
</c:if>