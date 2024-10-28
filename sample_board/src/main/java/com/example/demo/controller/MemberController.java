package com.example.demo.controller;

import java.security.*;

import org.springframework.beans.factory.annotation.*;
import org.springframework.security.access.prepost.*;
import org.springframework.stereotype.*;
import org.springframework.validation.*;
import org.springframework.validation.annotation.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.*;
import org.springframework.web.servlet.mvc.support.*;

import com.example.demo.dto.*;
import com.example.demo.enums.*;
import com.example.demo.service.*;

import jakarta.servlet.http.*;
import jakarta.validation.*;

@Validated
@Controller
public class MemberController {
	@Autowired
	private MemberService memberService;
	
	@PreAuthorize("isAnonymous()")
	@GetMapping("/member/join")
	public ModelAndView join() 	{
		return new ModelAndView("member/join");
	}
	
	@PreAuthorize("isAnonymous()")
	@PostMapping("/member/join")
	public ModelAndView join(@ModelAttribute @Valid MemberDto.Create dto, BindingResult br) {
		memberService.join(dto);
		return new ModelAndView("redirect:/member/login");
	}
	
	@PreAuthorize("isAnonymous()")
	@GetMapping("/member/find")
	public ModelAndView find() {
		return new ModelAndView("member/find");
	}
	
	@PreAuthorize("isAnonymous()")
	@GetMapping("/member/login")
	public ModelAndView login() {
		return new ModelAndView("member/login");
	}
	
	// 임시비밀번호로 로그인을 하면 비밀번호 변경으로 보내자
	@PreAuthorize("isAuthenticated()")
	@GetMapping("/member/update-password")
	public ModelAndView updatePassword(HttpSession session) {
		// 1회성 메시지를 출력하려면 스프링 RedirectAttributes
		// 출력할 메시지를 저장하는 곳이 로그인성공핸들러(스프링 컨트롤러가 아니어서 RedirectAttributes 사용불가능)
		//    - 그래서 session에 출력할 메시지를 담은 다음 화면 이동을 시켰다
		// 이동한 화면 컨트롤러에서는 session에서 메시지를 꺼내서 ModelAndView로 이동시킨다(session에서는 삭제)
		if(session.getAttribute("message")!=null) {
			String message = (String)session.getAttribute("message");
			session.removeAttribute("message");
			return new ModelAndView("member/update-password").addObject("message", message);
		}
		return new ModelAndView("member/update-password");
	}
	
	@PreAuthorize("isAuthenticated()")
	@PostMapping("/member/update-password")
	public ModelAndView updatePassword(@Valid MemberDto.UpdatePassword dto, BindingResult br, 
			Principal principal, RedirectAttributes ra) {
		PasswordChangeResult result = memberService.updatePassword(dto, principal.getName());
		if(result==PasswordChangeResult.SUCCESS)
			return new ModelAndView("redirect:/");
		else if(result==PasswordChangeResult.PASSWORD_CHECK_FAIL) {
			ra.addFlashAttribute("message", "비밀번호를 확인하지 못했습니다");
			return new ModelAndView("redirect:/member/update-password");
		} else {
			ra.addFlashAttribute("message", "새비밀번호와 기존비밀번호는 달라야 하니다");
			return new ModelAndView("redirect:/member/update-password");
		}
	}
	
	@PreAuthorize("isAuthenticated()")
	@GetMapping("/member/check-password")
	public ModelAndView checkPassword(HttpSession session) {
		if(session.getAttribute("checkPassword")!=null)
			return new ModelAndView("redirect:/member/readme");
		return new ModelAndView("member/check-password");
	}
	
	@PreAuthorize("isAuthenticated()")
	@PostMapping("/member/check-password")
	public ModelAndView checkPassword(String password, Principal principal, HttpSession session, RedirectAttributes ra) {
		boolean result = memberService.비밀번호확인(password, principal.getName());
		if(result==true) {
			session.setAttribute("checkPassword", true);
			return new ModelAndView("redirect:/member/readme");
		}
		ra.addFlashAttribute("message", "비밀번호를 확인하세요");
		return new ModelAndView("redirect:/member/check-password");
	}
	
	@PreAuthorize("isAuthenticated()")
	@GetMapping("/member/readme")
	public ModelAndView readme(HttpSession session, Principal principal) {
		if(session.getAttribute("checkPassword")==null) 
			return new ModelAndView("redirect:/member/check-password");
		MemberDto.Read dto = memberService.내정보보기(principal.getName());
		return new ModelAndView("member/readme").addObject("result", dto);
	}	
	
	@PreAuthorize("isAuthenticated()")
	@PostMapping("/member/withdraw")
	public ModelAndView 탈퇴(HttpSession session, Principal principal) {
		memberService.탈퇴(principal.getName());
		
		// 세션은 사용자 정보를 저장하는 서버측 공간
		// 모든 사용자는 세션을 가진다 -> invalidate()하면 세션을 파괴하고 새로운 세션을 생성
		session.invalidate();
		return new ModelAndView("redirect:/");
	}
	
	@PreAuthorize("isAuthenticated()")
	@PostMapping("/member/update")
	public ModelAndView update(@Valid MemberDto.Update dto, BindingResult br, Principal principal) {
		memberService.update(dto, principal.getName());
		return new ModelAndView("redirect:/member/readme");
	}
	
	@ExceptionHandler(ConstraintViolationException.class)
	public ModelAndView CVERestHandler() {
		return new ModelAndView("error/error").addObject("message", "잘못된 작업입니다");
	}
}




