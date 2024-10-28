package com.example.demo.security;

import java.io.*;
import java.util.*;

import org.springframework.beans.factory.annotation.*;
import org.springframework.security.core.*;
import org.springframework.security.web.authentication.*;
import org.springframework.stereotype.*;

import com.example.demo.dao.*;
import com.example.demo.entity.*;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.transaction.*;

@Component
public class LoginFailureHandler implements AuthenticationFailureHandler {
	@Autowired
	private MemberDao memberDao;
	
	@Transactional
	@Override
	public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException exception) throws IOException, ServletException {
		// 사용자 요청에서 아이디를 꺼낸다
		String username = request.getParameter("username");
		// 아이디를 가지고 사용자 정보를 가져온다(why? 로그인 실패횟수 증가 + 계정 블록)
		Optional<Member> result = memberDao.findById(username);
		// 사용자 정보를 저장하는 세션을 꺼내자
		HttpSession session = request.getSession();
		
		if(result.isEmpty()) {
			// 아이디가 없는 경우
			session.setAttribute("message", "사용자 정보를 찾을 수 없습니다");
		} else {
			// 비밀번호가 틀린 경우
			Member member = result.get();
			if(member.getEnabled()==false) {
				// 이미 블록
				session.setAttribute("message", "블록된 계정입니다. 관리자에게 연락하세요");
			} else if(member.getLoginFailCount()==4) {
				member.block();
				session.setAttribute("message", "블록된 계정입니다. 로그인에 5회 실패해 계정이 블록되었습니다. 관리자에게 연락하세요");
			} else {
				// 로그인 실패횟수 증가
				member.increaseLoginFailCount();
				session.setAttribute("message", "로그인에 " + member.getLoginFailCount() + "회 실패했습니다");
			}
		}
		
		response.sendRedirect("/member/login");
	}
}

/*
 * 로그인에 실패하면 /member/login 이동 후 상황에 따른 에러메시지를 출력하겠다
         상황															에러 메시지
// 아이디가 틀렸다													사용자 정보를 찾을 수 없습니다
// 비밀번호가 틀려서 실패횟수 증가										로그인에 3회 실패했습니다. 5회 실패시 계정이 블록됩니다
// 비밀번호가 틀려서 실패횟수를 증가했더니 5번이 되서 블록					로그인에 5회 실패해 계정이 블록되었습니다. 관리자에게 연락하세요
// 이미 블록														블록된 계정입니다. 관리자에게 연락하세요
 * 
 * 이동 후 에러메시지 출력 -> 스프링의 RedirectAttributes
 * 					  -> 이곳은 컨트롤러가 아니어서 RedirectAttributes를 사용할 수가 없다
 * 					 -> 대신 세션에다가 오류 메시지를 저장하자
 */














