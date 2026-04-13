package SUGGESTION.Service;

import java.util.List;

import SUGGESTION.DAO.SuggestionDAO;
import SUGGESTION.DTO.SuggestionDTO;

/**
 * 건의사항 서비스
 */
public class SuggestionService {

    private final SuggestionDAO suggestionDAO = new SuggestionDAO();

    public List<SuggestionDTO> getSuggestionList(String keyword, String status, String deptCode) {
        return suggestionDAO.selectSuggestionList(keyword, status, deptCode);
    }

    public SuggestionDTO getSuggestionDetail(long suggestionId, boolean increaseViewCount) {
        if (increaseViewCount) {
            suggestionDAO.updateViewCount(suggestionId);
        }
        return suggestionDAO.selectSuggestionById(suggestionId);
    }

    public int addSuggestion(SuggestionDTO dto) {
        return suggestionDAO.insertSuggestion(dto);
    }

    public int modifySuggestion(SuggestionDTO dto) {
        return suggestionDAO.updateSuggestion(dto);
    }

    public int removeSuggestion(long suggestionId) {
        return suggestionDAO.deleteSuggestion(suggestionId);
    }
}