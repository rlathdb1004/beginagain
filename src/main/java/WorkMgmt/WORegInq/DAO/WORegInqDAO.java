package WorkMgmt.WORegInq.DAO;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

import WorkMgmt.WORegInq.DTO.WORegInqDTO;

/*
 * 작업지시 등록/조회 DAO
 *
 * 역할
 * 1. 작업지시 전체 건수 조회
 * 2. 검색 조건이 반영된 페이지별 목록 조회
 * 3. 선택한 작업지시 논리삭제(USE_YN = 'N')
 *
 * 조회 기준
 * - WORK_ORDER + PRODUCTION_PLAN + ITEM + EMP 조인
 * - USE_YN = 'Y' 인 작업지시만 조회
 *
 * 주의
 * - 아래 컬럼명은 실제 DB와 다를 수 있으니 확인 필요
 *   1) EMP.EMP_NAME
 *   2) ROUTING.PROCESS_SEQ
 *   3) ROUTING.ROUTING_ID
 *   4) PROCESS.PROCESS_CODE
 *   5) BOM.VERSION_NO
 */
public class WORegInqDAO {

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
        sql.append("FROM WORK_ORDER wo ");
        sql.append("JOIN PRODUCTION_PLAN pp ON wo.PLAN_ID = pp.PLAN_ID ");
        sql.append("JOIN ITEM i ON wo.ITEM_ID = i.ITEM_ID ");
        sql.append("LEFT JOIN EMP e ON wo.EMP_ID = e.EMP_ID ");

        /*
         * 품목별 대표 공정 1건 조회
         *
         * ROUTING 에서 PROCESS_SEQ가 가장 작은 공정을 대표 공정으로 사용
         */
        sql.append("LEFT JOIN ( ");
        sql.append("    SELECT item_id, process_code ");
        sql.append("    FROM ( ");
        sql.append("        SELECT r.ITEM_ID, p.PROCESS_CODE, ");
        sql.append("               ROW_NUMBER() OVER (PARTITION BY r.ITEM_ID ORDER BY r.PROCESS_SEQ ASC, r.ROUTING_ID ASC) AS RN ");
        sql.append("        FROM ROUTING r ");
        sql.append("        JOIN PROCESS p ON r.PROCESS_ID = p.PROCESS_ID ");
        sql.append("    ) ");
        sql.append("    WHERE RN = 1 ");
        sql.append(") procInfo ON wo.ITEM_ID = procInfo.ITEM_ID ");

        /*
         * 품목별 대표 BOM 1건 조회
         *
         * USE_YN='Y' 우선
         * 같은 품목이면 VERSION_NO 높은 것 우선
         */
        sql.append("LEFT JOIN ( ");
        sql.append("    SELECT item_id, bom_code ");
        sql.append("    FROM ( ");
        sql.append("        SELECT b.ITEM_ID, ");
        sql.append("               'BOM-' || LPAD(b.BOM_ID, 3, '0') AS BOM_CODE, ");
        sql.append("               ROW_NUMBER() OVER ( ");
        sql.append("                   PARTITION BY b.ITEM_ID ");
        sql.append("                   ORDER BY CASE NVL(b.USE_YN, 'Y') WHEN 'Y' THEN 0 ELSE 1 END, ");
        sql.append("                            b.VERSION_NO DESC, b.BOM_ID DESC ");
        sql.append("               ) AS RN ");
        sql.append("        FROM BOM b ");
        sql.append("    ) ");
        sql.append("    WHERE RN = 1 ");
        sql.append(") bomInfo ON wo.ITEM_ID = bomInfo.ITEM_ID ");

        /*
         * 논리삭제 안 된 작업지시만 조회
         */
        sql.append("WHERE NVL(wo.USE_YN, 'Y') = 'Y' ");

        /*
         * 날짜 / 키워드 검색조건 추가
         */
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
     * - NO             -> WORK_ORDER_ID
     * - 작업지시번호    -> WO-YYYYMMDD-001 형식
     * - 일자           -> WORK_DATE
     * - 품목코드       -> ITEM.ITEM_CODE
     * - 품목명         -> ITEM.ITEM_NAME
     * - 지시량         -> WORK_ORDER.WORK_QTY
     * - 단위           -> ITEM.UNIT
     * - 라인           -> PRODUCTION_PLAN.LINE_CODE
     * - 공정           -> 대표 공정
     * - 작업자         -> EMP.EMP_NAME
     * - BOM            -> 대표 BOM
     * - 비고           -> WORK_ORDER.REMARK
     */
    public List<WORegInqDTO> getListByPage(
            String startDate, String endDate, String searchType, String keyword,
            int startRow, int endRow) {

        List<WORegInqDTO> list = new ArrayList<>();
        StringBuilder sql = new StringBuilder();
        List<Object> params = new ArrayList<>();

        sql.append("SELECT * ");
        sql.append("FROM ( ");
        sql.append("    SELECT ROW_NUMBER() OVER (ORDER BY wo.WORK_DATE DESC, wo.WORK_ORDER_ID DESC) AS RN, ");

        /*
         * 실제 PK
         * 화면 NO 컬럼에는 이 값을 넣는다
         */
        sql.append("           wo.WORK_ORDER_ID AS WORK_ORDER_ID, ");

        /*
         * 화면 표시용 작업지시번호
         * 예: WO-20260402-001
         */
        sql.append("           'WO-' || TO_CHAR(wo.WORK_DATE, 'YYYYMMDD') || '-' || LPAD(wo.WORK_ORDER_ID, 3, '0') AS WORK_ORDER_NO, ");

        sql.append("           wo.WORK_DATE AS WORK_DATE, ");
        sql.append("           i.ITEM_CODE AS ITEM_CODE, ");
        sql.append("           i.ITEM_NAME AS ITEM_NAME, ");
        sql.append("           wo.WORK_QTY AS WORK_QTY, ");
        sql.append("           i.UNIT AS UNIT, ");
        sql.append("           pp.LINE_CODE AS LINE_CODE, ");
        sql.append("           NVL(procInfo.PROCESS_CODE, '-') AS PROCESS_CODE, ");
        sql.append("           NVL(e.EMP_NAME, '-') AS EMP_NAME, ");
        sql.append("           NVL(bomInfo.BOM_CODE, '-') AS BOM_CODE, ");
        sql.append("           wo.STATUS AS STATUS, ");
        sql.append("           wo.REMARK AS REMARK ");

        sql.append("    FROM WORK_ORDER wo ");
        sql.append("    JOIN PRODUCTION_PLAN pp ON wo.PLAN_ID = pp.PLAN_ID ");
        sql.append("    JOIN ITEM i ON wo.ITEM_ID = i.ITEM_ID ");
        sql.append("    LEFT JOIN EMP e ON wo.EMP_ID = e.EMP_ID ");

        /*
         * 품목별 대표 공정 1건 조인
         */
        sql.append("    LEFT JOIN ( ");
        sql.append("        SELECT item_id, process_code ");
        sql.append("        FROM ( ");
        sql.append("            SELECT r.ITEM_ID, p.PROCESS_CODE, ");
        sql.append("                   ROW_NUMBER() OVER (PARTITION BY r.ITEM_ID ORDER BY r.PROCESS_SEQ ASC, r.ROUTING_ID ASC) AS RN ");
        sql.append("            FROM ROUTING r ");
        sql.append("            JOIN PROCESS p ON r.PROCESS_ID = p.PROCESS_ID ");
        sql.append("        ) ");
        sql.append("        WHERE RN = 1 ");
        sql.append("    ) procInfo ON wo.ITEM_ID = procInfo.ITEM_ID ");

        /*
         * 품목별 대표 BOM 1건 조인
         */
        sql.append("    LEFT JOIN ( ");
        sql.append("        SELECT item_id, bom_code ");
        sql.append("        FROM ( ");
        sql.append("            SELECT b.ITEM_ID, ");
        sql.append("                   'BOM-' || LPAD(b.BOM_ID, 3, '0') AS BOM_CODE, ");
        sql.append("                   ROW_NUMBER() OVER ( ");
        sql.append("                       PARTITION BY b.ITEM_ID ");
        sql.append("                       ORDER BY CASE NVL(b.USE_YN, 'Y') WHEN 'Y' THEN 0 ELSE 1 END, ");
        sql.append("                                b.VERSION_NO DESC, b.BOM_ID DESC ");
        sql.append("                   ) AS RN ");
        sql.append("            FROM BOM b ");
        sql.append("        ) ");
        sql.append("        WHERE RN = 1 ");
        sql.append("    ) bomInfo ON wo.ITEM_ID = bomInfo.ITEM_ID ");

        /*
         * 논리삭제 안 된 작업지시만 조회
         */
        sql.append("    WHERE NVL(wo.USE_YN, 'Y') = 'Y' ");

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
                    WORegInqDTO dto = new WORegInqDTO();

                    dto.setSeqNO(rs.getInt("WORK_ORDER_ID"));
                    dto.setWorkOrderNo(rs.getString("WORK_ORDER_NO"));
                    dto.setWorkDate(rs.getDate("WORK_DATE"));
                    dto.setItemCode(rs.getString("ITEM_CODE"));
                    dto.setItemName(rs.getString("ITEM_NAME"));
                    dto.setWorkQty(rs.getInt("WORK_QTY"));
                    dto.setUnit(rs.getString("UNIT"));
                    dto.setLineCode(rs.getString("LINE_CODE"));
                    dto.setProcessCode(rs.getString("PROCESS_CODE"));
                    dto.setEmpName(rs.getString("EMP_NAME"));
                    dto.setBomCode(rs.getString("BOM_CODE"));
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
     * 선택한 WORK_ORDER_ID의 USE_YN을 N으로 변경
     */
    public int deleteByIds(String[] seqNos) {
        int result = 0;

        if (seqNos == null || seqNos.length == 0) {
            return 0;
        }

        StringBuilder sql = new StringBuilder();
        sql.append("UPDATE WORK_ORDER ");
        sql.append("SET USE_YN = 'N', UPDATED_AT = SYSDATE ");
        sql.append("WHERE WORK_ORDER_ID IN (");

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
     * - WORK_DATE 기준
     *
     * 키워드
     * - 선택한 searchType 기준 LIKE 검색
     */
    private void appendSearchCondition(
            StringBuilder sql, List<Object> params,
            String startDate, String endDate, String searchType, String keyword) {

        // 날짜 검색
        if (startDate != null && !startDate.trim().equals("")) {
            sql.append(" AND wo.WORK_DATE >= ? ");
            params.add(Date.valueOf(startDate));
        }

        if (endDate != null && !endDate.trim().equals("")) {
            sql.append(" AND wo.WORK_DATE <= ? ");
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
                sql.append("  OR procInfo.PROCESS_CODE LIKE ? ");
                sql.append("  OR e.EMP_NAME LIKE ? ");
                sql.append("  OR bomInfo.BOM_CODE LIKE ? ");
                sql.append(" ) ");

                params.add(kw);
                params.add(kw);
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

            } else if ("processCode".equals(searchType)) {
                sql.append(" AND procInfo.PROCESS_CODE LIKE ? ");
                params.add(kw);

            } else if ("empName".equals(searchType)) {
                sql.append(" AND e.EMP_NAME LIKE ? ");
                params.add(kw);

            } else if ("bomCode".equals(searchType)) {
                sql.append(" AND bomInfo.BOM_CODE LIKE ? ");
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