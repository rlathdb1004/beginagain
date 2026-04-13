package item.service;

import java.sql.Connection;
import java.util.List;

import common.jdbc.DBCPUtil;
import item.dao.ItemDAO;
import item.dto.ItemDTO;

public class ItemService {

    private ItemDAO itemDAO = new ItemDAO();

    public List<ItemDTO> getItemList() {
        Connection conn = null;

        try {
            conn = DBCPUtil.getConnection();
            return itemDAO.selectItemList(conn);
        } finally {
            DBCPUtil.close(conn);
        }
    }

    public ItemDTO getItemById(int itemId) {
        Connection conn = null;

        try {
            conn = DBCPUtil.getConnection();
            return itemDAO.selectItemById(conn, itemId);
        } finally {
            DBCPUtil.close(conn);
        }
    }

    public boolean updateItem(ItemDTO dto) {
        Connection conn = null;

        try {
            conn = DBCPUtil.getConnection();
            int result = itemDAO.updateItem(conn, dto);
            return result > 0;
        } finally {
            DBCPUtil.close(conn);
        }
    }

    public boolean deleteItems(String[] itemIds) {
        Connection conn = null;

        try {
            conn = DBCPUtil.getConnection();
            int result = itemDAO.deleteItems(conn, itemIds);
            return result > 0;
        } finally {
            DBCPUtil.close(conn);
        }
    }

public boolean insertItem(ItemDTO dto) {
    Connection conn = null;
    try {
        conn = DBCPUtil.getConnection();
        int result = itemDAO.insertItem(conn, dto);
        return result > 0;
    } finally {
        DBCPUtil.close(conn);
    }
}

}