package com.gls.ppldv.common.interceptor;

import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.HandlerInterceptor;

public class AuthMemberInterceptor implements HandlerInterceptor {

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		
		// 전체 URL 요청 경로
		String requestURI = request.getRequestURI();
		// 프로젝트 경로
		String contextPath = request.getContextPath();
		// session에 저장된 로그인된 사용자 정보
		Object obj = request.getSession().getAttribute("loginMember");
		
		if (obj == null) {
			// 미로그인된 사용자
			if (request.getMethod().equalsIgnoreCase("get")) {
				response.setContentType("text/html;charset=utf-8");
				PrintWriter out = response.getWriter();
				
				out.println("<script>");
				out.println("alert('Login Please');");
				out.println("location.href='"+contextPath+"/user/login'");
				out.println("</script>");
				return false;
			}
			return true;
		} else {
			// 로그인된 사용자
			return true;
		}
	}
	
	
}
