package com.gls.ppldv.common.interceptor;

import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.HandlerInterceptor;

import com.gls.ppldv.user.entity.Member;
import com.gls.ppldv.user.entity.Role;

public class AuthDeveloperInterceptor implements HandlerInterceptor {

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
				// 로그인 된 사용자가 BUSINESS일 때 접속 불가하게
				Member m = (Member)obj; // 로그인된 사용자 정보
				if (m.getRole() == Role.BUSINESS) {
					if (requestURI.equals("/developer/Info")) {
						return true;
					}
					out = response.getWriter();
					
					out.println("<script>");
					out.println("alert('Only Developer Access Possible');");
					out.println("history.go(-1)");
					out.println("</script>");
					return false;
				} else {
					// 로그인된 사용자가 DEVELOPER
					String uno = request.getParameter("id");
					// 쿼리스트링 id가 숫자일때만
					if (uno != null && !uno.trim().equals("") && uno.matches(regex)) {
						int id = Integer.parseInt(uno); // uno
						Member mem = (Member)obj;
						// 로그인한 회원만 프로필에 등록할 수 있도록 요청id와 로그인된 회원이 같지 않으면
						if (!requestURI.equals("/developer/Info")) {
							if (mem.getId() != id) {
								out = response.getWriter();
								
								out.println("<script>");
								out.println("alert('You Don't Access His/Her Profile');");
								out.println("history.go(-1)");
								out.println("</script>");
								return false;
							} else return true; // 로그인한 회원이랑 요청 쿼리스트링 id가 같으면 요청 허용
						} else return true; // /developer/info 경로로 요청한 것은 id에 상관없이 접근 가능 
						
					} else return true; // 쿼리스트링 id가 숫자가 아닐때 (404가 뜰거임)
				}
			} else return true; // 요청이 POST일 때
		}
		
	}
	
}
