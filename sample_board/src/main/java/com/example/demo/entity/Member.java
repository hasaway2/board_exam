package com.example.demo.entity;

import java.time.*;
import java.time.temporal.*;

import org.hibernate.annotations.*;

import com.example.demo.dto.*;
import com.example.demo.enums.*;

import jakarta.persistence.*;
import lombok.*;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
// 이 클래스는 테이블과 1:1로 매치되는 테이블
@Entity
@DynamicUpdate
public class Member {
	@Id
	private String username;
	private String password;
	private String email;
	private LocalDate joinday;
	private LocalDate birthday;
	private String profile;
	private Integer loginFailCount;
	private Boolean enabled;
	@Enumerated(EnumType.STRING)
	private Role role;

	public void block() {
		this.enabled = false;
		this.loginFailCount = 5;
	}

	public void increaseLoginFailCount() {
		this.loginFailCount = 0;
	}

	public void changePassword(String password) {
		this.password = password;
	}

	public MemberDto.Read toReadDto(String url) {
		// 날짜를 계산하는 방법
		long days = ChronoUnit.DAYS.between(joinday, LocalDate.now());			
		return new MemberDto.Read(email, joinday, birthday, days, url + profile, role.name());
	}

	public void update(String email, String birthday, String 프로필이름) {
		// 이메일과 birthday의 경우 값을 변경하지 않으면 그 값을 그대로 업로드한다
		this.email = email;
		
		// 문자열 2020-11-11을 2020년 11월 11일 날짜로 변경
		
		// 문자열 2020-11-11을 -로 끊어 읽어서 문자열 배열로 변환
		String[] ar = birthday.split("-");
		int year = Integer.parseInt(ar[0]);
		int month = Integer.parseInt(ar[1]);
		int day = Integer.parseInt(ar[2]);
		LocalDate 생일 = LocalDate.of(year, month, day);
		this.birthday = 생일;
		
		if(프로필이름!=null)
			this.profile = 프로필이름;
	}
}





