package downtime.service;

import java.sql.Connection;
import java.util.List;

import common.jdbc.DBCPUtil;
import downtime.dao.DowntimeDAO;
import downtime.dto.DowntimeDTO;

public class DowntimeService {

    private DowntimeDAO downtimeDAO = new DowntimeDAO();

    public List<DowntimeDTO> getDowntimeList() {
        Connection conn = null;
        try {
            conn = DBCPUtil.getConnection();
            return downtimeDAO.selectDowntimeList(conn);
        } finally {
            DBCPUtil.close(conn);
        }
    }

    public DowntimeDTO getDowntimeById(int failureActionId) {
        Connection conn = null;
        try {
            conn = DBCPUtil.getConnection();
            return downtimeDAO.selectDowntimeById(conn, failureActionId);
        } finally {
            DBCPUtil.close(conn);
        }
    }
}