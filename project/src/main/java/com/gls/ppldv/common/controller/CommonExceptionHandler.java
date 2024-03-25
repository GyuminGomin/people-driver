package com.gls.ppldv.common.controller;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.gls.ppldv.configuration.userException.LoginFailedException;

@ControllerAdvice("com.gls.ppldv.user.controller")
public class CommonExceptionHandler {
	
	@ExceptionHandler(Exception.class)
	public void handleException(Exception e) {
		e.printStackTrace();
	}
	
	@ExceptionHandler(LoginFailedException.class)
	public ResponseEntity<String> handleLoginFailedException(LoginFailedException e) {
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "text/plain;charset=utf-8");
		return new ResponseEntity<>(e.getMessage(), headers, HttpStatus.OK);
	}
}
