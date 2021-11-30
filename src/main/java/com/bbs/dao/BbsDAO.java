package com.bbs.dao;

import com.bbs.vo.Boarder;

public interface BbsDAO {

	// 글쓰기 
	public void write(Boarder boarder) throws Exception;
	
	
}
