package QualityMgmt.DefectRegInq.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

import QualityMgmt.DefectRegInq.DTO.DefectRegInqDTO;

/*
 * 불량 등록 / 조회 DAO
 * 
 * DEFECT_PRODUCT
 *   -> DEFECT_CODE
 *   -> FINAL_INSPECTION
 *   -> PRODUCTION_RESULT
 *   -> WORK_ORDER
 *   -> PRODUCTION_PLAN
 *   -> ITEM
 * 
 * 주의:
 * FINAL_INSPECTION 테이블에는 DEFECT_QTY 컬럼이 없다.
 * 그래서 현재는 fi.INSPECT_QTY 를 AS DEFECT_QTY 별칭으로 사용한다.
 */
public class DefectRegInqDAO {

    // =========================
    // 1. 불량 목록 조회
    // =========================
    public List<DefectRegInqDTO> selectDefectRegInqList(DefectRegInqDTO searchDTO) {

        List<DefectRegInqDTO> list = new ArrayList<DefectRegInqDTO>();

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
            query += "     dp.DEFECT_PRODUCT_ID, ";
            query += "     dp.FINAL_INSPECTION_ID, ";
            query += "     fi.RESULT_ID, ";
            query += "     pp.ITEM_ID, ";
            query += "     i.ITEM_CODE, ";
            query += "     i.ITEM_NAME, ";
            query += "     pr.LOT_NO, ";
            query += "     fi.INSPECT_QTY AS DEFECT_QTY, ";
            query += "     dc.DEFECT_CODE_ID, ";
            query += "     dc.DEFECT_CODE, ";
            query += "     dc.DEFECT_NAME, ";
            query += "     dc.DEFECT_TYPE, ";
            query += "     dp.REMARK, ";
            query += "     dp.CREATED_AT, ";
            query += "     dp.UPDATED_AT ";
            query += " FROM DEFECT_PRODUCT dp ";
            query += " INNER JOIN DEFECT_CODE dc ";
            query += "    ON dp.DEFECT_CODE_ID = dc.DEFECT_CODE_ID ";
            query += " INNER JOIN FINAL_INSPECTION fi ";
            query += "    ON dp.FINAL_INSPECTION_ID = fi.FINAL_INSPECTION_ID ";
            query += " INNER JOIN PRODUCTION_RESULT pr ";
            query += "    ON fi.RESULT_ID = pr.RESULT_ID ";
            query += " INNER JOIN WORK_ORDER wo ";
            query += "    ON pr.WORK_ORDER_ID = wo.WORK_ORDER_ID ";
            query += " INNER JOIN PRODUCTION_PLAN pp ";
            query += "    ON wo.PLAN_ID = pp.PLAN_ID ";
            query += " INNER JOIN ITEM i ";
            query += "    ON pp.ITEM_ID = i.ITEM_ID ";
            query += " WHERE 1 = 1 ";

            query += " AND dc.USE_YN = 'Y' ";

            List<Object> paramList = new ArrayList<Object>();

            // --------------------------------------
            // 3) 불량유형 조건
            // --------------------------------------
            if (searchDTO.getDefectTypeSearch() != null
                    && !"".equals(searchDTO.getDefectTypeSearch().trim())
                    && !"전체".equals(searchDTO.getDefectTypeSearch())) {

                query += " AND dc.DEFECT_TYPE = ? ";
                paramList.add(searchDTO.getDefectTypeSearch());
            }

            // --------------------------------------
            // 4) 날짜 조건
            // --------------------------------------
            if (searchDTO.getStartDate() != null) {
                query += " AND TRUNC(dp.CREATED_AT) >= ? ";
                paramList.add(searchDTO.getStartDate());
            }

            if (searchDTO.getEndDate() != null) {
                query += " AND TRUNC(dp.CREATED_AT) <= ? ";
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
                } else if ("defectCode".equals(searchType)) {
                    query += " AND dc.DEFECT_CODE LIKE ? ";
                    paramList.add(keywordLike);
                } else if ("defectName".equals(searchType)) {
                    query += " AND dc.DEFECT_NAME LIKE ? ";
                    paramList.add(keywordLike);
                } else {
                    query += " AND (i.ITEM_CODE LIKE ? ";
                    query += "      OR i.ITEM_NAME LIKE ? ";
                    query += "      OR dc.DEFECT_CODE LIKE ? ";
                    query += "      OR dc.DEFECT_NAME LIKE ?) ";
                    paramList.add(keywordLike);
                    paramList.add(keywordLike);
                    paramList.add(keywordLike);
                    paramList.add(keywordLike);
                }
            }

            query += " ORDER BY dp.DEFECT_PRODUCT_ID DESC ";

            System.out.println("불량 목록 조회 SQL : " + query);

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
                DefectRegInqDTO dto = new DefectRegInqDTO();

                dto.setDefectProductId(rs.getInt("DEFECT_PRODUCT_ID"));
                dto.setFinalInspectionId(rs.getInt("FINAL_INSPECTION_ID"));
                dto.setResultId(rs.getInt("RESULT_ID"));
                dto.setItemId(rs.getInt("ITEM_ID"));
                dto.setItemCode(rs.getString("ITEM_CODE"));
                dto.setItemName(rs.getString("ITEM_NAME"));
                dto.setLotNo(rs.getString("LOT_NO"));
                dto.setDefectQty(rs.getDouble("DEFECT_QTY"));
                dto.setDefectCodeId(rs.getInt("DEFECT_CODE_ID"));
                dto.setDefectCode(rs.getString("DEFECT_CODE"));
                dto.setDefectName(rs.getString("DEFECT_NAME"));
                dto.setDefectType(rs.getString("DEFECT_TYPE"));
                dto.setRemark(rs.getString("REMARK"));
                dto.setCreatedAt(rs.getDate("CREATED_AT"));
                dto.setUpdatedAt(rs.getDate("UPDATED_AT"));

                list.add(dto);
            }

            System.out.println("불량 목록 조회 결과 건수 : " + list.size());

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
    // 2. 불량 상세 조회
    // =========================
    public DefectRegInqDTO selectDefectRegInqOne(int defectProductId) {

        DefectRegInqDTO dto = null;

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
            query += "     dp.DEFECT_PRODUCT_ID, ";
            query += "     dp.FINAL_INSPECTION_ID, ";
            query += "     fi.RESULT_ID, ";
            query += "     pp.ITEM_ID, ";
            query += "     i.ITEM_CODE, ";
            query += "     i.ITEM_NAME, ";
            query += "     pr.LOT_NO, ";
            query += "     fi.INSPECT_QTY AS DEFECT_QTY, ";
            query += "     dc.DEFECT_CODE_ID, ";
            query += "     dc.DEFECT_CODE, ";
            query += "     dc.DEFECT_NAME, ";
            query += "     dc.DEFECT_TYPE, ";
            query += "     dp.REMARK, ";
            query += "     dp.CREATED_AT, ";
            query += "     dp.UPDATED_AT ";
            query += " FROM DEFECT_PRODUCT dp ";
            query += " INNER JOIN DEFECT_CODE dc ";
            query += "    ON dp.DEFECT_CODE_ID = dc.DEFECT_CODE_ID ";
            query += " INNER JOIN FINAL_INSPECTION fi ";
            query += "    ON dp.FINAL_INSPECTION_ID = fi.FINAL_INSPECTION_ID ";
            query += " INNER JOIN PRODUCTION_RESULT pr ";
            query += "    ON fi.RESULT_ID = pr.RESULT_ID ";
            query += " INNER JOIN WORK_ORDER wo ";
            query += "    ON pr.WORK_ORDER_ID = wo.WORK_ORDER_ID ";
            query += " INNER JOIN PRODUCTION_PLAN pp ";
            query += "    ON wo.PLAN_ID = pp.PLAN_ID ";
            query += " INNER JOIN ITEM i ";
            query += "    ON pp.ITEM_ID = i.ITEM_ID ";
            query += " WHERE dp.DEFECT_PRODUCT_ID = ? ";
            query += "   AND dc.USE_YN = 'Y' ";

            // --------------------------------------
            // 3) PreparedStatement 생성
            // --------------------------------------
            ps = conn.prepareStatement(query);
            ps.setInt(1, defectProductId);

            // --------------------------------------
            // 4) SQL 실행
            // --------------------------------------
            rs = ps.executeQuery();

            // --------------------------------------
            // 5) 결과를 DTO에 담기
            // --------------------------------------
            if (rs.next()) {
                dto = new DefectRegInqDTO();

                dto.setDefectProductId(rs.getInt("DEFECT_PRODUCT_ID"));
                dto.setFinalInspectionId(rs.getInt("FINAL_INSPECTION_ID"));
                dto.setResultId(rs.getInt("RESULT_ID"));
                dto.setItemId(rs.getInt("ITEM_ID"));
                dto.setItemCode(rs.getString("ITEM_CODE"));
                dto.setItemName(rs.getString("ITEM_NAME"));
                dto.setLotNo(rs.getString("LOT_NO"));
                dto.setDefectQty(rs.getDouble("DEFECT_QTY"));
                dto.setDefectCodeId(rs.getInt("DEFECT_CODE_ID"));
                dto.setDefectCode(rs.getString("DEFECT_CODE"));
                dto.setDefectName(rs.getString("DEFECT_NAME"));
                dto.setDefectType(rs.getString("DEFECT_TYPE"));
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