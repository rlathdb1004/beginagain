<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:set var="pageTitle" value="대시보드" />
<c:set var="pageSubTitle" value="MES 시스템 메인 화면" />

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>MES 메인</title>
<link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/common.css" />
<link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/table.css" />
<script src="${pageContext.request.contextPath}/assets/js/layout.js"></script>
</head>
<body>

<div class="app">
    <jsp:include page="/WEB-INF/views/common/sidebar.jsp" />

    <div class="main">
        <jsp:include page="/WEB-INF/views/common/topbar.jsp" />

        <section class="page-wrap">
            <div class="page-card">
                <div class="dashboard-grid">
                    <div class="stat-card">
                        <div class="stat-title">품목 수</div>
                        <div class="stat-value">${dashboard.itemCount}</div>
                    </div>

                    <div class="stat-card">
                        <div class="stat-title">설비 수</div>
                        <div class="stat-value">${dashboard.equipmentCount}</div>
                    </div>

                    <div class="stat-card">
                        <div class="stat-title">공정 수</div>
                        <div class="stat-value">${dashboard.processCount}</div>
                    </div>

                    <div class="stat-card">
                        <div class="stat-title">작업지시 수</div>
                        <div class="stat-value">${dashboard.workOrderCount}</div>
                    </div>

                    <div class="stat-card">
                        <div class="stat-title">사원 수</div>
                        <div class="stat-value">${dashboard.memberCount}</div>
                    </div>
                </div>
            </div>

            <div class="page-card">
                <h3>대시보드 개발중</h3>
                <p>좌측 메뉴를 통해 각 관리 페이지로 이동</p>
            </div>

            <div class="page-card">
                <h3>로그인 정보</h3>
                <p>이름: ${loginUser.empName}</p>
                <p>사번: ${loginUser.empNo}</p>
                <p>권한: ${loginUser.roleName}</p>
            </div>
        </section>
    </div>
</div>

</body>
</html>