package MaterialMgmt.IORegInq.Service;

import java.util.List;

import MaterialMgmt.IORegInq.DAO.IORegInqDAO;
import MaterialMgmt.IORegInq.DTO.IORegInqDTO;

	/*
	 * 입출고 등록/조회 Service
	 * 
	 * 역할
	 * - Servlet에서 받은 요청을 DAO로 전달
	 */

public class IORegInqService {

	 // 입출고 목록 조회
	 
	public List<IORegInqDTO> getIORegInqList(IORegInqDTO searchDTO) {
		IORegInqDAO dao = new IORegInqDAO();
		List<IORegInqDTO> list = dao.selectIORegInqList(searchDTO);
		return list;
	}

	
	// 입출고 상세 조회
	 
	public IORegInqDTO getIORegInqDetail(int inoutId) {
		IORegInqDAO dao = new IORegInqDAO();
		IORegInqDTO dto = dao.selectIORegInqOne(inoutId);
		return dto;
	}
}
