package SUGGESTION.Service;

import java.util.List;

import SUGGESTION.DAO.SuggestionDAO;
import SUGGESTION.DTO.SuggestionDTO;

/**
 * 건의사항 서비스
 */
public class SuggestionService {

    private final SuggestionDAO suggestionDAO = new SuggestionDAO();

    public int getSuggestionCount(String keyword, String status, String deptCode) {
        return suggestionDAO.selectSuggestionCount(keyword, status, deptCode);
    }

    public List<SuggestionDTO> getSuggestionList(int startRow, int endRow,
                                                 String keyword, String status, String deptCode) {
        return suggestionDAO.selectSuggestionList(keyword, status, deptCode, startRow, endRow);
    }

    public SuggestionDTO getSuggestionDetail(long suggestionId) {
        return suggestionDAO.selectSuggestionById(suggestionId);
    }

    public int restoreSuggestion(long suggestionId) {
        return suggestionDAO.restoreSuggestion(suggestionId);
    }
    
    public SuggestionDTO getSuggestionDetail(long suggestionId, boolean increaseViewCount) {
        if (increaseViewCount) {
            suggestionDAO.updateViewCount(suggestionId);
        }
        return suggestionDAO.selectSuggestionById(suggestionId);
    }

    public int insertSuggestion(SuggestionDTO dto) {
        return suggestionDAO.insertSuggestion(dto);
    }

    public int updateSuggestion(SuggestionDTO dto) {
        return suggestionDAO.updateSuggestion(dto);
    }

    public int hideSuggestion(long suggestionId) {
        return suggestionDAO.hideSuggestion(suggestionId);
    }

    public int deleteSuggestion(long suggestionId) {
        return suggestionDAO.deleteSuggestion(suggestionId);
    }
}
