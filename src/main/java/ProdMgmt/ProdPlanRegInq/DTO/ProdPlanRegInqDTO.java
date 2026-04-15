package ProdMgmt.ProdPlanRegInq.DTO;

import java.sql.Date;

/*
 * 생산계획 등록/조회 화면에서 사용할 DTO
 *
 * 역할
 * - DAO가 조회한 데이터를 담는다.
 * - Controller -> JSP 로 데이터를 넘길 때 사용한다.
 *
 * 주의
 * - seqNO 는 화면의 NO 컬럼이며 실제로는 PLAN_ID 값을 넣는다.
 * - planNo 는 화면 표시용으로 가공한 문자열이다.
 */
public class ProdPlanRegInqDTO {

    // PLAN_ID를 화면 NO 및 삭제 키로 사용
    private int seqNO;

    // 화면 표시용 생산계획번호
    private String planNo;

    // 생산계획일자
    private Date planDate;

    // 품목 정보
    private String planCode;
    private String planName;
    private String planUnit;

    // 생산계획 정보
    private int planAmount;
    private String planLine;
    private String status;
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

    public String getPlanUnit() {
        return planUnit;
    }

    public void setPlanUnit(String planUnit) {
        this.planUnit = planUnit;
    }

    public int getPlanAmount() {
        return planAmount;
    }

    public void setPlanAmount(int planAmount) {
        this.planAmount = planAmount;
    }

    public String getPlanLine() {
        return planLine;
    }

    public void setPlanLine(String planLine) {
        this.planLine = planLine;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }
}