package MasterDataMgmt.BOMManagement;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

import MasterDataMgmt.ItemMgmt.ItemMgmtDTO;
import MasterDataMgmt.ItemMgmt.ItemMgmtSearchDTO;

public class BOMMgmtDAO {
	public List<BOMMgmtDTO> getBOMList(BOMMgmtSearchDTO dto) {

		List<BOMMgmtDTO> list = new ArrayList<>();

		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {

			Context ctx = new InitialContext();
			DataSource ds = (DataSource) ctx.lookup("java:comp/env/jdbc/oracle");

			conn = ds.getConnection();
			
			StringBuilder sql = new StringBuilder();
			sql.append("SELECT ");
			sql.append(" b.BOM_ID, ");
			sql.append(" i.ITEM_CODE, ");
			sql.append(" i.ITEM_NAME, ");
			sql.append(" b.REMARK, ");
			sql.append(" b.CREATED_AT ");
			sql.append("FROM BOM b ");
			sql.append("LEFT JOIN ITEM i ON b.ITEM_ID = i.ITEM_ID ");			
			sql.append("WHERE 1=1 ");

			List<Object> params = new ArrayList<>();

			// 날짜 조건
			if (dto.getStartDate() != null && !dto.getStartDate().isEmpty()
			        && dto.getEndDate() != null && !dto.getEndDate().isEmpty()) {

			    sql.append("AND b.CREATED_AT BETWEEN ? AND ? ");
			    params.add(java.sql.Date.valueOf(dto.getStartDate()));
			    params.add(java.sql.Date.valueOf(dto.getEndDate()));
			}

			// 검색
			if (dto.getKeyword() != null && !dto.getKeyword().trim().isEmpty()) {

			    String like = "%" + dto.getKeyword() + "%";

			    if (dto.getField() == null || dto.getField().isEmpty()) {

			        sql.append("AND (");
			        sql.append("UPPER(i.ITEM_CODE) LIKE UPPER(?) OR ");
			        sql.append("UPPER(i.ITEM_NAME) LIKE UPPER(?) ");
			        sql.append(") ");

			        params.add(like);
			        params.add(like);

			    } else if ("item_code".equals(dto.getField())) {

			        sql.append("AND UPPER(i.ITEM_CODE) LIKE UPPER(?) ");
			        params.add(like);

			    } else if ("item_name".equals(dto.getField())) {

			        sql.append("AND UPPER(i.ITEM_NAME) LIKE UPPER(?) ");
			        params.add(like);
			    }
			}

			sql.append("ORDER BY b.BOM_ID DESC");

			ps = conn.prepareStatement(sql.toString());

			for (int i = 0; i < params.size(); i++) {
				ps.setObject(i + 1, params.get(i));
			}

			rs = ps.executeQuery();

			while (rs.next()) {

				BOMMgmtDTO dtoBOM = new BOMMgmtDTO();

				dtoBOM.setBOM_id(rs.getInt("BOM_ID"));
			    dtoBOM.setItem_code(rs.getString("ITEM_CODE"));
			    dtoBOM.setItem_name(rs.getString("ITEM_NAME"));
			    dtoBOM.setRemark(rs.getString("REMARK"));
			    dtoBOM.setCreated_at(rs.getTimestamp("CREATED_AT").toLocalDateTime().toLocalDate());

				list.add(dtoBOM);
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (rs != null)
					rs.close();
			} catch (Exception e) {
			}
			try {
				if (ps != null)
					ps.close();
			} catch (Exception e) {
			}
			try {
				if (conn != null)
					conn.close();
			} catch (Exception e) {
			}
		}

		return list;
	}

	public void insertBOM(BOMMgmtDTO dto) {

		Connection conn = null;
		PreparedStatement ps = null;

		try {
			Context ctx = new InitialContext();
			DataSource dataFactory = (DataSource) ctx.lookup("java:comp/env/jdbc/oracle");
			conn = dataFactory.getConnection();

			String sql = "INSERT INTO BOM (BOM_ID, ITEM_CODE, VERSION_NO, USE_YN, REMARK, CREATED_AT, UPDATED_AT) "
					+ "VALUES (SEQ_BOM.NEXTVAL, ?, 1, ?, ?,  SYSDATE, SYSDATE)";

			ps = conn.prepareStatement(sql);

			ps.setString(1, dto.getItem_code());
//			ps.setInt(2, dto.getVersion_no());			
			ps.setString(2, "Y");
			ps.setString(3, dto.getRemark());			

			ps.executeUpdate();

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (ps != null)
					ps.close();
			} catch (Exception e) {
			}
			try {
				if (conn != null)
					conn.close();
			} catch (Exception e) {
			}
		}
	}

	public int deleteItem(int id) {

	    Connection conn = null;
	    PreparedStatement ps = null;

	    try {
	        Context ctx = new InitialContext();
	        DataSource dataFactory = (DataSource) ctx.lookup("java:comp/env/jdbc/oracle");

	        conn = dataFactory.getConnection();
		
	        String sql = "DELETE FROM BOM WHERE BOM_ID = ?";
        
            ps = conn.prepareStatement(sql);
            ps.setInt(1, id);

            return ps.executeUpdate(); 

	    } catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (ps != null)
					ps.close();
			} catch (Exception e) {
			}
			try {
				if (conn != null)
					conn.close();
			} catch (Exception e) {
			}
		}
        return 0;
	}
}
