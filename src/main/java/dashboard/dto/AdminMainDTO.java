package dashboard.dto;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AdminMainDTO {

    private Date baseDate;
    private Timestamp updatedAt;

    private Map<String, Object> summary = new HashMap<String, Object>();

    private List<Map<String, Object>> masterStatusList = new ArrayList<Map<String, Object>>();
    private List<Map<String, Object>> approvalList = new ArrayList<Map<String, Object>>();
    private List<Map<String, Object>> interfaceStatusList = new ArrayList<Map<String, Object>>();
    private List<Map<String, Object>> batchStatusList = new ArrayList<Map<String, Object>>();
    private List<Map<String, Object>> dataQualityList = new ArrayList<Map<String, Object>>();
    private List<Map<String, Object>> userStatusList = new ArrayList<Map<String, Object>>();
    private List<Map<String, Object>> auditLogList = new ArrayList<Map<String, Object>>();
    private List<Map<String, Object>> noticeList = new ArrayList<Map<String, Object>>();

    public Date getBaseDate() {
        return baseDate;
    }

    public void setBaseDate(Date baseDate) {
        this.baseDate = baseDate;
    }

    public Timestamp getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Timestamp updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Map<String, Object> getSummary() {
        return summary;
    }

    public void setSummary(Map<String, Object> summary) {
        this.summary = summary;
    }

    public List<Map<String, Object>> getMasterStatusList() {
        return masterStatusList;
    }

    public void setMasterStatusList(List<Map<String, Object>> masterStatusList) {
        this.masterStatusList = masterStatusList;
    }

    public List<Map<String, Object>> getApprovalList() {
        return approvalList;
    }

    public void setApprovalList(List<Map<String, Object>> approvalList) {
        this.approvalList = approvalList;
    }

    public List<Map<String, Object>> getInterfaceStatusList() {
        return interfaceStatusList;
    }

    public void setInterfaceStatusList(List<Map<String, Object>> interfaceStatusList) {
        this.interfaceStatusList = interfaceStatusList;
    }

    public List<Map<String, Object>> getBatchStatusList() {
        return batchStatusList;
    }

    public void setBatchStatusList(List<Map<String, Object>> batchStatusList) {
        this.batchStatusList = batchStatusList;
    }

    public List<Map<String, Object>> getDataQualityList() {
        return dataQualityList;
    }

    public void setDataQualityList(List<Map<String, Object>> dataQualityList) {
        this.dataQualityList = dataQualityList;
    }

    public List<Map<String, Object>> getUserStatusList() {
        return userStatusList;
    }

    public void setUserStatusList(List<Map<String, Object>> userStatusList) {
        this.userStatusList = userStatusList;
    }

    public List<Map<String, Object>> getAuditLogList() {
        return auditLogList;
    }

    public void setAuditLogList(List<Map<String, Object>> auditLogList) {
        this.auditLogList = auditLogList;
    }

    public List<Map<String, Object>> getNoticeList() {
        return noticeList;
    }

    public void setNoticeList(List<Map<String, Object>> noticeList) {
        this.noticeList = noticeList;
    }

    @Override
    public String toString() {
        return "AdminMainDTO{" +
                "baseDate=" + baseDate +
                ", updatedAt=" + updatedAt +
                ", summary=" + summary +
                ", masterStatusList=" + masterStatusList +
                ", approvalList=" + approvalList +
                ", interfaceStatusList=" + interfaceStatusList +
                ", batchStatusList=" + batchStatusList +
                ", dataQualityList=" + dataQualityList +
                ", userStatusList=" + userStatusList +
                ", auditLogList=" + auditLogList +
                ", noticeList=" + noticeList +
                '}';
    }
}