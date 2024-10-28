package com.example.demo.entity;

import java.time.*;

import lombok.*;

@Getter
@AllArgsConstructor
public class Comment {
	private Long cno;
	private String content;
	private String writer;
	private LocalDateTime writeTime;
	private Long bno;
}
