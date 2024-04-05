package com.gls.ppldv.configuration.security.handler;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.csrf.MissingCsrfTokenException;

@Configuration
public class CsrfAccessDeniedHandler implements AccessDeniedHandler {

	@Override
	public void handle(HttpServletRequest request, HttpServletResponse response,
			AccessDeniedException accessDeniedException) throws IOException, ServletException {
		
		String errorMessage = "csrf";
		
		// 응답 데이터 설정
		response.setStatus(HttpServletResponse.SC_FORBIDDEN); // 403 
	    response.setContentType("text/html;charset=utf-8");
	    response.getWriter().write(errorMessage);
	}
}
