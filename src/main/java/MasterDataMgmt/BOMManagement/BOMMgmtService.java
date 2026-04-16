package MasterDataMgmt.BOMManagement;

import java.util.List;


public class BOMMgmtService {

    private BOMMgmtDAO dao = new BOMMgmtDAO();
    private BOMMgmtChangeDAO change = new BOMMgmtChangeDAO();

   
    public void insert(BOMMgmtDTO dto) {

        try {

            int productItemId = change.findItemIdByCode(dto.getProduct_code());

            if (productItemId == 0) {
                throw new RuntimeException("완제품 코드가 존재하지 않습니다.");
            }

            dao.insertBOM(
                productItemId,
                dto.getUse_yn(),
                dto.getRemark()
            );

            int bomId = dao.getCurrBomId();

            if (bomId == 0) {
                throw new RuntimeException("BOM 생성 실패");
            }

           
            int materialItemId = change.findItemIdByCode(dto.getMaterial_code());

            if (materialItemId == 0) {
                throw new RuntimeException("원자재 코드가 존재하지 않습니다.");
            }

            dto.setBOM_id(bomId);
            dto.setMaterial_id(materialItemId);

            dao.insertBOMDetail(dto);

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("BOM 등록 실패: " + e.getMessage());
        }
    }

    public List<BOMMgmtDTO> getBOMList(BOMMgmtSearchDTO dto) {
        return dao.getBOMList(dto);
    }
}
