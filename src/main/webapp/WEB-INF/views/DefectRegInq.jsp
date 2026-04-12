<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<div class="taPageActions">
  <button type="button" class="taBtn taBtnPrimary">등록</button>
  <button type="button" class="taBtn taBtnOutline">삭제</button>
</div>

<form method="post" action="${pageContext.request.contextPath}/defectRegInq">
  <input type="hidden" name="cmd" value="list">

  <div class="taToolbarRow">

    <!-- 1) 불량유형 -->
    <div class="taToolbarField">
      <select class="taSelect" name="defectTypeSearch">
        <option value="전체"
          <c:if test="${empty defectRegInqSearchDTO.defectTypeSearch or defectRegInqSearchDTO.defectTypeSearch eq '전체'}">selected</c:if>>
          전체
        </option>
        <option value="원자재"
          <c:if test="${defectRegInqSearchDTO.defectTypeSearch eq '원자재'}">selected</c:if>>
          원자재
        </option>
        <option value="완제품"
          <c:if test="${defectRegInqSearchDTO.defectTypeSearch eq '완제품'}">selected</c:if>>
          완제품
        </option>
        <option value="포장"
          <c:if test="${defectRegInqSearchDTO.defectTypeSearch eq '포장'}">selected</c:if>>
          포장
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
          value="${defectRegInqSearchDTO.startDate}">

        <input
          type="date"
          class="taSearchInput"
          name="endDate"
          value="${defectRegInqSearchDTO.endDate}">
      </div>
    </div>

    <!-- 3) 조회기준 -->
    <div class="taToolbarField">
      <select class="taSelect" name="searchType">
        <option value=""
          <c:if test="${empty defectRegInqSearchDTO.searchType}">selected</c:if>>
          전체
        </option>
        <option value="itemCode"
          <c:if test="${defectRegInqSearchDTO.searchType eq 'itemCode'}">selected</c:if>>
          품목코드
        </option>
        <option value="itemName"
          <c:if test="${defectRegInqSearchDTO.searchType eq 'itemName'}">selected</c:if>>
          품목명
        </option>
        <option value="defectCode"
          <c:if test="${defectRegInqSearchDTO.searchType eq 'defectCode'}">selected</c:if>>
          불량코드
        </option>
        <option value="defectName"
          <c:if test="${defectRegInqSearchDTO.searchType eq 'defectName'}">selected</c:if>>
          불량명
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
          value="${defectRegInqSearchDTO.keyword}">

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
          <th class="taTableHeadCell taColGrow">LOT번호</th>
          <th class="taTableHeadCell taColFit">불량수량</th>
          <th class="taTableHeadCell taColFit">불량코드</th>
          <th class="taTableHeadCell taColGrow">불량명</th>
          <th class="taTableHeadCell taColFit">불량유형</th>
          <th class="taTableHeadCell taColAction taLastCol">상세보기</th>
        </tr>
      </thead>

      <tbody>
        <c:forEach var="dto" items="${defectRegInqList}">
          <tr class="taTableBodyRow">
            <td class="taTableBodyCell taColFit">${dto.defectProductId}</td>
            <td class="taTableBodyCell taColFit">${dto.itemCode}</td>
            <td class="taTableBodyCell taColGrow">${dto.itemName}</td>
            <td class="taTableBodyCell taColGrow">${dto.lotNo}</td>
            <td class="taTableBodyCell taColFit">${dto.defectQty}</td>
            <td class="taTableBodyCell taColFit">${dto.defectCode}</td>
            <td class="taTableBodyCell taColGrow">${dto.defectName}</td>
            <td class="taTableBodyCell taColFit">${dto.defectType}</td>
            <td class="taTableBodyCell taColAction taLastCol">
              <form method="post"
                    action="${pageContext.request.contextPath}/defectRegInq"
                    style="margin:0;">
                <input type="hidden" name="cmd" value="detail">
                <input type="hidden" name="defectProductId" value="${dto.defectProductId}">
                <button type="submit" class="taLinkButton">상세보기</button>
              </form>
            </td>
          </tr>
        </c:forEach>

        <c:if test="${empty defectRegInqList}">
          <tr class="taTableBodyRow">
            <td class="taTableBodyCell taLastCol" colspan="9" style="text-align:center;">
              조회된 데이터가 없습니다.
            </td>
          </tr>
        </c:if>
      </tbody>

    </table>
  </div>
</div>

<c:if test="${not empty defectRegInqDTO}">
  <div class="taTableShell" style="padding:20px; margin-top:20px;">
    <h3 style="margin-top:0;">상세조회 결과</h3>

    <p><strong>불량등록번호 :</strong> ${defectRegInqDTO.defectProductId}</p>
    <p><strong>완제품검사번호 :</strong> ${defectRegInqDTO.finalInspectionId}</p>
    <p><strong>생산실적번호 :</strong> ${defectRegInqDTO.resultId}</p>
    <p><strong>품목코드 :</strong> ${defectRegInqDTO.itemCode}</p>
    <p><strong>품목명 :</strong> ${defectRegInqDTO.itemName}</p>
    <p><strong>LOT번호 :</strong> ${defectRegInqDTO.lotNo}</p>
    <p><strong>불량수량 :</strong> ${defectRegInqDTO.defectQty}</p>
    <p><strong>불량코드 :</strong> ${defectRegInqDTO.defectCode}</p>
    <p><strong>불량명 :</strong> ${defectRegInqDTO.defectName}</p>
    <p><strong>불량유형 :</strong> ${defectRegInqDTO.defectType}</p>
    <p><strong>비고 :</strong> ${defectRegInqDTO.remark}</p>
    <p><strong>생성일 :</strong> ${defectRegInqDTO.createdAt}</p>
    <p><strong>수정일 :</strong> ${defectRegInqDTO.updatedAt}</p>
  </div>
</c:if>