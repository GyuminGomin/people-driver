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
import org.springframework.security.web.csrf.InvalidCsrfTokenException;
import org.springframework.security.web.csrf.MissingCsrfTokenException;

import com.gls.ppldv.user.entity.Member;
import com.gls.ppldv.user.entity.Role;

@Configuration
public class AuthAccessDeniedHandler implements AccessDeniedHandler {

	@Override
	public void handle(HttpServletRequest request, HttpServletResponse response,
			AccessDeniedException accessDeniedException) throws IOException, ServletException {
		
		// 로그인된 회원의 role이 Business인데 예외가 발생하면, developer 접속했다는 의미
		// dev는 반대
		
		Object user = request.getSession().getAttribute("loginMember");
		Member member = (Member) user; 
		
		if (accessDeniedException instanceof InvalidCsrfTokenException) {
			// csrf 토큰이 잘못되었을 경우
			response.sendRedirect("/errors?message=csrf");
		} else {
			// 권한에 따른 접속 에러는 AccessDeniedException이 발생
			if (member.getRole() == Role.DEVELOPER) {
				// 만약 개발자 권한일 때 발생한다면, Business만 접속 가능한 경로일 것임
				response.sendRedirect("/errors?message=business");
			} else {
				response.sendRedirect("/errors?message=developer");
			}
		}
		
		System.out.println("----------------------------------");
		System.out.println(accessDeniedException.getClass());
		System.out.println("----------------------------------");
	}
}
