package MasterDataMgmt.BOMManagement.service;

import java.sql.Connection;
import java.util.List;

import MasterDataMgmt.BOMManagement.dao.BOMMgmtChangeDAO;
import MasterDataMgmt.BOMManagement.dao.BOMMgmtDAO;
import MasterDataMgmt.BOMManagement.dto.BOMMgmtDTO;
import MasterDataMgmt.BOMManagement.dto.BOMMgmtSearchDTO;
import MasterDataMgmt.ItemMgmt.ItemMgmtDTO;
import common.jdbc.DBCPUtil;

public class BOMMgmtService {
    private BOMMgmtDAO dao = new BOMMgmtDAO();
    private BOMMgmtChangeDAO change = new BOMMgmtChangeDAO();

    public void insert(BOMMgmtDTO dto) {
        try {
            if (dto.getProduct_code() == null || dto.getProduct_code().trim().isEmpty()) {
                throw new RuntimeException("완제품을 선택하세요.");
            }
            if (dto.getMaterial_id() <= 0) {
                throw new RuntimeException("원자재를 선택하세요.");
            }
            if (dto.getQty_required() <= 0) {
                throw new RuntimeException("소요량은 0보다 커야 합니다.");
            }

            int productItemId = change.findItemIdByCode(dto.getProduct_code());
            ItemMgmtDTO productItem = change.getItemById(productItemId);
            ItemMgmtDTO materialItem = change.getItemById(dto.getMaterial_id());

            if (productItem == null || !"완제품".equals(productItem.getItem_type())) {
                throw new RuntimeException("완제품만 BOM 품목으로 등록할 수 있습니다.");
            }
            if (materialItem == null || !"원자재".equals(materialItem.getItem_type())) {
                throw new RuntimeException("원자재만 BOM 자재로 등록할 수 있습니다.");
            }

            int bomId = dao.findActiveBomIdByProductItemId(productItemId);
            if (bomId == 0) {
                bomId = dao.insertBOMAndReturnId(productItemId, "Y", dto.getRemark());
            }
            if (bomId == 0) {
                throw new RuntimeException("BOM 생성에 실패했습니다.");
            }
            if (dao.existsBomDetailCombination(bomId, dto.getMaterial_id())) {
                throw new RuntimeException("같은 완제품에 동일한 원자재가 이미 등록되어 있습니다.");
            }
            dto.setBOM_id(bomId);
            dto.setUnit(materialItem.getUnit());
            dao.insertBOMDetail(dto);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }
    }

    public List<BOMMgmtDTO> getBOMList(BOMMgmtSearchDTO dto) {
        return dao.getBOMList(dto);
    }

    public List<ItemMgmtDTO> getProductItems() {
        return change.getProductItems();
    }

    public List<ItemMgmtDTO> getMaterialItems() {
        return change.getMaterialItems();
    }

    public int delete(int id) {
        return dao.delete(id);
    }

    public int deleteList(List<Integer> ids) {
        int result = 0;
        for (int id : ids) result += dao.delete(id);
        return result;
    }

    public static boolean updateBom(BOMMgmtDTO dto) {
        Connection conn = null;
        try {
            if (dto.getQty_required() <= 0) {
                throw new RuntimeException("소요량은 0보다 커야 합니다.");
            }
            conn = DBCPUtil.getConnection();
            int result = BOMMgmtDAO.updateBom(conn, dto);
            return result > 0;
        } finally {
            DBCPUtil.close(conn);
        }
    }
}
