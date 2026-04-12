<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<div class="taPageActions">
  <button type="button" class="taBtn taBtnPrimary">등록</button>
  <button type="button" class="taBtn taBtnOutline">삭제</button>
</div>

<form method="post" action="${pageContext.request.contextPath}/matInspRegInq">
  <input type="hidden" name="cmd" value="list">

  <div class="taToolbarRow">

    <!-- 1) 판정구분 -->
    <div class="taToolbarField">
      <select class="taSelect" name="resultType">
        <option value="전체"
          <c:if test="${empty matInspRegInqSearchDTO.resultType or matInspRegInqSearchDTO.resultType eq '전체'}">selected</c:if>>
          전체
        </option>
        <option value="합격"
          <c:if test="${matInspRegInqSearchDTO.resultType eq '합격'}">selected</c:if>>
          합격
        </option>
        <option value="불합격"
          <c:if test="${matInspRegInqSearchDTO.resultType eq '불합격'}">selected</c:if>>
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
          value="${matInspRegInqSearchDTO.startDate}">

        <input
          type="date"
          class="taSearchInput"
          name="endDate"
          value="${matInspRegInqSearchDTO.endDate}">
      </div>
    </div>

    <!-- 3) 조회기준 -->
    <div class="taToolbarField">
      <select class="taSelect" name="searchType">
        <option value=""
          <c:if test="${empty matInspRegInqSearchDTO.searchType}">selected</c:if>>
          전체
        </option>
        <option value="itemCode"
          <c:if test="${matInspRegInqSearchDTO.searchType eq 'itemCode'}">selected</c:if>>
          품목코드
        </option>
        <option value="itemName"
          <c:if test="${matInspRegInqSearchDTO.searchType eq 'itemName'}">selected</c:if>>
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
          value="${matInspRegInqSearchDTO.keyword}">

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
          <th class="taTableHeadCell taColFit">검사수량</th>
          <th class="taTableHeadCell taColFit">양품수량</th>
          <th class="taTableHeadCell taColFit">불량수량</th>
          <th class="taTableHeadCell taColFit">판정</th>
          <th class="taTableHeadCell taColDate">검사일</th>
          <th class="taTableHeadCell taColAction taLastCol">상세보기</th>
        </tr>
      </thead>

      <tbody>
        <c:forEach var="dto" items="${matInspRegInqList}">
          <tr class="taTableBodyRow">
            <td class="taTableBodyCell taColFit">${dto.materialInspectionId}</td>
            <td class="taTableBodyCell taColFit">${dto.itemCode}</td>
            <td class="taTableBodyCell taColGrow">${dto.itemName}</td>
            <td class="taTableBodyCell taColFit">${dto.inspectQty}</td>
            <td class="taTableBodyCell taColFit">${dto.goodQty}</td>
            <td class="taTableBodyCell taColFit">${dto.defectQty}</td>
            <td class="taTableBodyCell taColFit">${dto.result}</td>
            <td class="taTableBodyCell taColDate">${dto.inspectionDate}</td>
            <td class="taTableBodyCell taColAction taLastCol">
              <form method="post"
                    action="${pageContext.request.contextPath}/matInspRegInq"
                    style="margin:0;">
                <input type="hidden" name="cmd" value="detail">
                <input type="hidden" name="materialInspectionId" value="${dto.materialInspectionId}">
                <button type="submit" class="taLinkButton">상세보기</button>
              </form>
            </td>
          </tr>
        </c:forEach>

        <c:if test="${empty matInspRegInqList}">
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

<c:if test="${not empty matInspRegInqDTO}">
  <div class="taTableShell" style="padding:20px; margin-top:20px;">
    <h3 style="margin-top:0;">상세조회 결과</h3>

    <p><strong>자재검사번호 :</strong> ${matInspRegInqDTO.materialInspectionId}</p>
    <p><strong>품목코드 :</strong> ${matInspRegInqDTO.itemCode}</p>
    <p><strong>품목명 :</strong> ${matInspRegInqDTO.itemName}</p>
    <p><strong>검사수량 :</strong> ${matInspRegInqDTO.inspectQty}</p>
    <p><strong>양품수량 :</strong> ${matInspRegInqDTO.goodQty}</p>
    <p><strong>불량수량 :</strong> ${matInspRegInqDTO.defectQty}</p>
    <p><strong>판정 :</strong> ${matInspRegInqDTO.result}</p>
    <p><strong>검사일 :</strong> ${matInspRegInqDTO.inspectionDate}</p>
    <p><strong>비고 :</strong> ${matInspRegInqDTO.remark}</p>
    <p><strong>생성일 :</strong> ${matInspRegInqDTO.createdAt}</p>
    <p><strong>수정일 :</strong> ${matInspRegInqDTO.updatedAt}</p>
  </div>
</c:if>