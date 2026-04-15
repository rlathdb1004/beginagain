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
	<link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/ProdPlanRegInq.css">
	<link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/ProdPerfRegInq.css">
	<link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/WORegInq.css">
	<link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/ceomain.css">
<%-- 	<link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/WorkStatusInq.css"> --%>
	
</head>
<body>
    <div class="app">

        <jsp:include page="/WEB-INF/views/common/sidebar.jsp" />

        <main class="main">
            <jsp:include page="/WEB-INF/views/common/topbar.jsp" />

            <div id="content-area" class="page-wrap">
                <jsp:include page="${contentPage}" />
            </div>
        </main>
    </div>
    <script src="${pageContext.request.contextPath}/assets/js/layout.js?v=4"></script>
    <script src="${pageContext.request.contextPath}/assets/js/common.js?v=4"></script>
</body>
</html>