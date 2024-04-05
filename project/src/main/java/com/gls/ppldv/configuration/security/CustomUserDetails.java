package com.gls.ppldv.configuration.security;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.gls.ppldv.user.entity.Member;

import lombok.Data;

@Data
public class CustomUserDetails implements UserDetails {

	private Member member; // composition
	
	public CustomUserDetails(Member member) {
		this.member = member;
	}
	
	/**
	 * 권한을 member 클래스의 Role 속성에 해당하는 값으로 설정
	 * GrantedAuthority는 Spring Security에서 사용자의 권한을 관리하고 처리하기 위해 제공하는 인터페이스
	 * SimpleGrantedAuthority는 GrantedAuthority 인터페이스의 간단한 구현체
	 * 
	 * Collection인 이유 : 다양한 권한을 담기위해
	 * 
	 */
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		Collection<GrantedAuthority> authorities = new ArrayList<>();
		authorities.add(new GrantedAuthority() {
			@Override
			public String getAuthority() {
				return member.getRole().toString();
			}
		});
		return authorities;
	}

	@Override
	public String getPassword() {
		return member.getPassword();
	}

	@Override
	public String getUsername() {
		return member.getEmail();
	}

	/**
	 * 사용자 계정의 만료 여부를 반환 (일반적으로 계정이 만료되지 않았으면 true를 반환
	 */
	@Override
	public boolean isAccountNonExpired() {
		return true;
	}
	
	/**
	 * 사용자 계정의 잠금 여부를 반환 (계정이 잠겨 있지 않으면 true를 반환)
	 */
	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	/**
	 * 사용자 자격 증명(암호)의 만료 여부를 반환 (일반적으로 자격 증명이 만료되지 않았으면 true를 반환)
	 */
	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	/**
	 * 사용자 계정의 활성화 여부를 반환, 활성화된 계정이면 true를 반환
	 */
	@Override
	public boolean isEnabled() {
		return true;
	}

}
