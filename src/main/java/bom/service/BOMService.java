package bom.service;

import java.sql.Connection;
import java.util.List;

import bom.dao.BOMDAO;
import bom.dto.BOMDTO;
import common.jdbc.DBCPUtil;

public class BOMService {

    private BOMDAO bomDAO = new BOMDAO();

    public List<BOMDTO> getBomListByItemId(int itemId) {
        Connection conn = null;

        try {
            conn = DBCPUtil.getConnection();
            return bomDAO.selectBomListByItemId(conn, itemId);
        } finally {
            DBCPUtil.close(conn);
        }
    }
}