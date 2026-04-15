<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<style>
/* =========================
   CEO Main Dashboard
   ceomain.jsp 전용
   ========================= */
.ceoPage {
    display: flex;
    flex-direction: column;
    gap: 20px;
    width: 100%;
    min-width: 0;
}

.ceoHero {
    display: flex;
    justify-content: space-between;
    align-items: stretch;
    gap: 16px;
    padding: 24px 28px;
    border-radius: 24px;
    border: 1px solid #d7e1f0;
    background:
        radial-gradient(circle at top right, rgba(0, 71, 171, 0.10) 0%, rgba(0, 71, 171, 0) 32%),
        linear-gradient(135deg, #ffffff 0%, #f4f8ff 100%);
    box-shadow: 0 16px 36px rgba(10, 30, 60, 0.06);
}

.ceoHeroTitle {
    margin: 0;
    font-size: 28px;
    font-weight: 800;
    color: #0A1E3C;
}

.ceoHeroSub {
    margin: 10px 0 0;
    font-size: 14px;
    color: #6F7B8D;
    line-height: 1.6;
}

.ceoHeroMeta {
    min-width: 240px;
    display: flex;
    flex-direction: column;
    justify-content: center;
    gap: 10px;
    padding: 18px 20px;
    border-radius: 20px;
    background: #ffffff;
    border: 1px solid #dbe6f4;
}

.ceoHeroMetaLabel {
    font-size: 12px;
    font-weight: 700;
    color: #7A8799;
    letter-spacing: 0.04em;
}

.ceoHeroMetaValue {
    font-size: 18px;
    font-weight: 800;
    color: #0A1E3C;
}

.ceoHeroMetaSub {
    display: flex;
    gap: 8px;
    flex-wrap: wrap;
}

.ceoChip {
    display: inline-flex;
    align-items: center;
    justify-content: center;
    min-height: 30px;
    padding: 0 12px;
    border-radius: 999px;
    font-size: 12px;
    font-weight: 700;
    white-space: nowrap;
}

.ceoChipBlue {
    background: #EAF3FF;
    color: #0047AB;
}

.ceoChipGreen {
    background: #EAF9F2;
    color: #22A06B;
}

.ceoChipAmber {
    background: #FFF7E8;
    color: #F5A524;
}

.ceoChipRed {
    background: #FFF1F2;
    color: #E5484D;
}

.ceoBriefingCard {
    background: #ffffff;
    border: 1px solid #d7e1f0;
    border-radius: 22px;
    padding: 20px 24px;
    box-shadow: 0 10px 24px rgba(10, 30, 60, 0.04);
}

.ceoSectionEyebrow {
    margin-bottom: 10px;
    font-size: 12px;
    font-weight: 800;
    letter-spacing: 0.08em;
    color: #0047AB;
}

.ceoBriefingRow {
    display: grid;
    grid-template-columns: 52px 1fr;
    gap: 16px;
    align-items: center;
}

.ceoBriefingIcon {
    width: 52px;
    height: 52px;
    border-radius: 16px;
    background: linear-gradient(135deg, #0047AB 0%, #4A9DFF 100%);
    color: #ffffff;
    display: flex;
    align-items: center;
    justify-content: center;
    font-size: 24px;
    font-weight: 800;
    box-shadow: 0 12px 22px rgba(0, 71, 171, 0.18);
}

.ceoBriefingText {
    font-size: 18px;
    font-weight: 700;
    color: #0A1E3C;
    line-height: 1.7;
}

.ceoKpiGrid {
    display: grid;
    grid-template-columns: repeat(5, minmax(0, 1fr));
    gap: 16px;
}

.ceoKpiCard {
    background: #ffffff;
    border: 1px solid #d7e1f0;
    border-radius: 20px;
    padding: 20px;
    box-shadow: 0 10px 24px rgba(10, 30, 60, 0.04);
    min-width: 0;
}

.ceoKpiLabel {
    font-size: 13px;
    font-weight: 700;
    color: #6F7B8D;
    margin-bottom: 10px;
}

.ceoKpiValueRow {
    display: flex;
    justify-content: space-between;
    align-items: end;
    gap: 12px;
}

.ceoKpiValue {
    font-size: 30px;
    font-weight: 800;
    line-height: 1;
    color: #0A1E3C;
}

.ceoKpiUnit {
    font-size: 13px;
    font-weight: 700;
    color: #7A8799;
    margin-left: 4px;
}

.ceoKpiSub {
    margin-top: 14px;
    display: flex;
    justify-content: space-between;
    gap: 8px;
    align-items: center;
}

.ceoKpiSubLabel {
    font-size: 12px;
    color: #7A8799;
}

.ceoKpiSubValue {
    font-size: 12px;
    font-weight: 700;
    color: #22304A;
}

.ceoGrid2 {
    display: grid;
    grid-template-columns: 1.25fr 1fr;
    gap: 20px;
    min-width: 0;
}

.ceoPanel {
    background: #ffffff;
    border: 1px solid #d7e1f0;
    border-radius: 22px;
    padding: 22px;
    box-shadow: 0 10px 24px rgba(10, 30, 60, 0.04);
    min-width: 0;
}

.ceoPanelHead {
    display: flex;
    justify-content: space-between;
    align-items: center;
    gap: 12px;
    margin-bottom: 16px;
}

.ceoPanelTitle {
    margin: 0;
    font-size: 20px;
    font-weight: 800;
    color: #0A1E3C;
}

.ceoPanelSub {
    font-size: 12px;
    color: #7A8799;
}

.ceoRiskList {
    display: flex;
    flex-direction: column;
    gap: 12px;
}

.ceoRiskItem {
    display: grid;
    grid-template-columns: 52px 1fr auto;
    gap: 14px;
    align-items: center;
    padding: 16px;
    border-radius: 18px;
    border: 1px solid #e1e9f5;
    background: #fbfdff;
}

.ceoRiskRank {
    width: 52px;
    height: 52px;
    border-radius: 16px;
    background: linear-gradient(135deg, #0A1E3C 0%, #20457E 100%);
    color: #ffffff;
    font-size: 18px;
    font-weight: 800;
    display: flex;
    align-items: center;
    justify-content: center;
}

.ceoRiskMain {
    min-width: 0;
}

.ceoRiskTitle {
    font-size: 15px;
    font-weight: 800;
    color: #0A1E3C;
    margin-bottom: 6px;
}

.ceoRiskMeta {
    display: flex;
    flex-wrap: wrap;
    gap: 8px;
}

.ceoBadge {
    display: inline-flex;
    align-items: center;
    min-height: 28px;
    padding: 0 10px;
    border-radius: 999px;
    font-size: 12px;
    font-weight: 700;
    white-space: nowrap;
    background: #F4F8FF;
    color: #294B79;
}

.ceoRiskDetail {
    font-size: 13px;
    color: #6F7B8D;
    line-height: 1.5;
    text-align: right;
    max-width: 170px;
}

.ceoStatusGrid {
    display: grid;
    grid-template-columns: repeat(5, minmax(0, 1fr));
    gap: 14px;
}

.ceoStatusCard {
    border: 1px solid #e1e9f5;
    border-radius: 18px;
    padding: 18px;
    background: linear-gradient(180deg, #ffffff 0%, #f8fbff 100%);
}

.ceoStatusName {
    font-size: 14px;
    font-weight: 800;
    color: #0A1E3C;
    margin-bottom: 12px;
}

.ceoStatusScoreRow {
    display: flex;
    justify-content: space-between;
    align-items: baseline;
    gap: 8px;
    margin-bottom: 10px;
}

.ceoStatusScore {
    font-size: 28px;
    font-weight: 800;
    color: #0A1E3C;
}

.ceoStatusScoreUnit {
    font-size: 12px;
    color: #7A8799;
}

.ceoProgress {
    width: 100%;
    height: 10px;
    border-radius: 999px;
    overflow: hidden;
    background: #E9EFF8;
    margin-bottom: 10px;
}

.ceoProgressBar {
    height: 100%;
    border-radius: 999px;
    background: linear-gradient(90deg, #0047AB 0%, #4A9DFF 100%);
}

.ceoStatusBottom {
    display: flex;
    justify-content: space-between;
    gap: 10px;
    font-size: 12px;
    color: #6F7B8D;
}

.ceoCauseGrid {
    display: grid;
    grid-template-columns: repeat(3, minmax(0, 1fr));
    gap: 16px;
}

.ceoCauseCard {
    border: 1px solid #e1e9f5;
    border-radius: 18px;
    padding: 18px;
    background: #fbfdff;
}

.ceoCauseTitle {
    margin: 0 0 14px;
    font-size: 16px;
    font-weight: 800;
    color: #0A1E3C;
}

.ceoCauseList {
    display: flex;
    flex-direction: column;
    gap: 10px;
}

.ceoCauseItem {
    display: flex;
    justify-content: space-between;
    gap: 10px;
    align-items: center;
    padding: 12px 14px;
    border-radius: 14px;
    background: #ffffff;
    border: 1px solid #e8eef8;
}

.ceoCauseName {
    font-size: 14px;
    font-weight: 700;
    color: #22304A;
}

.ceoCauseValue {
    font-size: 13px;
    font-weight: 800;
    color: #0047AB;
    white-space: nowrap;
}

.ceoApprovalTableWrap {
    overflow-x: auto;
    border: 1px solid #d7e1f0;
    border-radius: 18px;
}

.ceoApprovalTable {
    width: 100%;
    min-width: 820px;
    border-collapse: collapse;
    table-layout: fixed;
}

.ceoApprovalTable th,
.ceoApprovalTable td {
    padding: 14px 10px;
    text-align: center;
    border-bottom: 1px solid #e7eef8;
    border-right: 1px solid #edf2fa;
    font-size: 13px;
}

.ceoApprovalTable th:last-child,
.ceoApprovalTable td:last-child {
    border-right: 0;
}

.ceoApprovalTable th {
    background: linear-gradient(180deg, #eaf1fb 0%, #dfeaf8 100%);
    color: #0A1E3C;
    font-weight: 800;
}

.ceoApprovalTable td {
    background: #ffffff;
    color: #22304A;
}

.ceoTrendGrid {
    display: grid;
    grid-template-columns: repeat(3, minmax(0, 1fr));
    gap: 16px;
}

.ceoTrendCard {
    border: 1px solid #e1e9f5;
    border-radius: 18px;
    padding: 18px;
    background: linear-gradient(180deg, #ffffff 0%, #f8fbff 100%);
}

.ceoTrendTitle {
    margin: 0 0 14px;
    font-size: 16px;
    font-weight: 800;
    color: #0A1E3C;
}

.ceoTrendChart {
    height: 250px;
    display: flex;
    align-items: end;
    gap: 12px;
    padding-top: 14px;
    border-top: 1px solid #eef3fa;
}

.ceoTrendCol {
    flex: 1;
    min-width: 0;
    height: 100%;
    display: flex;
    flex-direction: column;
    justify-content: end;
    align-items: center;
    gap: 8px;
}

.ceoTrendBarWrap {
    width: 100%;
    flex: 1;
    display: flex;
    align-items: end;
    justify-content: center;
}

.ceoTrendBar {
    width: 68%;
    min-height: 12px;
    border-radius: 14px 14px 6px 6px;
    background: linear-gradient(180deg, #7FB0FF 0%, #0047AB 100%);
    box-shadow: 0 12px 20px rgba(0, 71, 171, 0.14);
}

.ceoTrendValue {
    font-size: 12px;
    font-weight: 800;
    color: #0A1E3C;
    white-space: nowrap;
}

.ceoTrendLabel {
    font-size: 12px;
    color: #6F7B8D;
    text-align: center;
    line-height: 1.4;
    word-break: keep-all;
}

.ceoEmpty {
    padding: 34px 18px;
    text-align: center;
    color: #7A8799;
    font-size: 14px;
    border: 1px dashed #d8e3f2;
    border-radius: 16px;
    background: #fbfdff;
}

@media (max-width: 1440px) {
    .ceoKpiGrid,
    .ceoStatusGrid {
        grid-template-columns: repeat(3, minmax(0, 1fr));
    }

    .ceoTrendGrid {
        grid-template-columns: 1fr;
    }
}

@media (max-width: 1100px) {
    .ceoGrid2,
    .ceoCauseGrid {
        grid-template-columns: 1fr;
    }

    .ceoHero {
        flex-direction: column;
    }

    .ceoHeroMeta {
        min-width: 0;
    }
}

@media (max-width: 768px) {
    .ceoKpiGrid,
    .ceoStatusGrid {
        grid-template-columns: 1fr;
    }

    .ceoBriefingRow,
    .ceoRiskItem {
        grid-template-columns: 1fr;
    }

    .ceoRiskDetail {
        max-width: none;
        text-align: left;
    }

    .ceoHeroTitle {
        font-size: 24px;
    }
}
</style>

<div class="ceoPage taMaterialsInoutOnly">

<!--     상단 헤더 -->
<!--     <section class="ceoHero"> -->
<!--         <div> -->
<!--             <h2 class="ceoHeroTitle">CEO 대시보드</h2> -->
<!--             <p class="ceoHeroSub"> -->
<!--                 공장 전체 운영 상태, 오늘의 리스크, 결재 대상, 생산/품질/출하 추이를 한 화면에서 확인합니다. -->
<!--             </p> -->
<!--         </div> -->

<!--         <div class="ceoHeroMeta"> -->
<!--             <div class="ceoHeroMetaLabel">기준 정보</div> -->
<!--             <div class="ceoHeroMetaValue"> -->
<%--                 <c:choose> --%>
<%--                     <c:when test="${not empty baseDate}"> --%>
<%--                         <fmt:formatDate value="${baseDate}" pattern="yyyy-MM-dd" /> --%>
<%--                     </c:when> --%>
<%--                     <c:otherwise>기준일 미설정</c:otherwise> --%>
<%--                 </c:choose> --%>
<!--             </div> -->
<!--             <div class="ceoHeroMetaSub"> -->
<%--                 <span class="ceoChip ceoChipBlue">라인 ${empty totalLineCount ? 0 : totalLineCount}개 운영</span> --%>
<!--                 <span class="ceoChip ceoChipGreen">오늘 실적 집계 기준</span> -->
<!--             </div> -->
<!--         </div> -->
<!--     </section> -->

    <!-- 한줄 브리핑 -->
    <section class="ceoBriefingCard">
        <div class="ceoSectionEyebrow">ONE LINE BRIEFING</div>
        <div class="ceoBriefingRow">
            <div class="ceoBriefingIcon">!</div>
            <div class="ceoBriefingText">
                <c:out value="${briefingText}" default="오늘 CEO 브리핑 데이터가 아직 집계되지 않았습니다. 컨트롤러에서 briefingText를 전달해 주세요." />
            </div>
        </div>
    </section>

    <!-- KPI -->
    <section>
        <h3 class="taSectionTitle">핵심 KPI</h3>
        <div class="ceoKpiGrid">

            <article class="ceoKpiCard">
                <div class="ceoKpiLabel">생산달성률</div>
                <div class="ceoKpiValueRow">
                    <div class="ceoKpiValue">
                        <fmt:formatNumber value="${kpi.productionRate}" pattern="0.0" />
                        <span class="ceoKpiUnit">%</span>
                    </div>
                    <span class="ceoChip ceoChipBlue">
                        목표 <fmt:formatNumber value="${kpi.productionTargetRate}" pattern="0.0" />%
                    </span>
                </div>
                <div class="ceoKpiSub">
                    <span class="ceoKpiSubLabel">계획 ${empty kpi.planQty ? 0 : kpi.planQty}</span>
                    <span class="ceoKpiSubValue">실적 ${empty kpi.actualQty ? 0 : kpi.actualQty}</span>
                </div>
            </article>

            <article class="ceoKpiCard">
                <div class="ceoKpiLabel">납기준수율</div>
                <div class="ceoKpiValueRow">
                    <div class="ceoKpiValue">
                        <fmt:formatNumber value="${kpi.deliveryRate}" pattern="0.0" />
                        <span class="ceoKpiUnit">%</span>
                    </div>
                    <span class="ceoChip ceoChipGreen">
                        정상 ${empty kpi.onTimeCount ? 0 : kpi.onTimeCount}건
                    </span>
                </div>
                <div class="ceoKpiSub">
                    <span class="ceoKpiSubLabel">전체 ${empty kpi.deliveryTargetCount ? 0 : kpi.deliveryTargetCount}건</span>
                    <span class="ceoKpiSubValue">지연 ${empty kpi.delayCount ? 0 : kpi.delayCount}건</span>
                </div>
            </article>

            <article class="ceoKpiCard">
                <div class="ceoKpiLabel">OEE</div>
                <div class="ceoKpiValueRow">
                    <div class="ceoKpiValue">
                        <fmt:formatNumber value="${kpi.oeeRate}" pattern="0.0" />
                        <span class="ceoKpiUnit">%</span>
                    </div>
                    <span class="ceoChip ceoChipBlue">
                        가동 ${empty kpi.availabilityRate ? 0 : kpi.availabilityRate}%
                    </span>
                </div>
                <div class="ceoKpiSub">
                    <span class="ceoKpiSubLabel">성능 ${empty kpi.performanceRate ? 0 : kpi.performanceRate}%</span>
                    <span class="ceoKpiSubValue">품질 ${empty kpi.qualityRate ? 0 : kpi.qualityRate}%</span>
                </div>
            </article>

            <article class="ceoKpiCard">
                <div class="ceoKpiLabel">불량률</div>
                <div class="ceoKpiValueRow">
                    <div class="ceoKpiValue">
                        <fmt:formatNumber value="${kpi.defectRate}" pattern="0.00" />
                        <span class="ceoKpiUnit">%</span>
                    </div>
                    <span class="ceoChip ceoChipRed">
                        불량 ${empty kpi.defectQty ? 0 : kpi.defectQty}
                    </span>
                </div>
                <div class="ceoKpiSub">
                    <span class="ceoKpiSubLabel">검사수량 ${empty kpi.inspectQty ? 0 : kpi.inspectQty}</span>
                    <span class="ceoKpiSubValue">양품 ${empty kpi.goodQty ? 0 : kpi.goodQty}</span>
                </div>
            </article>

            <article class="ceoKpiCard">
                <div class="ceoKpiLabel">재고위험</div>
                <div class="ceoKpiValueRow">
                    <div class="ceoKpiValue">
                        ${empty kpi.inventoryRiskCount ? 0 : kpi.inventoryRiskCount}
                        <span class="ceoKpiUnit">건</span>
                    </div>
                    <span class="ceoChip ceoChipAmber">
                        안전재고 미달
                    </span>
                </div>
                <div class="ceoKpiSub">
                    <span class="ceoKpiSubLabel">긴급발주 ${empty kpi.urgentOrderCount ? 0 : kpi.urgentOrderCount}건</span>
                    <span class="ceoKpiSubValue">출하영향 ${empty kpi.shipRiskCount ? 0 : kpi.shipRiskCount}건</span>
                </div>
            </article>

        </div>
    </section>

    <!-- 리스크 + 공장 상태 -->
    <section class="ceoGrid2">

        <article class="ceoPanel">
            <div class="ceoPanelHead">
                <div>
                    <h3 class="ceoPanelTitle">오늘의 리스크 TOP 5</h3>
                    <div class="ceoPanelSub">중요도와 영향도를 기준으로 정렬</div>
                </div>
                <span class="ceoChip ceoChipRed">즉시 확인 필요</span>
            </div>

            <c:choose>
                <c:when test="${not empty riskList}">
                    <div class="ceoRiskList">
                        <c:forEach var="risk" items="${riskList}" varStatus="status">
                            <div class="ceoRiskItem">
                                <div class="ceoRiskRank">${status.count}</div>

                                <div class="ceoRiskMain">
                                    <div class="ceoRiskTitle">${risk.title}</div>
                                    <div class="ceoRiskMeta">
                                        <span class="ceoBadge">${risk.category}</span>
                                        <span class="ceoBadge">${risk.targetName}</span>
                                        <span class="ceoBadge">${risk.severity}</span>
                                    </div>
                                </div>

                                <div class="ceoRiskDetail">
                                    ${risk.detail}
                                </div>
                            </div>
                        </c:forEach>
                    </div>
                </c:when>
                <c:otherwise>
                    <div class="ceoEmpty">riskList 데이터가 없어서 리스크 목록이 비어 있습니다.</div>
                </c:otherwise>
            </c:choose>
        </article>

        <article class="ceoPanel">
            <div class="ceoPanelHead">
                <div>
                    <h3 class="ceoPanelTitle">공장 전체 상태 맵</h3>
                    <div class="ceoPanelSub">영역별 상태 점수와 이슈 건수</div>
                </div>
                <span class="ceoChip ceoChipBlue">실시간 요약</span>
            </div>

            <c:choose>
                <c:when test="${not empty factoryStatusList}">
                    <div class="ceoStatusGrid">
                        <c:forEach var="status" items="${factoryStatusList}">
                            <div class="ceoStatusCard">
                                <div class="ceoStatusName">${status.areaName}</div>

                                <div class="ceoStatusScoreRow">
                                    <div>
                                        <span class="ceoStatusScore">${status.score}</span>
                                        <span class="ceoStatusScoreUnit">/ 100</span>
                                    </div>
                                    <span class="ceoChip
                                        ${status.score ge 90 ? 'ceoChipGreen' : (status.score ge 75 ? 'ceoChipAmber' : 'ceoChipRed')}">
                                        ${status.statusLabel}
                                    </span>
                                </div>

                                <div class="ceoProgress">
                                    <div class="ceoProgressBar" style="width:${status.score}%;"></div>
                                </div>

                                <div class="ceoStatusBottom">
                                    <span>이슈 ${status.issueCount}건</span>
                                    <span>${status.detail}</span>
                                </div>
                            </div>
                        </c:forEach>
                    </div>
                </c:when>
                <c:otherwise>
                    <div class="ceoStatusGrid">
                        <div class="ceoStatusCard">
                            <div class="ceoStatusName">생산</div>
                            <div class="ceoStatusScoreRow">
                                <div><span class="ceoStatusScore">0</span><span class="ceoStatusScoreUnit">/ 100</span></div>
                                <span class="ceoChip ceoChipBlue">대기</span>
                            </div>
                            <div class="ceoProgress"><div class="ceoProgressBar" style="width:0%;"></div></div>
                            <div class="ceoStatusBottom"><span>이슈 0건</span><span>데이터 없음</span></div>
                        </div>
                        <div class="ceoStatusCard">
                            <div class="ceoStatusName">품질</div>
                            <div class="ceoStatusScoreRow">
                                <div><span class="ceoStatusScore">0</span><span class="ceoStatusScoreUnit">/ 100</span></div>
                                <span class="ceoChip ceoChipBlue">대기</span>
                            </div>
                            <div class="ceoProgress"><div class="ceoProgressBar" style="width:0%;"></div></div>
                            <div class="ceoStatusBottom"><span>이슈 0건</span><span>데이터 없음</span></div>
                        </div>
                        <div class="ceoStatusCard">
                            <div class="ceoStatusName">설비</div>
                            <div class="ceoStatusScoreRow">
                                <div><span class="ceoStatusScore">0</span><span class="ceoStatusScoreUnit">/ 100</span></div>
                                <span class="ceoChip ceoChipBlue">대기</span>
                            </div>
                            <div class="ceoProgress"><div class="ceoProgressBar" style="width:0%;"></div></div>
                            <div class="ceoStatusBottom"><span>이슈 0건</span><span>데이터 없음</span></div>
                        </div>
                        <div class="ceoStatusCard">
                            <div class="ceoStatusName">자재</div>
                            <div class="ceoStatusScoreRow">
                                <div><span class="ceoStatusScore">0</span><span class="ceoStatusScoreUnit">/ 100</span></div>
                                <span class="ceoChip ceoChipBlue">대기</span>
                            </div>
                            <div class="ceoProgress"><div class="ceoProgressBar" style="width:0%;"></div></div>
                            <div class="ceoStatusBottom"><span>이슈 0건</span><span>데이터 없음</span></div>
                        </div>
                        <div class="ceoStatusCard">
                            <div class="ceoStatusName">출하</div>
                            <div class="ceoStatusScoreRow">
                                <div><span class="ceoStatusScore">0</span><span class="ceoStatusScoreUnit">/ 100</span></div>
                                <span class="ceoChip ceoChipBlue">대기</span>
                            </div>
                            <div class="ceoProgress"><div class="ceoProgressBar" style="width:0%;"></div></div>
                            <div class="ceoStatusBottom"><span>이슈 0건</span><span>데이터 없음</span></div>
                        </div>
                    </div>
                </c:otherwise>
            </c:choose>
        </article>

    </section>

    <!-- 원인 분석 요약 -->
    <section class="ceoPanel">
        <div class="ceoPanelHead">
            <div>
                <h3 class="ceoPanelTitle">원인 분석 요약</h3>
                <div class="ceoPanelSub">비가동 / 불량 / 납기 지연 주요 원인</div>
            </div>
            <span class="ceoChip ceoChipBlue">TOP 3 기준</span>
        </div>

        <div class="ceoCauseGrid">

            <div class="ceoCauseCard">
                <h4 class="ceoCauseTitle">비가동 원인 TOP 3</h4>
                <c:choose>
                    <c:when test="${not empty downtimeCauseList}">
                        <div class="ceoCauseList">
                            <c:forEach var="cause" items="${downtimeCauseList}">
                                <div class="ceoCauseItem">
                                    <span class="ceoCauseName">${cause.causeName}</span>
                                    <span class="ceoCauseValue">${cause.causeValue}</span>
                                </div>
                            </c:forEach>
                        </div>
                    </c:when>
                    <c:otherwise>
                        <div class="ceoEmpty">downtimeCauseList 데이터가 없습니다.</div>
                    </c:otherwise>
                </c:choose>
            </div>

            <div class="ceoCauseCard">
                <h4 class="ceoCauseTitle">불량 원인 TOP 3</h4>
                <c:choose>
                    <c:when test="${not empty defectCauseList}">
                        <div class="ceoCauseList">
                            <c:forEach var="cause" items="${defectCauseList}">
                                <div class="ceoCauseItem">
                                    <span class="ceoCauseName">${cause.causeName}</span>
                                    <span class="ceoCauseValue">${cause.causeValue}</span>
                                </div>
                            </c:forEach>
                        </div>
                    </c:when>
                    <c:otherwise>
                        <div class="ceoEmpty">defectCauseList 데이터가 없습니다.</div>
                    </c:otherwise>
                </c:choose>
            </div>

            <div class="ceoCauseCard">
                <h4 class="ceoCauseTitle">출하 지연 원인 TOP 3</h4>
                <c:choose>
                    <c:when test="${not empty delayCauseList}">
                        <div class="ceoCauseList">
                            <c:forEach var="cause" items="${delayCauseList}">
                                <div class="ceoCauseItem">
                                    <span class="ceoCauseName">${cause.causeName}</span>
                                    <span class="ceoCauseValue">${cause.causeValue}</span>
                                </div>
                            </c:forEach>
                        </div>
                    </c:when>
                    <c:otherwise>
                        <div class="ceoEmpty">delayCauseList 데이터가 없습니다.</div>
                    </c:otherwise>
                </c:choose>
            </div>

        </div>
    </section>

    <!-- 오늘 결재 / 승인 필요 항목 -->
    <section class="ceoPanel">
        <div class="ceoPanelHead">
            <div>
                <h3 class="ceoPanelTitle">오늘 결재 / 승인 필요 항목</h3>
                <div class="ceoPanelSub">우선순위가 높은 문서부터 빠르게 확인</div>
            </div>
            <span class="ceoChip ceoChipAmber">당일 처리 권장</span>
        </div>

        <c:choose>
            <c:when test="${not empty approvalList}">
                <div class="ceoApprovalTableWrap">
                    <table class="ceoApprovalTable">
                        <thead>
                            <tr>
                                <th style="width: 90px;">순번</th>
                                <th style="width: 120px;">문서유형</th>
                                <th>제목</th>
                                <th style="width: 120px;">요청자</th>
                                <th style="width: 140px;">요청일시</th>
                                <th style="width: 110px;">우선순위</th>
                                <th style="width: 120px;">상태</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach var="approval" items="${approvalList}" varStatus="status">
                                <tr>
                                    <td>${status.count}</td>
                                    <td>${approval.docType}</td>
                                    <td>${approval.title}</td>
                                    <td>${approval.requesterName}</td>
                                    <td>
                                        <fmt:formatDate value="${approval.requestedAt}" pattern="yyyy-MM-dd HH:mm" />
                                    </td>
                                    <td>${approval.priority}</td>
                                    <td>${approval.status}</td>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>
                </div>
            </c:when>
            <c:otherwise>
                <div class="ceoEmpty">approvalList 데이터가 없습니다.</div>
            </c:otherwise>
        </c:choose>
    </section>

    <!-- 생산 / 품질 / 출하 추이 -->
    <section class="ceoPanel">
        <div class="ceoPanelHead">
            <div>
                <h3 class="ceoPanelTitle">생산 / 품질 / 출하 추이</h3>
                <div class="ceoPanelSub">최근 기간 기준 흐름 확인</div>
            </div>
            <span class="ceoChip ceoChipBlue">트렌드 차트</span>
        </div>

        <div class="ceoTrendGrid">

            <!-- 생산 추이 -->
            <div class="ceoTrendCard">
                <h4 class="ceoTrendTitle">생산 추이</h4>
                <c:choose>
                    <c:when test="${not empty productionTrendList}">
                        <div class="ceoTrendChart">
                            <c:forEach var="item" items="${productionTrendList}">
                                <div class="ceoTrendCol">
                                    <div class="ceoTrendBarWrap">
                                        <div class="ceoTrendBar"
                                             style="height:${productionTrendMax gt 0 ? (item.value * 100 / productionTrendMax) : 0}%;">
                                        </div>
                                    </div>
                                    <div class="ceoTrendValue">${item.value}</div>
                                    <div class="ceoTrendLabel">${item.label}</div>
                                </div>
                            </c:forEach>
                        </div>
                    </c:when>
                    <c:otherwise>
                        <div class="ceoEmpty">productionTrendList 데이터가 없습니다.</div>
                    </c:otherwise>
                </c:choose>
            </div>

            <!-- 품질 추이 -->
            <div class="ceoTrendCard">
                <h4 class="ceoTrendTitle">품질 추이</h4>
                <c:choose>
                    <c:when test="${not empty qualityTrendList}">
                        <div class="ceoTrendChart">
                            <c:forEach var="item" items="${qualityTrendList}">
                                <div class="ceoTrendCol">
                                    <div class="ceoTrendBarWrap">
                                        <div class="ceoTrendBar"
                                             style="height:${qualityTrendMax gt 0 ? (item.value * 100 / qualityTrendMax) : 0}%;">
                                        </div>
                                    </div>
                                    <div class="ceoTrendValue">${item.value}</div>
                                    <div class="ceoTrendLabel">${item.label}</div>
                                </div>
                            </c:forEach>
                        </div>
                    </c:when>
                    <c:otherwise>
                        <div class="ceoEmpty">qualityTrendList 데이터가 없습니다.</div>
                    </c:otherwise>
                </c:choose>
            </div>

            <!-- 출하 추이 -->
            <div class="ceoTrendCard">
                <h4 class="ceoTrendTitle">출하 추이</h4>
                <c:choose>
                    <c:when test="${not empty shipmentTrendList}">
                        <div class="ceoTrendChart">
                            <c:forEach var="item" items="${shipmentTrendList}">
                                <div class="ceoTrendCol">
                                    <div class="ceoTrendBarWrap">
                                        <div class="ceoTrendBar"
                                             style="height:${shipmentTrendMax gt 0 ? (item.value * 100 / shipmentTrendMax) : 0}%;">
                                        </div>
                                    </div>
                                    <div class="ceoTrendValue">${item.value}</div>
                                    <div class="ceoTrendLabel">${item.label}</div>
                                </div>
                            </c:forEach>
                        </div>
                    </c:when>
                    <c:otherwise>
                        <div class="ceoEmpty">shipmentTrendList 데이터가 없습니다.</div>
                    </c:otherwise>
                </c:choose>
            </div>

        </div>
    </section>

</div>