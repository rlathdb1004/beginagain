package SUGGESTION.Service;

import java.util.List;

import SUGGESTION.DAO.SuggestionDAO;
import SUGGESTION.DTO.SuggestionDTO;

/**
 * 건의사항 서비스
 *
 * [역할]
 * - 서블릿과 DAO 사이 중간 처리
 * - 조회수 증가 판단
 * - 목록 / 상세 / 등록 / 수정 / 삭제 연결
 */
public class SuggestionService {

    /** 건의사항 DAO */
    private final SuggestionDAO suggestionDAO = new SuggestionDAO();

    /**
     * 전체 건수 조회
     */
    public int getSuggestionCount(String keyword, String status, String deptCode) {
        return suggestionDAO.selectSuggestionCount(keyword, status, deptCode);
    }

    /**
     * 페이징 목록 조회
     */
    public List<SuggestionDTO> getSuggestionList(String keyword, String status, String deptCode,
                                                 int startRow, int endRow) {
        return suggestionDAO.selectSuggestionList(keyword, status, deptCode, startRow, endRow);
    }

    /**
     * 상세 조회
     * - increaseViewCount가 true이면 조회수 증가 후 조회
     */
    public SuggestionDTO getSuggestionDetail(long suggestionId, boolean increaseViewCount) {
        if (increaseViewCount) {
            suggestionDAO.updateViewCount(suggestionId);
        }
        return suggestionDAO.selectSuggestionById(suggestionId);
    }

    /**
     * 등록
     */
    public int addSuggestion(SuggestionDTO dto) {
        return suggestionDAO.insertSuggestion(dto);
    }

    /**
     * 수정
     */
    public int modifySuggestion(SuggestionDTO dto) {
        return suggestionDAO.updateSuggestion(dto);
    }

    /**
     * 삭제
     */
    public int removeSuggestion(long suggestionId) {
        return suggestionDAO.deleteSuggestion(suggestionId);
    }
}