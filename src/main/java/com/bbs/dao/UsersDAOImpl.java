package com.bbs.dao;

import javax.inject.Inject;

import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

@Repository	// DAO 기능을 한다는 것을 추가시킴
public class UsersDAOImpl implements UsersDAO {
	
	@Inject
	SqlSession sqlSession;
	
	@Override
	public String idCheck(String user_id) throws Exception {
								// 처음 전달할 문자 : namespace 이름.실행할 구문 id 이름, 받아올데이터) 
		return sqlSession.selectOne("com.bbs.mappers.bbs.idCheck", user_id);
	}

	

}
