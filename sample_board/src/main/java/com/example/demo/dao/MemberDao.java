package com.example.demo.dao;

import java.util.*;

import org.springframework.data.jpa.repository.*;

import com.example.demo.entity.*;


public interface MemberDao extends JpaRepository<Member, String> {
	// jpa named 함수 -> 이름을 가지고 sql 생성
	public boolean existsByEmail(String email);

	// jpa named 함수가 지원하지 않는 기능 -> jpql을 작성 -> jpql을 sql로 번역
	// jpql도 문자열이니까 오타에 취약하잖아 -> queryDsl을 사용
	@Query("select m.username from Member m where m.email=:email")
	public Optional<String> findUsernameByEmail(String email);

	@Modifying
	@Query("update Member m set m.loginFailCount=0 where m.username=:username")
	public void resetLoginFailCount(String username);
}
