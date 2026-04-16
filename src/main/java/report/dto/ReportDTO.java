package report.dto;

import java.util.List;

public class ReportDTO {
    private ReportSummaryDTO summary;
    private List<ProductionTrendDTO> productionTrendList;
    private List<DefectTypeDTO> defectTypeList;
    private List<PlanAchievementDTO> achievementList;
    private List<EquipmentIssueDTO> equipmentIssueList;
    private String productionCaption;
    private String defectCaption;
    private String achievementCaption;
    private String equipmentCaption;

    public ReportSummaryDTO getSummary() { return summary; }
    public void setSummary(ReportSummaryDTO summary) { this.summary = summary; }
    public List<ProductionTrendDTO> getProductionTrendList() { return productionTrendList; }
    public void setProductionTrendList(List<ProductionTrendDTO> productionTrendList) { this.productionTrendList = productionTrendList; }
    public List<DefectTypeDTO> getDefectTypeList() { return defectTypeList; }
    public void setDefectTypeList(List<DefectTypeDTO> defectTypeList) { this.defectTypeList = defectTypeList; }
    public List<PlanAchievementDTO> getAchievementList() { return achievementList; }
    public void setAchievementList(List<PlanAchievementDTO> achievementList) { this.achievementList = achievementList; }
    public List<EquipmentIssueDTO> getEquipmentIssueList() { return equipmentIssueList; }
    public void setEquipmentIssueList(List<EquipmentIssueDTO> equipmentIssueList) { this.equipmentIssueList = equipmentIssueList; }
    public String getProductionCaption() { return productionCaption; }
    public void setProductionCaption(String productionCaption) { this.productionCaption = productionCaption; }
    public String getDefectCaption() { return defectCaption; }
    public void setDefectCaption(String defectCaption) { this.defectCaption = defectCaption; }
    public String getAchievementCaption() { return achievementCaption; }
    public void setAchievementCaption(String achievementCaption) { this.achievementCaption = achievementCaption; }
    public String getEquipmentCaption() { return equipmentCaption; }
    public void setEquipmentCaption(String equipmentCaption) { this.equipmentCaption = equipmentCaption; }
}
