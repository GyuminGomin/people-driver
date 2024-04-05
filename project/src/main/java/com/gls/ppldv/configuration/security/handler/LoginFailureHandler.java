package com.gls.ppldv.configuration.security.handler;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.csrf.MissingCsrfTokenException;

@Configuration
public class LoginFailureHandler implements AuthenticationFailureHandler {

	@Override
	public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException exception) throws IOException, ServletException {
		
		String errorMessage = null;
		
		if (exception instanceof BadCredentialsException || exception instanceof InternalAuthenticationServiceException){
			// 실패 상태 코드 설정
		    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED); // 401 Unauthorized
		    // 실패 응답 데이터 생성
		    errorMessage = "Not Exist";
		} else {
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST); // 400 Bad Request
			errorMessage = "Error";
		}
	    // 응답 데이터 설정
	    response.setContentType("text/html;charset=utf-8");
	    response.getWriter().write(errorMessage);
	}

}
