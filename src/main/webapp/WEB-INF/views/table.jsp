<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>${pageTitle}</title>

    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/common.css">
	<link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/table.css">
	<link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/modal.css">
	<script src="${pageContext.request.contextPath}/assets/js/layout.js"></script>

</head>
<body>
    <div class="app">

        <jsp:include page="/WEB-INF/views/common/sidebar.jsp" />

        <main class="main">
            <section class="global-topbar">
                <div class="global-box">
                    <div class="global-title" id="pageMainTitle">${pageTitle}</div>
                    <div class="global-sub" id="pageSubTitle">${pageSubTitle}</div>
                </div>

                <div class="global-clock">
                    <div class="value" id="liveCalendar"></div>
                    <div class="value" id="liveClock"></div>
                </div>
            </section>

            <div id="content-area" class="page-wrap">
                <jsp:include page="${contentPage}" />
            </div>
        </main>
    </div>
</body>
</html>