package com.example.demo.util;

import java.io.*;
import java.nio.file.*;
import java.util.*;

import org.jsoup.*;
import org.jsoup.nodes.*;
import org.jsoup.select.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.http.*;
import org.springframework.stereotype.*;

@Component
public class ImageUtil {
	@Value("${sample.board.image.folder}")
	private String imageSaveFolder;
	
	// Jsoup을 이용해 글 본문에서 이미지 이름들을 추출하는 함수
	public List<String> getImageNames(String html) {
		// Jsoup을 이용해 html을 파싱
		Document document = Jsoup.parse(html);
		
		// 파싱한 html에서 img 태그들을 추출
		Elements elements = document.select("img");
		
		List<String> result = new ArrayList<>();
		for(Element element:elements) {
			String src = element.attr("src");
			int 등호위치 = src.indexOf("=");
			String imagename = src.substring(등호위치+1);
			result.add(imagename);
		}
		return result;
	}
	
	
	public ResponseEntity<byte[]> getImageResponseEntity(String imagename) {
		// File 객체를 사용하는 건 java.io 패키지
		// java.io는 한번에 하나씩 읽고 쓴다. 여러 작업을 수행할 때 오래 걸리는 작업을 수행하면 나머지 작업들을 대기(blocking)
		// 개선 버전이 java.nio
		
		try {
			// 이미지가 저장된 경로를 계산한다
			Path filePath = Paths.get(imageSaveFolder + imagename);
			// 파일을 byte[]로 변환
			byte[] imageBytes = Files.readAllBytes(filePath);
			
			// 파일을 주면 mime type을 알아낸다(mimeType == contentType)
			// mime은 원래 이메일 첨부파일에서 사용하던 개념. 현재는 웹에서 널리 쓰이는 데 이걸 contentType
			String mimeType = Files.probeContentType(filePath);
			
			// mime을 스프링 MediaType으로 변환
			// MediaType은 mime을 상수화한 것
			// mime : "image/jpg" -> MediaType.IMAGE_JPEG
			MediaType mediaType = MediaType.parseMediaType(mimeType);
			return ResponseEntity.status(HttpStatus.OK).contentType(mediaType).body(imageBytes);
		} catch(IOException e) {
			e.printStackTrace();
		}
		return ResponseEntity.status(409).body(null);
	}
}
