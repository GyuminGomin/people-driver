package com.gls.ppldv.common.controller;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class WebErrorController implements ErrorController {
	
	// 에러 처리
	@GetMapping("errors")
	public String error(@RequestParam("message") String message, Model model) {
		model.addAttribute("message", message);
		return "/error/error";
	}
	
	@GetMapping("/error")
	public String handleErrorGet(HttpServletRequest request) {
		Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
		
		if (status != null) {
			int statusCode = Integer.parseInt(status.toString());
			
			if (statusCode == HttpStatus.NOT_FOUND.value()) {
				return "error/error_404";
			} else {
				request.setAttribute("message", "wrong");
				return "error/error";
			}
		} else {
			request.setAttribute("message", "wrong");
			return "error/error";
		}
	}
	
	@GetMapping("/error/error_405")
	public void handleError_405 () {}
}
