package ANSWER.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import ANSWER.DTO.AnswerDTO;
import common.jdbc.DBCPUtil;

/**
 * 답글 DAO
 * - 기본은 건의글 1건에 답글 여러 건까지 대응
 * - 숨김은 STATUS = '숨김' 으로 처리
 * - 삭제는 물리 삭제
 */
public class AnswerDAO {

    /**
     * 건의사항 번호 기준 대표 답글 1건 조회
     * - 최근 답글 1건
     */
    public AnswerDTO selectAnswerBySuggestionId(long suggestionId) {
        AnswerDTO dto = null;

        String sql = ""
                + " SELECT * FROM ( "
                + "     SELECT "
                + "         a.ANSWER_ID, "
                + "         a.SUGGESTION_ID, "
                + "         a.CONTENT, "
                + "         a.WRITER_EMP_ID, "
                + "         NVL(e.EMP_NAME, '-') AS WRITER_NAME, "
                + "         NVL(e.DEPT_CODE, '-') AS DEPT_CODE, "
                + "         a.STATUS, "
                + "         a.REMARK, "
                + "         a.CREATED_AT, "
                + "         a.UPDATED_AT "
                + "     FROM ANSWER a "
                + "     LEFT JOIN EMP e ON a.WRITER_EMP_ID = e.EMP_ID "
                + "     WHERE a.SUGGESTION_ID = ? "
                + "       AND NVL(a.STATUS, '-') <> '숨김' "
                + "     ORDER BY a.ANSWER_ID DESC "
                + " ) WHERE ROWNUM = 1 ";

        try (
            Connection conn = DBCPUtil.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql)
        ) {
            pstmt.setLong(1, suggestionId);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    dto = mapRow(rs);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return dto;
    }

    /**
     * 건의사항 번호 기준 답글 목록 조회
     */
    public List<AnswerDTO> selectAnswerListBySuggestionId(long suggestionId) {
        List<AnswerDTO> list = new ArrayList<>();

        String sql = ""
                + " SELECT "
                + "     a.ANSWER_ID, "
                + "     a.SUGGESTION_ID, "
                + "     a.CONTENT, "
                + "     a.WRITER_EMP_ID, "
                + "     NVL(e.EMP_NAME, '-') AS WRITER_NAME, "
                + "     NVL(e.DEPT_CODE, '-') AS DEPT_CODE, "
                + "     a.STATUS, "
                + "     a.REMARK, "
                + "     a.CREATED_AT, "
                + "     a.UPDATED_AT "
                + " FROM ANSWER a "
                + " LEFT JOIN EMP e ON a.WRITER_EMP_ID = e.EMP_ID "
                + " WHERE a.SUGGESTION_ID = ? "
                + "   AND NVL(a.STATUS, '-') <> '숨김' "
                + " ORDER BY a.ANSWER_ID ASC ";

        try (
            Connection conn = DBCPUtil.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql)
        ) {
            pstmt.setLong(1, suggestionId);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    list.add(mapRow(rs));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    /**
     * 답글 번호로 단건 조회
     */
    public AnswerDTO selectAnswerById(long answerId) {
        AnswerDTO dto = null;

        String sql = ""
                + " SELECT "
                + "     a.ANSWER_ID, "
                + "     a.SUGGESTION_ID, "
                + "     a.CONTENT, "
                + "     a.WRITER_EMP_ID, "
                + "     NVL(e.EMP_NAME, '-') AS WRITER_NAME, "
                + "     NVL(e.DEPT_CODE, '-') AS DEPT_CODE, "
                + "     a.STATUS, "
                + "     a.REMARK, "
                + "     a.CREATED_AT, "
                + "     a.UPDATED_AT "
                + " FROM ANSWER a "
                + " LEFT JOIN EMP e ON a.WRITER_EMP_ID = e.EMP_ID "
                + " WHERE a.ANSWER_ID = ? ";

        try (
            Connection conn = DBCPUtil.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql)
        ) {
            pstmt.setLong(1, answerId);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    dto = mapRow(rs);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return dto;
    }

    /**
     * 답글 등록
     */
    public int insertAnswer(AnswerDTO dto) {
        int result = 0;

        String sql = ""
                + " INSERT INTO ANSWER ( "
                + "     ANSWER_ID, SUGGESTION_ID, CONTENT, WRITER_EMP_ID, "
                + "     STATUS, REMARK, CREATED_AT, UPDATED_AT "
                + " ) VALUES ( "
                + "     (SELECT NVL(MAX(ANSWER_ID), 0) + 1 FROM ANSWER), "
                + "     ?, ?, ?, ?, ?, SYSDATE, SYSDATE "
                + " ) ";

        try (
            Connection conn = DBCPUtil.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql)
        ) {
            pstmt.setLong(1, dto.getSuggestionId());
            pstmt.setString(2, dto.getContent());
            pstmt.setLong(3, dto.getWriterEmpId());
            pstmt.setString(4, dto.getStatus());
            pstmt.setString(5, dto.getRemark());

            result = pstmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

    /**
     * 답글 수정
     */
    public int updateAnswer(AnswerDTO dto) {
        int result = 0;

        String sql = ""
                + " UPDATE ANSWER "
                + " SET CONTENT = ?, "
                + "     STATUS = ?, "
                + "     REMARK = ?, "
                + "     UPDATED_AT = SYSDATE "
                + " WHERE ANSWER_ID = ? ";

        try (
            Connection conn = DBCPUtil.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql)
        ) {
            pstmt.setString(1, dto.getContent());
            pstmt.setString(2, dto.getStatus());
            pstmt.setString(3, dto.getRemark());
            pstmt.setLong(4, dto.getAnswerId());

            result = pstmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

    /**
     * 답글 숨김
     */
    public int hideAnswer(long answerId) {
        int result = 0;

        String sql = ""
                + " UPDATE ANSWER "
                + " SET STATUS = '숨김', "
                + "     UPDATED_AT = SYSDATE "
                + " WHERE ANSWER_ID = ? ";

        try (
            Connection conn = DBCPUtil.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql)
        ) {
            pstmt.setLong(1, answerId);
            result = pstmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

    /**
     * 답글 삭제
     */
    public int deleteAnswer(long answerId) {
        int result = 0;

        String sql = " DELETE FROM ANSWER WHERE ANSWER_ID = ? ";

        try (
            Connection conn = DBCPUtil.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql)
        ) {
            pstmt.setLong(1, answerId);
            result = pstmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

    private AnswerDTO mapRow(ResultSet rs) throws Exception {
        AnswerDTO dto = new AnswerDTO();
        dto.setAnswerId(rs.getLong("ANSWER_ID"));
        dto.setSuggestionId(rs.getLong("SUGGESTION_ID"));
        dto.setContent(rs.getString("CONTENT"));
        dto.setWriterEmpId(rs.getLong("WRITER_EMP_ID"));
        dto.setWriterName(rs.getString("WRITER_NAME"));
        dto.setDeptCode(rs.getString("DEPT_CODE"));
        dto.setStatus(rs.getString("STATUS"));
        dto.setRemark(rs.getString("REMARK"));
        dto.setCreatedAt(rs.getTimestamp("CREATED_AT"));
        dto.setUpdatedAt(rs.getTimestamp("UPDATED_AT"));
        return dto;
    }
}
