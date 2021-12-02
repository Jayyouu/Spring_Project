package com.bbs.service;

import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.multipart.MultipartFile;

import com.bbs.vo.Boarder;
import com.bbs.vo.UploadFile;

public interface BbsService {
	
	// 게시물 작성
	public void writeAction(Boarder boarder, MultipartFile file) throws Exception;
	
	// 게시물 view 첨부파일과 게시글 내용 같이 관리하기 위해 HashMap타입으로 반환받음
	// HashMap<k,V> : K = key(이름), v = value (저장될 데이터타입 지정)
	// 다형성 (자손타입이 부모 타입으로 자동 형변환) - Object
	public HashMap<String, Object> view(Integer boarder_id) throws Exception;
	
	// 첨부파일 다운로드
	public void downloadAction(HttpServletRequest request, HttpServletResponse response, UploadFile uploadFile) throws Exception;
	
	// 게시물 수정 (파일, 작성자, 내용, 제목 - jsp (view)를 보고 판단)
	public void updateAction(Boarder boarder, MultipartFile file) throws Exception;
	
	// 게시글 list 출력, 페이징처리 (최대값 검색 , list 10개이하 검색 추가)
	public HashMap<String, Object> bbs(int pageNumber) throws Exception;
	
}
