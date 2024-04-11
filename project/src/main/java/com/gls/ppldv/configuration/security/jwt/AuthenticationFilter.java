package com.gls.ppldv.configuration.security.jwt;

import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import com.gls.ppldv.configuration.security.CustomUserDetails;

// 이거 설명할 때는 공식 문서 필터의 흐름을 참조하자
public class AuthenticationFilter extends UsernamePasswordAuthenticationFilter {
	
	private final AuthenticationManager authenticationManager;
	private final JwtProvider jwtProvider;
	
	// 직접 구현하게 되면, 부모 클래스를 호출해서 요청 경로를 수정해줘야 함
	public AuthenticationFilter(AuthenticationManager authenticationManager, JwtProvider jwtProvider) {
		super.setRequiresAuthenticationRequestMatcher(new AntPathRequestMatcher("/user/login", "POST"));
		this.authenticationManager = authenticationManager;
		this.jwtProvider = jwtProvider;
	}
	
	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
			throws AuthenticationException {
		
		// 요청을 가로채서 요청에 담겨있는 유저네임과 password 받기
		String email = request.getParameter("email");
		String password = request.getParameter("password");

		/*
		 * String username = obtainUsername(request); String password =
		 * obtainPassword(request);
		 */
		
		// 로그인 할 때, 권한이 무엇인지 비교할 필요는 딱히 없으므로 null로 설정함
		UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(email, password, null);
		
		// 검증 진행(formLogin 사용하면 이것들을 알아서 다 처리해줌)
		return authenticationManager.authenticate(authToken);
	}

	// 로그인 성공 시
	@Override
	protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
			Authentication authResult) throws IOException, ServletException {
		CustomUserDetails customUserDetails = (CustomUserDetails) authResult.getPrincipal();
		
		String username = customUserDetails.getUsername();
		
		Collection<? extends GrantedAuthority> authorities = authResult.getAuthorities();
		Iterator<? extends GrantedAuthority> Iterator = authorities.iterator();
		GrantedAuthority auth = Iterator.next();
		
		String role = auth.getAuthority();
		System.out.println("--------------------");
		System.out.println("role : " + role);
		System.out.println("--------------------");
		
		String token = jwtProvider.generateToken(authResult);
		
		response.addHeader("jwtToken", token);
	}

	// 로그인 실패 시
	@Override
	protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException failed) throws IOException, ServletException {
		// 실패시 401 응답 반환
		response.setStatus(401);
	}
	
	
	
	
	
	
}
