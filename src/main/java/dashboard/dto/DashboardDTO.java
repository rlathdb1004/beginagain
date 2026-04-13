package dashboard.dto;

public class DashboardDTO {

	private int itemCount;
	private int equipmentCount;
	private int processCount;
	private int workOrderCount;
	private int memberCount;

	public DashboardDTO() {
	}

	public int getItemCount() {
		return itemCount;
	}

	public void setItemCount(int itemCount) {
		this.itemCount = itemCount;
	}

	public int getEquipmentCount() {
		return equipmentCount;
	}

	public void setEquipmentCount(int equipmentCount) {
		this.equipmentCount = equipmentCount;
	}

	public int getProcessCount() {
		return processCount;
	}

	public void setProcessCount(int processCount) {
		this.processCount = processCount;
	}

	public int getWorkOrderCount() {
		return workOrderCount;
	}

	public void setWorkOrderCount(int workOrderCount) {
		this.workOrderCount = workOrderCount;
	}

	public int getMemberCount() {
		return memberCount;
	}

	public void setMemberCount(int memberCount) {
		this.memberCount = memberCount;
	}
}