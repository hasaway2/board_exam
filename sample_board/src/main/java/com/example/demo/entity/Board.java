package com.example.demo.entity;

import java.time.*;

import lombok.*;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class Board {
	private Long bno;
	private String title;
	private String content;
	private String writer;
	private LocalDateTime writeTime;
	private Integer readCnt;
	private Integer goodCnt;
	private Integer badCnt;

	// 글을 지우면 댓글을 어떻게 처리?
	// 1. 댓글까지 지운다
	// 2. 글의 내용을 출력하지 않고 댓글을 출력
	private Boolean isDeleted;
}
