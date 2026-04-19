package MaterialMgmt.IORegInq.DAO;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

import MaterialMgmt.IORegInq.DTO.IORegInqDTO;
import item.dto.ItemDTO;

public class IORegInqDAO {

    private Connection getConnection() throws Exception {
        Context ctx = new InitialContext();
        DataSource dataFactory = (DataSource) ctx.lookup("java:/comp/env/jdbc/oracle");
        return dataFactory.getConnection();
    }

    public List<IORegInqDTO> selectIORegInqList(IORegInqDTO searchDTO) {
        List<IORegInqDTO> list = new ArrayList<IORegInqDTO>();
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            conn = getConnection();

            StringBuilder query = new StringBuilder();
            query.append(" SELECT mi.INOUT_ID, mi.ITEM_ID, i.ITEM_CODE, i.ITEM_NAME, i.ITEM_TYPE, ");
            query.append("        mi.INOUT_TYPE, mi.QTY, mi.UNIT, mi.INOUT_DATE, mi.STATUS, mi.REMARK, ");
            query.append("        mi.CREATED_AT, mi.UPDATED_AT, NVL(iv.QTY_ON_HAND, 0) AS CURRENT_STOCK, i.ITEM_TYPE ");
            query.append("   FROM MATERIAL_INOUT mi ");
            query.append("   JOIN ITEM i ON mi.ITEM_ID = i.ITEM_ID ");
            query.append("   LEFT JOIN INVENTORY iv ON mi.ITEM_ID = iv.ITEM_ID ");
            query.append("  WHERE NVL(mi.USE_YN, 'Y') = 'Y' ");

            List<Object> paramList = new ArrayList<Object>();

            if (searchDTO.getInoutType() != null && !"".equals(searchDTO.getInoutType())
                    && !"전체".equals(searchDTO.getInoutType())) {
                query.append(" AND mi.INOUT_TYPE = ? ");
                paramList.add(searchDTO.getInoutType());
            }
            if (searchDTO.getStartDate() != null) {
                query.append(" AND mi.INOUT_DATE >= ? ");
                paramList.add(searchDTO.getStartDate());
            }
            if (searchDTO.getEndDate() != null) {
                query.append(" AND mi.INOUT_DATE <= ? ");
                paramList.add(searchDTO.getEndDate());
            }
            if (searchDTO.getKeyword() != null && !"".equals(searchDTO.getKeyword().trim())) {
                String keyword = "%" + searchDTO.getKeyword().trim() + "%";
                if ("itemCode".equals(searchDTO.getSearchType())) {
                    query.append(" AND i.ITEM_CODE LIKE ? ");
                    paramList.add(keyword);
                } else if ("itemName".equals(searchDTO.getSearchType())) {
                    query.append(" AND i.ITEM_NAME LIKE ? ");
                    paramList.add(keyword);
                } else {
                    query.append(" AND (i.ITEM_CODE LIKE ? OR i.ITEM_NAME LIKE ?) ");
                    paramList.add(keyword);
                    paramList.add(keyword);
                }
            }
            query.append(" ORDER BY mi.INOUT_DATE DESC, mi.INOUT_ID DESC ");

            ps = conn.prepareStatement(query.toString());
            bindParams(ps, paramList);
            rs = ps.executeQuery();

            while (rs.next()) {
                IORegInqDTO dto = new IORegInqDTO();
                dto.setInoutId(rs.getInt("INOUT_ID"));
                dto.setItemId(rs.getInt("ITEM_ID"));
                dto.setItemCode(rs.getString("ITEM_CODE"));
                dto.setItemName(rs.getString("ITEM_NAME"));
                dto.setInoutType(rs.getString("INOUT_TYPE"));
                dto.setQty(rs.getDouble("QTY"));
                dto.setUnit(rs.getString("UNIT"));
                dto.setInoutDate(rs.getDate("INOUT_DATE"));
                dto.setStatus(rs.getString("STATUS"));
                dto.setRemark(rs.getString("REMARK"));
                dto.setCreatedAt(rs.getDate("CREATED_AT"));
                dto.setUpdatedAt(rs.getDate("UPDATED_AT"));
                dto.setCurrentStock(rs.getDouble("CURRENT_STOCK"));
                dto.setItemType(rs.getString("ITEM_TYPE"));
                list.add(dto);
            }
        } catch (Exception e) {
            throw new RuntimeException("입출고 목록 조회 실패", e);
        } finally {
            close(rs); close(ps); close(conn);
        }
        return list;
    }

    public IORegInqDTO selectIORegInqOne(int inoutId) {
        IORegInqDTO dto = null;
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = getConnection();
            String sql = ""
                    + "SELECT mi.INOUT_ID, mi.ITEM_ID, i.ITEM_CODE, i.ITEM_NAME, i.ITEM_TYPE, "
                    + "       mi.INOUT_TYPE, mi.QTY, mi.UNIT, mi.INOUT_DATE, mi.STATUS, mi.REMARK, "
                    + "       mi.CREATED_AT, mi.UPDATED_AT, NVL(iv.QTY_ON_HAND, 0) AS CURRENT_STOCK, i.ITEM_TYPE "
                    + "  FROM MATERIAL_INOUT mi "
                    + "  JOIN ITEM i ON mi.ITEM_ID = i.ITEM_ID "
                    + "  LEFT JOIN INVENTORY iv ON mi.ITEM_ID = iv.ITEM_ID "
                    + " WHERE mi.INOUT_ID = ? AND NVL(mi.USE_YN, 'Y') = 'Y' ";
            ps = conn.prepareStatement(sql);
            ps.setInt(1, inoutId);
            rs = ps.executeQuery();
            if (rs.next()) {
                dto = new IORegInqDTO();
                dto.setInoutId(rs.getInt("INOUT_ID"));
                dto.setItemId(rs.getInt("ITEM_ID"));
                dto.setItemCode(rs.getString("ITEM_CODE"));
                dto.setItemName(rs.getString("ITEM_NAME"));
                dto.setInoutType(rs.getString("INOUT_TYPE"));
                dto.setQty(rs.getDouble("QTY"));
                dto.setUnit(rs.getString("UNIT"));
                dto.setInoutDate(rs.getDate("INOUT_DATE"));
                dto.setStatus(rs.getString("STATUS"));
                dto.setRemark(rs.getString("REMARK"));
                dto.setCreatedAt(rs.getDate("CREATED_AT"));
                dto.setUpdatedAt(rs.getDate("UPDATED_AT"));
                dto.setCurrentStock(rs.getDouble("CURRENT_STOCK"));
                dto.setItemType(rs.getString("ITEM_TYPE"));
            }
        } catch (Exception e) {
            throw new RuntimeException("입출고 상세 조회 실패", e);
        } finally {
            close(rs); close(ps); close(conn);
        }
        return dto;
    }

    public List<ItemDTO> selectMaterialItemList() {
        List<ItemDTO> list = new ArrayList<ItemDTO>();
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = getConnection();
            String sql = ""
                    + "SELECT i.ITEM_ID, i.ITEM_CODE, i.ITEM_NAME, i.ITEM_TYPE, i.UNIT, "
                    + "       NVL(iv.QTY_ON_HAND, 0) AS CURRENT_STOCK "
                    + "  FROM ITEM i "
                    + "  LEFT JOIN INVENTORY iv ON i.ITEM_ID = iv.ITEM_ID "
                    + " WHERE NVL(i.USE_YN, 'Y') = 'Y' "
                    + "   AND i.ITEM_TYPE IN ('원자재', '완제품') "
                    + " ORDER BY i.ITEM_CODE ";
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                ItemDTO dto = new ItemDTO();
                dto.setItemId(rs.getInt("ITEM_ID"));
                dto.setItemCode(rs.getString("ITEM_CODE"));
                dto.setItemName(rs.getString("ITEM_NAME"));
                dto.setItemType(rs.getString("ITEM_TYPE"));
                dto.setUnit(rs.getString("UNIT"));
                dto.setSafetyStock(rs.getDouble("CURRENT_STOCK")); // currentStock 임시 전달용
                list.add(dto);
            }
        } catch (Exception e) {
            throw new RuntimeException("자재 품목 목록 조회 실패", e);
        } finally {
            close(rs); close(ps); close(conn);
        }
        return list;
    }

    public ItemDTO selectMaterialItemById(int itemId) {
        ItemDTO dto = null;
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = getConnection();
            String sql = ""
                    + "SELECT i.ITEM_ID, i.ITEM_CODE, i.ITEM_NAME, i.ITEM_TYPE, i.UNIT, "
                    + "       NVL(iv.QTY_ON_HAND, 0) AS CURRENT_STOCK "
                    + "  FROM ITEM i "
                    + "  LEFT JOIN INVENTORY iv ON i.ITEM_ID = iv.ITEM_ID "
                    + " WHERE i.ITEM_ID = ? "
                    + "   AND NVL(i.USE_YN, 'Y') = 'Y' "
                    + "   AND i.ITEM_TYPE IN ('원자재', '완제품') ";
            ps = conn.prepareStatement(sql);
            ps.setInt(1, itemId);
            rs = ps.executeQuery();
            if (rs.next()) {
                dto = new ItemDTO();
                dto.setItemId(rs.getInt("ITEM_ID"));
                dto.setItemCode(rs.getString("ITEM_CODE"));
                dto.setItemName(rs.getString("ITEM_NAME"));
                dto.setItemType(rs.getString("ITEM_TYPE"));
                dto.setUnit(rs.getString("UNIT"));
                dto.setSafetyStock(rs.getDouble("CURRENT_STOCK")); // currentStock 임시 전달용
            }
        } catch (Exception e) {
            throw new RuntimeException("자재 품목 조회 실패", e);
        } finally {
            close(rs); close(ps); close(conn);
        }
        return dto;
    }

    
    public int insertIORegInq(IORegInqDTO dto) {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = getConnection();
            conn.setAutoCommit(false);

            int inoutId = nextInoutId(conn);
            String sql = ""
                    + "INSERT INTO MATERIAL_INOUT (INOUT_ID, ITEM_ID, INOUT_TYPE, QTY, UNIT, INOUT_DATE, STATUS, REMARK, CREATED_AT, UPDATED_AT) "
                    + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, SYSDATE, SYSDATE)";
            ps = conn.prepareStatement(sql);
            ps.setInt(1, inoutId);
            ps.setInt(2, dto.getItemId());
            ps.setString(3, dto.getInoutType());
            ps.setDouble(4, dto.getQty());
            ps.setString(5, dto.getUnit());
            ps.setDate(6, dto.getInoutDate());
            ps.setString(7, dto.getStatus());
            ps.setString(8, dto.getRemark());
            int result = ps.executeUpdate();
            close(ps); ps = null;

            if (result > 0 && "완료".equals(dto.getStatus())) {
                applyInventoryDelta(conn, dto.getItemId(), calculateDelta(dto.getInoutType(), dto.getQty()));
            }

            conn.commit();
            return result;
        } catch (Exception e) {
            rollback(conn);
            throw new RuntimeException("입출고 등록 실패", e);
        } finally {
            close(rs); close(ps); resetAutoCommit(conn); close(conn);
        }
    }

    public int updateIORegInq(IORegInqDTO dto) {
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = getConnection();
            conn.setAutoCommit(false);

            IORegInqDTO origin = selectIORegInqCore(conn, dto.getInoutId());
            if (origin == null) throw new RuntimeException("존재하지 않는 입출고 정보입니다.");

            String sql = ""
                    + "UPDATE MATERIAL_INOUT "
                    + "   SET INOUT_TYPE = ?, QTY = ?, INOUT_DATE = ?, STATUS = ?, REMARK = ?, UPDATED_AT = SYSDATE "
                    + " WHERE INOUT_ID = ?";
            ps = conn.prepareStatement(sql);
            ps.setString(1, dto.getInoutType());
            ps.setDouble(2, dto.getQty());
            ps.setDate(3, dto.getInoutDate());
            ps.setString(4, dto.getStatus());
            ps.setString(5, dto.getRemark());
            ps.setInt(6, dto.getInoutId());
            int result = ps.executeUpdate();
            close(ps); ps = null;

            if (result > 0 && !"완료".equals(origin.getStatus()) && "완료".equals(dto.getStatus())) {
                applyInventoryDelta(conn, origin.getItemId(), calculateDelta(dto.getInoutType(), dto.getQty()));
            }

            conn.commit();
            return result;
        } catch (Exception e) {
            rollback(conn);
            throw new RuntimeException("입출고 수정 실패", e);
        } finally {
            close(ps); resetAutoCommit(conn); close(conn);
        }
    }

    private int nextInoutId(Connection conn) throws SQLException {
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = conn.prepareStatement("SELECT NVL(MAX(INOUT_ID),0)+1 AS NEXT_ID FROM MATERIAL_INOUT");
            rs = ps.executeQuery();
            return rs.next() ? rs.getInt("NEXT_ID") : 1;
        } finally {
            close(rs); close(ps);
        }
    }

    private IORegInqDTO selectIORegInqCore(Connection conn, int inoutId) throws SQLException {
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            String sql = "SELECT INOUT_ID, ITEM_ID, INOUT_TYPE, QTY, STATUS FROM MATERIAL_INOUT WHERE INOUT_ID = ?";
            ps = conn.prepareStatement(sql);
            ps.setInt(1, inoutId);
            rs = ps.executeQuery();
            if (rs.next()) {
                IORegInqDTO dto = new IORegInqDTO();
                dto.setInoutId(rs.getInt("INOUT_ID"));
                dto.setItemId(rs.getInt("ITEM_ID"));
                dto.setInoutType(rs.getString("INOUT_TYPE"));
                dto.setQty(rs.getDouble("QTY"));
                dto.setStatus(rs.getString("STATUS"));
                return dto;
            }
            return null;
        } finally {
            close(rs); close(ps);
        }
    }

    private void applyInventoryDelta(Connection conn, int itemId, double delta) throws SQLException {
        double currentStock = selectCurrentStockByItemId(conn, itemId);
        double nextStock = currentStock + delta;
        if (nextStock < 0) {
            throw new RuntimeException("재고가 부족하여 완료 처리할 수 없습니다.");
        }

        PreparedStatement ps = null;
        try {
            ps = conn.prepareStatement("UPDATE INVENTORY SET QTY_ON_HAND = ?, UPDATED_AT = SYSDATE WHERE ITEM_ID = ?");
            ps.setDouble(1, nextStock);
            ps.setInt(2, itemId);
            int updated = ps.executeUpdate();
            close(ps); ps = null;

            if (updated == 0) {
                ps = conn.prepareStatement(
                    "INSERT INTO INVENTORY (INVENTORY_ID, ITEM_ID, QTY_ON_HAND, SAFETY_STOCK, REMARK, CREATED_AT, UPDATED_AT) "
                  + "VALUES ((SELECT NVL(MAX(INVENTORY_ID),0)+1 FROM INVENTORY), ?, ?, 0, NULL, SYSDATE, SYSDATE)");
                ps.setInt(1, itemId);
                ps.setDouble(2, nextStock);
                ps.executeUpdate();
            }
        } finally {
            close(ps);
        }
    }

    private double selectCurrentStockByItemId(Connection conn, int itemId) throws SQLException {
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = conn.prepareStatement("SELECT NVL(QTY_ON_HAND, 0) AS QTY_ON_HAND FROM INVENTORY WHERE ITEM_ID = ?");
            ps.setInt(1, itemId);
            rs = ps.executeQuery();
            return rs.next() ? rs.getDouble("QTY_ON_HAND") : 0.0;
        } finally {
            close(rs); close(ps);
        }
    }

    private double calculateDelta(String inoutType, double qty) {
        if ("입고".equals(inoutType) || "반품".equals(inoutType)) return qty;
        if ("출고".equals(inoutType) || "폐기".equals(inoutType)) return -qty;
        throw new RuntimeException("허용되지 않은 입출고구분입니다.");
    }

    private void rollback(Connection conn) {
        if (conn == null) return;
        try { conn.rollback(); } catch (Exception e) { e.printStackTrace(); }
    }

    private void resetAutoCommit(Connection conn) {
        if (conn == null) return;
        try { conn.setAutoCommit(true); } catch (Exception e) { e.printStackTrace(); }
    }


    public int deleteIORegInq(int[] inoutIds) {
        Connection conn = null;
        PreparedStatement ps = null;
        int result = 0;
        try {
            if (inoutIds == null || inoutIds.length == 0) return 0;
            conn = getConnection();
            StringBuilder sql = new StringBuilder("UPDATE MATERIAL_INOUT SET USE_YN = 'N', UPDATED_AT = SYSDATE WHERE INOUT_ID IN (");
            for (int i = 0; i < inoutIds.length; i++) {
                if (i > 0) sql.append(",");
                sql.append("?");
            }
            sql.append(")");
            ps = conn.prepareStatement(sql.toString());
            for (int i = 0; i < inoutIds.length; i++) ps.setInt(i + 1, inoutIds[i]);
            result = ps.executeUpdate();
        } catch (Exception e) {
            throw new RuntimeException("입출고 삭제 실패", e);
        } finally {
            close(ps); close(conn);
        }
        return result;
    }

    public int countCompletedByIds(int[] inoutIds) {
        if (inoutIds == null || inoutIds.length == 0) return 0;
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = getConnection();
            StringBuilder sql = new StringBuilder("SELECT COUNT(*) CNT FROM MATERIAL_INOUT WHERE NVL(USE_YN, 'Y') = 'Y' AND STATUS = '완료' AND INOUT_ID IN (");
            for (int i = 0; i < inoutIds.length; i++) {
                if (i > 0) sql.append(",");
                sql.append("?");
            }
            sql.append(")");
            ps = conn.prepareStatement(sql.toString());
            for (int i = 0; i < inoutIds.length; i++) ps.setInt(i + 1, inoutIds[i]);
            rs = ps.executeQuery();
            return rs.next() ? rs.getInt("CNT") : 0;
        } catch (Exception e) {
            throw new RuntimeException("완료 상태 입출고 조회 실패", e);
        } finally {
            close(rs); close(ps); close(conn);
        }
    }

    public IORegInqDTO selectIORegInqCore(int inoutId) {
        Connection conn = null;
        try {
            conn = getConnection();
            return selectIORegInqCore(conn, inoutId);
        } catch (Exception e) {
            throw new RuntimeException("입출고 기본 정보 조회 실패", e);
        } finally {
            close(conn);
        }
    }

    public double selectCurrentStockByItemId(int itemId) {
        Connection conn = null;
        try {
            conn = getConnection();
            return selectCurrentStockByItemId(conn, itemId);
        } catch (Exception e) {
            throw new RuntimeException("현재고 조회 실패", e);
        } finally {
            close(conn);
        }
    }

    private void bindParams(PreparedStatement ps, List<Object> params) throws SQLException {
        for (int i = 0; i < params.size(); i++) {
            Object obj = params.get(i);
            if (obj instanceof String) ps.setString(i + 1, (String) obj);
            else if (obj instanceof Date) ps.setDate(i + 1, (Date) obj);
        }
    }

    private void close(AutoCloseable c) {
        if (c == null) return;
        try { c.close(); } catch (Exception e) { e.printStackTrace(); }
    }
}
