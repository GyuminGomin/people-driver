package com.gls.ppldv.user.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.gls.ppldv.user.entity.Member;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {

	/**
	 * 자동 로그인
	 *
	 * @param email - 복호화된 이메일
	 * @return - 존재하는 회원 반환
	 */
	Optional<Member> findByEmail(String email);

	/**
	 * 아이디 이름 일치하는 회원 찾기
	 *
	 * @param email - 이메일
	 * @param name  - 이름
	 * @return - 존재하는 회원 반환
	 */
	Member findByEmailAndName(String email, String name);

	/**
	 * @param email - 삭제할 아이디
	 */
	void deleteByEmail(String email);

}
