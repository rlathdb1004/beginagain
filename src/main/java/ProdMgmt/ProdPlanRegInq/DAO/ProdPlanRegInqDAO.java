package ProdMgmt.ProdPlanRegInq.DAO;

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

import ProdMgmt.ProdPlanRegInq.DTO.ProdPlanRegInqDTO;

public class ProdPlanRegInqDAO {
	
	

	// 생산계획 전체조회
	public List<ProdPlanRegInqDTO> selectAll(){
		
		List<ProdPlanRegInqDTO> list = new ArrayList<ProdPlanRegInqDTO>();
		
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		try {
			// JNDI 방식
			// context.xml에 있는 DB 정보로 커넥션 풀을 가져온다
			Context ctx = new InitialContext();
			// DataSource : 커넥션 풀 관리자
			DataSource dataFactory = (DataSource)ctx.lookup("java:/comp/env/jdbc/oracle");
			
			// DB 접속(그런데 이제 커넥션 풀로)
			conn = dataFactory.getConnection();
			
			// SQL 준비
			String query = "select";
				   query += " row_number() OVER(ORDER BY p.plan_date DESC, p.plan_id desc) AS NO,"
				   		 +	" 'PP-' || TO_CHAR(p.PLAN_DATE, 'YYYYMMDD') || '-' || LPAD(p.PLAN_ID, 3, '0') AS PLAN_NO,"
				   		 +	" TO_CHAR(p.PLAN_DATE, 'YYYY-MM-DD') AS PLAN_DATE,"
				   		 +	" i.item_code,"
				   		 +	" i.ITEM_NAME,"
				   		 +	" p.PLAN_QTY,"
				   		 +	" i.UNIT,"
				   		 +	" p.LINE_CODE,"
				   		 +	" p.REMARK"
				   		 +	" from PRODUCTION_PLAN p"
				   		 +	" JOIN ITEM i"
				   		 +	" ON p.ITEM_ID = i.ITEM_ID";
			ps = conn.prepareStatement(query);
			
			// SQL 실행 및 결과 확보
			rs = ps.executeQuery();
			
			// 결과 활용
			while( rs.next() ) {
				
				int seqNO = rs.getInt("NO");
				String planNo = rs.getString("PLAN_NO");
				Date planDate = rs.getDate("PLAN_DATE");
				String planCode = rs.getString("ITEM_CODE");
				String planName = rs.getString("ITEM_NAME");
				int planAmount = rs.getInt("PLAN_QTY");
				String planUnit = rs.getString("UNIT");
				String planLine = rs.getString("PLAN_QTY");
				String memo = rs.getString("REMARK");
				
				System.out.println("planCode: "+ planCode);
				
				ProdPlanRegInqDTO dto = new ProdPlanRegInqDTO();
				
				dto.setSeqNO(seqNO);
				dto.setPlanNo(planNo);
				dto.setPlanDate(planDate);
				dto.setPlanCode(planCode);
				dto.setPlanName(planName);
				dto.setPlanAmount(planAmount);
				dto.setPlanUnit(planUnit);
				dto.setPlanLine(planLine);
				dto.setMemo(memo);
				
				list.add(dto);
			}
			
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			if(rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			
			if(ps != null) {
				try {
					ps.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			
			if(conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		
		System.out.println(list.size());
		return list;
	}
	
}
