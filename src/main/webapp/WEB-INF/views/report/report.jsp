<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<link rel="stylesheet"
	href="${pageContext.request.contextPath}/assets/css/report.css">
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/assets/css/responsive.css">

<div class="reportPage">
	<div class="reportFilterBox">
		<form method="get" action="${pageContext.request.contextPath}/report"
			class="reportFilterForm">
			<div class="reportField">
				<label for="startDate">시작일</label> <input class="reportInput"
					type="date" id="startDate" name="startDate" value="${startDate}">
			</div>
			<div class="reportField">
				<label for="endDate">종료일</label> <input class="reportInput"
					type="date" id="endDate" name="endDate" value="${endDate}">
			</div>
			<button type="submit" class="reportBtn">조회</button>
		</form>
	</div>

	<div class="reportSummaryGrid">
		<div class="reportSummaryCard">
			<div class="reportSummaryLabel">총 생산량</div>
			<div class="reportSummaryValue">
				<fmt:formatNumber value="${report.summary.totalProducedQty}"
					pattern="#,##0.###" />
			</div>
			<div class="reportSummaryHint">선택 기간 내 생산실적 합계</div>
		</div>
		<div class="reportSummaryCard">
			<div class="reportSummaryLabel">총 손실량</div>
			<div class="reportSummaryValue">
				<fmt:formatNumber value="${report.summary.totalLossQty}"
					pattern="#,##0.###" />
			</div>
			<div class="reportSummaryHint">LOSS_QTY 기준 집계</div>
		</div>
		<div class="reportSummaryCard">
			<div class="reportSummaryLabel">불량 발생 건수</div>
			<div class="reportSummaryValue">
				<fmt:formatNumber value="${report.summary.defectCount}"
					pattern="#,##0" />
			</div>
			<div class="reportSummaryHint">완제품 검사 연계 기준</div>
		</div>
		<div class="reportSummaryCard">
			<div class="reportSummaryLabel">목표 달성률</div>
			<div class="reportSummaryValue">
				<fmt:formatNumber value="${report.summary.achievementRate}"
					pattern="#0.0" />
				%
			</div>
			<div class="reportSummaryHint">생산계획 대비 생산실적 비율</div>
		</div>
	</div>

	<div class="reportTwoCol">
		<div class="reportPanel">
			<h3 class="reportPanelTitle">생산실적 리포트</h3>
			<p class="reportPanelSub">월별 생산량 및 손실량 (단위: ${prodUnit})</p>

			<div class="reportChartBox">
				<div class="reportChartTitle">월별 생산 현황</div>

				<c:choose>
					<c:when test="${not empty report.productionTrendList}">
						<c:set var="maxProduced" value="0" />

						<c:forEach var="row" items="${report.productionTrendList}">
							<c:if test="${row.producedQty > maxProduced}">
								<c:set var="maxProduced" value="${row.producedQty}" />
							</c:if>
						</c:forEach>

						<div class="reportProdChart">
							<c:forEach var="row" items="${report.productionTrendList}">
								<div class="reportProdCol">
									<div class="reportProdPlot">
										<div class="reportProdBar"
											style="height:${maxProduced == 0 ? 8 : (row.producedQty / maxProduced) * 180 + 12}px"
											title="생산량 <fmt:formatNumber value='${row.producedQty}' pattern='#,##0.###' />">
										</div>
									</div>

									<div class="reportProdValues">
										<div class="reportBarValue">
											생산
											<fmt:formatNumber value="${row.producedQty}"
												pattern="#,##0.###" />
										</div>
										<div class="reportLossValue">
											손실
											<fmt:formatNumber value="${row.lossQty}" pattern="#,##0.###" />
										</div>
									</div>

									<div class="reportBarLabel">${row.label}</div>
								</div>
							</c:forEach>
						</div>
					</c:when>

					<c:otherwise>
						<div class="reportEmpty">선택 기간에 생산실적 데이터가 없습니다.</div>
					</c:otherwise>
				</c:choose>
			</div>

			<div class="reportCaption">
				📌
				<c:out value="${report.productionCaption}" />
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
							<c:if test="${row.defectCount > maxDefectCount}">
								<c:set var="maxDefectCount" value="${row.defectCount}" />
							</c:if>
						</c:forEach>

						<div class="reportDefectChart">
							<c:forEach var="row" items="${report.defectTypeList}">
								<div class="reportDefectCol">
									<div class="reportDefectBars">
										<div class="reportDefectBar"
											style="height:${maxDefectCount == 0 ? 8 : (row.defectCount / maxDefectCount) * 180 + 12}px"
											title="${row.defectName} ${row.defectCount}건"></div>
									</div>
									<div class="reportDefectValue">${row.defectCount}건</div>
									<div class="reportDefectLabel">${row.defectName}</div>
								</div>
							</c:forEach>
						</div>
					</c:when>
					<c:otherwise>
						<div class="reportEmpty">선택 기간에 불량 데이터가 없습니다.</div>
					</c:otherwise>
				</c:choose>
			</div>
			<div class="reportCaption">
				📌
				<c:out value="${report.defectCaption}" />
			</div>
		</div>
	</div>

	<div class="reportPanel">
		<h3 class="reportPanelTitle">목표달성 리포트</h3>
		<p class="reportPanelSub">품목별 계획 수량, 실적 수량 비교</p>

		<c:set var="maxGapQty" value="0" />
		<c:if test="${not empty report.achievementList}">
			<c:forEach var="row" items="${report.achievementList}">
				<c:set var="gapQty" value="${row.planQty - row.producedQty}" />
				<c:if test="${gapQty > maxGapQty}">
					<c:set var="maxGapQty" value="${gapQty}" />
				</c:if>
			</c:forEach>
		</c:if>

		<div class="reportAchDesktop">
			<div class="taTableShell">
				<div class="taTableScroll">
					<table class="taMesTable">
						<thead>
							<tr>
								<th class="taTableHeadCell taColGrow">품목명</th>
								<th class="taTableHeadCell taColFit">계획수량</th>
								<th class="taTableHeadCell taColFit">실적수량</th>
								<th class="taTableHeadCell taColFit">부족수량</th>
								<th class="taTableHeadCell taLastCol taColFit">달성률</th>
							</tr>
						</thead>
						<tbody>
							<c:choose>
								<c:when test="${not empty report.achievementList}">
									<c:forEach var="row" items="${report.achievementList}">
										<c:set var="gapQty" value="${row.planQty - row.producedQty}" />
										<c:set var="safeGapQty" value="${gapQty > 0 ? gapQty : 0}" />

										<tr
											class="taTableBodyRow ${safeGapQty == maxGapQty && safeGapQty > 0 ? 'reportRowWeak' : ''}">
											<td class="taTableBodyCell taColGrow">
												<div class="reportAchItemCell">
													<span>${row.itemName}</span>
													<c:if test="${safeGapQty == maxGapQty && safeGapQty > 0}">
														<span class="reportAchWarn">우선 점검</span>
													</c:if>
												</div>
											</td>
											<td class="taTableBodyCell taColFit"><fmt:formatNumber
													value="${row.planQty}" pattern="#,##0.###" /> ${row.unit}
											</td>
											<td class="taTableBodyCell taColFit"><fmt:formatNumber
													value="${row.producedQty}" pattern="#,##0.###" />
												${row.unit}</td>
											<td class="taTableBodyCell taColFit"><span
												class="reportAchGapText"> <fmt:formatNumber
														value="${safeGapQty}" pattern="#,##0.###" /> ${row.unit}
											</span></td>
											<td class="taTableBodyCell taLastCol taColFit"><span
												class="reportAchRateText"> <fmt:formatNumber
														value="${row.achievementRate}" pattern="#0.0" />%
											</span></td>
										</tr>
									</c:forEach>
								</c:when>
								<c:otherwise>
									<tr>
										<td class="taTableBodyCell taLastCol" colspan="5">선택 기간에
											계획/실적 데이터가 없습니다.</td>
									</tr>
								</c:otherwise>
							</c:choose>
						</tbody>
					</table>
				</div>
			</div>
		</div>

		<div class="reportAchMobile">
			<div class="reportAchCardList">
				<c:forEach var="row" items="${report.achievementList}">
					<c:set var="gapQty" value="${row.planQty - row.producedQty}" />
					<c:set var="safeGapQty" value="${gapQty > 0 ? gapQty : 0}" />

					<div
						class="reportAchCard ${safeGapQty == maxGapQty && safeGapQty > 0 ? 'reportAchCardWeak' : ''}">
						<div class="reportAchCardHead">${row.itemName}</div>

						<div class="reportAchCardBody">
							<div class="reportAchCardRow">
								<div class="reportAchCardLabel">계획수량</div>
								<div class="reportAchCardValue">
									<fmt:formatNumber value="${row.planQty}" pattern="#,##0.###" />
									${row.unit}
								</div>
							</div>

							<div class="reportAchCardRow">
								<div class="reportAchCardLabel">실적수량</div>
								<div class="reportAchCardValue">
									<fmt:formatNumber value="${row.producedQty}"
										pattern="#,##0.###" />
									${row.unit}
								</div>
							</div>

							<div class="reportAchCardRow">
								<div class="reportAchCardLabel">부족수량</div>
								<div class="reportAchCardValue reportAchCardGap">
									<fmt:formatNumber value="${safeGapQty}" pattern="#,##0.###" />
									${row.unit}
								</div>
							</div>

							<div class="reportAchCardRow">
								<div class="reportAchCardLabel">달성률</div>
								<div class="reportAchCardValue">
									<fmt:formatNumber value="${row.achievementRate}" pattern="#0.0" />
									%
								</div>
							</div>

							<c:if test="${safeGapQty == maxGapQty && safeGapQty > 0}">
								<div class="reportAchCardWarn">우선 점검</div>
							</c:if>
						</div>
					</div>
				</c:forEach>
			</div>
		</div>

		<div class="reportCaption">
			📌
			<c:out value="${report.achievementCaption}" />
		</div>
	</div>

	<div class="reportPanel">
		<h3 class="reportPanelTitle">설비이력 리포트</h3>
		<p class="reportPanelSub">설비별 이슈 빈도</p>

		<c:choose>
			<c:when test="${not empty report.equipmentIssueList}">

				<!-- PC / 태블릿용 테이블 -->
				<div class="reportEquipDesktop">
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
									<c:forEach var="row" items="${report.equipmentIssueList}">
										<tr class="taTableBodyRow">
											<td class="taTableBodyCell taColGrow">${row.equipmentName}</td>
											<td class="taTableBodyCell taColFit">${row.maintenanceCount}</td>
											<td class="taTableBodyCell taColFit">${row.failureCount}</td>
											<td class="taTableBodyCell taLastCol taColFit">${row.totalIssueCount}</td>
										</tr>
									</c:forEach>
								</tbody>
							</table>
						</div>
					</div>
				</div>

				<!-- 모바일용 카드 -->
				<div class="reportEquipMobile">
					<div class="reportEquipCardList">
						<c:forEach var="row" items="${report.equipmentIssueList}">
							<div class="reportEquipCard">
								<div class="reportEquipCardHead">${row.equipmentName}</div>
								<div class="reportEquipCardBody">
									<div class="reportEquipCardRow">
										<div class="reportEquipCardLabel">정비</div>
										<div class="reportEquipCardValue">${row.maintenanceCount}</div>
									</div>
									<div class="reportEquipCardRow">
										<div class="reportEquipCardLabel">고장</div>
										<div class="reportEquipCardValue">${row.failureCount}</div>
									</div>
									<div class="reportEquipCardRow">
										<div class="reportEquipCardLabel">합계</div>
										<div class="reportEquipCardValue">${row.totalIssueCount}</div>
									</div>
								</div>
							</div>
						</c:forEach>
					</div>
				</div>

			</c:when>

			<c:otherwise>
				<div class="reportEmpty">선택 기간에 설비 이력 데이터가 없습니다.</div>
			</c:otherwise>
		</c:choose>

		<div class="reportCaption">
			📌
			<c:out value="${report.equipmentCaption}" />
		</div>
	</div>
</div>