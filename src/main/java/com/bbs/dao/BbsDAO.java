package com.bbs.dao;

import com.bbs.vo.Boarder;
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
}
