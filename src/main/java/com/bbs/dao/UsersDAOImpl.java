package com.bbs.dao;

import org.springframework.stereotype.Repository;

@Repository	// DAO 기능을 한다는 것을 추가시킴
public class UsersDAOImpl implements UsersDAO {

	@Override
	public int check_id(String user_id) throws Exception {
		
		return 0;
	}

}
