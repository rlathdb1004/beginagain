package member.service;

import java.sql.Connection;
import java.util.List;

import common.jdbc.DBCPUtil;
import common.util.SHA256Util;
import member.dao.MemberDAO;
import member.dto.MemberDTO;

public class MemberService {

    private MemberDAO memberDAO = new MemberDAO();

    public MemberDTO login(String empNo, String rawPassword) {
        Connection conn = null;

        try {
            conn = DBCPUtil.getConnection();

            MemberDTO member = memberDAO.findByEmpNo(conn, empNo);

            if (member == null) {
                return null;
            }

            String encryptedPassword = SHA256Util.encrypt(rawPassword);

            if (!encryptedPassword.equals(member.getPasswordHash())) {
                return null;
            }

            return member;
        } finally {
            DBCPUtil.close(conn);
        }
    }

    public boolean changePassword(int empId, String newRawPassword) {
        Connection conn = null;

        try {
            conn = DBCPUtil.getConnection();

            String newPasswordHash = SHA256Util.encrypt(newRawPassword);
            int result = memberDAO.updatePassword(conn, empId, newPasswordHash);

            return result > 0;
        } finally {
            DBCPUtil.close(conn);
        }
    }

    public List<MemberDTO> getMemberList() {
        Connection conn = null;

        try {
            conn = DBCPUtil.getConnection();
            return memberDAO.selectMemberList(conn);
        } finally {
            DBCPUtil.close(conn);
        }
    }

    public MemberDTO getMemberById(int empId) {
        Connection conn = null;

        try {
            conn = DBCPUtil.getConnection();
            return memberDAO.selectMemberById(conn, empId);
        } finally {
            DBCPUtil.close(conn);
        }
    }

    public boolean updateMember(MemberDTO dto) {
        Connection conn = null;

        try {
            conn = DBCPUtil.getConnection();
            int result = memberDAO.updateMember(conn, dto);
            return result > 0;
        } finally {
            DBCPUtil.close(conn);
        }
    }

    public boolean deleteMembers(String[] empIds) {
        Connection conn = null;

        try {
            conn = DBCPUtil.getConnection();
            int result = memberDAO.deleteMembers(conn, empIds);
            return result > 0;
        } finally {
            DBCPUtil.close(conn);
        }
    }

public boolean insertMember(MemberDTO dto) {
    Connection conn = null;
    try {
        conn = DBCPUtil.getConnection();
        int result = memberDAO.insertMember(conn, dto);
        return result > 0;
    } finally {
        DBCPUtil.close(conn);
    }
}

}