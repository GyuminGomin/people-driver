package com.gls.ppldv.configuration.security.jwt;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;


/**
 * 시큐리티가 가자고 있는 filter 중 BasicAuthenticationFilter
 * 권한이나 인증이 필요한 특정 주소를 요청했을 시 필터를 무조건 타게 되어 있음
 * 만약 권한이나 인증이 필요하지 않은 주소라면 이 필터를 타지 않음
 */
public class JwtAuthorizationFilter extends BasicAuthenticationFilter{

	public JwtAuthorizationFilter(AuthenticationManager authenticationManager) {
		super(authenticationManager);
	}

	
	
	// 인증이나 권한이 필요한 주소 요청이 있을 때 해당 필터를 타게 됨
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		// TODO Auto-generated method stub
		super.doFilterInternal(request, response, chain);
		
		String jwtHeader = request.getHeader("Authorization");
		
		// header가 있는지 확인
		
		if (jwtHeader == null || !jwtHeader.startsWith("Bearer")) {
			chain.doFilter(request, response);
			return;
		}
		
		Claims claims = Jwts.parser()
				.setSigningKey(SECRET_KEY)
		
		// JWT 토큰을 검증 해 정상적인 사용자인지 확인
		String jwtToken = request.getHeader("Authorization").replace("Bearer ", "");
		
		String username = Jwts.
	}
	
	
	
}
