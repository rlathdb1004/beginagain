<%@ page contentType="text/html; charset=UTF-8"%>

<aside class="sidebar">

	<!-- 햄버거 -->
	<button id="menuToggle">☰</button>

	<!-- 로고 -->
	<div class="sidebar-top">
		<div class="brand">
			<img class="brand-logo"
				src="${pageContext.request.contextPath}/assets/img/logo.png"
				alt="로고">
			<div>
				<div class="brand-title">Begin Again</div>
				<div class="brand-sub">2차전지 양극재 분체 가공</div>
			</div>
		</div>

		<!-- 프로필 -->
		<div class="profile-card">
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
		</div>

		<!-- 메뉴 -->
		<nav class="sidebar-nav">

			<!-- 1. 대시보드 -->
			<div class="menu-group">
				<button class="menu-title open" type="button">1. 대시보드</button>
				<div class="menu-items open">
					<a href="${pageContext.request.contextPath}/ceomain">메인</a> <a
						href="${pageContext.request.contextPath}/notice">공지사항</a> <a
						href="${pageContext.request.contextPath}/suggestion">건의사항</a>
				</div>
			</div>

			<!-- 2. 자재관리 -->
			<div class="menu-group">
				<button class="menu-title open" type="button">2. 자재관리</button>
				<div class="menu-items open">
					<a href="${pageContext.request.contextPath}/ioRegInq">입출고 등록 /조회</a> 
					<a href="${pageContext.request.contextPath}/invRegInq">재고등록 / 조회</a>
				</div>
			</div>

			<!-- 3. 생산관리 -->
			<div class="menu-group">
				<button class="menu-title open" type="button">3. 생산관리</button>
				<div class="menu-items open">
					<a href="${pageContext.request.contextPath}/prodplan">생산 계획 등록/조회</a> 
					<a href="${pageContext.request.contextPath}/prodperf">생산 실적 등록/조회</a>
				</div>
			</div>

			<!-- 4. 작업관리 -->
			<div class="menu-group">
				<button class="menu-title open" type="button">4. 작업관리</button>
				<div class="menu-items open">
					<a href="${pageContext.request.contextPath}/woreginq">작업
						지시 등록/조회</a> 
					<a href="${pageContext.request.contextPath}/workstatus">작업 현황 조회</a>
				</div>
			</div>

			<!-- 5. 품질관리 -->
			<div class="menu-group">
				<button class="menu-title open" type="button">5. 품질관리</button>
				<div class="menu-items open">
					<a href="${pageContext.request.contextPath}/matInspRegInq">자재 검사 등록/조회</a> 
					<a href="${pageContext.request.contextPath}/fpInspRegInq">완제품 검사 등록/조회</a> 
					<a href="${pageContext.request.contextPath}/defectRegInq">불량 등록/조회</a>
				</div>
			</div>

			<!-- 6. 리포트 -->
			<div class="menu-group">
				<button class="menu-title open" type="button">6. 리포트</button>
				<div class="menu-items open">
					<a href="${pageContext.request.contextPath}/report">리포트</a>
				</div>
			</div>

			<!-- 7. 설비관리 -->
			<div class="menu-group">
				<button class="menu-title open" type="button">7. 설비관리</button>
				<div class="menu-items open">
					<a href="#">정비 이력 등록/조회</a>
					<a href="#">설비 비가동 등록/조회</a>
				</div>
			</div>

			<!-- 8. 기준관리 -->
			<div class="menu-group">
				<button class="menu-title open" type="button">8. 기준관리</button>
				<div class="menu-items open">
					<a href="${pageContext.request.contextPath}/item/list">품목 관리</a> <a
						href="${pageContext.request.contextPath}/process/list">공정 관리</a> <a
						href="${pageContext.request.contextPath}/routing/list">라우팅 관리</a> <a 
						href="${pageContext.request.contextPath}/BOM-mgmt">BOM 관리</a> <a 
						href="${pageContext.request.contextPath}/defect-mgmt">불량 관리</a> <a
						href="${pageContext.request.contextPath}/equipment/list">설비 관리</a> <a
						href="${pageContext.request.contextPath}/member/list">직원 관리</a>
				</div>
			</div>

		</nav>
	</div>

	<!-- 로그아웃 -->
	<div style="padding: 14px 8px;">
		<a href="${pageContext.request.contextPath}/logout"
			style="display: block; text-align: center; padding: 10px 12px; border-radius: 12px; background: rgba(255, 255, 255, 0.08); color: #fff; text-decoration: none;">
			로그아웃 </a>
	</div>

</aside>