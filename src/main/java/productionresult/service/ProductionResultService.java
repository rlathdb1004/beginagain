package productionresult.service;

import java.sql.Connection;
import java.util.List;

import common.jdbc.DBCPUtil;
import productionresult.dao.ProductionResultDAO;
import productionresult.dto.ProductionResultDTO;

public class ProductionResultService {

	private ProductionResultDAO productionResultDAO = new ProductionResultDAO();

	public List<ProductionResultDTO> getProductionResultList(int workOrderId) {
		Connection conn = null;

		try {
			conn = DBCPUtil.getConnection();
			return productionResultDAO.selectProductionResultList(conn, workOrderId);
		} finally {
			DBCPUtil.close(conn);
		}
	}

	public ProductionResultDTO getProductionResultById(int resultId) {
		Connection conn = null;

		try {
			conn = DBCPUtil.getConnection();
			return productionResultDAO.selectProductionResultById(conn, resultId);
		} finally {
			DBCPUtil.close(conn);
		}
	}

	public boolean updateProductionResult(ProductionResultDTO dto) {
		Connection conn = null;

		try {
			conn = DBCPUtil.getConnection();
			int result = productionResultDAO.updateProductionResult(conn, dto);
			return result > 0;
		} finally {
			DBCPUtil.close(conn);
		}
	}

	public boolean deleteProductionResults(String[] resultIds) {
		Connection conn = null;

		try {
			conn = DBCPUtil.getConnection();
			int result = productionResultDAO.deleteProductionResults(conn, resultIds);
			return result > 0;
		} finally {
			DBCPUtil.close(conn);
		}
	}

public boolean insertProductionResult(ProductionResultDTO dto) {
    Connection conn = null;
    try {
        conn = DBCPUtil.getConnection();
        int result = productionResultDAO.insertProductionResult(conn, dto);
        return result > 0;
    } finally {
        DBCPUtil.close(conn);
    }
}

}