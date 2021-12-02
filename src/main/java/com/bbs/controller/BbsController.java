package com.bbs.controller;

import java.util.HashMap;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.bbs.service.BbsService;
import com.bbs.vo.Boarder;
import com.bbs.vo.UploadFile;

// /bbs url일 경우를 관리하기 위해여 생성한 클래스
@Controller
@RequestMapping(value = "/bbs/*")
public class BbsController {
	
	@Inject
	BbsService bbsService;
	
	// url 패턴이 'path/bbs/' 일 경우 
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String bbs(Model model) throws Exception {
		return "redirect:/bbs";
	}
	
	// url 패턴이 'path/bbs/write' 일 경우
	@RequestMapping(value = "/write", method = RequestMethod.GET)
	public String write(RedirectAttributes ra, HttpSession session) throws Exception {
		
		// 로그인이 안되있으면 로그인 페이지로 이동 시키고 '로그인이 필요합니다' 알려줌
		if(session.getAttribute("user_id") == null) {
			// '로그인이 필요합니다' 알림 메세지, redirect를 이용하여 RedirectAttribute 필요함
			ra.addFlashAttribute("msg","로그인이 필요합니다.");
			//로그인 페이지로 이동
			return "redirect:/login";
		}
		// 로그인 되어있으면 글쓰기 페이지 출력 
		return "bbs/write";
	}
	
	// url 패턴이 'path/bbs/view' 일 경우
	@RequestMapping(value = "/view", method = RequestMethod.GET)
	public String view(Integer boarder_id, Model model, RedirectAttributes ra) throws Exception {
		// Boarder 객체로 boarder를 받아와도 되지만 검증이 복잡해짐
		// view -> boarder.getBoarder_id() == 0 검증 해줘야함
		HashMap<String, Object> map = bbsService.view(boarder_id);
		
		
		if(map.get("boarder") == null) {
			// 존재하지 않는 게시물 입니다. msg
			ra.addFlashAttribute("msg", "존재하지 않는 게시물입니다.");
			// bbs로 돌려보냄
			return "redirect:/bbs";
		}
		
		// boarder != null 일 때, parameter를 표시 (?) 
		model.addAttribute("map",map);
		/* 위와 동일
		model.addAttribute("boarder",map.get("boarder"));
		model.addAttribute("uploadFile",map.get("uploadFile"));
		*/

		return "bbs/view";
		/*
		 http://localhost:8081/bbs/view 입력 시
		 500 오류 - 서버 오류
		 int boarder_id = null; 상황이 발생한것과 동일, 
		 정수는 null값을 받아오지 못함 객체타입으로 변환하면 null값 사용가능
		 Integer boarder_id (Integer 타입으로 받음) -> 정수를 객체형태로 형변환
		*/

	}
	
	// url 패턴이 'path/bbs/update' 일 경우
		@RequestMapping(value = "/update", method = RequestMethod.GET)
		public String update(Integer boarder_id, Model model, HttpSession session, RedirectAttributes ra) throws Exception {
			
			String user_id = (String) session.getAttribute("user_id");
			
			HashMap<String, Object> map = bbsService.view(boarder_id);
			Boarder boarder = (Boarder) map.get("boarder");
			
			// 로그인 되어있는지 검증
			if(user_id == null) {
				// 로그인이 되지 않을시 메시지 보내고 로그인 페이지로 이동
				ra.addFlashAttribute("msg", "로그인이 필요합니다.");
				return "redirect:/login";
			}
			
			// 존재하는 게시물인지 검증
			if(boarder == null) {
				// 게시물이 존재하지 않으면 존재하지 않는 게시물입니다. 메시지 보내고 게시판 페이지로 이동
				ra.addFlashAttribute("msg", "존재하지 않는 게시물입니다.");
				return "redirect:/bbs";
			}
			
			// user_id 와 작성자가 동일한지 검증
			if(!user_id.equals(boarder.getWriter())) {
				ra.addFlashAttribute("msg", "권한이 없습니다.");
				return "redirect:/bbs";
			}

			model.addAttribute("map",map);
	
			return "bbs/update";
	
		}

	
	// url 패턴이 'path/bbs/writeAction' 일 경우
		// 파라미터로 Boarder 객체, file(multipart로 전달 객체가 MultipartFile), session 받아옴
	@RequestMapping(value = "/writeAction", method = RequestMethod.POST)
	public String writeAction(Boarder boarder, MultipartFile file, HttpSession session, RedirectAttributes ra) throws Exception {
		
		String user_id = (String) session.getAttribute("user_id");
		
		if(user_id == null) {
			// 로그인이 필요합이다 msg 전송 후 로그인 페이지로 이동
			ra.addFlashAttribute("msg", "로그인이 필요합니다.");
			return "redirect:/login";
		}
		
		boarder.setWriter(user_id);
		bbsService.writeAction(boarder, file);
			
		return "redirect:/bbs";
	}
	
	// url 패턴이 'path/bbs/downloadAction' 일 경우
		// 비동기통신 - 새로운 페이지에서 출력한 결과물을 전송받아 clienmt view에 전송 (view나 url을 변경시키는 것이 아님)
	@RequestMapping(value = "/downloadAction", method = RequestMethod.GET)
	public String downloadAction(UploadFile uploadFile, HttpServletRequest request, HttpServletResponse response) throws Exception {
		// 파라미터 값으로 service에서 요구하는 것을 확인하고 받아옴 (UploadFile, HttpServletRequest, HttpServletResponse)
		
		bbsService.downloadAction(request, response, uploadFile);
		
		return "redirect:/bbs/view?boarder_id =" + uploadFile.getBoarder_id();
	}
	
	
	// url 패턴이 'path/bbs/updateAction' 일 경우
		// update.jsp의 form 확인 
		// -> <form method="POST" action="./updateAction" enctype="multipart/form-data">, boarder bean, MultipartFile 
	@RequestMapping(value = "/updateAction", method = RequestMethod.POST)
	public String updateAction(Boarder boarder, MultipartFile file) throws Exception {
		
		bbsService.updateAction(boarder, file);
		
		return "redirect:/bbs/view?boarder_id=" + boarder.getBoarder_id();
	}
	
	// url 패턴이 'path/bbs/deleteAction' 일 경우
	@RequestMapping(value = "/deleteAction", method = RequestMethod.GET)
	public String deleteAction(int boarder_id) throws Exception {
		
		bbsService.deleteAction(boarder_id);
		
		return "redirect:/bbs";
	}
	
}
