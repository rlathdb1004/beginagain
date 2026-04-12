<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>공통테이블화면</title>

<!-- 공통 테이블 CSS 연결 -->
<link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/table.css">
</head>
<body class="taBody">

  <section
    id="${empty pageId ? 'page-common-table' : pageId}"
    class="page-section active"
    data-title="${empty pageTitle ? '공통 테이블 화면' : pageTitle}"
    data-subtitle="${empty pageSubTitle ? '공통 테이블 화면입니다.' : pageSubTitle}"
  >
    <div class="taMaterialsInoutOnly">

      <!-- 여기 한 군데에 페이지 JSP 하나만 끼워 넣는다 -->
      <jsp:include page="${contentPage}" />

    </div>
  </section>

</body>
</html>