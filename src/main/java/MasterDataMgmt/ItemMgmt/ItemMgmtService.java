package MasterDataMgmt.ItemMgmt;

import java.util.List;

public class ItemMgmtService {
    private final ItemMgmtDAO dao = new ItemMgmtDAO();

    public List<ItemMgmtDTO> getItemList(ItemMgmtSearchDTO dto) {
        return dao.getItemList(dto);
    }

    public void insert(ItemMgmtDTO dto) {
        validateCommon(dto);
        if (dao.existsItemCode(dto.getItem_code(), null)) {
            throw new RuntimeException("이미 사용 중인 품목코드입니다.");
        }
        dao.insertItem(dto);
    }

    public int update(ItemMgmtDTO dto) {
        validateCommon(dto);
        ItemMgmtDTO current = dao.selectOne(dto.getItem_id());
        if (current == null) {
            throw new RuntimeException("존재하지 않는 품목입니다.");
        }
        if (dao.existsItemCode(dto.getItem_code(), dto.getItem_id())) {
            throw new RuntimeException("이미 사용 중인 품목코드입니다.");
        }
        int refCount = dao.countItemReferences(dto.getItem_id());
        if (refCount > 0) {
            if (!safeEquals(current.getItem_code(), dto.getItem_code())) {
                throw new RuntimeException("하위 데이터가 연결된 품목은 품목코드를 변경할 수 없습니다.");
            }
            if (!safeEquals(current.getItem_type(), dto.getItem_type())) {
                throw new RuntimeException("하위 데이터가 연결된 품목은 품목 유형을 변경할 수 없습니다.");
            }
        }
        return dao.update(dto);
    }

    public int delete(int id) {
        if (dao.countItemReferences(id) > 0) {
            throw new RuntimeException("하위 데이터가 연결된 품목은 삭제할 수 없습니다.");
        }
        return dao.delete(id);
    }

    public int deleteList(List<Integer> ids) {
        int result = 0;
        for (int id : ids) {
            result += delete(id);
        }
        return result;
    }

    private void validateCommon(ItemMgmtDTO dto) {
        if (dto.getItem_code() == null || dto.getItem_code().trim().isEmpty()) {
            throw new RuntimeException("품목코드는 필수입니다.");
        }
        if (dto.getItem_name() == null || dto.getItem_name().trim().isEmpty()) {
            throw new RuntimeException("품목명은 필수입니다.");
        }
        if (!("원자재".equals(dto.getItem_type()) || "완제품".equals(dto.getItem_type()))) {
            throw new RuntimeException("품목 유형이 올바르지 않습니다.");
        }
        if (!("EA".equals(dto.getUnit()) || "KG".equals(dto.getUnit()) || "TON".equals(dto.getUnit()))) {
            throw new RuntimeException("단위가 올바르지 않습니다.");
        }
        if (!("Y".equals(dto.getUse_yn()) || "N".equals(dto.getUse_yn()))) {
            throw new RuntimeException("사용여부가 올바르지 않습니다.");
        }
        if (dto.getSafety_stock() < 0) {
            throw new RuntimeException("안전재고는 0 이상이어야 합니다.");
        }
    }

    private boolean safeEquals(String a, String b) {
        return a == null ? b == null : a.equals(b);
    }
}
