package com.gls.ppldv.user.service;

import org.springframework.web.multipart.MultipartFile;

import com.gls.ppldv.user.dto.EditDTO;
import com.gls.ppldv.user.entity.Member;

public interface MemberService extends FindPassService {

	/**
	 * 회원 등록(이미지 포함)
	 *
	 * @param member - 회원 등록을 위한 객체
	 * @param file   - 클라이언트로부터 받은 파일
	 * @return - 메세지
	 */
	public String register(Member member, MultipartFile file) throws Exception;

	/**
	 * 회원정보 수정
	 *
	 * @param member - 회원 정보 받아올 것
	 * @param file   - 회원이 수정한 이미지
	 * @return - 수정 완료 메시지 반환
	 */
	public String editProfile(EditDTO member, MultipartFile file) throws Exception;

	/**
	 * 회원 삭제
	 */
	public void removeUser(String email) throws Exception;

	/**
	 * email을 통해 멤버 찾기
	 */
	public Member findMember(String email) throws Exception;

	/**
	 * 비밀번호 찾기 passAuth(인증이 완료되거나 실패되거나 일단 체크 한번 하고 난 뒤 삭제) 
	 * @param email 
	 */
	public void removeCode(String email);
}
