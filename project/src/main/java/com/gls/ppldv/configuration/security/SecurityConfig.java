package com.gls.ppldv.configuration.security;

import java.nio.file.AccessDeniedException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;

import com.gls.ppldv.configuration.security.handler.CsrfAccessDeniedHandler;
import com.gls.ppldv.configuration.security.handler.LoginFailureHandler;
import com.gls.ppldv.configuration.security.handler.LoginSuccessHandler;

@Configuration
public class SecurityConfig {

	@Autowired
	private LoginSuccessHandler loginSuccessHandler;
	@Autowired
	private LoginFailureHandler loginFailureHandler;
	@Autowired
	private CsrfAccessDeniedHandler csrfAccessDeniedHandler;
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

	/**
	 * 인증되든 인증되지 않든 모든 접속 유저
	 */
	private static final String[] PERMIT_URL = {
		// GET
		"/user/register", "/user/login", "/user/findPass",
		
		// POST
	};
	
	/**
	 * 로그인된 회원 (인증된 회원)
	 */
	private static final String[] AUTH_URL = {
		// GET
			// 나중에 토큰 방식 사용할 예정이기 때문에
			// /user/editProfile 수정 예정
		"/user/logout", "/user/editProfile", 
		
		
		
	};
	
	/**
	 * DEVELOPER 회원 (DEVELOPER 권한 인증 회원) 
	 */
	private static final String[] DEV_URL = {
		
	};
	
	/**
	 * BUSINESS 회원 (BUSINESS 권한 인증 회원) 
	 */
	private static final String[] BUS_URL = {
			
	};
	
	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http
			.csrf().csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
			.and()
			.exceptionHandling()
				.accessDeniedHandler(csrfAccessDeniedHandler)
				.and()
			.authorizeRequests()
				/*
				 * @Bean public WebSecurityCustomizer webSecurityCustomizer() { return (web) ->
				 * web.ignoring().antMatchers("/resources/**"); }
				 * 아래와 같은 것
				 */
				.antMatchers("/resources/**").permitAll()
				.antMatchers("/").permitAll()
				.antMatchers("/user/login", "/user/register", "/user/findPass").permitAll()
				.antMatchers("/user/logout").authenticated()
				.antMatchers("/user/edit", "/user/removeUser").authenticated()
				.antMatchers("/developer/**").hasRole("DEVELOPER")
				.antMatchers("business/**").hasRole("BUSINESS")
				.anyRequest().authenticated() // 나머지 요청은 인증된 사용자만 접근 가능
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
				.permitAll()
				.and()
			.headers()
				.contentSecurityPolicy("script-src 'self'");
		
		
		return http.build();
	}
}
