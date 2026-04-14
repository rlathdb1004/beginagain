package WorkMgmt.WORegInq.Service;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.List;

import WorkMgmt.WORegInq.DAO.WORegInqDAO;
import WorkMgmt.WORegInq.DTO.WORegInqDTO;

public class WORegInqService {

    private WORegInqDAO dao = new WORegInqDAO();

    public List<WORegInqDTO> getList(String startDate, String endDate, String searchType, String keyword,
            int startRow, int endRow) throws Exception {
        return dao.selectList(startDate, endDate, searchType, keyword, startRow, endRow);
    }

    public int getTotalCount(String startDate, String endDate, String searchType, String keyword) throws Exception {
        return dao.selectTotalCount(startDate, endDate, searchType, keyword);
    }

    public int insert(WORegInqDTO dto) throws Exception {
        validateInsert(dto);

        Integer itemId = dao.findItemIdByCode(dto.getItemCode());
        if (itemId == null) {
            throw new IllegalArgumentException("존재하지 않는 품목코드입니다.");
        }

        Integer empId = dao.findEmpIdByName(dto.getEmpName());
        if (empId == null) {
            throw new IllegalArgumentException("존재하지 않는 작업자명입니다.");
        }

        Integer planId = dao.findPlanId(itemId, dto.getWorkDate(), dto.getLineName());
        if (planId == null) {
            throw new IllegalArgumentException("연결할 생산계획을 찾지 못했습니다. 품목코드/일자/라인을 확인하세요.");
        }

        int nextWorkOrderId = dao.getNextWorkOrderId();

        String workOrderNo = buildWorkOrderNo(dto.getWorkOrderNo(), dto.getWorkDate(), nextWorkOrderId);
        String finalRemark = buildRemark(workOrderNo, dto.getLineName(), dto.getProcessName(), dto.getBomName(),
                dto.getRemark());

        dto.setWorkOrderId(nextWorkOrderId);
        dto.setWorkOrderNo(workOrderNo);
        dto.setRemark(finalRemark);

        return dao.insert(dto, itemId, planId, empId);
    }

    public int delete(int workOrderId) throws Exception {
        if (workOrderId <= 0) {
            throw new IllegalArgumentException("삭제할 작업지시가 올바르지 않습니다.");
        }
        return dao.deleteById(workOrderId);
    }

    private void validateInsert(WORegInqDTO dto) {
        if (dto.getWorkDate() == null) {
            throw new IllegalArgumentException("작업일자를 입력하세요.");
        }
        if (isBlank(dto.getItemCode())) {
            throw new IllegalArgumentException("품목코드를 입력하세요.");
        }
        if (dto.getWorkQty() == null || dto.getWorkQty().doubleValue() <= 0) {
            throw new IllegalArgumentException("지시량은 1 이상이어야 합니다.");
        }
        if (isBlank(dto.getLineName())) {
            throw new IllegalArgumentException("라인을 입력하세요.");
        }
        if (isBlank(dto.getProcessName())) {
            throw new IllegalArgumentException("공정을 입력하세요.");
        }
        if (isBlank(dto.getEmpName())) {
            throw new IllegalArgumentException("작업자를 입력하세요.");
        }
        if (isBlank(dto.getBomName())) {
            throw new IllegalArgumentException("BOM을 입력하세요.");
        }
    }

    private String buildWorkOrderNo(String inputWorkOrderNo, Date workDate, int workOrderId) {
        if (!isBlank(inputWorkOrderNo)) {
            return inputWorkOrderNo.trim();
        }

        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        String dateText = sdf.format(workDate);

        return "WO-" + dateText + "-" + String.format("%03d", workOrderId);
    }

    private String buildRemark(String workOrderNo, String lineName, String processName, String bomName, String remark) {
        StringBuilder sb = new StringBuilder();

        sb.append("[").append(workOrderNo).append("]");

        if (!isBlank(lineName)) {
            sb.append(" 라인=").append(lineName.trim());
        }
        if (!isBlank(processName)) {
            sb.append(" / 공정=").append(processName.trim());
        }
        if (!isBlank(bomName)) {
            sb.append(" / BOM=").append(bomName.trim());
        }
        if (!isBlank(remark)) {
            sb.append(" / ").append(remark.trim());
        }

        return sb.toString();
    }

    private boolean isBlank(String str) {
        return str == null || str.trim().isEmpty();
    }
}