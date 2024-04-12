package com.gls.ppldv.common.controller;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class PageController {
	// 회원 관련 페이지 이동
	@GetMapping("/user/register")
	public String userRegister() {
		return "/member/register";
	}
	@GetMapping("/user/login")
	public String userLogin() {
		return "/member/login";
	}
	@GetMapping("/user/findPass")
	public String userFindPass() {
		return "/member/findPass";
	}
	@GetMapping("/user/logout")
	public String userLogout(
			HttpServletResponse response,
			@CookieValue(name="Id", required = false) Cookie cookie,
			RedirectAttributes rttrs
			) {
		if (cookie != null) {
			cookie.setPath("/");
			cookie.setMaxAge(0);
			response.addCookie(cookie);
		}
		return "redirect:/";
	}
	@GetMapping("/user/editProfile")
	public String userEditProfile() {
		return "/member/editProfile";
	}
	
	
	// 개발자 관련 페이지 이동
	
	// 개발자 프로필 등록
	@GetMapping("/developer/register")
	public String devRegister() {
		return "/developer/register";
	}
	
	// 개발자 프로필 조회
	@GetMapping("/developer/profile")
	public String devProfile() {
		return "/developer/profile";
	}
	
	// 개발자가 다른 페이지 오픈
	
	// 비즈니스 관련 페이지 이동
}
