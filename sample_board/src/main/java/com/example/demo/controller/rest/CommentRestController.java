package com.example.demo.controller.rest;

import java.security.*;
import java.util.*;

import org.springframework.beans.factory.annotation.*;
import org.springframework.http.*;
import org.springframework.security.access.prepost.*;
import org.springframework.web.bind.annotation.*;

import com.example.demo.dto.*;
import com.example.demo.service.*;

@PreAuthorize("isAuthenticated()")
@RestController
public class CommentRestController {
	@Autowired
	private CommentService commentService;

	@PostMapping("/api/comments")
	public ResponseEntity<?> writeComment(Long bno, String content, Principal principal) {
		List<CommentDto.Read> comments = commentService.write(bno, content, principal.getName());
		return ResponseEntity.ok(comments);
	}
	
	@DeleteMapping("/api/comments")
	public ResponseEntity<List<CommentDto.Read>> remove(Long bno, Long cno, Principal principal) {
		List<CommentDto.Read> comments = commentService.remove(bno, cno, principal.getName());
		return ResponseEntity.ok(comments);
	}
}
