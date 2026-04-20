package MasterDataMgmt.DefectManagement.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

import MasterDataMgmt.DefectManagement.dto.DefectMgmtDTO;
import MasterDataMgmt.DefectManagement.dto.DefectMgmtSearchDTO;

public class DefectMgmtDAO {

    public List<DefectMgmtDTO> getList(DefectMgmtSearchDTO dto) {
        List<DefectMgmtDTO> list = new ArrayList<>();
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            conn = getConnection();
            StringBuilder sql = new StringBuilder();
            List<Object> params = new ArrayList<>();

            sql.append("SELECT * FROM ( ");
            sql.append("    SELECT ROW_NUMBER() OVER (ORDER BY DEFECT_CODE_ID DESC) AS RN, t.* ");
            sql.append("    FROM ( ");
            sql.append("        SELECT DEFECT_CODE_ID, DEFECT_CODE, DEFECT_NAME, DEFECT_TYPE, DESCRIPTION, USE_YN, REMARK ");
            sql.append("        FROM DEFECT_CODE ");
            sql.append("        WHERE NVL(USE_YN,'Y') = 'Y' ");
            appendSearchCondition(sql, params, dto);
            sql.append("        ORDER BY DEFECT_CODE_ID DESC ");
            sql.append("    ) t ");
            sql.append(") WHERE RN BETWEEN ? AND ?");

            params.add(dto.getStartRow());
            params.add(dto.getEndRow());

            ps = conn.prepareStatement(sql.toString());
            bindParams(ps, params);
            rs = ps.executeQuery();
            while (rs.next()) {
                list.add(map(rs));
            }
        } catch (Exception e) {
            throw new RuntimeException("불량코드 목록 조회 실패", e);
        } finally {
            close(rs, ps, conn);
        }
        return list;
    }

    public int getTotalCount(DefectMgmtSearchDTO dto) {
        int count = 0;
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = getConnection();
            StringBuilder sql = new StringBuilder();
            List<Object> params = new ArrayList<>();

            sql.append("SELECT COUNT(*) FROM DEFECT_CODE WHERE NVL(USE_YN,'Y') = 'Y' ");
            appendSearchCondition(sql, params, dto);

            ps = conn.prepareStatement(sql.toString());
            bindParams(ps, params);
            rs = ps.executeQuery();
            if (rs.next()) {
                count = rs.getInt(1);
            }
        } catch (Exception e) {
            throw new RuntimeException("불량코드 개수 조회 실패", e);
        } finally {
            close(rs, ps, conn);
        }
        return count;
    }

    private void appendSearchCondition(StringBuilder sql, List<Object> params, DefectMgmtSearchDTO dto) {
        String keyword = dto.getKeyword();
        String searchType = dto.getSearchType();
        if (keyword == null || keyword.trim().isEmpty()) {
            return;
        }

        String like = "%" + keyword.trim() + "%";
        sql.append("AND (");
        if (searchType == null || searchType.trim().isEmpty() || "all".equals(searchType)) {
            sql.append("UPPER(DEFECT_CODE) LIKE UPPER(?) OR UPPER(DEFECT_NAME) LIKE UPPER(?) OR UPPER(DEFECT_TYPE) LIKE UPPER(?)");
            params.add(like);
            params.add(like);
            params.add(like);
        } else if ("defectCode".equals(searchType)) {
            sql.append("UPPER(DEFECT_CODE) LIKE UPPER(?)");
            params.add(like);
        } else if ("defectName".equals(searchType)) {
            sql.append("UPPER(DEFECT_NAME) LIKE UPPER(?)");
            params.add(like);
        } else if ("defectType".equals(searchType)) {
            sql.append("UPPER(DEFECT_TYPE) LIKE UPPER(?)");
            params.add(like);
        } else {
            sql.append("UPPER(DEFECT_CODE) LIKE UPPER(?) OR UPPER(DEFECT_NAME) LIKE UPPER(?) OR UPPER(DEFECT_TYPE) LIKE UPPER(?)");
            params.add(like);
            params.add(like);
            params.add(like);
        }
        sql.append(") ");
    }

    private void bindParams(PreparedStatement ps, List<Object> params) throws Exception {
        for (int i = 0; i < params.size(); i++) {
            ps.setObject(i + 1, params.get(i));
        }
    }

    public void insert(DefectMgmtDTO dto) {
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = getConnection();

            int defectCodeId = getNextSequenceValue(conn, "SEQ_DEFECT_CODE");
            String defectCode = buildCode("DEF", defectCodeId);

            String sql = "INSERT INTO DEFECT_CODE (DEFECT_CODE_ID, DEFECT_CODE, DEFECT_NAME, DEFECT_TYPE, DESCRIPTION, USE_YN, REMARK, CREATED_AT, UPDATED_AT) "
                    + "VALUES (?, ?, ?, ?, ?, 'Y', ?, SYSDATE, SYSDATE)";

            ps = conn.prepareStatement(sql);
            ps.setInt(1, defectCodeId);
            ps.setString(2, defectCode);
            ps.setString(3, dto.getDefect_name());
            ps.setString(4, dto.getDefect_type());
            ps.setString(5, dto.getDescription());
            ps.setString(6, dto.getRemark());
            ps.executeUpdate();
        } catch (Exception e) {
            throw new RuntimeException("불량코드 등록 실패", e);
        } finally {
            close(null, ps, conn);
        }
    }

    public int delete(int id) {
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = getConnection();
            String sql = "UPDATE DEFECT_CODE SET USE_YN = 'N', UPDATED_AT = SYSDATE WHERE DEFECT_CODE_ID = ?";
            ps = conn.prepareStatement(sql);
            ps.setInt(1, id);
            return ps.executeUpdate();
        } catch (Exception e) {
            throw new RuntimeException("불량코드 삭제 실패", e);
        } finally {
            close(null, ps, conn);
        }
    }

    public int update(Connection conn, DefectMgmtDTO dto) {
        PreparedStatement ps = null;
        try {
            String sql = "UPDATE DEFECT_CODE SET DEFECT_NAME=?, DEFECT_TYPE=?, DESCRIPTION=?, REMARK=?, UPDATED_AT=SYSDATE WHERE DEFECT_CODE_ID=?";
            ps = conn.prepareStatement(sql);
            ps.setString(1, dto.getDefect_name());
            ps.setString(2, dto.getDefect_type());
            ps.setString(3, dto.getDescription());
            ps.setString(4, dto.getRemark());
            ps.setInt(5, dto.getDefect_code_id());
            return ps.executeUpdate();
        } catch (Exception e) {
            throw new RuntimeException("불량코드 수정 실패", e);
        } finally {
            try {
                if (ps != null) {
                    ps.close();
                }
            } catch (Exception e) {}
        }
    }

    public DefectMgmtDTO selectOne(int defectCodeId) {
        DefectMgmtDTO dto = null;
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = getConnection();
            String sql = "SELECT DEFECT_CODE_ID, DEFECT_CODE, DEFECT_NAME, DEFECT_TYPE, DESCRIPTION, REMARK, USE_YN FROM DEFECT_CODE WHERE DEFECT_CODE_ID = ?";
            ps = conn.prepareStatement(sql);
            ps.setInt(1, defectCodeId);
            rs = ps.executeQuery();
            if (rs.next()) {
                dto = map(rs);
            }
        } catch (Exception e) {
            throw new RuntimeException("불량코드 상세 조회 실패", e);
        } finally {
            close(rs, ps, conn);
        }
        return dto;
    }

    public boolean existsDuplicateCode(Connection conn, String defectCode, Integer excludeId) {
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            String sql = "SELECT COUNT(*) FROM DEFECT_CODE WHERE UPPER(DEFECT_CODE)=UPPER(?) AND NVL(USE_YN,'Y')='Y'"
                    + (excludeId != null ? " AND DEFECT_CODE_ID <> ?" : "");
            ps = conn.prepareStatement(sql);
            ps.setString(1, defectCode);
            if (excludeId != null) {
                ps.setInt(2, excludeId);
            }
            rs = ps.executeQuery();
            return rs.next() && rs.getInt(1) > 0;
        } catch (Exception e) {
            throw new RuntimeException("불량코드 중복 확인 실패", e);
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
            } catch (Exception e) {}
            try {
                if (ps != null) {
                    ps.close();
                }
            } catch (Exception e) {}
        }
    }

    public boolean isReferenced(Connection conn, int defectCodeId) {
        return count(conn, "SELECT COUNT(*) FROM DEFECT_PRODUCT WHERE DEFECT_CODE_ID = ? AND NVL(USE_YN,'Y')='Y'", defectCodeId) > 0
                || count(conn, "SELECT COUNT(*) FROM DEFECT_MATERIAL WHERE DEFECT_CODE_ID = ?", defectCodeId) > 0;
    }

    private int count(Connection conn, String sql, int id) {
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = conn.prepareStatement(sql);
            ps.setInt(1, id);
            rs = ps.executeQuery();
            return rs.next() ? rs.getInt(1) : 0;
        } catch (Exception e) {
            throw new RuntimeException("불량코드 참조 확인 실패", e);
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
            } catch (Exception e) {}
            try {
                if (ps != null) {
                    ps.close();
                }
            } catch (Exception e) {}
        }
    }

    private DefectMgmtDTO map(ResultSet rs) throws Exception {
        DefectMgmtDTO d = new DefectMgmtDTO();
        d.setDefect_code_id(rs.getInt("DEFECT_CODE_ID"));
        d.setDefect_code(rs.getString("DEFECT_CODE"));
        d.setDefect_name(rs.getString("DEFECT_NAME"));
        d.setDefect_type(rs.getString("DEFECT_TYPE"));
        d.setDescription(rs.getString("DESCRIPTION"));
        d.setUse_yn(rs.getString("USE_YN"));
        d.setRemark(rs.getString("REMARK"));
        return d;
    }

    private int getNextSequenceValue(Connection conn, String sequenceName) throws SQLException {
        String sql = "SELECT " + sequenceName + ".NEXTVAL FROM DUAL";
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
            throw new SQLException("시퀀스 값을 가져오지 못했습니다: " + sequenceName);
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
            } catch (Exception e) {}
            try {
                if (ps != null) {
                    ps.close();
                }
            } catch (Exception e) {}
        }
    }

    private String buildCode(String prefix, int id) {
        return prefix + String.format("%04d", id);
    }

    private Connection getConnection() throws Exception {
        Context ctx = new InitialContext();
        DataSource ds = (DataSource) ctx.lookup("java:comp/env/jdbc/oracle");
        return ds.getConnection();
    }

    private void close(ResultSet rs, PreparedStatement ps, Connection conn) {
        try {
            if (rs != null) {
                rs.close();
            }
        } catch (Exception e) {}
        try {
            if (ps != null) {
                ps.close();
            }
        } catch (Exception e) {}
        try {
            if (conn != null) {
                conn.close();
            }
        } catch (Exception e) {}
    }
}