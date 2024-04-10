package com.gls.ppldv.configuration.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.gls.ppldv.configuration.security.handler.AuthAccessDeniedHandler;
import com.gls.ppldv.configuration.security.handler.AuthenticationDeniedHandler;
import com.gls.ppldv.configuration.security.handler.LoginFailureHandler;
import com.gls.ppldv.configuration.security.handler.LoginSuccessHandler;

@Configuration
public class SecurityConfig {

	@Autowired
	private LoginSuccessHandler loginSuccessHandler;
	@Autowired
	private LoginFailureHandler loginFailureHandler;
	@Autowired
	private AuthenticationDeniedHandler authenticationDeniedHandler;
	@Autowired
	private AuthAccessDeniedHandler authAccessDeniedHandler;
	@Autowired
	private UserDetailsService uds;
	
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
	
	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http
			.csrf().disable()
			.httpBasic().disable() // 쿠키방식 사용, Bearer : 토큰(노출 가능) 방식 사용할 예정
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
				.antMatchers("/user/logout", "/user/editProfile", "developer/Info").authenticated()
				.antMatchers("/developer/register", "/developer/profile", "/developer/readPage").hasRole("DEVELOPER")
				.antMatchers("/business/register").hasRole("BUSINESS")
				.anyRequest().permitAll()
				.and()
			.formLogin()
				.loginPage("/user/login").permitAll()
				.usernameParameter("email")
				.passwordParameter("password")
				.loginProcessingUrl("/user/login")
				.failureHandler(loginFailureHandler)
				.successHandler(loginSuccessHandler)
				.and()
			.rememberMe()
				.rememberMeCookieName("Id")
				.rememberMeParameter("checked")
				.tokenValiditySeconds(60*60*24*15)
				.userDetailsService(uds)
				.authenticationSuccessHandler(loginSuccessHandler)
				.and()
			.logout()
				.logoutUrl("/user/logout")
				.and()
			.headers()
				.contentSecurityPolicy("script-src 'self' 'unsafe-inline' http://code.jquery.com http://d1p7wdleee1q2z.cloudfront.net http://dapi.kakao.com https://cdn.tiny.cloud http://t1.daumcdn.net" );
			
		
		return http.build();
	}
}
