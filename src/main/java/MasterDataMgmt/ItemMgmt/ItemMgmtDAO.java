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
	public List<ItemMgmtDTO> getItemList() {

		List<ItemMgmtDTO> list = new ArrayList<>();

		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {

			Context ctx = new InitialContext();
			DataSource dataFactory = (DataSource) ctx.lookup("java:comp/env/jdbc/oracle");

			conn = dataFactory.getConnection();

			String query = "select * from ITEM";
			ps = conn.prepareStatement(query);
			rs = ps.executeQuery();

			while (rs.next()) {

				int item_id = rs.getInt("ITEM_ID");
				String item_code = rs.getString("ITEM_CODE");
				String item_name = rs.getString("ITEM_NAME");
				String item_type = rs.getString("ITEM_TYPE");
				String unit = rs.getString("UNIT");
				String spec = rs.getString("SPEC");
				String supplier_name = rs.getString("SUPPLIER_NAME");
				int safety_stock = rs.getInt("SAFETY_STOCK");
				String use_yn = rs.getString("USE_YN");
				String remark = rs.getString("REMARK");
				LocalDate created_at = rs.getTimestamp("CREATED_AT").toLocalDateTime().toLocalDate();
				LocalDate updated_at = rs.getTimestamp("UPDATED_AT").toLocalDateTime().toLocalDate();

				ItemMgmtDTO itemDTO = new ItemMgmtDTO();
				itemDTO.setItem_id(item_id);
				itemDTO.setItem_code(item_code);
				itemDTO.setItem_name(item_name);
				itemDTO.setItem_type(item_type);
				itemDTO.setUnit(unit);
				itemDTO.setSpec(spec);
				itemDTO.setSupplier_name(supplier_name);
				itemDTO.setSafety_stock(safety_stock);
				itemDTO.setUse_yn(use_yn);
				itemDTO.setRemark(remark);
				itemDTO.setCreated_at(created_at);
				itemDTO.setUpdated_at(updated_at);

				list.add(itemDTO);
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (ps != null) {
				try {
					ps.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}

			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {

					e.printStackTrace();
				}
			}
		}
		return list;
	}

	public List<ItemMgmtDTO> search(String type, String dateType, String startDate, String endDate, String field,
			String keyword) {

		List<ItemMgmtDTO> list = new ArrayList<>();

		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			Context ctx = new InitialContext();
			DataSource dataFactory = (DataSource) ctx.lookup("java:comp/env/jdbc/oracle");

			conn = dataFactory.getConnection();

			StringBuilder sql = new StringBuilder();
			sql.append("SELECT * FROM ITEM WHERE 1=1 ");

			List<Object> params = new ArrayList<>();

			//분류
			if (type != null && !type.equals("") && !type.equals("전체")) {
				sql.append("AND ITEM_TYPE = ? ");
				params.add(type);
			}

			// 날짜
			if (startDate != null && !startDate.equals("") && endDate != null && !endDate.equals("")) {

				if ("reg".equals(dateType)) {
					sql.append("AND CREATED_AT BETWEEN ? AND ? ");
				} else if ("mod".equals(dateType)) {
					sql.append("AND UPDATED_AT BETWEEN ? AND ? ");
				}

				params.add(startDate);
				params.add(endDate);
			}

			// 검색
			if (keyword != null && !keyword.trim().equals("")) {

				if (field == null || field.equals("")) {

					sql.append("AND (");

			        sql.append("UPPER(ITEM_CODE) LIKE UPPER(?) OR ");
			        sql.append("UPPER(ITEM_NAME) LIKE UPPER(?) OR ");
			        sql.append("UPPER(ITEM_TYPE) LIKE UPPER(?) OR ");
			        sql.append("UPPER(UNIT) LIKE UPPER(?) OR ");
			        sql.append("UPPER(SPEC) LIKE UPPER(?) OR ");
			        sql.append("UPPER(SUPPLIER_NAME) LIKE UPPER(?) OR ");
			        sql.append("UPPER(USE_YN) LIKE UPPER(?) OR ");
			        sql.append("UPPER(REMARK) LIKE UPPER(?) OR ");

			        // 숫자
			        sql.append("TO_CHAR(ITEM_ID) LIKE ? OR ");
			        sql.append("TO_CHAR(SAFETY_STOCK) LIKE ? OR ");

			        // 날짜
			        sql.append("TO_CHAR(CREATED_AT, 'YYYY-MM-DD') LIKE ? OR ");
			        sql.append("TO_CHAR(UPDATED_AT, 'YYYY-MM-DD') LIKE ? ");

			        sql.append(") ");

			        String like = "%" + keyword + "%";

			        // 문자열
			        params.add(like); // item_code
			        params.add(like); // item_name
			        params.add(like); // item_type
			        params.add(like); // unit
			        params.add(like); // spec
			        params.add(like); // supplier_name
			        params.add(like); // use_yn
			        params.add(like); // remark

			        // 숫자
			        params.add(like); // item_id
			        params.add(like); // safety_stock

			        // 날짜
			        params.add(like); // created_at
			        params.add(like); // updated_at

			    } 
			    
			    else if ("item_id".equals(field)) {
			        sql.append("AND ITEM_ID = ? ");
			        params.add(Integer.parseInt(keyword));

			    } 
			    else {
			        sql.append("AND UPPER(" + field + ") LIKE UPPER(?) ");
			        params.add("%" + keyword + "%");
			    }
			}

			sql.append("ORDER BY ITEM_ID DESC");

			ps = conn.prepareStatement(sql.toString());

			// 파라미터 세팅
			for (int i = 0; i < params.size(); i++) {
				ps.setObject(i + 1, params.get(i));
			}

			rs = ps.executeQuery();

			while (rs.next()) {

				ItemMgmtDTO itemDTO = new ItemMgmtDTO();

				itemDTO.setItem_id(rs.getInt("ITEM_ID"));
				itemDTO.setItem_code(rs.getString("ITEM_CODE"));
				itemDTO.setItem_name(rs.getString("ITEM_NAME"));
				itemDTO.setItem_type(rs.getString("ITEM_TYPE"));
				itemDTO.setUnit(rs.getString("UNIT"));
				itemDTO.setSpec(rs.getString("SPEC"));
				itemDTO.setSupplier_name(rs.getString("SUPPLIER_NAME"));
				itemDTO.setSafety_stock(rs.getInt("SAFETY_STOCK"));
				itemDTO.setUse_yn(rs.getString("USE_YN"));
				itemDTO.setRemark(rs.getString("REMARK"));
				itemDTO.setCreated_at(rs.getTimestamp("CREATED_AT").toLocalDateTime().toLocalDate());
				itemDTO.setUpdated_at(rs.getTimestamp("UPDATED_AT").toLocalDateTime().toLocalDate());

				list.add(itemDTO);
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

	        String sql = "INSERT INTO ITEM (ITEM_ID, ITEM_CODE, ITEM_NAME, ITEM_TYPE, UNIT, SPEC, SUPPLIER_NAME, SAFETY_STOCK, USE_YN, CREATED_AT) "
	                   + "VALUES (ITEM_SEQ.NEXTVAL, ?, ?, ?, ?, ?, ?, ?, 'Y', SYSDATE)";

	        ps = conn.prepareStatement(sql);

	        ps.setString(1, dto.getItem_code());
	        ps.setString(2, dto.getItem_name());
	        ps.setString(3, dto.getItem_type());
	        ps.setString(4, dto.getUnit());
	        ps.setString(5, dto.getSpec());
	        ps.setString(6, dto.getSupplier_name());
	        ps.setInt(7, dto.getSafety_stock());

	        ps.executeUpdate();

	    } catch (Exception e) {
	        e.printStackTrace();
	    } finally {
	        try { if (ps != null) ps.close(); } catch (Exception e) {}
	        try { if (conn != null) conn.close(); } catch (Exception e) {}
	    }
	}
}
