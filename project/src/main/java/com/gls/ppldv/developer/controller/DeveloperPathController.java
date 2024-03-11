package com.gls.ppldv.developer.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.gls.ppldv.common.util.Criteria;
import com.gls.ppldv.common.util.PageMaker;
import com.gls.ppldv.common.util.Paging.Cri;
import com.gls.ppldv.common.util.Paging.PMaker;
import com.gls.ppldv.developer.dto.DeveloperDTO;
import com.gls.ppldv.developer.entity.DCareer;
import com.gls.ppldv.developer.entity.Developer;
import com.gls.ppldv.developer.service.DeveloperService;
import com.gls.ppldv.user.entity.Member;

import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("developer")
@RequiredArgsConstructor
public class DeveloperPathController {

	private final DeveloperService ds;

	@GetMapping("/register")
	public String register() {

		return "/developer/register";
	}

	@GetMapping("/profile")
	public String profile(Long id, Cri cri1, Criteria cri2 , Model model) {

		// 현재 회원 정보만
		Page<Developer> dlist = null;
		
		// 전체 회원 정보들
		Page<Developer> dlist2 = null;
		
		PMaker pm1 = null;

		PageMaker pm2 = null;
		
		try {
			pm1 = ds.getPageMaker(id, cri1);
			pm2 = ds.getPageMaker(cri2);
		} catch (Exception e) {
			e.printStackTrace();
		}

		dlist = ds.searchDev(id, cri1);
		
		dlist2 = ds.searchDev2(cri2);
		// Page 객체의 getContent는 List 형태로 바꿔 전송

		model.addAttribute("developerList", dlist.getContent());
		model.addAttribute("developerAllList", dlist2.getContent());
		model.addAttribute("pm1", pm1);
		model.addAttribute("pm2", pm2);
		
		return "/developer/profile";
	}

	@GetMapping("/readPage")
	public String readPage(
			Long dno, // 프로필 아이디
			Model model
		) {
		// 여기서 프로필 아이디만 받아서 정보 저장해두면 되겠네
		
		DeveloperDTO developerDTO = ds.searchDev(dno);
		
		model.addAttribute("developerInfo", developerDTO);
		return "/developer/readPage";
	}
	
	@GetMapping("/readOtherPage")
	public String readAllPage(
			Long dno, // dno
			Model model
		) {
		// 여기서 프로필 아이디만 받아서 정보 저장해두면 되겠네
		
		DeveloperDTO developerDTO = ds.searchDev(dno); 
		
		// 유저 네임 정보도 전달
		Member m = ds.findName(developerDTO.getUno());
		
		model.addAttribute("developerInfo", developerDTO);
		model.addAttribute("memberName", m.getName());
		return "/developer/readOtherPage";
	}
	
	@GetMapping("/search")
	public String search(
		Criteria cri,
		Model model
	) {
		// 전체 회원 정보들
		Page<Developer> dlist = null;
		PageMaker pm = null;
		
		try {
			pm = ds.getPageMaker(cri);
		} catch (Exception e) {
			e.printStackTrace();
		}

		dlist = ds.searchDev2(cri);
		// Page 객체의 getContent는 List 형태로 바꿔 전송

		model.addAttribute("developerAllList", dlist.getContent());
		model.addAttribute("pm", pm);
		
		return "/developer/search";
	}
	
	/**
	 * 개발자 찾기 페이지에서 첫번째 필드 검색
	 */
	@GetMapping("/searchFirst")
	public String searchFirst(
		Criteria cri,
		String name,
		Model model
	) {
		
		Page<Developer> dlist = null;
		PageMaker pm = null;
		
		try {
			pm = ds.getPageMaker(name, cri);
		} catch (Exception e) {
			e.printStackTrace();
		}

		dlist = ds.searchDev3(name, cri);
		
		model.addAttribute("developerAllList", dlist.getContent());
		model.addAttribute("pm", pm);
		
		return "/developer/search";
	}

	
	/**
	 * 회원 정보 조회
	 */
	@GetMapping("/Info")
	public String info(
		Long id,
		Model model
	) {
		System.out.println("------------------------"+ id);
		
		Member m = ds.findName(id);
		
		model.addAttribute("developer", m);
		return "/developer/info";
	}
	
}
