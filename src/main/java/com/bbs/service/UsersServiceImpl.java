package com.bbs.service;

import javax.inject.Inject;

import org.springframework.stereotype.Service;

import com.bbs.dao.UsersDAO;

@Service // 아래 내용들이 service 기능을 한다고 설정
public class UsersServiceImpl implements UsersService {

	@Inject
	UsersDAO dao;
	
	@Override
	public int check_id(String user_id) throws Exception {
		return 0;
	}
	

}
