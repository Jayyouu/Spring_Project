package com.bbs.controller;

import javax.inject.Inject;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.bbs.service.BbsService;
import com.bbs.service.UsersService;
import com.bbs.vo.Authmail;
import com.bbs.vo.Users;

// http://localhost:8081/ 

@Controller
public class MainController {
	
	private static final Logger logger = LoggerFactory.getLogger(MainController.class);
	
	@Inject
	UsersService usersService;
	@Inject
	BbsService	bbsService;
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
		
		// requst 사용하는 방식과 똑같이 model 이용해서 사용 할 수 있음
		// model.addAttribute("msg", "로그인 페이지");
		// 일반적인 url을 받을땐 request와 model 이용 가능
		
		return "main/login";
	}
	// sysyem.out을 통해 데이터 출력하여 값을 확인하면서 수정해나가기
	// url 패턴이 'path/bbs' 일 경우
	@RequestMapping(value = "/bbs", method = RequestMethod.GET)
		// pagenumber 따로 받아오는 이유 : 객체의 paiging number를 직접 처리하기 위해선
	public String bbs(Integer pageNumber, Model model) throws Exception {
		
		// null 값을 받기 위해 Integer 타입으로 받아옴
		if(pageNumber == null) pageNumber = 1;
		
		model.addAttribute("map", bbsService.bbs(pageNumber));
		
		
		return "bbs/bbs";
	}
		
	// url 패턴이 'path/loginAction' 일 경우
	@RequestMapping(value = "/loginAction", method = RequestMethod.POST)
	// ajax = 결과값 요구, form = 결과값 요구하지 않음, 따로 적을 필요 없음)
	// form = 파라미터가 name, ajax = id
	// 객체로 받으면 user_id, user_pw 값만 들어감. (요구하는것이 id, pw 이기 때문)
	// 세션은 Impl 까지 다 처리하고 추가
	public String loginAction(Users users, HttpSession session, RedirectAttributes ra) throws Exception {
		
		int result = usersService.loginAction(users);
		String url = null;
		
		
		// 로그인 성공 - session 처리
		if(result == 0) {
			session.setAttribute("user_id", users.getUser_id());
			url = "redirect:/";
		}
		// 로그인 실패
		else {
			// 메세지를 전달 (로그인 정보가 잘못되었습니다.)
			ra.addFlashAttribute("msg", "로그인 정보가 일치하지 않습니다.");
			// redirect 이용할 시 model 이랑, request는 사용할 수 없음
			// RedirectAttributes 이용함
			
			// 페이지 이동 -> localhost:8081/login 
			url = "redirect:/login";
		}
		
		return url;
	}
	
	// url 패턴이 'path/logout' 일 경우
	@RequestMapping(value = "/logout", method = RequestMethod.GET)
	public String logout(HttpSession session) throws Exception {
		
		session.invalidate();
		
		return "redirect:/";
		
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
	
	// url 패턴이 'path/sendAuthMail' 일 경우
	@RequestMapping(value = "/sendAuthMail", method = RequestMethod.GET)
	@ResponseBody
	public String sendAuthMail(String user_mail) throws Exception {
		
		int result = usersService.setAuthnum(user_mail);
		return result + "";	
	}
	
	// url 패턴이 'paht/mailAuth' 일 경우
	@RequestMapping(value = "/mailAuth", method = RequestMethod.POST)
	@ResponseBody	// value 자체에 대한 페이지를 구성하고자 할때 필요함
		// RequestMetho에 POST 올때 405 에러 발생 : return = "0"이고, @ResponseBody가 없을시
		// 										그냥 url을 타고 전송하는 방식은 전부 GET 방식 -> post 올 수 없음				
		
	public String mailAuth(Authmail authmail) throws Exception {
		// parameter에 자바빈 객체로 받아올 경우 property의 이름과 ajax에 있는 파라미터 data 이름과 완벽히 동일해야함
		// 자바빈의 변수명 과 ajax의 data 이름 이 동일해야함, 동일 하지 않으면 자바빈 객체로 받아 올 수 없음
		
		/*
		int result = usersService.checkAuthnum(authmail);
		return result + "";
		*/
		return usersService.checkAuthnum(authmail) + "";
		// 변수를 하나더 생성하지 않고 바로 반환함, 위에 것처럼 해줘도 됨
	}
	
	// url 패턴이 'paht/joinAction' 일 경우
	@RequestMapping(value = "/joinAction", method = RequestMethod.POST)
	public String joinAction(Users users, String addr1, String addr2, String addr3) throws Exception {
		// Users의 user_adddr 맞춤	
		users.setUser_addr(addr1 + " " + addr2 + " " + addr3);
		usersService.joinAction(users);
		
		// return "main/login" 으로 리턴받으면 url 경로가 원하지 않는 경로로 설정됨.
		// http://localhost:8081/joinAction 으로 설정되어있음
		// 실제 url 경로를 변경 시켜줘야함
		// http://localhost:8081/login 으로 url 변경
		// redirect: -> http://localhost:8081 동일
		return "redirect:/login";
	}
	
	
	
	
	
	
}








