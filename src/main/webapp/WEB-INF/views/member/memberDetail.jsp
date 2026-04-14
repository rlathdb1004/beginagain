<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:choose><c:when test="${empty member}"><div class="taFormShell taEmptyState"><p>조회된 사원 정보가 없습니다.</p><div class="taPageActions" style="justify-content:center; margin-top:16px;"><a class="taBtn taBtnOutline" href="${pageContext.request.contextPath}/member/list" style="text-decoration:none;">목록</a></div></div></c:when><c:otherwise>
<form action="${pageContext.request.contextPath}/member/update" method="post"><input type="hidden" name="empId" value="${member.empId}"><div class="taPageActions"><button type="submit" class="taBtn taBtnPrimary">수정</button><a class="taBtn taBtnOutline" href="${pageContext.request.contextPath}/member/list" style="text-decoration:none;">목록</a></div><div class="taFormShell"><table class="taFormTable">
<tr><th class="taFormLabel">ID</th><td class="taFormValue"><span class="taReadonlyText">${member.empId}</span></td></tr>
<tr><th class="taFormLabel">사번</th><td class="taFormValue"><span class="taReadonlyText">${member.empNo}</span></td></tr>
<tr><th class="taFormLabel">이름</th><td class="taFormValue"><input class="taFormInput" type="text" name="empName" value="${member.empName}"></td></tr>
<tr><th class="taFormLabel">부서코드</th><td class="taFormValue"><input class="taFormInput" type="text" name="deptCode" value="${member.deptCode}"></td></tr>
<tr><th class="taFormLabel">직급</th><td class="taFormValue"><input class="taFormInput" type="text" name="positionName" value="${member.positionName}"></td></tr>
<tr><th class="taFormLabel">이메일</th><td class="taFormValue"><input class="taFormInput" type="text" name="email" value="${member.email}"></td></tr>
<tr><th class="taFormLabel">전화번호</th><td class="taFormValue"><input class="taFormInput" type="text" name="phone" value="${member.phone}"></td></tr>
<tr><th class="taFormLabel">상태</th><td class="taFormValue"><input class="taFormInput" type="text" name="status" value="${member.status}"></td></tr>
<tr><th class="taFormLabel">권한</th><td class="taFormValue"><input class="taFormInput" type="text" name="roleName" value="${member.roleName}"></td></tr>
<tr><th class="taFormLabel">임시비밀번호 여부</th><td class="taFormValue"><input class="taFormInput" type="text" name="tempPwdYn" value="${member.tempPwdYn}"></td></tr>
<tr><th class="taFormLabel">비고</th><td class="taFormValue"><textarea class="taFormTextarea" name="remark">${member.remark}</textarea></td></tr>
</table></div></form></c:otherwise></c:choose>
