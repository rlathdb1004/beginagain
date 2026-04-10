package mes.service;

import mes.dao.CreateDAO;
import mes.dto.CreateDTO;

public class CreateService {
	CreateDAO dao = new CreateDAO();
	
	public int addItem(CreateDTO dto) {
		int result = dao.insertItem(dto);
		System.out.println("CreateService : "+ result);
		return result;
	}

}
