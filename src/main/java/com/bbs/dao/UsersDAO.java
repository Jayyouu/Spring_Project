package com.bbs.dao;

import com.bbs.vo.Authmail;
import com.bbs.vo.Users;

public interface UsersDAO {
	
	// void - 예외 가능한 모든 것들을 처리해줘야함
	// 반환타입이 없으면 void, Mapper sql문에 resultType(반환타입) 이 있으면 그것으로 설정
	// DAO method 와 Mybatis(mapper) id 와 동일하게 지정해주면 좋음
	// controller url(말단) 이름으로 지정해주는것이 편함
	// () 안의 파라미터 넘겨줄 값은 mapper의 반환형태 이름과 동일해야함 Mapper 의 #{} 안의 값
	
	// id 중복 체크
	public String idCheck(String user_id) throws Exception;
	
	// 이메일 인증
	public Integer getAuthnum(String user_mail) throws Exception;
	public void setAuthnum(Authmail authmail) throws Exception;
	public void resetAuthnum(Authmail authmail) throws Exception;
	
	// 이메일 인증번호 삭제 (인증 완료)
	public void delteAuthmail(String user_mail) throws Exception;
	
	// 회원가입 
	public void join(Users users) throws Exception;
	
}
