package com.gls.ppldv.user.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.gls.ppldv.user.dto.EditDTO;
import com.gls.ppldv.user.entity.Member;
import com.gls.ppldv.user.service.MemberService;

import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/user")
@RequiredArgsConstructor
public class MemberPathController {

	private final MemberService ms;

	// 회원정보 수정
	@PostMapping("/edit")
	public String editProfile(EditDTO member, MultipartFile file, RedirectAttributes rttrs, HttpSession session) throws Exception {

		// 만약 회원 이미지가 변경되었다면, 삭제 후 다시 업로드
		String message = ms.editProfile(member, file);

		Member m = ms.findMember(member.getEmail());
		session.setAttribute("loginMember", m);

		rttrs.addFlashAttribute("message", message);
		return "redirect:/user/editProfile";
	}

	// 회원정보 삭제
	@PostMapping("/removeUser")
	public String removeUser(String email, RedirectAttributes rttrs) throws Exception{
		ms.removeUser(email);
		
		return "redirect:/user/logout";
	}
	
	// 비밀번호 인증 코드
	@PostMapping("/passAuth")
	public String passAuth(String email, HttpServletRequest request) {
		request.setAttribute("email", email);
		return "/member/passAuth";
	}

	// 비밀번호 변경
	@PostMapping("/changePass")
	public String changePass(String email, HttpServletRequest request) {
		request.setAttribute("email", email);
		return "/member/changePass";
	}
}
