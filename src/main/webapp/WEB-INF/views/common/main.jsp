<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<div class="taSectionStack">
  <div class="taDashboardGrid">
    <div class="taDashboardCard"><div class="taDashboardLabel">품목 수</div><div class="taDashboardValue">${dashboard.itemCount}</div></div>
    <div class="taDashboardCard"><div class="taDashboardLabel">설비 수</div><div class="taDashboardValue">${dashboard.equipmentCount}</div></div>
    <div class="taDashboardCard"><div class="taDashboardLabel">공정 수</div><div class="taDashboardValue">${dashboard.processCount}</div></div>
    <div class="taDashboardCard"><div class="taDashboardLabel">작업지시 수</div><div class="taDashboardValue">${dashboard.workOrderCount}</div></div>
    <div class="taDashboardCard"><div class="taDashboardLabel">사원 수</div><div class="taDashboardValue">${dashboard.memberCount}</div></div>
  </div>
  <div class="taFormShell taEmptyState"><strong>대시보드 개발중</strong><div style="margin-top:8px;">좌측 메뉴를 통해 각 관리 페이지로 이동하세요.</div></div>
  <div class="taFormShell" style="padding:24px;"><div class="taSectionTitle" style="margin-top:0;">로그인 정보</div><div class="taInfoList"><div><strong>이름:</strong> ${loginUser.empName}</div><div><strong>사번:</strong> ${loginUser.empNo}</div><div><strong>권한:</strong> ${loginUser.roleName}</div></div></div>
</div>
