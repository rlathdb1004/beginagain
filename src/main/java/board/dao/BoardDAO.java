package board.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import board.dto.AnswerDTO;
import board.dto.NoticeBoardDTO;
import board.dto.SuggestionBoardDTO;

public class BoardDAO {

    public List<NoticeBoardDTO> selectNoticeList(Connection conn) {
        List<NoticeBoardDTO> list = new ArrayList<NoticeBoardDTO>();
        PreparedStatement ps = null;
        ResultSet rs = null;

        String sql = ""
                + "SELECT N.NOTICE_ID, N.TITLE, N.CONTENT, N.WRITER_EMP_ID, N.STATUS, "
                + "       N.VIEW_COUNT, N.USE_YN, N.REMARK, N.CREATED_AT, N.UPDATED_AT, "
                + "       E.EMP_NAME "
                + "FROM NOTICE_BOARD N "
                + "JOIN EMP E ON N.WRITER_EMP_ID = E.EMP_ID "
                + "WHERE N.USE_YN = 'Y' "
                + "ORDER BY N.NOTICE_ID DESC";

        try {
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();

            while (rs.next()) {
                NoticeBoardDTO dto = new NoticeBoardDTO();
                dto.setNoticeId(rs.getInt("NOTICE_ID"));
                dto.setTitle(rs.getString("TITLE"));
                dto.setContent(rs.getString("CONTENT"));
                dto.setWriterEmpId(rs.getInt("WRITER_EMP_ID"));
                dto.setWriterEmpName(rs.getString("EMP_NAME"));
                dto.setStatus(rs.getString("STATUS"));
                dto.setViewCount(rs.getInt("VIEW_COUNT"));
                dto.setUseYn(rs.getString("USE_YN"));
                dto.setRemark(rs.getString("REMARK"));
                dto.setCreatedAt(rs.getDate("CREATED_AT"));
                dto.setUpdatedAt(rs.getDate("UPDATED_AT"));
                list.add(dto);
            }
        } catch (Exception e) {
            throw new RuntimeException("공지사항 목록 조회 실패", e);
        } finally {
            try { if (rs != null) rs.close(); } catch (Exception e) { e.printStackTrace(); }
            try { if (ps != null) ps.close(); } catch (Exception e) { e.printStackTrace(); }
        }

        return list;
    }

    public NoticeBoardDTO selectNoticeById(Connection conn, int noticeId) {
        NoticeBoardDTO dto = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        String sql = ""
                + "SELECT N.NOTICE_ID, N.TITLE, N.CONTENT, N.WRITER_EMP_ID, N.STATUS, "
                + "       N.VIEW_COUNT, N.USE_YN, N.REMARK, N.CREATED_AT, N.UPDATED_AT, "
                + "       E.EMP_NAME "
                + "FROM NOTICE_BOARD N "
                + "JOIN EMP E ON N.WRITER_EMP_ID = E.EMP_ID "
                + "WHERE N.NOTICE_ID = ? "
                + "  AND N.USE_YN = 'Y'";

        try {
            ps = conn.prepareStatement(sql);
            ps.setInt(1, noticeId);
            rs = ps.executeQuery();

            if (rs.next()) {
                dto = new NoticeBoardDTO();
                dto.setNoticeId(rs.getInt("NOTICE_ID"));
                dto.setTitle(rs.getString("TITLE"));
                dto.setContent(rs.getString("CONTENT"));
                dto.setWriterEmpId(rs.getInt("WRITER_EMP_ID"));
                dto.setWriterEmpName(rs.getString("EMP_NAME"));
                dto.setStatus(rs.getString("STATUS"));
                dto.setViewCount(rs.getInt("VIEW_COUNT"));
                dto.setUseYn(rs.getString("USE_YN"));
                dto.setRemark(rs.getString("REMARK"));
                dto.setCreatedAt(rs.getDate("CREATED_AT"));
                dto.setUpdatedAt(rs.getDate("UPDATED_AT"));
            }
        } catch (Exception e) {
            throw new RuntimeException("공지사항 상세 조회 실패", e);
        } finally {
            try { if (rs != null) rs.close(); } catch (Exception e) { e.printStackTrace(); }
            try { if (ps != null) ps.close(); } catch (Exception e) { e.printStackTrace(); }
        }

        return dto;
    }

    public int updateNotice(Connection conn, NoticeBoardDTO dto) {
        PreparedStatement ps = null;
        int result = 0;

        String sql = ""
                + "UPDATE NOTICE_BOARD "
                + "SET TITLE = ?, "
                + "    CONTENT = ?, "
                + "    STATUS = ?, "
                + "    REMARK = ?, "
                + "    UPDATED_AT = SYSDATE "
                + "WHERE NOTICE_ID = ?";

        try {
            ps = conn.prepareStatement(sql);
            ps.setString(1, dto.getTitle());
            ps.setString(2, dto.getContent());
            ps.setString(3, dto.getStatus());
            ps.setString(4, dto.getRemark());
            ps.setInt(5, dto.getNoticeId());

            result = ps.executeUpdate();
        } catch (Exception e) {
            throw new RuntimeException("공지사항 수정 실패", e);
        } finally {
            try { if (ps != null) ps.close(); } catch (Exception e) { e.printStackTrace(); }
        }

        return result;
    }

    public int deleteNotice(Connection conn, String[] noticeIds) {
        PreparedStatement ps = null;
        int result = 0;

        String sql = ""
                + "UPDATE NOTICE_BOARD "
                + "SET USE_YN = 'N', "
                + "    UPDATED_AT = SYSDATE "
                + "WHERE NOTICE_ID = ?";

        try {
            ps = conn.prepareStatement(sql);

            for (int i = 0; i < noticeIds.length; i++) {
                ps.setInt(1, Integer.parseInt(noticeIds[i]));
                result += ps.executeUpdate();
            }
        } catch (Exception e) {
            throw new RuntimeException("공지사항 삭제 실패", e);
        } finally {
            try { if (ps != null) ps.close(); } catch (Exception e) { e.printStackTrace(); }
        }

        return result;
    }

    public List<SuggestionBoardDTO> selectSuggestionList(Connection conn) {
        List<SuggestionBoardDTO> list = new ArrayList<SuggestionBoardDTO>();
        PreparedStatement ps = null;
        ResultSet rs = null;

        String sql = ""
                + "SELECT S.SUGGESTION_ID, S.TITLE, S.CONTENT, S.WRITER_EMP_ID, S.STATUS, "
                + "       S.VIEW_COUNT, S.USE_YN, S.REMARK, S.CREATED_AT, S.UPDATED_AT, "
                + "       E.EMP_NAME "
                + "FROM SUGGESTION_BOARD S "
                + "JOIN EMP E ON S.WRITER_EMP_ID = E.EMP_ID "
                + "WHERE S.USE_YN = 'Y' "
                + "ORDER BY S.SUGGESTION_ID DESC";

        try {
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();

            while (rs.next()) {
                SuggestionBoardDTO dto = new SuggestionBoardDTO();
                dto.setSuggestionId(rs.getInt("SUGGESTION_ID"));
                dto.setTitle(rs.getString("TITLE"));
                dto.setContent(rs.getString("CONTENT"));
                dto.setWriterEmpId(rs.getInt("WRITER_EMP_ID"));
                dto.setWriterEmpName(rs.getString("EMP_NAME"));
                dto.setStatus(rs.getString("STATUS"));
                dto.setViewCount(rs.getInt("VIEW_COUNT"));
                dto.setUseYn(rs.getString("USE_YN"));
                dto.setRemark(rs.getString("REMARK"));
                dto.setCreatedAt(rs.getDate("CREATED_AT"));
                dto.setUpdatedAt(rs.getDate("UPDATED_AT"));
                list.add(dto);
            }
        } catch (Exception e) {
            throw new RuntimeException("건의사항 목록 조회 실패", e);
        } finally {
            try { if (rs != null) rs.close(); } catch (Exception e) { e.printStackTrace(); }
            try { if (ps != null) ps.close(); } catch (Exception e) { e.printStackTrace(); }
        }

        return list;
    }

    public SuggestionBoardDTO selectSuggestionById(Connection conn, int suggestionId) {
        SuggestionBoardDTO dto = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        String sql = ""
                + "SELECT S.SUGGESTION_ID, S.TITLE, S.CONTENT, S.WRITER_EMP_ID, S.STATUS, "
                + "       S.VIEW_COUNT, S.USE_YN, S.REMARK, S.CREATED_AT, S.UPDATED_AT, "
                + "       E.EMP_NAME "
                + "FROM SUGGESTION_BOARD S "
                + "JOIN EMP E ON S.WRITER_EMP_ID = E.EMP_ID "
                + "WHERE S.SUGGESTION_ID = ? "
                + "  AND S.USE_YN = 'Y'";

        try {
            ps = conn.prepareStatement(sql);
            ps.setInt(1, suggestionId);
            rs = ps.executeQuery();

            if (rs.next()) {
                dto = new SuggestionBoardDTO();
                dto.setSuggestionId(rs.getInt("SUGGESTION_ID"));
                dto.setTitle(rs.getString("TITLE"));
                dto.setContent(rs.getString("CONTENT"));
                dto.setWriterEmpId(rs.getInt("WRITER_EMP_ID"));
                dto.setWriterEmpName(rs.getString("EMP_NAME"));
                dto.setStatus(rs.getString("STATUS"));
                dto.setViewCount(rs.getInt("VIEW_COUNT"));
                dto.setUseYn(rs.getString("USE_YN"));
                dto.setRemark(rs.getString("REMARK"));
                dto.setCreatedAt(rs.getDate("CREATED_AT"));
                dto.setUpdatedAt(rs.getDate("UPDATED_AT"));
            }
        } catch (Exception e) {
            throw new RuntimeException("건의사항 상세 조회 실패", e);
        } finally {
            try { if (rs != null) rs.close(); } catch (Exception e) { e.printStackTrace(); }
            try { if (ps != null) ps.close(); } catch (Exception e) { e.printStackTrace(); }
        }

        return dto;
    }

    public List<AnswerDTO> selectAnswerListBySuggestionId(Connection conn, int suggestionId) {
        List<AnswerDTO> list = new ArrayList<AnswerDTO>();
        PreparedStatement ps = null;
        ResultSet rs = null;

        String sql = ""
                + "SELECT A.ANSWER_ID, A.SUGGESTION_ID, A.CONTENT, A.WRITER_EMP_ID, A.STATUS, "
                + "       A.USE_YN, A.REMARK, A.CREATED_AT, A.UPDATED_AT, "
                + "       E.EMP_NAME "
                + "FROM ANSWER A "
                + "JOIN EMP E ON A.WRITER_EMP_ID = E.EMP_ID "
                + "WHERE A.SUGGESTION_ID = ? "
                + "  AND A.USE_YN = 'Y' "
                + "ORDER BY A.ANSWER_ID ASC";

        try {
            ps = conn.prepareStatement(sql);
            ps.setInt(1, suggestionId);
            rs = ps.executeQuery();

            while (rs.next()) {
                AnswerDTO dto = new AnswerDTO();
                dto.setAnswerId(rs.getInt("ANSWER_ID"));
                dto.setSuggestionId(rs.getInt("SUGGESTION_ID"));
                dto.setContent(rs.getString("CONTENT"));
                dto.setWriterEmpId(rs.getInt("WRITER_EMP_ID"));
                dto.setWriterEmpName(rs.getString("EMP_NAME"));
                dto.setStatus(rs.getString("STATUS"));
                dto.setUseYn(rs.getString("USE_YN"));
                dto.setRemark(rs.getString("REMARK"));
                dto.setCreatedAt(rs.getDate("CREATED_AT"));
                dto.setUpdatedAt(rs.getDate("UPDATED_AT"));
                list.add(dto);
            }
        } catch (Exception e) {
            throw new RuntimeException("답변 목록 조회 실패", e);
        } finally {
            try { if (rs != null) rs.close(); } catch (Exception e) { e.printStackTrace(); }
            try { if (ps != null) ps.close(); } catch (Exception e) { e.printStackTrace(); }
        }

        return list;
    }

    public int updateSuggestion(Connection conn, SuggestionBoardDTO dto) {
        PreparedStatement ps = null;
        int result = 0;

        String sql = ""
                + "UPDATE SUGGESTION_BOARD "
                + "SET TITLE = ?, "
                + "    CONTENT = ?, "
                + "    STATUS = ?, "
                + "    REMARK = ?, "
                + "    UPDATED_AT = SYSDATE "
                + "WHERE SUGGESTION_ID = ?";

        try {
            ps = conn.prepareStatement(sql);
            ps.setString(1, dto.getTitle());
            ps.setString(2, dto.getContent());
            ps.setString(3, dto.getStatus());
            ps.setString(4, dto.getRemark());
            ps.setInt(5, dto.getSuggestionId());

            result = ps.executeUpdate();
        } catch (Exception e) {
            throw new RuntimeException("건의사항 수정 실패", e);
        } finally {
            try { if (ps != null) ps.close(); } catch (Exception e) { e.printStackTrace(); }
        }

        return result;
    }

    public int deleteSuggestion(Connection conn, String[] suggestionIds) {
        PreparedStatement ps = null;
        int result = 0;

        String sql = ""
                + "UPDATE SUGGESTION_BOARD "
                + "SET USE_YN = 'N', "
                + "    UPDATED_AT = SYSDATE "
                + "WHERE SUGGESTION_ID = ?";

        try {
            ps = conn.prepareStatement(sql);

            for (int i = 0; i < suggestionIds.length; i++) {
                ps.setInt(1, Integer.parseInt(suggestionIds[i]));
                result += ps.executeUpdate();
            }
        } catch (Exception e) {
            throw new RuntimeException("건의사항 삭제 실패", e);
        } finally {
            try { if (ps != null) ps.close(); } catch (Exception e) { e.printStackTrace(); }
        }

        return result;
    }

public int insertNotice(Connection conn, NoticeBoardDTO dto) {
    PreparedStatement ps = null;
    int result = 0;
    String sql = "INSERT INTO NOTICE_BOARD (NOTICE_ID, TITLE, CONTENT, WRITER_EMP_ID, STATUS, VIEW_COUNT, USE_YN, REMARK, CREATED_AT, UPDATED_AT) VALUES (SEQ_NOTICE_BOARD.NEXTVAL, ?, ?, ?, ?, 0, 'Y', ?, SYSDATE, SYSDATE)";
    try {
        ps = conn.prepareStatement(sql);
        ps.setString(1, dto.getTitle());
        ps.setString(2, dto.getContent());
        ps.setInt(3, dto.getWriterEmpId());
        ps.setString(4, dto.getStatus());
        ps.setString(5, dto.getRemark());
        result = ps.executeUpdate();
    } catch (Exception e) {
        throw new RuntimeException("공지사항 등록 실패", e);
    } finally {
        try { if (ps != null) ps.close(); } catch (Exception e) { e.printStackTrace(); }
    }
    return result;
}

public int insertSuggestion(Connection conn, SuggestionBoardDTO dto) {
    PreparedStatement ps = null;
    int result = 0;
    String sql = "INSERT INTO SUGGESTION_BOARD (SUGGESTION_ID, TITLE, CONTENT, WRITER_EMP_ID, STATUS, VIEW_COUNT, USE_YN, REMARK, CREATED_AT, UPDATED_AT) VALUES (SEQ_SUGGESTION_BOARD.NEXTVAL, ?, ?, ?, ?, 0, 'Y', ?, SYSDATE, SYSDATE)";
    try {
        ps = conn.prepareStatement(sql);
        ps.setString(1, dto.getTitle());
        ps.setString(2, dto.getContent());
        ps.setInt(3, dto.getWriterEmpId());
        ps.setString(4, dto.getStatus());
        ps.setString(5, dto.getRemark());
        result = ps.executeUpdate();
    } catch (Exception e) {
        throw new RuntimeException("건의사항 등록 실패", e);
    } finally {
        try { if (ps != null) ps.close(); } catch (Exception e) { e.printStackTrace(); }
    }
    return result;
}

}