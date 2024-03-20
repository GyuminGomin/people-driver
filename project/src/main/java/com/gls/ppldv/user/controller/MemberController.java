package com.gls.ppldv.user.controller;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.gls.ppldv.common.util.CookieUtils;
import com.gls.ppldv.configuration.userException.LoginFailedException;
import com.gls.ppldv.configuration.userException.RegisterFailedException;
import com.gls.ppldv.user.dto.LoginDTO;
import com.gls.ppldv.user.entity.Member;
import com.gls.ppldv.user.entity.PassCode;
import com.gls.ppldv.user.service.MemberService;

import lombok.RequiredArgsConstructor;

@RequestMapping("/user")
@RestController
@RequiredArgsConstructor
public class MemberController {

	private final MemberService ms;

	// 회원가입 처리
	@PostMapping("/register")
	public ResponseEntity<String> register(
			// @RequestParam(required=false, name="file") MultipartHttpServletRequest
			// request,
			MultipartFile file, Member member) {
		String message = null;
		ResponseEntity<String> response = null;
		HttpHeaders headers = new HttpHeaders();
		// MultipartFile로 변환
		headers.add("Content-Type", "text/plain;charset=utf-8");
		try {
			message = ms.register(member, file);
			response = new ResponseEntity<>(message, headers, HttpStatus.OK);
		} catch (RegisterFailedException e) {
			response = new ResponseEntity<>(e.getMessage(), headers, HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return response;
	}

	// 로그인 처리
	@PostMapping("/login")
	public ResponseEntity<Object> login(LoginDTO member,
			HttpServletRequest request,
			HttpServletResponse response) {
		HttpSession session = request.getSession(); // 세션을 받아와서
		ResponseEntity<Object> result = null; // 로그인성공,실패 보낼 전송 데이터
		Member m = null;
		HttpHeaders headers = new HttpHeaders();
		try {
			// 로그인 성공 시에는 Member 객체를 JSON 형식으로 전송
			m = ms.login(member);
			session.setAttribute("loginMember", m); // 로그인 된 회언 정보를 세션에 담아 보내주고

			if (member.isChecked()) {
				String encryptedEmail = CookieUtils.encrypt(m.getEmail());
				Cookie cookie = new Cookie("id", encryptedEmail);
				cookie.setMaxAge(60 * 60 * 24); // 1일
				cookie.setPath("/"); // 이 홈페이지의 모든 곳
				response.addCookie(cookie);
			}

			result = new ResponseEntity<>(m, headers, HttpStatus.OK);
			return result;
		} catch (LoginFailedException e) {
			// 아이디,비번 틀릴 시에는 로그인 실패 메시지를 Text 형식으로 전송
			headers.add("Content-Type", "text/plain;charset=utf-8");
			result = new ResponseEntity<>(e.getMessage(), headers, HttpStatus.OK);
			return result;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	// 비밀번호 찾기 처리
	@PostMapping("/find")
	public ResponseEntity<String> findPass(Member member, HttpServletRequest request) {
		String message = null;
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "text/plain;charset=utf-8");
		try {
			message = ms.findPassSubmit(member, request);
		} catch (NullPointerException e) {
			return new ResponseEntity<>(e.getMessage(), headers, HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ResponseEntity<>(message, headers, HttpStatus.OK);
	}

	// 코드 일치 확인
	@PostMapping("/passAccept")
	public ResponseEntity<String> passAccept(PassCode passCode) {
		String message = ms.changePassCode(passCode);
		
		// 일단 한번 확인했으면, 기존 코드 지워주는 작업
		ms.removeCode(passCode.getEmail());
		
		HttpHeaders headers = new HttpHeaders();
		
		headers.add("Content-Type", "text/plain;charset=utf-8");
		if (message.equals("Code Equal")) {
			return new ResponseEntity<>(message, headers, HttpStatus.OK);
		} else {
			// 코드 인증 실패
			return new ResponseEntity<>(message, headers, HttpStatus.OK);
		}
	}

	// 비밀번호 변경
	@PostMapping("/change")
	public ResponseEntity<String> changePass(Member member) {
		String message = null;
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "text/plain;charset=utf-8");
		try {
			message = ms.changePass(member);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(e.getMessage(), headers, HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity<>(message, headers, HttpStatus.OK);
	}
}
