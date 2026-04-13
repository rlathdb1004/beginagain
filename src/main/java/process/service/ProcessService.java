package process.service;

import java.sql.Connection;
import java.util.List;

import common.jdbc.DBCPUtil;
import process.dao.ProcessDAO;
import process.dto.ProcessDTO;

public class ProcessService {

	private ProcessDAO dao = new ProcessDAO();

	public List<ProcessDTO> getList() {
		Connection conn = DBCPUtil.getConnection();
		try {
			return dao.selectProcessList(conn);
		} finally {
			DBCPUtil.close(conn);
		}
	}

	public ProcessDTO getById(int id) {
		Connection conn = DBCPUtil.getConnection();
		try {
			return dao.selectProcessById(conn, id);
		} finally {
			DBCPUtil.close(conn);
		}
	}

	public boolean update(ProcessDTO dto) {
		Connection conn = DBCPUtil.getConnection();
		try {
			return dao.updateProcess(conn, dto) > 0;
		} finally {
			DBCPUtil.close(conn);
		}
	}

	public boolean delete(String[] ids) {
		Connection conn = DBCPUtil.getConnection();
		try {
			return dao.deleteProcess(conn, ids) > 0;
		} finally {
			DBCPUtil.close(conn);
		}
	}

public boolean insert(ProcessDTO dto) {
    Connection conn = DBCPUtil.getConnection();
    try {
        return dao.insertProcess(conn, dto) > 0;
    } finally {
        DBCPUtil.close(conn);
    }
}

}