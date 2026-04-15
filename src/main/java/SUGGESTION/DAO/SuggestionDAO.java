package SUGGESTION.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import SUGGESTION.DTO.SuggestionDTO;
import common.jdbc.DBCPUtil;

/**
 * 건의사항 DAO
 */
public class SuggestionDAO {

    public int selectSuggestionCount(String keyword, String status, String deptCode) {
        int count = 0;

        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT COUNT(*) ");
        sql.append(" FROM SUGGESTION_BOARD s ");
        sql.append(" LEFT JOIN EMP e ON s.WRITER_EMP_ID = e.EMP_ID ");
        sql.append(" WHERE NVL(s.STATUS, '-') <> '내림' ");

        List<Object> params = new ArrayList<>();

        if (keyword != null && !keyword.trim().isEmpty()) {
            sql.append(" AND (s.TITLE LIKE ? OR s.CONTENT LIKE ? OR e.EMP_NAME LIKE ?) ");
            String searchKeyword = "%" + keyword.trim() + "%";
            params.add(searchKeyword);
            params.add(searchKeyword);
            params.add(searchKeyword);
        }

        if (status != null && !status.trim().isEmpty()) {
            sql.append(" AND s.STATUS = ? ");
            params.add(status.trim());
        }

        if (deptCode != null && !deptCode.trim().isEmpty()) {
            sql.append(" AND e.DEPT_CODE = ? ");
            params.add(deptCode.trim());
        }

        try (
            Connection conn = DBCPUtil.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql.toString())
        ) {
            for (int i = 0; i < params.size(); i++) {
                pstmt.setObject(i + 1, params.get(i));
            }

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    count = rs.getInt(1);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return count;
    }

    public List<SuggestionDTO> selectSuggestionList(String keyword, String status, String deptCode,
                                                    int startRow, int endRow) {
        List<SuggestionDTO> list = new ArrayList<>();

        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT * ");
        sql.append(" FROM ( ");
        sql.append("     SELECT ROW_NUMBER() OVER (ORDER BY s.SUGGESTION_ID DESC) AS RN, ");
        sql.append("            s.SUGGESTION_ID, ");
        sql.append("            s.TITLE, ");
        sql.append("            s.CONTENT, ");
        sql.append("            s.WRITER_EMP_ID, ");
        sql.append("            NVL(e.EMP_NAME, '-') AS WRITER_NAME, ");
        sql.append("            NVL(e.DEPT_CODE, '-') AS DEPT_CODE, ");
        sql.append("            s.STATUS, ");
        sql.append("            s.VIEW_COUNT, ");
        sql.append("            s.REMARK, ");
        sql.append("            s.CREATED_AT, ");
        sql.append("            s.UPDATED_AT ");
        sql.append("     FROM SUGGESTION_BOARD s ");
        sql.append("     LEFT JOIN EMP e ON s.WRITER_EMP_ID = e.EMP_ID ");
        sql.append("     WHERE NVL(s.STATUS, '-') <> '내림' ");

        List<Object> params = new ArrayList<>();

        if (keyword != null && !keyword.trim().isEmpty()) {
            sql.append(" AND (s.TITLE LIKE ? OR s.CONTENT LIKE ? OR e.EMP_NAME LIKE ?) ");
            String searchKeyword = "%" + keyword.trim() + "%";
            params.add(searchKeyword);
            params.add(searchKeyword);
            params.add(searchKeyword);
        }

        if (status != null && !status.trim().isEmpty()) {
            sql.append(" AND s.STATUS = ? ");
            params.add(status.trim());
        }

        if (deptCode != null && !deptCode.trim().isEmpty()) {
            sql.append(" AND e.DEPT_CODE = ? ");
            params.add(deptCode.trim());
        }

        sql.append(" ) ");
        sql.append(" WHERE RN BETWEEN ? AND ? ");
        sql.append(" ORDER BY RN ");

        try (
            Connection conn = DBCPUtil.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql.toString())
        ) {
            int paramIndex = 1;
            for (Object param : params) {
                pstmt.setObject(paramIndex++, param);
            }
            pstmt.setInt(paramIndex++, startRow);
            pstmt.setInt(paramIndex, endRow);

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

    public SuggestionDTO selectSuggestionById(long suggestionId) {
        SuggestionDTO dto = null;

        String sql = ""
                + " SELECT "
                + "     s.SUGGESTION_ID, "
                + "     s.TITLE, "
                + "     s.CONTENT, "
                + "     s.WRITER_EMP_ID, "
                + "     NVL(e.EMP_NAME, '-') AS WRITER_NAME, "
                + "     NVL(e.DEPT_CODE, '-') AS DEPT_CODE, "
                + "     s.STATUS, "
                + "     s.VIEW_COUNT, "
                + "     s.REMARK, "
                + "     s.CREATED_AT, "
                + "     s.UPDATED_AT "
                + " FROM SUGGESTION_BOARD s "
                + " LEFT JOIN EMP e ON s.WRITER_EMP_ID = e.EMP_ID "
                + " WHERE s.SUGGESTION_ID = ? ";

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

    public int updateViewCount(long suggestionId) {
        int result = 0;
        String sql = ""
                + " UPDATE SUGGESTION_BOARD "
                + " SET VIEW_COUNT = NVL(VIEW_COUNT, 0) + 1 "
                + " WHERE SUGGESTION_ID = ? ";

        try (
            Connection conn = DBCPUtil.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql)
        ) {
            pstmt.setLong(1, suggestionId);
            result = pstmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

    public int insertSuggestion(SuggestionDTO dto) {
        int result = 0;

        String sql = ""
                + " INSERT INTO SUGGESTION_BOARD ( "
                + "     SUGGESTION_ID, TITLE, CONTENT, WRITER_EMP_ID, "
                + "     STATUS, VIEW_COUNT, REMARK, CREATED_AT, UPDATED_AT "
                + " ) VALUES ( "
                + "     SEQ_SUGGESTION_BOARD.NEXTVAL, ?, ?, ?, ?, 0, ?, SYSDATE, SYSDATE "
                + " ) ";

        try (
            Connection conn = DBCPUtil.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql)
        ) {
            pstmt.setString(1, dto.getTitle());
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

    public int updateSuggestion(SuggestionDTO dto) {
        int result = 0;

        String sql = ""
                + " UPDATE SUGGESTION_BOARD "
                + " SET TITLE = ?, "
                + "     CONTENT = ?, "
                + "     STATUS = ?, "
                + "     REMARK = ?, "
                + "     UPDATED_AT = SYSDATE "
                + " WHERE SUGGESTION_ID = ? ";

        try (
            Connection conn = DBCPUtil.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql)
        ) {
            pstmt.setString(1, dto.getTitle());
            pstmt.setString(2, dto.getContent());
            pstmt.setString(3, dto.getStatus());
            pstmt.setString(4, dto.getRemark());
            pstmt.setLong(5, dto.getSuggestionId());
            result = pstmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

    public int hideSuggestion(long suggestionId) {
        int result = 0;

        String sql = ""
                + " UPDATE SUGGESTION_BOARD "
                + " SET STATUS = '내림', "
                + "     UPDATED_AT = SYSDATE "
                + " WHERE SUGGESTION_ID = ? ";

        try (
            Connection conn = DBCPUtil.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql)
        ) {
            pstmt.setLong(1, suggestionId);
            result = pstmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

    public int deleteSuggestion(long suggestionId) {
        int result = 0;

        String sql = " DELETE FROM SUGGESTION_BOARD WHERE SUGGESTION_ID = ? ";

        try (
            Connection conn = DBCPUtil.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql)
        ) {
            pstmt.setLong(1, suggestionId);
            result = pstmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

    private SuggestionDTO mapRow(ResultSet rs) throws Exception {
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
        return dto;
    }
}