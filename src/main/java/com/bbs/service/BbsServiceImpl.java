package com.bbs.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.bbs.dao.BbsDAO;
import com.bbs.util.FileUpload;
import com.bbs.vo.Boarder;
import com.bbs.vo.Paging;
import com.bbs.vo.UploadFile;

@Service
public class BbsServiceImpl implements BbsService {
	
	@Inject
	BbsDAO dao;
	// webapp 에 resources에 생성한 upload file의 경로를 붙여줌 (우클릭 프로퍼티즈 하면 나옴)
	static final String PATH = "F:\\Eclipse\\workspace\\Spring_Project\\src\\main\\webapp\\resources\\upload\\";
	
	// 게시물 작성
	@Override
	public void writeAction(Boarder boarder, MultipartFile file) throws Exception {
	
		// 게시글 작성 기능
		boarder = dao.write(boarder);
		
		// 파일 업로드 기능 
		// 파일 객체가 비었을 때 (파일 입력하지 않았을때)
		if(file.isEmpty()) return;
		
		dao.fileUpload(FileUpload.upload(boarder, file, PATH));
		
		/*
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
		/*
		uploadFile.setBoarder_id(boarder.getBoarder_id());
		
		 boarder_id 값을 알수는 없지만 필요로함 (자동으로 생성되기 때문)
		 Boarder table 참조, 자동생성된 값을 알수 없지만 받아와야함
		 전체 id 갯수 받아오기 최댓값 받아오기 -> 같은 시간대 동시에 작성할 시 max값이 달라짐
		 -> 다른 게시물이 동일한 boarder_id를 받을 수 있는 경우가 생김
		 my sql에 한 커밋이 완료되지 않은 transaction 상태에 대하여 직전의 자동생선된값을 받아오는 기능
		 -> LAST_INSERT_ID 
		 LAST_INSERT_ID 한 boarder_id가 boarder 객체에 저장되어 있음 
		 */
		/*
		uploadFile.setFile_name(file_name);
		uploadFile.setFile_realName(file_realName);
		dao.fileUpload(uploadFile);
		*/
	
		
	}
	
	// 게시물 view
	/*
	 uploadfile과 boarder 2개 같이 전달 해줘야함
	 객체의 property를 하나하나 전달해줌
	 vo 객체 만들어 전달 -> 한번만 사용하려고 하는데 Bean객체 만들어줘야함
	 Map 객체 활용 : 
	 HashMap (Attribute로 관리) Attribute(이름, 이름에 해당하는 value) -> 이름으로 관리
	 List, ArrayList (index로 관리)
	 */
	@Override
	public HashMap<String, Object> view(Integer boarder_id) throws Exception {
		
		Boarder boarder = dao.getBoarder(boarder_id);
		// 첨부파일 불러오기
		UploadFile uploadFile = dao.getUploadFile(boarder_id);
		
		HashMap<String, Object> map = new HashMap<String, Object>();
		/*
		 = Map<String, Object> map = new HashMap<String, Object>(); 
		 Map<String, Object> map = new Map<String, Object>();  불가, 
		 -> 인터페이스와 추상클래스 같이 이용 불가, 완성되지 않은 메소드 사용 불가
		 상속받고 있으면 가능함 (자손타입을 조상타입에 넣어줄 수 있음) - 다형성
		*/
		map.put("boarder", boarder);
		map.put("uploadFile", uploadFile);
		
		return map;
	}
	
	// 첨부파일 다운로드
	@Override
	public void downloadAction(HttpServletRequest request, HttpServletResponse response, UploadFile uploadFile) throws Exception {
		
		//
		uploadFile = dao.getUploadFile(uploadFile.getFile_realName());
		
		// 헤더에서 user-agent 이름을 가진것을 가져옴
		String browser = request.getHeader("User-Agent");
		
		// 파일 인코딩 설정
		if(browser.contains("MSIE") || browser.contains("Trident") || browser.contains("Chrome")) {
			uploadFile.setFile_realName( URLEncoder.encode(uploadFile.getFile_realName(), "UTF-8").replaceAll("\\+", "%20") );
			uploadFile.setFile_name( URLEncoder.encode(uploadFile.getFile_name(), "UTF-8").replaceAll("\\+", "%20") );
		}
		else {
			uploadFile.setFile_realName( new String(uploadFile.getFile_realName().getBytes("UTF-8"), "ISO-8859-1") );
			uploadFile.setFile_name( new String(uploadFile.getFile_name().getBytes("UTF-8"), "ISO-8859-1") );
		}
		
		String file_name = PATH + uploadFile.getFile_realName();
		if (!new File(file_name).exists()) return;
		
		// 파일 전송 인코딩
		response.setContentType("application/octer-stream");
		response.setHeader("Content-Transfer-Encoding", "binary;");
		// 전송 했을시 이름을 무엇으로 할것인가에 대한 설정
		response.setHeader("Content-Disposition", "attatchment; filename=\"" + uploadFile.getFile_name() + "\"");
		
		// 실제 파일 전송 (서버에 있는 파일 클라이언트로 전송)
			// Input, Output 에대한 통로 만들어줌					
		OutputStream os 	= response.getOutputStream();	  // Code(프로그램) 에서 클라이언트로 보내주는 output 통로 만들어줌	
		FileInputStream fis = new FileInputStream(file_name); // 서버에서 프로그램(code)로 들어옴, input에 대한 통로 만들어줌, file을 input함
		
		int ncount	 = 0; 				// ncount = 512byte로 나누어준 통에 담기는 것들
		byte[] bytes = new byte[512];   // 2진수(binary)로 전송, 512byte에 맞게 잘라서 전송함 (ex 4kb를 512byte크기로 등분하여 각각 나누어줌)
		
		// 512bytes 만큼 계속 읽어오다가 더 이상 읽어올 용량이 없으면 -1을 반환해줌
		while((ncount = fis.read(bytes)) != -1) {
			os.write(bytes, 0, ncount);
		}
		
		fis.close();
		os.close();
					
	}
	
	// 게시물 수정
	@Override
	public void updateAction(Boarder boarder, MultipartFile file) throws Exception {
		
		// 게시물 수정 기능
		// 수정할때는 이미 boarder_id를 알고 있음
		dao.updateBoarder(boarder);
		
		// 파일 수정
		// 파일을 수정하지 않을 때 - 수정하고자 하는것을 바꾸지 않았을 때
		if(file.isEmpty()) return;
		

		// uploadFile을 데이터베이스에서 불러옴
		// 만약 null이면 원본이 존재하지 않음
		// null이 아니면 원본이 존재함
		// (boarder 객체에 있는 boarder_id 받아옴)
		UploadFile uploadFile = dao.getUploadFile(boarder.getBoarder_id());
		
		/* Util에 FileUpload 객체를 생성하여 이용, 중복을 피함
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
		UploadFile newUploadFile = new UploadFile();
		newUploadFile.setBoarder_id(boarder.getBoarder_id());
		newUploadFile.setFile_name(file_name);
		newUploadFile.setFile_realName(file_realName);
		*/
		
		// 원본에 첨부파일이 존재 하지 않을때
		if(uploadFile == null) {
			dao.fileUpload(FileUpload.upload(boarder, file, PATH));
		}
		// 원본에 첨부파일이 존재 할 때
		else {
			// 원본 삭제 - 해당경로에 있는 원본의 실제 이름을 찾아가 File 객체를 불러와서 삭제
			new File(PATH + uploadFile.getFile_realName()).delete();
			// 원본 제거 후 진행
			dao.updateFile(FileUpload.upload(boarder, file, PATH));
		}
	
	}
	
	// 게시글 list 출력, 페이징처리 (최대값 검색 , list 10개이하 검색 추가)
	@Override
	public HashMap<String, Object> bbs(int pageNumber) throws Exception {
		
		HashMap<String, Object> map = new HashMap<String, Object>();
		
		int max = dao.getMaxBoarder_id();
		// List 받아옴
		//  list = maxboarder_id -10(pageNumber-1)
		List<Boarder> list = dao.getBbsList(max - (pageNumber-1) * 10);
		// Paging 처리
		Paging paging = new Paging(pageNumber, max);
		/*
		 	dao.getMaxBoarder_id() 중복 - 많은 양의 데이터 처리시 속도 성능저하
		 	dao - database와 연결, mapper에 작성한 sql문을 이용해 database에 접속 
		 	-> database에 접속해서 검색, 검증 작업을 하는 기능을 해줌
		 	데이터베이스에 접근해 결과값을 가져오는 것들은 중복하여 함수로 직접접근이 아닌, 결과를 변수로 닮아둠
		 	int Max = dao.getMaxBoarder_id(); -> 한번만 접근 할 수 있도록 해줌
		    수정해줌 
		    List<Boarder> list = dao.getBbsList(dao.getMaxBoarder_id() -> max로 - (pageNumber-1) * 10);
			aging paging = new Paging(pageNumber, dao.getMaxBoarder_id()-> max로 );
		 			  
		 */
		
		map.put("list", list);
		map.put("paging", paging);
		
		return map;
	}
	
	// 게시물 삭제
	@Override
	public void deleteAction(int boarder_id) throws Exception {
		dao.deleteBoarder(boarder_id);
	}
	
	

}







