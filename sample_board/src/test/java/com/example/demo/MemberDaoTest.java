package com.example.demo;

import static org.junit.jupiter.api.Assertions.*;

import java.time.*;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.boot.test.context.*;
import org.springframework.data.domain.*;
import org.springframework.security.crypto.password.*;

import com.example.demo.dao.*;
import com.example.demo.entity.*;
import com.example.demo.enums.*;

import jakarta.transaction.*;

// junit이 스프링을 테스트하려면 스프링 ApplicationContext가 생성되야 한다
// 테스트가 가능하도록 스프링을 로딩하고 junit과 통합해준다
@SpringBootTest
public class MemberDaoTest {
	@Autowired
	private MemberDao memberDao;
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	//@Test
	public void saveTest() {
		Member m = new Member("summer", passwordEncoder.encode("1234"), 
				"summer@naver.com", LocalDate.now(), LocalDate.of(2020, 11, 20), "default.png", 0, 
				false, Role.USER);
		assertEquals(1, memberDao.save(m));	
	}
}









