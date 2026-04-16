package report.dto;

public class PlanAchievementDTO {
	private String itemName;
	private double planQty;
	private double producedQty;
	private String unit;
	private double achievementRate;

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public String getItemName() {
		return itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

	public double getPlanQty() {
		return planQty;
	}

	public void setPlanQty(double planQty) {
		this.planQty = planQty;
	}

	public double getProducedQty() {
		return producedQty;
	}

	public void setProducedQty(double producedQty) {
		this.producedQty = producedQty;
	}

	public double getAchievementRate() {
		return achievementRate;
	}

	public void setAchievementRate(double achievementRate) {
		this.achievementRate = achievementRate;
	}
}
