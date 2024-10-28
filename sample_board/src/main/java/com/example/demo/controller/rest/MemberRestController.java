package com.example.demo.controller.rest;

import java.util.*;

import org.springframework.beans.factory.annotation.*;
import org.springframework.http.*;
import org.springframework.validation.annotation.*;
import org.springframework.web.bind.annotation.*;

import com.example.demo.service.*;
import com.example.demo.util.*;

import jakarta.validation.*;
import jakarta.validation.constraints.*;


// 응답은 @RepsonseBody 또는 ResponseEntity(@ResponseBody + 상태코드)
@RestController
@Validated
public class MemberRestController {
	@Autowired
	private MemberService memberService;
	@Autowired
	private ImageUtil imageUtil;
	
	// rest 방식은 주로 명사를 가지고 주소를 구성
	// rest는 방식은 get, post뿐 아니라 put, patch, delete까지 사용한다
	// (get, post를 제외한 나머지는 사실상 사용되지 않던 메소드. 재발견)
	// GET 		/products/123		123번 상품을 읽어라
	// POST 	/products/123		123번 상품을 작성해라
	// PUT 		/products/123		123번 상품을 변경해라(전체 변경)
	// PATCH 	/products/123		123번 상품을 변경해라(부분 변경)
	// DELETE 	/products/123		123번 상품을 삭제해라
	
	@GetMapping("/members/username/available")
	public ResponseEntity<?> 아이디사용가능(@Valid @NotEmpty String username) {
		// 검증은 자바 검증, 스프링 검증이 있다
		boolean result = memberService.아이디사용가능(username);
		if(result==true)
			return ResponseEntity.ok(true);
		return ResponseEntity.status(409).body(false);
	}
	
	@GetMapping("/members/username")
	public ResponseEntity<?> 아이디찾기(@Valid @NotEmpty String email) {
		Optional<String> result = memberService.아이디찾기(email);
		if(result.isEmpty())
			return ResponseEntity.status(409).body(null);
		return ResponseEntity.ok(result.get());
	}
	
	@PatchMapping("/members/reset-password")
	public ResponseEntity<?> 비밀번호리셋(@Valid @NotEmpty String username) {
		boolean result = memberService.비밀번호찾기로_임시비밀번호_발급(username);
		if(result==true)
			return ResponseEntity.ok(null);
		return ResponseEntity.status(409).body(null);
	}
	
	// 예외를 처리하는 컨트롤러
	@ExceptionHandler(ConstraintViolationException.class) 
	public ResponseEntity<?> CVEHandler(ConstraintViolationException e) {
		System.out.println(e.getMessage());
		return ResponseEntity.status(409).body(e.getMessage());
	}
	
	@GetMapping("/member/profile")
	public ResponseEntity<byte[]> viewProfile(String profile) {
		return imageUtil.getImageResponseEntity(profile);
	}
	
}

