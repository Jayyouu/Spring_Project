package com.bbs.controller;

import javax.inject.Inject;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.bbs.service.BbsService;
import com.bbs.vo.Boarder;

// /bbs url일 경우를 관리하기 위해여 생성한 클래스
@Controller
@RequestMapping(value = "/bbs/*")
public class BbsController {
	
	@Inject
	BbsService bbsservice;
	
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
		bbsservice.writeAction(boarder, file);
			
		return "redirect:/bbs";
	}


	
}
