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

public class ProdPerfRegInqDAO {

    // JNDI 커넥션 풀 사용
    private Connection getConnection() throws Exception {
        Context ctx = new InitialContext();
        DataSource dataFactory = (DataSource) ctx.lookup("java:/comp/env/jdbc/oracle");
        return dataFactory.getConnection();
    }

    // -----------------------------------
    // 전체 건수 조회
    // searched=Y 일 때 컨트롤러에서 호출
    // -----------------------------------
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

        // 검색 조건 동적 추가
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

    // -----------------------------------
    // 페이지별 목록 조회
    // -----------------------------------
    public List<ProdPerfRegInqDTO> getListByPage(
            String startDate, String endDate, String searchType, String keyword,
            int startRow, int endRow) {

        List<ProdPerfRegInqDTO> list = new ArrayList<>();
        StringBuilder sql = new StringBuilder();
        List<Object> params = new ArrayList<>();

        sql.append("SELECT * ");
        sql.append("FROM ( ");
        sql.append("    SELECT ROW_NUMBER() OVER (ORDER BY pr.RESULT_DATE DESC, pr.RESULT_ID DESC) AS RN, ");

        // RESULT_ID를 화면 NO 및 삭제 키로 사용
        sql.append("           pr.RESULT_ID AS RESULT_ID, ");

        // 작업지시번호 표시용 가공
        // 형식은 WO-YYYYMMDD-001 스타일
        sql.append("           'WO-' || TO_CHAR(wo.WORK_DATE, 'YYYYMMDD') || '-' || LPAD(wo.WORK_ORDER_ID, 3, '0') AS WORK_ORDER_NO, ");

        sql.append("           pr.RESULT_DATE AS RESULT_DATE, ");
        sql.append("           i.ITEM_CODE AS ITEM_CODE, ");
        sql.append("           i.ITEM_NAME AS ITEM_NAME, ");
        sql.append("           pr.PRODUCED_QTY AS PRODUCED_QTY, ");
        sql.append("           pr.LOSS_QTY AS LOSS_QTY, ");
        sql.append("           i.UNIT AS UNIT, ");
        sql.append("           pp.LINE_CODE AS LINE_CODE, ");
        sql.append("           pr.LOT_NO AS LOT_NO, ");
        sql.append("           i.SUPPLIER_NAME AS SUPPLIER_NAME, ");
        sql.append("           pr.STATUS AS STATUS, ");
        sql.append("           pr.REMARK AS REMARK ");
        sql.append("    FROM PRODUCTION_RESULT pr ");
        sql.append("    JOIN WORK_ORDER wo ON pr.WORK_ORDER_ID = wo.WORK_ORDER_ID ");
        sql.append("    JOIN PRODUCTION_PLAN pp ON wo.PLAN_ID = pp.PLAN_ID ");
        sql.append("    JOIN ITEM i ON wo.ITEM_ID = i.ITEM_ID ");
        sql.append("    WHERE NVL(pr.USE_YN, 'Y') = 'Y' ");

        // 검색 조건 추가
        appendSearchCondition(sql, params, startDate, endDate, searchType, keyword);

        sql.append(") ");
        sql.append("WHERE RN BETWEEN ? AND ? ");
        sql.append("ORDER BY RN ");

        // 페이지 범위 파라미터
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
                    dto.setSupplierName(rs.getString("SUPPLIER_NAME"));
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

    // -----------------------------------
    // 논리삭제
    // RESULT_ID 기준으로 USE_YN='N'
    // -----------------------------------
    public int deleteByIds(String[] seqNos) {
        int result = 0;

        // 체크된 값 없으면 종료
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

    // -----------------------------------
    // 검색 조건 동적 추가
    // -----------------------------------
    private void appendSearchCondition(
            StringBuilder sql, List<Object> params,
            String startDate, String endDate, String searchType, String keyword) {

        // 날짜 검색: RESULT_DATE 기준
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

            // 검색기준이 전체일 때
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

            // 작업지시번호 검색
            } else if ("workOrderNo".equals(searchType)) {
                sql.append(" AND 'WO-' || TO_CHAR(wo.WORK_DATE, 'YYYYMMDD') || '-' || LPAD(wo.WORK_ORDER_ID, 3, '0') LIKE ? ");
                params.add(kw);

            // 품목코드 검색
            } else if ("itemCode".equals(searchType)) {
                sql.append(" AND i.ITEM_CODE LIKE ? ");
                params.add(kw);

            // 품목명 검색
            } else if ("itemName".equals(searchType)) {
                sql.append(" AND i.ITEM_NAME LIKE ? ");
                params.add(kw);

            // 라인 검색
            } else if ("lineCode".equals(searchType)) {
                sql.append(" AND pp.LINE_CODE LIKE ? ");
                params.add(kw);

            // LOT 검색
            } else if ("lotNo".equals(searchType)) {
                sql.append(" AND pr.LOT_NO LIKE ? ");
                params.add(kw);
            }
        }
    }

    // -----------------------------------
    // PreparedStatement 파라미터 바인딩
    // -----------------------------------
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
