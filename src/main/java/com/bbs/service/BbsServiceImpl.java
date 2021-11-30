package com.bbs.service;

import java.io.File;
import java.util.UUID;

import javax.inject.Inject;

import org.apache.commons.io.FilenameUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.bbs.dao.BbsDAO;
import com.bbs.vo.Boarder;
import com.bbs.vo.UploadFile;

@Service
public class BbsServiceImpl implements BbsService {
	
	@Inject
	BbsDAO dao;
	// webapp 에 resources에 생성한 upload file의 경로를 붙여줌 (우클릭 프로퍼티즈 하면 나옴)
	static final String PATH = "F:\\Eclipse\\workspace\\Spring_Project\\src\\main\\webapp\\resources\\upload\\";
	
	@Override
	public void writeAction(Boarder boarder, MultipartFile file) throws Exception {
	
		// 게시글 작성 기능
		boarder = dao.write(boarder);
		
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
		
		// 파일 업로드 전송
		UploadFile uploadFile = new UploadFile();
		
		uploadFile.setBoarder_id(boarder.getBoarder_id());
		// boarder_id 값을 알수는 없지만 필요로함 (자동으로 생성되기 때문)
		// Boarder table 참조, 자동생성된 값을 알수 없지만 받아와야함
		// 전체 id 갯수 받아오기 최댓값 받아오기 -> 같은 시간대 동시에 작성할 시 max값이 달라짐
		// -> 다른 게시물이 동일한 boarder_id를 받을 수 있는 경우가 생김
		// my sql에 한 커밋이 완료되지 않은 transaction 상태에 대하여 직전의 자동생선된값을 받아오는 기능
		// -> LAST_INSERT_ID 
		// LAST_INSERT_ID 한 boarder_id가 boarder 객체에 저장되어 있음
		uploadFile.setFile_name(file_name);
		uploadFile.setFile_realName(file_realName);
		
		dao.fileUpload(uploadFile);
		
		
	}

}
