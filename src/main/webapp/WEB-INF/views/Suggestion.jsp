<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="java.util.List" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%@ page import="SUGGESTION.DTO.SuggestionDTO" %>
<%@ page import="ANSWER.DTO.AnswerDTO" %>

<%
    /* =========================================================
       JSP에서 사용할 데이터 받기
       ========================================================= */
    List<SuggestionDTO> suggestionList =
            (List<SuggestionDTO>) request.getAttribute("suggestionList");

    SuggestionDTO selectedSuggestion =
            (SuggestionDTO) request.getAttribute("selectedSuggestion");

    AnswerDTO selectedAnswer =
            (AnswerDTO) request.getAttribute("selectedAnswer");

    String keyword = (String) request.getAttribute("keyword");
    String status = (String) request.getAttribute("status");
    String deptCode = (String) request.getAttribute("deptCode");
    String mode = (String) request.getAttribute("mode");
    String modal = (String) request.getAttribute("modal");

    if (keyword == null) keyword = "";
    if (status == null) status = "";
    if (deptCode == null) deptCode = "";
    if (mode == null || mode.trim().isEmpty()) mode = "list";
    if (modal == null) modal = "";

    int size = request.getAttribute("size") == null ? 10 : (Integer) request.getAttribute("size");
    int currentPage = request.getAttribute("page") == null ? 1 : (Integer) request.getAttribute("page");
    int totalCount = request.getAttribute("totalCount") == null ? 0 : (Integer) request.getAttribute("totalCount");
    int totalPage = request.getAttribute("totalPage") == null ? 1 : (Integer) request.getAttribute("totalPage");
    int startPage = request.getAttribute("startPage") == null ? 1 : (Integer) request.getAttribute("startPage");
    int endPage = request.getAttribute("endPage") == null ? 1 : (Integer) request.getAttribute("endPage");

    boolean canEditSuggestion = request.getAttribute("canEditSuggestion") == null ? false : (Boolean) request.getAttribute("canEditSuggestion");
    boolean canDeleteSuggestion = request.getAttribute("canDeleteSuggestion") == null ? false : (Boolean) request.getAttribute("canDeleteSuggestion");
    boolean canWriteAnswer = request.getAttribute("canWriteAnswer") == null ? false : (Boolean) request.getAttribute("canWriteAnswer");
    boolean canEditAnswer = request.getAttribute("canEditAnswer") == null ? false : (Boolean) request.getAttribute("canEditAnswer");
    boolean canDeleteAnswer = request.getAttribute("canDeleteAnswer") == null ? false : (Boolean) request.getAttribute("canDeleteAnswer");

    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

    /* =========================================================
       게시글 상태 배지 텍스트 / 클래스 가공
       ========================================================= */
    String suSelectedStatusClass = "suStatusReceipt";
    String suSelectedStatusText = "";

    if (selectedSuggestion != null) {
        suSelectedStatusText = selectedSuggestion.getStatus();

        if ("검토중".equals(selectedSuggestion.getStatus())) {
            suSelectedStatusClass = "suStatusReview";
        } else if ("답변완료".equals(selectedSuggestion.getStatus()) || "반영완료".equals(selectedSuggestion.getStatus())) {
            suSelectedStatusClass = "suStatusDone";
            suSelectedStatusText = "답변완료";
        } else if ("반려".equals(selectedSuggestion.getStatus())) {
            suSelectedStatusClass = "suStatusReject";
        }
    }
%>

<style>
/* =========================================================
   공통 래퍼
   ========================================================= */
.suBoardWrap,
.suDetailWrap {
    width: 100%;
    display: flex;
    flex-direction: column;
    box-sizing: border-box;
}

/* =========================================================
   검색 영역
   ========================================================= */
.suSearchBox {
    width: 100%;
    margin-bottom: 16px;
    padding: 18px;
    border: 1px solid #dbe3ec;
    border-radius: 18px;
    background: #ffffff;
    box-sizing: border-box;
}

.suSearchRow {
    display: grid;
    grid-template-columns: 180px 180px 1fr auto auto;
    gap: 12px;
    align-items: end;
}

.suSearchItem {
    display: flex;
    flex-direction: column;
    gap: 8px;
}

.suSearchItem label {
    font-size: 14px;
    font-weight: 600;
    color: #0A1E3C;
    line-height: 1;
}

.suSearchItem input,
.suSearchItem select,
.suField,
.suAnswerField {
    width: 100%;
    min-height: 40px;
    padding: 0 14px;
    border: 1px solid #ccd7e3;
    border-radius: 12px;
    background: #ffffff;
    box-sizing: border-box;
    font-size: 14px;
    font-weight: 400;
    color: #344054;
    outline: none;
}

.suSearchItem input::placeholder,
.suField::placeholder,
.suAnswerField::placeholder {
    color: #98a2b3;
    font-weight: 400;
}

.suSearchBtnIcon {
    width: 44px;
    height: 40px;
    border: 1px solid #ccd7e3;
    border-radius: 12px;
    background: #ffffff;
    display: inline-flex;
    align-items: center;
    justify-content: center;
    cursor: pointer;
    padding: 0;
    box-sizing: border-box;
}

.suSearchBtnIcon svg {
    width: 18px;
    height: 18px;
    stroke: #0A1E3C;
}

.suResetBtn {
    min-width: 84px;
    height: 40px;
    padding: 0 16px;
    border: 1px solid #ccd7e3;
    border-radius: 12px;
    background: #ffffff;
    color: #0A1E3C;
    font-size: 14px;
    font-weight: 500;
    text-decoration: none;
    display: inline-flex;
    align-items: center;
    justify-content: center;
    box-sizing: border-box;
    cursor: pointer;
}

/* =========================================================
   공통 버튼
   ========================================================= */
.suBoardHeader,
.suBtnRow {
    display: flex;
    justify-content: flex-end;
    align-items: center;
    gap: 10px;
    margin-bottom: 16px;
    flex-wrap: wrap;
}

.suBtn {
    min-width: 90px;
    height: 40px;
    padding: 0 16px;
    border: none;
    border-radius: 12px;
    box-sizing: border-box;
    font-size: 14px;
    font-weight: 600;
    cursor: pointer;
    text-decoration: none;
    display: inline-flex;
    align-items: center;
    justify-content: center;
}

.suBtnPrimary {
    background: #0047AB;
    color: #ffffff;
}

.suBtnGray {
    background: #ffffff;
    color: #344054;
    border: 1px solid #ccd7e3;
}

.suBtnDanger {
    background: #fff1f3;
    color: #c01048;
    border: 1px solid #ffd0db;
}

/* =========================================================
   목록 테이블
   ========================================================= */
.suTableBox {
    width: 100%;
    padding: 18px;
    border: 1px solid #dbe3ec;
    border-radius: 18px;
    background: #ffffff;
    box-sizing: border-box;
}

.suTableTop {
    padding: 0 2px 12px;
}

.suTableCount {
    margin: 0;
    font-size: 15px;
    font-weight: 600;
    color: #667085;
}

.suTableCount strong {
    color: #0047AB;
    font-weight: 700;
}

.suTableScroll {
    width: 100%;
    overflow-x: auto;
    box-sizing: border-box;
}

.suSuggestionTable {
    width: 100%;
    min-width: 1080px;
    border-collapse: collapse;
    table-layout: fixed;
}

.suSuggestionTable thead th {
    padding: 14px 12px;
    background: #eef3f8;
    border-top: 1px solid #dbe3ec;
    border-bottom: 1px solid #dbe3ec;
    text-align: center;
    font-size: 14px;
    font-weight: 600;
    color: #0A1E3C;
    white-space: nowrap;
}

.suSuggestionTable tbody td {
    padding: 14px 12px;
    border-bottom: 1px solid #edf2f7;
    text-align: center;
    vertical-align: middle;
    font-size: 14px;
    font-weight: 400;
    color: #344054;
    background: #ffffff;
}

.suSuggestionTable tbody tr:hover td {
    background: #fafcff;
}

.suTitleCell {
    text-align: left !important;
}

.suTitleLink {
    display: inline-block;
    max-width: 100%;
    overflow: hidden;
    text-overflow: ellipsis;
    white-space: nowrap;
    vertical-align: middle;
    color: #0A1E3C;
    font-weight: 600;
    text-decoration: none;
}

.suTitleLink:hover {
    color: #0047AB;
    text-decoration: underline;
}

.suViewBtn {
    display: inline-flex;
    align-items: center;
    justify-content: center;
    min-width: 76px;
    height: 34px;
    padding: 0 12px;
    border: 1px solid #ccd7e3;
    border-radius: 999px;
    box-sizing: border-box;
    background: #ffffff;
    color: #344054;
    text-decoration: none;
    font-size: 13px;
    font-weight: 500;
    cursor: pointer;
}

.suViewBtn:hover {
    border-color: #0047AB;
    color: #0047AB;
}

.suEmptyRow {
    padding: 60px 0 !important;
    color: #98a2b3 !important;
    font-weight: 500;
}

/* =========================================================
   상태 배지
   ========================================================= */
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
    font-weight: 600;
    border: 1px solid transparent;
    flex-shrink: 0;
}

.suStatusReceipt {
    background: #ecf8f1;
    color: #0a7a46;
}

.suStatusReview {
    background: #eef3f8;
    color: #4b5565;
}

.suStatusDone {
    background: #e9f1ff;
    color: #0047AB;
}

.suStatusReject {
    background: #fff4ea;
    color: #d96b00;
}

/* =========================================================
   페이징
   ========================================================= */
.suPagingWrap {
    display: flex;
    justify-content: center;
    align-items: center;
    gap: 10px;
    flex-wrap: wrap;
    margin-top: 18px;
}

.suPagingBtn {
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
    font-size: 14px;
    font-weight: 600;
    line-height: 1;
    cursor: pointer;
}

.suPagingBtnActive {
    background: #e9f1ff;
    color: #0047AB;
    border-color: #d5e4ff;
}

.suPagingBtn:disabled {
    color: #c0c7d1;
    cursor: default;
}

/* =========================================================
   상세 화면
   - 게시글 보기 + 댓글 보기 형태
   ========================================================= */
.suDetailWrap {
    gap: 18px;
}

.suPostCard,
.suCommentCard {
    width: 100%;
    padding: 22px 24px;
    border: 1px solid #dbe3ec;
    border-radius: 18px;
    background: #ffffff;
    box-sizing: border-box;
}

/* 게시글 제목 / 상태 */
.suPostHeader {
    display: flex;
    justify-content: space-between;
    align-items: flex-start;
    gap: 16px;
    margin-bottom: 14px;
    padding-bottom: 14px;
    border-bottom: 1px solid #eef2f6;
}

.suPostTitle {
    margin: 0;
    font-size: 28px;
    font-weight: 700;
    color: #0A1E3C;
    line-height: 1.4;
    word-break: break-word;
}

/* 게시글 메타정보 */
.suPostMeta,
.suCommentMeta {
    display: flex;
    flex-wrap: wrap;
    gap: 18px 28px;
    margin-bottom: 20px;
    font-size: 14px;
    color: #667085;
    line-height: 1.5;
}

.suMetaItem {
    display: inline-flex;
    align-items: center;
    gap: 8px;
}

.suMetaLabel {
    font-weight: 700;
    color: #0A1E3C;
}

.suMetaValue {
    color: #475467;
    font-weight: 500;
}

/* 게시글 본문 */
.suPostBody {
    min-height: 260px;
    padding: 24px;
    border: 1px solid #e7edf4;
    border-radius: 16px;
    background: #fbfcfe;
    box-sizing: border-box;
    color: #344054;
    font-size: 16px;
    line-height: 1.9;
    white-space: pre-wrap;
    word-break: break-word;
}

/* 게시글 비고 */
.suPostRemark {
    margin-top: 16px;
    padding: 14px 16px;
    border-radius: 12px;
    background: #f8fafc;
    border: 1px solid #eef2f6;
    font-size: 14px;
    color: #667085;
    line-height: 1.7;
}

/* 댓글 섹션 제목 */
.suCommentSectionTitle {
    margin: 0 0 16px;
    font-size: 22px;
    font-weight: 700;
    color: #0A1E3C;
}

/* 댓글 한 개 */
.suCommentItem {
    padding: 18px 0 0;
    border-top: 1px solid #eef2f6;
}

.suCommentTop {
    display: flex;
    justify-content: space-between;
    align-items: flex-start;
    gap: 16px;
    margin-bottom: 12px;
}

.suCommentWriter {
    font-size: 16px;
    font-weight: 700;
    color: #0A1E3C;
    margin-bottom: 8px;
}

.suCommentBody {
    padding-top: 6px;
    color: #344054;
    font-size: 15px;
    line-height: 1.85;
    white-space: pre-wrap;
    word-break: break-word;
}

.suCommentEmpty {
    padding: 26px 20px;
    border: 1px dashed #d5dee9;
    border-radius: 16px;
    background: #fbfcfe;
    color: #98a2b3;
    font-size: 15px;
    font-weight: 500;
    text-align: center;
}

/* 상세 하단 버튼 */
.suDetailBottomActions {
    display: flex;
    justify-content: flex-end;
    align-items: center;
    gap: 10px;
    flex-wrap: wrap;
}

/* =========================================================
   모달 공통
   ========================================================= */
.suModalWrap {
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

.suModalWrap.suModalShow {
    display: flex;
}

.suModalBox {
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

.suModalBoxSmall {
    width: 560px;
    max-width: 95%;
}

.suModalHeader {
    display: flex;
    align-items: center;
    justify-content: space-between;
    gap: 10px;
    padding: 18px 22px;
    background: #ffffff;
    border-bottom: 1px solid #eef2f6;
    color: #0A1E3C;
}

.suModalTitle {
    margin: 0;
    font-size: 18px;
    font-weight: 700;
    color: #0A1E3C;
}

.suModalCloseBtn {
    border: none;
    background: transparent;
    color: #667085;
    font-size: 28px;
    line-height: 1;
    cursor: pointer;
    text-decoration: none;
}

.suModalBody {
    padding: 18px 22px 22px;
    overflow-y: auto;
    box-sizing: border-box;
    background: #ffffff;
}

.suModalInner {
    border: 1px solid #e7edf4;
    border-radius: 16px;
    padding: 16px;
    background: #ffffff;
    box-sizing: border-box;
}

.suFormRow {
    display: grid;
    grid-template-columns: 140px 1fr;
    gap: 14px;
    margin-bottom: 14px;
    align-items: stretch;
}

.suLabelBox {
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

textarea.suField,
textarea.suAnswerField {
    height: 180px;
    min-height: 180px;
    padding: 14px;
    resize: none;
    line-height: 1.6;
}

.suInfoText {
    color: #667085;
    font-size: 14px;
    line-height: 1.7;
}

/* =========================================================
   반응형
   ========================================================= */
@media (max-width: 900px) {
    .suSearchRow {
        grid-template-columns: 1fr;
    }

    .suFormRow {
        grid-template-columns: 1fr;
    }

    .suPostHeader,
    .suCommentTop {
        flex-direction: column;
        align-items: stretch;
    }

    .suPostTitle {
        font-size: 22px;
    }

    .suPostBody {
        padding: 18px;
    }
}
</style>

<% if ("detail".equals(mode)) { %>

    <!-- =====================================================
         상세 화면
         ===================================================== -->
    <section class="suDetailWrap">

        <% if (selectedSuggestion == null) { %>

            <div class="suPostCard">
                <div class="suCommentEmpty">존재하지 않는 건의사항입니다.</div>

                <div class="suDetailBottomActions" style="margin-top:16px;">
                    <a href="<%= request.getContextPath() %>/suggestion/list" class="suBtn suBtnGray">목록</a>
                </div>
            </div>

        <% } else { %>

            <!-- 게시글 -->
            <div class="suPostCard">
                <div class="suPostHeader">
                    <h2 class="suPostTitle"><%= selectedSuggestion.getTitle() %></h2>
                    <span class="suStatusBadge <%= suSelectedStatusClass %>"><%= suSelectedStatusText %></span>
                </div>

                <div class="suPostMeta">
                    <div class="suMetaItem">
                        <span class="suMetaLabel">번호</span>
                        <span class="suMetaValue"><%= selectedSuggestion.getSuggestionId() %></span>
                    </div>

                    <div class="suMetaItem">
                        <span class="suMetaLabel">부서</span>
                        <span class="suMetaValue"><%= selectedSuggestion.getDeptCode() == null ? "-" : selectedSuggestion.getDeptCode() %></span>
                    </div>

                    <div class="suMetaItem">
                        <span class="suMetaLabel">작성자</span>
                        <span class="suMetaValue"><%= selectedSuggestion.getWriterName() == null ? "-" : selectedSuggestion.getWriterName() %></span>
                    </div>

                    <div class="suMetaItem">
                        <span class="suMetaLabel">작성일</span>
                        <span class="suMetaValue"><%= selectedSuggestion.getCreatedAt() == null ? "-" : sdf.format(selectedSuggestion.getCreatedAt()) %></span>
                    </div>

                    <div class="suMetaItem">
                        <span class="suMetaLabel">조회수</span>
                        <span class="suMetaValue"><%= selectedSuggestion.getViewCount() %></span>
                    </div>
                </div>

                <div class="suPostBody">
                    <%= selectedSuggestion.getContent() %>
                </div>

                <% if (selectedSuggestion.getRemark() != null && !"".equals(selectedSuggestion.getRemark().trim())) { %>
                    <div class="suPostRemark">
                        <strong style="color:#0A1E3C;">비고</strong><br>
                        <%= selectedSuggestion.getRemark() %>
                    </div>
                <% } %>
            </div>

            <!-- 답글 -->
            <div class="suCommentCard">
                <h3 class="suCommentSectionTitle">답글</h3>

                <% if (selectedAnswer != null) { %>

                    <div class="suCommentItem">
                        <div class="suCommentTop">
                            <div>
                                <div class="suCommentWriter"><%= selectedAnswer.getWriterName() == null ? "-" : selectedAnswer.getWriterName() %></div>

                                <div class="suCommentMeta">
                                    <div class="suMetaItem">
                                        <span class="suMetaLabel">작성일</span>
                                        <span class="suMetaValue"><%= selectedAnswer.getCreatedAt() == null ? "-" : sdf.format(selectedAnswer.getCreatedAt()) %></span>
                                    </div>

                                    <div class="suMetaItem">
                                        <span class="suMetaLabel">상태</span>
                                        <span class="suMetaValue"><%= selectedAnswer.getStatus() == null ? "-" : selectedAnswer.getStatus() %></span>
                                    </div>
                                </div>
                            </div>

                            <div class="suBtnRow" style="margin-top:0; margin-bottom:0;">
                                <% if (canEditAnswer) { %>
                                    <a href="<%= request.getContextPath() %>/suggestion/list?mode=detail&id=<%= selectedSuggestion.getSuggestionId() %>&modal=answerEdit"
                                       class="suBtn suBtnPrimary">답글수정</a>
                                <% } %>

                                <% if (canDeleteAnswer) { %>
                                    <a href="<%= request.getContextPath() %>/suggestion/list?mode=detail&id=<%= selectedSuggestion.getSuggestionId() %>&modal=answerDelete"
                                       class="suBtn suBtnDanger">답글삭제</a>
                                <% } %>
                            </div>
                        </div>

                        <div class="suCommentBody">
                            <%= selectedAnswer.getContent() == null ? "" : selectedAnswer.getContent() %>
                        </div>

                        <% if (selectedAnswer.getRemark() != null && !"".equals(selectedAnswer.getRemark().trim())) { %>
                            <div class="suPostRemark" style="margin-top:16px;">
                                <strong style="color:#0A1E3C;">비고</strong><br>
                                <%= selectedAnswer.getRemark() %>
                            </div>
                        <% } %>
                    </div>

                <% } else { %>

                    <div class="suCommentEmpty">
                        아직 등록된 답글이 없습니다.
                    </div>

                    <div class="suBtnRow" style="margin-top:16px;">
                        <% if (canWriteAnswer) { %>
                            <a href="<%= request.getContextPath() %>/suggestion/list?mode=detail&id=<%= selectedSuggestion.getSuggestionId() %>&modal=answerWrite"
                               class="suBtn suBtnPrimary">답글작성</a>
                        <% } %>
                    </div>

                <% } %>
            </div>

            <!-- 하단 버튼 -->
            <div class="suDetailBottomActions">
                <a href="<%= request.getContextPath() %>/suggestion/list" class="suBtn suBtnGray">목록</a>

                <% if (canEditSuggestion) { %>
                    <a href="<%= request.getContextPath() %>/suggestion/list?mode=detail&id=<%= selectedSuggestion.getSuggestionId() %>&modal=suggestionEdit"
                       class="suBtn suBtnPrimary">수정</a>
                <% } %>

                <% if (canDeleteSuggestion) { %>
                    <a href="<%= request.getContextPath() %>/suggestion/list?mode=detail&id=<%= selectedSuggestion.getSuggestionId() %>&modal=suggestionDelete"
                       class="suBtn suBtnDanger">삭제</a>
                <% } %>
            </div>

            <!-- 게시글 수정 모달 -->
            <div class="suModalWrap <%= "suggestionEdit".equals(modal) && canEditSuggestion ? "suModalShow" : "" %>">
                <div class="suModalBox">
                    <div class="suModalHeader">
                        <h3 class="suModalTitle">건의 수정</h3>
                        <a href="<%= request.getContextPath() %>/suggestion/list?mode=detail&id=<%= selectedSuggestion.getSuggestionId() %>"
                           class="suModalCloseBtn">×</a>
                    </div>

                    <div class="suModalBody">
                        <div class="suModalInner">
                            <form method="post" action="<%= request.getContextPath() %>/suggestion/update">
                                <input type="hidden" name="suggestionId" value="<%= selectedSuggestion.getSuggestionId() %>" />

                                <div class="suFormRow">
                                    <div class="suLabelBox">제목</div>
                                    <div>
                                        <input type="text"
                                               name="title"
                                               class="suField"
                                               value="<%= selectedSuggestion.getTitle() %>"
                                               required />
                                    </div>
                                </div>

                                <div class="suFormRow">
                                    <div class="suLabelBox">내용</div>
                                    <div>
                                        <textarea name="content" class="suField" required><%= selectedSuggestion.getContent() %></textarea>
                                    </div>
                                </div>

                                <div class="suFormRow">
                                    <div class="suLabelBox">상태</div>
                                    <div>
                                        <select name="status" class="suField">
                                            <option value="접수" <%= "접수".equals(selectedSuggestion.getStatus()) ? "selected" : "" %>>접수</option>
                                            <option value="검토중" <%= "검토중".equals(selectedSuggestion.getStatus()) ? "selected" : "" %>>검토중</option>
                                            <option value="반영완료" <%= "반영완료".equals(selectedSuggestion.getStatus()) ? "selected" : "" %>>답변완료</option>
                                            <option value="반려" <%= "반려".equals(selectedSuggestion.getStatus()) ? "selected" : "" %>>반려</option>
                                        </select>
                                    </div>
                                </div>

                                <div class="suFormRow">
                                    <div class="suLabelBox">비고</div>
                                    <div>
                                        <input type="text"
                                               name="remark"
                                               class="suField"
                                               value="<%= selectedSuggestion.getRemark() == null ? "" : selectedSuggestion.getRemark() %>" />
                                    </div>
                                </div>

                                <div class="suBtnRow">
                                    <a href="<%= request.getContextPath() %>/suggestion/list?mode=detail&id=<%= selectedSuggestion.getSuggestionId() %>"
                                       class="suBtn suBtnGray">취소</a>
                                    <button type="submit" class="suBtn suBtnPrimary">수정</button>
                                </div>
                            </form>
                        </div>
                    </div>
                </div>
            </div>

            <!-- 게시글 삭제 모달 -->
            <div class="suModalWrap <%= "suggestionDelete".equals(modal) && canDeleteSuggestion ? "suModalShow" : "" %>">
                <div class="suModalBox suModalBoxSmall">
                    <div class="suModalHeader">
                        <h3 class="suModalTitle">건의 삭제</h3>
                        <a href="<%= request.getContextPath() %>/suggestion/list?mode=detail&id=<%= selectedSuggestion.getSuggestionId() %>"
                           class="suModalCloseBtn">×</a>
                    </div>

                    <div class="suModalBody">
                        <div class="suModalInner">
                            <p class="suInfoText">정말 이 건의사항을 삭제하시겠습니까?</p>

                            <div class="suBtnRow" style="margin-top:16px;">
                                <a href="<%= request.getContextPath() %>/suggestion/list?mode=detail&id=<%= selectedSuggestion.getSuggestionId() %>"
                                   class="suBtn suBtnGray">취소</a>

                                <form method="post" action="<%= request.getContextPath() %>/suggestion/delete" style="margin:0;">
                                    <input type="hidden" name="suggestionId" value="<%= selectedSuggestion.getSuggestionId() %>" />
                                    <button type="submit" class="suBtn suBtnDanger">삭제</button>
                                </form>
                            </div>
                        </div>
                    </div>
                </div>
            </div>

            <!-- 답글 등록 모달 -->
            <div class="suModalWrap <%= "answerWrite".equals(modal) && canWriteAnswer ? "suModalShow" : "" %>">
                <div class="suModalBox">
                    <div class="suModalHeader">
                        <h3 class="suModalTitle">답글 작성</h3>
                        <a href="<%= request.getContextPath() %>/suggestion/list?mode=detail&id=<%= selectedSuggestion.getSuggestionId() %>"
                           class="suModalCloseBtn">×</a>
                    </div>

                    <div class="suModalBody">
                        <div class="suModalInner">
                            <form method="post" action="<%= request.getContextPath() %>/suggestion/answerInsert">
                                <input type="hidden" name="suggestionId" value="<%= selectedSuggestion.getSuggestionId() %>" />

                                <div class="suFormRow">
                                    <div class="suLabelBox">답글상태</div>
                                    <div>
                                        <input type="text" name="status" class="suAnswerField" value="등록" />
                                    </div>
                                </div>

                                <div class="suFormRow">
                                    <div class="suLabelBox">답글내용</div>
                                    <div>
                                        <textarea name="content" class="suAnswerField" required></textarea>
                                    </div>
                                </div>

                                <div class="suFormRow">
                                    <div class="suLabelBox">비고</div>
                                    <div>
                                        <input type="text" name="remark" class="suAnswerField" />
                                    </div>
                                </div>

                                <div class="suBtnRow">
                                    <a href="<%= request.getContextPath() %>/suggestion/list?mode=detail&id=<%= selectedSuggestion.getSuggestionId() %>"
                                       class="suBtn suBtnGray">취소</a>
                                    <button type="submit" class="suBtn suBtnPrimary">등록</button>
                                </div>
                            </form>
                        </div>
                    </div>
                </div>
            </div>

            <!-- 답글 수정 모달 -->
            <div class="suModalWrap <%= "answerEdit".equals(modal) && canEditAnswer && selectedAnswer != null ? "suModalShow" : "" %>">
                <div class="suModalBox">
                    <div class="suModalHeader">
                        <h3 class="suModalTitle">답글 수정</h3>
                        <a href="<%= request.getContextPath() %>/suggestion/list?mode=detail&id=<%= selectedSuggestion.getSuggestionId() %>"
                           class="suModalCloseBtn">×</a>
                    </div>

                    <div class="suModalBody">
                        <div class="suModalInner">
                            <form method="post" action="<%= request.getContextPath() %>/suggestion/answerUpdate">
                                <input type="hidden" name="answerId" value="<%= selectedAnswer == null ? 0 : selectedAnswer.getAnswerId() %>" />
                                <input type="hidden" name="suggestionId" value="<%= selectedSuggestion.getSuggestionId() %>" />

                                <div class="suFormRow">
                                    <div class="suLabelBox">답글상태</div>
                                    <div>
                                        <input type="text"
                                               name="status"
                                               class="suAnswerField"
                                               value="<%= selectedAnswer == null || selectedAnswer.getStatus() == null ? "" : selectedAnswer.getStatus() %>" />
                                    </div>
                                </div>

                                <div class="suFormRow">
                                    <div class="suLabelBox">답글내용</div>
                                    <div>
                                        <textarea name="content" class="suAnswerField" required><%= selectedAnswer == null || selectedAnswer.getContent() == null ? "" : selectedAnswer.getContent() %></textarea>
                                    </div>
                                </div>

                                <div class="suFormRow">
                                    <div class="suLabelBox">비고</div>
                                    <div>
                                        <input type="text"
                                               name="remark"
                                               class="suAnswerField"
                                               value="<%= selectedAnswer == null || selectedAnswer.getRemark() == null ? "" : selectedAnswer.getRemark() %>" />
                                    </div>
                                </div>

                                <div class="suBtnRow">
                                    <a href="<%= request.getContextPath() %>/suggestion/list?mode=detail&id=<%= selectedSuggestion.getSuggestionId() %>"
                                       class="suBtn suBtnGray">취소</a>
                                    <button type="submit" class="suBtn suBtnPrimary">수정</button>
                                </div>
                            </form>
                        </div>
                    </div>
                </div>
            </div>

            <!-- 답글 삭제 모달 -->
            <div class="suModalWrap <%= "answerDelete".equals(modal) && canDeleteAnswer && selectedAnswer != null ? "suModalShow" : "" %>">
                <div class="suModalBox suModalBoxSmall">
                    <div class="suModalHeader">
                        <h3 class="suModalTitle">답글 삭제</h3>
                        <a href="<%= request.getContextPath() %>/suggestion/list?mode=detail&id=<%= selectedSuggestion.getSuggestionId() %>"
                           class="suModalCloseBtn">×</a>
                    </div>

                    <div class="suModalBody">
                        <div class="suModalInner">
                            <p class="suInfoText">정말 이 답글을 삭제하시겠습니까?</p>

                            <div class="suBtnRow" style="margin-top:16px;">
                                <a href="<%= request.getContextPath() %>/suggestion/list?mode=detail&id=<%= selectedSuggestion.getSuggestionId() %>"
                                   class="suBtn suBtnGray">취소</a>

                                <form method="post" action="<%= request.getContextPath() %>/suggestion/answerDelete" style="margin:0;">
                                    <input type="hidden" name="answerId" value="<%= selectedAnswer == null ? 0 : selectedAnswer.getAnswerId() %>" />
                                    <input type="hidden" name="suggestionId" value="<%= selectedSuggestion.getSuggestionId() %>" />
                                    <button type="submit" class="suBtn suBtnDanger">삭제</button>
                                </form>
                            </div>
                        </div>
                    </div>
                </div>
            </div>

        <% } %>
    </section>

<% } else { %>

    <!-- =====================================================
         목록 화면
         ===================================================== -->
    <section class="suBoardWrap">

        <div class="suBoardHeader">
            <a href="<%= request.getContextPath() %>/suggestion/list?mode=write" class="suBtn suBtnPrimary">
                등록
            </a>
        </div>

        <form method="get"
              action="<%= request.getContextPath() %>/suggestion/list"
              class="suSearchBox"
              id="suSearchForm">

            <input type="hidden" name="page" id="suPage" value="<%= currentPage %>" />
            <input type="hidden" name="size" value="<%= size %>" />

            <div class="suSearchRow">
                <div class="suSearchItem">
                    <label for="suStatus">상태</label>
                    <select id="suStatus" name="status">
                        <option value="">전체</option>
                        <option value="접수" <%= "접수".equals(status) ? "selected" : "" %>>접수</option>
                        <option value="검토중" <%= "검토중".equals(status) ? "selected" : "" %>>검토중</option>
                        <option value="반영완료" <%= "반영완료".equals(status) ? "selected" : "" %>>답변완료</option>
                        <option value="반려" <%= "반려".equals(status) ? "selected" : "" %>>반려</option>
                    </select>
                </div>

                <div class="suSearchItem">
                    <label for="suDept">부서</label>
                    <select id="suDept" name="deptCode">
                        <option value="">전체</option>
                        <option value="PD" <%= "PD".equals(deptCode) ? "selected" : "" %>>PD</option>
                        <option value="MS" <%= "MS".equals(deptCode) ? "selected" : "" %>>MS</option>
                        <option value="MT" <%= "MT".equals(deptCode) ? "selected" : "" %>>MT</option>
                        <option value="EQ" <%= "EQ".equals(deptCode) ? "selected" : "" %>>EQ</option>
                    </select>
                </div>

                <div class="suSearchItem">
                    <label for="suKeyword">검색어</label>
                    <input type="text"
                           id="suKeyword"
                           name="keyword"
                           value="<%= keyword %>"
                           placeholder="제목 / 내용 / 작성자" />
                </div>

                <div class="suSearchItem">
                    <label>&nbsp;</label>
                    <button type="submit"
                            class="suSearchBtnIcon"
                            aria-label="검색"
                            onclick="document.getElementById('suPage').value=1;">
                        <svg viewBox="0 0 24 24" fill="none" stroke-width="2">
                            <circle cx="11" cy="11" r="7"></circle>
                            <path d="M20 20L16.65 16.65"></path>
                        </svg>
                    </button>
                </div>

                <div class="suSearchItem">
                    <label>&nbsp;</label>
                    <a href="<%= request.getContextPath() %>/suggestion/list" class="suResetBtn">초기화</a>
                </div>
            </div>
        </form>

        <section class="suTableBox" id="suTableBox">
            <div class="suTableTop">
                <p class="suTableCount">총 <strong><%= totalCount %></strong>건</p>
            </div>

            <div class="suTableScroll">
                <table class="suSuggestionTable">
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
                    <%
                        if (suggestionList == null || suggestionList.isEmpty()) {
                    %>
                        <tr>
                            <td colspan="8" class="suEmptyRow">조회된 건의사항이 없습니다.</td>
                        </tr>
                    <%
                        } else {
                            for (SuggestionDTO dto : suggestionList) {
                                String suStatusClass = "suStatusReceipt";
                                String suStatusText = dto.getStatus();

                                if ("검토중".equals(dto.getStatus())) {
                                    suStatusClass = "suStatusReview";
                                } else if ("답변완료".equals(dto.getStatus()) || "반영완료".equals(dto.getStatus())) {
                                    suStatusClass = "suStatusDone";
                                    suStatusText = "답변완료";
                                } else if ("반려".equals(dto.getStatus())) {
                                    suStatusClass = "suStatusReject";
                                }
                    %>
                        <tr>
                            <td><%= dto.getSuggestionId() %></td>
                            <td>
                                <span class="suStatusBadge <%= suStatusClass %>"><%= suStatusText %></span>
                            </td>
                            <td class="suTitleCell">
                                <a href="<%= request.getContextPath() %>/suggestion/list?mode=detail&id=<%= dto.getSuggestionId() %>"
                                   class="suTitleLink"
                                   title="<%= dto.getTitle() %>">
                                    <%= dto.getTitle() %>
                                </a>
                            </td>
                            <td><%= dto.getDeptCode() %></td>
                            <td><%= dto.getWriterName() %></td>
                            <td><%= dto.getViewCount() %></td>
                            <td><%= dto.getCreatedAt() == null ? "-" : sdf.format(dto.getCreatedAt()) %></td>
                            <td>
                                <a href="<%= request.getContextPath() %>/suggestion/list?mode=detail&id=<%= dto.getSuggestionId() %>"
                                   class="suViewBtn">보기</a>
                            </td>
                        </tr>
                    <%
                            }
                        }
                    %>
                    </tbody>
                </table>
            </div>

            <div class="suPagingWrap">
                <button type="button"
                        class="suPagingBtn"
                        onclick="movePage(<%= currentPage - 1 %>)"
                        <%= currentPage <= 1 ? "disabled" : "" %>>
                    &lt;&lt;
                </button>

                <%
                    for (int i = startPage; i <= endPage; i++) {
                %>
                    <button type="button"
                            class="suPagingBtn <%= i == currentPage ? "suPagingBtnActive" : "" %>"
                            onclick="movePage(<%= i %>)">
                        <%= i %>
                    </button>
                <%
                    }
                %>

                <button type="button"
                        class="suPagingBtn"
                        onclick="movePage(<%= currentPage + 1 %>)"
                        <%= currentPage >= totalPage ? "disabled" : "" %>>
                    &gt;&gt;
                </button>
            </div>
        </section>
    </section>

    <!-- 등록 모달 -->
    <div class="suModalWrap <%= "write".equals(mode) ? "suModalShow" : "" %>">
        <div class="suModalBox">
            <div class="suModalHeader">
                <h3 class="suModalTitle">건의 등록</h3>
                <a href="<%= request.getContextPath() %>/suggestion/list" class="suModalCloseBtn">×</a>
            </div>

            <div class="suModalBody">
                <div class="suModalInner">
                    <form method="post" action="<%= request.getContextPath() %>/suggestion/insert">
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
                            <a href="<%= request.getContextPath() %>/suggestion/list" class="suBtn suBtnGray">취소</a>
                            <button type="submit" class="suBtn suBtnPrimary">등록</button>
                        </div>
                    </form>
                </div>
            </div>
        </div>
    </div>

    <script>
    function movePage(page) {
        sessionStorage.setItem("suggestionMoveToTable", "Y");
        document.getElementById("suPage").value = page;
        document.getElementById("suSearchForm").submit();
    }

    window.addEventListener("load", function () {
        const suMoveToTable = sessionStorage.getItem("suggestionMoveToTable");

        if (suMoveToTable === "Y") {
            const suTableBox = document.getElementById("suTableBox");
            if (suTableBox) {
                suTableBox.scrollIntoView({ behavior: "auto", block: "start" });
            }
            sessionStorage.removeItem("suggestionMoveToTable");
        }
    });
    </script>

<% } %>