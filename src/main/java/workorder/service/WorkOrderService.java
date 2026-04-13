package workorder.service;

import java.sql.Connection;
import java.util.List;

import common.jdbc.DBCPUtil;
import workorder.dao.WorkOrderDAO;
import workorder.dto.WorkOrderDTO;

public class WorkOrderService {

	private WorkOrderDAO workOrderDAO = new WorkOrderDAO();

	public List<WorkOrderDTO> getWorkOrderList() {
		Connection conn = null;

		try {
			conn = DBCPUtil.getConnection();
			return workOrderDAO.selectWorkOrderList(conn);
		} finally {
			DBCPUtil.close(conn);
		}
	}

	public WorkOrderDTO getWorkOrderById(int workOrderId) {
		Connection conn = null;

		try {
			conn = DBCPUtil.getConnection();
			return workOrderDAO.selectWorkOrderById(conn, workOrderId);
		} finally {
			DBCPUtil.close(conn);
		}
	}

	public boolean updateWorkOrder(WorkOrderDTO dto) {
		Connection conn = null;

		try {
			conn = DBCPUtil.getConnection();
			int result = workOrderDAO.updateWorkOrder(conn, dto);
			return result > 0;
		} finally {
			DBCPUtil.close(conn);
		}
	}

	public boolean deleteWorkOrders(String[] workOrderIds) {
		Connection conn = null;

		try {
			conn = DBCPUtil.getConnection();
			int result = workOrderDAO.deleteWorkOrders(conn, workOrderIds);
			return result > 0;
		} finally {
			DBCPUtil.close(conn);
		}
	}

	public boolean insertWorkOrder(WorkOrderDTO dto) {
		Connection conn = null;

		try {
			conn = DBCPUtil.getConnection();
			int result = workOrderDAO.insertWorkOrder(conn, dto);
			return result > 0;
		} finally {
			DBCPUtil.close(conn);
		}
	}
}