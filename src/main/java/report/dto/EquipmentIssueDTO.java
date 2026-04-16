package report.dto;

public class EquipmentIssueDTO {
    private String equipmentName;
    private int maintenanceCount;
    private int failureCount;
    private int totalIssueCount;

    public String getEquipmentName() { return equipmentName; }
    public void setEquipmentName(String equipmentName) { this.equipmentName = equipmentName; }
    public int getMaintenanceCount() { return maintenanceCount; }
    public void setMaintenanceCount(int maintenanceCount) { this.maintenanceCount = maintenanceCount; }
    public int getFailureCount() { return failureCount; }
    public void setFailureCount(int failureCount) { this.failureCount = failureCount; }
    public int getTotalIssueCount() { return totalIssueCount; }
    public void setTotalIssueCount(int totalIssueCount) { this.totalIssueCount = totalIssueCount; }
}
