package routing.dto;

public class RoutingDTO {

	private int routingId;
	private int itemId;
	private int processId;
	private int equipmentId;

	private int processSeq;

	private String processCode;
	private String processName;
	private String equipmentName;

	public RoutingDTO() {
	}

	public int getRoutingId() {
		return routingId;
	}

	public void setRoutingId(int routingId) {
		this.routingId = routingId;
	}

	public int getItemId() {
		return itemId;
	}

	public void setItemId(int itemId) {
		this.itemId = itemId;
	}

	public int getProcessId() {
		return processId;
	}

	public void setProcessId(int processId) {
		this.processId = processId;
	}

	public int getEquipmentId() {
		return equipmentId;
	}

	public void setEquipmentId(int equipmentId) {
		this.equipmentId = equipmentId;
	}

	public int getProcessSeq() {
		return processSeq;
	}

	public void setProcessSeq(int processSeq) {
		this.processSeq = processSeq;
	}

	public String getProcessCode() {
		return processCode;
	}

	public void setProcessCode(String processCode) {
		this.processCode = processCode;
	}

	public String getProcessName() {
		return processName;
	}

	public void setProcessName(String processName) {
		this.processName = processName;
	}

	public String getEquipmentName() {
		return equipmentName;
	}

	public void setEquipmentName(String equipmentName) {
		this.equipmentName = equipmentName;
	}
}