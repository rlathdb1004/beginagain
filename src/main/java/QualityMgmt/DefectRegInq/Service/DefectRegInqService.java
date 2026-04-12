package QualityMgmt.DefectRegInq.Service;

import java.util.List;

import QualityMgmt.DefectRegInq.DAO.DefectRegInqDAO;
import QualityMgmt.DefectRegInq.DTO.DefectRegInqDTO;

/*
 * 불량 등록 / 조회 Service
 */
public class DefectRegInqService {

    // 목록 조회
    public List<DefectRegInqDTO> getDefectRegInqList(DefectRegInqDTO searchDTO) {
        DefectRegInqDAO dao = new DefectRegInqDAO();
        return dao.selectDefectRegInqList(searchDTO);
    }

    // 상세 조회
    public DefectRegInqDTO getDefectRegInqDetail(int defectProductId) {
        DefectRegInqDAO dao = new DefectRegInqDAO();
        return dao.selectDefectRegInqOne(defectProductId);
    }
}