package com.bbs.service;

import com.bbs.vo.Authmail;

// service를 구성할 interface 표준 설계 작성
public interface UsersService {
	
	// Service method 이름은 가능한한 기능의 이름으로 정해줌
	// 특정한 결과값을 반환할때 데이터타입은 대부분 int 타입으로 해줌
	
	public int idCheck(String user_id) throws Exception;
	public int setAuthnum(String user_mail) throws Exception;
	public int checkAuthnum(Authmail authmail) throws Exception;
	
}
