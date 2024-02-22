package com.gls.ppldv.user.service;

import org.springframework.web.multipart.MultipartFile;

import com.gls.ppldv.user.entity.Member;

public interface MemberService {
	
	/**
	 * ȸ�� ���
	 * @param member - ȸ�� ����� ���� ��ü
	 * @param file - Ŭ���̾�Ʈ�κ��� ���� ����
	 * @return - �޼���
	 */
	public String register(Member member, MultipartFile file) throws Exception;
}
