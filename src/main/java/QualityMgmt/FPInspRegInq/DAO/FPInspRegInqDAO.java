package QualityMgmt.FPInspRegInq.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

import QualityMgmt.FPInspRegInq.DTO.FPInspRegInqDTO;

/*
 * 완제품 검사 등록 / 조회 DAO
 * 
 * FINAL_INSPECTION
 *   -> PRODUCTION_RESULT
 *   -> WORK_ORDER
 *   -> PRODUCTION_PLAN
 *   -> ITEM
 * 
 * 주의:
 * FINAL_INSPECTION 테이블의 실제 컬럼명은 RESULT가 아니라 STATUS 이다.
 * 그래서 SQL에서는 STATUS를 조회하고,
 * DTO / JSP에서는 기존 result 필드를 그대로 쓰기 위해
 * AS RESULT 별칭을 사용한다.
 */
public class FPInspRegInqDAO {

    // =========================
    // 1. 완제품 검사 목록 조회
    // =========================
    public List<FPInspRegInqDTO> selectFPInspRegInqList(FPInspRegInqDTO searchDTO) {

        List<FPInspRegInqDTO> list = new ArrayList<FPInspRegInqDTO>();

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
            // 2) 기본 SQL 작성
            // --------------------------------------
            String query = "";
            query += " SELECT ";
            query += "     fi.FINAL_INSPECTION_ID, ";
            query += "     fi.RESULT_ID, ";
            query += "     pr.WORK_ORDER_ID, ";
            query += "     pp.ITEM_ID, ";
            query += "     i.ITEM_CODE, ";
            query += "     i.ITEM_NAME, ";
            query += "     pr.LOT_NO, ";
            query += "     fi.INSPECT_QTY, ";
            query += "     fi.STATUS AS RESULT, ";
            query += "     fi.INSPECTION_DATE, ";
            query += "     fi.REMARK, ";
            query += "     fi.CREATED_AT, ";
            query += "     fi.UPDATED_AT ";
            query += " FROM FINAL_INSPECTION fi ";
            query += " INNER JOIN PRODUCTION_RESULT pr ";
            query += "    ON fi.RESULT_ID = pr.RESULT_ID ";
            query += " INNER JOIN WORK_ORDER wo ";
            query += "    ON pr.WORK_ORDER_ID = wo.WORK_ORDER_ID ";
            query += " INNER JOIN PRODUCTION_PLAN pp ";
            query += "    ON wo.PLAN_ID = pp.PLAN_ID ";
            query += " INNER JOIN ITEM i ";
            query += "    ON pp.ITEM_ID = i.ITEM_ID ";
            query += " WHERE 1 = 1 ";

            // 사용여부 조건
            query += " AND fi.USE_YN = 'Y' ";

            List<Object> paramList = new ArrayList<Object>();

            // --------------------------------------
            // 3) 판정구분 조건
            // --------------------------------------
            if (searchDTO.getResultType() != null
                    && !"".equals(searchDTO.getResultType().trim())
                    && !"전체".equals(searchDTO.getResultType())) {

                query += " AND fi.STATUS = ? ";
                paramList.add(searchDTO.getResultType());
            }

            // --------------------------------------
            // 4) 날짜 조건
            // --------------------------------------
            if (searchDTO.getStartDate() != null) {
                query += " AND TRUNC(fi.INSPECTION_DATE) >= ? ";
                paramList.add(searchDTO.getStartDate());
            }

            if (searchDTO.getEndDate() != null) {
                query += " AND TRUNC(fi.INSPECTION_DATE) <= ? ";
                paramList.add(searchDTO.getEndDate());
            }

            // --------------------------------------
            // 5) 검색 조건
            // --------------------------------------
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

            query += " ORDER BY fi.FINAL_INSPECTION_ID DESC ";

            System.out.println("완제품 검사 목록 조회 SQL : " + query);

            // --------------------------------------
            // 6) PreparedStatement 생성
            // --------------------------------------
            ps = conn.prepareStatement(query);

            // --------------------------------------
            // 7) 바인딩 값 세팅
            // --------------------------------------
            for (int i = 0; i < paramList.size(); i++) {
                ps.setObject(i + 1, paramList.get(i));
            }

            // --------------------------------------
            // 8) SQL 실행
            // --------------------------------------
            rs = ps.executeQuery();

            // --------------------------------------
            // 9) 결과를 DTO에 담기
            // --------------------------------------
            while (rs.next()) {
                FPInspRegInqDTO dto = new FPInspRegInqDTO();

                dto.setFinalInspectionId(rs.getInt("FINAL_INSPECTION_ID"));
                dto.setResultId(rs.getInt("RESULT_ID"));
                dto.setWorkOrderId(rs.getInt("WORK_ORDER_ID"));
                dto.setItemId(rs.getInt("ITEM_ID"));
                dto.setItemCode(rs.getString("ITEM_CODE"));
                dto.setItemName(rs.getString("ITEM_NAME"));
                dto.setLotNo(rs.getString("LOT_NO"));
                dto.setInspectQty(rs.getDouble("INSPECT_QTY"));
                dto.setResult(rs.getString("RESULT"));
                dto.setInspectionDate(rs.getDate("INSPECTION_DATE"));
                dto.setRemark(rs.getString("REMARK"));
                dto.setCreatedAt(rs.getDate("CREATED_AT"));
                dto.setUpdatedAt(rs.getDate("UPDATED_AT"));

                list.add(dto);
            }

            System.out.println("완제품 검사 목록 조회 결과 건수 : " + list.size());

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
    // 2. 완제품 검사 상세 조회
    // =========================
    public FPInspRegInqDTO selectFPInspRegInqOne(int finalInspectionId) {

        FPInspRegInqDTO dto = null;

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
            query += "     fi.FINAL_INSPECTION_ID, ";
            query += "     fi.RESULT_ID, ";
            query += "     pr.WORK_ORDER_ID, ";
            query += "     pp.ITEM_ID, ";
            query += "     i.ITEM_CODE, ";
            query += "     i.ITEM_NAME, ";
            query += "     pr.LOT_NO, ";
            query += "     fi.INSPECT_QTY, ";
            query += "     fi.STATUS AS RESULT, ";
            query += "     fi.INSPECTION_DATE, ";
            query += "     fi.REMARK, ";
            query += "     fi.CREATED_AT, ";
            query += "     fi.UPDATED_AT ";
            query += " FROM FINAL_INSPECTION fi ";
            query += " INNER JOIN PRODUCTION_RESULT pr ";
            query += "    ON fi.RESULT_ID = pr.RESULT_ID ";
            query += " INNER JOIN WORK_ORDER wo ";
            query += "    ON pr.WORK_ORDER_ID = wo.WORK_ORDER_ID ";
            query += " INNER JOIN PRODUCTION_PLAN pp ";
            query += "    ON wo.PLAN_ID = pp.PLAN_ID ";
            query += " INNER JOIN ITEM i ";
            query += "    ON pp.ITEM_ID = i.ITEM_ID ";
            query += " WHERE fi.FINAL_INSPECTION_ID = ? ";
            query += "   AND fi.USE_YN = 'Y' ";

            // --------------------------------------
            // 3) PreparedStatement 생성
            // --------------------------------------
            ps = conn.prepareStatement(query);
            ps.setInt(1, finalInspectionId);

            // --------------------------------------
            // 4) SQL 실행
            // --------------------------------------
            rs = ps.executeQuery();

            // --------------------------------------
            // 5) 결과를 DTO에 담기
            // --------------------------------------
            if (rs.next()) {
                dto = new FPInspRegInqDTO();

                dto.setFinalInspectionId(rs.getInt("FINAL_INSPECTION_ID"));
                dto.setResultId(rs.getInt("RESULT_ID"));
                dto.setWorkOrderId(rs.getInt("WORK_ORDER_ID"));
                dto.setItemId(rs.getInt("ITEM_ID"));
                dto.setItemCode(rs.getString("ITEM_CODE"));
                dto.setItemName(rs.getString("ITEM_NAME"));
                dto.setLotNo(rs.getString("LOT_NO"));
                dto.setInspectQty(rs.getDouble("INSPECT_QTY"));
                dto.setResult(rs.getString("RESULT"));
                dto.setInspectionDate(rs.getDate("INSPECTION_DATE"));
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
}