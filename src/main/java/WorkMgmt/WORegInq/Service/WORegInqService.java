package WorkMgmt.WORegInq.Service;

import java.util.List;

import WorkMgmt.WORegInq.DAO.WORegInqDAO;
import WorkMgmt.WORegInq.DTO.WORegInqDTO;

/*
 * 작업지시 등록/조회 Service
 *
 * 역할
 * - Controller 와 DAO 사이의 중간 계층
 * - 현재는 조회 / 삭제 기능을 DAO에 위임한다
 *
 * 포함 기능
 * 1. 검색 조건 반영 전체 건수 조회
 * 2. 검색 조건 반영 페이지별 목록 조회
 * 3. 선택된 작업지시 논리삭제
 */
public class WORegInqService {

    // DAO 객체 생성
    private WORegInqDAO dao = new WORegInqDAO();

    /*
     * 전체 건수 조회
     *
     * 용도
     * - 페이징 처리 시 totalCount 계산
     *
     * 파라미터
     * - startDate  : 시작일
     * - endDate    : 종료일
     * - searchType : 검색 기준
     * - keyword    : 검색어
     *
     * 반환값
     * - 검색 조건이 반영된 전체 건수
     */
    public int getTotalCount(String startDate, String endDate, String searchType, String keyword) {
        return dao.getTotalCount(startDate, endDate, searchType, keyword);
    }

    /*
     * 페이지별 목록 조회
     *
     * 용도
     * - 현재 페이지에 필요한 작업지시 목록 조회
     *
     * 파라미터
     * - startDate  : 시작일
     * - endDate    : 종료일
     * - searchType : 검색 기준
     * - keyword    : 검색어
     * - startRow   : 시작 행 번호
     * - endRow     : 끝 행 번호
     *
     * 반환값
     * - 현재 페이지 범위에 해당하는 작업지시 목록
     */
    public List<WORegInqDTO> getListByPage(
            String startDate,
            String endDate,
            String searchType,
            String keyword,
            int startRow,
            int endRow) {

        return dao.getListByPage(startDate, endDate, searchType, keyword, startRow, endRow);
    }

    /*
     * 선택된 작업지시 논리삭제
     *
     * 용도
     * - 체크박스로 선택된 작업지시를 삭제할 때 사용
     * - 실제 삭제가 아니라 USE_YN = 'N' 처리
     *
     * 파라미터
     * - seqNos : 체크된 WORK_ORDER_ID 배열
     *
     * 반환값
     * - 업데이트된 행 수
     */
    public int deleteByIds(String[] seqNos) {
        return dao.deleteByIds(seqNos);
    }
}