package ANSWER.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import ANSWER.DTO.AnswerDTO;
import common.jdbc.DBCPUtil;

/**
 * 답글 DAO
 *
 * [현재 기준]
 * - 건의글 1건에 답글 1건 구조
 * - suggestionId로 답글 조회 가능
 * - answerId로 단건 조회 / 수정 / 삭제 가능
 */
public class AnswerDAO {

    /**
     * 건의사항 번호로 답글 1건 조회
     */
    public AnswerDTO selectAnswerBySuggestionId(long suggestionId) {
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
                + " WHERE a.SUGGESTION_ID = ? ";

        try (
            Connection conn = DBCPUtil.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql)
        ) {
            pstmt.setLong(1, suggestionId);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    dto = new AnswerDTO();

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
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return dto;
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
                    dto = new AnswerDTO();

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
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return dto;
    }

    /**
     * 답글 등록
     *
     * [주의]
     * 시퀀스명이 정해져 있지 않아 MAX + 1 방식 사용
     * 실제 시퀀스가 있으면 교체해도 됨
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
}