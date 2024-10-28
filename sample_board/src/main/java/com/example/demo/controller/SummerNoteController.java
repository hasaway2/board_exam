package com.example.demo.controller;

import org.springframework.stereotype.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.*;

@Controller
public class SummerNoteController {
	private String title;
	private String content;
	
	@GetMapping("/summer/read")
	public ModelAndView summerNoteRead() {
		return new ModelAndView("summer/read").addObject("content", this.content).addObject("title", this.title);
	}
	
	@GetMapping("/summer/write")
	public ModelAndView summerNoteWrite() {
		return new ModelAndView("summer/write");
	}
	
	@PostMapping("/summer/write")
	public ModelAndView summerNoteWrite(String title, String content) {
		this.title = title;
		this.content = content;
		return new ModelAndView("redirect:/summer/read");
	}
	
	@GetMapping("/summer/update")
	public ModelAndView summerNoteUpdate() {
		return new ModelAndView("summer/update").addObject("content", this.content).addObject("title", this.title);
	}
	
	@PostMapping("/summer/update")
	public ModelAndView summerNoteUpdate(String content) {
		this.content = content;
		return new ModelAndView("redirect:/summer/read");
	}
}
