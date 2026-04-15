package Notice.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

import Notice.DTO.NoticeDTO;

/*
 * 공지사항 DAO
 * - 실제 DB 컬럼명 WRITER_EMP_ID 사용
 * - 관리자 화면 기준으로 작성
 * - 삭제된 공지(USE_YN='N')는 목록에서 제외
 * - 내려진 공지(STATUS='내림')는 관리자 목록에서 보이도록 유지
 */
public class NoticeDAO {

    private DataSource noDataSource;

    public NoticeDAO() {
        try {
            Context noInitContext = new InitialContext();

            /*
             * 기존 프로젝트에서 사용 중인 JNDI 이름과 동일하게 유지할 것
             */
            noDataSource = (DataSource) noInitContext.lookup("java:comp/env/jdbc/oracle");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*
     * 공지사항 총 건수 조회
     * - 삭제되지 않은 공지 기준
     * - 검색 / 상태필터 반영
     */
    public int getNoticeCount(String noSearchType, String noKeyword, String noStatusFilter) {

        int noCount = 0;

        Connection noConn = null;
        PreparedStatement noPstmt = null;
        ResultSet noRs = null;

        StringBuilder noSql = new StringBuilder();

        try {
            noConn = noDataSource.getConnection();

            noSql.append(" SELECT COUNT(*) ");
            noSql.append("   FROM NOTICE_BOARD NB ");
            noSql.append("   LEFT JOIN EMP E ");
            noSql.append("     ON NB.WRITER_EMP_ID = E.EMP_ID ");
            noSql.append("  WHERE NB.USE_YN = 'Y' ");

            if (noStatusFilter != null && !"".equals(noStatusFilter) && !"all".equals(noStatusFilter)) {
                noSql.append(" AND NB.STATUS = ? ");
            }

            if (noKeyword != null && !noKeyword.trim().isEmpty()) {
                if ("title".equals(noSearchType)) {
                    noSql.append(" AND NB.TITLE LIKE ? ");
                } else if ("content".equals(noSearchType)) {
                    noSql.append(" AND NB.CONTENT LIKE ? ");
                } else if ("writer".equals(noSearchType)) {
                    noSql.append(" AND E.EMP_NAME LIKE ? ");
                } else {
                    noSql.append(" AND (NB.TITLE LIKE ? OR NB.CONTENT LIKE ? OR E.EMP_NAME LIKE ?) ");
                }
            }

            noPstmt = noConn.prepareStatement(noSql.toString());

            int noParamIndex = 1;

            if (noStatusFilter != null && !"".equals(noStatusFilter) && !"all".equals(noStatusFilter)) {
                noPstmt.setString(noParamIndex++, noStatusFilter);
            }

            noParamIndex = setSearchParameter(noPstmt, noParamIndex, noSearchType, noKeyword);

            noRs = noPstmt.executeQuery();

            if (noRs.next()) {
                noCount = noRs.getInt(1);
            }

        } catch (Exception e) {
            System.out.println("공지사항 총 건수 조회 오류");
            e.printStackTrace();
        } finally {
            close(noRs, noPstmt, noConn);
        }

        return noCount;
    }

    /*
     * 공지사항 목록 조회
     * - 삭제되지 않은 공지 기준
     * - 관리자 화면이므로 게시 / 내림 모두 조회 가능
     * - 게시 상태 공지를 우선 정렬 후 NOTICE_ID 내림차순
     */
    public List<NoticeDTO> getNoticeList(int noStartRow, int noEndRow, String noSearchType, String noKeyword, String noStatusFilter) {

        List<NoticeDTO> noNoticeList = new ArrayList<NoticeDTO>();

        Connection noConn = null;
        PreparedStatement noPstmt = null;
        ResultSet noRs = null;

        StringBuilder noSql = new StringBuilder();

        try {
            noConn = noDataSource.getConnection();

            noSql.append(" SELECT * ");
            noSql.append("   FROM ( ");
            noSql.append("         SELECT ROW_NUMBER() OVER ( ");
            noSql.append("                    ORDER BY CASE WHEN NB.STATUS = '게시' THEN 0 ELSE 1 END, NB.NOTICE_ID DESC ");
            noSql.append("                ) AS RN, ");
            noSql.append("                NB.NOTICE_ID, ");
            noSql.append("                NB.TITLE, ");
            noSql.append("                NB.CONTENT, ");
            noSql.append("                NB.WRITER_EMP_ID, ");
            noSql.append("                NVL(E.EMP_NAME, '작성자없음') AS WRITER_NAME, ");
            noSql.append("                NB.STATUS, ");
            noSql.append("                NB.VIEW_COUNT, ");
            noSql.append("                NB.USE_YN, ");
            noSql.append("                NB.REMARK, ");
            noSql.append("                TO_CHAR(NB.CREATED_AT, 'YYYY-MM-DD') AS CREATED_AT_STR, ");
            noSql.append("                TO_CHAR(NB.UPDATED_AT, 'YYYY-MM-DD') AS UPDATED_AT_STR ");
            noSql.append("           FROM NOTICE_BOARD NB ");
            noSql.append("           LEFT JOIN EMP E ");
            noSql.append("             ON NB.WRITER_EMP_ID = E.EMP_ID ");
            noSql.append("          WHERE NB.USE_YN = 'Y' ");

            if (noStatusFilter != null && !"".equals(noStatusFilter) && !"all".equals(noStatusFilter)) {
                noSql.append(" AND NB.STATUS = ? ");
            }

            if (noKeyword != null && !noKeyword.trim().isEmpty()) {
                if ("title".equals(noSearchType)) {
                    noSql.append(" AND NB.TITLE LIKE ? ");
                } else if ("content".equals(noSearchType)) {
                    noSql.append(" AND NB.CONTENT LIKE ? ");
                } else if ("writer".equals(noSearchType)) {
                    noSql.append(" AND E.EMP_NAME LIKE ? ");
                } else {
                    noSql.append(" AND (NB.TITLE LIKE ? OR NB.CONTENT LIKE ? OR E.EMP_NAME LIKE ?) ");
                }
            }

            noSql.append("        ) ");
            noSql.append("  WHERE RN BETWEEN ? AND ? ");
            noSql.append("  ORDER BY RN ");

            noPstmt = noConn.prepareStatement(noSql.toString());

            int noParamIndex = 1;

            if (noStatusFilter != null && !"".equals(noStatusFilter) && !"all".equals(noStatusFilter)) {
                noPstmt.setString(noParamIndex++, noStatusFilter);
            }

            noParamIndex = setSearchParameter(noPstmt, noParamIndex, noSearchType, noKeyword);

            noPstmt.setInt(noParamIndex++, noStartRow);
            noPstmt.setInt(noParamIndex, noEndRow);

            noRs = noPstmt.executeQuery();

            while (noRs.next()) {
                NoticeDTO noDto = new NoticeDTO();

                noDto.setNoticeId(noRs.getInt("NOTICE_ID"));
                noDto.setTitle(noRs.getString("TITLE"));
                noDto.setContent(noRs.getString("CONTENT"));
                noDto.setWriterId(noRs.getInt("WRITER_EMP_ID"));
                noDto.setWriterName(noRs.getString("WRITER_NAME"));
                noDto.setStatus(noRs.getString("STATUS"));
                noDto.setViewCount(noRs.getInt("VIEW_COUNT"));
                noDto.setUseYn(noRs.getString("USE_YN"));
                noDto.setRemark(noRs.getString("REMARK"));
                noDto.setCreatedAtStr(noRs.getString("CREATED_AT_STR"));
                noDto.setUpdatedAtStr(noRs.getString("UPDATED_AT_STR"));

                noNoticeList.add(noDto);
            }

        } catch (Exception e) {
            System.out.println("공지사항 목록 조회 오류");
            e.printStackTrace();
        } finally {
            close(noRs, noPstmt, noConn);
        }

        return noNoticeList;
    }

    /*
     * 공지사항 상세 조회
     */
    public NoticeDTO getNoticeDetail(int noNoticeId) {

        NoticeDTO noDto = null;

        Connection noConn = null;
        PreparedStatement noPstmt = null;
        ResultSet noRs = null;

        String noSql = ""
                + " SELECT NB.NOTICE_ID, "
                + "        NB.TITLE, "
                + "        NB.CONTENT, "
                + "        NB.WRITER_EMP_ID, "
                + "        NVL(E.EMP_NAME, '작성자없음') AS WRITER_NAME, "
                + "        NB.STATUS, "
                + "        NB.VIEW_COUNT, "
                + "        NB.USE_YN, "
                + "        NB.REMARK, "
                + "        TO_CHAR(NB.CREATED_AT, 'YYYY-MM-DD HH24:MI') AS CREATED_AT_STR, "
                + "        TO_CHAR(NB.UPDATED_AT, 'YYYY-MM-DD HH24:MI') AS UPDATED_AT_STR "
                + "   FROM NOTICE_BOARD NB "
                + "   LEFT JOIN EMP E "
                + "     ON NB.WRITER_EMP_ID = E.EMP_ID "
                + "  WHERE NB.NOTICE_ID = ? "
                + "    AND NB.USE_YN = 'Y' ";

        try {
            noConn = noDataSource.getConnection();
            noPstmt = noConn.prepareStatement(noSql);
            noPstmt.setInt(1, noNoticeId);

            noRs = noPstmt.executeQuery();

            if (noRs.next()) {
                noDto = new NoticeDTO();

                noDto.setNoticeId(noRs.getInt("NOTICE_ID"));
                noDto.setTitle(noRs.getString("TITLE"));
                noDto.setContent(noRs.getString("CONTENT"));
                noDto.setWriterId(noRs.getInt("WRITER_EMP_ID"));
                noDto.setWriterName(noRs.getString("WRITER_NAME"));
                noDto.setStatus(noRs.getString("STATUS"));
                noDto.setViewCount(noRs.getInt("VIEW_COUNT"));
                noDto.setUseYn(noRs.getString("USE_YN"));
                noDto.setRemark(noRs.getString("REMARK"));
                noDto.setCreatedAtStr(noRs.getString("CREATED_AT_STR"));
                noDto.setUpdatedAtStr(noRs.getString("UPDATED_AT_STR"));
            }

        } catch (Exception e) {
            System.out.println("공지사항 상세 조회 오류");
            e.printStackTrace();
        } finally {
            close(noRs, noPstmt, noConn);
        }

        return noDto;
    }

    /*
     * 공지사항 등록
     * - 시퀀스를 모르므로 MAX+1 방식 사용
     */
    public int insertNotice(NoticeDTO noDto) {

        int noResult = 0;

        Connection noConn = null;
        PreparedStatement noPstmt = null;

        String noSql = ""
                + " INSERT INTO NOTICE_BOARD ( "
                + "        NOTICE_ID, TITLE, CONTENT, WRITER_EMP_ID, STATUS, VIEW_COUNT, USE_YN, REMARK, CREATED_AT, UPDATED_AT "
                + " ) VALUES ( "
                + "        (SELECT NVL(MAX(NOTICE_ID), 0) + 1 FROM NOTICE_BOARD), "
                + "        ?, ?, ?, ?, 0, 'Y', ?, SYSDATE, SYSDATE "
                + " ) ";

        try {
            noConn = noDataSource.getConnection();
            noPstmt = noConn.prepareStatement(noSql);

            noPstmt.setString(1, noDto.getTitle());
            noPstmt.setString(2, noDto.getContent());
            noPstmt.setInt(3, noDto.getWriterId());
            noPstmt.setString(4, noDto.getStatus());
            noPstmt.setString(5, noDto.getRemark());

            noResult = noPstmt.executeUpdate();

        } catch (Exception e) {
            System.out.println("공지사항 등록 오류");
            e.printStackTrace();
        } finally {
            close(null, noPstmt, noConn);
        }

        return noResult;
    }

    /*
     * 공지사항 수정
     */
    public int updateNotice(NoticeDTO noDto) {

        int noResult = 0;

        Connection noConn = null;
        PreparedStatement noPstmt = null;

        String noSql = ""
                + " UPDATE NOTICE_BOARD "
                + "    SET TITLE = ?, "
                + "        CONTENT = ?, "
                + "        STATUS = ?, "
                + "        REMARK = ?, "
                + "        UPDATED_AT = SYSDATE "
                + "  WHERE NOTICE_ID = ? "
                + "    AND USE_YN = 'Y' ";

        try {
            noConn = noDataSource.getConnection();
            noPstmt = noConn.prepareStatement(noSql);

            noPstmt.setString(1, noDto.getTitle());
            noPstmt.setString(2, noDto.getContent());
            noPstmt.setString(3, noDto.getStatus());
            noPstmt.setString(4, noDto.getRemark());
            noPstmt.setInt(5, noDto.getNoticeId());

            noResult = noPstmt.executeUpdate();

        } catch (Exception e) {
            System.out.println("공지사항 수정 오류");
            e.printStackTrace();
        } finally {
            close(null, noPstmt, noConn);
        }

        return noResult;
    }

    /*
     * 조회수 증가
     */
    public int increaseViewCount(int noNoticeId) {

        int noResult = 0;

        Connection noConn = null;
        PreparedStatement noPstmt = null;

        String noSql = ""
                + " UPDATE NOTICE_BOARD "
                + "    SET VIEW_COUNT = VIEW_COUNT + 1, "
                + "        UPDATED_AT = SYSDATE "
                + "  WHERE NOTICE_ID = ? "
                + "    AND USE_YN = 'Y' ";

        try {
            noConn = noDataSource.getConnection();
            noPstmt = noConn.prepareStatement(noSql);
            noPstmt.setInt(1, noNoticeId);

            noResult = noPstmt.executeUpdate();

        } catch (Exception e) {
            System.out.println("조회수 증가 오류");
            e.printStackTrace();
        } finally {
            close(null, noPstmt, noConn);
        }

        return noResult;
    }

    /*
     * 공지 내리기
     */
    public int hideNotice(int noNoticeId) {

        int noResult = 0;

        Connection noConn = null;
        PreparedStatement noPstmt = null;

        String noSql = ""
                + " UPDATE NOTICE_BOARD "
                + "    SET STATUS = '내림', "
                + "        UPDATED_AT = SYSDATE "
                + "  WHERE NOTICE_ID = ? "
                + "    AND USE_YN = 'Y' ";

        try {
            noConn = noDataSource.getConnection();
            noPstmt = noConn.prepareStatement(noSql);
            noPstmt.setInt(1, noNoticeId);

            noResult = noPstmt.executeUpdate();

        } catch (Exception e) {
            System.out.println("공지 내리기 오류");
            e.printStackTrace();
        } finally {
            close(null, noPstmt, noConn);
        }

        return noResult;
    }

    /*
     * 공지 다시 게시
     */
    public int restoreNotice(int noNoticeId) {

        int noResult = 0;

        Connection noConn = null;
        PreparedStatement noPstmt = null;

        String noSql = ""
                + " UPDATE NOTICE_BOARD "
                + "    SET STATUS = '게시', "
                + "        UPDATED_AT = SYSDATE "
                + "  WHERE NOTICE_ID = ? "
                + "    AND USE_YN = 'Y' ";

        try {
            noConn = noDataSource.getConnection();
            noPstmt = noConn.prepareStatement(noSql);
            noPstmt.setInt(1, noNoticeId);

            noResult = noPstmt.executeUpdate();

        } catch (Exception e) {
            System.out.println("공지 다시 게시 오류");
            e.printStackTrace();
        } finally {
            close(null, noPstmt, noConn);
        }

        return noResult;
    }

    /*
     * 공지 삭제
     * - 물리삭제가 아니라 소프트 삭제
     */
    public int deleteNotice(int noNoticeId) {

        int noResult = 0;

        Connection noConn = null;
        PreparedStatement noPstmt = null;

        String noSql = ""
                + " UPDATE NOTICE_BOARD "
                + "    SET USE_YN = 'N', "
                + "        UPDATED_AT = SYSDATE "
                + "  WHERE NOTICE_ID = ? ";

        try {
            noConn = noDataSource.getConnection();
            noPstmt = noConn.prepareStatement(noSql);
            noPstmt.setInt(1, noNoticeId);

            noResult = noPstmt.executeUpdate();

        } catch (Exception e) {
            System.out.println("공지 삭제 오류");
            e.printStackTrace();
        } finally {
            close(null, noPstmt, noConn);
        }

        return noResult;
    }

    /*
     * 검색 파라미터 세팅 공통 메서드
     */
    private int setSearchParameter(PreparedStatement noPstmt, int noIndex, String noSearchType, String noKeyword) throws Exception {

        if (noKeyword != null && !noKeyword.trim().isEmpty()) {
            String noLikeKeyword = "%" + noKeyword.trim() + "%";

            if ("title".equals(noSearchType)) {
                noPstmt.setString(noIndex++, noLikeKeyword);
            } else if ("content".equals(noSearchType)) {
                noPstmt.setString(noIndex++, noLikeKeyword);
            } else if ("writer".equals(noSearchType)) {
                noPstmt.setString(noIndex++, noLikeKeyword);
            } else {
                noPstmt.setString(noIndex++, noLikeKeyword);
                noPstmt.setString(noIndex++, noLikeKeyword);
                noPstmt.setString(noIndex++, noLikeKeyword);
            }
        }

        return noIndex;
    }

    /*
     * JDBC 자원 해제
     */
    private void close(ResultSet noRs, PreparedStatement noPstmt, Connection noConn) {
        try {
            if (noRs != null) noRs.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            if (noPstmt != null) noPstmt.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            if (noConn != null) noConn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
}