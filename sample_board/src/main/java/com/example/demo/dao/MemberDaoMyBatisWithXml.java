package com.example.demo.dao;

import java.util.*;

import org.apache.ibatis.annotations.*;

import com.example.demo.entity.*;

// invalid bound statement
// 1. xml파일을 읽는다 -> 2.dao의 이름과 메소드 이름을 알 수 있다 -> 3. dao에서 메소드를 찾는다 -> 실패 


//@Mapper
public interface MemberDaoMyBatisWithXml {
	public int countByUsername(String username);
	
	public Optional<Member> findByUsername(String username);
	
	public Optional<String> findUsernameByEmail(String email);
	
	public int deleteByUsername(String username);
	
	public int save(Member member);
	
	public int update(Member member);
}
