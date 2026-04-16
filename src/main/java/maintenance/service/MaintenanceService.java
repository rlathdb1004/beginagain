package maintenance.service;

import java.sql.Connection;
import java.util.List;

import common.jdbc.DBCPUtil;
import maintenance.dao.MaintenanceDAO;
import maintenance.dto.MaintenanceDTO;

public class MaintenanceService {

    private MaintenanceDAO maintenanceDAO = new MaintenanceDAO();

    public List<MaintenanceDTO> getMaintenanceList() {
        Connection conn = null;
        try {
            conn = DBCPUtil.getConnection();
            return maintenanceDAO.selectMaintenanceList(conn);
        } finally {
            DBCPUtil.close(conn);
        }
    }

    public MaintenanceDTO getMaintenanceById(int maintenanceId) {
        Connection conn = null;
        try {
            conn = DBCPUtil.getConnection();
            return maintenanceDAO.selectMaintenanceById(conn, maintenanceId);
        } finally {
            DBCPUtil.close(conn);
        }
    }

    public boolean insertMaintenance(MaintenanceDTO dto) {
        Connection conn = null;
        try {
            conn = DBCPUtil.getConnection();
            return maintenanceDAO.insertMaintenance(conn, dto) > 0;
        } finally {
            DBCPUtil.close(conn);
        }
    }

    public boolean updateMaintenance(MaintenanceDTO dto) {
        Connection conn = null;
        try {
            conn = DBCPUtil.getConnection();
            return maintenanceDAO.updateMaintenance(conn, dto) > 0;
        } finally {
            DBCPUtil.close(conn);
        }
    }

    public boolean deleteMaintenance(String[] maintenanceIds) {
        Connection conn = null;
        try {
            conn = DBCPUtil.getConnection();
            return maintenanceDAO.deleteMaintenance(conn, maintenanceIds) > 0;
        } finally {
            DBCPUtil.close(conn);
        }
    }
}