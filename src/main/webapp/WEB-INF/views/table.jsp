<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<link rel="stylesheet" href="table.css">
</head>
<body class="taBody">
  <!-- 실제 화면에 보이는 본문 영역 시작 / taBody는 배경색, 글자색, 폰트 적용용 -->

  <section
    id="page-materials-register"
    class="page-section active"
    data-title="입출고 등록 / 조회"
    data-subtitle="자재 입출고 내역을 등록하고 조회하는 화면"
  >
    <!--
      이 section이 자재관리 > 입출고 등록/조회 한 페이지 전체 영역
      id="page-materials-register"
        : 나중에 메인 사이드바 JS에서 페이지를 찾을 때 사용하는 고유 id
      class="page-section active"
        : 공통 페이지 형식 + 현재 단독 실행에서는 active 상태
      data-title / data-subtitle
        : 나중에 메인 상단 제목/설명에 연결할 수 있는 데이터
    -->

    <div class="taMaterialsInoutOnly">
      <!--
        이 페이지의 실제 내용 전체를 감싸는 래퍼
        상단 버튼 / 검색영역 / 테이블을 세로로 배치하기 위한 박스
      -->

      <div class="taPageActions">
        <!-- 상단 오른쪽 버튼 영역 -->

        <button type="button" class="taBtn taBtnPrimary">등록</button>
        <!-- 등록 버튼
             taBtn         : 버튼 공통 모양
             taBtnPrimary  : 파란 배경의 강조 버튼 스타일 -->

        <button type="button" class="taBtn taBtnOutline">삭제</button>
        <!-- 삭제 버튼
             taBtn         : 버튼 공통 모양
             taBtnOutline  : 흰 배경 + 테두리 버튼 스타일 -->
      </div>

      <div class="taToolbarRow">
        <!--
          검색 조건 영역 한 줄 전체
          4칸 그리드 구조:
          1) 입출고구분
          2) 기간
          3) 조회기준
          4) 검색 입력창
        -->

        <div class="taToolbarField">
          <!-- 첫 번째 검색 필드 박스 -->

          <select class="taSelect">
            <!-- 드롭다운 공통 스타일 적용 -->
            <option>전체</option>
            <option>입고</option>
            <option>출고</option>
            <option>반품</option>
          </select>
        </div>

        <div class="taToolbarField">
          <!-- 두 번째 검색 필드 박스 -->

          <select class="taSelect">
            <option>기간</option>
            <option>오늘</option>
            <option>최근 7일</option>
            <option>최근 30일</option>
          </select>
        </div>

        <div class="taToolbarField">
          <!-- 세 번째 검색 필드 박스 -->

          <select class="taSelect">
            <option>조회</option>
            <option>품목명</option>
            <option>품목코드</option>
            <option>발주번호</option>
          </select>
        </div>

        <div class="taToolbarField taToolbarFieldGrow">
          <!--
            네 번째 검색 필드 박스
            taToolbarFieldGrow는 마지막 검색창 영역에서 추가 확장용으로 사용
          -->

          <div class="taSearchBox">
            <!-- 검색 input + 검색 버튼을 가로로 묶는 박스 -->

            <input type="text" class="taSearchInput" placeholder="검색키워드">
            <!-- 검색어 입력창 -->

            <button type="button" class="taSearchBtn" aria-label="검색">⌕</button>
            <!-- 검색 버튼 / aria-label은 접근성용 텍스트 -->
          </div>
        </div>
      </div>

      <div class="taTableShell">
        <!--
          테이블 바깥 껍데기 박스
          흰 배경, 둥근 모서리, 테두리 처리 담당
        -->

        <div class="taTableScroll">
          <!--
            가로 스크롤 허용 영역
            컬럼이 많아져도 표 전체가 깨지지 않도록 감싸는 박스
          -->

          <table class="taMesTable">
            <!-- 실제 데이터 표 -->

            <thead>

              <tr>
                <!-- 헤더 한 줄 -->
                <th class="taTableHeadCell taColFit">NO</th>
                <th class="taTableHeadCell taColFit">입출고구분</th>
                <th class="taTableHeadCell taColFit">품목코드</th>
                <th class="taTableHeadCell taColGrow">품목명</th>
                <th class="taTableHeadCell taColFit">입출고수량</th>
                <th class="taTableHeadCell taColFit">단위</th>
                <th class="taTableHeadCell taColDate">일자</th>
                <th class="taTableHeadCell taColAction taLastCol">상세보기</th>
              </tr>
            </thead>

            <tbody>
              <!-- 테이블 실제 데이터 영역 시작 -->

              <tr class="taTableBodyRow">
                <!-- 첫 번째 데이터 행 -->
                <td class="taTableBodyCell taColFit">10</td>
                <td class="taTableBodyCell taColFit">입고</td>
                <td class="taTableBodyCell taColFit">RM-LI-001</td>
                <td class="taTableBodyCell taColGrow">수산화리튬</td>
                <td class="taTableBodyCell taColFit">2,000</td>
                <td class="taTableBodyCell taColFit">kg</td>
                <td class="taTableBodyCell taColDate">2026-04-07</td>
                <td class="taTableBodyCell taColAction taLastCol">
                  <button type="button" class="taLinkButton">상세보기</button>
                  <!-- 행 내부 상세보기 버튼 -->
                </td>
              </tr>

              <tr class="taTableBodyRow">
                <!-- 두 번째 데이터 행 -->
                <td class="taTableBodyCell taColFit">9</td>
                <td class="taTableBodyCell taColFit">출고</td>
                <td class="taTableBodyCell taColFit">RM-LI-001</td>
                <td class="taTableBodyCell taColGrow">수산화리튬</td>
                <td class="taTableBodyCell taColFit">5,000</td>
                <td class="taTableBodyCell taColFit">kg</td>
                <td class="taTableBodyCell taColDate">2026-04-06</td>
                <td class="taTableBodyCell taColAction taLastCol">
                  <button type="button" class="taLinkButton">상세보기</button>
                </td>
              </tr>

            </tbody>
          </table>
        </div>
      </div>

    </div>
  </section>
</body>
</html>