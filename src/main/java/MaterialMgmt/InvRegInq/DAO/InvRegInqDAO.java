package MaterialMgmt.InvRegInq.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

import MaterialMgmt.InvRegInq.DTO.InvRegInqDTO;

/*
 * 재고 등록 / 조회 DAO
 * 
 * INVENTORY + ITEM 조인
 * 
 * [목록 조회]
 *  - 검색구분(searchType)
 *  - 검색어(keyword)
 * 
 * [상세 조회]
 *  - inventoryId로 단건 조회
 */
public class InvRegInqDAO {

    // =========================
    // 1. 재고 목록 조회
    // =========================
	public List<InvRegInqDTO> selectInvRegInqList(InvRegInqDTO searchDTO) {

	    List<InvRegInqDTO> list = new ArrayList<InvRegInqDTO>();

	    Connection conn = null;
	    PreparedStatement ps = null;
	    ResultSet rs = null;

	    try {
	        Context ctx = new InitialContext();
	        DataSource dataFactory = (DataSource) ctx.lookup("java:/comp/env/jdbc/oracle");
	        conn = dataFactory.getConnection();

	        System.out.println("현재 접속 DB USER : " + conn.getMetaData().getUserName());

	        String query = "";
	        query += " SELECT ";
	        query += "     iv.INVENTORY_ID, ";
	        query += "     iv.ITEM_ID, ";
	        query += "     i.ITEM_CODE, ";
	        query += "     i.ITEM_NAME, ";
	        query += "     iv.QTY_ON_HAND, ";
	        query += "     iv.SAFETY_STOCK, ";
	        query += "     i.UNIT, ";
	        query += "     iv.REMARK, ";
	        query += "     iv.CREATED_AT, ";
	        query += "     iv.UPDATED_AT ";
	        query += " FROM INVENTORY iv ";
	        query += " INNER JOIN ITEM i ";
	        query += "    ON iv.ITEM_ID = i.ITEM_ID ";
	        query += " WHERE 1 = 1 ";

	        List<Object> paramList = new ArrayList<Object>();

	        // 날짜 조건 추가
	        if (searchDTO.getStartDate() != null) {
	            query += " AND TRUNC(iv.CREATED_AT) >= ? ";
	            paramList.add(searchDTO.getStartDate());
	        }

	        if (searchDTO.getEndDate() != null) {
	            query += " AND TRUNC(iv.CREATED_AT) <= ? ";
	            paramList.add(searchDTO.getEndDate());
	        }

	        String searchType = searchDTO.getSearchType();
	        String keyword = searchDTO.getKeyword();

	        if (keyword != null && !"".equals(keyword.trim())) {
	            String keywordLike = "%" + keyword.trim() + "%";

	            if ("itemCode".equals(searchType)) {
	                query += " AND i.ITEM_CODE LIKE ? ";
	                paramList.add(keywordLike);
	            } else if ("itemName".equals(searchType)) {
	                query += " AND i.ITEM_NAME LIKE ? ";
	                paramList.add(keywordLike);
	            } else {
	                query += " AND (i.ITEM_CODE LIKE ? OR i.ITEM_NAME LIKE ?) ";
	                paramList.add(keywordLike);
	                paramList.add(keywordLike);
	            }
	        }

	        query += " ORDER BY iv.INVENTORY_ID DESC ";

	        System.out.println("재고 목록 조회 SQL : " + query);

	        ps = conn.prepareStatement(query);

	        for (int i = 0; i < paramList.size(); i++) {
	            ps.setObject(i + 1, paramList.get(i));
	        }

	        rs = ps.executeQuery();

	        while (rs.next()) {
	            InvRegInqDTO dto = new InvRegInqDTO();

	            dto.setInventoryId(rs.getInt("INVENTORY_ID"));
	            dto.setItemId(rs.getInt("ITEM_ID"));
	            dto.setItemCode(rs.getString("ITEM_CODE"));
	            dto.setItemName(rs.getString("ITEM_NAME"));
	            dto.setQtyOnHand(rs.getDouble("QTY_ON_HAND"));
	            dto.setSafetyStock(rs.getDouble("SAFETY_STOCK"));
	            dto.setUnit(rs.getString("UNIT"));
	            dto.setRemark(rs.getString("REMARK"));
	            dto.setCreatedAt(rs.getDate("CREATED_AT"));
	            dto.setUpdatedAt(rs.getDate("UPDATED_AT"));

	            list.add(dto);
	        }

	        System.out.println("재고 목록 조회 결과 건수 : " + list.size());

	    } catch (Exception e) {
	        e.printStackTrace();
	    } finally {
	        try { if (rs != null) rs.close(); } catch (Exception e) {}
	        try { if (ps != null) ps.close(); } catch (Exception e) {}
	        try { if (conn != null) conn.close(); } catch (Exception e) {}
	    }

	    return list;
	}

    // =========================
    // 2. 재고 상세 조회
    // =========================
    public InvRegInqDTO selectInvRegInqOne(int inventoryId) {

        InvRegInqDTO dto = null;

        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            // --------------------------------------
            // 1) JNDI로 커넥션 가져오기
            // --------------------------------------
            Context ctx = new InitialContext();
            DataSource dataFactory = (DataSource) ctx.lookup("java:/comp/env/jdbc/oracle");
            conn = dataFactory.getConnection();

            System.out.println("현재 접속 DB USER : " + conn.getMetaData().getUserName());

            // --------------------------------------
            // 2) 상세 조회 SQL 작성
            // --------------------------------------
            String query = "";
            query += " SELECT ";
            query += "     iv.INVENTORY_ID, ";
            query += "     iv.ITEM_ID, ";
            query += "     i.ITEM_CODE, ";
            query += "     i.ITEM_NAME, ";
            query += "     iv.QTY_ON_HAND, ";
            query += "     iv.SAFETY_STOCK, ";
            query += "     i.UNIT, ";
            query += "     iv.REMARK, ";
            query += "     iv.CREATED_AT, ";
            query += "     iv.UPDATED_AT ";
            query += " FROM INVENTORY iv ";
            query += " INNER JOIN ITEM i ";
            query += "    ON iv.ITEM_ID = i.ITEM_ID ";
            query += " WHERE iv.INVENTORY_ID = ? ";

            // --------------------------------------
            // 3) PreparedStatement 생성
            // --------------------------------------
            ps = conn.prepareStatement(query);
            ps.setInt(1, inventoryId);

            // --------------------------------------
            // 4) SQL 실행
            // --------------------------------------
            rs = ps.executeQuery();

            // --------------------------------------
            // 5) 단건 결과 DTO에 담기
            // --------------------------------------
            if (rs.next()) {
                dto = new InvRegInqDTO();

                dto.setInventoryId(rs.getInt("INVENTORY_ID"));
                dto.setItemId(rs.getInt("ITEM_ID"));
                dto.setItemCode(rs.getString("ITEM_CODE"));
                dto.setItemName(rs.getString("ITEM_NAME"));
                dto.setQtyOnHand(rs.getDouble("QTY_ON_HAND"));
                dto.setSafetyStock(rs.getDouble("SAFETY_STOCK"));
                dto.setUnit(rs.getString("UNIT"));
                dto.setRemark(rs.getString("REMARK"));
                dto.setCreatedAt(rs.getDate("CREATED_AT"));
                dto.setUpdatedAt(rs.getDate("UPDATED_AT"));
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try { if (rs != null) rs.close(); } catch (Exception e) {}
            try { if (ps != null) ps.close(); } catch (Exception e) {}
            try { if (conn != null) conn.close(); } catch (Exception e) {}
        }

        return dto;
    }


public int insertInvRegInq(InvRegInqDTO dto) {
    Connection conn = null; PreparedStatement ps = null;
    try {
        Context ctx = new InitialContext(); DataSource dataFactory = (DataSource) ctx.lookup("java:/comp/env/jdbc/oracle"); conn = dataFactory.getConnection();
        String sql = "INSERT INTO INVENTORY (INVENTORY_ID, ITEM_ID, QTY_ON_HAND, SAFETY_STOCK, REMARK, CREATED_AT, UPDATED_AT) VALUES ((SELECT NVL(MAX(INVENTORY_ID),0)+1 FROM INVENTORY), (SELECT ITEM_ID FROM ITEM WHERE ITEM_CODE = ?), ?, ?, ?, SYSDATE, SYSDATE)";
        ps = conn.prepareStatement(sql); ps.setString(1, dto.getItemCode()); ps.setDouble(2, dto.getQtyOnHand()); ps.setDouble(3, dto.getSafetyStock()); ps.setString(4, dto.getRemark()); return ps.executeUpdate();
    } catch (Exception e) { e.printStackTrace(); } finally { try { if (ps != null) ps.close(); } catch (Exception e) {} try { if (conn != null) conn.close(); } catch (Exception e) {} }
    return 0;
}

public int deleteInvRegInq(int[] inventoryIds) {
    Connection conn = null; PreparedStatement ps = null; int result = 0;
    try {
        if (inventoryIds == null || inventoryIds.length == 0) return 0;
        Context ctx = new InitialContext(); DataSource dataFactory = (DataSource) ctx.lookup("java:/comp/env/jdbc/oracle"); conn = dataFactory.getConnection();
        StringBuilder sql = new StringBuilder("DELETE FROM INVENTORY WHERE INVENTORY_ID IN (");
        for (int i = 0; i < inventoryIds.length; i++) { if (i > 0) sql.append(","); sql.append("?"); }
        sql.append(")");
        ps = conn.prepareStatement(sql.toString()); for (int i = 0; i < inventoryIds.length; i++) ps.setInt(i + 1, inventoryIds[i]); result = ps.executeUpdate();
    } catch (Exception e) { e.printStackTrace(); } finally { try { if (ps != null) ps.close(); } catch (Exception e) {} try { if (conn != null) conn.close(); } catch (Exception e) {} }
    return result;
}

}
