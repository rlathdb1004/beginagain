package routing.service;

import java.sql.Connection;
import java.util.List;

import common.jdbc.DBCPUtil;
import routing.dao.RoutingDAO;
import routing.dto.RoutingDTO;

public class RoutingService {

    private RoutingDAO routingDAO = new RoutingDAO();

    public List<RoutingDTO> getRoutingList() {
        Connection conn = null;

        try {
            conn = DBCPUtil.getConnection();
            return routingDAO.selectRoutingList(conn);
        } finally {
            DBCPUtil.close(conn);
        }
    }

    public List<RoutingDTO> getRoutingListByItemId(int itemId) {
        Connection conn = null;

        try {
            conn = DBCPUtil.getConnection();
            return routingDAO.selectRoutingListByItemId(conn, itemId);
        } finally {
            DBCPUtil.close(conn);
        }
    }

    public RoutingDTO getRoutingById(int routingId) {
        Connection conn = null;

        try {
            conn = DBCPUtil.getConnection();
            return routingDAO.selectRoutingById(conn, routingId);
        } finally {
            DBCPUtil.close(conn);
        }
    }

    public boolean insertRouting(RoutingDTO dto) {
        validate(dto, null);
        Connection conn = null;

        try {
            conn = DBCPUtil.getConnection();
            int result = routingDAO.insertRouting(conn, dto);
            return result > 0;
        } finally {
            DBCPUtil.close(conn);
        }
    }

    public boolean updateRouting(RoutingDTO dto) {
        validate(dto, dto.getRoutingId());
        Connection conn = null;

        try {
            conn = DBCPUtil.getConnection();
            int result = routingDAO.updateRouting(conn, dto);
            return result > 0;
        } finally {
            DBCPUtil.close(conn);
        }
    }

    public boolean deleteRouting(String[] routingIds) {
        Connection conn = null;

        try {
            conn = DBCPUtil.getConnection();
            int result = routingDAO.deleteRouting(conn, routingIds);
            return result > 0;
        } finally {
            DBCPUtil.close(conn);
        }
    }

    private void validate(RoutingDTO dto, Integer excludeRoutingId) {
        if (dto.getItemId() <= 0) throw new RuntimeException("완제품을 선택하세요.");
        if (dto.getProcessId() <= 0) throw new RuntimeException("공정을 선택하세요.");
        if (dto.getEquipmentId() <= 0) throw new RuntimeException("설비를 선택하세요.");
        if (dto.getProcessSeq() <= 0) throw new RuntimeException("공정순서는 1 이상이어야 합니다.");

        Connection conn = null;
        try {
            conn = DBCPUtil.getConnection();
            if (!routingDAO.existsFinishedItem(conn, dto.getItemId())) {
                throw new RuntimeException("완제품만 라우팅에 등록할 수 있습니다.");
            }
            if (!routingDAO.existsProcess(conn, dto.getProcessId())) {
                throw new RuntimeException("유효한 공정을 선택하세요.");
            }
            if (!routingDAO.existsEquipment(conn, dto.getEquipmentId())) {
                throw new RuntimeException("유효한 설비를 선택하세요.");
            }
            if (routingDAO.existsSeqDuplicate(conn, dto.getItemId(), dto.getProcessSeq(), excludeRoutingId)) {
                throw new RuntimeException("같은 품목에 동일한 공정순서는 등록할 수 없습니다.");
            }
            if (routingDAO.existsProcessEquipmentDuplicate(conn, dto.getItemId(), dto.getProcessId(), dto.getEquipmentId(), excludeRoutingId)) {
                throw new RuntimeException("같은 품목에 동일한 공정/설비 조합은 중복 등록할 수 없습니다.");
            }
        } finally {
            DBCPUtil.close(conn);
        }
    }
}
