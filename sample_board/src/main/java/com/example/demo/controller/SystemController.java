package com.example.demo.controller;

import org.springframework.stereotype.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.*;

@Controller
public class SystemController {
	@GetMapping("/error/403")
	public ModelAndView error403() {
		return new ModelAndView("error/403");
	}
}
