<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<c:if test="${not empty notice}">
    <form action="${pageContext.request.contextPath}/notice/update" method="post">
        <input type="hidden" name="noticeId" value="${notice.noticeId}">

        <div class="taPageActions">
            <button type="submit" class="taBtn taBtnPrimary">수정</button>
            <a href="${pageContext.request.contextPath}/notice/list" class="taBtn taBtnOutline" style="text-decoration:none; display:inline-flex; align-items:center;">목록</a>
        </div>

        <div class="taFormShell">
            <table class="taFormTable">
                <tr>
                    <th class="taFormLabel">ID</th>
                    <td class="taFormValue">${notice.noticeId}</td>
                </tr>
                <tr>
                    <th class="taFormLabel">작성자</th>
                    <td class="taFormValue">${notice.writerEmpName}</td>
                </tr>
                <tr>
                    <th class="taFormLabel">제목</th>
                    <td class="taFormValue"><input class="taFormInput" type="text" name="title" value="${notice.title}"></td>
                </tr>
                <tr>
                    <th class="taFormLabel">내용</th>
                    <td class="taFormValue"><textarea class="taFormTextarea" name="content">${notice.content}</textarea></td>
                </tr>
                <tr>
                    <th class="taFormLabel">상태</th>
                    <td class="taFormValue"><input class="taFormInput" type="text" name="status" value="${notice.status}"></td>
                </tr>
                <tr>
                    <th class="taFormLabel">비고</th>
                    <td class="taFormValue"><input class="taFormInput" type="text" name="remark" value="${notice.remark}"></td>
                </tr>
            </table>
        </div>
    </form>
</c:if>
