package QualityMgmt.FPInspRegInq.Service;

import java.util.List;

import QualityMgmt.FPInspRegInq.DAO.FPInspRegInqDAO;
import QualityMgmt.FPInspRegInq.DTO.FPInspRegInqDTO;

/*
 * 완제품 검사 등록 / 조회 Service
 */
public class FPInspRegInqService {

    // 목록 조회
    public List<FPInspRegInqDTO> getFPInspRegInqList(FPInspRegInqDTO searchDTO) {
        FPInspRegInqDAO dao = new FPInspRegInqDAO();
        return dao.selectFPInspRegInqList(searchDTO);
    }

    // 상세 조회
    public FPInspRegInqDTO getFPInspRegInqDetail(int finalInspectionId) {
        FPInspRegInqDAO dao = new FPInspRegInqDAO();
        return dao.selectFPInspRegInqOne(finalInspectionId);
    }
}