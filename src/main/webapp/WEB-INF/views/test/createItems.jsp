<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">

<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>Document</title>
<style>
@font-face {
	font-family: 'A2z';
	src:
		url('https://cdn.jsdelivr.net/gh/projectnoonnu/2601-6@1.0/에이투지체-4Regular.woff2')
		format('woff2');
	font-weight: 400;
	font-display: swap;
}

@font-face {
	font-family: 'A2z';
	src:
		url('https://cdn.jsdelivr.net/gh/projectnoonnu/2601-6@1.0/에이투지체-5Medium.woff2')
		format('woff2');
	font-weight: 500;
	font-display: swap;
}

@font-face {
	font-family: 'A2z';
	src:
		url('https://cdn.jsdelivr.net/gh/projectnoonnu/2601-6@1.0/에이투지체-7Bold.woff2')
		format('woff2');
	font-weight: 700;
	font-display: swap;
}

@font-face {
	font-family: 'A2z';
	src:
		url('https://cdn.jsdelivr.net/gh/projectnoonnu/2601-6@1.0/에이투지체-8ExtraBold.woff2')
		format('woff2');
	font-weight: 800;
	font-display: swap;
}

/*
    =============================================================
    1. 전역 변수 정의
    -------------------------------------------------------------
    - 사용자 요청 색상값을 CSS 변수로 먼저 선언한다.
    - 이후 전체 UI는 이 변수를 참조하므로 유지보수가 쉬워진다.
    =============================================================
*/
:root { -
	-main: #0A1E3C; -
	-symbol: #0047AB; -
	-point: #FF4500; -
	-bg: #F7F9FC; -
	-panel: #FFFFFF; -
	-panel-soft: #F5F8FC; -
	-line: #D3DCEC; -
	-line-soft: #E2E9F3; -
	-text: #22304A; -
	-text-soft: #6F7B8D; -
	-table-head: #E2EBF8; -
	-table-head-text: #102544; -
	-table-head-border: rgba(10, 30, 60, 0.12); -
	-shadow: 0 16px 36px rgba(10, 30, 60, 0.06); -
	-shadow-soft: 0 10px 24px rgba(10, 30, 60, 0.04); -
	-radius-lg: 22px; -
	-radius-md: 16px; -
	-radius-sm: 12px;
}

/*
    =============================================================
    2. 기본 태그 초기화
    -------------------------------------------------------------
    - 폰트는 에이투지체로 전체 통일
    - 배경색과 기본 글자색도 전역에서 통일
    =============================================================
*/
* {
	box-sizing: border-box;
}

html, body {
	margin: 0;
	padding: 0;
}

body {
	font-family: 'A2z', sans-serif;
	font-weight: 400;
	color: var(- -text);
	background: var(- -bg);
	-webkit-font-smoothing: antialiased;
	text-rendering: optimizeLegibility;
}

button, input, select {
	font-family: 'A2z', sans-serif;
}

.hidden {
	display: none !important;
}

.sr-only {
	position: absolute;
	width: 1px;
	height: 1px;
	overflow: hidden;
	clip: rect(0, 0, 0, 0);
}

.btn {
	height: 44px;
	border-radius: 12px;
	border: none;
	padding: 0 16px;
	font-size: 0.94rem;
	font-weight: 400;
	cursor: pointer;
	transition: transform .15s ease, box-shadow .15s ease, background .15s
		ease, border-color .15s ease;
}

.btn:hover {
	transform: translateY(-1px);
}

.btn-primary {
	background: var(- -main);
	color: #fff;
	box-shadow: 0 10px 22px rgba(10, 30, 60, 0.14);
}

.btn-primary:hover {
	box-shadow: 0 14px 28px rgba(10, 30, 60, 0.18);
	background: #0D244A;
}

.btn-soft {
	background: rgba(0, 71, 171, 0.06);
	color: var(- -main);
	border: 1px solid rgba(0, 71, 171, 0.10);
}

.btn-outline {
	background: #fff;
	color: var(- -text);
	border: 1px solid var(- -line);
}

.btn-ghost {
	background: transparent;
	border: 1px solid rgba(255, 255, 255, 0.18);
	color: #fff;
}

.btn-block {
	width: 100%;
}

.panel, .chart-card, .line-card, .summary-card {
	position: relative;
	padding: 20px;
	color: var(- -text);
	background: #fff;
	border: 1px solid rgba(10, 30, 60, 0.05);
	box-shadow: var(- -shadow-soft);
}

.panel {
	padding: 18px;
}

.split-block {
	display: grid;
	grid-template-columns: 1fr;
	gap: 18px;
}

.panel-head, .section-header-line {
	display: flex;
	justify-content: space-between;
	align-items: center;
	gap: 14px;
	margin-bottom: 14px;
}

.table-wrap {
	overflow: auto;
	border-radius: 18px;
	border: 1px solid rgba(10, 30, 60, 0.10);
	background: #fff;
}

.table-wrap.fixed-height {
	max-height: 340px;
}

.table-wrap.medium {
	max-height: 520px;
}

.table-wrap.tall {
	max-height: 680px;
}

.mes-table {
	width: 100%;
	border-collapse: collapse;
	min-width: 980px;
	table-layout: fixed;
}

.mes-table thead th {
	height: 44px;
	padding: 0 12px;
	font-size: 0.79rem;
	font-weight: 500;
	text-align: left;
	white-space: nowrap;
	overflow: hidden;
	text-overflow: ellipsis;
	background: linear-gradient(180deg, #E8F0FB 0%, #DCE7F6 100%);
	color: var(- -table-head-text);
	border-top: 1px solid rgba(10, 30, 60, 0.08);
	border-bottom: 1px solid rgba(10, 30, 60, 0.12);
	border-right: 1px solid rgba(10, 30, 60, 0.08);
}

.mes-table tbody td {
	height: 42px;
	padding: 0 12px;
	font-size: 0.78rem;
	line-height: 1.2;
	font-weight: 400;
	color: #23314B;
	white-space: nowrap;
	overflow: hidden;
	text-overflow: ellipsis;
	border-right: 1px solid #E0E7F1;
	border-bottom: 1px solid #DCE4EF;
	background: #fff;
}

.mes-table tbody tr td:first-child, .mes-table thead th:first-child {
	border-left: 1px solid #E0E7F1;
}

.mes-table tbody tr:hover td {
	background: #F1F6FC;
}

.mes-table tbody tr.blank td {
	height: 42px;
	background: #fff;
}

.mes-table th:first-child, .mes-table td:first-child {
	width: 56px;
}

.mes-table td.has-tooltip, .mes-table th.has-tooltip {
	cursor: default;
}

.mes-table td.has-tooltip[title] {
	position: relative;
}

.action-modal {
	width: min(720px, 100%);
	background: linear-gradient(180deg, #FBFCFE 0%, #FFFFFF 100%);
	border: 1px solid rgba(10, 30, 60, 0.06);
	border-radius: 26px;
	box-shadow: 0 24px 50px rgba(10, 30, 60, 0.18);
	overflow: hidden;
}

.action-modal .modal-header h3 {
	font-weight: 700;
}

.action-body {
	padding: 22px;
	display: grid;
	grid-template-columns: repeat(2, minmax(0, 1fr));
	gap: 14px;
}

.action-field {
	display: grid;
	gap: 8px;
}

.action-field.wide {
	grid-column: 1/-1;
}

.action-field label {
	font-size: 0.9rem;
	font-weight: 500;
	color: var(- -text-soft);
}

.action-field input, .action-field select, .action-field textarea {
	width: 100%;
	border: 1px solid var(- -line);
	border-radius: 14px;
	background: #fff;
	color: var(- -text);
	padding: 0 14px;
	font-size: 0.94rem;
	font-family: 'A2z', sans-serif;
}

.action-field input, .action-field select {
	height: 46px;
}

.action-field textarea {
	min-height: 108px;
	padding-top: 14px;
	resize: vertical;
}

.action-field input:focus, .action-field select:focus, .action-field textarea:focus
	{
	outline: none;
	border-color: var(- -symbol);
	box-shadow: 0 0 0 4px rgba(0, 71, 171, 0.08);
}

.action-modal .modal-footer {
	gap: 10px;
	justify-content: flex-end;
	background: linear-gradient(180deg, rgba(10, 30, 60, 0.00),
		rgba(10, 30, 60, 0.03));
}
.inputErorrMsg {
    font-size: 0.7rem;
    color: red;
}
</style>
</head>

<body>

	<!-- 
                    =================================================
                    4. 자재관리
                    - 입출고 등록/관리
                    - 입출고 조회
                    - 재고 조회
                    =================================================
                -->
	<section class="page-section" id="page-materials-register"
		data-title="자재관리" data-subtitle="입출고 등록 / 관리">
		<div class="section-title-chip">기준 관리</div>
		<div class="split-block">
			<div class="panel">
				<div class="panel-head">
					<h3>품목 등록</h3>
					<div class="action-row">
						<button class="btn btn-soft">추가</button>
						<button class="btn btn-primary">등록</button>
					</div>
				</div>
				<div class="table-wrap fixed-height">
					<table class="mes-table">
						<thead>
							<tr>
								<th>NO</th>
								<th>분류</th>
								<th>공급처</th>
								<th>품목코드</th>
								<th>품목명</th>
								<th>단위</th>
								<th>등록일자</th>
								<th>비고</th>
							</tr>
						</thead>
						<tbody>
							<tr>
								<td></td>
								<td></td>
								<td></td>
								<td></td>
								<td></td>
								<td></td>
								<td></td>
								<td></td>
							</tr>
						</tbody>
					</table>
				</div>
			</div>
		</div>
	</section>

	<form id="myForm" method="post" action="create">
		<div id="actionModal" class="modal-backdrop hidden">
			<div class="action-modal">
				<div class="modal-header">
					<div>
						<h3 id="actionModalTitle">데이터 등록</h3>
						<p id="actionModalSubtitle">선택한 화면에 맞는 기본 작성 폼입니다.</p>
					</div>
					<button id="closeActionModalBtn" class="modal-close" type="button">×</button>
				</div>

				<div class="action-body">
					<div class="action-field">
						<!-- <label for="modalFieldDate">기준일자</label>
                    <input id="modalFieldDate" type="date" value="2026-04-07" /> -->
						<label for="modalFieldSubject">분류</label> <input
							id="modalFieldSubject" type="text" value="원자재"
							name="subject" readonly>
					</div>
					<div class="action-field">
						<label for="modalFieldSuplier">공급처</label>
						<!-- <div>라인</div> -->
						<input class="mustInput" type="text" id="modalFieldSuplier" name="suplier" value="회사명">
                        <div class="inputErorrMsg hidden">정확히 입력해 주세요.</div>
					</div>
					<div class="action-field">
						<label for="modalFieldCode">코드</label> <input class="mustInput" id="modalFieldCode"
							type="text" name="itemsCode" placeholder="예: RM-LI-001" value="RM-LI-001">
                        <div class="inputErorrMsg hidden">정확히 입력해 주세요.</div>
					</div>
					<div class="action-field">
						<label for="modalFieldName">품목명</label> <input class="mustInput" id="modalFieldName"
							name="itemsName" type="text" placeholder="예: 수산화나트륨" value="수산화나트륨">
                        <div class="inputErorrMsg hidden">정확히 입력해 주세요.</div>
					</div>
					<div class="action-field">
						<label for="modalFieldUnit">단위</label> <input class="mustInput" id="odalFieldUnit"
							name="itemsUnit" type="text" placeholder="예: kg" value="kg">
                        <div class="inputErorrMsg hidden">정확히 입력해 주세요.</div>
					</div>
					<div class="action-field">
						<label for="modalFieldDate">등록일자</label> <input class="mustInput"
							id="modalFieldDate" type="date" value="2026-04-07" name="createDate">
					</div>
					<div class="action-field wide">
						<label for="modalFieldMemo">비고</label>
						<textarea id="modalFieldMemo" name="itemsMemo" placeholder="해당 자재에 대한 메모를 입력하세요."></textarea>
					</div>
				</div>

				<div class="modal-footer">
					<button id="cancelActionModalBtn" class="btn btn-outline"
						type="button">취소</button>
					<button id="saveActionModalBtn" class="btn btn-primary"
						type="button">저장</button>
				</div>
			</div>
		</div>
	</form>


	<div id="toast" class="toast">저장되었습니다.</div>
	<script src="https://code.jquery.com/jquery-3.7.1.js"></script>
	<script>
        const addBtn = document.querySelector('.btn-primary');
        const actionModal = document.getElementById('actionModal');
        const mustInputAll = document.querySelectorAll('.mustInput');
        const form = document.querySelector('#myForm');
        const inputErorrMsg = document.querySelectorAll('.inputErorrMsg');

        addBtn.addEventListener('click', ()=>{
            console.log(1,actionModal)
            actionModal.classList.remove('hidden')
        })

        const cancelActionModalBtn = document.getElementById('cancelActionModalBtn');

        cancelActionModalBtn.addEventListener('click', ()=>{
            console.log(2,actionModal)
            actionModal.classList.add('hidden')
        })

        form.addEventListener("submit", (e)=>{
            e.preventDefault(); // submit 막기
				mustInputAll.forEach((element, index)=>{
					if(mustInputAll[index].value.trim() === ""){
						console.log('1')
					} else{
						form.submit()
					}
				})
        })
        
    </script>
</body>

</html>