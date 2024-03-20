package com.gls.ppldv.developer.controller;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.gls.ppldv.developer.dto.DeveloperDTO;
import com.gls.ppldv.developer.service.DeveloperService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("developer")
@RequiredArgsConstructor
public class DeveloperController {

	private final DeveloperService ds;

	@PostMapping("/register")
	public ResponseEntity<String> registerDeveloper(DeveloperDTO developerDTO,
			@RequestPart(name = "file", required = false) MultipartFile file) throws Exception {
		// developerDTO와 file 객체를 사용하여 데이터 처리

		HttpHeaders headers = new HttpHeaders();
		// MultipartFile로 변환
		headers.add("Content-Type", "text/plain;charset=utf-8");

		String message = ds.register(developerDTO, file);

		ResponseEntity<String> response = null;

		if (message.equals("Reigster Success")) {
			response = new ResponseEntity<>(message, headers, HttpStatus.OK);
		} else {
			response = new ResponseEntity<>(message, headers, HttpStatus.OK);
		}

		return response;
	}
	
	@PostMapping("/edit")
	public ResponseEntity<String> editDeveloper(
			DeveloperDTO developerDTO, // 수정받을 대상
			Long dno, // 수정해야 할 대상
			@RequestPart(name = "file", required = false) MultipartFile file) throws Exception {
		// developerDTO와 file 객체를 사용하여 데이터 처리

		HttpHeaders headers = new HttpHeaders();
		// MultipartFile로 변환
		headers.add("Content-Type", "text/plain;charset=utf-8");

		String message = ds.edit(dno,developerDTO, file);

		ResponseEntity<String> response = null;

		if (message.equals("Edit Success")) {
			response = new ResponseEntity<>(message, headers, HttpStatus.OK);
		} else {
			response = new ResponseEntity<>(message, headers, HttpStatus.OK);
		}

		return response;
	}
	
	@PostMapping("/remove")
	public ResponseEntity<String> removeDeveloper (
				Long dno
				) {
		HttpHeaders headers = new HttpHeaders();
		// MultipartFile로 변환
		headers.add("Content-Type", "text/plain;charset=utf-8");
		
		String message = ds.remove(dno);
		
		ResponseEntity<String> response = null;
		
		response = new ResponseEntity<>(message, headers, HttpStatus.OK);
		
		return response;
	}
}
