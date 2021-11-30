package com.bbs.dao;

import com.bbs.vo.Boarder;
import com.bbs.vo.UploadFile;

public interface BbsDAO {

	// 글쓰기 반환타입 : Boarder타입
	public Boarder write(Boarder boarder) throws Exception;
	
	// 파일업로드
	public void fileUpload(UploadFile uploadFile) throws Exception;
	
}
