package com.bbs.dao;

import javax.inject.Inject;

import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import com.bbs.vo.Boarder;
import com.bbs.vo.UploadFile;

@Repository
public class BbsDAOImpl implements BbsDAO {
	
	@Inject
	SqlSession sqlSession;
	
	final String SESSION = "com.bbs.mappers.bbs";
	
	// 글쓰기 반환타입 : Boarder
	// boarder_id가 boarder객체에 담겨 져있음
	@Override
	public Boarder write(Boarder boarder) throws Exception {
		sqlSession.insert(SESSION + ".write", boarder);
		return boarder;
	}
	
	// 파일업로드
	@Override
	public void fileUpload(UploadFile uploadFile) throws Exception {
		sqlSession.insert(SESSION + ".fileUpload", uploadFile);
		
	}

	
}
