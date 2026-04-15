package Notice.Service;

import java.util.List;

import Notice.DAO.NoticeDAO;
import Notice.DTO.NoticeDTO;

/*
 * 공지사항 Service
 */
public class NoticeService {

    private NoticeDAO noNoticeDAO = new NoticeDAO();

    public int getNoticeCount(String noSearchType, String noKeyword, String noStatusFilter) {
        return noNoticeDAO.getNoticeCount(noSearchType, noKeyword, noStatusFilter);
    }

    public List<NoticeDTO> getNoticeList(int noStartRow, int noEndRow, String noSearchType, String noKeyword, String noStatusFilter) {
        return noNoticeDAO.getNoticeList(noStartRow, noEndRow, noSearchType, noKeyword, noStatusFilter);
    }

    public NoticeDTO getNoticeDetail(int noNoticeId) {
        return noNoticeDAO.getNoticeDetail(noNoticeId);
    }

    public int insertNotice(NoticeDTO noDto) {
        return noNoticeDAO.insertNotice(noDto);
    }

    public int updateNotice(NoticeDTO noDto) {
        return noNoticeDAO.updateNotice(noDto);
    }

    public int increaseViewCount(int noNoticeId) {
        return noNoticeDAO.increaseViewCount(noNoticeId);
    }

    public int hideNotice(int noNoticeId) {
        return noNoticeDAO.hideNotice(noNoticeId);
    }

    public int restoreNotice(int noNoticeId) {
        return noNoticeDAO.restoreNotice(noNoticeId);
    }

    public int deleteNotice(int noNoticeId) {
        return noNoticeDAO.deleteNotice(noNoticeId);
    }

    
}