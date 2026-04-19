package MasterDataMgmt.DefectManagement.service;

import java.sql.Connection;
import java.util.List;

import MasterDataMgmt.DefectManagement.dao.DefectMgmtDAO;
import MasterDataMgmt.DefectManagement.dto.DefectMgmtDTO;
import MasterDataMgmt.DefectManagement.dto.DefectMgmtSearchDTO;
import common.jdbc.DBCPUtil;

public class DefectMgmtService {
    private DefectMgmtDAO dao = new DefectMgmtDAO();

    public List<DefectMgmtDTO> getList(DefectMgmtSearchDTO dto) {
        return dao.getList(dto);
    }

    public int getTotalCount(DefectMgmtSearchDTO dto) {
        return dao.getTotalCount(dto);
    }

    public void insert(DefectMgmtDTO dto) {
        validate(dto, null);
        dao.insert(dto);
    }

    public int delete(int id) {
        Connection conn = null;
        try {
            conn = DBCPUtil.getConnection();
            if (dao.isReferenced(conn, id)) throw new RuntimeException("사용 중인 불량코드는 삭제할 수 없습니다.");
            return dao.delete(id);
        } finally { DBCPUtil.close(conn); }
    }

    public int deleteList(List<Integer> ids) {
        int result = 0;
        for (int id : ids) result += delete(id);
        return result;
    }

    public boolean update(DefectMgmtDTO dto) {
        validate(dto, dto.getDefect_code_id());
        Connection conn = null;
        try {
            conn = DBCPUtil.getConnection();
            return dao.update(conn, dto) > 0;
        } finally { DBCPUtil.close(conn); }
    }

    private void validate(DefectMgmtDTO dto, Integer excludeId) {
        if (dto.getDefect_code() == null || dto.getDefect_code().trim().isEmpty()) throw new RuntimeException("불량코드를 입력하세요.");
        if (dto.getDefect_name() == null || dto.getDefect_name().trim().isEmpty()) throw new RuntimeException("불량명을 입력하세요.");
        if (dto.getDefect_type() == null || dto.getDefect_type().trim().isEmpty()) throw new RuntimeException("불량유형을 선택하세요.");
        Connection conn = null;
        try {
            conn = DBCPUtil.getConnection();
            if (dao.existsDuplicateCode(conn, dto.getDefect_code().trim(), excludeId)) {
                throw new RuntimeException("이미 사용 중인 불량코드입니다.");
            }
        } finally { DBCPUtil.close(conn); }
    }
}
