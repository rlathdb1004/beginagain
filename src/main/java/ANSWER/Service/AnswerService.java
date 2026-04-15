package ANSWER.Service;

import ANSWER.DAO.AnswerDAO;
import ANSWER.DTO.AnswerDTO;

/**
 * 답글 Service
 */
public class AnswerService {

    /** 답글 DAO */
    private final AnswerDAO answerDAO = new AnswerDAO();

    /**
     * 건의사항 번호로 답글 조회
     */
    public AnswerDTO getAnswerBySuggestionId(long suggestionId) {
        return answerDAO.selectAnswerBySuggestionId(suggestionId);
    }

    /**
     * 답글 번호로 단건 조회
     */
    public AnswerDTO getAnswerById(long answerId) {
        return answerDAO.selectAnswerById(answerId);
    }

    /**
     * 답글 등록
     */
    public int addAnswer(AnswerDTO dto) {
        return answerDAO.insertAnswer(dto);
    }

    /**
     * 답글 수정
     */
    public int modifyAnswer(AnswerDTO dto) {
        return answerDAO.updateAnswer(dto);
    }

    /**
     * 답글 삭제
     */
    public int removeAnswer(long answerId) {
        return answerDAO.deleteAnswer(answerId);
    }
}