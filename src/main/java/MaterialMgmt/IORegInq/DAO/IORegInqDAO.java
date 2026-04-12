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

/*
	 * 입출고 등록/조회 DAO
	 * 
	 * 역할
	 * 1) 입출고 목록 조회
	 * 2) 입출고 상세 조회
	 * 
	 * DB 연결 방식
	 * - java:/comp/env/jdbc/oracle
	 */
public class IORegInqDAO {

	/*
	 * 입출고 목록 조회
	 * 
	 * 검색 조건
	 * 1) 입출고구분
	 * 2) 시작일 / 종료일
	 * 3) 품목코드 / 품목명 검색
	 */
	public List<IORegInqDTO> selectIORegInqList(IORegInqDTO searchDTO) {
		
		List<IORegInqDTO> list = new ArrayList<IORegInqDTO>();

		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			// --------------------------------------
			// 1. JNDI로 커넥션 풀 가져오기
			// --------------------------------------
			Context ctx = new InitialContext();
			DataSource dataFactory = (DataSource) ctx.lookup("java:/comp/env/jdbc/oracle");
			conn = dataFactory.getConnection();
			System.out.println("현재 접속 DB USER : " + conn.getMetaData().getUserName());

			// --------------------------------------
			// 2. 기본 SQL 작성
			//    MATERIAL_INOUT + ITEM 조인
			// --------------------------------------
			String query = "";
			query += " SELECT ";
			query += "     mi.INOUT_ID, ";
			query += "     mi.ITEM_ID, ";
			query += "     i.ITEM_CODE, ";
			query += "     i.ITEM_NAME, ";
			query += "     mi.INOUT_TYPE, ";
			query += "     mi.QTY, ";
			query += "     mi.UNIT, ";
			query += "     mi.INOUT_DATE, ";
			query += "     mi.STATUS, ";
			query += "     mi.REMARK, ";
			query += "     mi.CREATED_AT, ";
			query += "     mi.UPDATED_AT ";
			query += " FROM MATERIAL_INOUT mi ";
			query += " JOIN ITEM i ";
			query += "   ON mi.ITEM_ID = i.ITEM_ID ";
			query += " WHERE 1 = 1 ";

			// 동적 바인딩 값 저장용 리스트
			List<Object> paramList = new ArrayList<Object>();

			// ------><---------
			// 3. 입출고구분 조건
			//    전체가 아니고 값이 있을 때만 조건 추가
			// ------><---------
			if (searchDTO.getInoutType() != null 
					&& !"".equals(searchDTO.getInoutType())
					&& !"전체".equals(searchDTO.getInoutType())) {
				
				query += " AND mi.INOUT_TYPE = ? ";
				paramList.add(searchDTO.getInoutType());
			}

			// ------><---------
			// 4. 시작일 조건
			// ------><---------
			if (searchDTO.getStartDate() != null) {
				query += " AND mi.INOUT_DATE >= ? ";
				paramList.add(searchDTO.getStartDate());
			}

			// ------><---------
			// 5. 종료일 조건
			// ------><---------
			if (searchDTO.getEndDate() != null) {
				query += " AND mi.INOUT_DATE <= ? ";
				paramList.add(searchDTO.getEndDate());
			}

			// ------><---------
			// 6. 검색어 조건
			//    searchType = itemCode / itemName
			// ------><---------
			if (searchDTO.getKeyword() != null && !"".equals(searchDTO.getKeyword().trim())) {
				
				String keyword = "%" + searchDTO.getKeyword().trim() + "%";

				if ("itemCode".equals(searchDTO.getSearchType())) {
					query += " AND i.ITEM_CODE LIKE ? ";
					paramList.add(keyword);
				} else if ("itemName".equals(searchDTO.getSearchType())) {
					query += " AND i.ITEM_NAME LIKE ? ";
					paramList.add(keyword);
				} else {
					// searchType 값이 비어있거나 둘 다 검색하고 싶을 때
					query += " AND (i.ITEM_CODE LIKE ? OR i.ITEM_NAME LIKE ?) ";
					paramList.add(keyword);
					paramList.add(keyword);
				}
			}

			// ------><---------
			// 7. 정렬
			// ------><---------
			query += " ORDER BY mi.INOUT_DATE DESC, mi.INOUT_ID DESC ";

			// SQL 확인용 출력
			System.out.println("입출고 목록 조회 SQL : " + query);

			// ------><---------
			// 8. PreparedStatement 생성
			// ------><---------
			ps = conn.prepareStatement(query);

			// ------><---------
			// 9. 파라미터 바인딩
			// ------><---------
			for (int i = 0; i < paramList.size(); i++) {
				Object obj = paramList.get(i);

				if (obj instanceof String) {
					ps.setString(i + 1, (String) obj);
				} else if (obj instanceof Date) {
					ps.setDate(i + 1, (Date) obj);
				}
			}

			// ------><---------
			// 10. SQL 실행
			// ------><---------
			rs = ps.executeQuery();

			// ------><---------
			// 11. 결과를 DTO에 담아서 List에 추가
			// ------><---------
			
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

				list.add(dto);
			}
			System.out.println("입출고 목록 조회 결과 건수 : " + list.size());

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			// ------><---------
			// 12. 자원 반납
			// ------><---------
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

	
	/*
	 * 입출고 상세 조회
	 * 
	 * 전달받은 INOUT_ID 한 건만 조회
	 */
	public IORegInqDTO selectIORegInqOne(int inoutId) {
		
		IORegInqDTO dto = null;

		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			// ------><---------
			// 1. JNDI로 커넥션 풀 가져오기
			// ------><---------
			Context ctx = new InitialContext();
			DataSource dataFactory = (DataSource) ctx.lookup("java:/comp/env/jdbc/oracle");
			conn = dataFactory.getConnection();
			
			System.out.println("현재 접속 DB USER : " + conn.getMetaData().getUserName());

			// ------><---------
			// 2. 상세 조회 SQL 작성
			// ------><---------
			String query = "";
			query += " SELECT ";
			query += "     mi.INOUT_ID, ";
			query += "     mi.ITEM_ID, ";
			query += "     i.ITEM_CODE, ";
			query += "     i.ITEM_NAME, ";
			query += "     mi.INOUT_TYPE, ";
			query += "     mi.QTY, ";
			query += "     mi.UNIT, ";
			query += "     mi.INOUT_DATE, ";
			query += "     mi.STATUS, ";
			query += "     mi.REMARK, ";
			query += "     mi.CREATED_AT, ";
			query += "     mi.UPDATED_AT ";
			query += " FROM MATERIAL_INOUT mi ";
			query += " JOIN ITEM i ";
			query += "   ON mi.ITEM_ID = i.ITEM_ID ";
			query += " WHERE mi.INOUT_ID = ? ";

			ps = conn.prepareStatement(query);
			ps.setInt(1, inoutId);

			// ------><---------
			// 3. SQL 실행
			// ------><---------
			rs = ps.executeQuery();

			// ------><---------
			// 4. 결과를 DTO에 담기
			// ------><---------
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
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			// ------><---------
			// 5. 자원 반납
			// ------><---------
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

		return dto;
	}
}
