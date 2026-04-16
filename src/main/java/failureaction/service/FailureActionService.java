package failureaction.service;

import java.sql.Connection;
import java.util.List;

import common.jdbc.DBCPUtil;
import failureaction.dao.FailureActionDAO;
import failureaction.dto.FailureActionDTO;

public class FailureActionService {

    private FailureActionDAO failureActionDAO = new FailureActionDAO();

    public List<FailureActionDTO> getFailureActionListByMaintenanceId(int maintenanceId) {
        Connection conn = null;
        try {
            conn = DBCPUtil.getConnection();
            return failureActionDAO.selectFailureActionListByMaintenanceId(conn, maintenanceId);
        } finally {
            DBCPUtil.close(conn);
        }
    }

    public FailureActionDTO getFailureActionById(int failureActionId) {
        Connection conn = null;
        try {
            conn = DBCPUtil.getConnection();
            return failureActionDAO.selectFailureActionById(conn, failureActionId);
        } finally {
            DBCPUtil.close(conn);
        }
    }

    public boolean insertFailureAction(FailureActionDTO dto) {
        Connection conn = null;
        try {
            conn = DBCPUtil.getConnection();
            return failureActionDAO.insertFailureAction(conn, dto) > 0;
        } finally {
            DBCPUtil.close(conn);
        }
    }

    public boolean updateFailureAction(FailureActionDTO dto) {
        Connection conn = null;
        try {
            conn = DBCPUtil.getConnection();
            return failureActionDAO.updateFailureAction(conn, dto) > 0;
        } finally {
            DBCPUtil.close(conn);
        }
    }

    public boolean deleteFailureAction(String[] failureActionIds) {
        Connection conn = null;
        try {
            conn = DBCPUtil.getConnection();
            return failureActionDAO.deleteFailureAction(conn, failureActionIds) > 0;
        } finally {
            DBCPUtil.close(conn);
        }
    }
}