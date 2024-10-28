package com.example.demo;

import org.springframework.boot.*;
import org.springframework.boot.autoconfigure.*;
import org.springframework.context.annotation.*;
import org.springframework.security.crypto.bcrypt.*;
import org.springframework.security.crypto.password.*;

@SpringBootApplication
public class SampleBoardApplication {
	public static void main(String[] args) {
		SpringApplication.run(SampleBoardApplication.class, args);
	}
	
	// @Component를 붙이면 스프링이 ComponentScan을 통해 스프링빈을 생성 -> DI
	// 의존하는 클래스의 경우 객체를 생성하는 메소드에 @Bean을 지정 -> 스프링빈으로 등록, DI
	@Bean
	PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
}
