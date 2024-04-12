package com.gls.ppldv.configuration.security.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class FindPassInterceptor implements HandlerInterceptor {

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		
		String requestURI = request.getRequestURI();
		
		String contextPath = request.getContextPath();
		
		if (requestURI.equals("/user/passAuth")) {
			// TODO 처리하다가 코테준비와 면접준비, 정처기 실기 준비 때문에 잠시 미뤄두자
		} else {
			
		}
		
		
		
		return HandlerInterceptor.super.preHandle(request, response, handler);
	}

	
}
