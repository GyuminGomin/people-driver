package com.gls.ppldv.main.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class HomeController {

	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String home() {
		return "main/home";
	}

	@GetMapping("search")
	public String search() {
		return "/business/search";
	}
	
	@GetMapping("searchD")
	public String searchD() {
		return "redirect:developer/search";
	}
	
	// 성공 처리 (로그인 성공)
	@GetMapping("success")
	public String success(@RequestParam("message") String message, Model model) {
		model.addAttribute("message", message);
		return "/error/success";
	}
	
	@PostMapping("success")
	public String successP(
			HttpServletRequest request,
			HttpServletResponse response
			) {
		request.setAttribute("message", "loginSuccess");
		return "/error/success";
	}
	
}
