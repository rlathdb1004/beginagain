package WorkMgmt.WorkStatusInq.DAO;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

import WorkMgmt.WorkStatusInq.DTO.WorkStatusInqDTO;
import WorkMgmt.WorkStatusInq.DTO.WorkStatusLineProgressDTO;

/*
 * 작업 현황 조회 DAO
 *
 * 역할
 * 1. 작업 현황 전체 건수 조회
 * 2. 검색 조건 반영 페이지별 목록 조회
 * 3. 라인별 공정 진행도 조회
 *
 * 주의
 * - 현재 DB 구조상 "공정별 실제 완료 이력" 테이블이 없으므로
 *   그래프 진행률은 WORK_ORDER.STATUS 기반 추정값으로 계산한다.
 * - 나중에 공정 실적 테이블이 생기면 getLineProgressList() 쿼리만 바꾸면 된다.
 */
public class WorkStatusInqDAO {

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
     * 조건
     * - WORK_ORDER.USE_YN = 'Y'
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
        sql.append("WHERE NVL(wo.USE_YN, 'Y') = 'Y' ");

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
     * 페이지별 작업 현황 목록 조회
     *
     * 반환 컬럼
     * - NO             -> WORK_ORDER_ID
     * - 작업지시번호    -> WO-YYYYMMDD-001 형식
     * - 작업일자       -> WORK_DATE
     * - 품목코드       -> ITEM.ITEM_CODE
     * - 품목명         -> ITEM.ITEM_NAME
     * - 작업지시량     -> WORK_ORDER.WORK_QTY
     * - 생산량         -> PRODUCTION_RESULT.PRODUCED_QTY 합계
     * - 진행률         -> 생산량 / 작업지시량 * 100
     * - 라인           -> PRODUCTION_PLAN.LINE_CODE
     * - 작업자         -> EMP.EMP_NAME
     * - 상태           -> WORK_ORDER.STATUS
     * - 비고           -> WORK_ORDER.REMARK
     */
    public List<WorkStatusInqDTO> getListByPage(
            String startDate, String endDate, String searchType, String keyword,
            int startRow, int endRow) {

        List<WorkStatusInqDTO> list = new ArrayList<>();
        StringBuilder sql = new StringBuilder();
        List<Object> params = new ArrayList<>();

        sql.append("SELECT * ");
        sql.append("FROM ( ");
        sql.append("    SELECT ROW_NUMBER() OVER (ORDER BY wo.WORK_DATE DESC, wo.WORK_ORDER_ID DESC) AS RN, ");
        sql.append("           wo.WORK_ORDER_ID AS WORK_ORDER_ID, ");
        sql.append("           'WO-' || TO_CHAR(wo.WORK_DATE, 'YYYYMMDD') || '-' || LPAD(wo.WORK_ORDER_ID, 3, '0') AS WORK_ORDER_NO, ");
        sql.append("           wo.WORK_DATE AS WORK_DATE, ");
        sql.append("           i.ITEM_CODE AS ITEM_CODE, ");
        sql.append("           i.ITEM_NAME AS ITEM_NAME, ");
        sql.append("           wo.WORK_QTY AS WORK_QTY, ");
        sql.append("           NVL(prsum.PRODUCED_QTY, 0) AS PRODUCED_QTY, ");
        sql.append("           CASE ");
        sql.append("               WHEN NVL(wo.WORK_QTY, 0) = 0 THEN 0 ");
        sql.append("               ELSE ROUND((NVL(prsum.PRODUCED_QTY, 0) / wo.WORK_QTY) * 100, 0) ");
        sql.append("           END AS PROGRESS_RATE, ");
        sql.append("           pp.LINE_CODE AS LINE_CODE, ");
        sql.append("           NVL(e.EMP_NAME, '-') AS EMP_NAME, ");
        sql.append("           wo.STATUS AS STATUS, ");
        sql.append("           wo.REMARK AS REMARK ");
        sql.append("    FROM WORK_ORDER wo ");
        sql.append("    JOIN PRODUCTION_PLAN pp ON wo.PLAN_ID = pp.PLAN_ID ");
        sql.append("    JOIN ITEM i ON wo.ITEM_ID = i.ITEM_ID ");
        sql.append("    LEFT JOIN EMP e ON wo.EMP_ID = e.EMP_ID ");

        /*
         * 작업지시별 생산량 합계
         * - 논리삭제 안 된 생산실적만 합산
         */
        sql.append("    LEFT JOIN ( ");
        sql.append("        SELECT WORK_ORDER_ID, SUM(NVL(PRODUCED_QTY, 0)) AS PRODUCED_QTY ");
        sql.append("        FROM PRODUCTION_RESULT ");
        sql.append("        WHERE NVL(USE_YN, 'Y') = 'Y' ");
        sql.append("        GROUP BY WORK_ORDER_ID ");
        sql.append("    ) prsum ON wo.WORK_ORDER_ID = prsum.WORK_ORDER_ID ");

        sql.append("    WHERE NVL(wo.USE_YN, 'Y') = 'Y' ");

        // 검색 조건 추가
        appendSearchCondition(sql, params, startDate, endDate, searchType, keyword);

        sql.append(") ");
        sql.append("WHERE RN BETWEEN ? AND ? ");
        sql.append("ORDER BY RN ");

        // 페이징 범위 추가
        params.add(startRow);
        params.add(endRow);

        try (
            Connection conn = getConnection();
            PreparedStatement ps = conn.prepareStatement(sql.toString())
        ) {
            bindParams(ps, params);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    WorkStatusInqDTO dto = new WorkStatusInqDTO();

                    dto.setSeqNO(rs.getInt("WORK_ORDER_ID"));
                    dto.setWorkOrderNo(rs.getString("WORK_ORDER_NO"));
                    dto.setWorkDate(rs.getDate("WORK_DATE"));
                    dto.setItemCode(rs.getString("ITEM_CODE"));
                    dto.setItemName(rs.getString("ITEM_NAME"));
                    dto.setWorkQty(rs.getInt("WORK_QTY"));
                    dto.setProducedQty(rs.getInt("PRODUCED_QTY"));
                    dto.setProgressRate(rs.getInt("PROGRESS_RATE"));
                    dto.setLineCode(rs.getString("LINE_CODE"));
                    dto.setEmpName(rs.getString("EMP_NAME"));
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
     * 라인별 공정 진행도 조회
     *
     * 현재 DB 구조상 공정 완료 이력이 없으므로
     * WORK_ORDER.STATUS 를 기준으로 공정 진행률을 추정한다.
     *
     * 상태 매핑 예시
     * - 완료 / COMPLETE / DONE       -> 100
     * - 진행중 / IN_PROGRESS        -> 60
     * - 대기 / READY / WAIT         -> 0
     * - 그 외                       -> 20
     *
     * ROUTING + PROCESS 를 이용해서
     * 품목별 공정 순서를 라인 단위로 보여준다.
     */
    public List<WorkStatusLineProgressDTO> getLineProgressList(
            String startDate, String endDate, String searchType, String keyword) {

        List<WorkStatusLineProgressDTO> list = new ArrayList<>();
        StringBuilder sql = new StringBuilder();
        List<Object> params = new ArrayList<>();

        sql.append("SELECT ");
        sql.append("    pp.LINE_CODE AS LINE_CODE, ");
        sql.append("    p.PROCESS_NAME AS PROCESS_NAME, ");
        sql.append("    ROUND(AVG( ");
        sql.append("        CASE ");
        sql.append("            WHEN UPPER(NVL(wo.STATUS, '')) IN ('완료', 'COMPLETE', 'DONE') THEN 100 ");
        sql.append("            WHEN UPPER(NVL(wo.STATUS, '')) IN ('진행중', 'IN_PROGRESS', 'WORKING') THEN 60 ");
        sql.append("            WHEN UPPER(NVL(wo.STATUS, '')) IN ('대기', 'WAIT', 'READY') THEN 0 ");
        sql.append("            ELSE 20 ");
        sql.append("        END ");
        sql.append("    ), 0) AS PROGRESS_RATE ");
        sql.append("FROM WORK_ORDER wo ");
        sql.append("JOIN PRODUCTION_PLAN pp ON wo.PLAN_ID = pp.PLAN_ID ");
        sql.append("JOIN ITEM i ON wo.ITEM_ID = i.ITEM_ID ");
        sql.append("JOIN ROUTING r ON i.ITEM_ID = r.ITEM_ID ");
        sql.append("JOIN PROCESS p ON r.PROCESS_ID = p.PROCESS_ID ");
        sql.append("LEFT JOIN EMP e ON wo.EMP_ID = e.EMP_ID ");
        sql.append("WHERE NVL(wo.USE_YN, 'Y') = 'Y' ");
        sql.append("  AND NVL(r.USE_YN, 'Y') = 'Y' ");
        sql.append("  AND NVL(p.USE_YN, 'Y') = 'Y' ");

        /*
         * 그래프도 목록과 같은 검색 조건을 최대한 같이 적용
         * - 날짜 / 검색키워드 동일 반영
         */
        appendSearchCondition(sql, params, startDate, endDate, searchType, keyword);

        sql.append("GROUP BY pp.LINE_CODE, r.PROCESS_SEQ, p.PROCESS_NAME ");
        sql.append("ORDER BY pp.LINE_CODE, r.PROCESS_SEQ ");

        try (
            Connection conn = getConnection();
            PreparedStatement ps = conn.prepareStatement(sql.toString())
        ) {
            bindParams(ps, params);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    WorkStatusLineProgressDTO dto = new WorkStatusLineProgressDTO();

                    dto.setLineCode(rs.getString("LINE_CODE"));
                    dto.setProcessName(rs.getString("PROCESS_NAME"));
                    dto.setProgressRate(rs.getInt("PROGRESS_RATE"));

                    list.add(dto);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
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
                sql.append("  OR e.EMP_NAME LIKE ? ");
                sql.append("  OR wo.STATUS LIKE ? ");
                sql.append(" ) ");

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

            } else if ("empName".equals(searchType)) {
                sql.append(" AND e.EMP_NAME LIKE ? ");
                params.add(kw);

            } else if ("status".equals(searchType)) {
                sql.append(" AND wo.STATUS LIKE ? ");
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