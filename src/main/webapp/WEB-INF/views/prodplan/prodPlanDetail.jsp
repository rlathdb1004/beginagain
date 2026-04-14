<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:if test="${not empty sessionScope.flashMessage}">
    <script>
        alert('${sessionScope.flashMessage}');
    </script>
    <c:remove var="flashMessage" scope="session"/>
</c:if>
<c:choose>
    <c:when test="${empty detail}">
        <div class="taFormShell taEmptyState">
            <p>조회된 생산계획 정보가 없습니다.</p>
            <div class="taPageActions" style="justify-content:center; margin-top:16px;">
                <a class="taBtn taBtnOutline" href="${pageContext.request.contextPath}/prodplan">목록</a>
            </div>
        </div>
    </c:when>
    <c:otherwise>
        <div class="taSectionStack">
            <form action="${pageContext.request.contextPath}/prodplan" method="post">
                <input type="hidden" name="cmd" value="update">
                <input type="hidden" name="planId" value="${detail.seqNO}">
                <div class="taPageActions">
                    <button type="submit" class="taBtn taBtnPrimary">수정</button>
                    <a class="taBtn taBtnOutline" href="${pageContext.request.contextPath}/prodplan">목록</a>
                </div>
                <div class="taFormShell">
                    <table class="taFormTable">
                        <tr>
                            <th class="taFormLabel">생산계획 ID</th>
                            <td class="taFormValue"><span class="taReadonlyText">${detail.seqNO}</span></td>
                        </tr>
                        <tr>
                            <th class="taFormLabel">생산계획번호</th>
                            <td class="taFormValue"><span class="taReadonlyText">${detail.planNo}</span></td>
                        </tr>
                        <tr>
                            <th class="taFormLabel">품목코드</th>
                            <td class="taFormValue">
                                <span class="taReadonlyText">${detail.planCode}</span>
                                <input type="hidden" name="planCode" value="${detail.planCode}">
                            </td>
                        </tr>
                        <tr>
                            <th class="taFormLabel">품목명</th>
                            <td class="taFormValue"><span class="taReadonlyText">${detail.planName}</span></td>
                        </tr>
                        <tr>
                            <th class="taFormLabel">계획일자</th>
                            <td class="taFormValue"><input class="taFormInput" type="date" name="planDate" value="${detail.planDate}" required></td>
                        </tr>
                        <tr>
                            <th class="taFormLabel">생산계획량</th>
                            <td class="taFormValue"><input class="taFormInput" type="number" name="planAmount" value="${detail.planAmount}" required></td>
                        </tr>
                        <tr>
                            <th class="taFormLabel">단위</th>
                            <td class="taFormValue"><span class="taReadonlyText">${detail.planUnit}</span></td>
                        </tr>
                        <tr>
                            <th class="taFormLabel">라인</th>
                            <td class="taFormValue"><input class="taFormInput" type="text" name="planLine" value="${detail.planLine}" required></td>
                        </tr>
                        <tr>
                            <th class="taFormLabel">비고</th>
                            <td class="taFormValue"><textarea class="taFormTextarea" name="memo">${detail.memo}</textarea></td>
                        </tr>
                    </table>
                </div>
            </form>
            <form action="${pageContext.request.contextPath}/prodplan" method="post" onsubmit="return confirm('이 생산계획을 삭제하시겠습니까?');">
                <input type="hidden" name="cmd" value="deleteOne">
                <input type="hidden" name="planId" value="${detail.seqNO}">
                <div class="taPageActions" style="justify-content:flex-end; margin-top:-8px;">
                    <button type="submit" class="taBtn taBtnOutline">삭제</button>
                </div>
            </form>
        </div>
    </c:otherwise>
</c:choose>
