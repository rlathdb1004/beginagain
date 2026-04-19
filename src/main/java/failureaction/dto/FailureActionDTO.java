package failureaction.dto;

import java.sql.Date;

public class FailureActionDTO {

    private int failureActionId;
    private int maintenanceId;
    private Date failureDate;
    private String failurePart;
    private String failureContent;
    private String causeText;
    private String actionText;
    private Date actionDate;
    private String status;
    private String equipmentCode;
    private String equipmentName;
    private String maintenanceType;
    private Date maintenanceDate;

    public int getFailureActionId() {
        return failureActionId;
    }

    public void setFailureActionId(int failureActionId) {
        this.failureActionId = failureActionId;
    }

    public int getMaintenanceId() {
        return maintenanceId;
    }

    public void setMaintenanceId(int maintenanceId) {
        this.maintenanceId = maintenanceId;
    }

    public Date getFailureDate() {
        return failureDate;
    }

    public void setFailureDate(Date failureDate) {
        this.failureDate = failureDate;
    }

    public String getFailurePart() {
        return failurePart;
    }

    public void setFailurePart(String failurePart) {
        this.failurePart = failurePart;
    }

    public String getFailureContent() {
        return failureContent;
    }

    public void setFailureContent(String failureContent) {
        this.failureContent = failureContent;
    }

    public String getCauseText() {
        return causeText;
    }

    public void setCauseText(String causeText) {
        this.causeText = causeText;
    }

    public String getActionText() {
        return actionText;
    }

    public void setActionText(String actionText) {
        this.actionText = actionText;
    }

    public Date getActionDate() {
        return actionDate;
    }

    public void setActionDate(Date actionDate) {
        this.actionDate = actionDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getEquipmentCode() { return equipmentCode; }
    public void setEquipmentCode(String equipmentCode) { this.equipmentCode = equipmentCode; }
    public String getEquipmentName() { return equipmentName; }
    public void setEquipmentName(String equipmentName) { this.equipmentName = equipmentName; }
    public String getMaintenanceType() { return maintenanceType; }
    public void setMaintenanceType(String maintenanceType) { this.maintenanceType = maintenanceType; }
    public Date getMaintenanceDate() { return maintenanceDate; }
    public void setMaintenanceDate(Date maintenanceDate) { this.maintenanceDate = maintenanceDate; }
}