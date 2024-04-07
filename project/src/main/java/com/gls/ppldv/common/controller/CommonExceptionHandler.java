package com.gls.ppldv.common.controller;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class CommonExceptionHandler {
	
	@ExceptionHandler(Exception.class)
	public void handleException(Exception e, HttpServletResponse response) throws IOException {
		e.printStackTrace();
		System.out.println("----------------------");
		System.out.println(e.getClass());
		System.out.println("----------------------");
		
		response.sendRedirect("/error?message=wrong");
		// 지금 500 에러들은 모두 다 이게 처리하고 있음
		// 지금 403은 Spring Security에서 다 처리하고 있음
	}
	
	@ExceptionHandler(HttpRequestMethodNotSupportedException.class)
	public void handleException(HttpRequestMethodNotSupportedException e, HttpServletResponse response) throws IOException {
		response.sendRedirect("/error/error_405");
	}
}
