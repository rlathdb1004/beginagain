package ANSWER.DTO;

import java.sql.Timestamp;

/**
 * 답글 DTO
 */
public class AnswerDTO {

    /** 답글 번호 */
    private long answerId;

    /** 연결된 건의사항 번호 */
    private long suggestionId;

    /** 답글 내용 */
    private String content;

    /** 답글 작성자 사번 */
    private long writerEmpId;

    /** 답글 작성자명 */
    private String writerName;

    /** 답글 작성자 부서코드 */
    private String deptCode;

    /** 답글 상태 */
    private String status;

    /** 비고 */
    private String remark;

    /** 작성일 */
    private Timestamp createdAt;

    /** 수정일 */
    private Timestamp updatedAt;

    public long getAnswerId() {
        return answerId;
    }

    public void setAnswerId(long answerId) {
        this.answerId = answerId;
    }

    public long getSuggestionId() {
        return suggestionId;
    }

    public void setSuggestionId(long suggestionId) {
        this.suggestionId = suggestionId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public long getWriterEmpId() {
        return writerEmpId;
    }

    public void setWriterEmpId(long writerEmpId) {
        this.writerEmpId = writerEmpId;
    }

    public String getWriterName() {
        return writerName;
    }

    public void setWriterName(String writerName) {
        this.writerName = writerName;
    }

    public String getDeptCode() {
        return deptCode;
    }

    public void setDeptCode(String deptCode) {
        this.deptCode = deptCode;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public Timestamp getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Timestamp updatedAt) {
        this.updatedAt = updatedAt;
    }
}