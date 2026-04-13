package equipment.service;

import java.sql.Connection;
import java.util.List;

import common.jdbc.DBCPUtil;
import equipment.dao.EquipmentDAO;
import equipment.dto.EquipmentDTO;

public class EquipmentService {

	private EquipmentDAO equipmentDAO = new EquipmentDAO();

	public List<EquipmentDTO> getEquipmentList() {
		Connection conn = null;

		try {
			conn = DBCPUtil.getConnection();
			return equipmentDAO.selectEquipmentList(conn);
		} finally {
			DBCPUtil.close(conn);
		}
	}

	public EquipmentDTO getEquipmentById(int equipmentId) {
		Connection conn = null;

		try {
			conn = DBCPUtil.getConnection();
			return equipmentDAO.selectEquipmentById(conn, equipmentId);
		} finally {
			DBCPUtil.close(conn);
		}
	}

	public boolean updateEquipment(EquipmentDTO dto) {
		Connection conn = null;

		try {
			conn = DBCPUtil.getConnection();
			int result = equipmentDAO.updateEquipment(conn, dto);
			return result > 0;
		} finally {
			DBCPUtil.close(conn);
		}
	}

	public boolean deleteEquipment(String[] equipmentIds) {
		Connection conn = null;

		try {
			conn = DBCPUtil.getConnection();
			int result = equipmentDAO.deleteEquipment(conn, equipmentIds);
			return result > 0;
		} finally {
			DBCPUtil.close(conn);
		}
	}

public boolean insertEquipment(EquipmentDTO dto) {
    Connection conn = null;
    try {
        conn = DBCPUtil.getConnection();
        int result = equipmentDAO.insertEquipment(conn, dto);
        return result > 0;
    } finally {
        DBCPUtil.close(conn);
    }
}

}