package com.example.demo.controller.rest;

import java.io.*;
import java.security.*;
import java.util.*;

import org.apache.commons.io.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.http.*;
import org.springframework.security.access.prepost.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.*;

import com.example.demo.service.*;
import com.example.demo.util.*;

import jakarta.validation.constraints.*;

@RestController
public class BoardRestController {
	@Value("${sample.board.image.folder}")
	private String imageSaveFolder;
	@Value("${sample.board.image.url}")
	private String imageUrl;
	@Autowired
	private ImageUtil imageUtil;
	@Autowired
	private BoardService boardService;
	
	@PostMapping("/api/boards/image")
	public ResponseEntity<?> ck이미지업로드(MultipartFile upload) {
		// 업로드한 이미지를 저장 : 저장할 때 파일명이 겹칠 수 있으므로 미리 랜덤한 이름으로 변경하자
		// 1. 업로드한 이미지의 확장자를 잘라낸다 
		String originalFileName = upload.getOriginalFilename();
		String 확장자 = FilenameUtils.getExtension(originalFileName);
		// 2. 랜덤한 이미지 이름을 생성 
		String 저장할이미지이름 = UUID.randomUUID() + "." + 확장자;
		
		File file = new File(imageSaveFolder, 저장할이미지이름);
		try {
			// 업로드한 이미지(MultipartFile)은 메모리에 위치
			// transferTo는 파일을 이동
			upload.transferTo(file);
		} catch (IllegalStateException | IOException e) {
			e.printStackTrace();
		}
		
		// ck 이미지 업로드의 응답은 uploaded란 이름으로 1, url이란 이름으로 이미지 주소, filename이란 이름으로 이미지 이름
		
		String 주소 = imageUrl + 저장할이미지이름;
		Map<String, Object> map = Map.of("uploaded",1,"url",주소, "filename", 저장할이미지이름);
		return ResponseEntity.ok(map);
	}
	
	@GetMapping("/board/image")
	public ResponseEntity<byte[]> viewProfile(String imagename) {
		return imageUtil.getImageResponseEntity(imagename);
	}
	
	@PreAuthorize("isAuthenticated()")
	@PatchMapping("/api/boards/good")
	public ResponseEntity<Integer> 추천(@NotNull Long bno, Principal principal) {
		int 추천수 = boardService.추천(bno, principal.getName());
		return ResponseEntity.ok(추천수);
	}
	
	@PreAuthorize("isAuthenticated()")
	@PatchMapping("/api/boards/bad")
	public ResponseEntity<Integer> 비추(@NotNull Long bno, Principal principal) {
		int 비추수 = boardService.비추(bno, principal.getName());
		return ResponseEntity.ok(비추수);
	}
}

