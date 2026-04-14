package ProdMgmt.ProdPerfRegInq.Service;

import java.util.List;

import ProdMgmt.ProdPerfRegInq.DAO.ProdPerfRegInqDAO;
import ProdMgmt.ProdPerfRegInq.DTO.ProdPerfRegInqDTO;

public class ProdPerfRegInqService {

    private ProdPerfRegInqDAO dao = new ProdPerfRegInqDAO();

    // 검색 조건이 반영된 전체 건수 조회
    public int getTotalCount(String startDate, String endDate, String searchType, String keyword) {
        return dao.getTotalCount(startDate, endDate, searchType, keyword);
    }

    // 검색 조건이 반영된 페이지별 목록 조회
    public List<ProdPerfRegInqDTO> getListByPage(
            String startDate, String endDate, String searchType, String keyword,
            int startRow, int endRow) {
        return dao.getListByPage(startDate, endDate, searchType, keyword, startRow, endRow);
    }

    // 체크된 생산실적 논리삭제
    public int deleteByIds(String[] seqNos) {
        return dao.deleteByIds(seqNos);
    }
}