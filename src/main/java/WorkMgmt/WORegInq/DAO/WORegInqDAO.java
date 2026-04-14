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

public class WORegInqDAO {

    // context.xml / server.xml 에 설정한 JNDI 이름으로 바꿔서 사용
    private Connection getConnection() throws Exception {
        Context initContext = new InitialContext();
        Context envContext = (Context) initContext.lookup("java:comp/env");
        DataSource ds = (DataSource) envContext.lookup("jdbc/oracle");
        return ds.getConnection();
    }

    public List<WORegInqDTO> selectList(String startDate, String endDate, String searchType, String keyword,
            int startRow, int endRow) throws Exception {

        List<WORegInqDTO> list = new ArrayList<>();
        List<Object> params = new ArrayList<>();

        String baseSql = buildBaseSql(startDate, endDate, params);

        StringBuilder sql = new StringBuilder();
        sql.append("SELECT * ");
        sql.append("  FROM ( ");
        sql.append("        SELECT ROW_NUMBER() OVER (ORDER BY T.WORK_DATE DESC, T.WORK_ORDER_ID DESC) AS RN, T.* ");
        sql.append("          FROM ( ");
        sql.append(baseSql);
        sql.append("               ) T ");
        sql.append("         WHERE 1 = 1 ");

        appendSearchFilter(sql, params, searchType, keyword);

        sql.append("       ) ");
        sql.append(" WHERE RN BETWEEN ? AND ? ");

        params.add(startRow);
        params.add(endRow);

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql.toString())) {

            bindParams(pstmt, params);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    WORegInqDTO dto = new WORegInqDTO();

                    dto.setSeqNo(rs.getInt("RN"));
                    dto.setWorkOrderId(rs.getInt("WORK_ORDER_ID"));
                    dto.setWorkOrderNo(rs.getString("WORK_ORDER_NO"));
                    dto.setWorkDate(rs.getDate("WORK_DATE"));
                    dto.setItemCode(rs.getString("ITEM_CODE"));
                    dto.setItemName(rs.getString("ITEM_NAME"));
                    dto.setWorkQty(rs.getBigDecimal("WORK_QTY"));
                    dto.setUnitName(rs.getString("UNIT_NAME"));
                    dto.setLineName(rs.getString("LINE_NAME"));
                    dto.setProcessName(rs.getString("PROCESS_NAME"));
                    dto.setEmpName(rs.getString("EMP_NAME"));
                    dto.setBomName(rs.getString("BOM_NAME"));
                    dto.setRemark(rs.getString("REMARK"));

                    list.add(dto);
                }
            }
        }

        return list;
    }

    public int selectTotalCount(String startDate, String endDate, String searchType, String keyword) throws Exception {
        List<Object> params = new ArrayList<>();

        String baseSql = buildBaseSql(startDate, endDate, params);

        StringBuilder sql = new StringBuilder();
        sql.append("SELECT COUNT(*) ");
        sql.append("  FROM ( ");
        sql.append(baseSql);
        sql.append("       ) T ");
        sql.append(" WHERE 1 = 1 ");

        appendSearchFilter(sql, params, searchType, keyword);

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql.toString())) {

            bindParams(pstmt, params);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        }

        return 0;
    }

    public int getNextWorkOrderId() throws Exception {
        String sql = "SELECT SEQ_WORK_ORDER.NEXTVAL FROM DUAL";

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            if (rs.next()) {
                return rs.getInt(1);
            }
        }

        throw new IllegalStateException("작업지시 시퀀스 값을 가져오지 못했습니다.");
    }

    public Integer findItemIdByCode(String itemCode) throws Exception {
        String sql = ""
                + "SELECT ITEM_ID "
                + "  FROM ITEM "
                + " WHERE ITEM_CODE = ? "
                + "   AND NVL(USE_YN, 'Y') = 'Y'";

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, itemCode);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("ITEM_ID");
                }
            }
        }

        return null;
    }

    public Integer findEmpIdByName(String empName) throws Exception {
        String sql = ""
                + "SELECT EMP_ID "
                + "  FROM ( "
                + "        SELECT EMP_ID "
                + "          FROM EMP "
                + "         WHERE EMP_NAME = ? "
                + "           AND NVL(USE_YN, 'Y') = 'Y' "
                + "         ORDER BY EMP_ID DESC "
                + "       ) "
                + " WHERE ROWNUM = 1";

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, empName);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("EMP_ID");
                }
            }
        }

        return null;
    }

    public Integer findPlanId(int itemId, Date workDate, String lineCode) throws Exception {
        Integer planId = findExactPlanId(itemId, workDate, lineCode);
        if (planId != null) {
            return planId;
        }
        return findNearestPlanId(itemId, workDate, lineCode);
    }

    private Integer findExactPlanId(int itemId, Date workDate, String lineCode) throws Exception {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT PLAN_ID ");
        sql.append("  FROM ( ");
        sql.append("        SELECT PLAN_ID ");
        sql.append("          FROM PRODUCTION_PLAN ");
        sql.append("         WHERE ITEM_ID = ? ");
        sql.append("           AND PLAN_DATE = ? ");

        if (!isBlank(lineCode)) {
            sql.append("           AND LINE_CODE = ? ");
        }

        sql.append("         ORDER BY PLAN_ID DESC ");
        sql.append("       ) ");
        sql.append(" WHERE ROWNUM = 1 ");

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql.toString())) {

            int idx = 1;
            pstmt.setInt(idx++, itemId);
            pstmt.setDate(idx++, workDate);
            if (!isBlank(lineCode)) {
                pstmt.setString(idx++, lineCode);
            }

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("PLAN_ID");
                }
            }
        }

        return null;
    }

    private Integer findNearestPlanId(int itemId, Date workDate, String lineCode) throws Exception {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT PLAN_ID ");
        sql.append("  FROM ( ");
        sql.append("        SELECT PLAN_ID ");
        sql.append("          FROM PRODUCTION_PLAN ");
        sql.append("         WHERE ITEM_ID = ? ");

        if (!isBlank(lineCode)) {
            sql.append("           AND LINE_CODE = ? ");
        }

        sql.append("         ORDER BY ABS(PLAN_DATE - ?), PLAN_DATE DESC, PLAN_ID DESC ");
        sql.append("       ) ");
        sql.append(" WHERE ROWNUM = 1 ");

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql.toString())) {

            int idx = 1;
            pstmt.setInt(idx++, itemId);
            if (!isBlank(lineCode)) {
                pstmt.setString(idx++, lineCode);
            }
            pstmt.setDate(idx++, workDate);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("PLAN_ID");
                }
            }
        }

        return null;
    }

    public int insert(WORegInqDTO dto, int itemId, int planId, int empId) throws Exception {
        String sql = ""
                + "INSERT INTO WORK_ORDER ( "
                + "    WORK_ORDER_ID, ITEM_ID, PLAN_ID, EMP_ID, "
                + "    WORK_DATE, WORK_QTY, STATUS, REMARK, CREATED_AT, UPDATED_AT "
                + ") VALUES ( "
                + "    ?, ?, ?, ?, "
                + "    ?, ?, ?, ?, SYSDATE, SYSDATE "
                + ")";

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            int idx = 1;
            pstmt.setInt(idx++, dto.getWorkOrderId());
            pstmt.setInt(idx++, itemId);
            pstmt.setInt(idx++, planId);
            pstmt.setInt(idx++, empId);
            pstmt.setDate(idx++, dto.getWorkDate());
            pstmt.setBigDecimal(idx++, dto.getWorkQty());
            pstmt.setString(idx++, "대기");
            pstmt.setString(idx++, dto.getRemark());

            return pstmt.executeUpdate();
        }
    }

    public int deleteById(int workOrderId) throws Exception {
        String sql = "DELETE FROM WORK_ORDER WHERE WORK_ORDER_ID = ?";

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, workOrderId);
            return pstmt.executeUpdate();
        }
    }

    private String buildBaseSql(String startDate, String endDate, List<Object> params) {
        StringBuilder sql = new StringBuilder();

        sql.append("SELECT ");
        sql.append("       wo.WORK_ORDER_ID, ");
        sql.append("       NVL( ");
        sql.append("           REPLACE(REPLACE(REGEXP_SUBSTR(wo.REMARK, '\\\\[WO-[^]]+\\\\]'), '[', ''), ']', ''), ");
        sql.append("           'WO-' || TO_CHAR(wo.WORK_DATE, 'YYYYMMDD') || '-' || LPAD(wo.WORK_ORDER_ID, 3, '0') ");
        sql.append("       ) AS WORK_ORDER_NO, ");
        sql.append("       wo.WORK_DATE, ");
        sql.append("       it.ITEM_CODE, ");
        sql.append("       it.ITEM_NAME, ");
        sql.append("       wo.WORK_QTY, ");
        sql.append("       it.UNIT AS UNIT_NAME, ");
        sql.append("       NVL(pp.LINE_CODE, TRIM(REGEXP_SUBSTR(wo.REMARK, '라인=([^/]+)', 1, 1, NULL, 1))) AS LINE_NAME, ");
        sql.append("       NVL( ");
        sql.append("           (SELECT X.PROCESS_CODE ");
        sql.append("              FROM ( ");
        sql.append("                    SELECT p.PROCESS_CODE ");
        sql.append("                      FROM ROUTING r ");
        sql.append("                      JOIN PROCESS p ");
        sql.append("                        ON p.PROCESS_ID = r.PROCESS_ID ");
        sql.append("                     WHERE r.ITEM_ID = pp.ITEM_ID ");
        sql.append("                     ORDER BY r.PROCESS_SEQ ");
        sql.append("                   ) X ");
        sql.append("             WHERE ROWNUM = 1), ");
        sql.append("           TRIM(REGEXP_SUBSTR(wo.REMARK, '공정=([^/]+)', 1, 1, NULL, 1)) ");
        sql.append("       ) AS PROCESS_NAME, ");
        sql.append("       e.EMP_NAME, ");
        sql.append("       NVL( ");
        sql.append("           (SELECT X.BOM_NAME ");
        sql.append("              FROM ( ");
        sql.append("                    SELECT 'BOM-' || i2.ITEM_CODE || '-V' || LPAD(NVL(b.VERSION_NO, 1), 2, '0') AS BOM_NAME ");
        sql.append("                      FROM BOM b ");
        sql.append("                      JOIN ITEM i2 ");
        sql.append("                        ON i2.ITEM_ID = b.ITEM_ID ");
        sql.append("                     WHERE b.ITEM_ID = pp.ITEM_ID ");
        sql.append("                       AND NVL(b.USE_YN, 'Y') = 'Y' ");
        sql.append("                     ORDER BY b.VERSION_NO DESC ");
        sql.append("                   ) X ");
        sql.append("             WHERE ROWNUM = 1), ");
        sql.append("           TRIM(REGEXP_SUBSTR(wo.REMARK, 'BOM=([^/]+)', 1, 1, NULL, 1)) ");
        sql.append("       ) AS BOM_NAME, ");
        sql.append("       wo.REMARK ");
        sql.append("  FROM WORK_ORDER wo ");
        sql.append("  JOIN PRODUCTION_PLAN pp ");
        sql.append("    ON pp.PLAN_ID = wo.PLAN_ID ");
        sql.append("  JOIN ITEM it ");
        sql.append("    ON it.ITEM_ID = pp.ITEM_ID ");
        sql.append("  JOIN EMP e ");
        sql.append("    ON e.EMP_ID = wo.EMP_ID ");
        sql.append(" WHERE 1 = 1 ");

        if (!isBlank(startDate)) {
            sql.append(" AND wo.WORK_DATE >= TO_DATE(?, 'YYYY-MM-DD') ");
            params.add(startDate);
        }

        if (!isBlank(endDate)) {
            sql.append(" AND wo.WORK_DATE <= TO_DATE(?, 'YYYY-MM-DD') ");
            params.add(endDate);
        }

        return sql.toString();
    }

    private void appendSearchFilter(StringBuilder sql, List<Object> params, String searchType, String keyword) {
        if (isBlank(keyword)) {
            return;
        }

        String likeKeyword = "%" + keyword.trim() + "%";

        if (isBlank(searchType)) {
            sql.append(" AND ( ");
            sql.append("      UPPER(T.WORK_ORDER_NO) LIKE UPPER(?) ");
            sql.append("   OR UPPER(T.ITEM_CODE) LIKE UPPER(?) ");
            sql.append("   OR UPPER(T.ITEM_NAME) LIKE UPPER(?) ");
            sql.append("   OR UPPER(T.LINE_NAME) LIKE UPPER(?) ");
            sql.append("   OR UPPER(T.PROCESS_NAME) LIKE UPPER(?) ");
            sql.append("   OR UPPER(T.EMP_NAME) LIKE UPPER(?) ");
            sql.append(" ) ");

            params.add(likeKeyword);
            params.add(likeKeyword);
            params.add(likeKeyword);
            params.add(likeKeyword);
            params.add(likeKeyword);
            params.add(likeKeyword);
            return;
        }

        String column = getSearchColumn(searchType);
        if (column == null) {
            return;
        }

        sql.append(" AND UPPER(T.").append(column).append(") LIKE UPPER(?) ");
        params.add(likeKeyword);
    }

    private String getSearchColumn(String searchType) {
        switch (searchType) {
            case "workOrderNo":
                return "WORK_ORDER_NO";
            case "itemCode":
                return "ITEM_CODE";
            case "itemName":
                return "ITEM_NAME";
            case "lineName":
                return "LINE_NAME";
            case "processName":
                return "PROCESS_NAME";
            case "empName":
                return "EMP_NAME";
            default:
                return null;
        }
    }

    private void bindParams(PreparedStatement pstmt, List<Object> params) throws Exception {
        for (int i = 0; i < params.size(); i++) {
            Object value = params.get(i);

            if (value instanceof Integer) {
                pstmt.setInt(i + 1, (Integer) value);
            } else if (value instanceof Long) {
                pstmt.setLong(i + 1, (Long) value);
            } else if (value instanceof Date) {
                pstmt.setDate(i + 1, (Date) value);
            } else {
                pstmt.setString(i + 1, value == null ? null : value.toString());
            }
        }
    }

    private boolean isBlank(String str) {
        return str == null || str.trim().isEmpty();
    }
}