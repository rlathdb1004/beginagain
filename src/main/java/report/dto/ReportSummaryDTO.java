package report.dto;

public class ReportSummaryDTO {
    private double totalProducedQty;
    private double totalLossQty;
    private int defectCount;
    private double totalPlanQty;
    private double achievementRate;

    public double getTotalProducedQty() { return totalProducedQty; }
    public void setTotalProducedQty(double totalProducedQty) { this.totalProducedQty = totalProducedQty; }
    public double getTotalLossQty() { return totalLossQty; }
    public void setTotalLossQty(double totalLossQty) { this.totalLossQty = totalLossQty; }
    public int getDefectCount() { return defectCount; }
    public void setDefectCount(int defectCount) { this.defectCount = defectCount; }
    public double getTotalPlanQty() { return totalPlanQty; }
    public void setTotalPlanQty(double totalPlanQty) { this.totalPlanQty = totalPlanQty; }
    public double getAchievementRate() { return achievementRate; }
    public void setAchievementRate(double achievementRate) { this.achievementRate = achievementRate; }
}
