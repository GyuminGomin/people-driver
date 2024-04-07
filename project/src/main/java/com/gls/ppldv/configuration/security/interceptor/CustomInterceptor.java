package com.gls.ppldv.configuration.security.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import com.gls.ppldv.user.entity.Member;
import com.gls.ppldv.user.entity.Role;

@Component
public class CustomInterceptor implements HandlerInterceptor {

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		
		String requestURI = request.getRequestURI();
		
		String contextPath = request.getContextPath();
		
		Object obj = request.getSession().getAttribute("loginMember");
		
		String regex = "[-+]?\\d*\\.?\\d+"; // 숫자일때만 확인(게시글 번호가 숫자이므로)
		
		Member member = (Member) obj;
		
		// 일단 시큐리티 필터 체인에서 걸러지고
		// 들어올 때는 로그인 된 회원이며, Business 또는 Developer일 것임
		if (member.getRole() == Role.DEVELOPER) {
			// 권한이 Developer일 때 다른 사람 페이지로 접근 못하게 설정
			String uno = request.getParameter("id");
			// 쿼리스트링 id가 숫자일때만
			if (uno != null && !uno.trim().equals("") && uno.matches(regex)) {
				int id = Integer.parseInt(uno);
				if (member.getId() != id) {
					// 멤버의 Id가 일치하지 않으면, 처리 하지 않기로
					response.sendRedirect("/errors?message=wrongMember");
					return false;
				}
			}
			// 나머지는 문제 없으니
			return true;
		} else {
			// 권한이 BUSINESS일 때 다른 사람 페이지로 접근 못하게 설정
			String uno = request.getParameter("id");
			// 쿼리스트링 id가 숫자일때만
			if (uno != null && !uno.trim().equals("") && uno.matches(regex)) {
				int id = Integer.parseInt(uno);
				if (member.getId() != id) {
					// 멤버의 Id가 일치하지 않으면, 처리 하지 않기로
					response.sendRedirect("/errors?message=wrongMember");
					return false;
				}
			}
			// 나머지는 문제 없으니
			return true;
		}	
	}

}
