package com.gls.ppldv.common.filter;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class MyFilter1 implements Filter{

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		
		
		HttpServletRequest req = (HttpServletRequest) request;
		HttpServletResponse res = (HttpServletResponse) response;
		// id,pw 정상적으로 로그인이 완료 되면 토큰을 생성해주고 응답
		// 요청할 때 마다 header에 Authorization의 value값으로 토큰 탑재 후 전달
		// 토큰 검증 RSA, HS256, HMAC으로
		if (req.getMethod().equalsIgnoreCase("post")) {
			String headerAuth = req.getHeader("Authorization");
			System.out.println(headerAuth);
			
			if (headerAuth.equals("cos")) {
				chain.doFilter(req, res);
			} else {
				PrintWriter out = res.getWriter();
				out.println("인증 안됨");
			}
		}
	}

	
}
