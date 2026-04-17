package member.service;

import java.security.SecureRandom;
import java.sql.Connection;
import java.util.List;

import common.jdbc.DBCPUtil;
import common.util.SHA256Util;
import member.dao.MemberDAO;
import member.dto.MemberDTO;

public class MemberService {

    private static final SecureRandom SECURE_RANDOM = new SecureRandom();
    private static final String TEMP_PASSWORD_CHARS = "ABCDEFGHJKLMNPQRSTUVWXYZabcdefghijkmnopqrstuvwxyz23456789";

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

    public boolean checkCurrentPassword(int empId, String currentRawPassword) {
        Connection conn = null;

        try {
            conn = DBCPUtil.getConnection();

            String savedHash = memberDAO.selectPasswordHashByEmpId(conn, empId);
            if (savedHash == null) {
                return false;
            }

            String inputHash = SHA256Util.encrypt(currentRawPassword);
            return savedHash.equals(inputHash);
        } finally {
            DBCPUtil.close(conn);
        }
    }

    public boolean isValidPassword(String password, String empNo, String currentPassword) {
        if (password == null) {
            return false;
        }

        if (password.length() < 8 || password.length() > 20) {
            return false;
        }

        if (password.matches(".*\\s+.*")) {
            return false;
        }

        if (password.equals(currentPassword)) {
            return false;
        }

        if (empNo != null && !"".equals(empNo) && password.toLowerCase().contains(empNo.toLowerCase())) {
            return false;
        }

        int typeCount = 0;
        if (password.matches(".*[A-Z].*")) typeCount++;
        if (password.matches(".*[a-z].*")) typeCount++;
        if (password.matches(".*[0-9].*")) typeCount++;
        if (password.matches(".*[^A-Za-z0-9].*")) typeCount++;

        return typeCount >= 3;
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


    public String resetPasswordByAdmin(int targetEmpId) {
        Connection conn = null;

        try {
            conn = DBCPUtil.getConnection();

            String tempPassword = generateTempPassword(10);
            String tempPasswordHash = SHA256Util.encrypt(tempPassword);

            int result = memberDAO.resetPasswordByAdmin(conn, targetEmpId, tempPasswordHash);
            if (result > 0) {
                return tempPassword;
            }
            return null;
        } finally {
            DBCPUtil.close(conn);
        }
    }

    private String generateTempPassword(int length) {
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < length; i++) {
            int index = SECURE_RANDOM.nextInt(TEMP_PASSWORD_CHARS.length());
            sb.append(TEMP_PASSWORD_CHARS.charAt(index));
        }

        return sb.toString();
    }

    public boolean updateMyPage(MemberDTO dto) {
        Connection conn = null;

        try {
            conn = DBCPUtil.getConnection();
            int result = memberDAO.updateMyPage(conn, dto);
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
