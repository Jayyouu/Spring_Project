package com.bbs.dao;

import java.util.List;

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
	
	// 게시물 view
	@Override
	public Boarder getBoarder(Integer boarder_id) throws Exception {
		
		return sqlSession.selectOne(SESSION + ".getBoarder", boarder_id);
	}

	
	// 첨부파일 불러오기
	@Override
	public UploadFile getUploadFile(Integer boarder_id) throws Exception {
		
		return sqlSession.selectOne(SESSION + ".getUploadFile", boarder_id);
	}

	@Override
	public UploadFile getUploadFile(String file_realName) throws Exception {
		
		return sqlSession.selectOne(SESSION + ".getUploadFileReal", file_realName);
	}
	
	// 게시물 수정
	@Override
	public void updateBoarder(Boarder boarder) throws Exception {
		sqlSession.update(SESSION + ".updateBoarder", boarder);
	}
	
	// 파일 수정
	@Override
	public void updateFile(UploadFile uploadFile) throws Exception {
		sqlSession.update(SESSION + ".updateFile", uploadFile);
		
	}
	
	// 게시물 최대값 검색
	@Override
	public int getMaxBoarder_id() throws Exception {
		return sqlSession.selectOne(SESSION + ".getMaxBoarder_id");
	}
	
	// 게시글 list 10개이하 검색
	@Override
	public List<Boarder> getBbsList(int boarder_id) throws Exception {
		return sqlSession.selectList(SESSION + ".getBbsList", boarder_id);
	}
	
	// 게시물 삭제
	@Override
	public void deleteBoarder(int boarder_id) throws Exception {
		sqlSession.update(SESSION + ".deleteBoarder", boarder_id);
	}

	
	

	
}
