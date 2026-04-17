package MasterDataMgmt.DefectManagement.service;

import java.sql.Connection;
import java.util.List;

import MasterDataMgmt.BOMManagement.dao.BOMMgmtDAO;
import MasterDataMgmt.DefectManagement.dao.DefectMgmtDAO;
import MasterDataMgmt.DefectManagement.dto.DefectMgmtDTO;
import MasterDataMgmt.DefectManagement.dto.DefectMgmtSearchDTO;
import common.jdbc.DBCPUtil;

public class DefectMgmtService {
	private DefectMgmtDAO dao = new DefectMgmtDAO();

    public List<DefectMgmtDTO> getList(DefectMgmtSearchDTO dto) {
        return dao.getList(dto);
    }

    public void insert(DefectMgmtDTO dto) {
        dao.insert(dto);
    }

    public int delete(int id) {
        return dao.delete(id);
    }

    public int deleteList(List<Integer> ids) {
        int result = 0;
        for (int id : ids) {
            result += dao.delete(id);
        }
        return result;
    }  
    
    public static boolean update(DefectMgmtDTO dto) {
        Connection conn = null;

        try {
            conn = DBCPUtil.getConnection();

            DefectMgmtDAO dao = new DefectMgmtDAO();

            int result = dao.update(conn, dto);

            return result > 0;

        } finally {
            DBCPUtil.close(conn);
        }
    }
}
