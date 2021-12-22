package com.bbs.service;

import java.util.List;

import com.bbs.vo.Authmail;
import com.bbs.vo.Reply;
import com.bbs.vo.Users;

// service를 구성할 interface 표준 설계 작성
public interface UsersService {
	
	// Service method 이름은 가능한한 기능의 이름으로 정해줌
	// 특정한 결과값을 반환할때 데이터타입은 대부분 int 타입으로 해줌
	
	// 아이디 중복 체크 
	public int idCheck(String user_id) throws Exception;
	
	// 이메일 인증
	public int setAuthnum(String user_mail) throws Exception;
	
	// 이메일 인증번호삭제 (인증완료)
	public int checkAuthnum(Authmail authmail) throws Exception;
	
	// 회원가입
	public void joinAction(Users users) throws Exception;
	
	// 로그인
	// form - 반환값 필요
	// 존재하면 1, 존재하지 않으면 null
	public int loginAction(Users users) throws Exception;
	
	
}
