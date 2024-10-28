package com.example.demo;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.boot.test.context.*;

import com.example.demo.dao.*;

@SpringBootTest
public class MemberReadBoardDaoTest {
	@Autowired
	private MemberReadBoardDao dao;
	
	//@Test
	public void initTest() {
		assertNotNull(dao);
	}
	
	//@Test
	public void saveTest() {
		assertEquals(1, dao.save(111L, "spring"));
	}
	
	@Test
	public void existsByBnoAndUsernameTest() {
		assertEquals(true, dao.existsByBnoAndUsername(111L, "spring"));
		assertEquals(false, dao.existsByBnoAndUsername(200L, "spring"));
	}
}
