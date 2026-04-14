<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="java.util.List" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%@ page import="SUGGESTION.DTO.SuggestionDTO" %>
<%
    List<SuggestionDTO> suggestionList =
            (List<SuggestionDTO>) request.getAttribute("suggestionList");

    SuggestionDTO selectedSuggestion =
            (SuggestionDTO) request.getAttribute("selectedSuggestion");

    String keyword = (String) request.getAttribute("keyword");
    String status = (String) request.getAttribute("status");
    String deptCode = (String) request.getAttribute("deptCode");
    String mode = (String) request.getAttribute("mode");

    if (keyword == null) keyword = "";
    if (status == null) status = "";
    if (deptCode == null) deptCode = "";
    if (mode == null) mode = "";

    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
%>

<style>
    .suBoardWrap {
        width: 100%;
        max-width: 1280px;
        margin: 0;
    }

    .suBoardHeader {
        display: flex;
        justify-content: space-between;
        align-items: center;
        gap: 16px;
        margin-bottom: 18px;
    }

    .suBoardTitle {
        margin: 0 0 6px;
        font-size: 28px;
        font-weight: 800;
        color: #0A1E3C;
        letter-spacing: -0.4px;
    }

    .suBoardDesc {
        margin: 0;
        font-size: 13px;
        color: #6B7789;
    }

    .suBtn {
        display: inline-flex;
        align-items: center;
        justify-content: center;
        min-width: 88px;
        height: 40px;
        padding: 0 16px;
        border: 1px solid #DCE3EC;
        border-radius: 10px;
        background-color: #FFFFFF;
        color: #0A1E3C;
        font-size: 14px;
        font-weight: 700;
        text-decoration: none;
        cursor: pointer;
        box-sizing: border-box;
    }

    .suBtnPrimary {
        background-color: #0A1E3C;
        border-color: #0A1E3C;
        color: #FFFFFF;
    }

    .suBtnDanger {
        background-color: #FFF5F3;
        border-color: #F1C5BE;
        color: #D93025;
    }

    .suSearchBox {
        margin-bottom: 18px;
        padding: 18px;
        border: 1px solid #E8EDF4;
        border-radius: 18px;
        background-color: #FFFFFF;
        box-shadow: 0 8px 20px rgba(10, 30, 60, 0.06);
    }

    .suSearchRow {
        display: flex;
        flex-wrap: wrap;
        gap: 14px;
        align-items: flex-end;
    }

    .suSearchItem {
        display: flex;
        flex-direction: column;
        gap: 7px;
        min-width: 150px;
    }

    .suSearchItemWide {
        flex: 1;
        min-width: 260px;
    }

    .suSearchItem label {
        font-size: 13px;
        font-weight: 700;
        color: #0A1E3C;
    }

    .suSearchItem input,
    .suSearchItem select {
        width: 100%;
        height: 40px;
        padding: 0 12px;
        border: 1px solid #DCE3EC;
        border-radius: 10px;
        background-color: #FFFFFF;
        box-sizing: border-box;
    }

    .suSearchButtonRow {
        display: flex;
        justify-content: flex-end;
        gap: 10px;
        margin-top: 16px;
    }

    .suTableBox {
        padding: 18px;
        border: 1px solid #E8EDF4;
        border-radius: 18px;
        background-color: #FFFFFF;
        box-shadow: 0 8px 20px rgba(10, 30, 60, 0.06);
    }

    .suTableTop {
        display: flex;
        justify-content: space-between;
        align-items: center;
        margin-bottom: 12px;
    }

    .suTableCount {
        margin: 0;
        font-size: 14px;
        color: #6B7789;
    }

    .suTableCount strong {
        color: #0A1E3C;
    }

    .suTableScroll {
        width: 100%;
        overflow-x: auto;
    }

    .suSuggestionTable {
        width: 100%;
        min-width: 980px;
        border-collapse: collapse;
    }

    .suSuggestionTable thead th {
        height: 46px;
        padding: 0 8px;
        border-top: 1px solid #DCE3EC;
        border-bottom: 1px solid #DCE3EC;
        background-color: #F4F7FB;
        color: #0A1E3C;
        font-size: 13px;
        font-weight: 800;
        text-align: center;
    }

    .suSuggestionTable tbody td {
        height: 52px;
        padding: 0 8px;
        border-bottom: 1px solid #EDF2F7;
        text-align: center;
        font-size: 14px;
        color: #243041;
    }

    .suTitleCell {
        text-align: left !important;
        font-weight: 600;
    }

    .suStatusBadge {
        display: inline-flex;
        align-items: center;
        justify-content: center;
        min-width: 74px;
        height: 28px;
        padding: 0 10px;
        border-radius: 999px;
        font-size: 12px;
        font-weight: 800;
    }

    .suStatusReceipt {
        color: #0047AB;
        background-color: rgba(0, 71, 171, 0.10);
    }

    .suStatusReview {
        color: #FFFFFF;
        background-color: #0A1E3C;
    }

    .suStatusDone {
        color: #1F9D55;
        background-color: rgba(31, 157, 85, 0.12);
    }

    .suStatusReject {
        color: #D93025;
        background-color: rgba(217, 48, 37, 0.12);
    }

    .suPagingWrap {
        display: flex;
        justify-content: center;
        align-items: center;
        gap: 8px;
        margin-top: 18px;
    }

    .suPagingBtn {
        min-width: 34px;
        height: 34px;
        border: none;
        border-radius: 8px;
        background-color: transparent;
        color: #6D7684;
        font-size: 14px;
        font-weight: 500;
        cursor: pointer;
    }

    .suPagingBtnActive {
        background-color: rgba(0, 71, 171, 0.10);
        color: #0047AB;
        font-weight: 800;
    }

    .suModalWrap {
        display: none;
        position: fixed;
        inset: 0;
        z-index: 9999;
        background-color: rgba(10, 30, 60, 0.18);
        justify-content: center;
        align-items: center;
        padding: 20px;
    }

    .suModalWrap.suModalShow {
        display: flex;
    }

    .suModalBox {
        width: 100%;
        max-width: 900px;
        padding: 22px;
        border: 1px solid #DCE3EC;
        border-radius: 20px;
        background-color: #FBFCFE;
        box-shadow: 0 18px 36px rgba(10, 30, 60, 0.14);
        box-sizing: border-box;
    }

    .suModalBoxLarge {
        max-width: 1100px;
    }

    .suModalHeader {
        display: flex;
        justify-content: space-between;
        align-items: center;
        margin-bottom: 16px;
    }

    .suModalTitle {
        margin: 0;
        font-size: 20px;
        font-weight: 800;
        color: #0A1E3C;
    }

    .suModalCloseBtn {
        color: #5F6F84;
        font-size: 24px;
        font-weight: 700;
        text-decoration: none;
        line-height: 1;
    }

    .suFormWrap {
        padding: 16px;
        border: 1px solid #E8EDF4;
        border-radius: 12px;
        background-color: #F8FAFD;
    }

    .suFormRow {
        display: grid;
        grid-template-columns: 140px 1fr;
        gap: 16px;
        align-items: center;
    }

    .suFormRow + .suFormRow {
        margin-top: 14px;
    }

    .suFormRowTop {
        align-items: flex-start;
    }

    .suLabelBox {
        min-height: 44px;
        display: flex;
        align-items: center;
        justify-content: center;
        padding: 10px 12px;
        border: 1px solid #DCE3EC;
        border-radius: 8px;
        background-color: #FFFFFF;
        font-size: 14px;
        font-weight: 700;
        color: #0A1E3C;
        box-sizing: border-box;
    }

    .suField,
    .suValueBox {
        width: 100%;
        min-height: 44px;
        padding: 10px 12px;
        border: 1px solid #DCE3EC;
        border-radius: 8px;
        background-color: #FFFFFF;
        font-size: 14px;
        color: #243041;
        box-sizing: border-box;
    }

    textarea.suField,
    .suContentBox {
        min-height: 180px;
        resize: vertical;
        white-space: pre-wrap;
    }

    .suModalFooter {
        display: flex;
        justify-content: flex-end;
        gap: 10px;
        margin-top: 18px;
    }

    @media (max-width: 860px) {
        .suBoardHeader {
            flex-direction: column;
            align-items: flex-start;
        }

        .suFormRow {
            grid-template-columns: 1fr;
        }

        .suModalFooter {
            flex-wrap: wrap;
        }
    }
</style>

<section class="suBoardWrap">
    <div class="suBoardHeader">
<!--         <div> -->
<!--             <h2 class="suBoardTitle">건의사항 게시판</h2> -->
<!--             <p class="suBoardDesc">현재 페이지는 공통 레이아웃 안에 들어가는 본문 전용 JSP입니다.</p> -->
<!--         </div> -->

        <div>
            <a href="<%= request.getContextPath() %>/suggestion/list?mode=write"
               class="suBtn suBtnPrimary">등록</a>
        </div>
    </div>

    <form method="get" action="<%= request.getContextPath() %>/suggestion/list" class="suSearchBox">
        <div class="suSearchRow">
            <div class="suSearchItem suSearchItemWide">
                <label>검색어</label>
                <input type="text" name="keyword" value="<%= keyword %>" placeholder="제목 / 내용 / 작성자">
            </div>

            <div class="suSearchItem">
                <label>상태</label>
                <select name="status">
                    <option value="">전체</option>
                    <option value="접수" <%= "접수".equals(status) ? "selected" : "" %>>접수</option>
                    <option value="검토중" <%= "검토중".equals(status) ? "selected" : "" %>>검토중</option>
                    <option value="반영완료" <%= "반영완료".equals(status) ? "selected" : "" %>>반영완료</option>
                    <option value="반려" <%= "반려".equals(status) ? "selected" : "" %>>반려</option>
                </select>
            </div>

            <div class="suSearchItem">
                <label>부서코드</label>
                <select name="deptCode">
                    <option value="">전체</option>
                    <option value="PROD01" <%= "PROD01".equals(deptCode) ? "selected" : "" %>>PROD01</option>
                    <option value="PROD02" <%= "PROD02".equals(deptCode) ? "selected" : "" %>>PROD02</option>
                    <option value="QUAL01" <%= "QUAL01".equals(deptCode) ? "selected" : "" %>>QUAL01</option>
                    <option value="EQP01" <%= "EQP01".equals(deptCode) ? "selected" : "" %>>EQP01</option>
                    <option value="LOGI01" <%= "LOGI01".equals(deptCode) ? "selected" : "" %>>LOGI01</option>
                    <option value="MNG01" <%= "MNG01".equals(deptCode) ? "selected" : "" %>>MNG01</option>
                </select>
            </div>
        </div>

        <div class="suSearchButtonRow">
            <a href="<%= request.getContextPath() %>/suggestion/list" class="suBtn">초기화</a>
            <button type="submit" class="suBtn suBtnPrimary">조회</button>
        </div>
    </form>

    <section class="suTableBox">
        <div class="suTableTop">
            <p class="suTableCount">총 <strong><%= suggestionList == null ? 0 : suggestionList.size() %></strong>건</p>
        </div>

        <div class="suTableScroll">
            <table class="suSuggestionTable">
                <thead>
                <tr>
                    <th>번호</th>
                    <th>상태</th>
                    <th>제목</th>
                    <th>부서</th>
                    <th>작성자</th>
                    <th>조회수</th>
                    <th>작성일</th>
                    <th>상세</th>
                </tr>
                </thead>
                <tbody>
                <%
                    if (suggestionList == null || suggestionList.isEmpty()) {
                %>
                <tr>
                    <td colspan="8">조회된 건의사항이 없습니다.</td>
                </tr>
                <%
                    } else {
                        for (SuggestionDTO dto : suggestionList) {
                            String statusClass = "suStatusReceipt";
                            if ("검토중".equals(dto.getStatus())) {
                                statusClass = "suStatusReview";
                            } else if ("반영완료".equals(dto.getStatus())) {
                                statusClass = "suStatusDone";
                            } else if ("반려".equals(dto.getStatus())) {
                                statusClass = "suStatusReject";
                            }
                %>
                <tr>
                    <td><%= dto.getSuggestionId() %></td>
                    <td><span class="suStatusBadge <%= statusClass %>"><%= dto.getStatus() %></span></td>
                    <td class="suTitleCell"><%= dto.getTitle() %></td>
                    <td><%= dto.getDeptCode() %></td>
                    <td><%= dto.getWriterName() %></td>
                    <td><%= dto.getViewCount() %></td>
                    <td><%= dto.getCreatedAt() == null ? "-" : sdf.format(dto.getCreatedAt()) %></td>
                    <td>
                        <a href="<%= request.getContextPath() %>/suggestion/list?mode=detail&id=<%= dto.getSuggestionId() %>"
                           class="suBtn">보기</a>
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
            <button type="button" class="suPagingBtn">&lt;&lt;</button>
            <button type="button" class="suPagingBtn suPagingBtnActive">1</button>
            <button type="button" class="suPagingBtn">2</button>
            <button type="button" class="suPagingBtn">3</button>
            <button type="button" class="suPagingBtn">&gt;&gt;</button>
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

        <form method="post" action="<%= request.getContextPath() %>/suggestion/insert">
            <div class="suFormWrap">
                <div class="suFormRow">
                    <div class="suLabelBox">제목</div>
                    <div><input type="text" name="title" class="suField" required></div>
                </div>

                <div class="suFormRow suFormRowTop">
                    <div class="suLabelBox">내용</div>
                    <div><textarea name="content" class="suField" required></textarea></div>
                </div>

                <div class="suFormRow">
                    <div class="suLabelBox">비고</div>
                    <div><input type="text" name="remark" class="suField"></div>
                </div>
            </div>

            <div class="suModalFooter">
                <a href="<%= request.getContextPath() %>/suggestion/list" class="suBtn">취소</a>
                <button type="submit" class="suBtn suBtnPrimary">등록</button>
            </div>
        </form>
    </div>
</div>

<!-- 상세 모달 -->
<div class="suModalWrap <%= "detail".equals(mode) && selectedSuggestion != null ? "suModalShow" : "" %>">
    <div class="suModalBox suModalBoxLarge">
        <div class="suModalHeader">
            <h3 class="suModalTitle">건의 상세</h3>
            <a href="<%= request.getContextPath() %>/suggestion/list" class="suModalCloseBtn">×</a>
        </div>

        <%
            if ("detail".equals(mode) && selectedSuggestion != null) {
        %>
        <div class="suFormWrap">
            <div class="suFormRow">
                <div class="suLabelBox">번호</div>
                <div class="suValueBox"><%= selectedSuggestion.getSuggestionId() %></div>
            </div>

            <div class="suFormRow">
                <div class="suLabelBox">상태</div>
                <div class="suValueBox"><%= selectedSuggestion.getStatus() %></div>
            </div>

            <div class="suFormRow">
                <div class="suLabelBox">제목</div>
                <div class="suValueBox"><%= selectedSuggestion.getTitle() %></div>
            </div>

            <div class="suFormRow">
                <div class="suLabelBox">부서</div>
                <div class="suValueBox"><%= selectedSuggestion.getDeptCode() %></div>
            </div>

            <div class="suFormRow">
                <div class="suLabelBox">작성자</div>
                <div class="suValueBox"><%= selectedSuggestion.getWriterName() %></div>
            </div>

            <div class="suFormRow">
                <div class="suLabelBox">조회수</div>
                <div class="suValueBox"><%= selectedSuggestion.getViewCount() %></div>
            </div>

            <div class="suFormRow">
                <div class="suLabelBox">작성일</div>
                <div class="suValueBox">
                    <%= selectedSuggestion.getCreatedAt() == null ? "-" : sdf.format(selectedSuggestion.getCreatedAt()) %>
                </div>
            </div>

            <div class="suFormRow suFormRowTop">
                <div class="suLabelBox">내용</div>
                <div class="suValueBox suContentBox"><%= selectedSuggestion.getContent() %></div>
            </div>

            <div class="suFormRow">
                <div class="suLabelBox">비고</div>
                <div class="suValueBox"><%= selectedSuggestion.getRemark() == null ? "" : selectedSuggestion.getRemark() %></div>
            </div>
        </div>

        <div class="suModalFooter">
            <a href="<%= request.getContextPath() %>/suggestion/list" class="suBtn">목록</a>
            <a href="<%= request.getContextPath() %>/suggestion/list?mode=edit&id=<%= selectedSuggestion.getSuggestionId() %>"
               class="suBtn suBtnPrimary">수정</a>

            <form method="post"
                  action="<%= request.getContextPath() %>/suggestion/delete"
                  style="margin:0;"
                  onsubmit="return confirm('정말 삭제하시겠습니까?');">
                <input type="hidden" name="suggestionId" value="<%= selectedSuggestion.getSuggestionId() %>">
                <button type="submit" class="suBtn suBtnDanger">삭제</button>
            </form>
        </div>
        <%
            }
        %>
    </div>
</div>

<!-- 수정 모달 -->
<div class="suModalWrap <%= "edit".equals(mode) && selectedSuggestion != null ? "suModalShow" : "" %>">
    <div class="suModalBox suModalBoxLarge">
        <div class="suModalHeader">
            <h3 class="suModalTitle">건의 수정</h3>
            <a href="<%= request.getContextPath() %>/suggestion/list" class="suModalCloseBtn">×</a>
        </div>

        <%
            if ("edit".equals(mode) && selectedSuggestion != null) {
        %>
        <form method="post" action="<%= request.getContextPath() %>/suggestion/update">
            <input type="hidden" name="suggestionId" value="<%= selectedSuggestion.getSuggestionId() %>">

            <div class="suFormWrap">
                <div class="suFormRow">
                    <div class="suLabelBox">제목</div>
                    <div>
                        <input type="text" name="title" class="suField"
                               value="<%= selectedSuggestion.getTitle() %>" required>
                    </div>
                </div>

                <div class="suFormRow suFormRowTop">
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
                            <option value="반영완료" <%= "반영완료".equals(selectedSuggestion.getStatus()) ? "selected" : "" %>>반영완료</option>
                            <option value="반려" <%= "반려".equals(selectedSuggestion.getStatus()) ? "selected" : "" %>>반려</option>
                        </select>
                    </div>
                </div>

                <div class="suFormRow">
                    <div class="suLabelBox">비고</div>
                    <div>
                        <input type="text" name="remark" class="suField"
                               value="<%= selectedSuggestion.getRemark() == null ? "" : selectedSuggestion.getRemark() %>">
                    </div>
                </div>
            </div>

            <div class="suModalFooter">
                <a href="<%= request.getContextPath() %>/suggestion/list?mode=detail&id=<%= selectedSuggestion.getSuggestionId() %>"
                   class="suBtn">취소</a>
                <button type="submit" class="suBtn suBtnPrimary">수정</button>
            </div>
        </form>
        <%
            }
        %>
    </div>
</div>