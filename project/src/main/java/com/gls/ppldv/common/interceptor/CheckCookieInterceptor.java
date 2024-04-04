package com.gls.ppldv.common.interceptor;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.util.WebUtils;

import com.gls.ppldv.common.util.CookieUtils;
import com.gls.ppldv.user.entity.Member;
import com.gls.ppldv.user.repository.MemberRepository;

public class CheckCookieInterceptor implements HandlerInterceptor {

	@Autowired
	MemberRepository mr;

	/**
	 * 자동 로그인 구현
	 */
	@Override
	public boolean preHandle(HttpServletRequest request,
			HttpServletResponse response,
			Object handler)
			throws Exception {

		HttpSession session = request.getSession();
		if (session.getAttribute("loginMember") != null) {
			return true;
		}

		Cookie cookie = WebUtils.getCookie(request, "id");
		if (cookie != null) {
			String email = cookie.getValue();
			String decryptedEmail = CookieUtils.decrypt(email);
			Member member = mr.findByEmail(decryptedEmail).get();
			session.setAttribute("loginMember", member);
		}

		return true;
	}

}
