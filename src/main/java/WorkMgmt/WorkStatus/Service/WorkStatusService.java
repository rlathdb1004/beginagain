package WorkMgmt.WorkStatus.Service;

import java.sql.Connection;
import java.util.List;

import common.jdbc.DBCPUtil;
import WorkMgmt.WorkStatus.DAO.*;
import WorkMgmt.WorkStatus.DTO.*;

public class WorkStatusService {
    private WorkStatusDAO workStatusDAO = new WorkStatusDAO();

    public List<WorkStatusDTO> getWorkStatusList() {
        Connection conn = null;
        try {
            conn = DBCPUtil.getConnection();
            List<WorkStatusDTO> list = workStatusDAO.selectWorkStatusList(conn);
            for (WorkStatusDTO dto : list) {
                double planQty = dto.getWorkQty();
                double producedQty = dto.getProducedQty();
                double progressRate = 0;
                if (planQty > 0) {
                    progressRate = (producedQty / planQty) * 100.0;
                }
                if (progressRate < 0) {
                    progressRate = 0;
                }
                if (progressRate > 100) {
                    progressRate = 100;
                }
                dto.setProgressRate(Math.round(progressRate * 10.0) / 10.0);
                dto.setProgressStatus(resolveProgressStatus(dto));
                dto.setScheduleStatus(resolveScheduleStatus(dto));
            }
            return list;
        } finally {
            DBCPUtil.close(conn);
        }
    }
    
    public WorkStatusDTO getWorkStatusById(int workOrderId) {
        Connection conn = null;
        try {
            conn = DBCPUtil.getConnection();
            WorkStatusDTO dto = workStatusDAO.selectWorkStatusById(conn, workOrderId);

            if (dto != null) {
                double planQty = dto.getWorkQty();
                double producedQty = dto.getProducedQty();
                double progressRate = 0;

                if (planQty > 0) {
                    progressRate = (producedQty / planQty) * 100.0;
                }

                if (progressRate < 0) {
                    progressRate = 0;
                }
                if (progressRate > 100) {
                    progressRate = 100;
                }

                dto.setProgressRate(Math.round(progressRate * 10.0) / 10.0);
                dto.setProgressStatus(resolveProgressStatus(dto));
                dto.setScheduleStatus(resolveScheduleStatus(dto));
            }

            return dto;
        } finally {
            DBCPUtil.close(conn);
        }
    }

    private String resolveProgressStatus(WorkStatusDTO dto) {
        String status = dto.getWorkOrderStatus();
        double progressRate = dto.getProgressRate();
        if ("COMPLETED".equalsIgnoreCase(status) || progressRate >= 100.0) {
            return "완료";
        }
        if (progressRate > 0) {
            return "진행중";
        }
        if ("APPROVED".equalsIgnoreCase(status) || "READY".equalsIgnoreCase(status)) {
            return "대기";
        }
        return status == null || status.trim().isEmpty() ? "대기" : status;
    }

    private String resolveScheduleStatus(WorkStatusDTO dto) {
        java.sql.Date workDate = dto.getWorkDate();
        if (workDate == null) {
            return "일정 미등록";
        }
        java.time.LocalDate target = workDate.toLocalDate();
        java.time.LocalDate today = java.time.LocalDate.now();
        if ("완료".equals(dto.getProgressStatus())) {
            return "완료";
        }
        if (target.isBefore(today)) {
            return "지연";
        }
        if (target.isEqual(today)) {
            return "당일";
        }
        return "정상";
    }
}
