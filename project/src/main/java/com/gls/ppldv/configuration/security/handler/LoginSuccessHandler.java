package com.gls.ppldv.configuration.security.handler;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

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

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws IOException, ServletException {

		CustomUserDetails userDetail = (CustomUserDetails) authentication.getPrincipal();
		
		Member member = userDetail.getMember();
		
		// 회원 정보 session에 저장
		HttpSession session = request.getSession();
		session.setAttribute("loginMember", member);
	}
	
	
}
