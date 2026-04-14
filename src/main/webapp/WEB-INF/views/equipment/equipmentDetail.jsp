<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:choose><c:when test="${empty equipment}"><div class="taFormShell taEmptyState"><p>조회된 설비 정보가 없습니다.</p><div class="taPageActions" style="justify-content:center; margin-top:16px;"><a class="taBtn taBtnOutline" href="${pageContext.request.contextPath}/equipment/list" style="text-decoration:none;">목록</a></div></div></c:when><c:otherwise>
<form action="${pageContext.request.contextPath}/equipment/update" method="post"><input type="hidden" name="equipmentId" value="${equipment.equipmentId}">
<div class="taPageActions"><button type="submit" class="taBtn taBtnPrimary">수정</button><a class="taBtn taBtnOutline" href="${pageContext.request.contextPath}/equipment/list" style="text-decoration:none;">목록</a></div>
<div class="taFormShell"><table class="taFormTable">
<tr><th class="taFormLabel">설비번호</th><td class="taFormValue"><span class="taReadonlyText">${equipment.equipmentId}</span></td></tr>
<tr><th class="taFormLabel">설비코드</th><td class="taFormValue"><span class="taReadonlyText">${equipment.equipmentCode}</span></td></tr>
<tr><th class="taFormLabel">설비명</th><td class="taFormValue"><input class="taFormInput" type="text" name="equipmentName" value="${equipment.equipmentName}"></td></tr>
<tr><th class="taFormLabel">모델명</th><td class="taFormValue"><input class="taFormInput" type="text" name="modelName" value="${equipment.modelName}"></td></tr>
<tr><th class="taFormLabel">위치</th><td class="taFormValue"><input class="taFormInput" type="text" name="location" value="${equipment.location}"></td></tr>
<tr><th class="taFormLabel">제조사</th><td class="taFormValue"><input class="taFormInput" type="text" name="manufacturer" value="${equipment.manufacturer}"></td></tr>
<tr><th class="taFormLabel">공급업체</th><td class="taFormValue"><input class="taFormInput" type="text" name="vendorName" value="${equipment.vendorName}"></td></tr>
<tr><th class="taFormLabel">설비가격</th><td class="taFormValue"><input class="taFormInput" type="number" name="equipmentPrice" value="${equipment.equipmentPrice}"></td></tr>
<tr><th class="taFormLabel">구매일자</th><td class="taFormValue"><input class="taFormInput" type="date" name="purchaseDate" value="${equipment.purchaseDate}"></td></tr>
<tr><th class="taFormLabel">비고</th><td class="taFormValue"><textarea class="taFormTextarea" name="remark">${equipment.remark}</textarea></td></tr>
</table></div></form></c:otherwise></c:choose>
