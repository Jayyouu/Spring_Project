package com.bbs.dao;

import javax.inject.Inject;

import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import com.bbs.vo.Boarder;

@Repository
public class BbsDAOImpl implements BbsDAO {
	
	@Inject
	SqlSession sqlSession;
	
	final String SESSION = "com.bbs.mappers.bbs";
	
	// 글쓰기
	@Override
	public void write(Boarder boarder) throws Exception {
		sqlSession.insert(SESSION + ".write", boarder);
		
	}

	
}
