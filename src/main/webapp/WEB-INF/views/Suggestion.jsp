<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List"%>
<%@ page import="java.text.SimpleDateFormat"%>
<%@ page import="java.lang.reflect.Method"%>
<%@ page import="SUGGESTION.DTO.SuggestionDTO"%>
<%@ page import="ANSWER.DTO.AnswerDTO"%>

<%!private Object suInvokeGetter(Object target, String methodName) {
		if (target == null)
			return null;
		try {
			Method method = target.getClass().getMethod(methodName);
			return method.invoke(target);
		} catch (Exception e) {
			return null;
		}
	}

	private String suStringOrDash(Object value) {
		if (value == null)
			return "-";
		String text = String.valueOf(value).trim();
		return text.isEmpty() ? "-" : text;
	}

	private String suFormatDateTimeOrDash(Object value) {
		if (value == null)
			return "-";
		if (value instanceof java.util.Date) {
			return new SimpleDateFormat("yyyy-MM-dd HH:mm").format((java.util.Date) value);
		}
		String text = String.valueOf(value).trim();
		return text.isEmpty() ? "-" : text;
	}%>

<%
List<SuggestionDTO> suSuggestionList = (List<SuggestionDTO>) request.getAttribute("suggestionList");
SuggestionDTO suSelectedSuggestion = (SuggestionDTO) request.getAttribute("selectedSuggestion");
List<AnswerDTO> suAnswerList = (List<AnswerDTO>) request.getAttribute("answerList");
AnswerDTO suSelectedAnswer = (AnswerDTO) request.getAttribute("selectedAnswer");

String suKeyword = request.getAttribute("keyword") == null ? "" : String.valueOf(request.getAttribute("keyword"));
String suStatus = request.getAttribute("status") == null ? "" : String.valueOf(request.getAttribute("status"));
String suDeptCode = request.getAttribute("deptCode") == null ? "" : String.valueOf(request.getAttribute("deptCode"));
String suMode = request.getAttribute("mode") == null ? "list" : String.valueOf(request.getAttribute("mode"));
String suModal = request.getAttribute("modal") == null ? "" : String.valueOf(request.getAttribute("modal"));
String suAnswerMode = request.getAttribute("answerMode") == null
		? ""
		: String.valueOf(request.getAttribute("answerMode"));
String suEditAnswerIdParam = request.getParameter("editAnswerId") == null ? "" : request.getParameter("editAnswerId");
long suEditAnswerId = 0L;
try {
	suEditAnswerId = Long.parseLong(suEditAnswerIdParam);
} catch (Exception e) {
	suEditAnswerId = 0L;
}

long suLoginEmpId = 0L;
Object suLoginUserObj = session.getAttribute("loginUser");
Object suLoginEmpIdObj = session.getAttribute("loginEmpId");
Object suEmpIdObj = session.getAttribute("empId");

if (suLoginUserObj != null) {
	Object suLoginUserEmpId = suInvokeGetter(suLoginUserObj, "getEmpId");
	if (suLoginUserEmpId instanceof Number) {
		suLoginEmpId = ((Number) suLoginUserEmpId).longValue();
	} else if (suLoginUserEmpId != null) {
		try {
	suLoginEmpId = Long.parseLong(String.valueOf(suLoginUserEmpId));
		} catch (Exception e) {
	suLoginEmpId = 0L;
		}
	}
}

if (suLoginEmpId <= 0L && suLoginEmpIdObj instanceof Number) {
	suLoginEmpId = ((Number) suLoginEmpIdObj).longValue();
}

if (suLoginEmpId <= 0L && suEmpIdObj instanceof Number) {
	suLoginEmpId = ((Number) suEmpIdObj).longValue();
}

int suSize = request.getAttribute("size") == null ? 10 : (Integer) request.getAttribute("size");
int suCurrentPage = request.getAttribute("page") == null ? 1 : (Integer) request.getAttribute("page");
int suTotalCount = request.getAttribute("totalCount") == null ? 0 : (Integer) request.getAttribute("totalCount");
int suStartPage = request.getAttribute("startPage") == null ? 1 : (Integer) request.getAttribute("startPage");
int suEndPage = request.getAttribute("endPage") == null ? 1 : (Integer) request.getAttribute("endPage");
int suTotalPage = request.getAttribute("totalPage") == null ? 1 : (Integer) request.getAttribute("totalPage");

String suContextPath = request.getContextPath();
SimpleDateFormat suDateFormat = new SimpleDateFormat("yyyy-MM-dd");

String suSelectedStatusClass = "suStatusReceipt";
String suSelectedStatusText = "";
if (suSelectedSuggestion != null) {
	suSelectedStatusText = suSelectedSuggestion.getStatus() == null ? "" : suSelectedSuggestion.getStatus();
	if ("검토중".equals(suSelectedStatusText)) {
		suSelectedStatusClass = "suStatusReview";
	} else if ("답변완료".equals(suSelectedStatusText) || "반영완료".equals(suSelectedStatusText)) {
		suSelectedStatusClass = "suStatusDone";
		suSelectedStatusText = "답변완료";
	} else if ("반려".equals(suSelectedStatusText) || "내림".equals(suSelectedStatusText)) {
		suSelectedStatusClass = "suStatusReject";
	}
}
%>

<style>
.suWrap, .suDetailWrap {
	width: 100%;
	display: flex;
	flex-direction: column;
	box-sizing: border-box
}

.suBtn {
	min-width: 88px;
	height: 40px;
	padding: 0 16px;
	border: none;
	border-radius: 12px;
	box-sizing: border-box;
	font-size: 14px;
	font-weight: 500;
	cursor: pointer;
	text-decoration: none;
	display: inline-flex;
	align-items: center;
	justify-content: center
}

.suBtnPrimary {
	background: #0047AB;
	color: #fff
}

.suBtnGray {
	background: #fff;
	color: #344054;
	border: 1px solid #ccd7e3
}

.suBtnDanger {
	background: #fff1f3;
	color: #c01048;
	border: 1px solid #ffd0db
}

.suBtnWarn {
	background: #fff4ea;
	color: #d96b00;
	border: 1px solid #ffd7b2
}

.suBtnReplyAction {
	background: #f5f9ff;
	color: #0047AB;
	border: 1px solid #d7e7ff
}

.suBtnRow {
	display: flex;
	justify-content: flex-end;
	align-items: center;
	gap: 10px;
	flex-wrap: wrap
}

.suSearchBox, .suCard, .suTableBox {
	width: 100%;
	padding: 18px;
	border: 1px solid #dbe3ec;
	border-radius: 18px;
	background: #fff;
	box-sizing: border-box
}

.suSearchBox {
	margin-bottom: 16px
}

.suBoardHeader {
	display: flex;
	justify-content: flex-end;
	align-items: center;
	margin-bottom: 16px
}

.suSearchRow {
	display: grid;
	grid-template-columns: 180px 180px 1fr auto auto;
	gap: 12px;
	align-items: end
}

.suSearchItem {
	display: flex;
	flex-direction: column;
	gap: 8px
}

.suSearchItem label {
	font-size: 14px;
	font-weight: 600;
	color: #0A1E3C;
	line-height: 1
}

.suSearchItem input, .suSearchItem select, .suField {
	width: 100%;
	min-height: 40px;
	padding: 0 14px;
	border: 1px solid #ccd7e3;
	border-radius: 12px;
	background: #fff;
	box-sizing: border-box;
	font-size: 14px;
	color: #344054;
	outline: none
}

textarea.suField {
	height: 190px;
	min-height: 190px;
	padding: 14px;
	resize: none;
	line-height: 1.6;
}

.suSearchBtnIcon {
	width: 44px;
	height: 40px;
	border: 1px solid #ccd7e3;
	border-radius: 12px;
	background: #fff;
	display: inline-flex;
	align-items: center;
	justify-content: center;
	cursor: pointer;
	padding: 0;
	box-sizing: border-box
}

.suSearchBtnIcon svg {
	width: 18px;
	height: 18px;
	stroke: #0A1E3C
}

.suResetBtn {
	min-width: 84px;
	height: 40px;
	padding: 0 16px;
	border: 1px solid #ccd7e3;
	border-radius: 12px;
	background: #fff;
	color: #0A1E3C;
	font-size: 14px;
	font-weight: 500;
	text-decoration: none;
	display: inline-flex;
	align-items: center;
	justify-content: center;
	box-sizing: border-box
}

.suTableTop {
	padding: 0 2px 12px
}

.suTableCount {
	margin: 0;
	font-size: 15px;
	font-weight: 600;
	color: #667085
}

.suTableCount strong {
	color: #0047AB;
	font-weight: 700
}

.suTableScroll {
	width: 100%;
	overflow-x: hidden;
	box-sizing: border-box
}

.suTable {
	width: 100%;
	min-width: 0;
	border-collapse: collapse;
	table-layout: fixed
}

.suTable thead th {
	padding: 14px 12px;
	background: #eef3f8;
	border-top: 1px solid #dbe3ec;
	border-bottom: 1px solid #dbe3ec;
	text-align: center;
	font-size: 14px;
	font-weight: 600;
	color: #0A1E3C;
	white-space: nowrap
}

.suTable tbody td {
	padding: 14px 12px;
	border-bottom: 1px solid #edf2f7;
	text-align: center;
	vertical-align: middle;
	font-size: 14px;
	color: #344054;
	background: #fff
}

.suTable tbody tr:hover td {
	background: #fafcff
}

.suTitleCell {
	text-align: left !important
}

.suTitleLink {
	display: inline-block;
	max-width: 100%;
	overflow: hidden;
	text-overflow: ellipsis;
	white-space: nowrap;
	color: #0A1E3C;
	font-weight: 400;
	text-decoration: none
}

.suTitleLink:hover {
	color: #0047AB;
	text-decoration: underline
}

.suViewBtn {
	display: inline-flex;
	align-items: center;
	justify-content: center;
	min-width: 76px;
	height: 34px;
	padding: 0 12px;
	border: 1px solid #ccd7e3;
	border-radius: 10px;
	box-sizing: border-box;
	background: #fff;
	color: #344054;
	text-decoration: none;
	font-size: 13px;
	font-weight: 500
}

.suViewBtn.suReplyBtn {
	background: #f5f9ff;
	border-color: #d7e7ff;
	color: #0047AB
}

.suViewBtn.suReplyBtn:hover {
	background: #e8f0ff;
	border-color: #bfd6ff;
	color: #003b8f
}

.suPagingWrap {
	display: flex;
	justify-content: center;
	align-items: center;
	gap: 10px;
	flex-wrap: wrap;
	margin-top: 18px;
	padding-top: 0;
	font-family: inherit;
}

.suPagingBtn {
	display: inline-flex;
	align-items: center;
	justify-content: center;
	min-width: 36px;
	height: 36px;
	padding: 0 10px;
	border: 1px solid transparent;
	border-radius: 12px;
	box-sizing: border-box;
	background: transparent;
	color: #667085;
	text-decoration: none;
	font-size: 15px;
	font-weight: 400;
	line-height: 1;
	cursor: pointer;
	font-family: inherit;
	transition: background-color 0.15s ease, color 0.15s ease, border-color
		0.15s ease;
}

.suPagingBtn:hover:not(:disabled) {
	color: #0047AB;
	background: #f5f9ff;
}

.suPagingBtnActive {
	background: #e8f0ff;
	color: #0047AB;
	border-color: #d4e3ff;
	font-weight: 700;
}

.suPagingBtn:disabled {
	color: #c0c7d1;
	cursor: default;
	pointer-events: none;
	background: transparent;
	border-color: transparent;
}

.suAnswerListWrap {
	display: flex;
	flex-direction: column;
	gap: 14px;
}

.suAnswerItem {
	padding: 18px;
	border: 1px solid #e7edf4;
	border-radius: 16px;
	background: #fbfcfe;
	box-sizing: border-box;
}

.suAnswerItemHeader {
	display: flex;
	justify-content: space-between;
	align-items: center;
	gap: 12px;
	margin-bottom: 12px;
}

.suAnswerItemTitle {
	font-size: 16px;
	font-weight: 700;
	color: #0A1E3C;
}

.suAnswerEmpty {
	padding: 24px 18px;
	border: 1px dashed #d6e0ea;
	border-radius: 16px;
	background: #fbfcfe;
	font-size: 14px;
	color: #98a2b3;
	text-align: center;
}

.suStatusBadge {
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
	border: 1px solid transparent;
	flex-shrink: 0
}

.suStatusReceipt {
	background: #ecf8f1;
	color: #0a7a46
}

.suStatusReview {
	background: #eef3f8;
	color: #4b5565
}

.suStatusDone {
	background: #e9f1ff;
	color: #0047AB
}

.suStatusReject {
	background: #fff4ea;
	color: #d96b00
}

.suDetailWrap {
	gap: 18px
}

.suPostHeader {
	display: flex;
	justify-content: space-between;
	align-items: flex-start;
	gap: 16px;
	margin-bottom: 16px
}

.suPostTitle {
	margin: 0;
	font-size: 20px;
	font-weight: 700;
	color: #0A1E3C;
	line-height: 1.4;
	word-break: break-word
}

.suMeta {
	display: flex;
	flex-wrap: wrap;
	gap: 18px 24px;
	padding-bottom: 16px;
	margin-bottom: 24px;
	border-bottom: 1px solid #eef2f6;
	font-size: 14px;
	color: #667085
}

.suMetaItem {
	display: inline-flex;
	align-items: center;
	gap: 8px
}

.suMetaLabel {
	font-weight: 700;
	color: #0A1E3C
}

.suMetaValue {
	color: #475467;
	font-weight: 500
}

.suContent {
	min-height: 240px;
	font-size: 16px;
	line-height: 2;
	color: #344054;
	white-space: pre-wrap;
	word-break: break-word;
	text-align: left
}

.suRemark {
	margin-top: 24px;
	padding-top: 16px;
	border-top: 1px solid #eef2f6;
	font-size: 14px;
	line-height: 1.8;
	color: #667085
}

.suDetailBottomBtnRow {
	display: flex;
	justify-content: flex-end;
	align-items: center;
	gap: 10px;
	flex-wrap: wrap
}

.suModalWrap {
	display: none;
	position: fixed;
	inset: 0;
	z-index: 9999;
	background: rgba(10, 30, 60, .18);
	justify-content: center;
	align-items: center;
	padding: 20px;
	box-sizing: border-box
}

.suModalWrap.suModalShow {
	display: flex
}

.suModalBox {
	width: 1180px;
	max-width: 96%;
	max-height: 92vh;
	border-radius: 22px;
	overflow: hidden;
	background: #fff;
	box-shadow: 0 24px 48px rgba(15, 23, 42, .12);
	display: flex;
	flex-direction: column
}

.suModalHeader {
	display: flex;
	align-items: center;
	justify-content: space-between;
	gap: 10px;
	padding: 18px 22px;
	background: #fff;
	border-bottom: 1px solid #eef2f6
}

.suModalTitle {
	margin: 0;
	font-size: 18px;
	font-weight: 700;
	color: #0A1E3C
}

.suModalCloseBtn {
	border: none;
	background: transparent;
	color: #667085;
	font-size: 28px;
	line-height: 1;
	text-decoration: none
}

.suModalBody {
	padding: 18px 22px 22px;
	overflow-y: auto;
	box-sizing: border-box;
	background: #fff
}

.suModalInner {
	border: 1px solid #e7edf4;
	border-radius: 16px;
	padding: 16px;
	background: #fff;
	box-sizing: border-box
}

.suModalSection {
	margin-bottom: 18px;
	padding: 16px;
	border: 1px solid #eef2f6;
	border-radius: 16px;
	background: #fbfcfe
}

.suModalSectionTitle {
	margin: 0 0 14px;
	font-size: 16px;
	font-weight: 700;
	color: #0A1E3C
}

.suFormRow {
	display: grid;
	grid-template-columns: 140px 1fr;
	gap: 14px;
	margin-bottom: 14px;
	align-items: start;
}

.suLabelBox {
	display: flex;
	align-items: center;
	justify-content: center;
	height: 40px;
	min-height: 40px;
	align-self: start;
	border: 1px solid #d6e0ea;
	border-radius: 12px;
	background: #ffffff;
	color: #0A1E3C;
	font-size: 14px;
	font-weight: 600;
	box-sizing: border-box;
}
/* 댓글 수정버튼 */
.suCommentMiniBtn {
	min-width: 72px;
	height: 32px;
	padding: 0 12px;
	font-size: 13px;
	border-radius: 16px;
}

/* 댓글취소버튼 */
.suCommentCancelBtn, .suCommentCancelBtn:visited {
	background: #ffffff;
	border: 1px solid #d7e7ff;
	color: #0047AB;
	text-decoration: none;
}

.suCommentCancelBtn:hover {
	background: #f8fbff;
	border-color: #bfd6ff;
	color: #003b8f;
	text-decoration: none;
}

@media ( max-width : 900px) {
	.suSearchRow {
		grid-template-columns: 44px 1fr;
	}
	.suSearchRow .suSearchItem:nth-child(1), .suSearchRow .suSearchItem:nth-child(2),
		.suSearchRow .suSearchItem:nth-child(3) {
		grid-column: 1/-1;
	}
	.suSearchRow .suSearchItem:nth-child(4) {
		grid-column: 1/2;
	}
	.suSearchRow .suSearchItem:nth-child(5) {
		grid-column: 2/3;
	}
	.suResetBtn {
		width: 100%;
		min-width: 0;
	}
}
</style>

<%
if ("detail".equals(suMode)) {
%>
<div class="suDetailWrap">
	<%
	if (suSelectedSuggestion == null) {
	%>
	<div class="suCard">
		<div class="suInfoText">존재하지 않는 건의사항입니다.</div>
		<div class="suDetailBottomBtnRow" style="margin-top: 16px;">
			<a href="<%=suContextPath + "/suggestion/list"%>"
				class="suBtn suBtnGray">목록</a>
		</div>
	</div>
	<%
	} else {
	%>
	<div class="suCard">
		<div class="suPostHeader">
			<h2 class="suPostTitle"><%=suSelectedSuggestion.getTitle() == null ? "" : suSelectedSuggestion.getTitle()%></h2>
			<span class="suStatusBadge <%=suSelectedStatusClass%>"><%=suSelectedStatusText%></span>
		</div>
		<div class="suMeta">
			<div class="suMetaItem">
				<span class="suMetaLabel">번호</span><span class="suMetaValue"><%=suSelectedSuggestion.getSuggestionId()%></span>
			</div>
			<div class="suMetaItem">
				<span class="suMetaLabel">부서</span><span class="suMetaValue"><%=suSelectedSuggestion.getDeptCode() == null ? "-" : suSelectedSuggestion.getDeptCode()%></span>
			</div>
			<div class="suMetaItem">
				<span class="suMetaLabel">작성자</span><span class="suMetaValue"><%=suSelectedSuggestion.getWriterName() == null ? "-" : suSelectedSuggestion.getWriterName()%></span>
			</div>
			<div class="suMetaItem">
				<span class="suMetaLabel">작성일</span><span class="suMetaValue"><%=suSelectedSuggestion.getCreatedAt() == null ? "-" : suDateFormat.format(suSelectedSuggestion.getCreatedAt())%></span>
			</div>
			<div class="suMetaItem">
				<span class="suMetaLabel">조회수</span><span class="suMetaValue"><%=suSelectedSuggestion.getViewCount()%></span>
			</div>
		</div>
		<div class="suContent"><%=suSelectedSuggestion.getContent() == null ? "" : suSelectedSuggestion.getContent()%></div>
		<%
		if (suSelectedSuggestion.getRemark() != null && !"".equals(suSelectedSuggestion.getRemark().trim())) {
		%>
		<div class="suRemark">
			<strong style="color: #0A1E3C;">비고</strong><br><%=suSelectedSuggestion.getRemark()%></div>
		<%
		}
		%>
	</div>

	<div class="suCard">
		<h3 class="suModalSectionTitle" style="margin-bottom: 16px;">답글</h3>
		<%
		if (suAnswerList != null && !suAnswerList.isEmpty()) {
		%>
		<div class="suAnswerListWrap">
			<%
			for (AnswerDTO suAnswerDto : suAnswerList) {
			%>
			<%
			String suAnswerWriterName = suStringOrDash(suInvokeGetter(suAnswerDto, "getWriterName"));
			String suAnswerCreatedAt = suFormatDateTimeOrDash(suInvokeGetter(suAnswerDto, "getCreatedAt"));
			%>
			<%
			Object suAnswerWriterEmpIdObj = suInvokeGetter(suAnswerDto, "getWriterEmpId");
			long suAnswerWriterEmpId = 0L;
			if (suAnswerWriterEmpIdObj instanceof Number) {
				suAnswerWriterEmpId = ((Number) suAnswerWriterEmpIdObj).longValue();
			} else if (suAnswerWriterEmpIdObj != null) {
				try {
					suAnswerWriterEmpId = Long.parseLong(String.valueOf(suAnswerWriterEmpIdObj));
				} catch (Exception e) {
					suAnswerWriterEmpId = 0L;
				}
			}
			Object suAnswerIdObj = suInvokeGetter(suAnswerDto, "getAnswerId");
			long suAnswerId = 0L;
			if (suAnswerIdObj instanceof Number) {
				suAnswerId = ((Number) suAnswerIdObj).longValue();
			} else if (suAnswerIdObj != null) {
				try {
					suAnswerId = Long.parseLong(String.valueOf(suAnswerIdObj));
				} catch (Exception e) {
					suAnswerId = 0L;
				}
			}
			boolean suCanEditOwnAnswer = suLoginEmpId > 0L && suLoginEmpId == suAnswerWriterEmpId;
			boolean suIsEditTarget = suEditAnswerId == suAnswerId;
			String suAnswerRemark = suInvokeGetter(suAnswerDto, "getRemark") == null
					? ""
					: String.valueOf(suInvokeGetter(suAnswerDto, "getRemark"));
			%>
			<div class="suAnswerItem" id="suAnswerItem-<%=suAnswerId%>">
				<div class="suAnswerItemHeader">
					<div class="suAnswerItemTitle">댓글</div>
					<span
						class="suStatusBadge <%="내림".equals(suAnswerDto.getStatus()) ? "suStatusReject" : "suStatusDone"%>"><%=suAnswerDto.getStatus() == null ? "등록" : suAnswerDto.getStatus()%></span>
				</div>
				<div class="suMeta" style="margin: 0 0 16px;">
					<div class="suMetaItem">
						<span class="suMetaLabel">작성자</span><span class="suMetaValue"><%=suAnswerWriterName%></span>
					</div>
					<div class="suMetaItem">
						<span class="suMetaLabel">등록시간</span><span class="suMetaValue"><%=suAnswerCreatedAt%></span>
					</div>
				</div>
				<div class="suContent"
					style="min-height: auto; font-size: 15px; line-height: 1.9;"><%=suAnswerDto.getContent() == null ? "" : suAnswerDto.getContent()%></div>
				<%
				if (suCanEditOwnAnswer) {
				%>
				<div class="suAnswerActionRow">
					<%
					if (!suIsEditTarget) {
					%>
					<a
						href="<%=suContextPath + "/suggestion/list?mode=detail&id=" + suSelectedSuggestion.getSuggestionId() + "&editAnswerId="
		+ suAnswerId + "#suAnswerItem-" + suAnswerId%>"
						class="suBtn suBtnReplyAction suCommentMiniBtn">댓글수정</a>
					<%
					}
					%>
				</div>
				<%
				}
				%>
				<%
				if (suCanEditOwnAnswer && suIsEditTarget) {
				%>
				<form method="post"
					action="<%=suContextPath + "/suggestion/answerUpdate"%>"
					class="suAnswerEditForm">
					<input type="hidden" name="answerId" value="<%=suAnswerId%>" /> <input
						type="hidden" name="suggestionId"
						value="<%=suSelectedSuggestion.getSuggestionId()%>" /> <input
						type="hidden" name="status"
						value="<%=suAnswerDto.getStatus() == null ? "등록" : suAnswerDto.getStatus()%>" />
					<input type="hidden" name="remark" value="<%=suAnswerRemark%>" />
					<div class="suFormRow" style="margin-top: 14px;">
						<div class="suLabelBox">수정내용</div>
						<div>
							<textarea name="content" class="suField" required><%=suAnswerDto.getContent() == null ? "" : suAnswerDto.getContent()%></textarea>
						</div>
					</div>
					<div class="suBtnRow"
						style="margin-top: 12px; justify-content: flex-end;">
						<a
							href="<%=suContextPath + "/suggestion/list?mode=detail&id=" + suSelectedSuggestion.getSuggestionId() + "#suAnswerItem-"
		+ suAnswerId%>"
							class="ssuBtn suCommentMiniBtn suCommentCancelBtn">취소</a>
						<button type="submit" class="suBtn suBtnReplyAction">수정저장</button>
					</div>
				</form>
				<%
				}
				%>
			</div>
			<%
			}
			%>
		</div>
		<%
		} else {
		%>
		<div class="suAnswerEmpty">아직 등록된 답글이 없습니다.</div>
		<%
		}
		%>
	</div>

	<%
	if (suSelectedSuggestion != null) {
	%>
	<div class="suCard" id="suAnswerWriteBox">
		<form method="post"
			action="<%=suContextPath + "/suggestion/answerInsert"%>">
			<input type="hidden" name="suggestionId"
				value="<%=suSelectedSuggestion.getSuggestionId()%>" /> <input
				type="hidden" name="status" value="등록" />
			<div class="suModalSection" style="margin-bottom: 0;">
				<h4 class="suModalSectionTitle">댓글 작성</h4>
				<div class="suFormRow">
					<div class="suLabelBox">내용</div>
					<div>
						<textarea name="content" class="suField" required></textarea>
					</div>
				</div>
				<div class="suBtnRow">
					<a
						href="<%=suContextPath + "/suggestion/list?mode=detail&id=" + suSelectedSuggestion.getSuggestionId()%>"
						class="suBtn suCommentMiniBtn suCommentCancelBtn">취소</a>
					<button type="submit"
						class="suBtn suBtnReplyAction suCommentMiniBtn">댓글등록</button>
				</div>
			</div>
		</form>
	</div>
	<%
	}
	%>

	<div class="suDetailBottomBtnRow">
		<a href="<%=suContextPath + "/suggestion/list"%>"
			class="suBtn suBtnGray">목록</a>
		<!-- 		<a href="#suAnswerWriteBox" -->
		<!-- 			class="suBtn suBtnReplyAction">댓글등록</a> -->
		<a
			href="<%=suContextPath + "/suggestion/list?mode=detail&id=" + suSelectedSuggestion.getSuggestionId()
		+ "&modal=suProcess"%>"
			class="suBtn suBtnPrimary">확인</a>
	</div>

	<div
		class="suModalWrap <%="suProcess".equals(suModal) ? "suModalShow" : ""%>">
		<div class="suModalBox">
			<div class="suModalHeader">
				<h3 class="suModalTitle">건의 처리</h3>
				<a
					href="<%=suContextPath + "/suggestion/list?mode=detail&id=" + suSelectedSuggestion.getSuggestionId()%>"
					class="suModalCloseBtn">×</a>
			</div>
			<div class="suModalBody">
				<div class="suModalInner">
					<form method="post"
						action="<%=suContextPath + "/suggestion/update"%>">
						<input type="hidden" name="suggestionId"
							value="<%=suSelectedSuggestion.getSuggestionId()%>" />
						<div class="suModalSection">
							<h4 class="suModalSectionTitle">게시글 처리</h4>
							<div class="suFormRow">
								<div class="suLabelBox">제목</div>
								<div>
									<input type="text" name="title" class="suField"
										value="<%=suSelectedSuggestion.getTitle() == null ? "" : suSelectedSuggestion.getTitle()%>"
										required />
								</div>
							</div>
							<div class="suFormRow">
								<div class="suLabelBox">내용</div>
								<div>
									<textarea name="content" class="suField" required><%=suSelectedSuggestion.getContent() == null ? "" : suSelectedSuggestion.getContent()%></textarea>
								</div>
							</div>
							<div class="suFormRow">
								<div class="suLabelBox">상태</div>
								<div>
									<select name="status" class="suField">
										<option value="접수"
											<%="접수".equals(suSelectedSuggestion.getStatus()) ? "selected" : ""%>>접수</option>
										<option value="검토중"
											<%="검토중".equals(suSelectedSuggestion.getStatus()) ? "selected" : ""%>>검토중</option>
										<option value="반영완료"
											<%="반영완료".equals(suSelectedSuggestion.getStatus()) ? "selected" : ""%>>답변완료</option>
										<option value="반려"
											<%="반려".equals(suSelectedSuggestion.getStatus()) ? "selected" : ""%>>반려</option>
										<option value="내림"
											<%="내림".equals(suSelectedSuggestion.getStatus()) ? "selected" : ""%>>내림</option>
									</select>
								</div>
							</div>
							<div class="suFormRow">
								<div class="suLabelBox">비고</div>
								<div>
									<input type="text" name="remark" class="suField"
										value="<%=suSelectedSuggestion.getRemark() == null ? "" : suSelectedSuggestion.getRemark()%>" />
								</div>
							</div>
							<div class="suBtnRow">
								<button type="submit" class="suBtn suBtnPrimary">게시글 저장</button>
							</div>
						</div>
					</form>

					<div class="suModalSection">
						<h4 class="suModalSectionTitle">게시글 빠른 처리</h4>
						<div class="suBtnRow">
							<%
							if ("내림".equals(suSelectedSuggestion.getStatus())) {
							%>
							<form method="post"
								action="<%=suContextPath + "/suggestion/restore"%>"
								style="margin: 0;">
								<input type="hidden" name="suggestionId"
									value="<%=suSelectedSuggestion.getSuggestionId()%>" />
								<button type="submit" class="suBtn suBtnPrimary">게시글복구</button>
							</form>
							<%
							} else {
							%>
							<form method="post"
								action="<%=suContextPath + "/suggestion/hide"%>"
								style="margin: 0;">
								<input type="hidden" name="suggestionId"
									value="<%=suSelectedSuggestion.getSuggestionId()%>" />
								<button type="submit" class="suBtn suBtnWarn">게시글내리기</button>
							</form>
							<%
							}
							%>

							<form method="post"
								action="<%=suContextPath + "/suggestion/delete"%>"
								style="margin: 0;" onsubmit="return confirm('정말 삭제하시겠습니까?');">
								<input type="hidden" name="suggestionId"
									value="<%=suSelectedSuggestion.getSuggestionId()%>" />
								<button type="submit" class="suBtn suBtnDanger">게시글삭제</button>
							</form>
						</div>
					</div>

					<div class="suModalSection">
						<h4 class="suModalSectionTitle">댓글 관리</h4>
						<%
						if (suAnswerList != null && !suAnswerList.isEmpty()) {
						%>
						<%
						for (AnswerDTO suAnswerDto : suAnswerList) {
						%>
						<%
						String suModalAnswerWriterName = suStringOrDash(suInvokeGetter(suAnswerDto, "getWriterName"));
						String suModalAnswerCreatedAt = suFormatDateTimeOrDash(suInvokeGetter(suAnswerDto, "getCreatedAt"));
						%>
						<div class="suAnswerItem" style="margin-top: 12px;">
							<div class="suMeta" style="margin: 0 0 14px;">
								<div class="suMetaItem">
									<span class="suMetaLabel">작성자</span><span class="suMetaValue"><%=suModalAnswerWriterName%></span>
								</div>
								<div class="suMetaItem">
									<span class="suMetaLabel">등록시간</span><span class="suMetaValue"><%=suModalAnswerCreatedAt%></span>
								</div>
							</div>
							<div class="suFormRow">
								<div class="suLabelBox">상태</div>
								<div>
									<input type="text" class="suField"
										value="<%=suAnswerDto.getStatus() == null ? "" : suAnswerDto.getStatus()%>"
										readonly />
								</div>
							</div>
							<div class="suFormRow">
								<div class="suLabelBox">내용</div>
								<div>
									<textarea class="suField" readonly><%=suAnswerDto.getContent() == null ? "" : suAnswerDto.getContent()%></textarea>
								</div>
							</div>
							<div class="suFormRow">
								<div class="suLabelBox">비고</div>
								<div>
									<input type="text" class="suField"
										value="<%=suAnswerDto.getRemark() == null ? "" : suAnswerDto.getRemark()%>"
										readonly />
								</div>
							</div>
							<div class="suBtnRow">
								<form method="post"
									action="<%=suContextPath + "/suggestion/answerHide"%>"
									style="margin: 0;">
									<input type="hidden" name="answerId"
										value="<%=suAnswerDto.getAnswerId()%>" /> <input
										type="hidden" name="suggestionId"
										value="<%=suSelectedSuggestion.getSuggestionId()%>" />
									<button type="submit" class="suBtn suBtnWarn">댓글내리기</button>
								</form>
								<form method="post"
									action="<%=suContextPath + "/suggestion/answerDelete"%>"
									style="margin: 0;" onsubmit="return confirm('댓글을 삭제하시겠습니까?');">
									<input type="hidden" name="answerId"
										value="<%=suAnswerDto.getAnswerId()%>" /> <input
										type="hidden" name="suggestionId"
										value="<%=suSelectedSuggestion.getSuggestionId()%>" />
									<button type="submit" class="suBtn suBtnDanger">댓글삭제</button>
								</form>
							</div>
						</div>
						<%
						}
						%>
						<%
						} else {
						%>
						<div class="suAnswerEmpty">등록된 댓글이 없습니다.</div>
						<%
						}
						%>
					</div>
				</div>
			</div>
		</div>
	</div>
	<%
	}
	%>
</div>
<%
} else {
%>
<div class="suWrap">
	<div class="suBoardHeader">
		<a href="<%=suContextPath + "/suggestion/list?mode=write"%>"
			class="suBtn suBtnPrimary">등록</a>
	</div>

	<form method="get" action="<%=suContextPath + "/suggestion/list"%>"
		class="suSearchBox" id="suSearchForm">
		<input type="hidden" name="page" id="suPage"
			value="<%=suCurrentPage%>" /> <input type="hidden" name="size"
			value="<%=suSize%>" />
		<div class="suSearchRow">
			<div class="suSearchItem">
				<label for="suStatus">상태</label> <select id="suStatus" name="status">
					<option value="">전체</option>
					<option value="접수" <%="접수".equals(suStatus) ? "selected" : ""%>>접수</option>
					<option value="검토중" <%="검토중".equals(suStatus) ? "selected" : ""%>>검토중</option>
					<option value="반영완료" <%="반영완료".equals(suStatus) ? "selected" : ""%>>답변완료</option>
					<option value="반려" <%="반려".equals(suStatus) ? "selected" : ""%>>반려</option>
					<option value="내림" <%="내림".equals(suStatus) ? "selected" : ""%>>내림</option>
				</select>
			</div>
			<div class="suSearchItem">
				<label for="suDept">부서</label> <select id="suDept" name="deptCode">
					<option value="">전체</option>
					<option value="PD" <%="PD".equals(suDeptCode) ? "selected" : ""%>>PD</option>
					<option value="MS" <%="MS".equals(suDeptCode) ? "selected" : ""%>>MS</option>
					<option value="MT" <%="MT".equals(suDeptCode) ? "selected" : ""%>>MT</option>
					<option value="EQ" <%="EQ".equals(suDeptCode) ? "selected" : ""%>>EQ</option>
				</select>
			</div>
			<div class="suSearchItem">
				<label for="suKeyword">검색어</label> <input type="text" id="suKeyword"
					name="keyword" value="<%=suKeyword%>" placeholder="제목 / 내용 / 작성자" />
			</div>
			<div class="suSearchItem">
				<label>&nbsp;</label>
				<button type="submit" class="suSearchBtnIcon" aria-label="검색"
					onclick="document.getElementById('suPage').value=1;">
					<svg viewBox="0 0 24 24" fill="none" stroke-width="2">
						<circle cx="11" cy="11" r="7"></circle>
						<path d="M20 20L16.65 16.65"></path></svg>
				</button>
			</div>
			<div class="suSearchItem">
				<label>&nbsp;</label> <a
					href="<%=suContextPath + "/suggestion/list"%>" class="suResetBtn">초기화</a>
			</div>
		</div>
	</form>

	<div class="suTableBox" id="suTableBox">
		<div class="suTableTop">
			<p class="suTableCount">
				총 <strong><%=suTotalCount%></strong>건
			</p>
		</div>
		<div class="suTableScroll">
			<table class="suTable">
				<thead>
					<tr>
						<th style="width: 80px;">번호</th>
						<th style="width: 100px;">상태</th>
						<th>제목</th>
						<th style="width: 110px;">부서</th>
						<th style="width: 110px;">작성자</th>
						<th style="width: 90px;">조회수</th>
						<th style="width: 120px;">작성일</th>
						<th style="width: 110px;">댓글등록</th>
						<th style="width: 100px;">상세</th>
					</tr>
				</thead>
				<tbody>
					<%
					if (suSuggestionList == null || suSuggestionList.isEmpty()) {
					%>
					<tr>
						<td colspan="9">조회된 건의사항이 없습니다.</td>
					</tr>
					<%
					} else {
					for (SuggestionDTO suDto : suSuggestionList) {
						String suRowStatusClass = "suStatusReceipt";
						String suRowStatusText = suDto.getStatus() == null ? "" : suDto.getStatus();
						if ("검토중".equals(suRowStatusText)) {
							suRowStatusClass = "suStatusReview";
						} else if ("답변완료".equals(suRowStatusText) || "반영완료".equals(suRowStatusText)) {
							suRowStatusClass = "suStatusDone";
							suRowStatusText = "답변완료";
						} else if ("반려".equals(suRowStatusText)) {
							suRowStatusClass = "suStatusReject";
						}
					%>
					<tr>
						<td><%=suDto.getSuggestionId()%></td>
						<td><span class="suStatusBadge <%=suRowStatusClass%>"><%=suRowStatusText%></span></td>
						<td class="suTitleCell"><a
							href="<%=suContextPath + "/suggestion/list?mode=detail&id=" + suDto.getSuggestionId()%>"
							class="suTitleLink"><%=suDto.getTitle() == null ? "" : suDto.getTitle()%></a></td>
						<td><%=suDto.getDeptCode() == null ? "-" : suDto.getDeptCode()%></td>
						<td><%=suDto.getWriterName() == null ? "-" : suDto.getWriterName()%></td>
						<td><%=suDto.getViewCount()%></td>
						<td><%=suDto.getCreatedAt() == null ? "-" : suDateFormat.format(suDto.getCreatedAt())%></td>
						<td><a
							href="<%=suContextPath + "/suggestion/list?mode=detail&id=" + suDto.getSuggestionId() + "#suAnswerWriteBox"%>"
							class="suViewBtn suReplyBtn">댓글등록</a></td>
						<td><a
							href="<%=suContextPath + "/suggestion/list?mode=detail&id=" + suDto.getSuggestionId()%>"
							class="suViewBtn">상세</a></td>
					</tr>
					<%
					}
					}
					%>
				</tbody>
			</table>
		</div>
		<div class="suPagingWrap">
			<button type="button" class="suPagingBtn" onclick="movePage(1)"
				<%=suCurrentPage <= 1 ? "disabled=\"disabled\"" : ""%>>
				&lt;&lt;</button>

			<button type="button" class="suPagingBtn"
				onclick="movePage(<%=suCurrentPage - 1%>)"
				<%=suCurrentPage <= 1 ? "disabled=\"disabled\"" : ""%>>
				&lt;</button>

			<%
			for (int suPageNo = suStartPage; suPageNo <= suEndPage; suPageNo++) {
			%>
			<button type="button"
				class="suPagingBtn <%=suPageNo == suCurrentPage ? "suPagingBtnActive" : ""%>"
				onclick="movePage(<%=suPageNo%>)">
				<%=suPageNo%>
			</button>
			<%
			}
			%>

			<button type="button" class="suPagingBtn"
				onclick="movePage(<%=suCurrentPage + 1%>)"
				<%=suCurrentPage >= suTotalPage ? "disabled=\"disabled\"" : ""%>>
				&gt;</button>

			<button type="button" class="suPagingBtn"
				onclick="movePage(<%=suTotalPage%>)"
				<%=suCurrentPage >= suTotalPage ? "disabled=\"disabled\"" : ""%>>
				&gt;&gt;</button>
		</div>
	</div>

	<div
		class="suModalWrap <%="write".equals(suMode) ? "suModalShow" : ""%>">
		<div class="suModalBox">
			<div class="suModalHeader">
				<h3 class="suModalTitle">건의 등록</h3>
				<a href="<%=suContextPath + "/suggestion/list"%>"
					class="suModalCloseBtn">×</a>
			</div>
			<div class="suModalBody">
				<div class="suModalInner">
					<form method="post"
						action="<%=suContextPath + "/suggestion/insert"%>">
						<div class="suFormRow">
							<div class="suLabelBox">제목</div>
							<div>
								<input type="text" name="title" class="suField" required />
							</div>
						</div>
						<div class="suFormRow">
							<div class="suLabelBox">내용</div>
							<div>
								<textarea name="content" class="suField" required></textarea>
							</div>
						</div>
						<div class="suFormRow">
							<div class="suLabelBox">비고</div>
							<div>
								<input type="text" name="remark" class="suField" />
							</div>
						</div>
						<div class="suBtnRow">
							<a href="<%=suContextPath + "/suggestion/list"%>"
								class="suBtn suCommentMiniBtn suCommentCancelBtn">취소</a>
							<button type="submit" class="suBtn suBtnPrimary">등록</button>
						</div>
					</form>
				</div>
			</div>
		</div>
	</div>
</div>
<script>
function movePage(suPage) {
    var suTargetPage = suPage;
    var suMinPage = 1;
    var suMaxPage = <%=suTotalPage%>;

    if (suTargetPage < suMinPage) {
        suTargetPage = suMinPage;
    }

    if (suTargetPage > suMaxPage) {
        suTargetPage = suMaxPage;
    }

    sessionStorage.setItem("suggestionMoveToTable", "Y");
    document.getElementById("suPage").value = suTargetPage;
    document.getElementById("suSearchForm").submit();
}

document.addEventListener("DOMContentLoaded", function() {
    var suMoveToTable = sessionStorage.getItem("suggestionMoveToTable");

    if (suMoveToTable === "Y") {
        var suTableBox = document.getElementById("suTableBox");
        if (suTableBox) {
            suTableBox.scrollIntoView({ behavior: "auto", block: "start" });
        }
        sessionStorage.removeItem("suggestionMoveToTable");
    }
});
</script>
<%
}
%>