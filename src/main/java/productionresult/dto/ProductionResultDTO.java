package productionresult.dto;

import java.sql.Date;

public class ProductionResultDTO {

	private int resultId;
	private int workOrderId;

	private String itemName;
	private String empName;

	private Date resultDate;
	private String lotNo;
	private double producedQty;
	private double lossQty;
	private String status;
	private String useYn;
	private String remark;
	private Date createdAt;
	private Date updatedAt;

	public ProductionResultDTO() {
	}

	public int getResultId() {
		return resultId;
	}

	public void setResultId(int resultId) {
		this.resultId = resultId;
	}

	public int getWorkOrderId() {
		return workOrderId;
	}

	public void setWorkOrderId(int workOrderId) {
		this.workOrderId = workOrderId;
	}

	public String getItemName() {
		return itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

	public String getEmpName() {
		return empName;
	}

	public void setEmpName(String empName) {
		this.empName = empName;
	}

	public Date getResultDate() {
		return resultDate;
	}

	public void setResultDate(Date resultDate) {
		this.resultDate = resultDate;
	}

	public String getLotNo() {
		return lotNo;
	}

	public void setLotNo(String lotNo) {
		this.lotNo = lotNo;
	}

	public double getProducedQty() {
		return producedQty;
	}

	public void setProducedQty(double producedQty) {
		this.producedQty = producedQty;
	}

	public double getLossQty() {
		return lossQty;
	}

	public void setLossQty(double lossQty) {
		this.lossQty = lossQty;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getUseYn() {
		return useYn;
	}

	public void setUseYn(String useYn) {
		this.useYn = useYn;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Date getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

	public Date getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(Date updatedAt) {
		this.updatedAt = updatedAt;
	}
}