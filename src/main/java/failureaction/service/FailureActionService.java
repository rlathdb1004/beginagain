package failureaction.service;

import java.sql.Connection;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import common.jdbc.DBCPUtil;
import failureaction.dao.FailureActionDAO;
import failureaction.dto.FailureActionDTO;
import maintenance.service.MaintenanceService;

public class FailureActionService {

    private FailureActionDAO failureActionDAO = new FailureActionDAO();
    private MaintenanceService maintenanceService = new MaintenanceService();

    private static final Set<String> ALLOWED_STATUS = new HashSet<String>(Arrays.asList("접수", "처리중", "완료"));

    public List<FailureActionDTO> getFailureActionListByMaintenanceId(int maintenanceId) {
        Connection conn = null;
        try {
            conn = DBCPUtil.getConnection();
            return failureActionDAO.selectFailureActionListByMaintenanceId(conn, maintenanceId);
        } finally {
            DBCPUtil.close(conn);
        }
    }

    public FailureActionDTO getFailureActionById(int failureActionId) {
        Connection conn = null;
        try {
            conn = DBCPUtil.getConnection();
            return failureActionDAO.selectFailureActionById(conn, failureActionId);
        } finally {
            DBCPUtil.close(conn);
        }
    }

    public boolean insertFailureAction(FailureActionDTO dto) {
        validate(dto);
        Connection conn = null;
        try {
            conn = DBCPUtil.getConnection();
            if (!failureActionDAO.existsMaintenance(conn, dto.getMaintenanceId())) {
                throw new RuntimeException("유효한 정비이력에만 고장조치를 등록할 수 있습니다.");
            }
            boolean ok = failureActionDAO.insertFailureAction(conn, dto) > 0;
            if (ok) maintenanceService.updateStatusFromFailureAction(dto.getMaintenanceId(), dto.getStatus());
            return ok;
        } finally {
            DBCPUtil.close(conn);
        }
    }

    public boolean updateFailureAction(FailureActionDTO dto) {
        validate(dto);
        Connection conn = null;
        try {
            conn = DBCPUtil.getConnection();
            if (!failureActionDAO.existsMaintenance(conn, dto.getMaintenanceId())) {
                throw new RuntimeException("유효한 정비이력 정보가 없습니다.");
            }
            boolean ok = failureActionDAO.updateFailureAction(conn, dto) > 0;
            if (ok) maintenanceService.updateStatusFromFailureAction(dto.getMaintenanceId(), dto.getStatus());
            return ok;
        } finally {
            DBCPUtil.close(conn);
        }
    }

    public boolean deleteFailureAction(String[] failureActionIds) {
        Connection conn = null;
        try {
            conn = DBCPUtil.getConnection();
            return failureActionDAO.deleteFailureAction(conn, failureActionIds) > 0;
        } finally {
            DBCPUtil.close(conn);
        }
    }

    private void validate(FailureActionDTO dto) {
        if (dto.getMaintenanceId() <= 0) throw new RuntimeException("정비이력 정보가 올바르지 않습니다.");
        if (dto.getFailureDate() == null) throw new RuntimeException("고장일자를 입력해 주세요.");
        if (dto.getActionDate() != null && dto.getActionDate().before(dto.getFailureDate())) {
            throw new RuntimeException("조치일은 고장일자보다 빠를 수 없습니다.");
        }
        if (!ALLOWED_STATUS.contains(dto.getStatus())) throw new RuntimeException("허용되지 않은 상태값입니다.");
    }
}
