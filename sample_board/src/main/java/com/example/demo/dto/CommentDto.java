package com.example.demo.dto;

import lombok.*;

public class CommentDto {
	private CommentDto() {}
	
	@Data
	public static class Read {
		private Long cno;
		private String content;
		private String writer;
		private String writeTime;
	}
}
