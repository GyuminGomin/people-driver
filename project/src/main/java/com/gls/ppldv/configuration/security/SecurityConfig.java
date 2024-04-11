package com.gls.ppldv.configuration.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.gls.ppldv.configuration.security.handler.AuthAccessDeniedHandler;
import com.gls.ppldv.configuration.security.handler.AuthenticationDeniedHandler;
import com.gls.ppldv.configuration.security.jwt.AuthenticationFilter;
import com.gls.ppldv.configuration.security.jwt.JwtProvider;

@Configuration
public class SecurityConfig {

	@Autowired
	private AuthenticationDeniedHandler authenticationDeniedHandler;
	@Autowired
	private AuthAccessDeniedHandler authAccessDeniedHandler;
	@Autowired
	private AuthenticationConfiguration authenticationConfiguration;
	@Autowired
	private JwtProvider jwtProvider;
	
	/**
	 * 패스워드 인코더
	 * BCrypt (Blowfish 알고리즘 기반)
	 * - Salting(해싱된 비밀번호에 무작위 솔트를 추가하여 레인보우 테이블과 같은해킹 기법을 방어)
	 * - Iterations(해싱 알고리즘을 반복적으로 적용)
	 */
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	/**
	 * AuthenticationManager (인증 담당)을 시큐리티에서 가져오기 위한 설정 
	 */
	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
		return configuration.getAuthenticationManager();
	}
	
	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http
			.csrf().disable()
			.formLogin().disable()
			.httpBasic().disable() // 쿠키방식 사용, Bearer : 토큰(노출 가능) 방식 사용할 예정
			.addFilterAt(new AuthenticationFilter(authenticationManager(authenticationConfiguration), jwtProvider), UsernamePasswordAuthenticationFilter.class) // 인증 필터 등록
			.sessionManagement() // 세션 정책을 Stateless로 지정해 Spring Security가 세션을 생성하지 않고, 각 요청 간 상태를 유지하지 않음을 의미 -> SecurityContextHolder가 세션을 사용하지 않게 됨
				.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
				.and()
			.exceptionHandling()
				.accessDeniedHandler(authAccessDeniedHandler)
				.authenticationEntryPoint(authenticationDeniedHandler)
				.and()
			.authorizeRequests()
				/*
				 * @Bean public WebSecurityCustomizer webSecurityCustomizer() { return (web) ->
				 * web.ignoring().antMatchers("/resources/**"); }
				 * 아래와 같은 것
				 */
				.antMatchers("/resources/**").permitAll()
				.antMatchers("/user/logout", "/user/editProfile", "/developer/Info").authenticated()
				.antMatchers("/developer/register", "/developer/profile", "/developer/readPage").hasRole("DEVELOPER")
				.antMatchers("/business/register").hasRole("BUSINESS")
				.anyRequest().permitAll()
				.and()
			.rememberMe()
				.rememberMeCookieName("Id")
				.rememberMeParameter("checked")
				.tokenValiditySeconds(60*60*24*15)
				.and()
			.logout()
				.logoutUrl("/user/logout")
				.and()
			.headers()
				.contentSecurityPolicy("script-src 'self' 'unsafe-inline' http://code.jquery.com http://d1p7wdleee1q2z.cloudfront.net http://dapi.kakao.com https://cdn.tiny.cloud http://t1.daumcdn.net" );
			
		
		return http.build();
	}
}
