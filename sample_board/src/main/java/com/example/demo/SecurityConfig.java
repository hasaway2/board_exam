package com.example.demo;

import org.springframework.beans.factory.annotation.*;
import org.springframework.context.annotation.*;
import org.springframework.security.config.annotation.method.configuration.*;
import org.springframework.security.config.annotation.web.builders.*;
import org.springframework.security.config.annotation.web.configuration.*;
import org.springframework.security.web.*;

import com.example.demo.security.*;

// 이 클래스는 스프링 설정 객체다 - java config, xml config
@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled=true, securedEnabled=true)
public class SecurityConfig {
	@Autowired
	private LoginSuccessHandler successHandler;
	@Autowired
	private LoginFailureHandler failureHandler;
	@Autowired
	private MyEntryPoint myEntryPoint;
	
	@Bean
	SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		// 스프링 시큐리티 필터 중 내가 원하는 부분만 살짝 살짝 커스터마이즈해서 등록
		
		// csrf 검증하지 않을 주소 지정 : /api/boards/image
		http.csrf(c->c.ignoringRequestMatchers("/api/boards/image"));
		
		http.formLogin(c->c.loginPage("/member/login").loginProcessingUrl("/member/login")
			.successHandler(successHandler).failureHandler(failureHandler));
		http.logout(c->c.logoutUrl("/member/logout").logoutSuccessUrl("/"));
		http.exceptionHandling(c->c.authenticationEntryPoint(myEntryPoint));
		return http.build();
	}
}

// ※ 스프링 시큐리티 ※ 
// 1. 인증과 인가
// - 인증(Authentication) : 상대방의 신원을 확인 -> 일반적으로 로그인을 사용 -> 401 오류
//						   SSR방식은 로그인창으로 이동, CSR방식은 401로 응답 -> 프론트에서 이동 처리
// - 인가(Authorization) : 인증된 사용자를 대상으로 권한을 확인 -> 403 오류
// - 인증에는 사용자 인증 뿐 아니라 데이터 인증도 있다
//			데이터 위변조를 막자 -> 암호화(너무 비싸)
//			데이터 위변조를 잡자 -> 데이터 인증(데이터 뒤에 인증코드를 붙여서 전송 -> 수신측에서 인증코드를 확인)
// - 스프링 시큐리티는 웹페이지 위변조를 막는 csrf 토큰 기능을 제공한다

// 2. 웹서버와 WAS(Web Application Server)
// - 웹서버는 http요청을 접수한 다음 정적인 파일(html, css, 이미지...)인 경우 직접 응답한다
// - 정적인 파일 요청인 아닌 경우 뒤에 있는 WAS에게 요청을 넘긴다
// - WAS가 동적인 작업을 처리한 다음 결과 html을 만들어 웹서버에 전달 -> 웹서버가 응답

// 3. WAS가 돌리는 백엔드 자바 표준 - Servlet
// - 웹서버(국제 표준) -> WAS(서블릿으로 처리. 자바 표준)
// - 웹서버 + WAS 구조에서 스프링이 돌아가기 위해서 스프링은 모든 요청/응답을 담당하는 DispatcherServlet을 제공 
// - 웹서버 + WAS + DispatcherServlet + 스프링의 구조로 스프링이 운용된다

// 4. 반복되는 작업(횡단관심 - 대표적으로 로그인)을 분리해서 처리하기
// - Filter : DispatcherServlet 앞에서 실행(서블릿 기술. 스프링 X)
// - Interceptor : Controller 앞이나 뒤에서 실행
// - AOP : 더 정밀하게 실행 조건을 지정(패키지를 지정, 리턴되면, 예외가 발생하면....)

// 5. 스프링 시큐리티는 필터 기반으로 동작 -> 필터 동작을 확인하라면 로깅을 debug로
// - UserDetailsService -> 사용자 정보를 가져와서 스프링 시큐리티 인증 핸들러에 넘겨주는 역할 
// - 로그인 성공 핸들러 -> 로그인 후 후처리
// - 로그인 실패 핸들러 -> 로그인 실패 후처리
// - AuthenticationEntryPoint -> 401에 대응 (기본값 : 로그인 주소로 redirect)
// - AccessDeniedHander -> 403에 대응 (기본값 : BasicErrorController에서 403 오류화면)















