package ProdMgmt.ProdPerfRegInq.DAO;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

import ProdMgmt.ProdPerfRegInq.DTO.ProdPerfRegInqDTO;

/*
 * 생산실적 등록/조회 DAO
 *
 * 역할
 * 1. 생산실적 전체 건수 조회
 * 2. 검색 조건이 반영된 페이지별 목록 조회
 * 3. 선택한 생산실적 논리삭제(USE_YN = 'N')
 *
 * 조회 기준
 * - PRODUCTION_RESULT + WORK_ORDER + PRODUCTION_PLAN + ITEM 조인
 * - USE_YN = 'Y' 인 생산실적만 조회
 *
 * 주의
 * - ITEM.SUPPLIER_NAME 컬럼이 실제 DB에 없으면 해당 부분만 수정 필요
 */
public class ProdPerfRegInqDAO {

    /*
     * JNDI 커넥션 풀에서 DB 연결 가져오기
     */
    private Connection getConnection() throws Exception {
        Context ctx = new InitialContext();
        DataSource dataFactory = (DataSource) ctx.lookup("java:/comp/env/jdbc/oracle");
        return dataFactory.getConnection();
    }

    /*
     * 전체 건수 조회
     *
     * 용도
     * - 페이징 totalCount 계산
     *
     * 조건
     * - USE_YN = 'Y'
     * - 날짜 / 키워드 검색조건 반영
     */
    public int getTotalCount(String startDate, String endDate, String searchType, String keyword) {
        int count = 0;

        StringBuilder sql = new StringBuilder();
        List<Object> params = new ArrayList<>();

        sql.append("SELECT COUNT(*) ");
        sql.append("FROM PRODUCTION_RESULT pr ");
        sql.append("JOIN WORK_ORDER wo ON pr.WORK_ORDER_ID = wo.WORK_ORDER_ID ");
        sql.append("JOIN PRODUCTION_PLAN pp ON wo.PLAN_ID = pp.PLAN_ID ");
        sql.append("JOIN ITEM i ON wo.ITEM_ID = i.ITEM_ID ");
        sql.append("WHERE NVL(pr.USE_YN, 'Y') = 'Y' ");

        // 날짜 / 키워드 검색조건 추가
        appendSearchCondition(sql, params, startDate, endDate, searchType, keyword);

        try (
            Connection conn = getConnection();
            PreparedStatement ps = conn.prepareStatement(sql.toString())
        ) {
            bindParams(ps, params);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    count = rs.getInt(1);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return count;
    }

    /*
     * 페이지별 목록 조회
     *
     * 반환 컬럼
     * - NO             -> RESULT_ID
     * - 작업지시번호    -> WO-YYYYMMDD-001 형식
     * - 일자           -> RESULT_DATE
     * - 품목코드       -> ITEM.ITEM_CODE
     * - 품목명         -> ITEM.ITEM_NAME
     * - 생산량         -> PRODUCTION_RESULT.PRODUCED_QTY
     * - 단위           -> ITEM.UNIT
     * - 라인           -> PRODUCTION_PLAN.LINE_CODE
     * - LOT            -> PRODUCTION_RESULT.LOT_NO
     * - 비고           -> PRODUCTION_RESULT.REMARK
     */
    public List<ProdPerfRegInqDTO> getListByPage(
            String startDate, String endDate, String searchType, String keyword,
            int startRow, int endRow) {

        List<ProdPerfRegInqDTO> list = new ArrayList<>();
        StringBuilder sql = new StringBuilder();
        List<Object> params = new ArrayList<>();

        sql.append("SELECT * ");
        sql.append("FROM ( ");
        sql.append("    SELECT ROW_NUMBER() OVER (ORDER BY pr.RESULT_DATE DESC, pr.RESULT_ID DESC) AS RN, ");

        /*
         * 실제 PK
         * 화면 NO 컬럼에는 이 값을 넣는다
         */
        sql.append("           pr.RESULT_ID AS RESULT_ID, ");

        /*
         * 화면 표시용 작업지시번호
         * 예: WO-20260402-001
         */
        sql.append("           'WO-' || TO_CHAR(wo.WORK_DATE, 'YYYYMMDD') || '-' || LPAD(wo.WORK_ORDER_ID, 3, '0') AS WORK_ORDER_NO, ");

        sql.append("           pr.RESULT_DATE AS RESULT_DATE, ");
        sql.append("           i.ITEM_CODE AS ITEM_CODE, ");
        sql.append("           i.ITEM_NAME AS ITEM_NAME, ");
        sql.append("           pr.PRODUCED_QTY AS PRODUCED_QTY, ");
        sql.append("           pr.LOSS_QTY AS LOSS_QTY, ");
        sql.append("           i.UNIT AS UNIT, ");
        sql.append("           pp.LINE_CODE AS LINE_CODE, ");
        sql.append("           pr.LOT_NO AS LOT_NO, ");
        sql.append("           pr.STATUS AS STATUS, ");
        sql.append("           pr.REMARK AS REMARK ");

        sql.append("    FROM PRODUCTION_RESULT pr ");
        sql.append("    JOIN WORK_ORDER wo ON pr.WORK_ORDER_ID = wo.WORK_ORDER_ID ");
        sql.append("    JOIN PRODUCTION_PLAN pp ON wo.PLAN_ID = pp.PLAN_ID ");
        sql.append("    JOIN ITEM i ON wo.ITEM_ID = i.ITEM_ID ");

        /*
         * 논리삭제 안 된 생산실적만 조회
         */
        sql.append("    WHERE NVL(pr.USE_YN, 'Y') = 'Y' ");

        /*
         * 검색 조건 추가
         */
        appendSearchCondition(sql, params, startDate, endDate, searchType, keyword);

        sql.append(") ");
        sql.append("WHERE RN BETWEEN ? AND ? ");
        sql.append("ORDER BY RN ");

        /*
         * 페이징 범위 추가
         */
        params.add(startRow);
        params.add(endRow);

        try (
            Connection conn = getConnection();
            PreparedStatement ps = conn.prepareStatement(sql.toString())
        ) {
            bindParams(ps, params);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    ProdPerfRegInqDTO dto = new ProdPerfRegInqDTO();

                    dto.setSeqNO(rs.getInt("RESULT_ID"));
                    dto.setWorkOrderNo(rs.getString("WORK_ORDER_NO"));
                    dto.setResultDate(rs.getDate("RESULT_DATE"));
                    dto.setItemCode(rs.getString("ITEM_CODE"));
                    dto.setItemName(rs.getString("ITEM_NAME"));
                    dto.setProducedQty(rs.getInt("PRODUCED_QTY"));
                    dto.setLossQty(rs.getInt("LOSS_QTY"));
                    dto.setUnit(rs.getString("UNIT"));
                    dto.setLineCode(rs.getString("LINE_CODE"));
                    dto.setLotNo(rs.getString("LOT_NO"));
                    dto.setStatus(rs.getString("STATUS"));
                    dto.setRemark(rs.getString("REMARK"));

                    list.add(dto);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    /*
     * 논리삭제
     *
     * 선택한 RESULT_ID의 USE_YN을 N으로 변경
     */
    public int deleteByIds(String[] seqNos) {
        int result = 0;

        if (seqNos == null || seqNos.length == 0) {
            return 0;
        }

        StringBuilder sql = new StringBuilder();
        sql.append("UPDATE PRODUCTION_RESULT ");
        sql.append("SET USE_YN = 'N', UPDATED_AT = SYSDATE ");
        sql.append("WHERE RESULT_ID IN (");

        for (int i = 0; i < seqNos.length; i++) {
            sql.append("?");
            if (i < seqNos.length - 1) {
                sql.append(", ");
            }
        }
        sql.append(")");

        try (
            Connection conn = getConnection();
            PreparedStatement ps = conn.prepareStatement(sql.toString())
        ) {
            for (int i = 0; i < seqNos.length; i++) {
                ps.setInt(i + 1, Integer.parseInt(seqNos[i]));
            }

            result = ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

    /*
     * 검색 조건 동적 추가
     *
     * 날짜
     * - RESULT_DATE 기준
     *
     * 키워드
     * - 선택한 searchType 기준 LIKE 검색
     */
    private void appendSearchCondition(
            StringBuilder sql, List<Object> params,
            String startDate, String endDate, String searchType, String keyword) {

        // 날짜 검색
        if (startDate != null && !startDate.trim().equals("")) {
            sql.append(" AND pr.RESULT_DATE >= ? ");
            params.add(Date.valueOf(startDate));
        }

        if (endDate != null && !endDate.trim().equals("")) {
            sql.append(" AND pr.RESULT_DATE <= ? ");
            params.add(Date.valueOf(endDate));
        }

        // 키워드 검색
        if (keyword != null && !keyword.trim().equals("")) {
            String kw = "%" + keyword.trim() + "%";

            if (searchType == null || searchType.trim().equals("")) {
                sql.append(" AND ( ");
                sql.append("     'WO-' || TO_CHAR(wo.WORK_DATE, 'YYYYMMDD') || '-' || LPAD(wo.WORK_ORDER_ID, 3, '0') LIKE ? ");
                sql.append("  OR i.ITEM_CODE LIKE ? ");
                sql.append("  OR i.ITEM_NAME LIKE ? ");
                sql.append("  OR pp.LINE_CODE LIKE ? ");
                sql.append("  OR pr.LOT_NO LIKE ? ");
                sql.append(" ) ");

                params.add(kw);
                params.add(kw);
                params.add(kw);
                params.add(kw);
                params.add(kw);

            } else if ("workOrderNo".equals(searchType)) {
                sql.append(" AND 'WO-' || TO_CHAR(wo.WORK_DATE, 'YYYYMMDD') || '-' || LPAD(wo.WORK_ORDER_ID, 3, '0') LIKE ? ");
                params.add(kw);

            } else if ("itemCode".equals(searchType)) {
                sql.append(" AND i.ITEM_CODE LIKE ? ");
                params.add(kw);

            } else if ("itemName".equals(searchType)) {
                sql.append(" AND i.ITEM_NAME LIKE ? ");
                params.add(kw);

            } else if ("lineCode".equals(searchType)) {
                sql.append(" AND pp.LINE_CODE LIKE ? ");
                params.add(kw);

            } else if ("lotNo".equals(searchType)) {
                sql.append(" AND pr.LOT_NO LIKE ? ");
                params.add(kw);
            }
        }
    }

    /*
     * PreparedStatement 파라미터 바인딩
     */
    private void bindParams(PreparedStatement ps, List<Object> params) throws Exception {
        for (int i = 0; i < params.size(); i++) {
            Object param = params.get(i);

            if (param instanceof Date) {
                ps.setDate(i + 1, (Date) param);
            } else if (param instanceof Integer) {
                ps.setInt(i + 1, (Integer) param);
            } else {
                ps.setString(i + 1, String.valueOf(param));
            }
        }
    }
}