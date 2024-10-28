package com.example.demo.exception;

import lombok.*;

// 사용자 정의 예외
// 예를 들어서 Exception에 대한 ExceptionHandler를 작성하면 오류가 발생하면 모두 에러 페이지로 이동
// 에러 페이지는 사용자에게 보여줄 목적, 개발자는 에러메시지를 봐야해

@Getter
@AllArgsConstructor
public class JobFailException extends RuntimeException {
	String message;
}
