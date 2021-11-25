package com.bbs.controller;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bbs.service.UsersService;

// http://localhost:8081/ 

@Controller
public class MainController {
	
	private static final Logger logger = LoggerFactory.getLogger(MainController.class);
	
	@Inject
	UsersService usersService;
	/*
	@RequestMapping(value = "url", method = RequestMethod.전송방식)
	public String 함수명(받아올 파라미터) throws Exception {
		-> servlet 에서 request 파라미터 받아와 모델해주는 긴 코드를 작성할 필요 없어짐
		
		return null;
	}
	*/
	
	// url 패턴이 'path/' 일 경우
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String main(Model model) throws Exception{
			
		return "main/main";
	}
	
	// url 패턴이 'path/join' 일 경우
	@RequestMapping(value = "/join", method = RequestMethod.GET)
	public String join(Model model) throws Exception{

		return "main/join";
	}
	
	// url 패턴이 'path/login' 일 경우
	@RequestMapping(value = "/login", method = RequestMethod.GET)
	public String login(Model model) throws Exception{

		return "main/login";
	}
	
	// url 패턴이 'path/idCheck' 일 경우
	@RequestMapping(value = "/idCheck", method = RequestMethod.GET)
	// 반환 값을 페이지에 직접 출력
	// http://localhost:8081/idCheck?user_id=qwer
	// @ResponseBody -> 없으면 위 url 입력시 404 오류 발생 , view의 path를 찾아가기 때문
	@ResponseBody 	// 있으면 해당 url에 결과값이 표시됨, ajax나 결과값을 return으로 받아오기 원할 경우 추가 해줌
	public String idCheck(String user_id) throws Exception {
		
		int result = usersService.idCheck(user_id);
		
		// return 값으로 경로를 원함. 결과값을 주도록 바꿔줌 -> @ResponseBody 추가해줌
		// @ResponseBody 없으면 중복체크 실패 alrert 뜨고, 있으면 사용가능한 아이디입니다 alert 뜸
		return result + "";
	}
	
}








