package com.example.demo.controller;

import java.security.*;

import org.springframework.beans.factory.annotation.*;
import org.springframework.security.access.prepost.*;
import org.springframework.security.core.*;
import org.springframework.stereotype.*;
import org.springframework.validation.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.*;

import com.example.demo.dto.*;
import com.example.demo.entity.*;
import com.example.demo.exception.*;
import com.example.demo.service.*;

import jakarta.validation.*;

@Controller
public class BoardController {
	@Autowired
	private BoardService boardService;
	
	@PreAuthorize("isAuthenticated()")
	@GetMapping("/board/write")
	public ModelAndView ckEditor사용() {
		return new ModelAndView("board/write");
	}
	
	@PreAuthorize("isAuthenticated()")
	@PostMapping("/board/write")
	public ModelAndView ckEditor작성(@Valid BoardDto.Create dto, BindingResult br, Principal principal) {
		Long bno = boardService.write(dto, principal.getName());
		return new ModelAndView("redirect:/board/read?bno=" + bno);
	}
	
	@GetMapping("/board/read")
	public ModelAndView read(Long bno, Principal principal) {
		String loginId = principal==null? null : principal.getName();
		BoardDto.Read dto = boardService.read(bno, loginId);
		return new ModelAndView("board/read").addObject("result", dto);
	}
	
	@GetMapping({"/", "/board/list"})
	public ModelAndView list(@RequestParam(defaultValue="1") Integer pageno) {
		return new ModelAndView("board/list").addObject("result", boardService.findAll(pageno));
	}
	
	@PreAuthorize("isAuthenticated()")
	@GetMapping("/board/update")
	public ModelAndView update(Long bno) {
		BoardDto.Read dto = boardService.read(bno, null);
		return new ModelAndView("board/update").addObject("result", dto);
	}
	
	@PreAuthorize("isAuthenticated()")
	@PostMapping("/board/update")
	public ModelAndView update(@Valid BoardDto.Update dto, BindingResult br, Principal principal) {
		System.out.println(dto);
		boardService.update(dto, principal.getName());
		return new ModelAndView("redirect:/board/read?bno="+dto.getBno());
	}

	@PreAuthorize("isAuthenticated()")
	@PostMapping("/board/delete")
	public ModelAndView delete(long bno, Principal principal) {
		boardService.delete(bno, principal.getName());
		return new ModelAndView("redirect:/");
	}
	
	@ExceptionHandler(JobFailException.class)
	public ModelAndView jobFailExceptionHandler(JobFailException e) {
		return new ModelAndView("error/error").addObject("message", e.getMessage());
	}
	
}
