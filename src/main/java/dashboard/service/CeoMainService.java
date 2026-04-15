package dashboard.service;

import java.sql.Connection;

import common.jdbc.DBCPUtil;
import dashboard.dao.DashboardDAO;
import dashboard.dto.DashboardDTO;

public class CeoMainService {

	private DashboardDAO dashboardDAO = new DashboardDAO();

	public DashboardDTO getDashboardData() {
		Connection conn = null;

		try {
			conn = DBCPUtil.getConnection();
			return dashboardDAO.selectDashboardData(conn);
		} finally {
			DBCPUtil.close(conn);
		}
	}
}