package member.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import member.dto.MemberDTO;

public class MemberDAO {

    public MemberDTO findByEmpNo(Connection conn, String empNo) {
        MemberDTO member = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        String sql = ""
                + "SELECT EMP_ID, EMP_NO, EMP_NAME, PASSWORD_HASH, DEPT_CODE, POSITION_NAME, "
                + "       EMAIL, PHONE, STATUS, ROLE_NAME, USE_YN, TEMP_PWD_YN, "
                + "       REMARK, "
                + "       CREATED_AT, UPDATED_AT "
                + "FROM EMP "
                + "WHERE EMP_NO = ? "
                + "  AND USE_YN = 'Y'";

        try {
            ps = conn.prepareStatement(sql);
            ps.setString(1, empNo);
            rs = ps.executeQuery();

            if (rs.next()) {
                member = new MemberDTO();
                member.setEmpId(rs.getInt("EMP_ID"));
                member.setEmpNo(rs.getString("EMP_NO"));
                member.setEmpName(rs.getString("EMP_NAME"));
                member.setPasswordHash(rs.getString("PASSWORD_HASH"));
                member.setDeptCode(rs.getString("DEPT_CODE"));
                member.setPositionName(rs.getString("POSITION_NAME"));
                member.setEmail(rs.getString("EMAIL"));
                member.setPhone(rs.getString("PHONE"));
                member.setStatus(rs.getString("STATUS"));
                member.setRoleName(rs.getString("ROLE_NAME"));
                member.setUseYn(rs.getString("USE_YN"));
                member.setTempPwdYn(rs.getString("TEMP_PWD_YN"));
                member.setRemark(rs.getString("REMARK"));
                member.setCreatedAt(rs.getTimestamp("CREATED_AT"));
                member.setUpdatedAt(rs.getTimestamp("UPDATED_AT"));
            }

        } catch (Exception e) {
            throw new RuntimeException("사번 조회 실패", e);
        } finally {
            close(rs);
            close(ps);
        }

        return member;
    }

    public String selectPasswordHashByEmpId(Connection conn, int empId) {
        PreparedStatement ps = null;
        ResultSet rs = null;
        String passwordHash = null;

        String sql = ""
                + "SELECT PASSWORD_HASH "
                + "FROM EMP "
                + "WHERE EMP_ID = ? "
                + "  AND USE_YN = 'Y'";

        try {
            ps = conn.prepareStatement(sql);
            ps.setInt(1, empId);
            rs = ps.executeQuery();

            if (rs.next()) {
                passwordHash = rs.getString("PASSWORD_HASH");
            }
        } catch (Exception e) {
            throw new RuntimeException("현재 비밀번호 조회 실패", e);
        } finally {
            close(rs);
            close(ps);
        }

        return passwordHash;
    }

    public int updatePassword(Connection conn, int empId, String newPasswordHash) {
        PreparedStatement ps = null;
        int result = 0;

        String sql = ""
                + "UPDATE EMP "
                + "SET PASSWORD_HASH = ?, "
                + "    TEMP_PWD_YN = 'N', "
                + "    UPDATED_AT = CAST(SYSTIMESTAMP AT TIME ZONE 'Asia/Seoul' AS DATE) "
                + "WHERE EMP_ID = ?";

        try {
            ps = conn.prepareStatement(sql);
            ps.setString(1, newPasswordHash);
            ps.setInt(2, empId);

            result = ps.executeUpdate();
        } catch (Exception e) {
            throw new RuntimeException("비밀번호 변경 실패", e);
        } finally {
            close(ps);
        }

        return result;
    }

    public List<MemberDTO> selectMemberList(Connection conn) {
        List<MemberDTO> list = new ArrayList<MemberDTO>();
        PreparedStatement ps = null;
        ResultSet rs = null;

        String sql = ""
                + "SELECT EMP_ID, EMP_NO, EMP_NAME, DEPT_CODE, POSITION_NAME, EMAIL, PHONE, "
                + "       STATUS, ROLE_NAME, USE_YN, TEMP_PWD_YN, REMARK, "
                + "       CREATED_AT, UPDATED_AT "
                + "FROM EMP "
                + "WHERE USE_YN = 'Y' "
                + "ORDER BY EMP_ID DESC";

        try {
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();

            while (rs.next()) {
                MemberDTO dto = new MemberDTO();
                dto.setEmpId(rs.getInt("EMP_ID"));
                dto.setEmpNo(rs.getString("EMP_NO"));
                dto.setEmpName(rs.getString("EMP_NAME"));
                dto.setDeptCode(rs.getString("DEPT_CODE"));
                dto.setPositionName(rs.getString("POSITION_NAME"));
                dto.setEmail(rs.getString("EMAIL"));
                dto.setPhone(rs.getString("PHONE"));
                dto.setStatus(rs.getString("STATUS"));
                dto.setRoleName(rs.getString("ROLE_NAME"));
                dto.setUseYn(rs.getString("USE_YN"));
                dto.setTempPwdYn(rs.getString("TEMP_PWD_YN"));
                dto.setRemark(rs.getString("REMARK"));
                dto.setCreatedAt(rs.getTimestamp("CREATED_AT"));
                dto.setUpdatedAt(rs.getTimestamp("UPDATED_AT"));

                list.add(dto);
            }
        } catch (Exception e) {
            throw new RuntimeException("사원 목록 조회 실패", e);
        } finally {
            close(rs);
            close(ps);
        }

        return list;
    }

    public MemberDTO selectMemberById(Connection conn, int empId) {
        MemberDTO dto = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        String sql = ""
                + "SELECT EMP_ID, EMP_NO, EMP_NAME, DEPT_CODE, POSITION_NAME, EMAIL, PHONE, "
                + "       STATUS, ROLE_NAME, USE_YN, TEMP_PWD_YN, REMARK, "
                + "       CREATED_AT, UPDATED_AT "
                + "FROM EMP "
                + "WHERE EMP_ID = ? "
                + "  AND USE_YN = 'Y'";

        try {
            ps = conn.prepareStatement(sql);
            ps.setInt(1, empId);
            rs = ps.executeQuery();

            if (rs.next()) {
                dto = new MemberDTO();
                dto.setEmpId(rs.getInt("EMP_ID"));
                dto.setEmpNo(rs.getString("EMP_NO"));
                dto.setEmpName(rs.getString("EMP_NAME"));
                dto.setDeptCode(rs.getString("DEPT_CODE"));
                dto.setPositionName(rs.getString("POSITION_NAME"));
                dto.setEmail(rs.getString("EMAIL"));
                dto.setPhone(rs.getString("PHONE"));
                dto.setStatus(rs.getString("STATUS"));
                dto.setRoleName(rs.getString("ROLE_NAME"));
                dto.setUseYn(rs.getString("USE_YN"));
                dto.setTempPwdYn(rs.getString("TEMP_PWD_YN"));
                dto.setRemark(rs.getString("REMARK"));
                dto.setCreatedAt(rs.getTimestamp("CREATED_AT"));
                dto.setUpdatedAt(rs.getTimestamp("UPDATED_AT"));
            }
        } catch (Exception e) {
            throw new RuntimeException("사원 상세 조회 실패", e);
        } finally {
            close(rs);
            close(ps);
        }

        return dto;
    }

    public int updateMember(Connection conn, MemberDTO dto) {
        PreparedStatement ps = null;
        int result = 0;

        String sql = ""
                + "UPDATE EMP "
                + "SET EMP_NAME = ?, "
                + "    DEPT_CODE = ?, "
                + "    POSITION_NAME = ?, "
                + "    EMAIL = ?, "
                + "    PHONE = ?, "
                + "    STATUS = ?, "
                + "    ROLE_NAME = ?, "
                + "    TEMP_PWD_YN = ?, "
                + "    REMARK = ?, "
                + "    UPDATED_AT = CAST(SYSTIMESTAMP AT TIME ZONE 'Asia/Seoul' AS DATE) "
                + "WHERE EMP_ID = ?";

        try {
            ps = conn.prepareStatement(sql);
            ps.setString(1, dto.getEmpName());
            ps.setString(2, dto.getDeptCode());
            ps.setString(3, dto.getPositionName());
            ps.setString(4, dto.getEmail());
            ps.setString(5, dto.getPhone());
            ps.setString(6, dto.getStatus());
            ps.setString(7, dto.getRoleName());
            ps.setString(8, dto.getTempPwdYn());
            ps.setString(9, dto.getRemark());
            ps.setInt(10, dto.getEmpId());

            result = ps.executeUpdate();
        } catch (Exception e) {
            throw new RuntimeException("사원 수정 실패", e);
        } finally {
            close(ps);
        }

        return result;
    }

    public int resetPasswordByAdmin(Connection conn, int empId, String tempPasswordHash) {
        PreparedStatement ps = null;
        int result = 0;

        String sql = ""
                + "UPDATE EMP "
                + "SET PASSWORD_HASH = ?, "
                + "    TEMP_PWD_YN = 'Y', "
                + "    UPDATED_AT = CAST(SYSTIMESTAMP AT TIME ZONE 'Asia/Seoul' AS DATE) "
                + "WHERE EMP_ID = ? "
                + "  AND USE_YN = 'Y'";

        try {
            ps = conn.prepareStatement(sql);
            ps.setString(1, tempPasswordHash);
            ps.setInt(2, empId);

            result = ps.executeUpdate();
        } catch (Exception e) {
            throw new RuntimeException("관리자 비밀번호 초기화 실패", e);
        } finally {
            close(ps);
        }

        return result;
    }

    public int updateMyPage(Connection conn, MemberDTO dto) {
        PreparedStatement ps = null;
        int result = 0;

        String sql = ""
                + "UPDATE EMP "
                + "SET EMAIL = ?, "
                + "    PHONE = ?, "
                + "    REMARK = ?, "
                + "    UPDATED_AT = CAST(SYSTIMESTAMP AT TIME ZONE 'Asia/Seoul' AS DATE) "
                + "WHERE EMP_ID = ? "
                + "  AND USE_YN = 'Y'";

        try {
            ps = conn.prepareStatement(sql);
            ps.setString(1, dto.getEmail());
            ps.setString(2, dto.getPhone());
            ps.setString(3, dto.getRemark());
            ps.setInt(4, dto.getEmpId());

            result = ps.executeUpdate();
        } catch (Exception e) {
            throw new RuntimeException("마이페이지 수정 실패", e);
        } finally {
            close(ps);
        }

        return result;
    }

    public int deleteMembers(Connection conn, String[] empIds) {
        PreparedStatement ps = null;
        int result = 0;

        String sql = ""
                + "UPDATE EMP "
                + "SET USE_YN = 'N', "
                + "    UPDATED_AT = CAST(SYSTIMESTAMP AT TIME ZONE 'Asia/Seoul' AS DATE) "
                + "WHERE EMP_ID = ?";

        try {
            ps = conn.prepareStatement(sql);

            for (int i = 0; i < empIds.length; i++) {
                ps.setInt(1, Integer.parseInt(empIds[i]));
                result += ps.executeUpdate();
            }
        } catch (Exception e) {
            throw new RuntimeException("사원 삭제 실패", e);
        } finally {
            close(ps);
        }

        return result;
    }

    public int insertMember(Connection conn, MemberDTO dto) {
        PreparedStatement ps = null;
        int result = 0;

        String sql = ""
                + "INSERT INTO EMP (EMP_ID, EMP_NO, EMP_NAME, PASSWORD_HASH, DEPT_CODE, POSITION_NAME, "
                + "EMAIL, PHONE, STATUS, ROLE_NAME, USE_YN, TEMP_PWD_YN, REMARK, CREATED_AT, UPDATED_AT) "
                + "VALUES (SEQ_EMP.NEXTVAL, ?, ?, ?, ?, ?, ?, ?, ?, ?, 'Y', 'Y', ?, "
                + "CAST(SYSTIMESTAMP AT TIME ZONE 'Asia/Seoul' AS DATE), "
                + "CAST(SYSTIMESTAMP AT TIME ZONE 'Asia/Seoul' AS DATE))";

        try {
            ps = conn.prepareStatement(sql);
            ps.setString(1, dto.getEmpNo());
            ps.setString(2, dto.getEmpName());
            ps.setString(3, "03ac674216f3e15c761ee1a5e255f067953623c8b388b4459e13f978d7c846f4");
            ps.setString(4, dto.getDeptCode());
            ps.setString(5, dto.getPositionName());
            ps.setString(6, dto.getEmail());
            ps.setString(7, dto.getPhone());
            ps.setString(8, dto.getStatus());
            ps.setString(9, dto.getRoleName());
            ps.setString(10, dto.getRemark());

            result = ps.executeUpdate();
        } catch (Exception e) {
            throw new RuntimeException("사원 등록 실패", e);
        } finally {
            close(ps);
        }

        return result;
    }

    private void close(ResultSet rs) {
        try {
            if (rs != null) rs.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void close(PreparedStatement ps) {
        try {
            if (ps != null) ps.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}