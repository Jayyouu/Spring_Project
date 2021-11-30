package com.bbs.service;

import org.springframework.web.multipart.MultipartFile;

import com.bbs.vo.Boarder;

public interface BbsService {
	
	// 게시물 작성
	public void writeAction(Boarder boarder, MultipartFile file) throws Exception;
	
	// 게시물 view
	public Boarder view(Integer boarder_id) throws Exception;
}
