package com.example.demo;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.boot.test.context.*;

import com.example.demo.util.*;

@SpringBootTest
public class MailUtilTest {
	@Autowired
	private MailUtil util;
	

	public void initTest() {
		assertNotNull(util);
	}
	
	//@Test
	public void sendMailTest() {
		util.sendMail("hasaway@naver.com", "테스트", "<h1>테스트 메일</h1>");
	}
	
	@Test
	public void 가짜가입확인메일테스트() {
		String link = "<a href='http://localhost:8081/member/join-check?checkcode=1234'>가입확인</a>";
		util.sendMail("hasaway@naver.com", "가입하려면 링크를 클릭하세요", link);
	}
}
