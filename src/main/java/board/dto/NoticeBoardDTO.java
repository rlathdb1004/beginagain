package board.dto;

import java.sql.Date;

public class NoticeBoardDTO {

	private int noticeId;
	private String title;
	private String content;
	private int writerEmpId;
	private String writerEmpName;
	private String status;
	private int viewCount;
	private String useYn;
	private String remark;
	private Date createdAt;
	private Date updatedAt;

	public NoticeBoardDTO() {
	}

	public int getNoticeId() {
		return noticeId;
	}

	public void setNoticeId(int noticeId) {
		this.noticeId = noticeId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
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

	public int getViewCount() {
		return viewCount;
	}

	public void setViewCount(int viewCount) {
		this.viewCount = viewCount;
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