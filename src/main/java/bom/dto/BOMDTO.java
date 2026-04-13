package bom.dto;

public class BOMDTO {

	private int bomId;
	private int bomDetailId;
	private int parentItemId;
	private int childItemId;

	private String parentItemName;
	private String childItemCode;
	private String childItemName;
	private String unit;

	private double requiredQty;

	public BOMDTO() {
	}

	public int getBomId() {
		return bomId;
	}

	public void setBomId(int bomId) {
		this.bomId = bomId;
	}

	public int getBomDetailId() {
		return bomDetailId;
	}

	public void setBomDetailId(int bomDetailId) {
		this.bomDetailId = bomDetailId;
	}

	public int getParentItemId() {
		return parentItemId;
	}

	public void setParentItemId(int parentItemId) {
		this.parentItemId = parentItemId;
	}

	public int getChildItemId() {
		return childItemId;
	}

	public void setChildItemId(int childItemId) {
		this.childItemId = childItemId;
	}

	public String getParentItemName() {
		return parentItemName;
	}

	public void setParentItemName(String parentItemName) {
		this.parentItemName = parentItemName;
	}

	public String getChildItemCode() {
		return childItemCode;
	}

	public void setChildItemCode(String childItemCode) {
		this.childItemCode = childItemCode;
	}

	public String getChildItemName() {
		return childItemName;
	}

	public void setChildItemName(String childItemName) {
		this.childItemName = childItemName;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public double getRequiredQty() {
		return requiredQty;
	}

	public void setRequiredQty(double requiredQty) {
		this.requiredQty = requiredQty;
	}
}