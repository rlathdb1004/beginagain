<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
	<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
		<!DOCTYPE html>
		<html lang="ko">

		<head>
			<meta charset="UTF-8">
			<title>품목관리</title>
			<link rel="stylesheet" href="/beginagain/assets/css/sidebar.css">
			<link rel="stylesheet" href="/beginagain/assets/css/itemMgmt.css">
			<link rel="stylesheet" href="/beginagain/assets/css/modal.css">
		</head>

		<body>
			<div class="app">
				<aside class="sidebar">
					<button id="menuToggle">☰</button>
					<div class="sidebar-top">
						<div class="brand">
							<img class="brand-logo" src="/beginagain/assets/img/logo.png">
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
							<svg class="alarm" xmlns="http://www.w3.org/2000/svg" width="24" height="24"
								viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"
								stroke-linecap="round" stroke-linejoin="round" class="lucide lucide-bell w-4 h-4">
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
									<button data-page="work-order-manage">작업 지시 등록/조회 </button>
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
							<svg xmlns="http://www.w3.org/2000/svg" width="18" height="18" viewBox="0 0 24 24"
								fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round"
								stroke-linejoin="round">
								<path d="M10 3h8a2 2 0 0 1 2 2v14a2 2 0 0 1-2 2h-8" />
								<path d="M15 12H3" />
								<path d="M7 8l-4 4 4 4" />
							</svg>
							로그아웃
						</button>
					</div>
				</aside>

				<main class="main">

					<section class="global-topbar">
						<div class="global-box">
							<div class="global-title" id="pageMainTitle">대시보드</div>
							<div class="global-sub" id="pageSubTitle">생산, 품질, 설비 현황을 한 눈에 보는 메인 화면</div>
						</div>

						<div class="global-clock">
							<div class="value" id="liveCalendar">2026-04-06</div>
							<div class="value" id="liveClock">09:00:00</div>
						</div>
					</section>

					<!-- 개별 화면 내용이 들어갈 곳 -->
					<div id="content-area">
						<section class="page-section active" id="page-master-item" data-title="8. 기준관리"
							data-subtitle="품목관리">

							<div class="filter-bar">
								<div class="filters">
									<div class="filter-type">
										<label for="itemType" class="label">분류</label> <select id="itemType">
											<option>전체</option>
											<option>원자재</option>
											<option>완제품</option>
										</select>
									</div>

									<div class="date-filter">
										<div class="filter-date">
											<label for="dateType" class="label">기간 선택</label> <select id="dateType">
												<option value="reg">등록일</option>
												<option value="mod">수정일</option>
											</select>
										</div>

										<input type="date" id="startDate"> <span class="ft">~</span>
										<input type="date" id="endDate">
									</div>
									<div class="filter-options">
										<label class="label">검색 항목 선택</label> <select id="searchField">
											<option value="">전체</option>
											<option value="item_id">품목 ID</option>
											<option value="item_code">품목 코드</option>
											<option value="item_name">품목명</option>
											<option value="unit">단위</option>
											<option value="spec">규격</option>
											<option value="supplier_name">공급업체</option>
											<option value="safety_stock">안전재고</option>
											<option value="use_yn">사용 여부</option>
											<option value="created_at">등록일</option>
											<option value="updated_at">수정일</option>
										</select>
									</div>

									<div class="search-box">
										<input type="text" id="keyword" placeholder="검색" /> <span class="icon">🔍</span>
									</div>
								</div>

								<!-- 우측 버튼 -->
								<div class="taPageActions">
									<button type="button" class="taBtn taBtnPrimary">등록</button>
									<button type="button" class="taBtn taBtnOutline">삭제</button>
								</div>
							</div>

							<table id="table">
								<tr>
									<th>품목 ID</th>
									<th>품목 코드</th>
									<th>품목명</th>
									<th>품목 구분</th>
									<th>단위</th>
									<th>규격</th>
									<th>공급업체</th>
									<th>안전재고</th>
									<th>사용 여부</th>
									<th>비고</th>
									<th>등록일</th>
									<th>수정일</th>
								</tr>

								<c:forEach var="item" items="${itemList}">
									<tr class="td">
										<td>${item.item_id}</td>
										<td>${item.item_code}</td>
										<td>${item.item_name}</td>
										<td>${item.item_type}</td>
										<td>${item.unit}</td>
										<td>${item.spec}</td>
										<td>${item.supplier_name}</td>
										<td>${item.safety_stock}</td>
										<td>${item.use_yn}</td>
										<td>${item.remark}</td>
										<td>${item.created_at}</td>
										<td>${item.updated_at}</td>
									</tr>
								</c:forEach>
							</table>

							<div class="modal-overlay">
								<div class="modal-box">
									<form class="modal-form-group">
										<h3>품목 등록</h3>

										<input type="text" name="item_code" placeholder="코드">
										<input type="text" name="item_name" placeholder="이름">
										<input type="text" name="item_type" placeholder="타입">
										<input type="text" name="unit" placeholder="단위">
										<input type="text" name="spec" placeholder="규격">
										<input type="text" name="supplier_name" placeholder="공급처">
										<input type="number" name="safety_stock" placeholder="안전재고">

										<button class="add" type="button">등록</button>
										<button class="modal-close">닫기</button>
									</form>


								</div>
							</div>
						</section>
					</div>

				</main>
			</div>

			<div id="toast" class="toast">저장되었습니다.</div>

			<script src="/beginagain/assets/js/sidebar.js"></script>
			<script src="${pageContext.request.contextPath}/assets/js/itemMgmt.js"></script>
		</body>

		</html>
		