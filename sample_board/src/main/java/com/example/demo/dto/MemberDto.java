package com.example.demo.dto;

import java.time.*;

import org.aspectj.lang.annotation.*;
import org.springframework.format.annotation.*;
import org.springframework.web.multipart.*;

import com.example.demo.entity.*;
import com.example.demo.enums.*;

import jakarta.validation.constraints.*;
import lombok.*;

public class MemberDto {
	// MemberDto 객체는 필요없다. 객체 생성 금지
	private MemberDto() {}
	
	@Data
	public static class Create {
		@NotEmpty
		@Pattern(regexp="^[a-z0-9]{6,10}$")
		private String username;
		
		@NotEmpty	
		private String password;
		
		@NotEmpty 
		@Email 
		private String email;
		
		// <input type="date"> -> 2010-10-20 -> 스프링이 못 받는다
		@DateTimeFormat(pattern="yyyy-MM-dd")
		private LocalDate birthday;
		
		// 파일 업로드 형식
		private MultipartFile profile;
		
		public Member toEntity(String profileName, String encodedPassword) {
			return new Member(username.toLowerCase(), encodedPassword, email, LocalDate.now(), birthday, profileName, 0, true, Role.USER);
		}
	}
	
	@Data
	public static class UpdatePassword {
		private String oldPassword;
		private String newPassword;
	}
	
	@Data
	public static class Update {
		@NotEmpty
		@Email 
		private String email;
		
		// 스프링 빈 검증은 LocalDate를 지원하지 않는다
		// 검증을 원한다면 private String birthday로 받아서 검증을 수행한 다음 LocalDate로 변환
		@NotEmpty
		@Pattern(regexp="^[0-9]{4}-[0-9]{2}-[0-9]{2}$")
		private String birthday;
		
		private MultipartFile profile;
	}
	
	@Data
	@AllArgsConstructor
	public static class Read {
		private String email;
		private LocalDate joinday;			
		private LocalDate birthday;
		private long days;
		private String profile;
		private String role;
	}
}
