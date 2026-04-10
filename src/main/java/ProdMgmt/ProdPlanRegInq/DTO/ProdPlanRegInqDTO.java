package ProdMgmt.ProdPlanRegInq.DTO;

import java.sql.Date;

public class ProdPlanRegInqDTO {

	private int seqNO;
	private String planNo;
	private Date planDate;
	private String planCode;
	private String planName;
	private int planAmount;
	private String planUnit;
	private String planLine;
	private String memo;
	
	public int getSeqNO() {
		return seqNO;
	}
	public void setSeqNO(int seqNO) {
		this.seqNO = seqNO;
	}
	public String getPlanNo() {
		return planNo;
	}
	public void setPlanNo(String planNo) {
		this.planNo = planNo;
	}
	public Date getPlanDate() {
		return planDate;
	}
	public void setPlanDate(Date planDate) {
		this.planDate = planDate;
	}
	public String getPlanCode() {
		return planCode;
	}
	public void setPlanCode(String planCode) {
		this.planCode = planCode;
	}
	public String getPlanName() {
		return planName;
	}
	public void setPlanName(String planName) {
		this.planName = planName;
	}
	public int getPlanAmount() {
		return planAmount;
	}
	public void setPlanAmount(int planAmount) {
		this.planAmount = planAmount;
	}
	public String getPlanUnit() {
		return planUnit;
	}
	public void setPlanUnit(String planUnit) {
		this.planUnit = planUnit;
	}
	public String getPlanLine() {
		return planLine;
	}
	public void setPlanLine(String planLine) {
		this.planLine = planLine;
	}
	public String getMemo() {
		return memo;
	}
	public void setMemo(String memo) {
		this.memo = memo;
	}
	
	@Override
	public String toString() {
		return "ProdPlanRegInqDTO [seqNO=" + seqNO + ", planNo=" + planNo + ", planDate=" + planDate + ", planCode="
				+ planCode + ", planName=" + planName + ", planAmount=" + planAmount + ", planUnit=" + planUnit
				+ ", planLine=" + planLine + ", planCompany=" + ", memo=" + memo + "]";
	}
	
	
	
	
	
}
