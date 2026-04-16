package MasterDataMgmt.DefectManagement;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

public class DefectMgmtDAO {

    public List<DefectMgmtDTO> getList(DefectMgmtSearchDTO dto) {

    	List<DefectMgmtDTO> list = new ArrayList<>();

        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            Context ctx = new InitialContext();
            DataSource ds = (DataSource) ctx.lookup("java:comp/env/jdbc/oracle");
            conn = ds.getConnection();

            StringBuilder sql = new StringBuilder();
            sql.append("SELECT * FROM DEFECT_CODE WHERE 1=1 ");

            List<Object> params = new ArrayList<>();

            if (dto.getKeyword() != null && !dto.getKeyword().trim().isEmpty()) {
                String like = "%" + dto.getKeyword() + "%";

                sql.append("AND (");
                sql.append("UPPER(DEFECT_CODE) LIKE UPPER(?) OR ");
                sql.append("UPPER(DEFECT_NAME) LIKE UPPER(?) ");
                sql.append(") ");

                params.add(like);
                params.add(like);
            }

            sql.append("ORDER BY DEFECT_CODE_ID DESC");

            ps = conn.prepareStatement(sql.toString());

            for (int i = 0; i < params.size(); i++) {
                ps.setObject(i + 1, params.get(i));
            }

            rs = ps.executeQuery();

            while (rs.next()) {
                DefectMgmtDTO d = new DefectMgmtDTO();

                d.setDefect_code_id(rs.getInt("DEFECT_CODE_ID"));
                d.setDefect_code(rs.getString("DEFECT_CODE"));
                d.setDefect_name(rs.getString("DEFECT_NAME"));
                d.setDefect_type(rs.getString("DEFECT_TYPE"));
                d.setDescription(rs.getString("DESCRIPTION"));
                d.setUse_yn(rs.getString("USE_YN"));
                d.setRemark(rs.getString("REMARK"));

                list.add(d);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try { if (rs != null) rs.close(); } catch (Exception e) {}
            try { if (ps != null) ps.close(); } catch (Exception e) {}
            try { if (conn != null) conn.close(); } catch (Exception e) {}
        }

        return list;
    }

    public void insert(DefectMgmtDTO dto) {

        Connection conn = null;
        PreparedStatement ps = null;

        try {
            Context ctx = new InitialContext();
            DataSource ds = (DataSource) ctx.lookup("java:comp/env/jdbc/oracle");

            conn = ds.getConnection();

            String sql = "INSERT INTO DEFECT_CODE "
                    + "(DEFECT_CODE_ID, DEFECT_CODE, DEFECT_NAME, DEFECT_TYPE, DESCRIPTION, USE_YN, REMARK, CREATED_AT, UPDATED_AT) "
                    + "VALUES (SEQ_DEFECT_CODE.NEXTVAL, ?, ?, ?, ?, ?, ?, SYSDATE, SYSDATE)";

            ps = conn.prepareStatement(sql);

            ps.setString(1, dto.getDefect_code());
            ps.setString(2, dto.getDefect_name());
            ps.setString(3, dto.getDefect_type());
            ps.setString(4, dto.getDescription());
            ps.setString(5, "Y");
            ps.setString(6, dto.getRemark());

            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try { if (ps != null) ps.close(); } catch (Exception e) {}
            try { if (conn != null) conn.close(); } catch (Exception e) {}
        }
    }
    
    public int delete(int id) {

        Connection conn = null;
        PreparedStatement ps = null;

        try {
            Context ctx = new InitialContext();
            DataSource ds = (DataSource) ctx.lookup("java:comp/env/jdbc/oracle");

            conn = ds.getConnection();

            String sql = "DELETE FROM DEFECT_CODE WHERE DEFECT_CODE_ID = ?";

            ps = conn.prepareStatement(sql);
            ps.setInt(1, id);

            return ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try { if (ps != null) ps.close(); } catch (Exception e) {}
            try { if (conn != null) conn.close(); } catch (Exception e) {}
        }

        return 0;
    }
    
    public int update(DefectMgmtDTO dto) {

        Connection conn = null;
        PreparedStatement ps = null;

        try {
            Context ctx = new InitialContext();
            DataSource ds = (DataSource) ctx.lookup("java:comp/env/jdbc/oracle");

            conn = ds.getConnection();

            String sql = "UPDATE DEFECT_CODE SET "
                    + "DEFECT_CODE=?, DEFECT_NAME=?, DEFECT_TYPE=?, REMARK=?, UPDATED_AT=SYSDATE "
                    + "WHERE DEFECT_CODE_ID=?";

            ps = conn.prepareStatement(sql);

            ps.setString(1, dto.getDefect_code());
            ps.setString(2, dto.getDefect_name());
            ps.setString(3, dto.getDefect_type());
            ps.setString(4, dto.getRemark());
            ps.setInt(5, dto.getDefect_code_id());

            return ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try { if (ps != null) ps.close(); } catch (Exception e) {}
            try { if (conn != null) conn.close(); } catch (Exception e) {}
        }

        return 0;
    }
}