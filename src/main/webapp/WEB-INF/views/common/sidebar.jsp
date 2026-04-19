<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<c:set var="siCtx" value="${pageContext.request.contextPath}" />
<c:set var="siUri" value="${pageContext.request.requestURI}" />

<c:set var="siIsDashboard" value="${fn:contains(siUri, '/ceomain') or fn:contains(siUri, '/notice') or fn:contains(siUri, '/suggestion')}" />
<c:set var="siIsMaterials" value="${fn:contains(siUri, '/ioRegInq') or fn:contains(siUri, '/invRegInq')}" />
<c:set var="siIsProduction" value="${fn:contains(siUri, '/prodplan') or fn:contains(siUri, '/prodperf')}" />
<c:set var="siIsWork" value="${fn:contains(siUri, '/woreginq') or fn:contains(siUri, '/workstatus')}" />
<c:set var="siIsQuality" value="${fn:contains(siUri, '/matInspRegInq') or fn:contains(siUri, '/fpInspRegInq') or fn:contains(siUri, '/defectRegInq')}" />
<c:set var="siIsReport" value="${fn:contains(siUri, '/report')}" />
<c:set var="siIsFacility" value="${fn:contains(siUri, '/maintenance/list') or fn:contains(siUri, '/downtime/list')}" />
<c:set var="siIsMaster" value="${fn:contains(siUri, '/item/list') or fn:contains(siUri, '/process/list') or fn:contains(siUri, '/routing/list') or fn:contains(siUri, '/BOM-mgmt') or fn:contains(siUri, '/defect-mgmt') or fn:contains(siUri, '/equipment/list') or fn:contains(siUri, '/member/list')}" />

<aside class="sidebar">

    <!-- 햄버거 -->
    <button id="menuToggle">☰</button>

    <!-- 로고 -->
    <div class="sidebar-top">
        <div class="brand">
            <img class="brand-logo"
                 src="${siCtx}/assets/img/logo.png"
                 alt="로고">
            <div>
                <div class="brand-title">Begin Again</div>
                <div class="brand-sub">2차전지 양극재 분체 가공</div>
            </div>
        </div>

        <!-- 프로필 -->
        <a class="profile-card" href="${siCtx}/mypage" style="text-decoration:none; color:inherit;">
            <div class="profile-icon">
                <svg viewBox="0 0 24 24" fill="none" stroke="currentColor">
                    <circle cx="12" cy="8" r="4" />
                    <path d="M4 20c0-4 4-6 8-6s8 2 8 6" />
                </svg>
            </div>

            <div class="profile-text">
                <div class="name">${loginUser.empName}</div>
                <div class="role">${loginUser.roleName}</div>
            </div>

            <svg class="alarm" xmlns="http://www.w3.org/2000/svg" width="24"
                 height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor"
                 stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
                <path d="M6 8a6 6 0 0 1 12 0c0 7 3 9 3 9H3s3-2 3-9"></path>
                <path d="M10.3 21a1.94 1.94 0 0 0 3.4 0"></path>
            </svg>
        </a>

        <!-- 메뉴 -->
        <nav class="sidebar-nav">

            <!-- 1. 대시보드 -->
            <div class="menu-group">
                <button class="menu-title${siIsDashboard ? ' open' : ''}" type="button">1. 대시보드</button>
                <div class="menu-items${siIsDashboard ? ' open' : ''}">
                    <a href="${siCtx}/ceomain" class="${fn:contains(siUri, '/ceomain') ? 'active' : ''}">메인</a>
                    <a href="${siCtx}/notice" class="${fn:contains(siUri, '/notice') ? 'active' : ''}">공지사항</a>
                    <a href="${siCtx}/suggestion" class="${fn:contains(siUri, '/suggestion') ? 'active' : ''}">건의사항</a>
                </div>
            </div>

            <!-- 2. 자재관리 -->
            <div class="menu-group">
                <button class="menu-title${siIsMaterials ? ' open' : ''}" type="button">2. 자재관리</button>
                <div class="menu-items${siIsMaterials ? ' open' : ''}">
                    <a href="${siCtx}/ioRegInq" class="${fn:contains(siUri, '/ioRegInq') ? 'active' : ''}">입출고 등록 /조회</a>
                    <a href="${siCtx}/invRegInq" class="${fn:contains(siUri, '/invRegInq') ? 'active' : ''}">재고등록 / 조회</a>
                </div>
            </div>

            <!-- 3. 생산관리 -->
            <div class="menu-group">
                <button class="menu-title${siIsProduction ? ' open' : ''}" type="button">3. 생산관리</button>
                <div class="menu-items${siIsProduction ? ' open' : ''}">
                    <a href="${siCtx}/prodplan" class="${fn:contains(siUri, '/prodplan') ? 'active' : ''}">생산 계획 등록/조회</a>
                    <a href="${siCtx}/prodperf" class="${fn:contains(siUri, '/prodperf') ? 'active' : ''}">생산 실적 등록/조회</a>
                </div>
            </div>

            <!-- 4. 작업관리 -->
            <div class="menu-group">
                <button class="menu-title${siIsWork ? ' open' : ''}" type="button">4. 작업관리</button>
                <div class="menu-items${siIsWork ? ' open' : ''}">
                    <a href="${siCtx}/woreginq" class="${fn:contains(siUri, '/woreginq') ? 'active' : ''}">작업 지시 등록/조회</a>
                    <a href="${siCtx}/workstatus" class="${fn:contains(siUri, '/workstatus') ? 'active' : ''}">작업 현황 조회</a>
                </div>
            </div>

            <!-- 5. 품질관리 -->
            <div class="menu-group">
                <button class="menu-title${siIsQuality ? ' open' : ''}" type="button">5. 품질관리</button>
                <div class="menu-items${siIsQuality ? ' open' : ''}">
                    <a href="${siCtx}/matInspRegInq" class="${fn:contains(siUri, '/matInspRegInq') ? 'active' : ''}">자재 검사 등록/조회</a>
                    <a href="${siCtx}/fpInspRegInq" class="${fn:contains(siUri, '/fpInspRegInq') ? 'active' : ''}">완제품 검사 등록/조회</a>
                    <a href="${siCtx}/defectRegInq" class="${fn:contains(siUri, '/defectRegInq') ? 'active' : ''}">불량 이력 등록/조회</a>
                </div>
            </div>

            <!-- 6. 리포트 -->
            <div class="menu-group">
                <button class="menu-title${siIsReport ? ' open' : ''}" type="button">6. 리포트</button>
                <div class="menu-items${siIsReport ? ' open' : ''}">
                    <a href="${siCtx}/report" class="${fn:contains(siUri, '/report') ? 'active' : ''}">리포트</a>
                </div>
            </div>

            <!-- 7. 설비운영 -->
            <div class="menu-group">
                <button class="menu-title${siIsFacility ? ' open' : ''}" type="button">7. 설비운영</button>
                <div class="menu-items${siIsFacility ? ' open' : ''}">
                    <a href="${siCtx}/maintenance/list" class="${fn:contains(siUri, '/maintenance/list') ? 'active' : ''}">정비 이력 등록/조회</a>
                    <a href="${siCtx}/downtime/list" class="${fn:contains(siUri, '/downtime/list') ? 'active' : ''}">비가동 현황</a>
                </div>
            </div>

            <!-- 8. 기준관리 -->
            <div class="menu-group">
                <button class="menu-title${siIsMaster ? ' open' : ''}" type="button">8. 기준관리</button>
                <div class="menu-items${siIsMaster ? ' open' : ''}">
                    <a href="${siCtx}/item/list" class="${fn:contains(siUri, '/item/list') ? 'active' : ''}">품목 관리</a>
                    <a href="${siCtx}/process/list" class="${fn:contains(siUri, '/process/list') ? 'active' : ''}">공정 관리</a>
                    <a href="${siCtx}/routing/list" class="${fn:contains(siUri, '/routing/list') ? 'active' : ''}">라우팅 관리</a>
                    <a href="${siCtx}/BOM-mgmt" class="${fn:contains(siUri, '/BOM-mgmt') ? 'active' : ''}">BOM 관리</a>
                    <a href="${siCtx}/defect-mgmt" class="${fn:contains(siUri, '/defect-mgmt') ? 'active' : ''}">불량 관리</a>
                    <a href="${siCtx}/equipment/list" class="${fn:contains(siUri, '/equipment/list') ? 'active' : ''}">설비 관리</a>
                    <a href="${siCtx}/member/list" class="${fn:contains(siUri, '/member/list') ? 'active' : ''}">직원 관리</a>
                </div>
            </div>

        </nav>
    </div>

    <!-- 로그아웃 -->
    <div style="padding: 14px 8px;">
        <a href="${siCtx}/logout"
           style="display: block; text-align: center; padding: 10px 12px; border-radius: 12px; background: rgba(255, 255, 255, 0.08); color: #fff; text-decoration: none;">
            로그아웃
        </a>
    </div>

</aside>
