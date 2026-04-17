<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>비밀번호 변경</title>
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/assets/css/common.css" />
<style>
body {
	display: grid;
	place-items: center;
	min-height: 100vh
}
.login-card {
	width: 460px;
	background: var(--panel);
	padding: 28px;
	border-radius: 24px;
	box-shadow: var(--shadow);
	border: 1px solid var(--line-soft)
}
.field {
	margin-bottom: 12px
}
.field input {
	width: 100%;
	height: 44px;
	border: 1px solid var(--line);
	border-radius: 12px;
	padding: 0 12px
}
.error {
	color: var(--danger);
	margin-bottom: 10px
}
.primary-btn {
	width: 100%;
	background: var(--main);
	color: #fff
}
.pwd-guide {
    display:none;
    margin-top:8px;
    font-size:13px;
    line-height:1.6;
}
.invalid { color:#e53935; }
.valid { color:#1e88e5; }
</style>
</head>
<body>
	<div class="login-card">
		<h2 style="margin-top: 0">최초 로그인 비밀번호 변경</h2>
		<p style="color: var(--text-soft)">임시 비밀번호를 변경해야 합니다.</p>
		<c:if test="${not empty pwdErrorMsg}">
			<div class="error">${pwdErrorMsg}</div>
			<c:remove var="pwdErrorMsg" scope="session" />
		</c:if>
		<form action="${pageContext.request.contextPath}/changePassword" method="post" id="initChangePasswordForm">
			<div class="field">
				<input type="password" name="newPassword" id="newPassword" placeholder="새 비밀번호" required>
                <div class="pwd-guide" id="pwdGuide">
                    <div id="ruleLength" class="invalid">• 8~20자 이내</div>
                    <div id="ruleMix" class="invalid">• 영문 대/소문자, 숫자, 특수문자 중 3종 이상 조합</div>
                    <div id="ruleSpace" class="invalid">• 공백 사용 불가</div>
                    <div id="ruleEmpNo" class="invalid">• 사번 포함 불가</div>
                </div>
			</div>
			<div class="field">
				<input type="password" name="confirmPassword" id="confirmPassword" placeholder="새 비밀번호 확인" required>
                <div id="confirmMsg" class="invalid" style="margin-top:8px;"></div>
			</div>
			<button class="btn primary-btn" type="submit">변경</button>
		</form>
	</div>
<script>
(function() {
    const form = document.getElementById('initChangePasswordForm');
    const newPassword = document.getElementById('newPassword');
    const confirmPassword = document.getElementById('confirmPassword');
    const pwdGuide = document.getElementById('pwdGuide');
    const ruleLength = document.getElementById('ruleLength');
    const ruleMix = document.getElementById('ruleMix');
    const ruleSpace = document.getElementById('ruleSpace');
    const ruleEmpNo = document.getElementById('ruleEmpNo');
    const confirmMsg = document.getElementById('confirmMsg');
    const empNo = '${sessionScope.loginUser.empNo}';

    function setRule(el, valid) {
        el.classList.remove('valid', 'invalid');
        el.classList.add(valid ? 'valid' : 'invalid');
    }

    function countTypes(value) {
        let count = 0;
        if (/[A-Z]/.test(value)) count++;
        if (/[a-z]/.test(value)) count++;
        if (/[0-9]/.test(value)) count++;
        if (/[^A-Za-z0-9]/.test(value)) count++;
        return count;
    }

    function validatePassword() {
        const value = newPassword.value;
        const lengthValid = value.length >= 8 && value.length <= 20;
        const mixValid = countTypes(value) >= 3;
        const spaceValid = !/\s/.test(value);
        const empNoValid = value !== '' && (!empNo || value.toLowerCase().indexOf(empNo.toLowerCase()) === -1);

        setRule(ruleLength, lengthValid);
        setRule(ruleMix, mixValid);
        setRule(ruleSpace, spaceValid);
        setRule(ruleEmpNo, empNoValid);

        return lengthValid && mixValid && spaceValid && empNoValid;
    }

    function validateConfirm() {
        if (confirmPassword.value === '') {
            confirmMsg.textContent = '';
            confirmMsg.className = 'invalid';
            return false;
        }

        const matched = newPassword.value === confirmPassword.value;
        confirmMsg.textContent = matched ? '새 비밀번호가 일치합니다.' : '새 비밀번호가 일치하지 않습니다.';
        confirmMsg.className = matched ? 'valid' : 'invalid';
        return matched;
    }

    newPassword.addEventListener('focus', function() {
        pwdGuide.style.display = 'block';
    });
    newPassword.addEventListener('input', function() {
        validatePassword();
        validateConfirm();
    });
    confirmPassword.addEventListener('input', validateConfirm);

    form.addEventListener('submit', function(e) {
        const pwdValid = validatePassword();
        const confirmValid = validateConfirm();

        if (!pwdValid || !confirmValid) {
            e.preventDefault();
            alert('비밀번호 규칙을 확인해주세요.');
        }
    });
})();
</script>
</body>
</html>
