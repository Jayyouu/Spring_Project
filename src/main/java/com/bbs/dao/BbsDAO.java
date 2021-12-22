package com.bbs.dao;

import java.util.List;

import com.bbs.vo.Boarder;
import com.bbs.vo.Reply;
import com.bbs.vo.UploadFile;

public interface BbsDAO {

	// 글쓰기 반환타입 : Boarder타입
	public Boarder write(Boarder boarder) throws Exception;
	
	// 파일업로드
	public void fileUpload(UploadFile uploadFile) throws Exception;
	
	// 게시물 view
	public Boarder getBoarder(Integer boarder_id) throws Exception;
	
	// 첨부파일 불러오기
	// view 작업에서 boarder_id를 integr로 받아와 uploadfile 에서도 동일하게 받아옴
	// 업로드 파일을 받아오는 동일한 작업이라 이름은 동일하게 함 (기능이 같음)
	// parameter 매개변수만 다른 함수 인식하도록 함, 오버로드
	public UploadFile getUploadFile(Integer boarder_id) throws Exception;
	public UploadFile getUploadFile(String file_realName) throws Exception;
	
	// 게시물 수정
	public void updateBoarder(Boarder boarder) throws Exception;
	
	// 파일 수정
	public void updateFile(UploadFile uploadFile) throws Exception;
	
	// 게시물 최대값 검색
	public int getMaxBoarder_id() throws Exception;
	
	// 게시글 list 10개이하 검색 (객체 하나만 받오은것이 아니라 List<> 이용) 
	public List<Boarder> getBbsList(int boarder_id) throws Exception;
	
	// 게시물 삭제
	public void deleteBoarder(int boarder_id) throws Exception;
	
	// 댓글 추가
	// 반환타입 없으니 void 타입
	public void insertReply(Reply reply) throws Exception;
	
	// 댓글 내용 불러오기
	// boarder_id 값을 반환 받아 List를 불러옴
	public List<Reply> getReplyList(int boarder_id) throws Exception;
	
	// 댓글 삭제
	public void deleteReply(int reply_id) throws Exception;
	
}










