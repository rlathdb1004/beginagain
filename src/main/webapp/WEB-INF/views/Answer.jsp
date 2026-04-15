<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List"%>
<%@ page import="java.util.ArrayList"%>
<%@ page import="java.text.SimpleDateFormat"%>
<%@ page import="SUGGESTION.DTO.SuggestionDTO"%>
<%@ page import="ANSWER.DTO.AnswerDTO"%>

<%
SuggestionDTO anSelectedSuggestion = (SuggestionDTO) request.getAttribute("selectedSuggestion");
AnswerDTO anSelectedAnswer = (AnswerDTO) request.getAttribute("selectedAnswer");
List<AnswerDTO> anAnswerList = (List<AnswerDTO>) request.getAttribute("answerList");

if (anAnswerList == null) {
	anAnswerList = new ArrayList<AnswerDTO>();
	if (anSelectedAnswer != null) {
		anAnswerList.add(anSelectedAnswer);
	}
}

String anViewMode = request.getParameter("anViewMode");
if (anViewMode == null || "".equals(anViewMode.trim())) {
	anViewMode = "detail";
}

String anContextPath = request.getContextPath();
SimpleDateFormat anDateFormat = new SimpleDateFormat("yyyy-MM-dd");
%>

<style>
.anReplySection {
	width: 100%;
	padding: 24px 28px;
	border: 1px solid #dbe3ec;
	border-radius: 18px;
	background: #fff;
	box-sizing: border-box
}

.anReplySectionTitle {
	margin: 0 0 18px;
	font-size: 22px;
	font-weight: 700;
	color: #0A1E3C
}

.anReplyComment {
	padding: 18px 0;
	border-top: 1px solid #eef2f6
}

.anReplyTop {
	display: flex;
	justify-content: space-between;
	align-items: flex-start;
	gap: 16px;
	margin-bottom: 12px
}

.anReplyInfo {
	display: flex;
	flex-direction: column;
	gap: 8px
}

.anReplyWriter {
	font-size: 16px;
	font-weight: 700;
	color: #0A1E3C
}

.anReplyMeta {
	display: flex;
	flex-wrap: wrap;
	gap: 18px 24px;
	font-size: 13px;
	color: #667085
}

.anReplyMetaItem {
	display: inline-flex;
	align-items: center;
	gap: 8px
}

.anReplyMetaLabel {
	font-weight: 700;
	color: #0A1E3C
}

.anReplyMetaValue {
	color: #667085;
	font-weight: 500
}

.anReplyBody {
	font-size: 15px;
	line-height: 1.9;
	color: #344054;
	white-space: pre-wrap;
	word-break: break-word;
	text-align: left
}

.anReplyRemark {
	margin-top: 14px;
	font-size: 13px;
	line-height: 1.7;
	color: #98a2b3
}

.anReplyEmpty {
	padding: 26px 20px;
	border: 1px dashed #d5dee9;
	border-radius: 16px;
	background: #fbfcfe;
	color: #98a2b3;
	font-size: 15px;
	font-weight: 500;
	text-align: center
}

.anManageSection {
	margin-top: 18px;
	padding: 16px;
	border: 1px solid #eef2f6;
	border-radius: 16px;
	background: #fbfcfe
}

.anManageTitle {
	margin: 0 0 14px;
	font-size: 16px;
	font-weight: 700;
	color: #0A1E3C
}

.anBtn {
	min-width: 90px;
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

.anBtnPrimary {
	background: #0047AB;
	color: #fff
}

.anBtnDanger {
	background: #fff1f3;
	color: #c01048;
	border: 1px solid #ffd0db
}

.anBtnWarn {
	background: #fff4ea;
	color: #d96b00;
	border: 1px solid #ffd7b2
}

.anBtnRow {
	display: flex;
	justify-content: flex-end;
	align-items: center;
	gap: 10px;
	flex-wrap: wrap
}

.anFormRow {
    display: grid;
    grid-template-columns: 140px 1fr;
    gap: 14px;
    margin-bottom: 14px;
    align-items: start;
}

.anLabelBox {
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

.anField {
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

textarea.anField {
    height: 190px;
    min-height: 190px;
    padding: 14px;
    resize: none;
    line-height: 1.6;
}

.anManageMeta {
	display: flex;
	flex-wrap: wrap;
	gap: 18px 24px;
	margin-bottom: 14px;
	font-size: 13px;
	color: #667085;
}

.anManageMetaItem {
	display: inline-flex;
	align-items: center;
	gap: 8px;
}

.anManageMetaLabel {
	font-weight: 700;
	color: #0A1E3C;
}

.anManageMetaValue {
	color: #667085;
	font-weight: 500;
}

@media ( max-width :900px) {
	.anReplyTop, .anFormRow {
		grid-template-columns: 1fr;
		align-items: stretch
	}
	.anReplySection {
		padding: 20px 18px
	}
}
</style>

<%
if ("detail".equals(anViewMode)) {
%>
<div class="anReplySection">
	<h3 class="anReplySectionTitle">답글</h3>
	<%
	if (anAnswerList == null || anAnswerList.isEmpty()) {
	%>
	<div class="anReplyEmpty">아직 등록된 답글이 없습니다.</div>
	<%
	} else {
	%>
	<%
	for (AnswerDTO anDto : anAnswerList) {
	%>
	<div class="anReplyComment">
		<div class="anReplyTop">
			<div class="anReplyInfo">
				<div class="anReplyWriter"><%=anDto.getWriterName() == null ? "-" : anDto.getWriterName()%></div>

				<div class="anReplyMeta">
					<div class="anReplyMetaItem">
						<span class="anReplyMetaLabel">작성일</span> <span
							class="anReplyMetaValue"><%=anDto.getCreatedAt() == null ? "-" : anDateFormat.format(anDto.getCreatedAt())%></span>
					</div>
				</div>
			</div>
		</div>
		<div class="anReplyBody"><%=anDto.getContent() == null ? "" : anDto.getContent()%></div>
		<%
		if (anDto.getRemark() != null && !"".equals(anDto.getRemark().trim())) {
		%>
		<div class="anReplyRemark">
			비고 :
			<%=anDto.getRemark()%></div>
		<%
		}
		%>
	</div>
	<%
	}
	%>
	<%
	}
	%>
</div>
<%
} else {
%>
<div class="anManageSection">
	<h4 class="anManageTitle">답글 작성</h4>

	<form method="post"
		action="<%=anContextPath + "/suggestion/answerInsert"%>">
		<input type="hidden" name="suggestionId"
			value="<%=anSelectedSuggestion == null ? 0 : anSelectedSuggestion.getSuggestionId()%>" />
		<input type="hidden" name="status" value="등록" />

		<div class="anFormRow">
			<div class="anLabelBox">답글내용</div>
			<div>
				<textarea name="content" class="anField" required></textarea>
			</div>
		</div>

		<div class="anFormRow">
			<div class="anLabelBox">비고</div>
			<div>
				<input type="text" name="remark" class="anField" />
			</div>
		</div>

		<div class="anBtnRow">
			<button type="submit" class="anBtn anBtnPrimary">답글쓰기</button>
		</div>
	</form>
</div>

<%
if (anAnswerList != null && !anAnswerList.isEmpty()) {
%>
<%
for (AnswerDTO anDto : anAnswerList) {
%>
<div class="anManageSection">
    <h4 class="anManageTitle">답글 관리</h4>

    <form method="post" action="<%= anContextPath + "/suggestion/answerUpdate" %>">
        <input type="hidden" name="answerId" value="<%= anDto.getAnswerId() %>" />
        <input type="hidden" name="suggestionId" value="<%= anSelectedSuggestion == null ? 0 : anSelectedSuggestion.getSuggestionId() %>" />
        <input type="hidden" name="status" value="<%= anDto.getStatus() == null ? "등록" : anDto.getStatus() %>" />

        <div class="anFormRow">
            <div class="anLabelBox">답글내용</div>
            <div><textarea name="content" class="anField" required><%= anDto.getContent() == null ? "" : anDto.getContent() %></textarea></div>
        </div>

        <div class="anFormRow">
            <div class="anLabelBox">비고</div>
            <div><input type="text" name="remark" class="anField" value="<%= anDto.getRemark() == null ? "" : anDto.getRemark() %>" /></div>
        </div>

        <div class="anBtnRow">
            <button type="submit" class="anBtn anBtnPrimary">답글수정</button>
        </div>
    </form>

    <div class="anBtnRow" style="margin-top:12px;">
        <form method="post" action="<%= anContextPath + "/suggestion/answerHide" %>" style="margin:0;">
            <input type="hidden" name="answerId" value="<%= anDto.getAnswerId() %>" />
            <input type="hidden" name="suggestionId" value="<%= anSelectedSuggestion == null ? 0 : anSelectedSuggestion.getSuggestionId() %>" />
            <button type="submit" class="anBtn anBtnWarn">답글숨김</button>
        </form>

        <form method="post" action="<%= anContextPath + "/suggestion/answerDelete" %>" style="margin:0;">
            <input type="hidden" name="answerId" value="<%= anDto.getAnswerId() %>" />
            <input type="hidden" name="suggestionId" value="<%= anSelectedSuggestion == null ? 0 : anSelectedSuggestion.getSuggestionId() %>" />
            <button type="submit" class="anBtn anBtnDanger">답글삭제</button>
        </form>
    </div>
</div>
<%
}
%>
<%
}
%>
<%
}
%>
