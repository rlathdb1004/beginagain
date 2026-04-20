<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<c:set var="siCtx" value="${pageContext.request.contextPath}" />
<c:set var="siUri" value="${pageContext.request.requestURI}" />

<c:set var="role" value="${sessionScope.loginUser.roleName}" />
<c:set var="isCeo" value="${role eq 'CEO'}" />
<c:set var="isMesAdmin" value="${role eq 'MES_ADMIN'}" />
<c:set var="isSiteManager" value="${role eq 'SITE_MANAGER'}" />
<c:set var="isWorker" value="${role eq 'WORKER'}" />

<c:set var="canUseProdMain"
	value="${isMesAdmin or isSiteManager or isWorker}" />
<c:set var="canViewMasterMenu" value="${isCeo or isMesAdmin}" />

<c:set var="showMaterialsMenu" value="${isMesAdmin or isSiteManager}" />
<c:set var="showProductionMenu"
	value="${isMesAdmin or isSiteManager or isWorker or isCeo}" />
<c:set var="showWorkMenu"
	value="${isMesAdmin or isSiteManager or isWorker or isCeo}" />
<c:set var="showQualityMenu"
	value="${isMesAdmin or isSiteManager or isWorker}" />
<c:set var="showFacilityMenu" value="${isMesAdmin or isSiteManager}" />
<c:set var="showReportMenu" value="true" />

<c:set var="siIsDashboard"
	value="${fn:contains(siUri, '/ceomain')
		or fn:contains(siUri, '/prodmain')
		or fn:contains(siUri, '/adminmain')
		or fn:contains(siUri, '/notice')
		or fn:contains(siUri, '/suggestion')}" />

<c:set var="siIsMaterials"
	value="${fn:contains(siUri, '/ioRegInq')
		or fn:contains(siUri, '/invRegInq')}" />

<c:set var="siIsProduction"
	value="${fn:contains(siUri, '/prodplan')
		or fn:contains(siUri, '/prodperf')}" />

<c:set var="siIsWork"
	value="${fn:contains(siUri, '/woreginq')
		or fn:contains(siUri, '/workstatus')}" />

<c:set var="siIsQuality"
	value="${fn:contains(siUri, '/matInspRegInq')
		or fn:contains(siUri, '/fpInspRegInq')
		or fn:contains(siUri, '/defectRegInq')}" />

<c:set var="siIsReport" value="${fn:contains(siUri, '/report')}" />

<c:set var="siIsFacility"
	value="${fn:contains(siUri, '/maintenance/list')
		or fn:contains(siUri, '/downtime/list')}" />

<c:set var="siIsMaster"
	value="${fn:contains(siUri, '/item/list')
		or fn:contains(siUri, '/item/detail')
		or fn:contains(siUri, '/process/list')
		or fn:contains(siUri, '/process/detail')
		or fn:contains(siUri, '/routing/list')
		or fn:contains(siUri, '/routing/detail')
		or fn:contains(siUri, '/BOM-mgmt')
		or fn:contains(siUri, '/defect-mgmt')
		or fn:contains(siUri, '/equipment/list')
		or fn:contains(siUri, '/equipment/detail')
		or fn:contains(siUri, '/member/list')
		or fn:contains(siUri, '/member/detail')}" />

<aside class="sidebar">

	<button id="menuToggle">☰</button>

	<div class="sidebar-top">
		<div class="brand">
			<img class="brand-logo" src="${siCtx}/assets/img/logo.png" alt="로고">
			<div>
				<div class="brand-title">Begin Again</div>
				<div class="brand-sub">2차전지 양극재 분체 가공</div>
			</div>
		</div>

		<a class="profile-card" href="${siCtx}/mypage"
			style="text-decoration: none; color: inherit;">
			<div class="profile-icon">
				<svg viewBox="0 0 24 24" fill="none" stroke="currentColor">
					<circle cx="12" cy="8" r="4" />
					<path d="M4 20c0-4 4-6 8-6s8 2 8 6" />
				</svg>
			</div>

			<div class="profile-text">
				<div class="name">${loginUser.empName}</div>
				<div class="role">
					<c:choose>
						<c:when test="${loginUser.roleName eq 'CEO'}">ceo</c:when>
						<c:when test="${loginUser.roleName eq 'MES_ADMIN'}">mes관리자</c:when>
						<c:when test="${loginUser.roleName eq 'SITE_MANAGER'}">현장관리자</c:when>
						<c:when test="${loginUser.roleName eq 'WORKER'}">작업자</c:when>
						<c:otherwise>${loginUser.roleName}</c:otherwise>
					</c:choose>
				</div>
			</div> <svg class="alarm" xmlns="http://www.w3.org/2000/svg" width="24"
				height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor"
				stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
				<path d="M6 8a6 6 0 0 1 12 0c0 7 3 9 3 9H3s3-2 3-9"></path>
				<path d="M10.3 21a1.94 1.94 0 0 0 3.4 0"></path>
			</svg>
		</a>

		<nav class="sidebar-nav">

			<!-- 1. 대시보드 -->
			<div class="menu-group">
				<button class="menu-title${siIsDashboard ? ' open' : ''}"
					type="button">1. 대시보드</button>
				<div class="menu-items${siIsDashboard ? ' open' : ''}">
					<c:if test="${isCeo or isMesAdmin}">
						<a href="${siCtx}/ceomain"
							class="${fn:contains(siUri, '/ceomain') ? 'active' : ''}">ceo 메인</a>
					</c:if>
					<c:if test="${canUseProdMain}">
						<a href="${siCtx}/prodmain"
							class="${fn:contains(siUri, '/prodmain') ? 'active' : ''}">생산관리 메인</a>
					</c:if>

					<c:if test="${isMesAdmin}">
						<a href="${siCtx}/adminmain"
							class="${fn:contains(siUri, '/adminmain') ? 'active' : ''}">mes관리자 메인</a>
					</c:if>

					<a href="${siCtx}/notice"
						class="${fn:contains(siUri, '/notice') ? 'active' : ''}">공지사항</a>

					<a href="${siCtx}/suggestion"
						class="${fn:contains(siUri, '/suggestion') ? 'active' : ''}">건의사항</a>

				</div>
			</div>

			<!-- 2. 자재관리 -->
			<c:if test="${showMaterialsMenu}">
				<div class="menu-group">
					<button class="menu-title${siIsMaterials ? ' open' : ''}"
						type="button">2. 자재관리</button>
					<div class="menu-items${siIsMaterials ? ' open' : ''}">
						<a href="${siCtx}/ioRegInq"
							class="${fn:contains(siUri, '/ioRegInq') ? 'active' : ''}">입출고
							관리</a> <a href="${siCtx}/invRegInq"
							class="${fn:contains(siUri, '/invRegInq') ? 'active' : ''}">재고
							관리</a>
					</div>
				</div>
			</c:if>

			<!-- 3. 생산관리 -->
			<c:if test="${showProductionMenu}">
				<div class="menu-group">
					<button class="menu-title${siIsProduction ? ' open' : ''}"
						type="button">3. 생산관리</button>
					<div class="menu-items${siIsProduction ? ' open' : ''}">
						<c:if test="${isMesAdmin or isSiteManager}">
							<a href="${siCtx}/prodplan"
								class="${fn:contains(siUri, '/prodplan') ? 'active' : ''}">생산계획
								관리</a>
						</c:if>

						<c:if test="${isMesAdmin or isSiteManager or isWorker or isCeo}">
							<a href="${siCtx}/prodperf"
								class="${fn:contains(siUri, '/prodperf') ? 'active' : ''}">생산실적
								관리</a>
						</c:if>
					</div>
				</div>
			</c:if>

			<!-- 4. 작업관리 -->
			<c:if test="${showWorkMenu}">
				<div class="menu-group">
					<button class="menu-title${siIsWork ? ' open' : ''}" type="button">4.
						작업관리</button>
					<div class="menu-items${siIsWork ? ' open' : ''}">
						<c:if test="${isMesAdmin or isSiteManager}">
							<a href="${siCtx}/woreginq"
								class="${fn:contains(siUri, '/woreginq') ? 'active' : ''}">작업지시
								관리</a>
						</c:if>

						<c:if test="${isMesAdmin or isSiteManager or isWorker or isCeo}">
							<a href="${siCtx}/workstatus"
								class="${fn:contains(siUri, '/workstatus') ? 'active' : ''}">작업현황</a>
						</c:if>
					</div>
				</div>
			</c:if>

			<!-- 5. 품질관리 -->
			<c:if test="${showQualityMenu}">
				<div class="menu-group">
					<button class="menu-title${siIsQuality ? ' open' : ''}"
						type="button">5. 품질관리</button>
					<div class="menu-items${siIsQuality ? ' open' : ''}">
						<a href="${siCtx}/matInspRegInq"
							class="${fn:contains(siUri, '/matInspRegInq') ? 'active' : ''}">자재검사
							관리</a> <a href="${siCtx}/fpInspRegInq"
							class="${fn:contains(siUri, '/fpInspRegInq') ? 'active' : ''}">완제품검사
							관리</a> <a href="${siCtx}/defectRegInq"
							class="${fn:contains(siUri, '/defectRegInq') ? 'active' : ''}">불량이력
							관리</a>
					</div>
				</div>
			</c:if>

			<!-- 6. 리포트 -->
			<c:if test="${showReportMenu}">
				<div class="menu-group">
					<button class="menu-title${siIsReport ? ' open' : ''}"
						type="button">6. 리포트</button>
					<div class="menu-items${siIsReport ? ' open' : ''}">
						<a href="${siCtx}/report"
							class="${fn:contains(siUri, '/report') ? 'active' : ''}">리포트
							조회</a>
					</div>
				</div>
			</c:if>

			<!-- 7. 설비운영 -->
			<c:if test="${showFacilityMenu}">
				<div class="menu-group">
					<button class="menu-title${siIsFacility ? ' open' : ''}"
						type="button">7. 설비운영</button>
					<div class="menu-items${siIsFacility ? ' open' : ''}">
						<a href="${siCtx}/maintenance/list"
							class="${fn:contains(siUri, '/maintenance/list') ? 'active' : ''}">정비이력</a>
						<a href="${siCtx}/downtime/list"
							class="${fn:contains(siUri, '/downtime/list') ? 'active' : ''}">설비
							가동 현황</a>
					</div>
				</div>
			</c:if>

			<!-- 8. 기준관리 -->
			<c:if test="${canViewMasterMenu}">
				<div class="menu-group">
					<button class="menu-title${siIsMaster ? ' open' : ''}"
						type="button">8. 기준관리</button>
					<div class="menu-items${siIsMaster ? ' open' : ''}">
						<a href="${siCtx}/item/list"
							class="${fn:contains(siUri, '/item/list') or fn:contains(siUri, '/item/detail') ? 'active' : ''}">품목
							관리</a> <a href="${siCtx}/process/list"
							class="${fn:contains(siUri, '/process/list') or fn:contains(siUri, '/process/detail') ? 'active' : ''}">공정
							관리</a> <a href="${siCtx}/routing/list"
							class="${fn:contains(siUri, '/routing/list') or fn:contains(siUri, '/routing/detail') ? 'active' : ''}">라우팅
							관리</a> <a href="${siCtx}/BOM-mgmt"
							class="${fn:contains(siUri, '/BOM-mgmt') ? 'active' : ''}">BOM
							관리</a> <a href="${siCtx}/defect-mgmt"
							class="${fn:contains(siUri, '/defect-mgmt') ? 'active' : ''}">불량
							관리</a> <a href="${siCtx}/equipment/list"
							class="${fn:contains(siUri, '/equipment/list') or fn:contains(siUri, '/equipment/detail') ? 'active' : ''}">설비
							관리</a>

						<c:if test="${isMesAdmin}">
							<a href="${siCtx}/member/list"
								class="${fn:contains(siUri, '/member/list') or fn:contains(siUri, '/member/detail') ? 'active' : ''}">직원
								관리</a>
						</c:if>
					</div>
				</div>
			</c:if>

		</nav>
	</div>

	<div style="padding: 14px 8px;">
		<a href="${siCtx}/logout"
			style="display: block; text-align: center; padding: 10px 12px; border-radius: 12px; background: rgba(255, 255, 255, 0.08); color: #fff; text-decoration: none;">
			로그아웃 </a>
	</div>

</aside>