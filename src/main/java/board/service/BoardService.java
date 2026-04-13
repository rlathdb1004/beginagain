package board.service;

import java.sql.Connection;
import java.util.List;

import board.dao.BoardDAO;
import board.dto.AnswerDTO;
import board.dto.NoticeBoardDTO;
import board.dto.SuggestionBoardDTO;
import common.jdbc.DBCPUtil;

public class BoardService {

	private BoardDAO boardDAO = new BoardDAO();

	public List<NoticeBoardDTO> getNoticeList() {
		Connection conn = null;
		try {
			conn = DBCPUtil.getConnection();
			return boardDAO.selectNoticeList(conn);
		} finally {
			DBCPUtil.close(conn);
		}
	}

	public NoticeBoardDTO getNoticeById(int noticeId) {
		Connection conn = null;
		try {
			conn = DBCPUtil.getConnection();
			return boardDAO.selectNoticeById(conn, noticeId);
		} finally {
			DBCPUtil.close(conn);
		}
	}

	public boolean updateNotice(NoticeBoardDTO dto) {
		Connection conn = null;
		try {
			conn = DBCPUtil.getConnection();
			return boardDAO.updateNotice(conn, dto) > 0;
		} finally {
			DBCPUtil.close(conn);
		}
	}

	public boolean deleteNotice(String[] noticeIds) {
		Connection conn = null;
		try {
			conn = DBCPUtil.getConnection();
			return boardDAO.deleteNotice(conn, noticeIds) > 0;
		} finally {
			DBCPUtil.close(conn);
		}
	}

	public List<SuggestionBoardDTO> getSuggestionList() {
		Connection conn = null;
		try {
			conn = DBCPUtil.getConnection();
			return boardDAO.selectSuggestionList(conn);
		} finally {
			DBCPUtil.close(conn);
		}
	}

	public SuggestionBoardDTO getSuggestionById(int suggestionId) {
		Connection conn = null;
		try {
			conn = DBCPUtil.getConnection();
			return boardDAO.selectSuggestionById(conn, suggestionId);
		} finally {
			DBCPUtil.close(conn);
		}
	}

	public List<AnswerDTO> getAnswerListBySuggestionId(int suggestionId) {
		Connection conn = null;
		try {
			conn = DBCPUtil.getConnection();
			return boardDAO.selectAnswerListBySuggestionId(conn, suggestionId);
		} finally {
			DBCPUtil.close(conn);
		}
	}

	public boolean updateSuggestion(SuggestionBoardDTO dto) {
		Connection conn = null;
		try {
			conn = DBCPUtil.getConnection();
			return boardDAO.updateSuggestion(conn, dto) > 0;
		} finally {
			DBCPUtil.close(conn);
		}
	}

	public boolean deleteSuggestion(String[] suggestionIds) {
		Connection conn = null;
		try {
			conn = DBCPUtil.getConnection();
			return boardDAO.deleteSuggestion(conn, suggestionIds) > 0;
		} finally {
			DBCPUtil.close(conn);
		}
	}

public boolean insertNotice(NoticeBoardDTO dto) {
    Connection conn = null;
    try {
        conn = DBCPUtil.getConnection();
        return boardDAO.insertNotice(conn, dto) > 0;
    } finally {
        DBCPUtil.close(conn);
    }
}

public boolean insertSuggestion(SuggestionBoardDTO dto) {
    Connection conn = null;
    try {
        conn = DBCPUtil.getConnection();
        return boardDAO.insertSuggestion(conn, dto) > 0;
    } finally {
        DBCPUtil.close(conn);
    }
}

}