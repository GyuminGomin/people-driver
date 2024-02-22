package com.gls.ppldv.user.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gls.ppldv.user.service.MemberService;

import lombok.RequiredArgsConstructor;

@RequestMapping("/user")
@RestController
@RequiredArgsConstructor
public class MemberController {

	private final MemberService ms;
	
	// ȸ������ ó��
	@PostMapping("/register")
	public ResponseEntity<String> register(){
		// TODO ���� ����Ʈ���� ajax�� �۾��ϰ� ������ ������ �����غ���
		String message = null;
		
		try {
			message = ms.register(null, null);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return new ResponseEntity<>(message ,HttpStatus.OK);
	}
	
}
