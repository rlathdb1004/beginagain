package mes.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

import mes.dto.CreateDTO;

public class CreateDAO {
	
	private Connection getConn() {
		Connection conn = null;
		try {
			// JNDI 방식
			// context.xml에 있는 DB 정보로 커넥션 풀을 가져온다
			Context ctx = new InitialContext();
			// DataSource : 커넥션 풀 관리자
			DataSource dataFactory = (DataSource) ctx.lookup("java:/comp/env/jdbc/oracle");

			// DB 접속(그런데 이제 커넥션 풀로)
			conn = dataFactory.getConnection();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return conn;
	}
	
	
	public int insertItem(CreateDTO dto){
		int result = -1;
		
		
		try (Connection conn = getConn();
				PreparedStatement ps = conn.prepareStatement(
						  " INSERT INTO test1 (NO, subject, suplier, itemsCode, itemsName, itemsUnit, createDate, itemsMemo)"
						+ " Values (test1_seq.nextval, ?, ?, ?, ?, ?, sysdate, ?)"
				);
				ResultSet rs=null;) {
			ps.setString(1, dto.getSubject().trim());
			ps.setString(2, dto.getSuplier());
			ps.setString(3, dto.getItemsCode());
			ps.setString(4, dto.getItemsName());
			ps.setString(5, dto.getItemsUnit());
//			ps.setDate(6, dto.getCreateDate());
			ps.setString(6, dto.getItemsMemo());
			
			result = ps.executeUpdate();

//			while (rs.next()) {
//				System.out.println("CreateDAO : " + dto);}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return result;
	}

}
