package report.service;

import java.sql.Connection;
import java.sql.Date;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.List;

import common.jdbc.DBCPUtil;
import report.dao.ReportDAO;
import report.dto.DefectTypeDTO;
import report.dto.EquipmentIssueDTO;
import report.dto.PlanAchievementDTO;
import report.dto.ProductionTrendDTO;
import report.dto.ReportDTO;
import report.dto.ReportSummaryDTO;

public class ReportService {
	private ReportDAO reportDAO = new ReportDAO();

	public ReportDTO getReport(Date startDate, Date endDate) {
		Connection conn = null;
		try {
			conn = DBCPUtil.getConnection();
			ReportDTO report = new ReportDTO();
			ReportSummaryDTO summary = reportDAO.selectSummary(conn, startDate, endDate);

			if (summary.getTotalPlanQty() > 0) {
				summary.setAchievementRate(
						Math.round((summary.getTotalProducedQty() / summary.getTotalPlanQty()) * 1000.0) / 10.0);
			} else {
				summary.setAchievementRate(0);
			}

			List<ProductionTrendDTO> productionTrendList = reportDAO.selectProductionTrend(conn, startDate, endDate);
			List<DefectTypeDTO> defectTypeList = reportDAO.selectDefectTypeRank(conn, startDate, endDate);
			List<PlanAchievementDTO> achievementList = reportDAO.selectPlanAchievement(conn, startDate, endDate);
			List<EquipmentIssueDTO> equipmentIssueList = reportDAO.selectEquipmentIssueRank(conn, startDate, endDate);

			report.setSummary(summary);
			report.setProductionTrendList(productionTrendList);
			report.setDefectTypeList(defectTypeList);
			report.setAchievementList(achievementList);
			report.setEquipmentIssueList(equipmentIssueList);

			LocalDate endLocalDate = endDate.toLocalDate();
			Date currentMonthStart = Date.valueOf(endLocalDate.withDayOfMonth(1));
			Date currentMonthEnd = Date.valueOf(endLocalDate.with(TemporalAdjusters.lastDayOfMonth()));

			LocalDate previousMonthDate = endLocalDate.minusMonths(1);
			Date previousMonthStart = Date.valueOf(previousMonthDate.withDayOfMonth(1));
			Date previousMonthEnd = Date.valueOf(previousMonthDate.with(TemporalAdjusters.lastDayOfMonth()));

			double currentProduced = reportDAO.selectProducedQtySum(conn, currentMonthStart, currentMonthEnd);
			double previousProduced = reportDAO.selectProducedQtySum(conn, previousMonthStart, previousMonthEnd);
			double currentLoss = reportDAO.selectLossQtySum(conn, currentMonthStart, currentMonthEnd);
			double previousLoss = reportDAO.selectLossQtySum(conn, previousMonthStart, previousMonthEnd);
			report.setProductionCaption(
					makeProductionCaption(currentProduced, previousProduced, currentLoss, previousLoss));

			int currentDefectCount = reportDAO.selectDefectCount(conn, currentMonthStart, currentMonthEnd);
			int previousDefectCount = reportDAO.selectDefectCount(conn, previousMonthStart, previousMonthEnd);
			String topDefectName = defectTypeList != null && !defectTypeList.isEmpty()
					? defectTypeList.get(0).getDefectName()
					: null;
			report.setDefectCaption(makeDefectCaption(currentDefectCount, previousDefectCount, topDefectName));

			double currentAchievementRate = reportDAO.selectAchievementRate(conn, currentMonthStart, currentMonthEnd);
			double previousAchievementRate = reportDAO.selectAchievementRate(conn, previousMonthStart,
					previousMonthEnd);
			String lowItemName = findLowestAchievementItem(achievementList);
			report.setAchievementCaption(
					makeAchievementCaption(currentAchievementRate, previousAchievementRate, lowItemName));

			int currentEquipmentIssueCount = reportDAO.selectEquipmentIssueCount(conn, currentMonthStart,
					currentMonthEnd);
			int previousEquipmentIssueCount = reportDAO.selectEquipmentIssueCount(conn, previousMonthStart,
					previousMonthEnd);
			String topEquipmentName = equipmentIssueList != null && !equipmentIssueList.isEmpty()
					? equipmentIssueList.get(0).getEquipmentName()
					: null;
			report.setEquipmentCaption(
					makeEquipmentCaption(currentEquipmentIssueCount, previousEquipmentIssueCount, topEquipmentName));

			return report;
		} finally {
			DBCPUtil.close(conn);
		}
	}

	private String findLowestAchievementItem(List<PlanAchievementDTO> achievementList) {
		if (achievementList == null || achievementList.isEmpty()) {
			return null;
		}

		PlanAchievementDTO target = null;
		for (PlanAchievementDTO row : achievementList) {
			if (row.getPlanQty() <= 0) {
				continue;
			}
			if (target == null || row.getAchievementRate() < target.getAchievementRate()) {
				target = row;
			}
		}
		return target == null ? null : target.getItemName();
	}

	private String makeProductionCaption(double currentProduced, double previousProduced, double currentLoss,
			double previousLoss) {
		String producedText = makeChangeText(currentProduced, previousProduced, "생산량");
		String lossText = makeChangeText(currentLoss, previousLoss, "손실량");
		return producedText + " " + lossText;
	}

	private String makeDefectCaption(int currentDefectCount, int previousDefectCount, String topDefectName) {
		String changeText = makeChangeText(currentDefectCount, previousDefectCount, "불량 건수");
		if (topDefectName != null && !topDefectName.trim().isEmpty()) {
			return changeText + " 선택 기간 기준 가장 많이 발생한 불량 유형은 " + topDefectName + "입니다.";
		}
		return changeText + " 선택 기간 기준 상위 불량 유형 데이터가 없습니다.";
	}

	private String makeAchievementCaption(double currentAchievementRate, double previousAchievementRate,
			String lowItemName) {
		String changeText = makeRateChangeText(currentAchievementRate, previousAchievementRate, "평균 목표 달성률");
		if (lowItemName != null && !lowItemName.trim().isEmpty()) {
			return changeText + " 선택 기간 기준 우선 점검이 필요한 품목은 " + lowItemName + "입니다.";
		}
		return changeText + " 선택 기간 기준 달성률 비교 대상 품목이 없습니다.";
	}

	private String makeEquipmentCaption(int currentEquipmentIssueCount, int previousEquipmentIssueCount,
			String topEquipmentName) {
		String changeText = makeChangeText(currentEquipmentIssueCount, previousEquipmentIssueCount, "설비 이력 건수");
		if (topEquipmentName != null && !topEquipmentName.trim().isEmpty()) {
			return changeText + " 선택 기간 기준 이력이 가장 많은 설비는 " + topEquipmentName + "입니다.";
		}
		return changeText + " 선택 기간 기준 설비 이력 데이터가 없습니다.";
	}

	private String makeChangeText(double currentValue, double previousValue, String label) {
		if (previousValue == 0) {
			if (currentValue == 0) {
				return "전월 대비 " + label + "은 동일한 수준입니다.";
			}
			return "전월 데이터가 없어 " + label + " 증감률 산정은 제외했으며, 이번 달 수치는 " + formatNumber(currentValue) + "입니다.";
		}

		double rate = ((currentValue - previousValue) / previousValue) * 100.0;
		if (Math.abs(rate) < 0.05) {
			return "전월 대비 " + label + "은 유사한 수준입니다.";
		}

		return "전월 대비 " + label + "은 " + formatPercent(Math.abs(rate)) + "% " + (rate > 0 ? "증가" : "감소") + "했습니다.";
	}

	private String makeRateChangeText(double currentValue, double previousValue, String label) {
		if (previousValue == 0) {
			if (currentValue == 0) {
				return "전월 대비 " + label + "은 동일한 수준입니다.";
			}
			return "전월 데이터가 없어 " + label + " 증감률 산정은 제외했으며, 이번 달 수치는 " + formatPercent(currentValue) + "%입니다.";
		}

		double diff = currentValue - previousValue;
		if (Math.abs(diff) < 0.05) {
			return "전월 대비 " + label + "은 유사한 수준입니다.";
		}

		return "전월 대비 " + label + "은 " + formatPercent(Math.abs(diff)) + "%p " + (diff > 0 ? "상승" : "하락") + "했습니다.";
	}

	private String formatNumber(double value) {
		if (Math.abs(value - Math.round(value)) < 0.000001) {
			return String.format("%,d", (long) Math.round(value));
		}
		return String.format("%,.1f", value);
	}

	private String formatPercent(double value) {
		return String.format("%.1f", value);
	}
}