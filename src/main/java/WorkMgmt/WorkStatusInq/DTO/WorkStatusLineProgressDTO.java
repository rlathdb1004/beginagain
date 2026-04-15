package WorkMgmt.WorkStatusInq.DTO;

/*
 * 라인별 공정 진행도 그래프용 DTO
 *
 * 용도
 * - WorkStatusInq.jsp 상단 그래프 영역에서 사용
 * - Controller에서 request.setAttribute("lineProgressList", list) 형태로 전달
 *
 * 사용 필드
 * - lineCode     : 라인코드
 * - processName  : 공정명
 * - progressRate : 진행률(%)
 */
public class WorkStatusLineProgressDTO {

    // 라인코드
    private String lineCode;

    // 공정명
    private String processName;

    // 진행률(%)
    private int progressRate;

    public String getLineCode() {
        return lineCode;
    }

    public void setLineCode(String lineCode) {
        this.lineCode = lineCode;
    }

    public String getProcessName() {
        return processName;
    }

    public void setProcessName(String processName) {
        this.processName = processName;
    }

    public int getProgressRate() {
        return progressRate;
    }

    public void setProgressRate(int progressRate) {
        this.progressRate = progressRate;
    }
}