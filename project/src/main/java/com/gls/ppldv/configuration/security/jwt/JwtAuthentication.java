package com.gls.ppldv.configuration.security.jwt;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.fasterxml.jackson.core.exc.StreamReadException;
import com.fasterxml.jackson.databind.DatabindException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gls.ppldv.user.entity.Member;

import lombok.RequiredArgsConstructor;

// 스프링 시큐리티에 있는 UsernamePasswordAuthenticationFilter
// 로그인 요청하고 username, password를 post로 전송하면 이 필터가 동작
@RequiredArgsConstructor
public class JwtAuthentication extends UsernamePasswordAuthenticationFilter{
	// formLogin disable하면 작동하지 않음
	// AuthenticationFilter(AuthenticationManager) 던져줘야 함
	
	private final AuthenticationManager authenticationManager;

	// login 요청 하면 로그인 시도를 위해 실행되는 함수
	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
			throws AuthenticationException{
		
		ObjectMapper om = new ObjectMapper();
		try {
			Member member = om.readValue(request.getInputStream(), Member.class);
			
			UsernamePasswordAuthenticationToken authenticationToken =
					new UsernamePasswordAuthenticationToken(member.getEmail(), member.getPassword());
			
			// CustomDetailsService의 loadUserByUsename() 함수가 실행됨
			Authentication authentication =
					authenticationManager.authenticate(authenticationToken);
			return authentication;
		} catch (IOException e) {
			e.printStackTrace();
		} 
		
		// 로그인 시도를 autheticationManger로 시도하면
		// CustomDetailsService가 호출해서 loadbyUsername()을 실행
		// CustomDetailsService를 세션에 담아 (권한 관리를 위해)
		return null;
	}
}
