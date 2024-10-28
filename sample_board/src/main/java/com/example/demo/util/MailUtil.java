package com.example.demo.util;

import org.springframework.beans.factory.annotation.*;
import org.springframework.mail.javamail.*;
import org.springframework.stereotype.*;

import jakarta.mail.*;
import jakarta.mail.internet.*;

@Component
public class MailUtil {
	@Autowired
	private JavaMailSender mailSender;
	private String 보내는사람 = "admin@icia.com";
	
	public void sendMail(String 받는사람, String 제목, String 내용) {
		// Mime : 메일에 파일을 첨부할 때, 파일을 구별하기 위해서 만들어진 표준 -> 사실상 웹표준의 일부
		//		  image/jpg, application/excel, text/html....
		// 인터넷에 포함된 문서의 종류를 표시하려고 사용 -> contentType	  
		MimeMessage message = mailSender.createMimeMessage();
		MimeMessageHelper helper;
		try {
			helper = new MimeMessageHelper(message, false, "utf-8");
			helper.setFrom(보내는사람);
			helper.setTo(받는사람);
			helper.setSubject(제목);
			helper.setText(내용, true);
			mailSender.send(message);
		} catch (MessagingException e) {
			e.printStackTrace();
		}
	}
}
