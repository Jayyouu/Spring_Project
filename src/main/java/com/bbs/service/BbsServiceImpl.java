package com.bbs.service;

import java.io.File;
import java.util.UUID;

import javax.inject.Inject;

import org.apache.commons.io.FilenameUtils;
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
		dao.write(boarder);
		
		// 파일 업로드 기능 
		// 파일 객체가 비었을 때 (파일 입력하지 않았을때)
		if(file.isEmpty()) return;
		
		// 작성자가 올린 파일의 원본 이름
		String file_name = file.getOriginalFilename();
		// 파일 확장자를 구함
		String suffix 	 = FilenameUtils.getExtension(file_name);
		// 랜덤한 중복되지 않는 ID값 받아옴
		UUID uuid 		 = UUID.randomUUID();
		// 파일이 저장될 때 이름
		String file_realName = uuid + "." + suffix;
		// 파일 업로드
		file.transferTo(new File(PATH + file_realName));
		
		
	}

}
