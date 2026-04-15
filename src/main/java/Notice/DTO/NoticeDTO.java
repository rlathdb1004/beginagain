package Notice.DTO;

/*
 * 공지사항 DTO
 * - 실제 NOTICE_BOARD 테이블 컬럼 기준
 */
public class NoticeDTO {

    /* 공지 PK */
    private int noticeId;

    /* 제목 */
    private String title;

    /* 내용 */
    private String content;

    /* 작성자 사번 */
    private int writerId;

    /* 작성자 이름 */
    private String writerName;

    /* 상태 : 게시 / 내림 */
    private String status;

    /* 조회수 */
    private int viewCount;

    /* 사용여부 : Y / N */
    private String useYn;

    /* 비고 */
    private String remark;

    /* 등록일 문자열 */
    private String createdAtStr;

    /* 수정일 문자열 */
    private String updatedAtStr;

    public NoticeDTO() {}

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

    public int getWriterId() {
        return writerId;
    }

    public void setWriterId(int writerId) {
        this.writerId = writerId;
    }

    public String getWriterName() {
        return writerName;
    }

    public void setWriterName(String writerName) {
        this.writerName = writerName;
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

    public String getCreatedAtStr() {
        return createdAtStr;
    }

    public void setCreatedAtStr(String createdAtStr) {
        this.createdAtStr = createdAtStr;
    }

    public String getUpdatedAtStr() {
        return updatedAtStr;
    }

    public void setUpdatedAtStr(String updatedAtStr) {
        this.updatedAtStr = updatedAtStr;
    }

	@Override
	public String toString() {
		return "NoticeDTO [noticeId=" + noticeId + ", title=" + title + ", content=" + content + ", writerId="
				+ writerId + ", writerName=" + writerName + ", status=" + status + ", viewCount=" + viewCount
				+ ", useYn=" + useYn + ", remark=" + remark + ", createdAtStr=" + createdAtStr + ", updatedAtStr="
				+ updatedAtStr + "]";
	}
    
}