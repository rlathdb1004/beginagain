<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<c:set var="summary" value="${summary}" />
<c:set var="approvalPendingCount"
	value="${empty summary.approvalPendingCount ? 0 : summary.approvalPendingCount}" />
<c:set var="interfaceFailCount"
	value="${empty summary.interfaceFailCount ? 0 : summary.interfaceFailCount}" />
<c:set var="batchFailCount"
	value="${empty summary.batchFailCount ? 0 : summary.batchFailCount}" />
<c:set var="dataIssueCount"
	value="${empty summary.dataIssueCount ? 0 : summary.dataIssueCount}" />
<c:set var="authRequestCount"
	value="${empty summary.authRequestCount ? 0 : summary.authRequestCount}" />
<c:set var="noticeUnreadCount"
	value="${empty summary.noticeUnreadCount ? 0 : summary.noticeUnreadCount}" />

<div class="adminDashPage">

	<!-- 상단 헤더 -->
	<section class="adminDashHead">
		<div class="adminDashHeadLeft">
			<h2 class="adminDashTitle">MES 관리자 대시보드</h2>
			<p class="adminDashSub">시스템 운영 상태, 승인 대기, 인터페이스, 배치, 데이터 품질을 통합
				관리</p>
		</div>

		<div class="adminDashHeadRight">
			<div class="adminDashDateBox">
				<span class="adminDashDateLabel">기준일</span> <strong
					class="adminDashDateValue"> <c:choose>
						<c:when test="${not empty baseDate}">
							<fmt:formatDate value="${baseDate}" pattern="yyyy-MM-dd" />
						</c:when>
						<c:otherwise>-</c:otherwise>
					</c:choose>
				</strong>
			</div>


		</div>
	</section>

	<!-- 한줄 브리핑 -->
	<section class="adminDashBrief">
		<div class="adminDashBriefText">
			오늘 미처리 승인 <strong>${approvalPendingCount}건</strong>, 인터페이스 실패 <strong>${interfaceFailCount}건</strong>,
			배치 실패 <strong>${batchFailCount}건</strong>, 데이터 이상 <strong>${dataIssueCount}건</strong>,
			권한 요청 <strong>${authRequestCount}건</strong>입니다.
		</div>
	</section>

	<!-- 운영 핵심 지표 -->
	<section class="adminDashSection">
		<h3 class="adminDashSectionTitle">운영 핵심 지표</h3>

		<div class="adminDashKpiGrid">
			<article class="adminDashKpiCard adminDashKpiClickable"
				onclick="openAdminDashModal('adminApprovalModal')">
				<div class="adminDashKpiLabel">미처리 승인</div>
				<div class="adminDashKpiValueRow">
					<div class="adminDashKpiValue">${approvalPendingCount}</div>
					<div class="adminDashKpiUnit">건</div>
				</div>
				<div class="adminDashKpiMeta">품목, BOM, 설비, 권한 승인 대기</div>
			</article>

			<article class="adminDashKpiCard adminDashKpiClickable"
				onclick="openAdminDashModal('adminInterfaceModal')">
				<div class="adminDashKpiLabel">인터페이스 실패</div>
				<div class="adminDashKpiValueRow">
					<div class="adminDashKpiValue">${interfaceFailCount}</div>
					<div class="adminDashKpiUnit">건</div>
				</div>
				<div class="adminDashKpiMeta">ERP/MES 송수신 실패 건수</div>
			</article>

			<article class="adminDashKpiCard adminDashKpiClickable"
				onclick="openAdminDashModal('adminBatchModal')">
				<div class="adminDashKpiLabel">배치 실패</div>
				<div class="adminDashKpiValueRow">
					<div class="adminDashKpiValue">${batchFailCount}</div>
					<div class="adminDashKpiUnit">건</div>
				</div>
				<div class="adminDashKpiMeta">스케줄러 및 집계 배치 비정상 종료</div>
			</article>

			<article class="adminDashKpiCard adminDashKpiClickable"
				onclick="openAdminDashModal('adminDataQualityModal')">
				<div class="adminDashKpiLabel">데이터 이상</div>
				<div class="adminDashKpiValueRow">
					<div class="adminDashKpiValue">${dataIssueCount}</div>
					<div class="adminDashKpiUnit">건</div>
				</div>
				<div class="adminDashKpiMeta">누락, 미등록, 참조 오류, 이상 데이터</div>
			</article>

			<article class="adminDashKpiCard adminDashKpiClickable"
				onclick="openAdminDashModal('adminAuthModal')">
				<div class="adminDashKpiLabel">권한 요청</div>
				<div class="adminDashKpiValueRow">
					<div class="adminDashKpiValue">${authRequestCount}</div>
					<div class="adminDashKpiUnit">건</div>
				</div>
				<div class="adminDashKpiMeta">계정 생성, 권한 변경, 잠금 해제 요청</div>
			</article>

			<article class="adminDashKpiCard adminDashKpiClickable"
				onclick="openAdminDashModal('adminNoticeModal')">
				<div class="adminDashKpiLabel">미열람 공지</div>
				<div class="adminDashKpiValueRow">
					<div class="adminDashKpiValue">${noticeUnreadCount}</div>
					<div class="adminDashKpiUnit">건</div>
				</div>
				<div class="adminDashKpiMeta">운영 공지 및 점검 안내 미확인</div>
			</article>
		</div>
	</section>

	<!-- 2단 영역 1 -->
	<section class="adminDashGridTwo">
		<article class="adminDashPanel">
			<div class="adminDashPanelHead">
				<div>
					<h3 class="adminDashPanelTitle">기준정보 관리 현황</h3>
					<p class="adminDashPanelSub">품목, 라인, 설비, 재고, 사용자 기준 현황</p>
				</div>
			</div>

			<div class="adminDashStatusGrid">
				<c:choose>
					<c:when test="${not empty masterStatusList}">
						<c:forEach var="master" items="${masterStatusList}">
							<div class="adminDashStatusCard">
								<span class="adminDashStatusLabel">${empty master.label ? '-' : master.label}</span>
								<strong class="adminDashStatusValue"> <fmt:formatNumber
										value="${empty master.count ? 0 : master.count}"
										pattern="#,##0" />
								</strong> <span class="adminDashStatusMeta">${empty master.meta ? '-' : master.meta}</span>
							</div>
						</c:forEach>
					</c:when>
					<c:otherwise>
						<div class="adminDashEmpty">기준정보 현황 데이터가 없습니다.</div>
					</c:otherwise>
				</c:choose>
			</div>
		</article>

		<article class="adminDashPanel">
			<div class="adminDashPanelHead">
				<div>
					<h3 class="adminDashPanelTitle">승인 대기함</h3>
					<p class="adminDashPanelSub">관리자 승인 필요 요청 목록</p>
				</div>
			</div>

			<div class="adminDashTableWrap">
				<table class="adminDashTable">
					<thead>
						<tr>
							<th>요청번호</th>
							<th>유형</th>
							<th>요청자</th>
							<th>요청일시</th>
							<th>상태</th>
						</tr>
					</thead>
					<tbody>
						<c:choose>
							<c:when test="${not empty approvalList}">
								<c:forEach var="approval" items="${approvalList}">
									<tr>
										<td>${empty approval.requestNo ? '-' : approval.requestNo}</td>
										<td>${empty approval.requestType ? '-' : approval.requestType}</td>
										<td>${empty approval.requestUser ? '-' : approval.requestUser}</td>
										<td>${empty approval.requestDateText ? '-' : approval.requestDateText}</td>
										<td>${empty approval.status ? '-' : approval.status}</td>
									</tr>
								</c:forEach>
							</c:when>
							<c:otherwise>
								<tr>
									<td colspan="5" class="adminDashEmptyCell">승인 대기 데이터가
										없습니다.</td>
								</tr>
							</c:otherwise>
						</c:choose>
					</tbody>
				</table>
			</div>
		</article>
	</section>

	<!-- 2단 영역 2 -->
	<section class="adminDashGridTwo">
		<article class="adminDashPanel">
			<div class="adminDashPanelHead">
				<div>
					<h3 class="adminDashPanelTitle">인터페이스 상태</h3>
					<p class="adminDashPanelSub">ERP/MES 연동 및 송수신 상태 모니터링</p>
				</div>
			</div>

			<div class="adminDashTableWrap">
				<table class="adminDashTable">
					<thead>
						<tr>
							<th>인터페이스명</th>
							<th>방향</th>
							<th>대상시스템</th>
							<th>상태</th>
							<th>최근 실행</th>
							<th>실패메시지</th>
						</tr>
					</thead>
					<tbody>
						<c:choose>
							<c:when test="${not empty interfaceStatusList}">
								<c:forEach var="itf" items="${interfaceStatusList}">
									<tr>
										<td>${empty itf.interfaceName ? '-' : itf.interfaceName}</td>
										<td>${empty itf.direction ? '-' : itf.direction}</td>
										<td>${empty itf.targetSystem ? '-' : itf.targetSystem}</td>
										<td>${empty itf.status ? '-' : itf.status}</td>
										<td>${empty itf.lastRunText ? '-' : itf.lastRunText}</td>
										<td class="taLeft">${empty itf.message ? '-' : itf.message}</td>
									</tr>
								</c:forEach>
							</c:when>
							<c:otherwise>
								<tr>
									<td colspan="6" class="adminDashEmptyCell">인터페이스 상태 데이터가
										없습니다.</td>
								</tr>
							</c:otherwise>
						</c:choose>
					</tbody>
				</table>
			</div>
		</article>

		<article class="adminDashPanel">
			<div class="adminDashPanelHead">
				<div>
					<h3 class="adminDashPanelTitle">배치 실행 현황</h3>
					<p class="adminDashPanelSub">일일 집계, 연계, 로그 정리, 재처리 배치 상태</p>
				</div>
			</div>

			<div class="adminDashTableWrap">
				<table class="adminDashTable">
					<thead>
						<tr>
							<th>배치명</th>
							<th>상태</th>
							<th>시작시각</th>
							<th>종료시각</th>
							<th>소요시간</th>
							<th>비고</th>
						</tr>
					</thead>
					<tbody>
						<c:choose>
							<c:when test="${not empty batchStatusList}">
								<c:forEach var="batch" items="${batchStatusList}">
									<tr>
										<td>${empty batch.batchName ? '-' : batch.batchName}</td>
										<td>${empty batch.status ? '-' : batch.status}</td>
										<td>${empty batch.startTimeText ? '-' : batch.startTimeText}</td>
										<td>${empty batch.endTimeText ? '-' : batch.endTimeText}</td>
										<td>${empty batch.durationText ? '-' : batch.durationText}</td>
										<td class="taLeft">${empty batch.message ? '-' : batch.message}</td>
									</tr>
								</c:forEach>
							</c:when>
							<c:otherwise>
								<tr>
									<td colspan="6" class="adminDashEmptyCell">배치 실행 데이터가
										없습니다.</td>
								</tr>
							</c:otherwise>
						</c:choose>
					</tbody>
				</table>
			</div>
		</article>
	</section>

	<!-- 2단 영역 3 -->
	<section class="adminDashGridTwo">
		<article class="adminDashPanel">
			<div class="adminDashPanelHead">
				<div>
					<h3 class="adminDashPanelTitle">데이터 품질 점검</h3>
					<p class="adminDashPanelSub">누락, 미등록, 참조 오류, 이상치 점검</p>
				</div>
			</div>

			<div class="adminDashTableWrap">
				<table class="adminDashTable">
					<thead>
						<tr>
							<th>점검항목</th>
							<th>건수</th>
							<th>심각도</th>
							<th>설명</th>
						</tr>
					</thead>
					<tbody>
						<c:choose>
							<c:when test="${not empty dataQualityList}">
								<c:forEach var="dq" items="${dataQualityList}">
									<tr>
										<td>${empty dq.checkName ? '-' : dq.checkName}</td>
										<td><fmt:formatNumber
												value="${empty dq.issueCount ? 0 : dq.issueCount}"
												pattern="#,##0" /></td>
										<td>${empty dq.severity ? '-' : dq.severity}</td>
										<td class="taLeft">${empty dq.description ? '-' : dq.description}</td>
									</tr>
								</c:forEach>
							</c:when>
							<c:otherwise>
								<tr>
									<td colspan="4" class="adminDashEmptyCell">데이터 품질 점검 결과가
										없습니다.</td>
								</tr>
							</c:otherwise>
						</c:choose>
					</tbody>
				</table>
			</div>
		</article>

		<article class="adminDashPanel">
			<div class="adminDashPanelHead">
				<div>
					<h3 class="adminDashPanelTitle">사용자 / 권한 관리</h3>
					<p class="adminDashPanelSub">활성 계정, 잠금 계정, 권한 요청, 최근 변경 현황</p>
				</div>
			</div>

			<div class="adminDashTableWrap">
				<table class="adminDashTable">
					<thead>
						<tr>
							<th>구분</th>
							<th>수치</th>
							<th>설명</th>
						</tr>
					</thead>
					<tbody>
						<c:choose>
							<c:when test="${not empty userStatusList}">
								<c:forEach var="userStat" items="${userStatusList}">
									<tr>
										<td>${empty userStat.label ? '-' : userStat.label}</td>
										<td><fmt:formatNumber
												value="${empty userStat.count ? 0 : userStat.count}"
												pattern="#,##0" /></td>
										<td class="taLeft">${empty userStat.meta ? '-' : userStat.meta}</td>
									</tr>
								</c:forEach>
							</c:when>
							<c:otherwise>
								<tr>
									<td colspan="3" class="adminDashEmptyCell">사용자 / 권한 현황
										데이터가 없습니다.</td>
								</tr>
							</c:otherwise>
						</c:choose>
					</tbody>
				</table>
			</div>
		</article>
	</section>
	<!-- 최근 변경 이력 -->
	<article class="adminDashPanel">
		<div class="adminDashPanelHead">
			<div>
				<h3 class="adminDashPanelTitle">최근 변경 이력</h3>
				<p class="adminDashPanelSub">품목, 계획, 작업지시, 실적, 검사 기준 최근 변경 로그</p>
			</div>
		</div>

		<div class="adminDashTableWrap">
			<table class="adminDashTable">
				<thead>
					<tr>
						<th>변경일시</th>
						<th>구분</th>
						<th>대상</th>
						<th>변경자</th>
						<th>변경내용</th>
					</tr>
				</thead>
				<tbody>
					<c:choose>
						<c:when test="${not empty auditLogList}">
							<c:forEach var="log" items="${auditLogList}">
								<tr>
									<td>${empty log.changeTimeText ? '-' : log.changeTimeText}</td>
									<td>${empty log.category ? '-' : log.category}</td>
									<td>${empty log.targetName ? '-' : log.targetName}</td>
									<td>${empty log.userName ? '-' : log.userName}</td>
									<td class="taLeft">${empty log.changeSummary ? '-' : log.changeSummary}</td>
								</tr>
							</c:forEach>
						</c:when>
						<c:otherwise>
							<tr>
								<td colspan="5" class="adminDashEmptyCell">최근 변경 이력이 없습니다.</td>
							</tr>
						</c:otherwise>
					</c:choose>
				</tbody>
			</table>
		</div>
	</article>

	<!-- 시스템 공지 -->
	<article class="adminDashPanel">
		<div class="adminDashPanelHead">
			<div>
				<h3 class="adminDashPanelTitle">시스템 공지 / 점검 안내</h3>
				<p class="adminDashPanelSub">운영 공지, 점검 일정, 긴급 안내</p>
			</div>
		</div>

		<div class="adminDashNoticeList">
			<c:choose>
				<c:when test="${not empty noticeList}">
					<c:forEach var="notice" items="${noticeList}">
						<div class="adminDashNoticeItem">
							<div class="adminDashNoticeTop">
								<strong class="adminDashNoticeTitle">${empty notice.title ? '-' : notice.title}</strong>
								<span class="adminDashNoticeBadge">${empty notice.noticeType ? '-' : notice.noticeType}</span>
							</div>
							<div class="adminDashNoticeMeta">
								<span>${empty notice.noticeDateText ? '-' : notice.noticeDateText}</span>
								<span>${empty notice.writerName ? '-' : notice.writerName}</span>
							</div>
							<div class="adminDashNoticeDesc">${empty notice.contentPreview ? '-' : notice.contentPreview}</div>
						</div>
					</c:forEach>
				</c:when>
				<c:otherwise>
					<div class="adminDashEmpty">시스템 공지 데이터가 없습니다.</div>
				</c:otherwise>
			</c:choose>
		</div>
	</article>

	<!-- KPI MODAL : 미처리 승인 -->
	<div id="adminApprovalModal" class="adminDashModal"
		onclick="closeAdminDashModalByBackdrop(event, 'adminApprovalModal')">
		<div class="adminDashModalDialog">
			<div class="adminDashModalHead">
				<h3 class="adminDashModalTitle">미처리 승인 상세</h3>
				<button type="button" class="adminDashModalClose"
					onclick="closeAdminDashModal('adminApprovalModal')">×</button>
			</div>
			<div class="adminDashModalBody">
				<div class="adminDashModalSummary">
					<div class="adminDashModalSummaryItem">
						<span>미처리 승인</span> <strong>${approvalPendingCount}건</strong>
					</div>
				</div>

				<div class="adminDashModalTableWrap">
					<table class="adminDashModalTable">
						<thead>
							<tr>
								<th>요청번호</th>
								<th>유형</th>
								<th>요청자</th>
								<th>요청일시</th>
								<th>상태</th>
							</tr>
						</thead>
						<tbody>
							<c:choose>
								<c:when test="${not empty approvalList}">
									<c:forEach var="approval" items="${approvalList}">
										<tr>
											<td>${empty approval.requestNo ? '-' : approval.requestNo}</td>
											<td>${empty approval.requestType ? '-' : approval.requestType}</td>
											<td>${empty approval.requestUser ? '-' : approval.requestUser}</td>
											<td>${empty approval.requestDateText ? '-' : approval.requestDateText}</td>
											<td>${empty approval.status ? '-' : approval.status}</td>
										</tr>
									</c:forEach>
								</c:when>
								<c:otherwise>
									<tr>
										<td colspan="5" class="adminDashEmptyCell">미처리 승인 데이터가
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

	<!-- KPI MODAL : 인터페이스 실패 -->
	<div id="adminInterfaceModal" class="adminDashModal"
		onclick="closeAdminDashModalByBackdrop(event, 'adminInterfaceModal')">
		<div class="adminDashModalDialog">
			<div class="adminDashModalHead">
				<h3 class="adminDashModalTitle">인터페이스 실패 상세</h3>
				<button type="button" class="adminDashModalClose"
					onclick="closeAdminDashModal('adminInterfaceModal')">×</button>
			</div>
			<div class="adminDashModalBody">
				<div class="adminDashModalSummary">
					<div class="adminDashModalSummaryItem">
						<span>인터페이스 실패</span> <strong>${interfaceFailCount}건</strong>
					</div>
				</div>

				<div class="adminDashModalTableWrap">
					<table class="adminDashModalTable">
						<thead>
							<tr>
								<th>인터페이스명</th>
								<th>방향</th>
								<th>대상시스템</th>
								<th>상태</th>
								<th>최근 실행</th>
								<th>실패메시지</th>
							</tr>
						</thead>
						<tbody>
							<c:choose>
								<c:when test="${not empty interfaceStatusList}">
									<c:forEach var="itf" items="${interfaceStatusList}">
										<tr>
											<td>${empty itf.interfaceName ? '-' : itf.interfaceName}</td>
											<td>${empty itf.direction ? '-' : itf.direction}</td>
											<td>${empty itf.targetSystem ? '-' : itf.targetSystem}</td>
											<td>${empty itf.status ? '-' : itf.status}</td>
											<td>${empty itf.lastRunText ? '-' : itf.lastRunText}</td>
											<td class="taLeft">${empty itf.message ? '-' : itf.message}</td>
										</tr>
									</c:forEach>
								</c:when>
								<c:otherwise>
									<tr>
										<td colspan="6" class="adminDashEmptyCell">인터페이스 상태 데이터가
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

	<!-- KPI MODAL : 배치 실패 -->
	<div id="adminBatchModal" class="adminDashModal"
		onclick="closeAdminDashModalByBackdrop(event, 'adminBatchModal')">
		<div class="adminDashModalDialog">
			<div class="adminDashModalHead">
				<h3 class="adminDashModalTitle">배치 실패 상세</h3>
				<button type="button" class="adminDashModalClose"
					onclick="closeAdminDashModal('adminBatchModal')">×</button>
			</div>
			<div class="adminDashModalBody">
				<div class="adminDashModalSummary">
					<div class="adminDashModalSummaryItem">
						<span>배치 실패</span> <strong>${batchFailCount}건</strong>
					</div>
				</div>

				<div class="adminDashModalTableWrap">
					<table class="adminDashModalTable">
						<thead>
							<tr>
								<th>배치명</th>
								<th>상태</th>
								<th>시작시각</th>
								<th>종료시각</th>
								<th>소요시간</th>
								<th>비고</th>
							</tr>
						</thead>
						<tbody>
							<c:choose>
								<c:when test="${not empty batchStatusList}">
									<c:forEach var="batch" items="${batchStatusList}">
										<tr>
											<td>${empty batch.batchName ? '-' : batch.batchName}</td>
											<td>${empty batch.status ? '-' : batch.status}</td>
											<td>${empty batch.startTimeText ? '-' : batch.startTimeText}</td>
											<td>${empty batch.endTimeText ? '-' : batch.endTimeText}</td>
											<td>${empty batch.durationText ? '-' : batch.durationText}</td>
											<td class="taLeft">${empty batch.message ? '-' : batch.message}</td>
										</tr>
									</c:forEach>
								</c:when>
								<c:otherwise>
									<tr>
										<td colspan="6" class="adminDashEmptyCell">배치 실행 데이터가
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

	<!-- KPI MODAL : 데이터 이상 -->
	<div id="adminDataQualityModal" class="adminDashModal"
		onclick="closeAdminDashModalByBackdrop(event, 'adminDataQualityModal')">
		<div class="adminDashModalDialog">
			<div class="adminDashModalHead">
				<h3 class="adminDashModalTitle">데이터 이상 상세</h3>
				<button type="button" class="adminDashModalClose"
					onclick="closeAdminDashModal('adminDataQualityModal')">×</button>
			</div>
			<div class="adminDashModalBody">
				<div class="adminDashModalSummary">
					<div class="adminDashModalSummaryItem">
						<span>데이터 이상</span> <strong>${dataIssueCount}건</strong>
					</div>
				</div>

				<div class="adminDashModalTableWrap">
					<table class="adminDashModalTable">
						<thead>
							<tr>
								<th>점검항목</th>
								<th>건수</th>
								<th>심각도</th>
								<th>설명</th>
							</tr>
						</thead>
						<tbody>
							<c:choose>
								<c:when test="${not empty dataQualityList}">
									<c:forEach var="dq" items="${dataQualityList}">
										<tr>
											<td>${empty dq.checkName ? '-' : dq.checkName}</td>
											<td><fmt:formatNumber
													value="${empty dq.issueCount ? 0 : dq.issueCount}"
													pattern="#,##0" /></td>
											<td>${empty dq.severity ? '-' : dq.severity}</td>
											<td class="taLeft">${empty dq.description ? '-' : dq.description}</td>
										</tr>
									</c:forEach>
								</c:when>
								<c:otherwise>
									<tr>
										<td colspan="4" class="adminDashEmptyCell">데이터 품질 점검 결과가
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

	<!-- KPI MODAL : 권한 요청 -->
	<div id="adminAuthModal" class="adminDashModal"
		onclick="closeAdminDashModalByBackdrop(event, 'adminAuthModal')">
		<div class="adminDashModalDialog">
			<div class="adminDashModalHead">
				<h3 class="adminDashModalTitle">권한 요청 상세</h3>
				<button type="button" class="adminDashModalClose"
					onclick="closeAdminDashModal('adminAuthModal')">×</button>
			</div>
			<div class="adminDashModalBody">
				<div class="adminDashModalSummary">
					<div class="adminDashModalSummaryItem">
						<span>권한 요청</span> <strong>${authRequestCount}건</strong>
					</div>
				</div>

				<div class="adminDashModalTableWrap">
					<table class="adminDashModalTable">
						<thead>
							<tr>
								<th>구분</th>
								<th>수치</th>
								<th>설명</th>
							</tr>
						</thead>
						<tbody>
							<c:choose>
								<c:when test="${not empty userStatusList}">
									<c:forEach var="userStat" items="${userStatusList}">
										<tr>
											<td>${empty userStat.label ? '-' : userStat.label}</td>
											<td><fmt:formatNumber
													value="${empty userStat.count ? 0 : userStat.count}"
													pattern="#,##0" /></td>
											<td class="taLeft">${empty userStat.meta ? '-' : userStat.meta}</td>
										</tr>
									</c:forEach>
								</c:when>
								<c:otherwise>
									<tr>
										<td colspan="3" class="adminDashEmptyCell">사용자 / 권한 현황
											데이터가 없습니다.</td>
									</tr>
								</c:otherwise>
							</c:choose>
						</tbody>
					</table>
				</div>
			</div>
		</div>
	</div>

	<!-- KPI MODAL : 미열람 공지 -->
	<div id="adminNoticeModal" class="adminDashModal"
		onclick="closeAdminDashModalByBackdrop(event, 'adminNoticeModal')">
		<div class="adminDashModalDialog">
			<div class="adminDashModalHead">
				<h3 class="adminDashModalTitle">미열람 공지 상세</h3>
				<button type="button" class="adminDashModalClose"
					onclick="closeAdminDashModal('adminNoticeModal')">×</button>
			</div>
			<div class="adminDashModalBody">
				<div class="adminDashModalSummary">
					<div class="adminDashModalSummaryItem">
						<span>미열람 공지</span> <strong>${noticeUnreadCount}건</strong>
					</div>
				</div>

				<div class="adminDashNoticeList">
					<c:choose>
						<c:when test="${not empty noticeList}">
							<c:forEach var="notice" items="${noticeList}">
								<div class="adminDashNoticeItem">
									<div class="adminDashNoticeTop">
										<strong class="adminDashNoticeTitle">${empty notice.title ? '-' : notice.title}</strong>
										<span class="adminDashNoticeBadge">${empty notice.noticeType ? '-' : notice.noticeType}</span>
									</div>
									<div class="adminDashNoticeMeta">
										<span>${empty notice.noticeDateText ? '-' : notice.noticeDateText}</span>
										<span>${empty notice.writerName ? '-' : notice.writerName}</span>
									</div>
									<div class="adminDashNoticeDesc">${empty notice.contentPreview ? '-' : notice.contentPreview}</div>
								</div>
							</c:forEach>
						</c:when>
						<c:otherwise>
							<div class="adminDashEmpty">시스템 공지 데이터가 없습니다.</div>
						</c:otherwise>
					</c:choose>
				</div>
			</div>
		</div>
	</div>
	<c:if test="${not empty updatedAt}">
		<div class="adminDashUpdateBox">
			<span class="adminDashDateLabel">갱신일시</span> <strong
				class="adminDashDateValue"> <fmt:formatDate
					value="${updatedAt}" pattern="yyyy-MM-dd HH:mm:ss" />
			</strong>
		</div>
	</c:if>

</div>

<script>
	function openAdminDashModal(modalId) {
		var modal = document.getElementById(modalId);
		if (modal) {
			modal.classList.add("is-open");
			document.body.classList.add("adminDashModalOpen");
		}
	}

	function closeAdminDashModal(modalId) {
		var modal = document.getElementById(modalId);
		if (modal) {
			modal.classList.remove("is-open");
		}

		if (!document.querySelector(".adminDashModal.is-open")) {
			document.body.classList.remove("adminDashModalOpen");
		}
	}

	function closeAdminDashModalByBackdrop(event, modalId) {
		if (event.target.id === modalId) {
			closeAdminDashModal(modalId);
		}
	}

	document.addEventListener("keydown", function(e) {
		if (e.key === "Escape") {
			var opened = document.querySelectorAll(".adminDashModal.is-open");
			opened.forEach(function(modal) {
				modal.classList.remove("is-open");
			});
			document.body.classList.remove("adminDashModalOpen");
		}
	});
</script>