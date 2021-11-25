package com.bbs.controller;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.bbs.service.UsersService;

// http://localhost:8081/ 

@Controller
public class MainController {
	
	private static final Logger logger = LoggerFactory.getLogger(MainController.class);
	
	@Inject
	UsersService usersService;
	
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String main(Model model) throws Exception{
			
		return "main/main";
	}
	
	@RequestMapping(value = "/join", method = RequestMethod.GET)
	public String join(Model model) throws Exception{

		return "main/join";
	}
	
	@RequestMapping(value = "/login", method = RequestMethod.GET)
	public String login(Model model) throws Exception{

		return "main/login";
	}
	/*
	@RequestMapping(value = "url", method = RequestMethod.전송방식)
	public String 함수명(받아올 파라미터) throws Exception {
		-> servlet 에서 request 파라미터 받아와 모델해주는 긴 코드를 작성할 필요 없어짐
		
		return null;
	}
	*/
	
	@RequestMapping(value = "/idCheck", method = RequestMethod.GET)
	public String idCheck(String user_id) throws Exception {
		
		int result = usersService.idCheck(user_id);
		
		return null;
	}
	
}








