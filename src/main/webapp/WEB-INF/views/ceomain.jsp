<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<div class="ceoPage taMaterialsInoutOnly">

	<!-- 한줄 브리핑 -->
	<section class="ceoBriefingCard">
		<div class="ceoSectionEyebrow">ONE LINE BRIEFING</div>
		<div class="ceoBriefingRow">
			<div class="ceoBriefingIcon">!</div>
			<div class="ceoBriefingText">
				<c:out value="${briefingText}" default="오늘 CEO 브리핑 데이터가 없습니다." />
			</div>
		</div>
	</section>

	<c:set var="deliveryTargetRate" value="100.0" />
	<c:set var="oeeTargetRate" value="85.0" />
	<c:set var="defectTargetText" value="목표 1.0% 이하" />
	<c:set var="costVarianceTargetText" value="목표 ±0.0%" />

	<!-- KPI -->
	<section>
		<h3 class="taSectionTitle">핵심 KPI</h3>

		<c:set var="dailyCostSum" value="${0}" />
		<c:set var="dailyCostTopName" value="-" />
		<c:set var="dailyCostTopUnitCost" value="${0}" />

		<c:forEach var="item" items="${topCostItemList}" varStatus="status">
			<c:set var="dailyCostSum"
				value="${dailyCostSum + (empty item.totalCost ? 0 : item.totalCost)}" />
			<c:if test="${status.first}">
				<c:set var="dailyCostTopName" value="${item.itemName}" />
				<c:set var="dailyCostTopUnitCost"
					value="${empty item.actualUnitCost ? 0 : item.actualUnitCost}" />
			</c:if>
		</c:forEach>

		<div class="ceoKpiGrid">

			<article class="ceoKpiCard ceoKpiCardProduction">
				<div class="ceoKpiTop">
					<div class="ceoKpiLabel">생산달성률</div>
					<div class="ceoKpiValueRow">
						<div class="ceoKpiValueWrap">
							<div class="ceoKpiValue">
								<fmt:formatNumber
									value="${empty kpi.productionRate ? 0 : kpi.productionRate}"
									pattern="#,##0.0" />
							</div>
							<div class="ceoKpiUnit">%</div>
						</div>
						<span class="ceoChip ceoChipBlue"> 목표 <fmt:formatNumber
								value="${empty kpi.productionTargetRate ? 0 : kpi.productionTargetRate}"
								pattern="#,##0.0" />%
						</span>
					</div>
				</div>

				<div class="ceoKpiBottom ceoKpiSimpleList">
					<c:forEach var="idx" begin="0" end="2">
						<c:choose>
							<c:when
								test="${not empty topCostItemList and idx lt fn:length(topCostItemList)}">
								<c:set var="item" value="${topCostItemList[idx]}" />
								<div class="ceoKpiMetaRow ceoKpiSimpleRow">
									<span class="ceoKpiMetaLabel ceoKpiSimpleName">${item.itemName}</span>
									<span class="ceoKpiMetaValue ceoKpiSimpleInfo"> 계획 <fmt:formatNumber
											value="${empty item.planQty ? 0 : item.planQty}"
											pattern="#,##0.###" /> ${empty item.itemUnit ? 'kg' : item.itemUnit}
										/ 실적 <fmt:formatNumber
											value="${empty item.actualQty ? (empty item.producedQty ? 0 : item.producedQty) : item.actualQty}"
											pattern="#,##0.###" /> ${empty item.itemUnit ? 'kg' : item.itemUnit}
									</span>
								</div>
							</c:when>
							<c:otherwise>
								<div class="ceoKpiMetaRow ceoKpiSimpleRow">
									<span class="ceoKpiMetaLabel ceoKpiSimpleName">&nbsp;</span> <span
										class="ceoKpiMetaValue ceoKpiSimpleInfo">&nbsp;</span>
								</div>
							</c:otherwise>
						</c:choose>
					</c:forEach>
				</div>
			</article>

			<article class="ceoKpiCard">
				<div class="ceoKpiTop">
					<div class="ceoKpiLabel">납기준수율</div>
					<div class="ceoKpiValueRow">
						<div class="ceoKpiValueWrap">
							<div class="ceoKpiValue">
								<fmt:formatNumber
									value="${empty kpi.deliveryRate ? 0 : kpi.deliveryRate}"
									pattern="#,##0.0" />
							</div>
							<div class="ceoKpiUnit">%</div>
						</div>
						<span class="ceoChip ceoChipBlue ceoChipTarget"> 목표 <fmt:formatNumber
								value="${deliveryTargetRate}" pattern="#,##0.0" />%
						</span>
					</div>
				</div>

				<div class="ceoKpiBottom">
					<div class="ceoKpiMetaRow">
						<span class="ceoKpiMetaLabel">전체</span> <span
							class="ceoKpiMetaValue">${empty kpi.deliveryTargetCount ? 0 : kpi.deliveryTargetCount}건</span>
					</div>
					<div class="ceoKpiMetaRow">
						<span class="ceoKpiMetaLabel">지연</span> <span
							class="ceoKpiMetaValue">${empty kpi.delayCount ? 0 : kpi.delayCount}건</span>
					</div>
					<div class="ceoKpiMetaRow">&nbsp;</div>
				</div>
			</article>

			<article class="ceoKpiCard">
				<div class="ceoKpiTop">
					<div class="ceoKpiLabel">OEE</div>
					<div class="ceoKpiValueRow">
						<div class="ceoKpiValueWrap">
							<div class="ceoKpiValue">
								<fmt:formatNumber value="${empty kpi.oeeRate ? 0 : kpi.oeeRate}"
									pattern="#,##0.0" />
							</div>
							<div class="ceoKpiUnit">%</div>
						</div>
						<span class="ceoChip ceoChipBlue ceoChipTarget"> 목표 <fmt:formatNumber
								value="${oeeTargetRate}" pattern="#,##0.0" />%
						</span>
					</div>
				</div>

				<div class="ceoKpiBottom">
					<div class="ceoKpiMetaRow">
						<span class="ceoKpiMetaLabel">라인가동률</span> <span
							class="ceoKpiMetaValue"> <fmt:formatNumber
								value="${empty kpi.availabilityRate ? 0 : kpi.availabilityRate}"
								pattern="#,##0.0" />%
						</span>
					</div>
					<div class="ceoKpiMetaRow">
						<span class="ceoKpiMetaLabel">생산달성률</span> <span
							class="ceoKpiMetaValue"> <fmt:formatNumber
								value="${empty kpi.performanceRate ? 0 : kpi.performanceRate}"
								pattern="#,##0.0" />%
						</span>
					</div>
					<div class="ceoKpiMetaRow">
						<span class="ceoKpiMetaLabel">품질양품률</span> <span
							class="ceoKpiMetaValue"> <fmt:formatNumber
								value="${empty kpi.qualityRate ? 0 : kpi.qualityRate}"
								pattern="#,##0.0" />%
						</span>
					</div>
				</div>
			</article>

			<article class="ceoKpiCard">
				<div class="ceoKpiTop">
					<div class="ceoKpiLabel">불량률</div>
					<div class="ceoKpiValueRow">
						<div class="ceoKpiValueWrap">
							<div class="ceoKpiValue">
								<fmt:formatNumber
									value="${empty kpi.defectRate ? 0 : kpi.defectRate}"
									pattern="#,##0.00" />
							</div>
							<div class="ceoKpiUnit">%</div>
						</div>
						<span class="ceoChip ceoChipBlue ceoChipTargetWide">
							${defectTargetText} </span>
					</div>
				</div>

				<div class="ceoKpiBottom">
					<div class="ceoKpiMetaRow">
						<span class="ceoKpiMetaLabel">검사수량</span> <span
							class="ceoKpiMetaValue"> <fmt:formatNumber
								value="${empty kpi.inspectQty ? 0 : kpi.inspectQty}"
								pattern="#,##0.###" /> kg
						</span>
					</div>
					<div class="ceoKpiMetaRow">
						<span class="ceoKpiMetaLabel">양품</span> <span
							class="ceoKpiMetaValue"> <fmt:formatNumber
								value="${empty kpi.goodQty ? 0 : kpi.goodQty}"
								pattern="#,##0.###" /> kg
						</span>
					</div>
					<div class="ceoKpiMetaRow">
						<span class="ceoKpiMetaLabel">불량</span> <span
							class="ceoKpiMetaValue ceoTextDanger"> <fmt:formatNumber
								value="${empty kpi.defectQty ? 0 : kpi.defectQty}"
								pattern="#,##0.###" /> kg
						</span>
					</div>
				</div>
			</article>

			<article class="ceoKpiCard">
				<div class="ceoKpiTop">
					<div class="ceoKpiLabel">원가편차율</div>
					<div class="ceoKpiValueRow">
						<div class="ceoKpiValueWrap">
							<div class="ceoKpiValue">
								<fmt:formatNumber
									value="${empty kpi.costVarianceRate ? 0 : kpi.costVarianceRate}"
									pattern="#,##0.00" />
							</div>
							<div class="ceoKpiUnit">%</div>
						</div>
						<span class="ceoChip ceoChipBlue ceoChipTargetWide">
							${costVarianceTargetText} </span>
					</div>
				</div>

				<div class="ceoKpiBottom">
					<div class="ceoKpiMetaRow">
						<span class="ceoKpiMetaLabel">표준단가</span> <span
							class="ceoKpiMetaValue"> <fmt:formatNumber
								value="${empty kpi.standardUnitCost ? 0 : kpi.standardUnitCost}"
								pattern="#,##0" /> 원/kg
						</span>
					</div>
					<div class="ceoKpiMetaRow">
						<span class="ceoKpiMetaLabel">실적단가</span> <span
							class="ceoKpiMetaValue"> <fmt:formatNumber
								value="${empty kpi.actualUnitCost ? 0 : kpi.actualUnitCost}"
								pattern="#,##0" /> 원/kg
						</span>
					</div>
					<div class="ceoKpiMetaRow">&nbsp;</div>
				</div>
			</article>

			<article class="ceoKpiCard ceoKpiCardCostSimple">
				<div class="ceoKpiTop">
					<div class="ceoKpiLabel">일일생산원가</div>
				</div>

				<div class="ceoKpiBottom ceoKpiSimpleList">
					<c:forEach var="idx" begin="0" end="2">
						<c:choose>
							<c:when
								test="${not empty topCostItemList and idx lt fn:length(topCostItemList)}">
								<c:set var="item" value="${topCostItemList[idx]}" />
								<div class="ceoKpiMetaRow ceoKpiSimpleRow">
									<span class="ceoKpiMetaLabel ceoKpiSimpleName">${item.itemName}</span>
									<span class="ceoKpiMetaValue ceoKpiSimpleInfo"> <fmt:formatNumber
											value="${empty item.actualUnitCost ? 0 : item.actualUnitCost}"
											pattern="#,##0" /> 원/${empty item.itemUnit ? 'kg' : item.itemUnit}
										[<fmt:formatNumber
											value="${empty item.actualQty ? (empty item.producedQty ? 0 : item.producedQty) : item.actualQty}"
											pattern="#,##0.###" /> ${empty item.itemUnit ? 'kg' : item.itemUnit}]
									</span>
								</div>
							</c:when>
							<c:otherwise>
								<div class="ceoKpiMetaRow ceoKpiSimpleRow">
									<span class="ceoKpiMetaLabel ceoKpiSimpleName">&nbsp;</span> <span
										class="ceoKpiMetaValue ceoKpiSimpleInfo">&nbsp;</span>
								</div>
							</c:otherwise>
						</c:choose>
					</c:forEach>
				</div>
			</article>

		</div>
	</section>

	<!-- 리스크 + 공장 상태 -->
	<section class="ceoRiskStatusLayout">

		<!-- 왼쪽 : 리스크 TOP5 -->
		<article class="ceoPanel ceoRiskPanel">
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
									<div class="ceoRiskMeta ceoRiskMetaOneLine">
										<span class="ceoBadge">${risk.category}</span> <span
											class="ceoBadge">${risk.targetName}</span> <span
											class="ceoBadge">${risk.severity}</span>
									</div>
								</div>

								<div class="ceoRiskDetail">${risk.detail}</div>
							</div>
						</c:forEach>
					</div>
				</c:when>
				<c:otherwise>
					<div class="ceoEmpty">riskList 데이터가 없습니다.</div>
				</c:otherwise>
			</c:choose>
		</article>

		<!-- 오른쪽 : 공장 상태 맵만 -->
		<article class="ceoPanel ceoStatusPanel ceoStatusPanelWide">
			<div class="ceoPanelHead">
				<div>
					<h3 class="ceoPanelTitle">공장 전체 상태 맵</h3>
					<div class="ceoPanelSub">영역별 상태 점수와 이슈 건수</div>
				</div>
				<span class="ceoChip ceoChipBlue">실시간 요약</span>
			</div>

			<c:choose>
				<c:when test="${not empty factoryStatusList}">
					<div class="ceoStatusGrid ceoStatusGridCompact">
						<c:forEach var="status" items="${factoryStatusList}">
							<div class="ceoStatusCard">
								<div>
									<div class="ceoStatusName">${status.areaName}</div>
									<div class="ceoStatusScoreRow">
										<div class="ceoStatusScoreWrap">
											<span class="ceoStatusScore">${status.score}</span> <span
												class="ceoStatusScoreUnit">/ 100</span>
										</div>
										<span
											class="ceoChip ${status.score ge 90 ? 'ceoChipGreen' : (status.score ge 75 ? 'ceoChipAmber' : 'ceoChipRed')}">
											${status.statusLabel} </span>
									</div>
									<div class="ceoProgress">
										<div class="ceoProgressBar" style="width:${status.score}%;"></div>
									</div>
								</div>
								<div class="ceoStatusBottom">
									<span>이슈 <fmt:formatNumber value="${status.issueCount}"
											pattern="#,##0" />건
									</span> <span>${status.detail}</span>
								</div>
							</div>
						</c:forEach>
					</div>
				</c:when>
				<c:otherwise>
					<div class="ceoEmpty">factoryStatusList 데이터가 없습니다.</div>
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
									<span class="ceoCauseName">${cause.causeName}</span> <span
										class="ceoCauseValue">${cause.causeValue}</span>
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
									<span class="ceoCauseName">${cause.causeName}</span> <span
										class="ceoCauseValue">${cause.causeValue}</span>
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
									<span class="ceoCauseName">${cause.causeName}</span> <span
										class="ceoCauseValue">${cause.causeValue}</span>
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
							<c:forEach var="approval" items="${approvalList}"
								varStatus="status">
								<tr>
									<td>${status.count}</td>
									<td>${approval.docType}</td>
									<td class="ceoTdTitle">${approval.title}</td>
									<td>${approval.requesterName}</td>
									<td><fmt:formatDate value="${approval.requestedAt}"
											pattern="yyyy-MM-dd HH:mm" /></td>
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

	<!-- 추이 -->
	<section class="ceoPanel">
		<div class="ceoPanelHead">
			<div>
				<h3 class="ceoPanelTitle">생산 / 품질 / 출하 / 생산원가 추이</h3>
				<div class="ceoPanelSub">최근 7일 기준 흐름 확인</div>
			</div>
			<span class="ceoChip ceoChipBlue">최근 7일 추이도</span>
		</div>

		<div class="ceoTrendGrid">

			<div class="ceoTrendCard">
				<div class="ceoTrendTitleWrap">
					<h4 class="ceoTrendTitle">생산 추이</h4>
					<div class="ceoTrendSub">일자별 생산실적 기준</div>
				</div>

				<c:choose>
					<c:when test="${not empty productionTrendList}">
						<div class="ceoTrendChart">
							<c:forEach var="item" items="${productionTrendList}">
								<div class="ceoTrendCol">
									<div class="ceoTrendBarWrap">
										<div class="ceoTrendBar"
											style="height:${productionTrendMax gt 0 ? (item.value * 100 / productionTrendMax) : 0}%;"></div>
									</div>
									<div class="ceoTrendValue">
										<fmt:formatNumber value="${empty item.value ? 0 : item.value}"
											pattern="#,##0" />
										kg
									</div>
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

			<div class="ceoTrendCard">
				<div class="ceoTrendTitleWrap">
					<h4 class="ceoTrendTitle">품질 추이</h4>
					<div class="ceoTrendSub">일자별 합격률 기준</div>
				</div>

				<c:choose>
					<c:when test="${not empty qualityTrendList}">
						<div class="ceoTrendChart">
							<c:forEach var="item" items="${qualityTrendList}">
								<div class="ceoTrendCol">
									<div class="ceoTrendBarWrap">
										<div class="ceoTrendBar ceoTrendBarQuality"
											style="height:${qualityTrendMax gt 0 ? (item.value * 100 / qualityTrendMax) : 0}%;"></div>
									</div>
									<div class="ceoTrendValue">
										<fmt:formatNumber value="${empty item.value ? 0 : item.value}"
											pattern="#,##0.0" />
										%
									</div>
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

			<div class="ceoTrendCard">
				<div class="ceoTrendTitleWrap">
					<h4 class="ceoTrendTitle">출하 추이</h4>
					<div class="ceoTrendSub">일자별 출하수량 기준</div>
				</div>

				<c:choose>
					<c:when test="${not empty shipmentTrendList}">
						<div class="ceoTrendChart">
							<c:forEach var="item" items="${shipmentTrendList}">
								<div class="ceoTrendCol">
									<div class="ceoTrendBarWrap">
										<div class="ceoTrendBar ceoTrendBarShipment"
											style="height:${shipmentTrendMax gt 0 ? (item.value * 100 / shipmentTrendMax) : 0}%;"></div>
									</div>
									<div class="ceoTrendValue">
										<fmt:formatNumber value="${empty item.value ? 0 : item.value}"
											pattern="#,##0" />
										kg
									</div>
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

			<div class="ceoTrendCard">
				<div class="ceoTrendTitleWrap">
					<h4 class="ceoTrendTitle">생산원가 추이</h4>
					<div class="ceoTrendSub">일자별 평균 실적단가 기준</div>
				</div>

				<c:choose>
					<c:when test="${not empty costTrendList}">
						<div class="ceoTrendChart">
							<c:forEach var="item" items="${costTrendList}">
								<div class="ceoTrendCol">
									<div class="ceoTrendBarWrap">
										<div class="ceoTrendBar ceoTrendBarCost"
											style="height:${costTrendMax gt 0 ? (item.value * 100 / costTrendMax) : 0}%;"></div>
									</div>
									<div class="ceoTrendValue">
										<fmt:formatNumber value="${empty item.value ? 0 : item.value}"
											pattern="#,##0" />
										원/kg
									</div>
									<div class="ceoTrendLabel">${item.label}</div>
								</div>
							</c:forEach>
						</div>
					</c:when>
					<c:otherwise>
						<div class="ceoEmpty">costTrendList 데이터가 없습니다.</div>
					</c:otherwise>
				</c:choose>
			</div>

		</div>
	</section>

	<div class="ceoPageUpdate">
		정보갱신일시 :
		<c:choose>
			<c:when test="${not empty baseDate}">
				<fmt:formatDate value="${baseDate}" pattern="yyyy-MM-dd HH:mm" />
			</c:when>
			<c:otherwise>-</c:otherwise>
		</c:choose>
	</div>
</div>