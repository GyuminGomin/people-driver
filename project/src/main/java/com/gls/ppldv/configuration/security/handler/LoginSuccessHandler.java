package com.gls.ppldv.configuration.security.handler;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import com.gls.ppldv.configuration.security.CustomUserDetails;
import com.gls.ppldv.user.entity.Member;

@Configuration
public class LoginSuccessHandler implements AuthenticationSuccessHandler{

	private PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws IOException, ServletException {

		CustomUserDetails userDetail = (CustomUserDetails) authentication.getPrincipal();
		
		Member member = userDetail.getMember();
		
		// 로그인 성공 시 처리할 로직 담당
		response.sendRedirect("/user/login?");
		String root = request.getContextPath();
		response.sendRedirect(root);
		
	}
	
	
}
