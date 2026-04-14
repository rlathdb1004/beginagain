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

    int size = request.getAttribute("size") == null ? 10 : (Integer) request.getAttribute("size");
    int currentPage = request.getAttribute("page") == null ? 1 : (Integer) request.getAttribute("page");
    int totalCount = request.getAttribute("totalCount") == null ? 0 : (Integer) request.getAttribute("totalCount");
    int totalPage = request.getAttribute("totalPage") == null ? 1 : (Integer) request.getAttribute("totalPage");
    int startPage = request.getAttribute("startPage") == null ? 1 : (Integer) request.getAttribute("startPage");
    int endPage = request.getAttribute("endPage") == null ? 1 : (Integer) request.getAttribute("endPage");

    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
%>

<style>
.suBoardWrap {
    width: 100%;
    max-width: 1280px;
    margin: 0;
    display: flex;
    flex-direction: column;
}

.suSearchBox {
    order: 1;
    margin-bottom: 16px;
    padding: 18px;
    border: 1px solid #E5ECF4;
    border-radius: 18px;
    background-color: #FFFFFF;
    box-shadow: 0 4px 14px rgba(10, 30, 60, 0.04);
}

.suBoardHeader {
    order: 2;
    display: flex;
    justify-content: flex-end;
    align-items: center;
    margin-bottom: 16px;
    gap: 0;
}

.suTableBox {
    order: 3;
    padding: 18px;
    border: 1px solid #E5ECF4;
    border-radius: 18px;
    background-color: #FFFFFF;
    box-shadow: 0 4px 14px rgba(10, 30, 60, 0.04);
}

.suBtn {
    display: inline-flex;
    align-items: center;
    justify-content: center;
    min-width: 88px;
    height: 40px;
    padding: 0 18px;
    border: 1px solid #C8D5E6;
    border-radius: 12px;
    background-color: #FFFFFF;
    color: #0A1E3C;
    font-size: 14px;
    font-weight: 500;
    text-decoration: none;
    cursor: pointer;
    box-sizing: border-box;
}

.suBtnPrimary {
    background-color: #0047AB;
    border-color: #0047AB;
    color: #FFFFFF;
}

.suBtnDanger {
    background-color: #FFF5F3;
    border-color: #F1C5BE;
    color: #D93025;
}

.suSearchRow {
    display: grid;
    grid-template-columns: 200px 200px minmax(0, 1fr) 88px;
    gap: 12px;
    align-items: end;
}

.suSearchItem {
    display: flex;
    flex-direction: column;
    gap: 8px;
    min-width: 0;
}

.suSearchItem label {
    font-size: 14px;
    font-weight: 600;
    color: #0A1E3C;
}

.suSearchItem input,
.suSearchItem select {
    width: 100%;
    height: 40px;
    padding: 0 14px;
    border: 1px solid #CCD8E6;
    border-radius: 12px;
    background-color: #FFFFFF;
    box-sizing: border-box;
    font-size: 14px;
    font-weight: 400;
    color: #526275;
    outline: none;
}

.suSearchItem input::placeholder {
    color: #97A4B5;
    font-weight: 400;
}

.suSearchAction {
    display: flex;
    flex-direction: column;
    gap: 10px;
    align-items: flex-end;
}

.suSearchAction .suBtn {
    width: 88px;
    min-width: 88px;
}

.suSearchIconBtn {
    width: 40px;
    height: 40px;
    border: 1px solid #CCD8E6;
    border-radius: 12px;
    background-color: #FFFFFF;
    cursor: pointer;
    position: relative;
    padding: 0;
    box-sizing: border-box;
}

.suSearchIcon {
    position: relative;
    display: inline-block;
    width: 12px;
    height: 12px;
    border: 2px solid #0A1E3C;
    border-radius: 50%;
    vertical-align: middle;
}

.suSearchIcon::after {
    content: "";
    position: absolute;
    width: 6px;
    height: 2px;
    background-color: #0A1E3C;
    border-radius: 2px;
    right: -5px;
    bottom: -1px;
    transform: rotate(45deg);
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
    font-weight: 400;
    color: #8693A5;
}

.suTableCount strong {
    color: #0A1E3C;
    font-weight: 700;
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
    height: 42px;
    padding: 0 8px;
    border-top: 1px solid #D9E3EE;
    border-bottom: 1px solid #D9E3EE;
    background-color: #EEF3F9;
    color: #0A1E3C;
    font-size: 14px;
    font-weight: 600;
    text-align: center;
}

.suSuggestionTable tbody td {
    height: 48px;
    padding: 0 8px;
    border-bottom: 1px solid #EDF2F7;
    text-align: center;
    font-size: 14px;
    font-weight: 400;
    color: #5A687A;
}

.suTitleCell {
    text-align: left;
    font-weight: 500;
    color: #324255;
}

.suStatusBadge {
    display: inline-flex;
    align-items: center;
    justify-content: center;
    width: 88px;
    height: 32px;
    padding: 0 14px;
    border-radius: 999px;
    box-sizing: border-box;
    font-size: 13px;
    font-weight: 500;
    letter-spacing: -0.1px;
    border: 1px solid transparent;
}

.suStatusReceipt {
    color: #111111;
    background-color: #E4F3EA;
    border-color: #B9DCC7;
}

.suStatusReview {
    color: #111111;
    background-color: #E7EBF1;
    border-color: #C8D2DF;
}

.suStatusDone {
    color: #111111;
    background-color: #EAF0FB;
    border-color: #C6D5F0;
}

.suStatusReject {
    color: #E74C3C;
    background-color: #FFF5F3;
    border-color: #F1C5BE;
}

.suViewBtn {
    display: inline-flex;
    align-items: center;
    justify-content: center;
    min-width: 82px;
    height: 36px;
    padding: 0 16px;
    border: 1px solid #D7E0EC;
    border-radius: 12px;
    background-color: #FFFFFF;
    color: #0A1E3C;
    font-size: 14px;
    font-weight: 600;
    text-decoration: none;
    cursor: pointer;
    box-sizing: border-box;
}

.suPagingWrap {
    display: flex;
    justify-content: center;
    align-items: center;
    gap: 12px;
    margin-top: 18px;
}

.suPagingBtn {
    min-width: 30px;
    height: 30px;
    border: none;
    border-radius: 8px;
    background-color: transparent;
    color: #7F8B9B;
    font-size: 14px;
    font-weight: 500;
    cursor: pointer;
}

.suPagingBtnActive {
    background-color: #E6EEF9;
    color: #0047AB;
    font-weight: 700;
}

.suPagingBtn:disabled {
    opacity: 0.4;
    cursor: default;
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
    font-weight: 700;
    color: #0A1E3C;
}

.suModalCloseBtn {
    color: #6B7789;
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
    min-height: 42px;
    display: flex;
    align-items: center;
    justify-content: center;
    padding: 10px 12px;
    border: 1px solid #DCE3EC;
    border-radius: 8px;
    background-color: #FFFFFF;
    font-size: 13px;
    font-weight: 600;
    color: #0A1E3C;
    box-sizing: border-box;
}

.suField,
.suValueBox {
    width: 100%;
    min-height: 42px;
    padding: 10px 12px;
    border: 1px solid #DCE3EC;
    border-radius: 8px;
    background-color: #FFFFFF;
    font-size: 13px;
    font-weight: 400;
    color: #59697C;
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

@media (max-width: 1024px) {
    .suSearchRow {
        grid-template-columns: 1fr;
    }

    .suSearchAction {
        flex-direction: row;
        justify-content: flex-end;
        align-items: center;
    }
}

@media (max-width: 860px) {
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
        <div class="suBoardHeaderBtn">
            <a href="<%= request.getContextPath() %>/suggestion/list?mode=write" class="suBtn suBtnPrimary">
                등록
            </a>
        </div>
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
                <input
                    type="text"
                    id="suKeyword"
                    name="keyword"
                    value="<%= keyword %>"
                    placeholder="제목 / 내용 / 작성자"
                />
            </div>

            <div class="suSearchAction">
                <button type="submit"
                        class="suSearchIconBtn"
                        aria-label="검색"
                        onclick="document.getElementById('suPage').value=1;">
                    <span class="suSearchIcon"></span>
                </button>

                <a href="<%= request.getContextPath() %>/suggestion/list" class="suBtn">초기화</a>
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
                            String statusText = dto.getStatus();

                            if ("검토중".equals(dto.getStatus())) {
                                statusClass = "suStatusReview";
                            } else if ("답변완료".equals(dto.getStatus()) || "반영완료".equals(dto.getStatus())) {
                                statusClass = "suStatusDone";
                                statusText = "답변완료";
                            } else if ("반려".equals(dto.getStatus())) {
                                statusClass = "suStatusReject";
                            }
                %>
                    <tr>
                        <td><%= dto.getSuggestionId() %></td>
                        <td>
                            <span class="suStatusBadge <%= statusClass %>"><%= statusText %></span>
                        </td>
                        <td class="suTitleCell"><%= dto.getTitle() %></td>
                        <td><%= dto.getDeptCode() %></td>
                        <td><%= dto.getWriterName() %></td>
                        <td><%= dto.getViewCount() %></td>
                        <td><%= dto.getCreatedAt() == null ? "-" : sdf.format(dto.getCreatedAt()) %></td>
                        <td>
                            <a href="<%= request.getContextPath() %>/suggestion/list?mode=detail&id=<%= dto.getSuggestionId() %>" class="suViewBtn">
                                보기
                            </a>
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

        <form method="post" action="<%= request.getContextPath() %>/suggestion/insert">
            <div class="suFormWrap">
                <div class="suFormRow">
                    <div class="suLabelBox">제목</div>
                    <div>
                        <input type="text" name="title" class="suField" required />
                    </div>
                </div>

                <div class="suFormRow suFormRowTop">
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
            </div>

            <div class="suModalFooter">
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
                String detailStatusText = selectedSuggestion.getStatus();
                if ("반영완료".equals(detailStatusText)) {
                    detailStatusText = "답변완료";
                }
        %>
        <div class="suFormWrap">
            <div class="suFormRow">
                <div class="suLabelBox">번호</div>
                <div class="suValueBox"><%= selectedSuggestion.getSuggestionId() %></div>
            </div>

            <div class="suFormRow">
                <div class="suLabelBox">상태</div>
                <div class="suValueBox"><%= detailStatusText %></div>
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
            <a href="<%= request.getContextPath() %>/suggestion/list?mode=edit&id=<%= selectedSuggestion.getSuggestionId() %>" class="suBtn suBtnPrimary">
                수정
            </a>

            <form method="post"
                  action="<%= request.getContextPath() %>/suggestion/delete"
                  style="margin: 0;"
                  onsubmit="return confirm('정말 삭제하시겠습니까?');">
                <input type="hidden" name="suggestionId" value="<%= selectedSuggestion.getSuggestionId() %>" />
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
            <input type="hidden" name="suggestionId" value="<%= selectedSuggestion.getSuggestionId() %>" />

            <div class="suFormWrap">
                <div class="suFormRow">
                    <div class="suLabelBox">제목</div>
                    <div>
                        <input type="text" name="title" class="suField" value="<%= selectedSuggestion.getTitle() %>" required />
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
                            <option value="반영완료" <%= "반영완료".equals(selectedSuggestion.getStatus()) ? "selected" : "" %>>답변완료</option>
                            <option value="반려" <%= "반려".equals(selectedSuggestion.getStatus()) ? "selected" : "" %>>반려</option>
                        </select>
                    </div>
                </div>

                <div class="suFormRow">
                    <div class="suLabelBox">비고</div>
                    <div>
                        <input type="text" name="remark" class="suField" value="<%= selectedSuggestion.getRemark() == null ? "" : selectedSuggestion.getRemark() %>" />
                    </div>
                </div>
            </div>

            <div class="suModalFooter">
                <a href="<%= request.getContextPath() %>/suggestion/list?mode=detail&id=<%= selectedSuggestion.getSuggestionId() %>" class="suBtn">
                    취소
                </a>
                <button type="submit" class="suBtn suBtnPrimary">수정</button>
            </div>
        </form>
        <%
            }
        %>
    </div>
</div>

<script>
function movePage(page) {
    sessionStorage.setItem("suggestionMoveToTable", "Y");
    document.getElementById("suPage").value = page;
    document.getElementById("suSearchForm").submit();
}

window.addEventListener("load", function () {
    const moveToTable = sessionStorage.getItem("suggestionMoveToTable");

    if (moveToTable === "Y") {
        const tableBox = document.getElementById("suTableBox");
        if (tableBox) {
            tableBox.scrollIntoView({ behavior: "auto", block: "start" });
        }
        sessionStorage.removeItem("suggestionMoveToTable");
    }
});
</script>