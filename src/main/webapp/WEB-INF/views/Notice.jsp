<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<style>
/* =========================================
       공지사항 전체 영역
       ========================================= */
.noNoticeWrap {
	width: 100%;
	display: flex;
	flex-direction: column;
	gap: 18px;
	box-sizing: border-box;
}

/* =========================================
       검색 카드
       ========================================= */
.noFilterCard {
	width: 100%;
	padding: 18px 18px 16px;
	border: 1px solid #dbe3ec;
	border-radius: 18px;
	background: #ffffff;
	box-sizing: border-box;
}

.noFilterGrid {
	display: grid;
	grid-template-columns: 180px 180px 1fr auto auto;
	gap: 12px;
	align-items: end;
}

.noFilterItem {
	display: flex;
	flex-direction: column;
	gap: 8px;
}

.noFilterLabel {
	font-size: 14px;
	font-weight: 600;
	color: #0A1E3C;
	line-height: 1;
}

.noSearchSelect, .noSearchInput, .noModalInput, .noModalSelect,
	.noModalTextarea {
	border: 1px solid #ccd7e3;
	border-radius: 12px;
	box-sizing: border-box;
	font-size: 14px;
	font-weight: 400;
	color: #344054;
	background: #ffffff;
	outline: none;
}

.noSearchSelect, .noSearchInput, .noModalInput, .noModalSelect {
	height: 40px;
	padding: 0 14px;
}

.noSearchInput::placeholder, .noModalInput::placeholder,
	.noModalTextarea::placeholder {
	color: #98a2b3;
	font-weight: 400;
}

.noSearchBtnIcon, .noResetBtn {
	height: 40px;
	border-radius: 12px;
	box-sizing: border-box;
	cursor: pointer;
	font-size: 14px;
	font-weight: 500;
}

.noSearchBtnIcon {
	width: 44px;
	border: 1px solid #ccd7e3;
	background: #ffffff;
	display: inline-flex;
	align-items: center;
	justify-content: center;
}

.noSearchBtnIcon svg {
	width: 18px;
	height: 18px;
	stroke: #0A1E3C;
}

.noResetBtn {
	min-width: 84px;
	padding: 0 16px;
	border: 1px solid #ccd7e3;
	background: #ffffff;
	color: #0A1E3C;
}

/* =========================================
       상단 버튼 행
       ========================================= */
.noTopActionRow {
	display: flex;
	justify-content: flex-end;
	align-items: center;
}

.noBtn {
	min-width: 90px;
	height: 40px;
	padding: 0 16px;
	border: none;
	border-radius: 12px;
	box-sizing: border-box;
	font-size: 14px;
	font-weight: 500;
	cursor: pointer;
}

.noBtnPrimary {
	background: #0047AB;
	color: #ffffff;
}

.noBtnPrimary:hover {
	opacity: 0.94;
}

.noBtnGray {
	background: #ffffff;
	color: #344054;
	border: 1px solid #ccd7e3;
}

.noBtnGray:hover {
	background: #f8fafc;
}

.noBtnOrange {
	background: #ffede5;
	color: #d9480f;
	border: 1px solid #ffd3bf;
}

.noBtnOrange:hover {
	background: #ffe4d6;
}

.noBtnDanger {
	background: #fff1f3;
	color: #c01048;
	border: 1px solid #ffd0db;
}

.noBtnDanger:hover {
	background: #ffe7ee;
}

/* =========================================
       게시판 카드
       ========================================= */
.noBoardCard {
    width: 100%;
    padding: 0 18px 22px;
    border: 1px solid #dbe3ec;
    border-radius: 18px;
    background: #ffffff;
    box-sizing: border-box;
    overflow: hidden;
}

.noBoardCardTop {
	padding: 18px 2px 12px;
}

.noTotalCountText {
	font-size: 15px;
	font-weight: 600;
	color: #667085;
}

.noTotalCountText span {
	color: #0047AB;
	font-weight: 700;
}

.noTableWrap {
	width: 100%;
	overflow-x: hidden;
	box-sizing: border-box;
}

.noTable {
	width: 100%;
	min-width: 0;
	border-collapse: collapse;
	table-layout: fixed;
}

.noTable thead th {
	padding: 12px 8px;
	background: #eef3f8;
	border-top: 1px solid #dbe3ec;
	border-bottom: 1px solid #dbe3ec;
	text-align: center;
	font-size: 13px;
	font-weight: 600;
	color: #0A1E3C;
	white-space: nowrap;
}

.noTable tbody td {
	padding: 12px 8px;
	border-bottom: 1px solid #edf2f7;
	text-align: center;
	vertical-align: middle;
	font-size: 13px;
	font-weight: 400;
	color: #344054;
	background: #ffffff;
}

.noTable tbody tr:hover td {
	background: #fafcff;
}

.noTitleTd {
	text-align: left !important;
}

.noTitleLink {
	display: inline-block;
	max-width: 100%;
	overflow: hidden;
	text-overflow: ellipsis;
	white-space: nowrap;
	vertical-align: middle;
	color: #0A1E3C;
	font-weight: 400;
	text-decoration: none;
}

.noTitleLink:hover {
	color: #0047AB;
	text-decoration: underline;
}

.noStatusBadge {
	display: inline-flex;
	align-items: center;
	justify-content: center;
	min-width: 70px;
	height: 30px;
	padding: 0 12px;
	border-radius: 999px;
	box-sizing: border-box;
	font-size: 12px;
	font-weight: 500;
}

.noStatusPost {
	background: #ecf8f1;
	color: #0a7a46;
}

.noStatusHide {
	background: #fff4ea;
	color: #d96b00;
}

.noDetailBtn {
	display: inline-flex;
	align-items: center;
	justify-content: center;
	min-width: 76px;
	height: 34px;
	padding: 0 12px;
	border: 1px solid #ccd7e3;
	border-radius: 10px;
	box-sizing: border-box;
	background: #ffffff;
	color: #344054;
	text-decoration: none;
	font-size: 14px;
	font-weight: 500;
	cursor: pointer;
}

.noDetailBtn:hover {
	border-color: #0047AB;
	color: #0047AB;
}

.noEmptyRow {
	padding: 60px 0 !important;
	color: #98a2b3 !important;
	font-weight: 500;
}

/* =========================================
       페이징
       - 요청한 3번째 사진 느낌
       ========================================= */
/* =========================================
   페이징
   - 건의사항 느낌으로 항상 같은 자리에서 보이게 수정
   ========================================= */
/* =========================================
   페이징
   - << / 페이지번호 / >> 형태
   - <<, >> 는 이전/다음 페이지 이동
   ========================================= */
.noPagingWrap {
    display: flex;
    align-items: center;
    justify-content: center;
    gap: 10px;
    flex-wrap: wrap;
    margin-top: 18px;
    padding-top: 0;
}

.noPageBtn {
    display: inline-flex;
    align-items: center;
    justify-content: center;
    min-width: 34px;
    height: 34px;
    padding: 0 10px;
    border: 1px solid transparent;
    border-radius: 10px;
    box-sizing: border-box;
    background: transparent;
    color: #667085;
    text-decoration: none;
    font-size: 14px;
    font-weight: 600;
    line-height: 1;
}

.noPageBtn:hover {
    color: #0047AB;
}

.noPageBtn.noPageActive {
    background: #e9f1ff;
    color: #0047AB;
    border-color: #d5e4ff;
}

.noPageBtn.noPageDisabled {
    color: #c0c7d1;
    pointer-events: none;
}

/* =========================================
       모달
       - 건의게시판 느낌처럼 더 밝고 부드럽게
       ========================================= */
.noModalOverlay {
	display: none;
	position: fixed;
	inset: 0;
	z-index: 9999;
	background: rgba(10, 30, 60, 0.18);
	justify-content: center;
	align-items: center;
	padding: 20px;
	box-sizing: border-box;
}

.noModalBox {
	width: 1100px;
	max-width: 95%;
	max-height: 90vh;
	border-radius: 22px;
	overflow: hidden;
	background: #ffffff;
	box-shadow: 0 24px 48px rgba(15, 23, 42, 0.12);
	display: flex;
	flex-direction: column;
}

.noModalHeader {
	display: flex;
	align-items: center;
	justify-content: space-between;
	gap: 10px;
	padding: 18px 22px;
	background: #ffffff;
	border-bottom: 1px solid #eef2f6;
	color: #0A1E3C;
}

.noModalTitle {
	margin: 0;
	font-size: 18px;
	font-weight: 700;
	color: #0A1E3C;
}

.noModalClose {
	border: none;
	background: transparent;
	color: #667085;
	font-size: 28px;
	line-height: 1;
	cursor: pointer;
}

.noModalBody {
	padding: 18px 22px 22px;
	overflow-y: auto;
	box-sizing: border-box;
	background: #ffffff;
}

.noModalInner {
	border: 1px solid #e7edf4;
	border-radius: 16px;
	padding: 16px;
	background: #ffffff;
}

.noFormRow {
	display: grid;
	grid-template-columns: 140px 1fr;
	gap: 14px;
	margin-bottom: 14px;
	align-items: stretch;
}

.noFormLabel {
	display: flex;
	align-items: center;
	justify-content: center;
	min-height: 40px;
	border: 1px solid #d6e0ea;
	border-radius: 12px;
	background: #ffffff;
	color: #0A1E3C;
	font-size: 14px;
	font-weight: 600;
	box-sizing: border-box;
}

.noFormRowTop .noFormLabel {
    min-height: 40px;
    height: 40px;
    align-self: start;
}

.noContentLabel {
    min-height: 40px;
    height: 40px;
    align-self: start;
}

.noFormControl {
	width: 100%;
	min-height: 40px;
}

.noModalTextarea {
	width: 100%;
	min-height: 180px;
	padding: 14px;
	resize: vertical;
	line-height: 1.6;
	box-sizing: border-box;
}

.noModalFooter {
	display: flex;
	justify-content: flex-end;
	gap: 10px;
	flex-wrap: wrap;
	margin-top: 18px;
}

.noHidden {
	display: none !important;
}

.noPageBtn:disabled {
    color: #c0c7d1;
    cursor: default;
}

@media ( max-width : 1200px) {
	.noFilterGrid {
		grid-template-columns: 1fr 1fr 1.4fr auto auto;
	}
}

@media ( max-width : 900px) {
	.noFilterGrid {
		grid-template-columns: 1fr;
	}
	.noFormRow {
		grid-template-columns: 1fr;
	}
}
</style>

<div class="noNoticeWrap" id="noNoticeSection">

	<!-- 등록 버튼 -->
	<div class="noTopActionRow">
		<button type="button" class="noBtn noBtnPrimary"
			onclick="noOpenRegisterModal()">등록</button>
	</div>
	
	<!-- 검색 -->
	<form
		action="${pageContext.request.contextPath}/notice"
    method="get"
    class="noFilterCard"
    id="noSearchForm">
    <input type="hidden" name="action" value="list">
    <input type="hidden" name="page" id="noPage" value="${noCurrentPage}">

		<div class="noFilterGrid">
			<div class="noFilterItem">
				<label class="noFilterLabel">상태</label> <select name="statusFilter"
					class="noSearchSelect">
					<option value="all"
						<c:if test="${noStatusFilter eq 'all'}">selected="selected"</c:if>>전체</option>
					<option value="게시"
						<c:if test="${noStatusFilter eq '게시'}">selected="selected"</c:if>>게시</option>
					<option value="내림"
						<c:if test="${noStatusFilter eq '내림'}">selected="selected"</c:if>>내림</option>
				</select>
			</div>

			<div class="noFilterItem">
				<label class="noFilterLabel">검색구분</label> <select name="searchType"
					class="noSearchSelect">
					<option value="all"
						<c:if test="${noSearchType eq 'all'}">selected="selected"</c:if>>전체</option>
					<option value="title"
						<c:if test="${noSearchType eq 'title'}">selected="selected"</c:if>>제목</option>
					<option value="content"
						<c:if test="${noSearchType eq 'content'}">selected="selected"</c:if>>내용</option>
					<option value="writer"
						<c:if test="${noSearchType eq 'writer'}">selected="selected"</c:if>>작성자</option>
				</select>
			</div>

			<div class="noFilterItem">
				<label class="noFilterLabel">검색어</label> <input type="text"
					name="keyword" class="noSearchInput" value="${noKeyword}"
					placeholder="검색어를 입력하세요.">
			</div>

			<div class="noFilterItem">
				<label class="noFilterLabel">&nbsp;</label>
				<button type="submit" class="noSearchBtnIcon" aria-label="검색">
					<svg viewBox="0 0 24 24" fill="none" stroke-width="2">
                        <circle cx="11" cy="11" r="7"></circle>
                        <path d="M20 20L16.65 16.65"></path>
                    </svg>
				</button>
			</div>

			<div class="noFilterItem">
				<label class="noFilterLabel">&nbsp;</label>
				<button type="button" class="noResetBtn"
					onclick="location.href='${pageContext.request.contextPath}/notice?action=list#noNoticeSection'">
					초기화</button>
			</div>
		</div>
	</form>

	<!-- 게시판 카드 -->
	<div class="noBoardCard" id="noTableBox" >
		<div class="noBoardCardTop">
			<div class="noTotalCountText">
				총 <span>${noTotalCount}</span>건
			</div>
		</div>

		<div class="noTableWrap">
			<table class="noTable">
				<thead>
					<tr>
						<th style="width: 80px;">번호</th>
						<th style="width: 100px;">상태</th>
						<th>제목</th>
						<th style="width: 110px;">부서</th>
						<th style="width: 110px;">작성자</th>
						<th style="width: 90px;">조회수</th>
						<th style="width: 120px;">작성일</th>
						<th style="width: 100px;">상세</th>
					</tr>
				</thead>

				<tbody>
					<c:choose>
						<c:when test="${empty noNoticeList}">
							<tr>
								<td colspan="8" class="noEmptyRow">조회된 공지사항이 없습니다.</td>
							</tr>
						</c:when>

						<c:otherwise>
							<c:forEach var="noNotice" items="${noNoticeList}"
								varStatus="noStatus">
								<tr>
									<td>${noStartNo - noStatus.index}</td>

									<td><c:choose>
											<c:when test="${noNotice.status eq '게시'}">
												<span class="noStatusBadge noStatusPost">게시</span>
											</c:when>
											<c:otherwise>
												<span class="noStatusBadge noStatusHide">내림</span>
											</c:otherwise>
										</c:choose></td>

									<td class="noTitleTd"><a class="noTitleLink"
										href="${pageContext.request.contextPath}/notice?action=detail&noticeId=${noNotice.noticeId}&page=${noCurrentPage}&searchType=${noSearchType}&keyword=${noKeyword}&statusFilter=${noStatusFilter}#noNoticeSection"
										title="${noNotice.title}">
											${noNotice.title} </a></td>

									<td>-</td>
									<td>${noNotice.writerName}</td>
									<td>${noNotice.viewCount}</td>
									<td>${noNotice.createdAtStr}</td>

									<td><a class="noDetailBtn"
										href="${pageContext.request.contextPath}/notice?action=detail&noticeId=${noNotice.noticeId}&page=${noCurrentPage}&searchType=${noSearchType}&keyword=${noKeyword}&statusFilter=${noStatusFilter}#noNoticeSection">
											상세 </a></td>
								</tr>
							</c:forEach>
						</c:otherwise>
					</c:choose>
				</tbody>
			</table>
		</div>

<div class="noPagingWrap">
    <button type="button"
            class="noPageBtn"
            onclick="noMovePage(${noCurrentPage - 1})"
            <c:if test="${noCurrentPage <= 1}">disabled="disabled"</c:if>>
        &lt;&lt;
    </button>

    <c:forEach begin="${noStartPage}" end="${noEndPage}" var="noPageNo">
        <button type="button"
                class="noPageBtn ${noPageNo == noCurrentPage ? 'noPageActive' : ''}"
                onclick="noMovePage(${noPageNo})">
            ${noPageNo}
        </button>
    </c:forEach>

    <button type="button"
            class="noPageBtn"
            onclick="noMovePage(${noCurrentPage + 1})"
            <c:if test="${noCurrentPage >= noTotalPage}">disabled="disabled"</c:if>>
        &gt;&gt;
    </button>
</div>
	</div>
</div>

<!-- 등록 모달 -->
<div class="noModalOverlay" id="noRegisterModal">
	<div class="noModalBox" onclick="event.stopPropagation();">
		<div class="noModalHeader">
			<h3 class="noModalTitle">공지 등록</h3>
			<button type="button" class="noModalClose"
				onclick="noCloseRegisterModal()">&times;</button>
		</div>

		<div class="noModalBody">
			<div class="noModalInner">
				<form action="${pageContext.request.contextPath}/notice"
					method="post">
					<input type="hidden" name="action" value="create">

					<div class="noFormRow">
						<div class="noFormLabel">제목</div>
						<input type="text" name="title" class="noModalInput noFormControl"
							required>
					</div>

					<div class="noFormRow">
						<div class="noFormLabel noContentLabel">내용</div>
						<textarea name="content" class="noModalTextarea noFormControl"
							required></textarea>
					</div>

					<div class="noFormRow">
						<div class="noFormLabel">상태</div>
						<select name="status" class="noModalSelect noFormControl">
							<option value="게시">게시</option>
							<option value="내림">내림</option>
						</select>
					</div>

					<div class="noFormRow">
						<div class="noFormLabel">비고</div>
						<input type="text" name="remark"
							class="noModalInput noFormControl">
					</div>

					<div class="noModalFooter">
						<button type="button" class="noBtn noBtnGray"
							onclick="noCloseRegisterModal()">취소</button>
						<button type="submit" class="noBtn noBtnPrimary">등록</button>
					</div>
				</form>
			</div>
		</div>
	</div>
</div>

<!-- 상세 / 수정 모달 -->
<c:if test="${not empty noDetailNotice}">
	<div class="noModalOverlay" id="noDetailModal">
		<div class="noModalBox" onclick="event.stopPropagation();">
			<div class="noModalHeader">
				<h3 class="noModalTitle">공지 수정</h3>
				<button type="button" class="noModalClose"
					onclick="noCloseDetailModal()">&times;</button>
			</div>

			<div class="noModalBody">

				<!-- 상세보기 -->
				<div id="noDetailViewArea" class="noModalInner">
					<div class="noFormRow">
						<div class="noFormLabel">제목</div>
						<input type="text" class="noModalInput noFormControl"
							value="${noDetailNotice.title}" readonly="readonly">
					</div>

					<div class="noFormRow">
						<div class="noFormLabel noContentLabel">내용</div>
						<textarea class="noModalTextarea noFormControl"
							readonly="readonly">${noDetailNotice.content}</textarea>
					</div>

					<div class="noFormRow">
						<div class="noFormLabel">상태</div>
						<input type="text" class="noModalInput noFormControl"
							value="${noDetailNotice.status}" readonly="readonly">
					</div>

					<div class="noFormRow">
						<div class="noFormLabel">비고</div>
						<input type="text" class="noModalInput noFormControl"
							value="${noDetailNotice.remark}" readonly="readonly">
					</div>

					<div class="noModalFooter">
						<button type="button" class="noBtn noBtnGray"
							onclick="noOpenEditMode()">수정</button>

						<c:choose>
							<c:when test="${noDetailNotice.status eq '게시'}">
								<form action="${pageContext.request.contextPath}/notice"
									method="post" style="display: inline;">
									<input type="hidden" name="action" value="hide"> <input
										type="hidden" name="noticeId"
										value="${noDetailNotice.noticeId}">
									<button type="submit" class="noBtn noBtnOrange">공지내리기</button>
								</form>
							</c:when>
							<c:otherwise>
								<form action="${pageContext.request.contextPath}/notice"
									method="post" style="display: inline;">
									<input type="hidden" name="action" value="restore"> <input
										type="hidden" name="noticeId"
										value="${noDetailNotice.noticeId}">
									<button type="submit" class="noBtn noBtnPrimary">다시게시</button>
								</form>
							</c:otherwise>
						</c:choose>

						<form action="${pageContext.request.contextPath}/notice"
							method="post" style="display: inline;"
							onsubmit="return confirm('정말 삭제하시겠습니까?');">
							<input type="hidden" name="action" value="delete"> <input
								type="hidden" name="noticeId" value="${noDetailNotice.noticeId}">
							<button type="submit" class="noBtn noBtnDanger">삭제</button>
						</form>

						<button type="button" class="noBtn noBtnGray"
							onclick="noCloseDetailModal()">닫기</button>
					</div>
				</div>

				<!-- 수정 -->
				<div id="noEditViewArea" class="noModalInner noHidden">
					<form action="${pageContext.request.contextPath}/notice"
						method="post">
						<input type="hidden" name="action" value="update"> <input
							type="hidden" name="noticeId" value="${noDetailNotice.noticeId}">

						<div class="noFormRow">
							<div class="noFormLabel">제목</div>
							<input type="text" name="title"
								class="noModalInput noFormControl"
								value="${noDetailNotice.title}" required>
						</div>

						<div class="noFormRow">
							<div class="noFormLabel noContentLabel">내용</div>
							<textarea name="content" class="noModalTextarea noFormControl"
								required>${noDetailNotice.content}</textarea>
						</div>

						<div class="noFormRow">
							<div class="noFormLabel">상태</div>
							<select name="status" class="noModalSelect noFormControl">
								<option value="게시"
									<c:if test="${noDetailNotice.status eq '게시'}">selected="selected"</c:if>>게시</option>
								<option value="내림"
									<c:if test="${noDetailNotice.status eq '내림'}">selected="selected"</c:if>>내림</option>
							</select>
						</div>

						<div class="noFormRow">
							<div class="noFormLabel">비고</div>
							<input type="text" name="remark"
								class="noModalInput noFormControl"
								value="${noDetailNotice.remark}">
						</div>

						<div class="noModalFooter">
							<button type="button" class="noBtn noBtnGray"
								onclick="noCloseEditMode()">취소</button>
							<button type="submit" class="noBtn noBtnPrimary">수정</button>
						</div>
					</form>
				</div>

			</div>
		</div>
	</div>
</c:if>

<script>
	function noOpenRegisterModal() {
		document.getElementById("noRegisterModal").style.display = "flex";
	}

	function noCloseRegisterModal() {
		document.getElementById("noRegisterModal").style.display = "none";
	}

	function noCloseDetailModal() {
		location.href = "${pageContext.request.contextPath}/notice?action=list"
				+ "&page=${noCurrentPage}" + "&searchType=${noSearchType}"
				+ "&keyword=${noKeyword}" + "&statusFilter=${noStatusFilter}"
				+ "#noNoticeSection";
	}

	function noOpenEditMode() {
		document.getElementById("noDetailViewArea").classList.add("noHidden");
		document.getElementById("noEditViewArea").classList.remove("noHidden");
	}

	function noCloseEditMode() {
		document.getElementById("noEditViewArea").classList.add("noHidden");
		document.getElementById("noDetailViewArea").classList
				.remove("noHidden");
	}

	document.addEventListener("DOMContentLoaded", function() {
		var noDetailModal = document.getElementById("noDetailModal");
		var noRegisterModal = document.getElementById("noRegisterModal");

		if (noDetailModal) {
			noDetailModal.style.display = "flex";

			noDetailModal.addEventListener("click", function() {
				noCloseDetailModal();
			});
		}

		if (noRegisterModal) {
			noRegisterModal.addEventListener("click", function() {
				noCloseRegisterModal();
			});
		}
	});
    function noMovePage(noPageValue) {
        sessionStorage.setItem("noticeMoveToTable", "Y");
        document.getElementById("noPage").value = noPageValue;
        document.getElementById("noSearchForm").submit();
    }

    document.addEventListener("DOMContentLoaded", function() {
        var noDetailModal = document.getElementById("noDetailModal");
        var noRegisterModal = document.getElementById("noRegisterModal");

        if (noDetailModal) {
            noDetailModal.style.display = "flex";

            noDetailModal.addEventListener("click", function() {
                noCloseDetailModal();
            });
        }

        if (noRegisterModal) {
            noRegisterModal.addEventListener("click", function() {
                noCloseRegisterModal();
            });
        }

        var noMoveToTable = sessionStorage.getItem("noticeMoveToTable");
        if (noMoveToTable === "Y") {
            var noTableBox = document.getElementById("noTableBox");
            if (noTableBox) {
                noTableBox.scrollIntoView({ behavior: "auto", block: "start" });
            }
            sessionStorage.removeItem("noticeMoveToTable");
        }
    });
</script>