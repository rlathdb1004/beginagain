package routing.service;

import java.sql.Connection;
import java.util.List;

import common.jdbc.DBCPUtil;
import routing.dao.RoutingDAO;
import routing.dto.RoutingDTO;

public class RoutingService {

    private RoutingDAO routingDAO = new RoutingDAO();

    public List<RoutingDTO> getRoutingListByItemId(int itemId) {
        Connection conn = null;

        try {
            conn = DBCPUtil.getConnection();
            return routingDAO.selectRoutingListByItemId(conn, itemId);
        } finally {
            DBCPUtil.close(conn);
        }
    }

    public RoutingDTO getRoutingById(int routingId) {
        Connection conn = null;

        try {
            conn = DBCPUtil.getConnection();
            return routingDAO.selectRoutingById(conn, routingId);
        } finally {
            DBCPUtil.close(conn);
        }
    }

    public boolean insertRouting(RoutingDTO dto) {
        Connection conn = null;

        try {
            conn = DBCPUtil.getConnection();
            int result = routingDAO.insertRouting(conn, dto);
            return result > 0;
        } finally {
            DBCPUtil.close(conn);
        }
    }

    public boolean updateRouting(RoutingDTO dto) {
        Connection conn = null;

        try {
            conn = DBCPUtil.getConnection();
            int result = routingDAO.updateRouting(conn, dto);
            return result > 0;
        } finally {
            DBCPUtil.close(conn);
        }
    }

    public boolean deleteRouting(String[] routingIds) {
        Connection conn = null;

        try {
            conn = DBCPUtil.getConnection();
            int result = routingDAO.deleteRouting(conn, routingIds);
            return result > 0;
        } finally {
            DBCPUtil.close(conn);
        }
    }
}
