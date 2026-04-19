package dashboard.service;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import dashboard.dao.AdminMainDAO;
import dashboard.dto.AdminMainDTO;

public class AdminMainService {

	private final AdminMainDAO dao = new AdminMainDAO();

	public AdminMainDTO getDashboard(Date requestBaseDate) throws Exception {
		AdminMainDTO dashboard = new AdminMainDTO();

		// 기준일은 항상 오늘
		Date baseDate = new Date(System.currentTimeMillis());
		dashboard.setBaseDate(baseDate);
		dashboard.setUpdatedAt(new Timestamp(System.currentTimeMillis()));

		// 기본 조회
		Map<String, Object> summary = nullSafeMap(dao.selectSummary(baseDate));

		List<Map<String, Object>> masterStatusList = nullSafeList(dao.selectMasterStatusList(baseDate));
		List<Map<String, Object>> approvalList = nullSafeList(dao.selectApprovalList(baseDate));
		List<Map<String, Object>> interfaceStatusList = nullSafeList(dao.selectInterfaceStatusList(baseDate));
		List<Map<String, Object>> batchStatusList = nullSafeList(dao.selectBatchStatusList(baseDate));
		List<Map<String, Object>> dataQualityList = nullSafeList(dao.selectDataQualityList(baseDate));
		List<Map<String, Object>> userStatusList = nullSafeList(dao.selectUserStatusList(baseDate));
		List<Map<String, Object>> auditLogList = nullSafeList(dao.selectAuditLogList(baseDate));
		List<Map<String, Object>> noticeList = nullSafeList(dao.selectNoticeList(baseDate));

		// 후처리
		enrichSummary(summary);
		enrichMasterStatusList(masterStatusList);
		enrichApprovalList(approvalList);
		enrichInterfaceStatusList(interfaceStatusList);
		enrichBatchStatusList(batchStatusList);
		enrichDataQualityList(dataQualityList);
		enrichUserStatusList(userStatusList);
		enrichAuditLogList(auditLogList);
		enrichNoticeList(noticeList);

		// DTO 세팅
		dashboard.setSummary(summary);
		dashboard.setMasterStatusList(masterStatusList);
		dashboard.setApprovalList(approvalList);
		dashboard.setInterfaceStatusList(interfaceStatusList);
		dashboard.setBatchStatusList(batchStatusList);
		dashboard.setDataQualityList(dataQualityList);
		dashboard.setUserStatusList(userStatusList);
		dashboard.setAuditLogList(auditLogList);
		dashboard.setNoticeList(noticeList);

		return dashboard;
	}

	private void enrichSummary(Map<String, Object> summary) {
		summary.put("approvalPendingCount", toInt(summary.get("approvalPendingCount")));
		summary.put("interfaceFailCount", toInt(summary.get("interfaceFailCount")));
		summary.put("batchFailCount", toInt(summary.get("batchFailCount")));
		summary.put("dataIssueCount", toInt(summary.get("dataIssueCount")));
		summary.put("authRequestCount", toInt(summary.get("authRequestCount")));
		summary.put("noticeUnreadCount", toInt(summary.get("noticeUnreadCount")));
	}

	private void enrichMasterStatusList(List<Map<String, Object>> list) {
		for (Map<String, Object> row : list) {
			if (isEmpty(row.get("label"))) {
				row.put("label", "-");
			}
			row.put("count", toInt(row.get("count")));
			if (isEmpty(row.get("meta"))) {
				row.put("meta", "-");
			}
		}
	}

	private void enrichApprovalList(List<Map<String, Object>> list) {
		for (Map<String, Object> row : list) {
			if (isEmpty(row.get("requestNo"))) {
				row.put("requestNo", "-");
			}
			if (isEmpty(row.get("requestType"))) {
				row.put("requestType", "-");
			}
			if (isEmpty(row.get("requestUser"))) {
				row.put("requestUser", "-");
			}
			if (isEmpty(row.get("requestDateText"))) {
				row.put("requestDateText", "-");
			}
			if (isEmpty(row.get("status"))) {
				row.put("status", "-");
			}
		}
	}

	private void enrichInterfaceStatusList(List<Map<String, Object>> list) {
		for (Map<String, Object> row : list) {
			if (isEmpty(row.get("interfaceName"))) {
				row.put("interfaceName", "-");
			}
			if (isEmpty(row.get("direction"))) {
				row.put("direction", "-");
			}
			if (isEmpty(row.get("targetSystem"))) {
				row.put("targetSystem", "-");
			}
			if (isEmpty(row.get("status"))) {
				row.put("status", "-");
			}
			if (isEmpty(row.get("lastRunText"))) {
				row.put("lastRunText", "-");
			}
			if (isEmpty(row.get("message"))) {
				row.put("message", "-");
			}
		}
	}

	private void enrichBatchStatusList(List<Map<String, Object>> list) {
		for (Map<String, Object> row : list) {
			if (isEmpty(row.get("batchName"))) {
				row.put("batchName", "-");
			}
			if (isEmpty(row.get("status"))) {
				row.put("status", "-");
			}
			if (isEmpty(row.get("startTimeText"))) {
				row.put("startTimeText", "-");
			}
			if (isEmpty(row.get("endTimeText"))) {
				row.put("endTimeText", "-");
			}
			if (isEmpty(row.get("durationText"))) {
				row.put("durationText", "-");
			}
			if (isEmpty(row.get("message"))) {
				row.put("message", "-");
			}
		}
	}

	private void enrichDataQualityList(List<Map<String, Object>> list) {
		for (Map<String, Object> row : list) {
			if (isEmpty(row.get("checkName"))) {
				row.put("checkName", "-");
			}
			row.put("issueCount", toInt(row.get("issueCount")));
			if (isEmpty(row.get("severity"))) {
				row.put("severity", "-");
			}
			if (isEmpty(row.get("description"))) {
				row.put("description", "-");
			}
		}
	}

	private void enrichUserStatusList(List<Map<String, Object>> list) {
		for (Map<String, Object> row : list) {
			if (isEmpty(row.get("label"))) {
				row.put("label", "-");
			}
			row.put("count", toInt(row.get("count")));
			if (isEmpty(row.get("meta"))) {
				row.put("meta", "-");
			}
		}
	}

	private void enrichAuditLogList(List<Map<String, Object>> list) {
		for (Map<String, Object> row : list) {
			if (isEmpty(row.get("changeTimeText"))) {
				row.put("changeTimeText", "-");
			}
			if (isEmpty(row.get("category"))) {
				row.put("category", "-");
			}
			if (isEmpty(row.get("targetName"))) {
				row.put("targetName", "-");
			}
			if (isEmpty(row.get("userName"))) {
				row.put("userName", "-");
			}
			if (isEmpty(row.get("changeSummary"))) {
				row.put("changeSummary", "-");
			}
		}
	}

	private void enrichNoticeList(List<Map<String, Object>> list) {
		for (Map<String, Object> row : list) {
			if (isEmpty(row.get("title"))) {
				row.put("title", "-");
			}
			if (isEmpty(row.get("noticeType"))) {
				row.put("noticeType", "-");
			}
			if (isEmpty(row.get("noticeDateText"))) {
				row.put("noticeDateText", "-");
			}
			if (isEmpty(row.get("writerName"))) {
				row.put("writerName", "-");
			}
			if (isEmpty(row.get("contentPreview"))) {
				row.put("contentPreview", "-");
			}
		}
	}

	private Map<String, Object> nullSafeMap(Map<String, Object> map) {
		return map == null ? new HashMap<String, Object>() : map;
	}

	private List<Map<String, Object>> nullSafeList(List<Map<String, Object>> list) {
		return list == null ? new ArrayList<Map<String, Object>>() : list;
	}

	private boolean isEmpty(Object obj) {
		return obj == null || String.valueOf(obj).trim().isEmpty();
	}

	private int toInt(Object obj) {
		if (obj == null) {
			return 0;
		}

		if (obj instanceof Number) {
			return ((Number) obj).intValue();
		}

		try {
			return Integer.parseInt(String.valueOf(obj));
		} catch (Exception e) {
			return 0;
		}
	}
}