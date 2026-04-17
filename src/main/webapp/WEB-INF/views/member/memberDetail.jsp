<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:choose>
    <c:when test="${empty member}">
        <div class="taFormShell taEmptyState">
            <p>조회된 사원 정보가 없습니다.</p>
            <div class="taPageActions" style="justify-content:center; margin-top:16px;">
                <a class="taBtn taBtnOutline" href="${pageContext.request.contextPath}/member/list" style="text-decoration:none;">목록</a>
            </div>
        </div>
    </c:when>
    <c:otherwise>
        <form id="memberUpdateForm" action="${pageContext.request.contextPath}/member/update" method="post">
            <input type="hidden" name="empId" value="${member.empId}">
            <div class="taPageActions">
                <button type="button" id="memberUpdateFormEditBtn" class="taBtn taBtnPrimary">수정</button>
                <c:if test="${sessionScope.loginUser.roleName eq '관리자'}">
                    <button type="button" id="memberResetPasswordBtn" class="taBtn taBtnOutline">비밀번호 초기화</button>
                </c:if>
                <button type="submit" id="memberUpdateFormSaveBtn" class="taBtn taBtnPrimary" style="display:none;">수정완료</button>
                <button type="button" id="memberUpdateFormCancelBtn" class="taBtn taBtnOutline" style="display:none;">취소</button>
                <a class="taBtn taBtnOutline" href="${pageContext.request.contextPath}/member/list" style="text-decoration:none;">목록</a>
            </div>
            <c:if test="${param.resetSuccess eq 'Y'}">
                <div class="taFormShell" style="margin-bottom:16px; border:1px solid #d7e3ff; background:#f7faff; padding:16px 24px 16px 18px; box-sizing:border-box;">
                    <div style="font-weight:700; margin-bottom:8px; color:#1f4fa3;">비밀번호 초기화 완료</div>
                    <div style="line-height:1.7; padding-right:8px;">임시 비밀번호: <strong>${param.tempPassword}</strong></div>
                    <div style="margin-top:6px; color:#666; font-size:13px; padding-right:8px;">해당 사원은 다음 로그인 시 새 비밀번호로 반드시 변경해야 합니다.</div>
                </div>
            </c:if>
            <c:if test="${param.resetSuccess eq 'N'}">
                <div class="taFormShell" style="margin-bottom:16px; border:1px solid #ffd7d7; background:#fff8f8; color:#b42318;">
                    비밀번호 초기화에 실패했습니다. 다시 시도해주세요.
                </div>
            </c:if>
            <div class="taFormShell">
                <table class="taFormTable">
                    <tr><th class="taFormLabel">ID</th><td class="taFormValue"><span class="taReadonlyText">${member.empId}</span></td></tr>
                    <tr><th class="taFormLabel">사번</th><td class="taFormValue"><span class="taReadonlyText">${member.empNo}</span></td></tr>
                    <tr><th class="taFormLabel">이름</th><td class="taFormValue"><input class="taFormInput taEditableField" type="text" name="empName" value="${member.empName}"></td></tr>
                    <tr><th class="taFormLabel">부서코드</th><td class="taFormValue"><input class="taFormInput taEditableField" type="text" name="deptCode" value="${member.deptCode}"></td></tr>
                    <tr><th class="taFormLabel">직급</th><td class="taFormValue"><input class="taFormInput taEditableField" type="text" name="positionName" value="${member.positionName}"></td></tr>
                    <tr><th class="taFormLabel">이메일</th><td class="taFormValue"><input class="taFormInput taEditableField" type="text" name="email" value="${member.email}"></td></tr>
                    <tr><th class="taFormLabel">전화번호</th><td class="taFormValue"><input class="taFormInput taEditableField" type="text" name="phone" value="${member.phone}"></td></tr>
                    <tr><th class="taFormLabel">상태</th><td class="taFormValue"><select class="taFormInput taEditableField" name="status"><option value="재직" ${member.status eq '재직' ? 'selected' : ''}>재직</option><option value="휴직" ${member.status eq '휴직' ? 'selected' : ''}>휴직</option><option value="퇴사" ${member.status eq '퇴사' ? 'selected' : ''}>퇴사</option></select></td></tr>
                    <tr><th class="taFormLabel">권한</th><td class="taFormValue"><select class="taFormInput taEditableField" name="roleName"><option value="관리자" ${member.roleName eq '관리자' ? 'selected' : ''}>관리자</option><option value="사원" ${member.roleName eq '사원' ? 'selected' : ''}>사원</option></select></td></tr>
                    <tr><th class="taFormLabel">임시비밀번호 여부</th><td class="taFormValue"><select class="taFormInput taEditableField" name="tempPwdYn"><option value="Y" ${member.tempPwdYn eq 'Y' ? 'selected' : ''}>Y</option><option value="N" ${member.tempPwdYn eq 'N' ? 'selected' : ''}>N</option></select></td></tr>
                    <tr><th class="taFormLabel">비고</th><td class="taFormValue"><textarea class="taFormTextarea taEditableField" name="remark">${member.remark}</textarea></td></tr>
                </table>
            </div>
        </form>

        <c:if test="${sessionScope.loginUser.roleName eq '관리자'}">
            <form id="memberResetPasswordForm" action="${pageContext.request.contextPath}/member/resetPassword" method="post" style="display:none;">
                <input type="hidden" name="empId" value="${member.empId}">
            </form>
        </c:if>
<script>
(function() {
    const form = document.getElementById("memberUpdateForm");
    if (!form) return;

    const editBtn = document.getElementById("memberUpdateFormEditBtn");
    const saveBtn = document.getElementById("memberUpdateFormSaveBtn");
    const cancelBtn = document.getElementById("memberUpdateFormCancelBtn");
    const fields = form.querySelectorAll('.taEditableField');
    const resetBtn = document.getElementById('memberResetPasswordBtn');
    const resetForm = document.getElementById('memberResetPasswordForm');

    function setViewMode() {
        fields.forEach(function(field) {
            if (field.tagName === 'SELECT') {
                field.disabled = true;
            } else {
                field.readOnly = true;
            }
        });
        editBtn.style.display = '';
        saveBtn.style.display = 'none';
        cancelBtn.style.display = 'none';
    }

    function setEditMode() {
        fields.forEach(function(field) {
            if (field.tagName === 'SELECT') {
                field.disabled = false;
            } else {
                field.readOnly = false;
            }
        });
        editBtn.style.display = 'none';
        saveBtn.style.display = '';
        cancelBtn.style.display = '';
    }

    fields.forEach(function(field) {
        field.dataset.originalValue = field.value;
    });

    editBtn.addEventListener('click', function() {
        setEditMode();
    });

    if (resetBtn && resetForm) {
        resetBtn.addEventListener('click', function() {
            if (confirm('해당 사원의 비밀번호를 임시 비밀번호로 초기화하시겠습니까?')) {
                resetForm.submit();
            }
        });
    }

    cancelBtn.addEventListener('click', function() {
        fields.forEach(function(field) {
            field.value = field.dataset.originalValue || '';
        });
        setViewMode();
    });

    form.addEventListener('submit', function(e) {
        if (saveBtn.style.display === 'none') {
            e.preventDefault();
            return;
        }
        fields.forEach(function(field) {
            if (field.tagName === 'SELECT') {
                field.disabled = false;
            }
        });
        if (!confirm('수정하시겠습니까?')) {
            e.preventDefault();
        }
    });

    setViewMode();
})();
</script>
    </c:otherwise>
</c:choose>
