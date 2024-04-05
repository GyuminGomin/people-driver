package com.gls.ppldv.configuration.security;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.gls.ppldv.user.entity.Member;
import com.gls.ppldv.user.repository.MemberRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

	private final MemberRepository mr;
	
	/**
	 * orElseThrow는 Optional 클래스의 메서드 중 하나이다.
	 * 그리고 isPresent를 사용해 존재하는지 없는지 비교한다. null로 비교 하면 x
	 */
	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		Member member = mr.findByEmail(email)
				.orElseThrow(() -> new UsernameNotFoundException("Not Exist"));
		return new CustomUserDetails(member);
		
	}
}
