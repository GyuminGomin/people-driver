package com.gls.ppldv.configuration.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
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
	 * Permit_all (전체 허용)
	 */
	private static final String[] PERMIT_URL = {
		// /user/register는 회원가입, /user/login은 로그인, /user/findPass는 비밀번호 찾기, /user/logout 인증된 회원, /user/editProfile 인증된 회원
		// user
		"/user/register", "/user/login", "/user/findPass"
		// /dev/register는 내 이력서 등록, /dev/profile 내 이력서 확인, /dev/readPage 내 이력서 수정 삭제, /dev/readViewCount all, /dev/readOtherPage all, /dev/search all, /dev/searchFirst all, /dev/Info 인증된 회원
		// developer
		,"/developer/register", "/developer/profile", "/developer/readPage", "/developer/readViewCount", "/developer/readOtherPage", "/developer/search", "/developer/searchFirst", "/developer/Info"
		// business는 생략
	};
	
	/**
	 * 로그인된 회원 (인증된 회원) (authentication)
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
				.authenticationEntryPoint()
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
				.permitAll()
				.and()
			.headers()
				.contentSecurityPolicy("script-src 'self'");
		
		
		return http.build();
	}
}
