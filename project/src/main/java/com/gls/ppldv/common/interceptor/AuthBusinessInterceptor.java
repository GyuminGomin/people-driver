package com.gls.ppldv.common.interceptor;

import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.HandlerInterceptor;

import com.gls.ppldv.user.entity.Member;
import com.gls.ppldv.user.entity.Role;

public class AuthBusinessInterceptor implements HandlerInterceptor {

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		
		// 전체 URL 요청 경로
		String requestURI = request.getRequestURI();
		// 프로젝트 경로
		String contextPath = request.getContextPath();
		// session에 저장된 로그인된 사용자 정보
		Object obj = request.getSession().getAttribute("loginMember");
		
		String regex = "[-+]?\\d*\\.?\\d+"; // 숫자일때만 확인(게시글 번호가 숫자이므로)
	
		response.setContentType("text/html;charset=utf-8");
		PrintWriter out = null;
		
		// 미로그인된 사용자
		if (obj == null) {
			if (request.getMethod().equalsIgnoreCase("get")) {
				// get 매핑일 때만
				out = response.getWriter();
				
				out.println("<script>");
				out.println("alert('Login Please');");
				out.println("location.href='"+contextPath+"/user/login'");
				out.println("</script>");
				return false;
			}
			return true;
		// 로그인된 사용자
		} else {
			if (request.getMethod().equalsIgnoreCase("get")) {
				// 로그인 된 사용자가 DEVELOPER일 때 접속 불가하게
				Member m = (Member)obj; // 로그인된 사용자 정보
				if (m.getRole() == Role.DEVELOPER) {
					out = response.getWriter();
					
					out.println("<script>");
					out.println("alert('Only Business Access Possible');");
					out.println("history.go(-1)");
					out.println("</script>");
					
					return false;
				} else {
					// 로그인된 사용자가 BUSINESS
					String uno = request.getParameter("id");
					// 쿼리스트링 id가 숫자일때만
					if (uno != null && !uno.trim().equals("") && uno.matches(regex)) {
						int id = Integer.parseInt(uno);
						Member mem = (Member)obj;
						// 로그인한 회원만 등록할 수 있도록 요청 id와 로그인한 회원이 같지 않으면
						if (!requestURI.equals("/business/register")) {
							if (mem.getId() != id) {
								out = response.getWriter();
								
								out.println("<script>");
								out.println("alert('You Don't Access His/Her Profile');");
								out.println("history.go(-1)");
								out.println("</script>");
								return false;
							} else return true; // 같을때,
						} else return true; // business/register가 아닐때
					} else return true; // 숫자가 아닐때 404가 뜰거임
				}
			} else return true; // post 방식일 때
		}
	}

	
}
