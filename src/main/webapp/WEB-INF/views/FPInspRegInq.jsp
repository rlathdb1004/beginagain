<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<div class="taPageActions">
  <button type="button" class="taBtn taBtnPrimary">등록</button>
  <button type="button" class="taBtn taBtnOutline">삭제</button>
</div>

<form method="post" action="${pageContext.request.contextPath}/fpInspRegInq">
  <input type="hidden" name="cmd" value="list">

  <div class="taToolbarRow">

    <!-- 1) 판정구분 -->
    <div class="taToolbarField">
      <select class="taSelect" name="resultType">
        <option value="전체"
          <c:if test="${empty fpInspRegInqSearchDTO.resultType or fpInspRegInqSearchDTO.resultType eq '전체'}">selected</c:if>>
          전체
        </option>
        <option value="합격"
          <c:if test="${fpInspRegInqSearchDTO.resultType eq '합격'}">selected</c:if>>
          합격
        </option>
        <option value="불합격"
          <c:if test="${fpInspRegInqSearchDTO.resultType eq '불합격'}">selected</c:if>>
          불합격
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
          value="${fpInspRegInqSearchDTO.startDate}">

        <input
          type="date"
          class="taSearchInput"
          name="endDate"
          value="${fpInspRegInqSearchDTO.endDate}">
      </div>
    </div>

    <!-- 3) 조회기준 -->
    <div class="taToolbarField">
      <select class="taSelect" name="searchType">
        <option value=""
          <c:if test="${empty fpInspRegInqSearchDTO.searchType}">selected</c:if>>
          전체
        </option>
        <option value="itemCode"
          <c:if test="${fpInspRegInqSearchDTO.searchType eq 'itemCode'}">selected</c:if>>
          품목코드
        </option>
        <option value="itemName"
          <c:if test="${fpInspRegInqSearchDTO.searchType eq 'itemName'}">selected</c:if>>
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
          value="${fpInspRegInqSearchDTO.keyword}">

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
          <th class="taTableHeadCell taColFit">검사수량</th>
          <th class="taTableHeadCell taColFit">판정</th>
          <th class="taTableHeadCell taColDate">검사일</th>
          <th class="taTableHeadCell taColAction taLastCol">상세보기</th>
        </tr>
      </thead>

      <tbody>
        <c:forEach var="dto" items="${fpInspRegInqList}">
          <tr class="taTableBodyRow">
            <td class="taTableBodyCell taColFit">${dto.finalInspectionId}</td>
            <td class="taTableBodyCell taColFit">${dto.itemCode}</td>
            <td class="taTableBodyCell taColGrow">${dto.itemName}</td>
            <td class="taTableBodyCell taColGrow">${dto.lotNo}</td>
            <td class="taTableBodyCell taColFit">${dto.inspectQty}</td>
            <td class="taTableBodyCell taColFit">${dto.result}</td>
            <td class="taTableBodyCell taColDate">${dto.inspectionDate}</td>
            <td class="taTableBodyCell taColAction taLastCol">
              <form method="post"
                    action="${pageContext.request.contextPath}/fpInspRegInq"
                    style="margin:0;">
                <input type="hidden" name="cmd" value="detail">
                <input type="hidden" name="finalInspectionId" value="${dto.finalInspectionId}">
                <button type="submit" class="taLinkButton">상세보기</button>
              </form>
            </td>
          </tr>
        </c:forEach>

        <c:if test="${empty fpInspRegInqList}">
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

<c:if test="${not empty fpInspRegInqDTO}">
  <div class="taTableShell" style="padding:20px; margin-top:20px;">
    <h3 style="margin-top:0;">상세조회 결과</h3>

    <p><strong>완제품검사번호 :</strong> ${fpInspRegInqDTO.finalInspectionId}</p>
    <p><strong>생산실적번호 :</strong> ${fpInspRegInqDTO.resultId}</p>
    <p><strong>작업지시번호 :</strong> ${fpInspRegInqDTO.workOrderId}</p>
    <p><strong>품목코드 :</strong> ${fpInspRegInqDTO.itemCode}</p>
    <p><strong>품목명 :</strong> ${fpInspRegInqDTO.itemName}</p>
    <p><strong>LOT번호 :</strong> ${fpInspRegInqDTO.lotNo}</p>
    <p><strong>검사수량 :</strong> ${fpInspRegInqDTO.inspectQty}</p>
    <p><strong>판정 :</strong> ${fpInspRegInqDTO.result}</p>
    <p><strong>검사일 :</strong> ${fpInspRegInqDTO.inspectionDate}</p>
    <p><strong>비고 :</strong> ${fpInspRegInqDTO.remark}</p>
    <p><strong>생성일 :</strong> ${fpInspRegInqDTO.createdAt}</p>
    <p><strong>수정일 :</strong> ${fpInspRegInqDTO.updatedAt}</p>
  </div>
</c:if>