package MasterDataMgmt.ItemMgmt;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

public class ItemMgmtDAO {
	public List<ItemMgmtDTO> getItemList(ItemMgmtSearchDTO dto) {

		List<ItemMgmtDTO> list = new ArrayList<>();

		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {

			Context ctx = new InitialContext();
			DataSource ds = (DataSource) ctx.lookup("java:comp/env/jdbc/oracle");

			conn = ds.getConnection();

			StringBuilder sql = new StringBuilder();
			sql.append("SELECT * FROM ITEM WHERE 1=1 ");

			List<Object> params = new ArrayList<>();

			if (dto.getType() != null && !dto.getType().isEmpty() && !"전체".equals(dto.getType())) {
				sql.append("AND ITEM_TYPE = ? ");
				params.add(dto.getType());
			}

			if (dto.getStartDate() != null && !dto.getStartDate().isEmpty() && dto.getEndDate() != null
					&& !dto.getEndDate().isEmpty()) {

				if (dto.getDateType() == null || dto.getDateType().isEmpty()) {
					dto.setDateType("reg");
				}

				if ("reg".equals(dto.getDateType())) {
					sql.append("AND CREATED_AT BETWEEN ? AND ? ");
				} else {
					sql.append("AND UPDATED_AT BETWEEN ? AND ? ");
				}

				params.add(java.sql.Date.valueOf(dto.getStartDate()));
				params.add(java.sql.Date.valueOf(dto.getEndDate()));
			}

			if (dto.getKeyword() != null && !dto.getKeyword().trim().isEmpty()) {

				String like = "%" + dto.getKeyword() + "%";

				if (dto.getField() == null || dto.getField().isEmpty()) {

					sql.append("AND (");
					sql.append("UPPER(ITEM_CODE) LIKE UPPER(?) OR ");
					sql.append("UPPER(ITEM_NAME) LIKE UPPER(?) OR ");
					sql.append("UPPER(ITEM_TYPE) LIKE UPPER(?) OR ");
					sql.append("UPPER(UNIT) LIKE UPPER(?) OR ");
					sql.append("UPPER(SPEC) LIKE UPPER(?) OR ");
					sql.append("UPPER(SUPPLIER_NAME) LIKE UPPER(?) OR ");
					sql.append("UPPER(USE_YN) LIKE UPPER(?) OR ");
					sql.append("UPPER(REMARK) LIKE UPPER(?) OR ");
					sql.append("TO_CHAR(ITEM_ID) LIKE ? OR ");
					sql.append("TO_CHAR(SAFETY_STOCK) LIKE ? ");
					sql.append(") ");

					for (int i = 0; i < 10; i++) {
						params.add(like);
					}

				} else if ("item_id".equals(dto.getField())) {

					sql.append("AND ITEM_ID = ? ");
					params.add(Integer.parseInt(dto.getKeyword()));

				} else {
					sql.append("AND UPPER(" + dto.getField() + ") LIKE UPPER(?) ");
					params.add(like);
				}
			}

			sql.append("ORDER BY ITEM_ID DESC");

			ps = conn.prepareStatement(sql.toString());

			for (int i = 0; i < params.size(); i++) {
				ps.setObject(i + 1, params.get(i));
			}

			rs = ps.executeQuery();

			while (rs.next()) {

				ItemMgmtDTO dtoItem = new ItemMgmtDTO();

				dtoItem.setItem_id(rs.getInt("ITEM_ID"));
				dtoItem.setItem_code(rs.getString("ITEM_CODE"));
				dtoItem.setItem_name(rs.getString("ITEM_NAME"));
				dtoItem.setItem_type(rs.getString("ITEM_TYPE"));
				dtoItem.setUnit(rs.getString("UNIT"));
				dtoItem.setSpec(rs.getString("SPEC"));
				dtoItem.setSupplier_name(rs.getString("SUPPLIER_NAME"));
				dtoItem.setSafety_stock(rs.getInt("SAFETY_STOCK"));
				dtoItem.setUse_yn(rs.getString("USE_YN"));
				dtoItem.setRemark(rs.getString("REMARK"));
				dtoItem.setCreated_at(rs.getTimestamp("CREATED_AT").toLocalDateTime().toLocalDate());
				dtoItem.setUpdated_at(rs.getTimestamp("UPDATED_AT").toLocalDateTime().toLocalDate());

				list.add(dtoItem);
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

	public void insertItem(ItemMgmtDTO dto) {

		Connection conn = null;
		PreparedStatement ps = null;

		try {
			Context ctx = new InitialContext();
			DataSource dataFactory = (DataSource) ctx.lookup("java:comp/env/jdbc/oracle");
			conn = dataFactory.getConnection();

			String sql = "INSERT INTO ITEM (ITEM_ID, ITEM_CODE, ITEM_NAME, ITEM_TYPE, UNIT, SPEC, SUPPLIER_NAME, SAFETY_STOCK, USE_YN, CREATED_AT, UPDATED_AT) "
					+ "VALUES (SEQ_ITEM.NEXTVAL, ?, ?, ?, ?, ?, ?, ?, ?, SYSDATE, SYSDATE)";

			ps = conn.prepareStatement(sql);

			ps.setString(1, dto.getItem_code());
			ps.setString(2, dto.getItem_name());
			ps.setString(3, dto.getItem_type());
			ps.setString(4, dto.getUnit());
			ps.setString(5, dto.getSpec());
			ps.setString(6, dto.getSupplier_name());
			ps.setInt(7, dto.getSafety_stock());
			ps.setString(8, dto.getUse_yn());

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
		
	        String sql = "DELETE FROM ITEM WHERE ID = ?";
        
            ps = conn.prepareStatement(sql);
            ps.setInt(1, id);

            return ps.executeUpdate(); // 1이면 성공

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
