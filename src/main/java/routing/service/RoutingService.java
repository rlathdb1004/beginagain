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
}