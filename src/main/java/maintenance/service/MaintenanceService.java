package maintenance.service;

import java.sql.Connection;
import java.sql.Date;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import common.jdbc.DBCPUtil;
import maintenance.dao.MaintenanceDAO;
import maintenance.dto.MaintenanceDTO;

public class MaintenanceService {

    private final MaintenanceDAO maintenanceDAO = new MaintenanceDAO();

    private static final Set<String> ALLOWED_TYPES = new HashSet<String>(Arrays.asList(
            "정기점검", "예방정비", "고장정비"
    ));

    private static final Set<String> ALLOWED_STATUS = new HashSet<String>(Arrays.asList(
            "정상", "점검중", "고장", "수리완료"
    ));

    public List<MaintenanceDTO> getMaintenanceList() {
        Connection conn = null;
        try {
            conn = DBCPUtil.getConnection();
            return maintenanceDAO.selectMaintenanceList(conn);
        } finally {
            DBCPUtil.close(conn);
        }
    }

    public MaintenanceDTO getMaintenanceById(int maintenanceId) {
        Connection conn = null;
        try {
            conn = DBCPUtil.getConnection();
            return maintenanceDAO.selectMaintenanceById(conn, maintenanceId);
        } finally {
            DBCPUtil.close(conn);
        }
    }

    public boolean insertMaintenance(MaintenanceDTO dto) {
        validateMaintenance(dto, false, null);
        Connection conn = null;
        try {
            conn = DBCPUtil.getConnection();
            if (!maintenanceDAO.existsEquipment(conn, dto.getEquipmentId())) {
                throw new RuntimeException("유효한 설비만 선택할 수 있습니다.");
            }
            return maintenanceDAO.insertMaintenance(conn, dto) > 0;
        } finally {
            DBCPUtil.close(conn);
        }
    }

    public boolean updateMaintenance(MaintenanceDTO dto) {
        Connection conn = null;
        try {
            conn = DBCPUtil.getConnection();
            MaintenanceDTO origin = maintenanceDAO.selectMaintenanceById(conn, dto.getMaintenanceId());
            if (origin == null) {
                throw new RuntimeException("정비이력 정보를 찾을 수 없습니다.");
            }
            dto.setEquipmentId(origin.getEquipmentId());
            validateMaintenance(dto, true, origin);
            return maintenanceDAO.updateMaintenance(conn, dto) > 0;
        } finally {
            DBCPUtil.close(conn);
        }
    }

    public boolean deleteMaintenance(String[] maintenanceIds) {
        Connection conn = null;
        try {
            conn = DBCPUtil.getConnection();
            for (String id : maintenanceIds) {
                int maintenanceId = Integer.parseInt(id);
                if (maintenanceDAO.countFailureActionByMaintenanceId(conn, maintenanceId) > 0) {
                    throw new RuntimeException("고장조치가 연결된 정비이력은 삭제할 수 없습니다.");
                }
            }
            return maintenanceDAO.deleteMaintenance(conn, maintenanceIds) > 0;
        } finally {
            DBCPUtil.close(conn);
        }
    }

    public void updateStatusFromFailureAction(int maintenanceId, String failureStatus) {
        Connection conn = null;
        try {
            conn = DBCPUtil.getConnection();
            String maintenanceStatus = "고장";
            if ("완료".equals(failureStatus)) {
                maintenanceStatus = "수리완료";
            }
            maintenanceDAO.updateMaintenanceStatus(conn, maintenanceId, maintenanceStatus);
        } finally {
            DBCPUtil.close(conn);
        }
    }

    private void validateMaintenance(MaintenanceDTO dto, boolean isUpdate, MaintenanceDTO origin) {
        if (dto.getEquipmentId() <= 0) {
            throw new RuntimeException("설비를 선택해 주세요.");
        }
        if (dto.getMaintenanceDate() == null) {
            throw new RuntimeException("정비일자를 입력해 주세요.");
        }
        if (dto.getNextMaintenanceDate() != null && dto.getNextMaintenanceDate().before(dto.getMaintenanceDate())) {
            throw new RuntimeException("다음정비일은 정비일자보다 빠를 수 없습니다.");
        }
        if (!ALLOWED_TYPES.contains(dto.getMaintenanceType())) {
            throw new RuntimeException("허용되지 않은 정비유형입니다.");
        }
        if (!ALLOWED_STATUS.contains(dto.getStatus())) {
            throw new RuntimeException("허용되지 않은 상태값입니다.");
        }
        if (isUpdate && origin != null && origin.getEquipmentId() != dto.getEquipmentId()) {
            throw new RuntimeException("정비이력의 설비는 변경할 수 없습니다.");
        }
    }
}
