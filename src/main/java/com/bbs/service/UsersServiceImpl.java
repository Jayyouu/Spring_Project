package com.bbs.service;

import java.util.Properties;
import java.util.Random;

import javax.inject.Inject;
import javax.mail.Address;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.springframework.stereotype.Service;

import com.bbs.dao.UsersDAO;
import com.bbs.util.Mail;
import com.bbs.vo.Authmail;
import com.bbs.vo.Users;

@Service // 아래 내용들이 service 기능을 한다고 설정
public class UsersServiceImpl implements UsersService {

	@Inject
	UsersDAO dao;
	
	// idCheck service 구현
	@Override
	public int idCheck(String user_id) throws Exception {
		
		int result = 0;
		if(dao.idCheck(user_id) != null) result = 1;
				
		return result;
	}
	
	// email 인증 service 구현
	@Override
	public int setAuthnum(String user_mail) throws Exception {
		
		int result = 0;
		Random rd = new Random();
		
		// 4자리의 임의의 인증번호 생성
		int auth_num = rd.nextInt(9999) + 1;
		
		String from = "1223020@gmail.com";
		String to   = user_mail;
		String subject = "인증번호 메일";
		String content = "다음 인증번호를 입력하세요. <br> <h2>" + auth_num + "</h2>";
		
		// 해당 email 주소의 인증번호가 존재하는지 확인
		// email이 존재하지 않으면 Insert, 존재하면 update 
		Integer exist = dao.getAuthnum(to);
		
		if(exist == null) dao.setAuthnum(new Authmail(to, auth_num));
					 else dao.resetAuthnum(new Authmail(to, auth_num));
					
		Properties p = new Properties();
		
		p.put("mail.smtp.user", from);
		p.put("mail.smtp.host", "smtp.gmail.com");
		p.put("mail.smtp.port", "587");
		p.put("mail.smtp.starttls.enable", "true");
		p.put("mail.smtp.auth", "true");
		p.put("mail.smtp.debug", "true");
		p.put("mail.smtp.socketFactory.port", "587");
		p.put("mail.smtp.socketFactory.fallback", "false");
		p.put("mail.smtp.ssl.trust", "smtp.gmail.com");
		p.put("mail.smtp.ssl.protocols", "TLSv1.2");
		
		try {
			
			Authenticator auth = new Mail();
			Session s = Session.getInstance(p, auth);
			s.setDebug(true);
			
			MimeMessage msg = new MimeMessage(s);
			
			Address fromAddr = new InternetAddress(from);
			Address toAddr = new InternetAddress(to);

			msg.setFrom(fromAddr);
			msg.setRecipient(Message.RecipientType.TO, toAddr);
			msg.setSubject(subject);
			msg.setContent(content, "text/html;charset=UTF-8");
			
			Transport.send(msg);
			
		} catch(Exception e) {
			e.printStackTrace();
			result = -1;
		}
		
		return result;
	}
	
	// email 인증 완료 (인증번호 제거)service 구현
	@Override
	public int checkAuthnum(Authmail authmail) throws Exception {
		
		int result = 1;
		Integer exist = dao.getAuthnum(authmail.getUser_mail());
		
		// 동일할 때
		// 객체고, Integer타입과 int 타입 비교라서 == 연산자 보다 .equals가 더 정확할수 있음. 
		if(exist.equals(authmail.getAuth_num())) {
			dao.delteAuthmail(authmail.getUser_mail());
			result = 0;
		}
		/* 다를 때 -> 논리상 표시, 실제로는 필요없는 코드
		else {
			result = -1;
		}
		*/
		
		return result;
	}
	
	// 회원가입 service 구현
	@Override
	public void joinAction(Users users) throws Exception {
		dao.join(users);
	}
	


}
