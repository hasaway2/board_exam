package com.example.demo;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.boot.test.context.*;
import org.springframework.security.crypto.password.*;

import com.example.demo.dao.*;
import com.example.demo.entity.*;

@SpringBootTest
public class MemberDaoWithXmlTest {
	@Autowired
	private MemberDaoMyBatisWithXml dao;
	@Autowired
	private PasswordEncoder encoder;
	
	//@Test
	public void initTest() {
		assertNotNull(dao);
	}
	
	//@Test
	public void countByUsernameTest() {
		System.out.println(dao.countByUsername("spring"));
	}
	
	@Test
	public void updateTest() {
		// 로그인 실패횟수만 바꿔
		dao.update(Member.builder().loginFailCount(2).username("spring").build());
		// block
		dao.update(Member.builder().loginFailCount(5).enabled(false).username("spring").build());
		// password
		dao.update(Member.builder().password(encoder.encode("1234")).username("spring").build());
		// enabled를 true로
		dao.update(Member.builder().enabled(true).username("spring").build());
	}
}




