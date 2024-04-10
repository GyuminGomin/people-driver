package com.gls.ppldv.configuration.security.jwt;

import java.util.Date;
import java.util.stream.Collectors;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import com.gls.ppldv.configuration.security.CustomUserDetails;
import com.gls.ppldv.user.entity.Member;

import io.jsonwebtoken.Jwts;
// import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

@PropertySource("classpath:prop/jwt.properties")
@Component
public class JwtProvider {

	@Value("${secret-key}")
	private String secretKey;
	@Value("${expiration}")
	private Long expiration;
	@Value("${issuer}")
	private String issuer;
	
	/**
	 * 인증 객체를 받아 사용자의 권한 정보를 추출하고 JWT 토큰 생성
	 */
	public String generateToken(Authentication authentication) {
		
		SecretKey key = Keys.hmacShaKeyFor(secretKey.getBytes());
		
		// 각 권한 목록을 getAuthority 메서드를 호출하여 권한 이름으로 변환 (스트림으로 매핑)
		String authorities = authentication.getAuthorities().stream()
				.map(GrantedAuthority::getAuthority)
				.collect(Collectors.joining(","));
		
		CustomUserDetails cuds = (CustomUserDetails) authentication.getPrincipal();
		
		// 이게 작동 안할 거라고 생각하고 있음
		Member member = cuds.getMember();
		Long Id = member.getId();
		String email = member.getEmail();
		
		// AccessToken 생성
		String jwtToken = Jwts.builder()
				// header
				.setSubject(authentication.getName())
				// payload
				.claim("userId", Id)
				.claim("email", email)
				.claim("authority", authorities)
				.setExpiration(new Date(expiration))
				// header
				.signWith(key)
				.compact();
		
		return "Bearer " + jwtToken;
	}
	
	
	/**
	 * 
	 */
}
