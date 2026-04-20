package MaterialMgmt.IORegInq.Service;

import java.util.Arrays;
import java.util.List;

import MaterialMgmt.IORegInq.DAO.IORegInqDAO;
import MaterialMgmt.IORegInq.DTO.IORegInqDTO;
import item.dto.ItemDTO;

public class IORegInqService {
    private static final List<String> ALLOWED_TYPES = Arrays.asList("입고", "출고");
    private static final List<String> ALLOWED_STATUS = Arrays.asList("완료");

    private final IORegInqDAO dao = new IORegInqDAO();

    public List<IORegInqDTO> getIORegInqList(IORegInqDTO searchDTO) { return dao.selectIORegInqList(searchDTO); }
    public IORegInqDTO getIORegInqDetail(int inoutId) { return dao.selectIORegInqOne(inoutId); }
    public List<ItemDTO> getItemList() { return dao.selectItemList(); }

    public int register(IORegInqDTO dto) {
        ItemDTO item = dao.selectItemById(dto.getItemId());
        if (item == null) throw new RuntimeException("유효한 품목만 등록할 수 있습니다.");
        validateCommon(dto);
        dto.setItemCode(item.getItemCode());
        dto.setItemName(item.getItemName());
        dto.setUnit(item.getUnit());
        if ("완료".equals(dto.getStatus())) {
            if (isInbound(dto.getInoutType())) {
                double availableInboundQty = Math.max(0, item.getAvailableInboundQty());
                if (availableInboundQty <= 0) {
                    throw new RuntimeException("검사 합격 수량이 남아있는 품목만 입고할 수 있습니다.");
                }
                if (dto.getQty() > availableInboundQty) {
                    throw new RuntimeException("검사 합격 기준 남은 입고 가능 수량(" + trimQty(availableInboundQty) + ")을 초과할 수 없습니다.");
                }
            } else if (isStockDecrease(dto.getInoutType())) {
                double currentStock = dao.selectCurrentStockByItemId(dto.getItemId());
                if (dto.getQty() > currentStock) {
                    throw new RuntimeException("현재고보다 많이 " + dto.getInoutType() + "할 수 없습니다.");
                }
            }
        }
        return dao.insertIORegInq(dto);
    }

    public int update(IORegInqDTO dto) {
        IORegInqDTO origin = dao.selectIORegInqCore(dto.getInoutId());
        if (origin == null) throw new RuntimeException("존재하지 않는 입출고 정보입니다.");
        if ("완료".equals(origin.getStatus())) {
            throw new RuntimeException("완료된 입출고는 수정할 수 없습니다.");
        }
        validateCommon(dto);
        dto.setItemId(origin.getItemId());
        dto.setUnit(dao.selectIORegInqOne(dto.getInoutId()).getUnit());
        if (isStockDecrease(dto.getInoutType()) && "완료".equals(dto.getStatus())) {
            double currentStock = dao.selectCurrentStockByItemId(origin.getItemId());
            if (dto.getQty() > currentStock) {
                throw new RuntimeException("현재고보다 많이 " + dto.getInoutType() + "할 수 없습니다.");
            }
        }
        return dao.updateIORegInq(dto);
    }

    public int delete(int[] inoutIds) {
        if (inoutIds == null || inoutIds.length == 0) return 0;
        if (dao.countCompletedByIds(inoutIds) > 0) {
            throw new RuntimeException("완료된 입출고는 삭제할 수 없습니다.");
        }
        return dao.deleteIORegInq(inoutIds);
    }

    private void validateCommon(IORegInqDTO dto) {
        if (dto.getQty() <= 0) throw new RuntimeException("수량은 0보다 커야 합니다.");
        if (!ALLOWED_TYPES.contains(dto.getInoutType())) throw new RuntimeException("허용되지 않은 입출고구분입니다.");
        if (!ALLOWED_STATUS.contains(dto.getStatus())) throw new RuntimeException("허용되지 않은 상태값입니다.");
        if (dto.getInoutDate() == null) throw new RuntimeException("입출고일자를 입력해주세요.");
    }

    private boolean isStockDecrease(String inoutType) {
        return "출고".equals(inoutType);
    }

    private boolean isInbound(String inoutType) {
        return "입고".equals(inoutType);
    }

    private String trimQty(double qty) {
        if (Math.floor(qty) == qty) {
            return String.valueOf((long) qty);
        }
        return String.valueOf(qty);
    }
}
