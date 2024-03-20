package com.gls.ppldv.common.interceptor;

import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.web.servlet.HandlerInterceptor;

public class CheckTokenInterceptor implements HandlerInterceptor {
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		
		if (request.getMethod().toUpperCase().equals("POST")) {
			HttpSession session = request.getSession();
			String sessionToken = (String)session.getAttribute("csrf_token"); // 세션에 저장되어 있는 토큰
			String requestToken = request.getParameter("csrf_token"); // 전송하는 토큰(삭제요청할때 전송)
			// 상세보기 요청을 클릭하면 csrf_token이 존재
			if(requestToken == null || requestToken.equals("") || !requestToken.equals(sessionToken)) {
				// 토큰이 없거나, null이거나, 같지 않으면 (정상적인 경로로 우리 사이트를 통해 요청이 전달된게 아님)
				response.setContentType("text/html;charset=utf-8");
				PrintWriter out = response.getWriter();
				out.println("<script>");
				out.println("alert('Wrong Access');");
				out.println("history.go(-1);");
				out.println("</script>");
				return false;
			}
		}
		
		return true;
	}
}
