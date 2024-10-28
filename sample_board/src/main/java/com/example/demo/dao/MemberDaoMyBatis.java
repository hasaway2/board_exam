package com.example.demo.dao;

import java.util.*;

import org.apache.ibatis.annotations.*;

import com.example.demo.entity.*;

// mybatis : 제약조건에 위배 - 기본키는 중복불가능. ora-000001
// 

//@Mapper
public interface MemberDaoMyBatis {
	// 아이디 사용여부
	@Select("select count(username) from member where username=#{username}")
	public int countByUsername(String username);
	
	// 회원 가입
	@Insert("insert into member values(#{username},#{password},#{email},#{joinday},#{birthday},#{profile},0,1,#{role})")
	public int save(Member member);
	
	// 로그인 실패
	@Update("update member set login_fail_count=login_fail_count+1 where username=#{username}")
	public int increaseLoginFailCount(String username);
	
	// 로그인 실패
	@Update("update member set login_fail_count=5, enabled=0 where username=#{username}")
	public int block(String username);
	
	// 로그인 성공
	@Update("update member set login_fail_count=0 where username=#{username}")
	public int resetLoginFailCount(String username);
	
	// Member 읽어오기 : 로그인, 비밀번호 확인 ....
	@Select("select * from member where username=#{username}")
	public Optional<Member> findByUsername(String username);
	
	// 아이디 찾기 
	@Select("select username from member where email=#{email}")
	public Optional<String> findUsernameByEmail(String email);
	

	// 비밀번호 리셋 - 임시비밀번호를 생성해서 비밀번호 변경 후 이메일을 보내자
	// 비밀번호 찾기 기능이 있어서는 안된다 - 1. 정보보호법, 2. 우리 사이트가 비밀번호를 알고 있으면 지켜야 한다
	@Update("update member set password=#{password} where username=#{username}")
	public int updatePassword(String password, String username);
	
	// 사용자 정보 변경 - 비밀번호, 이메일
	@Update("update member set email=#{email}, password=#{password} where username=#{username}")
	public int update(String email, String password, String username);
	
	// 사용자 삭제
	@Delete("delete from member where username=#{username}")
	public int deleteByUsername(String username);
}








