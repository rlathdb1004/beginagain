<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<style>
.reportPage { display:flex; flex-direction:column; gap:20px; }
.reportFilterBox, .reportPanel { background:#fff; border:1px solid #d7e1f0; border-radius:20px; box-shadow:0 10px 24px rgba(10,30,60,.04); }
.reportFilterBox { padding:18px 20px; }
.reportFilterForm { display:flex; flex-wrap:wrap; gap:12px; align-items:end; }
.reportField { display:flex; flex-direction:column; gap:6px; min-width:180px; }
.reportField label { font-size:13px; color:#6F7B8D; }
.reportInput { height:40px; border:1px solid #d7e1f0; border-radius:12px; padding:0 12px; font-size:14px; }
.reportBtn { height:40px; padding:0 18px; border:none; border-radius:12px; background:#0047AB; color:#fff; font-size:14px; cursor:pointer; }
.reportSummaryGrid { display:grid; grid-template-columns:repeat(4, minmax(0,1fr)); gap:16px; }
.reportSummaryCard { background:linear-gradient(180deg,#ffffff 0%, #f7faff 100%); border:1px solid #d7e1f0; border-radius:18px; padding:18px; }
.reportSummaryLabel { color:#6F7B8D; font-size:13px; margin-bottom:8px; }
.reportSummaryValue { color:#0A1E3C; font-size:28px; font-weight:800; }
.reportSummaryHint { margin-top:8px; color:#6F7B8D; font-size:12px; }
.reportPanel { padding:20px; }
.reportPanelTitle { margin:0 0 6px; font-size:20px; color:#0A1E3C; }
.reportPanelSub { margin:0 0 18px; color:#6F7B8D; font-size:13px; }
.reportTwoCol { display:grid; grid-template-columns:1.4fr 1fr; gap:20px; }
.reportChartBox { border:1px solid #e3ebf6; border-radius:18px; padding:16px; background:#fbfdff; }
.reportChartTitle { font-size:15px; font-weight:700; margin-bottom:14px; color:#102544; }
.reportChart { display:flex; align-items:flex-end; gap:14px; min-height:260px; padding:0 8px; }
.reportChartCol { flex:1; min-width:0; display:flex; flex-direction:column; align-items:center; gap:8px; }
.reportBars { width:100%; height:210px; display:flex; align-items:flex-end; justify-content:center; gap:6px; }
.reportBar { width:24px; border-radius:10px 10px 0 0; min-height:4px; }
.reportBarProduced { background:linear-gradient(180deg, #4a9dff 0%, #0047AB 100%); }
.reportBarLoss { background:linear-gradient(180deg, #ffb48a 0%, #FF4500 100%); }
.reportBarLabel { font-size:12px; color:#6F7B8D; text-align:center; word-break:keep-all; }
.reportBarValue { font-size:11px; color:#22304A; font-weight:700; }
.reportRankList { display:flex; flex-direction:column; gap:12px; }
.reportRankItem { display:grid; grid-template-columns:120px 1fr 60px; gap:12px; align-items:center; }
.reportRankName { font-size:13px; color:#22304A; white-space:nowrap; overflow:hidden; text-overflow:ellipsis; }
.reportRankTrack { height:14px; border-radius:999px; background:#edf3fb; overflow:hidden; }
.reportRankFill { height:100%; border-radius:999px; background:linear-gradient(90deg,#ff8b52 0%, #FF4500 100%); }
.reportRankCount { text-align:right; font-size:12px; font-weight:700; color:#102544; }
.reportProgressWrap { display:flex; align-items:center; gap:10px; }
.reportProgressTrack { flex:1; height:12px; border-radius:999px; background:#edf3fb; overflow:hidden; }
.reportProgressFill { height:100%; border-radius:999px; background:linear-gradient(90deg,#8ec5ff 0%, #0047AB 100%); }
.reportProgressText { width:58px; text-align:right; font-size:12px; font-weight:700; color:#102544; }
.reportEmpty { padding:24px; border:1px dashed #c9d7eb; border-radius:16px; color:#6F7B8D; text-align:center; background:#fbfdff; }
.reportCaption { margin-top:14px; padding:12px 14px; border-radius:14px; background:#f5f8fc; color:#44536a; font-size:13px; line-height:1.6; }
@media (max-width: 1200px) {
  .reportSummaryGrid, .reportTwoCol { grid-template-columns:1fr 1fr; }
}
@media (max-width: 900px) {
  .reportSummaryGrid, .reportTwoCol { grid-template-columns:1fr; }
}
</style>

<div class="reportPage">
    <div class="reportFilterBox">
        <form method="get" action="${pageContext.request.contextPath}/report" class="reportFilterForm">
            <div class="reportField">
                <label for="startDate">시작일</label>
                <input class="reportInput" type="date" id="startDate" name="startDate" value="${startDate}">
            </div>
            <div class="reportField">
                <label for="endDate">종료일</label>
                <input class="reportInput" type="date" id="endDate" name="endDate" value="${endDate}">
            </div>
            <button type="submit" class="reportBtn">조회</button>
        </form>
    </div>

    <div class="reportSummaryGrid">
        <div class="reportSummaryCard">
            <div class="reportSummaryLabel">총 생산량</div>
            <div class="reportSummaryValue"><fmt:formatNumber value="${report.summary.totalProducedQty}" pattern="#,##0.###" /></div>
            <div class="reportSummaryHint">선택 기간 내 생산실적 합계</div>
        </div>
        <div class="reportSummaryCard">
            <div class="reportSummaryLabel">총 손실량</div>
            <div class="reportSummaryValue"><fmt:formatNumber value="${report.summary.totalLossQty}" pattern="#,##0.###" /></div>
            <div class="reportSummaryHint">LOSS_QTY 기준 집계</div>
        </div>
        <div class="reportSummaryCard">
            <div class="reportSummaryLabel">불량 발생 건수</div>
            <div class="reportSummaryValue"><fmt:formatNumber value="${report.summary.defectCount}" pattern="#,##0" /></div>
            <div class="reportSummaryHint">완제품 검사 연계 기준</div>
        </div>
        <div class="reportSummaryCard">
            <div class="reportSummaryLabel">목표 달성률</div>
            <div class="reportSummaryValue"><fmt:formatNumber value="${report.summary.achievementRate}" pattern="#0.0" />%</div>
            <div class="reportSummaryHint">생산계획 대비 생산실적 비율</div>
        </div>
    </div>

    <div class="reportTwoCol">
        <div class="reportPanel">
            <h3 class="reportPanelTitle">생산실적 리포트</h3>
            <p class="reportPanelSub">월별 생산량, 손실량 비교</p>
            <div class="reportChartBox">
                <div class="reportChartTitle">월별 생산 / 손실 추이</div>
                <c:choose>
                    <c:when test="${not empty report.productionTrendList}">
                        <c:set var="maxProduced" value="0" />
                        <c:forEach var="row" items="${report.productionTrendList}">
                            <c:if test="${row.producedQty > maxProduced}"><c:set var="maxProduced" value="${row.producedQty}" /></c:if>
                            <c:if test="${row.lossQty > maxProduced}"><c:set var="maxProduced" value="${row.lossQty}" /></c:if>
                        </c:forEach>
                        <div class="reportChart">
                            <c:forEach var="row" items="${report.productionTrendList}">
                                <div class="reportChartCol">
                                    <div class="reportBars">
                                        <div class="reportBar reportBarProduced" style="height:${maxProduced == 0 ? 4 : (row.producedQty / maxProduced) * 180 + 12}px" title="생산량"></div>
                                        <div class="reportBar reportBarLoss" style="height:${maxProduced == 0 ? 4 : (row.lossQty / maxProduced) * 180 + 12}px" title="손실량"></div>
                                    </div>
                                    <div class="reportBarValue"><fmt:formatNumber value="${row.producedQty}" pattern="#,##0.###" /></div>
                                    <div class="reportBarLabel">${row.label}</div>
                                </div>
                            </c:forEach>
                        </div>
                    </c:when>
                    <c:otherwise><div class="reportEmpty">선택 기간에 생산실적 데이터가 없습니다.</div></c:otherwise>
                </c:choose>
            </div>
            <div class="reportCaption">
                📌 <c:out value="${report.productionCaption}" />
            </div>
        </div>

        <div class="reportPanel">
            <h3 class="reportPanelTitle">불량분석 리포트</h3>
            <p class="reportPanelSub">불량 발생 건수 집계</p>
            <div class="reportChartBox">
                <div class="reportChartTitle">상위 불량 유형 TOP 5</div>
                <c:choose>
                    <c:when test="${not empty report.defectTypeList}">
                        <c:set var="maxDefectCount" value="0" />
                        <c:forEach var="row" items="${report.defectTypeList}">
                            <c:if test="${row.defectCount > maxDefectCount}"><c:set var="maxDefectCount" value="${row.defectCount}" /></c:if>
                        </c:forEach>
                        <div class="reportRankList">
                            <c:forEach var="row" items="${report.defectTypeList}">
                                <div class="reportRankItem">
                                    <div class="reportRankName">${row.defectName}</div>
                                    <div class="reportRankTrack">
                                        <div class="reportRankFill" style="width:${maxDefectCount == 0 ? 0 : (row.defectCount / maxDefectCount) * 100}%"></div>
                                    </div>
                                    <div class="reportRankCount">${row.defectCount}건</div>
                                </div>
                            </c:forEach>
                        </div>
                    </c:when>
                    <c:otherwise><div class="reportEmpty">선택 기간에 불량 데이터가 없습니다.</div></c:otherwise>
                </c:choose>
            </div>
            <div class="reportCaption">
                📌 <c:out value="${report.defectCaption}" />
            </div>
        </div>
    </div>

    <div class="reportPanel">
        <h3 class="reportPanelTitle">목표달성 리포트</h3>
        <p class="reportPanelSub">품목별 계획 수량, 실적 수량 비교</p>
        <div class="taTableShell">
            <div class="taTableScroll">
                <table class="taMesTable">
                    <thead>
                        <tr>
                            <th class="taTableHeadCell taColGrow">품목명</th>
                            <th class="taTableHeadCell taColFit">계획수량</th>
                            <th class="taTableHeadCell taColFit">실적수량</th>
                            <th class="taTableHeadCell taLastCol">달성률</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:choose>
                            <c:when test="${not empty report.achievementList}">
                                <c:forEach var="row" items="${report.achievementList}">
                                    <tr class="taTableBodyRow">
                                        <td class="taTableBodyCell taColGrow">${row.itemName}</td>
                                        <td class="taTableBodyCell taColFit"><fmt:formatNumber value="${row.planQty}" pattern="#,##0.###" /></td>
                                        <td class="taTableBodyCell taColFit"><fmt:formatNumber value="${row.producedQty}" pattern="#,##0.###" /></td>
                                        <td class="taTableBodyCell taLastCol">
                                            <div class="reportProgressWrap">
                                                <div class="reportProgressTrack">
                                                    <div class="reportProgressFill" style="width:${row.achievementRate > 100 ? 100 : row.achievementRate}%"></div>
                                                </div>
                                                <div class="reportProgressText"><fmt:formatNumber value="${row.achievementRate}" pattern="#0.0" />%</div>
                                            </div>
                                        </td>
                                    </tr>
                                </c:forEach>
                            </c:when>
                            <c:otherwise>
                                <tr><td class="taTableBodyCell taLastCol" colspan="4">선택 기간에 계획/실적 데이터가 없습니다.</td></tr>
                            </c:otherwise>
                        </c:choose>
                    </tbody>
                </table>
            </div>
        </div>
        <div class="reportCaption">
            📌 <c:out value="${report.achievementCaption}" />
        </div>
    </div>

    <div class="reportPanel">
        <h3 class="reportPanelTitle">설비이력 리포트</h3>
        <p class="reportPanelSub">설비별 이슈 빈도</p>
        <div class="taTableShell">
            <div class="taTableScroll">
                <table class="taMesTable">
                    <thead>
                        <tr>
                            <th class="taTableHeadCell taColGrow">설비명</th>
                            <th class="taTableHeadCell taColFit">정비</th>
                            <th class="taTableHeadCell taColFit">고장</th>
                            <th class="taTableHeadCell taLastCol taColFit">합계</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:choose>
                            <c:when test="${not empty report.equipmentIssueList}">
                                <c:forEach var="row" items="${report.equipmentIssueList}">
                                    <tr class="taTableBodyRow">
                                        <td class="taTableBodyCell taColGrow">${row.equipmentName}</td>
                                        <td class="taTableBodyCell taColFit">${row.maintenanceCount}</td>
                                        <td class="taTableBodyCell taColFit">${row.failureCount}</td>
                                        <td class="taTableBodyCell taLastCol taColFit">${row.totalIssueCount}</td>
                                    </tr>
                                </c:forEach>
                            </c:when>
                            <c:otherwise>
                                <tr><td class="taTableBodyCell taLastCol" colspan="4">선택 기간에 설비 이력 데이터가 없습니다.</td></tr>
                            </c:otherwise>
                        </c:choose>
                    </tbody>
                </table>
            </div>
        </div>
        <div class="reportCaption">
            📌 <c:out value="${report.equipmentCaption}" />
        </div>
    </div>
</div>
