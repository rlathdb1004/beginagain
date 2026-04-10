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
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/assets/css/common.css" />
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/assets/css/ProdPlanRegInq.css" />
<script src="${pageContext.request.contextPath}/assets/js/common.js"></script>
</head>
<body>
	<%-- 공통 사이드바 --%>
	<div class="app">
		<aside class="sidebar">
			<button id="menuToggle">☰</button>
			<div class="sidebar-top">
				<div class="brand">
					<img class="brand-logo"
						src="${pageContext.request.contextPath}/assets/img/logo.png"
						alt="로고">
					<div>
						<div class="brand-title">Begin Again MES</div>
						<div class="brand-sub">2차전지 양극재 분채 가공</div>
					</div>
				</div>

				<div id="openProfileBtn" class="profile-card">
					<div class="profile-icon">
						<svg viewBox="0 0 24 24" fill="none" stroke="currentColor">
              <circle cx="12" cy="8" r="4" />
              <path d="M4 20c0-4 4-6 8-6s8 2 8 6" />
            </svg>
					</div>
					<div class="profile-text">
						<div class="name">CEO</div>
						<div class="role">최고경영자</div>
					</div>
					<svg class="alarm" xmlns="http://www.w3.org/2000/svg" width="24"
						height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor"
						stroke-width="2" stroke-linecap="round" stroke-linejoin="round"
						class="lucide lucide-bell w-4 h-4">
            <path d="M6 8a6 6 0 0 1 12 0c0 7 3 9 3 9H3s3-2 3-9"></path>
            <path d="M10.3 21a1.94 1.94 0 0 0 3.4 0"></path>
          </svg>
				</div>

				<nav class="sidebar-nav">
					<button class="nav-home active" data-page="dashboard">대시보드</button>

					<div class="menu-group">
						<button class="menu-title open" type="button">2. 자재관리</button>
						<div class="menu-items open">
							<button data-page="materials-register">입출고 등록 / 조회</button>
							<button data-page="materials-stock">재고 등록/조회</button>
						</div>
					</div>

					<div class="menu-group">
						<button class="menu-title open" type="button">3. 생산관리</button>
						<div class="menu-items open">
							<button data-page="production-plan-search">생산계획 등록/조회</button>
							<button data-page="production-result-search">생산실적 등록/조회</button>
						</div>
					</div>

					<div class="menu-group">
						<button class="menu-title open" type="button">4. 작업관리</button>
						<div class="menu-items open">
							<button data-page="work-order-manage">작업 지시 등록/조회</button>
							<button data-page="work-order-search">작업 현황 조회</button>
						</div>
					</div>

					<div class="menu-group">
						<button class="menu-title open" type="button">5. 품질관리</button>
						<div class="menu-items open">
							<button data-page="quality-rm">자재 검사 등록/조회</button>
							<button data-page="quality-fg">완제품 검사 등록/조회</button>
							<button data-page="quality-defect">불량 등록/조회</button>
						</div>
					</div>

					<div class="menu-group">
						<button class="menu-title open" type="button">6. 리포트</button>
						<div class="menu-items open">
							<button data-page="report">리포트</button>
						</div>
					</div>

					<div class="menu-group">
						<button class="menu-title open" type="button">7. 설비관리</button>
						<div class="menu-items open">
							<button data-page="facility-search">설비 등록/조회</button>
							<button data-page="facility-operation">설비 (비)가동률 등록/조회</button>
						</div>
					</div>

					<div class="menu-group">
						<button class="menu-title open" type="button">8. 기준관리</button>
						<div class="menu-items open">
							<button data-page="master-item">품목 관리</button>
							<button data-page="master-process">공정 관리</button>
							<button data-page="master-bom">BOM 관리</button>
							<button data-page="master-defect">불량 관리</button>
							<button data-page="master-equipment">설비 관리</button>
							<button data-page="master-employee">직원 등록</button>
						</div>
					</div>
				</nav>
			</div>

			<div class="sidebar-bottom">
				<button id="logoutBtn" class="btn logoutBtn">
					<svg xmlns="http://www.w3.org/2000/svg" width="18" height="18"
						viewBox="0 0 24 24" fill="none" stroke="currentColor"
						stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
            <path d="M10 3h8a2 2 0 0 1 2 2v14a2 2 0 0 1-2 2h-8" />
            <path d="M15 12H3" />
            <path d="M7 8l-4 4 4 4" />
          </svg>
					로그아웃
				</button>
			</div>
		</aside>

		<%-- 컨텐츠 상단 고정부 --%>
		<main class="main">

			<section class="global-topbar">
				<div class="global-box">
					<div class="global-title" id="pageMainTitle">생산관리</div>
					<div class="global-sub" id="pageSubTitle">생산계획 등록/조회</div>
				</div>

				<div class="global-clock">
					<div class="value" id="liveCalendar">2026-04-06</div>
					<div class="value" id="liveClock">09:00:00</div>
				</div>
			</section>

			<!-- 개별 화면 내용이 들어갈 곳 -->
            <%-- 생산계획 등록테이블 --%>
			<section class="page-section active"
				id="page-production-result-register" data-title="생산관리"
				data-subtitle="생산실적 등록">
				<div class="split-block">
					<div class="panel">
						<div class="panel-head">
							<h3>생산계획 등록</h3>
							<div class="action-row">
								<button class="btn btn-soft">추가</button>
								<button class="btn btn-primary">등록</button>
							</div>
						</div>
						<div class="table-wrap fixed-height">

                        <%-- 컬럼명 --%>
							<table class="mes-table">
								<thead>
									<tr>
										<th>NO</th>
										<th>생산계획번호</th>
										<th>일자</th>
										<th>품목코드</th>
										<th>품목명</th>
										<th>생산계획량</th>
										<th>단위</th>
										<th>라인</th>
										<th>비고</th>
										<th>상세보기</th>
									</tr>
								</thead>
                                <%-- 내용 --%>
								<tbody>
									<c:forEach var="dto" items="${list}">
										<tr>
											<td>${dto.seqNO}</td>
											<td>${dto.planNo}</td>
											<td>${dto.planDate}</td>
											<td>${dto.planCode}</td>
											<td>${dto.planName}</td>
											<td>${dto.planAmount}</td>											
											<td>${dto.planUnit}</td>											
											<td>${dto.planLine}</td>											
											<td>${dto.memo}</td>
											<td>상세보기</td>										
										</tr>
									</c:forEach>								
								</tbody>
							</table>
						</div>
					</div>
					<div class="panel">
						<div class="panel-head">
						<h3>생산계획 조회</h3>
                        </div>
                        <%-- 검색영역 --%>
                        <form method="get" action="prodplan">
                            <div class="filter-bar">
                                <div class="field-group">
                                    <label>기간</label> 
                                    <select>
                                        <option>기간</option>
                                        <option>오늘</option>
                                        <option>최근 7일</option>
                                    </select>
                                </div>
                                <div class="field-group field-grow">
                                    <label>검색키워드</label>
                                    <div class="search-box">
                                        <input type="text" placeholder="품목명 / 품목코드 / 생산계획번호 검색" />
                                        <button type="submit">🔍</button>
                                    </div>
                                </div>
                            </div>
                        </form>
					</div>
					<div class="table-wrap fixed-height">
                        <%-- 생산계획 조회 테이블 --%>
						<table class="mes-table">
							<thead>
								<tr>
									<th>NO</th>
									<th>생산계획번호</th>
									<th>일자</th>
									<th>품목코드</th>
									<th>품목명</th>
									<th>생산계획량</th>
									<th>단위</th>
									<th>라인</th>
									<th>비고</th>
									<th>상세보기</th>
								</tr>
							</thead>
							<tbody>
								<c:forEach var="dto" items="${list}">
									<tr>
										<td>${dto.seqNO}</td>
										<td>${dto.planNo}</td>
										<td>${dto.planDate}</td>
										<td>${dto.planCode}</td>
										<td>${dto.planName}</td>
										<td>${dto.planAmount}</td>											
										<td>${dto.planUnit}</td>											
										<td>${dto.planLine}</td>											
										<td>${dto.memo}</td>
										<td>상세보기</td>										
									</tr>
								</c:forEach>
							</tbody>
						</table>
					</div>
				</div>
	</div>
	</section>
	</main>
	</div>
</body>
</html>