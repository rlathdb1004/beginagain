package QualityMgmt.MatInspRegInq.Service;

import java.util.List;

import QualityMgmt.MatInspRegInq.DAO.MatInspRegInqDAO;
import QualityMgmt.MatInspRegInq.DTO.MatInspRegInqDTO;

/*
 * 자재 검사 등록 / 조회 Service
 */
public class MatInspRegInqService {

    // 목록 조회
    public List<MatInspRegInqDTO> getMatInspRegInqList(MatInspRegInqDTO searchDTO) {
        MatInspRegInqDAO dao = new MatInspRegInqDAO();
        return dao.selectMatInspRegInqList(searchDTO);
    }

    // 상세 조회
    public MatInspRegInqDTO getMatInspRegInqDetail(int materialInspectionId) {
        MatInspRegInqDAO dao = new MatInspRegInqDAO();
        return dao.selectMatInspRegInqOne(materialInspectionId);
    }
}