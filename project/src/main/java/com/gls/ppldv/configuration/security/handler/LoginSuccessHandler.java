package com.gls.ppldv.configuration.security.handler;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import com.gls.ppldv.configuration.security.CustomUserDetails;
import com.gls.ppldv.configuration.security.jwt.JwtProvider;
import com.gls.ppldv.user.entity.Member;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class LoginSuccessHandler implements AuthenticationSuccessHandler{

	private final JwtProvider jwtProvider;
	
	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws IOException, ServletException {

		CustomUserDetails userDetail = (CustomUserDetails) authentication.getPrincipal();
		
		Member member = userDetail.getMember();
		
		String jwtToken = jwtProvider.generateToken(authentication);
		
		/*
		Cookie cookie = new Cookie("jt", jwtToken);
		cookie.setPath("/");
		response.addCookie(cookie);
		*/
		
		// response.setHeader("jwtToken", jwtToken);
		
		request.setAttribute("jwtToken", jwtToken);
		// 이전 요청이 POST 였기 때문에 POST로 forward 요청을 보내게 되어서 문제가 발생
		// 그래서 이러한 경우를 해결해주기 위해서는 어떻게 해야할까?
		// /success라는 post 요청처리하는 컨트롤러를 만들고, modelAndView 객체로 model의 header에 jwtToken을 담아서 보내주면 된다.
		request.getRequestDispatcher("/success").forward(request, response);
		
		
		// 정리
		/*
		 * 왜 jwt Token을 restful한 서버에서 사용하냐 하면,
		 * 응답에 따른 성공과 에러 처리를 한 페이지에서 처리할 수 있기 때문
		 * 만약 form을 사용하게 되면, RESTAPI로 데이터를 보내도 (프론트가) 받을 수가 없음
		 * 따라서 무조건 ModelAndView 객체로 담아서 프론트로 보내줘야 하는데, 서버에 의존성이 극심해지고, 독립성을 유지할 수가 없음
		 * 즉, 서버가 페이지를 이동하는 처리 로직을 담당하므로 서버에 부하가 많이 생김과 동시에 새롭게 process를 담당하는 추가적인 페이지도 개설해야 하므로, 프론트의 용량이 너무 커지게 됨
		 */
		
		/*
		 * 따라서 나는 이제 부터 서버에 대한 의존성을 줄이기 위해 form 태그로 작성한 코드들을
		 * 전부 다 ajax로 바꿔서 처리를 하려고 함
		 */
	}
	
	
}
