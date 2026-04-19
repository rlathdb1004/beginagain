package dashboard.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

public class AdminMainDAO {

    private Connection getConnection() throws Exception {
        Context ctx = new InitialContext();
        DataSource dataFactory = (DataSource) ctx.lookup("java:/comp/env/jdbc/oracle");
        return dataFactory.getConnection();
    }

    // =========================================================
    // 운영 요약
    // =========================================================
    public Map<String, Object> selectSummary(Date baseDate) throws Exception {
        Map<String, Object> result = new LinkedHashMap<String, Object>();

        String sql =
                "SELECT " +
                "    0 AS approval_pending_count, " +
                "    0 AS interface_fail_count, " +
                "    0 AS batch_fail_count, " +
                "    ( " +
                "        NVL((SELECT COUNT(*) " +
                "               FROM WORK_ORDER WO " +
                "               LEFT JOIN PRODUCTION_PLAN PP ON PP.PLAN_ID = WO.PLAN_ID " +
                "               LEFT JOIN PRODUCTION_RESULT PR ON PR.WORK_ORDER_ID = WO.WORK_ORDER_ID " +
                "              WHERE TRUNC(NVL(WO.CREATED_AT, PP.PLAN_DATE)) = TRUNC(?) " +
                "                AND NVL(WO.USE_YN, 'Y') = 'Y' " +
                "                AND PR.WORK_ORDER_ID IS NULL), 0) " +
                "      + NVL((SELECT COUNT(*) " +
                "               FROM PRODUCTION_RESULT PR " +
                "               LEFT JOIN FINAL_INSPECTION FI " +
                "                 ON FI.RESULT_ID = PR.RESULT_ID " +
                "                AND NVL(FI.USE_YN, 'Y') = 'Y' " +
                "              WHERE TRUNC(PR.RESULT_DATE) = TRUNC(?) " +
                "                AND NVL(PR.USE_YN, 'Y') = 'Y' " +
                "                AND FI.RESULT_ID IS NULL), 0) " +
                "      + NVL((SELECT COUNT(*) " +
                "               FROM INVENTORY IV " +
                "              WHERE NVL(IV.QTY_ON_HAND, 0) < NVL(IV.SAFETY_STOCK, 0)), 0) " +
                "      + NVL((SELECT COUNT(*) " +
                "               FROM ITEM I " +
                "              WHERE NVL(I.USE_YN, 'Y') = 'Y' " +
                "                AND (I.UNIT IS NULL OR TRIM(I.UNIT) = '')), 0) " +
                "    ) AS data_issue_count, " +
                "    0 AS auth_request_count, " +
                "    0 AS notice_unread_count " +
                "FROM DUAL";

        try (
            Connection conn = getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)
        ) {
            ps.setDate(1, baseDate);
            ps.setDate(2, baseDate);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    result.put("approvalPendingCount", rs.getInt("approval_pending_count"));
                    result.put("interfaceFailCount", rs.getInt("interface_fail_count"));
                    result.put("batchFailCount", rs.getInt("batch_fail_count"));
                    result.put("dataIssueCount", rs.getInt("data_issue_count"));
                    result.put("authRequestCount", rs.getInt("auth_request_count"));
                    result.put("noticeUnreadCount", rs.getInt("notice_unread_count"));
                }
            }
        }

        return result;
    }

    // =========================================================
    // 기준정보 관리 현황
    // =========================================================
    public List<Map<String, Object>> selectMasterStatusList(Date baseDate) throws Exception {
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

        String sql =
                "SELECT label, cnt, meta " +
                "  FROM ( " +
                "        SELECT '품목 마스터' AS label, COUNT(*) AS cnt, '활성 품목 수' AS meta " +
                "          FROM ITEM " +
                "         WHERE NVL(USE_YN, 'Y') = 'Y' " +
                "        UNION ALL " +
                "        SELECT '라인 마스터' AS label, COUNT(*) AS cnt, '등록 라인 수' AS meta " +
                "          FROM LINE " +
                "         WHERE NVL(USE_YN, 'Y') = 'Y' " +
                "        UNION ALL " +
                "        SELECT '설비 마스터' AS label, COUNT(*) AS cnt, '등록 설비 수' AS meta " +
                "          FROM EQUIPMENT " +
                "         WHERE NVL(USE_YN, 'Y') = 'Y' " +
                "        UNION ALL " +
                "        SELECT '재고 기준' AS label, COUNT(*) AS cnt, '재고 관리 품목 수' AS meta " +
                "          FROM INVENTORY " +
                "        UNION ALL " +
                "        SELECT '작업자 계정' AS label, COUNT(*) AS cnt, '활성 사용자 수' AS meta " +
                "          FROM EMP " +
                "         WHERE NVL(USE_YN, 'Y') = 'Y' " +
                "        UNION ALL " +
                "        SELECT '생산계획 기준' AS label, COUNT(*) AS cnt, '오늘 계획 기준 건수' AS meta " +
                "          FROM PRODUCTION_PLAN " +
                "         WHERE TRUNC(PLAN_DATE) = TRUNC(?) " +
                "           AND NVL(USE_YN, 'Y') = 'Y' " +
                "       ) " +
                " ORDER BY label";

        try (
            Connection conn = getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)
        ) {
            ps.setDate(1, baseDate);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Map<String, Object> row = new LinkedHashMap<String, Object>();
                    row.put("label", rs.getString("label"));
                    row.put("count", rs.getInt("cnt"));
                    row.put("meta", rs.getString("meta"));
                    list.add(row);
                }
            }
        }

        return list;
    }

    // =========================================================
    // 승인 대기함
    // 전용 승인 테이블 없으면 빈 목록
    // =========================================================
    public List<Map<String, Object>> selectApprovalList(Date baseDate) throws Exception {
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

        String sql =
                "SELECT CAST(NULL AS VARCHAR2(50)) AS request_no, " +
                "       CAST(NULL AS VARCHAR2(50)) AS request_type, " +
                "       CAST(NULL AS VARCHAR2(50)) AS request_user, " +
                "       CAST(NULL AS VARCHAR2(50)) AS request_date_text, " +
                "       CAST(NULL AS VARCHAR2(30)) AS status " +
                "  FROM DUAL " +
                " WHERE 1 = 0";

        try (
            Connection conn = getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery()
        ) {
            while (rs.next()) {
                Map<String, Object> row = new LinkedHashMap<String, Object>();
                row.put("requestNo", rs.getString("request_no"));
                row.put("requestType", rs.getString("request_type"));
                row.put("requestUser", rs.getString("request_user"));
                row.put("requestDateText", rs.getString("request_date_text"));
                row.put("status", rs.getString("status"));
                list.add(row);
            }
        }

        return list;
    }

    // =========================================================
    // 인터페이스 상태
    // 전용 로그 테이블 없으면 빈 목록
    // =========================================================
    public List<Map<String, Object>> selectInterfaceStatusList(Date baseDate) throws Exception {
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

        String sql =
                "SELECT CAST(NULL AS VARCHAR2(100)) AS interface_name, " +
                "       CAST(NULL AS VARCHAR2(30)) AS direction, " +
                "       CAST(NULL AS VARCHAR2(50)) AS target_system, " +
                "       CAST(NULL AS VARCHAR2(30)) AS status, " +
                "       CAST(NULL AS VARCHAR2(50)) AS last_run_text, " +
                "       CAST(NULL AS VARCHAR2(200)) AS message " +
                "  FROM DUAL " +
                " WHERE 1 = 0";

        try (
            Connection conn = getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery()
        ) {
            while (rs.next()) {
                Map<String, Object> row = new LinkedHashMap<String, Object>();
                row.put("interfaceName", rs.getString("interface_name"));
                row.put("direction", rs.getString("direction"));
                row.put("targetSystem", rs.getString("target_system"));
                row.put("status", rs.getString("status"));
                row.put("lastRunText", rs.getString("last_run_text"));
                row.put("message", rs.getString("message"));
                list.add(row);
            }
        }

        return list;
    }

    // =========================================================
    // 배치 실행 현황
    // 전용 배치 로그 테이블 없으면 빈 목록
    // =========================================================
    public List<Map<String, Object>> selectBatchStatusList(Date baseDate) throws Exception {
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

        String sql =
                "SELECT CAST(NULL AS VARCHAR2(100)) AS batch_name, " +
                "       CAST(NULL AS VARCHAR2(30)) AS status, " +
                "       CAST(NULL AS VARCHAR2(50)) AS start_time_text, " +
                "       CAST(NULL AS VARCHAR2(50)) AS end_time_text, " +
                "       CAST(NULL AS VARCHAR2(50)) AS duration_text, " +
                "       CAST(NULL AS VARCHAR2(200)) AS message " +
                "  FROM DUAL " +
                " WHERE 1 = 0";

        try (
            Connection conn = getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery()
        ) {
            while (rs.next()) {
                Map<String, Object> row = new LinkedHashMap<String, Object>();
                row.put("batchName", rs.getString("batch_name"));
                row.put("status", rs.getString("status"));
                row.put("startTimeText", rs.getString("start_time_text"));
                row.put("endTimeText", rs.getString("end_time_text"));
                row.put("durationText", rs.getString("duration_text"));
                row.put("message", rs.getString("message"));
                list.add(row);
            }
        }

        return list;
    }

    // =========================================================
    // 데이터 품질 점검
    // =========================================================
    public List<Map<String, Object>> selectDataQualityList(Date baseDate) throws Exception {
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

        String sql =
                "SELECT check_name, issue_count, severity, description " +
                "  FROM ( " +
                "        SELECT '작업지시 미실적' AS check_name, " +
                "               COUNT(*) AS issue_count, " +
                "               'HIGH' AS severity, " +
                "               '생성된 작업지시 중 생산실적이 없는 건수' AS description " +
                "          FROM WORK_ORDER WO " +
                "          LEFT JOIN PRODUCTION_PLAN PP ON PP.PLAN_ID = WO.PLAN_ID " +
                "          LEFT JOIN PRODUCTION_RESULT PR ON PR.WORK_ORDER_ID = WO.WORK_ORDER_ID " +
                "         WHERE TRUNC(NVL(WO.CREATED_AT, PP.PLAN_DATE)) = TRUNC(?) " +
                "           AND NVL(WO.USE_YN, 'Y') = 'Y' " +
                "           AND PR.WORK_ORDER_ID IS NULL " +
                "        UNION ALL " +
                "        SELECT '생산실적 미검사' AS check_name, " +
                "               COUNT(*) AS issue_count, " +
                "               'HIGH' AS severity, " +
                "               '생산실적 중 최종검사가 누락된 건수' AS description " +
                "          FROM PRODUCTION_RESULT PR " +
                "          LEFT JOIN FINAL_INSPECTION FI " +
                "            ON FI.RESULT_ID = PR.RESULT_ID " +
                "           AND NVL(FI.USE_YN, 'Y') = 'Y' " +
                "         WHERE TRUNC(PR.RESULT_DATE) = TRUNC(?) " +
                "           AND NVL(PR.USE_YN, 'Y') = 'Y' " +
                "           AND FI.RESULT_ID IS NULL " +
                "        UNION ALL " +
                "        SELECT '안전재고 미달' AS check_name, " +
                "               COUNT(*) AS issue_count, " +
                "               'MEDIUM' AS severity, " +
                "               '현재고가 안전재고보다 낮은 품목 수' AS description " +
                "          FROM INVENTORY IV " +
                "         WHERE NVL(IV.QTY_ON_HAND, 0) < NVL(IV.SAFETY_STOCK, 0) " +
                "        UNION ALL " +
                "        SELECT '품목 단위 미등록' AS check_name, " +
                "               COUNT(*) AS issue_count, " +
                "               'MEDIUM' AS severity, " +
                "               '활성 품목 중 단위가 비어 있는 건수' AS description " +
                "          FROM ITEM I " +
                "         WHERE NVL(I.USE_YN, 'Y') = 'Y' " +
                "           AND (I.UNIT IS NULL OR TRIM(I.UNIT) = '') " +
                "        UNION ALL " +
                "        SELECT '라인 비가동 발생' AS check_name, " +
                "               COUNT(*) AS issue_count, " +
                "               'LOW' AS severity, " +
                "               '당일 비가동 이력이 발생한 라인/설비 건수' AS description " +
                "          FROM EQUIPMENT_DOWNTIME ED " +
                "         WHERE TRUNC(ED.START_TIME) = TRUNC(?) " +
                "           AND NVL(ED.USE_YN, 'Y') = 'Y' " +
                "       ) " +
                " ORDER BY issue_count DESC, check_name";

        try (
            Connection conn = getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)
        ) {
            ps.setDate(1, baseDate);
            ps.setDate(2, baseDate);
            ps.setDate(3, baseDate);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Map<String, Object> row = new LinkedHashMap<String, Object>();
                    row.put("checkName", rs.getString("check_name"));
                    row.put("issueCount", rs.getInt("issue_count"));
                    row.put("severity", rs.getString("severity"));
                    row.put("description", rs.getString("description"));
                    list.add(row);
                }
            }
        }

        return list;
    }

    // =========================================================
    // 사용자 / 권한 관리 현황
    // =========================================================
    public List<Map<String, Object>> selectUserStatusList(Date baseDate) throws Exception {
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

        String sql =
                "SELECT label, cnt, meta " +
                "  FROM ( " +
                "        SELECT '전체 사용자' AS label, COUNT(*) AS cnt, 'EMP 기준 전체 인원 수' AS meta " +
                "          FROM EMP " +
                "        UNION ALL " +
                "        SELECT '활성 사용자' AS label, COUNT(*) AS cnt, 'USE_YN = Y 기준' AS meta " +
                "          FROM EMP " +
                "         WHERE NVL(USE_YN, 'Y') = 'Y' " +
                "        UNION ALL " +
                "        SELECT '비활성 사용자' AS label, COUNT(*) AS cnt, 'USE_YN = N 기준' AS meta " +
                "          FROM EMP " +
                "         WHERE NVL(USE_YN, 'Y') = 'N' " +
                "        UNION ALL " +
                "        SELECT '권한 요청 대기' AS label, 0 AS cnt, '전용 권한 요청 테이블 미구축' AS meta " +
                "          FROM DUAL " +
                "       ) " +
                " ORDER BY label";

        try (
            Connection conn = getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery()
        ) {
            while (rs.next()) {
                Map<String, Object> row = new LinkedHashMap<String, Object>();
                row.put("label", rs.getString("label"));
                row.put("count", rs.getInt("cnt"));
                row.put("meta", rs.getString("meta"));
                list.add(row);
            }
        }

        return list;
    }

    // =========================================================
    // 최근 변경 이력
    // 전용 감사로그 테이블이 없어서 운영 테이블 기반 최근 등록 이력으로 대체
    // =========================================================
    public List<Map<String, Object>> selectAuditLogList(Date baseDate) throws Exception {
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

        String sql =
                "SELECT change_time_text, category, target_name, user_name, change_summary " +
                "  FROM ( " +
                "        SELECT change_time_text, category, target_name, user_name, change_summary " +
                "          FROM ( " +
                "                SELECT NVL(PP.CREATED_AT, CAST(PP.PLAN_DATE AS TIMESTAMP)) AS sort_time, " +
                "                       TO_CHAR(NVL(PP.CREATED_AT, CAST(PP.PLAN_DATE AS TIMESTAMP)), 'YYYY-MM-DD HH24:MI') AS change_time_text, " +
                "                       '생산계획' AS category, " +
                "                       'PP-' || TO_CHAR(NVL(PP.CREATED_AT, PP.PLAN_DATE), 'YYYYMMDD') || '-' || PP.PLAN_ID AS target_name, " +
                "                       '-' AS user_name, " +
                "                       NVL(I.ITEM_NAME, '-') || ' 생산계획 등록/변경' AS change_summary " +
                "                  FROM PRODUCTION_PLAN PP " +
                "                  LEFT JOIN ITEM I ON I.ITEM_ID = PP.ITEM_ID " +
                "                 WHERE NVL(PP.USE_YN, 'Y') = 'Y' " +
                "                UNION ALL " +
                "                SELECT NVL(WO.CREATED_AT, CAST(PP.PLAN_DATE AS TIMESTAMP)) AS sort_time, " +
                "                       TO_CHAR(NVL(WO.CREATED_AT, CAST(PP.PLAN_DATE AS TIMESTAMP)), 'YYYY-MM-DD HH24:MI') AS change_time_text, " +
                "                       '작업지시' AS category, " +
                "                       'WO-' || TO_CHAR(NVL(WO.CREATED_AT, PP.PLAN_DATE), 'YYYYMMDD') || '-' || WO.WORK_ORDER_ID AS target_name, " +
                "                       NVL(E.EMP_NAME, '-') AS user_name, " +
                "                       NVL(I.ITEM_NAME, '-') || ' 작업지시 등록/변경' AS change_summary " +
                "                  FROM WORK_ORDER WO " +
                "                  LEFT JOIN PRODUCTION_PLAN PP ON PP.PLAN_ID = WO.PLAN_ID " +
                "                  LEFT JOIN ITEM I ON I.ITEM_ID = WO.ITEM_ID " +
                "                  LEFT JOIN EMP E ON E.EMP_ID = WO.EMP_ID " +
                "                 WHERE NVL(WO.USE_YN, 'Y') = 'Y' " +
                "                UNION ALL " +
                "                SELECT CAST(PR.RESULT_DATE AS TIMESTAMP) AS sort_time, " +
                "                       TO_CHAR(PR.RESULT_DATE, 'YYYY-MM-DD HH24:MI') AS change_time_text, " +
                "                       '생산실적' AS category, " +
                "                       'PR-' || TO_CHAR(PR.RESULT_DATE, 'YYYYMMDD') || '-' || PR.RESULT_ID AS target_name, " +
                "                       NVL(E.EMP_NAME, '-') AS user_name, " +
                "                       NVL(I.ITEM_NAME, '-') || ' 생산실적 등록' AS change_summary " +
                "                  FROM PRODUCTION_RESULT PR " +
                "                  LEFT JOIN WORK_ORDER WO ON WO.WORK_ORDER_ID = PR.WORK_ORDER_ID " +
                "                  LEFT JOIN ITEM I ON I.ITEM_ID = WO.ITEM_ID " +
                "                  LEFT JOIN EMP E ON E.EMP_ID = WO.EMP_ID " +
                "                 WHERE NVL(PR.USE_YN, 'Y') = 'Y' " +
                "                UNION ALL " +
                "                SELECT CAST(FI.INSPECTION_DATE AS TIMESTAMP) AS sort_time, " +
                "                       TO_CHAR(FI.INSPECTION_DATE, 'YYYY-MM-DD HH24:MI') AS change_time_text, " +
                "                       '최종검사' AS category, " +
                "                       'FI-' || TO_CHAR(FI.INSPECTION_DATE, 'YYYYMMDD') || '-' || FI.FINAL_INSPECTION_ID AS target_name, " +
                "                       NVL(E.EMP_NAME, '-') AS user_name, " +
                "                       NVL(I.ITEM_NAME, '-') || ' 최종검사 등록' AS change_summary " +
                "                  FROM FINAL_INSPECTION FI " +
                "                  LEFT JOIN PRODUCTION_RESULT PR ON PR.RESULT_ID = FI.RESULT_ID " +
                "                  LEFT JOIN WORK_ORDER WO ON WO.WORK_ORDER_ID = PR.WORK_ORDER_ID " +
                "                  LEFT JOIN ITEM I ON I.ITEM_ID = WO.ITEM_ID " +
                "                  LEFT JOIN EMP E ON E.EMP_ID = FI.EMP_ID " +
                "                 WHERE NVL(FI.USE_YN, 'Y') = 'Y' " +
                "               ) logs " +
                "         ORDER BY sort_time DESC " +
                "       ) " +
                " WHERE ROWNUM <= 10";

        try (
            Connection conn = getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery()
        ) {
            while (rs.next()) {
                Map<String, Object> row = new LinkedHashMap<String, Object>();
                row.put("changeTimeText", rs.getString("change_time_text"));
                row.put("category", rs.getString("category"));
                row.put("targetName", rs.getString("target_name"));
                row.put("userName", rs.getString("user_name"));
                row.put("changeSummary", rs.getString("change_summary"));
                list.add(row);
            }
        }

        return list;
    }

    // =========================================================
    // 시스템 공지
    // 전용 공지 테이블 없으면 빈 목록
    // =========================================================
    public List<Map<String, Object>> selectNoticeList(Date baseDate) throws Exception {
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

        String sql =
                "SELECT CAST(NULL AS VARCHAR2(200)) AS title, " +
                "       CAST(NULL AS VARCHAR2(30)) AS notice_type, " +
                "       CAST(NULL AS VARCHAR2(50)) AS notice_date_text, " +
                "       CAST(NULL AS VARCHAR2(50)) AS writer_name, " +
                "       CAST(NULL AS VARCHAR2(400)) AS content_preview " +
                "  FROM DUAL " +
                " WHERE 1 = 0";

        try (
            Connection conn = getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery()
        ) {
            while (rs.next()) {
                Map<String, Object> row = new LinkedHashMap<String, Object>();
                row.put("title", rs.getString("title"));
                row.put("noticeType", rs.getString("notice_type"));
                row.put("noticeDateText", rs.getString("notice_date_text"));
                row.put("writerName", rs.getString("writer_name"));
                row.put("contentPreview", rs.getString("content_preview"));
                list.add(row);
            }
        }

        return list;
    }
}