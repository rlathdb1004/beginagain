<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<c:set var="qtyUnit"
	value="${empty kpi.qtyUnitLabel ? 'kg' : kpi.qtyUnitLabel}" />
<c:set var="productionAchievementRate"
	value="${empty kpi.productionAchievementRate ? 0 : kpi.productionAchievementRate}" />
<c:set var="yieldRate"
	value="${empty kpi.yieldRate ? 0 : kpi.yieldRate}" />
<c:set var="otdRate" value="${empty kpi.otdRate ? 0 : kpi.otdRate}" />
<c:set var="oeeRate" value="${empty kpi.oeeRate ? 0 : kpi.oeeRate}" />

<div class="prodDashPage">

	<!-- 상단 헤더 -->
	<section class="prodDashHead">
		<div class="prodDashHeadLeft">
			<h2 class="prodDashTitle">생산관리 대시보드</h2>
			<p class="prodDashSub">생산계획 달성률, 수율, 납기 준수율, 설비 종합 효율을 한 화면에서 관리</p>
		</div>

		<div class="prodDashHeadRight">
			<div class="prodDashDateBox">
				<span class="prodDashDateLabel">기준일</span> <strong
					class="prodDashDateValue"> <c:choose>
						<c:when test="${not empty baseDate}">
							<fmt:formatDate value="${baseDate}" pattern="yyyy-MM-dd" />
						</c:when>
						<c:otherwise>-</c:otherwise>
					</c:choose>
				</strong>
			</div>
		</div>
	</section>

	<!-- KPI -->
	<section class="prodDashSection">
		<h3 class="prodDashSectionTitle">핵심 KPI</h3>

		<div class="prodDashKpiGrid prodDashKpiGridFour">

			<!-- 생산계획 달성률 -->
			<article class="prodDashKpiCard prodDashKpiClickable"
				onclick="openProdKpiModal('kpiAchievementModal')">
				<div class="prodDashKpiLabel">생산계획 달성률</div>
				<div class="prodDashKpiValueRow">
					<div class="prodDashKpiValue">
						<fmt:formatNumber value="${productionAchievementRate}"
							pattern="#,##0.0" />
					</div>
					<div class="prodDashKpiUnitInline">%</div>
				</div>
				<div class="prodDashKpiMeta">계획대로 생산이 완료되었는지 측정하는 가장 기초적인 지표</div>
			</article>

			<!-- 수율 -->
			<article class="prodDashKpiCard prodDashKpiClickable"
				onclick="openProdKpiModal('kpiYieldModal')">
				<div class="prodDashKpiLabel">수율 (Yield)</div>
				<div class="prodDashKpiValueRow">
					<div class="prodDashKpiValue">
						<fmt:formatNumber value="${yieldRate}" pattern="#,##0.0" />
					</div>
					<div class="prodDashKpiUnitInline">%</div>
				</div>
				<div class="prodDashKpiMeta">불량 없이 양품을 얼마나 만들어냈는지 평가하는 품질 및
					효율성 지표</div>
			</article>

			<!-- 납기 준수율 -->
			<article class="prodDashKpiCard prodDashKpiClickable"
				onclick="openProdKpiModal('kpiOtdModal')">
				<div class="prodDashKpiLabel">납기 준수율 (OTD)</div>
				<div class="prodDashKpiValueRow">
					<div class="prodDashKpiValue">
						<fmt:formatNumber value="${otdRate}" pattern="#,##0.0" />
					</div>
					<div class="prodDashKpiUnitInline">%</div>
				</div>
				<div class="prodDashKpiMeta">고객이 요구한 날짜에 제품을 납품했는지 측정</div>
			</article>

			<!-- OEE -->
			<article class="prodDashKpiCard prodDashKpiClickable"
				onclick="openProdKpiModal('kpiOeeModal')">
				<div class="prodDashKpiLabel">설비 종합 효율 (OEE)</div>
				<div class="prodDashKpiValueRow">
					<div class="prodDashKpiValue">
						<fmt:formatNumber value="${oeeRate}" pattern="#,##0.0" />
					</div>
					<div class="prodDashKpiUnitInline">%</div>
				</div>
				<div class="prodDashKpiMeta">가동률, 성능 효율, 양품률을 종합한 설비 효율 지표</div>
			</article>

		</div>
	</section>

	<!-- 라인별 생산 / 설비 현황 -->
	<section class="prodDashSection">
		<article class="prodDashPanel">
			<div class="prodDashPanelHead">
				<div>
					<h3 class="prodDashPanelTitle">라인별 생산 / 설비 현황</h3>
					<p class="prodDashPanelSub">라인 상태, 생산 달성률, 설비 가동률, 비가동시간을 한 번에
						표시</p>
				</div>
			</div>

			<div class="prodDashMergedLineGrid">
				<c:choose>
					<c:when test="${not empty lineStatusList}">
						<c:forEach var="line" items="${lineStatusList}" varStatus="st">
							<c:set var="runtime" value="${lineRuntimeList[st.index]}" />

							<div class="prodDashMergedLineCard">
								<div class="prodDashMergedLineHead">
									<div class="prodDashLineName">${empty line.lineName ? '-' : line.lineName}
									</div>
									<div class="prodDashLineState">${empty line.statusLabel ? '-' : line.statusLabel}
									</div>
								</div>

								<div class="prodDashMergedDonutWrap">
									<div class="prodDashMergedDonut"
										style="--runtime:${empty runtime.runtimeRate ? 0 : runtime.runtimeRate};">
										<div class="prodDashMergedDonutInner">
											<strong> <fmt:formatNumber
													value="${empty runtime.runtimeRate ? 0 : runtime.runtimeRate}"
													pattern="#,##0.0" />%
											</strong> <span>가동률</span>
										</div>
									</div>
								</div>

								<div class="prodDashMergedInfoList">
									<div class="prodDashMergedInfoRow">
										<span>생산 달성률</span> <strong> <fmt:formatNumber
												value="${empty line.achievementRate ? 0 : line.achievementRate}"
												pattern="#,##0.0" />%
										</strong>
									</div>
									<div class="prodDashMergedInfoRow">
										<span>가동시간</span> <strong> <fmt:formatNumber
												value="${empty runtime.runtimeMin ? 0 : runtime.runtimeMin}"
												pattern="#,##0" />분
										</strong>
									</div>
									<div class="prodDashMergedInfoRow">
										<span>비가동시간</span> <strong> <fmt:formatNumber
												value="${empty runtime.downtimeMin ? 0 : runtime.downtimeMin}"
												pattern="#,##0" />분
										</strong>
									</div>
								</div>
							</div>
						</c:forEach>
					</c:when>
					<c:otherwise>
						<div class="prodDashEmpty">라인별 생산 / 설비 현황 데이터가 없습니다.</div>
					</c:otherwise>
				</c:choose>
			</div>
		</article>
	</section>

	<!-- 비가동 원인 -->
	<article class="prodDashPanel">
		<div class="prodDashPanelHead">
			<div>
				<h3 class="prodDashPanelTitle">비가동 원인</h3>
				<p class="prodDashPanelSub">라인별 비가동 주요 원인과 시간</p>
			</div>
		</div>

		<div class="prodDashTableWrap">
			<table class="prodDashTable">
				<thead>
					<tr>
						<th>라인</th>
						<th>설비명</th>
						<th>비가동시간</th>
						<th>원인코드</th>
						<th>원인상세</th>
						<th>시작시각</th>
					</tr>
				</thead>
				<tbody>
					<c:choose>
						<c:when test="${not empty downtimeCauseList}">
							<c:forEach var="cause" items="${downtimeCauseList}">
								<tr>
									<td>${empty cause.lineName ? '-' : cause.lineName}</td>
									<td>${empty cause.equipmentName ? '-' : cause.equipmentName}</td>
									<td><fmt:formatNumber
											value="${empty cause.downtimeMin ? 0 : cause.downtimeMin}"
											pattern="#,##0" />분</td>
									<td>${empty cause.causeCode ? '-' : cause.causeCode}</td>
									<td class="taLeft">${empty cause.causeDetail ? '-' : cause.causeDetail}</td>
									<td>${empty cause.startTimeText ? '-' : cause.startTimeText}</td>
								</tr>
							</c:forEach>
						</c:when>
						<c:otherwise>
							<tr>
								<td colspan="6" class="prodDashEmptyCell">비가동 원인 데이터가 없습니다.</td>
							</tr>
						</c:otherwise>
					</c:choose>
				</tbody>
			</table>
		</div>
	</article>

	<!-- 자재 / 설비 알림 -->
	<article class="prodDashPanel">
		<div class="prodDashPanelHead">
			<div>
				<h3 class="prodDashPanelTitle">자재 / 설비 알림</h3>
				<p class="prodDashPanelSub">현장 운영에 영향이 있는 알림</p>
			</div>
		</div>

		<div class="prodDashAlertSection prodDashAlertSectionTwoCol">
			<div class="prodDashAlertBlock">
				<div class="prodDashMiniTitle">자재 알림</div>
				<ul class="prodDashMiniList">
					<c:choose>
						<c:when test="${not empty materialAlertList}">
							<c:forEach var="material" items="${materialAlertList}">
								<li><span>${material.itemName}</span> <strong>${material.detail}</strong>
								</li>
							</c:forEach>
						</c:when>
						<c:otherwise>
							<li class="prodDashMiniEmpty">자재 알림 없음</li>
						</c:otherwise>
					</c:choose>
				</ul>
			</div>

			<div class="prodDashAlertBlock">
				<div class="prodDashMiniTitle">설비 알림</div>
				<ul class="prodDashMiniList">
					<c:choose>
						<c:when test="${not empty equipmentAlertList}">
							<c:forEach var="equipment" items="${equipmentAlertList}">
								<li><span>${equipment.equipmentName}</span> <strong>${equipment.detail}</strong>
								</li>
							</c:forEach>
						</c:when>
						<c:otherwise>
							<li class="prodDashMiniEmpty">설비 알림 없음</li>
						</c:otherwise>
					</c:choose>
				</ul>
			</div>
		</div>
	</article>

	<!-- =========================
         KPI MODAL : 생산계획 달성률
         ========================= -->
	<div id="kpiAchievementModal" class="prodDashModal"
		onclick="closeProdKpiModalByBackdrop(event, 'kpiAchievementModal')">
		<div class="prodDashModalDialog">
			<div class="prodDashModalHead">
				<h3 class="prodDashModalTitle">생산계획 달성률 상세</h3>
				<button type="button" class="prodDashModalClose"
					onclick="closeProdKpiModal('kpiAchievementModal')">×</button>
			</div>

			<div class="prodDashModalBody">
				<div class="prodDashModalSummary">
					<div class="prodDashModalSummaryItem">
						<span>생산계획 달성률</span> <strong><fmt:formatNumber
								value="${productionAchievementRate}" pattern="#,##0.0" />%</strong>
					</div>
					<div class="prodDashModalSummaryItem">
						<span>총 계획수량</span> <strong><fmt:formatNumber
								value="${empty kpi.planQty ? 0 : kpi.planQty}"
								pattern="#,##0.###" /> ${qtyUnit}</strong>
					</div>
					<div class="prodDashModalSummaryItem">
						<span>총 실적수량</span> <strong><fmt:formatNumber
								value="${empty kpi.actualQty ? 0 : kpi.actualQty}"
								pattern="#,##0.###" /> ${qtyUnit}</strong>
					</div>
				</div>

				<div class="prodDashModalTableWrap">
					<table class="prodDashModalTable">
						<thead>
							<tr>
								<th>생산계획번호</th>
								<th>작업지시번호</th>
								<th>품목명</th>
								<th>계획수량</th>
								<th>실적수량</th>
								<th>달성률</th>
								<th>단위</th>
								<th>납기일</th>
								<th>작업자</th>
								<th>상태</th>
							</tr>
						</thead>
						<tbody>
							<c:choose>
								<c:when test="${not empty planDetailList}">
									<c:forEach var="plan" items="${planDetailList}">
										<tr>
											<td>${empty plan.planNo ? '-' : plan.planNo}</td>
											<td>${empty plan.workOrderNo ? '-' : plan.workOrderNo}</td>
											<td class="taLeft">${empty plan.itemName ? '-' : plan.itemName}</td>
											<td><fmt:formatNumber
													value="${empty plan.planQty ? 0 : plan.planQty}"
													pattern="#,##0.###" /></td>
											<td><fmt:formatNumber
													value="${empty plan.actualQty ? 0 : plan.actualQty}"
													pattern="#,##0.###" /></td>
											<td><fmt:formatNumber
													value="${empty plan.achievementRate ? 0 : plan.achievementRate}"
													pattern="#,##0.0" />%</td>
											<td>${empty plan.unit ? qtyUnit : plan.unit}</td>
											<td>${empty plan.dueDateText ? '-' : plan.dueDateText}</td>
											<td>${empty plan.workerName ? '-' : plan.workerName}</td>
											<td>${empty plan.status ? '-' : plan.status}</td>
										</tr>
									</c:forEach>
								</c:when>
								<c:otherwise>
									<tr>
										<td colspan="10" class="prodDashEmptyCell">생산계획 상세 데이터가
											없습니다.</td>
									</tr>
								</c:otherwise>
							</c:choose>
						</tbody>
					</table>
				</div>
			</div>
		</div>
	</div>

	<!-- KPI MODAL : 수율 -->
	<div id="kpiYieldModal" class="prodDashModal"
		onclick="closeProdKpiModalByBackdrop(event, 'kpiYieldModal')">
		<div class="prodDashModalDialog">
			<div class="prodDashModalHead">
				<h3 class="prodDashModalTitle">수율 상세</h3>
				<button type="button" class="prodDashModalClose"
					onclick="closeProdKpiModal('kpiYieldModal')">×</button>
			</div>

			<div class="prodDashModalBody">
				<div class="prodDashModalSummary">
					<div class="prodDashModalSummaryItem">
						<span>수율</span> <strong><fmt:formatNumber
								value="${yieldRate}" pattern="#,##0.0" />%</strong>
					</div>
					<div class="prodDashModalSummaryItem">
						<span>양품수량</span> <strong><fmt:formatNumber
								value="${empty kpi.goodQty ? 0 : kpi.goodQty}"
								pattern="#,##0.###" /> ${qtyUnit}</strong>
					</div>
					<div class="prodDashModalSummaryItem">
						<span>불량수량</span> <strong><fmt:formatNumber
								value="${empty kpi.defectQty ? 0 : kpi.defectQty}"
								pattern="#,##0.###" /> ${qtyUnit}</strong>
					</div>
				</div>

				<div class="prodDashModalTableWrap">
					<table class="prodDashModalTable">
						<thead>
							<tr>
								<th>생산계획번호</th>
								<th>작업지시번호</th>
								<th>생산실적번호</th>
								<th>품목명</th>
								<th>총생산량</th>
								<th>양품수량</th>
								<th>불량수량</th>
								<th>수율</th>
								<th>단위</th>
								<th>작업자</th>
							</tr>
						</thead>
						<tbody>
							<c:choose>
								<c:when test="${not empty yieldDetailList}">
									<c:forEach var="yieldRow" items="${yieldDetailList}">
										<tr>
											<td>${empty yieldRow.planNo ? '-' : yieldRow.planNo}</td>
											<td>${empty yieldRow.workOrderNo ? '-' : yieldRow.workOrderNo}</td>
											<td>${empty yieldRow.resultNo ? '-' : yieldRow.resultNo}</td>
											<td class="taLeft">${empty yieldRow.itemName ? '-' : yieldRow.itemName}</td>
											<td><fmt:formatNumber
													value="${empty yieldRow.actualQty ? 0 : yieldRow.actualQty}"
													pattern="#,##0.###" /></td>
											<td><fmt:formatNumber
													value="${empty yieldRow.goodQty ? 0 : yieldRow.goodQty}"
													pattern="#,##0.###" /></td>
											<td><fmt:formatNumber
													value="${empty yieldRow.defectQty ? 0 : yieldRow.defectQty}"
													pattern="#,##0.###" /></td>
											<td><fmt:formatNumber
													value="${empty yieldRow.yieldRate ? 0 : yieldRow.yieldRate}"
													pattern="#,##0.0" />%</td>
											<td>${empty yieldRow.unit ? qtyUnit : yieldRow.unit}</td>
											<td>${empty yieldRow.workerName ? '-' : yieldRow.workerName}</td>
										</tr>
									</c:forEach>
								</c:when>
								<c:otherwise>
									<tr>
										<td colspan="10" class="prodDashEmptyCell">수율 상세 데이터가
											없습니다.</td>
									</tr>
								</c:otherwise>
							</c:choose>
						</tbody>
					</table>
				</div>
			</div>
		</div>
	</div>

	<!-- KPI MODAL : 납기 준수율 -->
	<div id="kpiOtdModal" class="prodDashModal"
		onclick="closeProdKpiModalByBackdrop(event, 'kpiOtdModal')">
		<div class="prodDashModalDialog">
			<div class="prodDashModalHead">
				<h3 class="prodDashModalTitle">납기 준수율 상세</h3>
				<button type="button" class="prodDashModalClose"
					onclick="closeProdKpiModal('kpiOtdModal')">×</button>
			</div>

			<div class="prodDashModalBody">
				<div class="prodDashModalSummary">
					<div class="prodDashModalSummaryItem">
						<span>납기 준수율</span> <strong><fmt:formatNumber
								value="${otdRate}" pattern="#,##0.0" />%</strong>
					</div>
				</div>

				<div class="prodDashModalTableWrap">
					<table class="prodDashModalTable">
						<thead>
							<tr>
								<th>생산계획번호</th>
								<th>작업지시번호</th>
								<th>판매오더번호</th>
								<th>품목명</th>
								<th>출하번호</th>
								<th>요구납기일</th>
								<th>실제출하일</th>
								<th>정시여부</th>
								<th>수량</th>
								<th>단위</th>
								<th>작업자</th>
							</tr>
						</thead>
						<tbody>
							<c:choose>
								<c:when test="${not empty shipmentDetailList}">
									<c:forEach var="ship" items="${shipmentDetailList}">
										<tr>
											<td>${empty ship.planNo ? '-' : ship.planNo}</td>
											<td>${empty ship.workOrderNo ? '-' : ship.workOrderNo}</td>
											<td>${empty ship.salesOrderNo ? '-' : ship.salesOrderNo}</td>
											<td class="taLeft">${empty ship.itemName ? '-' : ship.itemName}</td>
											<td>${empty ship.shipmentNo ? '-' : ship.shipmentNo}</td>
											<td>${empty ship.dueDateText ? '-' : ship.dueDateText}</td>
											<td>${empty ship.shipDateText ? '-' : ship.shipDateText}</td>
											<td>${empty ship.onTimeYn ? '-' : ship.onTimeYn}</td>
											<td><fmt:formatNumber
													value="${empty ship.shipQty ? 0 : ship.shipQty}"
													pattern="#,##0.###" /></td>
											<td>${empty ship.unit ? qtyUnit : ship.unit}</td>
											<td>${empty ship.workerName ? '-' : ship.workerName}</td>
										</tr>
									</c:forEach>
								</c:when>
								<c:otherwise>
									<tr>
										<td colspan="11" class="prodDashEmptyCell">납기 상세 데이터가
											없습니다.</td>
									</tr>
								</c:otherwise>
							</c:choose>
						</tbody>
					</table>
				</div>
			</div>
		</div>
	</div>

	<!-- KPI MODAL : OEE -->
	<div id="kpiOeeModal" class="prodDashModal"
		onclick="closeProdKpiModalByBackdrop(event, 'kpiOeeModal')">
		<div class="prodDashModalDialog">
			<div class="prodDashModalHead">
				<h3 class="prodDashModalTitle">OEE 상세</h3>
				<button type="button" class="prodDashModalClose"
					onclick="closeProdKpiModal('kpiOeeModal')">×</button>
			</div>

			<div class="prodDashModalBody">
				<div class="prodDashModalSummary">
					<div class="prodDashModalSummaryItem">
						<span>OEE</span> <strong><fmt:formatNumber
								value="${oeeRate}" pattern="#,##0.0" />%</strong>
					</div>
					<div class="prodDashModalSummaryItem">
						<span>가동률</span> <strong><fmt:formatNumber
								value="${empty kpi.availabilityRate ? 0 : kpi.availabilityRate}"
								pattern="#,##0.0" />%</strong>
					</div>
					<div class="prodDashModalSummaryItem">
						<span>성능 효율</span> <strong><fmt:formatNumber
								value="${empty kpi.performanceRate ? 0 : kpi.performanceRate}"
								pattern="#,##0.0" />%</strong>
					</div>
					<div class="prodDashModalSummaryItem">
						<span>양품률</span> <strong><fmt:formatNumber
								value="${empty kpi.qualityRate ? 0 : kpi.qualityRate}"
								pattern="#,##0.0" />%</strong>
					</div>
				</div>

				<div class="prodDashModalTableWrap">
					<table class="prodDashModalTable">
						<thead>
							<tr>
								<th>라인명</th>
								<th>생산계획번호</th>
								<th>작업지시번호</th>
								<th>품목명</th>
								<th>가동시간</th>
								<th>비가동시간</th>
								<th>가동률</th>
								<th>성능효율</th>
								<th>양품률</th>
								<th>작업자</th>
							</tr>
						</thead>
						<tbody>
							<c:choose>
								<c:when test="${not empty oeeDetailList}">
									<c:forEach var="oee" items="${oeeDetailList}">
										<tr>
											<td>${empty oee.lineName ? '-' : oee.lineName}</td>
											<td>${empty oee.planNo ? '-' : oee.planNo}</td>
											<td>${empty oee.workOrderNo ? '-' : oee.workOrderNo}</td>
											<td class="taLeft">${empty oee.itemName ? '-' : oee.itemName}</td>
											<td><fmt:formatNumber
													value="${empty oee.runtimeMin ? 0 : oee.runtimeMin}"
													pattern="#,##0" />분</td>
											<td><fmt:formatNumber
													value="${empty oee.downtimeMin ? 0 : oee.downtimeMin}"
													pattern="#,##0" />분</td>
											<td><fmt:formatNumber
													value="${empty oee.runtimeRate ? 0 : oee.runtimeRate}"
													pattern="#,##0.0" />%</td>
											<td><fmt:formatNumber
													value="${empty oee.performanceRate ? 0 : oee.performanceRate}"
													pattern="#,##0.0" />%</td>
											<td><fmt:formatNumber
													value="${empty oee.qualityRate ? 0 : oee.qualityRate}"
													pattern="#,##0.0" />%</td>
											<td>${empty oee.workerName ? '-' : oee.workerName}</td>
										</tr>
									</c:forEach>
								</c:when>
								<c:otherwise>
									<tr>
										<td colspan="10" class="prodDashEmptyCell">OEE 상세 데이터가
											없습니다.</td>
									</tr>
								</c:otherwise>
							</c:choose>
						</tbody>
					</table>
				</div>
			</div>
		</div>
	</div>
	<c:if test="${not empty updatedAt}">
		<div class="prodDashUpdatedAt">
			갱신일시 :
			<fmt:formatDate value="${updatedAt}" pattern="yyyy-MM-dd HH:mm:ss" />
		</div>
	</c:if>

</div>

<script>
	function openProdKpiModal(modalId) {
		var modal = document.getElementById(modalId);
		if (modal) {
			modal.classList.add("is-open");
			document.body.classList.add("prodDashModalOpen");
		}
	}

	function closeProdKpiModal(modalId) {
		var modal = document.getElementById(modalId);
		if (modal) {
			modal.classList.remove("is-open");
		}

		if (!document.querySelector(".prodDashModal.is-open")) {
			document.body.classList.remove("prodDashModalOpen");
		}
	}

	function closeProdKpiModalByBackdrop(event, modalId) {
		if (event.target.id === modalId) {
			closeProdKpiModal(modalId);
		}
	}

	document.addEventListener("keydown", function(e) {
		if (e.key === "Escape") {
			var opened = document.querySelectorAll(".prodDashModal.is-open");
			opened.forEach(function(modal) {
				modal.classList.remove("is-open");
			});
			document.body.classList.remove("prodDashModalOpen");
		}
	});
</script>