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
			sql.append(" p.ITEM_CODE AS PRODUCT_CODE, ");
			sql.append(" p.ITEM_NAME AS PRODUCT_NAME, ");
			sql.append(" d.ITEM_ID AS MATERIAL_ID, ");
			sql.append(" m.ITEM_CODE AS MATERIAL_CODE, ");
			sql.append(" m.ITEM_NAME AS MATERIAL_NAME, ");
			sql.append(" d.QTY_REQUIRED, ");
			sql.append(" d.UNIT ");
			sql.append("FROM BOM b ");
			sql.append("JOIN ITEM p ON b.ITEM_ID = p.ITEM_ID ");
			sql.append("JOIN BOM_DETAIL d ON b.BOM_ID = d.BOM_ID ");
			sql.append("JOIN ITEM m ON d.ITEM_ID = m.ITEM_ID ");
			sql.append("WHERE 1=1 ");

			List<Object> params = new ArrayList<>();

			if (dto.getProduct_code() != null && !dto.getProduct_code().isEmpty()) {
				sql.append("AND p.ITEM_CODE = ? ");
				params.add(dto.getProduct_code());
			}

			sql.append(" ORDER BY b.BOM_ID DESC ");

			ps = conn.prepareStatement(sql.toString());

			for (int i = 0; i < params.size(); i++) {
				ps.setObject(i + 1, params.get(i));
			}

			rs = ps.executeQuery();

			while (rs.next()) {

				BOMMgmtDTO dtoBOM = new BOMMgmtDTO();

				dtoBOM.setBOM_id(rs.getInt("BOM_ID"));

				dtoBOM.setProduct_code(rs.getString("PRODUCT_CODE"));
				dtoBOM.setProduct_name(rs.getString("PRODUCT_NAME"));

				dtoBOM.setMaterial_id(rs.getInt("MATERIAL_ID"));
				dtoBOM.setMaterial_code(rs.getString("MATERIAL_CODE"));
				dtoBOM.setMaterial_name(rs.getString("MATERIAL_NAME"));

				dtoBOM.setQty_required(rs.getDouble("QTY_REQUIRED"));
				dtoBOM.setUnit(rs.getString("UNIT"));

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

	public void insertBOM(int itemId, String useYn, String remark) {

	    Connection conn = null;
	    PreparedStatement ps = null;

	    try {

	        Context ctx = new InitialContext();
	        DataSource ds = (DataSource) ctx.lookup("java:comp/env/jdbc/oracle");

	        conn = ds.getConnection();

	        String sql = "INSERT INTO BOM "
	                   + "(BOM_ID, ITEM_ID, VERSION_NO, USE_YN, REMARK, CREATED_AT, UPDATED_AT) "
	                   + "VALUES (SEQ_BOM.NEXTVAL, ?, 1, ?, ?, SYSDATE, SYSDATE)";

	        ps = conn.prepareStatement(sql);

	        ps.setInt(1, itemId);
	        ps.setString(2, useYn != null ? useYn : "Y");
	        ps.setString(3, remark);

	        ps.executeUpdate();

	    } catch (Exception e) {
	        e.printStackTrace();
	    } finally {
	        try { if (ps != null) ps.close(); } catch (Exception e) {}
	        try { if (conn != null) conn.close(); } catch (Exception e) {}
	    }
	}
	public void insertBOMDetail(BOMMgmtDTO dto) {

	    Connection conn = null;
	    PreparedStatement ps = null;

	    try {

	        Context ctx = new InitialContext();
	        DataSource ds = (DataSource) ctx.lookup("java:comp/env/jdbc/oracle");

	        conn = ds.getConnection();

	        String sql = "INSERT INTO BOM_DETAIL "
	                   + "(BOM_DETAIL_ID, BOM_ID, ITEM_ID, QTY_REQUIRED, UNIT, REMARK, CREATED_AT) "
	                   + "VALUES (SEQ_BOM_DETAIL.NEXTVAL, ?, ?, ?, ?, ?, SYSDATE)";

	        ps = conn.prepareStatement(sql);

	        ps.setInt(1, dto.getBOM_id());
	        ps.setInt(2, dto.getMaterial_id());
	        ps.setDouble(3, dto.getQty_required());
	        ps.setString(4, dto.getUnit());
	        ps.setString(5, dto.getRemark());

	        ps.executeUpdate();

	    } catch (Exception e) {
	        e.printStackTrace();
	    } finally {
	        try { if (ps != null) ps.close(); } catch (Exception e) {}
	        try { if (conn != null) conn.close(); } catch (Exception e) {}
	    }
	}

	public int getCurrBomId() {

	    Connection conn = null;
	    PreparedStatement ps = null;
	    ResultSet rs = null;

	    int bomId = 0;

	    try {

	        Context ctx = new InitialContext();
	        DataSource ds = (DataSource) ctx.lookup("java:comp/env/jdbc/oracle");

	        conn = ds.getConnection();

	        String sql = "SELECT SEQ_BOM.CURRVAL FROM DUAL";

	        ps = conn.prepareStatement(sql);

	        rs = ps.executeQuery();

	        if (rs.next()) {
	            bomId = rs.getInt(1);
	        }

	    } catch (Exception e) {
	        e.printStackTrace();
	    } finally {
	        try { if (rs != null) rs.close(); } catch (Exception e) {}
	        try { if (ps != null) ps.close(); } catch (Exception e) {}
	        try { if (conn != null) conn.close(); } catch (Exception e) {}
	    }

	    return bomId;
	}

}
