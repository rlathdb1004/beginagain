<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<c:set var="user" value="${myPageUser}" />
<c:set var="hasPwdError" value="${not empty sessionScope.pwdErrorMsg}" />
<c:set var="pwdErrorText" value="${sessionScope.pwdErrorMsg}" />

<style>
.my-page-wrap {
	max-width: 1100px;
}

.my-page-header {
	margin-bottom: 18px;
}

.my-page-header h2 {
	margin: 0;
	font-size: 22px;
	font-weight: 800;
	color: #0f2747;
}

.my-page-header p {
	margin: 6px 0 0;
	color: #6b778c;
	font-size: 14px;
}

.my-page-card {
	background: #fff;
	border: 1px solid #dbe4f0;
	border-radius: 18px;
	overflow: hidden;
	box-shadow: 0 8px 24px rgba(15, 39, 71, 0.06);
}

.my-page-toolbar {
	display: flex;
	justify-content: flex-end;
	gap: 10px;
	padding: 18px 20px;
	background: #f7faff;
	border-bottom: 1px solid #e5edf7;
}

.mypage-btn {
	min-width: 120px;
	height: 42px;
	border: none;
	border-radius: 10px;
	font-size: 14px;
	font-weight: 700;
	cursor: pointer;
}

.mypage-btn.primary {
	background: #0c57c7;
	color: #fff;
}

.mypage-btn.secondary {
	background: #eaf1fb;
	color: #0f2747;
}

.my-page-table {
	width: 100%;
	border-collapse: collapse;
}

.my-page-table th, .my-page-table td {
	border-bottom: 1px solid #edf2f8;
	padding: 18px 20px;
	vertical-align: middle;
}

.my-page-table tr:last-child th, .my-page-table tr:last-child td {
	border-bottom: none;
}

.my-page-table th {
	width: 180px;
	background: #f6f9fe;
	color: #0f2747;
	font-weight: 800;
}

.my-page-value {
	color: #18293f;
	font-weight: 600;
}

.my-page-input, .my-page-textarea, .pwd-input {
	width: 100%;
	box-sizing: border-box;
	border: 1px solid #c8d5e6;
	border-radius: 10px;
	padding: 11px 14px;
	font-size: 14px;
	outline: none;
}

.my-page-input:focus, .my-page-textarea:focus, .pwd-input:focus {
	border-color: #0c57c7;
	box-shadow: 0 0 0 3px rgba(12, 87, 199, 0.12);
}

.my-page-textarea {
	min-height: 110px;
	resize: vertical;
}

.my-page-input[readonly], .my-page-textarea[readonly] {
	background: #f7faff;
	color: #4a5870;
	cursor: default;
}

.my-page-input.editable, .my-page-textarea.editable {
	background: #fff;
	color: #18293f;
}

.flash-msg {
	margin: 0 0 14px;
	padding: 12px 14px;
	border-radius: 12px;
	font-size: 14px;
	font-weight: 700;
}

.flash-msg.success {
	background: #edf7ed;
	color: #1f7a33;
	border: 1px solid #cfe8d2;
}

.flash-msg.error {
	background: #fff1f1;
	color: #cc2f2f;
	border: 1px solid #f2caca;
}

.pwd-modal-overlay {
	position: fixed;
	inset: 0;
	display: none;
	align-items: center;
	justify-content: center;
	background: rgba(15, 23, 42, 0.45);
	z-index: 9999;
	padding: 20px;
	box-sizing: border-box;
}

.pwd-modal {
	width: 100%;
	max-width: 560px;
	background: #fff;
	border-radius: 18px;
	box-shadow: 0 18px 48px rgba(15, 23, 42, 0.25);
	overflow: hidden;
}

.pwd-modal-header {
	display: flex;
	justify-content: space-between;
	align-items: center;
	padding: 18px 22px;
	border-bottom: 1px solid #e7eef7;
}

.pwd-modal-header h3 {
	margin: 0;
	color: #0f2747;
	font-size: 20px;
	font-weight: 800;
}

.pwd-close-btn {
	border: none;
	background: transparent;
	font-size: 28px;
	line-height: 1;
	cursor: pointer;
	color: #4a5870;
}

.pwd-modal-body {
	padding: 20px 22px 22px;
}

.pwd-row {
	margin-bottom: 16px;
}

.pwd-row label {
	display: block;
	margin-bottom: 8px;
	font-weight: 800;
	color: #0f2747;
}

.pwd-guide {
	display: none;
	margin-top: 10px;
	padding: 12px 14px;
	background: #fafcff;
	border: 1px solid #e7eef7;
	border-radius: 12px;
}

.pwd-guide-item {
	margin: 4px 0;
	font-size: 13px;
	font-weight: 700;
}

.pwd-guide-item.invalid, .pwd-confirm.invalid, .pwd-server-error {
	color: #d93025;
}

.pwd-guide-item.valid, .pwd-confirm.valid {
	color: #0c57c7;
}

.pwd-confirm {
	min-height: 18px;
	margin-top: 8px;
	font-size: 13px;
	font-weight: 700;
}

.pwd-server-error {
	margin-bottom: 14px;
	padding: 11px 12px;
	border-radius: 10px;
	background: #fff3f3;
	border: 1px solid #f0cccc;
	font-size: 14px;
	font-weight: 700;
}

.pwd-modal-footer {
	display: flex;
	justify-content: flex-end;
	gap: 10px;
	margin-top: 22px;
}

@media ( max-width : 900px) {
	.my-page-table th, .my-page-table td {
		display: block;
		width: 100%;
		box-sizing: border-box;
	}
	.my-page-table th {
		border-bottom: none;
		padding-bottom: 8px;
	}
	.my-page-table td {
		padding-top: 0;
	}
	.my-page-toolbar {
		flex-direction: column-reverse;
		align-items: stretch;
	}
	.mypage-btn {
		width: 100%;
	}
}
</style>

<div class="my-page-wrap">

	<c:if test="${not empty sessionScope.successMsg}">
		<div class="flash-msg success">${sessionScope.successMsg}</div>
		<c:remove var="successMsg" scope="session" />
	</c:if>
	<c:if test="${not empty sessionScope.errorMsg}">
		<div class="flash-msg error">${sessionScope.errorMsg}</div>
		<c:remove var="errorMsg" scope="session" />
	</c:if>

	<form action="${pageContext.request.contextPath}/mypage/update"
		method="post" id="myPageForm">
		<div class="my-page-card">
			<div class="my-page-toolbar">
				<button type="button" class="mypage-btn secondary"
					id="openPwdModalBtn">비밀번호 변경</button>
				<button type="button" class="mypage-btn secondary"
					id="cancelEditBtn" style="display: none;">취소</button>
				<button type="button" class="mypage-btn primary" id="editBtn">수정</button>
				<button type="submit" class="mypage-btn primary" id="saveBtn"
					style="display: none;">저장</button>
			</div>

			<table class="my-page-table">
				<tbody>
					<tr>
						<th>사번</th>
						<td><div class="my-page-value">${user.empNo}</div></td>
					</tr>
					<tr>
						<th>이름</th>
						<td><div class="my-page-value">${user.empName}</div></td>
					</tr>
					<tr>
						<th>부서</th>
						<td><div class="my-page-value">${user.deptCode}</div></td>
					</tr>
					<tr>
						<th>직급</th>
						<td><div class="my-page-value">${user.positionName}</div></td>
					</tr>
					<tr>
						<th>권한</th>
						<td>
							<div class="my-page-value">
								<c:choose>
									<c:when test="${user.roleName eq 'CEO'}">ceo</c:when>
									<c:when test="${user.roleName eq 'MES_ADMIN'}">mes관리자</c:when>
									<c:when test="${user.roleName eq 'SITE_MANAGER'}">현장관리자</c:when>
									<c:when test="${user.roleName eq 'WORKER'}">작업자</c:when>
									<c:otherwise>${user.roleName}</c:otherwise>
								</c:choose>
							</div>
						</td>
					</tr>
					<tr>
						<th>상태</th>
						<td><div class="my-page-value">${user.status}</div></td>
					</tr>
					<tr>
						<th>이메일</th>
						<td><input type="text" class="my-page-input editable-field"
							id="myPageEmail" name="email" value="${user.email}"
							maxlength="100" placeholder="example@mes.com" readonly></td>
					</tr>
					<tr>
						<th>전화번호</th>
						<td><input type="text" class="my-page-input editable-field"
							id="myPagePhone" name="phone" value="${user.phone}"
							maxlength="13" placeholder="010-0000-0000" readonly></td>
					</tr>
					<tr>
						<th>수정일시</th>
						<td><c:choose>
								<c:when test="${not empty user.updatedAt}">
									<fmt:timeZone value="Asia/Seoul">
										<fmt:formatDate value="${user.updatedAt}"
											pattern="yyyy-MM-dd HH:mm:ss" />
									</fmt:timeZone>
								</c:when>
								<c:otherwise>-</c:otherwise>
							</c:choose></td>
					</tr>
					<tr>
						<th>비고</th>
						<td><textarea class="my-page-textarea editable-field"
								id="myPageRemark" name="remark" maxlength="500"
								placeholder="비고를 입력하세요." readonly>${user.remark}</textarea></td>
					</tr>
				</tbody>
			</table>
		</div>
	</form>
</div>

<div class="pwd-modal-overlay" id="pwdModal">
	<div class="pwd-modal">
		<div class="pwd-modal-header">
			<h3>비밀번호 변경</h3>
			<button type="button" class="pwd-close-btn" id="closePwdModalBtn">&times;</button>
		</div>

		<div class="pwd-modal-body">
			<c:if test="${not empty sessionScope.pwdErrorMsg}">
				<div class="pwd-server-error" id="pwdServerError">${pwdErrorText}</div>
				<c:remove var="pwdErrorMsg" scope="session" />
			</c:if>

			<form action="${pageContext.request.contextPath}/changePassword"
				method="post" id="pwdForm">
				<input type="hidden" name="returnUrl"
					value="${pageContext.request.contextPath}/mypage">

				<div class="pwd-row">
					<label for="currentPassword">현재 비밀번호</label> <input type="password"
						class="pwd-input" id="currentPassword" name="currentPassword"
						maxlength="20" autocomplete="current-password" required>
				</div>

				<div class="pwd-row">
					<label for="newPassword">새 비밀번호</label> <input type="password"
						class="pwd-input" id="newPassword" name="newPassword"
						maxlength="20" autocomplete="new-password" required>
					<div class="pwd-guide" id="pwdGuide">
						<div id="ruleLength" class="pwd-guide-item invalid">• 8~20자
							이내로 입력해주세요.</div>
						<div id="ruleMix" class="pwd-guide-item invalid">• 영문
							대문자/소문자/숫자/특수문자 중 3종 이상 조합해주세요.</div>
						<div id="ruleSpace" class="pwd-guide-item invalid">• 공백은 사용할
							수 없습니다.</div>
						<div id="ruleSame" class="pwd-guide-item invalid">• 현재 비밀번호와
							동일하게 설정할 수 없습니다.</div>
						<div id="ruleEmpNo" class="pwd-guide-item invalid">• 사번을 포함할
							수 없습니다.</div>
					</div>
				</div>

				<div class="pwd-row">
					<label for="confirmPassword">새 비밀번호 확인</label> <input
						type="password" class="pwd-input" id="confirmPassword"
						name="confirmPassword" maxlength="20" autocomplete="new-password"
						required>
					<div class="pwd-confirm invalid" id="confirmMsg"></div>
				</div>

				<div class="pwd-modal-footer">
					<button type="button" class="mypage-btn secondary"
						id="cancelPwdModalBtn">취소</button>
					<button type="submit" class="mypage-btn primary">변경</button>
				</div>
			</form>
		</div>
	</div>
</div>


<script>
	(function() {
		var myPageForm = document.getElementById('myPageForm');
		var editBtn = document.getElementById('editBtn');
		var saveBtn = document.getElementById('saveBtn');
		var cancelEditBtn = document.getElementById('cancelEditBtn');
		var editableFields = myPageForm.querySelectorAll('.editable-field');
		var originalValues = {};

		function captureOriginalValues() {
			for (var i = 0; i < editableFields.length; i++) {
				var field = editableFields[i];
				originalValues[field.name] = field.value;
			}
		}

		function setEditMode(editing) {
			for (var i = 0; i < editableFields.length; i++) {
				var field = editableFields[i];
				field.readOnly = !editing;
				if (editing) {
					field.classList.add('editable');
				} else {
					field.classList.remove('editable');
				}
			}

			editBtn.style.display = editing ? 'none' : 'inline-block';
			saveBtn.style.display = editing ? 'inline-block' : 'none';
			cancelEditBtn.style.display = editing ? 'inline-block' : 'none';
		}

		function restoreOriginalValues() {
			for (var i = 0; i < editableFields.length; i++) {
				var field = editableFields[i];
				field.value = originalValues[field.name] || '';
			}
		}

		editBtn.addEventListener('click', function() {
			setEditMode(true);
			if (editableFields.length > 0) {
				editableFields[0].focus();
			}
		});

		cancelEditBtn.addEventListener('click', function() {
			restoreOriginalValues();
			setEditMode(false);
		});

		captureOriginalValues();
		setEditMode(false);
	})();
</script>

<script>
	(function() {
		var pwdModal = document.getElementById('pwdModal');
		var openPwdModalBtn = document.getElementById('openPwdModalBtn');
		var closePwdModalBtn = document.getElementById('closePwdModalBtn');
		var cancelPwdModalBtn = document.getElementById('cancelPwdModalBtn');
		var pwdForm = document.getElementById('pwdForm');

		var currentPassword = document.getElementById('currentPassword');
		var newPassword = document.getElementById('newPassword');
		var confirmPassword = document.getElementById('confirmPassword');
		var pwdGuide = document.getElementById('pwdGuide');
		var confirmMsg = document.getElementById('confirmMsg');

		var ruleLength = document.getElementById('ruleLength');
		var ruleMix = document.getElementById('ruleMix');
		var ruleSpace = document.getElementById('ruleSpace');
		var ruleSame = document.getElementById('ruleSame');
		var ruleEmpNo = document.getElementById('ruleEmpNo');

		var empNo = '${user.empNo}';
		var hasPwdError = ${hasPwdError ? 'true' : 'false'};

		function openModal() {
			pwdModal.style.display = 'flex';
			document.body.style.overflow = 'hidden';
			if (currentPassword) {
				currentPassword.focus();
			}
		}

		function closeModal() {
			pwdModal.style.display = 'none';
			document.body.style.overflow = '';
			pwdForm.reset();
			pwdGuide.style.display = 'none';
			confirmMsg.textContent = '';
			confirmMsg.className = 'pwd-confirm invalid';
			resetRules();
		}

		function resetRules() {
			setRule(ruleLength, false);
			setRule(ruleMix, false);
			setRule(ruleSpace, false);
			setRule(ruleSame, false);
			setRule(ruleEmpNo, false);
		}

		function setRule(target, valid) {
			target.className = 'pwd-guide-item '
					+ (valid ? 'valid' : 'invalid');
		}

		function countTypes(value) {
			var count = 0;
			if (/[A-Z]/.test(value))
				count++;
			if (/[a-z]/.test(value))
				count++;
			if (/[0-9]/.test(value))
				count++;
			if (/[^A-Za-z0-9]/.test(value))
				count++;
			return count;
		}

		function validatePasswordRules() {
			var value = newPassword.value;
			var currentValue = currentPassword.value;

			var lengthValid = value.length >= 8 && value.length <= 20;
			var mixValid = countTypes(value) >= 3;
			var spaceValid = !/\s/.test(value);
			var sameValid = value !== '' && value !== currentValue;
			var empNoValid = value !== ''
					&& value.toLowerCase().indexOf(String(empNo).toLowerCase()) === -1;

			setRule(ruleLength, lengthValid);
			setRule(ruleMix, mixValid);
			setRule(ruleSpace, spaceValid);
			setRule(ruleSame, sameValid);
			setRule(ruleEmpNo, empNoValid);

			return lengthValid && mixValid && spaceValid && sameValid
					&& empNoValid;
		}

		function validateConfirm() {
			if (confirmPassword.value === '') {
				confirmMsg.textContent = '';
				confirmMsg.className = 'pwd-confirm invalid';
				return false;
			}

			var matched = newPassword.value === confirmPassword.value;
			confirmMsg.textContent = matched ? '새 비밀번호가 일치합니다.'
					: '새 비밀번호가 일치하지 않습니다.';
			confirmMsg.className = 'pwd-confirm '
					+ (matched ? 'valid' : 'invalid');
			return matched;
		}

		openPwdModalBtn.addEventListener('click', openModal);
		closePwdModalBtn.addEventListener('click', closeModal);
		cancelPwdModalBtn.addEventListener('click', closeModal);

		pwdModal.addEventListener('click', function(e) {
			if (e.target === pwdModal) {
				closeModal();
			}
		});

		newPassword.addEventListener('focus', function() {
			pwdGuide.style.display = 'block';
			validatePasswordRules();
		});

		currentPassword.addEventListener('input', validatePasswordRules);
		newPassword.addEventListener('input', function() {
			validatePasswordRules();
			validateConfirm();
		});
		confirmPassword.addEventListener('input', validateConfirm);

		pwdForm.addEventListener('submit', function(e) {
			var rulesValid = validatePasswordRules();
			var confirmValid = validateConfirm();

			if (!rulesValid || !confirmValid) {
				e.preventDefault();
				alert('비밀번호 규칙을 확인해주세요.');
			}
		});

		if (hasPwdError) {
			openModal();
			pwdGuide.style.display = 'block';
		}
	})();
</script>
