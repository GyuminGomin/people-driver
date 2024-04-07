package com.gls.ppldv.user.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

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
			MultipartFile file, Member member) throws Exception {
		HttpHeaders headers = new HttpHeaders();
		// MultipartFile로 변환
		headers.add("Content-Type", "text/plain;charset=utf-8");
		
		String message = ms.register(member, file);
		if (message.equals("ID Duplicate Retry!")) {
			return new ResponseEntity<>(message, headers, HttpStatus.OK);
		}
		return new ResponseEntity<>(message, headers, HttpStatus.OK);
	}

	// 비밀번호 찾기 처리
	@PostMapping("/find")
	public ResponseEntity<String> findPass(Member member, HttpServletRequest request) throws Exception {
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "text/plain;charset=utf-8");
		
		String message = ms.findPassSubmit(member, request);
		if (message.equals("NOT EXISTS")) {
			return new ResponseEntity<>(message, headers, HttpStatus.OK);
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
	public ResponseEntity<String> changePass(Member member) throws Exception {
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "text/plain;charset=utf-8");
		
		String message = ms.changePass(member);
		
		return new ResponseEntity<>(message, headers, HttpStatus.OK);
	}
}