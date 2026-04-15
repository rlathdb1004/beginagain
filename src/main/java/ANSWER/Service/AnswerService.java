package ANSWER.Service;

import java.util.List;

import ANSWER.DAO.AnswerDAO;
import ANSWER.DTO.AnswerDTO;

/**
 * 답글 Service
 */
public class AnswerService {

    private final AnswerDAO answerDAO = new AnswerDAO();

    public AnswerDTO getAnswerBySuggestionId(long suggestionId) {
        return answerDAO.selectAnswerBySuggestionId(suggestionId);
    }

    public List<AnswerDTO> getAnswerListBySuggestionId(long suggestionId) {
        return answerDAO.selectAnswerListBySuggestionId(suggestionId);
    }

    public AnswerDTO getAnswerById(long answerId) {
        return answerDAO.selectAnswerById(answerId);
    }

    public int insertAnswer(AnswerDTO dto) {
        return answerDAO.insertAnswer(dto);
    }

    public int updateAnswer(AnswerDTO dto) {
        return answerDAO.updateAnswer(dto);
    }

    public int hideAnswer(long answerId) {
        return answerDAO.hideAnswer(answerId);
    }

    public int deleteAnswer(long answerId) {
        return answerDAO.deleteAnswer(answerId);
    }
}