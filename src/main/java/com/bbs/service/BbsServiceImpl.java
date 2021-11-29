package com.bbs.service;

import javax.inject.Inject;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.bbs.dao.BbsDAO;
import com.bbs.vo.Boarder;

@Service
public class BbsServiceImpl implements BbsService {
	
	@Inject
	BbsDAO dao;
	// webapp 에 resources에 생성한 upload file의 경로를 붙여줌 (우클릭 프로퍼티즈 하면 나옴)
	static final String PATH = "F:\\Eclipse\\workspace\\Spring_Project\\src\\main\\webapp\\resources\\upload\\";
	
	@Override
	public void writeAction(Boarder boarder, MultipartFile file) throws Exception {
	
		// 게시글 작성 기능
		
		// 파일 업로드 기능 
		
	}

}
