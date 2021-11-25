package com.bbs.dao;

import javax.inject.Inject;

import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import com.bbs.vo.Authmail;

@Repository	// DAO 기능을 한다는 것을 추가시킴
public class UsersDAOImpl implements UsersDAO {
	
	@Inject
	SqlSession sqlSession;
	
	final String SESSION = "com.bbs.mappers.bbs";
	
	@Override
	public String idCheck(String user_id) throws Exception {
								// 처음 전달할 문자 : namespace 이름.실행할 구문 id 이름, 받아올데이터) 
		return sqlSession.selectOne(SESSION + ".idCheck", user_id);
	}
	
	@Override
	public Integer getAuthnum(String user_mail) throws Exception {
		return sqlSession.selectOne(SESSION + ".getAuthnum", user_mail);
	}
	
	@Override
	public void setAuthnum(Authmail authmail) throws Exception {
		// 반환 타입 void라 리턴 작업 필요 없음
		sqlSession.insert(SESSION + ".setAuthnum", authmail);
	}

	@Override
	public void resetAuthnum(Authmail authmail) throws Exception {
		sqlSession.update(SESSION + ".resetAuthnum", authmail);
	}

	

}
