package board.dto;

import java.sql.Date;

public class AnswerDTO {

	private int answerId;
	private int suggestionId;
	private String content;
	private int writerEmpId;
	private String writerEmpName;
	private String status;
	private String useYn;
	private String remark;
	private Date createdAt;
	private Date updatedAt;

	public AnswerDTO() {
	}

	public int getAnswerId() {
		return answerId;
	}

	public void setAnswerId(int answerId) {
		this.answerId = answerId;
	}

	public int getSuggestionId() {
		return suggestionId;
	}

	public void setSuggestionId(int suggestionId) {
		this.suggestionId = suggestionId;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public int getWriterEmpId() {
		return writerEmpId;
	}

	public void setWriterEmpId(int writerEmpId) {
		this.writerEmpId = writerEmpId;
	}

	public String getWriterEmpName() {
		return writerEmpName;
	}

	public void setWriterEmpName(String writerEmpName) {
		this.writerEmpName = writerEmpName;
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