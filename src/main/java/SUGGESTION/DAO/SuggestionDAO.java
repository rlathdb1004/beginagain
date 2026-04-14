package SUGGESTION.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

import SUGGESTION.DTO.SuggestionDTO;

/**
 * 건의사항 DAO
 */
public class SuggestionDAO {

    /**
     * 건의사항 목록 조회
     */
    public List<SuggestionDTO> selectSuggestionList(String keyword, String status, String deptCode) {

        List<SuggestionDTO> list = new ArrayList<SuggestionDTO>();

        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            // =========================
            // 1. JNDI 커넥션 연결
            // =========================
            Context ctx = new InitialContext();
            DataSource dataFactory = (DataSource) ctx.lookup("java:/comp/env/jdbc/oracle");
            conn = dataFactory.getConnection();

            System.out.println("현재 접속 DB USER : " + conn.getMetaData().getUserName());

            // =========================
            // 2. SQL 작성
            // =========================
            String query = "";
            query += " SELECT ";
            query += "     s.SUGGESTION_ID, ";
            query += "     s.TITLE, ";
            query += "     s.CONTENT, ";
            query += "     s.WRITER_EMP_ID, ";
            query += "     NVL(e.EMP_NAME, '-') AS WRITER_NAME, ";
            query += "     NVL(e.DEPT_CODE, '-') AS DEPT_CODE, ";
            query += "     s.STATUS, ";
            query += "     s.VIEW_COUNT, ";
            query += "     s.REMARK, ";
            query += "     s.CREATED_AT, ";
            query += "     s.UPDATED_AT ";
            query += " FROM SUGGESTION_BOARD s ";
            query += " LEFT JOIN EMP e ";
            query += "    ON s.WRITER_EMP_ID = e.EMP_ID ";
            query += " WHERE 1 = 1 ";

            List<Object> paramList = new ArrayList<Object>();

            if (keyword != null && !"".equals(keyword.trim())) {
                query += " AND (s.TITLE LIKE ? OR s.CONTENT LIKE ? OR e.EMP_NAME LIKE ?) ";
                String keywordLike = "%" + keyword.trim() + "%";
                paramList.add(keywordLike);
                paramList.add(keywordLike);
                paramList.add(keywordLike);
            }

            if (status != null && !"".equals(status.trim())) {
                query += " AND s.STATUS = ? ";
                paramList.add(status.trim());
            }

            if (deptCode != null && !"".equals(deptCode.trim())) {
                query += " AND e.DEPT_CODE = ? ";
                paramList.add(deptCode.trim());
            }

            query += " ORDER BY s.SUGGESTION_ID DESC ";

            System.out.println("건의사항 목록 조회 SQL : " + query);

            // =========================
            // 3. PreparedStatement 생성
            // =========================
            ps = conn.prepareStatement(query);

            for (int i = 0; i < paramList.size(); i++) {
                ps.setObject(i + 1, paramList.get(i));
            }

            // =========================
            // 4. SQL 실행
            // =========================
            rs = ps.executeQuery();

            // =========================
            // 5. 결과 DTO에 담기
            // =========================
            while (rs.next()) {
                SuggestionDTO dto = new SuggestionDTO();

                dto.setSuggestionId(rs.getLong("SUGGESTION_ID"));
                dto.setTitle(rs.getString("TITLE"));
                dto.setContent(rs.getString("CONTENT"));
                dto.setWriterEmpId(rs.getLong("WRITER_EMP_ID"));
                dto.setWriterName(rs.getString("WRITER_NAME"));
                dto.setDeptCode(rs.getString("DEPT_CODE"));
                dto.setStatus(rs.getString("STATUS"));
                dto.setViewCount(rs.getInt("VIEW_COUNT"));
                dto.setRemark(rs.getString("REMARK"));
                dto.setCreatedAt(rs.getTimestamp("CREATED_AT"));
                dto.setUpdatedAt(rs.getTimestamp("UPDATED_AT"));

                list.add(dto);
            }

            System.out.println("건의사항 목록 조회 결과 건수 : " + list.size());

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try { if (rs != null) rs.close(); } catch (Exception e) {}
            try { if (ps != null) ps.close(); } catch (Exception e) {}
            try { if (conn != null) conn.close(); } catch (Exception e) {}
        }

        return list;
    }

    /**
     * 건의사항 상세 조회
     */
    public SuggestionDTO selectSuggestionById(long suggestionId) {

        SuggestionDTO dto = null;

        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            // =========================
            // 1. JNDI 커넥션 연결
            // =========================
            Context ctx = new InitialContext();
            DataSource dataFactory = (DataSource) ctx.lookup("java:/comp/env/jdbc/oracle");
            conn = dataFactory.getConnection();

            System.out.println("현재 접속 DB USER : " + conn.getMetaData().getUserName());

            // =========================
            // 2. SQL 작성
            // =========================
            String query = "";
            query += " SELECT ";
            query += "     s.SUGGESTION_ID, ";
            query += "     s.TITLE, ";
            query += "     s.CONTENT, ";
            query += "     s.WRITER_EMP_ID, ";
            query += "     NVL(e.EMP_NAME, '-') AS WRITER_NAME, ";
            query += "     NVL(e.DEPT_CODE, '-') AS DEPT_CODE, ";
            query += "     s.STATUS, ";
            query += "     s.VIEW_COUNT, ";
            query += "     s.REMARK, ";
            query += "     s.CREATED_AT, ";
            query += "     s.UPDATED_AT ";
            query += " FROM SUGGESTION_BOARD s ";
            query += " LEFT JOIN EMP e ";
            query += "    ON s.WRITER_EMP_ID = e.EMP_ID ";
            query += " WHERE s.SUGGESTION_ID = ? ";

            // =========================
            // 3. PreparedStatement 생성
            // =========================
            ps = conn.prepareStatement(query);
            ps.setLong(1, suggestionId);

            // =========================
            // 4. SQL 실행
            // =========================
            rs = ps.executeQuery();

            // =========================
            // 5. 단건 결과 DTO에 담기
            // =========================
            if (rs.next()) {
                dto = new SuggestionDTO();

                dto.setSuggestionId(rs.getLong("SUGGESTION_ID"));
                dto.setTitle(rs.getString("TITLE"));
                dto.setContent(rs.getString("CONTENT"));
                dto.setWriterEmpId(rs.getLong("WRITER_EMP_ID"));
                dto.setWriterName(rs.getString("WRITER_NAME"));
                dto.setDeptCode(rs.getString("DEPT_CODE"));
                dto.setStatus(rs.getString("STATUS"));
                dto.setViewCount(rs.getInt("VIEW_COUNT"));
                dto.setRemark(rs.getString("REMARK"));
                dto.setCreatedAt(rs.getTimestamp("CREATED_AT"));
                dto.setUpdatedAt(rs.getTimestamp("UPDATED_AT"));
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try { if (rs != null) rs.close(); } catch (Exception e) {}
            try { if (ps != null) ps.close(); } catch (Exception e) {}
            try { if (conn != null) conn.close(); } catch (Exception e) {}
        }

        return dto;
    }

    /**
     * 조회수 증가
     */
    public int updateViewCount(long suggestionId) {

        int result = 0;

        Connection conn = null;
        PreparedStatement ps = null;

        try {
            Context ctx = new InitialContext();
            DataSource dataFactory = (DataSource) ctx.lookup("java:/comp/env/jdbc/oracle");
            conn = dataFactory.getConnection();

            String query = "";
            query += " UPDATE SUGGESTION_BOARD ";
            query += " SET VIEW_COUNT = NVL(VIEW_COUNT, 0) + 1 ";
            query += " WHERE SUGGESTION_ID = ? ";

            ps = conn.prepareStatement(query);
            ps.setLong(1, suggestionId);

            result = ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try { if (ps != null) ps.close(); } catch (Exception e) {}
            try { if (conn != null) conn.close(); } catch (Exception e) {}
        }

        return result;
    }

    /**
     * 건의사항 등록
     * - 시퀀스명은 실제 DB에 맞게 사용
     */
    public int insertSuggestion(SuggestionDTO dto) {

        int result = 0;

        Connection conn = null;
        PreparedStatement ps = null;

        try {
            Context ctx = new InitialContext();
            DataSource dataFactory = (DataSource) ctx.lookup("java:/comp/env/jdbc/oracle");
            conn = dataFactory.getConnection();

            String query = "";
            query += " INSERT INTO SUGGESTION_BOARD ( ";
            query += "     SUGGESTION_ID, TITLE, CONTENT, WRITER_EMP_ID, ";
            query += "     STATUS, VIEW_COUNT, REMARK, CREATED_AT, UPDATED_AT ";
            query += " ) VALUES ( ";
            query += "     SEQ_SUGGESTION_BOARD.NEXTVAL, ?, ?, ?, ?, 0, ?, SYSDATE, SYSDATE ";
            query += " ) ";

            ps = conn.prepareStatement(query);
            ps.setString(1, dto.getTitle());
            ps.setString(2, dto.getContent());
            ps.setLong(3, dto.getWriterEmpId());
            ps.setString(4, dto.getStatus());
            ps.setString(5, dto.getRemark());

            result = ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try { if (ps != null) ps.close(); } catch (Exception e) {}
            try { if (conn != null) conn.close(); } catch (Exception e) {}
        }

        return result;
    }

    /**
     * 건의사항 수정
     */
    public int updateSuggestion(SuggestionDTO dto) {

        int result = 0;

        Connection conn = null;
        PreparedStatement ps = null;

        try {
            Context ctx = new InitialContext();
            DataSource dataFactory = (DataSource) ctx.lookup("java:/comp/env/jdbc/oracle");
            conn = dataFactory.getConnection();

            String query = "";
            query += " UPDATE SUGGESTION_BOARD ";
            query += " SET TITLE = ?, ";
            query += "     CONTENT = ?, ";
            query += "     STATUS = ?, ";
            query += "     REMARK = ?, ";
            query += "     UPDATED_AT = SYSDATE ";
            query += " WHERE SUGGESTION_ID = ? ";

            ps = conn.prepareStatement(query);
            ps.setString(1, dto.getTitle());
            ps.setString(2, dto.getContent());
            ps.setString(3, dto.getStatus());
            ps.setString(4, dto.getRemark());
            ps.setLong(5, dto.getSuggestionId());

            result = ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try { if (ps != null) ps.close(); } catch (Exception e) {}
            try { if (conn != null) conn.close(); } catch (Exception e) {}
        }

        return result;
    }

    /**
     * 건의사항 삭제
     */
    public int deleteSuggestion(long suggestionId) {

        int result = 0;

        Connection conn = null;
        PreparedStatement ps = null;

        try {
            Context ctx = new InitialContext();
            DataSource dataFactory = (DataSource) ctx.lookup("java:/comp/env/jdbc/oracle");
            conn = dataFactory.getConnection();

            String query = " DELETE FROM SUGGESTION_BOARD WHERE SUGGESTION_ID = ? ";

            ps = conn.prepareStatement(query);
            ps.setLong(1, suggestionId);

            result = ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try { if (ps != null) ps.close(); } catch (Exception e) {}
            try { if (conn != null) conn.close(); } catch (Exception e) {}
        }

        return result;
    }
}