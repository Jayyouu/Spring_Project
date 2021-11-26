package com.bbs.dao;

import javax.inject.Inject;

import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import com.bbs.vo.Authmail;
import com.bbs.vo.Users;

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
	
	// 이메일 인증
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

	// 이메일 인증완료, 인증번호 삭제
	@Override
	public void delteAuthmail(String user_mail) throws Exception {
		// sqlSession -> Session을 받아온다. (로그인한다) delete로 작성한 sql mapper에 " " 값을 찾아오고, user_mail을 전송한다
		sqlSession.delete(SESSION + ".deleteAuthmail", user_mail);
	}
	
	// 회원가입
	@Override
	public void join(Users users) throws Exception {
		sqlSession.insert(SESSION + ".join", users);
		
	}
	
	// 로그인
	/* parameter 로 String user_id, String user_pw 받아올 시
	@Override
	public Users login(String user_id, String user_pw) throws Exception {
		// 결과값이 1개 아니면 0이니 selectOne 이용 -> 하나의 객체로 만들어서 전달해 줘야함
		// bean 객체 만들기 (주로 이용) or 
		// hashmap {"이름" : "실제 데이터", "이름" : "실제 데이터",...} 2중 하나로 전달 -> 이름을 기준으로 매칭 시킴 (복잡함)
		// sqlSession.selectOne() : 하나의 Object 파라미터만 받아올 수 있음 -> id, pw 2개 넘겨 줄수 없음
		return sqlSession.selectOne(SESSION + ".login", user_id, user_pw);
	}
	*/
	@Override
	public Users login(Users users) throws Exception {
		// 결과값이 1개 아니면 0이니 selectOne 이용 -> 하나의 객체로 만들어서 전달해 줘야함
		return sqlSession.selectOne(SESSION + ".login", users);
	}
	
	

}
