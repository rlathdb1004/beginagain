package process.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import process.dto.ProcessDTO;

public class ProcessDAO {

    public List<ProcessDTO> selectProcessList(Connection conn) {
        List<ProcessDTO> list = new ArrayList<>();
        PreparedStatement ps = null;
        ResultSet rs = null;

        String sql = "SELECT * FROM PROCESS WHERE USE_YN='Y' ORDER BY PROCESS_ID DESC";

        try {
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();

            while (rs.next()) {
                ProcessDTO dto = new ProcessDTO();
                dto.setProcessId(rs.getInt("PROCESS_ID"));
                dto.setProcessCode(rs.getString("PROCESS_CODE"));
                dto.setProcessName(rs.getString("PROCESS_NAME"));
                dto.setDescription(rs.getString("DESCRIPTION"));
                dto.setUseYn(rs.getString("USE_YN"));
                dto.setRemark(rs.getString("REMARK"));
                dto.setCreatedAt(rs.getDate("CREATED_AT"));
                dto.setUpdatedAt(rs.getDate("UPDATED_AT"));

                list.add(dto);
            }

        } catch (Exception e) {
            throw new RuntimeException("공정 목록 조회 실패", e);
        } finally {
            try { if (rs != null) rs.close(); } catch (Exception e) {}
            try { if (ps != null) ps.close(); } catch (Exception e) {}
        }

        return list;
    }

    public ProcessDTO selectProcessById(Connection conn, int processId) {
        ProcessDTO dto = null;

        String sql = "SELECT * FROM PROCESS WHERE PROCESS_ID=? AND USE_YN='Y'";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, processId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                dto = new ProcessDTO();
                dto.setProcessId(rs.getInt("PROCESS_ID"));
                dto.setProcessCode(rs.getString("PROCESS_CODE"));
                dto.setProcessName(rs.getString("PROCESS_NAME"));
                dto.setDescription(rs.getString("DESCRIPTION"));
                dto.setUseYn(rs.getString("USE_YN"));
                dto.setRemark(rs.getString("REMARK"));
            }

        } catch (Exception e) {
            throw new RuntimeException("공정 상세 조회 실패", e);
        }

        return dto;
    }

    public int updateProcess(Connection conn, ProcessDTO dto) {
        String sql = "UPDATE PROCESS SET PROCESS_NAME=?, DESCRIPTION=?, REMARK=?, UPDATED_AT=SYSDATE WHERE PROCESS_ID=?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, dto.getProcessName());
            ps.setString(2, dto.getDescription());
            ps.setString(3, dto.getRemark());
            ps.setInt(4, dto.getProcessId());

            return ps.executeUpdate();
        } catch (Exception e) {
            throw new RuntimeException("공정 수정 실패", e);
        }
    }

    public int deleteProcess(Connection conn, String[] ids) {
        int result = 0;

        String sql = "UPDATE PROCESS SET USE_YN='N', UPDATED_AT=SYSDATE WHERE PROCESS_ID=?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            for (String id : ids) {
                ps.setInt(1, Integer.parseInt(id));
                result += ps.executeUpdate();
            }
        } catch (Exception e) {
            throw new RuntimeException("공정 삭제 실패", e);
        }

        return result;
    }

public int insertProcess(Connection conn, ProcessDTO dto) {
    PreparedStatement ps = null;
    int result = 0;
    String sql = "INSERT INTO PROCESS (PROCESS_ID, PROCESS_CODE, PROCESS_NAME, DESCRIPTION, USE_YN, REMARK, CREATED_AT, UPDATED_AT) "
            + "SELECT seq_no, 'PROC' || LPAD(seq_no, 4, '0'), ?, ?, 'Y', ?, SYSDATE, SYSDATE "
            + "FROM (SELECT SEQ_PROCESS.NEXTVAL AS seq_no FROM DUAL)";
    try {
        ps = conn.prepareStatement(sql);
        ps.setString(1, dto.getProcessName());
        ps.setString(2, dto.getDescription());
        ps.setString(3, dto.getRemark());
        result = ps.executeUpdate();
    } catch (Exception e) {
        throw new RuntimeException("공정 등록 실패", e);
    } finally {
        try { if (ps != null) ps.close(); } catch (Exception e) {}
    }
    return result;
}

}